package com.tmxxxx.consumer;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;




import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tmxxxx.core.Properties;
import com.tmxxxx.dao.MySqlDAO;


@Component("kafkaRunner")
public class KafkaConsumerRunner implements Runnable{
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
    
	
	public KafkaConsumerRunner(){
		
	}
	
	
    public KafkaConsumerRunner(Map<String,Object> map){
    	this.map=map;
    }
    

    public void run() {
        try {
        	consumer=new KafkaConsumer<String,String>(map);
        	//String topic=Properties.getProperty("topic.name");
            consumer.subscribe(Arrays.asList("monitor.default"));
            while (true) {
            	//log.info("closed value:"+closed.get());
                ConsumerRecords<String,String> records = consumer.poll(20);
                if(records.count()>0){
                	//log.info("I get "+records.count()+" message");
                    StringBuilder sql= new StringBuilder("insert into log values ");
                    for (ConsumerRecord<String, String> record : records){
                    	//System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                    	String threadName=Thread.currentThread().getName();
                    	//log.info(threadName+","+record.offset()+","+record.key()+","+record.value());
                    	sql.append("(").append(record.value().replace("\\t", ",")).append("),");
                    }
                    //remove the last comma
       	            sql.deleteCharAt(sql.length()-1);
       	            log.info("sql="+sql);
       	            try{
       	            	daoImp.executeSingleQuery(sql.toString());
       	            }catch(Exception e){
       	            	log.info("sql error",e);
       	            	throw new SQLException();
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
