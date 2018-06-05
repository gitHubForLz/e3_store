package cn.e3mall.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener{

	@Override
	public void onMessage(Message message) {

		try {
			TextMessage textMessage = (TextMessage) message;
			//取消息内容
			String text = textMessage.getText();
			System.out.println(text);
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
