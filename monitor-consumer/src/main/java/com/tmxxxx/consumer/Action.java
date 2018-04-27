package com.tmxxxx.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.tmxxxx.Util.*;
import com.tmxxxx.core.Properties;


public class Action{
	
	
	public static void main(String[] args){
		
		
		GenericXmlApplicationContext ctx=new GenericXmlApplicationContext(); 
		ctx.load("applicationContext.xml");  
		ctx.refresh(); 
		
		final int threadNo=1;
		
		
		ExecutorService executor=Executors.newFixedThreadPool(threadNo);
		
		Map<String,Object> map=Properties.getMap();
		
		for(int i=0;i<threadNo;i++){
			KafkaConsumerRunner runner=(KafkaConsumerRunner)ctx.getBean("kafkaRunner");
			runner.setMap(map);
			executor.submit(runner);
			//LogConsumerRunner comsumer=(LogConsumerRunner)ctx.getBean("logConsumer");
			//comsumer.setMap(map);
			//executor.submit(comsumer);
		}
		
		
		
		
		/*
		String regx1="\\[info (\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,4}).*(TraceExecution|TraceAuthentication) <DRDAConnThread_([0-9]{1,})> tid=0x([0-9a-f]+).*(executing|User DN =)\\s(.*)";
		String regx2="\\[error (\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,4}) CST (.*)";
		
		
		Pattern pInfo = Pattern.compile(regx1, Pattern.CASE_INSENSITIVE);
		Pattern pError = Pattern.compile(regx2, Pattern.CASE_INSENSITIVE);
	
		
		List<String> testList= new ArrayList<String>();
		testList.add("[info 2017/02/20 14:44:23.869 CST GFXD:TraceExecution <DRDAConnThread_329> tid=0x50b] EmbedStatement#executeStatement: executing UPDATE ODS.CLAIM_ADDITIONAL_DIAGNOSIS SET VER=?,DIAG_CD_ID=?,TRNSLT_DIAG_CD_ID=?,SBMT_DIAG_CD_ID=?,PRSNT_ON_ADM_IND=?,VLD_FRM_DT=?,VLD_TO_DT=?,SRC_SYS_REF_ID=?,SRC_SYS_REC_ID=? WHERE PRSN_ID=? and CLM_ID=? and CLM_ADD_DIAG_ID=? and CLIENT_ID=?;, args: ;value=166342455,type=0;value=261143,type=0;value=369300,type=0;value=14798994,type=0;value=,type=0;value=2017-02-20 14:41:04.603,type=0;value=9999-12-31 12:00:00.0,type=0;value=6618292,type=0;value=CMC_CLMD_DIAG_CT|162440044900|01|0x00000000000000000001,type=0;value=583676,type=0;value=6308645,type=0;value=475696,type=0;value=9030,type=0");
		testList.add("[error 2017/02/25 07:42:02.443 CST  <Notification Handler> tid=0x4e] Member: 10.66.13.165(28457)<v272>:44001 above heap critical threshold");
		testList.add("[error 2017/02/20 14:42:22.827 CST  <Pooled Waiting Message Processor 27> tid=0x1fc3] GfxdIndexManager::deleteFromIndexes: Encountered primary exception while doing index maintenance. Storing the exception & continuing index maintenance");
		testList.add("[info 2017/03/28 09:09:08.345 CDT GFXD:TraceAuthentication <DRDAConnThread_746529> tid=0xbcbe8] User DN = [CN=Haojie Ma (hma),OU=TMG Users,DC=tmghealth,DC=com]");
		
		
		for(String str:testList){
			
			Matcher m=pInfo.matcher(str);
			
			if(m.find()){
				System.out.println("date="+m.group(1));
				long mili=DateUtil.Date2Mil(m.group(1));
				System.out.println("miliseconds="+mili);
				System.out.println("action="+m.group(2));
				System.out.println("threadId="+m.group(3));
				System.out.println("tid="+m.group(4));
				System.out.println("statement="+m.group(6));
			}else{
				m=pError.matcher(str);
				if(m.find()){
					System.err.println("date="+m.group(1));
					System.err.println("action="+m.group(2));
				}
			}
		}
	*/	
		
	}
	
	
	
}
