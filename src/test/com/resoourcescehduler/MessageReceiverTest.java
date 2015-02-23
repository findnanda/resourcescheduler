package com.resoourcescehduler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.io.FileNotFoundException;
import java.util.HashMap;

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
import com.resourcescehduler.ResourceManager;
import com.resourcescehduler.sorting.ISortingRules;
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
	private ISortingRules sortRules;
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
		resourceManager = Mockito.mock(ResourceManager.class);
		((ResourceManager)resourceManager).setResourcePool(2);
		messagingQueue = Mockito.mock(MessagingQueue.class);
		sortRules = Mockito.mock(ISortingRules.class);
		
		receiver.setGroupCancellationReceiver(groupCancellationReceiver);
		receiver.setSendToGateway(sendToGateway);
		receiver.setResourceManager(resourceManager);
		receiver.setMessagingQueue(messagingQueue);
		receiver.setSortRules(sortRules);
		receiver.setForcePGSOff(false);
		receiver.setGroupPriortizationMap(new HashMap<Integer, Long>());

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
	public void testReceiveMessageWithMessageInQueueWithExtraResource()
			throws InterruptedException {
		logger.debug("testReceiveMessageWithMessageInQueueWithExtraResource");
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
		when(((ResourceManager)resourceManager).getResourcePool()).thenReturn(2);
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
		verify(sortRules,times(1)).sortByPrioritisedGroupIds(true);
		assertEquals(resource.getStatus(), Resource.BUSY);
		assertEquals(resource.getMsgId(), 1);

	}
	
	@Test
	public void testReceiveMessageWithMessageInQueueWithAResourcePool()
			throws InterruptedException {
		logger.debug("testReceiveMessageWithMessageInQueueWithAResourcePool");
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
		when(((ResourceManager)resourceManager).getResourcePool()).thenReturn(1);
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
		verify(sortRules, atLeastOnce()).sortByPrioritisedGroupIds(false);
		assertEquals(resource.getStatus(), Resource.BUSY);
		assertEquals(resource.getMsgId(), 1);

	}
	
	@Test
	public void testReceiveMessageWithForcePGSOffTrue()
			throws InterruptedException {
		logger.debug("testReceiveMessageWithForcePGSOffTrue");
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
		when(((ResourceManager)resourceManager).getResourcePool()).thenReturn(1);
		when(resourceManager.isResourceAvailable()).thenReturn(resource);
		when(messagingQueue.getCurrentMessageId()).thenReturn(1);
		when(messagingQueue.isHeadMsg(1)).thenReturn(true,false);
		receiver.setForcePGSOff(true);		
		receiver.receiver(message);
		Thread.sleep(1000);
		verify(groupCancellationReceiver).doesContain(anyInt());
		verify(messagingQueue).addToQueue((MessageDecorator)any());
		verify(messagingQueue, times(2)).isQueueEmpty();
		verify(messagingQueue, times(1)).getCurrentMessageId();
		verify(messagingQueue,times(2)).isHeadMsg(1);
		verify(sortRules, atLeastOnce()).sortByPrioritisedGroupIds(false);
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
		//wait for the child to finish
		Thread.sleep(1000);
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
