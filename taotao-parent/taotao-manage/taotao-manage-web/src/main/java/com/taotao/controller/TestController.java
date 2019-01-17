package com.taotao.controller;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.service.TestService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class TestController {
    @Resource
    private TestService TestService;

    /**
     * 测试dubbo配置是否正常
     * @return
     * http://localhost:8081/test/queryNow
     */
    @RequestMapping("/test/queryNow")
    @ResponseBody
    public String queryNow(){
        return TestService.queryNow();
    }

    @RequestMapping("/test/query")
    @ResponseBody
    public List<TbUser> query(){
        return TestService.findAllUser();
    }
}
