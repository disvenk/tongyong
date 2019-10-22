package com.resto.shop.web.controller.business;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.ExcelUtil;
import com.resto.brand.web.dto.ShopDetailDto;
import com.resto.shop.web.constant.Common;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.dto.RechargeLogDto;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.ChargeOrder;
import com.resto.shop.web.service.ChargeOrderService;


@Controller
@RequestMapping("recharge")

public class RechargeLogController extends GenericController{

	@Resource
	BrandService brandService;

	@Resource
	ShopDetailService shopDetailService;


	@Resource
	ChargeOrderService chargeorderService;


	@RequestMapping("/list")
	public String  list(){
		return "recharge/list";
	}

	@RequestMapping("/shop/list")
	public String  shopList(){
		return "recharge/shopList";
	}

	/**
	 * name:yjuany
	 * @param shopDetailId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping("/shopRechargeLog")
	public String shopchargerecord(String shopDetailId,String beginDate,String endDate, String shopName){
		getRequest().setAttribute("shopDetailId",shopDetailId);
		getRequest().setAttribute("beginDate",beginDate);
		getRequest().setAttribute("endDate",endDate);
        getRequest().setAttribute("shopName",shopName);
		return "recharge/shopchargerecord";
	}

	/**
	 * 得到所有店铺名字
	 * @param
	 * @return
	 * name:yjuany
	 */
	public String  getShopList(String shopDetailId){
		List<ShopDetail> shoplist = getCurrentShopDetails();
		if(!shoplist.isEmpty()){
			shoplist = shopDetailService.selectByBrandId(getCurrentBrandId());
		}
		for ( ShopDetail shopd:shoplist) {
			  if(shopd.getId().equals(shopDetailId)){
			  	return  shopd.getName();
			  }
		}
		return  "----";
	}


