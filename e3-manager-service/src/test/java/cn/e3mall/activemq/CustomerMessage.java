package cn.e3mall.activemq;

import static org.junit.Assert.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

/**
 * 消费者：接收消息。
 * 
 * @author Li
 *
 */

public class CustomerMessage {
/**
 * quenue
 * @throws Exception
 */
	@Test
	public void cusomer() throws Exception {
		// 第一步：创建一个ConnectionFactory对象。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		// 第二步：从ConnectionFactory对象中获得一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		// 第三步：开启连接。调用Connection对象的start方法。
		connection.start();
		// 第四步：使用Connection对象创建一个Session对象
		Session createSession = connection.createSession(false, 1);
		// 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
		Queue queue = createSession.createQueue("test-queue");
		// 第六步：使用Session对象创建一个Consumer对象。
		MessageConsumer consumer = createSession.createConsumer(queue);
		// 第七步：接收消息。
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				try {
					TextMessage textMessage = (TextMessage) message;
					String text = null;
					// 取消息的内容
					text = textMessage.getText();
					// 第八步：打印消息。
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}

			}
		});
		// 等待键盘输入
		System.in.read();

		// 第九步：关闭资源
		consumer.close();
		createSession.close();
		connection.close();

	}
	/**
	 * topic
	 */
	@Test
	public void cusomerTopic() throws Exception {
		// 第一步：创建一个ConnectionFactory对象。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		// 第二步：从ConnectionFactory对象中获得一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		// 第三步：开启连接。调用Connection对象的start方法。
		connection.start();
		// 第四步：使用Connection对象创建一个Session对象
		Session createSession = connection.createSession(false, 1);
		// 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
		Topic topic = createSession.createTopic("test-topic");
		// 第六步：使用Session对象创建一个Consumer对象。
		MessageConsumer consumer = createSession.createConsumer(topic);
		// 第七步：接收消息。
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				try {
					TextMessage textMessage = (TextMessage) message;
					String text = null;
					// 取消息的内容
					text = textMessage.getText();
					// 第八步：打印消息。
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}

			}
		});
		// 等待键盘输入
		System.in.read();

		// 第九步：关闭资源
		consumer.close();
		createSession.close();
		connection.close();

	}
}
