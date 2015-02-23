package com.resoourcescehduler.sorting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.Log4jConfigurer;

import com.resourcescehduler.sorting.GroupSortingStrategy;
import com.resourcescehduler.sorting.ISortingStrategy;
import com.resourcescehduler.vo.Message;
import com.resourcescehduler.vo.MessageDecorator;

public class GroupSortingStrategyTest {

	private static Log logger = LogFactory
			.getLog(GroupSortingStrategyTest.class);
	private ISortingStrategy fifoSortingStrategy;
	private GroupSortingStrategy groupSortingStrategy;

	@Before
	public void setUp() {
		try {
			Log4jConfigurer.initLogging("classpath:log4j.xml");
		} catch (FileNotFoundException ex) {
			System.err.println("Cannot Initialize log4j");
		}

		logger.info("*********** In Setup ******************");
		fifoSortingStrategy = Mockito.mock(ISortingStrategy.class);
		groupSortingStrategy = new GroupSortingStrategy();
		groupSortingStrategy.setSortStrategy(fifoSortingStrategy);
	}

	@After
	public void tearDown() {
		logger.info("*********** In tear down ******************");
	}

	@Test
	public void testGroupSortingStrategyWithDefaultSortOrder() {
		logger.info("testGroupSortingStrategyWithDefaultSortOrder");
		Message message = null;
		MessageDecorator msgDecorator = null;
		List<MessageDecorator> list = new ArrayList<MessageDecorator>();
		for (int index = 10; index >= 1; index--) {
			message = new Message();
			message.setMessageId(index);
			message.setName("MSG" + index);
			if (index == 5 || index == 6) {
				message.setGroupId(20);
			} else if (index >= 3 && index <= 8) {
				message.setGroupId(15);
			} else {
				message.setGroupId(index);
			}
			msgDecorator = new MessageDecorator(message);
			list.add(msgDecorator);
		}
		groupSortingStrategy.switchOffCurrentSort(false);
		Collections.sort(list, groupSortingStrategy);
		assertEquals(1, list.get(0).getMessage().getGroupId());

		for (MessageDecorator messageDecorator : list) {
			logger.debug("GroupId= "
					+ messageDecorator.getMessage().getGroupId());
		}
	}
	
	@Test
	public void testGroupSortingStrategyWithPriorityQueueDefaultSortOrder() throws InterruptedException {
		logger.info("testGroupSortingStrategyWithPriorityQueueDefaultSortOrder");
		Message message = null;
		MessageDecorator msgDecorator = null;
		groupSortingStrategy.setSortStrategy(null);
		groupSortingStrategy.setSortType(GroupSortingStrategy.ASCENDING);
		PriorityBlockingQueue<MessageDecorator> pbq = new PriorityBlockingQueue<MessageDecorator>(100, groupSortingStrategy);
		for (int index = 10; index >= 1; index--) {
			message = new Message();
			message.setMessageId(index);
			message.setName("MSG" + index);
			if (index == 5 || index == 6) {
				message.setGroupId(20);
			} else if (index >= 3 && index <= 8) {
				message.setGroupId(15);
			} else {
				message.setGroupId(index);
			}
			msgDecorator = new MessageDecorator(message);
			pbq.add(msgDecorator);
		}

		MessageDecorator messageDecorator;
		while (!pbq.isEmpty()) {
			messageDecorator = (MessageDecorator)pbq.take();
			logger.debug("GroupId= "
					+ messageDecorator.getMessage().getGroupId());
		}
	}

	@Test
	public void testGroupSortingStrategyWithASC() {
		logger.info("testGroupSortingStrategyWithASC");
		Message message = null;
		MessageDecorator msgDecorator = null;
		List<MessageDecorator> list = new ArrayList<MessageDecorator>();
		for (int index = 10; index >= 1; index--) {
			message = new Message();
			message.setMessageId(index);
			message.setName("MSG" + index);
			if (index == 5 || index == 6) {
				message.setGroupId(20);
			} else if (index >= 3 && index <= 8) {
				message.setGroupId(15);
			} else {
				message.setGroupId(index);
			}
			msgDecorator = new MessageDecorator(message);
			list.add(msgDecorator);
		}
		groupSortingStrategy.setSortType(GroupSortingStrategy.ASCENDING);
		groupSortingStrategy.switchOffCurrentSort(false);
		Collections.sort(list, groupSortingStrategy);
		assertEquals(1, list.get(0).getMessage().getGroupId());

		for (MessageDecorator messageDecorator : list) {
			logger.debug("GroupId= "
					+ messageDecorator.getMessage().getGroupId());
		}
	}

