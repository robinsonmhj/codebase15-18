/**
 * 
 */
package com.tmg.quartz.Jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.tmg.Log.Constant;
import com.tmg.Log.ProcessFile;
import com.tmg.Util.FileUtil;
import com.tmg.gf.DAOImp.GreenplumDAOImp;

/**
 * @author Haojie Ma
 * @date Sep 11, 2015
 */
public class TstABGPLogMonitorJob extends QuartzJobBean {
	
	private static Logger log=Logger.getLogger(ProdABGPLogMonitorJob.class);
	@Resource(name = "GreenplumDAO")
	private GreenplumDAOImp gpDAOImp;
	
	
protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		
	log.info("Prod ActiveBatch Log Monitor Job Starts");
	
	String dir=Constant.tst_path;
	String ext=".log";
	String env="TST";
	SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
	Date today= new Date();
	String date=df.format(today);
	List<String> fList=FileUtil.getFileList(dir,ext,date);
	
	Iterator<String> ite=fList.iterator();
	ProcessFile processFile=null;
	int count=0;
	while(ite.hasNext()){
		String fileName=ite.next();
		processFile= new ProcessFile(fileName,env);
		long jobId=processFile.getFileBean().getJobId();
		if(gpDAOImp.getReportByJobId(jobId))
			continue;
		gpDAOImp.insert(processFile);
		count++;
	}
	
	log.info(count+" Records added!");
		
	}
	

}


