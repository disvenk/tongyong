package com.resto.service.appraise.service.impl;

import com.resto.api.appraise.service.NewAppraiseGradeService;
import com.resto.api.appraise.entity.AppraiseGrade;
import com.resto.service.appraise.service.impl.service.AppraiseGradeService;
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
public class NewAppraiseGradeServiceImpl implements NewAppraiseGradeService {

    @Resource
    private AppraiseGradeService appraiseGradeService;

    @Override
    public int dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseGrade appraiseGrade) {
        return appraiseGradeService.dbSave(appraiseGrade);
    }

    @Override
    public int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody List<AppraiseGrade> list) {
        return appraiseGradeService.dbInsertList(list);
    }

    @Override
    public int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseGrade appraiseGrade) {
        return appraiseGradeService.dbDelete(appraiseGrade);
    }

    @Override
    public int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object var) {
        return appraiseGradeService.dbDeleteByPrimaryKey(new Integer(var.toString()));
    }

    @Override
    public int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String var) {
        return appraiseGradeService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseGrade appraiseGrade) {
        return appraiseGradeService.dbUpdate(appraiseGrade);
    }

    @Override
    public List<AppraiseGrade> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseGrade t) {
        return appraiseGradeService.dbSelect(t);
    }

    @Override
    public List<AppraiseGrade> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseGrade t, Integer pageNum, Integer pageSize) {
        return appraiseGradeService.dbSelectPage(t,pageNum,pageSize);
    }

    @Override
    public AppraiseGrade dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object key) {
        return appraiseGradeService.dbSelectByPrimaryKey(key);
    }

    @Override
    public AppraiseGrade dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseGrade record) {
        return appraiseGradeService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseGrade record) {
        return appraiseGradeService.dbSelectCount(record);
    }

    @Override
    public List<AppraiseGrade> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String ids) {
        return appraiseGradeService.dbSelectByIds(ids);
    }

    @Override
    public List<AppraiseGrade> selectByAppraiseId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Long appraiseId) {
        return appraiseGradeService.selectByAppraiseId(appraiseId);
    }
}
