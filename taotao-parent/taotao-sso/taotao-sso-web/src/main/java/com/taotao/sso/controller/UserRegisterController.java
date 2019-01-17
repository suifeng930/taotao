package com.taotao.sso.controller;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.IUserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户校验
 * @author Andre930
 * @create 2019-01-10 18:06
 */
@Controller
public class UserRegisterController {

    //注入service
    @Autowired
    private IUserRegisterService registerService;

    /**
     * url ：/user/check/{param}/{type}
     * @param param
     * @param type 1  2  3
     * @return
     */
    @RequestMapping(value = "/user/check/{param}/{type}",method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult checkData(@PathVariable String param, @PathVariable Integer type){

        return registerService.checkData(param,type);
    }

    /**
     * 注册用户
     * 业务逻辑：
     1、使用TbUser接收提交的请求。
     2、补全TbUser其他属性。
     3、密码要进行MD5加密。
     4、把用户信息插入到数据库中。
     5、返回TaotaoResult。
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser user){

        return registerService.register(user);
    }
}
