package com.bharath.jms.guaranteedmessaging;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MesageProducer {

	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();

				/*
				 * With this session mode -> AUTO_ACKNOWLEDGE, the JMSContext's session
				 * automatically acknowledges a client's receipt of a message either when the
				 * session has successfully returned from a call to receive or when the message
				 * listener the session has called to process the message successfully returns.
				 */

				JMSContext jmsContext = cf.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {

			JMSProducer producer = jmsContext.createProducer();
			producer.send(requestQueue, "Mesage 1");

		}
	}
}
