/*
 * @Author: shenzheng
 * @Date: 2020/6/7 17:43
 */

package com.example.springbootmiaosha.Service.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderModel {

    //订单ID
    private String id;
    //购买用户ID
    private Integer userId;
    //购买商品ID
    private Integer itemId;
    //购买商品的单价
    private BigDecimal itemPrice;
    //购买数量
    private Integer amount;
    //购买金额
    private BigDecimal orderAmount;

    private Integer promoId;

}
