package com.taotao.order.service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;

/**
 * 订单service
 * @author Andre930
 * @create 2019-01-13 22:36
 */
public interface OrderService {

    /**
     * 创建订单
     * 根据orderInfo 掺入订单表 插入订单物流表 插入订单明细表
     * @param info
     * @return
     */
    public TaotaoResult createOrder(OrderInfo info);
}
