/**
 * 
 */
package com.tmg.quartz.Jobs;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;



import org.springframework.scheduling.quartz.QuartzJobBean;

import com.tmg.gf.DAOImp.GemfireDAOImp;
import com.tmg.gf.DAOImp.GfSessionDAOImp;
import com.tmg.gf.DAOImp.QueryKilledLogDAOImp;
import com.tmg.gf.DAOImp.QueryStatusDAOImp;
import com.tmg.gf.Model.QueryKilledLog;
import com.tmg.gf.Model.QueryStatus;

/**
 * @author Haojie Ma
 * @date Jun 5, 2015
 */
@DisallowConcurrentExecution
public class KillSlowQueryJob extends QuartzJobBean {


	private Logger log = Logger.getLogger(KillSlowQueryJob.class);
	@Autowired
	@Qualifier("GemfireDAO")
	private  GemfireDAOImp gfDaoImp;
	@Autowired
	@Qualifier("GfSessionDAO")
	private  GfSessionDAOImp sessionDaoImp;
	
	@Resource(name="QueryKilledLogDAO")
	private  QueryKilledLogDAOImp killLogDaoImp;
	
	@Resource(name = "QueryStatusDAO")
	private QueryStatusDAOImp queryStatusImp;
	
	
	public void setgfDaoImp(GemfireDAOImp gfDaoImp){
		this.gfDaoImp=gfDaoImp;
	}
	
	public void setsessionDaoImp(GfSessionDAOImp sessionDaoImp){
		this.sessionDaoImp=sessionDaoImp;
	}
	
	
	public void setkillLogDaoImp(QueryKilledLogDAOImp killLogDaoImp){
		this.killLogDaoImp=killLogDaoImp;
	}
	
	public void setqueryStatusImp(QueryStatusDAOImp queryStatusImp) {
		this.queryStatusImp = queryStatusImp;
	}
	

    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
    		log.info("Kill Slow Query Job Start....");
		 
		   List<String>  list = sessionDaoImp.getSlowQuery();
		   if(list==null){
			   log.info("No slow query found!");
			   log.info("Kill Slow Query Job Ends");
			   return;
		   }
			   
		   Iterator<String> iter=list.iterator();
	       
	       while(iter.hasNext()){
	    	   String uuid=iter.next();
	    	   log.info("Slow Query:"+uuid);
	    	   QueryKilledLog killLog=new QueryKilledLog();
	    	   Date date = new java.util.Date();
	    	   Timestamp insertTime = new Timestamp(date.getTime());
	    	   killLog.setStatement_uuid(uuid);
	    	   killLog.setInsert_time(insertTime);
	    	   killLogDaoImp.insert(killLog);
	    	   StringBuilder sql=new StringBuilder("call sys.cancel_statement('");
	    	   sql.append(uuid);
	    	   sql.append("')");
	    	   gfDaoImp.callProcedure(sql.toString());
	    	   QueryStatus querystatus = new QueryStatus();
	    	   querystatus.setSTATEMENT_UUID(uuid);
	    	   querystatus.setSTATEMENT_STATUS("KILLED");
	    	   querystatus.setInsert_time(insertTime);
	    	   queryStatusImp.insert(querystatus);
	    	   log.debug("Killed "+uuid);
	       }
	       log.info("Kill Slow Query Job Ends");
	     
	   
    }


}


