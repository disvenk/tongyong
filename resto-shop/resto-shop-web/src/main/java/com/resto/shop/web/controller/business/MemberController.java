package com.resto.shop.web.controller.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.AppendToExcelUtil;
import com.resto.brand.core.util.ExcelUtil;
import com.resto.brand.web.dto.MemberSelectionDto;
import com.resto.brand.web.dto.MemberUserDto;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Coupon;
import com.resto.shop.web.service.CouponService;
import com.resto.shop.web.service.CustomerService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

 @Controller
@RequestMapping("member")
public class MemberController extends GenericController{
	
	@Resource
	private CustomerService customerService;

	@Resource
	private ShopDetailService shopDetailService;

	@Autowired
	private CouponService couponService;

	
	@RequestMapping("/myList")
	public String list(){
        return "member/myList";
	}
	
	//查询当前店铺的所有用户
	@RequestMapping("/myConList")
	@ResponseBody
	public Result selectAllMath(String beginDate,String endDate){
	    JSONObject object = new JSONObject();
	    try {
            //得到品牌用户信息
            String count = customerService.selectBrandUser();
            String[] counts = new String[0];
            if (StringUtils.isNotBlank(count)) {
                counts = count.split(",");
            }
            Integer customerCount = 0;
            Integer registeredCustomerCount = 0;
            Integer unregisteredCustomerCount = 0;
            Integer maleCustomerCount = 0;
            Integer femaleCustomerCount = 0;
            Integer unknownCustomerCount = 0;
            Integer i = 0;
            for (String str : counts) {
                switch (i) {
                    case 0:
                        customerCount = Integer.valueOf(str);
                        break;
                    case 1:
                        registeredCustomerCount = Integer.valueOf(str);
                        break;
                    case 2:
                        unregisteredCustomerCount = Integer.valueOf(str);
                        break;
                    case 3:
                        maleCustomerCount = Integer.valueOf(str);
                        break;
                    case 4:
                        femaleCustomerCount = Integer.valueOf(str);
                        break;
                    case 5:
                        unknownCustomerCount = Integer.valueOf(str);
                        break;
                    default:
                        break;
                }
                i++;
            }
            JSONObject brandCustomerCount = new JSONObject();
            brandCustomerCount.put("brandName", getBrandName());
            brandCustomerCount.put("customerCount", customerCount);
            brandCustomerCount.put("registeredCustomerCount", registeredCustomerCount);
            brandCustomerCount.put("unregisteredCustomerCount", unregisteredCustomerCount);
            brandCustomerCount.put("maleCustomerCount", maleCustomerCount);
            brandCustomerCount.put("femaleCustomerCount", femaleCustomerCount);
            brandCustomerCount.put("unknownCustomerCount", unknownCustomerCount);
            List<MemberUserDto> memberUserDtos = customerService.callListMemberUser(beginDate, endDate);
            object.put("brandCustomerCount", brandCustomerCount);
            object.put("memberUserDtos", memberUserDtos);
        }catch (Exception e){
            log.error("查询会员信息报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
		return getSuccessResult(object);
	}
	
	 /**
     * 查询当前用户的优惠券 wql
     * 
     * @return
     */
	
	@RequestMapping("/show/billReport")
	public String showModal(String customerId,HttpServletRequest request){
		request.setAttribute("customerId", customerId);
		return "member/billReport";
	}
	
	
	@RequestMapping("/list_all_shopId")
    @ResponseBody
	public Result list_all_shopId(String customerId){
	    JSONObject object = new JSONObject();
	    try {
            List<Coupon> list =  couponService.getListByCustomerId(customerId);
            List<ShopDetail> shopDetails = shopDetailService.selectByBrandId(getCurrentBrandId());
            object.put("coupons",list);
            object.put("shopDetails",shopDetails);
        }catch (Exception e){
            log.error("查看会员优惠券信息");
            e.printStackTrace();
            return new Result(false);
        }
		return getSuccessResult(object);
	}


	//下载会员信息报表
	@RequestMapping("member_excel")
	@ResponseBody
	public Result reportIncome(String beginDate,String endDate,MemberUserDto memberUserDto,HttpServletRequest request){
        List<ShopDetail> shopDetailList = getCurrentShopDetails();
        if(shopDetailList==null){
            shopDetailList = shopDetailService.selectByBrandId(getCurrentBrandId());
        }
        //导出文件名
        String fileName = "会员管理列表"+beginDate+"至"+endDate+".xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns={"isBindPhone","isCharge","nickname","sex","telephone","birthday","province","city"
                ,"remain","chargeRemain","presentRemain","redRemain","sumMoney","amount","money"};
        //定义数据
        List<MemberUserDto>  result = new ArrayList<>();
        if (memberUserDto.getBrandCustomerCount() != null){
            Map<String, Object> brandCustomerMap = memberUserDto.getBrandCustomerCount();
            MemberUserDto customerCount = new MemberUserDto();
            customerCount.setIsBindPhone(brandCustomerMap.get("brandName").toString());
            customerCount.setIsCharge(brandCustomerMap.get("customerCount").toString());
            customerCount.setNickname(brandCustomerMap.get("registeredCustomerCount").toString());
            customerCount.setSex(brandCustomerMap.get("unregisteredCustomerCount").toString());
            customerCount.setTelephone(brandCustomerMap.get("maleCustomerCount").toString());
            customerCount.setBirthday(brandCustomerMap.get("femaleCustomerCount").toString());
            customerCount.setRemain(brandCustomerMap.get("unknownCustomerCount").toString());
            result.add(customerCount);
        }
        if (memberUserDto.getMemberUserDtos() != null){
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("brandCustomerCount");
            filter.getExcludes().add("memberUserDtos");
            String json = JSON.toJSONString(memberUserDto.getMemberUserDtos(),filter);
            List<MemberUserDto> memberUserDtos = JSON.parseObject(json, new TypeReference<List<MemberUserDto>>(){});
            result.addAll(memberUserDtos);
        }
        String shopName="";
        for (ShopDetail shopDetail : shopDetailList) {
            shopName += shopDetail.getName()+",";
        }
        shopName = shopName.substring(0,shopName.length() - 1);
        Map<String,String> map = new HashMap<>();
        map.put("brandName", getBrandName());
        map.put("shops", shopName);
        map.put("beginDate", beginDate);
        map.put("reportType", "会员信息报表");//表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "14");//显示的位置
        map.put("reportTitle", "会员信息");//表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] headers = {{"品牌/用户类型","25"},{"用户总数/储值","25"},{"注册用户数/昵称","25"},{"未注册用户数/性别","25"},{"男性顾客/联系电话","25"},{"女性顾客/生日","25"},
                {"省/市","25"},{"城/区","25"},{"未知性别/余额","25"},{"充值金额","25"},{"充值赠送金额","25"},{"红包金额","25"},{"订单总额","25"},{"订单数","25"},{"订单平均金额","25"}};
        //定义excel工具类对象
        ExcelUtil<MemberUserDto> excelUtil=new ExcelUtil<MemberUserDto>();
        try{
            OutputStream out = new FileOutputStream(path);
            excelUtil.ExportExcel(headers, columns, result, out, map);
            out.close();
		}catch(Exception e){
		    log.error("生成会员店铺报表出错！");
			e.printStackTrace();
            return new Result(false);
		}
		return getSuccessResult(path);
	}

