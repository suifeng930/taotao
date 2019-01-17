package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;

import java.util.List;

public interface IContentCatogeryService {

    /**
     * 通过节点的id 查询该节点的子节点列表
     * @param parentId
     * @return
     */
    List<EasyUITreeNode> getContentCatList(long parentId);
    //实现新增节点这个功能

    /**
     * 添加内容分类
     * 父节点的id
     * 新增节点的名称
     * @param parentId 父节点的id
     * @param name 新增分类的名称
     * @return
     */
    TaotaoResult insertContentCat(long parentId,String name);


}
