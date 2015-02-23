package com.resoourcescehduler.sorting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.Log4jConfigurer;

import com.resourcescehduler.sorting.FIFOSortStrategy;
import com.resourcescehduler.sorting.ISortingStrategy;
import com.resourcescehduler.vo.Message;
import com.resourcescehduler.vo.MessageDecorator;

/**
 * @author NYalamanchili
 *
 */
public class FIFOSortStrategyTest {

	private static Log logger = LogFactory.getLog(FIFOSortStrategyTest.class);
	private FIFOSortStrategy fifoSortingStrategy;

	@Before
	public void setUp() {
		try {
			Log4jConfigurer.initLogging("classpath:log4j.xml");
		} catch (FileNotFoundException ex) {
			System.err.println("Cannot Initialize log4j");
		}

		logger.info("*********** In Setup ******************");
		fifoSortingStrategy = new FIFOSortStrategy();
	}

	@After
	public void tearDown() {
		logger.info("*********** In tear down ******************");
		fifoSortingStrategy = null;
	}

	@Test
	public void testFIFOSortingStrategy() throws InterruptedException {
		logger.info("testFIFOSortingStrategy");
		
		ThreadRunner runner = new ThreadRunner();
		Thread t1 = new Thread(runner);
		Thread t2 = new Thread(runner);
		Thread t3 = new Thread(runner);
		Thread t4 = new Thread(runner);
		t2.start();
		t1.start();
		t3.start();
		t4.start();

		t1.join();
		t2.join();
		t3.join();
		t4.join();
		MessageDecorator msg = null;
		while (!runner.priorityQueue.isEmpty()) {
			msg = (MessageDecorator) runner.priorityQueue.take();
			logger.debug("SeqNum: " + msg.getSeqNum());
		}
		// the last message
		assertEquals(39, msg.getSeqNum());
	}
	
	@Test
	public void testFIFOSortingStrategySwitchOff() throws InterruptedException {
		logger.info("testFIFOSortingStrategySwitchOff");
		
		ThreadRunner runner = new ThreadRunner();
		Thread t1 = new Thread(runner);
		Thread t2 = new Thread(runner);
		Thread t3 = new Thread(runner);
		Thread t4 = new Thread(runner);
		t2.start();
		t1.start();
		t3.start();
		t4.start();

		t1.join();
		t2.join();
		t3.join();
		t4.join();
		
		MessageDecorator msg = null;
		fifoSortingStrategy.switchOffCurrentSort(true);
		while (!runner.priorityQueue.isEmpty()) {
			msg = (MessageDecorator) runner.priorityQueue.take();
			logger.debug("SeqNum: " + msg.getSeqNum());
		}
		assertNotNull(msg.getSeqNum());
	}

	public class ThreadRunner implements Runnable {
		private Log log = LogFactory.getLog(ThreadRunner.class);
		PriorityBlockingQueue<MessageDecorator> priorityQueue = new PriorityBlockingQueue<MessageDecorator>(
				15, fifoSortingStrategy);

		public void run() {
			for (int index = 1; index <= 5; index++) {
				Message message = new Message();
				message.setMessageId(index);
				message.setName("MSG" + index);
				if (index == 5 || index == 6) {
					message.setGroupId(20);
				} else if (index >= 3 && index <= 8) {
					message.setGroupId(15);
				} else {
					message.setGroupId(index);
				}
				MessageDecorator msgDecorator = new MessageDecorator(message);
				priorityQueue.add(msgDecorator);
				log.debug("" + msgDecorator.getSeqNum()+ " NanoTime:"+System.nanoTime());
			}

		}
	}
}
