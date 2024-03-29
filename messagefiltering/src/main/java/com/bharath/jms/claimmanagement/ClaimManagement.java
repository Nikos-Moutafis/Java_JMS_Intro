package com.bharath.jms.claimmanagement;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class ClaimManagement {

	//Synchronous messaging
	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/claimQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

			JMSProducer producer = jmsContext.createProducer();
			
			//Create a filter on consumer, only the message with IntProperty hospitalId = 1
			// will be consumed, others will be ignored
			// we can also use  sql like commands ( between 1000 AND 5000)
			//default priority of messages is 4
			
			//consumer = jmsContext.createConsumer(requestQueue, "hospitalId=1");
			//JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "claimAmount BETWEEN 1000 AND 1500");
			//JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "doctorName='John'");
			//JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "doctorName LIKE 'J%'");
			//JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "doctorType IN ('neuro', 'psych') OR"
				//	+ " JMSPriority BETWEEN 3 AND 6");

			JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "(insuranceProvider='blue cross')");
		//	JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "doctorName LIKE 'H%' ");
			//JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "  claimAmount % 10  =  0");

			//JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
			ObjectMessage objectMessage = jmsContext.createObjectMessage();

			//Producer sets a message with an IntProperty hospital Id = 1
			//and knows that only these messages will be 
			//consumed by the above consumer
			//IntProperty is also called identifier
			//and is a contract between consumer and producer
			
			
			//objectMessage.setIntProperty("hospitalId", 1);
			//objectMessage.setDoubleProperty("claimAmount", 1000);
			//objectMessage.setStringProperty("doctorName", "John");
			objectMessage.setStringProperty("insuranceProvider", "blue cross");
			Claim claim = new Claim();
			claim.setHospitalId(1);
			claim.setClaimAmount(1000);
			claim.setDoctorName("Hohn");
			claim.setDoctorType("gyna");
			claim.setInsuranceProvider("blue cross");

			objectMessage.setObject(claim);

			producer.send(requestQueue, objectMessage);

			 Claim receiveBody = consumer.receiveBody(Claim.class);
			 System.out.println(receiveBody.getClaimAmount());
		}

	}

}
