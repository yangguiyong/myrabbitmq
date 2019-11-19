package com.cmzy.rabbitmq.ps;

import com.cmzy.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Provider {

	private static final String EXCHANGE_NAME="my_exchange";
	private static final String ROUTE_KEY = "my_route.delete.delete";
	public static void main(String[] args) throws Exception {
		Connection conn = RabbitMqUtil.getConnection();
		Channel channel = conn.createChannel();
		//声明交换机，指明分发类型
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		//发送消息到交换机
		channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY, null, ROUTE_KEY.getBytes());
		channel.close();
		conn.close();
	}
}
