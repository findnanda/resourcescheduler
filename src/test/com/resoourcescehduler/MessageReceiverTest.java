package com.resoourcescehduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.anyBoolean;
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
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.util.Log4jConfigurer;

import com.resourcescehduler.IGroupCancellationReceiver;
import com.resourcescehduler.IResourceManager;
import com.resourcescehduler.ISendToGateway;
import com.resourcescehduler.MessageReceiver;
import com.resourcescehduler.vo.Message;
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
		receiver.setGroupCancellationReceiver(groupCancellationReceiver);
		receiver.setSendToGateway(sendToGateway);
		receiver.setResourceManager(resourceManager);

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
		receiver.receiveMessage(message);
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

		Resource resource = new Resource();
		resource.setStatus(Resource.PREPARE);
		resource.setResourceId(1);

		when(groupCancellationReceiver.doesContain(1)).thenReturn(false);
		when(sendToGateway.isQueueEmpty()).thenReturn(false, true);
		when(resourceManager.isResourceAvailable()).thenReturn(resource);

		receiver.receiveMessage(message);

		verify(groupCancellationReceiver).doesContain(anyInt());
		verify(sendToGateway).addToQueue(message);
		verify(sendToGateway, times(2)).isQueueEmpty();
		verify(resourceManager).isResourceAvailable();

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

		Resource resource = new Resource();
		resource.setStatus(Resource.FREE);
		resource.setResourceId(1);

		when(groupCancellationReceiver.doesContain(1)).thenReturn(false);
		when(sendToGateway.isQueueEmpty()).thenReturn(true);

		receiver.receiveMessage(message);

		verify(groupCancellationReceiver).doesContain(anyInt());
		verify(sendToGateway).addToQueue(message);
		verify(sendToGateway, times(1)).isQueueEmpty();

		assertEquals(resource.getStatus(), Resource.FREE);

	}

	@After
	public void tearDown() {
		logger.info("*********** In tear down ******************");
	}
}
