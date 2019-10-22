package com.resto.api.customer.service;

import com.resto.api.customer.entity.Customer;
import com.resto.api.customer.exception.AppException;
import com.resto.api.customer.util.ProjectConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by disvenk.dai on 2018-10-17 11:07
 */
@FeignClient(value = ProjectConstant.BASE_NAME)
@Api(description = "用户信息 ", position = 1)
@RequestMapping(value = ProjectConstant.OLD, produces = MediaType.APPLICATION_JSON_VALUE)
public interface NewCustomerService {

    @ApiOperation(value = "增加")
    @PostMapping("/dbSave")
    Customer dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody Customer customer);

    @ApiOperation(value = "批量增加")
    @PostMapping("/dbInsertList")
    int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody List<Customer> list);

    @ApiOperation(value = "根据条件删除")
    @GetMapping("/dbDelete")
    int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Customer customer);

    @ApiOperation(value = "主键id删除")
    @GetMapping("/dbDeleteByPrimaryKey")
    int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object var);

    @ApiOperation(value = "主键id进行查询")
    @GetMapping("/dbDeleteByIds")
    int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String var);

    @ApiOperation(value = "更新")
    @PostMapping("/dbUpdate")
    int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Customer customer);

    @ApiOperation(value = "根据条件list查询")
    @GetMapping("/dbSelect")
    List<Customer> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody Customer t);

    @ApiOperation(value = "分页查询")
    @GetMapping("/dbSelectPage")
    List<Customer> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Customer t, Integer pageNum, Integer pageSize);

    @ApiOperation(value = "主键id查询")
    @GetMapping("/dbSelectByPrimaryKey")
    Customer dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object key);

    @ApiOperation(value = "根据条件单条查询")
    @GetMapping("/dbSelectOne")
    Customer dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Customer record);

    @ApiOperation(value = "根据条件查询条数")
    @GetMapping("/dbSelectCount")
    int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Customer record);

    @ApiOperation(value = "根据主键id查询list")
    @GetMapping("/dbSelectByIds")
    List<Customer> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String ids);

    @GetMapping("/login")
    public Customer login(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String openid);

    @GetMapping("/selectById")
    public Customer selectById(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String id);

    @GetMapping("/register")
    public Customer register(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Customer customer);

    @GetMapping("/registerCard")
    public Customer registerCard(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Customer customer);

    @GetMapping("/updateCustomer")
    public void updateCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Customer customer);

    @GetMapping("/bindPhone")
    public Customer bindPhone(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String phone, String currentCustomerId,Integer couponType,String shopId,String shareCustomer,String shareOrderId) throws AppException;

    @GetMapping("/selectNickNameAndTelephone")
    public Customer selectNickNameAndTelephone(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId);

    @GetMapping("/selectNickNameAndTelephone")
    public List<Customer> selectListByBrandId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String currentBrandId);

    @GetMapping("/selectTelephoneList")
    public List<String> selectTelephoneList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId);

    @GetMapping("/unbindphone")
    public void unbindphone(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String currentCustomerId);

    @GetMapping("/updateNewNoticeTime")
    public void updateNewNoticeTime(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String id);

    @GetMapping("/updateFirstOrderTime")
    public void updateFirstOrderTime(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String id);

    @GetMapping("/checkRegistered")
    public Boolean checkRegistered(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String id);

    @GetMapping("/selectByOpenIdInfo")
    public Customer selectByOpenIdInfo(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String openId);

    @GetMapping("/selectListByBrandIdHasRegister")
    public List<Customer> selectListByBrandIdHasRegister(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String beginDate, String endDate);

    @GetMapping("/selectCustomerAccount")
    public Customer selectCustomerAccount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String telephone);

    @GetMapping("/selectByShareCustomer")
    public Integer selectByShareCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId);

    @GetMapping("/selectBirthUser")
    public List<Customer> selectBirthUser(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId);

    @GetMapping("/selectByTelePhone")
    public Customer selectByTelePhone(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String telePhone);

    @GetMapping("/selectBySerialNumber")
    public Customer selectBySerialNumber(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String number);

    @GetMapping("/getCustomerLimitOne")
    public Customer getCustomerLimitOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId);

    @GetMapping("/selectByTelePhones")
    public List<Customer> selectByTelePhones(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,List<String> telePhones);

    @GetMapping("/getCommentCustomer")
    public List<Customer> getCommentCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String startTime, Integer time,Integer type);

    @GetMapping("/selectShareCustomerList")
    public List<Customer> selectShareCustomerList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId, Integer currentPage, Integer showCount);

    @GetMapping("/updateCustomerWechatId")
    public int updateCustomerWechatId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Customer customer);

    @GetMapping("/selectByAccountId")
    public Customer selectByAccountId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String accountId);

    @GetMapping("/selectCustomerByUnionId")
    public String selectCustomerByUnionId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String unionId);













}
