package com.taotao.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 */
public class GlobleExceptionResolver implements HandlerExceptionResolver {

    Logger logger = LoggerFactory.getLogger(GlobleExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        //1.日志写入到日志文件中

        logger.error("系统发生异常",ex);

        //打印到控制台
        System.out.println(ex.getMessage());
        ex.printStackTrace();
        //2.及时通知开发人员
        // 通过第三方接口 发送短信 邮件等
        ModelAndView modelAndView=new ModelAndView();
        //设置模型数据
        modelAndView.addObject("message","系统发生异常，请稍后重试");
        //3.给用户一个友好提示页面
        //返回逻辑视图
        modelAndView.setViewName("error/exception");

        return modelAndView;
    }
}
