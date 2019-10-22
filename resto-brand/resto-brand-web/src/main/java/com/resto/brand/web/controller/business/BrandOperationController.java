package com.resto.brand.web.controller.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.Encrypter;
import com.resto.brand.core.util.ExcelUtil;
import com.resto.brand.core.util.JdbcUtils;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.dto.BrandOperationDto;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/brandOperation")
public class BrandOperationController extends GenericController {

    @Resource
    BrandService brandService;

    @RequestMapping("/list")
    public void list(){}

    /**
     * 查询品牌运营数据
     * @param beginDate
     * @param endDate
     * @return
     */
    @RequestMapping("/selectBrandOperationData")
    @ResponseBody
    public Result selectBrandOperationData(String beginDate, String endDate){
        JSONObject object = new JSONObject();
        try {
            List<Brand> brandDetails = brandService.selectBrandDetailInfo();
            List<Map<String, Object>> brandOperationDtos = new ArrayList<>();
            List<Map<String, Object>> shopOperationDtos = new ArrayList<>();
            Map<String, Object> brandOperationCount = new HashMap<>();
            brandOperationCount.put("brandCount", brandDetails.size());
            BigDecimal shopCount = new BigDecimal(0);
            BigDecimal orderCount = new BigDecimal(0);
            BigDecimal orderMoney = new BigDecimal(0);
            BigDecimal weChatPay = new BigDecimal(0);
            BigDecimal aliPay = new BigDecimal(0);
            BigDecimal customerCount = new BigDecimal(0);
            BigDecimal newCustomerCount = new BigDecimal(0);
            BigDecimal newRegiestCustomerCount = new BigDecimal(0);
            BigDecimal chargeCount = new BigDecimal(0);
            BigDecimal chargeMoney = new BigDecimal(0);
            for (Brand brand : brandDetails) {
                shopCount = shopCount.add(new BigDecimal(brand.getShopDetail() != null ? brand.getShopDetail().size() :
                        0));
                String userName = Encrypter.decrypt(brand.getDatabaseConfig().getUsername());
                String password = Encrypter.decrypt(brand.getDatabaseConfig().getPassword());
                String driver = brand.getDatabaseConfig().getDriverClassName();
                String url = brand.getDatabaseConfig().getUrl();
                JdbcUtils jdbcUtils = new JdbcUtils(userName, password, driver, url);
                jdbcUtils.getConnection();
                //品牌订单数据Sql
                String brandOrderSql = "select SUM(r.orderCount) orderCount, SUM(r.orderMoney)orderMoney, " +
                        "SUM(r.weChatPay) weChatPay, SUM(r.aliPay) aliPay, SUM(r.customerCount) customerCount " +
                        "from " +
                        "(select IF(td.parent_order_id is null,1,0) orderCount,td.order_money orderMoney, " +
                        "SUM(IF(top.payment_mode_id = 1,top.pay_value,0)) weChatPay, " +
                        "SUM(IF(top.payment_mode_id = 10,top.pay_value,0)) aliPay, " +
                        "IFNULL(IF(td.parent_order_id is null,td.customer_count,0),0) customerCount " +
                        "from tb_order td " +
                        "left join tb_order_payment_item top " +
                        "on td.id = top.order_id " +
                        "where td.order_state in (2,10,11,12) " +
                        "and td.production_status in (2,3) " +
                        "and td.create_time >= '" + beginDate + " 00:00:00' " +
                        "and td.create_time <= '" + endDate + " 23:59:59' " +
                        "GROUP BY td.id) r";
                //品牌新用户数据Sql
                String brandNewCustomerSql = "select COUNT(1) newCustomerCount from tb_customer " +
                        "where regiest_time >= '" + beginDate + " 00:00:00' " +
                        "and regiest_time <= '" + endDate + " 23:59:59'";
                //品牌注册用户数据Sql
                String brandNewRegiestCustomerSql = "select COUNT(1) newRegiestCustomerCount from tb_customer " +
                        "where bind_phone_time >= '" + beginDate + " 00:00:00' " +
                        "and bind_phone_time <= '" + endDate + " 23:59:59' " +
                        "and is_bind_phone = 1";
                //品牌充值数据Sql
                String brandChargeSql = "select COUNT(1) chargeCount,SUM(charge_money) chargeMoney from tb_charge_order " +
                        "where order_state = 1 and finish_time >= '" + beginDate + " 00:00:00' " +
                        "and finish_time <= '" + endDate + " 23:59:59'";
                //店铺订单Sql
                String shopOrderSql = "select r.shop_detail_id shopId, SUM(r.orderCount) orderCount, SUM(r.orderMoney)orderMoney, " +
                        "SUM(r.weChatPay) weChatPay, SUM(r.aliPay) aliPay, SUM(r.customerCount) customerCount " +
                        "from " +
                        "(select td.shop_detail_id, " +
                        "IF(td.parent_order_id is null,1,0) orderCount,td.order_money orderMoney, " +
                        "SUM(IF(top.payment_mode_id = 1,top.pay_value,0)) weChatPay, " +
                        "SUM(IF(top.payment_mode_id = 10,top.pay_value,0)) aliPay, " +
                        "IFNULL(IF(td.parent_order_id is null,td.customer_count,0),0) customerCount " +
                        "from tb_order td " +
                        "left join tb_order_payment_item top " +
                        "on td.id = top.order_id " +
                        "where td.order_state in (2,10,11,12) " +
                        "and td.production_status in (2,3) " +
                        "and td.create_time >= '" + beginDate + " 00:00:00' " +
                        "and td.create_time <= '" + endDate + " 23:59:59' " +
                        "GROUP BY td.id) r GROUP BY r.shop_detail_id";
                //店铺新用户数据Sql
                String shopNewCustomerSql = "select register_shop_id shopId,COUNT(1) newCustomerCount from tb_customer " +
                        "where create_time >= '" + beginDate + " 00:00:00' " +
                        "and create_time <= '" + endDate + " 23:59:59' GROUP BY register_shop_id";
                //店铺注册用户数据Sql
                String shopNewRegiestCustomerSql = "select bind_phone_shop shopId,COUNT(1) newRegiestCustomerCount from tb_customer " +
                        "where bind_phone_time >= '" + beginDate + " 00:00:00' " +
                        "and bind_phone_time <= '" + endDate + " 23:59:59' " +
                        "and is_bind_phone = 1 GROUP BY bind_phone_shop";
                //店铺充值数据Sql
                String shopChargeSql = "select shop_detail_id shopId,COUNT(1) chargeCount,SUM(charge_money) chargeMoney from tb_charge_order " +
                        "where order_state = 1 and finish_time >= '" + beginDate + " 00:00:00' " +
                        "and finish_time <= '" + endDate + " 23:59:59' GROUP BY shop_detail_id";
                //获取品牌运营表数据
                Map<String, Object> resultBrandMap = new HashMap<>();
                Map<String, Object> resultBrandOrder = jdbcUtils.findSimpleResultWithoutClose(brandOrderSql, null);
                Map<String, Object> resultBrandNewCustomer = jdbcUtils.findSimpleResultWithoutClose(brandNewCustomerSql, null);
                Map<String, Object> resultBrandNewRegiestCustomer = jdbcUtils.findSimpleResultWithoutClose(brandNewRegiestCustomerSql, null);
                Map<String, Object> resultBrandCharge = jdbcUtils.findSimpleResultWithoutClose(brandChargeSql, null);
                resultBrandMap.put("brandName",brand.getBrandName());
                resultBrandMap.put("orderCount",resultBrandOrder.get("orderCount") != "" ? resultBrandOrder.get("orderCount") : 0);
                resultBrandMap.put("orderMoney",resultBrandOrder.get("orderMoney") != "" ? resultBrandOrder.get("orderMoney") : 0);
                resultBrandMap.put("weChatPay",resultBrandOrder.get("weChatPay") != "" ? resultBrandOrder.get("weChatPay") : 0);
                resultBrandMap.put("aliPay",resultBrandOrder.get("aliPay") != "" ? resultBrandOrder.get("aliPay") : 0);
                resultBrandMap.put("customerCount",resultBrandOrder.get("customerCount") != "" ? resultBrandOrder.get("customerCount") : 0);
                resultBrandMap.put("newCustomerCount",resultBrandNewCustomer.get("newCustomerCount") != "" ? resultBrandNewCustomer.get("newCustomerCount") : 0);
                resultBrandMap.put("newRegiestCustomerCount",resultBrandNewRegiestCustomer.get("newRegiestCustomerCount") != "" ? resultBrandNewRegiestCustomer.get("newRegiestCustomerCount") : 0);
                resultBrandMap.put("chargeCount",resultBrandCharge.get("chargeCount") != "" ? resultBrandCharge.get("chargeCount") : 0);
                resultBrandMap.put("chargeMoney",resultBrandCharge.get("chargeMoney") != "" ? resultBrandCharge.get("chargeMoney") : 0);
                brandOperationDtos.add(resultBrandMap);
                orderCount = orderCount.add(new BigDecimal(resultBrandMap.get("orderCount").toString()));
                orderMoney = orderMoney.add(new BigDecimal(resultBrandMap.get("orderMoney").toString()));
                weChatPay = weChatPay.add(new BigDecimal(resultBrandMap.get("weChatPay").toString()));
                aliPay = aliPay.add(new BigDecimal(resultBrandMap.get("aliPay").toString()));
                customerCount = customerCount.add(new BigDecimal(resultBrandMap.get("customerCount").toString()));
                newCustomerCount = newCustomerCount.add(new BigDecimal(resultBrandMap.get("newCustomerCount").toString()));
                newRegiestCustomerCount = newRegiestCustomerCount.add(new BigDecimal(resultBrandMap.get("newRegiestCustomerCount").toString()));
                chargeCount = chargeCount.add(new BigDecimal(resultBrandMap.get("chargeCount").toString()));
                chargeMoney = chargeMoney.add(new BigDecimal(resultBrandMap.get("chargeMoney").toString()));
                //获取店铺运营表数据
                List<Map<String, Object>> resultShopOrders = jdbcUtils.findModeResult(shopOrderSql, null);
                List<Map<String, Object>> resultShopNewCustomers = jdbcUtils.findModeResult(shopNewCustomerSql, null);
                List<Map<String, Object>> resultShopNewRegiestCustomers = jdbcUtils.findModeResult(shopNewRegiestCustomerSql, null);
                List<Map<String, Object>> resultShopCharges = jdbcUtils.findModeResult(shopChargeSql, null);
                //查询完关闭数据库连接
                JdbcUtils.close();
                if (brand.getShopDetail() != null) {
                    for (ShopDetail shopDetail : brand.getShopDetail()) {
                        Map<String, Object> resultShopMap = new HashMap<>();
                        resultShopMap.put("brandName", brand.getBrandName());
                        resultShopMap.put("shopName", shopDetail.getName());
                        BigDecimal shopOrderCount = new BigDecimal(0);
                        BigDecimal shopOrderMoney = new BigDecimal(0);
                        BigDecimal shopWeChatPay = new BigDecimal(0);
                        BigDecimal shopAliPay = new BigDecimal(0);
                        BigDecimal shopCustomerCount = new BigDecimal(0);
                        BigDecimal shopNewCustomerCount = new BigDecimal(0);
                        BigDecimal shopNewRegiestCustomerCount = new BigDecimal(0);
                        BigDecimal shopChargeCount = new BigDecimal(0);
                        BigDecimal shopChargeMoney = new BigDecimal(0);
                        for (Map orderMap : resultShopOrders) {
                            if (orderMap.get("shopId").equals(shopDetail.getId())) {
                                shopOrderCount = new BigDecimal(orderMap.get("orderCount").toString());
                                shopOrderMoney = new BigDecimal(orderMap.get("orderMoney").toString());
                                shopWeChatPay = new BigDecimal(orderMap.get("weChatPay").toString());
                                shopAliPay = new BigDecimal(orderMap.get("aliPay").toString());
                                shopCustomerCount = new BigDecimal(orderMap.get("customerCount").toString());
                                break;
                            }
                        }
                        for (Map newCustomerMap : resultShopNewCustomers) {
                            if (newCustomerMap.get("shopId").equals(shopDetail.getId())) {
                                shopNewCustomerCount = new BigDecimal(newCustomerMap.get("newCustomerCount").toString());
                                break;
                            }
                        }
                        for (Map newRegiestCustomerMap : resultShopNewRegiestCustomers) {
                            if (newRegiestCustomerMap.get("shopId").equals(shopDetail.getId())) {
                                shopNewRegiestCustomerCount = new BigDecimal(newRegiestCustomerMap.get("newRegiestCustomerCount").toString());
                                break;
                            }
                        }
                        for (Map chargeMap : resultShopCharges) {
                            if (chargeMap.get("shopId").equals(shopDetail.getId())) {
                                shopChargeCount = new BigDecimal(chargeMap.get("chargeCount").toString());
                                shopChargeMoney = new BigDecimal(chargeMap.get("chargeMoney").toString());
                                break;
                            }
                        }
                        resultShopMap.put("orderCount", shopOrderCount);
                        resultShopMap.put("orderMoney", shopOrderMoney);
                        resultShopMap.put("weChatPay", shopWeChatPay);
                        resultShopMap.put("aliPay", shopAliPay);
                        resultShopMap.put("customerCount", shopCustomerCount);
                        resultShopMap.put("newCustomerCount", shopNewCustomerCount);
                        resultShopMap.put("newRegiestCustomerCount", shopNewRegiestCustomerCount);
                        resultShopMap.put("chargeCount", shopChargeCount);
                        resultShopMap.put("chargeMoney", shopChargeMoney);
                        shopOperationDtos.add(resultShopMap);
                    }
                }
            }
            brandOperationCount.put("shopCount", shopCount);
            brandOperationCount.put("orderCount", orderCount);
            brandOperationCount.put("orderMoney", orderMoney);
            brandOperationCount.put("weChatPay", weChatPay);
            brandOperationCount.put("aliPay", aliPay);
            brandOperationCount.put("customerCount", customerCount);
            brandOperationCount.put("newCustomerCount", newCustomerCount);
            brandOperationCount.put("newRegiestCustomerCount", newRegiestCustomerCount);
            brandOperationCount.put("chargeCount", chargeCount);
            brandOperationCount.put("chargeMoney", chargeMoney);
            object.put("brandOperationCount",brandOperationCount);
            object.put("brandOperationDtos",brandOperationDtos);
            object.put("shopOperationDtos",shopOperationDtos);
        }catch (Exception e){
            log.error(e.getMessage()+"查询品牌运营数据出错！");
            e.printStackTrace();
            return new Result("查询失败，后台报错了！",false);
        }
        return getSuccessResult(object);
    }

