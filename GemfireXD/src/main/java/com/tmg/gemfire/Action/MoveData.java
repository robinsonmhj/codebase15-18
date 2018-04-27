/**
 * 
 */
package com.tmg.gemfire.Action;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tmg.core.Container;
import com.tmg.gemfire.Bean.Column;
import com.tmg.gemfire.Bean.Table;
import com.tmg.gemfire.DAOImp.GreenplumDAOImp;
import com.tmg.gemfire.Util.TableUtil;

/**
 * @author Haojie Ma
 * @date Apr 22, 2016
 */

@Component
public class MoveData implements Runnable{
	
	
private static Logger log=Logger.getLogger(TableUtil.class);
	
	
	@Autowired
	private  GreenplumDAOImp gpDaoImp;
	
	/*
	public void setgpDaoImp(GreenplumDAOImp gpDaoImp){
		this.gpDaoImp=gpDaoImp;
	}
	*/
	Container<String> container;
	
	public MoveData(){
		
	}
	
	public void setContainer(Container<String> container){
		this.container=container;
	}
	
	public void run(){
		String sourceSchema="odm";
		String targetSchema="odm_eval";
		String validate_from="valid_from_ts";
		String validate_to="valid_to_ts";
		while(true){
			
			String tableName=container.get();
			if(tableName==null)
				return;
			//==========================================================================change it back===============================================================
			//Table table=gpDaoImp.getTableMetaData(sourceSchema, "fact_claim_detail");
			
			Table table=gpDaoImp.getTableMetaData(sourceSchema, tableName);
			List<Column> columnList=table.getCloumns();
			Map<String,String> pkMap= new HashMap<String,String>();
			for(Column c:columnList){
				
				if(c.isPrimary()){
					pkMap.put(c.getName(),c.getType());
				}
				
			}
			if(pkMap.containsKey(validate_from)){
				String tmpTable=sourceSchema+"."+tableName+"3259";
				//StringBuilder tmpTableSql= new StringBuilder("create external table ").append(tmpTable).append("\n");
				StringBuilder tmpTableSql= new StringBuilder().append(tmpTable).append("\n");//in order to reuse the code, you have to fill create external table or writeable table 
				tmpTableSql.append("(\n");
				for(String key:pkMap.keySet()){
					if(!key.equals(validate_from))
						tmpTableSql.append(key).append(" ").append(pkMap.get(key)).append(",");
				}
				
				tmpTableSql.append(validate_from).append(" ").append(pkMap.get(validate_from)).append(",");
				tmpTableSql.append(validate_to).append(" timestamp without time zone").append(")\n");
				tmpTableSql.append("LOCATION ( 'gpfdist://10.67.35.12:8081/");
				tmpTableSql.append(tableName).append(".txt')");
				tmpTableSql.append("FORMAT 'TEXT' (DELIMITER '|' NULL as '');");
				
				StringBuilder sql= new StringBuilder("drop external table if exists ").append(tmpTable).append(";\n");
				sql.append("create WRITABLE EXTERNAL TABLE ").append(tmpTableSql);
				sql.append("insert into  ");
				sql.append(tmpTable);
				sql.append(" select ");
				StringBuilder selectList= new StringBuilder();
				for(String key:pkMap.keySet()){
					if(!key.equals(validate_from)){
						selectList.append(key).append(",");
						sql.append("o.").append(key).append(",");
					}
						
				}
				
				selectList.deleteCharAt(selectList.length()-1);//remove the last comma
				sql.append("o.").append(validate_from);
				sql.append(",null from ").append(sourceSchema).append(".").append(tableName).append(" o join (select ");
				
				sql.append(selectList);
				sql.append(",count(1) ");
				sql.append(" from ").append(sourceSchema).append(".").append(tableName);
				sql.append(" group by ");
				sql.append(selectList);
				sql.append(" having count(1)>1) t\n");
				sql.append(" on ");
				
				for(String key:pkMap.keySet()){
					if(!key.equals(validate_from)){
						//selectList.append(key).append(",");
						sql.append("o.").append(key).append("=").append("t.").append(key).append(" and ");
					}
						
				}
				
				//remove the last and
				sql.delete(sql.length()-5, sql.length()-1);
				//sql.append(" order by ");
				//sql.append(selectList);
				//sql.append(",").append(validate_from);
		
				int[] result=gpDaoImp.executeMultipleQuery(sql.toString());
				
				if(result!=null){
					for(int i=0;i<result.length;i++)
						log.info("result["+i+"]="+result[i]);
					//if there is no more than 1 rows based on the primary except validate_from, then
					//copy all the data from the origianl to the target
					if(result[2]==0){
						//==========================================================================change it back===============================================================
						//String targetTableName="fact_claim_detail";
						
						StringBuilder sql1= new StringBuilder("insert into ");
						sql1.append(targetSchema).append(".").append(tableName);
						sql1.append(" select * from ");
						sql1.append(sourceSchema).append(".").append(tableName).append(";\n");
						sql1.append("drop external table if exists ").append(tmpTable);
						
						int[] result1=gpDaoImp.executeMultipleQuery(sql1.toString());
						if(result1!=null){
							
							log.info("successfully insert "+result1[0]+" in table "+targetSchema+"."+tableName);
						}
						continue;
					}
						
				}
				
				
				BufferedWriter writer=null;
				BufferedReader reader=null;
				String source="C:\\work\\gpfdist\\"+tableName+".txt";
				String target="C:\\work\\gpfdist\\"+tableName+"1.txt";
				File fSource=new File(source);
				File fTarget=new File(target);
				try{
					writer= new BufferedWriter(new FileWriter(fTarget));
					reader= new BufferedReader(new FileReader(fSource));
					List<String> list= new ArrayList<String>();
					String line=reader.readLine();
					while(line!=null){
						list.add(line.substring(0, line.length()-1));
						line=reader.readLine();
					}
					
					Collections.sort(list);
					
					
					String previous=list.get(0);
					for(int i=1;i<list.size();i++){
						String current=list.get(i);
						writer.write(previous);
						writer.write("|");
						if(current.substring(0, current.lastIndexOf("|")).equals(previous.substring(0, previous.lastIndexOf("|")))){
							writer.write(current.substring(current.lastIndexOf("|")+1, current.length()));
						}
						writer.write("\n");
						previous=current;
					}
					
					//write the last line
					writer.write(previous);
					writer.write("|\n");

					
				}catch(Exception e){
					
					
					log.info("",e);
					
					
					
				}finally{
					
					try{
						
						writer.close();
						reader.close();
					}catch(Exception ex){
						
						log.info("close file error",ex);
						
						
					}
					
					
				}
				
				//rename
				fSource.delete();
				while(!fTarget.renameTo(fSource)){
					log.info("rename failed");
					//Thread.sleep(1000);
					
				}
				
				log.info("rename succeed");
				
				
				//drop external table
				sql= new StringBuilder("drop external table if exists ").append(tmpTable).append(";\n");
				
				//create external table
				sql.append("create external table ").append(tmpTableSql);
				
				
				//==========================================================================change it back===============================================================
				//String targetTableName="fact_claim_detail";
				sql.append("insert into ").append(targetSchema).append(".").append(tableName);
				//sql.append("insert into ").append(targetSchema).append(".").append("fact_claim_detail");
				sql.append(" select ");
				
				for(Column column:columnList){
					String columnName=column.getName();
					if(!columnName.equals(validate_to))
						sql.append("s.");
					else
						sql.append("t.");
					sql.append(columnName).append(",");
					
				}
				
				//remove the last comma
				sql.deleteCharAt(sql.length()-1);
				
				sql.append(" from ").append(sourceSchema).append(".").append(tableName).append(" s left join ");
				sql.append(tmpTable).append(" t on ");
				for(String key:pkMap.keySet()){
						sql.append("s.").append(key).append("=").append("t.").append(key).append(" and ");
						
				}
				
				//remove the last and
				sql.delete(sql.length()-5, sql.length()-1);
				
				sql.append(";\n");
				
				
				sql.append("drop external table ").append(tmpTable).append(";\n");
				
				
				gpDaoImp.executeMultipleQuery(sql.toString());
				
			}else{
				//==========================================================================change it back===============================================================
				//String targetTableName="fact_claim_detail";
				StringBuilder sql= new StringBuilder("insert into ");
				sql.append(targetSchema).append(".").append(tableName);
				sql.append(" select * from ");
				sql.append(sourceSchema).append(".").append(tableName);
				
				int[] result=gpDaoImp.executeMultipleQuery(sql.toString());
				if(result!=null){
					
					log.info("successfully insert "+result[0]+" in table "+targetSchema+"."+tableName);
				}
				
				
				
			}
			
			
			
			
			
		}
		
		
		
	}
	
	
	

}


