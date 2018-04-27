package com.tmg.gemfire.Util;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tmg.core.Mapper;
import com.tmg.core.Properties;
import com.tmg.gemfire.Bean.Column;
import com.tmg.gemfire.Bean.Table;
import com.tmg.gemfire.DAOImp.GemfireDAOImp;
import com.tmg.gemfire.DAOImp.GreenplumDAOImp;



@Component
public class TableUtil {
	
	
	private static Logger log=Logger.getLogger(TableUtil.class);
	
	@Autowired
	private  GemfireDAOImp gfDaoImp;
	@Autowired
	private  GreenplumDAOImp gpDaoImp;
	
	/*
	public void setgfDaoImp(GemfireDAOImp gfDaoImp){
		this.gfDaoImp=gfDaoImp;
	}

	public void setgpDaoImp(GreenplumDAOImp gpDaoImp){
		this.gpDaoImp=gpDaoImp;
	}
	*/
	final String delimiter="$$";
	
	public  void copySchema2GF(String schemaName,List<String> tableList,List<String> dsKeyList,boolean _1){
	
				
		
		String capital=Properties.getProperty("tmg.gf.table.default");
		log.info("tmg.gf.table.default:"+capital);
		if(capital==null||capital.trim().equals(""))
			capital="capital";
		
			ArrayList<String> tables = gpDaoImp.getTables(schemaName);

				Iterator<String> it = tables.iterator();
				String tableName,schema;
				
				
				// if tableList ==null, copy all the tables under the schema
				if(tableList==null){
					while (it.hasNext()) {
						StringBuilder sql=new StringBuilder("drop table if exists ");
						tableName = it.next();	
						if(capital.equalsIgnoreCase("capital")){
							tableName=tableName.toUpperCase();
						}
							
						//put double quoto around table name, in case that the table name is reserved word
						tableName="\""+tableName+"\"";
						Table table = gpDaoImp.getTableMetaData(schemaName,tableName);
						sql.append(schemaName).append(".").append(tableName).append(";");
						try{
							
							//gfDaoImp.executeUpdate(sql.toString());
							log.debug(sql);
							schema=gpDaoImp.generateSchema(schemaName,table,_1);
							//gfDaoImp.executeUpdate(schema);
							log.debug(schema);
							//log.info(tableName+" copied done!\n");
						}catch(Exception e){
							log.info("error",e);
						}
						//System.out.println(schema);
						
					}
				}else{
					int i=0;
					while (it.hasNext()) {
						tableName = it.next();
						//only copy the files which is in the list
						if (!tableList.contains(tableName))
								continue;
						//put double quoto around table name, in case that the table name is reserved word
						if(capital.equalsIgnoreCase("capital"))
							tableName=tableName.toUpperCase();
							
					
						tableName="\""+tableName+"_1\"";
						StringBuilder sql=new StringBuilder("drop table if exists ");
						//put double quoto around table name, in case that the table name is reserved word
						sql.append(schemaName).append(".").append(tableName).append(";");
						//sql.append(schemaName).append(".").append(tableName).append(";");
						try{
							
							//gfDaoImp.executeUpdate(sql.toString());
							log.debug(sql);
							System.out.println(sql);
							//Table table = gpDaoImp.getTableMetaData(schemaName,tableName);
							//schema=gpDaoImp.generateSchema(schemaName,table,dsKeyList);
							//log.debug(schema);
							
							//gfDaoImp.executeUpdate(schema);
							//log.info(tableName+" copied done!\n");
						}catch(Exception e){
							log.info("",e);
						}
						i++;
						//System.out.println(i);
					}
				}
		
	}
	
	public  void copySchema2GF(String schemaName,List<String> dsKeyList){
		
		List<String> tableList=null;
		copySchema2GF(schemaName,tableList,dsKeyList,false);
		
	}
	
