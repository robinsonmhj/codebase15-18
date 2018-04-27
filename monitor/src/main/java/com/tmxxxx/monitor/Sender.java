package com.tmxxxx.monitor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;



public class Sender {
	
	private static Logger log= Logger.getLogger(Sender.class);
	public static Sender instance=null;
	private Producer<String, String> producer; 
	private String topic;
	private String delimiter;
	private Sender(){
		//Map<String, Object> map=Properties.getMap();
		Map<String, Object> map= new HashMap<String,Object>();
		map.put("bootstrap.servers", "T440p121.tmghealth.com:9092,T440p121.tmghealth.com:9093,T440p121.tmghealth.com:9094");
		map.put("producer.type", "async");
		map.put("topic.name", "monitor.default");
		map.put("buffer.memory", "2048000");
		map.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		map.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		map.put("retries", "0");
		map.put("linger.ms", "20");
		map.put("batch.size", "100");
		map.put("acks", "1");
		map.put("max.block.ms", "0");//controls how long send() method will block
		map.put("request.timeout.ms", "100");
		//map.put("metadata.fetch.timeout.ms", "100");
		map.put("compression.type", "gzip");
		map.put("delimiter", ",");
		//topic=Properties.getProperty("topic.name");
		//delimiter=Properties.getProperty("delimiter");
		topic=(String)map.get("topic.name");
		delimiter=(String)map.get("delimiter");
		try{
			//log.info("I am before creating producer");
			producer = new KafkaProducer<String,String>(map);
			//log.info("I am after creating producer");
		}catch(Exception e){
			log.info("create productor error",e);
		}
		
	}
	
	public synchronized static Sender getInstance(){	
		if(instance==null){
			instance= new Sender();
		}
		return instance;
	}
	
	public void send(String hostname,long currentTime,String itemName,long itemValue,String comment){
		StringBuilder value=new StringBuilder();
		value.append("'").append(hostname).append("'").append(delimiter);
		value.append(currentTime).append(delimiter);
		value.append("'").append(itemName).append("'").append(delimiter);
		value.append(itemValue).append(delimiter);
		value.append("'").append(comment).append("'");
		//long start=System.currentTimeMillis();
		try{
			producer.send(new ProducerRecord<String, String>(topic,hostname,value.toString()));
		}catch(Exception e){
			log.info("send message error",e);
		}
		
		//long end=System.currentTimeMillis();
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
		log.info(timeStamp+","+value);
	}
	
	

}
