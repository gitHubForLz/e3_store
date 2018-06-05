package cn.e3mall.redisj;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;
import redis.clients.jedis.Jedis;

public class RedisTest {
	@Test
	public void testJedis() {
		
//		第一步：创建一个Jedis对象。需要指定服务端的ip及端口。
		Jedis jedis =new Jedis("192.168.25.128",6379);
//		第二步：使用Jedis对象操作数据库，每个redis命令对应一个方法。
		String result = jedis.get("hello");
//		第三步：打印结果。
		System.out.println(result);
//		第四步：关闭Jedis
		jedis.close();
		
	}
	@Test
	public void testJedisSpring() throws Exception {
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient bean =  applicationContext.getBean(JedisClient.class);
//		String string = bean.get("hello");
		bean.set("1", "2");
		String string2 = bean.get("1");
		
		System.out.println(string2);
	}
}
