package com.taotao;

import static org.junit.Assert.assertTrue;

import com.taotao.content.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testJedisSingle(){
        // 第一步：创建一个Jedis对象。需要指定服务端的ip及端口。
        Jedis jedis = new Jedis("192.168.25.175", 6379);
        // 第二步：使用Jedis对象操作数据库，每个redis命令对应一个方法。
        String result = jedis.get("hello");
        // 第三步：打印结果。
        System.out.println(result);
        // 第四步：关闭Jedis
        jedis.close();

    }


    @Test
    public void testJedisPool(){

        //第一步 创建一个连接池对象
        JedisPool jedisPool=new JedisPool("192.168.25.175",6379);
        //2.从链接池中获取连接
        Jedis jedis = jedisPool.getResource();
        //3.添加一跳数据
        jedis.set("test", "where is redis");
        //4. 打印结果
        System.out.println(jedis.get("test"));
        // 关闭jedis 释放资源到连接池
        jedis.close();
        //系统关闭前关闭连接池
        jedisPool.close();
    }

//    @Test
//    public void testJedisClient()throws Exception{
//
//        //初始化 spring 容器
//        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:applicationContext-redis.xml");
//        //从容器中获取jedis对象
//        JedisClient jedisClient = context.getBean(JedisClient.class);
//        String result = jedisClient.get("hello");
//        System.out.println(result);
//
//    }
}