	public void compareTable(Table t1,Table t2){
		
		

		if(t1==null||t2==null)
			return;
		
		Map<String,Column> map1= new HashMap<String,Column>();
		Map<String,Column> map2= new HashMap<String,Column>();
		List<Column> l1 = t1.getCloumns();
		List<Column> l2 = t2.getCloumns();
		String columnName;
		Column column,c1,c2;
		int size1=l1.size();
		for(int i=0;i<size1;i++){
			column=l1.get(i);
			columnName=column.getName();
			map1.put(columnName, column);
		}
		int size2=l2.size();
		for(int i=0;i<size2;i++){
			column=l2.get(i);
			columnName=column.getName();
			map2.put(columnName, column);
		}
			
			for(String key:map1.keySet()){
				c1=map1.get(key);
				c2=map2.get(key);
				//System.out.println(c1);
				//System.out.println(c2);
				if(!c1.equals(c2)){
					System.out.println("t1"+c1);
					System.out.println("t2"+c2);
				}
			}
			
			for(String key:map2.keySet()){
				c2=map2.get(key);
				c1=map1.get(key);
				if(c1==null){
					
					System.out.println("Table1 doesn't have column "+c2);
				}
			}
			
			
	
		
				
		}


	
	
	//suffix format: schemaName.tableName where balalalala;
	private String getSmallestKey(List<String> keyList,String suffixSql){
		
		log.debug("function getSmallestKey");
		String key=null,tmpKey;
		if(keyList==null){
			log.error("keyList cannot be null");
			return null;
		}
			
		//String prefixSql= "";
		Iterator<String> iter=keyList.iterator();
		int count=0;
		StringBuilder sql=null;
		boolean bool=true;//fist round
		while(iter.hasNext()){
			tmpKey=iter.next();
			log.debug(tmpKey);
			//2016-05-12 21:11
			//ver cannot be key
			//select ...group by ver is ignored
			if(suffixSql.contains(tmpKey.toLowerCase())||suffixSql.contains(tmpKey.toUpperCase())||"ver".equalsIgnoreCase(tmpKey)){
				log.debug(tmpKey+" ignored");
				continue;
			}
				
			sql= new StringBuilder("select count(1) from (select distinct ");
			sql.append(tmpKey).append(" from ").append(suffixSql).append(") a;");
			try{
				int tmpCount=gpDaoImp.execute(sql.toString().toLowerCase());
				if(bool){
					count=tmpCount;
					key=tmpKey;
					bool=false;
				}else{
					if(count>tmpCount){
						count=tmpCount;
						key=tmpKey;
					}
				}
				
				log.debug("tmpKey:"+tmpKey+",tmpCount:"+tmpCount);
				log.debug("key:"+key+",count:"+count);
			}catch(Exception e){
				log.info("",e);
			}
			
			
			
		}
		
		
		return key+":"+count;
		
	}
	
	
	public Table convertGP2GF(Table gp){
		
		List<Column> columnList=gp.getCloumns();
		Iterator<Column> iterator=columnList.iterator();
		String oldType,newType,columnName="";
		int columnLen=0,columnDecimal;
		List<String> specialDT= new ArrayList<String>();
		specialDT.add("NUMERIC");
		specialDT.add("numeric");
		int precision=40;
		int scale=10;
		gp.setName(gp.getName().toUpperCase());
		while(iterator.hasNext()){
			Column column=iterator.next();
			oldType=column.getType();
			newType = Mapper.GP2Gemfire(oldType);
			if (newType == null) {
				log.error(columnName+":No such type:" + oldType+ " is definied the mapper file\n");
				System.err.println(columnName+":No such type:" + oldType+ " is definied the mapper file");
				continue;
			}else if (newType.equals("incompatible")) {
				log.error(oldType+ " is a incompatible datatype\n");
				System.err.println(oldType+ " is a incompatible datatype");
				continue;
			}
			
			if (Mapper.getParameter(oldType) == 2) {
				
				 columnDecimal = column.getDecimalLen();
				//the default len of numeric is 131089
				
				//make a concession in gemfire, as the default precision and scale are different from GP.
				//By default, the two value of GP is infinite, precison in GF is 5 and scale is 0;
				//Now make the default as below
				//precision=40, scale=10
				
				if(specialDT.contains(oldType)){
					if(columnLen==131089){//the default len of numberic is 131089
						columnLen=precision;
						column.setLen(columnLen);
					}
						
					if(columnDecimal==0){
						columnDecimal=scale;
						column.setDecimalLen(columnDecimal);
					}
				}
				
		}
		
			column.setType(newType.toUpperCase());
			column.setName(column.getName().toUpperCase());
			
		
		}
		
		
		
		
		
		return gp;
		
	}
	
	
	private List<String> getPrimary(Table table){
		
		if(table==null){
			log.error("Table is null");
			return null;
		}
		
		
		List<String> keyList= new ArrayList<String>();
		
		List<Column> columnList=table.getCloumns();
		
		
		Iterator<Column> iter= columnList.iterator();
		
		while(iter.hasNext()){
			Column column=iter.next();
			if(column.isPrimary()){
				String columnName=column.getName();
				keyList.add(columnName);
				log.debug("PrimaryKey:"+columnName+"added");
			}
		}
		
		return keyList;
		
		
	}
	
	
	//suffixSql format: columnName=value1 or columnName1=value1 and columnName2=value2
	public void copyData2GF(String schemaName,String tableName,String suffixSql){
		
		long start=System.currentTimeMillis();
		int recordCount;
		String client_id=null;
		if(suffixSql!=null)
			client_id=suffixSql.split("=")[1];
		String dir=Properties.getProperty("tmg.gf.local.dir");
		String sFile=dir+"/"+tableName+".sql";
		if(client_id!=null&&!client_id.trim().equals(""))
			sFile=dir+"/"+tableName+"_"+client_id+".sql";
		File file=new File(sFile);
		long total=0;
		log.info(tableName+" begin:");
		String capital=Properties.getProperty("tmg.gf.table.default");
		if(capital==null||capital.trim().equals(""))
			capital="capital";
		
		if(capital.equalsIgnoreCase("capital"))
			tableName=tableName.toUpperCase();
		
		//all the table name should be double quoted
		tableName="\""+tableName+"\"";
		//get the truncate mode
		String truncateMode=Properties.getProperty("tmg.insert.truncate.mode");
		if(truncateMode==null||truncateMode.trim().equals(""))
			truncateMode="false";
		log.debug("tmg.insert.truncate.mode:"+truncateMode);
		String targetSchema=Properties.getProperty("tmg.gf.target.schema");
		if(targetSchema==null||targetSchema.trim()=="")
			targetSchema="ODS";
		log.debug("tmg.gf.target.schema:"+targetSchema);
		
		
		StringBuilder sql= new StringBuilder("select count(1) from ");
		sql.append(schemaName).append(".").append(tableName);
		//if the tables doesn't contains client_id, and the table
		//has data,do nothing
		if(suffixSql==null||suffixSql.trim().equals("")){
			if(truncateMode.equalsIgnoreCase("false")){
				sql.append(";");
				int c=1;
				try{
					//only for temporary
					c=gfDaoImp.execute(sql.toString());
				}catch(Exception e){
					log.info("exception",e);
				}
				if(c!=0)
					return;
			}
			
			
		}else if(suffixSql!=null&&!suffixSql.trim().equals(""))
			sql.append(" where ").append(suffixSql).append(";");
		
			int count=1;
			try{
				count=gpDaoImp.execute(sql.toString().toLowerCase());
			}catch(Exception e){
					log.error("error",e);
			}
			if(count==0){
				log.info("No data for table"+tableName);
				return;
				
			}
		
		
		Table tableGf=gfDaoImp.getTableMetaData(targetSchema,tableName);
		Table tableGp=gpDaoImp.getTableMetaData(schemaName, tableName);
		
		/*
		boolean equal= compareTable(tableGf,tableGp);
		if(!equal){
			log.error("Cannot copy data due to different table definition in the databases");
			log.error("Table Name is "+tableName);
			return;
		}
		*/
		
		
		try{
			
			int maxCount=50000;
			try{
				maxCount=Integer.valueOf(Properties.getProperty("tmg.select.max"));
				//if table is related with claim, make the maxCount=10000;
				//if(tableName.contains("claim")||tableName.contains("CLAIM")){
					//maxCount=maxCount/3;
					//set the right 4 digit as 0
					//maxCount=maxCount&0xfff0;
				//}
					
			}catch(Exception e){
				log.info("parse tmg.select.max error",e);
				
			}
			//truncate the target table
			if(truncateMode.equalsIgnoreCase("true")){
				
				//comment it out when inserting data in db directly 
				/*
				if(suffixSql==null||suffixSql.trim().equals(""))
					//cannot truncate the table because 
					gfDaoImp.executeUpdate("delete from "+targetSchema+"."+tableName+";");
				else
					gfDaoImp.executeUpdate("delete from "+targetSchema+"."+tableName+" where "+suffixSql+";");
				*/
				//used to export data into local disk
				Files.deleteIfExists(file.toPath());
			}
				
			if(count>maxCount){
				List<String> dsList=gpDaoImp.getDistributedKey(schemaName, tableName);
				String dsKey="";
				for(String column:dsList)
					dsKey+=column+",";
				int columnCount;
				if(dsKey!=null&&!dsKey.trim().equals("")){
					dsKey=dsKey.substring(0, dsKey.length()-1);
					StringBuilder sqlColumnCount=new StringBuilder("select count(1) from (select distinct ").append(dsKey);
					sqlColumnCount.append(" from ").append(schemaName).append(".").append(tableName);
					
					if(suffixSql!=null&&!suffixSql.trim().equals(""))
						sqlColumnCount.append(" where ").append(suffixSql);
						
					sqlColumnCount.append(") a;");
					
					columnCount=gpDaoImp.execute(sqlColumnCount.toString());
				}else{
					String newsuffixSql;
					List<String> keyList=getPrimary(tableGp);
					if(suffixSql!=null&&!suffixSql.trim().equals(""))
						newsuffixSql=schemaName+"."+tableName+" where "+suffixSql;//format the sqlSuffix
					else
						newsuffixSql=schemaName+"."+tableName;			
					
					String[] columnCountArray=getSmallestKey(keyList,newsuffixSql).split(":");
					
					dsKey=columnCountArray[0];
					columnCount=Integer.valueOf(columnCountArray[1]);
					log.info("second key is "+dsKey+",count is "+columnCount);
					
				}
				
				
				log.info("dsKey="+dsKey);
				int maxLen=(int)(Math.log10(maxCount)+1);//the length of maxCount 
				int multiple=(int)Math.pow(10, maxLen);//
			
				int recordPerColumn=count/columnCount;
				
				log.info("recordPerColumn:"+recordPerColumn);
				
				List<String> valueList=gpDaoImp.getDistinctDataByColumn(schemaName, tableName, dsKey, suffixSql);
				
				Iterator<String> iter= valueList.iterator();
				
				String tmpTable="tmp_"+tableName.replace("\"", "");
				if(client_id!=null&&!client_id.trim().equals(""))
					tmpTable=tmpTable+"_"+client_id;
				
				String tmpSchema="gf_ods";
				try{
					tmpSchema=Properties.getProperty("tmg.gp.target.schema");
				}catch(Exception e){
					
					throw new Exception("tmg.gp.target.schema not found !");
					
				}
				
				
				
				//only the latest id
				gpDaoImp.executeUpdate("drop table if exists "+tmpSchema+"."+tmpTable);
				StringBuilder create= new StringBuilder("create  table ").append(tmpSchema).append(".").append(tmpTable);
				create.append(" ( ").append(dsKey).append(" bigint) distributed by (").append(dsKey).append(");");
				log.debug(create);
				StringBuilder changeOwner=new StringBuilder("ALTER TABLE ").append(tmpSchema).append(".").append(tmpTable).append(" OWNER TO dev_edw_etl;");
				StringBuilder grant=new StringBuilder("GRANT ALL ON TABLE ").append(tmpSchema).append(".").append(tmpTable).append(" TO dev_edw_etl;");
				gpDaoImp.executeUpdate(create.toString());
				gpDaoImp.executeUpdate(changeOwner.toString());
				gpDaoImp.executeUpdate(grant.toString());
			
				
				create= new StringBuilder("insert into  ").append(tmpSchema).append(".").append(tmpTable).append(" values ");
				count=1;
				while(iter.hasNext()){
					String value=iter.next();
					create.append("(").append(value).append("),");
		
					int totalCount=count*recordPerColumn;
					if((totalCount>maxCount)&&(totalCount/multiple*multiple)%maxCount==0){
						create.deleteCharAt(create.length()-1);
						create.append(";");
						gpDaoImp.executeUpdate(create.toString());
						recordCount=copyData2GF(schemaName,targetSchema,tableName,suffixSql,dsKey,tableGf);
						total+=recordCount;
						count=1;
						gpDaoImp.executeUpdate("truncate table "+tmpSchema+"."+tmpTable+";");
						create= new StringBuilder("insert into  ").append(tmpSchema).append(".").append(tmpTable).append(" values ");
					}
					count++;
				}
				
				if((count-1)*recordPerColumn%maxCount!=0){
					create.deleteCharAt(create.length()-1);
					create.append(";");
					gpDaoImp.executeUpdate(create.toString());
					recordCount=copyData2GF(schemaName,targetSchema,tableName,suffixSql,dsKey,tableGf);
					total+=recordCount;
				}
				
				gpDaoImp.executeUpdate("drop table if exists "+tmpSchema+"."+tmpTable);
			}else{
				
				//get directly directly	
				
				recordCount=copyData2GF(schemaName,targetSchema,tableName,suffixSql,null,tableGf);
				total+=recordCount;
			}
			
			String insertDB=Properties.getProperty("tmg.gf.insert.db");
			if(insertDB==null||insertDB.trim().equals(""))
				insertDB="false";
		
			
			
			if(insertDB.equalsIgnoreCase("true")){
				long s1=System.currentTimeMillis();
				String bulkInsert=generateBulkStatement(schemaName,tableName,sFile);
				gfDaoImp.callProcedure(bulkInsert);
				long e1=System.currentTimeMillis();
				log.info("Bulk Insert using time"+(e1-s1));
			}
			
			
			
			String keepData=Properties.getProperty("tmg.gf.keep.data");
			if(keepData==null||keepData.trim().equals(""))
				keepData="false";
			
			if(keepData.equalsIgnoreCase("false"))
				Files.deleteIfExists(file.toPath());
			
			
			
		}catch(Exception e){
			
			
			log.info("Exception",e);
			
		}
				
		long end=System.currentTimeMillis();
		
		log.info("tableName:"+tableName+" done, Total records:"+total+" using time:"+(end-start));
		
		
		
	}
	
	
	
