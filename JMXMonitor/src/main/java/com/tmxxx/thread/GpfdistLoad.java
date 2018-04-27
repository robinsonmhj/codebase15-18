/**
 * 
 */
package com.tmxxx.thread;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tmxxx.Bean.Container;
import com.tmxxx.DAO.GreenplumDAOImp;
import com.tmxxx.core.Properties;

/**
 * @author Haojie Ma
 * @date Dec 18, 2015
 */

@Component("gpLoader")
@Scope("prototype")
public class GpfdistLoad implements Runnable{
	
	
	private Logger log=Logger.getLogger(GpfdistLoad.class);
	
	private Container<String> container;
	@Autowired
	@Qualifier("gp")
	private  GreenplumDAOImp gpDaoImp;
	public void setgpDaoImp(GreenplumDAOImp gpDaoImp){
		this.gpDaoImp=gpDaoImp;
	}
	
	public GpfdistLoad(){
	}
	
	
	public GpfdistLoad(Container<String> container){
		this.container=container;
	}
	
	
	public void run(){
		String server=Properties.getProperty("tmg.gpfdist.server");
		String port=Properties.getProperty("tmg.gpfdist.port");
		String dir=Properties.getProperty("tmg.gpfdist.dir");
		String schemaName="dl";
		Map<String,String> map= new HashMap<String,String>();
		map.put("vm", "resourceStatistics");
		map.put("table", "tableStatistics");
		long waitTime=120*60*1000L;
		while(true){
			String fileName=container.get();
			if(fileName==null){
				try{
					log.info("I am going to sleep");
					Thread.sleep(waitTime);
					continue;
				}catch(Exception e){
					log.info("I cannot sleep",e);
				}
			}
			String tableName=null;
			if(fileName.startsWith("vm"))
				tableName=map.get("vm");
			else
				tableName=map.get("table");
			String extTable=schemaName+"."+tableName+"_ext"+System.currentTimeMillis();
			String table=schemaName+"."+tableName;
			StringBuilder sb= new StringBuilder("CREATE EXTERNAL TABLE ").append(extTable);
			sb.append(" (like ").append(table).append(")\n");
			sb.append("LOCATION ('gpfdist://").append(server).append(":").append(port).append("/").append(fileName);
			sb.append("')\n FORMAT 'TEXT' ( DELIMITER ',' NULL as 'null');");
			
			//load data from flat file into gp table
			sb.append("insert into ").append(table).append(" select * from ").append(extTable).append(";");
			
			//drop the externerl table
			sb.append("drop external table ").append(extTable).append(";");
			
			long start=System.currentTimeMillis();
			int[] results=gpDaoImp.executeMultipleQuery(sb.toString());
			long end=System.currentTimeMillis();
			long used=end-start;
			StringBuilder resultSb= new StringBuilder();
			if(results!=null){
				for(int i=0;i<results.length;i++){
					resultSb.append(results[i]).append(",");
				}
			}
			log.info("Load Table "+tableName+" finished,using time "+used+",results:"+resultSb);
			
			/*
			if(results[1]!=0){
				log.info("file "+fileName+ "loaded succeed");
			}else{
				log.info("file "+fileName+ "loaded failed");
			}
			*/
			
			File deleteFile= new File(dir+fileName);
			try{
				//only delete the file if it is loaded into successfully
				if(results[1]!=0){
					if(!deleteFile.delete())
						log.error(fileName+" can not be removed");
					else
						log.info(fileName+" removed");
				}else{
					log.info("please check "+fileName+", as it is not loaded into db");
				}
			}catch(Exception e){
				log.info("delete file "+fileName+" faild",e);
			}
			
		}
		
	}

	public void setContainer(Container<String> container) {
		this.container = container;
	}
	
	
	

}


