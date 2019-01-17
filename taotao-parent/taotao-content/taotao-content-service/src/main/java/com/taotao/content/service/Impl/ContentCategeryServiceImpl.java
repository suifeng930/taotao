package com.taotao.content.service.Impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.IContentCatogeryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理服务
 */
@Service
public class ContentCategeryServiceImpl implements IContentCatogeryService {

    @Autowired
    private TbContentCategoryMapper mapper;


    @Override
    public List<EasyUITreeNode> getContentCatList(long parentId) {

        //注入mapper
        //创建example
        //设置条件
        //执行查询
        //转成 EasyUITreeNode
        //返回数据
        TbContentCategoryExample example = new TbContentCategoryExample();
        //设置查询条件
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        // 3、得到List<TbContentCategory>
        List<TbContentCategory> list = mapper.selectByExample(example);
        // 4、把列表转换成List<EasyUITreeNode>ub
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbContentCategory tbContentCategory : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            //添加到列表
            resultList.add(node);
        }
        return resultList;


    }

    @Override
    public TaotaoResult insertContentCat(long parentId, String name) {
        // 创建一个内容分类对象
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setName(name);
        contentCategory.setParentId(parentId);
        // 新添加的节点都是叶子节点
        contentCategory.setIsParent(false);
        // 排序方法默认设置为1
        contentCategory.setSortOrder(1);
        // 分类状态：1(正常)，2(删除)
        contentCategory.setStatus(1);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        // 插入节点
        mapper.insert(contentCategory);
        // 判断父节点是否为叶子节点，为何要判断？
        TbContentCategory parentNode = mapper.selectByPrimaryKey(parentId);
        if (!parentNode.getIsParent()) {
            parentNode.setIsParent(true);
            // 更新父节点
            mapper.updateByPrimaryKey(parentNode);
        }
        return TaotaoResult.ok(contentCategory); // contentCategory对象里面是包含id属性的

    }
}
