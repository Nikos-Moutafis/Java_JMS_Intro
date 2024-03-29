package com.bharath.jms.guaranteedmessaging;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageConsumer {

	
	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				/*
				 * with this -> CLIENT_ACKNOWLEDGE you can consume the message as many times as you want because the
				 * provider will not remove the message from the queue unless you use
				 * message.acknowledge explicitly
				 */
				JMSContext jmsContext = cf.createContext(JMSContext.CLIENT_ACKNOWLEDGE)) {
			/*
			 * With this session mode -> CLIENT_ACKNOWLEDGE, the client acknowledges a
			 * consumed message by calling the message's acknowledge method.
			 *
			 * Acknowledging a consumed message acknowledges all messages that the session
			 * has consumed. When this session mode is used, a client may build up a large
			 * number of unacknowledged messages while attempting to process them. A JMS
			 * provider should provide administrators with a way to limit client overrun so
			 * that clients are not driven to resource exhaustion and ensuing failure when
			 * some resource they are using is temporarily blocked.
			 */

			JMSConsumer consumer = jmsContext.createConsumer(requestQueue);

			TextMessage message = (TextMessage) consumer.receive();
			System.out.println(message.getText());

			/*
			 * Acknowledges all consumed messages of the session of this consumed message.
			 * All consumed JMS messages support the acknowledge method for use when a
			 * client has specified that its JMS session's consumed messages are to be
			 * explicitly acknowledged. By invoking acknowledge on a consumed message, a
			 * client acknowledges all messages consumed by the session that the message was
			 * delivered to.
			 * 
			 */
			message.acknowledge();
		}
	}

}
