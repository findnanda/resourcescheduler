package com.resourcescehduler;

import com.resourcescehduler.vo.Message;

/**
 * @author NYalamanchili
 *
 */
public interface IGateway {

	/**
	 * @param message
	 */
	public abstract void send(Message message);

}
