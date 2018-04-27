/**
 * 
 */
package com.tmg.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import com.tmg.Bean.Cache;
import com.tmg.Bean.Table;
import com.tmg.Bean.TableContainer;
import com.tmg.Util.FileUtil;
import com.tmg.Util.TableUtil;
import com.tmg.core.Properties;
import com.tmg.greenplum.DAOImp.GreenplumDAOImp;

/**
 * @author Haojie Ma
 * @date Nov 2, 2015
 */



public class ExportThread implements Runnable{
	
	
	private static Logger log = Logger.getLogger(ExportThread.class);
	
	@Autowired
	@Qualifier("GreenplumDAO")
	private GreenplumDAOImp gpDAOImp;
	
	
	
	private TableContainer tableConsumer;
	private TableContainer tableProducer;//contains processed tableName, consumed by combined thread
	private String url;
	private Cache<String,Table> gpCache;
	private Cache<String,Table> drCache;
	private Map<String,List<String>> map;//map to store the tables have been processed. key: tableName value:partitionedTableName
	private String from;
	private String to;
	
	public ExportThread(){
		
		
	}
	
	
	public ExportThread(TableContainer tableConsumer,String url,Cache<String,Table> gpCache,Cache<String,Table> drCache){
		this.tableConsumer=tableConsumer;
		this.url=url;
		this.gpCache=gpCache;
		this.drCache=drCache;
	}
	
	public void setgpDAOImp(GreenplumDAOImp gpDAOImp){
		this.gpDAOImp=gpDAOImp;
	}
	
	public TableContainer getTableConsumer() {
		return tableConsumer;
	}


	public void setTableConsumer(TableContainer tableConsumer) {
		this.tableConsumer = tableConsumer;
	}

	

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public Cache<String, Table> getGpCache() {
		return gpCache;
	}


	public void setGpCache(Cache<String, Table> gpCache) {
		this.gpCache = gpCache;
	}


	public Cache<String, Table> getDrCache() {
		return drCache;
	}


	public void setDrCache(Cache<String, Table> drCache) {
		this.drCache = drCache;
	}


	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public String getTo() {
		return to;
	}


	public void setTo(String to) {
		this.to = to;
	}
	
	
	public Map<String, List<String>> getMap() {
		return map;
	}


	public void setMap(Map<String, List<String>> map) {
		this.map = map;
	}

	
	
	

	public TableContainer getTableProducer() {
		return tableProducer;
	}


	public void setTableProducer(TableContainer tableProducer) {
		this.tableProducer = tableProducer;
	}


