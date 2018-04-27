/**
 * 
 */
package com.tmxxxx.amq;

/**
 * @author Haojie Ma
 * @date May 20, 2016
 */
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.springframework.stereotype.Component;



	@Component
	public class JmsExceptionListener implements ExceptionListener
	{
	    public void onException( final JMSException e )
	    {
	        e.printStackTrace();
	    }
	}
