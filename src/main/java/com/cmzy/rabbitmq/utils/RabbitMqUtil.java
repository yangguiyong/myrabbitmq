package com.cmzy.rabbitmq.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqUtil {
	
	public static Connection getConnection() throws IOException, TimeoutException {
		//获取工厂，通过工厂创建连接
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.21.47");
		factory.setPort(5672);
		factory.setVirtualHost("/host1");
		factory.setUsername("admin");
		factory.setPassword("admin");
		
		Connection conn = null;
		conn = factory.newConnection();

		return conn;
	}
}
