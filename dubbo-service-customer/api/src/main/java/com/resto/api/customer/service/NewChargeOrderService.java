package com.resto.api.customer.service;

import com.resto.api.customer.entity.AccountLog;
import com.resto.api.customer.entity.ChargeOrder;
import com.resto.api.customer.util.ProjectConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-10-30 17:46
 */

@FeignClient(value = ProjectConstant.BASE_NAME)
@Api(description = "充值订单", position = 1)
@RequestMapping(value = ProjectConstant.OLD, produces = MediaType.APPLICATION_JSON_VALUE)
public interface NewChargeOrderService {
    @ApiOperation(value = "增加")
    @PostMapping("/dbSave")
    ChargeOrder dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody ChargeOrder chargeOrder);

    @ApiOperation(value = "批量增加")
    @PostMapping("/dbInsertList")
    int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody List<ChargeOrder> list);

    @ApiOperation(value = "根据条件删除")
    @GetMapping("/dbDelete")
    int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody ChargeOrder chargeOrder);

    @ApiOperation(value = "主键id删除")
    @GetMapping("/dbDeleteByPrimaryKey")
    int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object var);

    @ApiOperation(value = "主键id进行查询")
    @GetMapping("/dbDeleteByIds")
    int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String var);

    @ApiOperation(value = "更新")
    @PostMapping("/dbUpdate")
    int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody ChargeOrder chargeOrder);

    @ApiOperation(value = "根据条件list查询")
    @GetMapping("/dbSelect")
    List<ChargeOrder> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody ChargeOrder t);

    @ApiOperation(value = "分页查询")
    @GetMapping("/dbSelectPage")
    List<ChargeOrder> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody ChargeOrder t, Integer pageNum, Integer pageSize);

    @ApiOperation(value = "主键id查询")
    @GetMapping("/dbSelectByPrimaryKey")
    ChargeOrder dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object key);

    @ApiOperation(value = "根据条件单条查询")
    @GetMapping("/dbSelectOne")
    ChargeOrder dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody ChargeOrder record);

    @ApiOperation(value = "根据条件查询条数")
    @GetMapping("/dbSelectCount")
    int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody ChargeOrder record);

    @ApiOperation(value = "根据主键id查询list")
    @GetMapping("/dbSelectByIds")
    List<ChargeOrder> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String ids);

    @GetMapping("/refundCharge")
    public void refundCharge(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,BigDecimal payValue, String id,String shopDetailId);

    @GetMapping("/refundMoney")
    public void refundMoney(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,BigDecimal charge, BigDecimal reward, String id, String shopDetailId);

    @GetMapping("/refundReward")
    public void refundReward(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,BigDecimal payValue, String id,String shopDetailId);

    @GetMapping("/selectByDateAndShopId")
    public List<ChargeOrder> selectByDateAndShopId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String beginDate, String endDate, String shopId);

    @GetMapping("/selectByDateAndBrandId")
    public List<ChargeOrder> selectByDateAndBrandId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String beginDate, String endDate);

    @GetMapping("/selectByShopToDay")
    public List<Map<String, Object>> selectByShopToDay(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Map<String, Object> selectMap);

    @GetMapping("/selectListByDateAndShopId")
    public List<ChargeOrder> selectListByDateAndShopId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String zuoriDay, String zuoriDay1, String id);

    @GetMapping("/selectByCustomerIdAndBrandId")
    public List<ChargeOrder> selectByCustomerIdAndBrandId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId);

    @GetMapping("/getChargeSumInfo")
    public List<Map<String, Object>> getChargeSumInfo(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Map<String, Object> selectMap);

    @GetMapping("/selectCustomerChargeOrder")
    public List<String> selectCustomerChargeOrder(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,List<String> customerIds);

    @GetMapping("/selectTotalBalanceByTimeAndShopId")
    public BigDecimal selectTotalBalanceByTimeAndShopId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Date monthBegin, Date monthEnd, String shopId);

    @GetMapping("/selectRemainderReturn")
    public List<ChargeOrder> selectRemainderReturn(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId);

    @GetMapping("/updateRemainderReturn")
    public void updateRemainderReturn(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,ChargeOrder chargeOrder);

    @GetMapping("/selectByCustomerIdNotChangeId")
    public BigDecimal selectByCustomerIdNotChangeId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId);
}
