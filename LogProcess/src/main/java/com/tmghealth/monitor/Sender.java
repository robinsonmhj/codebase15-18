package com.tmghealth.monitor;

import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.tmghealth.core.Properties;


public class Sender {
	
	public static Sender instance=null;
	private Producer<String, String> producer; 
	private String topic;
	private String delimiter;
	private Sender(){
		Map<String, Object> map=Properties.getMap();
		topic=Properties.getProperty("topic.name");
		delimiter=Properties.getProperty("delimiter");
		producer = new KafkaProducer<String,String>(map);
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
		value.append(itemName).append(delimiter);
		value.append(itemValue).append(delimiter);
		value.append("'").append(comment).append("'");
		producer.send(new ProducerRecord<String, String>(hostname,value.toString()));
	}
	
	public void send(String key,String value){
		
		producer.send(new ProducerRecord<String, String>(key,value));
		
		
	}

}
