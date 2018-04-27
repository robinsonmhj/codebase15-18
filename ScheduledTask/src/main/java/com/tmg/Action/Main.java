/**
 * 
 */
package com.tmg.Action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tmg.Log.Constant;
import com.tmg.Log.ProcessFile;
import com.tmg.Util.FileUtil;
import com.tmg.gf.DAO.GfSessionDAO;
import com.tmg.gf.DAOImp.GfSessionDAOImp;
import com.tmg.gf.DAOImp.QueryStatusDAOImp;
import com.tmg.gf.Model.GfSession;



/**
 * @author Haojie Ma
 * @date Jun 5, 2015
 */
public class Main {
	
	
	private static Logger log=Logger.getLogger(Main.class);


	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		//AbstractApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
		//testHibernate();
		//testScheduler();
		//testCallShell();
		
		GfSessionDAOImp sessionDaoImp=(GfSessionDAOImp)context.getBean(GfSessionDAO.class);
		String username="%_gf_admin";
		
		List<GfSession> list=null;
		while(true){
			try{
				list=sessionDaoImp.getQueryByUserName(username);
			}catch(Exception e){
				log.error("",e);
			}
			
			if(list==null||list.isEmpty()){
				log.info("no sessions for "+username);
				
			}else{
				for(GfSession session:list){
					log.info("I catch him"+session);
				}
			}
			try{
				Thread.sleep(3000);
			}catch(Exception e){
				log.info("I cannot sleep",e);
			}
			
		}
		
		
		

	}

	
	public static void testCallShell(){
		
		BufferedReader reader=null;
		try{
			Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","netstat -a|grep 'tlisrv'"});
			System.out.println("Hello");
			reader= new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line=reader.readLine();
			String[] lines;
			while(line!=null){
				
				//System.out.println(line);
				line=reader.readLine();
				lines=line.split(" ");
				int len=lines.length;
				String tmp=lines[len-2];//host and port
				String host=tmp.split(":")[0];
				System.out.println(host);
			}
		}catch(Exception e){
			
			log.error("",e);
			
		}
		
		
		
		
	}
	
	
	
	
	public static void test(){
		
		
		SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-d");
		Date today= new Date();
		String date=df.format(today);
		System.out.println(date);
		
		
		//testAnnocation personDAO = context.getBean(testAnnocation.class);
		//personDAO.test();
		
		//KillBadQueryJob personDAO = context.getBean(KillBadQueryJob.class);
		//personDAO.execute();
	}
	
	/*
	
	public static void testScheduler() {

		try {

			JobDetail job = JobBuilder.newJob(KillBadQueryJob.class)
					.withIdentity("KillSlowQueryJob").build();

			// Trigger trigger =
			// TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever()).build();

			CronTrigger cronTrigger = TriggerBuilder
					.newTrigger()
					.withIdentity("KillSlowQueryTrigger", "GemfireXD")
					.withSchedule(
							CronScheduleBuilder.cronSchedule("0/30 * * * * ?"))
					.build();
			//crobtab job format minites,hour,day of month,month,day of week,command linux
			//java seconds,minutes,hours,day of month,month,day of week,year
			SchedulerFactory schFactory = new StdSchedulerFactory();
			Scheduler sch = schFactory.getScheduler();
			sch.start();
			sch.scheduleJob(job, cronTrigger);

		} catch (Exception e) {

			log.error("error",e);

		}

	}
*/
	public static void testHibernate() {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		QueryStatusDAOImp personDAO = context.getBean(QueryStatusDAOImp.class);

		List<String> list = personDAO.getStatusByUUID("98784247832-98784328383-1");

		for (String p : list) {
			System.out.println(p);
		}
		// close resources
		context.close();

	}
	
	
	public static void testLog(String date,String env){
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		com.tmg.gf.DAOImp.GreenplumDAOImp gpDAOImp = (com.tmg.gf.DAOImp.GreenplumDAOImp) context.getBean("GreenplumDAO");
		String dir=null;
		if(env.toUpperCase().equals("PRD"))
			dir=Constant.prd_path;
		else
			dir=Constant.tst_path;
		String ext=".log";
		List<String> fList=FileUtil.getFileList(dir,ext,date);
		
		Iterator<String> ite=fList.iterator();
		ProcessFile processFile=null;
		int failedCount=0;
		int total=fList.size();
		while(ite.hasNext()){
			String fileName=ite.next();
			processFile= new ProcessFile(fileName,env);
			long jobId=processFile.getFileBean().getJobId();
			if(gpDAOImp.getReportByJobId(jobId))
				continue;
			gpDAOImp.insert(processFile);
			boolean failed=processFile.isfailed();
			if(failed){
				System.out.println(fileName);
				failedCount++;
			}
			
		}
			
		System.out.println(failedCount+" out of "+total+" failed!");
		
	}
	

}
