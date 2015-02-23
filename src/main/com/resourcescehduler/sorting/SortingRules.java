package com.resourcescehduler.sorting;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author NYalamanchili
 *
 */
public class SortingRules implements ISortingRules {

	private static final int SORT_BY_PRIORITISED_GROUPID = 3;
	private static final int SORT_BY_GROUP = 2;
	private static final int SORT_BY_LIFO = 1;
	
	private static Log logger = LogFactory.getLog(SortingRules.class);

	private ISortingStrategy prioritisedGroupSortStrategy;
	private ISortingStrategy groupSortingStrategy;
	private ISortingStrategy fifoSortStrategy;
	
	@Override
	public void sortingStrategyOrder(List<Map<String, Object>> sortStrategyOrder) {
		//TODO
		
	}

	@Override
	public void sortByFIFO(boolean enableSort) {
		fifoSortStrategy.switchOffCurrentSort(!enableSort);
	}

	@Override
	public void sortByGroupAsc(boolean enableSort) {
		groupSortingStrategy.switchOffCurrentSort(!enableSort);
		((GroupSortingStrategy)groupSortingStrategy).setSortType(GroupSortingStrategy.ASCENDING);
		
	}

	@Override
	public void sortBygroupDesc(boolean enableSort) {
		groupSortingStrategy.switchOffCurrentSort(!enableSort);
		((GroupSortingStrategy)groupSortingStrategy).setSortType(GroupSortingStrategy.DESCENDING);
		
	}

	@Override
	public void sortByPrioritisedGroupIds(boolean enableSort) {
		((PriortisedGroupSortingStrategy)prioritisedGroupSortStrategy).switchOffCurrentSort(!enableSort);

	}

	/**
	 * @return the prioritisedGroupSortStrategy
	 */
	public ISortingStrategy getPrioritisedGroupSortStrategy() {
		return prioritisedGroupSortStrategy;
	}

	/**
	 * @param prioritisedGroupSortStrategy the prioritisedGroupSortStrategy to set
	 */
	public void setPrioritisedGroupSortStrategy(
			ISortingStrategy prioritisedGroupSortStrategy) {
		this.prioritisedGroupSortStrategy = prioritisedGroupSortStrategy;
	}
	
	/**
	 * @return the groupSortingStrategy
	 */
	public ISortingStrategy getGroupSortingStrategy() {
		return groupSortingStrategy;
	}

	/**
	 * @param groupSortingStrategy the groupSortingStrategy to set
	 */
	public void setGroupSortingStrategy(ISortingStrategy groupSortingStrategy) {
		this.groupSortingStrategy = groupSortingStrategy;
	}

	/**
	 * @return the fifoSortStrategy
	 */
	public ISortingStrategy getFifoSortStrategy() {
		return fifoSortStrategy;
	}

	/**
	 * @param fifoSortStrategy the fifoSortStrategy to set
	 */
	public void setFifoSortStrategy(ISortingStrategy fifoSortStrategy) {
		this.fifoSortStrategy = fifoSortStrategy;
	}

}
