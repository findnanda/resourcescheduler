package com.resourcescehduler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.resourcescehduler.vo.Message;
import com.resourcescehduler.vo.Resource;

/**
 * @author NYalamanchili
 * 
 */
public class MessageReceiver {

	private static Log logger = LogFactory.getLog(MessageReceiver.class);
	private IResourceManager resourceManager;
	private ISendToGateway sendToGateway;
	private IGroupCancellationReceiver groupCancellationReceiver;

	// This will receive messages from scheduler
	/**
	 * @param message
	 * @throws InterruptedException
	 */
	public void receiveMessage(Message message) throws InterruptedException {
		Resource resource = null;
		if (!groupCancellationReceiver.doesContain(message.getGroupId())) {
			sendToGateway.addToQueue(message);
			while (!sendToGateway.isQueueEmpty()) {
				// get a resource if availabale
				if ((resource = resourceManager.isResourceAvailable()) != null) {
					Thread thread = new Thread((Runnable) sendToGateway);
					logger.debug("receivedMessage, resource thread with resource id ="
							+ resource.getResourceId());
					resource.setStatus(Resource.BUSY);
					resource.setMsgId(message.getMessageId());
					logger.debug("receivedMessage, resource set to busy for "
							+ message.getName() + " resource Id="+resource.getResourceId());
					thread.start();
				}
			}
		}
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
	 * @return ISendToGateway
	 */
	public ISendToGateway getSendToGateway() {
		return sendToGateway;
	}

	/**
	 * @param sendToGateway
	 */
	public void setSendToGateway(ISendToGateway sendToGateway) {
		this.sendToGateway = sendToGateway;
	}

	/**
	 * @return the groupCancellationReceiver
	 */
	public IGroupCancellationReceiver getGroupCancellationReceiver() {
		return groupCancellationReceiver;
	}

	/**
	 * @param groupCancellationReceiver
	 *            the groupCancellationReceiver to set
	 */
	public void setGroupCancellationReceiver(
			IGroupCancellationReceiver groupCancellationReceiver) {
		this.groupCancellationReceiver = groupCancellationReceiver;
	}
}
