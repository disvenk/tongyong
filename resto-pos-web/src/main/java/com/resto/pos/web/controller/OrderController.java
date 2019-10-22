package com.resto.pos.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.*;
import com.resto.brand.web.dto.ArticleSellDto;
import com.resto.brand.web.dto.ShopIncomeDto;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.RefundRemark;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.*;
import com.resto.pos.web.constant.Dictionary;
import com.resto.pos.web.util.QrCodeUtils;
import com.resto.shop.web.constant.*;
import com.resto.shop.web.dto.ReceiptOrder;
import com.resto.shop.web.dto.ReceiptPos;
import com.resto.shop.web.dto.ReceiptPosOrder;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

@RestController
@RequestMapping("order")
@SuppressWarnings("all")
public class OrderController extends GenericController {

    @Resource
    OrderService orderService;

    @Resource
    CustomerService customerService;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    OrderPaymentItemService orderPaymentItemService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    BrandService brandService;

    @Resource
    PrinterService printerService;

    @Resource
    ArticleFamilyService articleFamilyService;

    @Resource
    ChargeOrderService chargeOrderService;

    @Autowired
    ThirdService thirdService;

    @Resource
    OffLineOrderService offLineOrderService;

    @Resource
    AccountService accountService;

    @Resource
    CouponService couponService;

    @Autowired
    WechatConfigService wechatConfigService;

    @Autowired
    ArticleRecommendService articleRecommendService;

    @Autowired
    RefundRemarkService refundRemarkService;

    @Resource
    ReceiptService receiptService;

    @Resource
    CloseShopService closeShopService;

    @Resource
    AccountLogService accountLogService;

    @Resource
    PlatformOrderService platformOrderService;

    @Resource
    TableGroupService tableGroupService;

    @Autowired
    RedisService redisService;

    @Autowired
    WeChatService weChatService;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @RequestMapping("callNumber")
    public Result callOrder(String orderId) {
        orderService.callNumber(orderId);
        return getSuccessResult();
    }

    @RequestMapping("/printReceipt")
    public Result printReceipt(String orderId, Integer printerId) {
        Map<String, Object> receipt = orderService.printReceipt(orderId, printerId);
        return getSuccessResult(receipt);
    }

    @RequestMapping("/printHungerReceipt")
    public Result printHungerReceipt(String orderId, Integer printerId) {
        Map<String, Object> receipt = thirdService.printReceipt(orderId, printerId, "shoudong");
        return getSuccessResult(receipt);
    }

    @RequestMapping("/printOrderAll")
    public Result printOrderAll(String orderId) {
        List<Map<String, Object>> printTask = orderService.printOrderAll(orderId);
        return getSuccessResult(printTask);
    }

    @RequestMapping("/printPlatformOrderAll")
    public Result printPlatformOrderAll(String orderId, Integer type) {
        List<Map<String, Object>> printTask = thirdService.printOrderByPlatform(orderId, type);
        return getSuccessResult(printTask);
    }

    @RequestMapping("/printSuccess")
    public Result printSuccess(String orderId) throws AppException {
        orderService.printSuccess(orderId,openBrandAccount(),getAccountSetting());
        return getSuccessResult();
    }

    @RequestMapping("/printUpdate")
    public Result printUpdate(String orderId){
        orderService.printUpdate(orderId);
        return getSuccessResult();
    }

    @RequestMapping("/printOrderAndAutoSuccess")
    public Result printOrderAllAndAutoSuccess(String orderId) throws AppException {
        List<Map<String, Object>> printTask = orderService.printOrderAll(orderId);
        orderService.printSuccess(orderId,openBrandAccount(),getAccountSetting());
        return getSuccessResult(printTask);
    }

    @RequestMapping("/orderPaymentItems")
    public Result orderPaymentItems(String beginDate, String endDate) {

        return getSuccessResult(getResult(beginDate, endDate));
    }

    @RequestMapping("/receiveMoney")
    public Result receiveMoney(String orderId) throws AppException {
        Map<String, Object> printTask = printerService.openCashDrawer(orderId, getCurrentShopId());
        return getSuccessResult(printTask);
    }

    @RequestMapping("/receiveMoneyNew")
    public Result receiveMoneyNew(String orderId) throws AppException {
        Map<String, Object> printTask = printerService.openCashDrawerNew(orderId, getCurrentShopId());
        return getSuccessResult(printTask);
    }


    @RequestMapping("/changeMode")
    public Result changeMode(String oid) {
        orderService.changeOrderMode(oid);
        return getSuccessResult();
    }

    /**
     * 店铺收入报表查询
     * 2016-10-29
     * @param beginDate
     * @param endDate
     * @return
     */
    private List<ShopIncomeDto> getResult(String beginDate, String endDate) {
        List<ShopIncomeDto> shopIncomeDtos = new ArrayList<>();
        ShopIncomeDto shopIncomeDto = new ShopIncomeDto(getCurrentShop().getName(), getCurrentShopId());
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("beginDate", beginDate);
        selectMap.put("endDate", endDate);
        selectMap.put("shopId", getCurrentShopId());
        //用来接收分段查询出来的订单金额信息
        List<ShopIncomeDto> shopIncomeDtosItem = new ArrayList<>();
        shopIncomeDtosItem.add(new ShopIncomeDto());
        //用来累加分段查询出来的订单金额信息
        List<ShopIncomeDto> shopIncomeDtosItems = new ArrayList<>();
        //用来接收分段查询出来的订单支付项信息
        List<ShopIncomeDto> shopIncomeDtosPayMent = new ArrayList<>();
        shopIncomeDtosPayMent.add(new ShopIncomeDto());
        //用来累加分段查询出来的订单支付项信息
        List<ShopIncomeDto> shopIncomeDtosPayMents = new ArrayList<>();
        for (int pageNo = 0; (shopIncomeDtosItem != null && !shopIncomeDtosItem.isEmpty())
                || (shopIncomeDtosPayMent != null && !shopIncomeDtosPayMent.isEmpty()); pageNo ++){
            selectMap.put("pageNo", pageNo * 1000);
            shopIncomeDtosItem = orderService.callProcDayAllOrderItem(selectMap);
            shopIncomeDtosPayMent = orderService.callProcDayAllOrderPayMent(selectMap);
            shopIncomeDtosItems.addAll(shopIncomeDtosItem);
            shopIncomeDtosPayMents.addAll(shopIncomeDtosPayMent);
        }
        //循环累加店铺订单总额、原价金额
        for (ShopIncomeDto shopIncomeDtoItem : shopIncomeDtosItems){
            if (shopIncomeDtoItem.getParentOrderId() == null){
                shopIncomeDto.setOrderCount(shopIncomeDto.getOrderCount() + 1);
            }
            shopIncomeDto.setOriginalAmount(shopIncomeDto.getOriginalAmount().add(shopIncomeDtoItem.getOriginalAmount()));
            shopIncomeDto.setTotalIncome(shopIncomeDto.getTotalIncome().add(shopIncomeDtoItem.getTotalIncome()));
        }
        //循环累加得到店铺各个支付项的值
        for (ShopIncomeDto shopIncomeDtoPayMent : shopIncomeDtosPayMents){
            shopIncomeDto.setWechatIncome(shopIncomeDto.getWechatIncome().add(shopIncomeDtoPayMent.getWechatIncome()));
            shopIncomeDto.setChargeAccountIncome(shopIncomeDto.getChargeAccountIncome().add(shopIncomeDtoPayMent.getChargeAccountIncome()));
            shopIncomeDto.setRedIncome(shopIncomeDto.getRedIncome().add(shopIncomeDtoPayMent.getRedIncome()));
            shopIncomeDto.setCouponIncome(shopIncomeDto.getCouponIncome().add(shopIncomeDtoPayMent.getCouponIncome()));
            shopIncomeDto.setChargeGifAccountIncome(shopIncomeDto.getChargeGifAccountIncome().add(shopIncomeDtoPayMent.getChargeGifAccountIncome()));
            shopIncomeDto.setWaitNumberIncome(shopIncomeDto.getWaitNumberIncome().add(shopIncomeDtoPayMent.getWaitNumberIncome()));
            shopIncomeDto.setAliPayment(shopIncomeDto.getAliPayment().add(shopIncomeDtoPayMent.getAliPayment()));
            shopIncomeDto.setBackCartPay(shopIncomeDto.getBackCartPay().add(shopIncomeDtoPayMent.getBackCartPay()));
            shopIncomeDto.setMoneyPay(shopIncomeDto.getMoneyPay().add(shopIncomeDtoPayMent.getMoneyPay()));
            shopIncomeDto.setShanhuiPayment(shopIncomeDto.getShanhuiPayment().add(shopIncomeDtoPayMent.getShanhuiPayment()));
            shopIncomeDto.setIntegralPayment(shopIncomeDto.getIntegralPayment().add(shopIncomeDtoPayMent.getIntegralPayment()));
            shopIncomeDto.setArticleBackPay(shopIncomeDto.getArticleBackPay().add(shopIncomeDtoPayMent.getArticleBackPay()));
            shopIncomeDto.setOtherPayment(shopIncomeDto.getOtherPayment().add(shopIncomeDtoPayMent.getOtherPayment()));
            shopIncomeDto.setRefundCrashPayment(shopIncomeDto.getRefundCrashPayment().add(shopIncomeDtoPayMent.getRefundCrashPayment().abs()));
        }
        shopIncomeDtos.add(shopIncomeDto);
        return shopIncomeDtos;
    }

