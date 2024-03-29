package com.bharath.jms.basics;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

/*
 * This class shows the JMS 2.0 API for messages 
 * and demonstrates the simplicity we have in contrary to JMS 1.0 
 * A JMSContext is the main interface in the simplified JMS API introduced for JMS 2.0. 
 * This combines in a single object the functionality of two separate objects from the JMS 1.1 API: a Connection and a Session.
*/

public class JMSContextDemo {

	public static void main(String[] args) throws NamingException {

		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");

		/*
		 * A JMSContext may be created by calling one of several createContext methods
		 * on a ConnectionFactory. A JMSContext that is created in this way is described
		 * as being application-managed. An application-managed JMSContext must be
		 * closed when no longer needed by calling its close method.
		 */
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

			/*
			 * When an application needs to send messages it use the createProducer method
			 * to create a JMSProducer which provides methods to configure and send
			 * messages. Messages may be sent either synchronously or asynchronously.
			 * 
			 * In terms of the JMS 1.1 API a JMSContext should be thought of as representing
			 * both a Connection and a Session. Although the simplified API removes the need
			 * for applications to use those objects, the concepts of connection and session
			 * remain important. A connection represents a physical link to the JMS server
			 * and a session represents a single-threaded context for sending and receiving
			 * messages.
			 */
			jmsContext.createProducer().send(queue, "Arise and don't stop");

			/*
			 * Creates a JMSConsumer for the specified destination. A client uses a
			 * JMSConsumer object to receive messages that have been sent to a destination.
			 * 
			 * receiveBody -> Receives the next message produced for this JMSConsumer and
			 * returns its body as an object of the specified type.
			 */
			String messageReceived = jmsContext.createConsumer(queue).receiveBody(String.class);

			System.out.println(messageReceived);
			/* jmsContext.close(); */
		}
	}

}
