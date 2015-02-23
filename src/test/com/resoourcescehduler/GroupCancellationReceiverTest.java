package com.resoourcescehduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.Log4jConfigurer;

import com.resourcescehduler.GroupCancellationReceiver;
import com.resourcescehduler.IGateway;
import com.resourcescehduler.IResourceManager;
import com.resourcescehduler.SendToGateway;
import com.resourcescehduler.vo.Message;
import com.resourcescehduler.vo.MessageDecorator;
import com.resourcescehduler.vo.MessagingQueue;

public class GroupCancellationReceiverTest {
	private static Log logger = LogFactory.getLog(SendToGatewayTest.class);
	private GroupCancellationReceiver groupCancellationReceiver;

	/**
	 * 
	 */
	@Before
	public void setUp() {
		try {
			Log4jConfigurer.initLogging("classpath:log4j.xml");
		} catch (FileNotFoundException ex) {
			System.err.println("Cannot Initialize log4j");
		}
		logger.info("*********** In Setup ******************");
		groupCancellationReceiver = new GroupCancellationReceiver();
	}
	
	@Test
	public void testCancelGroup(){
		Message message1 = new Message();
		message1.setMessageId(1);
		message1.setGroupId(1);
		message1.setName("message " + 1);
		MessageDecorator msgDec1 = new MessageDecorator(message1);
		
		Message message2 = new Message();
		message2.setMessageId(1);
		message2.setGroupId(1);
		message2.setName("message " + 1);
		MessageDecorator msgDec2 = new MessageDecorator(message2);
		
		groupCancellationReceiver.cancelGroup(1);
		
		assertTrue(groupCancellationReceiver.doesContain(1));
	}
	
}
