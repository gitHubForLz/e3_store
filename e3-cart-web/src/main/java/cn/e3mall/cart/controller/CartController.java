package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

@Controller
public class CartController {
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;

	@Value("${TT_CART}")
	private String TT_CART;
	@Value("${COOKIE_EXPIRE}")
	private int COOKIE_EXPIRE;

	/**
	 * 未登录添加商品
	 * 
	 * @param product_id
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/add/{product_id}")
	public String addCart(@PathVariable long product_id, Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 登录状态检测
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			// 登录添加购物车，才能够Redis做缓存
			cartService.addCart(product_id, num, user.getId());
			return "cartSuccess";
		}
		
		// 未登录cookies保存
		// 1、从cookie中查询商品列表。
		List<TbItem> cartList = getCartList(request);
		// 2、判断商品在商品列表中是否存在。
		boolean existItem = false;
		for (TbItem tbItem : cartList) {
			// 3、如果存在，商品数量相加。
			if (tbItem.getId() == product_id) {
				tbItem.setNum(tbItem.getNum() + num);
				existItem = true;
				break;
			}
		}
		// 4、不存在，根据商品id查询商品信息。
		if (!existItem) {
			TbItem tbItem = itemService.getItemById(product_id);
			// 补全属性
			// 取第一张照片
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
				tbItem.setImage(tbItem.getImage().split(",")[0]);
			}
			tbItem.setNum(num);
			// 5、把商品添加到购车列表。
			cartList.add(tbItem);
		}
		// 6、把购车商品列表写入cookie。
		CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(cartList), COOKIE_EXPIRE, true);
		return "cartSuccess";
	}

	@RequestMapping(value = "/cart/update/num/{product_id}/{num}", method = RequestMethod.POST)
	@ResponseBody
	public E3Result updateCart(@PathVariable long product_id, @PathVariable Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null) {
			cartService.updateCart(product_id, num, user.getId());
			return E3Result.ok();
		}
		// 得到cookies商品信息
		List<TbItem> cartList = getCartList(request);
		for (TbItem tbItem : cartList) {
			// 、如果存在，商品数量相加。
			if (tbItem.getId() == product_id) {
				tbItem.setNum(num);
				break;
			}
		}
		CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(cartList), COOKIE_EXPIRE, true);

		return E3Result.ok();
	}

	@RequestMapping(value = "/cart/delete/{product_id}")

	public String deleteCart(@PathVariable long product_id, HttpServletRequest request, HttpServletResponse response) {
		TbUser user = (TbUser) request.getAttribute("user");
		if (user!=null) {
			cartService.deleteCart(product_id, user.getId());
			return "redirect:/cart/cart.html";
		}
		// 得到cookies商品信息
		List<TbItem> cartList = getCartList(request);
		for (TbItem tbItem : cartList) {
			// 、如果存在，商品数量相加。
			if (tbItem.getId() == product_id) {

				cartList.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(cartList), COOKIE_EXPIRE, true);

		return "redirect:/cart/cart.html";
	}

	@RequestMapping("/cart/cart")
	public String cartList(HttpServletRequest request,HttpServletResponse response, Model model) {
		TbUser user = (TbUser) request.getAttribute("user");
		List<TbItem> cartList = null;
		if (user != null) {
			cartService.mergeCart(user.getId(), getCartList(request));
			
			CookieUtils.deleteCookie(request, response, TT_CART);
			
			cartList = cartService.cartList(user.getId());
		} else {
			cartList = getCartList(request);
		}
		model.addAttribute("cartList", cartList);
		return "cart";
	}

	/**
	 * 从cookie中取购物车列表
	 * <p>
	 * Title: getCartList
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartList(HttpServletRequest request) {

		// 取购物车列表
		String json = CookieUtils.getCookieValue(request, TT_CART, true);
		// 判断json是否为null
		if (StringUtils.isNotBlank(json)) {
			// 把json转换成商品列表返回
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		return new ArrayList<>();
	}

}
