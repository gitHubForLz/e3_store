package cn.e3mall.sso.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;

@Controller
public class LoginController {
	@Autowired
	private UserService userService;
	@Value("${COOKIE_TOKEN}")
	private String  COOKIE_TOKEN;
	@Value("${COOKIE_EXPIRE}")
	private int  COOKIE_EXPIRE;
	
	@RequestMapping("/page/login")
	public String loginPage(Model model,String redirect) {
		model.addAttribute("redirect", redirect);
		return "login";
	}
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result login(TbUser user,HttpServletRequest request, HttpServletResponse response) {
		
		E3Result e3Result = userService.login(user);
		if (e3Result.getStatus()==200) {
			String token = e3Result.getData().toString();
			CookieUtils.setCookie(request, response, COOKIE_TOKEN,token );
		
		}
		
		return e3Result;
}
	
	
	
	  


}
