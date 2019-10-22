package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.ShopTvConfig;
import com.resto.brand.web.service.ShopTvConfigService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by carl on 2017/7/17.
 */
@Controller
@RequestMapping("shopTvConfig")
public class ShopTvConfigController extends GenericController {

    @Resource
    ShopTvConfigService shopTvConfigService;

    @Autowired
    private PosService posService;

    @RequestMapping("/list")
    public void list(){
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(){
        ShopTvConfig shopTvConfig = shopTvConfigService.selectByShopId(getCurrentShopId());
        return getSuccessResult(shopTvConfig);
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(ShopTvConfig shopTvConfig){
        if(shopTvConfig.getShopDetailId() == null || "".equals(shopTvConfig.getShopDetailId())){
            shopTvConfig.setShopDetailId(getCurrentShopId());
            shopTvConfig.setBrandId(getCurrentBrandId());
            shopTvConfigService.insert(shopTvConfig);
            //消息通知newpos后台发生变化
            ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
            shopMsgChangeDto.setBrandId(getCurrentBrandId());
            shopMsgChangeDto.setShopId(getCurrentShopId());
            shopMsgChangeDto.setTableName(NewPosTransmissionUtils.SHOPTVCONFIG);
            shopMsgChangeDto.setType("add");
            shopMsgChangeDto.setId(shopTvConfig.getId().toString());
            try{
                posService.shopMsgChange(shopMsgChangeDto);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            shopTvConfigService.update(shopTvConfig);
            //消息通知newpos后台发生变化
            ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
            shopMsgChangeDto.setBrandId(getCurrentBrandId());
            shopMsgChangeDto.setShopId(getCurrentShopId());
            shopMsgChangeDto.setTableName(NewPosTransmissionUtils.SHOPTVCONFIG);
            shopMsgChangeDto.setType("modify");
            shopMsgChangeDto.setId(shopTvConfig.getId().toString());
            try{
                posService.shopMsgChange(shopMsgChangeDto);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return Result.getSuccess();
    }
}