	public void run(){
		
		String tableName,extTableName,tableWithoutSchema,schema,baseTableName;
		String ext=Properties.getProperty("tmg.gpfdist.file.extension");
		Table t1,t2;
		int index;
		boolean flag=false;//indicating if the table is the first time to compare
		while(true){
			List<Integer> resultList= new ArrayList<Integer>();
			tableName=tableConsumer.getTableFromQueue();
			if(tableName==null)
				break;
			index=tableName.indexOf(".");
			schema=tableName.substring(0,index);
			tableWithoutSchema=tableName.substring(index+1, tableName.length());
			String[] tmp=tableWithoutSchema.split("_[0-9]{0,}_prt_");
			baseTableName=tmp[0];

		
			/*
			// combined thread is very fast, we don't need to start them at the
			// same time with the expoart thread
			String key = schema + "." + baseTableName;
			synchronized (map) {
				List<String> list = map.get(key);
				if (list == null) {
					list = new ArrayList<String>();
					tableProducer.add2Queue(key);// only put the table name once
													// in the table container
				}

				list.add(tableName);
				map.put(key, list);
			}
			*/
			long start1=System.currentTimeMillis();
			synchronized(gpCache){
				t1=gpCache.get(baseTableName);
				if(t1==null){
					t1=gpDAOImp.getTableMetaData(schema, baseTableName);
					gpCache.put(baseTableName, t1);
					flag=true;
				}
			}
			
			synchronized(drCache){
				t2=drCache.get(baseTableName);
				if(t2==null){
					gpDAOImp.setEnv("DR");
					t2=gpDAOImp.getTableMetaData(schema, baseTableName);
					drCache.put(baseTableName, t2);
					gpDAOImp.setEnv(null);
				}
			}
			long end1=System.currentTimeMillis();
			long used1=end1-start1;
			log.debug(Thread.currentThread().getName()+",Get table meta data using time:"+used1+","+baseTableName);
			
			if(!t1.equals(t2)){
				if(flag){
					log.error("table definition are different,"+schema+"."+baseTableName);
					log.info("table in gp"+t1);
					log.info("table in dr"+t2);
					flag=false;
				}				
				continue;
			}
				

						
			String tsColumn=FileUtil.getTSColumn(t1);
			long count=gpDAOImp.getRowCount(tableName,tsColumn,from,to);
			if(count==0){
				log.info("0 records in "+tableName);
				continue;
			}
			
			

			StringBuilder sql=new StringBuilder();
			int random=(int)(Math.random()*1000);
			/*
			 String tmpTable=tableWithoutSchema+random;
			 //it is recommended to use create temp table tableName select ** from table
			 //insert into temp is historical 
			 //sql.append("create temp table ").append(tmpTable).append(" (like ").append(tableName).append(")\n;");
			 //sql.append("insert into ").append(tmpTable).append(" select * from ").append(tableName);
			
			
			sql.append("create temp table ").append(tmpTable).append(" as ");
			sql.append("select * from ").append(tableName);
			*/
			
			 //using real table instead of temp table, as temp table consumed too much memory
			 // better than temp tables when do inital load, as there are too many data 
			  String tmpTable=tableName+random;
			  StringBuilder create=TableUtil.createTable(t1,tmpTable);
			  sql.append("drop table if exists ").append(tmpTable).append(";\n");
			  sql.append(create);// create tmptable
			  sql.append("insert into ").append(tmpTable).append(" select * from ").append(tableName);
			 

			
			if(tsColumn!=null){
				if(!StringUtils.isEmpty(from)&&!StringUtils.isEmpty(to)){
					sql.append(" where ").append(tsColumn).append(">='").append(from);
					sql.append("' and ").append(tsColumn).append("<'").append(to).append("'");
				}
			}
			sql.append(";\n");
			
			 
				
			/*split the insert into tmp table and the insert into external table
			 insert into external table takes a long time, if they are not splited,
			 no update/insert/delete(write) on the table can be executed during the transaction 
			
			the sql should be splitted as the sharedlock will also block update
			export data to external table is too slow. It will lock the table for a long time
			 */

			int[] results=gpDAOImp.executeMultipleQuery(sql.toString());
			for(int i=0;i<results.length;i++)
				 resultList.add(results[i]);
			 
			
			//another transaction for inserting into the external table
			sql= new StringBuilder();
			
			
			extTableName=tableName+"_extSpecial";

			sql.append("drop external table if exists ").append(extTableName).append(";\n");
			sql.append("CREATE WRITABLE EXTERNAL TABLE ").append(extTableName);
			sql.append(" (like ").append(tableName).append(")\n");
			sql.append("LOCATION ( '").append(url).append("/").append(tableName).append(ext).append("' )\n");
			sql.append("FORMAT 'TEXT' ( DELIMITER ',' NULL '')").append(";\n");
			sql.append("insert into ").append(extTableName).append(" select * from ").append(tmpTable).append(";\n");
			
			sql.append("drop TABLE ").append(tmpTable).append(";\n");
			sql.append("drop EXTERNAL TABLE ").append(extTableName).append(";\n");
			
			//System.out.println(sql);
			
			long start=System.currentTimeMillis();
			results=gpDAOImp.executeMultipleQuery(sql.toString());
			for(int i=0;i<results.length;i++)
				 resultList.add(results[i]);
			long end=System.currentTimeMillis();
			
			long used=end-start;
			
			
			StringBuilder sb= new StringBuilder();
			int size=resultList.size();
			if(size!=0){
				for(int i=0;i<size;i++){
					sb.append(resultList.get(i)).append(",");
				}
				
				log.info(Thread.currentThread().getName()+":"+tableName+",using time:"+used+";results:"+sb);
				
				//only put the tableName in the list when it is actually processed(has data and have table definition in DR)
				String key = schema + "." + baseTableName;
				synchronized (map) {
					List<String> list = map.get(key);
					if (list == null) {
						list = new ArrayList<String>();
						tableProducer.add2Queue(key);// only put the table name once in the table container
					}

					list.add(tableName);
					map.put(key, list);
				}
				
			}
			
			
			
		
		}
		
		
		
	}
	
	
	
	
	

}


