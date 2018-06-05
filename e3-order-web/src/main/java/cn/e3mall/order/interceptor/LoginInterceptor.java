package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
/**
 * 结账前登录判断
 * @author Li
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	
	@Value("${COOKIE_TOKEN}")
	private String COOKIE_TOKEN;
	@Value("${SSO_URL}")
	private String SSO_URL;
	@Value("${TT_CART}")
	private String TT_CART;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN);
		if(StringUtils.isBlank(token)) {
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			
			
			return false;
			}
		E3Result e3Result = tokenService.cookieLogin(token);
		if(e3Result.getStatus()!=200) {
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute("user", user);
		String json = CookieUtils.getCookieValue(request, TT_CART, true);
		if (StringUtils.isNotBlank(json)) {
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(json, TbItem.class));
			//删除cookie中的购物车数据
			CookieUtils.setCookie(request, response, TT_CART, "");
			
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
