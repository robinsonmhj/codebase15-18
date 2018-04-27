package com.tmxxxx.consumer;



import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import com.tmxxxx.core.Properties;

public class Receiver {
	
	
	private static Logger log=Logger.getLogger(Receiver.class);
	
	
	public static void main(String[] args){
		//System.out.println("Hello");
		Map<String,Object> map=Properties.getMap();
		Consumer<String, String> consumer = new KafkaConsumer<String,String>(map);
		 consumer.subscribe(Arrays.asList("monitor.log","monitor.default"));
		 while (true) {
	         ConsumerRecords<String, String> records = consumer.poll(5);
	         for (ConsumerRecord<String, String> record : records){
	        	 //System.out.println("Hello1");
	        	 String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
	             //System.out.printf(timeStamp+",offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
	             log.info(timeStamp+","+record.offset()+","+record.key()+","+record.value());
	         }
	        	
	     }
	}
	
	
	
	

}