	// can only called by void copyData2GF(String schemaName,String tableName,String suffixSql) 
	private  int copyData2GF(String source,String target,String tableName,String suffixSql,String cName,Table tableGf){
		
		
		//all the table name should be double quoted
		//tableName="\""+tableName+"\"";
		
		log.debug("copyData2GF begin source:"+source+",target:"+target+",tableName:"+tableName);
		List<String> typeList= new ArrayList<String>();//type list which needs to add single quote to contain the value
		typeList.add("char");
		typeList.add("varchar");
		typeList.add("long varchar");
		typeList.add("time");
		typeList.add("timestamp");
		typeList.add("date");
		BufferedWriter writer=null;
		List<Map<String,String>> recordsList;
		Map<String,String> recordMap;
		String type,value,columnName;
		Column column;
		//int batch=500,updateCount=0;
		int count=1;
		StringBuilder sbsql=new StringBuilder();
		String dir=Properties.getProperty("tmg.gf.local.dir");
		String client_id=null;
		if(suffixSql!=null)
			client_id=suffixSql.split("=")[1];
		log.debug("suffixSql="+suffixSql);
		log.debug("client_id="+client_id);
		String file=dir+"/"+tableName.replaceAll("\"", "")+".sql";
		if(client_id!=null&&!client_id.trim().equals(""))
			file=dir+"/"+tableName.replaceAll("\"", "")+"_"+client_id+".sql";
		
		log.debug("file="+file);
		
		//get the batchNo from configuration file
		/*
		try{
			batch=Integer.valueOf(Properties.getProperty("tmg.insert.batchNo"));
			//if the table is related to claim//make the insert number as 100
			if(tableName.contains("claim")||tableName.contains("CLAIM"))
				batch=100;
		}catch(Exception e){
			log.info("parse tmg.insert.batchNo execption",e);
		}
		*/
		try{
			//Table tableGf=gfDaoImp.getTableMetaData(target,tableName);
			
			writer= new BufferedWriter(new FileWriter(new File(file),true));
			
			//get all the records
			//get the lasted records of the data
			String insertMode=Properties.getProperty("tmg.insert.latest.mode");
			if(insertMode==null||insertMode.trim().equals(""))
				insertMode="false";
			
			if(insertMode.equalsIgnoreCase("true"))
				recordsList=gpDaoImp.getLatestRecord(source, tableName,cName,suffixSql);
			else
				recordsList=gpDaoImp.getData(source, tableName,cName,suffixSql);
			
			if(recordsList==null||recordsList.size()==0){
				log.info("No data in table "+source+"."+tableName);
				return 0;
			}
			List<Column> columnList = tableGf.getCloumns();
			Iterator<Map<String,String>> iterRecord=recordsList.iterator();
			//insert into db directly
			//sbsql.append("insert into ").append(target).append(".").append(tableName).append(" values");
			while(iterRecord.hasNext()){
				recordMap=iterRecord.next();
				//sbsql.append("(");
				int size=columnList.size();
				for(int i=0;i<size;i++){
					column=columnList.get(i);
					columnName=column.getName().toLowerCase();
					type=column.getType();
					//if(tableName.equalsIgnoreCase("reference_data")||columnName.equalsIgnoreCase("descr"))
						//log.debug("columnName "+columnName+"type "+type);
				
					value=recordMap.get(columnName);
					//add a single quote for the columns such as char,varchar,date 
					if(typeList.contains(type.toLowerCase())){
						//null cannnot be contains with quoto
						if(value!=null){
							
							writer.write("\""+value.replaceAll("\"", "'")+"\"");
							value=value.replaceAll("'", "''");//replace the column value which contains single quote
							//sbsql.append("'").append(value).append("'");
						}else{
							//sbsql.append(value);
							writer.write("");
						}
							
							
					}else{
						if(type.equalsIgnoreCase("smallint")&&value!=null){
								if(value.equalsIgnoreCase("f"))
									value="0";
								else
									value="1";
						}
						//sbsql.append(value);
						if(value==null)
							writer.write("");
						else
							writer.write(value);
					}
					
					//write the value into local disk
					//sbsql.append(",");
					if(i!=(size-1))
						writer.write(",");
					
				}
				//sbsql.deleteCharAt(sbsql.length()-1);//remove the last comma
				//sbsql.append("),");
				writer.write("\n");
				//insert batch records at one time
				/*
				if(count%batch==0){
					sbsql.deleteCharAt(sbsql.length()-1);//remove the last comma
					sbsql.append(";");
					gfDaoImp.executeUpdate(sbsql.toString());
					updateCount+=batch;
					sbsql=new StringBuilder("insert into ").append(target).append(".").append(tableName).append(" values");
				}
				*/	
				count++;
			}
			//make sure that the records number under batch are also inserted 
			/*
			if((count-1)%batch!=0){
				sbsql.deleteCharAt(sbsql.length()-1);//remove the last comma
				sbsql.append(";");
				gfDaoImp.executeUpdate(sbsql.toString());
			}
			*/
			count=count-1;//it starts from 1 so minus 1
			//log.debug("TableName:"+tableName+",Total Count:"+count+",updated by batch:"+updateCount);
		}catch(Exception e){
			log.error("",e);
			log.info("the sql is:");
			log.info(sbsql);
		}finally{
			try{
				writer.flush();
				writer.close();
			}catch(Exception e){
				log.error("close writer error",e);
			}
			
			
			
		}
		
		return count;
	}
	
	
	
	
 public void insertFromFile(String schemaName,String tableName,String file,int batch){
	 
	 BufferedReader reader=null;
	 String value="";
	 
	 try{
		 
		 File f=new File(file);
		 
		 if(!f.exists()){
			 
			 log.error(file+" doesn't exists");
			 return;
		 }
			 
		 if(!f.isFile()){
			 log.error(file+" is not file");
			 return;
		 }
			
		 List<String> typeList= new ArrayList<String>();//type list which needs to add single quote to contain the value
		 typeList.add("char");
		 typeList.add("varchar");
		 typeList.add("long varchar");
		 typeList.add("time");
		 typeList.add("timestamp");
		 //typeList.add("date");
		 
		 reader= new BufferedReader(new FileReader(f));
		 
		 Table t=gfDaoImp.getTableMetaData(schemaName, tableName);
		 
		 List<Column> l=t.getCloumns();
		 
		 //for(int i=0;i<l.size();i++)
			 //System.out.println(l.get(i).toString());
		 
		 
		 
		 
		 String line=reader.readLine();
		 
		 int count=1;
		 StringBuilder sb= new StringBuilder("insert into ").append(schemaName).append(".\"").append(tableName).append("\"");
		 /*
		 for(int i=0;i<l.size();i++){
			 Column column=l.get(i);
			 String columnName=column.getName();
			 sb.append(columnName).append(",");
		 }
		 
		 sb.deleteCharAt(sb.length()-1);
		 */
		 
		 sb.append(" values ");
		 while(line!=null){
			 
			 String[] lines=line.split(",");
			/*
			 int length=lines.length;
			 if(length!=70){
				 log.error("columns length not corrent,length="+length);
				 log.error(line);
				 continue;
			 }
			*/	 
			 
			 if(count%batch==0){
				 
				 sb.deleteCharAt(sb.length()-1);
				 
				 sb.append(";");
				 
				 try{
					 gfDaoImp.executeUpdate(sb.toString());
				 
				 }catch(Exception e){
					 log.info("Sql is"+sb);
					 log.info("error",e);
					 
				 }
				 sb= new StringBuilder("insert into ").append(schemaName).append(".\"").append(tableName).append("\"");
				/*
				 for(int i=0;i<l.size();i++){
					 Column column=l.get(i);
					 String columnName=column.getName();
					 sb.append(columnName).append(",");
				 }
				 
				 sb.deleteCharAt(sb.length()-1);
				 */
				 
				 sb.append(" values ");
				 count=1;
				 
			 }
			 
			 sb.append("(");
			 for(int i=0;i<lines.length;i++){
				 Column column=l.get(i);
				 value=lines[i];
				 if(value==null||value.equals("")){
					 if(typeList.contains(column.getType().toLowerCase()))
							 sb.append("'").append(value).append("',");
					 else
						 sb.append("null").append(",");
				 }else{
					 value=value.replaceAll("\"", "'");
					 sb.append(value).append(",");
				 }
					
			 }
			 
			 
			 sb.deleteCharAt(sb.length()-1);
			 
			 sb.append("),");
			 
			 line=reader.readLine(); 
			 count++;
		 }
		 
		 
		 if(count%batch!=0){
			 
			 sb.deleteCharAt(sb.length()-1);
			 
			 sb.append(";");
			 
			 try{
				 gfDaoImp.executeUpdate(sb.toString());
			 }catch(Exception e){
				 
				 log.info("sql is \n"+sb,e);
			 }
			 
			 
		 }
		 
		 
		 
		 
	 }catch(Exception e){
		 
		 log.info("",e);
		 
		 
	 }finally{
		 
		 try{
			 reader.close();
			 
		 }catch(Exception e){
			 
			 log.error("close file error",e);
		 }
		 
	 }
	 
	 
	 
	 
	 
	 
 }
	
 
 
