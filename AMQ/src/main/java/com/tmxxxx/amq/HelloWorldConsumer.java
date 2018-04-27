/**
 * 
 */
package com.tmxxxx.amq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;


/**
 * @author Haojie Ma
 * @date May 19, 2016
 */
public  class HelloWorldConsumer implements Runnable, ExceptionListener {
    public void run() {
        try {

            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.setExceptionListener(this);

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("multiplethreadTest");

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);

            //start connection
            connection.start();
            // Wait for a message
            while(true){
            	 Message message = consumer.receive(1000);
            	 if(message==null)
            		 break;
                 if (message instanceof TextMessage) {
                     TextMessage textMessage = (TextMessage) message;
                     String text = textMessage.getText();
                     System.out.println("Received: " + text);
                 } else {
                     System.out.println("Received: " + message);
                 }
            }
           

            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }
}


