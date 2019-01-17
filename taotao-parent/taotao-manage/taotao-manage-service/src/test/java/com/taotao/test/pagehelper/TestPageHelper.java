package com.taotao.test.pagehelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.*;
import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
//@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestPageHelper {

//    @Test
//    public void testhelper(){
//
//        //2.初始化spring 容器
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:*.xml,*.properties");
//        //3.获取mapper的代理对象
//        TbItemMapper itemMapper = context.getBean(TbItemMapper.class);
//        //1.设置分页信息
//        PageHelper.startPage(1, 3);//3行  紧跟着的第一个查询才会被分页
//        //4.调用mapper的方法查询数据
//        TbItemExample example = new TbItemExample();//设置查询条件使用
//        List<TbItem> list = itemMapper.selectByExample(example);//select * from tb_item;
//        List<TbItem> list2 = itemMapper.selectByExample(example);//select * from tb_item;
//
//
//        //取分页信息
//        PageInfo<TbItem> info = new PageInfo<>(list);
//
//        System.out.println("第一个分页的list的集合长度"+list.size());
//        System.out.println("第二个分页的list的集合长度"+list2.size());
//
//        //5.遍历结果集  打印
//        System.out.println("查询的总记录数数："+info.getTotal());
//
//        for (TbItem tbItem : list) {
//            System.out.println(tbItem.getId()+"》》》mingch>>"+tbItem.getTitle());
//        }
//
//
//
//    }

    @Test
    public void mains() throws ClassNotFoundException, SQLException {
        String URL="jdbc:mysql://localhost:3306/spring?serverTimezone=Hongkong&useUnicode=true&characterEncoding=utf8&useSSL=false";
        String USER="root";
        String PASSWORD="123456";
        //1.加载驱动程序
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获得数据库链接
        Connection conn= DriverManager.getConnection(URL, USER, PASSWORD);
        //3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("select * from account");
        //4.处理数据库的返回结果(使用ResultSet类)
        while(rs.next()){
            System.out.println(rs.getString("name"));
        }
        //关闭资源
        rs.close();
        st.close();
        conn.close();
    }
}
