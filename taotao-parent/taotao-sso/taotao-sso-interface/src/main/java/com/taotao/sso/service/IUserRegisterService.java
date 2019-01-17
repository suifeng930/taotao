package com.taotao.sso.service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbUser;

/**
 * 用户注册的接口
 * @author Andre930
 * @create 2019-01-10 17:47
 */
public interface IUserRegisterService {

    /**
     * 根据参数和类型进行校验
     * @param param   要校验的数据
     * @param type 1  2   3   username  phone  email
     * @return
     */
    public TaotaoResult  checkData(String param,Integer type);

    /**
     * 用户注册
     * @param user
     * @return
     */
    public TaotaoResult register(TbUser user);
}
