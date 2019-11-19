package com.cmzy.rabbitmq.simple;

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
		//发送消息
		for(int i=0; i<50; i++) {
			String msg = "hello "+i;
			channel.basicPublish("", QUNUE_NAME, null, msg.getBytes());
		}
		
		channel.close();
		conn.close();
	}
}
