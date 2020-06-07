/*
 * @Author: shenzheng
 * @Date: 2020/6/7 15:28
 */

package com.example.springbootmiaosha.Service.impl;

import com.example.springbootmiaosha.Service.ItemService;
import com.example.springbootmiaosha.Service.PromoService;
import com.example.springbootmiaosha.Service.model.ItemModel;
import com.example.springbootmiaosha.Service.model.PromoModel;
import com.example.springbootmiaosha.dao.ItemDOMapper;
import com.example.springbootmiaosha.dao.ItemStockDOMapper;
import com.example.springbootmiaosha.dataobject.ItemDO;
import com.example.springbootmiaosha.dataobject.ItemStockDO;
import com.example.springbootmiaosha.error.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service("itemServiceImpl")
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDOMapper itemDOMapper;


    @Autowired
    private ItemStockDOMapper itemStockDOMapper;
    @Autowired
    private PromoService promoService;

    private ItemDO convertItemDOFromItemModel(ItemModel itemModel) {

        if (itemModel == null) return null;
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }

    private ItemStockDO convertItemStockDoFromItemModel(ItemModel itemModel) {

        if (itemModel == null) return null;
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    @Override
//    @Transactional
    public ItemModel createItemModel(ItemModel itemModel) {

        //校验入参

        //利用ValidationResult
        //转化itemModel->dataObject

        ItemDO itemDO = this.convertItemDOFromItemModel(itemModel);
        //写入
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());

        ItemStockDO itemStockDO = this.convertItemStockDoFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        //返回
        return getItemModelById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> list = itemDOMapper.listItem();
        List<ItemModel> itemModels = list.stream().map(itemDO -> {

            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = this.convertModelFromDataObject(itemDO, itemStockDO);
            return itemModel;

        }).collect(Collectors.toList());
        return itemModels;
    }

    @Override
    public ItemModel getItemModelById(Integer id) {

        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null) {
            return null;
        }

        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());

        ItemModel itemModel = convertModelFromDataObject(itemDO, itemStockDO);
        //将dataobject->model
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());

          if(promoModel!=null&&promoModel.getStatus()!=3){
              itemModel.setPromoModel(promoModel);
          }
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {

        int result = itemStockDOMapper.decreaseStock(itemId, amount);
        if (result <= 0) return false;

        return true;
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {

        itemDOMapper.increaseSales(itemId, amount);
    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO) {
        ItemModel itemModel = new ItemModel();
        if (itemDO == null || itemStockDO == null) return null;

        BeanUtils.copyProperties(itemDO, itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
