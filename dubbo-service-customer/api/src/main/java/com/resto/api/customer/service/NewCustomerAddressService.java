package com.resto.api.customer.service;

import com.resto.api.customer.entity.ChargeOrder;
import com.resto.api.customer.entity.Customer;
import com.resto.api.customer.entity.CustomerAddress;
import com.resto.api.customer.util.ProjectConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by disvenk.dai on 2018-10-30 18:10
 */

@FeignClient(value = ProjectConstant.BASE_NAME)
@Api(description = "用户地址信息", position = 1)
@RequestMapping(value = ProjectConstant.OLD, produces = MediaType.APPLICATION_JSON_VALUE)
public interface NewCustomerAddressService {
    @ApiOperation(value = "增加")
    @PostMapping("/dbSave")
    CustomerAddress dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody CustomerAddress customerAddress);

    @ApiOperation(value = "批量增加")
    @PostMapping("/dbInsertList")
    int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody List<CustomerAddress> list);

    @ApiOperation(value = "根据条件删除")
    @GetMapping("/dbDelete")
    int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody CustomerAddress customerAddress);

    @ApiOperation(value = "主键id删除")
    @GetMapping("/dbDeleteByPrimaryKey")
    int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object var);

    @ApiOperation(value = "主键id进行查询")
    @GetMapping("/dbDeleteByIds")
    int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String var);

    @ApiOperation(value = "更新")
    @PostMapping("/dbUpdate")
    int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody CustomerAddress customerAddress);

    @ApiOperation(value = "根据条件list查询")
    @GetMapping("/dbSelect")
    List<CustomerAddress> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody CustomerAddress t);

    @ApiOperation(value = "分页查询")
    @GetMapping("/dbSelectPage")
    List<CustomerAddress> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody CustomerAddress t, Integer pageNum, Integer pageSize);

    @ApiOperation(value = "主键id查询")
    @GetMapping("/dbSelectByPrimaryKey")
    CustomerAddress dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object key);

    @ApiOperation(value = "根据条件单条查询")
    @GetMapping("/dbSelectOne")
    CustomerAddress dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody CustomerAddress record);

    @ApiOperation(value = "根据条件查询条数")
    @GetMapping("/dbSelectCount")
    int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody CustomerAddress record);

    @ApiOperation(value = "根据主键id查询list")
    @GetMapping("/dbSelectByIds")
    List<CustomerAddress> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String ids);

    @GetMapping("/deleteByPrimaryKey")
    public int deleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String id);

    @GetMapping("/insert")
    public int insert(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,CustomerAddress record);

    @GetMapping("/insertSelective")
    public int insertSelective(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,CustomerAddress record);

    @GetMapping("/selectByPrimaryKey")
    public CustomerAddress selectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String id);

    @GetMapping("/updateByPrimaryKeySelective")
    public int updateByPrimaryKeySelective(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,CustomerAddress record);

    @GetMapping("/updateByPrimaryKey")
    public int updateByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,CustomerAddress record);

    @GetMapping("/selectOneList")
    public List<CustomerAddress> selectOneList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customer_id);

}
