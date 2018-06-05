package cn.e3mall.order.service;

import java.util.List;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderItem;
import cn.e3mall.pojo.TbUser;

public interface OrderService {
	E3Result orderCreat(OrderItem orderItem );
}
