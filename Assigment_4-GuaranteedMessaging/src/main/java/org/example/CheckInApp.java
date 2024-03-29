package org.example;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.model.Passenger;

public class CheckInApp {

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED)) {

			JMSProducer producer = jmsContext.createProducer();
			ObjectMessage message1 = jmsContext.createObjectMessage();

			Passenger passenger1 = new Passenger();
			passenger1.setId(1L);
			passenger1.setFirstname("Johnathan");
			passenger1.setLastname("Wick");
			passenger1.setPhone("123454321");
			passenger1.setEmail("nmn@gmail.com");

			message1.setObject(passenger1);
			message1.setJMSCorrelationID("123");
			JMSConsumer consumer = jmsContext.createConsumer(replyQueue);

			for (int i = 0; i < 2; i++) {
				producer.send(requestQueue, message1);
			}
			
			jmsContext.commit();
			ObjectMessage message2 = jmsContext.createObjectMessage();

			Passenger passenger2 = new Passenger();
			passenger2.setId(1L);
			passenger2.setFirstname("John");
			passenger2.setLastname("Rambo");
			passenger2.setPhone("9999999");
			passenger2.setEmail("nmn@gmail.com");

			message2.setObject(passenger2);
			message2.setJMSCorrelationID("777");
			
			producer.send(requestQueue, message2);
			jmsContext.rollback();

			for (int i = 0; i < 2; i++) {

				MapMessage replyMessage = (MapMessage) consumer.receive(30000);
				System.out.println("eligibility is: " + replyMessage.getBoolean("isEligible"));
				jmsContext.commit();
			}

		}
	}

}
