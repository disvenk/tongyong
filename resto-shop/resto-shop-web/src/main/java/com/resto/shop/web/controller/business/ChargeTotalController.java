package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ExcelUtil;
import com.resto.brand.web.dto.ChargeTotalDto;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Account;
import com.resto.shop.web.model.ChargeOrder;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.service.ChargeOrderService;
import com.resto.shop.web.service.CustomerService;
import com.resto.shop.web.util.DataTablesOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 查询品牌充值汇总信息、查询用户充值信息
 */
@Controller
@RequestMapping("/chargeTotal")
public class ChargeTotalController extends GenericController{

    Logger log = LoggerFactory.getLogger(ChargeTotalController.class);

    @Resource
    private ChargeOrderService chargeOrderService;

    @Resource
    private CustomerService customerService;

    /**
     * 进入页面
     */
    @RequestMapping("/list")
    public void list(){
        getRequest().setAttribute("brandName", getBrandName());
    }


    /**
     * 查询品牌充值汇总信息
     * @return
     */
    @RequestMapping("/getChargeTotalInfo")
    @ResponseBody
    public Result getChargeTotalInfo(){
        try {
            //查询品牌的充值汇总信息
            ChargeTotalDto chargeTotalList = chargeOrderService.selectChargeTotal();
            //返回数据
            return getSuccessResult(chargeTotalList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取充值汇总信息出错");
            return new Result(false);
        }
    }

    /**
     * 查询用户充值汇总信息
     * @param draw datatable当前绘制次数
     * @param start 页码
     * @param length 每页显示数量
     * @return
     */
    @RequestMapping("/getCustomerChargeTotal")
    @ResponseBody
    public DataTablesOutput getCustomerChargeTotal(Integer draw, Integer start, Integer length){
        try {
            //获取到用户的搜索信息
            String search = getRequest().getParameter("search[value]");
            //根据当前查询条件查询用户信息
            Map<String, Object> selectMap = new HashMap<String, Object>(){
                {
                    put("selectCount", true);
                    put("text", search);
                    put("queryPage", true);
                    put("pageNo", start);
                    put("pageSize", length);
                }
            };
            //查询数据
            Map<String, Object> result = customerService.queryCustomerPaging(selectMap);
            //取出查询结果，封装用户充值信息
            List<Customer> customerList = (List<Customer>)result.get("customerList");
            List<Account> accountList = (List<Account>)result.get("accountList");
            List<ChargeOrder> chargeOrderList = (List<ChargeOrder>)result.get("chargeOrderList");
            List<ChargeTotalDto> chargeTotalDtoList = getChargeTotalDto(customerList, accountList, chargeOrderList);
            //dataTable返回
            DataTablesOutput<ChargeTotalDto> dataTablesOutput = new DataTablesOutput<>(draw);
            dataTablesOutput.setData(chargeTotalDtoList);
            dataTablesOutput.setRecordsTotal(Long.valueOf(result.get("customerCount").toString()));
            dataTablesOutput.setRecordsFiltered(dataTablesOutput.getRecordsTotal());
            return dataTablesOutput;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询用户充值汇总信息出错");
            return new DataTablesOutput(draw);
        }
    }

    /**
     * 获取用户充值信息
     * @param customerList
     * @param accountList
     * @param chargeOrderList
     * @return
     */
    private List<ChargeTotalDto> getChargeTotalDto(List<Customer> customerList, List<Account> accountList
            , List<ChargeOrder> chargeOrderList) {
        List<ChargeTotalDto> chargeTotalDtoList = new ArrayList<>();
        customerList.forEach(customer -> {
            ChargeTotalDto chargeTotalDto =  new ChargeTotalDto();
            //用户名
            chargeTotalDto.setNickname(customer.getNickname());
            //电话号码
            chargeTotalDto.setTelephone(Optional.ofNullable(customer.getTelephone()).orElse("未注册"));
            //账户余额
            BigDecimal remain = accountList.stream().filter(account -> account.getId().equalsIgnoreCase(customer.getAccountId())).map(Account::getRemain)
                    .findFirst().get();
            chargeTotalDto.setRemain(remain);
            //获取当前用户所有充值信息的充值本金
            List<BigDecimal> chargeMoney = chargeOrderList.stream().filter(chargeOrder -> chargeOrder.getCustomerId().equalsIgnoreCase(customer.getId()))
                    .map(ChargeOrder::getChargeMoney).collect(Collectors.toList());
            //获取当前用户所有充值信息的赠送金额
            List<BigDecimal> rewardMoney = chargeOrderList.stream().filter(chargeOrder -> chargeOrder.getCustomerId().equalsIgnoreCase(customer.getId()))
                    .map(ChargeOrder::getRewardMoney).collect(Collectors.toList());
            //获取当前用户所有充值信息的充值本金的剩余金额
            List<BigDecimal> chargeBalance = chargeOrderList.stream().filter(chargeOrder -> chargeOrder.getCustomerId().equalsIgnoreCase(customer.getId()))
                    .map(ChargeOrder::getChargeBalance).collect(Collectors.toList());
            //获取当前用户所有充值信息的赠送金额的剩余金额
            List<BigDecimal> rewardBalance = chargeOrderList.stream().filter(chargeOrder -> chargeOrder.getCustomerId().equalsIgnoreCase(customer.getId()))
                    .map(ChargeOrder::getRewardBalance).collect(Collectors.toList());
            //充值次数
            chargeTotalDto.setHistoryChargeCount((int)chargeOrderList.stream().filter(chargeOrder -> chargeOrder.getCustomerId().equalsIgnoreCase(customer.getId()))
                    .count());
            //历史储值总额
            chargeTotalDto.setHistoryChargeMoney(takeAndTake(chargeMoney));
            //储值赠送总额
            chargeTotalDto.setHistoryRewardMoney(takeAndTake(rewardMoney));
            //充值账户余额
            chargeTotalDto.setHistoryChargeBalance(takeAndTake(chargeBalance));
            //充值赠送余额
            chargeTotalDto.setHistoryRewardBalance(takeAndTake(rewardBalance));
            //红包剩余总额
            chargeTotalDto.setRedBalance(chargeTotalDto.getRemain().subtract(chargeTotalDto.getHistoryChargeBalance())
                    .subtract(chargeTotalDto.getHistoryRewardBalance()));
            chargeTotalDtoList.add(chargeTotalDto);
        });
        return chargeTotalDtoList;
    }

    /**
     * list取和
     * @param param
     * @return
     */
    private BigDecimal takeAndTake(List<BigDecimal> param){
        return param.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @RequestMapping("/createBrandChargeTotalExcel")
    @ResponseBody
    public Result createBrandChargeTotalExcel(ChargeTotalDto chargeTotalDto, HttpServletRequest request){
        //导出文件名
        String fileName = "品牌账户报表.xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns = {"historyChargeCount","historyChargeMoney","historyRewardMoney","historyChargeBalance","historyRewardBalance"};
        //定义数据
        List<ChargeTotalDto> result = new ArrayList<>();
        result.add(chargeTotalDto);
        String shopName = getCurrentShopNames();
        Map<String,String> map = new HashMap<>();
        map.put("brandName", getBrandName());
        map.put("shops", shopName);
        map.put("beginDate", "品牌上线");
        map.put("reportType", "品牌账户报表");//表的头，第一行内容
        map.put("endDate", "今日");
        map.put("num", "4");//显示的位置
        map.put("reportTitle", "品牌账户报表");//表的名字
        map.put("timeType", "yyyy-MM-dd");
        //定义列明
        String[][] headers = {{"品牌历史储值总数量","25"},{"品牌历史储值总额","25"},{"品牌历史储值赠送总额","25"},{"储值剩余总额","25"},{"储值赠送剩余总额","25"}};
        //定义excel工具类对象
        ExcelUtil<ChargeTotalDto> excelUtil = new ExcelUtil<>();
        try{
            OutputStream out = new FileOutputStream(path);
            excelUtil.ExportExcel(headers, columns, result, out, map);
            out.close();
        }catch(Exception e){
            log.error("品牌账户报表");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(path);
    }

    /**
     * 生成会员账户报表
     * @param search
     * @param request
     * @return
     */
    @RequestMapping("/createCustomerChargeExcel")
    @ResponseBody
    public Result createCustomerChargeExcel(String search, HttpServletRequest request){
        //导出文件名
        String fileName = "会员账户报表.xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns = {"nickname","telephone","historyChargeCount","historyChargeMoney","historyRewardMoney","remain","historyChargeBalance"
                ,"historyRewardBalance","redBalance"};
        String shopName = getCurrentShopNames();
        Map<String,String> map = new HashMap<>();
        map.put("brandName", getBrandName());
        map.put("shops", shopName);
        map.put("beginDate", "品牌上线");
        map.put("reportType", "会员账户报表");//表的头，第一行内容
        map.put("endDate", "今日");
        map.put("num", "8");//显示的位置
        map.put("reportTitle", "会员账户报表");//表的名字
        map.put("timeType", "yyyy-MM-dd");
        //定义列明
        String[][] headers = {{"昵称","25"},{"手机号码","25"},{"历史储值总数量","25"},{"历史储值总额","25"},{"历史储值赠送总额","25"}
                ,{"账户余额","25"},{"储值剩余总额","25"},{"储值赠送剩余总额","25"},{"红包余额","25"}};
        //定义excel工具类对象
        ExcelUtil<ChargeTotalDto> excelUtil = new ExcelUtil<>();
        try {
            //根据当前查询条件查询用户信息
            Map<String, Object> selectMap = new HashMap<String, Object>(){
                {
                    put("text", search);
                    put("queryPage", true);
                    put("pageSize", 1000);
                }
            };
            List<Customer> customerList = new ArrayList<>();
            List<Account> accountList = new ArrayList<>();
            List<ChargeOrder> chargeOrderList = new ArrayList<>();
            //查询数据
            Map<String, Object> queryResult;
            Integer nowTotal;
            AtomicReference<Integer> pageNo = new AtomicReference<>(0);
            do {
                selectMap.put("pageNo", pageNo.get() * 1000);
                queryResult = customerService.queryCustomerPaging(selectMap);
                customerList.addAll((List<Customer>)queryResult.get("customerList"));
                accountList.addAll((List<Account>)queryResult.get("accountList"));
                chargeOrderList.addAll((List<ChargeOrder>)queryResult.get("chargeOrderList"));
                nowTotal = ((List<Customer>) queryResult.get("customerList")).size();
                //pageNo累加
                pageNo.updateAndGet(v -> v + 1);
            } while (nowTotal > 0);
            //定义数据
            List<ChargeTotalDto> result = getChargeTotalDto(customerList, accountList, chargeOrderList);
            OutputStream out = new FileOutputStream(path);
            excelUtil.ExportExcel(headers, columns, result, out, map);
            out.close();
            return getSuccessResult(path);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成会员账户报表出错");
            return new Result(false);
        }
    }

    /**
     * 下载报表
     * @param path
     * @param response
     */
    @RequestMapping("/downloadExcel")
    public void downloadExcel(String path, HttpServletResponse response){
        //定义excel工具类对象
        ExcelUtil<ChargeTotalDto> excelUtil=new ExcelUtil<>();
        try {
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        }catch (Exception e){
            log.error("下载报表出错！");
            e.printStackTrace();
        }
    }
}
