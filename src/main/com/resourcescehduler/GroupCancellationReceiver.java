package com.resourcescehduler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author NYalamanchili
 * 
 */
public class GroupCancellationReceiver implements IGroupCancellationReceiver {
	private static Log logger = LogFactory
			.getLog(GroupCancellationReceiver.class);

	private ReadWriteLock globalLock;
	private Lock readLock;
	private Lock writeLock;
	private Set<Integer> cancelGroupSet;

	/**
	 * 
	 */
	public GroupCancellationReceiver() {
		cancelGroupSet = new HashSet<Integer>();
		globalLock = new ReentrantReadWriteLock();
		readLock = globalLock.readLock();
		writeLock = globalLock.writeLock();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.resourcescehduler.IGroupCancellationReceiver#cancelGroup(int)
	 */
	@Override
	public void cancelGroup(int groupId) {
		logger.debug("received cancellation request for groupid=" + groupId);
		writeLock.lock();
		try {
			cancelGroupSet.add(groupId);
			logger.debug("added group id to the cancel set"+groupId);
		} finally {
			writeLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.resourcescehduler.IGroupCancellationReceiver#doesContain(int)
	 */
	@Override
	public boolean doesContain(int groupId) {
		readLock.lock();
		try {
			if (cancelGroupSet.contains(groupId)) {
				logger.debug("Cancelled groupId=" + groupId);
				return true;
			} 
		} finally {
			readLock.unlock();
		}
		return false;
	}

}
