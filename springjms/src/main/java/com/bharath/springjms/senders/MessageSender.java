package com.bharath.springjms.senders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Value("${springjms.myQueue}")
	private String queue;

	public void send(String message) {

		/* Automatically converts this message to JMS TextMessage */
		jmsTemplate.convertAndSend(queue, message);

		/*
		 * Here you can pass also headers and other properties unlike the convertAndSend method
		 * 
		 * MessageCreator mc = s -> s.createTextMessage("Hello Spring Jms!");
		 * jmsTemplate.send(queue, mc);
		 */


		/*
		 * Default value is false, if set to true JMS Template knows the message is sent
		 * to a topic
		 * jmsTemplate.setPubSubDomain(true); 
		 */
	}

}
