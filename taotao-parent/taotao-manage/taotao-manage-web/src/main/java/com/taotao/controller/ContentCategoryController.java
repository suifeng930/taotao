package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.IContentCatogeryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理
 */
@Controller
@RequestMapping(value = "/content/category")
public class ContentCategoryController {

    @Autowired
    private IContentCatogeryService service;


    /**
     *返回内容下拉列表
     * @param parentId  id 值
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(@RequestParam( value = "id",defaultValue = "0") Long parentId){

        return service.getContentCatList(parentId);
    }

    ///create
    @RequestMapping(value = "/create")
    @ResponseBody
    public TaotaoResult CreateContentCat(Long parentId, String name){

        return service.insertContentCat(parentId,name);
    }


}
