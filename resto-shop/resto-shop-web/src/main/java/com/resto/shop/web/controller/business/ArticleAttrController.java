package com.resto.shop.web.controller.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.ArticleAttr;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.ArticleAttrService;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("articleattr")
public class ArticleAttrController extends GenericController {

    @Resource
    ArticleAttrService articleattrService;

    @Autowired
    private PosService posService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @RequestMapping("/list")
    public void list() {
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<ArticleAttr> listData() {
        return articleattrService.selectListByShopId(getCurrentShopId());
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(Integer id) {
        ArticleAttr articleattr = articleattrService.selectById(id);
        return getSuccessResult(articleattr);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid ArticleAttr articleAttr) {
        articleAttr.setShopDetailId(getCurrentShopId());
        articleattrService.create(articleAttr,getCurrentBrandId(),getCurrentShopId());
        //消息通知newpos后台发生变化
//        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
//        shopMsgChangeDto.setBrandId(getCurrentBrandId());
//        shopMsgChangeDto.setShopId(getCurrentShopId());
//        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEATTR);
//        shopMsgChangeDto.setType("add");
//        shopMsgChangeDto.setId(articleAttr.getId().toString());
//        try{
//            posService.shopMsgChange(shopMsgChangeDto);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid ArticleAttr brand) {
        articleattrService.updateInfo(brand,getCurrentBrandId(),getCurrentShopId());
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEATTR);
        shopMsgChangeDto.setType("modify");
        shopMsgChangeDto.setId(brand.getId().toString());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Integer id) {
        articleattrService.deleteInfo(id,getCurrentBrandId(),getCurrentShopId());
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEATTR);
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
