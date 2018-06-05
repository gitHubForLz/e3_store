package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;

@Controller
public class RegisterController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("/user/register")
	public String toRegister() {
		return "register";
	}
	
	@RequestMapping("/user/check/{parma}/{type}")
	@ResponseBody
	public E3Result checkUser(@PathVariable String parma,@PathVariable Integer type) {
		E3Result result = userService.checkUser(parma, type);
		return result;
	}
	//zhuce
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result registerUser(TbUser user) {
		E3Result e3Result = userService.register(user);
		return e3Result;
	}
	
}
