package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.ShopType;
import com.resto.brand.web.service.ShopTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by carl on 2018/04/2018/4/4
 */
@Controller
@RequestMapping("shoptype")
public class ShopTypeController extends GenericController {

    @Resource
    ShopTypeService shopTypeService;

    @RequestMapping("/list")
    public void list(){
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<ShopType> listData(){
        return shopTypeService.selectList();
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(Integer id){
        ShopType shopType = shopTypeService.selectById(id);
        return getSuccessResult(shopType);
    }
    @RequestMapping("one")
    @ResponseBody
    public ShopType listOne(Integer id){
        ShopType shopType = shopTypeService.selectById(id);
        return shopType;
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid ShopType brand){
        shopTypeService.insert(brand);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid ShopType brand){
        shopTypeService.update(brand);
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Integer id){
        shopTypeService.delete(id);
        return Result.getSuccess();
    }
}
