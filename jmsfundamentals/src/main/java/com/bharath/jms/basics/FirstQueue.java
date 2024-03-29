package com.bharath.jms.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;

import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstQueue {

	public static void main(String[] args) {

		/*
		 * This class -> InitialContext is the starting context for performing naming operations. All
		 * naming operations are relative to a context. The initial context implements
		 * the Context interface and provides the starting point for resolution of
		 * names. When the initial context is constructed, its environment is
		 * initialized with properties defined in the environment parameter passed to
		 * the constructor, and in any application resource files.
		 * In case of an Application Server an InitialContext is injected and provided automatically by Application Server
		 */
		InitialContext initialContext = null;
		Connection connection = null;

		try {
			initialContext = new InitialContext();

			/*
			 * A ConnectionFactory object encapsulates a set of connection configuration
			 * parameters Although the interfaces for administered objects do not explicitly
			 * depend on the Java Naming and Directory Interface (JNDI) API, the JMS API
			 * establishes the convention that JMS clients find administered objects by
			 * looking them up in a JNDI name space.
			 */
			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			connection = cf.createConnection();

			/*
			 * A Session object is a single-threaded context for producing and consuming
			 * messages. Although it may allocate provider resources outside the Java
			 * virtual machine (JVM), it is considered a lightweight JMS object.
			 */
			Session session = connection.createSession();

			Queue queue = (Queue) initialContext.lookup("queue/myQueue");
			MessageProducer producer = session.createProducer(queue);

			/*
			 * Creates an initialized TextMessage object. A TextMessage object is used to
			 * send a message containing a String. The message object returned may be sent
			 * using any Session or JMSContext. It is not restricted to being sent using the
			 * JMSContext used to create it. The message object returned may be optimised
			 * for use with the JMS provider used to create it. However it can be sent using
			 * any JMS provider, not just the JMS provider used to create it.
			 * 
			 * 
			 */
			TextMessage message = session.createTextMessage("Hello from Piraeus");

			producer.send(message);

			System.out.println("Message sent: " + message.getText());

			MessageConsumer consumer = session.createConsumer(queue);
			connection.start();

			/*
			 * A TextMessage object is used to send a message containing a java.lang.String.
			 * It inherits from the Message interface and adds a text message body. This
			 * message type can be used to transport text-based messages, including those
			 * with XML content.
			 * 
			 */
			TextMessage messageReceived = (TextMessage) consumer.receive(5000);

			System.out.println("Message received:  " + messageReceived.getText());
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			if (initialContext != null) {
				try {
					initialContext.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
