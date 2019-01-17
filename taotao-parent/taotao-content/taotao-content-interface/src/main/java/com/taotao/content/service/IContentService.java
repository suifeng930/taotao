package com.taotao.content.service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * 内容处理接口
 */
public interface IContentService {

    /**
     *插入内容表记录
     * @param tbContent vo 对象
     * @return
     */
    public TaotaoResult insertContent(TbContent tbContent);

    /**
     * 根据商品id 查询首页打广告列表
     * @param categoryId
     * @return list
     */
    public List<TbContent> getContentList(long categoryId);
}
