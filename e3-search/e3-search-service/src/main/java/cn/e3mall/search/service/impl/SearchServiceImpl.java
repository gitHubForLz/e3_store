package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.container.page.PageServlet;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;
@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	private SearchDao searchDao;
	
	@Value("${DEFAULT_FIELD}")
	private String DEFAULT_FIELD;


	@Override
	public SearchResult search(String keyWord, int page, int rows) throws Exception {
		SolrQuery solrQuery=new SolrQuery();
		
		solrQuery.setQuery(keyWord);
		
		solrQuery.setStart((page-1)*rows);
		
		solrQuery.setRows(rows);
		
		solrQuery.set("df", DEFAULT_FIELD);
		
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField(keyWord);
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		
		SearchResult searchResult = searchDao.search(solrQuery);
		//计算总数
		int recourdCount = searchResult.getRecourdCount();
		int pages =recourdCount / rows;
		if (recourdCount%rows>0) page++;
		
		searchResult.setTotalPages(pages);
		
		return searchResult;
		
	}

}
