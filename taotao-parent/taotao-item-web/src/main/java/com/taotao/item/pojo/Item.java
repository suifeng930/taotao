package com.taotao.item.pojo;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.taotao.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * 封装返回商品数据的pojo
 * @author Andre930
 * @create 2019-01-07 18:24
 */
public class Item extends TbItem {

    public String[] getImages(){

        if (StringUtils.isNotBlank(super.getImage())){
            //如果有图片调用 TbItem类中的getImage() 并使用split()方法拆分
            return super.getImage().split(",");
        }
        //否则返回null
        return null;
    }

    public Item() {
    }

    /**
     * 将 TbItem 中的数据copy到 item类中
     * @param item
     */
    public Item(TbItem item){
        //将原来数据有的属性值 拷贝到item有的属性中
        BeanUtils.copyProperties(item,this);
    }


}