    @RequestMapping("/orderArticleItems")
    public Result reportList(String beginDate, String endDate, String sort) {
        //菜品销售记录
        List<ArticleSellDto> list = orderService.selectShopArticleByDate(getCurrentShopId(), beginDate, endDate, sort);
        return getSuccessResult(list);
    }


    @RequestMapping("/getOrderInfo")
    public Result getOrderInfo(String orderId) {
        Order order = orderService.getOrderInfoPos(orderId);
        order.setShopName(getCurrentShop().getName());
        return getSuccessResult(order);
    }

    @RequestMapping("/getChildItem")
    public Result getChildItem(String orderId) {
        List<Order> result = orderService.getChildItem(orderId);
        return getSuccessResult(result);
    }


    @RequestMapping("/getOutFoodInfo")
    public Result getOutFoodInfo(String orderId) {
        HungerOrder order = thirdService.getOutFoodInfo(orderId);
        return getSuccessResult(order);
    }

    @RequestMapping("/getHistroyOrderList")
    public Result getHistroyOrderList(Date date) {
        List<Order> lists = orderService.selectHistoryOrderList(getCurrentShopId(), new Date(), getCurrentShop().getShopMode());
        return getSuccessResult(lists);
    }

    @RequestMapping("/getOutFoodList")
    public Result getOutFoodList(Date date) {
        List<HungerOrder> lists = thirdService.getOutFoodList(getCurrentShopId());
        return getSuccessResult(lists);
    }

    @RequestMapping("/getReceiptList")
    public Result getReceiptList() {
        List<ReceiptPos> lists = receiptService.getReceiptList(getCurrentShopId(),null);
        return getSuccessResult(lists);
    }

    @RequestMapping("/getReceiptStateList")
    public Result getReceiptStateList(String state) {
        List<ReceiptPos> lists = receiptService.getReceiptList(getCurrentShopId(),state);
        return getSuccessResult(lists);
    }

    @RequestMapping("/getReceiptOrderList")
    @ResponseBody
    public ReceiptPosOrder getReceiptOrderList(String receiptId) {
        ReceiptPosOrder receiptPosOrder = receiptService.getReceiptOrderList(receiptId);
        return receiptPosOrder;
    }

    @RequestMapping("/getPosReceiptList")
    @ResponseBody
    public ReceiptPos getPosReceiptList(String orderNumber) {
        ReceiptPos receiptPos = receiptService.getPosReceiptList(orderNumber);
        return receiptPos;
    }

