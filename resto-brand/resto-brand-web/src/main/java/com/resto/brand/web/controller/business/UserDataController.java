package com.resto.brand.web.controller.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.JdbcUtils;
import com.resto.brand.web.config.SessionKey;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.DatabaseConfig;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.DatabaseConfigService;
import com.resto.brand.web.service.ShopDetailService;

/**
 * 用于操作用户信息的控制类
 * 各种基础的查询和修改，方便工作人员对异常数据进行操作
 * @author Lmx
 *
 */
@RequestMapping("userdata")
@Controller
public class UserDataController extends GenericController {

	private String testBrandId = "31946c940e194311b117e3fff5327215";//餐加空间的品牌ID
	
	@Resource
	ShopDetailService shopDetailService;
	@Resource
	BrandService brandService;
	@Resource
    private DatabaseConfigService databaseConfigService;
	
	@RequestMapping("/list")
	public void list() {
	}
	
	@RequestMapping("/queryBrands")
	@ResponseBody
	public Result queryBrands() {
		List<Brand> brands = brandService.selectList();
		return getSuccessResult(brands);
	}
	
	@RequestMapping("/queryShops")
	@ResponseBody
	public Result queryShops(String brandId) {
		List<ShopDetail> shops = shopDetailService.selectByBrandId(brandId);
		return getSuccessResult(shops);
	}
	
	@RequestMapping("/queryUserData")
	@ResponseBody
	public Result queryUserData(String brandId,String shopId,String telephone){
		DatabaseConfig databaseConfig = databaseConfigService.selectByBrandId(brandId);
		JdbcUtils jdbcUtils = new JdbcUtils(databaseConfig.getUsername(), databaseConfig.getPassword(), databaseConfig.getDriverClassName(), databaseConfig.getUrl());
		String userInfoSql = "SELECT c.id as customerId,a.id as accountId,c.nickname,c.telephone,a.remain from tb_customer c LEFT JOIN"
				+ " tb_account a on c.account_id = a.id where c.telephone = ? ";
        String chargeOrderListSql = "select tco.charge_money chargeMoney,tco.reward_money rewardMoney,IF(tco.type = 1,'微信充值','pos充值') chargeType, " +
                "DATE_FORMAT(tco.finish_time,'%Y-%m-%d %H:%i:%S') finishTime from tb_charge_order tco " +
                "inner join tb_customer tc " +
                "on tco.customer_id = tc.id " +
                "where tc.telephone = ? " +
                "and tco.order_state = 1 " +
                "ORDER BY tco.finish_time desc";
        String accountLogListSql = "select IF(tal.payment_type = 1,CONCAT('+',tal.money),CONCAT('-',tal.money)) money, remain, remark, " +
                "DATE_FORMAT(tal.create_time,'%Y-%m-%d %H:%i:%S') createTime " +
                "from tb_account_log tal " +
                "inner join tb_customer tc " +
                "on tal.account_id = tc.account_id " +
                "where tc.telephone = ? " +
                "ORDER BY tal.create_time desc";
        String orderListSql = "select td.id,td.brand_id brandId,td.serial_number serialNumber, td.table_number tableNumber, IFNULL(td.customer_count,0) customerCount, td.meal_all_number mealAllNumber, " +
                "CASE td.order_state " +
                "\tWHEN 2 THEN '已付款' " +
                "\tWHEN 10 THEN '已确定' " +
                "\tWHEN 11 THEN '已评价' " +
                "\tWHEN 12 THEN '已分享' " +
                "END orderState, IF(td.production_status = 2,'已打印','已叫号') productionStatus, td.order_money orderMoney, " +
                "IFNULL(DATE_FORMAT(td.push_order_time,'%Y-%m-%d %H:%i:%S'),'--') pushOrderTime, " +
                "IFNULL(DATE_FORMAT(td.print_order_time,'%Y-%m-%d %H:%i:%S'),'--') printOrderTime, " +
                "IFNULL(DATE_FORMAT(td.call_number_time,'%Y-%m-%d %H:%i:%S'),'--') callNumberTime " +
                "from tb_order td " +
                "inner join tb_customer tc " +
                "on td.customer_id = tc.id " +
                "where td.order_state in (2,10,11,12) " +
                "and td.production_status in (2,3) " +
                "and tc.telephone = ? " +
                "ORDER BY td.create_time desc";
		List<Object> params = new ArrayList<Object>(); 
		params.add(telephone);
		JSONObject jsonObject = new JSONObject();
		Map<String, Object> userInfo = null;
		List<Map<String, Object>> chargeOrderList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> accountLogList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		try {
            jdbcUtils.getConnection();
			userInfo = jdbcUtils.findSimpleResult(userInfoSql, params);
			jsonObject.put("userInfo",userInfo);
            JdbcUtils.close();
            jdbcUtils.getConnection();
            chargeOrderList = jdbcUtils.findModeResult(chargeOrderListSql, params);
            jsonObject.put("chargeOrderList",chargeOrderList);
            JdbcUtils.close();
            jdbcUtils.getConnection();
            accountLogList = jdbcUtils.findModeResult(accountLogListSql, params);
            jsonObject.put("accountLogList",accountLogList);
            JdbcUtils.close();
            jdbcUtils.getConnection();
            orderList = jdbcUtils.findModeResult(orderListSql, params);
            jsonObject.put("orderList",orderList);
            JdbcUtils.close();
			return getSuccessResult(jsonObject);
		} catch (SQLException e) {
			e.printStackTrace();
			JdbcUtils.close();
			return new Result("查询失败，后台报错了！",false);
		}
	}
	
