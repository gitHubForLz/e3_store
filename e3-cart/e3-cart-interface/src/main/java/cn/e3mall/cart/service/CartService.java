package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

public interface CartService {
	E3Result addCart(long itemId,Integer num,long userId); 
	
	E3Result updateCart (long itemId,Integer num,long userId); 
	
	E3Result deleteCart (long itemId,long userId); 
	
	public E3Result mergeCart(long userId, List<TbItem> itemList);
	
	List<TbItem> cartList(long userId);

	void deleteAllItem(Long userId); 
	
	
}
