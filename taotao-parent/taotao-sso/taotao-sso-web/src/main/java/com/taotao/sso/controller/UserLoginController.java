package com.taotao.sso.controller;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.sso.service.IUserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录
 * @author Andre930
 * @create 2019-01-10 21:30
 */
@Controller
public class UserLoginController {


   @Autowired
    private IUserLoginService userLoginServiceImpl;

    @Value("${TT_TOKEN_KEY}")
    private String COOKIE_TOKEN_KEY;

    @RequestMapping(value = "/user/login" ,method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username, String password,
                              HttpServletRequest request, HttpServletResponse response) {
        // 1、接收两个参数。
        // 2、调用Service进行登录。
        TaotaoResult result = userLoginServiceImpl.login(username, password);
        // 3、从返回结果中取token，写入cookie。Cookie要跨域。
        String token = result.getData().toString();
        CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
        // 4、响应数据。Json数据。TaotaoResult，其中包含Token。
        return result;

    }

    /**
     * 请求的url：/user/token/{token}
     参数：String token需要从url中取。
     返回值：json数据。使用TaotaoResult包装Tbuser对象。

     * @param token
     * @return
     */
    @RequestMapping(value = "/user/token/{token}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback) {
        /**
         * 1、接收callback参数，取回调的js的方法名。
         2、业务逻辑处理。
         3、响应结果，拼接一个js语句。
         */
        // 判断是否是jsonp请求
        if(StringUtils.isNotBlank(callback)){
            // 如果是jsonp 需要拼接成类似 fun（{id:1}）
            TaotaoResult userByToken = userLoginServiceImpl.getUserByToken(token);
            String jsonpstr=callback+"("+ JsonUtils.objectToJson(userByToken)+")";
            return jsonpstr;

        }
        TaotaoResult result = userLoginServiceImpl.getUserByToken(token);

        return JsonUtils.objectToJson(result);
    }

    /**
     * 安全退出
     * @param token
     * @return
     */

    @RequestMapping(value = "/user/logout/{token}",method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult logout(@PathVariable  String token){

        TaotaoResult logout = userLoginServiceImpl.logout(token);
        return logout;
    }


}
