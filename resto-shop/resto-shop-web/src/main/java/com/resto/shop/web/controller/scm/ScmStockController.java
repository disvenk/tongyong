package com.resto.shop.web.controller.scm;

import com.alibaba.dubbo.config.annotation.Reference;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.dto.DocStockCountDetailDo;
import com.resto.shop.web.dto.DocStockCountHeaderDo;
import com.resto.shop.web.dto.DocStockInput;
import com.resto.shop.web.dto.MaterialStockDo;
import com.resto.shop.web.service.StockCountCheckService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by bruce on 2017-09-18 13:41
 * 库存盘点
 */
@Controller
@RequestMapping("scmStockCount")
public class ScmStockController extends GenericController{

    @Resource
    StockCountCheckService stockCountCheckService;
    @Resource
    BrandSettingService brandSettingService;

    @RequestMapping("/list")
    public String list(){
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if (brandSetting.getIsOpenScm().equals(Common.YES)){
            return "scmStockCount/list";
        }else {
            return "notopen";
        }
    }

    @RequestMapping("/list_default")
    @ResponseBody
    public Result list_(){
        List<MaterialStockDo> list = stockCountCheckService.findDefaultStock(getCurrentShopId());
        return getSuccessResult(list);
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public Result listData(String shopId){
        String shopDetailId = StringUtils.isEmpty(shopId)?getCurrentShopId():shopId;
        List<DocStockCountHeaderDo> list = stockCountCheckService.findStockList(shopDetailId);
        for(DocStockCountHeaderDo docStockCountHeaderDo:list){
            for(DocStockCountDetailDo dcStockCountDetailDo:docStockCountHeaderDo.getStockCountDetailList()){
                String s=dcStockCountDetailDo.getMeasureUnit();
                if(s.indexOf(".") > 0){
                    //正则表达
                    s = s.replaceAll("0+?$", "");//去掉后面无用的零
                    s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
                }
                dcStockCountDetailDo.setMeasureUnit(s);
            }
        }
        return getSuccessResult(list);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid @RequestBody DocStockInput docStockInput){
        //docStockInput.setCreateId(getCurrentUserId());
        try {
            stockCountCheckService.saveStock(docStockInput);
            return Result.getSuccess();
        }catch (Exception e){
            return new Result("保存失败", 5000,false);
        }
    }

    /***
     *审核
     * @return
     */
    @RequestMapping("approve")
    @ResponseBody
    public Result approveStockStatusById(Long id,String orderStatus,String shopId){
        try {
            String shopDetailId = StringUtils.isEmpty(shopId)?getCurrentShopId():shopId;
            stockCountCheckService.approveStockStatusById(id,orderStatus,shopDetailId,getUserName());
            return Result.getSuccess();
        }catch (Exception e){
            return new Result("保存失败", 5000,false);
        }
    }

}
