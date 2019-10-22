package com.resto.api.customer.service;

import com.resto.api.customer.entity.Account;
import com.resto.api.customer.entity.AccountLog;
import com.resto.api.customer.util.ProjectConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-10-18 11:31
 */

@FeignClient(value = ProjectConstant.BASE_NAME)
@Api(description = "用户账户日志 ", position = 1)
@RequestMapping(value = ProjectConstant.OLD, produces = MediaType.APPLICATION_JSON_VALUE)
public interface NewAccountLogService {
    @ApiOperation(value = "增加")
    @PostMapping("/dbSave")
    AccountLog dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody AccountLog accountLog);

    @ApiOperation(value = "批量增加")
    @PostMapping("/dbInsertList")
    int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody List<AccountLog> list);

    @ApiOperation(value = "根据条件删除")
    @GetMapping("/dbDelete")
    int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AccountLog accountLog);

    @ApiOperation(value = "主键id删除")
    @GetMapping("/dbDeleteByPrimaryKey")
    int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object var);

    @ApiOperation(value = "主键id进行查询")
    @GetMapping("/dbDeleteByIds")
    int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String var);

    @ApiOperation(value = "更新")
    @PostMapping("/dbUpdate")
    int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody AccountLog accountLog);

    @ApiOperation(value = "根据条件list查询")
    @GetMapping("/dbSelect")
    List<AccountLog> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody AccountLog t);

    @ApiOperation(value = "分页查询")
    @GetMapping("/dbSelectPage")
    List<AccountLog> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AccountLog t, Integer pageNum, Integer pageSize);

    @ApiOperation(value = "主键id查询")
    @GetMapping("/dbSelectByPrimaryKey")
    AccountLog dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object key);

    @ApiOperation(value = "根据条件单条查询")
    @GetMapping("/dbSelectOne")
    AccountLog dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AccountLog record);

    @ApiOperation(value = "根据条件查询条数")
    @GetMapping("/dbSelectCount")
    int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody AccountLog record);

    @ApiOperation(value = "根据主键id查询list")
    @GetMapping("/dbSelectByIds")
    List<AccountLog> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String ids);

    @GetMapping("/selectLogsByAccountId")
    public List<AccountLog> selectLogsByAccountId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String accountId);

    @GetMapping("/selectByCustomerIdNumber")
    public int selectByCustomerIdNumber(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String id);

    @GetMapping("/selectBrandMarketing")
    public List<String> selectBrandMarketing(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Map<String, String> selectMap);

    @GetMapping("/selectByShareMoney")
    public BigDecimal selectByShareMoney(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String accountId);

    @GetMapping("/selectByShareMoneyCount")
    public Integer selectByShareMoneyCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String accountId);

    @GetMapping("/selectByOrderId")
    public AccountLog selectByOrderId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String orderId);
}
