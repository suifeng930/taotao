package com.taotao.content.service.Impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.IContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentSErviceImpl implements IContentService {
    @Autowired
    private TbContentMapper mapper;


    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;


    /**
     * 对内容信息做增删改操作后只需要把对应缓存key删除即可。
     * @param tbContent vo 对象
     * @return
     */

    @Override
    public TaotaoResult insertContent(TbContent tbContent) {

        // 补全pojo的属性
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        // 向内容表中插入数据
        mapper.insert(tbContent);
        //缓存同步
        jedisClient.hdel(CONTENT_KEY, tbContent.getCategoryId().toString());

        return TaotaoResult.ok();


      }

    @Override
    public List<TbContent> getContentList(long categoryId) {

        //加入jedis  缓存
      //查询缓存
        try {
            String json = jedisClient.hget(CONTENT_KEY, categoryId + "");
            //判断json是否为空
            if (StringUtils.isNotBlank(json)) {
                //把json转换成list
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                System.out.println("=====================redis 有缓存");
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //根据cid查询内容列表
        TbContentExample example = new TbContentExample();
        //设置查询条件
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //执行查询
        List<TbContent> list = mapper.selectByExample(example);
        //向缓存中添加数据
        try {
            System.out.println("-----------------没有缓存");
            jedisClient.hset(CONTENT_KEY, categoryId + "", JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
