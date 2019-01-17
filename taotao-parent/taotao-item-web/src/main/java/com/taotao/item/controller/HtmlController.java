package com.taotao.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andre930
 * @create 2019-01-08 15:14
 */
@SuppressWarnings("ALL")
@Controller
public class HtmlController {


    @Autowired
    private FreeMarkerConfigurer config;
    /**
     * 生成静态页面
     * @return
     */
    @RequestMapping(value = "/genHtml")
    @ResponseBody
    public String genHtml() throws Exception{
        //生成静态页面

        //根据config 获取configuration 对象
        Configuration configuration= config.getConfiguration();
        //根据config 获取模板文件对象  加载模板文件
        Template template = configuration.getTemplate("Template.ftl");
        //创建数据集    从数据库中获取
        Map model=new HashMap();
        model.put("springTestKey","hello");
        Writer writer=new FileWriter(new File("D:\\Test\\freemarker.html"));
        template.process(model,writer);
        writer.close();
        //创建流对象
        //遍历方法输入
        //关闭流资源

        return "ok";

    }
}
