package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 显示页面
 */
@Controller
public class PageController {


    /**
     * 展示首页
     * @return
     */
    @RequestMapping("/")
    public String showIndex(){

        return "index";
    }

    /**
     * 显示商品管理  商品的查询页面
     * @return
     * url  iten-list  item -add
     */
    @RequestMapping("/{page}")
    public String showpage(@PathVariable String page){

        return page;
    }




}
