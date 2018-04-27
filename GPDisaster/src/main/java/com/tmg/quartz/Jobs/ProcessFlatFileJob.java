/**
 * 
 */
package com.tmg.quartz.Jobs;




import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.tmg.greenplum.DAOImp.GreenplumDAOImp;


/**
 * @author Haojie Ma
 * @date Sep 11, 2015
 */
public class ProcessFlatFileJob extends QuartzJobBean {
	
	
	private static Logger log=Logger.getLogger(ProcessFlatFileJob.class);
	
	@Autowired
	@Qualifier("GreenplumDAO")
	private GreenplumDAOImp gpDAOImp;
	
	
	
	public void setgpDAOImp(GreenplumDAOImp gpDAOImp){
		this.gpDAOImp=gpDAOImp;
	}
	
	
	
	protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		
		log.info("Process Flat File Process Starts....");
		final int count=3;
		final int producerCount=count;
		final int consumerCount=count;
		
	}
	

}


