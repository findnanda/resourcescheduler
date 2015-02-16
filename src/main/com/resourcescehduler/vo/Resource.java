package com.resourcescehduler.vo;


/**
 * @author NYalamanchili
 *
 */
public class Resource {

	public static final int BUSY =2;
	public static final int PREPARE =1;
	public static final int FREE=0;
	
	private int status;
	private int msgId;
	private int resourceId;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public void resetResource(){
		setStatus(0);
	}
	
	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	/**
	 * @return the resourceId
	 */
	public int getResourceId() {
		return resourceId;
	}

	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
}
