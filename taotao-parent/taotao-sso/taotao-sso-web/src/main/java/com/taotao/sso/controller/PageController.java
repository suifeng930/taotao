package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 跳转页面
 * @author Andre930
 * @create 2019-01-11 10:40
 */
@Controller
public class PageController {



    @RequestMapping(value = "/page/{page}")
    public  String showPage(@PathVariable String page, String redirect, Model model){

        System.out.println(redirect);
        model.addAttribute("redirect",redirect);//把url 返回到页面

        return page;
    }
}