    /**
     * 创建品牌运营表
     * @param beginDate
     * @param endDate
     * @param operationDto
     * @param request
     * @return
     */
    @RequestMapping("/createBrandOperationDto")
    @ResponseBody
    private Result createBrandOperationDto(String beginDate, String endDate, BrandOperationDto operationDto, HttpServletRequest request){
        //导出文件名
        String fileName = "品牌运营报表（品牌运营表）"+beginDate+"至"+endDate+".xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns={"brandName","shopName","orderCount","orderMoney","weChatPay","aliPay","customerCount","newCustomerCount","newRegiestCustomerCount",
                "chargeCount","chargeMoney"};
        //定义数据
        List<BrandOperationDto> result = new ArrayList<>();
        if (operationDto.getBrandOperationCount() != null) {
            BrandOperationDto brandOperationDto = new BrandOperationDto();
            Map<String, Object> brandOperationCount = operationDto.getBrandOperationCount();
            brandOperationDto.setBrandName(brandOperationCount.get("brandCount").toString());
            brandOperationDto.setShopName(brandOperationCount.get("shopCount").toString());
            brandOperationDto.setOrderCount(Integer.valueOf(brandOperationCount.get("orderCount").toString()));
            brandOperationDto.setOrderMoney(new BigDecimal(brandOperationCount.get("orderMoney").toString()));
            brandOperationDto.setWeChatPay(new BigDecimal(brandOperationCount.get("weChatPay").toString()));
            brandOperationDto.setAliPay(new BigDecimal(brandOperationCount.get("aliPay").toString()));
            brandOperationDto.setCustomerCount((Integer.valueOf(brandOperationCount.get("customerCount").toString())));
            brandOperationDto.setNewCustomerCount(Integer.valueOf(brandOperationCount.get("newCustomerCount").toString()));
            brandOperationDto.setNewRegiestCustomerCount(Integer.valueOf(brandOperationCount.get("newRegiestCustomerCount").toString()));
            brandOperationDto.setChargeCount(Integer.valueOf(brandOperationCount.get("chargeCount").toString()));
            brandOperationDto.setChargeMoney(new BigDecimal(brandOperationCount.get("chargeMoney").toString()));
            result.add(brandOperationDto);
        }
        if (operationDto.getBrandOperationDtos() != null) {
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("brandOperationCount");
            filter.getExcludes().add("brandOperationDtos");
            filter.getExcludes().add("shopOperationDtos");
            String json = JSON.toJSONString(operationDto.getBrandOperationDtos(),filter);
            List<BrandOperationDto> brandOperationDtos = JSON.parseObject(json, new TypeReference<List<BrandOperationDto>>(){});
            result.addAll(brandOperationDtos);
        }
        Map<String,String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("reportType", "品牌运营报表（品牌运营表）");//表的头，第一行内容
        map.put("num", "10");//显示的位置
        map.put("reportTitle", "品牌运营报表（品牌运营表）");//表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] headers = {{"品牌名称/品牌数","35"},{"店铺名称/店铺数","35"},{"订单总数","35"},{"订单总额","35"},{"微信支付","35"},{"支付宝支付","35"},{"就餐人数","35"},{"新增用户","35"},
                {"新注册用户","35"},{"充值次数","35"},{"充值金额","35"}};
        //定义excel工具类对象
        ExcelUtil<BrandOperationDto> excelUtil=new ExcelUtil<>();
        try{
            OutputStream out = new FileOutputStream(path);
            excelUtil.ExportBrandDtoExcel(headers, columns, result, out, map);
            out.close();
        }catch(Exception e){
            e.printStackTrace();
            return new Result("创建execl失败",false);
        }
        return getSuccessResult(path);
    }

    /**
     * 下载品牌运营表
     * @param path
     * @param response
     */
    @RequestMapping("/downloadBrandOperationDto")
    public void downloadBrandOperationDto(String path, HttpServletResponse response){
        try {
            //定义excel工具类对象
            ExcelUtil<BrandOperationDto> excelUtil=new ExcelUtil<>();
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        }catch (Exception e){
            log.error(e.getMessage()+"下载报表出错！");
        }
    }

    /**
     * 创建店铺运营表
     * @param beginDate
     * @param endDate
     * @param operationDto
     * @param request
     * @return
     */
    @RequestMapping("/createShopOperationDto")
    @ResponseBody
    private Result createShopOperationDto(String beginDate, String endDate, BrandOperationDto operationDto, HttpServletRequest request){
        //导出文件名
        String fileName = "品牌运营报表（店铺运营表）"+beginDate+"至"+endDate+".xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns={"brandName","shopName","orderCount","orderMoney","weChatPay","aliPay","customerCount","newCustomerCount","newRegiestCustomerCount",
                "chargeCount","chargeMoney"};
        //定义数据
        List<BrandOperationDto> result = new ArrayList<>();
        if (operationDto.getBrandOperationCount() != null) {
            BrandOperationDto shopOperationDto = new BrandOperationDto();
            Map<String, Object> brandOperationCount = operationDto.getBrandOperationCount();
            shopOperationDto.setBrandName(brandOperationCount.get("brandCount").toString());
            shopOperationDto.setShopName(brandOperationCount.get("shopCount").toString());
            shopOperationDto.setOrderCount(Integer.valueOf(brandOperationCount.get("orderCount").toString()));
            shopOperationDto.setOrderMoney(new BigDecimal(brandOperationCount.get("orderMoney").toString()));
            shopOperationDto.setWeChatPay(new BigDecimal(brandOperationCount.get("weChatPay").toString()));
            shopOperationDto.setAliPay(new BigDecimal(brandOperationCount.get("aliPay").toString()));
            shopOperationDto.setCustomerCount((Integer.valueOf(brandOperationCount.get("customerCount").toString())));
            shopOperationDto.setNewCustomerCount(Integer.valueOf(brandOperationCount.get("newCustomerCount").toString()));
            shopOperationDto.setNewRegiestCustomerCount(Integer.valueOf(brandOperationCount.get("newRegiestCustomerCount").toString()));
            shopOperationDto.setChargeCount(Integer.valueOf(brandOperationCount.get("chargeCount").toString()));
            shopOperationDto.setChargeMoney(new BigDecimal(brandOperationCount.get("chargeMoney").toString()));
            result.add(shopOperationDto);
        }
        if (operationDto.getShopOperationDtos() != null){
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("brandOperationCount");
            filter.getExcludes().add("brandOperationDtos");
            filter.getExcludes().add("shopOperationDtos");
            String json = JSON.toJSONString(operationDto.getShopOperationDtos(),filter);
            List<BrandOperationDto> shopOperationDtos = JSON.parseObject(json, new TypeReference<List<BrandOperationDto>>(){});
            result.addAll(shopOperationDtos);
        }
        Map<String,String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("reportType", "品牌运营报表（店铺运营表）");//表的头，第一行内容
        map.put("num", "10");//显示的位置
        map.put("reportTitle", "品牌运营报表（店铺运营表）");//表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] headers = {{"品牌名称/品牌数","35"},{"店铺名称/店铺数","35"},{"订单总数","35"},{"订单总额","35"},{"微信支付","35"},{"支付宝支付","35"},{"就餐人数","35"},{"新增用户","35"},
                {"新注册用户","35"},{"充值次数","35"},{"充值金额","35"}};
        //定义excel工具类对象
        ExcelUtil<BrandOperationDto> excelUtil=new ExcelUtil<>();
        try{
            OutputStream out = new FileOutputStream(path);
            excelUtil.ExportBrandDtoExcel(headers, columns, result, out, map);
            out.close();
        }catch(Exception e){
            e.printStackTrace();
            return new Result("创建execl失败",false);
        }
        return getSuccessResult(path);
    }

    /**
     * 下载店铺运营表
     * @param path
     * @param response
     */
    @RequestMapping("/downloadShopOperationDto")
    public void downloadShopOperationDto(String path, HttpServletResponse response){
        try {
            //定义excel工具类对象
            ExcelUtil<BrandOperationDto> excelUtil=new ExcelUtil<>();
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        }catch (Exception e){
            log.error(e.getMessage()+"下载报表出错！");
        }
    }
}
