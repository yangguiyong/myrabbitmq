package com.cmzy.rabbitmq.work;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.cmzy.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Provider {
	private static final String QUNUE_NAME="test_qunue";
	public static void main(String args[]) throws Exception {
		
		//获取rabbitMQ连接
		Connection conn = RabbitMqUtil.getConnection();
		//创建channel
		Channel channel = conn.createChannel();
		//创建队列声明
		channel.queueDeclare(QUNUE_NAME, false, false, false, null);
		//每个消费者发送确认消息之前，消息队列不发送下一个消息到消费者，一次只处理一个消息
		
		//限制发送给同一个消费者不得超过一条消息
		int count = 1;
		channel.basicQos(count);
		
		//发送消息
		for(int i=0; i<20; i++) {
			String msg = "hello "+i;
			channel.basicPublish("", QUNUE_NAME, null, msg.getBytes());
		}
		
		channel.close();
		conn.close();
	}
}
