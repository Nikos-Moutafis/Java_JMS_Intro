package com.bharath.jms.grouping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageGroupingDemo {

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {

		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		Map<String,String> receivedMessages = new ConcurrentHashMap<>();

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext();
				JMSContext jmsContext2 = cf.createContext()) {
			
			JMSProducer producer = jmsContext.createProducer();
			JMSConsumer consumer1 = jmsContext2.createConsumer(queue);
			consumer1.setMessageListener(new MyListener("Consumer-1", receivedMessages));

			
			JMSConsumer consumer2 = jmsContext2.createConsumer(queue);
			consumer2.setMessageListener(new MyListener("Consumer-2", receivedMessages));

			
			int count = 10000;
			TextMessage[] messages = new TextMessage[count];

			for (int i = 0; i < count; i++) {
				messages[i] = jmsContext.createTextMessage("Group-0 message " + i);
				messages[i].setStringProperty("JMSXGroupID", "Group-0");
				producer.send(queue, messages[i]);
			}
			
			Thread.sleep(8000);
			
			for(TextMessage message : messages) {
				if(!receivedMessages.get(message.getText()).equals("Consumer-1")){
					throw new IllegalStateException("Group Message " + message.getText() + " has gone to the wrong receiver");
				}
			}
		}
	}
}
