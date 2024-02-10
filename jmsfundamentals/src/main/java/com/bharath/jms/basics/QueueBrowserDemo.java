package com.bharath.jms.basics;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;


//This class demonstrates how we can browse the queue and the messages within it 
//without consuming the messages
public class QueueBrowserDemo {

	public static void main(String[] args) {

		InitialContext initialContext = null;
		Connection connection = null;

		try {
			initialContext = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			connection =  cf.createConnection();
			
			Session session = connection.createSession();
			
			Queue queue = (Queue) initialContext.lookup("queue/myQueue");
			MessageProducer producer = session.createProducer(queue);
			
			TextMessage message1 = session.createTextMessage("Message 1");
			TextMessage message2 = session.createTextMessage("Message 2");
			
			producer.send(message1);
			producer.send(message2);

			
			System.out.println("Message sent: " + message1.getText());
			System.out.println("Message sent: " + message2.getText());

			QueueBrowser browser = session.createBrowser(queue);
			
			Enumeration messagesEnum = browser.getEnumeration();
			
			while(messagesEnum.hasMoreElements()) {
				TextMessage eachMessage = (TextMessage) messagesEnum.nextElement();
				System.out.println("Browsing: " + eachMessage.getText());
			}
			

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}finally {
			if(initialContext != null) {
				try {
					initialContext.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
