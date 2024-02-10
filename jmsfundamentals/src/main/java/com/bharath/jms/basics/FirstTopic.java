package com.bharath.jms.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

//This class demonstrates how we can create a publish-subscribe within a topic
public class FirstTopic {

	public static void main(String[] args) {
		InitialContext initialContext = null;
		Connection connection = null;

		try {
			initialContext = new InitialContext();
			Topic topic = (Topic) initialContext.lookup("topic/myTopic");

			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			connection = cf.createConnection();

			Session session = connection.createSession();
			MessageProducer producer = session.createProducer(topic);

			MessageConsumer consumer1 = session.createConsumer(topic);
			MessageConsumer consumer2 = session.createConsumer(topic);

			TextMessage message = (TextMessage) session.createTextMessage("All the power is with in me.");
			producer.send(message);

			TextMessage message11 = (TextMessage) session.createTextMessage("$$$$$$$$$$$.");
			producer.send(message11);

			connection.start();

			TextMessage message1 = (TextMessage) consumer1.receive();
			System.out.println("Consumer 1 message received: " + message1.getText());

			TextMessage message2 = (TextMessage) consumer2.receive();
			System.out.println("Consumer 2 message received: " + message2.getText());

			
			TextMessage message3 = (TextMessage) consumer1.receive();
			System.out.println("Consumer 1 message no2 received: " + message3.getText());

			TextMessage message4 = (TextMessage) consumer2.receive();
			System.out.println("Consumer 2 message no2 received: " + message4.getText());
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}

			if (initialContext != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
