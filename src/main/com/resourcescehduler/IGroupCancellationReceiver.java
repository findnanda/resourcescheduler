package com.resourcescehduler;

/**
 * @author NYalamanchili
 *
 */
public interface IGroupCancellationReceiver {

	/**
	 * @param groupId
	 * @return boolean
	 */
	public abstract boolean doesContain(int groupId);

	/**
	 * @param groupId
	 */
	public abstract void cancelGroup(int groupId);

}
