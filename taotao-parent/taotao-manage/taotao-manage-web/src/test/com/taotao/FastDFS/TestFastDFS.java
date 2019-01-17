package com.taotao.FastDFS;

import com.taotao.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

public class TestFastDFS {


    @Test
    public void uploadFile()throws Exception{

        //1.向工程中添加jar包
        //2.创建一个配置文件 配置tracker服务器地址
        //3.加载配置文件
        //4.创建一个trackerClient对象
        //5.使用trackerClient对象获得trackerServer对象
        //6.创建一个stoagerServer的引用 null  就行了
        //7.创建一个stroagerClient对象 trackerServer  stoagerServer对象  group1/M00/00/00/wKgZr1wouyiAWUttAAEoqeoPLvQ136.jpg
        //8.创建一个stoagerClient 上传文件
        ClientGlobal.init("D:\\Java\\Demo\\taotao\\taotao-parent\\taotao-manage\\taotao-manage-web\\src\\main\\resources\\resource.conf");
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer=null;
        StorageClient storageClient=new StorageClient(trackerServer,storageServer);
        String[] jpgs = storageClient.upload_file("C:\\Users\\Andre930\\Pictures\\Saved Pictures\\asda (2).jpg", "jpg", null);
        for ( String  string:jpgs) {
            System.out.println("获取到的文件路径：");
            System.out.println(string);
        }


    }

    @Test
    public void testFastDFSClient() throws Exception{

        FastDFSClient fastDFSClient=new FastDFSClient("D:\\Java\\Demo\\taotao\\taotao-parent\\taotao-manage\\taotao-manage-web\\src\\main\\resources\\resource.conf");
        String s = fastDFSClient.uploadFile("C:\\Users\\Andre930\\Pictures\\Saved Pictures\\asda (5).jpg", "jpg", null);
        System.out.println(s);
    }
}
