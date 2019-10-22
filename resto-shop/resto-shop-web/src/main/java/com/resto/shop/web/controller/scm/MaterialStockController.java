package com.resto.shop.web.controller.scm;

import com.alibaba.dubbo.config.annotation.Reference;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.PinyinUtil;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.dto.MaterialStockDo;
import com.resto.shop.web.model.MdMaterialConsumeDetail;
import com.resto.shop.web.service.MaterialStockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

/**
 * 原料库存
 */
@Controller
@RequestMapping("scmMaterialStock")
public class MaterialStockController extends GenericController {

    @Resource
    MaterialStockService materialstockService;

    @Resource
    BrandSettingService brandSettingService;


    @RequestMapping("/list")
    public String list(){
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if (brandSetting.getIsOpenScm().equals(Common.YES)){
            return "scmMaterialStock/list";
        }else {
            return "notopen";
        }
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public Result listData(String shopId,String startTime,String endTime) {
        String shopDetailId =StringUtils.isEmpty(shopId)?this.getCurrentShopId():shopId;
        List<MaterialStockDo> list = materialstockService.queryJoin4Page(shopDetailId,startTime,endTime);
        for(MaterialStockDo materialStockDo:list){
            materialStockDo.setInitials(PinyinUtil.getPinYinHeadChar(materialStockDo.getMaterialName()));
            String s=materialStockDo.getMeasureUnit();
            if(s.indexOf(".") > 0){
                //正则表达
                s = s.replaceAll("0+?$", "");//去掉后面无用的零
                s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
            }
            materialStockDo.setMeasureUnit(s);
        }
        return getSuccessResult(list);

    }

    @RequestMapping("/findMaterialStockConsumeDetail")
    @ResponseBody
    public Result findMaterialStockConsumeDetail(String shopId,Long materialId) {
        String shopDetailId =StringUtils.isEmpty(shopId)?this.getCurrentShopId():shopId;
        List<MdMaterialConsumeDetail> list = materialstockService.findMaterialStockConsumeDetailByMaterialId(shopDetailId,materialId);
        return getSuccessResult(list);

    }


}
