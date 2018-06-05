package cn.e3mall.sso.service;
/**
 * 注册
 * 登录
 * @author Li
 *
 */

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;

public interface UserService {
	public E3Result checkUser(String parma,Integer type);
	
	public E3Result register(TbUser user);
	
	public E3Result login(TbUser user) ;
	
}
