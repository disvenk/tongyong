package com.resto.api.appraise.service;

import com.resto.api.appraise.entity.AppraiseComment;
import com.resto.api.appraise.util.ProjectConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Created by carl on 2016/11/20.
 */
@FeignClient(value = ProjectConstant.BASE_NAME)
@Api(description = "评论回复", position = 1)
@RequestMapping(value = ProjectConstant.COMMENT, produces = MediaType.APPLICATION_JSON_VALUE)
public interface NewAppraiseCommentService{

    @ApiOperation(value = "增加")
    @PostMapping("/dbSave")
    int dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseComment appraiseComment);

    @ApiOperation(value = "批量增加")
    @PostMapping("/dbInsertList")
    int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody List<AppraiseComment> list);

    @ApiOperation(value = "根据条件删除")
    @GetMapping("/dbDelete")
    int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseComment appraiseComment);

    @ApiOperation(value = "主键id删除")
    @GetMapping("/dbDeleteByPrimaryKey")
    int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object var);

    @ApiOperation(value = "主键id进行查询")
    @GetMapping("/dbDeleteByIds")
    int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String var);

    @ApiOperation(value = "更新")
    @PostMapping("/dbUpdate")
    int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseComment appraiseComment);

    @ApiOperation(value = "根据条件list查询")
    @GetMapping("/dbSelect")
    List<AppraiseComment> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseComment t);

    @ApiOperation(value = "分页查询")
    @GetMapping("/dbSelectPage")
    List<AppraiseComment> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseComment t, Integer pageNum, Integer pageSize);

    @ApiOperation(value = "主键id查询")
    @GetMapping("/dbSelectByPrimaryKey")
    AppraiseComment dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object key);

    @ApiOperation(value = "根据条件单条查询")
    @GetMapping("/dbSelectOne")
    AppraiseComment dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseComment record);

    @ApiOperation(value = "根据条件查询条数")
    @GetMapping("/dbSelectCount")
    int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseComment record);

    @ApiOperation(value = "根据主键id查询list")
    @GetMapping("/dbSelectByIds")
    List<AppraiseComment> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String ids);

    @ApiOperation(value = "根据评论id查询评论回复")
    @GetMapping("/appraiseCommentList")
    List<AppraiseComment> appraiseCommentList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@ApiParam(value = "评论id", required = true) @RequestParam(value ="appraiseId") String appraiseId);

    @ApiOperation(value = "插入评论回复")
    @PostMapping("/insertComment")
    AppraiseComment insertComment(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseComment appraiseComment);

    @ApiOperation(value = "会员id查询回复条数")
    @GetMapping("/selectByCustomerCount")
    int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId);

}
