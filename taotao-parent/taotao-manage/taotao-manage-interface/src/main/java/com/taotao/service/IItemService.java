package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

import java.util.List;

/**
 * 商品相关的处理service
 */
public interface IItemService {

    /**
     * 根据当前页面合每页的函数进行分页查询
     * @param page
     * @param rows
     * @return
     */
    public EasyUIDataGridResult getItemList( Integer page,Integer rows);


    /**
     * 商品添加
     * @param item
     * @param desc
     * @return
     */
    TaotaoResult addItem(TbItem item,String desc);


    /**
     * 根据商品id  查询商品数据
     * @param itemId 商品id
     * @return
     */
    public TbItem getItemById(Long itemId);

    /**
     * 根据商品id 查询数商品的描述字段
     * @param itemId
     * @return
     */
    public TbItemDesc getItemDescById(Long itemId);

}
