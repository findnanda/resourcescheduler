package com.resourcescehduler.sorting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.resourcescehduler.vo.MessageDecorator;

/**
 * @author NYalamanchili This class will enable you to prioritise the messages from a particular group
 * that is set in sortByGroupId..
 * 
 */
public class PriortiseSpecificGroupSortingStrategy implements ISortingStrategy {
	
	private static Log logger = LogFactory.getLog(PriortiseSpecificGroupSortingStrategy.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 7956101753455154924L;
	
	private int sortByGroupId;
	
	private ISortingStrategy sortStrategy;

	private boolean switchOff=true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(MessageDecorator msg1, MessageDecorator msg2) {
		return sortPriortisedByGroupId(msg1, msg2);
	}

	/* (non-Javadoc)
	 * @see com.resourcescehduler.ISortingStrategy#sort(com.resourcescehduler.MessageDecorator, com.resourcescehduler.MessageDecorator)
	 */
	@Override
	public int sort(MessageDecorator msg1, MessageDecorator msg2) {
		return sortPriortisedByGroupId(msg1, msg2);
	}
	
	private int sortPriortisedByGroupId(MessageDecorator msg1, MessageDecorator msg2){
		int retVal = 0;
		if(sortByGroupId != 0 && !switchOff){
			logger.debug("Applying PriortisedSpecificGroupSortingStrategy.");
			if(msg1.getMessage().getGroupId() == sortByGroupId){
				retVal = -1;
			} else {
				retVal = 1;
			}
		} 
//		else {
//			logger.warn("Applying after sort strategy. Priortised groupid sorting is not applied. groupId="+sortByGroupId+" switchOff="+switchOff);
//			retVal = applyAfter(msg1, msg2);
//		}
		return retVal;
	}
	
	private int applyAfter(MessageDecorator msg1, MessageDecorator msg2){
		if(sortStrategy != null){
			return sortStrategy.sort(msg1, msg2);
		}
		return 1;
	}

	/**
	 * @return the sortStrategy
	 */
	public ISortingStrategy getSortStrategy() {
		return sortStrategy;
	}

	/**
	 * @param sortStrategy the sortStrategy to set
	 */
	public void setSortStrategy(ISortingStrategy sortStrategy) {
		this.sortStrategy = sortStrategy;
	}

	public int getGroupId() {
		return sortByGroupId;
	}

	public void setGroupId(int groupId) {
		this.sortByGroupId = groupId;
	}

	@Override
	public void switchOffCurrentSort(boolean switchOff) {
		this.switchOff = switchOff;
		
	}
}
