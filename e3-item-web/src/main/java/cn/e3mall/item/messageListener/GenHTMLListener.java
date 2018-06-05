package cn.e3mall.item.messageListener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.store.jdbc.adapter.HsqldbJDBCAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class GenHTMLListener implements MessageListener{
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Value("${Directory_WRITER}")
	private String Directory_WRITER;
	
	@Override
	public void onMessage(Message message) {
		
		try {
			TextMessage textMessage=(TextMessage) message;
			String string = textMessage.getText();
			Long itemId=new Long(string);
			if(itemId!=null) {
				Thread.sleep(1000);
				TbItem tbItem = itemService.getItemById(itemId);
				TbItemDesc itemDescById = itemService.geTbItemDescById(itemId);
				Item item=new Item(tbItem);
				Configuration configuration = freeMarkerConfigurer.getConfiguration();
				Template template = configuration.getTemplate("item.ftl");
				Writer writer=new FileWriter(new File(Directory_WRITER+itemId+".html"));
				System.out.println(writer);
				Map map=new HashMap<>();
				map.put("item", item);
				map.put("itemDesc", itemDescById);
				template.process(map, writer);
				writer.close();
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
