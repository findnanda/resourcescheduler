package com.resourcescehduler.sorting;

import java.io.Serializable;
import java.util.Comparator;

import com.resourcescehduler.vo.MessageDecorator;

/**
 * @author NYalamanchili
 *
 */
public interface ISortingStrategy extends Comparator<MessageDecorator>, Serializable{

	public int sort(MessageDecorator msg1, MessageDecorator msg2);
	
	/**
	 * By default sorting is enabled on the strategy. Set switch off to true will disable only the current sort, if the 
	 * setSortStrategy is set then it will ignore the current sort but still execute the apply after sort strategy. If any point
	 * the current sort needs to be enabled passing false to switchOffCurrentSort will re-enable current sort strategy.
	 * @param switchOff
	 */
	public void switchOffCurrentSort(boolean switchOff);

}
