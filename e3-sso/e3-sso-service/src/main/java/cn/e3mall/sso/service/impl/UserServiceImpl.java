package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${EXPIRE_TIME}")
	private int EXPIRE_TIME;
	@Value("${USER_INFO}")
	private String USER_INFO;

	@Override
	// 校验用户名，手机，email

	public E3Result checkUser(String parma, Integer type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 1判断用户名是否注册，2判断手机3邮箱
		if (type == 1) {
			criteria.andUsernameEqualTo(parma);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(parma);
		} else if (type == 3) {
			criteria.andEmailEqualTo(parma);
		} else {
			return E3Result.build(400, "非法请求");
		}
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return E3Result.ok(true);
		}
		;
		return E3Result.ok(false);
	}

	@Override
	// 注册用户
	public E3Result register(TbUser user) {
		if (StringUtils.isBlank(user.getUsername()))
			return E3Result.build(400, "用户名为空");
		if (StringUtils.isBlank(user.getPassword()))
			return E3Result.build(400, "密码为空");
		if (StringUtils.isBlank(user.getPhone())) {
			return E3Result.build(400, "电话为空");
		}
		// 校验数据是否可用
		E3Result result = checkUser(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return E3Result.build(400, "此用户名已经被使用");
		}
		// 校验电话是否可以
		if (StringUtils.isNotBlank(user.getPhone())) {
			result = checkUser(user.getPhone(), 2);
			if (!(boolean) result.getData()) {
				return E3Result.build(400, "此手机号已经被使用");
			}
		}
		// 校验email是否可用
		if (StringUtils.isNotBlank(user.getEmail())) {
			result = checkUser(user.getEmail(), 3);
			if (!(boolean) result.getData()) {
				return E3Result.build(400, "此邮件地址已经被使用");
			}
		}
		// 2、补全TbUser其他属性。
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 3、密码要进行MD5加密。
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		// 4、把用户信息插入到数据库中。
		tbUserMapper.insert(user);
		// 5、返回e3Result。
		return E3Result.ok();
	}

	@Override
	public E3Result login(TbUser user) {
		TbUserExample example=new  TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(user.getUsername());
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if(list!=null&&list.size()>0) {
			//数据库中的tbUser
			TbUser tbUser = list.get(0);
			if (tbUser.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
				//不传密码回去
				tbUser.setPassword(null);
				//写入Redis缓存
				String token = UUID.randomUUID().toString();
				jedisClient.set(USER_INFO+":"+token, JsonUtils.objectToJson(tbUser));
				jedisClient.expire(USER_INFO+":"+token, EXPIRE_TIME);
				return E3Result.ok(token);
			}
		}
		return E3Result.build(400, "用户名或密码错误");
	}

}
