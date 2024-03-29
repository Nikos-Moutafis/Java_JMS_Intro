package com.bharath.jms.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageDelayDemo {  

	public static void main(String[] args) throws NamingException, InterruptedException {

		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");


		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

			JMSProducer producer = jmsContext.createProducer();
			producer.setDeliveryDelay(3000);
			producer.send(queue, "Arise and don't stop");
			
			Message messageReceived = jmsContext.createConsumer(queue).receive(5000);
			System.out.println(messageReceived);
			
		}
	}

}
