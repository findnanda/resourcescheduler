package com.resourcescehduler;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.resourcescehduler.sorting.ISortingRules;
import com.resourcescehduler.vo.Message;

/**
 * This is a very simple scheduler. Change it as per the needs. This scheduler is only for testing various conditions and sorting strategies.
 * This will dynamically add and remove  group sorting strategy as well.
 * 
 * @author NYalamanchili
 * 
 */
public class Scheduler {
	static MessageReceiver receiver = null;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"/app-context.xml");
		receiver = (MessageReceiver) ctx.getBean("messageReceiver");
		
		ISortingRules sortRules = (ISortingRules) ctx.getBean("sortingRules");

		for (int i = 1; i <= 50; i++) {
			Message message = new Message();
			message.setMessageId(i);
			message.setGroupId(i);
			message.setName("message " + i);
			System.out.println("sending message number " + i);
			if (i > 8 && i <= 15) {
				message.setGroupId(2);
			}
			if (i >= 12 && i <= 13) {
				message.setGroupId(1);
			}
			if (i >= 20 && i <= 24) {
				message.setGroupId(2);
			}
			
			receiver.receiver(message);

			if (i == 5) {
				IGroupCancellationReceiver cancelReceiver = (IGroupCancellationReceiver) ctx
						.getBean("groupCancellationReceiver");
				cancelReceiver.cancelGroup(5);
				cancelReceiver.cancelGroup(8);
			}
			
			//enable group sorting while the messages are getting processed.
			if(i >=30){
				//Let few messages gets processed
				if(i==30){
					receiver.setForcePGSOff(true);
					// During this time only default fifo will be on.
					Thread.sleep(10000);
				}
				//Enable sort by group in descending order
				sortRules.sortBygroupDesc(true);
			} 
		}
	}
}