	@Test
	public void testGroupSortingStrategyWithDESC() {
		logger.info("testGroupSortingStrategyWithDESC");
		Message message = null;
		MessageDecorator msgDecorator = null;
		List<MessageDecorator> list = new ArrayList<MessageDecorator>();
		for (int index = 10; index >= 1; index--) {
			message = new Message();
			message.setMessageId(index);
			message.setName("MSG" + index);
			if (index == 5 || index == 6) {
				message.setGroupId(20);
			} else if (index >= 3 && index <= 8) {
				message.setGroupId(15);
			} else {
				message.setGroupId(index);
			}
			msgDecorator = new MessageDecorator(message);
			list.add(msgDecorator);
		}
		groupSortingStrategy.setSortType(GroupSortingStrategy.DESCENDING);
		groupSortingStrategy.switchOffCurrentSort(false);
		Collections.sort(list, groupSortingStrategy);
		assertEquals(20, list.get(0).getMessage().getGroupId());

		for (MessageDecorator messageDecorator : list) {
			logger.debug("GroupId= "
					+ messageDecorator.getMessage().getGroupId());
		}
	}

	@Test
	public void testGroupSortingStrategyWithNullMessages() {
		logger.info("testGroupSortingStrategyWithNullMessages");
		Message message = null;
		MessageDecorator msgDecorator1 = null;
		MessageDecorator msgDecorator2 = null;
		List<MessageDecorator> list = new ArrayList<MessageDecorator>();
		list.add(msgDecorator1);
		list.add(msgDecorator2);
		groupSortingStrategy.setSortType(GroupSortingStrategy.DESCENDING);
		Collections.sort(list, groupSortingStrategy);
		assertNull(list.get(0));
	}

	@Test
	public void testGroupSortingStrategyWithNullMgs1() {
		logger.info("testGroupSortingStrategyWithNullMgs1");
		Message message = null;

		message = new Message();
		message.setMessageId(20);
		message.setName("MSG" + 20);
		message.setGroupId(20);
		MessageDecorator msgDecorator1 = null;
		MessageDecorator msgDecorator2 = null;
		msgDecorator2 = new MessageDecorator(message);
		List<MessageDecorator> list = new ArrayList<MessageDecorator>();
		list.add(msgDecorator1);
		list.add(msgDecorator2);
		groupSortingStrategy.setSortType(GroupSortingStrategy.ASCENDING);
		Collections.sort(list, groupSortingStrategy);
		// This shows the order has not changed
		assertNull(list.get(0));
		assertNotNull(list.get(1).getMessage());
	}
	
	@Test
	public void testGroupSortingStrategySwitchOff() {
		logger.info("testGroupSortingStrategySwitchOff");
		Message message = null;
		MessageDecorator msgDecorator = null;
		List<MessageDecorator> list = new ArrayList<MessageDecorator>();
		for (int index = 10; index >= 1; index--) {
			message = new Message();
			message.setMessageId(index);
			message.setName("MSG" + index);
			if (index == 5 || index == 6) {
				message.setGroupId(20);
			} else if (index >= 3 && index <= 8) {
				message.setGroupId(15);
			} else {
				message.setGroupId(index);
			}
			msgDecorator = new MessageDecorator(message);
			list.add(msgDecorator);
		}
		groupSortingStrategy.setSortType(GroupSortingStrategy.ASCENDING);
		groupSortingStrategy.switchOffCurrentSort(true);
		Collections.sort(list, groupSortingStrategy);
		assertEquals(10,list.get(0).getMessage().getGroupId());

		for (MessageDecorator messageDecorator : list) {
			logger.debug("GroupId= "
					+ messageDecorator.getMessage().getGroupId());
		}
	}
}
