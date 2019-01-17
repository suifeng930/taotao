package com.taotao.sso.service.impl;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.IUserRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @author Andre930
 * @create 2019-01-10 17:50
 */
@Service
public class UserRegisterServiceImpl implements IUserRegisterService {

    //注入TbUserMapper
    @Autowired
    private TbUserMapper userMapper;


    @Override
    public TaotaoResult checkData(String param, Integer type) {
        /**
         * 1.注入mapper
         * 2.根据参数动态生成查询的条件
         * 3.调用mapper 的查询方法 获取数据
         * 4.如果查询到数据   数据不可用  返回 false
         * 5.如果没有查询到数据    数据是可用的  返回 true
         */

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        if(type==1){
            if(StringUtils.isEmpty(param)){
                //username  为空  直接返回
                return TaotaoResult.ok(false);
            }
            //param   ==》username
            criteria.andUsernameEqualTo(param);
        }else if(type==2){
            //param  ==> phone
            criteria.andPhoneEqualTo(param);
        }else if (type==3){
            // param  ==> email
            criteria.andEmailEqualTo(param);
        }else {
            //是非法参数  返回
            return TaotaoResult.build(400,"非法的参数");
        }
        //执行查询方法
        List<TbUser> userList = userMapper.selectByExample(example);
        //判断是否有数据
        if (userList!=null&& userList.size()>0){
            //查询到数据   则表明数据不可用 返回false
            return TaotaoResult.ok(false);
        }
        //没有查询到数据   则表明数据可用 返回true
        return TaotaoResult.ok(true);
    }

    @Override
    @Transactional(readOnly = false)
    public TaotaoResult register(TbUser user) {
        /**
         * 1.注入mapper
         * 2.校验数据
         * 3.校验成功  补全其他的属性
         * 4.对密码进行md5加密
         * 5.插入数据库
         * 6.返回 taotaoResult
         */
        //判断用户名和密码是否为空 如果用户名 和密码为空直接返回
        if(StringUtils.isEmpty(user.getUsername())){
            return TaotaoResult.build(400,"注册失败，请校验数据后请再提交数据");
        }
        if(StringUtils.isEmpty(user.getPassword())){
            return TaotaoResult.build(400,"注册失败，请校验数据后请再提交数据");
        }
        //校验用户名是否存在
        TaotaoResult result = checkData(user.getUsername(), 1);
        if (!(Boolean) result.getData()){
            //数据不可用
            return TaotaoResult.build(400, "用户名已经被注册");
        }
        //校验电话号码
        if (StringUtils.isNotBlank(user.getPhone())){
            TaotaoResult result2 = checkData(user.getPhone(), 2);
            if (!(boolean)result2.getData()){
                //数据不可用
                return TaotaoResult.build(400, "手机已经被注册");
            }
        }
        //校验emali
        if(StringUtils.isNotBlank(user.getEmail())){
            TaotaoResult result1 = checkData(user.getEmail(), 3);
            if (!(boolean)result1.getData()){
                //数据不可用
                return TaotaoResult.build(400, "邮箱已经被注册");

            }
        }
        //校验成功 补全其他属性
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        //对密码进行加密
        String md5Pass= DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //插入数据
        userMapper.insertSelective(user);
        //返回数据

        return TaotaoResult.ok();
    }
}
