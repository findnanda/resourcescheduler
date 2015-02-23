package com.resoourcescehduler;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.util.Log4jConfigurer;

import com.resourcescehduler.IGateway;
import com.resourcescehduler.IResourceManager;
import com.resourcescehduler.ISendToGateway;
import com.resourcescehduler.SendToGateway;
import com.resourcescehduler.vo.IMessagingQueue;
import com.resourcescehduler.vo.Message;
import com.resourcescehduler.vo.MessageDecorator;
import com.resourcescehduler.vo.MessagingQueue;

/**
 * @author NYalamanchili
 *
 */
public class SendToGatewayTest {

	private static Log logger = LogFactory.getLog(SendToGatewayTest.class);
	private IGateway gateway;
	private IResourceManager resourceManager;
	private MessagingQueue messagingQueue;
	private SendToGateway sendToGateway;

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
		sendToGateway = new SendToGateway();
		gateway = Mockito.mock(IGateway.class);
		resourceManager = Mockito.mock(IResourceManager.class);
		messagingQueue = Mockito.mock(MessagingQueue.class);
		sendToGateway.setMessagingQueue(messagingQueue);
		sendToGateway.setResourceManager(resourceManager);
		sendToGateway.setGateway(gateway);
	}
	
	@Test
	public void testRunWithValidMessage() throws InterruptedException{
		logger.debug("testRunWithValidMessage");
		Message message1 = new Message();
		message1.setMessageId(1);
		message1.setGroupId(1);
		message1.setName("message " + 1);
		MessageDecorator msgDec1 = new MessageDecorator(message1);
		
		Message message2 = new Message();
		message2.setMessageId(2);
		message2.setGroupId(2);
		message2.setName("message " + 2);
		MessageDecorator msgDec2 = new MessageDecorator(message2);
		
		messagingQueue.setSharedQueue(new PriorityBlockingQueue<MessageDecorator>());
		
		messagingQueue.addToQueue(msgDec1);
		
		when(messagingQueue.consumeMessage()).thenReturn(msgDec1);
		
		Thread t = new Thread(sendToGateway);
		t.start();
		//while you runs a s suite it needs this
		Thread.sleep(1000);
		verify(gateway).send((Message) argThat(new MesssageEqualityArgumentMatcher(message1)));
		verify(resourceManager).releaseResource(message1.getMessageId());
	}
	
	@Test
	public void testRunWithNullMessage() throws InterruptedException{
		logger.debug("testRunWithNullMessage");
		MessageDecorator msgDec1 =null;
		messagingQueue.setSharedQueue(new PriorityBlockingQueue<MessageDecorator>());
		
		messagingQueue.addToQueue(msgDec1);
		when(messagingQueue.consumeMessage()).thenReturn(msgDec1);


		Thread t = new Thread(sendToGateway);
		t.start();
		verifyZeroInteractions(gateway);
		verifyZeroInteractions(resourceManager);
	}
	
	private class MesssageEqualityArgumentMatcher<T> extends ArgumentMatcher<T> {
	    T message;

	    public MesssageEqualityArgumentMatcher(T thisObject) {
	        this.message = thisObject;
	    }

	    @Override
	    public boolean matches(Object argument) {
	        return message.equals(argument);
	    }
	}
}
