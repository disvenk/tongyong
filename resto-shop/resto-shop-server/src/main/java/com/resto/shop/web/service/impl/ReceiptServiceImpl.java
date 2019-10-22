package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.FaPiaoUtils;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ElectronicTicketConfig;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.TableQrcode;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.PrinterType;
import com.resto.shop.web.constant.TicketType;
import com.resto.shop.web.dao.OrderMapper;
import com.resto.shop.web.dao.OrderPaymentItemMapper;
import com.resto.shop.web.dao.ReceiptMapper;
import com.resto.shop.web.dao.ReceiptTitleMapper;
import com.resto.shop.web.dto.ReceiptOrder;
import com.resto.shop.web.dto.ReceiptPos;
import com.resto.shop.web.dto.ReceiptPosOrder;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.AreaService;
import com.resto.shop.web.service.PrinterService;
import com.resto.shop.web.service.ReceiptService;
import com.resto.shop.web.service.ReceiptTitleService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

/**
 * Created by xielc on 2017/9/5.
 */
@Component
@Service
public class ReceiptServiceImpl extends GenericServiceImpl<Receipt,Long> implements ReceiptService {

    @Resource
    ReceiptMapper receiptMapper;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    PrinterService printerService;

    @Resource
    BrandService brandService;

    @Resource
    ReceiptTitleService receiptTitleService;

    @Autowired
    private TableQrcodeService tableQrcodeService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private ReceiptTitleMapper receiptTitleMapper;

    @Autowired
    private ElectronicTicketConfigService electronicTicketConfigService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderPaymentItemMapper orderPaymentItemMapper;

    @Autowired
    RedisService redisService;

    @Override
    public GenericDao<Receipt, Long> getDao() {
        return receiptMapper;
    }

    @Override
    public Receipt insertSelective(Receipt record){
        int count =receiptMapper.insertSelective(record);
        if(count==0){
            return null;
        }else{
            return record;
        }
    }

