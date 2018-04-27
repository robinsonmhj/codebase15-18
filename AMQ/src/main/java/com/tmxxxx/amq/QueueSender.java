/**
 * 
 */
package com.tmxxxx.amq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Haojie Ma
 * @date May 20, 2016
 */
@Component("messageSender")
@Scope("prototype")
public class QueueSender
{
   // private final JmsTemplate jmsTemplate;
	//@Autowired
    private String name;

    /*
    @Autowired
    public QueueSender(final JmsTemplate jmsTemplate )
    {
        this.jmsTemplate = jmsTemplate;
    }
	*/
    @Autowired
    public QueueSender(@Value("queue") String name)
    {
        this.name=name;
    }
    
    public void send(final String message )
    {
        //jmsTemplate.convertAndSend( "multiplethreadTest", message);
    	System.out.println("Sender is sending "+message);
    	try{
    		
    		Thread.sleep(1000);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    public String getName(){
    	return name;
    	
    	
    }
    
 
    public void  setName(String name){
    	this.name=name;
    	
    	
    }
    
}

