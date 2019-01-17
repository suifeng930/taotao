package com.taotao.controller;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.search.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 添加solr索引库
 */
@Controller
public class ImportAllItems {


    @Autowired
    private ISearchService searchService;


    /**
     * 导入所有的商品数据到索引库中
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/index/importall")
    @ResponseBody
    public TaotaoResult ImportAllItems() throws Exception {

        //引入服务
        //注入服务
        //调用方法


        return searchService.importAllSearchItems();
    }
}
