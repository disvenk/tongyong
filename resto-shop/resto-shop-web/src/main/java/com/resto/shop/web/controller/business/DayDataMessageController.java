package com.resto.shop.web.controller.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import javax.validation.Valid;

import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.ExcelUtil;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.MessageType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.DayDataMessage;
import com.resto.shop.web.service.DayDataMessageService;
import org.springframework.web.servlet.ModelAndView;

 @Controller
@RequestMapping("daydatamessage")
public class DayDataMessageController extends GenericController{

	@Resource
	DayDataMessageService daydatamessageService;


	@Resource
     ShopDetailService shopDetailService;

	@RequestMapping("/list")
    public ModelAndView list(){
	    //判断是否要显示旬 和 月
       int type= DateUtil.getEarlyMidLateEnd(new Date());
       ModelAndView mv = new ModelAndView();
        mv.setViewName("daydatamessage/list");
        mv.addObject("type",type);
       return mv;
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<DayDataMessage> listData(){
		return daydatamessageService.selectList();
	}

	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		DayDataMessage daydatamessage = daydatamessageService.selectById(id);
		return getSuccessResult(daydatamessage);
	}

	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid DayDataMessage daydatamessage){
		daydatamessageService.insert(daydatamessage);
		return Result.getSuccess();
	}

	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid DayDataMessage daydatamessage){
		daydatamessageService.update(daydatamessage);
		return Result.getSuccess();
	}

	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		daydatamessageService.delete(id);
		return Result.getSuccess();
	}

    @RequestMapping("getShopData")
    @ResponseBody
    public Result getShopData(String date,Integer type){
        List<DayDataMessage> dayDataMessageList =  getData(date,type);
        return getSuccessResult(dayDataMessageList);
    }

     //根据状态(正常/删除) 时间 类型(1,2,3日/旬/月)
     private   List<DayDataMessage> getData(String date, Integer type) {
         List<DayDataMessage> dayDataMessageList = daydatamessageService.selectListByTime(MessageType.NORMAL,date,type);
         List<ShopDetail> shopDetailList = shopDetailService.selectByBrandId(getCurrentBrandId());
         List<String> shopIds = new ArrayList<>();
         for(ShopDetail shopDetail:shopDetailList){
             shopIds.add(shopDetail.getId());
         }

         //结店时候 如果存在 当日（旬/月） 当前shopId 有多条数据的时候取最新的一条
         Map<String,DayDataMessage> map = new HashMap<>();

         if(dayDataMessageList!=null&&!dayDataMessageList.isEmpty()){

            for(DayDataMessage dayDataMessage:dayDataMessageList){

                if(shopIds.contains(dayDataMessage.getShopId())){
                    map.put(dayDataMessage.getShopId(),dayDataMessage);
                }
            }
         }
         Collection<DayDataMessage> list =  map.values();
         List<DayDataMessage> dayDataMessages = new ArrayList<>();

         if(list!=null&&!list.isEmpty()){
             for(DayDataMessage dm:list){
                 dayDataMessages.add(dm);
             }
         }


         if(dayDataMessages!=null&&!dayDataMessages.isEmpty()){
             for(DayDataMessage dm:dayDataMessages){
                 dm.setNewCustomerOrder(dm.getNewCuostomerOrderNum()+"笔/"+dm.getNewCustomerOrderSum()+"元");//新用户消费
                 dm.setNewNormalCustomerOrder(dm.getNewNormalCustomerOrderNum()+"笔/"+dm.getNewNormalCustomerOrderSum()+"元");//自然户消费
                 dm.setNewShareCutomerOrder(dm.getNewShareCustomerOrderNum()+"笔/"+dm.getNewShareCustomerOrderSum()+"元");//分享用户消费
                 dm.setBackCustomerOrder(dm.getBackCustomerOrderNum()+"笔/"+dm.getBackCustomerOrderSum()+"元");//回头用户消费
                 dm.setBackTwoCustomerOrder(dm.getBackCustomerOrderNum()+"笔/"+dm.getBackCustomerOrderSum()+"元");//二次户消费
                 dm.setBackTwoMoreCustomerOrder(dm.getBackTwoMoreCustomerOrderNum()+"笔/"+dm.getBackTwoMoreCustomerOrderSum()+"元");//多次回头用户消费
             }
         }

        return dayDataMessages;



     }


     //生成店铺日结短信报表
     @SuppressWarnings("unchecked")
     @RequestMapping("create_dayMessage")
     @ResponseBody
     public Result create_dayMessage(String date, Integer type, HttpServletRequest request){
         //导出文件名
         String fileName = "店铺营运表"+date+".xls";
         //定义读取文件的路径
         String path = request.getSession().getServletContext().getRealPath(fileName);
         //定义列
         String[]columns={"shopName","customerOrderRatio","backCustomerOrderRatio","newCustomerOrderRatio","newCustomerOrder","newNormalCustomerOrder","newShareCutomerOrder","backCustomerOrder","backTwoCustomerOrder","backTwoMoreCustomerOrder"};
         //定义数据
         List<DayDataMessage>  result = new ArrayList<>();
         String shopName="";
         result =getData(date,type);
         if(!result.isEmpty()){
             for(DayDataMessage d:result){
                 shopName+=d.getShopName();
             }
         }
         shopName = shopName.substring(0, shopName.length() - 1);
         Map<String,String> map = new HashMap<>();
         map.put("brandName", getBrandName());
         map.put("shops", shopName);
         map.put("endDate", date);
         map.put("beginDate",date);
         if(type==2){//说明是旬
             map.put("beginDate",DateUtil.formatDate(DateUtil.getAfterDayDate(DateUtil.fomatDate(date),-10),"yyyy-MM-dd"));
         }
        if(type==3){//说明是月
             map.put("beginDate",DateUtil.getMonthBegin());
        }
         map.put("reportType", "店铺营运表");//表的头，第一行内容
         map.put("num", "9");//显示的位置
         map.put("reportTitle", "营运分析报表");//表的名字
         map.put("timeType", "yyyy-MM-dd");

         String[][] headers = {{"店铺名称","25"},{"用户消费比率","25"},{"回头消费比率","25"},{"新增用户比率","25"},{"新用户消费","25"},{"自然用户消费","25"},{"分享用户消费","25"},{"回头用户消费","25"},{"二次回头用户消费","25"},{"多次回头用户消费","25"}};
         //定义excel工具类对象
         ExcelUtil<DayDataMessage> excelUtil=new ExcelUtil<>();
         try{
             OutputStream out = new FileOutputStream(path);
             excelUtil.ExportExcel(headers, columns, result, out, map);
             out.close();
         }catch(Exception e){
             log.error("生成店铺营运表出错！");
             e.printStackTrace();
             return new Result(false);
         }
         return getSuccessResult(path);
     }


     /**
      * 下载营运报表
      * @param path
      * @param response
      * @throws IOException
      */
     @RequestMapping("downLoadExcel")
     public void exportXls(String path,HttpServletResponse response) throws IOException {
         //定义excel工具类对象
         ExcelUtil<DayDataMessage> excelUtil=new ExcelUtil<>();
         try {
             excelUtil.download(path, response);
             JOptionPane.showMessageDialog(null, "导出成功！");
             log.info("excel导出成功");
         }catch (Exception e){
             log.error("下载店铺营运表报错！");
             e.printStackTrace();
         }
     }









 }
