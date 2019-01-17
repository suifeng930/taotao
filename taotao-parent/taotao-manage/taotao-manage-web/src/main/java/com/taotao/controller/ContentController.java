package com.taotao.controller;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.IContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理内容相关
 */
@Controller
@RequestMapping(value = "/content")
public class ContentController {


    @Autowired
    private IContentService service;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody //返回json数据
    public TaotaoResult saveContent(TbContent content){


        return service.insertContent(content);
    }


}