	@RequestMapping("/downloadExcel")
	public void downloadExcel(String path, HttpServletResponse response){
	    ExcelUtil<Object> excelUtil = new ExcelUtil<>();
	    try{
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        }catch (Exception e){
            log.error("下载会员报表出错！");
            e.printStackTrace();
        }
    }

    @RequestMapping("/appendExcel")
    @ResponseBody
    public Result appendExcel(String path, Integer startPosition, MemberUserDto memberUserDto){
        try {
            String[][] items = new String[memberUserDto.getMemberUserDtos().size()][];
            int i = 0;
            for (Map map : memberUserDto.getMemberUserDtos()){
                items[i] = new String[15];
                items[i][0] = map.get("isBindPhone").toString();
                items[i][1] = map.get("isCharge").toString();
                items[i][2] = map.get("nickname").toString();
                items[i][3] = map.get("sex").toString();
                items[i][4] = map.get("telephone").toString();
                items[i][5] = map.get("birthday").toString();
                items[i][6] = map.get("province").toString();
                items[i][7] = map.get("city").toString();
                items[i][8] = map.get("remain").toString();
                items[i][9] = map.get("chargeRemain").toString();
                items[i][10] = map.get("presentRemain").toString();
                items[i][11] = map.get("redRemain").toString();
                items[i][12] = map.get("sumMoney").toString();
                items[i][13] = map.get("amount").toString();
                items[i][14] = map.get("money").toString();
                i++;
            }
            AppendToExcelUtil.insertRows(path,startPosition,items);
        } catch (Exception e){
            log.error("追加会员报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult();
    }

	@RequestMapping("/show/orderReport")
	public String showModal1(String endDate,String beginDate,String customerId,HttpServletRequest request){
		request.setAttribute("beginDate",beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("customerId", customerId);
		return "orderReport/shopReport";
	}

    //下载会员信息报表
    @RequestMapping("/createMemberSelectionDto")
    @ResponseBody
    public Result createMemberSelectionDto(@RequestBody List<MemberSelectionDto> memberSelectionDtos, HttpServletRequest request){
        List<ShopDetail> shopDetailList = getCurrentShopDetails();
        if(shopDetailList==null){
            shopDetailList = shopDetailService.selectByBrandId(getCurrentBrandId());
        }
        //导出文件名
        String fileName = "会员筛选信息.xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns={"customerType","isValue","nickname","sex","telephone","birthday","orderCount","orderMoney"
                ,"avgOrderMoney"};
        //定义数据
        List<MemberSelectionDto>  result = memberSelectionDtos;
        String shopName = "";
        for (ShopDetail shopDetail : shopDetailList) {
            shopName += shopDetail.getName()+",";
        }
        shopName = shopName.substring(0,shopName.length() - 1);
        Map<String,String> map = new HashMap<>();
        map.put("brandName", getBrandName());
        map.put("shops", shopName);
        map.put("beginDate", "--");
        map.put("reportType", "会员筛选信息报表");//表的头，第一行内容
        map.put("endDate", "--");
        map.put("num", "8");//显示的位置
        map.put("reportTitle", "会员筛选信息报表");//表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] headers = {{"用户类型","25"},{"储值","25"},{"昵称","25"},{"性别","25"},{"手机号","25"},{"生日","25"},
                {"订单总数","25"},{"订单总额","25"},{"平均消费金额","25"}};
        //定义excel工具类对象
        ExcelUtil<MemberSelectionDto> excelUtil=new ExcelUtil<>();
        try{
            OutputStream out = new FileOutputStream(path);
            excelUtil.ExportExcel(headers, columns, result, out, map);
            out.close();
        }catch(Exception e){
            log.error("生成会员筛选信息报表");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(path);
    }

    @RequestMapping("/appendMemberSelectionExcel")
    @ResponseBody
    public Result appendMemberSelectionExcel(String path, Integer startPosition, MemberSelectionDto memberSelectionDto){
        try {
            String[][] items = new String[memberSelectionDto.getMemberSelectionDtos().size()][];
            int i = 0;
            for (Map map : memberSelectionDto.getMemberSelectionDtos()){
                items[i] = new String[9];
                items[i][0] = map.get("customerType").toString();
                items[i][1] = map.get("isValue").toString();
                items[i][2] = map.get("nickname").toString();
                items[i][3] = map.get("sex").toString();
                items[i][4] = map.get("telephone").toString();
                items[i][5] = map.get("birthday").toString();
                items[i][6] = map.get("orderCount").toString();
                items[i][7] = map.get("orderMoney").toString();
                items[i][8] = map.get("avgOrderMoney").toString();
                i++;
            }
            AppendToExcelUtil.insertRows(path,startPosition,items);
        } catch (Exception e){
            log.error("追加筛选的会员信息报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult();
    }
}
