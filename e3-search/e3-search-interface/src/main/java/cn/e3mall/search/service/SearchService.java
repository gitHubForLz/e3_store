package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

public interface SearchService {
	//首页搜索查询
	public SearchResult search(String keyWord, int page, int rows) throws Exception;
}
