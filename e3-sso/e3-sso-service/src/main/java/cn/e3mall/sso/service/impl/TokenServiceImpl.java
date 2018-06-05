package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
@Service
public class TokenServiceImpl implements TokenService {
	@Autowired
	private JedisClient jedisClient;
	@Value("${USER_INFO}")
	private String USER_INFO;
	@Value("${EXPIRE_TIME}")
	private int EXPIRE_TIME;
	
	@Override
	public E3Result cookieLogin(String cookie_token) {
		String json = jedisClient.get(USER_INFO+":"+cookie_token);
		if(StringUtils.isBlank(json)) {
			return E3Result.build(400, "登录已过期");
		}
		jedisClient.expire(USER_INFO+":"+cookie_token, EXPIRE_TIME);
		//返回用户
		TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
		return E3Result.ok(tbUser);
	}

}
