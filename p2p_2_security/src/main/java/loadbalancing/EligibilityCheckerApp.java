package loadbalancing;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.bharath.jms.hm.model.Patient;

/*
 * Synchronous messaging with load balancing
 */

public class EligibilityCheckerApp {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

			JMSConsumer consumer1 = jmsContext.createConsumer(requestQueue);
			JMSConsumer consumer2 = jmsContext.createConsumer(requestQueue);

			for (int i = 1; i <= 10; i += 2) {
				Patient patient1 = consumer1.receiveBody(Patient.class);
				System.out.println(patient1);

				Patient patient2 = consumer2.receiveBody(Patient.class);
				System.out.println(patient2);
			}

		}
	}
}
