package com.resto.service.appraise.service.impl;

import com.resto.api.appraise.service.NewAppraiseStepService;
import com.resto.api.appraise.entity.AppraiseStep;
import com.resto.service.appraise.service.impl.service.AppraiseStepService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@RestController
public class NewAppraiseStepServiceImpl implements NewAppraiseStepService {

    @Resource
    private AppraiseStepService appraiseStepService;

    @Override
    public int dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseStep appraiseStep) {
        return appraiseStepService.dbSave(appraiseStep);
    }

    @Override
    public int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody List<AppraiseStep> list) {
        return appraiseStepService.dbInsertList(list);
    }

    @Override
    public int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseStep appraiseStep) {
        return appraiseStepService.dbDelete(appraiseStep);
    }

    @Override
    public int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object var) {
        return appraiseStepService.dbDeleteByPrimaryKey(new Integer(var.toString()));
    }

    @Override
    public int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String var) {
        return appraiseStepService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseStep appraiseStep) {
        return appraiseStepService.dbUpdate(appraiseStep);
    }

    @Override
    public List<AppraiseStep> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseStep t) {
        return appraiseStepService.dbSelect(t);
    }

    @Override
    public List<AppraiseStep> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseStep t, Integer pageNum, Integer pageSize) {
        return appraiseStepService.dbSelectPage(t,pageNum,pageSize);
    }

    @Override
    public AppraiseStep dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object key) {
        return appraiseStepService.dbSelectByPrimaryKey(key);
    }

    @Override
    public AppraiseStep dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseStep record) {
        return appraiseStepService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseStep record) {
        return appraiseStepService.dbSelectCount(record);
    }

    @Override
    public List<AppraiseStep> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String ids) {
        return appraiseStepService.dbSelectByIds(ids);
    }

    @Override
    public List<AppraiseStep> selectByAppraiseId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Long appraiseId) {
        return appraiseStepService.selectByAppraiseId(appraiseId);
    }
}
