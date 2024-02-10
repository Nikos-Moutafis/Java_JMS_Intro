package com.bharath.jms.grouping;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyListener implements MessageListener {

	private String name;

	private final Map<String, String> receivedMessages;

	public MyListener(String name, Map<String, String> receivedMessages) {
		this.name = name;
		this.receivedMessages = receivedMessages;
	}

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println("Message received is " + textMessage.getText());
			System.out.println("Listener name is " + name);

			receivedMessages.put(textMessage.getText(), name);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
