package test.resources.cn.solr.test;

import static org.junit.Assert.*;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

public class SolrQueryTest {
	@Test
	public void simpleQuery() throws Exception {
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.128:8081/solr");
		
		SolrQuery query =new  SolrQuery();
		
		query.setQuery("*:*");
		
		QueryResponse response = solrServer.query(query);
		
		SolrDocumentList results = response.getResults();
		
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println("+++++++++++++++++++++++++");
		}
		
		
	}
}
