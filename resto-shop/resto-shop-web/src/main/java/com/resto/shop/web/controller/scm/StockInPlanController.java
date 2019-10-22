package com.resto.shop.web.controller.scm;

import com.alibaba.dubbo.config.annotation.Reference;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.dto.DocStkInPlanHeaderDo;
import com.resto.shop.web.service.StockInPlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;

/***
 *
 * 库存入库
 *
 */
@Controller
@RequestMapping("scmStockPlan")
public class StockInPlanController extends GenericController {

    @Resource
    StockInPlanService stockinplanService;
    @Resource
    BrandSettingService brandSettingService;

    @RequestMapping("/list")
    public String list() {

        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if (brandSetting.getIsOpenScm().equals(Common.YES)) {
            return "scmStockPlan/list";
        } else {
            return "notopen";
        }

    }

    @RequestMapping("/list_all")
    @ResponseBody
    public Result listData(String shopId) {
        String shopDetailId = StringUtils.isEmpty(shopId) ? getCurrentShopId() : shopId;
        return getSuccessResult(stockinplanService.queryJoin4Page(shopDetailId));
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(Long id) {
        //DocStkInPlanHeaderDo stockinplan = stockinplanService.queryByStkInPlanHeaderId();
        return getSuccessResult(null);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid @RequestBody DocStkInPlanHeaderDo stockinplan) {
        String shopDetailId = stockinplan.getShopDetailId();
        if (StringUtils.isEmpty(shopDetailId)) {
            stockinplan.setShopDetailId(getCurrentShopId());
        }

        stockinplanService.addDocStkInPlanHeaderDo(stockinplan);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid @RequestBody DocStkInPlanHeaderDo stockinplan) {
        stockinplanService.updateDocStkInPlanHeaderDo(stockinplan);
        return Result.getSuccess();
    }

    @RequestMapping("approve")
    @ResponseBody
    public Result approve(Long id, String orderStatus) {
        stockinplanService.updateDocStkStatusById(id, orderStatus,getUserName(),getCurrentShopName());
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Long id) {
        stockinplanService.deleteById(id);
        return Result.getSuccess();
    }
}
