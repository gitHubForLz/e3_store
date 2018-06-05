package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderItem;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbOrder;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import cn.e3mall.pojo.TbUser;
@Service
public class OrderServiceImpl implements OrderService {
@Autowired
private TbOrderItemMapper orderItemMapper;
@Autowired
private TbOrderShippingMapper orderShippingMapper;
@Autowired
private TbOrderMapper orderMapper;
@Autowired
private JedisClient jedisClient;

@Value("${ORDER_ID_GEN_KEY}")
private String ORDER_ID_GEN_KEY;
@Value("${ORDER_DETAIL_ID_GEN_KEY}")
private String ORDER_DETAIL_ID_GEN_KEY;
@Value("${ORDER_ID_START}")
private String ORDER_ID_START;

	@Override
	public E3Result orderCreat(OrderItem orderItem) {
//		1、接收表单的数据
//		2、生成订单id
		if (!jedisClient.exists(ORDER_ID_GEN_KEY)) {
			//设置初始值
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
//		3、向订单表插入数据。
//		4、向订单明细表插入数据
//		5、向订单物流表插入数据。
//		6、返回e3Result。

		//将order补全
//		状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderItem.setOrderId(orderId);
		orderItem.setStatus(1);
		orderItem.setCreateTime(new Date());
		orderItem.setUpdateTime(new Date());
		orderMapper.insert(orderItem);
		//orderitem
		List<TbOrderItem> orderItems=orderItem.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//生成明细id
			String odId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			//补全pojo的属性
			tbOrderItem.setId(odId);
			tbOrderItem.setOrderId(orderId);
			//向明细表插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		//向订单物流表插入数据
		TbOrderShipping orderShipping = orderItem.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		//返回E3Result，包含订单号
		return E3Result.ok(orderId);
	}

	
	
	

}
