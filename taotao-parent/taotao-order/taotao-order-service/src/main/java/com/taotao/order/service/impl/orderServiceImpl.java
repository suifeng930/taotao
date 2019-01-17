package com.taotao.order.service.impl;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.order.service.jedis.JedisClient;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Andre930
 * @create 2019-01-13 22:38
 */
@Service
public class orderServiceImpl implements OrderService {
/**
    @Autowired
    private JedisClient jedisClient;


    @Value("${GEN_ORDER_ID_KEY}")
    private String GEN_ORDER_ID_KEY;
    @Value("${GEN_ORDER_ID_INIT}")
    private String GEN_ORDER_ID_INIT;
    @Value("${GEN_ORDER_ITEM_ID_KEY}")
    private String GEN_ORDER_ITEM_ID_KEY;
    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper itemMapper;
    @Autowired
    private TbOrderShippingMapper shippingMapper;

*/


//
//    @Override
//    public TaotaoResult createOrder(OrderInfo info) {
        /**
         * 1.插入订单表
         *     通过redis的 incr 生成订单id
         *     补全其他属性
         * 2.插入订单项表
         *     设置订单项的id 通过redis的incr  生成订单项id
         * 3.插入订单物流表
         *     设置订单id
         *     补全其他的属性
         *
//         */
//        //判断key 是否存在 不存在 初始化一个key 并设置一个初始值
//        if (!jedisClient.exists(GEN_ORDER_ID_KEY)) {
//            jedisClient.set(GEN_ORDER_ID_KEY,GEN_ORDER_ID_INIT);
//
//        }
//        String orderId = jedisClient.incr(GEN_ORDER_ID_KEY).toString();
//        //补全其他属性
////        info.setBuyerNick(buyNick);  由controller 设置
//        info.setCreateTime(new Date());
//        info.setOrderId(orderId);
//        info.setPostFee("0");//邮费
//        info.setStatus(1);//未付款
////        info.setUserId(); controller 设置
//        info.setUpdateTime(info.getCreateTime());
//        orderMapper.insert(info);
//        //补全其他属性
//        //插入订单项表
//        List<TbOrderItem> orderItems = info.getOrderItems();
//        for (TbOrderItem orderItem : orderItems) {
//            String incr = jedisClient.incr(GEN_ORDER_ITEM_ID_KEY).toString();
//            orderItem.setId(incr);
//            orderItem.setOrderId(orderId);
//            //插入订单项表
//            itemMapper.insert(orderItem);
//        }
//        //插入订单物流表
//        TbOrderShipping shipping = info.getOrderShipping();
//        shipping.setOrderId(orderId);
//        shipping.setCreated(info.getCreateTime());
//        shipping.setUpdated(info.getCreateTime());
//        shippingMapper.insert(shipping);
//
//
//
//        //返回需要包含订单的id
//        return TaotaoResult.ok(orderId);
//    }


        @Autowired
        private TbOrderMapper mapper;
        @Autowired
        private TbOrderItemMapper orderitemmapper;
        @Autowired
        private TbOrderShippingMapper shippingmapper;
        @Autowired
        private JedisClient client;

        @Value("${GEN_ORDER_ID_KEY}")
        private String GEN_ORDER_ID_KEY;

        @Value("${GEN_ORDER_ID_INIT}")
        private String GEN_ORDER_ID_INIT;

        @Value("${GEN_ORDER_ITEM_ID_KEY}")
        private String GEN_ORDER_ITEM_ID_KEY;

        @Override
        public TaotaoResult createOrder(OrderInfo info) {
            //1.插入订单表
            //通过redis的incr 生成订单id
            //判断如果key没存在 需要初始化一个key设置一个初始值
            if(!client.exists(GEN_ORDER_ID_KEY)){
                client.set(GEN_ORDER_ID_KEY, GEN_ORDER_ID_INIT);
            }
            String orderId = client.incr(GEN_ORDER_ID_KEY).toString();
            //补全其他的属性
            //info.setBuyerNick(buyerNick);  在controller设置
            info.setCreateTime(new Date());
            info.setOrderId(orderId);
            info.setPostFee("0");
            info.setStatus(1);
            //info.setUserId(userId);由controller设置
            info.setUpdateTime(info.getCreateTime());
            //注入mapper
            mapper.insert(info);
            //2.插入订单项表
            //补全其他的属性
            List<TbOrderItem> orderItems = info.getOrderItems();
            for (TbOrderItem tbOrderItem : orderItems) {
                //设置订单项的id 通过redis的incr 生成订单项的id
                String incr = client.incr(GEN_ORDER_ITEM_ID_KEY).toString();
                tbOrderItem.setId(incr);
                tbOrderItem.setOrderId(orderId);
                //插入订单项表
                orderitemmapper.insert(tbOrderItem);
            }
            //3.插入订单物流表
            //设置订单id
            TbOrderShipping shipping = info.getOrderShipping();
            //补全其他的属性
            shipping.setOrderId(orderId);
            shipping.setCreated(info.getCreateTime());
            shipping.setUpdated(info.getCreateTime());
            //chauru
            shippingmapper.insert(shipping);
            //返回需要包含订单的ID
            return TaotaoResult.ok(orderId);
        }

    }
