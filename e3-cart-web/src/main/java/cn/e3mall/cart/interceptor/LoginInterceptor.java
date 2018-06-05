package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private  TokenService tokenService;
	
	@Value("${USER_INFO}")
	private String USER_INFO;
	@Value("${COOKIE_TOKEN}")
	private String COOKIE_TOKEN;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//判断用户是否登录
		//本地cookie
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN);
		if(StringUtils.isBlank(token)) {
			return true ;
		}
		//Redis缓存
		E3Result e3Result = tokenService.cookieLogin(token);
		if(e3Result.getStatus()!=200) {
			return true;
		}
		
		request.setAttribute("user", (TbUser)e3Result.getData());
		return true;
		
//		handler 前处理
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
//		handler后处理，model之前
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
//model之后处理
	}

}
