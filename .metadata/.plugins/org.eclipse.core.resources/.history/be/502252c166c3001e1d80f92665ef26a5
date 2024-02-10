package com.bharath.jms.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

//This class 
//and demonstrates the expiration in messages and how we can access an expired message
public class MessageExpirationDemo {  

	public static void main(String[] args) throws NamingException, InterruptedException {

		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		Queue expiryQueue = (Queue) context.lookup("queue/expiryQueue");


		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

			JMSProducer producer = jmsContext.createProducer();
		    producer.setTimeToLive(2000);
			producer.send(queue, "Arise and don't stop");
			Thread.sleep(5000);
			
			Message messageReceived = jmsContext.createConsumer(queue).receive(5000);
			System.out.println(messageReceived);
			
			System.out.println(jmsContext.createConsumer(expiryQueue).receiveBody(String.class));
		}
	}

}