	@RequestMapping("/updateUserData")
	@ResponseBody
	public Result updateUserData(String brandId,String accountId,String remain,@RequestParam(defaultValue="")String pwd ){
		//如果操作的品牌不是 餐加空间的话，则需要独立密码验证
		if(!testBrandId.equals(brandId) && !SessionKey.SECRET_KEY.equals(ApplicationUtils.pwd(pwd))){
			return new Result("迷之口令错误！",false);
		}
		DatabaseConfig databaseConfig = databaseConfigService.selectByBrandId(brandId);
		JdbcUtils jdbcUtils = new JdbcUtils(databaseConfig.getUsername(), databaseConfig.getPassword(), databaseConfig.getDriverClassName(), databaseConfig.getUrl());
		jdbcUtils.getConnection();
		String sql = "update tb_account set remain = ? where id = ?";
		List<Object> params = new ArrayList<Object>(); 
		params.add(remain);
		params.add(accountId);
		try {
			boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);
			JdbcUtils.close();
			return new Result(flag?null:"操作失败！",flag);
		} catch (SQLException e) {
			e.printStackTrace();
			JdbcUtils.close();
			return new Result("操作失败，后台报错了！",false);
		}
	}
	

	@RequestMapping("/deleteUserData")
	@ResponseBody
	public Result deleteUserData(String brandId,String customerId,String accountId){
		DatabaseConfig databaseConfig = databaseConfigService.selectByBrandId(brandId);
		JdbcUtils jdbcUtils = new JdbcUtils(databaseConfig.getUsername(), databaseConfig.getPassword(), databaseConfig.getDriverClassName(), databaseConfig.getUrl());
		jdbcUtils.getConnection();
		try {
			String sql = "DELETE from tb_customer where id = ?";
			List<Object> params = new ArrayList<Object>(); 
			params.add(customerId);
			boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);
			if(flag){
				sql = "DELETE from tb_account where id = ?";
				params.clear();
				params.add(accountId);
				flag = jdbcUtils.updateByPreparedStatement(sql, params);
			}
			JdbcUtils.close();
			log.info("用户ID："+getCurrentBrandId()+"  执行了SQL语句：  DELETE from tb_customer where id = "+accountId+" ；");
			return new Result(flag?null:"操作失败！",flag);
		} catch (SQLException e) {
			e.printStackTrace();
			JdbcUtils.close();
			return new Result("操作失败，后台报错了！",false);
		}
	}

	@RequestMapping("/initOrderInfo")
	public String initOrderInfo(String brandId, String orderId, HttpServletRequest request){
	    request.setAttribute("orderId",orderId);
        request.setAttribute("brandId",brandId);
        return "userdata/orderInfo";
    }

    @RequestMapping("/orderInfo")
    @ResponseBody
    public Result orderInfo(String brandId, String orderId){
        JSONObject result = new JSONObject();
        try {
            DatabaseConfig databaseConfig = databaseConfigService.selectByBrandId(brandId);
            JdbcUtils jdbcUtils = new JdbcUtils(databaseConfig.getUsername(), databaseConfig.getPassword(), databaseConfig.getDriverClassName(), databaseConfig.getUrl());
            String orderInfoSql = "select toi.article_name articleName, " +
                    "toi.orgin_count orginCount, " +
                    "toi.count, " +
                    "toi.refund_count refundCount, " +
                    "toi.meal_fee_number mealFeeNumber, " +
                    "toi.final_price finalPrice " +
                    "from tb_order_item toi " +
                    "where toi.order_id = ?";
            String payMentSql = "select top.pay_value payValue,(" +
                    "CASE top.payment_mode_id " +
                    "WHEN 1 THEN '微信支付' " +
                    "WHEN 2 THEN '红包支付' " +
                    "WHEN 3 THEN '优惠券支付' " +
                    "WHEN 4 THEN '其他方式支付' " +
                    "WHEN 5 THEN '银行卡支付' " +
                    "WHEN 6 THEN '充值金额支付' " +
                    "WHEN 7 THEN '充值赠送的金额支付' " +
                    "WHEN 8 THEN '等位红包' " +
                    "WHEN 9 THEN '饿了吗' " +
                    "WHEN 10 THEN '支付宝' " +
                    "WHEN 11 THEN '菜品退款支付' " +
                    "WHEN 12 THEN '现金支付' " +
                    "END" +
                    ") paymentMode,IFNULL(DATE_FORMAT(top.pay_time,'%Y-%m-%d %H:%i:%S'),'--') payTime,top.remark " +
                    "from tb_order_payment_item top " +
                    "where top.order_id = ?";
            List<Object> params = new ArrayList<Object>();
            params.add(orderId);
            List<Map<String, Object>> orderItemList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> payMentList = new ArrayList<Map<String, Object>>();
            jdbcUtils.getConnection();
            orderItemList = jdbcUtils.findModeResult(orderInfoSql, params);
            result.put("orderItemList",orderItemList);
            JdbcUtils.close();
            jdbcUtils.getConnection();
            payMentList = jdbcUtils.findModeResult(payMentSql, params);
            result.put("payMentList",payMentList);
            JdbcUtils.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.close();
            return new Result(false);
        }
        return getSuccessResult(result);
    }
}
