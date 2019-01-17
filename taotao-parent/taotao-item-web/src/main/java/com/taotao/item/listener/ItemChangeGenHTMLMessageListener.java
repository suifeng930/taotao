package com.taotao.item.listener;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.IItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 输出文件的名称：商品id+“.html”
 * 输出文件的路径：工程外部的任意目录。网页访问：使用nginx访问网页。
 * 在此方案下tomcat只有一个作用就是生成静态页面。
 * 工程部署：可以把taotao-item-web部署到多个服务器上。
 * 生成静态页面的时机：商品添加后，生成静态页面。
 * 可以使用Activemq，订阅topic（商品添加）
 * @author Andre930
 * @create 2019-01-08 18:01
 *
 *获取监听器
 * 执行生成静态页面的业务逻辑
 *
 */

public class ItemChangeGenHTMLMessageListener implements MessageListener {

    /**
     * 生成静态网页的逻辑：
     做的事情：准备模板文件，准备数据集，数据集由消息获取商品ID查询数据库获取。

     1、配置freemarker 的配置文件（模板的目录，默认字符集）
     2、获取configuration
     3、设置数据集
     4、加载模板
     5、设置输出目录文件(FileWriter)
     6、生成文件，关闭流（输出文件的名称：商品id+“.html”）
     7、部署http服务器（推荐使用nginx）
     * @param message
     */
    @Autowired
    private FreeMarkerConfigurer configurer;
    @Autowired
    private IItemService iItemService;

    @Override
    public void onMessage(Message message) {

        //1.获取消息 商品的id

        if (message instanceof TextMessage){
            TextMessage message1= (TextMessage) message;
            try {
                Long  itemId = Long.valueOf(message1.getText());
                //2.重数据库中获取数据  可以调用manage 中的服务
                  //引入服务
                 //注入服务
                //调用服务
                TbItem tbItem = iItemService.getItemById(itemId);
                Item item=new Item(tbItem);//转成再页面中显示数据时的pojo
                TbItemDesc itemDesc = iItemService.getItemDescById(itemId);

                //3.生成静态页面  准备好模板 数据集
                genHtmlFreemarker(item,itemDesc);
            } catch (Exception e) {
                e.printStackTrace();
            }




        }
    }

    /**
     * 生成静态页面
     * @param item
     * @param itemDesc
     */
    private void genHtmlFreemarker(Item item, TbItemDesc itemDesc) throws Exception{

        //注入对象

        Configuration configuration=configurer.getConfiguration();

        //创建模板 获取模板文件
        Template template = configuration.getTemplate("item.ftl");

        //创建数据集
        Map model=new HashMap();
        model.put("item",item);
        model.put("itemDesc",itemDesc);

        //输出数据
       // D:\Test\item
        Writer writer=new FileWriter(new File("D:\\Test\\item"+"\\"+item.getId()+".html"));

        template.process(model,writer);

        writer.close();


    }
}