	public String generateBulkStatement(String schemaName, String tableName,String file) {

		StringBuilder sb = null;
		
		try {
			Table table = gpDaoImp.getTableMetaData(schemaName, tableName);
			List<Column> list = table.getCloumns();
			Iterator<Column> it = list.iterator();
			sb = new StringBuilder("CALL SYSCS_UTIL.IMPORT_DATA_EX(\n");
			sb.append("'").append(schemaName).append("',\n");// schema name
			sb.append("'").append(tableName).append("',\n");// tableName name
			sb.append("'");
			while (it.hasNext()) {
				String column = it.next().getName();
				// columnName
				sb.append(column.toUpperCase()).append(",");
			}

			sb.deleteCharAt(sb.length() - 1);
			sb.append("',\n");//insertColumnList
			sb.append("null,\n");// columnIndex
			sb.append("'").append(file).append("',\n");// file localtion
			sb.append("',',\n");// column delimiter
			sb.append("null,\n");//characterDelimiter, default is double quotes
			sb.append("null,\n");//codeset
			sb.append("1,\n");//replace not 0; insert 0
			sb.append("0,\n");//lockTable not 0; not lock table with 0
			sb.append("10,\n");//numThreads
			sb.append("0,\n");//caseSensitiveNames 0 case insensitive
			sb.append("null,\n");//importClassName
			sb.append("null\n");//errorFile
			sb.append(");\n");
		} catch (Exception e) {
			log.info("", e);
		}

		if (sb == null)
			return null;
		else
			return sb.toString();

	}

 
	
