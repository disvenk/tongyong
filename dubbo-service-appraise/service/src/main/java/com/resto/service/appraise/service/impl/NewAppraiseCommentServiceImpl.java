package com.resto.service.appraise.service.impl;

import com.github.pagehelper.PageHelper;
import com.resto.api.appraise.service.NewAppraiseCommentService;
import com.resto.api.appraise.entity.AppraiseComment;
import javax.annotation.Resource;
import java.util.List;
import com.resto.service.appraise.service.impl.service.AppraiseCommentService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by carl on 2016/11/20.
 */
@RestController
public class NewAppraiseCommentServiceImpl implements NewAppraiseCommentService {

    @Resource
    private AppraiseCommentService appraiseCommentService;

    @Override
    public int dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseComment appraiseComment) {
      return appraiseCommentService.dbSave(appraiseComment);
    }

    @Override
    public int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody List<AppraiseComment> list) {
        return appraiseCommentService.dbInsertList(list);
    }

    @Override
    public int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseComment appraiseComment) {
        return appraiseCommentService.dbDelete(appraiseComment);
    }

    @Override
    public int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object var) {
        return appraiseCommentService.dbDeleteByPrimaryKey(String.valueOf(var));
    }

    @Override
    public int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String var) {
        return appraiseCommentService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseComment appraiseComment) {
        return appraiseCommentService.dbUpdate(appraiseComment);
    }

    @Override
    public List<AppraiseComment> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseComment t) {
        return appraiseCommentService.dbSelect(t);
    }

    @Override
    public List<AppraiseComment> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseComment t, Integer pageNum, Integer pageSize) {
        return appraiseCommentService.dbSelectPage(t,pageNum,pageSize);
    }

    @Override
    public AppraiseComment dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object key) {
        return appraiseCommentService.dbSelectByPrimaryKey(key);
    }

    @Override
    public AppraiseComment dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseComment record) {
        return appraiseCommentService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseComment record) {
        return appraiseCommentService.dbSelectCount(record);
    }

    @Override
    public List<AppraiseComment> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String ids) {
        return appraiseCommentService.dbSelectByIds(ids);
    }

    @Override
    public List<AppraiseComment> appraiseCommentList(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @ApiParam(value = "评论id", required = true) @RequestParam(value = "appraiseId") String appraiseId) {
        return appraiseCommentService.appraiseCommentList(appraiseId);
    }

    @Override
    public AppraiseComment insertComment(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseComment appraiseComment) {
        appraiseCommentService.insertComment(appraiseComment);
        return appraiseComment;
    }

    @Override
    public int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String customerId) {
        return appraiseCommentService.selectByCustomerCount(customerId);
    }

}
