package com.resourcescehduler.sorting;

import java.util.Comparator;
import java.util.List;

import com.resourcescehduler.vo.MessageDecorator;

/**
 * MessageComparators will enable you to apply multiple sorting strategies together.
 * @author NYalamanchili
 *
 */
public class MessageComparators implements Comparator<MessageDecorator> {

	private List<Comparator<MessageDecorator>> comparators;

    public MessageComparators() {
    }

    @Override
    public int compare(MessageDecorator o1, MessageDecorator o2) {
        int compareResult = 0;
        for(Comparator<MessageDecorator> comparator : getComparators()) {
            if(comparator.compare(o1, o2)!=0) {
                return comparator.compare(o1, o2);
            }
        }
        return compareResult;
    }

	/**
	 * @return the comparators
	 */
	public List<Comparator<MessageDecorator>> getComparators() {
		return comparators;
	}

	/**
	 * @param comparators the comparators to set
	 */
	public void setComparators(List<Comparator<MessageDecorator>> comparators) {
		this.comparators = comparators;
	}


}
