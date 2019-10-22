package com.resto.service.appraise.service.impl;

import com.resto.api.appraise.service.NewAppraisePraiseService;
import com.resto.api.appraise.entity.AppraisePraise;
import com.resto.service.appraise.service.impl.service.AppraisePraiseService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;
/**
 * Created by carl on 2016/11/20.
 */
@RestController
public class NewAppraisePraiseServiceImpl implements NewAppraisePraiseService {

    @Resource
    private AppraisePraiseService appraisePraiseService;

    @Override
    public int dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraisePraise appraisePraise) {
        return appraisePraiseService.dbSave(appraisePraise);
    }

    @Override
    public int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody List<AppraisePraise> list) {
        return appraisePraiseService.dbInsertList(list);
    }

    @Override
    public int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraisePraise appraisePraise) {
        return appraisePraiseService.dbDelete(appraisePraise);
    }

    @Override
    public int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object var) {
        return appraisePraiseService.dbDeleteByPrimaryKey(var.toString());
    }

    @Override
    public int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String var) {
        return appraisePraiseService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraisePraise appraisePraise) {
        return appraisePraiseService.dbUpdate(appraisePraise);
    }

    @Override
    public List<AppraisePraise> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraisePraise t) {
        return appraisePraiseService.dbSelect(t);
    }

    @Override
    public List<AppraisePraise> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraisePraise t, Integer pageNum, Integer pageSize) {
        return appraisePraiseService.dbSelectPage(t,pageNum,pageSize);
    }

    @Override
    public AppraisePraise dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object key) {
        return appraisePraiseService.dbSelectByPrimaryKey(key);
    }

    @Override
    public AppraisePraise dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraisePraise record) {
        return appraisePraiseService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraisePraise record) {
        return appraisePraiseService.dbSelectCount(record);
    }

    @Override
    public List<AppraisePraise> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String ids) {
        return appraisePraiseService.dbSelectByIds(ids);
    }

    @Override
    public AppraisePraise updateCancelPraise(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String appraiseId, String customerId, Integer isDel) {
        appraisePraiseService.updateCancelPraise(appraiseId, customerId, isDel);
        AppraisePraise appraisePraise = appraisePraiseService.selectByAppraiseIdCustomerId(appraiseId, customerId);
        return appraisePraise;
    }

    @Override
    public AppraisePraise updateCancelPraise(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraisePraise appraisePraise) {
        appraisePraiseService.updateCancelPraise(appraisePraise);
        return appraisePraise;
    }

    @Override
    public List<AppraisePraise> appraisePraiseList(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String appraiseId) {
        return appraisePraiseService.appraisePraiseList(appraiseId);
    }

    @Override
    public AppraisePraise selectByAppraiseIdCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String appraiseId, String customerId) {
        return appraisePraiseService.selectByAppraiseIdCustomerId(appraiseId, customerId);
    }

    @Override
    public int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String customerId) {
        return appraisePraiseService.selectByCustomerCount(customerId);
    }

}
