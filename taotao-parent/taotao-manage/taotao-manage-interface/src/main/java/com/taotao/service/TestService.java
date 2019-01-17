package com.taotao.service;


import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;

import java.util.List;

public interface TestService {
    /**
     * 从数据库中获取当前的时间
     * @return
     */
    public String queryNow();

    List<TbUser> selectByExample(TbUserExample example);


    List<TbUser> findAllUser();

}
