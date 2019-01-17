package com.taotao.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传controller
 */
@Controller
public class PickController {

    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping(value = "/pic/upload")
    @ResponseBody //响应json
    public String picUpload(MultipartFile uploadFile){

        //接受上传文件
        //去扩展名
        //上传到图片服务器
        //响应上传图片的url  格式：

        try {
            String oldFileName = uploadFile.getOriginalFilename();// 获取到文件名
            String extName=oldFileName.substring(oldFileName.lastIndexOf(".")+1); //分割扩展名 截取最后的扩展名
            FastDFSClient fastDFSClient=new FastDFSClient("classpath:resource.conf");

            String fileUrl = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);//group1/M00/00/00/wKgZr1wovRSAHI_rAACok8pSPEw721.jpg
            fileUrl=IMAGE_SERVER_URL+fileUrl;//完整url路径
            Map result=new HashMap();
            //响应url 到页面
            result.put("error",0);
            result.put("url",fileUrl);
            return JsonUtils.objectToJson(result); //返回json 数据

        } catch (Exception e) {
            e.printStackTrace();
            Map result=new HashMap();
            //响应url 到页面
            result.put("error",1);
            result.put("message","图片上传失败");
            return JsonUtils.objectToJson(result);
        }
    }


}
