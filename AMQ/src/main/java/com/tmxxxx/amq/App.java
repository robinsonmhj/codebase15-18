/**
 * 
 */
package com.tmxxxx.amq;





/**
 * @author Haojie Ma
 * @date May 19, 2016
 */
public class App {
	
	public static void main(String[] args) throws Exception {
       
		
		thread(new HelloWorldProducer(), false);
	    thread(new HelloWorldProducer(), false);
	    thread(new HelloWorldConsumer(), false);
	    Thread.sleep(1000);
	    thread(new HelloWorldConsumer(), false);
	    thread(new HelloWorldProducer(), false);
	    thread(new HelloWorldConsumer(), false);
	    thread(new HelloWorldProducer(), false);
	    Thread.sleep(1000);
	    thread(new HelloWorldConsumer(), false);
	    thread(new HelloWorldProducer(), false);
	    thread(new HelloWorldConsumer(), false);
	    thread(new HelloWorldConsumer(), false);
	    thread(new HelloWorldProducer(), false);
	    thread(new HelloWorldProducer(), false);
	    Thread.sleep(1000);
	    thread(new HelloWorldProducer(), false);
	    thread(new HelloWorldConsumer(), false);
	    thread(new HelloWorldConsumer(), false);
	    thread(new HelloWorldProducer(), false);
	    thread(new HelloWorldConsumer(), false);
	    thread(new HelloWorldProducer(), false);
	    thread(new HelloWorldConsumer(), false);
	    thread(new HelloWorldProducer(), false);
	    thread(new HelloWorldConsumer(), false);
	    thread(new HelloWorldConsumer(), false);
	    thread(new HelloWorldProducer(), false);
	    
	}
 
	
	
	
    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
 

}


