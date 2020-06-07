/*
 * @Author: shenzheng
 * @Date: 2020/6/7 22:17
 */

package com.example.springbootmiaosha.Service.impl;

import com.example.springbootmiaosha.Service.PromoService;
import com.example.springbootmiaosha.Service.model.PromoModel;
import com.example.springbootmiaosha.dao.PromoDOMapper;
import com.example.springbootmiaosha.dataobject.PromoDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDOMapper promoDOMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {

        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        PromoModel promoModel = convertFromDataObject(promoDO);
        if(promoModel==null)return null;

        Date now = new Date();
        if(now.before(promoModel.getStartDate()))
        {
            promoModel.setStatus(1);
        }else if(now.after(promoModel.getEndDate())){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    private PromoModel convertFromDataObject(PromoDO promoDO) {

        if (promoDO == null) return null;

        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);

        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));

        promoModel.setStartDate(promoDO.getStartDate());
        promoModel.setEndDate(promoDO.getEndDate());

        return promoModel;
    }
}
