package com.taotao.sso.service;

import com.taotao.common.utils.TaotaoResult;

/**
 * 用户登录
 * @author Andre930
 * @create 2019-01-10 21:04
 */
public interface IUserLoginService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return  TaotaoResult，包装token。 登录成功返回200  登录失败 返回400
     */
    public TaotaoResult login(String username, String password);

    /**
     * 查看用户登录状态  根据 token 获取用户的信息
     * @param token  sessionid
     * @return  taotaoResult 应该包含用户是信息
     */
    public TaotaoResult getUserByToken(String token);

    /**
     * 安全退出
     * @param token  设置token失效 及就是 删除redis 缓存
     * @return
     */
    public TaotaoResult logout(String token);
}
