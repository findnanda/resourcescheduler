package com.resourcescehduler.vo;


/**
 * @author NYalamanchili
 *
 */
public interface IMessagingQueue {

	/**
	 * It will check if the passed in message is the message going to be consumed.
	 * @param messagedecorator 
	 * @return true if its a head message.
	 */
	public abstract boolean isHeadMsg(MessageDecorator messagedecorator);

	/**
	 * Checks if the queue is empty
	 * @return true if its empty.
	 */
	public abstract boolean isQueueEmpty();

	/**
	 * This method will allow you add messages to the priority blocking queue.
	 * @param messagedecorator 
	 * @throws InterruptedException
	 */
	public abstract void addToQueue(MessageDecorator messagedecorator) throws InterruptedException;

	/**
	 * Call this method to consume messages of the queue.
	 * @return generic object that could be casted. 
	 */
	public abstract Object consumeMessage();

	/**
	 * Retrives the currentMsgId to be consumed by the SendToGateway.
	 * @return messageId
	 */
	public abstract int getCurrentMessageId();

	/**
	 * It will check if the passed in message is the message going to be consumed.
	 * @param messageId 
	 * @return true if its a head message.
	 */
	public abstract boolean isHeadMsg(int messageId);

}
