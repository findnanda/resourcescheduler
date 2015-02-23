package com.resourcescehduler.vo;

import java.util.concurrent.atomic.AtomicLong;


public class MessageDecorator {

	static final AtomicLong seq = new AtomicLong(0);
	private Message message;
	private long seqNum;
	private long groupPriority;
	
	public MessageDecorator(Message message){
		this.setMessage(message);
		seqNum = seq.getAndIncrement();
	}


	public long getSeqNum() {
		return seqNum;
	}


	public void setSeqNum(long seqNum) {
		this.seqNum = seqNum;
	}


	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(Message message) {
		this.message = message;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + (int) (seqNum ^ (seqNum >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageDecorator other = (MessageDecorator) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (seqNum != other.seqNum)
			return false;
		return true;
	}


	/**
	 * @return the groupPriority
	 */
	public long getGroupPriority() {
		return groupPriority;
	}


	/**
	 * @param groupPriority the groupPriority to set
	 */
	public void setGroupPriority(long groupPriority) {
		this.groupPriority = groupPriority;
	}

}
