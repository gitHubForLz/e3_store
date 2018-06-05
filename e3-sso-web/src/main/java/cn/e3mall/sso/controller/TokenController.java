package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Callback;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.sso.service.TokenService;

@Controller
public class TokenController {
	@Autowired
	private TokenService tokenService;
	
	//跨域请求一
/*	@RequestMapping(value="/user/token/{_ticket}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String tokenLogin(@PathVariable String _ticket,String callback ) {
		
		E3Result e3Result = tokenService.cookieLogin(_ticket);
		//是否为json请求
		if(StringUtils.isNotBlank(callback)) {
			String stringResult=callback+"("+JsonUtils.objectToJson(e3Result)+");";
			return stringResult;
		}
		return JsonUtils.objectToJson(e3Result);
	}*/
	//跨域请求二
	@RequestMapping(value="/user/token/{_ticket}")
	@ResponseBody
	public Object tokenLogin(@PathVariable String _ticket,String callback ) {
		
		E3Result e3Result = tokenService.cookieLogin(_ticket);
		//是否为json请求
		if(StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(e3Result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return e3Result;
	}
	
}
