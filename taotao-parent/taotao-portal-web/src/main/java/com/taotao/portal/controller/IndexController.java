package com.taotao.portal.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.IContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.Ad1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页展示controller
 */
@Controller
public class IndexController {


    @Autowired
    private IContentService contentService;

    @Value("${AD1_CATEGORY_ID}")
    private Long AD1_CATEGORY_ID;

    @Value("${AD1_HEIGHT}")
    private String AD1_HEIGHT;
    @Value("${AD1_HEIGHT_B}")
    private String AD1_HEIGHT_B;
    @Value("${AD1_WIDTH}")
    private String AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")
    private String AD1_WIDTH_B;



    /**
     * 展示首页
     * 访问时 找到欢迎页 找到index.jsp  页面时 没有找到 则会继续寻找index.html
     * @return 逻辑视图
     */
    @RequestMapping(value = "/index")
    public String showIndex(Model model){
        //1.引入服务
        //2.注入服务
        //3.调用方法 tbcontent的列表
        List<TbContent> list = contentService.getContentList(AD1_CATEGORY_ID);

        //4.转换成ad1node列表
        List<Ad1Node> nodes=new ArrayList<>();
        for ( TbContent tbContent:list) {
            Ad1Node node=new Ad1Node();
            node.setAlt(tbContent.getSubTitle());
            node.setHref(tbContent.getUrl());
            node.setSrc(tbContent.getPic());
            node.setSrcB(tbContent.getPic2());
            node.setHeight(AD1_HEIGHT);
            node.setHeightB(AD1_HEIGHT_B);
            node.setWidth(AD1_WIDTH);
            node.setWidthB(AD1_WIDTH_B);

            nodes.add(node);

        }
        //将数据转换成json数据
        String toJson = JsonUtils.objectToJson(nodes);
        //讲json数据设置到model中
        model.addAttribute("ad1", toJson);
        return "index";
    }


}
