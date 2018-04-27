/**
 * 
 */
package com.tmxxxx.amq;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author Haojie Ma
 * @date May 20, 2016
 */
public class TestAmq {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		GenericXmlApplicationContext ctx=new GenericXmlApplicationContext();  
	   ctx.load("classpath:applicationContext.xml");  
	   ctx.refresh();  
	   
	
	  
	   final int no=4;
	   ThreadSender[] threadSenders= new ThreadSender[no];
	   Thread[] threads= new Thread[no];
	   
	   for(int i=0;i<no;i++){
		   threadSenders[i]=ctx.getBean("threadSender", ThreadSender.class);
		   threadSenders[i].setName("thredSend"+i);
		   threads[i]=new Thread(threadSenders[i],"thread"+i);
	   }
		   
	   
	   for(int i=0;i<no;i++)
		   threads[i].start();

	 /*  
	   ThreadSender sender=ctx.getBean("messageSender", ThreadSender.class);
	   sender.setName("Sender");
	*/   
	   
	   
	   
	   
	   
	   
	   
	   /*
	   QueueSender sender=ctx.getBean("messageSender", QueueSender.class);
	   for(int i=0;i<100;i++){
		   sender.send("Hello"+i+", at time:"+System.currentTimeMillis());
	   }
	   //System.out.println("sending message ending"+System.currentTimeMillis());
	   */
	    

	}

}


