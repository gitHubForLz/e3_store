package cn.e3mall.activemq;
/**
 * 发布消息
 * @author Li
 *
 */

import static org.junit.Assert.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ProducerMessage {

	@Test
	public void queuePublish() throws Exception {
//		第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
		ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
//		第二步：使用ConnectionFactory对象创建一个Connection对象。
		Connection connection = connectionFactory.createConnection();
//		第三步：开启连接，调用Connection对象的start方法。
		connection.start();
//		第四步：使用Connection对象创建一个Session对象。
		//第一个参数：是否开启事务。true：开启事务，第二个参数忽略。
				//第二个参数：当第一个参数为false时，才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//		第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象。
		//Queue queue = session.createQueue("test-queue");
		Topic topic = session.createTopic("test-topic");
//		第六步：使用Session对象创建一个Producer对象。
		MessageProducer producer = session.createProducer(topic);
//		第七步：创建一个Message对象，创建一个TextMessage对象。
		TextMessage message = session.createTextMessage("你好");
//		第八步：使用Producer对象发送消息。
		producer.send(message);
//		第九步：关闭资源。
		producer.close();
		session.close();
		connection.close();
	}
	/**
	 * spring
	 */
	@Test
		public void testSpringActiveMq() throws Exception {
			//初始化spring容器
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
			//从spring容器中获得JmsTemplate对象
			JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
			//从spring容器中取Destination对象
			Destination destination = (Destination) applicationContext.getBean("queueDestination");
			//使用JmsTemplate对象发送消息。
			jmsTemplate.send(destination, new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					//创建一个消息对象并返回
					TextMessage textMessage = session.createTextMessage("spring activemq queue message");
					return textMessage;
				}
			});
		}


}
