package com.resourcescehduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.resourcescehduler.sorting.ISortingRules;
import com.resourcescehduler.vo.IMessagingQueue;
import com.resourcescehduler.vo.Message;
import com.resourcescehduler.vo.MessageDecorator;
import com.resourcescehduler.vo.Resource;

/**
 * This class will receive messages from the scheduler which it adds them to the queue to pass it to the gateway for processing.
 * Messages to the queue are added and by default it's first in first out sort.
 * @author NYalamanchili
 * 
 */
public class MessageReceiver {

	private static Log logger = LogFactory.getLog(MessageReceiver.class);
	private IResourceManager resourceManager;
	private ISendToGateway sendToGateway;
	private IMessagingQueue messagingQueue;
	private IGroupCancellationReceiver groupCancellationReceiver;
	private ISortingRules sortRules;
	private MessageProcessor holder;
	private Thread thread = null;
	private Map<Integer,Long> groupPriortizationMap;
	// Need not be static as this is singleton class
	private final AtomicLong groupPriorityGenerator;
	private boolean forcePGSOff;
	/**
	 * 
	 */
	public MessageReceiver(){
		holder = new MessageProcessor();
		groupPriorityGenerator = new AtomicLong(0);
	}
	/**
	 * This will receive messages from scheduler, prioritise them if needed and calls child thread to process them.
	 * @param message
	 * @throws InterruptedException
	 */
	public void receiver(final Message message) throws InterruptedException {
		final MessageDecorator msgDecorator = new MessageDecorator(message);
		if (!groupCancellationReceiver.doesContain(message.getGroupId())) {
			logger.debug("Received message with msgId : "+message.getMessageId());
			if(!isForcePGSOff()){
				prioritiseGroup(msgDecorator);
			}
			messagingQueue.addToQueue(msgDecorator);
			if (thread == null || !thread.isAlive()) {				
				thread = new Thread(holder);
				thread.start();
			}
		} else {
			logger.debug("This group id has been cancelled. "+message.getGroupId());
		}
	}

// If resource size is greater then 1, then only prioritise the messages as they receive.
	private void prioritiseGroup(MessageDecorator msgDecorator){
		if(resourceManager.getResourcePool()>1){
			sortRules.sortByPrioritisedGroupIds(true);
			long groupPriority;
			if(!groupPriortizationMap.containsKey(msgDecorator.getMessage().getGroupId())){
				groupPriority = groupPriorityGenerator.getAndIncrement();
				groupPriortizationMap.put(msgDecorator.getMessage().getGroupId(), groupPriority);
			} else {
				groupPriority = groupPriortizationMap.get(msgDecorator.getMessage().getGroupId());
			}
			msgDecorator.setGroupPriority(groupPriority);
		} else {
			disableGroupIdPrioritisation();
		}
	}
	
	private void disableGroupIdPrioritisation(){
		sortRules.sortByPrioritisedGroupIds(false);
	}
	
	/**
	 * This will  process the message and send it to the gateway processor 
	 * and aslo request for resource allocation until the queue is empty.
	 *
	 */
	class MessageProcessor implements Runnable {

		@Override
		public void run() {
			Resource resource = null;
			while (!messagingQueue.isQueueEmpty()) {
				// get a resource if availabale
				if ((resource = resourceManager.isResourceAvailable()) != null) {
					Thread thread = new Thread((Runnable) sendToGateway);
					logger.debug("receivedMessage, resource thread with resource id ="
							+ resource.getResourceId());
					resource.setStatus(Resource.BUSY);
					int msgId = messagingQueue.getCurrentMessageId();

					resource.setMsgId(msgId);
					thread.start();
					while (messagingQueue.isHeadMsg(msgId)) {
						// Wake up and allocate new resource only if the
						// received message is
						// different from the head message.
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							logger.debug("exception: ", e.getCause());
						}
					}
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

	/**
	 * @return messagingQueue
	 */
	public IMessagingQueue getMessagingQueue() {
		return messagingQueue;
	}

	/**
	 * @param messagingQueue
	 */
	public void setMessagingQueue(IMessagingQueue messagingQueue) {
		this.messagingQueue = messagingQueue;
	}

	/**
	 * @return the sortRules
	 */
	public ISortingRules getSortRules() {
		return sortRules;
	}

	/**
	 * @param sortRules the sortRules to set
	 */
	public void setSortRules(ISortingRules sortRules) {
		this.sortRules = sortRules;
	}
	/**
	 * Return true if forcePGSOff is turned on.
	 * @return the forcePGSOff
	 */
	public boolean isForcePGSOff() {
		return forcePGSOff;
	}
	/**
	 * This allows to force switch off the groupids prioritisation.
	 * @param forcePGSOff the forcePGSOff to set
	 */
	public void setForcePGSOff(boolean forcePGSOff) {
		this.forcePGSOff = forcePGSOff;
		disableGroupIdPrioritisation();
	}
	/**
	 * @return the groupPriortizationMap
	 */
	public Map<Integer,Long> getGroupPriortizationMap() {
		return groupPriortizationMap;
	}
	/**
	 * @param groupPriortizationMap the groupPriortizationMap to set
	 */
	public void setGroupPriortizationMap(Map<Integer,Long> groupPriortizationMap) {
		this.groupPriortizationMap = groupPriortizationMap;
	}

}
