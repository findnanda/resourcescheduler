package com.resourcescehduler;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.resourcescehduler.vo.Message;

/**
 * @author NYalamanchili
 * 
 */
public class Scheduler {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"/app-context.xml");
		MessageReceiver receiver = (MessageReceiver) ctx
				.getBean("messageReceiver");
		for (int i = 1; i <= 10; i++) {
			Message message = new Message();
			message.setMessageId(i);
			message.setGroupId(i);
			message.setName("message " + i);
			System.out.println("sending " + i);
			receiver.receiveMessage(message);
			if (i == 5) {
				IGroupCancellationReceiver cancelReceiver = (IGroupCancellationReceiver) ctx
						.getBean("groupCancellationReceiver");
				cancelReceiver.cancelGroup(5);
				cancelReceiver.cancelGroup(8);
			}
		}
	}
}
