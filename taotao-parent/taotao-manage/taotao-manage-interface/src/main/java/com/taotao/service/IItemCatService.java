package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * 商品分类管理
 */
public interface IItemCatService {
    /**
     * 根据parentId id 查询商品分类信息
     * @param parentId
     * @return  list 集合
     */
    List<EasyUITreeNode> getItemCast(long parentId);
}
