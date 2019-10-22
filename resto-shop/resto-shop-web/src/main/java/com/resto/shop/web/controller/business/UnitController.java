package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.RedisService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Unit;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.service.UnitService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by KONATA on 2016/9/11.
 */
@RequestMapping("unit")
@Controller
public class UnitController extends GenericController {

    @Autowired
    private UnitService unitService;

    @Autowired
    PosService posService;

    @Autowired
    RedisService redisService;

    @Autowired
    private BrandSettingService brandSettingService;
    
    @RequestMapping("/unitlist")
    public ModelAndView index() {
        return new ModelAndView("unit/list");
    }

    @RequestMapping("/brand_unitlist")
    public ModelAndView brand_list(){
        BrandSetting setting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if(setting != null && setting.getOpenArticleLibrary().equals(new Integer(1))){
            return new ModelAndView("unit/brand_unitlist");
        }else{
            return new ModelAndView("unit/none");
        }
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<Unit> getList() {
        List<Unit> result = unitService.getUnits(getCurrentShopId());
        return result;
    }

    @RequestMapping("/brandList_all")
    @ResponseBody
    public List<Unit> getBrandList() {
        List<Unit> result = unitService.getBrandUnits("-1");
        return result;
    }


    @RequestMapping("/list_all_id")
    @ResponseBody
    public List<Unit> getListById(String articleId) {
        List<Unit> result = unitService.getUnitsByArticleId(getCurrentShopId(), articleId);
        return result;
    }


    @RequestMapping("/create")
    @ResponseBody
    public Result create(@Valid @RequestBody Unit unit) {
        //创建主表
        String id = ApplicationUtils.randomUUID();
        unit.setId(id);
        if(unit.getOpenArticleLibrary()!=null&&unit.getOpenArticleLibrary()==true){
            unit.setShopId("-1");
        }else {
            unit.setShopId(getCurrentShopId());
        }
        unitService.insert(unit);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBUNIT);
        shopMsgChangeDto.setType("add");
        shopMsgChangeDto.setId(unit.getId());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        //创建属性
        unitService.insertDetail(unit);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto gshopMsgChangeDto=new ShopMsgChangeDto();
        gshopMsgChangeDto.setBrandId(getCurrentBrandId());
        gshopMsgChangeDto.setShopId(getCurrentShopId());
        gshopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBUNITDETAIL);
        gshopMsgChangeDto.setType("add");
        gshopMsgChangeDto.setId(unit.getId());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(redisService.get(getCurrentShopId()+"unit") != null){
            redisService.remove(getCurrentShopId()+"unit");
        }
        return new Result(true);
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Result modify(@Valid @RequestBody Unit unit) {
        unitService.update(unit);
        unitService.initUnit(unit);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBUNIT);
        shopMsgChangeDto.setType("modify");
        shopMsgChangeDto.setId(unit.getId());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        //创建属性
        Unit u = unitService.insertDetail(unit);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto gshopMsgChangeDto=new ShopMsgChangeDto();
        gshopMsgChangeDto.setBrandId(getCurrentBrandId());
        gshopMsgChangeDto.setShopId(getCurrentShopId());
        gshopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBUNITDETAIL);
        gshopMsgChangeDto.setType("add");
        gshopMsgChangeDto.setId(unit.getId());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }

        //同步更新 使用该规格包的菜品信息
        unitService.modifyUnit(u,getCurrentBrandId(),getCurrentShopId());
        if(redisService.get(getCurrentShopId()+"unit") != null){
            redisService.remove(getCurrentShopId()+"unit");
        }
        return new Result(true);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(String id) {
        unitService.delete(id);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBUNIT);
        shopMsgChangeDto.setType("delete");
        shopMsgChangeDto.setId(id);
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        unitService.deleteUnit(id);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto gshopMsgChangeDto=new ShopMsgChangeDto();
        gshopMsgChangeDto.setBrandId(getCurrentBrandId());
        gshopMsgChangeDto.setShopId(getCurrentShopId());
        gshopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBUNITDETAIL);
        gshopMsgChangeDto.setType("delete");
        gshopMsgChangeDto.setId(id);
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(redisService.get(getCurrentShopId()+"unit") != null){
            redisService.remove(getCurrentShopId()+"unit");
        }
        return Result.getSuccess();
    }

    @RequestMapping("/getUnitById")
    @ResponseBody
    public Unit getUnitById(String id) {
        return unitService.getUnitById(id);
    }


}
