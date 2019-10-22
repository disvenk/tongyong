package com.resto.api.appraise.service;

import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.entity.AppraiseStep;
import com.resto.api.appraise.util.ProjectConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(value = ProjectConstant.BASE_NAME)
@Api(description = "菜品赞踩", position = 1)
@RequestMapping(value = ProjectConstant.STEP, produces = MediaType.APPLICATION_JSON_VALUE)
public interface NewAppraiseStepService{

    @ApiOperation(value = "增加")
    @PostMapping("/dbSave")
    int dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody AppraiseStep appraiseStep);

    @ApiOperation(value = "批量增加")
    @PostMapping("/dbInsertList")
    int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody List<AppraiseStep> list);

    @ApiOperation(value = "根据条件删除")
    @GetMapping("/dbDelete")
    int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseStep appraiseStep);

    @ApiOperation(value = "主键id删除")
    @GetMapping("/dbDeleteByPrimaryKey")
    int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object var);

    @ApiOperation(value = "主键id进行查询")
    @GetMapping("/dbDeleteByIds")
    int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String var);

    @ApiOperation(value = "更新")
    @PostMapping("/dbUpdate")
    int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseStep appraiseStep);

    @ApiOperation(value = "根据条件list查询")
    @GetMapping("/dbSelect")
    List<AppraiseStep> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody AppraiseStep t);

    @ApiOperation(value = "分页查询")
    @GetMapping("/dbSelectPage")
    List<AppraiseStep> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseStep t, Integer pageNum, Integer pageSize);

    @ApiOperation(value = "主键id查询")
    @GetMapping("/dbSelectByPrimaryKey")
    AppraiseStep dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object key);

    @ApiOperation(value = "根据条件单条查询")
    @GetMapping("/dbSelectOne")
    AppraiseStep dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseStep record);

    @ApiOperation(value = "根据条件查询条数")
    @GetMapping("/dbSelectCount")
    int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseStep record);

    @ApiOperation(value = "根据主键id查询list")
    @GetMapping("/dbSelectByIds")
    List<AppraiseStep> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String ids);

    @GetMapping("/selectByAppraiseId")
    List<AppraiseStep> selectByAppraiseId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Long appraiseId);
}