    @RequestMapping(value = "/qrcode")
    private ModelAndView createQrCode(String content, int width, int height, HttpServletResponse response) {
        BufferedImage bi = QrCodeUtils.encodePRToBufferedImage(content, width,height);
        try {
            response.setContentType("image/jpeg");
            OutputStream os = response.getOutputStream();
            ImageIO.write(bi, "jpg", os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }


    @RequestMapping("/getOrderAccount")
    public Result getOrderAccount() {
        Order order = orderService.getOrderAccount(getCurrentShopId());
        redisService.set(getCurrentShopId()+Dictionary.ORDER_TOTAL,order.getOrderTotal());
        redisService.set(getCurrentShopId()+Dictionary.ORDER_COUNT,order.getOrderCount());
        return getSuccessResult(order);
    }

    @RequestMapping("/getErrorOrderList")
    public Result getErrorOrderList(Date date) {
        List<Order> lists = orderService.selectErrorOrderList(getCurrentShopId(), new Date());
        for(Order order:lists){
            if(order.getDistributionModeId()==2){
                order.setType(4);
            }
        }
        return getSuccessResult(lists);
    }

    @RequestMapping("/getPlatFormErrorOrder")
    public Result getPlatFormErrorOrder() {
        List<PlatformOrder> lists = platformOrderService.selectPlatFormErrorOrderList(getCurrentShopId(), new Date());
        for(PlatformOrder order : lists){
            order.setId(order.getPlatformOrderId());
        }
        return getSuccessResult(lists);
    }


    @RequestMapping("/listOrderNoPay")
    public Result listOrderNoPay(Date date) {
        List<Order> lists = orderService.getOrderNoPayList(getCurrentShopId(), new Date());
        return getSuccessResult(lists);
    }

    /**
     * 取消订单
     * orderID 如果是父订单，则取消父订单和所有子订单
     * orderID 如果是子订单，则只取消此订单
     * @param orderId
     * @return
     * @throws AppException
     */
    @RequestMapping("/cancelOrderPos")
    public Result cancelOrderPos(String orderId) throws AppException {
        try {
            //查询是否存在子订单
            Order orderFar = orderService.selectById(orderId);
//            if(MemcachedUtils.get(orderId + "WxPay") != null && orderFar.getOrderState() == OrderState.SUBMIT) {
            if(redisService.get(orderId + "WxPay") != null && orderFar.getOrderState() == OrderState.SUBMIT) {
                return new JSONResult(orderFar, "此订单正在微信支付，无法拒绝！", false);
            }
            List<Order> orders = orderService.selectByParentId(orderId, orderFar.getPayType());
            for (Order order : orders) {
                if (!order.getClosed()) {
                    orderService.cancelOrderPos(order.getId());//取消子订单
                }
            }
            orderService.cancelOrderPos(orderId);//取消父订单
        } catch (Exception e) {
            log.error("pos端拒绝订单失败！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult();
    }


    @RequestMapping("/confirmOrderPos")
    public Result confirmOrderPos(String orderId) throws AppException {
        orderService.confirmOrderPos(orderId);
        orderService.afterPayShareBenefits(orderId);
        return getSuccessResult();
    }


    @RequestMapping("/selectOrderByVercode")
    public Result selectOrderByVercode(String vercode) {
        List<Order> orderlist = orderService.selectOrderByVercode(vercode, getCurrentShopId());
        return getSuccessResult(orderlist);
    }

    @RequestMapping("/selectOrderStatesById")
    public Result selectOrderStatesById(String id) {
        Order order = orderService.selectOrderStatesById(id);
        return getSuccessResult(order);
    }

    @RequestMapping("/selectOrderByTableNumber")
    public Result selectOrderByTableNumber(String tableNumber) {
        List<Order> orderlist = orderService.selectOrderByTableNumber(tableNumber, getCurrentShopId());
        return getSuccessResult(orderlist);
    }

    @RequestMapping("/swtichMode")
    public Result swtichDistrubitionMode(Integer modeId, String orderId) {
        orderService.updateDistributionMode(modeId, orderId);
        return getSuccessResult();
    }

    @RequestMapping("/bindingTableNumber")
    public Result BindingTableNumberById(String orderId, String tableNumber) {
        orderService.setTableNumber(orderId, tableNumber);
        return getSuccessResult();
    }

    @RequestMapping("clearCallNumber")
    public Result clearCallNumber() {
        orderService.clearNumber(getCurrentShopId());
        return getSuccessResult();
    }

    @RequestMapping("listOrderByStatus")
    public Result listOrderByStatus() {
        Date date = new Date();
        Date begin = DateUtil.getDateBegin(date);
        Date end = DateUtil.getDateEnd(date);
        int[] productionStatus = new int[]{ProductionStatus.PRINTED, ProductionStatus.HAS_ORDER};
        int[] orderState = new int[]{OrderState.PAYMENT, OrderState.CONFIRM, OrderState.HASAPPRAISE};
        List<Order> list = orderService.listOrderByStatus(getCurrentShopId(), begin, end, productionStatus, orderState);
        return getSuccessResult(list);
    }

    @RequestMapping("getOrderItems")
    public Result getOrderItems(String orderId) {
        Map<String, String> param = new HashMap<>();
        param.put("orderId", orderId);
        List<OrderItem> orderItems = orderItemService.listByOrderId(param);
        for(OrderItem orderItem : orderItems){
            if(orderItem.getArticleName().indexOf("|_") > -1){
                orderItem.setArticleName(orderItem.getArticleName().replace("|_","-"));
            }
        }
        return getSuccessResult(orderItems);
    }

    @RequestMapping("getOrderItemsBySerialNumber")
    public Result getOrderItemsBySerialNumber(String serialNumber) {
        Order order = orderService.selectBySerialNumber(serialNumber);
        Map<String, String> param = new HashMap<>();
        param.put("orderId", order.getId());
        List<OrderItem> orderItems = orderItemService.listByOrderId(param);
        for(OrderItem orderItem : orderItems){
            if(orderItem.getArticleName().indexOf("|_") > -1){
                orderItem.setArticleName(orderItem.getArticleName().replace("|_","-"));
            }
            orderItem.setOrderId(order.getId());
            orderItem.setVerCode(order.getVerCode());
        }
        return getSuccessResult(orderItems);
    }

    @RequestMapping("getPrinters")
    public Result getPrinters(Integer printerType) {
        List<Printer> printers = printerService.selectByShopAndType(getCurrentShopId(), printerType);
        return getSuccessResult(printers);
    }


    @RequestMapping("/checkError")
    public Result checkError() {
        int result = printerService.checkError(getCurrentShopId());
        return new JSONResult<>(result);
    }


    @RequestMapping("printTotal")
    public Result printTotal(String beginDate, String endDate) {
        List<Map<String, Object>> printTask = orderService.printTotal(getCurrentShopId(), beginDate, endDate);
        //如果此时是在42服务器上使用1.0版本POS结店，则认为是实施同事在使用
        if ("139.196.222.42".equalsIgnoreCase(getLocalIP())){
            //遍历获取到的打印模板
            for (Map<String, Object> tasks : printTask){
                for (Map.Entry<String, Object> task : tasks.entrySet()){
                    if ("IP".equalsIgnoreCase(task.getKey())){
                        task.setValue("192.168.3.115");
                    }
                }
            }
        }
        return getSuccessResult(printTask);
    }

    @RequestMapping("printKitchenReceipt")
    public Result printKitchenReceipt(String orderId) {
        List<Map<String, Object>> printTask = orderService.printKitchenReceipt(orderId);
        return getSuccessResult(printTask);
    }

    @RequestMapping("printHungerKitchenReceipt")
    public Result printHungerKitchenReceipt(String orderId) {
        List<Map<String, Object>> printTask = thirdService.printKitchenReceipt(orderId);
        return getSuccessResult(printTask);
    }


    /**
     * pos端店铺收入报表 导出
     * 2016-10-29
     * @param beginDate
     * @param endDate
     * @param request
     * @param response
     */

    @RequestMapping("income_excel")
    @ResponseBody
    public void reportIncome(String beginDate, String endDate, HttpServletRequest request, HttpServletResponse response) {

        //导出文件名
        String fileName = "收入详情报表" + beginDate + "至" + endDate + ".xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[] columns = {"shopName", "totalIncome", "wechatIncome", "chargeAccountIncome", "redIncome", "couponIncome", "chargeGifAccountIncome", "waitNumberIncome"};
        //定义数据
        List<ShopIncomeDto> result = this.getResult(beginDate, endDate);
        Map<String, String> map = new HashMap<>();
        map.put("brandName", getCurrentBrand().getBrandName());
        map.put("shops", getCurrentShop().getName());
        map.put("beginDate", beginDate);
        map.put("reportType", "店铺收入报表");//表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "8");//显示的位置
        map.put("reportTitle", "店铺收入");//表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] headers = {{"店铺", "20"}, {"订单总额(元)", "16"}, {"微信支付收入(元)", "16"}, {"充值账户支付(元)", "17"},
                {"红包支付(元)", "16"}, {"优惠券支付(元)", "19"}, {"充值赠送支付(元)", "23"}, {"等位红包支付(元)", "23"}};
        //定义excel工具类对象
        ExcelUtil<ShopIncomeDto> excelUtil = new ExcelUtil<ShopIncomeDto>();
        try {
            OutputStream out = new FileOutputStream(path);
            excelUtil.ExportExcel(headers, columns, result, out, map);
            out.close();
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //店铺 菜品  销售报表

    @RequestMapping("article_excel")
    @ResponseBody
    public void reportArticle(String beginDate, String endDate, String selectValue, String sort, HttpServletRequest request, HttpServletResponse response) {
        //导出文件名
        String fileName = "菜品销售报表" + beginDate + "至" + endDate + ".xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[] columns = {"articleFamilyName", "articleName", "shopSellNum"};
        //定义数据
        List<ArticleSellDto> result = new LinkedList<>();
        //定义一个map用来存数据表格的前四项,1.报表类型,2.品牌名称3,.店铺名称4.日期
        Map<String, String> map = new HashMap<>();
        //获取店铺名称
        String shopId = getCurrentShopId();
        map.put("brandName", getCurrentBrand().getBrandName());
        map.put("shops", getCurrentShop()

                .getName());
        map.put("beginDate", beginDate);
        map.put("reportType", "菜品销售报表");//表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "2");//显示的位置
        map.put("reportTitle", "菜品销售");//表的名字
        map.put("timeType", "yyyy-MM-dd");

        //定义excel表格的表头
        if (selectValue == null || "".equals(selectValue)) {
            selectValue = "全部";
            result = orderService.selectShopArticleByDate(shopId, beginDate, endDate, sort);
        } else {
            //根据菜品分类的名称获取菜品分类的id
            String articleFamilyId = articleFamilyService.selectByName(selectValue);
            result = orderService.selectShopArticleByDateAndArcticleFamilyId(beginDate, endDate, shopId, articleFamilyId, sort);
        }
        String[][] headers = {{"菜品分类(" + selectValue + ")", "25"}, {"菜品名称", "25"}, {"菜品销量(份)", "25"}};

        //定义excel工具类对象
        ExcelUtil<ArticleSellDto> excelUtil = new ExcelUtil<ArticleSellDto>();
        try {
            OutputStream out = new FileOutputStream(path);
            excelUtil.ExportExcel(headers, columns, result, out, map);
            out.close();
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 得到 POS 中新列表订单中，未处理的订单
     * @return
     */
    @RequestMapping("getPosOrder")
    @ResponseBody
    public Result getPosOrder() {
        String[] orderStates = null;
        String[] productionStates = null;
        if (getCurrentShop().getShopMode() == 5) {//后付款模式,需要查询未支付的订单
            orderStates = new String[]{"1"};//【1：未支付】
        } else if (getCurrentShop().getShopMode() == 6 && getCurrentShop().getAllowFirstPay() == 0 && getCurrentShop().getAllowAfterPay() == 0) {
            orderStates = new String[]{"1", "2", "10", "11"};
        } else if (getCurrentShop().getShopMode() == 6 && getCurrentShop().getAllowFirstPay() == 1 && getCurrentShop().getAllowAfterPay() == 0) {
            orderStates = new String[]{"1"};
        } else if (getCurrentShop().getShopMode() == 6 && getCurrentShop().getAllowFirstPay() == 0 && getCurrentShop().getAllowAfterPay() == 1) {
            orderStates = new String[]{"2", "10", "11"};
        } else {
            orderStates = new String[]{"2", "10", "11"};//【2：已支付】
        }
        //productionStates = new String[]{"1","2"};//【1：已扫】  【2：已打印】
        productionStates = new String[]{"0", "1", "2"};//【2：已打印】
        List<Order> orders = orderService.selectByOrderSatesAndProductionStates(getCurrentShopId(), orderStates, productionStates);
        //新增订单里面  添加  加菜使用现金 银联 支付宝 闪惠 支付的订单  支付完毕之后消失
        List<Order> list = orderService.selectByOrderSatesAndNoPay(getCurrentShopId());
        if (list.size() != 0) {
            orders.addAll(list);
        }
        return getSuccessResult(orders);
    }

    /**
     * POS端确认订单支付【只有 后付款 模式调用】
     * @param oid
     * @return
     */
    @RequestMapping("confirmOrder")
    @ResponseBody
    public Result confirmOrder(String oid) {
        if (getCurrentShop().getShopMode() == 5) {//如果店铺模式不是 后付款模式，则无法调用
            orderService.payOrderModeFive(oid);
            return getSuccessResult();
        }
        return null;
    }


    @RequestMapping("getOrderById")
    @ResponseBody
    public Order getOrderById(String id) {
        Order order = orderService.selectById(id);
        if (order.getParentOrderId() == null) {
            return order;
        } else {
            return orderService.selectById(order.getParentOrderId());
        }
    }


    @RequestMapping("/payMoney")
    @ResponseBody
    public Result payMoney(String id) {
        Order order = orderService.selectById(id);
        if (order.getParentOrderId() == null) {
            orderService.payOrderModeFive(id);
        } else {
            orderService.payOrderModeFive(order.getParentOrderId());
        }

        return getSuccessResult();
    }


    /**
     * 结店操作
     * 将所有未消费的订单，进行退单处理
     * @return
     */
    @RequestMapping("cleanShopOrder")
    @ResponseBody
    public Result cleanShopOrder(@RequestBody OffLineOrder offLineOrder) {
        try {
            //判断结店日期是否是当前日期
            if (!DateUtil.isSameDate(offLineOrder.getCreateDate(), new Date())) {
                //如果不是今天的日期 （由于）

                String str = DateUtil.formatDate(offLineOrder.getCreateDate(), "yyyy-MM-dd");

                str = str + " " + DateUtil.getHourTme();

                offLineOrder.setCreateTime(DateUtil.fomatDate(str, "yyyy-MM-dd HH:mm:ss"));

            } else {
                offLineOrder.setCreateTime(new Date());
            }

            Result r = new Result(true);

            //存储线下订单
            Boolean flag = insertOffLineOrder(getCurrentBrand().getId(), getCurrentShopId(), offLineOrder);
            r.setMessage(offLineOrder.getId());
            return r;
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false);
        }
    }


    /**
     * 发送结店信息
     * @param offLineOrderId
     * @return
     */
    @RequestMapping("/sendCleanShopMessage")
    @ResponseBody
    public Result sendCleanShopMessage(){
        try{
            Result result = new Result(false);
            OffLineOrder offLineOrder = offLineOrderService.selectByTimeSourceAndShopId(OfflineOrderSource.OFFLINE_POS, getCurrentShopId(), DateUtil.getDateBegin(new Date()), DateUtil.getDateEnd(new Date()));
            Optional.ofNullable(offLineOrder).ifPresent((offLine) -> {
                result.setSuccess(true);
                closeShopService.cleanShopOrder(getCurrentShop(), offLineOrder, getCurrentBrand());
            });
            if (!result.isSuccess()) {
                result.setMessage("发送短信失败，当日线下营业数据未录入请重新录入后发送短信");
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            log.error("发送结店信息出错"  + e.getMessage());
            return new Result(false);
        }
    }

    @RequestMapping("/saveChange")
    @ResponseBody
    public Result saveChange(String orderId, Integer count, String orderItemId, Integer type) {
        Order order = orderService.selectById(orderId);
        String oid = order.getParentOrderId() == null ? order.getId() : order.getParentOrderId();
//        if(MemcachedUtils.get(oid + "WxPay") != null){
        if(redisService.get(oid + "WxPay") != null){
            return new JSONResult(order , "用户正在进行微信买单，不可编辑菜品！",false);
        }
        if(order.getPayMode() == OrderPayMode.XJ_PAY || order.getPayMode() == OrderPayMode.YL_PAY){
            return new JSONResult(order, "此订单正在等待pos支付确认，无法编辑菜品！", false);
        }
        // 编辑菜品 如果订单折扣过  则恢复未折扣的样子
        if(!oid.equals(orderId)){
            Order faOrder = orderService.selectById(oid);
            if(faOrder.getPosDiscount().compareTo(new BigDecimal(1)) != 0
                    || faOrder.getEraseMoney().compareTo(new BigDecimal(0)) != 0
                    || faOrder.getNoDiscountMoney().compareTo(new BigDecimal(0)) != 0){
                orderService.posDiscount(oid, new BigDecimal(1), new ArrayList<OrderItem>(), new BigDecimal(0), new BigDecimal(0), 0, new BigDecimal(0));
            }
        }else{
            if(order.getPosDiscount().compareTo(new BigDecimal(1)) != 0
                    || order.getEraseMoney().compareTo(new BigDecimal(0)) != 0
                    || order.getNoDiscountMoney().compareTo(new BigDecimal(0)) != 0){
                orderService.posDiscount(orderId, new BigDecimal(1), new ArrayList<OrderItem>(), new BigDecimal(0), new BigDecimal(0), 0, new BigDecimal(0));
            }
        }
        //编辑菜品
        Result result = orderService.updateOrderItem(orderId, count, orderItemId, type);
        if (result.isSuccess()) {
            if (StringUtils.isNotBlank(result.getMessage())) {
                List<Map<String, Object>> printTask = JSON.parseObject(result.getMessage(), new TypeReference<List<Map<String, Object>>>() {
                });
                return getSuccessResult(printTask);
            }
        }
        return result;
    }


    @RequestMapping("/refundOrder")
    @ResponseBody
    public Result refundOrder(@RequestBody Order order) {
        List<Map<String, Object>> printTask = new ArrayList<Map<String, Object>>();
        Order thisOrder = orderService.selectById(order.getId());
        if(thisOrder.getPosDiscount().compareTo(new BigDecimal(1)) != 0
                || thisOrder.getEraseMoney().compareTo(new BigDecimal(0)) != 0
                || thisOrder.getNoDiscountMoney().compareTo(new BigDecimal(0)) != 0){
            return new JSONResult(thisOrder , "折扣订单不可退菜！",false);
        }
        try {
            orderService.refundItem(order);
            orderService.refundArticleMsg(order);
            if (getCurrentShop().getPrintReceipt().equals(Common.YES)) {
                printTask.addAll(orderService.refundOrderPrintReceipt(order));
            }
            if (getCurrentShop().getPrintKitchen().equals(Common.YES)) {
                List<Map<String, Object>> list = orderService.refundOrderPrintKitChen(order);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> map = list.get(i);
                        printTask.add(map);
                    }
                }
            }

            Order refundOrder = orderService.getOrderInfo(order.getId());

            //判断订单是否是主订单   主订单是否参与  1:1消费返利活动
            if(refundOrder.getParentOrderId() == null){
                if(refundOrder.getIsConsumptionRebate() == 1){
                    updateAccountLog(refundOrder);
                }
            }else{
                Order faOrder = orderService.getOrderInfo(refundOrder.getParentOrderId());
                if(faOrder.getIsConsumptionRebate() == 1){
                    updateAccountLog(faOrder);
                }
            }

            //判断是否清空
            boolean flag = true;
            for (OrderItem item : refundOrder.getOrderItems()) {
                if (item.getCount() > 0) {
                    flag = false;
                }
            }
            if (flag) {
                if (refundOrder.getParentOrderId() == null) {
                    List<Order> orders = orderService.selectByParentId(refundOrder.getId(), 1); //得到子订单
                    if (orders.size() > 0) {
                        for (Order child : orders) { //遍历子订单
                            child = orderService.getOrderInfo(child.getId());
                            for (OrderItem item : child.getOrderItems()) {
                                if (item.getCount() > 0) {
                                    flag = false;
                                }
                            }
                        }
                        if (flag) {
                            refundOrderArticleNull(refundOrder);
                        }
                    } else {
                        refundOrderArticleNull(refundOrder);
                    }
                } else {
                    refundOrderArticleNull(refundOrder);
                }
                //存在退菜之后订单菜品是为负的情况  所以这边添加此校验  若为负则为零
                if(refundOrder.getArticleCount() != null && refundOrder.getArticleCount() < 0){
                    refundOrder.setArticleCount(0);
                }else if(refundOrder.getCountWithChild() != null && refundOrder.getCountWithChild() < 0){
                    refundOrder.setCountWithChild(0);
                }
                orderService.update(refundOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = "退菜失败";
            //如果报错信息不为空
            if (StringUtils.isBlank(e.getMessage()) && e instanceof InvocationTargetException){
                String exMsg = ((InvocationTargetException)e).getTargetException().getMessage();
                JSONObject object = JSON.parseObject(exMsg);
                if (object != null){
                    if (StringUtils.isNotBlank(object.getString("err_code_des"))) { //微信退款失败
                        errorMsg = object.getString("err_code_des");
                    }
                }
            }
            return new Result(errorMsg,false);
        }
        return getSuccessResult(printTask);
    }

    public void updateAccountLog(Order order){
        Customer customer = customerService.selectById(order.getCustomerId());
        AccountLog accountLog = accountLogService.selectByOrderId(order.getId());
        if(accountLog != null){
            BigDecimal money = (order.getAmountWithChildren().doubleValue() > 0 ? order.getAmountWithChildren() : order.getOrderMoney());
            if(money.compareTo(accountLog.getMoney()) != 0){
                BigDecimal cha = accountLog.getMoney().subtract(money);
                accountLog.setMoney(money);
                accountLogService.update(accountLog);
                Account account = accountService.selectById(customer.getAccountId());
                account.setFrozenRemain(account.getFrozenRemain().subtract(cha));
                accountService.update(account);
            }
        }
    }

    public void refundOrderArticleNull(Order refundOrder) {
        if (refundOrder.getServicePrice().doubleValue() <= 0) {
            refundOrder.setAllowAppraise(false);
            refundOrder.setAllowContinueOrder(false);
            refundOrder.setIsRefundOrder(true);
            refundOrder.setProductionStatus(ProductionStatus.REFUND_ARTICLE);

            //如果是多人订单的  则释放组（原因：订单菜品退光了）
            if(refundOrder.getParentOrderId() == null  && refundOrder.getGroupId() != null){
                TableGroup tableGroup = tableGroupService.selectByGroupId(refundOrder.getGroupId());
                tableGroup.setState(2);
                tableGroupService.update(tableGroup);
            }
        }
    }

    @RequestMapping("/updateReceiptOrderNumber")
    public Result updateReceiptOrderNumber(String serialNumber){
        log.info("进入更新发票信息,订单编号："+serialNumber);
        int count = receiptService.getReceiptOrderNumberCount(serialNumber);
        log.info("发票订单是否存在"+count);
        if(count>0){
            ReceiptOrder receiptOrder=receiptService.selectReceiptMoney(serialNumber);
            Receipt record=new Receipt();
            record.setReceiptMoney(receiptOrder.getReceiptMoney());
            record.setOrderNumber(serialNumber);
            record.setOrderMoney(receiptOrder.getOrderMoney());
            int ucount=receiptService.updateReceiptOrderNumber(record);
            log.info("更新记录条数"+ucount);
        }
        return getSuccessResult();
    }

    @RequestMapping("/checkOrder")
    @ResponseBody
    public Result checkOrder(@RequestBody Order order) {

        return new Result(orderService.checkOrder(order));
    }

    @RequestMapping("saveOrder")
    public Result order(@RequestBody Order order) throws AppException {
        order.setShopDetailId(getCurrentShopId());
        order.setBrandId(getCurrentBrandId());
        order.setCreateOrderByAddress("pos");
        if(order.getParentOrderId() != null){
//            if(MemcachedUtils.get(order.getParentOrderId() + "WxPay") != null){
            if(redisService.get(order.getParentOrderId() + "WxPay") != null){
                return new JSONResult(order , "此订单正在微信支付，请勿加菜！",false);
            }
        }
        if(order.getParentOrderId() != null && !"".equals(order.getParentOrderId())){
            // 加菜品 如果订单折扣过  则恢复未折扣的样子
            Order faOrder = orderService.selectById(order.getParentOrderId());
            if((faOrder.getPosDiscount().compareTo(new BigDecimal(1)) != 0
                    || faOrder.getEraseMoney().compareTo(new BigDecimal(0)) != 0
                    || faOrder.getNoDiscountMoney().compareTo(new BigDecimal(0)) != 0) && faOrder.getOrderState() == OrderState.SUBMIT){
                orderService.posDiscount(order.getParentOrderId(), new BigDecimal(1), new ArrayList<OrderItem>(), new BigDecimal(0), new BigDecimal(0), 0, new BigDecimal(0));
            }
        }
        JSONResult jsonResult = orderService.createOrder(order);
        order = (Order) jsonResult.getData();
        return jsonResult;
    }

    @RequestMapping("/payOrder")
    @ResponseBody
    public Result payOrder(String orderId, Integer payMode, String couponId, BigDecimal payValue, BigDecimal giveChange, BigDecimal remainValue, BigDecimal couponValue) {
        try {
            Order order = orderService.selectById(orderId);
            if(order.getOrderState() > OrderState.SUBMIT  && order.getOrderState() != OrderState.CANCEL){
                return new Result("该订单已进行过结算或已取消", false);
            }
//            if(MemcachedUtils.get(orderId + "WxPay") != null){
            if(redisService.get(orderId + "WxPay") != null){
                return new JSONResult(order , "此订单正在微信支付，请勿多次结算！",false);
            }

            if(couponId != null && !"".equals(couponId)){
                List<OrderPaymentItem> orderPaymentItems = orderPaymentItemService.selectByOrderId(orderId);
                for(OrderPaymentItem op : orderPaymentItems){
                    if(op.getPaymentModeId() == PayMode.COUPON_PAY){
                        return new JSONResult(order , "此订单已经存在优惠券的使用，请重新打开pos结算页面！",false);
                    }
                }
            }

            BigDecimal payMoney = payValue.add(giveChange.multiply(new BigDecimal(-1))).add(remainValue).add(couponValue);
            BigDecimal orderMoney = order.getAmountWithChildren().compareTo(BigDecimal.ZERO) > 0 ? order.getAmountWithChildren() : order.getPaymentAmount();
            //判断这个订单是否使用过优惠券
            BigDecimal useCouponValue = new BigDecimal(0);
            BigDecimal value = orderService.selectPayBefore(orderId);
            if(value != null && value.doubleValue() > 0){
                useCouponValue = value;
            }
            if(order.getAmountWithChildren().compareTo(BigDecimal.ZERO) > 0){
                orderMoney = orderMoney.subtract(useCouponValue);
            }
            if (payMoney.compareTo(orderMoney) < 0) {
                return new Result("订单金额发生变化，请关掉结算框重新结算", false);
            }
            orderService.posPayOrder(orderId, payMode, couponId, payValue == null ? BigDecimal.ZERO : payValue, giveChange, remainValue, couponValue);
            orderService.confirmOrder(order);
            return getSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("pos端结算后付模式未付款的订单出错！");
            return new Result(false);
        }
    }

    @RequestMapping("/posPayOrderGetCustomer")
    @ResponseBody
    public Result posPayOrderGetCustomer(String orderId) {
        Order order = orderService.selectById(orderId);
        List<OrderPaymentItem> orderPaymentItems = orderPaymentItemService.selectByOrderId(orderId);
        JSONObject resultObj = new JSONObject();
        BigDecimal payValue = order.getAmountWithChildren().compareTo(BigDecimal.ZERO) > 0 ? order.getAmountWithChildren() : order.getPaymentAmount();
        //判断这个订单是否使用过优惠券
        BigDecimal useCouponValue = new BigDecimal(0);
        BigDecimal value = orderService.selectPayBefore(orderId);
        if(value != null && value.doubleValue() > 0){
            useCouponValue = value;
        }
        if(order.getAmountWithChildren().compareTo(BigDecimal.ZERO) > 0){
            payValue = payValue.subtract(useCouponValue);
        }
        order.setOrderMoney(payValue);
        resultObj.put("order", order);
        BigDecimal couponValue = BigDecimal.ZERO;
        BigDecimal remainValue = BigDecimal.ZERO;
        Customer customer = customerService.selectById(order.getCustomerId());
        Account account = accountService.selectById(customer.getAccountId());
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("customerId", customer.getId());
        selectMap.put("shopId", order.getShopDetailId());
        selectMap.put("orderMoney", payValue);
        if (account.getRemain().compareTo(BigDecimal.ZERO) > 0) {
            selectMap.put("useWithAccount", 1);
        }
        if(useCouponValue.compareTo(BigDecimal.ZERO) == 0){
            Coupon coupon = couponService.selectPosPayOrderCanUseCoupon(selectMap);
            if (coupon != null) {
                payValue = payValue.subtract(coupon.getValue());
                couponValue = coupon.getValue();
                resultObj.put("couponId", coupon.getId());
            }
        }
        if (payValue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal oldRemain = account.getRemain();
            BigDecimal remain = oldRemain;
            if (remain.compareTo(BigDecimal.ZERO) > 0) {
                remain = remain.subtract(payValue);
                if (remain.compareTo(BigDecimal.ZERO) >= 0) {
                    remainValue = payValue;
                    payValue = BigDecimal.ZERO;
                } else {
                    remainValue = oldRemain;
                    payValue = remain.abs();
                }
            }
        } else {
            payValue = BigDecimal.ZERO;
        }
        resultObj.put("payValue", payValue);
        resultObj.put("remainValue", remainValue);
        resultObj.put("couponValue", couponValue);
        resultObj.put("useCouponValue", useCouponValue);
        return getSuccessResult(resultObj);
    }


    @RequestMapping("/checkErrorTask")
    @ResponseBody
    public synchronized Result checkErrorTask(String orderId, Integer type, String id) {
        Order order = orderService.selectById(orderId);
        Brand brand = brandService.selectById(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        WechatConfig config = brand.getWechatConfig();
        ArticleRecommend articleRecommend = articleRecommendService.selectById(id);
        Customer customer = customerService.selectById(order.getCustomerId());
        Boolean result = null;
        if (type == PrintType.TICKET) { //总单
//            if (order.getPrintFailFlag() == PrintStatus.UNPRINT) { //是0说明总单打印异常
            order.setPrintFailFlag(PrintStatus.PRINT_ERROR);
            orderService.update(order);
            result = false;
            Map logMap = new HashMap(4);
            logMap.put("brandName", brand.getBrandName());
            logMap.put("fileName", shopDetail.getName());
            logMap.put("type", "posAction");
            logMap.put("content", "订单:" + order.getId() + "，打印总单失败");
            doPostAnsc(logMap);
            if(order.getParentOrderId() != null){
                Order parent = orderService.selectById(order.getParentOrderId());
                parent.setPrintFailFlag(PrintStatus.PRINT_ERROR);
                orderService.update(parent);
            }
        } else if (type == PrintType.KITCHEN) { //厨打
            OrderItem item = orderItemService.selectById(id);
            if(order.getParentOrderId() != null){
                Order parent = orderService.selectById(order.getParentOrderId());
                parent.setPrintKitchenFlag(PrintStatus.PRINT_ERROR);
                orderService.update(parent);
            }
            if (item != null) {
//                if (item.getPrintFailFlag() == PrintStatus.UNPRINT) { //是0说明 打印异常
                item.setPrintFailFlag(PrintStatus.PRINT_ERROR);
                if (shopDetail.getPrintType() == 0 && item.getType() == OrderItemType.SETMEALS) { //如果是套餐总单出
                    List<OrderItem> childs = orderItemService.getListByParentId(item.getId());
                    for (OrderItem child : childs) {
                        child.setPrintFailFlag(PrintStatus.PRINT_ERROR);
                        orderItemService.update(child);
                    }
                }
                if (shopDetail.getPrintType() == 1 && item.getType() == OrderItemType.MEALS_CHILDREN) {
                    OrderItem parent = orderItemService.selectById(item.getParentId());
                    if (parent.getPrintFailFlag() != PrintStatus.PRINT_ERROR) {
                        parent.setPrintFailFlag(PrintStatus.PRINT_ERROR);
                        orderItemService.update(parent);
                    }
                }

                weChatService.sendCustomerMsg("尊敬的客户，您的" + item.getArticleName() + "自动下单失败，请速与服务员联系"
                        , customer.getWechatId(), config.getAppid(), config.getAppsecret());
                orderItemService.update(item);
                order.setPrintKitchenFlag(PrintStatus.PRINT_ERROR);
                orderService.update(order);
                Map logMap = new HashMap(4);
                logMap.put("brandName", brand.getBrandName());
                logMap.put("fileName", shopDetail.getName());
                logMap.put("type", "posAction");
                logMap.put("content", "订单:" + order.getId() + "，" + item.getArticleName() + "打印失败");
                doPostAnsc(logMap);
                result = false;
            } else if(articleRecommend != null){
                if(articleRecommend.getPrintType() == Dictionary.PRINT_RECOMMEND_All){ //推荐配餐整单出
                    List<OrderItem> orderItems = orderItemService.getListByRecommendId(id, order.getId());
                    for(OrderItem orderItem : orderItems){
                        orderItem.setPrintFailFlag(PrintStatus.PRINT_ERROR);
                        orderItemService.update(orderItem);
                        weChatService.sendCustomerMsg("尊敬的客户，您的" + orderItem.getArticleName() + "自动下单失败，请速与服务员联系"
                                , customer.getWechatId(), config.getAppid(), config.getAppsecret());
                        Map logMap = new HashMap(4);
                        logMap.put("brandName", brand.getBrandName());
                        logMap.put("fileName", shopDetail.getName());
                        logMap.put("type", "posAction");
                        logMap.put("content", "订单:" + order.getId() + "，" + orderItem.getArticleName() + "打印失败");
                        doPostAnsc(logMap);
                    }
                }
            }

        }


        JSONResult json = new JSONResult();
        json.setSuccess(result);
        return json;
    }


    @RequestMapping("/printOk")
    @ResponseBody
    public synchronized Result printAgain(String orderId, Integer type, String id) {
        String message = "";
        Order order = orderService.selectById(orderId);
        Order parent = orderService.selectById(order.getParentOrderId());
        if((order.getPrintFailFlag() == PrintStatus.PRINT_SUCCESS || order.getPrintFailFlag() == PrintStatus.PRINT_FIXED)
                && (order.getPrintKitchenFlag() == PrintStatus.PRINT_FIXED || order.getPrintKitchenFlag() == PrintStatus.PRINT_SUCCESS)
                ){
            return new Result(false);
        }
        Brand brand = brandService.selectById(order.getBrandId());
        ArticleRecommend articleRecommend = articleRecommendService.selectById(id);
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        Customer customer = customerService.selectById(order.getCustomerId());
        OrderItem orderItem = orderItemService.selectById(id);
        WechatConfig config = brand.getWechatConfig();
        if (type == PrintType.TICKET) {//总单
            if (order.getPrintFailFlag() == PrintStatus.PRINT_ERROR) {
                order.setPrintFailFlag(PrintStatus.PRINT_FIXED);
                Map logMap = new HashMap(4);
                logMap.put("brandName", brand.getBrandName());
                logMap.put("fileName", shopDetail.getName());
                logMap.put("type", "posAction");
                logMap.put("content", "订单:" + order.getId() + "，打印总单从失败变为成功");
                doPostAnsc(logMap);
            } else {
                order.setPrintFailFlag(PrintStatus.PRINT_SUCCESS);
            }
            orderService.update(order);

            message = "ticket";
            List<Order> childs = orderService.selectListByParentId(orderId);
            if(!CollectionUtils.isEmpty(childs)){
                for(Order child : childs){
                    child.setPrintFailFlag(child.getPrintFailFlag() == PrintStatus.PRINT_ERROR ? PrintStatus.PRINT_FIXED : PrintStatus.PRINT_SUCCESS);
                    orderService.update(child);
                }
            }


            if (order.getPrintKitchenFlag() == PrintStatus.PRINT_FIXED || order.getPrintKitchenFlag() == PrintStatus.PRINT_SUCCESS) {
                message = "all";
            }
        } else if (type == PrintType.KITCHEN) { //厨打
            if (orderItem != null) {
                if (orderItem.getPrintFailFlag() == PrintStatus.PRINT_ERROR) {
                    orderItem.setPrintFailFlag(PrintStatus.PRINT_FIXED);
                    weChatService.sendCustomerMsg("尊敬的客户，您的" + orderItem.getArticleName() + "已下单成功，请耐心等待"
                            , customer.getWechatId(), config.getAppid(), config.getAppsecret());
                    Map logMap = new HashMap(4);
                    logMap.put("brandName", brand.getBrandName());
                    logMap.put("fileName", shopDetail.getName());
                    logMap.put("type", "posAction");
                    logMap.put("content", "订单:" + order.getId() + "，打印厨打从失败变为成功");
                    doPostAnsc(logMap);
                } else {
                    orderItem.setPrintFailFlag(PrintStatus.PRINT_SUCCESS);
                }
                orderItemService.update(orderItem);
                if (shopDetail.getPrintType() == 0 && orderItem.getType() == OrderItemType.SETMEALS) { //如果是套餐总单出
                    List<OrderItem> childs = orderItemService.getListByParentId(orderItem.getId());
                    for (OrderItem child : childs) {
                        child.setPrintFailFlag(orderItem.getPrintFailFlag() == PrintStatus.PRINT_ERROR ? PrintStatus.PRINT_FIXED : PrintStatus.PRINT_SUCCESS);
                        orderItemService.update(child);
                    }
                }
                if (shopDetail.getPrintType() == 1 && orderItem.getType() == OrderItemType.MEALS_CHILDREN) {
                    List<OrderItem> childs = orderItemService.getListByParentId(orderItem.getParentId());
                    Boolean check = true;
                    for (OrderItem child : childs) {
                        if ((child.getPrintFailFlag() != PrintStatus.PRINT_FIXED && child.getPrintFailFlag() != PrintStatus.PRINT_SUCCESS)
                                && !child.getId().equals(id)) {
                            check = false;
                        }
                    }
                    if (check) {
                        OrderItem p = orderItemService.selectById(orderItem.getParentId());
                        p.setPrintFailFlag(orderItem.getPrintFailFlag() == PrintStatus.PRINT_ERROR ? PrintStatus.PRINT_FIXED : PrintStatus.PRINT_SUCCESS);
                        orderItemService.update(p);
                    }
                }
            }else if (articleRecommend != null){
                if(articleRecommend.getPrintType() == Dictionary.PRINT_RECOMMEND_All){
                    List<OrderItem> orderItems = orderItemService.getListByRecommendId(id, order.getId());
                    for(OrderItem item : orderItems){
                        item.setPrintFailFlag(item.getPrintFailFlag() == PrintStatus.PRINT_ERROR ? PrintStatus.PRINT_FIXED : PrintStatus.PRINT_SUCCESS);
                        orderItemService.update(item);
                        weChatService.sendCustomerMsg("尊敬的客户，您的" + item.getArticleName() + "已下单成功，请耐心等待"
                                , customer.getWechatId(), config.getAppid(), config.getAppsecret());
                        Map logMap = new HashMap(4);
                        logMap.put("brandName", brand.getBrandName());
                        logMap.put("fileName", shopDetail.getName());
                        logMap.put("type", "posAction");
                        logMap.put("content", "订单:" + order.getId() +  "，打印厨打从失败变为成功");
                        doPostAnsc(logMap);
                    }
                }
            }
            Map<String, String> param = new HashMap<>();
            param.put("orderId", orderId);
            List<OrderItem> orderItems = orderItemService.listByOrderId(param);
            Boolean allPrint = true;
            for (OrderItem item : orderItems) {
                if ((item.getPrintFailFlag() != PrintStatus.PRINT_SUCCESS && item.getPrintFailFlag() != PrintStatus.PRINT_FIXED) && !item.getId().equals(id)) {
                    allPrint = false;
                }
            }
            if (allPrint) {
                if (order.getPrintKitchenFlag() == PrintStatus.PRINT_ERROR) {
                    order.setPrintKitchenFlag(PrintStatus.PRINT_FIXED);
                } else {
                    order.setPrintKitchenFlag(PrintStatus.PRINT_SUCCESS);
                }

                orderService.update(order);
                if (order.getPrintFailFlag() == PrintStatus.PRINT_SUCCESS || order.getPrintFailFlag() == PrintStatus.PRINT_FIXED) {
                    message = "all";
                } else {
                    message = "kitchen";
                }


                if(parent != null){
                    List<Order> childs = orderService.selectListByParentId(order.getParentOrderId());
                    Boolean check = true;
                    for(Order child : childs){
                        Map<String, String> params = new HashMap<>();
                        param.put("orderId", child.getId());
                        List<OrderItem> items = orderItemService.listByOrderId(param);
                        for(OrderItem item : items){
                            if(item.getPrintFailFlag() == PrintStatus.UNPRINT || item.getPrintFailFlag() == PrintStatus.PRINT_ERROR){
                                check = false;
                            }
                        }
                    }
                    if(check){
                        parent.setPrintKitchenFlag(parent.getPrintFailFlag() == PrintStatus.PRINT_ERROR ? PrintStatus.PRINT_FIXED : PrintStatus.PRINT_SUCCESS);
                        orderService.update(parent);
                    }
                }


            }

        }
        return new Result(message, true);
    }

    /**
     * 催单打印菜品
     * @param orderItemIds
     * @param orderId
     * @return
     */
    @RequestMapping("/reminder")
    @ResponseBody
    public Result reminder(String orderItemIds, String orderId){
        try{
            List<Map<String, Object>> list = orderService.reminder(orderItemIds, orderId);
            return getSuccessResult(list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false);
        }
    }

    /**
     * 获取所有开启的退菜原因
     * @return
     */
    @RequestMapping("/getRefundRemarkList")
    public Result getRefundRemarkList() {
        List<RefundRemark> refundRemarks = refundRemarkService.selectList();
        return getSuccessResult(refundRemarks);
    }

    @RequestMapping("/badAppraisePrintOrder")
    @ResponseBody
    public Result badAppraisePrintOrder(String orderId){
        try{
            //得到打印模板
            List<Map<String, Object>> printTask = orderService.badAppraisePrintOrder(orderId);
            log.info("得到差评打印模板：" + printTask.toString());
            return getSuccessResult(printTask);
        }catch (Exception e){
            e.printStackTrace();
            log.error("差评打印订单获取打印模板出错" + e.getMessage());
            return new Result("差评打印订单获取打印模板出错",false);
        }
    }
    /**
     * 换桌打印
     * @return
     */
    @RequestMapping("/turntable")
    @ResponseBody
    public Result turntable(String id,String brandId,String tableNumber,String serialNumber,String createTime,String shopDetailId,String oldtableNumber){
        try{
            Order order=new Order();
            order.setId(id);
            order.setBrandId(brandId);
            order.setTableNumber(tableNumber);
            order.setSerialNumber(serialNumber);
            Long sL = Long.parseLong(createTime);
            Date a = new Date(sL);
            order.setCreateTime(a);
            order.setShopDetailId(shopDetailId);
            List<Map<String, Object>> list = orderService.printTurnTable(order,oldtableNumber);
            return getSuccessResult(list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false);
        }
    }

    /**
     * 更新换桌的桌号
     * @return
     */
    @RequestMapping("/updateTurnTable")
    public Result updateTurnTable(String tableNumber,String order_id) {
        String message="";
        Order parent=new Order();
        parent.setId(order_id);
        parent.setTableNumber(tableNumber);
        int count=orderService.update(parent);
        if(count==0){
            message="换桌失败";
        }else{
            message="换桌成功";
        }
        return new Result(message, true);
    }

    /**
     * 发票打印
     * @return
     */
    @RequestMapping("/receiptOrderPrints")
    @ResponseBody
    public Result receiptOrderPrints(String receiptId){
        try{
            List<Map<String, Object>> list = receiptService.printReceiptOrder(receiptId,getCurrentShopId());
            return getSuccessResult(list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false);
        }
    }
    /**
     * pos自动发票打印
     * @return
     */
    @RequestMapping("/receiptOrderPosPrints")
    @ResponseBody
    public Result receiptOrderPosPrints(String orderNumber){
        try{
            List<Map<String, Object>> list = receiptService.printReceiptPosOrder(orderNumber,getCurrentShopId());
            return getSuccessResult(list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false);
        }
    }

    /**
     * 发票开关变更是否开具
     * @return
     */
    @RequestMapping("/updateReceiptState")
    public Result updateReceiptState(String receiptId,String state) {
        String message="";
        Receipt receipt=new Receipt();
        receipt.setId(Long.valueOf(receiptId));
        receipt.setState(Integer.parseInt(state));
        receipt.setCreateTime(new java.sql.Date(System.currentTimeMillis()));
        int count=receiptService.updateState(receipt);
        if(count==0){
            message="变更发票状态失败";
        }else{
            message="变更发票状态成功";
        }
        return new Result(message, true);
    }

    @RequestMapping("posDisCount")
    public Result posDisCount(String orderId, BigDecimal discount, BigDecimal eraseMoney, BigDecimal noDiscountMoney, Integer type, BigDecimal orderPosDiscountMoney) throws AppException {
        Order order = orderService.selectById(orderId);
//        if(MemcachedUtils.get(orderId + "WxPay") != null) {
        if(redisService.get(orderId + "WxPay") != null) {
            return new JSONResult(order, "此订单正在微信支付，无法打折！", false);
        }
        if(order.getPayMode() == OrderPayMode.XJ_PAY || order.getPayMode() == OrderPayMode.YL_PAY){
            return new JSONResult(order, "此订单正在等待pos支付确认，无法打折！", false);
        }
        order = orderService.posDiscount(orderId, discount, new ArrayList<OrderItem>(), eraseMoney, noDiscountMoney, type, orderPosDiscountMoney);
        Customer customer = customerService.selectById(order.getCustomerId());
        Brand brand = brandService.selectByPrimaryKey(order.getBrandId());
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", customer.getId());
        map.put("type", "UserAction");
        map.put("content", "用户:" + customer.getNickname() + "的订单id为：" + orderId + "被折扣。\n折扣率:" + discount + "\n折扣金额:" + eraseMoney + "\n不参与折扣的金额:" + noDiscountMoney + "\n请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        return getSuccessResult();
    }

    private Boolean insertOffLineOrder(String brandId, String shopId,OffLineOrder offLineOrder) {
        Boolean flag = false;
        Date cleanDate = offLineOrder.getCreateTime();
        OffLineOrder offLineOrder1 = offLineOrderService.selectByTimeSourceAndShopId(OfflineOrderSource.OFFLINE_POS, shopId, DateUtil.getDateBegin(cleanDate), DateUtil.getDateEnd(cleanDate));
        if (null != offLineOrder1) {
            offLineOrder1.setState(Common.NO);
            offLineOrderService.update(offLineOrder1);
        }

        offLineOrder.setCreateTime(cleanDate);
        offLineOrder.setCreateDate(cleanDate);
        offLineOrder.setId(ApplicationUtils.randomUUID());
        offLineOrder.setState(Common.YES);
        offLineOrder.setResource(OfflineOrderSource.OFFLINE_POS);
        offLineOrder.setBrandId(brandId);
        offLineOrder.setShopDetailId(shopId);
        int temp = offLineOrderService.insert(offLineOrder);
        if(temp > 0){
            flag = true;
        }
        return flag;
    }
}
