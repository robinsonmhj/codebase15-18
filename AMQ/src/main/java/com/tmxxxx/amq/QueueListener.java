/**
 * 
 */
package com.tmxxxx.amq;

/**
 * @author Haojie Ma
 * @date May 20, 2016
 */
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

@Component("queueListener")
public class QueueListener implements MessageListener
{
    public void onMessage( final Message message )
    {
        if ( message instanceof TextMessage )
        {
            final TextMessage textMessage = (TextMessage) message;
            try
            {
                System.out.println(Thread.currentThread().getName()+" consumeed "+textMessage.getText()+":"+System.currentTimeMillis());
               
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

