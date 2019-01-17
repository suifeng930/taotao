package com.taotao.order.interceptor;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.sso.service.IUserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录身份认证的拦截器
 * @author Andre930
 * @create 2019-01-13 16:46
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;
    @Value("${SSO_URL}")
    private  String SSO_URL; //http://localhost:8088

    @Autowired
    private IUserLoginService loginService;
    //进入目标方法之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {


        //用户的身份认证 在此验证

        //1.取cookie中的token
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        //2.判断token是否存在
        if (StringUtils.isEmpty(token)){
            //3.如果不存在   说明没登录   》》重定向到登录的页面
            //request.getRequestURI().toString() 访问的url   localhost :8092/order/order-cart.html
            response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURI().toString());
            return  false;
        }
        //4.如果token存在   调用sso的服务  查询用户的信息  查看用户是否已经过期
        TaotaoResult result = loginService.getUserByToken(token);
        if (result.getStatus()!=200){
            //5.用户已经过期  》》重定向到登录的页面
            response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURI().toString());
            return false;
        }
        //6.用户没有过期    放行
        //设置用户信息到request域中 目标方法的request就可以获取到用户的信息
        request.setAttribute("USER_INFO",result.getData());
        return true;
    }

    //进入目标方法之后，在返回modelAndView之前执行

    //公用方法的设置
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    //返回ModelAndView之后,渲染页面之前执行
    //异常处理  日志  清理工作
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
