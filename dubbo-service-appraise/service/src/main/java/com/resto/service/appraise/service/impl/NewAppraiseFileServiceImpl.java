package com.resto.service.appraise.service.impl;

import com.resto.api.appraise.service.NewAppraiseFileService;
import com.resto.api.appraise.entity.AppraiseFile;
import javax.annotation.Resource;
import java.util.List;
import com.resto.service.appraise.service.impl.service.AppraiseFileService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by carl on 2016/11/20.
 */
@RestController
public class NewAppraiseFileServiceImpl implements NewAppraiseFileService {

    @Resource
    private AppraiseFileService appraiseFileService;

    @Override
    public int dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseFile appraiseFile) {
        return appraiseFileService.dbSave(appraiseFile);
    }

    @Override
    public int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody List<AppraiseFile> list) {
        return appraiseFileService.dbInsertList(list);
    }

    @Override
    public int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseFile appraiseFile) {
        return appraiseFileService.dbDelete(appraiseFile);
    }

    @Override
    public int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object var) {
        return appraiseFileService.dbDeleteByPrimaryKey(String.valueOf(var));
    }

    @Override
    public int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String var) {
        return appraiseFileService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseFile appraiseFile) {
        return appraiseFileService.dbUpdate(appraiseFile);
    }

    @Override
    public List<AppraiseFile> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseFile t) {
        return appraiseFileService.dbSelect(t);
    }

    @Override
    public List<AppraiseFile> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseFile t, Integer pageNum, Integer pageSize) {
        return appraiseFileService.dbSelectPage(t,pageNum,pageSize);
    }

    @Override
    public AppraiseFile dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object key) {
        return appraiseFileService.dbSelectByPrimaryKey(key);
    }

    @Override
    public AppraiseFile dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseFile record) {
        return appraiseFileService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseFile record) {
        return appraiseFileService.dbSelectCount(record);
    }

    @Override
    public List<AppraiseFile> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String ids) {
        return appraiseFileService.dbSelectByIds(ids);
    }

    @Override
    public List<AppraiseFile> appraiseFileList(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String appraiseId) {
        List<AppraiseFile> list=appraiseFileService.appraiseFileList(appraiseId);
        return list;
    }
}
