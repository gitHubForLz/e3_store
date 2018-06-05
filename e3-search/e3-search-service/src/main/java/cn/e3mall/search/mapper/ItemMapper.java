package cn.e3mall.search.mapper;

import java.util.List;

import cn.e3mall.common.pojo.SearchItem;

public interface ItemMapper {
	//首页搜索返回数据库
	List<SearchItem> getItemList();
	//manage-service传回id查数据库
	SearchItem getItemId(long itemid);
}
