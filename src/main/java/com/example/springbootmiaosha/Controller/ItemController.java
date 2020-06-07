/*
 * @Author: shenzheng
 * @Date: 2020/6/7 15:46
 */

package com.example.springbootmiaosha.Controller;

import com.example.springbootmiaosha.Controller.response.CommonReturnType;
import com.example.springbootmiaosha.Controller.vo.ItemVo;
import com.example.springbootmiaosha.Service.ItemService;
import com.example.springbootmiaosha.Service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller("item")
@RequestMapping("/item")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {CONTENT_TYPE})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam("title") String title,
                                       @RequestParam("description") String description,
                                       @RequestParam("price") BigDecimal price,
                                       @RequestParam("stock") Integer stock,
                                       @RequestParam("imgUrl") String imgUrl) {

        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelForReturn = itemService.createItemModel(itemModel);


        ItemVo itemVo = convertVoFromModel(itemModelForReturn);
        return CommonReturnType.create(itemModel);
    }


    //商品详情页
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getItem(@RequestParam("id") Integer id) {

        ItemModel itemModel = itemService.getItemModelById(id);
        ItemVo itemVo = convertVoFromModel(itemModel);
        return CommonReturnType.create(itemVo);
    }

    //商品详情页
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType list() {

        List<ItemModel> itemModelList = itemService.listItem();

        //使用stream apiJ将list内的itemModel转化为ITEMVO;
        List<ItemVo> itemVOList = new ArrayList<>();
        for (int i = 0; i < itemModelList.size(); i++) {
            ItemVo itemVo = this.convertVoFromModel(itemModelList.get(i));
            itemVOList.add(itemVo);
        }
        return CommonReturnType.create(itemVOList);
    }


    private ItemVo convertVoFromModel(ItemModel itemModel) {
        if(itemModel == null){
            return null;
        }
        ItemVo itemVO = new ItemVo();
        BeanUtils.copyProperties(itemModel,itemVO);

        if(itemModel.getPromoModel()!=null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(df.format(itemModel.getPromoModel().getStartDate()));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            itemVO.setPromoStatus(0);
        }

       return itemVO;
    }
}
