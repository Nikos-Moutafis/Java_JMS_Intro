package loadbalancing;

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

import com.bharath.jms.hm.model.Patient;

public class ClinicalsApp {

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

			JMSProducer producer = jmsContext.createProducer();

			for (int i = 1; i <= 10; i++) {
			    ObjectMessage objectMessage = jmsContext.createObjectMessage();

			    Patient patient = new Patient();

			    patient.setId(i); 
			    patient.setName("Patient " + i); 
			    patient.setInsuranceProvider("Insurance Provider " + i); 
			    patient.setCopay(200d); 
			    patient.setAmountToBePayed(500d); 

			    objectMessage.setObject(patient);

			    // Send the message to the requestQueue
			    producer.send(requestQueue, objectMessage);
			}


		}
	}
}
