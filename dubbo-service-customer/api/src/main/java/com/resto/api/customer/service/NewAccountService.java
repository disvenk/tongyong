package com.resto.api.customer.service;

import com.resto.api.customer.entity.Account;
import com.resto.api.customer.entity.Customer;
import com.resto.api.customer.util.ProjectConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-10-18 10:35
 */
@FeignClient(value = ProjectConstant.BASE_NAME)
@Api(description = "用户账户 ", position = 1)
@RequestMapping(value = ProjectConstant.OLD, produces = MediaType.APPLICATION_JSON_VALUE)
public interface NewAccountService {
    @ApiOperation(value = "增加")
    @PostMapping("/dbSave")
    Account dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody Account account);

    @ApiOperation(value = "批量增加")
    @PostMapping("/dbInsertList")
    int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody List<Account> list);

    @ApiOperation(value = "根据条件删除")
    @GetMapping("/dbDelete")
    int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Account account);

    @ApiOperation(value = "主键id删除")
    @GetMapping("/dbDeleteByPrimaryKey")
    int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object var);

    @ApiOperation(value = "主键id进行查询")
    @GetMapping("/dbDeleteByIds")
    int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String var);

    @ApiOperation(value = "更新")
    @PostMapping("/dbUpdate")
    int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody Account account);

    @ApiOperation(value = "根据条件list查询")
    @GetMapping("/dbSelect")
    List<Account> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody Account t);

    @ApiOperation(value = "分页查询")
    @GetMapping("/dbSelectPage")
    List<Account> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Account t, Integer pageNum, Integer pageSize);

    @ApiOperation(value = "主键id查询")
    @GetMapping("/dbSelectByPrimaryKey")
    Account dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object key);

    @ApiOperation(value = "根据条件单条查询")
    @GetMapping("/dbSelectOne")
    Account dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Account record);

    @ApiOperation(value = "根据条件查询条数")
    @GetMapping("/dbSelectCount")
    int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Account record);

    @ApiOperation(value = "根据主键id查询list")
    @GetMapping("/dbSelectByIds")
    List<Account> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String ids);

    @GetMapping("/useAccount")
    public BigDecimal useAccount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,BigDecimal payMoney, Account account, Integer source, String shopDetailId);

    @GetMapping("/addAccount")
    public void addAccount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,BigDecimal value, String accountId, String remark,Integer source,String shopDetailId);

    @GetMapping("/selectAccountAndLogByCustomerId")
    public Account selectAccountAndLogByCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId);

    @GetMapping("/createCustomerAccount")
    public Account createCustomerAccount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Customer cus);

    @GetMapping("/selectRebate")
    public List<Account> selectRebate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId);

    @GetMapping("/selectAccountByCustomerId")
    public Account selectAccountByCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId);

    @GetMapping("/selectAccountByIds")
    public List<Account> selectAccountByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,List<String> ids);
}
