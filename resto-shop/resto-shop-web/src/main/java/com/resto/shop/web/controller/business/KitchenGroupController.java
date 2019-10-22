package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.KitchenGroup;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.KitchenGroupService;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("kitchenGroup")
public class KitchenGroupController extends GenericController {

    @Autowired
    private KitchenGroupService kitchenGroupService;

    @Autowired
    private PosService posService;

    @RequestMapping("/list_all")
    @ResponseBody
    public List<KitchenGroup> listData(){
        List<KitchenGroup> kitchenGroups = kitchenGroupService.selectKitchenGroupByShopDetailId(getCurrentBrandId(),getCurrentShopId());
        return kitchenGroups;
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(Integer id){
        KitchenGroup kitchenGroup = kitchenGroupService.selectById(id);
        return getSuccessResult(kitchenGroup);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@RequestBody KitchenGroup kitchenGroup){
        kitchenGroup.setShopDetailId(getCurrentShopId());
        kitchenGroup.setBrandId(getCurrentBrandId());
        kitchenGroupService.insertSelective(kitchenGroup);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@RequestBody KitchenGroup kitchenGroup){
        kitchenGroup.setShopDetailId(getCurrentShopId());
        kitchenGroup.setBrandId(getCurrentBrandId());
        kitchenGroupService.update(kitchenGroup);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBKITCHENGROUP);
        shopMsgChangeDto.setType("modify");
        shopMsgChangeDto.setId(kitchenGroup.getId().toString());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Integer id){
        kitchenGroupService.delete(getCurrentShopId(),id);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBKITCHENGROUP);
        shopMsgChangeDto.setType("delete");
        shopMsgChangeDto.setId(id.toString());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Result.getSuccess();
    }
}
