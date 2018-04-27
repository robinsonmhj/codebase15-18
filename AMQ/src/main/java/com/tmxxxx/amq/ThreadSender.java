/**
 * 
 */
package com.tmxxxx.amq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Haojie Ma
 * @date May 23, 2016
 */

@Component("threadSender")
@Scope("prototype")
public class ThreadSender implements Runnable{
	
	
	private String name;
	//@Qualifier("messageSender")
	@Autowired
	private QueueSender sender;

	@Autowired
    public ThreadSender(QueueSender sender)
    {
        this.sender = sender;
    }
	
	
	
	public void run(){
		
		String name=Thread.currentThread().getName();
		while(true){
			sender.setName(name);;
			sender.send(name+":"+this.name+sender.getName());
		}
			
			
			
	}
	
	public void setName(String name){
		
		this.name=name;
		
	}
	
	
	public String getName(){
		return name;
		
		
	}

}


