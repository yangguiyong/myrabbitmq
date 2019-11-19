package com.cmzy.rabbitmq.work;

import java.io.IOException;

import com.cmzy.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Reciver {
	private static final String QUNUE_NAME = "test_qunue";

	public static void main(String[] args) throws Exception {
		newApi();
	}

	public static void newApi() throws Exception {
		// 获取连接
		Connection conn = RabbitMqUtil.getConnection();
		// 创建channel
		final Channel channel = conn.createChannel();
		// 声明队列，如果生产者中声明了队列，消费者中可以不声明
		channel.queueDeclare(QUNUE_NAME, false, false, false, null);

		channel.basicQos(1);

		// 创建消费者
		Consumer consumer = new DefaultConsumer(channel) {
			// 当生产者发送消息到rabbitmq之后，会自动触发消费者中的handleDelivery方法
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {

				try {
					String msg = new String(body, "UTF-8");
					System.out.println("msg1:" + msg);
					Thread.sleep(2000);
					int i = 1/0;
					channel.basicAck(envelope.getDeliveryTag(), false);
				} catch (Exception e) {
					System.out.println("异常了");
				} finally {
					//channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}

		};
		// 监听队列,将自动应答关闭，使用手动应答
		boolean autoAck = false;
		channel.basicConsume(QUNUE_NAME, autoAck, consumer);
	}

	/**
	 * 过期的方式
	 * 
	 * @throws Exception
	 */
	public static void oldApi() throws Exception {
		Connection conn = RabbitMqUtil.getConnection();
		Channel channel = conn.createChannel();
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUNUE_NAME, true, consumer);

		while (true) {
			Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println(msg);
		}
	}
}
