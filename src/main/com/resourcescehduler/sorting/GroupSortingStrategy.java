package com.resourcescehduler.sorting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.resourcescehduler.vo.Message;
import com.resourcescehduler.vo.MessageDecorator;

/**
 * This will sort the messages by either ascending or descending groupids. By default it is set to ascending.
 * @author NYalamanchili
 * 
 */
public class GroupSortingStrategy implements ISortingStrategy {

	private static Log logger = LogFactory.getLog(GroupSortingStrategy.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -4699389918988886611L;

	public static final String ASCENDING = "ASC";

	public static final String DESCENDING = "DESC";

	// by default set to ascending
	private String sortType = ASCENDING;

	private ISortingStrategy sortStrategy;

	private boolean switchOff=true;

	@Override
	public int compare(MessageDecorator msg1, MessageDecorator msg2) {
		return sortByGroup(msg1, msg2);
	}

	@Override
	public int sort(MessageDecorator msg1, MessageDecorator msg2) {
		return sortByGroup(msg1, msg2);
	}

	private int sortByGroup(MessageDecorator msg1, MessageDecorator msg2) {
		int retVal = 0;
		if (msg1 != null && msg2 != null) {
			Message m1 = msg1.getMessage();
			Message m2 = msg2.getMessage();
			if (!switchOff) {
				logger.debug("Applying GroupSortingStrategy.");
				if (m1.getGroupId() > m2.getGroupId()) {
					return sortType.equals(ASCENDING) ? 1 : -1;
				} else if (m1.getGroupId() == m2.getGroupId()) {
					//applyAfter(msg1, msg2);
					return 0;
				} else {
					return sortType.equals(ASCENDING) ? -1 : 1;
				}
			} 
//			else {
//				logger.warn("Group Sorting strategy is switched off. Applying the apply after sorting strategy if present.");
//				applyAfter(msg1, msg2);
//			}
		}
		return retVal;
	}

	/**
	 * Any after sort strategy to be applied to break the tie.
	 */
	private int applyAfter(MessageDecorator msg1, MessageDecorator msg2) {
		if (sortStrategy != null) {
			return sortStrategy.sort(msg1, msg2);
		}
		return 0;
	}

	/**
	 * @return the sortType
	 */
	public String getSortType() {
		return sortType;
	}

	/**
	 * @param sortType
	 *            the sortType to set
	 */
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public ISortingStrategy getSortStrategy() {
		return sortStrategy;
	}

	public void setSortStrategy(ISortingStrategy sortStrategy) {
		this.sortStrategy = sortStrategy;
	}

	@Override
	public void switchOffCurrentSort(boolean switchOff) {
		this.switchOff = switchOff;

	}

}
