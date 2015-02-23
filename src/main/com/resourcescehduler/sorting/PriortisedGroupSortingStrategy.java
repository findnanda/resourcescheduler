package com.resourcescehduler.sorting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.resourcescehduler.vo.MessageDecorator;

/**
 * @author NYalamanchili This class will sort the messages based on the received groupids.
 * 
 */
public class PriortisedGroupSortingStrategy implements ISortingStrategy {
	
	private static Log logger = LogFactory.getLog(PriortisedGroupSortingStrategy.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 7956101753455154924L;
	
	private ISortingStrategy sortStrategy;

	private boolean switchOff=true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(MessageDecorator msg1, MessageDecorator msg2) {
		if(!switchOff){
			return sortPriortisedByGroupId(msg1, msg2);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see com.resourcescehduler.ISortingStrategy#sort(com.resourcescehduler.MessageDecorator, com.resourcescehduler.MessageDecorator)
	 */
	@Override
	public int sort(MessageDecorator msg1, MessageDecorator msg2) {
		if(!switchOff){
			return sortPriortisedByGroupId(msg1, msg2);
		} else {
			return 0;
		}
	}
	
	private int sortPriortisedByGroupId(MessageDecorator msg1, MessageDecorator msg2){
		logger.debug("Applying PriortisedGroupSortingStrategy");
		return Long.signum(msg1.getGroupPriority() - msg2.getGroupPriority());
	}
	
	/**
	 * If any after sort strategy to apply to break the tie.
	 * @param msg1
	 * @param msg2
	 * @return signum value
	 */
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

	@Override
	public void switchOffCurrentSort(boolean switchOff) {
		this.switchOff = switchOff;
		
	}
}
