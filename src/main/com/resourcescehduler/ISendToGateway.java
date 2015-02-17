package com.resourcescehduler;

import com.resourcescehduler.vo.Message;

/**
 * @author NYalamanchili
 *
 */
public interface ISendToGateway {

	/**
	 * @return boolean
	 */
	public abstract boolean isQueueEmpty();

	/**
	 * @param message
	 * @throws InterruptedException
	 */
	public abstract void addToQueue(Message message) throws InterruptedException;

	public abstract boolean isHeadMsg(Message message);

}