    @Override
    public int updateByPrimaryKeySelective(Receipt record){
        return receiptMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateState(Receipt record){
        return receiptMapper.updateState(record);
    }

    @Override
    public int updateReceiptOrderNumber(Receipt record){
        if(!record.getOrderMoney().equals(new BigDecimal("0.00"))){
            ReceiptOrder r=receiptMapper.selectReceiptOrderOneMoney(record.getOrderNumber());
            record.setReceiptMoney(record.getReceiptMoney().intValue() <= r.getReceiptMoney().intValue()? record.getReceiptMoney() : r.getReceiptMoney());
            return receiptMapper.updateReceiptOrderNumber(record);
        }
        record.setReceiptMoney(record.getOrderMoney());
        return receiptMapper.updateReceiptOrderNumber(record);
    }

    @Override
    public int getReceiptOrderNumberCount(String orderNumber){
        return receiptMapper.getReceiptOrderNumberCount(orderNumber);
    }

    @Override
    public List<ReceiptOrder> selectReceiptOrderList(String customerId,String shopId,String state,Integer ticketType){
        if(state==null||state.equals("")){
            List<ReceiptOrder> rlist=receiptMapper.selectApplyReceiptOrderList(customerId,shopId);
            if(rlist!=null && !rlist.isEmpty()){
                for(ReceiptOrder receiptOrder:rlist){
                    ReceiptOrder r=receiptMapper.selectReceiptMoney(receiptOrder.getOrderNumber());
                    receiptOrder.setReceiptMoney(receiptOrder.getReceiptMoney().compareTo(r.getReceiptMoney())<=0? receiptOrder.getReceiptMoney().setScale(2, BigDecimal.ROUND_HALF_UP) : r.getReceiptMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            }
            return rlist;
        }else{
            return receiptMapper.selectReceiptOrderList(customerId,shopId,ticketType);
        }
    }
    @Override
    public ReceiptPosOrder getReceiptOrderList(String receiptId){
        return receiptMapper.getReceiptOrderList(Integer.parseInt(receiptId));
    }
    @Override
    public ReceiptOrder selectReceiptMoney(String orderNumber){
        return receiptMapper.selectReceiptMoney(orderNumber);
    }

    /**
    *@Description:批量开票
    *@Author:disvenk.dai
    *@Date:19:57 2018/6/8 0008
    */
    @Override
    public Boolean ticketToMany(Receipt receipt ,ReceiptTitle title, String brandId,String url) {

        Brand brand = brandService.selectById(brandId);
        ElectronicTicketConfig electronicTicketConfig = electronicTicketConfigService.selectByBrandId(brandId);

        Order order = orderMapper.selectBySerialNumber(receipt.getOrderNumber());
        BigDecimal money = orderPaymentItemMapper.selectEnableTicketMoney(order.getId());
        log.info("可开票金额"+money);

        //价税合计
        String count =money.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        log.info("价税合计"+count);

        //总价和单价一样，因为只有一份,发票之间的误差不能超过6分钱
        BigDecimal sumPrice = money.divide(BigDecimal.valueOf(1.06),2,BigDecimal.ROUND_HALF_UP);
        log.info("单价"+sumPrice);


        //税额
        BigDecimal tax = sumPrice.multiply(BigDecimal.valueOf(0.06)).setScale(2, BigDecimal.ROUND_HALF_UP);
        String taxStr = tax.toString();
        String sumPriceStr = money.subtract(tax).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        log.info("税额"+tax);
        //进行阿里发票的接口请求
        String serial = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSSS");
        Boolean result = false;

        //选择已经录入好的抬头
        if(receipt.getReceiptTitleId()!=null){
            ReceiptTitle receiptTitle = receiptTitleMapper.selectByPrimaryKey(Long.valueOf(receipt.getReceiptTitleId()));
            if( receiptTitle.getType()==2){//个人
                result = FaPiaoUtils.applyTicket(electronicTicketConfig.getAppkey(),electronicTicketConfig.getAppsecret(),receipt.getOrderNumber(),serial,electronicTicketConfig.getAddress(),
                        electronicTicketConfig.getName(),receiptTitle.getDutyParagraph(),electronicTicketConfig.getOperator(),count,
                        sumPriceStr,taxStr,electronicTicketConfig.getPayeeRegisterNo(),receiptTitle.getCompanyAddress(),
                        receiptTitle.getBankNumber(),receiptTitle.getName(),receiptTitle.getMobileNo(),
                        electronicTicketConfig.getPayeeChecker(),electronicTicketConfig.getPayeeReceiver(),electronicTicketConfig.getPayeePhone());

                if(result){
                receipt.setPayeeRegisterNo(electronicTicketConfig.getPayeeRegisterNo());
                receipt.setSerialNo(serial);
                receipt.setTicketType(1);
                receipt.setTicketUrl(url+"/wechat/receipt/downLoadPdf?serial="+serial+
                        "&platformTid="+receipt.getOrderNumber()+"&payeeRegisterNo="+electronicTicketConfig.getPayeeRegisterNo());
                receipt.setState(1);
                receipt.setCreateTime(new Date());
                receiptMapper.insert(receipt);
                }
            }else {//公司
                try {
                    Thread.sleep(100);
                    result = FaPiaoUtils.applyTicket(electronicTicketConfig.getAppkey(),electronicTicketConfig.getAppsecret(),receipt.getOrderNumber(),serial,electronicTicketConfig.getAddress(),
                            electronicTicketConfig.getName(),receiptTitle.getDutyParagraph(),electronicTicketConfig.getOperator(),count,
                            sumPriceStr,taxStr,electronicTicketConfig.getPayeeRegisterNo(),receiptTitle.getCompanyAddress(),
                            receiptTitle.getBankOfDeposit(),receiptTitle.getName(),receiptTitle.getMobileNo(),
                            electronicTicketConfig.getPayeeChecker(),electronicTicketConfig.getPayeeReceiver(),electronicTicketConfig.getPayeePhone());

                    if(result){
                        receipt.setPayeeRegisterNo(electronicTicketConfig.getPayeeRegisterNo());
                        receipt.setSerialNo(serial);
                        receipt.setTicketType(1);
                        receipt.setTicketUrl(url+"/wechat/receipt/downloadPdf?serial="+serial+
                                "&platformTid="+receipt.getOrderNumber()+"&payeeRegisterNo="+electronicTicketConfig.getPayeeRegisterNo());
                        receipt.setState(1);
                        receipt.setCreateTime(new Date());
                        receiptMapper.insert(receipt);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        //手动输入的
        }else {
            if( title.getType()==2){//个人
                result = FaPiaoUtils.applyTicket(electronicTicketConfig.getAppkey(),electronicTicketConfig.getAppsecret(),receipt.getOrderNumber(),serial,electronicTicketConfig.getAddress(),
                        electronicTicketConfig.getName(),title.getDutyParagraph(),electronicTicketConfig.getOperator(),count,
                        sumPriceStr,taxStr,electronicTicketConfig.getPayeeRegisterNo(),title.getCompanyAddress(),
                        title.getBankOfDeposit(),title.getName(),title.getMobileNo(),
                        electronicTicketConfig.getPayeeChecker(),electronicTicketConfig.getPayeeReceiver(),electronicTicketConfig.getPayeePhone());

               // receiptTitleMapper.insert(title);
                if(result){
                    receipt.setPayeeRegisterNo(electronicTicketConfig.getPayeeRegisterNo());
                    receipt.setSerialNo(serial);
                    receipt.setTicketType(1);
                    receipt.setTicketUrl(url+"/wechat/receipt/downloadPdf?serial="+serial+
                            "&platformTid="+receipt.getOrderNumber()+"&payeeRegisterNo="+electronicTicketConfig.getPayeeRegisterNo());
                    receipt.setState(1);
                    receipt.setCreateTime(new Date());
                    receiptMapper.insert(receipt);
                    //receiptTitleMapper.insert(title);
                }
            }else {//公司
                try {
                    Thread.sleep(100);
                    result = FaPiaoUtils.applyTicket(electronicTicketConfig.getAppkey(),electronicTicketConfig.getAppsecret(),receipt.getOrderNumber(),serial,electronicTicketConfig.getAddress(),
                            electronicTicketConfig.getName(),title.getDutyParagraph(),electronicTicketConfig.getOperator(),count,
                            sumPriceStr,taxStr,electronicTicketConfig.getPayeeRegisterNo(),title.getCompanyAddress(),
                            title.getBankOfDeposit(),title.getName(),title.getMobileNo(),
                            electronicTicketConfig.getPayeeChecker(),electronicTicketConfig.getPayeeReceiver(),electronicTicketConfig.getPayeePhone());

                   // receiptTitleMapper.insert(title);
                    if(result){
                        receipt.setPayeeRegisterNo(electronicTicketConfig.getPayeeRegisterNo());
                        receipt.setSerialNo(serial);
                        receipt.setTicketType(1);
                        receipt.setTicketUrl(url+"/wechat/receipt/downloadPdf?serial="+serial+
                                "&platformTid="+receipt.getOrderNumber()+"&payeeRegisterNo="+electronicTicketConfig.getPayeeRegisterNo());
                        receipt.setState(1);
                        receipt.setCreateTime(new Date());
                        receiptMapper.insert(receipt);
                        //receiptTitleMapper.insert(title);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    @Override
    public ReceiptPos getPosReceiptList(String orderNumber){
        return receiptMapper.getPosReceiptList(orderNumber);
    }
    @Override
    public List<ReceiptPos> getReceiptList(String shopId,String state){
        List<ReceiptPos> rlist=new ArrayList<>();
        if(state==null||state.equals("")){
            //查询当天的所有发票
            List<ReceiptPos> now = receiptMapper.getNowReceiptList(shopId,null);
            rlist.addAll(now);
            //查询以前的所有发票
            List<ReceiptPos> before = receiptMapper.getBeforeReceiptList(shopId);
            rlist.addAll(before);
        }else if(state.equals("0")){
            List<ReceiptPos> now = receiptMapper.getNowReceiptList(shopId,Integer.parseInt(state));
            rlist.addAll(now);
            List<ReceiptPos> before = receiptMapper.getBeforeReceiptList(shopId);
            rlist.addAll(before);
        }else {
            List<ReceiptPos> now = receiptMapper.getNowReceiptList(shopId,Integer.parseInt(state));
            rlist.addAll(now);
        }
        return rlist;
    }

    /**
     * 发票打印
     * @return
     */
    @Override
    public List<Map<String, Object>> printReceiptOrder(String receiptId,String ShopId){
        ReceiptPosOrder receiptPosOrder = receiptMapper.getReceiptOrderList(Integer.parseInt(receiptId));
        ShopDetail shopDetail = shopDetailService.selectById(ShopId);
        List<Map<String, Object>> printTask = new ArrayList<>();
        List<Printer> ticketPrinter=new ArrayList<>();
        ticketPrinter.addAll(printerService.selectPrintByType(ShopId,PrinterType.RECEPTION));
        for (Printer printer : ticketPrinter) {
            getReceiptOrderModel(receiptPosOrder, printer,shopDetail, printTask);
        }
        Brand brand = brandService.selectById(shopDetail.getBrandId());
        JSONArray json = new JSONArray(printTask);
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", shopDetail.getName());
        map.put("type", "posAction");
        map.put("content", "发票订单:" + receiptPosOrder.getOrderNumber() + "返回(手动)打印发票模版" + json.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        return printTask;
    }

    /**
     * pos发票打印
     * @return
     */
    @Override
    public List<Map<String, Object>> printReceiptPosOrder(String orderNumber,String ShopId){
        ReceiptPosOrder receiptPosOrder = receiptMapper.getReceiptOrderNumberList(orderNumber);
        ShopDetail shopDetail = shopDetailService.selectById(ShopId);
        List<Map<String, Object>> printTask = new ArrayList<>();
        List<Printer> ticketPrinter=new ArrayList<>();
        ticketPrinter.addAll(printerService.selectPrintByType(ShopId,PrinterType.RECEPTION));
        for (Printer printer : ticketPrinter) {
            getReceiptOrderModel(receiptPosOrder, printer,shopDetail, printTask);
        }
        Brand brand = brandService.selectById(shopDetail.getBrandId());
        JSONArray json = new JSONArray(printTask);
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", shopDetail.getName());
        map.put("type", "posAction");
        map.put("content", "发票订单:" + receiptPosOrder.getOrderNumber() + "返回(自动)打印发票模版" + json.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        return printTask;
    }

    private void getReceiptOrderModel(ReceiptPosOrder receiptPosOrder, Printer printer,ShopDetail shopDetail, List<Map<String, Object>> printTask){
        String modeText="增值税发票";
        Map<String, Object> print = new HashMap<String, Object>();
        print.put("PORT", printer.getPort());
        print.put("OID", receiptPosOrder.getReceiptId());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_TASK_ID", print_id);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("ORDER_ID", receiptPosOrder.getOrderNumber());
        data.put("DATETIME", DateUtil.formatDate(receiptPosOrder.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
        data.put("DISTRIBUTION_MODE", modeText+receiptPosOrder.getTableNumber());
        //data.put("TABLE_NUMBER", "");
        //添加当天打印订单的序号
        TableQrcode tableQrcode = tableQrcodeService.selectByTableNumberShopId(shopDetail.getId(), Integer.valueOf(receiptPosOrder.getTableNumber()));
        if (tableQrcode == null) {
            //data.put("ORDER_NUMBER",  "---");
            data.put("TABLE_NUMBER", "---");
        } else {
            if (tableQrcode.getAreaId() == null) {
                //data.put("ORDER_NUMBER", "---");
                data.put("TABLE_NUMBER", "---");
            } else {
                Area area = areaService.selectById(tableQrcode.getAreaId());
                if (area == null) {
                    //data.put("ORDER_NUMBER", "---");
                    data.put("TABLE_NUMBER", "---");
                } else {
                    //data.put("ORDER_NUMBER", area.getName());
                    data.put("TABLE_NUMBER", area.getName());
                }
            }

        }
        ReceiptTitle receiptTitle=receiptTitleService.selectByPrimaryKey(receiptPosOrder.getReceiptTitleId());
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> itemMoney = new HashMap<String, Object>();
        itemMoney.put("ARTICLE_COUNT","订单金额:");
        itemMoney.put("ARTICLE_NAME",receiptPosOrder.getOrderMoney()+"元");
        items.add(itemMoney);
        Map<String, Object> itemName = new HashMap<String, Object>();
        itemName.put("ARTICLE_COUNT","    抬头:");
        itemName.put("ARTICLE_NAME",receiptPosOrder.getName());
        items.add(itemName);
        Map<String, Object> itemDutyParagraph = new HashMap<String, Object>();
        itemDutyParagraph.put("ARTICLE_COUNT","    税号:");
        itemDutyParagraph.put("ARTICLE_NAME",receiptPosOrder.getDutyParagraph());
        items.add(itemDutyParagraph);
        Map<String, Object> itemCompanyAddress = new HashMap<String, Object>();
        itemCompanyAddress.put("ARTICLE_COUNT","单位地址:");
        itemCompanyAddress.put("ARTICLE_NAME",receiptTitle.getCompanyAddress());
        items.add(itemCompanyAddress);
        Map<String, Object> itemMobileNo = new HashMap<String, Object>();
        itemMobileNo.put("ARTICLE_COUNT","公司电话:");
        itemMobileNo.put("ARTICLE_NAME",receiptTitle.getMobileNo());
        items.add(itemMobileNo);
        Map<String, Object> itemBankOfDeposit = new HashMap<String, Object>();
        itemBankOfDeposit.put("ARTICLE_COUNT","开户银行:");
        itemBankOfDeposit.put("ARTICLE_NAME",receiptTitle.getBankOfDeposit());
        items.add(itemBankOfDeposit);
       Map<String, Object> itemBankNumber = new HashMap<String, Object>();
        itemBankNumber.put("ARTICLE_COUNT","银行账号:");
        itemBankNumber.put("ARTICLE_NAME",receiptTitle.getBankNumber());
        items.add(itemBankNumber);
        data.put("ITEMS", items);
        data.put("CUSTOMER_SATISFACTION", "");
        data.put("CUSTOMER_SATISFACTION_DEGREE", "");
        data.put("CUSTOMER_PROPERTY", "");
        print.put("DATA", data);
        print.put("STATUS", "0");
        print.put("TICKET_TYPE", TicketType.KITCHEN);
        //添加到 打印集合
        printTask.add(print);
//        RedisUtil.set(print_id, print);
        redisService.set(print_id, print);
    }

}
