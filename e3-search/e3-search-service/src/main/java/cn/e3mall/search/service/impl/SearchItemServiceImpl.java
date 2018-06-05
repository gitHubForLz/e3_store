package cn.e3mall.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchItemService;
/**
 * 创建索引库
 * @author Li
 *
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer httpSolrServer;

	@Override
	public E3Result importAllItems() {

		try {
			// 功能分析
			// 1、查询所有商品数据。
			List<SearchItem> itemList = itemMapper.getItemList();
			// 2、循环把商品数据添加到索引库。使用solrJ实现。
			for (SearchItem searchItem : itemList) {
				SolrInputDocument document = new SolrInputDocument();

				// 向文档中添加域
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				httpSolrServer.add(document);
			}
				httpSolrServer.commit();
			// 3、返回成功。返回E3Result
				return E3Result.ok();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return E3Result.build(500, "导入失败");
		}

	}

	@Override
	public E3Result importByIdItem(long itemID) {
		try {
			
			SearchItem searchItem = itemMapper.getItemId(itemID);
			SolrInputDocument document = new SolrInputDocument();
			
			// 向文档中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			
			httpSolrServer.add(document);
			httpSolrServer.commit();
			
			return E3Result.ok();
		} catch (Exception e) {
			return null;
		}
	}

}
