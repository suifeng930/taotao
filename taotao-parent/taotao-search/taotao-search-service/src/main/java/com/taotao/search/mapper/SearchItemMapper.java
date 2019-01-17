package com.taotao.search.mapper;

import com.taotao.common.pojo.SearchItem;

import java.util.List;

/**
 * 定义mapper 关联查询3张表  查询数搜索时的商品数据
 */

public interface SearchItemMapper {
    /**
     * 查询 所有商品的数据
     * @return list
     */
    public List<SearchItem> getSearchItemList();

    /**
     * 根据商品id查询商品数据。
     * @param itemId
     * @return
     */
    public SearchItem getItemById(Long itemId);

}
