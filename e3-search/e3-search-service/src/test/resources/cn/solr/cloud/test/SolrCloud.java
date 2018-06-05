package test.resources.cn.solr.cloud.test;

import static org.junit.Assert.*;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrCloud {
	/**
	 * solr集群连接测添加索引库
	 * @throws Exception
	 */
	@Test
	public void testName() throws Exception {
		//zookeeper地址
		CloudSolrServer  solrServer=new CloudSolrServer("192.168.25.131:2181,192.168.25.131:2182,192.168.25.131:2183");
		solrServer.setDefaultCollection("collection2");
		SolrInputDocument document=new SolrInputDocument();
		document.addField("id", "test12");
		document.addField("item_title", "1234gg胡56lllll");
		document.addField("item_price", "199");
		solrServer.add(document);
		solrServer.commit();
	}
}
