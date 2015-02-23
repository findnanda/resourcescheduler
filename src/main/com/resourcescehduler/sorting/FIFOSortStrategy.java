package com.resourcescehduler.sorting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.resourcescehduler.vo.MessageDecorator;

/**
 * This should always be the last sorting policy to be applied as it first in first out and it will break all the ties.
 * @author NYalamanchili
 * 
 */
public class FIFOSortStrategy implements ISortingStrategy {

	private static Log logger = LogFactory.getLog(FIFOSortStrategy.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 547791581472913733L;

	private boolean switchOff;

	@Override
	public int compare(MessageDecorator msg1, MessageDecorator msg2) {
		return sort(msg1, msg2);
	}

	@Override
	public int sort(MessageDecorator msg1, MessageDecorator msg2) {
		return fifoSort(msg1, msg2);
	}

	private int fifoSort(MessageDecorator msg1, MessageDecorator msg2) {
		int retVal=0;
		if (!switchOff) {
			logger.debug("Applying FIFO.");
			if (msg1.getSeqNum() > msg2.getSeqNum()) {
//				logger.debug("FIFO 1 [msg1.getSeqNum()="+msg1.getSeqNum()+" with msg1.messageId= "+msg1.getMessage().getMessageId()+"] " +
//						"msg2.getSeqNum()="+msg2.getSeqNum()+" with msg2.messageId= "+msg2.getMessage().getMessageId());
				retVal = 1;
			} else {
//				logger.debug("FIFO -1 [msg1.getSeqNum()="+msg1.getSeqNum()+" with msg1.messageId= "+msg1.getMessage().getMessageId()+"] " +
//						"msg2.getSeqNum()="+msg2.getSeqNum()+" with msg2.messageId= "+msg2.getMessage().getMessageId());
				retVal = -1;
			}
		}
		return retVal;
	}

	@Override
	public void switchOffCurrentSort(boolean switchOff) {
		this.switchOff = switchOff;

	}
}
