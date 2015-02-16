package com.resourcescehduler.vo;


/**
 * @author NYalamanchili
 *
 */
public class Message {

	private String name;
	private int messageId;
	private int groupId;
	private boolean completed;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return completed;
	}
	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
		
	public void completed(){
		setCompleted(true);
	}
}
