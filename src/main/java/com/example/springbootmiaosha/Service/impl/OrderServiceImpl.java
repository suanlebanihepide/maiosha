/*
 * @Author: shenzheng
 * @Date: 2020/6/7 17:54
 */

package com.example.springbootmiaosha.Service.impl;

import com.example.springbootmiaosha.Service.ItemService;
import com.example.springbootmiaosha.Service.OrderService;
import com.example.springbootmiaosha.Service.UserService;
import com.example.springbootmiaosha.Service.model.ItemModel;
import com.example.springbootmiaosha.Service.model.OrderModel;
import com.example.springbootmiaosha.Service.model.UserModel;
import com.example.springbootmiaosha.dao.OrderDOMapper;
import com.example.springbootmiaosha.dao.SequenceDOMapper;
import com.example.springbootmiaosha.dataobject.OrderDO;
import com.example.springbootmiaosha.dataobject.SequenceDO;
import com.example.springbootmiaosha.error.BusinessException;
import com.example.springbootmiaosha.error.EmBusinessError;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Service("orderServiceImpl")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId,Integer promoId, Integer amount) throws BusinessException {
        //1.校验下单状态，下单商品是否存在

        ItemModel itemModel = itemService.getItemModelById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        }
        if (amount <= 0 || amount >= 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量超出限制");
        }
        //校验互动信息
        if(promoId!=null)
        {
            //1.校验活动是否存在这个商品
            if(promoId!=itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
                //2.活动是否进行
            }else if (itemModel.getPromoModel().getStatus()!=2){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动未开始");
            }
        }
        //2.落单减库存，支付减库存

        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);

        if(promoId!=null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setOrderAmount(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

        orderModel.setPromoId(promoId);
        orderModel.setId(generateOrderNo());
        OrderDO orderDO = covertFromOrderModel(orderModel);
        //生成交易流水号
        orderDOMapper.insert(orderDO);
        //加上商品销量

        itemService.increaseSales(itemId,amount);
        return orderModel;
    }




    //在已有事务中开启一个新的事务，确保不会因为上层事务回滚导致发生数据更改
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected String generateOrderNo() {
        //订单号16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息 年月日

        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);
        //中间6位为自增序列

        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getStep() + sequenceDO.getCurrentValue());
        sequenceDOMapper.updateByPrimaryKey(sequenceDO);

        String sequenceString = String.valueOf(sequence);

        for (int i = 0; i < 6 - sequenceString.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceString);

        //后两位分库分表位
        stringBuilder.append("00");

        return stringBuilder.toString();
    }

    private OrderDO covertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) return null;
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderAmount().doubleValue());
        orderDO.setPromoId(orderModel.getPromoId());
        return orderDO;
    }
}
