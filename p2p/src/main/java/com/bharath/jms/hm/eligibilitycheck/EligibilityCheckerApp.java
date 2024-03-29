package com.bharath.jms.hm.eligibilitycheck;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.bharath.jms.hm.eligibilitycheck.listener.EligibilityCheckListener;

/*
 *  Asynchronous Messaging with MessageListener
 *  If we use synchronous messaging, if there are no messages on the queue
 *  consumer will block on the queue without doing other work, only when a message comes into the queue 
 *  the consumer will receive it with the receive method.Until then it will be polling on the queue 
 *  to check if there is a message which is an overhead and waste of time for the consumer
 *  
 */
public class EligibilityCheckerApp {

	public static void main(String[] args) throws NamingException, InterruptedException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

			JMSConsumer consumer = jmsContext.createConsumer(requestQueue);

			/*
			 * Once we create this message listener we can register it 
			 * on the consumer side and the jms provider whenever there is a message on the
			 * queue it will automatically invoke the on message method of the listener.
			 * 
			 * In asynchronous processing   consumer doesn't need to  wait or poll any
			 * longer, will simply register a listener and the provider will asynchronously call that listeners on
			 * message method when the message comes in and 
			 * it will wrap the message inside this message and it will hand it
			 * over to the on message method.
			 */
			consumer.setMessageListener(new EligibilityCheckListener());

			
			/*
			 * When we use JMS APIs, in the context of an Application Server, the Server 
			 * will be up and running (also the JVM), but here when we run this class' main method,
			 * it will run the main method and will be done without any waiting.
			 * On Application Servers we don't need this sleep method, but since
			 * we are running this applications individually, the EligibilityCheckerApp has to wait 
			 * for some time to receive a message
			 */
			Thread.sleep(10000);
		}
	}
}
