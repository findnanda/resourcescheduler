package com.resourcescehduler.sorting;

import java.util.List;
import java.util.Map;

/**
 * @author NYalamanchili
 *
 */
public interface ISortingRules {
	/**
	 *  This method provides you the flexibility to combine and control the order of sort strategy. 
	 *  For example: 
	 *  sortStrategyOrder = new HashMap();
	 *  sortStrategyOrder.put(SortingRules.SORT_BY_PRIORITISED_GROUPID,2);
	 *  sortStrategyOrder.put(SortingRules.SORT_BY_PRIORITISED_GROUPID,4);
	 *  sortStrategyOrder.put(SortingRules.SORT_BY_PRIORITISED_GROUPID,10);
	 *  sortStrategyOrder.put(SortingRules.SORT_BY_GROUP,"ASC");
	 *  sortStrategyOrder.put(SortingRules.SORT_BY_FIFO,null);
	 * @param sortStrategyOrder
	 */
	public void sortingStrategyOrder(List<Map<String, Object>> sortStrategyOrder);
	
	/**
	 * Apply first in first out strategy
	 * @param enableSort = true 
	 */
	public void sortByFIFO(boolean enableSort);
	
	/**
	 * Sort elements by their group id in ascending order.
	 * @param enableSort = true 
	 */
	public void sortByGroupAsc(boolean enableSort);
	
	/**
	 * Sort elements by their group id in descending order.
	 * @param enableSort = true 
	 */
	public void sortBygroupDesc(boolean enableSort);
	
	/**
	 * Prioritize the elements by their groupId as they receive.
	 * @param enableSort = true 
	 */
	public void sortByPrioritisedGroupIds(boolean enableSort);

}
