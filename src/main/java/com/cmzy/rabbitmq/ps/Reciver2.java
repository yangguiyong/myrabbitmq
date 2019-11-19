package com.cmzy.rabbitmq.ps;

import java.io.IOException;

import com.cmzy.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Reciver2 {
	private static final String EXCHANGE_NAME = "my_exchange";
	private static final String QUNUE_NAME = "exchange_qunue2";
	private static final String ROUTE_KEY = "my_route.#";
	public static void main(String[] args) throws Exception {
		Connection conn = RabbitMqUtil.getConnection();
		Channel channel = conn.createChannel();
		//声明队列
		channel.queueDeclare(QUNUE_NAME,false,false,false,null);
		//绑定队列到交换机
		channel.queueBind(QUNUE_NAME, EXCHANGE_NAME, ROUTE_KEY);
		//设置每次只发送一条消息
		channel.basicQos(1);
		
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				
				String msg = new String(body);
				System.out.println(msg);
			}
		};
		channel.basicConsume(QUNUE_NAME, true, consumer);
	}
}
