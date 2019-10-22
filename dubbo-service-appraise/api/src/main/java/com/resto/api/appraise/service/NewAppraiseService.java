package com.resto.api.appraise.service;

import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.exception.AppException;
import com.resto.api.appraise.util.ProjectConstant;
import com.resto.conf.db.IBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@FeignClient(value = ProjectConstant.BASE_NAME)
@Api(description = "老版评论", position = 1)
@RequestMapping(value = ProjectConstant.OLD, produces = MediaType.APPLICATION_JSON_VALUE)
public interface NewAppraiseService{

	@ApiOperation(value = "增加")
	@PostMapping("/dbSave")
	Appraise dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody Appraise appraise);

	@ApiOperation(value = "批量增加")
	@PostMapping("/dbInsertList")
	int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody List<Appraise> list);

	@ApiOperation(value = "根据条件删除")
	@GetMapping("/dbDelete")
	int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Appraise appraise);

	@ApiOperation(value = "主键id删除")
	@GetMapping("/dbDeleteByPrimaryKey")
	int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object var);

	@ApiOperation(value = "主键id进行查询")
	@GetMapping("/dbDeleteByIds")
	int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String var);

	@ApiOperation(value = "更新")
	@PostMapping("/dbUpdate")
	int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Appraise appraise);

	@ApiOperation(value = "根据条件list查询")
	@GetMapping("/dbSelect")
	List<Appraise> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody Appraise t);

	@ApiOperation(value = "分页查询")
	@GetMapping("/dbSelectPage")
	List<Appraise> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Appraise t, Integer pageNum, Integer pageSize);

	@ApiOperation(value = "主键id查询")
	@GetMapping("/dbSelectByPrimaryKey")
	Appraise dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object key);

	@ApiOperation(value = "根据条件单条查询")
	@GetMapping("/dbSelectOne")
	Appraise dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Appraise record);

	@ApiOperation(value = "根据条件查询条数")
	@GetMapping("/dbSelectCount")
	int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Appraise record);

	@ApiOperation(value = "根据主键id查询list")
	@GetMapping("/dbSelectByIds")
	List<Appraise> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String ids);
	
	/**
	 * 根据当前 店铺ID 查询所有的评论数量和平均分
	 * @param currentShopId
	 * @return
	 */
	@GetMapping("/appraiseCount")
	Map<String, Object> appraiseCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String currentShopId);
	
	/**
	 * 根据当前 店铺ID 查询每个月的评论数量和平均分
	 * @param currentShopId
	 * @return
	 */
	@GetMapping("/appraiseMonthCount")
	List<Map<String, Object>> appraiseMonthCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String currentShopId);

	@GetMapping("/selectDetailedById")
	Appraise selectDetailedById(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String appraiseId);

	@GetMapping("/selectDeatilByOrderId")
	Appraise selectDeatilByOrderId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String orderId, String customerId);

	@GetMapping("/selectAppraiseByCustomerId")
	Appraise selectAppraiseByCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId, String shopId);

	@GetMapping("/selectCustomerAllAppraise")
	List<Appraise> selectCustomerAllAppraise(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId, Integer currentPage, Integer showCount);

	@GetMapping("/selectByCustomerCount")
	int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId);

	/**
	 * 查询每个品牌前500条好评
	 * @return
     */
	@GetMapping("/selectByGoodAppraise")
	List<Appraise> selectByGoodAppraise(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId);
	
	/**
	 * 查询用户综合评分
	 * @return
	 */
	@GetMapping("/selectCustomerAppraiseAvg")
	Map<String, Object> selectCustomerAppraiseAvg(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId);

    /**
     * 查询时间段内的评论
     * @param begin
     * @param end
     * @param shopId
     * @return
     */
	@GetMapping("/selectByTimeAndShopId")
    List<Appraise> selectByTimeAndShopId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String shopId, String begin, String end);

	/**
	 * yz 2017/07/28
	 * 时间内品牌分数查询
	 * @param begin
	 * @param end
	 * @return
	 */
	@GetMapping("/selectByTimeAndBrandId")
	List<Appraise> selectByTimeAndBrandId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Date begin, Date end);

	/**
	 * 该订单下某人的评论记录
	 * @param orderId
	 * @param customerId
     * @return
     */
	@GetMapping("/selectByOrderIdCustomerId")
	Appraise selectByOrderIdCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String orderId, String customerId);

	/**
	 * 根据 店铺id 和 用户id 查询所有评论，以时间倒序
	 * @param shopId
	 * @param customerId
	 * @return
	 */
	@GetMapping("/selectAllAppraiseByShopIdAndCustomerId")
	List<Appraise> selectAllAppraiseByShopIdAndCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String shopId, String customerId);

	@GetMapping("/listAppraise")
	List<Appraise> listAppraise(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String currentShopId,Integer currentPage,Integer showCount,Integer maxLevel,Integer minLevel);
}
