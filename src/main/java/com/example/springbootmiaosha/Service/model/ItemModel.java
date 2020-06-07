/*
 * @Author: shenzheng
 * @Date: 2020/6/7 15:06
 */

package com.example.springbootmiaosha.Service.model;

import lombok.Data;
import org.omg.PortableInterceptor.INACTIVE;

import java.math.BigDecimal;

@Data
public class ItemModel {

    private Integer id;
    //商品名称
    private String title;
    //商品价格
    private BigDecimal price;
    //商品库存
    private Integer stock;
    //商品描述
    private String description;
    //销量
    private Integer sales;
    //图片url
    private String imgUrl;

    //使用聚合模型,如果不为空则表示有还未结束的秒杀活动

    private PromoModel promoModel;
}
