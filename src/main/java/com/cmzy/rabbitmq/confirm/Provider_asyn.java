package com.cmzy.rabbitmq.confirm;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import com.cmzy.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

public class Provider_asyn {
	private static final String QUEUE_NAME = "confirm_queue_asyn";
	public static void main(String[] args) throws Exception {
		Connection conn = RabbitMqUtil.getConnection();
		Channel channel = conn.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		channel.confirmSelect();

		//存放发送消息的消息编号的代码
		final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
		
		//添加channel的监听
		channel.addConfirmListener(new ConfirmListener() {
			//发送不成功执行，deliveryTag为消息的编号，multiple表示是否是多条，true表示批量发送确认消息，表示在此消息编号之前的所有消息都已经接收到了
			//通过该方法可以用于实现失败消息重发，保持消息接口幂等性。根据具体的业务逻辑实现。
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				
				if(multiple) {
					System.out.println("发送失败，multiple："+multiple);
					confirmSet.headSet(deliveryTag+1).clear();
				}
				else {
					confirmSet.remove(deliveryTag);
				}
			}
			//发送成功执行，deliveryTag为消息的编号，multiple表示是否是多条，true表示该编号消息有多条
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				// TODO Auto-generated method stub
				if(multiple) {
					System.out.println("发送成功，multiple："+multiple);
					confirmSet.headSet(deliveryTag+1).clear();
				}
				else {
					confirmSet.remove(deliveryTag);
				}
			}
		});
		while(true) {
			long seqNo = channel.getNextPublishSeqNo();
			channel.basicPublish("", QUEUE_NAME, null, "异步消息发送".getBytes());
			confirmSet.add(seqNo);
		}
		
	}
}
