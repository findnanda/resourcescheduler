package com.resoourcescehduler.sorting;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Log4jConfigurer;

import com.resourcescehduler.sorting.PriortisedGroupSortingStrategy;
import com.resourcescehduler.vo.Message;
import com.resourcescehduler.vo.MessageDecorator;

/**
 * @author NYalamanchili
 * 
 */
public class PriortisedGroupSortingStrategyTest {

	private static Log logger = LogFactory
			.getLog(PriortisedGroupSortingStrategyTest.class);
	private PriortisedGroupSortingStrategy priortisedGroupSortingStrategy;

	@Before
	public void setUp() {
		try {
			Log4jConfigurer.initLogging("classpath:log4j.xml");
		} catch (FileNotFoundException ex) {
			System.err.println("Cannot Initialize log4j");
		}

		logger.info("*********** In Setup ******************");
		priortisedGroupSortingStrategy = new PriortisedGroupSortingStrategy();
	}

	@After
	public void tearDown() {
		logger.info("*********** In tear down ******************");
	}

	@Test
	public void testPriortisedGroupSortingStrategyWithGroupPrioritySet()
			throws InterruptedException {
		logger.info("testPriortisedGroupSortingStrategyWithGroupPrioritySet");
		Message message = null;
		MessageDecorator msgDecorator = null;
		
		priortisedGroupSortingStrategy.switchOffCurrentSort(false);

		BlockingQueue<MessageDecorator> queue = new PriorityBlockingQueue<MessageDecorator>(
				10, priortisedGroupSortingStrategy);
		
		for (int index = 0; index <= 5; index++) {
			message = new Message();
			msgDecorator = new MessageDecorator(message);
			message.setMessageId(index);
			message.setName("MSG" + index);
			msgDecorator.setGroupPriority(2);
			if (index == 5 || index == 6) {
				message.setGroupId(20);
				msgDecorator.setGroupPriority(1);
			} else if (index == 7 || index == 8) {
				message.setGroupId(15);
				msgDecorator.setGroupPriority(100);
			} else {
				message.setGroupId(index);
			}

			queue.add(msgDecorator);
			logger.debug("adding to queue");
		}
		int count = 0;
		MessageDecorator result = null;
		while (!queue.isEmpty()) {
			MessageDecorator msgDec = (MessageDecorator) queue.take();
			if(count == 0){
				result = msgDec;
				count++;
			}
			logger.debug("msgId= " + msgDec.getMessage().getMessageId()
					+ " GroupId= " + msgDec.getMessage().getGroupId() + " Group Priority= "+msgDec.getGroupPriority());
		}
		
		logger.debug("msgId= " + result.getMessage().getMessageId());
		
		assertEquals(5, result.getMessage().getMessageId());
	}

	@Test
	public void testPriortisedGroupSortingStrategySwitchOff() throws InterruptedException {
		logger.info("testPriortisedGroupSortingStrategySwitchOff");
		Message message = null;
		MessageDecorator msgDecorator = null;
		
		priortisedGroupSortingStrategy.switchOffCurrentSort(true);

		BlockingQueue<MessageDecorator> queue = new PriorityBlockingQueue<MessageDecorator>(
				10, priortisedGroupSortingStrategy);
		
		for (int index = 0; index <= 5; index++) {
			message = new Message();
			msgDecorator = new MessageDecorator(message);
			message.setMessageId(index);
			message.setName("MSG" + index);
			msgDecorator.setGroupPriority(2);
			if (index == 5 || index == 6) {
				message.setGroupId(20);
				msgDecorator.setGroupPriority(1);
			} else if (index == 7 || index == 8) {
				message.setGroupId(15);
				msgDecorator.setGroupPriority(100);
			} else {
				message.setGroupId(index);
			}

			queue.add(msgDecorator);
			logger.debug("adding to queue");
		}
		int count = 0;
		MessageDecorator result = null;
		while (!queue.isEmpty()) {
			MessageDecorator msgDec = (MessageDecorator) queue.take();
			if(count == 0){
				result = msgDec;
				count++;
			}
			logger.debug("msgId= " + msgDec.getMessage().getMessageId()
					+ " GroupId= " + msgDec.getMessage().getGroupId() + " Group Priority= "+msgDec.getGroupPriority());
		}
		
		logger.debug("msgId= " + result.getMessage().getMessageId());
		
		assertEquals(0, result.getMessage().getMessageId());
	}

}
