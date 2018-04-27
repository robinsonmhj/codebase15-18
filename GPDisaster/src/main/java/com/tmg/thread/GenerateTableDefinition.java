/**
 * 
 */
package com.tmg.thread;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import com.tmg.Bean.Column;
import com.tmg.Bean.Table;
import com.tmg.Bean.TableContainer;
import com.tmg.Util.TableUtil;
import com.tmg.core.Mapper;
import com.tmg.greenplum.DAOImp.GreenplumDAOImp;

/**
 * @author Haojie Ma
 * @date Nov 18, 2015
 */
public class GenerateTableDefinition implements Runnable{
	
	private static Logger log = Logger.getLogger(GenerateTableDefinition.class);
	
	@Autowired
	@Qualifier("GreenplumDAO")
	private GreenplumDAOImp gpDAOImp;
	
	private TableContainer container;
	private Map<String,List<String>> map;//key:tableName,value:partition list
	
	
	public void setgpDAOImp(GreenplumDAOImp gpDAOImp){
		this.gpDAOImp=gpDAOImp;
	}
	
	
	
	public TableContainer getContainer() {
		return container;
	}





	public void setContainer(TableContainer container) {
		this.container = container;
	}





	public Map<String, List<String>> getMap() {
		return map;
	}





	public void setMap(Map<String, List<String>> map) {
		this.map = map;
	}





	public void run(){
		
		String tableName,schema,basicTableName,columnName,columnType,flag;
		int index,len;
		List<String> partitionList;
		Table table;
		StringBuilder sql;
		Column column;
		while(true){
			
			tableName=container.getTableFromQueue();
			if(tableName==null)
				break;
			
			partitionList=map.get(tableName);
			
			index=tableName.indexOf(".");
			len=tableName.length();
			schema=tableName.substring(0, index);
			basicTableName=tableName.substring(index+1,len);
			table=gpDAOImp.getTableMetaData(schema, basicTableName);
			
			sql= new StringBuilder("create table ").append(tableName).append("(\n");
			List<Column> columnList=table.getCloumns();
			Iterator<Column> iterator=columnList.iterator();
			while(iterator.hasNext()){
				
				column=iterator.next();
				columnName=column.getName();
				columnType=column.getType();
				flag=column.getFlag();
				int columnLen=column.getLen();
				sql.append("\t").append(columnName).append(" ");
				//datatype bpchar wasn't regonized in the create table definition
				if(columnType.equals("bpchar"))
					sql.append("character");
				else
					sql.append(columnType);
				
				//numeric default length is 131089. we don't need to specify that
				if(!columnType.equals("numeric")&&columnLen!=131089){
					int type=Mapper.getParameter(columnType);
					if(type==1){
						sql.append("(").append(columnLen).append(") ");	
					}else if(type==2){
						int decimal=column.getDecimalLen();
						sql.append("(").append(columnLen).append(",").append(decimal);
						sql.append(") ");
					}
				}
				
				
				if(flag.equals("NO"))
					sql.append(" NOT NULL");
				
				sql.append(",\n");
			}
			
			List<String> pkList=TableUtil.getPrimary(table);
			List<String> dsList=gpDAOImp.getDistributedKey(schema, basicTableName);
			if(!StringUtils.isEmpty(pkList)){
				Iterator<String> pkItera=pkList.iterator();
				String pk;
				sql.append("\tCONSTRAINT ").append(basicTableName).append("_pkey PRIMARY KEY (");
				
				//distributed key should be exactly the same with primary key or left sub set of primary key
				for(int i=0;i<dsList.size();i++){
					pk=dsList.get(i);
					sql.append(pk).append(",");
				}
				while(pkItera.hasNext()){
					pk=pkItera.next();
					if(!dsList.contains(pk))
						sql.append(pk).append(",");
				}
				//remove the last comma
				len=sql.length();
				sql.delete(len-1, len);
				
				sql.append(")\n");
			}
			
			sql.append(")\n");//bucket to close the column definition
			
			
			if(!StringUtils.isEmpty(dsList)){
				Iterator<String> dsKeyItera=dsList.iterator();
				sql.append("DISTRIBUTED BY (");
				while(dsKeyItera.hasNext()){
					String dsKey=dsKeyItera.next();
					sql.append(dsKey).append(",");
					
				}
				
				
				//remove the last comma
				len=sql.length();
				sql.delete(len-1, len);
			
				sql.append(")\n");
			}
			
			//partitionedKey
			
			if(!StringUtils.isEmpty(partitionList)){
				String partitionColumn=TableUtil.getPartitionColumn(table);
				sql.append("PARTITION BY LIST(").append(partitionColumn).append(")\n");
				sql.append("(\n");
				Iterator<String> partitionItera=partitionList.iterator();
				while(partitionItera.hasNext()){
					
					String partitionKey=partitionItera.next();
					sql.append("\tPARTITION ").append(partitionKey.toLowerCase());
					sql.append(" VALUES('").append(partitionKey.toUpperCase()).append("'),\n");
					
				}
				
				sql.append("\tDEFAULT PARTITION others \n");
				sql.append("); \n");
				
			}
			
			log.info(sql);
			
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	

}


