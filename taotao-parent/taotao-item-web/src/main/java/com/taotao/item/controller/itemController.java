package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Andre930
 * @create 2019-01-07 18:52
 */
@Controller
public class itemController {

    @Autowired
    private IItemService iItemService;

    @RequestMapping(value = "/item/{itemId}")
    public String getItem(@PathVariable Long itemId, Model model){


        //注入服务
        //调用 服务的方法
        TbItem tbItem = iItemService.getItemById(itemId);
        TbItemDesc tbItemDesc = iItemService.getItemDescById(itemId);

        //商品的基本信息   tbItem  没有getImages
            // 商品的描述信息
        //tbItem  转成item
        Item item = new Item(tbItem);
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",tbItemDesc);

        return "item";
    }
}
