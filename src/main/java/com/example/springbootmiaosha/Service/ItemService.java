package com.example.springbootmiaosha.Service;

import com.example.springbootmiaosha.Service.model.ItemModel;
import com.example.springbootmiaosha.error.BusinessException;

import java.util.List;

public interface ItemService {

    ItemModel createItemModel(ItemModel itemModel);

    List<ItemModel> listItem();

    ItemModel getItemModelById(Integer id);

    boolean decreaseStock(Integer itemId,Integer amount)throws BusinessException;

    //商品销量增加
    void increaseSales(Integer itemId,Integer amount)throws BusinessException;
}
