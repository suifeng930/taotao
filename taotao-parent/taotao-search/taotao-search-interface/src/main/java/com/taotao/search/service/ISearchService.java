package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.utils.TaotaoResult;



public interface ISearchService {

    /**
     * 导入所有的商品数据到索引库中
     * @return
     */
    public TaotaoResult importAllSearchItems() throws Exception;

    /**
     *
     * 根据搜索的田间 搜索结果
     * @param queryString 查询的主条件
     * @param page 查询的当前页码
     * @param rows 每页显示的行数 这个再controller中写死就行了
     * @return
     * @throws Exception
     */
    public SearchResult search( String queryString,Integer page,Integer rows)throws Exception;

    //根据商品的id查询商品的数据，并且更新到索引库中

    /**
     *
     * @param itemId
     * @return
     * @throws Exception
     */
    public TaotaoResult updateItemById(Long itemId) throws Exception;

}
