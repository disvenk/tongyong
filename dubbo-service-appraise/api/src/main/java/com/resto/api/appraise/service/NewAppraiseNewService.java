package com.resto.api.appraise.service;

import com.resto.api.appraise.dto.NewAppraiseCustomerDto;
import com.resto.api.appraise.entity.AppraiseNew;
import com.resto.api.appraise.util.ProjectConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(value = ProjectConstant.BASE_NAME)
@Api(description = "新版评论（有评论维度）", position = 1)
@RequestMapping(value = ProjectConstant.NEW, produces = MediaType.APPLICATION_JSON_VALUE)
public interface NewAppraiseNewService{

    @ApiOperation(value = "增加")
    @PostMapping("/dbSave")
    int dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody AppraiseNew appraiseNew);

    @ApiOperation(value = "批量增加")
    @PostMapping("/dbInsertList")
    int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody List<AppraiseNew> list);

    @ApiOperation(value = "根据条件删除")
    @GetMapping("/dbDelete")
    int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseNew appraiseNew);

    @ApiOperation(value = "主键id删除")
    @GetMapping("/dbDeleteByPrimaryKey")
    int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object var);

    @ApiOperation(value = "主键id进行查询")
    @GetMapping("/dbDeleteByIds")
    int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String var);

    @ApiOperation(value = "更新")
    @PostMapping("/dbUpdate")
    int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseNew appraiseNew);

    @ApiOperation(value = "根据条件list查询")
    @GetMapping("/dbSelect")
    List<AppraiseNew> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody AppraiseNew t);

    @ApiOperation(value = "分页查询")
    @GetMapping("/dbSelectPage")
    List<AppraiseNew> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseNew t, Integer pageNum, Integer pageSize);

    @ApiOperation(value = "主键id查询")
    @GetMapping("/dbSelectByPrimaryKey")
    AppraiseNew dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object key);

    @ApiOperation(value = "根据条件单条查询")
    @GetMapping("/dbSelectOne")
    AppraiseNew dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseNew record);

    @ApiOperation(value = "根据条件查询条数")
    @GetMapping("/dbSelectCount")
    int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseNew record);

    @ApiOperation(value = "根据主键id查询list")
    @GetMapping("/dbSelectByIds")
    List<AppraiseNew> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String ids);
    
    @GetMapping("/selectByOrderIdCustomerId")
    AppraiseNew selectByOrderIdCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String orderId, String customerId);

    @GetMapping("/ListAppraiseCustomer")
    List<NewAppraiseCustomerDto> ListAppraiseCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Integer currentPage, Integer showCount, Integer level, String shopId);

    @GetMapping("/ListAppraiseCustomerId")
    List<NewAppraiseCustomerDto> ListAppraiseCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Integer currentPage, Integer showCount, String customerId, String shopId);

    @GetMapping("/selectByAppraiseId")
    NewAppraiseCustomerDto selectByAppraiseId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String appraiseId);

    @GetMapping("/selectByCustomerCount")
    int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId);

    @GetMapping("/appraiseCount")
    Map<String, Object> appraiseCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String currentShopId);

    @GetMapping("/appraiseMonthCount")
    List<Map<String, Object>> appraiseMonthCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String currentShopId);

    @GetMapping("/selectByTimeAndShopId")
    List<AppraiseNew> selectByTimeAndShopId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String shopId, String begin, String end);

    @PostMapping("/add")
    AppraiseNew add(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AppraiseNew appraiseNew);
}
