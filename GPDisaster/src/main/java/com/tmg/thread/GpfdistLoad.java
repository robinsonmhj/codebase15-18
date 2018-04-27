/**
 * 
 */
package com.tmg.thread;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tmg.Bean.TableContainer;
import com.tmg.core.Properties;
import com.tmg.greenplum.DAOImp.GreenplumDAOImp;

/**
 * @author Haojie Ma
 * @date Dec 18, 2015
 */
public class GpfdistLoad implements Runnable{
	
	
	private Logger log=Logger.getLogger(GpfdistLoad.class);
	
	private String schemaName;
	private TableContainer container;
	@Autowired
	@Qualifier("GreenplumDAO")
	private  GreenplumDAOImp gpDaoImp;
	public void setgpDaoImp(GreenplumDAOImp gpDaoImp){
		this.gpDaoImp=gpDaoImp;
	}
	
	public GpfdistLoad(){
		
		
	}
	
	
	public GpfdistLoad(String schemaName,TableContainer container){
		this.schemaName=schemaName;
		this.container=container;
		
	}
	
	
	public void run(){
		String server=Properties.getProperty("tmg.gpfdist.server");
		String port=Properties.getProperty("tmg.gpfdist.port");
		String ext=Properties.getProperty("tmg.gpfdist.file.extension");
		
		while(true){
			
			String tableName=container.getTableFromQueue();
			if(tableName==null)
				break;
			
			//implement the codes to load data to gp
			String extTable=schemaName+"."+tableName+"ext";
			String table=schemaName+"."+tableName;
			StringBuilder sb= new StringBuilder("CREATE EXTERNAL TABLE").append(extTable);;
			sb.append(" (like").append(table).append(")\n");
			sb.append("LOCATION ('gpfdist://").append(server).append(":").append(port).append("/").append(tableName).append(ext);
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
			
		}
		
	}
	
	
	

}


