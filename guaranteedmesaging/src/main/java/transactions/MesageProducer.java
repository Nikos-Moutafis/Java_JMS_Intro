package transactions;

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
				 * This session mode instructs the JMSContext's session to deliver and consume
				 * messages in a local transaction which will be subsequently committed by
				 * calling commit() or rolled back by calling rollback(). This method -> createContext() must not be
				 * used by applications running in the Java EE web or EJB containers because
				 * doing so would violate the restriction that such an application must not
				 * attempt to create more than one active (not closed) Session object per
				 * connection. If this method is called in a Java EE web or EJB container then a
				 * JMSRuntimeException will be thrown.
				 * 
				 */

				JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED)) {

			JMSProducer producer = jmsContext.createProducer();
			producer.send(requestQueue, "Mesage 1");
			producer.send(requestQueue, "Mesage 2");

			jmsContext.commit();
		}
	}
}
