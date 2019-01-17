package com.taotao.sso.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.IUserLoginService;
import com.taotao.sso.service.jedis.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * @author Andre930
 * @create 2019-01-10 21:06
 */
@Service
public class UserLoginServiceImpl implements IUserLoginService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${USER_INFO}")
    private String USER_INFO;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;
    @Override
    public TaotaoResult login(String username, String password) {
        /**
         * 业务逻辑：
         1、判断用户名密码是否正确。
         2、登录成功后生成token。Token相当于原来的jsessionid，字符串，可以使用uuid。
         3、把用户信息保存到redis。Key就是token，value就是TbUser对象转换成json。
         4、使用String类型保存Session信息。可以使用“前缀:token”为key
         5、设置key的过期时间。模拟Session的过期时间。一般半个小时。
         6、返回TaotaoResult包装token。
         */
        //先校验用户名
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return TaotaoResult.build(400,"用户名或密码错误");
        }
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username); //查询用户名是否存在
        List<TbUser> userList = userMapper.selectByExample(example);
        if (userList==null && userList.size()==0){
            //用户名不存在
            return TaotaoResult.build(400,"用户名不存在");
        }
        //再校验密码
        TbUser user = userList.get(0);
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5DigestAsHex.equals(user.getPassword())){
            //密码不正确
            return TaotaoResult.build(400,"用户名密码错误");
        }
        //校验成功    生成token   uuid
        String token = UUID.randomUUID().toString();
        //存放数据到 redis中 为了管理方便  加一个前缀
        //设置密码为空
        user.setPassword(null);
        jedisClient.set(USER_INFO + ":" + token, JsonUtils.objectToJson(user));
        //设置key的过期时间。模拟Session的过期时间。一般半个小时。
        jedisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        // 把token 设置到 cookie  在表现层设置
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {

        /**
         * 1.注入 jedisClient
         * 2.调用根据token 查询 用户信息的方法   get方法
         * 3.判断是否能够查询到
         * 4.如果查询不到 返回400
         * 5.如果查询到数据  将数据封装为pojo
         */
        // 2、根据token查询redis。
        String json = jedisClient.get(USER_INFO + ":" + token);
        if (StringUtils.isBlank(json)) {
            // 3、如果查询不到数据。返回用户已经过期。
            return TaotaoResult.build(400, "用户登录已经过期，请重新登录。");
        }
        // 4、如果查询到数据，说明用户已经登录。
        // 5、需要重置key的过期时间。
        jedisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        // 6、把json数据转换成TbUser对象，然后使用TaotaoResult包装并返回。
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return TaotaoResult.ok(user);

    }

    @Override
    public TaotaoResult logout(String token) {
        // 5、需要重置key的过期时间。 为零
        jedisClient.expire(USER_INFO + ":" + token, 0);

        return TaotaoResult.ok();
    }
}
