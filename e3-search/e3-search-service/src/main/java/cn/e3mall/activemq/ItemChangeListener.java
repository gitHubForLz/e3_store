package cn.e3mall.activemq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.service.SearchItemService;
import cn.e3mall.search.service.SearchService;

public class ItemChangeListener implements MessageListener {
	@Autowired
	private  SearchItemService searchItemService;
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage=(TextMessage) message;
			String itemIdS = textMessage.getText();
			long itemId = Long.parseLong(itemIdS);
			searchItemService.importByIdItem(itemId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		

	}

}
