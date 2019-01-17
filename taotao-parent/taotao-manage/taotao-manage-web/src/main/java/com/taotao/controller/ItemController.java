package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private IItemService service;


    /**
     *
     * @param page 页数
     * @param rows 行数
     * @return jsom
     */
    @RequestMapping(value = "/item/list",method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page,Integer rows){

        //1. 引入服务
        //2. 注入服务
        //3. 调用服务的方法

        EasyUIDataGridResult itemList = service.getItemList(page, rows);
        return  itemList;

    }


    @RequestMapping(value = "/item/save")
    @ResponseBody
    public TaotaoResult addItem(TbItem item,String desc){

        TaotaoResult taotaoResult = service.addItem(item, desc);

        return taotaoResult;
    }
}
