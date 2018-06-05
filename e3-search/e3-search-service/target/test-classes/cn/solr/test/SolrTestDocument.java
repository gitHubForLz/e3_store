package test.resources.cn.solr.test;




import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrTestDocument {

	@Test
	public void add() throws Exception, Exception {
//		第一步：把solrJ的jar包添加到工程中。
//		第二步：创建一个SolrServer，使用HttpSolrServer创建对象。
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.128:8081/solr");
//		第三步：创建一个文档对象SolrInputDocument对象。
		SolrInputDocument document=new  SolrInputDocument();
//		第四步：向文档中添加域。必须有id域，域的名称必须在schema.xml中定义。
		document.addField("id", "test12");
		document.addField("item_title", "1234gg胡56lllll");
		document.addField("item_price", "199");

//		第五步：把文档添加到索引库中。
		solrServer.add(document);
//		第六步：提交。
		solrServer.commit();

	}
	@Test
	public void delte() throws Exception {
		
			// 第一步：创建一个SolrServer对象。
			SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr");
			// 第二步：调用SolrServer对象的根据id删除的方法。
			solrServer.deleteById("test006");
			// 第三步：提交。
			solrServer.commit();
		

	}

}
