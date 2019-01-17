package com.taotao.order.pojo;

import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

/**
 * 使用pojo接收表单的数据。
 可以扩展TbOrder，在子类中添加两个属性一个是商品明细列表，一个是配送信息。
 * @author Andre930
 * @create 2019-01-13 22:34
 */
public class OrderInfo extends TbOrder implements Serializable {

    private List<TbOrderItem> orderItems;//订单项
    private TbOrderShipping orderShipping;//物流信息项
    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }
    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }


}
