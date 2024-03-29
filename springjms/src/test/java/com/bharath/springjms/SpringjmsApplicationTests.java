package com.bharath.springjms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bharath.springjms.senders.MessageSender;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringjmsApplicationTests {

	@Autowired
	MessageSender sender;
	
	@Test
	public void testSendAndReceive() {
		sender.send("Hello Spring JMS");
	}

}
