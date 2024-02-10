package com.bharath.jms.messagestructure;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

//This class shows the JMS 2.0 API for messages
//and demonstrates the simplicity we have in contrary to JMS 1.0
public class RequestReplyDemo {

	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext context = new InitialContext();
		//Queue queue = (Queue) context.lookup("queue/requestQueue");
	//	Queue replyQueue = (Queue) context.lookup("queue/replyQueue");

		
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

			JMSProducer producer = jmsContext.createProducer();
			Queue queue =jmsContext.createTemporaryQueue();

			Queue replyQueue = jmsContext.createTemporaryQueue();
			TextMessage message = jmsContext.createTextMessage("Hello !!");
			message.setJMSReplyTo(replyQueue);
			producer.send(queue, message);
			System.out.println("ID sent: " + message.getJMSMessageID());
			
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			TextMessage messageReceived = (TextMessage) consumer.receive();
			System.out.println("Message in request queue: " + messageReceived.getText());
			
			JMSProducer replyProducer = jmsContext.createProducer();
			TextMessage replyMessage = jmsContext.createTextMessage("You are awesome!!");
			replyMessage.setJMSCorrelationID(messageReceived.getJMSMessageID());
			System.out.println("end1");

			replyProducer.send(messageReceived.getJMSReplyTo(), replyMessage);
			System.out.println("end2");

			
			JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
			//String repliedMessage = replyConsumer.receiveBody(String.class);
			System.out.println("end3");

			//System.out.println("Message from reply queue: " + repliedMessage);
			TextMessage replyReceived = (TextMessage) replyConsumer.receive();
			System.out.println("end4");

			System.out.println("Id in reply "  + replyReceived.getJMSCorrelationID());
		}
	}

}
