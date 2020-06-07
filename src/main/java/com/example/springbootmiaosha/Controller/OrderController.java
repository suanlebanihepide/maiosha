/*
 * @Author: shenzheng
 * @Date: 2020/6/7 18:42
 */

package com.example.springbootmiaosha.Controller;

import com.example.springbootmiaosha.Controller.response.CommonReturnType;
import com.example.springbootmiaosha.Service.OrderService;
import com.example.springbootmiaosha.Service.model.OrderModel;
import com.example.springbootmiaosha.Service.model.UserModel;
import com.example.springbootmiaosha.error.BusinessException;
import com.example.springbootmiaosha.error.EmBusinessError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //封装下单请求
    @RequestMapping("/createOrder")
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam("itemId") Integer itemId,
                                        @RequestParam(value = "promoId",required = false)Integer promoId,
                                        @RequestParam("amount") Integer amount, HttpServletRequest request) throws BusinessException {

        boolean isLogin = (boolean) request.getSession().getAttribute("IS_LOGIN");
        if (!isLogin) {

            throw new BusinessException(EmBusinessError.USER_NOT_EXIST, "用户未登录");
        }

      UserModel userModel = (UserModel) request.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, promoId,amount);
        return CommonReturnType.create(null);
    }
}
