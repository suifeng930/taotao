package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.ISearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service("ISearchService")
public class SearchServiceImpl implements ISearchService {


    @Autowired
    private SearchItemMapper mapper;

    @Autowired //注入solr
    private SolrServer solrServer;
    @Autowired
    private SearchDao searchDao;

    @Override
    public TaotaoResult importAllSearchItems() throws Exception {
        // 注入mapper
        List<SearchItem> searchItemList = mapper.getSearchItemList();
        //  通过mapper 的方法查询所有的商品数据
          //1.创建solrserver
         //2.创建solrDocunment 将列表中的元素一个个放到索引库中
        // 3、为每个商品创建一个SolrInputDocument对象。
        for (SearchItem searchItem : searchItemList) {
            SolrInputDocument document = new SolrInputDocument();
            // 4、为文档添加域
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            document.addField("item_desc", searchItem.getItem_desc());
            // 5、向索引库中添加文档。
            solrServer.add(document);
        }
        //提交修改
        solrServer.commit();
        // 6、返回TaotaoResult。
        return TaotaoResult.ok();

    }

    @Override
    public SearchResult search(String queryString, Integer page, Integer rows) throws Exception {
        //1.创建一个solrQuery对象
        SolrQuery query=new SolrQuery();
        //2.设置主查询条件
        if (StringUtils.isNotBlank(queryString)){
            query.setQuery(queryString);
            //如果有查询条件 则根据条件查询

        }else {
            //没有就查询全部
            query.setQuery("*:*");
        }
        //设置过滤条件 设置分页
        if(page==null)page=1;
        if(rows==null)rows=60;
        query.setStart((page-1)*rows); //page-1 *rows
        query.setRows(rows);
        //设置默认的搜索域
        query.set("df","item_keywords");
        //设置高亮显示
        query.setHighlight(true);
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        query.addHighlightField("item_title");//设置高亮显示的域

        //3.调用dao方法 返回的是searchResult  只包含了总记录数和商品的列表
        SearchResult search = searchDao.search(query);

        //4.设置SearchResult 的总页数
        long pageCount=0l;
        pageCount = search.getRecordCount() / rows;
        if ( search.getRecordCount() % rows>0){
            pageCount++;
        }
        search.setPageCount(pageCount);
        //5. 返回

        return search;
    }

    @Override
    public TaotaoResult updateItemById(Long itemId) throws Exception {
        return searchDao.updateItemById(itemId);
    }
}
