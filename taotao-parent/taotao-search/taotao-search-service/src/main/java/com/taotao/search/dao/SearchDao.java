package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 搜索商品的dao  从索引库中搜索
 */
@Repository
public class SearchDao {


    @Autowired
    private SolrServer solrServer;

    @Autowired
    private SearchItemMapper mapper;

    /**
     * 根据查询条件查询商品 的结果集
     * @param query
     * @return
     * @throws Exception
     */
    public SearchResult search(SolrQuery query)throws Exception{

        SearchResult searchResult=new SearchResult();
        //1.创建SolrServer对象 由spring管理注入
        //2.直接执行查询
        QueryResponse response = solrServer.query(query);
        //3.获取结果集
        SolrDocumentList results = response.getResults();
        //设置searchResult的总记录数
        searchResult.setRecordCount(results.getNumFound());
        //4.遍历结果集
        List<SearchItem> itemList=new ArrayList<>();

        //去高亮
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SolrDocument document:results) {
            // 将document 中的属性 一个个设置到searchitem中
            SearchItem item=new SearchItem();
            item.setId(Long.parseLong(document.get("id").toString())); //转换成long类型
            item.setCategory_name((String) document.get("item_category_name"));
            item.setImage((String) document.get("item_image"));
            item.setPrice((long) document.get("item_price"));
            item.setSell_point((String) document.get("item_sell_point"));
            //设置高亮显示
            List<String> list = highlighting.get(document.get("id")).get("item_title");
            //判断list是否为空
            String gaoliang="";
            if(list!=null && list.size()>0){
                //有高亮设置
                gaoliang=list.get(0);
            }else {
                //没有高亮
                gaoliang= (String) document.get("item_title");
            }
            item.setTitle(gaoliang);
            itemList.add(item);

            // 再将searchItem 封装到SearchResult中的itemList属性中
        }
        //5.设置searchResult的属性
        searchResult.setItemList(itemList);
        return searchResult;
    }


    /**
     *  * 更新索引库
     * 根据id  查询数据库 更新到索引库
     * @param itemId 商品id
     * @return
     * @throws Exception
     */
    public TaotaoResult updateItemById(Long itemId) throws Exception {
        //1.调用mapper中的方法
        SearchItem searchItem = mapper.getItemById(itemId);
        //2.创建solrinputdocument
        SolrInputDocument document = new SolrInputDocument();
        //3.向文档中添加域
        document.addField("id", searchItem.getId());
        document.addField("item_title", searchItem.getTitle());
        document.addField("item_sell_point", searchItem.getSell_point());
        document.addField("item_price", searchItem.getPrice());
        document.addField("item_image", searchItem.getImage());
        document.addField("item_category_name", searchItem.getCategory_name());
        document.addField("item_desc", searchItem.getItem_desc());
        //4.添加文档到索引库中
        solrServer.add(document);
        //5.提交
        solrServer.commit();
        return TaotaoResult.ok();
    }

}
