package com.resourcescehduler.vo;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author NYalamanchili
 *
 */
public class MessagingQueue implements IMessagingQueue {
	
	private static Log logger = LogFactory.getLog(MessagingQueue.class);

	private BlockingQueue<MessageDecorator> sharedQueue;

	/* (non-Javadoc)
	 * @see com.resourcescehduler.IMessagingQueue#addToQueue(com.resourcescehduler.vo.Message)
	 */
	@Override
	public void addToQueue(MessageDecorator messageDecorator) throws InterruptedException{
		//logger.debug("COMPARATOR: "+((PriorityBlockingQueue)sharedQueue).comparator().getClass());
		this.sharedQueue.put(messageDecorator);
	}
	
	/* (non-Javadoc)
	 * @see com.resourcescehduler.IMessagingQueue#isQueueEmpty()
	 */
	@Override
	public boolean isQueueEmpty(){
		return this.sharedQueue.isEmpty();
	}

	/* (non-Javadoc)
	 * @see com.resourcescehduler.IMessagingQueue#isHeadMsg(com.resourcescehduler.vo.Message)
	 */
	@Override
	public boolean isHeadMsg(MessageDecorator messageDecorator){
		MessageDecorator msgDecorator = this.sharedQueue.peek();
		boolean retVal = false;
		if(msgDecorator != null && messageDecorator.getMessage().getMessageId() == msgDecorator.getMessage().getMessageId()){
			retVal = true;
			logger.debug("message to be picked up. ");
		}
		return retVal;
	}
	
	@Override
	public boolean isHeadMsg(int messageId){
		MessageDecorator msgDecorator = this.sharedQueue.peek();
		boolean retVal = false;
		if(msgDecorator != null && messageId == msgDecorator.getMessage().getMessageId()){
			retVal = true;
			logger.debug("message to be picked up. ");
		}
		return retVal;
	}
	
	
	/* (non-Javadoc)
	 * @see com.resourcescehduler.IMessagingQueue#consumeMessage()
	 */
	@Override
	public Object consumeMessage(){
		Object obj = null;
		try {
			logger.debug("consumeMessage");
			obj = this.sharedQueue.take();
		} catch (InterruptedException ie) {
			logger.error("Exception:"+ie);
		}
		return obj;
	}
	
	@Override
	public int getCurrentMessageId(){
		MessageDecorator msgDec =  this.sharedQueue.peek();
		return msgDec.getMessage().getMessageId();
	}
	/**
	 * @return BlockingQueue<Message>
	 */
	public BlockingQueue<MessageDecorator> getSharedQueue() {
		return sharedQueue;
	}

	/**
	 * @param sharedQueue
	 */
	public void setSharedQueue(BlockingQueue<MessageDecorator> sharedQueue) {
		this.sharedQueue = sharedQueue;
	}
}
