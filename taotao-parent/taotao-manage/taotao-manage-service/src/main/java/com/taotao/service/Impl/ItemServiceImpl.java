package com.taotao.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.IItemService;

import com.taotao.service.jedis.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import javax.jms.*;

import java.util.Date;
import java.util.List;

/**
 * 实现商品查询相关数据
 *
 *
 */
@Service
public class ItemServiceImpl implements IItemService {


    @Autowired
    private TbItemMapper mapper;
    @Autowired
    private TbItemDescMapper descMapper;

    @Autowired
    private JedisClient jedisClient; //
    @Value("${ITEM_INFO_KEY}")
    private String ITEM_INFO_KEY; //redis 前缀
    @Value("${ITEM_INFO_KEY_EXPIRE}")
    private Integer ITEM_INFO_KEY_EXPIRE; // redis  过期时间

    @Autowired
    private JmsTemplate  jmsTemplate;

    @Resource(name = "topicDestination")
    private Destination topicDestination;
     @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {


        //1.设置分页的信息 使用pageHelper
        //2.注入 mapper
        //3.创建一个example对象
        //4.根据mapper 调用查询所有的方法
        //5 获取分页信息
        //6.封装到EasyUIDataGridResult
        //7 返回到 controller

        if (page==null) page=1;
        if (rows==null) rows=30;
        PageHelper.startPage(page,rows);

        TbItemExample example=new TbItemExample();

        List<TbItem> list = mapper.selectByExample(example);

        PageInfo<TbItem> info=new PageInfo<TbItem>(list);

        EasyUIDataGridResult result=new EasyUIDataGridResult();
        result.setTotal((int) info.getTotal());
        result.setRows(info.getList());



        return result;
    }

    @Override
    public TaotaoResult addItem(TbItem item, String desc) {

        //生成商品id;
       final long itemId = IDUtils.genItemId();
        //补全item的属性
        item.setId(itemId);
        //商品状态 1.正常 2. 下架 3.删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //向商品表插入数据
        mapper.insert(item);

        //创建一个商品表述表对应的pojo
        TbItemDesc itemDesc=new TbItemDesc();
        // 补全pojo属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(new Date());
        itemDesc.setCreated(new Date());
        //向商品描述表中插入数据
        descMapper.insert(itemDesc);
        //发送一个商品添加消息
        jmsTemplate.send((javax.jms.Destination) topicDestination, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                //发送的消息
                // 创建一个消息对象
                // 要在匿名内部类访问局部变量itemId，itemId需要用final修饰
                TextMessage message = session.createTextMessage(String.valueOf(itemId));
                return message;
            }
        });


        //返回taotaoResult
        return TaotaoResult.ok();
    }

    @Override
    public TbItem getItemById(Long itemId) {
        /**
         * 1.添加缓存
         * 2.从缓存中获取数据，如果有直接返回
         * 3.如果没有， 去数据库中查询数据，
         * 4.将查询到数据添加到redis中
         * 5.返回数据
         *
         * 需要使用String类型来保存商品数据。可以加前缀方法对redis中的key进行归类。
         * 例如：key:
         *         ITEM_INFO:123456:BASE
         *        ITEM_INFO:123456:DESC
         */
        //判断redis中是否有数据
        try {

            String jsonStr = jedisClient.get(ITEM_INFO_KEY + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(jsonStr)) {
                //存在缓存   则不查数据库直接返回
                //设置商品存在的有效期
                jedisClient.expire(ITEM_INFO_KEY+itemId+":BASE",ITEM_INFO_KEY_EXPIRE); //
                System.out.println("redis中有商品基本信息的缓存数据===========================");
                return  JsonUtils.jsonToPojo(jsonStr,TbItem.class);

            }
        }catch (Exception e1){
            e1.printStackTrace();
        }


        // 注入ItemMapper
        // 调用mapper
        TbItem tbItem = mapper.selectByPrimaryKey(itemId);
        try {
            //添加redis缓存
            //注入 JedisClients
            System.out.println("================ redis中没有商品基本信息的缓存数据===========================");
            jedisClient.set(ITEM_INFO_KEY+itemId+":BASE", JsonUtils.objectToJson(tbItem));//存入json数据类型
            //设置缓存的有效期 为一天
            jedisClient.expire(ITEM_INFO_KEY+itemId+":BASE",   ITEM_INFO_KEY_EXPIRE); //

        }catch (Exception e12){
            e12.printStackTrace();
        }
        //商品数据
        return tbItem;
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        //判断redis中是否有数据
        try {
            String jsonStr = jedisClient.get(ITEM_INFO_KEY + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(jsonStr)) {
                //存在缓存   则不查数据库直接返回
                //设置商品存在的有效期
                jedisClient.expire(ITEM_INFO_KEY+itemId+":DESC",ITEM_INFO_KEY_EXPIRE); //
                System.out.println("redis中有商品描述信息的缓存数据-----------------------");
                return  JsonUtils.jsonToPojo(jsonStr,TbItemDesc.class);
            }
        }catch (Exception e1){
            e1.printStackTrace();
        }
        //没有缓存数据 向数据库中做查询
        TbItemDesc itemDesc = descMapper.selectByPrimaryKey(itemId);
        try {
            //添加redis缓存
            //注入 JedisClients
            System.out.println("----------------- redis中没有商品描述信息的缓存数据------------------");
            jedisClient.set(ITEM_INFO_KEY+itemId+":DESC", JsonUtils.objectToJson(itemDesc));//存入json数据类型
            //设置缓存的有效期 为一天
            jedisClient.expire(ITEM_INFO_KEY+itemId+":DESC",ITEM_INFO_KEY_EXPIRE); //
        }catch (Exception e12){
            e12.printStackTrace();
        }
        //商品数据
        return itemDesc;

    }

}
