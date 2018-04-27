package com.tmxxxx.monitor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

public class Main {
	private static Logger log=Logger.getLogger(Main.class);

	public static void main(String[] args) {
		
		
		Sender sender= Sender.getInstance();
		
		String hostname="localhost";
		String itemName="test";
		long itemValue=0;
		Random rand = new Random();
		while(itemValue<1000000000){
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
			long time=System.currentTimeMillis();
			int index=rand.nextInt(3)+1;
			try{
				Thread.sleep(5);
			}catch(Exception e){
				log.info("I cannot sleep",e);
			}
			//log.info("I am in main before sending message");
			sender.send(hostname+index,time,itemName,itemValue,timeStamp);
			//log.info("I am in main after sending message");
			itemValue++;
			//System.out.println("itemValue="+itemValue);
			
		}
		
		
		
		
		
		

	}

}
