package com.taotao.service.Impl;


import com.taotao.mapper.ITestMapper;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private ITestMapper testMapper;
    @Autowired
    private TbUserMapper userMapper;
    @Override
    public String queryNow() {
        System.out.println("55555555555555");
        /**
         * 查询当前时间
         */
        return testMapper.queryNow();
    }

    @Override
    public List<TbUser> selectByExample(TbUserExample example) {
        TbUserExample example1=new TbUserExample();
        return userMapper.selectByExample(example1);
    }

    @Override
    public List<TbUser> findAllUser() {
        TbUserExample example = new TbUserExample();
        return  userMapper.selectByExample(example);
    }
}
