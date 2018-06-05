package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

@Service
public class CartServiceImpl implements cn.e3mall.cart.service.CartService {
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemMapper itemMapper;

	@Value("${CART_PRE}")
	private String CART_PRE;

	@Override
	public E3Result addCart(long itemId, Integer num, long userId) {
		// hash key：用户id field：商品id value：商品信息
		// a)判断购物车中是否有此商品
		Boolean flag = jedisClient.hexists(CART_PRE + ":" + userId, itemId + "");
		// 如果存在
		if (flag) {
			String value = jedisClient.hget(CART_PRE + ":" + userId, itemId + "");
			// 把json转成item
			TbItem tbItem = JsonUtils.jsonToPojo(value, TbItem.class);
			// 修改num属性
			tbItem.setNum(tbItem.getNum() + num);
			jedisClient.hset(CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		}
		// 不存在
		TbItem itemById = itemMapper.selectByPrimaryKey(itemId);
		itemById.setNum(num);
		// 取一张图片
		String image = itemById.getImage();
		if (StringUtils.isNotBlank(image)) {
			itemById.setImage(image.split(",")[0]);
		}
		jedisClient.hset(CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(itemById));

		return E3Result.ok();
	}

	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		// if cookieslist是否为空
		// 遍历 if List中与Redis相同 add
		if (itemList != null) {
			for (TbItem tbItem : itemList) {

				addCart(tbItem.getId(), tbItem.getNum(), userId);
			}
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> cartList(long userId) {
		List<String> hvals = jedisClient.hvals(CART_PRE + ":" + userId);
		List<TbItem> list = new ArrayList<TbItem>();
		for (String string : hvals) {
			Object TbItem;
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			list.add(item);
		}
		return list;
	}

	@Override
	public E3Result updateCart(long itemId, Integer num, long userId) {
		Boolean flag = jedisClient.hexists(CART_PRE + ":" + userId, itemId + "");
		// 如果存在
		if (flag) {
			String value = jedisClient.hget(CART_PRE + ":" + userId, itemId + "");
			// 把json转成item
			TbItem tbItem = JsonUtils.jsonToPojo(value, TbItem.class);
			// 修改num属性
			tbItem.setNum(num);
			jedisClient.hset(CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		}
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCart(long itemId, long userId) {
		jedisClient.hdel(CART_PRE + ":" + userId, itemId + "");
		return E3Result.ok();
	}

	@Override
	public void deleteAllItem(Long userId) {
		jedisClient.del(CART_PRE + ":" + userId);
	}

}