	public static Map<String,List<String>> getTableIdentity(){
		
		Map<String,List<String>> identityMap= new HashMap<String,List<String>>();
		BufferedReader reader=null;
		final String file="/AutoIdentity.properties";
		List<String> columnList=null;
		try{
			
			//read for current folder
			//reader= new BufferedReader(new FileReader(new File(file)));
			InputStream stream =TableUtil.class.getResourceAsStream(file);
			reader= new BufferedReader(new InputStreamReader(stream));
			String line=reader.readLine();
			String[] lines;
			String table,column;
			int para=0;
			
			while(line!=null){
				lines=line.split(",");
				if(lines.length!=2){
					log.error("format error,"+line);
					continue;
				}
				table=lines[0].toUpperCase();
				column=lines[1].toUpperCase();
				
				if(identityMap.containsKey(table))
					columnList=identityMap.get(table);
				else
					columnList= new ArrayList<String>();
				
				columnList.add(column);
				
				identityMap.put(table, columnList);
			
				line=reader.readLine();
			}
		}catch(Exception e){
			log.info("excpetion",e);
		}finally{
			try{
				reader.close();
			}catch(Exception ex){
				log.info("close file "+file+" error ",ex);
			}
			
			
		}
				
		return identityMap;
		
		
		
	}
	
	
public static Map<String,String> getDistributedKeyTableMap(){
		
		Map<String,String> DistributedKeyTableMap= new HashMap<String,String>();
		BufferedReader reader=null;
		final String file="/DistributedKey.properties";
		try{
			
			//read for current folder
			//reader= new BufferedReader(new FileReader(new File(file)));
			InputStream stream =TableUtil.class.getResourceAsStream(file);
			reader= new BufferedReader(new InputStreamReader(stream));
			String line=reader.readLine();
			String[] lines;
			String dsKey,tableName;
			int para=0;
			
			while(line!=null){
				lines=line.split(",");
				if(lines.length!=2){
					log.error("format error,"+line);
					continue;
				}
				dsKey=lines[0].toUpperCase();
				tableName=lines[1].toUpperCase();
				
				
				DistributedKeyTableMap.put(dsKey, tableName);
			
				line=reader.readLine();
			}
		}catch(Exception e){
			log.info("excpetion",e);
		}finally{
			try{
				reader.close();
			}catch(Exception ex){
				log.info("close file "+file+" error ",ex);
			}
			
			
		}
				
		return DistributedKeyTableMap;
		
		
		
	}
	
	



}
