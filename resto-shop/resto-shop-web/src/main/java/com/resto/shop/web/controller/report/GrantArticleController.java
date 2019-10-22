package com.resto.shop.web.controller.report;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.service.BrandService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.dto.GrantArticleInfoDto;
import com.resto.shop.web.dto.GrantInfoDto;
import com.resto.shop.web.dto.SvipActReportDto;
import com.resto.shop.web.dto.SvipActivityDto;
import com.resto.shop.web.service.OrderItemService;
import com.resto.shop.web.util.DateStr;
import com.resto.shop.web.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.AbstractDocument;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-12-06 13:53
 */

@Controller
@RequestMapping("grantArticleReport")
public class GrantArticleController extends GenericController {

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    BrandService brandService;

    @RequestMapping("/list")
    public String list(){
        return "grantrticlereport/list";
    }

    @RequestMapping("/getGrantArticleList")
    @ResponseBody
    public List<GrantArticleInfoDto> getGrantArticleList(String beginDate,String endDate,Integer status){

        switch (status){
            case 1 ://今日
                String[] today = DateStr.toDayStr();
                return orderItemService.getGrantArticleList(today[0],today[1] );
            case 2 ://昨日
                String[] yestoday = DateStr.yestodayStr();
                return orderItemService.getGrantArticleList(yestoday[0],yestoday[1] );
            case 3 ://本周
                String[] week = DateStr.getTimeInterval(new Date());
                return orderItemService.getGrantArticleList(week[0],week[1] );
            case 4 ://本月
                String[] month = DateStr.getBeginEndMonthDate(new Date());
                return orderItemService.getGrantArticleList(month[0],month[1] );
            default:
                break;
        }
        return orderItemService.getGrantArticleList(beginDate,endDate );
    }

    @RequestMapping("getInfo")
    @ResponseBody
    public GrantInfoDto getInfo(String itemId){
        return orderItemService.getGrantInfo(itemId);
    }

    @RequestMapping("exprotActExcel")
    @ResponseBody
    public Result exprotActExcel(@RequestParam("beginDate") String beginDate,
                                 @RequestParam("endDate") String endDate,
                                 HttpServletResponse response,
                                 HttpServletRequest request){
        List<GrantArticleInfoDto> grantArticleList = orderItemService.getGrantArticleList(beginDate, endDate);
        Brand brand = brandService.selectByPrimaryKey(getCurrentBrandId());

        Map<String, String> map = new HashMap<>();
        map.put("reportTitle", brand.getBrandName());// sheet的名字
        map.put("reportType", "赠菜报表");// 表的头，第一行内容
        map.put("brandName", brand.getBrandName());
        if(beginDate.equals(endDate)){
            map.put("date", beginDate);
        }else {
            map.put("endDate", endDate);
            map.put("beginDate", beginDate);
        }
        map.put("num", "16");// 显示的位置
        map.put("timeType", "yyyy-MM-dd");

        String[][] header2 = {
                {"店铺名称", "20"},
                {"订单编号", "23"},
                {"桌号", "23"},
                {"菜品类别", "23"},
                {"菜品名称", "23"},
                {"赠菜数量", "23"},
                {"赠菜金额", "23"},
                {"赠菜原因", "23"}
        };
        String[] column = {"shopName", "orderId", "tableNo","familyName","articleName","count","money","refundMark"};


        HSSFWorkbook workbook2 = new HSSFWorkbook();

        ExcelUtil<GrantArticleInfoDto> excelUtil2 = new ExcelUtil<>();

        String str2 = brand.getBrandName()+"赠菜报表.xls";

        try {
            excelUtil2.ExportExcel(str2,request,response,workbook2, header2, column, grantArticleList, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("下载失败",false);
        }
        return new Result("下载成功",true);
    }

}
