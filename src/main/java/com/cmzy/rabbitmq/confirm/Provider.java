package com.cmzy.rabbitmq.confirm;

import com.cmzy.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Provider {
	private static final String QUEUE_NAME = "confirm_queue";
	public static void main(String[] args) throws Exception {
		Connection conn = RabbitMqUtil.getConnection();
		Channel channel = conn.createChannel();
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		//开启confirm模式
		channel.confirmSelect();
		//发送消息
		for(int i=0; i<10; i++) {
			channel.basicPublish("", QUEUE_NAME, null, ("first confirm message"+i).getBytes());
		}
		
		//判断消息是否发送成功
		if(channel.waitForConfirms()) {
			System.out.println("发送消息成功");
		}
		channel.close();
		conn.close();
	}
}
