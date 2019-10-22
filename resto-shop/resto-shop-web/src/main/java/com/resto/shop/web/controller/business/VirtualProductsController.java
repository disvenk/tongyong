package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Article;
import com.resto.shop.web.model.Kitchen;
import com.resto.shop.web.model.VirtualProducts;
import com.resto.shop.web.model.VirtualProductsAndKitchen;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.*;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangwei on 2017/2/22.
 * 虚拟餐品Controller
 */
@RequestMapping("virtual")
@Controller
public class VirtualProductsController extends GenericController {

    @Autowired
    private VirtualProductsService virtualProductsService;

    @Autowired
    private KitchenService kitchenService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private PosService posService;

    @RequestMapping("/list")
    public ModelAndView index() {
        return new ModelAndView("virtual/list");
    }

    @RequestMapping("/listAll")
    @ResponseBody
    public List<VirtualProducts> getVirtuals(){
        List<VirtualProducts> result=virtualProductsService.getAllProducuts(getCurrentShopId());
        for (VirtualProducts virtualProducts:result) {
            List<Kitchen> kitchens = new ArrayList<>();
            List<VirtualProductsAndKitchen> VirtualProductsAndKitchens=virtualProductsService.getVirtualProductsAndKitchenById(virtualProducts.getId());
            if(VirtualProductsAndKitchens.size()>0){
                for (VirtualProductsAndKitchen virtualProductsAndKitchen:VirtualProductsAndKitchens) {
                    Kitchen kitchen=kitchenService.selectById(virtualProductsAndKitchen.getKitchenId());
                    if(kitchen != null){
                        for (int i = 0; i < 1; i++) {
                            kitchens.add(kitchen);
                        }
                    }
                }
            }
            virtualProducts.setKitchens(kitchens);
        }
        return result;
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid VirtualProducts virtualProducts){
        virtualProducts.setShopDetailId(getCurrentShopId());
        virtualProducts.setCreateTime(new Date());
            virtualProductsService.insert(virtualProducts);
            //消息通知newpos后台发生变化
            ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
            shopMsgChangeDto.setBrandId(getCurrentBrandId());
            shopMsgChangeDto.setShopId(getCurrentShopId());
            shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBVIRTUALPRODUCTS);
            shopMsgChangeDto.setType("add");
//            shopMsgChangeDto.setId(virtualProducts.getId().toString());
            try{
                posService.shopMsgChange(shopMsgChangeDto);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(virtualProducts.getKitchenList()!=null){
                VirtualProductsAndKitchen virtualProductsAndKitchen=new VirtualProductsAndKitchen();
                Integer i=virtualProductsService.selectMaxId();
                virtualProductsAndKitchen.setVirtualId(virtualProductsService.selectMaxId());
                for (Integer kitchenId:virtualProducts.getKitchenList()) {
                    virtualProductsAndKitchen.setKitchenId(kitchenId);
                    virtualProductsService.insertVirtualProductsKitchen(virtualProductsAndKitchen);
                    //消息通知newpos后台发生变化
                    ShopMsgChangeDto vshopMsgChangeDto=new ShopMsgChangeDto();
                    vshopMsgChangeDto.setBrandId(getCurrentBrandId());
                    vshopMsgChangeDto.setShopId(getCurrentShopId());
                    vshopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBVIRTUALPRODUCTSKITCHEN);
                    vshopMsgChangeDto.setType("add");
                    vshopMsgChangeDto.setId(String.valueOf(virtualProductsAndKitchen.getVirtualId()));
                    try{
                        posService.shopMsgChange(vshopMsgChangeDto);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }else{
                return Result.getSuccess();
            }
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid VirtualProducts virtualProducts){
        virtualProducts.setShopDetailId(getCurrentShopId());
        virtualProducts.setCreateTime(new Date());
        virtualProductsService.updateVirtual(virtualProducts);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBVIRTUALPRODUCTS);
        shopMsgChangeDto.setType("modify");
        shopMsgChangeDto.setId(virtualProducts.getId().toString());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        virtualProductsService.deleteVirtualById(virtualProducts.getId());
        //消息通知newpos后台发生变化
        ShopMsgChangeDto vshopMsgChangeDto=new ShopMsgChangeDto();
        vshopMsgChangeDto.setBrandId(getCurrentBrandId());
        vshopMsgChangeDto.setShopId(getCurrentShopId());
        vshopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBVIRTUALPRODUCTSKITCHEN);
        vshopMsgChangeDto.setType("delete");
        vshopMsgChangeDto.setId(virtualProducts.getId().toString());
        try{
            posService.shopMsgChange(vshopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        VirtualProductsAndKitchen virtualProductsAndKitchen=new VirtualProductsAndKitchen();
        if(virtualProducts.getKitchenList()!=null){
            for (Integer kitchenId:virtualProducts.getKitchenList()) {
                //修改关系表时，先删除当前条，然后在添加
                virtualProductsAndKitchen.setKitchenId(kitchenId);
                virtualProductsAndKitchen.setVirtualId(virtualProducts.getId());
                virtualProductsService.insertVirtualProductsAndKitchen(virtualProductsAndKitchen);
                //消息通知newpos后台发生变化
                ShopMsgChangeDto oshopMsgChangeDto=new ShopMsgChangeDto();
                oshopMsgChangeDto.setBrandId(getCurrentBrandId());
                oshopMsgChangeDto.setShopId(getCurrentShopId());
                oshopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBVIRTUALPRODUCTSKITCHEN);
                oshopMsgChangeDto.setType("add");
                oshopMsgChangeDto.setId(String.valueOf(virtualProductsAndKitchen.getVirtualId()));
                try{
                    posService.shopMsgChange(oshopMsgChangeDto);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            virtualProductsService.deleteVirtualById(virtualProducts.getId());
            //消息通知newpos后台发生变化
            ShopMsgChangeDto eshopMsgChangeDto=new ShopMsgChangeDto();
            eshopMsgChangeDto.setBrandId(getCurrentBrandId());
            eshopMsgChangeDto.setShopId(getCurrentShopId());
            eshopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBVIRTUALPRODUCTSKITCHEN);
            eshopMsgChangeDto.setType("delete");
            eshopMsgChangeDto.setId(virtualProducts.getId().toString());
            try{
                posService.shopMsgChange(eshopMsgChangeDto);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Integer id){
        virtualProductsService.deleteById(id);


        String currentShopId = getCurrentShopId();

        //菜品virtual_id置为null
        List<Article> articleList = articleService.getArticleList(id,currentShopId);
        for (Article article : articleList) {

            articleService.updateArticle(article.getId(),currentShopId);
        }

        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBVIRTUALPRODUCTS);
        shopMsgChangeDto.setType("delete");
        shopMsgChangeDto.setId(id.toString());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        virtualProductsService.deleteVirtualById(id);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto eshopMsgChangeDto=new ShopMsgChangeDto();
        eshopMsgChangeDto.setBrandId(getCurrentBrandId());
        eshopMsgChangeDto.setShopId(getCurrentShopId());
        eshopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBVIRTUALPRODUCTSKITCHEN);
        eshopMsgChangeDto.setType("delete");
        eshopMsgChangeDto.setId(id.toString());
        try{
            posService.shopMsgChange(eshopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Result.getSuccess();
    }

    @RequestMapping("/getVirtualById")
    @ResponseBody
    public Result getVirtualById(Integer id) {
        VirtualProducts virtualProducts=virtualProductsService.getVirtualProductsById(id);
        virtualProducts.setKitchenList(virtualProductsService.getAllKitchenIdById(id));
        return getSuccessResult(virtualProducts);
    }

}
