package com.resoourcescehduler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.io.FileNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.Log4jConfigurer;

import com.resourcescehduler.IGroupCancellationReceiver;
import com.resourcescehduler.IResourceManager;
import com.resourcescehduler.ISendToGateway;
import com.resourcescehduler.MessageReceiver;
import com.resourcescehduler.vo.IMessagingQueue;
import com.resourcescehduler.vo.Message;
import com.resourcescehduler.vo.MessageDecorator;
import com.resourcescehduler.vo.MessagingQueue;
import com.resourcescehduler.vo.Resource;

/**
 * @author NYalamanchili
 * 
 */
public class MessageReceiverTest {
	private static Log logger = LogFactory.getLog(MessageReceiverTest.class);
	private MessageReceiver receiver;
	private ISendToGateway sendToGateway;
	private IGroupCancellationReceiver groupCancellationReceiver;
	private IResourceManager resourceManager;
	private IMessagingQueue messagingQueue;

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
		
		receiver = new MessageReceiver();
		
		sendToGateway = Mockito.mock(ISendToGateway.class, withSettings()
				.extraInterfaces(Runnable.class));
		groupCancellationReceiver = Mockito
				.mock(IGroupCancellationReceiver.class);
		resourceManager = Mockito.mock(IResourceManager.class);
		messagingQueue = Mockito.mock(MessagingQueue.class);
		
		receiver.setGroupCancellationReceiver(groupCancellationReceiver);
		receiver.setSendToGateway(sendToGateway);
		receiver.setResourceManager(resourceManager);
		receiver.setMessagingQueue(messagingQueue);

	}

	@Test
	public void testReceiveMessageContainsCancelGroupId()
			throws InterruptedException {
		logger.debug("testReceiveMessageContainsCancelGroupId");
		Message message = new Message();
		message.setMessageId(1);
		message.setGroupId(1);
		message.setName("message " + 1);
		when(groupCancellationReceiver.doesContain(1)).thenReturn(true);
		receiver.receiver(message);
		verify(groupCancellationReceiver).doesContain(1);
	}

	@Test
	public void testReceiveMessageWithMessageInQueue()
			throws InterruptedException {
		logger.debug("testReceiveMessageWithMessageInQueue");
		Message message = new Message();
		message.setMessageId(1);
		message.setGroupId(1);
		message.setName("message " + 1);
		MessageDecorator msgDec = new MessageDecorator(message);
		Resource resource = new Resource();
		resource.setStatus(Resource.PREPARE);
		resource.setResourceId(1);

		when(groupCancellationReceiver.doesContain(1)).thenReturn(false);
		when(messagingQueue.isQueueEmpty()).thenReturn(false, true);
		when(resourceManager.isResourceAvailable()).thenReturn(resource);
		when(messagingQueue.getCurrentMessageId()).thenReturn(1);
		when(messagingQueue.isHeadMsg(1)).thenReturn(true,false);
				
		receiver.receiver(message);
		Thread.sleep(1000);
		verify(groupCancellationReceiver).doesContain(anyInt());
		verify(messagingQueue).addToQueue((MessageDecorator)any());
		verify(messagingQueue, times(2)).isQueueEmpty();
		verify(messagingQueue, times(1)).getCurrentMessageId();
		verify(messagingQueue,times(2)).isHeadMsg(1);

		assertEquals(resource.getStatus(), Resource.BUSY);
		assertEquals(resource.getMsgId(), 1);

	}

	@Test
	public void testReceiveMessageWithNoMessageInQueue()
			throws InterruptedException {
		logger.debug("testReceiveMessageWithNoMessageInQueue");
		
		Message message = new Message();
		message.setMessageId(1);
		message.setGroupId(1);
		message.setName("message " + 1);
		MessageDecorator msgDec = new MessageDecorator(message);
		Resource resource = new Resource();
		resource.setStatus(Resource.FREE);
		resource.setResourceId(1);

		when(groupCancellationReceiver.doesContain(1)).thenReturn(false);
		when(messagingQueue.isQueueEmpty()).thenReturn(true);

		receiver.receiver(message);

		verify(groupCancellationReceiver).doesContain(anyInt());
		verify(messagingQueue).addToQueue((MessageDecorator)any());
		verify(messagingQueue, times(1)).isQueueEmpty();

		assertEquals(resource.getStatus(), Resource.FREE);

	}

	@After
	public void tearDown() {
		logger.info("*********** In tear down ******************");
	}
}
