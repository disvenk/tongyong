package com.resto.api.appraise.service;

import com.resto.api.appraise.entity.AppraisePraise;
import com.resto.api.appraise.entity.AppraisePraise;
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
@Api(description = "点赞", position = 1)
@RequestMapping(value = ProjectConstant.PRAISE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface NewAppraisePraiseService{

    @ApiOperation(value = "增加")
    @PostMapping("/dbSave")
    int dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody AppraisePraise appraisePraise);

    @ApiOperation(value = "批量增加")
    @PostMapping("/dbInsertList")
    int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody List<AppraisePraise> list);

    @ApiOperation(value = "根据条件删除")
    @GetMapping("/dbDelete")
    int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraisePraise appraisePraise);

    @ApiOperation(value = "主键id删除")
    @GetMapping("/dbDeleteByPrimaryKey")
    int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object var);

    @ApiOperation(value = "主键id进行查询")
    @GetMapping("/dbDeleteByIds")
    int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String var);

    @ApiOperation(value = "更新")
    @PostMapping("/dbUpdate")
    int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraisePraise appraisePraise);

    @ApiOperation(value = "根据条件list查询")
    @GetMapping("/dbSelect")
    List<AppraisePraise> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody AppraisePraise t);

    @ApiOperation(value = "分页查询")
    @GetMapping("/dbSelectPage")
    List<AppraisePraise> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraisePraise t, Integer pageNum, Integer pageSize);

    @ApiOperation(value = "主键id查询")
    @GetMapping("/dbSelectByPrimaryKey")
    AppraisePraise dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object key);

    @ApiOperation(value = "根据条件单条查询")
    @GetMapping("/dbSelectOne")
    AppraisePraise dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraisePraise record);

    @ApiOperation(value = "根据条件查询条数")
    @GetMapping("/dbSelectCount")
    int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraisePraise record);

    @ApiOperation(value = "根据主键id查询list")
    @GetMapping("/dbSelectByIds")
    List<AppraisePraise> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String ids);

    @PostMapping("/updateCancelPraise")
    AppraisePraise updateCancelPraise(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String appraiseId, String customerId, Integer isDel);

    @PostMapping("/updateCancelPraises")
    AppraisePraise updateCancelPraise(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraisePraise appraisePraise);

    @GetMapping("/appraisePraiseList")
    List<AppraisePraise> appraisePraiseList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String appraiseId);

    @GetMapping("/selectByAppraiseIdCustomerId")
    AppraisePraise selectByAppraiseIdCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String appraiseId, String customerId);

    @GetMapping("/selectByCustomerCount")
    int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId);
}
