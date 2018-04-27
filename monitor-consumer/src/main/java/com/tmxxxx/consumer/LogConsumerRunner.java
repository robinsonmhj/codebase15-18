package com.tmxxxx.consumer;




import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tmxxxx.Util.DateUtil;
import com.tmxxxx.dao.MySqlDAO;

@Component("logConsumer")
public class LogConsumerRunner implements Runnable{
	private static Logger log=Logger.getLogger(KafkaConsumerRunner.class);
	private final AtomicBoolean closed = new AtomicBoolean(false);
    private KafkaConsumer<String,String> consumer=null;
    private Map<String,Object> map;
    @Autowired
    @Qualifier("MySqlDAO")
    private MySqlDAO daoImp;
	public void setdaoImp(MySqlDAO daoImp){
		this.daoImp=daoImp;
	}
    
	
	public LogConsumerRunner(){
		
	}
	
	
    public LogConsumerRunner(Map<String,Object> map){
    	this.map=map;
    }
    

    public void run() {
        try {
        	consumer=new KafkaConsumer<String,String>(map);
            consumer.subscribe(Arrays.asList("monitor.log"));
            
            String regx1="\\[info (\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,4}).*(TraceExecution|TraceAuthentication) <DRDAConnThread_([0-9]{1,})> tid=0x([0-9a-f]+).*(executing|User DN =)\\s(.*)";
    		String regx2="\\[error (\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,4}) CST (.*)";
    		Pattern pInfo = Pattern.compile(regx1, Pattern.CASE_INSENSITIVE);
    		Pattern pError = Pattern.compile(regx2, Pattern.CASE_INSENSITIVE);
    		
    		int errorCount=0,logCount=0;
            while (true) {
            	//log.info(" I will live for ever");
                ConsumerRecords<String,String> records = consumer.poll(20);
                if(records.count()>0){
                	//log.info("I get some records");
                	errorCount=0;
                	logCount=0;
                	StringBuilder errorSql=new StringBuilder("insert into log(servername,occurtime,metrixname,value,comment) values ");
                	StringBuilder logSql=new StringBuilder("insert into audit(hostname,date,action,threadId,tid,statement) values ");
                    for (ConsumerRecord<String, String> record : records){
                    	log.info(record.offset()+","+record.key()+","+record.value());
                    	String hostname=record.key();
                    	String content=record.value();
                    	Matcher m=pInfo.matcher(content);
            			if(m.find()){
            				String date=m.group(1);
            				String action=m.group(2);
            				String threadId=m.group(3);
            				int tid= Integer.parseInt(m.group(4),16);
            				String statement=m.group(6);
            				logSql.append("('").append(hostname).append("','").append(date).append("','").append(action).append("',");
            				logSql.append(threadId).append(",").append(tid).append(",'").append(statement).append("'),");
            				logCount++;
            				log.info("catchOne:"+hostname+","+content);
            			}else{
            				m=pError.matcher(content);
            				if(m.find()){
            					long mili=DateUtil.Date2Mil(m.group(1));
            					String comment=m.group(2);
            					String matrixName="error_count";
            					int value=1;
            					errorSql.append("('").append(hostname).append("',").append(mili).append(",'").append(matrixName).append("',").append(value).append(",'").append(comment).append("'),");
            					errorCount++;
            					log.info("catch error:"+hostname+","+content);
            				}
            			}
                    	
                    	
                    }
                    if(errorCount>0){
                    	errorSql.deleteCharAt(errorSql.length()-1);
           	            log.info("sql="+errorSql);
           	            try{
           	            	daoImp.executeSingleQuery(errorSql.toString());
           	            }catch(Exception e){
           	            	log.info("sql error",e);
           	            }
                    }
                    if(logCount>0){
                    	logSql.deleteCharAt(logSql.length()-1);
           	            log.info("sql="+logSql);
           	            try{
           	            	daoImp.executeSingleQuery(logSql.toString());
           	            }catch(Exception e){
           	            	log.info("sql error",e);
           	            }
                    }
                } 
            }
       } catch (Exception  e) {
           log.info("",e);
       } finally {
            consumer.close();
       }
    }

    
    
    
  
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}


	// Shutdown hook which can be called from a separate thread
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
    
    
    
}