	/**
	 * 店铺详细
	 * @param shopId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping("/queryShopchargecord")
    @ResponseBody
	public Result queryShopchargecord(String shopId, String beginDate, String endDate){
	    JSONObject object = new JSONObject();
	    try {
            String shopname = getShopList(shopId);
            Date begin = DateUtil.getformatBeginDate(beginDate);
            Date end = DateUtil.getformatEndDate(endDate);
            List<ChargeOrder> chargeList = chargeorderService.shopChargeCodes(shopId, begin, end);
            for (int i = 0; i < chargeList.size(); i++) {
                chargeList.get(i).getChargelog().setShopName(shopname);
            }
            object.put("result",chargeList);
        }catch (Exception e){
            return new Result(false);
        }
        return getSuccessResult(object);
	}

    /**
     * 店铺详细下载报表
     * @return
	 *name: yjuany
     */
    public Map<String,Object> getResultSetDto(String shopDetailId,String beginDate,String endDate){
		   String shopname=getShopList(shopDetailId);//店铺名字
           Map<String,Object>  mapshopDetailDto= chargeorderService.shopChargeCodesSetDto(shopDetailId,beginDate,endDate,shopname);

        return  mapshopDetailDto;
    }
    @RequestMapping("shopDetail_excel")
	@ResponseBody
   public void reportShopDetail(String shopDetailId,String beginDate, String endDate, HttpServletRequest request, HttpServletResponse response){
		String shopname=getShopList(shopDetailId);//店铺名字
        List<ShopDetailDto>  result = new LinkedList<>();
        Map<String,Object>  resultMap=this.getResultSetDto(shopDetailId,beginDate,endDate);
        result.addAll((Collection<? extends ShopDetailDto>) resultMap.get("shopDetailMap"));
        for (ShopDetailDto shopDetailDto : result){
            if (shopDetailDto.getType().equals(1)) {
                shopDetailDto.setTypeString("微信充值");
            }else{
                shopDetailDto.setTypeString("pos充值");
            }
        }
        //导出文件名
        String fileName = "店铺充值记录详细"+beginDate+"至"+endDate
                +".xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns=
                         {"shopname","typeString","customerPhone","chargeMoney",
						"rewardMoney","finishTime","operationPhone"};
                        //定义数据
      //获取店铺名称
        Map<String,String> map = new HashMap<>();
		map.put("brandName", getBrandName());
		map.put("shops",shopname);
		map.put("beginDate", beginDate);
		map.put("reportType", "充值报表");//表的头，第一行内容
		map.put("endDate", endDate);
		map.put("num", "6");//显示的位置
		map.put("reportTitle", "充值报表");//表的名字
		map.put("timeType", "yyyy-MM-dd");

		String[][] headers = {{"店铺名字","25"},{"充值方式","25"},{"充值手机","25"},{"充值金额(元)","25"}
        ,{"充值赠送金额（元）","25"} ,{"充值时间（元）","25"} ,{"操作人手机","25"}};
        //定义excel工具类对象
		ExcelUtil<ShopDetailDto> excelUtil=new ExcelUtil<ShopDetailDto>();
		try{
			OutputStream out = new FileOutputStream(path);
			excelUtil.ExportExcel(headers, columns, result, out, map);
			out.close();
			excelUtil.download(path, response);
			JOptionPane.showMessageDialog(null, "导出成功！");
			log.info("excel导出成功");
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	@RequestMapping("/rechargeLog")
	@ResponseBody
	public Result selectBrandOrShopRecharge(String beginDate,String endDate){
        Result result = new Result();
		try {
            result = this.getResult(beginDate, endDate);
            result.setSuccess(true);
        }catch (Exception e){
            log.error("查询充值报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return result;
	}

	private Result getResult(String beginDate, String endDate) {
		return getSuccessResult(this.RechargeList(beginDate, endDate));
	}
	
	public Map<String, Object> RechargeList(String beginDate,String endDate){
		BigDecimal initZero=BigDecimal.ZERO;
		//初始化品牌充值记录
		RechargeLogDto brandInit = new RechargeLogDto(getBrandName(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		RechargeLogDto rechargeLogDto=chargeorderService.selectRechargeLog(beginDate, endDate,getCurrentBrandId());
		if(rechargeLogDto!=null){
			brandInit.setBrandName(getBrandName());
			//判断为空则为0
			if(rechargeLogDto.getRechargeCount() == null){
				brandInit.setRechargeCount(initZero);
			}else{
				brandInit.setRechargeCount(rechargeLogDto.getRechargeCount());
			}
			if(rechargeLogDto.getRechargeCsNum() == null){
				brandInit.setRechargeCsNum(initZero);
			}else{
				brandInit.setRechargeCsNum(rechargeLogDto.getRechargeCsNum());
			}
			if(rechargeLogDto.getRechargeGaCsNum() == null){
				brandInit.setRechargeGaCsNum(initZero);
			}else{
				brandInit.setRechargeGaCsNum(rechargeLogDto.getRechargeGaCsNum());
			}
			if(rechargeLogDto.getRechargeGaNum() == null){
				brandInit.setRechargeGaNum(initZero);
			}else{
				brandInit.setRechargeGaNum(rechargeLogDto.getRechargeGaNum());
			}
			if(rechargeLogDto.getRechargeGaSpNum() == null){
				brandInit.setRechargeGaSpNum(initZero);
			}else{
				brandInit.setRechargeGaSpNum(rechargeLogDto.getRechargeGaSpNum());
			}
			if(rechargeLogDto.getRechargeNum() == null){
				brandInit.setRechargeNum(initZero);
			}else{
				brandInit.setRechargeNum(rechargeLogDto.getRechargeNum());
			}
			if(rechargeLogDto.getRechargePos() == null){
				brandInit.setRechargePos(initZero);
			}else{
				brandInit.setRechargePos(rechargeLogDto.getRechargePos());
			}
			if(rechargeLogDto.getRechargeSpNum() == null){
				brandInit.setRechargeSpNum(initZero);
			}else{
				brandInit.setRechargeSpNum(rechargeLogDto.getRechargeSpNum());
			}
			if(rechargeLogDto.getRechargeWeChat() == null){
				brandInit.setRechargeWeChat(initZero);
			}else{
				brandInit.setRechargeWeChat(rechargeLogDto.getRechargeWeChat());
			}
		}
		
		List<RechargeLogDto> shopRrchargeLogs=new ArrayList<>();
		
		List<ShopDetail> shoplist = getCurrentShopDetails();
        if(shoplist.isEmpty()){
            shoplist = shopDetailService.selectByBrandId(getCurrentBrandId());
        }
        for (ShopDetail shopDetail : shoplist) {
        	//初始化店铺充值记录
        	RechargeLogDto shopRechargeLogDto=chargeorderService.selectShopRechargeLog(beginDate, endDate, shopDetail.getId());
        	if(shopRechargeLogDto == null){
        		shopRechargeLogDto=new RechargeLogDto(shopDetail.getId(),shopDetail.getName(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        	}
        		shopRechargeLogDto.setShopId(shopDetail.getId());
        		shopRechargeLogDto.setShopName(shopDetail.getName());
	        	//判断为空则为0
	        	if(shopRechargeLogDto.getShopCount()==null){
	        		shopRechargeLogDto.setShopCount(initZero);
	        	}else{
	        		shopRechargeLogDto.setShopCount(shopRechargeLogDto.getShopCount());
	        	}
	        	if(shopRechargeLogDto.getShopGaNum()==null){
	        		shopRechargeLogDto.setShopNum(initZero);
	        	}else{
	        		shopRechargeLogDto.setShopNum(shopRechargeLogDto.getShopNum());
	        	}
	        	if(shopRechargeLogDto.getShopGaNum() == null){
	        		shopRechargeLogDto.setShopGaNum(initZero);
	        	}else{
	        		shopRechargeLogDto.setShopGaNum(shopRechargeLogDto.getShopGaNum());
	        	}
	        	if(shopRechargeLogDto.getShopWeChat() == null){
	        		shopRechargeLogDto.setShopWeChat(initZero);
	        	}else{
	        		shopRechargeLogDto.setShopWeChat(shopRechargeLogDto.getShopWeChat());
	        	}
	        	if(shopRechargeLogDto.getShopPos() == null){
	        		shopRechargeLogDto.setShopPos(initZero);
	        	}else{
	        		shopRechargeLogDto.setShopPos(shopRechargeLogDto.getShopPos());
	        	}
	        	if(shopRechargeLogDto.getShopCsNum() == null){
	        		shopRechargeLogDto.setShopCsNum(initZero);
	        	}else{
	        		shopRechargeLogDto.setShopCsNum(shopRechargeLogDto.getShopCsNum());
	        	}
	        	if(shopRechargeLogDto.getShopGaCsNum() == null){
	        		shopRechargeLogDto.setShopGaCsNum(initZero);
		    	}else{
		    		shopRechargeLogDto.setShopGaCsNum(shopRechargeLogDto.getShopGaCsNum());
	        	}
	        	shopRrchargeLogs.add(shopRechargeLogDto);
        }
        
		Map<String, Object> map=new HashMap<>();
		map.put("brandInit", brandInit);
		map.put("shopRrchargeLogs", shopRrchargeLogs);
		
		return map;
	}

	
    	//下载充值报表
        @SuppressWarnings("unchecked")
    	@RequestMapping("brandOrShop_excel")
    	@ResponseBody
    	public Result reportIncome(String beginDate,String endDate,RechargeLogDto rechargeLogDto,HttpServletRequest request){
        	List<ShopDetail> shopDetailList = getCurrentShopDetails();
            if(shopDetailList==null){
                shopDetailList = shopDetailService.selectByBrandId(getCurrentBrandId());
            }
            List<RechargeLogDto> result = new ArrayList<>();
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
			filter.getExcludes().add("brandChargeLogs");
			filter.getExcludes().add("shopChargeLogs");
            if (rechargeLogDto.getBrandChargeLogs() != null){
                Map<String, Object> brandMap = rechargeLogDto.getBrandChargeLogs();
                RechargeLogDto brandLogDto = new RechargeLogDto();
                brandLogDto.setShopName(brandMap.get("brandName").toString());
                brandLogDto.setShopCount(new BigDecimal(brandMap.get("rechargeCount").toString()));
                brandLogDto.setShopNum(new BigDecimal(brandMap.get("rechargeNum").toString()));
                brandLogDto.setShopGaNum(new BigDecimal(brandMap.get("rechargeGaNum").toString()));
                brandLogDto.setShopWeChat(new BigDecimal(brandMap.get("rechargeWeChat").toString()));
                brandLogDto.setShopPos(new BigDecimal(brandMap.get("rechargePos").toString()));
                brandLogDto.setShopCsNum(new BigDecimal(brandMap.get("rechargeCsNum").toString()));
                brandLogDto.setShopGaCsNum(new BigDecimal(brandMap.get("rechargeGaCsNum").toString()));
                brandLogDto.setRechargeSpNum(new BigDecimal(brandMap.get("rechargeSpNum").toString()));
                brandLogDto.setRechargeGaSpNum(new BigDecimal(brandMap.get("rechargeGaSpNum").toString()));
                result.add(brandLogDto);
            }
			String json = JSON.toJSONString(rechargeLogDto.getShopChargeLogs(),filter);
			List<RechargeLogDto> shopLogs = JSON.parseObject(json,new TypeReference<List<RechargeLogDto>>(){});
			result.addAll(shopLogs);
    		//导出文件名
    		String fileName = "充值报表"+beginDate+"至"+endDate+".xls";
    		//定义读取文件的路径
    		String path = request.getSession().getServletContext().getRealPath(fileName);
    		//定义列
    		String[]columns={"shopName","shopCount","shopNum","shopGaNum","shopWeChat","shopPos","shopCsNum","shopGaCsNum","rechargeSpNum","rechargeGaSpNum"};
    		//定义数据
    		String shopName="";
    		for (ShopDetail shopDetail : shopDetailList) {
    			shopName += shopDetail.getName()+",";
    		}
            shopName = shopName.substring(0,shopName.length()-1);
    		Map<String,String> map = new HashMap<>();
    		map.put("brandName", getBrandName());
    		map.put("shops", shopName);
    		map.put("beginDate", beginDate);
    		map.put("reportType", "充值报表");//表的头，第一行内容
    		map.put("endDate", endDate);
    		map.put("num", "9");//显示的位置
    		map.put("reportTitle", "充值报表");//表的名字
    		map.put("timeType", "yyyy-MM-dd");

    		String[][] headers = {{"店铺名","25"},{"充值单数","25"},{"店铺充值总额","25"},{"店铺充值赠送总额","25"},{"店铺微信充值总额","25"},{"店铺POS端充值总额","25"},{"店铺充值消费","25"},{"店铺充值赠送消费","25"}
                    ,{"充值剩余总额","25"},{"充值赠送剩余总额","25"}};
    		//定义excel工具类对象
    		ExcelUtil<RechargeLogDto> excelUtil=new ExcelUtil<RechargeLogDto>();
    		try{
    			OutputStream out = new FileOutputStream(path);
    			excelUtil.ExportExcel(headers, columns, result, out, map);
    			out.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		return getSuccessResult(path);
    	}

    @RequestMapping("/download_brand_excel")
    public void downloadBrandExcel(String path, HttpServletResponse response){
        ExcelUtil<Object> excelUtil = new ExcelUtil<>();
        try{
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/createMonthDto")
    @ResponseBody
    public Result createMonthDto(String year, String month, Integer type, HttpServletRequest request){
        Integer monthDay = getMonthDay(year, month);
        // 导出文件名
        String typeName = type.equals(Common.YES) ? "店铺充值记录月报表" : "品牌充值记录月报表" ;
        String str = typeName + year.concat("-").concat(month).concat("-01") + "至"
                + year.concat("-").concat(month).concat("-").concat(String.valueOf(monthDay)) + ".xls";
        String path = request.getSession().getServletContext().getRealPath(str);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            List<ShopDetail> shopDetails = getCurrentShopDetails();
            String[] shopNames = new String[1];
            RechargeLogDto[][] rechargeLogDtos = new RechargeLogDto[1][monthDay];
            Map<String, Object> selectMap = new HashMap<>();
            selectMap.put("beginDate",year.concat("-").concat(month).concat("-01"));
            selectMap.put("endDate",year.concat("-").concat(month).concat("-").concat(String.valueOf(monthDay)));
            if (type.equals(Common.YES)){
                shopNames = new String[shopDetails.size()];
                rechargeLogDtos = new RechargeLogDto[shopDetails.size()][monthDay];
                int i = 0;
                int j = 0;
                for (ShopDetail shopDetail : shopDetails){
                    selectMap.put("shopId",shopDetail.getId());
                    List<ChargeOrder> chargeOrders = chargeorderService.selectMonthDto(selectMap);
                    for (int day = 0; day < monthDay; day++){
                        Date beginDate = getBeginDay(year, month, day);
                        Date endDate = getEndDay(year, month, day);
                        RechargeLogDto rechargeLogDto = new RechargeLogDto(format.format(beginDate), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                        for (ChargeOrder chargeOrder : chargeOrders){
                            if (endDate.getTime() >= chargeOrder.getFinishTime().getTime() && beginDate.getTime() <= chargeOrder.getFinishTime().getTime()){
                                rechargeLogDto.setShopCount(rechargeLogDto.getShopCount().add(new BigDecimal(1)));
                                rechargeLogDto.setShopNum(rechargeLogDto.getShopNum().add(chargeOrder.getChargeMoney()));
                                rechargeLogDto.setShopGaNum(rechargeLogDto.getShopGaNum().add(chargeOrder.getRewardMoney()));
                                if (chargeOrder.getType() == null) {
                                    rechargeLogDto.setShopWeChat(rechargeLogDto.getShopWeChat().add(chargeOrder.getChargeMoney()));
                                }else{
                                    if (chargeOrder.getType().equals(Common.YES)) {
                                        rechargeLogDto.setShopWeChat(rechargeLogDto.getShopWeChat().add(chargeOrder.getChargeMoney()));
                                    } else {
                                        rechargeLogDto.setShopPos(rechargeLogDto.getShopPos().add(chargeOrder.getChargeMoney()));
                                    }
                                }
                                rechargeLogDto.setShopCsNum(rechargeLogDto.getShopCsNum().add(chargeOrder.getChargeMoney().subtract(chargeOrder.getChargeBalance())));
                                rechargeLogDto.setShopGaCsNum(rechargeLogDto.getShopGaCsNum().add(chargeOrder.getRewardMoney().subtract(chargeOrder.getRewardBalance())));
                            }
                        }
                        rechargeLogDtos[i][j] = rechargeLogDto;
                        j++;
                    }
                    shopNames[i] = shopDetail.getName();
                    i++;
                    j = 0;
                }
            } else{
                String shopName = getCurrentShopDetails().stream().map(ShopDetail::getName).collect(Collectors.joining(","));;
                shopNames[0] = shopName;
                List<ChargeOrder> chargeOrders = chargeorderService.selectMonthDto(selectMap);
                int j = 0;
                for (int day = 0; day < monthDay; day++){
                    Date beginDate = getBeginDay(year, month, day);
                    Date endDate = getEndDay(year, month, day);
                    RechargeLogDto rechargeLogDto = new RechargeLogDto(format.format(beginDate), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                    for (ChargeOrder chargeOrder : chargeOrders){
                        if (endDate.getTime() >= chargeOrder.getFinishTime().getTime() && beginDate.getTime() <= chargeOrder.getFinishTime().getTime()){
                            rechargeLogDto.setShopCount(rechargeLogDto.getShopCount().add(new BigDecimal(1)));
                            rechargeLogDto.setShopNum(rechargeLogDto.getShopNum().add(chargeOrder.getChargeMoney()));
                            rechargeLogDto.setShopGaNum(rechargeLogDto.getShopGaNum().add(chargeOrder.getRewardMoney()));
                            if (chargeOrder.getType().equals(Common.YES)){
                                rechargeLogDto.setShopWeChat(rechargeLogDto.getShopWeChat().add(chargeOrder.getChargeMoney()));
                            } else{
                                rechargeLogDto.setShopPos(rechargeLogDto.getShopPos().add(chargeOrder.getChargeMoney()));
                            }
                            rechargeLogDto.setShopCsNum(rechargeLogDto.getShopCsNum().add(chargeOrder.getChargeMoney().subtract(chargeOrder.getChargeBalance())));
                            rechargeLogDto.setShopGaCsNum(rechargeLogDto.getShopGaCsNum().add(chargeOrder.getRewardMoney().subtract(chargeOrder.getRewardBalance())));
                        }
                    }
                    rechargeLogDtos[0][j] = rechargeLogDto;
                    j++;
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("brandName", getBrandName());
            map.put("beginDate", year.concat("-").concat(month).concat("-01"));
            map.put("reportType", typeName);// 表的头，第一行内容
            map.put("endDate", year.concat("-").concat(month).concat("-").concat(String.valueOf(monthDay)));
            map.put("num", "7");// 显示的位置
            map.put("timeType", "yyyy-MM-dd");
            map.put("reportTitle", shopNames);// 表的名字
            String[][] headers = {{"日期","25"},{"充值单数","25"},{"店铺充值总额","25"},{"店铺充值赠送总额","25"},{"店铺微信充值总额","25"},{"店铺POS端充值总额","25"},{"店铺充值消费","25"},{"店铺充值赠送消费","25"}};
            String[] columns = {"date","shopCount","shopNum","shopGaNum","shopWeChat","shopPos","shopCsNum","shopGaCsNum"};
            ExcelUtil<RechargeLogDto> excelUtil = new ExcelUtil<>();
            OutputStream out = new FileOutputStream(path);
            excelUtil.createMonthDtoExcel(headers, columns, rechargeLogDtos, out, map);
            out.close();
        } catch (Exception e){
            e.printStackTrace();
            log.error("生成充值月报表出错!");
            new Result(false);
        }
        return getSuccessResult(path);
    }


    public Integer getMonthDay(String year, String month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public Date getBeginDay(String year, String month, Integer day){
        Calendar beginDate = Calendar.getInstance();
        beginDate.set(Calendar.YEAR, Integer.parseInt(year));
        beginDate.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        beginDate.set(Calendar.DATE, day + 1);
        beginDate.set(Calendar.HOUR_OF_DAY, 0);
        beginDate.set(Calendar.MINUTE, 0);
        beginDate.set(Calendar.SECOND,1);
        return beginDate.getTime();
    }

    public Date getEndDay(String year, String month, Integer day){
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.YEAR, Integer.parseInt(year));
        endDate.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        endDate.set(Calendar.DATE, day + 1);
        endDate.set(Calendar.HOUR_OF_DAY, 23);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND,59);
        return endDate.getTime();
    }

}