package com.resourcescehduler;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.resourcescehduler.vo.Message;

/**
 * @author NYalamanchili
 *
 */
public class SendToGateway implements Runnable, ISendToGateway {
	
	private static Log logger = LogFactory.getLog(SendToGateway.class);
	
	private BlockingQueue<Message> sharedQueue;
	private IGateway gateway;
	private IResourceManager resourceManager;

	@SuppressWarnings("javadoc")
	@Override
	public void run() {
		try {
			Message message = (Message)this.sharedQueue.take();
			logger.debug("sending message to gateway with msg id= " +message.getMessageId());
			gateway.send(message);
			resourceManager.releaseResource(message.getMessageId());
			logger.debug("***exit run***");
		} catch (InterruptedException e) {
			logger.error("exception",e);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.resourcescehduler.ISendToGateway#addToQueue(com.resourcescehduler.vo.Message)
	 */
	@Override
	public void addToQueue(Message message) throws InterruptedException{
		this.sharedQueue.put(message);
	}
	
	/* (non-Javadoc)
	 * @see com.resourcescehduler.ISendToGateway#isQueueEmpty()
	 */
	@Override
	public boolean isQueueEmpty(){
		return this.sharedQueue.isEmpty();
	}

	/**
	 * @return BlockingQueue<Message>
	 */
	public BlockingQueue<Message> getSharedQueue() {
		return sharedQueue;
	}

	/**
	 * @param sharedQueue
	 */
	public void setSharedQueue(BlockingQueue<Message> sharedQueue) {
		this.sharedQueue = sharedQueue;
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

}

