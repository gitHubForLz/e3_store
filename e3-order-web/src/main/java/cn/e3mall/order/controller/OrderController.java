package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderItem;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

@Controller
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/order/order-cart")
	public String  showOrder(HttpServletRequest request) {
		TbUser user= (TbUser) request.getAttribute("user");
		//根据用户id取收货地址列表
		//使用静态数据。。。
		//取支付方式列表
		//静态数据
		//根据用户id取购物车列表
		List<TbItem> cartList = cartService.cartList(user.getId());
		//把购物车列表传递给jsp
		request.setAttribute("cartList", cartList);
		
		//返回页面
		return "order-cart";
	}
	@RequestMapping("/order/create")
	public String  creatOrder (OrderItem orderItem,Model m,HttpServletRequest request){
		//取用户信息
				TbUser user = (TbUser) request.getAttribute("user");
				//把用户信息添加到orderInfo中。
				orderItem.setUserId(user.getId());
				E3Result e3Result = orderService.orderCreat(orderItem);
		//调用服务生成订单
				//如果订单生成成功，需要删除购物车
				if (e3Result.getStatus() == 200) {
					//清空购物车
					cartService.deleteAllItem(user.getId());
				}
				//把订单号传递给页面
				request.setAttribute("orderId", e3Result.getData());
				request.setAttribute("payment", orderItem.getPayment());
				//返回逻辑视图
				return "success";
	}
}
