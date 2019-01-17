package com.taotao.search.controller;


import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Value("${ITEM_ROWS}")
    private Integer ITEM_ROWS;

    @Autowired
    private ISearchService iSearchService;
    /**
     *根据条件搜索商品数据
     * @param page
     * @param queryString
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/search")
    public String search(@RequestParam(defaultValue = "1") Integer page, @RequestParam(value = "q") String queryString, Model model)throws  Exception{


        //处理中文乱码
//        queryString = new String(queryString.getBytes("ISO8859-1"),"utf-8");
        //引入数据
        //注入dubbo
        //调用
        SearchResult result = iSearchService.search(queryString, page, ITEM_ROWS);
        //设置数据传递到jsp中
        model.addAttribute("query",queryString);
        model.addAttribute("totalPages",result.getPageCount());//总页数
        model.addAttribute("itemList",result.getItemList());
        model.addAttribute("page",page);

        return "search";
    }
}
