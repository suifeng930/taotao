package com.taotao.search.listener;


import com.taotao.common.pojo.SearchItem;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.ISearchService;
import com.taotao.search.service.impl.SearchServiceImpl;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 接受消息的监听器
 */
public class ItemchangeMessageListener implements MessageListener {

    /**
//    //注入service 直接调用方法
    /**
     * 查了下这个问题，发现在使用Spring框架中@Autowired标签时默认情况下使用 @Autowired 注释进行自动注入时，Spring 容器中匹配的候选 Bean 数目必须有且仅有一个。当找不到一个匹配的 Bean 时，Spring 容器将抛BeanCreationException 异常，并指出必须至少拥有一个匹配的 Bean。
     这时的解决办法就是：
     通过 @Qualifier 注释指定注入 Bean 的名称，这样歧义就消除了。
     原文：https://blog.csdn.net/paradise003/article/details/51568386

    @Autowired
//    @Qualifier("ISearchService")
    private ISearchService searchService;

    @Override
    public void onMessage(Message message) {
//判断消息的类型是否为textmessage
        if(message instanceof TextMessage){

            //如果是 获取商品的id
            TextMessage message2 = (TextMessage)message;
            String itemidstr;
            try {
                //获取的就是商品的id的字符串
                itemidstr = message2.getText();
                Long itemId = Long.parseLong(itemidstr);
                //通过商品的id查询数据   需要开发mapper 通过id查询商品(搜索时)的数据
                //更新索引库
                TaotaoResult taotaoResult = searchService.updateItemById(itemId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //判断消息的类型是否是textMessage
    //如果是获取商品的id
    //通过商品的id  查询数据  需要开发mapper  通过id 查询商品搜索时的数据
    //更新索引库
     */

    @Autowired
    private SearchItemMapper itemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        // 从消息中取商品id
        try {
            TextMessage textMessage = (TextMessage) message;
            String strItemId = textMessage.getText();
            // 转换成Long
            Long itemId = new Long(strItemId);
            // 根据商品id来查询商品信息
            SearchItem searchItem = itemMapper.getItemById(itemId);
            // 把商品信息添加到索引库
            SolrInputDocument document = new SolrInputDocument();
            // 为文档添加域
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            document.addField("item_desc", searchItem.getItem_desc());
            // 向索引库中添加文档。
            solrServer.add(document);
            // 提交
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
