package com.cmzy.rabbitmq.tx;

import com.cmzy.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Provider {
	private static final String QUEUE_NAME="queue_tx";
	public static void main(String[] args) throws Exception{
		Connection conn = RabbitMqUtil.getConnection();
		Channel channel = conn.createChannel();
		try {
			//开启事务
			channel.txSelect();
			channel.basicPublish("", QUEUE_NAME, null, "message".getBytes());
			//提交
			channel.txCommit();
		} catch (Exception e) {
			//回滚
			System.out.println("异常,数据回滚");
			channel.txRollback();
		}
		finally {
			channel.close();
			conn.close();
		}
	}
}
