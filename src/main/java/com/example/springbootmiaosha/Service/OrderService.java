/*
 * @Author: shenzheng
 * @Date: 2020/6/7 17:53
 */

package com.example.springbootmiaosha.Service;

import com.example.springbootmiaosha.Service.model.OrderModel;
import com.example.springbootmiaosha.error.BusinessException;

public interface OrderService {

    //1.通过前端url上传秒杀活动Id,校验对应id的对应商品是否已经开始
    //2.直接下单接口判断对应商品是否有秒杀活动，有则按照秒杀价格下单
    OrderModel createOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;


}
