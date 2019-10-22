package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.WeightPackage;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.service.WeightPackageDetailService;
import com.resto.shop.web.service.WeightPackageService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by carl on 2018/03/19.
 */
@RequestMapping("weightPackage")
@Controller
public class WeightPackageController extends GenericController {

    @Autowired
    private WeightPackageService weightPackageService;

    @Autowired
    private WeightPackageDetailService weightPackageDetailService;

    @Autowired
    private PosService posService;

    @Autowired
    private BrandSettingService brandSettingService;

    @RequestMapping("/list")
    public void list(){
    }

    @RequestMapping("/brand_list")
    public ModelAndView brand_list(){
        BrandSetting setting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if(setting != null && setting.getOpenArticleLibrary().equals(new Integer(1))){
            return new ModelAndView("weightPackage/brand_list");
        }else{
            return new ModelAndView("weightPackage/none");
        }
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<WeightPackage> getList() {
        List<WeightPackage> result = weightPackageService.getAllWeightPackages(getCurrentShopId());
        return result;
    }

    @RequestMapping("/bradList_all")
    @ResponseBody
    public List<WeightPackage> getBrandList() {
        List<WeightPackage> result = weightPackageService.getAllBrandWeightPackages("-1");
        return result;
    }

    @RequestMapping("/create")
    @ResponseBody
    public Result create(@Valid @RequestBody WeightPackage weightPackage) {
        WeightPackage w = weightPackageService.selectByDateShopId(weightPackage.getName(), weightPackage.getShopId());
        if(w != null){
            return new Result("存在同名的重量包", false);
        }
        //创建主表
        Date time = new Date();
        weightPackage.setShopId(getCurrentShopId());
        weightPackage.setCreateTime(time);
        if(weightPackage.getOpenArticleLibrary()!=null&&weightPackage.getOpenArticleLibrary()==true){
            weightPackage.setShopId("-1");
        }else {
            weightPackage.setShopId(getCurrentShopId());
        }
        weightPackageService.insert(weightPackage);
        //消息通知newpos后台发生变化
//        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
//        shopMsgChangeDto.setBrandId(getCurrentBrandId());
//        shopMsgChangeDto.setShopId(getCurrentShopId());
//        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBWEIGHTPACKAGE);
//        shopMsgChangeDto.setType("add");
//        shopMsgChangeDto.setId(weightPackage.getId().toString());
//        try{
//            posService.shopMsgChange(shopMsgChangeDto);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        WeightPackage wp = weightPackageService.selectByDateShopId(weightPackage.getName(), weightPackage.getShopId());
        //创建属性
        weightPackage.setId(wp.getId());
        weightPackageService.insertDetail(weightPackage);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto ushopMsgChangeDto=new ShopMsgChangeDto();
        ushopMsgChangeDto.setBrandId(getCurrentBrandId());
        ushopMsgChangeDto.setShopId(getCurrentShopId());
        ushopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBWEIGHTPACKAGEDETAIL);
        ushopMsgChangeDto.setType("add");
        ushopMsgChangeDto.setId(wp.getId().toString());
        try{
            posService.shopMsgChange(ushopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new Result(true);
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Result modify(@Valid @RequestBody WeightPackage weightPackage) {
        weightPackageService.update(weightPackage);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBWEIGHTPACKAGE);
        shopMsgChangeDto.setType("modify");
        shopMsgChangeDto.setId(weightPackage.getId().toString());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        weightPackageService.initWeightPackageDetail(weightPackage);
        //创建属性
        weightPackageService.insertDetail(weightPackage);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto ushopMsgChangeDto=new ShopMsgChangeDto();
        ushopMsgChangeDto.setBrandId(getCurrentBrandId());
        ushopMsgChangeDto.setShopId(getCurrentShopId());
        ushopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBWEIGHTPACKAGEDETAIL);
        ushopMsgChangeDto.setType("modify");
        ushopMsgChangeDto.setId(weightPackage.getId().toString());
        try{
            posService.shopMsgChange(ushopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new Result(true);
    }

    @RequestMapping("/getWeightPackageById")
    @ResponseBody
    public WeightPackage getWeightPackageById(Long id) {
        return weightPackageService.getWeightPackageById(id);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(Long id) {
        weightPackageService.delete(id);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBWEIGHTPACKAGE);
        shopMsgChangeDto.setType("delete");
        shopMsgChangeDto.setId(id.toString());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        weightPackageDetailService.deleteDetails(id);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto ushopMsgChangeDto=new ShopMsgChangeDto();
        ushopMsgChangeDto.setBrandId(getCurrentBrandId());
        ushopMsgChangeDto.setShopId(getCurrentShopId());
        ushopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBWEIGHTPACKAGEDETAIL);
        ushopMsgChangeDto.setType("delete");
        ushopMsgChangeDto.setId(id.toString());
        try{
            posService.shopMsgChange(ushopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Result.getSuccess();
    }
}
