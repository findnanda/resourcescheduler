package com.resourcescehduler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.resourcescehduler.vo.Message;

/**
 * @author NYalamanchili
 *
 */
public class Gateway implements IGateway {
	private static Log logger = LogFactory.getLog(Gateway.class);

	/* (non-Javadoc)
	 * @see com.resourcescehduler.IGateway#send(com.resourcescehduler.vo.Message)
	 */
	@Override
	public void send(Message message) {
		try {
			logger.debug("Gateway processing message with mesageId="
					+ message.getMessageId());
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		message.setCompleted(true);
		logger.debug("Gateway processing done. ");
	}
}
