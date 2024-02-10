package org.example;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.model.Passenger;

public class EligibilityCheckListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		ObjectMessage objectMessage = (ObjectMessage) message;

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED)) {

			InitialContext initialContext = new InitialContext();

			Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

			MapMessage replyMessage = jmsContext.createMapMessage();

			Passenger passenger = (Passenger) objectMessage.getObject();

			System.out.println("passenger: " + passenger);

			message.acknowledge();

			if (objectMessage.getJMSCorrelationID().equals("123")) {
				replyMessage.setBoolean("isEligible", true);
			} else {
				replyMessage.setBoolean("isEligible", false);
			}

			JMSProducer producer = jmsContext.createProducer();
			producer.send(replyQueue, replyMessage);
			jmsContext.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

}
