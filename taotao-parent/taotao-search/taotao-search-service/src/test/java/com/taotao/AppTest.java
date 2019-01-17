package com.taotao;

import static org.junit.Assert.assertTrue;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testSolrJ() throws IOException, SolrServerException {


        //创建SolrServer  建立 连接需要指定的ip地址
        //创建solrducument
        //向文档中添加域
        //讲文档提交到索引库中

        SolrServer solrServer=new HttpSolrServer("http://192.168.25.175:8180/solr");

        SolrInputDocument document=new SolrInputDocument();
        document.addField("id", "test001");
        document.addField("item_title", "测试商品");
        document.addField("item_price", "199");
        solrServer.add(document);
        solrServer.commit();

    }


    @Test
    public  void testQury() throws SolrServerException {
        //创建solrQuery对象
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.175:8180/solr");
        //创建solrQuery对象  设置各种过滤条件  主查询条件 排序
        SolrQuery query=new SolrQuery();
        //设置查询的条件
        query.setQuery("阿尔卡特");
        query.addFilterQuery("item_price:[0 TO 300000]");
        query.set("df","item_title");

        //执行查询
        QueryResponse response = solrServer.query(query);


        //获取结果集
        SolrDocumentList results = response.getResults();
        System.out.println("查询的总记录数："+results.getNumFound());

        //遍历结果集  打印
        for (SolrDocument s:results) {

            System.out.println(s.get("id"));
            System.out.println(s.get("item_title"));
        }
    }
}

