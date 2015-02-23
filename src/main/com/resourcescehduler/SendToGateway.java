package com.resourcescehduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.resourcescehduler.vo.IMessagingQueue;
import com.resourcescehduler.vo.Message;
import com.resourcescehduler.vo.MessageDecorator;

/**
 * @author NYalamanchili
 *
 */
public class SendToGateway implements Runnable, ISendToGateway {
	
	private static Log logger = LogFactory.getLog(SendToGateway.class);
	
	private IGateway gateway;
	private IResourceManager resourceManager;
	private IMessagingQueue messagingQueue;

	@SuppressWarnings("javadoc")
	@Override
	public void run() {
			MessageDecorator messageDecorator = (MessageDecorator)messagingQueue.consumeMessage();
			if(messageDecorator != null) {
				Message message = messageDecorator.getMessage();
				logger.debug("sending message to gateway with msg id= " +message.getMessageId());
				gateway.send(message);
				resourceManager.releaseResource(message.getMessageId());
				logger.debug("***exit run***");
			}
	}

	/**
	 * @return IGateway
	 */
	public IGateway getGateway() {
		return gateway;
	}

	/**
	 * @param gateway
	 */
	public void setGateway(IGateway gateway) {
		this.gateway = gateway;
	}

	/**
	 * @return IResourceManager
	 */
	public IResourceManager getResourceManager() {
		return resourceManager;
	}

	/**
	 * @param resourceManager
	 */
	public void setResourceManager(IResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	/**
	 * @return the messagingQueue
	 */
	public IMessagingQueue getMessagingQueue() {
		return messagingQueue;
	}

	/**
	 * @param messagingQueue the messagingQueue to set
	 */
	public void setMessagingQueue(IMessagingQueue messagingQueue) {
		this.messagingQueue = messagingQueue;
	}

}

