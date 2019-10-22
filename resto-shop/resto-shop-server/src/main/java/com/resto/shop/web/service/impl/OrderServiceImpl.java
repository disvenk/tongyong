package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.service.NewAppraiseService;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.enums.BehaviorType;
import com.resto.brand.core.enums.DetailType;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.*;
import com.resto.brand.web.dto.*;
import com.resto.brand.web.model.*;
import com.resto.brand.web.model.TableQrcode;
import com.resto.brand.web.service.*;
import com.resto.brand.web.service.TableQrcodeService;
import com.resto.shop.web.constant.*;
import com.resto.shop.web.container.OrderProductionStateContainer;
import com.resto.shop.web.dao.*;
import com.resto.shop.web.datasource.DataSourceContextHolder;
import com.resto.shop.web.dto.EnabelTicketOrderDto;
import com.resto.shop.web.dto.OrderNumDto;
import com.resto.shop.web.dto.Summarry;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.*;
import com.resto.shop.web.model.Area;
import com.resto.shop.web.model.Employee;
import com.resto.shop.web.producer.MQMessageProducer;
import com.resto.shop.web.producer.MQTTMQMessageProducer;
import com.resto.shop.web.report.MealAttrMapperReport;
import com.resto.shop.web.report.OrderMapperReport;
import com.resto.shop.web.service.*;
import com.resto.shop.web.service.AreaService;
import com.resto.shop.web.util.BrandAccountSendUtil;
import com.resto.shop.web.util.DateTimeUtils;
import com.resto.shop.web.util.LogTemplateUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;
import static com.resto.brand.core.util.OrderCountUtils.getOrderMoney;

/**
 *
 */
@Component
@Service
public class OrderServiceImpl extends GenericServiceImpl<Order, String> implements OrderService {

    //用来添加打印小票的序号
    //添加两个Map 一个是订单纬度,一个是店铺纬度
    private static final Map<String, Map<String, Integer>> NUMBER_ORDER_MAP = new ConcurrentHashMap<>();

    private static final Map<String, Map<String, Integer>> NUMBER_SHOP_MAP = new ConcurrentHashMap<>();

    private static final String NUMBER = "0123456789";

    @Autowired
    CustomerGroupService customerGroupService;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderMapperReport orderMapperReport;

    @Resource
    private MealAttrMapperReport mealAttrMapperReport;

    @Resource
    private OrderItemMapper orderitemMapper;

    @Resource
    private RedPacketService redPacketService;

    @Autowired
    BrandTemplateEditService brandTemplateEditService;

    @Resource
    private CustomerService customerService;

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticlePriceService articlePriceService;

    @Value("#{configProperties2['logPath']}")
    public static String logPath;

    @Resource
    private CouponService couponService;

    @Autowired
    private OrderBeforeService orderBeforeService;

    @Resource
    OrderPaymentItemService orderPaymentItemService;

    @Resource
    private AccountService accountService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    ShopCartService shopCartService;

    @Resource
    WechatConfigService wechatConfigService;

    @Resource
    OrderProductionStateContainer orderProductionStateContainer;

    @Resource
    BrandSettingService brandSettingService;

    @Resource
    BrandService brandService;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    KitchenService kitchenService;

    @Resource
    PrinterService printerService;

    @Autowired
    NewAppraiseService newAppraiseService;

    @Resource
    MealItemService mealItemService;

    @Resource
    ChargeOrderService chargeOrderService;

    @Resource
    MealAttrMapper mealAttrMapper;

    @Resource
    ArticlePriceMapper articlePriceMapper;

    @Resource
    private ArticleFamilyMapper articleFamilyMapper;

    @Autowired
    private GetNumberService getNumberService;

    @Autowired
    private CustomerDetailMapper customerDetailMapper;

    @Resource
    private OrderRefundRemarkMapper orderRefundRemarkMapper;

    @Autowired
    private ReceiptMapper receiptMapper;

    @Resource
    private BrandAccountLogService brandAccountLogService;

    @Override
    public GenericDao<Order, String> getDao() {
        return orderMapper;
    }

    @Autowired
    ArticleRecommendMapper articleRecommendMapper;

    @Autowired
    WxServerConfigService wxServerConfigService;

    @Autowired
    AccountLogService accountLogService;

    @Autowired
    OffLineOrderMapper offLineOrderMapper;

    @Autowired
    VirtualProductsService virtualProductsService;

    @Autowired
    private TableQrcodeService tableQrcodeService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private SmsLogService smsLogService;

    @Resource
    private AccountSettingService accountSettingService;

    @Resource
    private BrandAccountService brandAccountService;

    @Resource
    private AccountNoticeService accountNoticeService;

    @Resource
    TemplateService templateService;

    @Resource
    private ParticipantService participantService;

    @Resource
    private TableGroupService tableGroupService;

    @Resource
    NewCustomCouponService newCustomCouponService;



    @Autowired
    RedisService redisService;

    @Autowired
    MQMessageProducer mqMessageProducer;

    @Autowired
    MQTTMQMessageProducer mqttMQMessageProducer;

    @Autowired
    WeChatService weChatService;

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public List<Order> listOrder(Integer start, Integer datalength, String shopId, String customerId, String orderState) {
        String[] states = null;
        if (orderState != null) {
            states = orderState.split(",");
        }
        return orderMapper.orderList(start, datalength, shopId, customerId, states);
    }

    @Override
    public List<Order> selectOrderSuccess(Date beginTime, Date endTime, String shopId) {

        return orderMapper.selectOrderSuccess(beginTime,endTime,shopId);
    }

    @Override
    public Order selectOrderStatesById(String orderId) {
        return orderMapper.selectOrderStatesById(orderId);
    }

    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
        }
        return sb.toString();
    }


    @Override
    public JSONResult repayOrder(Order order) throws AppException {
        JSONResult jsonResult = new JSONResult();
        Order old = orderMapper.selectByPrimaryKey(order.getId());

        if (old.getServicePrice() == null) {
            old.setServicePrice(new BigDecimal(0));
        }

        if (old.getMealFeePrice() == null) {
            old.setMealFeePrice(new BigDecimal(0));
        }

        //先获取菜品金额
        BigDecimal articleTotalPirce = old.getPaymentAmount().subtract(old.getServicePrice()).subtract(old.getMealFeePrice());

        //重新计算这单子的现有价格
        if (order.getServicePrice() == null) {
            old.setServicePrice(new BigDecimal(0));
        } else {
            old.setServicePrice(order.getServicePrice());
        }
        if (order.getMealFeePrice() == null) {
            old.setMealFeePrice(new BigDecimal(0));
        } else {
            old.setMealFeePrice(order.getMealFeePrice());
        }

        //重新计算订单价格
        BigDecimal orderMoney = articleTotalPirce.add(old.getMealFeePrice()).add(old.getServicePrice());

        old.setOriginalAmount(orderMoney);
        old.setOrderMoney(orderMoney);

        //计算订单应付金额
        BigDecimal payment = orderMoney;


        // 等位红包
        ShopDetail detail = shopDetailService.selectById(old.getShopDetailId());
        if (order.getWaitMoney().doubleValue() > 0) {
            OrderPaymentItem item = new OrderPaymentItem();
            item.setId(ApplicationUtils.randomUUID());
            item.setOrderId(order.getId());
            item.setPaymentModeId(PayMode.WAIT_MONEY);
            item.setPayTime(order.getCreateTime());
            item.setPayValue(order.getWaitMoney());
            item.setRemark("等位红包支付:" + order.getWaitMoney());
//            item.setResultData(order.getWaitId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
            item.setToPayId(order.getWaitId());
            orderPaymentItemService.insert(item);

            GetNumber getNumber = getNumberService.selectById(order.getWaitId());
            getNumber.setState(WaitModerState.WAIT_MODEL_NUMBER_THREE);
            getNumberService.update(getNumber);

            payment.subtract(order.getWaitMoney());
        }
        Customer customer = customerService.selectById(old.getCustomerId());


        if (detail.getShopMode() != ShopMode.HOUFU_ORDER) {
            if (order.getUseCoupon() != null) {
                Coupon coupon = couponService.useCoupon(orderMoney, old);
                OrderPaymentItem item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(order.getId());
                item.setPaymentModeId(PayMode.COUPON_PAY);
                item.setPayTime(order.getCreateTime());
                item.setPayValue(coupon.getValue());
                item.setRemark("优惠券支付:" + item.getPayValue());
//                item.setResultData(coupon.getId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
                item.setToPayId(coupon.getId());
                orderPaymentItemService.insert(item);
                payment = payment.subtract(item.getPayValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            // 使用余额
            if (payment.doubleValue() > 0 && order.isUseAccount()) {
                BigDecimal payValue = accountService.payOrder(old, payment, customer, null, null);
//			    BigDecimal payValue = accountService.useAccount(payMoney, account,AccountLog.SOURCE_PAYMENT);
                if (payValue.doubleValue() > 0) {
                    payment = payment.subtract(payValue.setScale(2, BigDecimal.ROUND_HALF_UP));

                }
            }
        }

        if (payment.doubleValue() < 0) {
            payment = BigDecimal.ZERO;
        }
        old.setPaymentAmount(payment);

        if (old.getPaymentAmount().doubleValue() == 0) {
            payOrderSuccess(old);
        }
        update(old);
        jsonResult.setSuccess(Boolean.TRUE);
        jsonResult.setData(old);
        if (old.getOrderMode() == ShopMode.HOUFU_ORDER) {
            if (old.getParentOrderId() != null) {  //子订单
                Order parent = selectById(old.getParentOrderId());
                int articleCountWithChildren = selectArticleCountById(parent.getId(), old.getOrderMode());
                if (parent.getLastOrderTime() == null || parent.getLastOrderTime().getTime() < old.getCreateTime().getTime()) {
                    parent.setLastOrderTime(old.getCreateTime());
                }
                Double amountWithChildren = orderMapper.selectParentAmount(parent.getId(), parent.getOrderMode());
                parent.setCountWithChild(articleCountWithChildren);
                parent.setAmountWithChildren(new BigDecimal(amountWithChildren));
                update(parent);

            }
        }
        return jsonResult;
    }

    /**
     * 计算菜品折扣
     *
     * @param price       价格
     * @param discount    当前菜品的折扣
     * @param wxdiscount  微信前端传入的折扣
     * @param articleName 菜品名称
     * @return
     * @throws AppException
     */
    private BigDecimal discount(BigDecimal price, int discount, int wxdiscount, String articleName) throws AppException {
        if (price != null) {
            if (discount != wxdiscount) {
                //折扣不匹配
                throw new AppException(AppException.DISCOUNT_TIMEOUT, articleName + "折扣活动已结束，请重新选购餐品~");
            }
            return price.multiply(new BigDecimal(discount)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return price;
        }
    }

    /**
     * @Author: KONATA
     * @Description
     * @Date: 15:42 2017/3/30
     */
    @Override
    public JSONResult createOrder(Order order) throws AppException {

        log.info(">>>>>>>>>>>>>>>>>>>>>>进入createOrder方法--->同步机制>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        JSONResult jsonResult = new JSONResult();
        String orderId = ApplicationUtils.randomUUID();
        order.setId(orderId);
        order.setPosDiscount(new BigDecimal(1));
        if (order.getMemberDiscount() == null) {
            order.setMemberDiscount(new BigDecimal(1));
        }
        Customer customer = customerService.selectById(order.getCustomerId());
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
//        Boolean loginFlag = (Boolean) redisService.get(order.getShopDetailId() + "loginStatus");
        Boolean loginFlag = (Boolean) redisService.get(order.getShopDetailId() + "loginStatus");
        if (shopDetail.getPosVersion() == PosVersion.VERSION_2_0) {
            if (loginFlag == null || loginFlag == false) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("当前店铺暂未开启在线点餐，请联系服务员详询，谢谢");
                return jsonResult;
            }
            Boolean checkTable = (Boolean) redisService.get(order.getShopDetailId() + order.getTableNumber() + "status");
            if(checkTable != null && !checkTable){
                //获取桌号最后一笔主订单
                Order tableOrder = orderMapper.selectLastOrderByTableNumber(order.getShopDetailId(), order.getTableNumber());
                //如果点单取消了或者退光了
                if(tableOrder == null || tableOrder.getOrderState() == OrderState.CANCEL || tableOrder.getProductionStatus() == ProductionStatus.REFUND_ARTICLE){
                    checkTable = null;
                    redisService.remove(order.getShopDetailId() + order.getTableNumber() + "status");
                }
            }
            if (checkTable != null && !checkTable && shopDetail.getAllowAfterPay() == 0 && shopDetail.getAllowFirstPay() == 1
                    && order.getParentOrderId() == null) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("该桌位已存在订单，请重新扫码获取订单");
                return jsonResult;
            }
        }


        if (!StringUtils.isEmpty(order.getGroupId())) {
            log.info("多人点餐");
            TableGroup tableGroup = tableGroupService.selectByGroupId(order.getGroupId());
            if (tableGroup.getState() == TableGroup.FINISH) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("该组已经释放，请重新选择桌位！");
                return jsonResult;
            }
            Boolean bool = (Boolean) redisService.get(order.getCustomerId() + order.getGroupId());
            if (!bool) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("万分抱歉，菜品发生变动，请重新获取~");
                return jsonResult;
            }

            //如果这个订单已经被组里的人买单了，那么其他人不能在
            if (redisService.get(order.getShopDetailId() + order.getGroupId()) != null) {
//                String customerId = String.valueOf(redisService.get(order.getGroupId() + "pay"));
                String customerId = String.valueOf(redisService.get(order.getGroupId() + "pay"));
                if (!customer.getId().equals(customerId)) {
                    jsonResult.setSuccess(false);
                    Customer payer = customerService.selectById(customerId);
                    jsonResult.setMessage("该订单正在被" + payer.getNickname() + "支付中，请勿重复买单！");
                    return jsonResult;
                }
            } else {
//                redisService.set(order.getGroupId() + "pay", customer.getId());
//                redisService.set(order.getShopDetailId() + order.getGroupId(), 1, 30l);
                redisService.set(order.getGroupId() + "pay", customer.getId());
                redisService.set(order.getShopDetailId() + order.getGroupId(), 1, 30l);
            }
        }

        if (customer != null) {
            if ("15618232089".equals(customer.getTelephone()) || "11111111111".equals(customer.getTelephone())) {
                //  如果是英杰的账号，则不进行 此验证 , 可使用 此手机号 进行并发下单测试   -lmx
//            } else if (!MemcachedUtils.add(customer.getId() + "createOrder", 1, 15)) {
//            } else if (!redisService.setnxTime(customer.getId() + "createOrder", 1, 15L)) {
            } else if (!redisService.setnxTime(customer.getId() + "createOrder", 1, 15)) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("下单过于频繁，请稍后再试！");
                return jsonResult;
            }
        }

        if (customer == null && "wechat".equals(order.getCreateOrderByAddress())) {
            throw new AppException(AppException.CUSTOMER_NOT_EXISTS);
        } else if (order.getOrderItems().isEmpty()) {
            throw new AppException(AppException.ORDER_ITEMS_EMPTY);
        }

        if (!StringUtils.isEmpty(order.getTableNumber()) && order.getTableNumber().length() > 5) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("桌号异常,请扫码正确的二维码！");
            return jsonResult;
        }

        Brand brand = brandService.selectById(order.getBrandId());
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
        if (order.getOrderItems().isEmpty()) {
            throw new AppException(AppException.ORDER_ITEMS_EMPTY);
        }

        if (order.getOrderBefore() == null && brandSetting.getIsUseServicePrice().equals(Common.YES) && shopDetail.getIsUseServicePrice().equals(Common.YES)
                && (order.getCustomerCount() == null || order.getCustomerCount() == 0)
                && (order.getParentOrderId() == null || "".equals(order.getParentOrderId()))
                && order.getDistributionModeId() == DistributionType.RESTAURANT_MODE_ID) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("请输入就餐人数！");
            return jsonResult;
        }

        if (!StringUtils.isEmpty(order.getTableNumber())) { //如果存在桌号
            int orderCount = orderMapper.checkTableNumber(order.getShopDetailId(), order.getTableNumber(), order.getCustomerId(), brandSetting.getCloseContinueTime());
            if (orderCount > 0 && StringUtils.isEmpty(order.getGroupId())) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("不好意思，这桌有人了");
                return jsonResult;
            }
        } else if ((order.getDistributionModeId() == 3 && shopDetail.getContinueOrderScan() == 1 && StringUtils.isEmpty(order.getTableNumber()) && shopDetail.getShopMode() == ShopMode.BOSS_ORDER)
                || order.getDistributionModeId() == 1 && StringUtils.isEmpty(order.getTableNumber()) && shopDetail.getShopMode() == ShopMode.BOSS_ORDER) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("桌号不得为空");
            return jsonResult;
        }
        if (!StringUtils.isEmpty(order.getParentOrderId())) {  //如果是加菜订单
            Order farOrder = orderMapper.selectByPrimaryKey(order.getParentOrderId());
            if(farOrder.getOrderMoney().doubleValue() == 0){
                jsonResult.setSuccess(false);
                jsonResult.setMessage("订单已发生变化，请重新扫码点餐");
                return jsonResult;
            }
            if (farOrder.getOrderState() == OrderState.SUBMIT && (farOrder.getPayMode() == OrderPayMode.YL_PAY || farOrder.getPayMode() == OrderPayMode.XJ_PAY ||
                    farOrder.getPayMode() == OrderPayMode.SHH_PAY || farOrder.getPayMode() == OrderPayMode.JF_PAY)) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("付款中的订单，请等待服务员确认后在进行加菜");
                return jsonResult;
            }
            if (farOrder.getOrderState() == OrderState.SUBMIT && farOrder.getPayType() == PayType.NOPAY && farOrder.getIsPay().equals(OrderPayState.ALIPAYING)) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("请先支付完选择支付宝支付的订单，再进行加菜！");
                return jsonResult;
            }



            if ((farOrder.getOrderState() == OrderState.PAYMENT || farOrder.getOrderState() == OrderState.CONFIRM ||
                    farOrder.getOrderState() == OrderState.HASAPPRAISE || farOrder.getOrderState() == OrderState.SHARED)
                    && farOrder.getPayType() == PayType.NOPAY && order.getPayType() == PayType.NOPAY) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("下单失败，订单金额变动，请重新下单！");
                return jsonResult;
            }
//            if((farOrder.getIsPay() == OrderPayState.PAYING || farOrder.getIsPay() == OrderPayState.NOT_PAY) && farOrder.getPayMode() == OrderPayMode.WX_PAY
//                    && farOrder.getOrderState() == OrderState.SUBMIT && farOrder.getPayType() == PayType.NOPAY){
//                jsonResult.setSuccess(false);
//                jsonResult.setMessage("正在微信付款中，请勿加菜！");
//                return jsonResult;
//            }
        }
        Date now = new Date();
        //判断这比订单是否属于   1:1 消费返利的订单
        if (brandSetting.getConsumptionRebate() == 1 && shopDetail.getConsumptionRebate() == 1
                && shopDetail.getRebateTime().compareTo(now) == 1) {
            order.setIsConsumptionRebate(2);
        }
//        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        List<Article> articles = articleService.selectList(order.getShopDetailId());
        List<ArticlePrice> articlePrices = articlePriceService.selectList(order.getShopDetailId());
        Map<String, Article> articleMap = ApplicationUtils.convertCollectionToMap(String.class, articles);
        Map<String, ArticlePrice> articlePriceMap = ApplicationUtils.convertCollectionToMap(String.class,
                articlePrices);

        if (customer != null && customer.getTelephone() != null) {
            order.setVerCode(customer.getTelephone().substring(7));
        } else if (customer == null && order.getOrderMode() == ShopMode.CALL_NUMBER && order.getTableNumber() != null) {
            order.setVerCode(order.getTableNumber());
        } else {
            if (org.springframework.util.StringUtils.isEmpty(order.getParentOrderId())) {
                order.setVerCode(generateString(5));
            } else {
                Order p = getOrderInfo(order.getParentOrderId());
                order.setVerCode(p.getVerCode());
            }
        }
        order.setId(orderId);
        order.setCreateTime(new Date());
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", order.getId());
        map.put("type", "orderAction");
        map.put("content", "订单:" + order.getId() + "已创建,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        if (customer != null) {
            Map customerMap = new HashMap(4);
            customerMap.put("brandName", brand.getBrandName());
            customerMap.put("fileName", customer.getId());
            customerMap.put("type", "UserAction");
            customerMap.put("content", "用户:" + customer.getNickname() + "创建了订单Id为:" + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(customerMap);
        }
        BigDecimal totalMoney = BigDecimal.ZERO;
        BigDecimal originMoney = BigDecimal.ZERO;
        int articleCount = 0;
        BigDecimal extraMoney = BigDecimal.ZERO;

        Map<String, Map<String, Integer>> kucunMap = new HashMap<>();

        //记录订单菜品-------------------------------
        for (OrderItem item : order.getOrderItems()) {
            Article a = articleMap.get(item.getArticleId());;
            BigDecimal org_price = null;
            int mealFeeNumber = 0;
            BigDecimal price = null;
            BigDecimal fans_price = null;
            item.setId(ApplicationUtils.randomUUID());
            String remark = "";
            switch (item.getType()) {
                case OrderItemType.ARTICLE://无规格单品
                    // 查出 item对应的 商品信息，并将item的原价，单价，总价，商品名称，商品详情 设置为对应的
                    item.setArticleName(a.getName());
                    org_price = a.getPrice();
                    price = discount(a.getPrice(), a.getDiscount(), item.getDiscount(), a.getName());                      //计算折扣
                    if (a.getDiscount() != 100) {
                        fans_price = discount(a.getPrice(), a.getDiscount(), item.getDiscount(), a.getName());       //计算折扣 （update：粉丝价 更改为 原价*折扣  2017年4月18日 14:08:04  ---lmx）
                    } else {
                        fans_price = a.getFansPrice();
                    }
                    mealFeeNumber = a.getMealFeeNumber() == null ? 0 : a.getMealFeeNumber();
                    remark = a.getDiscount() + "%";          //设置菜品当前折扣
                    break;
                case OrderItemType.RECOMMEND://推荐餐品
                    // 查出 item对应的 商品信息，并将item的原价，单价，总价，商品名称，商品详情 设置为对应的
                    item.setArticleName(a.getName());
                    org_price = a.getPrice();
                    price = a.getPrice();
                    fans_price = a.getFansPrice();
                    mealFeeNumber = a.getMealFeeNumber() == null ? 0 : a.getMealFeeNumber();
                    break;
                case OrderItemType.UNITPRICE://老规格
                    ArticlePrice p = articlePriceMap.get(item.getArticleId());
                    a = articleMap.get(p.getArticleId());
                    item.setArticleName(a.getName() + p.getName());
                    org_price = p.getPrice();
                    price = discount(p.getPrice(), a.getDiscount(), item.getDiscount(), p.getName());                      //计算折扣
                    if (a.getDiscount() != 100) {
                        fans_price = discount(p.getPrice(), a.getDiscount(), item.getDiscount(), p.getName());       //计算折扣 （update：粉丝价 更改为 原价*折扣  2017年4月18日 14:08:04  ---lmx）
                    } else {
                        fans_price = p.getFansPrice();
                    }
                    remark = a.getDiscount() + "%";          //设置菜品当前折扣
                    mealFeeNumber = a.getMealFeeNumber() == null ? 0 : a.getMealFeeNumber();
                    break;
                case OrderItemType.UNIT_NEW://新规格
                    //判断折扣是否匹配，如果不匹配则不允许买单
                    item.setArticleName(item.getName());
//                    if(a.getDiscount() != 100){
//                        org_price = a.getPrice();
//                    }else if (a.getFansPrice() != null) {
//                        org_price = item.getPrice().subtract(a.getFansPrice()).add(a.getPrice());
//                    } else {
//                        org_price = item.getPrice();
//                    }
                    if (item.getDiscount() < 100) {
                        org_price = item.getPrice().divide(new BigDecimal(item.getDiscount()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    } else if (item.getPrice().compareTo(a.getPrice()) == 0) {
                        org_price = item.getPrice();
                    } else if (a.getFansPrice() != null && a.getFansPrice().doubleValue() > 0) {
                        org_price = item.getPrice().subtract(a.getFansPrice()).add(a.getPrice());
                    } else {
                        org_price = item.getPrice();
                    }
                    price = item.getPrice();
                    fans_price = item.getPrice();
                    mealFeeNumber = a.getMealFeeNumber() == null ? 0 : a.getMealFeeNumber();
                    break;
                case OrderItemType.WEIGHT_PACKAGE_ARTICLE:
                    item.setArticleName(item.getName());
                    org_price = item.getPrice();
                    price = item.getPrice();
                    fans_price = item.getPrice();
                    mealFeeNumber = a.getMealFeeNumber() == null ? 0 : a.getMealFeeNumber();
                    item.setWeight(item.getWeight());
                    //买了称重餐包则需要pos核重
                    order.setNeedConfirmOrderItem(1);
                    item.setNeedRemind(1);
                    break;
                case OrderItemType.SETMEALS://套餐主品
                    if (a.getIsEmpty()) {
                        //原有逻辑不对
//                        jsonResult.setSuccess(false);
//                        jsonResult.setMessage("菜品供应时间变动，请重新购买");
//                        return jsonResult;
                        //套餐下单不需要判断套餐本身的状态
                        a.setIsEmpty(false);
                        articleService.update(a);
                    }
                    item.setArticleName(a.getName());
                    org_price = a.getPrice();
                    price = discount(a.getPrice(), a.getDiscount(), item.getDiscount(), a.getName());
                    if (a.getDiscount() != 100) {
                        fans_price = discount(a.getPrice(), a.getDiscount(), item.getDiscount(), a.getName());  //计算折扣 （update：粉丝价 更改为 原价*折扣  2017年4月18日 14:08:04  ---lmx）
                    } else {
                        fans_price = a.getFansPrice();
                    }
                    remark = a.getDiscount() + "%";//设置菜品当前折扣
                    Integer[] mealItemIds = item.getMealItems();
//                    List<MealAttr> mealAttrs = mealAttrMapper.selectList(item.getArticleId());
//                    boolean checkMeal = true;
//                    for (MealAttr mealAttr : mealAttrs) {
//                        if (mealAttr.getChoiceType() == 0) {
//                            //必选
//                            List<MealItem> mealItems = mealItemService.selectByAttrId(mealAttr.getId());
//                            //找到这个属性下所有的菜品
//                            int count = 0;
//                            for (MealItem mealItem : mealItems) {
//                                Integer redisCount = (Integer) redisService.get(mealItem.getArticleId() + Common.KUCUN);
//                                if (redisCount == null) {
//                                    Article article = articleService.selectById(mealItem.getArticleId());
//                                    redisCount = article.getCurrentWorkingStock();
//                                }
//                                if (redisCount > 0) {
//                                    count++;
//                                }
//                            }
//                            if (count < mealAttr.getChoiceCount()) {
//                                checkMeal = false;
//                            }
//                        }
//                    }
//                    if (!checkMeal) {
//                        jsonResult.setSuccess(false);
//                        jsonResult.setMessage("万分抱歉,您购买的套餐" + item.getName() + "已售罄,请重新下单");
//                        articleService.setEmpty(item.getArticleId());
//                        if (customer != null) {
//                            shopCartService.deleteCustomerArticle(customer.getId(), item.getArticleId());
//                        }
//                        return jsonResult;
//                    }
                    List<MealItem> items = mealItemService.selectByIds(mealItemIds);

                    item.setChildren(new ArrayList<OrderItem>());
                    mealFeeNumber = a.getMealFeeNumber() == null ? 0 : a.getMealFeeNumber();
                    for (MealItem mealItem : items) {

                        Integer redisCount = (Integer) redisService.get(mealItem.getArticleId() + Common.KUCUN);

                        if(redisCount==null){
                            if (mealItem.getArticleId().contains("@")){
                                redisCount = orderMapper.selectArticlePriceCount(mealItem.getArticleId());
                            }else {
                                redisCount = orderMapper.selectArticleCount(mealItem.getArticleId());
                            }
                        }

                        if (redisCount<=0){
                            jsonResult.setSuccess(false);
                            jsonResult.setMessage("万分抱歉,您购买的套餐子品" + mealItem.getArticleName() + "已售罄,请重新下单");

                            if(!order.getCustomerId().equals(0) && !order.getCustomerId().equals("") && order.getCustomerId() != null){
                                List<ShopCart> shopCarts = shopCartService.selectByArticleIdAndcustomerId(mealItem.getArticleId(),order.getCustomerId());
                                for(ShopCart shopCart : shopCarts){
                                    shopCartService.delMealArticle(shopCart.getId().toString());
                                    shopCartService.delMealArticle(shopCart.getPid().toString());
                                }
                            }
                            return jsonResult;
                        }

                        OrderItem child = new OrderItem();
                        Article ca = articleMap.get(mealItem.getArticleId());
                        child.setId(ApplicationUtils.randomUUID());
                        child.setMealItemId(mealItem.getId());
                        child.setArticleName(mealItem.getName());
                        child.setMealFeeNumber(0);
                        child.setArticleId(ca.getId());
                        child.setCount(item.getCount());
                        child.setArticleDesignation(ca.getDescription());
                        child.setParentId(item.getId());
                        child.setOriginalPrice(mealItem.getPriceDif());
                        child.setStatus(1);
                        child.setSort(0);
                        child.setUnitPrice(mealItem.getPriceDif().multiply(new BigDecimal(a.getDiscount()).divide(new BigDecimal(100))).multiply(order.getMemberDiscount()));
                        child.setBaseUnitPrice(mealItem.getPriceDif().multiply(order.getMemberDiscount()));
                        child.setType(OrderItemType.MEALS_CHILDREN);
                        child.setCustomerId(item.getCustomerId());
                        BigDecimal finalMoney = child.getUnitPrice().multiply(new BigDecimal(child.getCount())).setScale(2, BigDecimal.ROUND_HALF_UP);
                        if (finalMoney != null && finalMoney.doubleValue() > 0) {
                            extraMoney = extraMoney.add(finalMoney);
                        }
                        child.setFinalPrice(finalMoney);
                        child.setOrderId(orderId);
                        totalMoney = totalMoney.add(finalMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                        item.getChildren().add(child);
                    }
                    break;
                default:
                    throw new AppException(AppException.UNSUPPORT_ITEM_TYPE, "不支持的餐品类型:" + item.getType());
            }
            if (!a.getShopDetailId().equals(order.getShopDetailId())) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("门店选择错误，请尝试扫描桌号二维码进行点餐！");
                return jsonResult;
            }
            item.setMealFeeNumber(mealFeeNumber);
            item.setArticleDesignation(a.getDescription());
            item.setOriginalPrice(org_price);
            item.setStatus(1);
            item.setSort(0);
            if ("0%".equals(remark)) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage(a.getName() + "供应时间发生改变，请重新购买");
                return jsonResult;
            }
            item.setRemark(remark);
            if (order.getMemberDiscount() != null && order.getMemberDiscount().doubleValue() > 0) {
                if (fans_price != null) {
                    fans_price = fans_price.multiply(order.getMemberDiscount());
                } else {
                    price = price.multiply(order.getMemberDiscount());
                }
            }
            if (fans_price != null && "pos".equals(order.getCreateOrderByAddress()) && shopDetail.getPosPlusType() == 1) {
                item.setUnitPrice(price);
            } else if (fans_price != null && "pos".equals(order.getCreateOrderByAddress()) && shopDetail.getPosPlusType() != 1) {
                item.setUnitPrice(fans_price);
            } else if (fans_price != null && "wechat".equals(order.getCreateOrderByAddress())) {
                item.setUnitPrice(fans_price);
            } else {
                item.setUnitPrice(price);
            }
            BigDecimal finalMoney = item.getUnitPrice().multiply(new BigDecimal(item.getCount())).setScale(2, BigDecimal.ROUND_HALF_UP);
            articleCount += item.getCount();
            item.setFinalPrice(finalMoney);
            item.setOrderId(orderId);
            item.setBaseUnitPrice(item.getUnitPrice());
            item.setPosDiscount("100%");
            totalMoney = totalMoney.add(finalMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
            originMoney = originMoney.add(item.getOriginalPrice().multiply(BigDecimal.valueOf(item.getCount()))).setScale(2, BigDecimal.ROUND_HALF_UP);
//            Result check = new Result();
//            if (item.getType() == OrderItemType.ARTICLE) {
//                check = checkArticleList(item, item.getCount());
//            } else if (item.getType() == OrderItemType.UNITPRICE) {
//                check = checkArticleList(item, item.getCount());
//            } else if (item.getType() == OrderItemType.SETMEALS) {
//                check = checkArticleList(item, articleCount);
//            } else if (item.getType() == OrderItemType.UNIT_NEW) {
//                check = checkArticleList(item, item.getCount());
//            } else if (item.getType() == OrderItemType.RECOMMEND) {
//                check = checkArticleList(item, item.getCount());
//            } else if (item.getType() == OrderItemType.WEIGHT_PACKAGE_ARTICLE) {
//                item.setNeedRemind(1);
//                order.setNeedConfirmOrderItem(1);
//                check = new Result(true);
//            }
//            jsonResult.setMessage(check.getMessage());
//            jsonResult.setSuccess(check.isSuccess());
//
//            if (!check.isSuccess()) {
//                break;
//            }

            String articleName = item.getArticleName();
            if (item.getType().equals(OrderItemType.UNIT_NEW)){
                //如果当前菜品是新规格则只存放主菜品名称
                articleName = articleName.substring(0, articleName.indexOf("("));
            }
            Map<String, Integer> atNameAndCount = new HashMap<>();
            if (kucunMap.get(item.getArticleId()) == null) {
                atNameAndCount.put(articleName, item.getCount());
                kucunMap.put(item.getArticleId(), atNameAndCount);
            } else {
                Map<String, Integer> map1 = kucunMap.get(item.getArticleId());
                Integer integer = map1.get(articleName);
                Integer zongcount = (integer==null ? 0 : integer) + item.getCount();
                atNameAndCount.put(articleName, zongcount);
                kucunMap.put(item.getArticleId(), atNameAndCount);
            }
            //判断是否为菜品库菜品，是则将orderItem的articleId替换为article里面的articleID
            if (a.getOpenArticleLibrary()) {
                item.setArticleId(a.getArticleId());
                order.setOpenArticleLibrary(true);
            }
        }

        //校验库存
        Result result = checkoutKucun(kucunMap);
        jsonResult.setMessage(result.getMessage());
        jsonResult.setSuccess(result.isSuccess());

        if (!result.isSuccess()){
            return jsonResult;
        }

        if (!jsonResult.isSuccess()) {
            return jsonResult;
        }
        if (order.getServicePrice() == null) {
            order.setServicePrice(new BigDecimal(0));
        }
        if (order.getMealFeePrice() == null) {
            order.setMealFeePrice(new BigDecimal(0));
        }
        orderItemService.insertItems(order.getOrderItems());
        //记录订单菜品 yz 2017-03-27
        LogTemplateUtils.getOrderItemLogByOrderType(brand.getBrandName(), order.getId(), order.getOrderItems());

        BigDecimal payMoney = totalMoney.add(order.getServicePrice());
        payMoney = payMoney.add(order.getMealFeePrice());
        if (!StringUtils.isEmpty(order.getBeforeId())) {
            order.setParentOrderId(null);
            //如果这个订单之前存在预点餐的餐品
            Order before = selectById(order.getBeforeId());
            payMoney = payMoney.add(before.getOrderMoney());
            totalMoney = totalMoney.add(before.getOrderMoney());
            originMoney = originMoney.add(before.getOriginalAmount());
        }


        log.info(">>>>>>>>>>>>>>>>>>>>>>进入createOrder方法--->同步机制,判断customer != null>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        if (customer != null) {
            ShopDetail detail = shopDetailService.selectById(order.getShopDetailId());
            if (order.getWaitId() != null && !"".equals(order.getWaitId())) {
                OrderPaymentItem item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(orderId);
                item.setPaymentModeId(PayMode.WAIT_MONEY);
                item.setPayTime(order.getCreateTime());
                item.setPayValue(order.getWaitMoney());
                item.setRemark("等位红包支付:" + order.getWaitMoney());
//                item.setResultData(order.getWaitId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
                item.setToPayId(order.getWaitId());
                if (order.getWaitMoney().doubleValue() > 0) {
                    orderPaymentItemService.insert(item);
                }
                LogTemplateUtils.getWaitMoneyLogByOrderType(brand.getBrandName(), order.getId(), item.getPayValue());
                LogTemplateUtils.getWaitMoneyLogByUserType(brand.getBrandName(), order.getId(), item.getPayValue(), customer.getNickname());

                GetNumber getNumber = getNumberService.selectById(order.getWaitId());
                log.error(order.getWaitId() + "-----------222222222222222");
                getNumber.setState(WaitModerState.WAIT_MODEL_NUMBER_THREE);
                getNumberService.update(getNumber);
            }

            payMoney = payMoney.subtract(order.getWaitMoney());

            if (detail.getShopMode() != ShopMode.HOUFU_ORDER && order.getPayType() != PayType.NOPAY) {
                if (order.getUseCoupon() != null && order.getParentOrderId() == null) {
                    Coupon coupon = couponService.useCoupon(totalMoney, order);
                    OrderPaymentItem item = new OrderPaymentItem();
                    item.setId(ApplicationUtils.randomUUID());
                    item.setOrderId(orderId);
                    item.setPaymentModeId(PayMode.COUPON_PAY);
                    item.setPayTime(order.getCreateTime());
                    item.setRemark("优惠券支付:" + item.getPayValue());
//                    item.setResultData(coupon.getId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
                    item.setToPayId(coupon.getId());
                    if(order.getUseProductCoupon() == 1){
                        item.setCouponArticleId(order.getProductCouponArticleId());
                        Article article = articleService.selectByPrimaryKey(order.getProductCouponArticleId());
                        if(article != null){
                            BigDecimal m = article.getFansPrice() == null ? article.getPrice() : article.getFansPrice();
                            if(m.doubleValue() < coupon.getValue().doubleValue()){
                                item.setPayValue(m);
                            }else{
                                item.setPayValue(coupon.getValue());
                            }
                        }else{
                            item.setPayValue(coupon.getValue());
                        }
                    }else{
                        item.setPayValue(coupon.getValue());
                    }
                    orderPaymentItemService.insert(item);
                    LogTemplateUtils.getCouponByOrderType(brand.getBrandName(), order.getId(), item.getPayValue());
                    LogTemplateUtils.getCouponByUserType(brand.getBrandName(), customer.getId(), customer.getNickname(), item.getPayValue());
                    payMoney = payMoney.subtract(item.getPayValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                // 使用余额
                if (payMoney.doubleValue() > 0 && order.isUseAccount() && order.getPayType() != PayType.NOPAY) {
                    BigDecimal payValue = accountService.payOrder(order, payMoney, customer, brand, shopDetail);
                    log.info("这里用户使用了余额");
//			    BigDecimal payValue = accountService.useAccount(payMoney, account,AccountLog.SOURCE_PAYMENT);
                    if (payValue.doubleValue() > 0) {
                        payMoney = payMoney.subtract(payValue.setScale(2, BigDecimal.ROUND_HALF_UP));
//				OrderPaymentItem item = new OrderPaymentItem();
//				item.setId(ApplicationUtils.randomUUID());
//				item.setOrderId(orderId);
//				item.setPaymentModeId(PayMode.ACCOUNT_PAY);
//				item.setPayTime(order.getCreateTime());
//				item.setPayValue(payValue);
//				item.setRemark("余额支付:" + item.getPayValue());
//				item.setResultData(account.getId());
//				orderPaymentItemService.insert(item);
//				payMoney = payMoney.subtract(item.getPayValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                }
            }

            //如果是余额不满足时，使用现金或者银联支付
            if (payMoney.compareTo(BigDecimal.ZERO) > 0 && order.getPayMode() == OrderPayMode.YL_PAY) {
                OrderPaymentItem item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(orderId);
                item.setPaymentModeId(PayMode.BANK_CART_PAY);
                item.setPayTime(order.getCreateTime());
                item.setPayValue(payMoney);
                item.setRemark("银联支付:" + item.getPayValue());
                orderPaymentItemService.insert(item);
                LogTemplateUtils.getBankByOrderType(brand.getBrandName(), order.getId(), item.getPayValue());
                LogTemplateUtils.getBankByUserType(brand.getBrandName(), order.getId(), customer.getNickname(), item.getPayValue());
                order.setAllowContinueOrder(false);
            } else if (payMoney.compareTo(BigDecimal.ZERO) > 0 && order.getPayMode() == OrderPayMode.XJ_PAY) {
                OrderPaymentItem item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(orderId);
                item.setPaymentModeId(PayMode.CRASH_PAY);
                item.setPayTime(order.getCreateTime());
                item.setPayValue(payMoney);
                item.setRemark("现金支付:" + item.getPayValue());
                orderPaymentItemService.insert(item);
                LogTemplateUtils.getMoneyByOrderType(brand.getBrandName(), order.getId(), item.getPayValue());
                LogTemplateUtils.getMoneyByUserType(brand.getBrandName(), order.getId(), customer.getNickname(), item.getPayValue());
                order.setAllowContinueOrder(false);
            } else if (payMoney.compareTo(BigDecimal.ZERO) > 0 && order.getPayMode() == OrderPayMode.SHH_PAY) {
                OrderPaymentItem item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(orderId);
                item.setPaymentModeId(PayMode.SHANHUI_PAY);
                item.setPayTime(order.getCreateTime());
                item.setPayValue(payMoney);
                item.setRemark("新美大支付:" + item.getPayValue());
                orderPaymentItemService.insert(item);
                order.setAllowContinueOrder(false);
            } else if (payMoney.compareTo(BigDecimal.ZERO) > 0 && order.getPayMode() == OrderPayMode.JF_PAY) {
                OrderPaymentItem item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(orderId);
                item.setPaymentModeId(PayMode.INTEGRAL_PAY);
                item.setPayTime(order.getCreateTime());
                item.setPayValue(payMoney);
                item.setRemark("会员支付:" + item.getPayValue());
                orderPaymentItemService.insert(item);
                Map crashPayMap = new HashMap(4);
                crashPayMap.put("brandName", brand.getBrandName());
                crashPayMap.put("fileName", order.getId());
                crashPayMap.put("type", "orderAction");
                crashPayMap.put("content", "订单:" + order.getId() + "订单使用会员支付了：" + item.getPayValue() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(crashPayMap);
                Map CustomerCrashPayMap = new HashMap(4);
                CustomerCrashPayMap.put("brandName", brand.getBrandName());
                CustomerCrashPayMap.put("fileName", customer.getId());
                CustomerCrashPayMap.put("type", "UserAction");
                CustomerCrashPayMap.put("content", "用户:" + customer.getNickname() + "使用会员支付了：" + item.getPayValue() + "订单Id为:" + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(CustomerCrashPayMap);
                order.setAllowContinueOrder(false);
            }

            if (payMoney.doubleValue() < 0) {
                payMoney = BigDecimal.ZERO;
            }
            //yz 记录订单支付项 2017-03-27
            order.setAccountingTime(order.getCreateTime()); // 财务结算时间

            order.setAllowCancel(true); // 订单是否允许取消
            order.setAllowAppraise(false);
            if (!StringUtils.isEmpty(order.getBeforeId())) {
                Order beforeOrder = orderMapper.selectByPrimaryKey(order.getBeforeId());
                order.setArticleCount(articleCount + beforeOrder.getArticleCount());
            } else {
                order.setArticleCount(articleCount); // 订单餐品总数
            }
            order.setClosed(false); // 订单是否关闭 否
            order.setSerialNumber(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSSS")); // 流水号
            order.setOriginalAmount(originMoney.add(order.getServicePrice().divide(order.getMemberDiscount())).add(order.getMealFeePrice().divide(order.getMemberDiscount())).add(extraMoney));// 原价
            order.setReductionAmount(BigDecimal.ZERO);// 折扣金额
            order.setOrderMoney(totalMoney.add(order.getServicePrice()).add(order.getMealFeePrice())); // 订单实际金额
            order.setPaymentAmount(payMoney); // 订单剩余需要维修支付的金额
            order.setPrintTimes(0);
            //判断当前订单所在店铺的服务费类型  0：经典版  1：升级版
            if (Common.YES.equals(shopDetail.getServiceType())) { //如果是启用的升级版服务费则将其拆分(拆分为shop端店铺设置开启的餐具费、纸巾费、酱料费)
                if (order.getCustomerCount() != null) {
                    if (Common.YES.equals(shopDetail.getIsOpenSauceFee())) { //如果店铺开启了餐具费
                        order.setSauceFeeCount(order.getCustomerCount());//将对应的就餐人数设置为餐具费的数量
                        order.setSauceFeePrice(new BigDecimal(order.getSauceFeeCount()).multiply(shopDetail.getSauceFeePrice()));//餐具费价格等于数量乘以设置的单价
                    }
                    if (Common.YES.equals(shopDetail.getIsOpenTowelFee())) {//如果店铺开启了纸巾费
                        order.setTowelFeeCount(order.getCustomerCount());
                        order.setTowelFeePrice(new BigDecimal(order.getTowelFeeCount()).multiply(shopDetail.getTowelFeePrice()));
                    }
                    if (Common.YES.equals(shopDetail.getIsOpenTablewareFee())) {//如果店铺开启了酱料费
                        order.setTablewareFeeCount(order.getCustomerCount());
                        order.setTablewareFeePrice(new BigDecimal(order.getTablewareFeeCount()).multiply(shopDetail.getTablewareFeePrice()));
                    }
                    order.setIsUseNewService(Common.YES);
                }
            }
            order.setOrderMode(detail.getShopMode());
            if (order.getOrderMode() == ShopMode.CALL_NUMBER) {
                order.setTableNumber(order.getVerCode());
            }

//        if(!order.getOrderMode().equals(ShopMode.HOUFU_ORDER)){
            if (!StringUtils.isEmpty(order.getTableNumber())) {
                if (order.getParentOrderId() != null) {
                    Order parentOrder = selectById(order.getParentOrderId());
                    order.setTableNumber(parentOrder.getTableNumber());
                    order.setVerCode(parentOrder.getVerCode());
                    order.setCustomerCount(parentOrder.getCustomerCount());
                } else if (StringUtils.isEmpty(order.getBeforeId()) && order.getOrderBefore() == null) {
                    Order lastOrder = orderMapper.getLastOrderByCustomer(customer.getId(), order.getShopDetailId(), brandSetting.getCloseContinueTime(), order.getTableNumber());
                    if (lastOrder != null && lastOrder.getParentOrderId() != null) {
                        Order parent = orderMapper.selectByPrimaryKey(lastOrder.getParentOrderId());
                        if (parent != null && parent.getAllowContinueOrder()) {
                            if (parent.getGroupId() != null && !"".equals(parent.getGroupId())
                                    && order.getGroupId() != null && !"".equals(order.getGroupId())
                                    && !order.getGroupId().equals(parent.getGroupId())) {

                            } else {
                                order.setParentOrderId(parent.getId());
                                order.setTableNumber(parent.getTableNumber());
                                order.setVerCode(parent.getVerCode());
                                order.setCustomerCount(parent.getCustomerCount());
                                order.setServicePrice(new BigDecimal(0));
                            }
                        }
                    } else {
                        if (lastOrder != null && lastOrder.getAllowContinueOrder()) {
                            if (lastOrder.getGroupId() != null && !"".equals(lastOrder.getGroupId())
                                    && order.getGroupId() != null && !"".equals(order.getGroupId())
                                    && !order.getGroupId().equals(lastOrder.getGroupId())) {

                            } else {
                                order.setParentOrderId(lastOrder.getId());
                                Order parentOrder = selectById(order.getParentOrderId());
                                order.setTableNumber(parentOrder.getTableNumber());
                                order.setVerCode(parentOrder.getVerCode());
                                order.setCustomerCount(parentOrder.getCustomerCount());
                                order.setServicePrice(new BigDecimal(0));
                            }
                        }
                    }
                }
            }
            //判断是否是后付款模式
            if (order.getOrderMode() == ShopMode.HOUFU_ORDER || order.getPayType() == PayType.NOPAY) {
                order.setOrderState(OrderState.SUBMIT);
                order.setProductionStatus(ProductionStatus.NOT_ORDER);
                if (order.getDistributionModeId() != 3) {
                    order.setAllowContinueOrder(true);
                }
            } else if (order.getPaymentAmount().doubleValue() == 0) {
                order.setOrderState(OrderState.PAYMENT);
                order.setProductionStatus(ProductionStatus.NOT_ORDER);
            } else {
                order.setOrderState(OrderState.SUBMIT);
                order.setProductionStatus(ProductionStatus.NOT_ORDER);
            }
            if (order.getDistributionModeId() == DistributionType.TAKE_IT_SELF && detail.getContinueOrderScan().equals(Common.NO)) {
                order.setTableNumber(order.getVerCode());
            }
            if (order.getDistributionModeId() == DistributionType.DELIVERY_MODE_ID) {
                order.setTableNumber(order.getVerCode());
            }

            if (order.getDistributionModeId() == DistributionType.TAKE_IT_SELF && detail.getContinueOrderScan().equals(Common.YES)) {
                order.setNeedScan(Common.YES);
            } else if (order.getDistributionModeId() != DistributionType.TAKE_IT_SELF && order.getOrderMode() == ShopMode.TABLE_MODE
                    && StringUtils.isEmpty(order.getTableNumber())) {
                order.setNeedScan(Common.YES);
            } else if (order.getDistributionModeId() != DistributionType.TAKE_IT_SELF && order.getOrderMode() == ShopMode.HOUFU_ORDER
                    && StringUtils.isEmpty(order.getTableNumber())) {
                order.setNeedScan(Common.YES);
            }

            if (order.getOrderMode() == ShopMode.MANUAL_ORDER) {
                order.setNeedScan(Common.YES);
            }

            //创建订单时候  如果存在groupId  则组状态改变   以及添加订单参与者信息
            if (order.getGroupId() != null && !"".equals(order.getGroupId()) && order.getParentOrderId() == null) {
                TableGroup tableGroup = tableGroupService.selectByGroupId(order.getGroupId());
                tableGroup.setState(TableGroup.PAY);
                tableGroupService.update(tableGroup);
                //获取去重后的点餐人员列表  记录参与者
                List<String> customerIdList = shopCartService.getListByGroupIdDistinctCustomerId(order.getGroupId());
                List<ShopCart> shopCarts = shopCartService.getListByGroupId(order.getGroupId());
                Map orderMap = new HashMap(4);
                orderMap.put("brandName", brand.getBrandName());
                orderMap.put("fileName", order.getId());
                orderMap.put("type", "orderAction");
                orderMap.put("content", "点餐参与者:" + shopCarts.toString());
                doPostAnsc(orderMap);
                for (String cId : customerIdList) {
                    Participant participant = new Participant();
                    participant.setGroupId(order.getGroupId());
                    participant.setCustomerId(cId);
                    participant.setShopDetailId(order.getShopDetailId());
                    participant.setBrandId(order.getBrandId());
                    participant.setOrderId(order.getId());
                    participant.setIsPay(0);
                    participant.setAppraise(0);
                    participantService.insert(participant);
                }
                //判断付款人是否参与点餐    如果为买单并且未加过菜 状态1    如果买单且加过菜  状态2
                if (!customerIdList.contains(order.getCustomerId())) {
                    Participant participant = new Participant();
                    participant.setGroupId(order.getGroupId());
                    participant.setCustomerId(order.getCustomerId());
                    participant.setOrderId(order.getId());
                    participant.setIsPay(1);
                    participant.setShopDetailId(order.getShopDetailId());
                    participant.setAppraise(0);
                    participant.setBrandId(order.getBrandId());
                    participantService.insert(participant);
                } else {
                    participantService.updateIsPayByOrderIdCustomerId(order.getGroupId(), order.getId(), order.getCustomerId());
                }
            } else if (order.getGroupId() != null && !"".equals(order.getGroupId()) && order.getParentOrderId() != null) {
                //加菜  查出加菜订单的记录  判断在现有参与 是否存在   不存在则记录  如果买单人是新出现(以前没有参与，加菜订单也未点餐，仅买单的人) is_pay的状态也当作0处理
                List<String> cIdParticipant = participantService.selectCustomerIdByGroupId(order.getGroupId());
                List<String> customerIdList = shopCartService.getListByGroupIdDistinctCustomerId(order.getGroupId());
                for (String cId : customerIdList) {
                    if (!cIdParticipant.contains(cId)) {
                        Participant participant = new Participant();
                        participant.setGroupId(order.getGroupId());
                        participant.setCustomerId(cId);
                        participant.setOrderId(order.getId());
                        participant.setIsPay(0);
                        participant.setAppraise(0);
                        participant.setBrandId(order.getBrandId());
                        participant.setShopDetailId(order.getShopDetailId());
                        participantService.insert(participant);
                    }
                }
                if (!cIdParticipant.contains(order.getCustomerId()) && !customerIdList.contains(order.getCustomerId())) {
                    Participant participant = new Participant();
                    participant.setGroupId(order.getGroupId());
                    participant.setCustomerId(order.getCustomerId());
                    participant.setOrderId(order.getId());
                    participant.setIsPay(0);
                    participant.setAppraise(0);
                    participant.setBrandId(order.getBrandId());
                    participant.setShopDetailId(order.getShopDetailId());
                    participantService.insert(participant);
                }
            }

            //创建0元订单 。添加0元余额支付项
            if(order.getOrderMoney().doubleValue() == 0){
                OrderPaymentItem item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(orderId);
                item.setPaymentModeId(PayMode.CHARGE_PAY);
                item.setPayTime(order.getCreateTime());
                item.setPayValue(new BigDecimal(0));
                item.setRemark("余额:" + 0);
                orderPaymentItemService.insert(item);
            }

            //美食广场模式不可以加菜
            if(order.getOrderMode() == ShopMode.MEISHI){
                order.setAllowContinueOrder(false);
            }
            orderMapper.insertSelective(order);
            customerService.changeLastOrderShop(order.getShopDetailId(), order.getCustomerId());
            if (order.getPaymentAmount().doubleValue() == 0 && order.getPayType() == PayType.PAY) {
                payOrderSuccess(order);
            }
            jsonResult.setData(order);

            if (order.getOrderMode() == ShopMode.HOUFU_ORDER) {
                if (order.getParentOrderId() != null) {  //子订单
                    Order parent = selectById(order.getParentOrderId());
                    int articleCountWithChildren = selectArticleCountById(parent.getId(), order.getOrderMode());
                    if (parent.getLastOrderTime() == null || parent.getLastOrderTime().getTime() < order.getCreateTime().getTime()) {
                        parent.setLastOrderTime(order.getCreateTime());
                    }
                    Double amountWithChildren = orderMapper.selectParentAmount(parent.getId(), parent.getOrderMode());
                    parent.setCountWithChild(articleCountWithChildren);
                    parent.setAmountWithChildren(new BigDecimal(amountWithChildren));
                    //如果子订单有重量包  则考虑主订单是否存在需要pos端确认  不需要确认的情况下需要改为需要确认。
                    if (parent.getNeedConfirmOrderItem() == 0 && order.getNeedConfirmOrderItem() != null
                            && order.getNeedConfirmOrderItem() == 1) {
                        parent.setNeedConfirmOrderItem(1);
                    }
                    update(parent);
                }
            } else if (order.getPayType() == PayType.NOPAY && order.getOrderMode() == ShopMode.BOSS_ORDER) {
                if (order.getParentOrderId() != null) {  //子订单
                    Order parent = selectById(order.getParentOrderId());
                    int articleCountWithChildren = orderMapper.selectArticleCountByIdBossOrder(parent.getId());
                    if (parent.getLastOrderTime() == null || parent.getLastOrderTime().getTime() < order.getCreateTime().getTime()) {
                        parent.setLastOrderTime(order.getCreateTime());
                    }
                    Double amountWithChildren = orderMapper.selectParentAmountByBossOrder(parent.getId());
                    parent.setCountWithChild(articleCountWithChildren);
                    parent.setAmountWithChildren(new BigDecimal(amountWithChildren));
                    //如果子订单有重量包  则考虑主订单是否存在需要pos端确认  不需要确认的情况下需要改为需要确认。
                    if (parent.getNeedConfirmOrderItem() == 0 && order.getNeedConfirmOrderItem() != null
                            && order.getNeedConfirmOrderItem() == 1) {
                        parent.setNeedConfirmOrderItem(1);
                    }
                    update(parent);
                }
            }
        } else {

            log.info(">>>>>>>>>>>>>>>>>>>>>>进入createOrder方法--->同步机制,进入else>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            //pos开台支付不存在用户的时候执行
            OrderPaymentItem item = null;
            order.setOrderState(OrderState.PAYMENT);
            order.setAllowContinueOrder(false);
            order.setAccountingTime(order.getCreateTime()); // 财务结算时间
            order.setAllowCancel(true); // 订单是否允许取消
            order.setAllowAppraise(false);
            order.setArticleCount(articleCount); // 订单餐品总数
            order.setClosed(false); // 订单是否关闭 否
            order.setSerialNumber(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSSS")); // 流水号
            order.setOriginalAmount(originMoney.add(extraMoney));// 原价
            order.setReductionAmount(BigDecimal.ZERO);// 折扣金额
            order.setOrderMoney(totalMoney); // 订单实际金额
            order.setPrintTimes(0);
            order.setCustomerId("0");
            order.setOrderMode(ShopMode.CALL_NUMBER);
            order.setProductionStatus(ProductionStatus.NOT_ORDER);
            orderMapper.insertSelective(order);
            jsonResult.setData(order);
            switch (order.getPayMode()) {
                case OrderPayMode.WX_PAY:
                    item = new OrderPaymentItem();
                    item.setId(ApplicationUtils.randomUUID());
                    item.setOrderId(orderId);
                    item.setPaymentModeId(PayMode.WEIXIN_PAY);
                    item.setPayTime(order.getCreateTime());
                    item.setPayValue(order.getPaymentAmount());
                    item.setRemark("微信支付:" + order.getPaymentAmount());
                    orderPaymentItemService.insert(item);
                    break;
                case OrderPayMode.ALI_PAY:
                    item = new OrderPaymentItem();
                    item.setId(ApplicationUtils.randomUUID());
                    item.setOrderId(orderId);
                    item.setPaymentModeId(PayMode.ALI_PAY);
                    item.setPayTime(order.getCreateTime());
                    item.setPayValue(order.getPaymentAmount());
                    item.setRemark("支付宝支付:" + order.getPaymentAmount());
                    orderPaymentItemService.insert(item);
                    break;
                case OrderPayMode.YL_PAY:
                    item = new OrderPaymentItem();
                    item.setId(ApplicationUtils.randomUUID());
                    item.setOrderId(orderId);
                    item.setPaymentModeId(PayMode.BANK_CART_PAY);
                    item.setPayTime(order.getCreateTime());
                    item.setPayValue(order.getPaymentAmount());
                    item.setRemark("银联支付:" + order.getPaymentAmount());
                    orderPaymentItemService.insert(item);
                    break;
                case OrderPayMode.XJ_PAY:
                    item = new OrderPaymentItem();
                    item.setId(ApplicationUtils.randomUUID());
                    item.setOrderId(orderId);
                    item.setPaymentModeId(PayMode.CRASH_PAY);
                    item.setPayTime(order.getCreateTime());
                    item.setPayValue(order.getPaymentAmount());
                    item.setRemark("现金支付:" + order.getPaymentAmount());
                    orderPaymentItemService.insert(item);
                    break;
                case OrderPayMode.SHH_PAY:
                    item = new OrderPaymentItem();
                    item.setId(ApplicationUtils.randomUUID());
                    item.setOrderId(orderId);
                    item.setPaymentModeId(PayMode.SHANHUI_PAY);
                    item.setPayTime(order.getCreateTime());
                    item.setPayValue(order.getPaymentAmount());
                    item.setRemark("大众点评支付:" + order.getPaymentAmount());
                    orderPaymentItemService.insert(item);
                    break;
                case OrderPayMode.JF_PAY:
                    item = new OrderPaymentItem();
                    item.setId(ApplicationUtils.randomUUID());
                    item.setOrderId(orderId);
                    item.setPaymentModeId(PayMode.INTEGRAL_PAY);
                    item.setPayTime(order.getCreateTime());
                    item.setPayValue(order.getPaymentAmount());
                    item.setRemark("大众点评支付:" + order.getPaymentAmount());
                    orderPaymentItemService.insert(item);
                    break;
                default:
                    break;
            }
            if (order.getGiveChange().doubleValue() > 0) {
                item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(orderId);
                item.setPaymentModeId(PayMode.GIVE_CHANGE);
                item.setPayTime(order.getCreateTime());
                item.setPayValue(order.getGiveChange().multiply(new BigDecimal(-1)));
                item.setRemark("找零:" + order.getGiveChange());
                orderPaymentItemService.insert(item);
            }
        }
        return jsonResult;
    }


    private Result checkoutKucun(Map<String, Map<String, Integer>> kucunMap) {
        Result result = new Result();
        Boolean boo = false;
        int current = 0;
        String msg = "";
        for (String key : kucunMap.keySet()) {
            Map<String, Integer> map1 = kucunMap.get(key);
            Integer kuc = (Integer) redisService.get(key + Common.KUCUN);

            if (kuc != null) {
                current = kuc;
            } else {
                if (key.contains("@")){
                    current = orderMapper.selectArticlePriceCount(key);
                }else {
                    current = orderMapper.selectArticleCount(key);
                }
            }

            for (String key1 : map1.keySet()) {
                Integer count = map1.get(key1);
                boo = current >= count;
                msg = current == 0 ? key1 + "已售罄,请取消订单后重新下单" :
                        current >= count ? "库存足够" : key1 + "中单品库存不足,最大购买" + current + "个,请重新选购餐品";
            }
        }
        result.setSuccess(boo);
        result.setMessage(msg);
        return result;
    }



    @Override
    public void updateOrderChild(String orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        Order parent = selectById(order.getParentOrderId());
        int articleCountWithChildren = selectArticleCountById(parent.getId(), parent.getOrderMode());
        if (parent.getLastOrderTime() == null || parent.getLastOrderTime().getTime() < order.getCreateTime().getTime()) {
            parent.setLastOrderTime(order.getCreateTime());
        }
        Double amountWithChildren = orderMapper.selectParentAmount(parent.getId(), parent.getOrderMode());
        parent.setCountWithChild(articleCountWithChildren);
        if (amountWithChildren == parent.getOrderMoney().doubleValue()) {
            parent.setAmountWithChildren(new BigDecimal(0.0));
        } else {
            parent.setAmountWithChildren(new BigDecimal(amountWithChildren));
        }

        update(parent);
    }

    public Result checkArticleList(OrderItem orderItem, int count) {
        Boolean result = true;
        String msg = "";

        //订单菜品不可为空
        //有任何一个菜品售罄则不能出单
        Result check = checkStock(orderItem, count);
        if (!check.isSuccess()) {
            result = false;
            msg = check.getMessage();
        }
        return new Result(msg, result);
    }

    public Order payOrderSuccess(Order order) {

        log.info(">>>>>>>>>>>>>>>>>>开始进入payOrderSuccess方法,同步机制>>>>>>>>>>>>>>>>>>>>");

        log.info("--------开始进入payOrderSuccess方法------订单状态："+order.getOrderState());

        if (order.getOrderMode() != ShopMode.HOUFU_ORDER) {
            order.setOrderState(OrderState.PAYMENT);
            order.setIsPay(OrderPayState.PAYED);

            if (order.getPayMode() == OrderPayMode.ALI_PAY && order.getIsPay().equals(OrderPayState.ALIPAYING)) {
                order.setIsPay(OrderPayState.ALIPAYED);
            } else if (order.getPayMode() != OrderPayMode.WX_PAY && order.getIsPay().equals(OrderPayState.ALIPAYING)) {
                order.setIsPay(OrderPayState.PAYED);
            } else if (order.getPayMode() != OrderPayMode.ALI_PAY && order.getIsPay().equals(OrderPayState.ALIPAYING)) {
                order.setIsPay(OrderPayState.NOT_PAY);
            }

            if(order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.NOPAY){
                order.setAllowContinueOrder(false);
            }
            update(order);
            log.info("-------------进入payOrderSuccess方法，判断OrderMode不等于后付-------------订单状态："+order.getOrderState());
        }



        if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getParentOrderId() == null && order.getPayType() == PayType.NOPAY) {
            updateChild(order);
            log.info("----------进入updateChild方法后的订单状态------------订单状态："+order.getOrderState());
        }


        if (order.getParentOrderId() != null) {  //子订单
            Order parent = selectById(order.getParentOrderId());
            int articleCountWithChildren = selectArticleCountById(parent.getId(), parent.getOrderMode());
            if (parent.getLastOrderTime() == null || parent.getLastOrderTime().getTime() < order.getCreateTime().getTime()) {
                parent.setLastOrderTime(order.getCreateTime());
            }
            Double amountWithChildren = orderMapper.selectParentAmount(parent.getId(), parent.getOrderMode());
            parent.setCountWithChild(articleCountWithChildren);
            parent.setAmountWithChildren(new BigDecimal(amountWithChildren));
            update(parent);
            log.info("----------进入payOrderSuccess方法，并且是子订单-------------父订单状态:"+order.getOrderState());
            try {
                JSONResult jsonResult = pushOrder(order.getId());
                if (jsonResult.isSuccess()) {
                    order = (Order) jsonResult.getData();
                    log.info("子订单，自动下单成功：" + order.getId());
                    mqMessageProducer.sendPlaceOrderMessage(order);
                }
            } catch (AppException e) {
                e.printStackTrace();
                log.error("子订单自动下单失败:" + e.getMessage());
                changePushOrder(order);
            }
        }
        //发放消费返利优惠券
        couponService.addConsumptionRebateCoupon(order);
        log.info(">>>>>>>>>>>>>>>>>>>>>>开始进入payOrderSuccess方法--->同步机制,开始进入支付同步机制方法>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return order;
    }

    private int selectArticleCountById(String id, Integer shopMode) {
        return orderMapper.selectArticleCountById(id, shopMode);
    }

    @Override
    public Order findCustomerNewOrder(String customerId, String shopId, String orderId) {
        Date beginDate = DateUtil.getDateBegin(new Date());
        return findCustomerNewOrder(beginDate, customerId, shopId, orderId);
    }

    public Order findCustomerNewOrder(Date beginDate, String customerId, String shopId, String orderId) {
        Integer[] orderState = new Integer[]{OrderState.SUBMIT, OrderState.PAYMENT, OrderState.CONFIRM};
        Order order = orderMapper.findCustomerNewOrder(beginDate, customerId, shopId, orderState, orderId);
        if (order != null) {
            if (order.getParentOrderId() != null && (order.getOrderState() != OrderState.SUBMIT || order.getOrderMode() == ShopMode.HOUFU_ORDER
                    || (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.NOPAY))) {
                return findCustomerNewOrder(customerId, shopId, order.getParentOrderId());
            }
            Map<String, String> param = new HashMap<>();
            param.put("orderId", order.getId());
            List<OrderItem> itemList = orderItemService.listByOrderId(param);
            order.setOrderItems(itemList);
            if (order.getOrderState() != OrderState.SUBMIT || order.getOrderMode() == ShopMode.HOUFU_ORDER
                    || (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.NOPAY)) {
                List<String> childIds = selectChildIdsByParentId(order.getId());
                List<OrderItem> childItems = orderItemService.listByOrderIds(childIds);
                order.getOrderItems().addAll(childItems);
            }

        }
        return order;
    }

    private List<String> selectChildIdsByParentId(String id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order.getOrderMode() == ShopMode.HOUFU_ORDER ||
                (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPayType() == PayType.NOPAY)) {
            return orderMapper.selectChildIdsByParentIdByFive(id);
        } else {
            return orderMapper.selectChildIdsByParentId(id);
        }

    }

    @Override
    public List<Order> selectByParentId(String parentOrderId, Integer parentOrderPayType) {
        return orderMapper.selectByParentId(parentOrderId, parentOrderPayType);
    }

    @Override
    public boolean cancelOrder(String orderId) {
        Order order = selectById(orderId);
        if (order.getAllowCancel() && order.getProductionStatus() != ProductionStatus.PRINTED && (order.getOrderState().equals(OrderState.SUBMIT) || order.getOrderState() == OrderState.PAYMENT)) {
            order.setAllowCancel(false);
            order.setClosed(true);
            order.setAllowAppraise(false);
            order.setAllowContinueOrder(false);
            order.setOrderState(OrderState.CANCEL);
            update(order);
            mqMessageProducer.sendCancelOrder(order);
            refundOrder(order);

            log.info("取消订单成功:" + order.getId());

            //拒绝订单后还原库存
            Boolean addStockSuccess = false;
            addStockSuccess = addStock(getOrderInfo(orderId));
            if (!addStockSuccess) {
                log.info("库存还原失败:" + order.getId());
            }
//            orderMapper.setStockBySuit(order.getShopDetailId());//自动更新套餐数量
            return true;
        } else {
            log.warn("取消订单失败，订单状态订单状态或者订单可取消字段为false" + order.getId());
            return false;
        }
    }

    @Override
    public boolean cancelExceptionOrder(String orderId) {
        Order order = selectById(orderId);
        if (order.getAllowCancel() && order.getProductionStatus() != ProductionStatus.PRINTED && (order.getOrderState().equals(OrderState.SUBMIT) || order.getOrderState() == OrderState.PAYMENT)) {
            order.setAllowCancel(false);
            order.setClosed(true);
            order.setAllowAppraise(false);
            order.setAllowContinueOrder(false);
            order.setOrderState(OrderState.CANCEL);
            update(order);
            refundOrder(order);
            log.info("取消订单成功:" + order.getId());
            return true;
        } else {
            log.warn("取消订单失败，订单状态订单状态或者订单可取消字段为false" + order.getId());
            return false;
        }
    }

    @Override
    public List<Order> selectNeedCacelOrderList(String brandId, String beginTime, String endTime) {
        Date begin = DateUtil.getformatBeginDate(beginTime);
        Date end = DateUtil.getformatEndDate(endTime);
        return orderMapper.selectNeedCacelOrderList(brandId, begin, end);
    }


    @Override
    public Boolean checkRefundLimit(Order order) {
        Integer orderMode = order.getOrderMode();
        Boolean result = false;
        switch (orderMode) {
            case ShopMode.MANUAL_ORDER: //验证码下单
            case ShopMode.CALL_NUMBER: //电视叫号
            case ShopMode.TABLE_MODE: //坐下点餐
                result = (((order.getOrderState().equals(OrderState.CONFIRM) ||
                        order.getOrderState().equals(OrderState.PAYMENT))
                        &&
                        order.getProductionStatus().equals(ProductionStatus.NOT_PRINT))
                        || (order.getOrderState().equals(OrderState.PAYMENT) &&
                        order.getProductionStatus().equals(ProductionStatus.NOT_ORDER)))
                        || (order.getOrderState().equals(OrderState.SUBMIT) && order.getProductionStatus().equals(ProductionStatus.NOT_ORDER))
                ;
                break;
            default:
                log.info("未知的店铺模式:" + orderMode);
                break;
        }

        return result;
    }

    @Override
    public boolean autoRefundOrder(String orderId) {
        Order order = selectById(orderId);
        if (order.getAllowCancel()) {
            order.setAllowCancel(false);
            order.setClosed(true);
            order.setAllowAppraise(false);
            order.setAllowContinueOrder(false);
            order.setOrderState(OrderState.CANCEL);
            update(order);
            refundOrder(order);
            mqMessageProducer.sendCancelOrder(order);
            log.info("自动退款成功:" + order.getId());
            return true;
        } else {
            log.warn("款项自动退还到相应账户失败，订单状态不是已付款或商品状态不是已付款未下单" + order.getId());
            return false;
        }

    }

    @Override
    public Result refundPaymentByUnfinishedOrder(String orderId) {
        Result result = new Result();
        Order order = selectById(orderId);
//        MemcachedUtils.delete(orderId + "WxPay");
//        redisService.remove(orderId + "WxPay");
        redisService.remove(orderId + "WxPay");
//        if (MemcachedUtils.get(order.getCustomerId() + "createOrder") != null) {
//            MemcachedUtils.delete(order.getCustomerId() + "createOrder");
//        }
//        if (redisService.get(order.getCustomerId() + "createOrder") != null) {
//            redisService.remove(order.getCustomerId() + "createOrder");
        if (redisService.get(order.getCustomerId() + "createOrder") != null) {
            redisService.remove(order.getCustomerId() + "createOrder");
        }
        if (order.getOrderState() != OrderState.SUBMIT) {
            return new Result(false);
        }
        if (!order.getIsPay().equals(OrderPayState.ALIPAYING)) {
            order.setIsPay(OrderPayState.NOT_PAY);
        }
        if (order.getPayMode() == 2) {
            if (!order.getIsPay().equals(OrderPayState.ALIPAYING)) {
                order.setIsPay(OrderPayState.NOT_PAY);
            }
            orderMapper.updateByPrimaryKeySelective(order);
            return new Result("支付宝订单更改为微信支付，支付时点击关闭不取消订单", false);
        }
        if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getProductionStatus() == ProductionStatus.PRINTED) {
            refundOrderHoufu(order);
            result.setSuccess(true);
            BigDecimal hasPay = orderMapper.getPayHoufu(orderId);
            if (hasPay == null) {
                hasPay = BigDecimal.valueOf(0);
            }
            order.setPaymentAmount(order.getOrderMoney().subtract(hasPay));
        } else {
            if (!"sb".equals(order.getOperatorId())) {
                result.setSuccess(autoRefundOrder(orderId));
            }
        }
        update(order);
        if (order.getParentOrderId() != null) {  //子订单
            Order parent = selectById(order.getParentOrderId());
            int articleCountWithChildren = selectArticleCountById(parent.getId(), order.getOrderMode());
            if (parent.getLastOrderTime() == null || parent.getLastOrderTime().getTime() < order.getCreateTime().getTime()) {
                parent.setLastOrderTime(order.getCreateTime());
            }
            Double amountWithChildren = orderMapper.selectParentAmount(parent.getId(), parent.getOrderMode());
            parent.setCountWithChild(articleCountWithChildren);
            parent.setAmountWithChildren(new BigDecimal(amountWithChildren));
            update(parent);
        }

        //拒绝订单后还原库存
        if (order.getPayType() != PayType.NOPAY) {
            Boolean addStockSuccess = false;
            addStockSuccess = addStock(getOrderInfo(orderId));
            if (!addStockSuccess) {
                log.info("库存还原失败:" + order.getId());
            }
//            orderMapper.setStockBySuit(order.getShopDetailId());//自动更新套餐数量
        }

        //用户取消微信支付记录UserAction日志
        Brand brand = brandService.selectByPrimaryKey(order.getBrandId());
        Map customerMap = new HashMap(4);
        customerMap.put("brandName", brand.getBrandName());
        customerMap.put("fileName", order.getCustomerId());
        customerMap.put("type", "UserAction");
        customerMap.put("content", "用户:" + order.getCustomerId() + "取消微信支付，订单Id:" + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(customerMap);
        if (!StringUtils.isEmpty(order.getGroupId())) {
//            MemcachedUtils.delete(order.getShopDetailId() + order.getGroupId());
//            redisService.remove(order.getShopDetailId() + order.getGroupId());
            redisService.remove(order.getShopDetailId() + order.getGroupId());
        }
        return result;
    }

    private void refundOrder(Order order) {
        List<OrderPaymentItem> payItemsList = orderPaymentItemService.selectByOrderId(order.getId());
        Brand brand = brandService.selectById(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        for (OrderPaymentItem item : payItemsList) {
            String newPayItemId = ApplicationUtils.randomUUID();
            int refundTotal = 0;
            BigDecimal aliRefund = new BigDecimal(0);
            BigDecimal aliPay = new BigDecimal(0);
            if (item.getPaymentModeId() == PayMode.WEIXIN_PAY) {
                BigDecimal sum = orderMapper.getRefundSumByOrderId(order.getId(), PayMode.WEIXIN_PAY);
                if (sum != null) {
                    refundTotal = sum.multiply(new BigDecimal(100)).intValue();
                }

            } else if (item.getPaymentModeId() == PayMode.ALI_PAY) {
                BigDecimal sum = orderMapper.getRefundSumByOrderId(order.getId(), PayMode.ALI_PAY);
                aliPay = orderMapper.getAliPayment(order.getId());
                if (sum != null) {
                    aliRefund = sum;
                }
            }

            if (item.getPaymentModeId() == PayMode.WEIXIN_PAY && item.getPayValue().doubleValue() < 0) {
                continue;
            }
            if (item.getPaymentModeId() == PayMode.ALI_PAY && item.getPayValue().doubleValue() < 0) {
                continue;
            }

            if (refundTotal != 0 && refundTotal == order.getPaymentAmount().multiply(new BigDecimal(-100)).intValue()) { //如果已经全部退款完毕
                continue;
            }

            if (aliRefund.doubleValue() < 0 && aliRefund.doubleValue() == aliPay.multiply(new BigDecimal(-1)).doubleValue()) { //如果已经全部退款完毕
                continue;
            }

            switch (item.getPaymentModeId()) {
                case PayMode.COUPON_PAY:
                    couponService.refundCoupon(item.getToPayId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.ACCOUNT_PAY:
                    accountService.addAccount(item.getPayValue(), item.getToPayId(), "取消订单返还", AccountLog.SOURCE_CANCEL_ORDER, order.getShopDetailId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.APPRAISE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getToPayId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.SHARE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getToPayId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.REFUND_ARTICLE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getToPayId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.THIRD_MONEY_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getToPayId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.REBATE_MONEY_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getToPayId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.CHARGE_PAY:
                    chargeOrderService.refundCharge(item.getPayValue(), item.getToPayId(), order.getShopDetailId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.REWARD_PAY:
                    chargeOrderService.refundReward(item.getPayValue(), item.getToPayId(), order.getShopDetailId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.WEIXIN_PAY:
                    WechatConfig config = wechatConfigService.selectByBrandId(DataSourceContextHolder.getDataSourceName());
                    JSONObject obj = new JSONObject(item.getResultData());
                    int refund = obj.getInt("total_fee") + refundTotal;
                    Map<String, String> result = null;
                    if (shopDetail.getWxServerId() == null) {
                        result = WeChatPayUtils.refund(newPayItemId, obj.getString("transaction_id"),
                                obj.getInt("total_fee"), refund, config.getAppid(), config.getMchid(),
                                config.getMchkey(), config.getPayCertPath());
                    } else {
                        WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());

                        result = WeChatPayUtils.refundNew(newPayItemId, obj.getString("transaction_id"),
                                obj.getInt("total_fee"), refund, wxServerConfig.getAppid(), wxServerConfig.getMchid(),
                                StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(), wxServerConfig.getMchkey(), wxServerConfig.getPayCertPath());
                    }
                    if (result.containsKey("ERROR")) {
                        throw new RuntimeException("微信退款异常！" + new JSONObject(result).toString());
                    }
                    item.setPayValue(new BigDecimal(refund).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(-1)));
                    item.setResultData(new JSONObject(result).toString());

                    break;
                case PayMode.WAIT_MONEY:
                    getNumberService.refundWaitMoney(order);
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.ALI_PAY: //如果是支付宝支付
                    BrandSetting brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
                    AliPayUtils.connection(StringUtils.isEmpty(shopDetail.getAliAppId()) ? brandSetting.getAliAppId() : shopDetail.getAliAppId().trim(),
                            StringUtils.isEmpty(shopDetail.getAliPrivateKey()) ? brandSetting.getAliPrivateKey().trim() : shopDetail.getAliPrivateKey().trim(),
                            StringUtils.isEmpty(shopDetail.getAliPublicKey()) ? brandSetting.getAliPublicKey().trim() : shopDetail.getAliPublicKey().trim(),
                            shopDetail.getAliEncrypt());
                    Map map = new HashMap();
                    map.put("out_trade_no", order.getId());
                    map.put("refund_amount", aliPay.add(aliRefund));
                    map.put("out_request_no", newPayItemId);
                    Map<String, String> returnMap = AliPayUtils.refundPay(map);
                    if (returnMap.get("success").equalsIgnoreCase("flase")) {
                        throw new RuntimeException("支付宝退款异常！" + returnMap.toString());
                    }
                    item.setResultData(returnMap.get("returnMsg"));
                    item.setPayValue(aliPay.add(aliRefund).multiply(new BigDecimal(-1)));
                    break;
                case PayMode.ARTICLE_BACK_PAY:
                    Customer customer = customerService.selectById(order.getCustomerId());

                    if (item.getPayValue().doubleValue() < 0) {
                        accountService.addAccount(item.getPayValue(), customer.getAccountId(), "取消订单扣除", -1, order.getShopDetailId());
                    }
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.BANK_CART_PAY:
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.CRASH_PAY:
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.SHANHUI_PAY:
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.INTEGRAL_PAY:
                    accountService.addAccount(item.getPayValue(), item.getToPayId(), "取消订单返还", AccountLog.SOURCE_CANCEL_ORDER, order.getShopDetailId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                default:
                    break;
            }
            item.setId(newPayItemId);
            orderPaymentItemService.insert(item);
        }
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", order.getId());
        map.put("type", "orderAction");
        map.put("content", "订单:" + order.getId() + "已取消,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
//        if (!MemcachedUtils.add(order.getCustomerId() + "createOrder", 1, 30)) {
//            MemcachedUtils.delete(order.getCustomerId() + "createOrder");
//        }
//        if (!redisService.setnxTime(order.getCustomerId() + "createOrder", 1, 30L)) {
//            redisService.remove(order.getCustomerId() + "createOrder");
        if (!redisService.setnxTime(order.getCustomerId() + "createOrder", 1, 30)) {
            redisService.remove(order.getCustomerId() + "createOrder");
        }
    }

    @Override
    public String fixedRefund(String brandId, String shopId,
                              int total, int refund, String transaction_id, String mchid, String id) {
        WechatConfig config = wechatConfigService.selectByBrandId(brandId);
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);
        Map<String, String> result = null;
        String newPayItemId = ApplicationUtils.randomUUID();
        if (shopDetail.getWxServerId() == null) {
            result = WeChatPayUtils.refund(newPayItemId, transaction_id,
                    total, refund, config.getAppid(), config.getMchid(),
                    config.getMchkey(), config.getPayCertPath());
        } else {
            WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());

            result = WeChatPayUtils.refundNew(newPayItemId, transaction_id,
                    total, refund, wxServerConfig.getAppid(), wxServerConfig.getMchid(),
                    StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(), wxServerConfig.getMchkey(), wxServerConfig.getPayCertPath());
        }
//        OrderPaymentItem orderPaymentItem = orderPaymentItemService.selectById(id);
//        orderPaymentItem.setResultData(new JSONObject(result).toString());
//        orderPaymentItemService.update(orderPaymentItem);
        return new JSONObject(result).toString();
    }

    @Override
    public List<OrderItem> selectListByShopIdAndTime(String zuoriDay, String id) {
        Date beginTime = DateUtil.getformatBeginDate(zuoriDay);
        Date endTime = DateUtil.getformatEndDate(zuoriDay);
        return orderMapper.selectListByShopIdAndTime(beginTime, endTime, id);
    }

    @Override
    public List<OrderItem> selectCustomerListByShopIdAndTime(String zuoriDay, String id) {
        Date beginTime = DateUtil.getformatBeginDate(zuoriDay);
        Date endTime = DateUtil.getformatEndDate(zuoriDay);
        return orderMapper.selectCustomerListByShopIdAndTime(beginTime, endTime, id);
    }

    @Override
    public void alipayRefund(String orderId, BigDecimal refundTotal) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        String newPayItemId = ApplicationUtils.randomUUID();
        BrandSetting brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
        AliPayUtils.connection(StringUtils.isEmpty(shopDetail.getAliAppId()) ? brandSetting.getAliAppId() : shopDetail.getAliAppId().trim(),
                StringUtils.isEmpty(shopDetail.getAliPrivateKey()) ? brandSetting.getAliPrivateKey().trim() : shopDetail.getAliPrivateKey().trim(),
                StringUtils.isEmpty(shopDetail.getAliPublicKey()) ? brandSetting.getAliPublicKey().trim() : shopDetail.getAliPublicKey().trim(),
                shopDetail.getAliEncrypt());
        Map map = new HashMap();
        map.put("out_trade_no", order.getId());
        map.put("refund_amount", refundTotal);
        map.put("out_request_no", newPayItemId);
        Map<String, String> returnMap = AliPayUtils.refundPay(map);
        if (returnMap.get("success").equalsIgnoreCase("false")) {
            log.error("支付宝退款失败！失败信息：" + returnMap.toString());
            throw new RuntimeException(returnMap.toString());
        }

        OrderPaymentItem back = new OrderPaymentItem();
        back.setId(ApplicationUtils.randomUUID());
        back.setOrderId(order.getId());
        back.setPaymentModeId(PayMode.ALI_PAY);
        back.setPayTime(new Date());
        back.setPayValue(new BigDecimal(-1).multiply(refundTotal));
        back.setRemark("支付宝支付返回" + refundTotal + "金额");
        back.setResultData(returnMap.get("returnMsg"));
        orderPaymentItemService.insert(back);
    }

    private void refundOrderHoufu(Order order) {
        List<OrderPaymentItem> payItemsList = orderPaymentItemService.selectByOrderId(order.getId());
        List<String> chargeList = new ArrayList<>();
        for (OrderPaymentItem item : payItemsList) {
//            String newPayItemId = ApplicationUtils.randomUUID();
            switch (item.getPaymentModeId()) {
                case PayMode.ACCOUNT_PAY:
                    accountService.addAccount(item.getPayValue(), item.getToPayId(), "取消订单返还", AccountLog.SOURCE_CANCEL_ORDER, order.getShopDetailId());
//                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
//                    item.setId(newPayItemId);
//                    orderPaymentItemService.insert(item);
                    break;
                case PayMode.CHARGE_PAY:
                    if (!chargeList.contains(item.getToPayId())) {
                        chargeList.add(item.getToPayId());
                    }
//                    if (!MemcachedUtils.add(item.getToPayId() + "chargeValue", item.getPayValue(), 600)) {
//                        MemcachedUtils.put(item.getToPayId() + "chargeValue", item.getPayValue().add((BigDecimal) MemcachedUtils.get(item.getToPayId() + "chargeValue")));
//                    }
//                    if (!redisService.setnxTime(item.getToPayId() + "chargeValue", item.getPayValue(), 600L)) {
//                        redisService.set(item.getToPayId() + "chargeValue", item.getPayValue().add((BigDecimal) redisService.get(item.getToPayId() + "chargeValue")));
                    if (!redisService.setnxTime(item.getToPayId() + "chargeValue", item.getPayValue(), 600)) {
                        redisService.set(item.getToPayId() + "chargeValue", item.getPayValue().add((BigDecimal) redisService.get(item.getToPayId() + "chargeValue")));
                    }
                    break;
                case PayMode.REWARD_PAY:
                    if (!chargeList.contains(item.getToPayId())) {
                        chargeList.add(item.getToPayId());
                    }
//                    if (!MemcachedUtils.add(item.getToPayId() + "rewardValue", item.getPayValue(), 600)) {
//                        MemcachedUtils.put(item.getToPayId() + "rewardValue", item.getPayValue().add((BigDecimal) MemcachedUtils.get(item.getToPayId() + "rewardValue")));
//                    }
//                    if (!redisService.setnxTime(item.getToPayId() + "rewardValue", item.getPayValue(), 600L)) {
//                        redisService.set(item.getToPayId() + "rewardValue", item.getPayValue().add((BigDecimal) redisService.get(item.getToPayId() + "rewardValue")));
                    if (!redisService.setnxTime(item.getToPayId() + "rewardValue", item.getPayValue(), 600)) {
                        redisService.set(item.getToPayId() + "rewardValue", item.getPayValue().add((BigDecimal) redisService.get(item.getToPayId() + "rewardValue")));
                    }
                    break;
                case PayMode.APPRAISE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getToPayId());
                    break;
                case PayMode.SHARE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getToPayId());
                    break;
                case PayMode.REFUND_ARTICLE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getToPayId());
                    break;
                case PayMode.THIRD_MONEY_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getToPayId());
                    break;
                case PayMode.REBATE_MONEY_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getToPayId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                default:
                    break;
            }
        }
        if (!CollectionUtils.isEmpty(chargeList)) {
            for (String id : chargeList) {
//                BigDecimal rewardValue = (BigDecimal) redisService.get(id + "rewardValue");
//                BigDecimal chargeValue = (BigDecimal) redisService.get(id + "chargeValue");
//                chargeOrderService.refundMoney(chargeValue, rewardValue, id, order.getShopDetailId());
//                redisService.remove(id + "rewardValue");
//                redisService.remove(id + "chargeValue");
                BigDecimal rewardValue = (BigDecimal) redisService.get(id + "rewardValue");
                BigDecimal chargeValue = (BigDecimal) redisService.get(id + "chargeValue");
                chargeOrderService.refundMoney(chargeValue, rewardValue, id, order.getShopDetailId());
                redisService.remove(id + "rewardValue");
                redisService.remove(id + "chargeValue");
            }
        }
        orderPaymentItemService.deleteByOrderId(order.getId());
        Brand brand = brandService.selectById(order.getBrandId());
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", order.getId());
        map.put("type", "orderAction");
        map.put("content", "订单:" + order.getId() + "已取消,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
    }

    @Override
    public JSONResult orderWxPaySuccess(OrderPaymentItem item) {
        JSONResult jsonResult = new JSONResult();
        Order order = selectOrderInfo(item.getOrderId());
        orderPaymentItemService.insert(item);
//        if (order.getOrderMode() == ShopMode.HOUFU_ORDER) {
//            order.setPaymentAmount(item.getPayValue());
//            update(order);
//        }
        payOrderSuccess(order);
        log.info("-----------走完payOrderSuccess方法后--------------订单状态："+order.getOrderState());
        if (!StringUtils.isEmpty(order.getGroupId())) {
            //如果多人点餐支付成功
//            redisService.remove(order.getShopDetailId() + order.getGroupId());
            redisService.remove(order.getShopDetailId() + order.getGroupId());
        }
        order = orderMapper.selectByPrimaryKey(order.getId());
        jsonResult.setSuccess(true);
        jsonResult.setData(order);
        return jsonResult;
    }

    @Override
    public JSONResult orderWxPaySuccessAspect(JSONResult o) {
        return o;
    }

    @Override
    public JSONResult pushOrder(String orderId) throws AppException {
        JSONResult jsonResult = new JSONResult();
        String time = DateUtil.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss");
        Order order = orderMapper.selectByPrimaryKey(orderId);
        //可能存在不满足pushorder条件的订单  --order = null  或者已经打印的订单
        if (order == null || order.getProductionStatus() == ProductionStatus.PRINTED) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单异常或订单已打印");
            return jsonResult;
        }

        if (!redisService.setnxTime(orderId + "actionPrintForNewPos", 1, 60)) {
            log.info(orderId + "订单正在执行打印！");
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单正在执行打印！");
            return jsonResult;
        }

        //如果是后付款模式 不验证直接进行修改模式
        if (order.getOrderMode() == ShopMode.HOUFU_ORDER) {
            log.info("后付款模式：pushOrder修改生产状态：" + ProductionStatus.HAS_ORDER + "订单id为：" + orderId + "当前时间为：" + time);
            order.setProductionStatus(ProductionStatus.HAS_ORDER);
            order.setPushOrderTime(new Date());
            update(order);
        } else if (validOrderCanPush(order)) {
            log.info("pushOrder时候支付宝支付修改状态：" + ProductionStatus.HAS_ORDER + "订单id为：" + orderId + "当前时间为：" + time);
            order.setProductionStatus(ProductionStatus.HAS_ORDER);
            order.setPushOrderTime(new Date());
            update(order);
        }
        jsonResult.setSuccess(true);
        jsonResult.setData(order);
        return jsonResult;
    }

    private boolean validOrderCanPush(Order order) throws AppException {
        if (order.getPayMode() != null && order.getPayMode() == OrderPayMode.ALI_PAY
                && order.getProductionStatus().equals(ProductionStatus.NOT_ORDER) && order.getOrderState().equals(OrderState.SUBMIT)) {
            return true;
        }


        switch (order.getOrderMode()) {
//            case 1:
//                if (order.getTableNumber() == null) {
//                    throw new AppException(AppException.ORDER_MODE_CHECK, "桌号不得为空");
//                }
//                break;
            case ShopMode.BOSS_ORDER:
                log.error("【BOSS_ORDER】立即下单失败: " + order.getId() + "\n" + "orderStarte:" + order.getOrderState() + "\n" + "productionStatus:" + order.getProductionStatus());
                if (order.getPayType() == PayType.PAY && order.getDistributionModeId() != DistributionType.DELIVERY_MODE_ID) {
                    if (order.getOrderState() != OrderState.CONFIRM && order.getOrderState() != OrderState.PAYMENT || ProductionStatus.NOT_ORDER != order.getProductionStatus()) {
                        log.error("立即下单失败: " + order.getId() + "\n" + "orderStarte:" + order.getOrderState() + "\n" + "productionStatus:" + order.getProductionStatus());
                        throw new AppException(AppException.ORDER_STATE_ERR);
                    }
                } else if (order.getPayType() == PayType.NOPAY) {
                    if (order.getOrderState() != OrderState.SUBMIT || ProductionStatus.NOT_ORDER != order.getProductionStatus()) {
                        log.error("立即下单失败: " + order.getId());
                        throw new AppException(AppException.ORDER_STATE_ERR);
                    }
                }
                break;
            case ShopMode.CALL_NUMBER:
                if (order.getOrderState() != OrderState.PAYMENT || ProductionStatus.NOT_ORDER != order.getProductionStatus()) {
                    log.error("立即下单失败: " + order.getId());
                    throw new AppException(AppException.ORDER_STATE_ERR);
                }
                break;
            case ShopMode.MANUAL_ORDER:
                if (order.getOrderState() != OrderState.PAYMENT || ProductionStatus.NOT_ORDER != order.getProductionStatus()) {
                    log.error("立即下单失败: " + order.getId());
                    throw new AppException(AppException.ORDER_STATE_ERR);
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public JSONResult callNumber(String orderId) {
        JSONResult jsonResult = new JSONResult();
        Order order = selectById(orderId);
        order.setProductionStatus(ProductionStatus.HAS_CALL);
        order.setCallNumberTime(new Date());
        update(order);
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        Brand brand = brandService.selectById(order.getBrandId());
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", shopDetail.getName());
        map.put("type", "posAction");
        map.put("content", "订单:" + order.getId() + "被叫号推送微信就餐提醒并修改productionStatus为3,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        Map orderMap = new HashMap(4);
        orderMap.put("brandName", brand.getBrandName());
        orderMap.put("fileName", order.getId());
        orderMap.put("type", "orderAction");
        orderMap.put("content", "订单:" + order.getId() + "被叫号推送微信就餐提醒并修改productionStatus为3,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(orderMap);
        jsonResult.setSuccess(true);
        jsonResult.setData(order);
        return jsonResult;
    }

    @Override
    public JSONResult printSuccess(String orderId, Boolean openBrandAccount, AccountSetting accountSetting) {
        log.info("======[订单号："+ orderId +"]开始执行订单打印");
        JSONResult jsonResult = new JSONResult();
        Order order = selectById(orderId);
        Brand brand = brandService.selectById(order.getBrandId());
        if (StringUtils.isEmpty(order.getParentOrderId())) {
            log.info("打印成功，订单为主订单，允许加菜-:" + order.getId());
            LogTemplateUtils.getParentOrderPrintSuccessByOrderType(brand.getBrandName(), order.getId(), order.getProductionStatus());
            LogTemplateUtils.getParentOrderPrintSuccessByPOSType(brand.getBrandName(), order.getId(), order.getProductionStatus());
            //现金 银联 新美大 积分 支付的时候  在付款中 服务员尚未确定的时候  不可加菜  有一段加菜真空期！  已废弃  wyj
//            if (order.getOrderMode() != ShopMode.CALL_NUMBER && order.getPayMode() != OrderPayMode.YL_PAY && order.getPayMode() != OrderPayMode.XJ_PAY
//                    && order.getPayMode() != OrderPayMode.SHH_PAY && order.getPayMode() != OrderPayMode.JF_PAY) {
            if (order.getOrderMode() != ShopMode.CALL_NUMBER && order.getOrderMode() != ShopMode.MEISHI) {
                if (order.getPayType() == PayType.NOPAY && order.getOrderState() == OrderState.PAYMENT) {

                } else if (order.getPayType() == PayType.NOPAY && order.getOrderState() == OrderState.CONFIRM) {

                } else {
                    if (order.getDistributionModeId() == DistributionType.RESTAURANT_MODE_ID) {
                        order.setAllowContinueOrder(true);
                    }
                }
            }
        } else {
            log.info("打印成功，订单为子订单:" + order.getId() + " pid:" + order.getParentOrderId());
            order.setAllowContinueOrder(false);
            order.setAllowAppraise(false);
            LogTemplateUtils.getChildOrderPrintSuccessByOrderType(brand.getBrandName(), order.getId(), order.getProductionStatus());
            LogTemplateUtils.getChildOrderPrintSuccessByPOSType(brand.getBrandName(), order.getId(), order.getProductionStatus());
        }
        order.setProductionStatus(ProductionStatus.PRINTED);
        order.setPrintOrderTime(new Date());
        order.setAllowCancel(false);
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
        //yz 2017/07/29计费系统
        Boolean flag = false;
        if (openBrandAccount != null) {
            if (openBrandAccount) {
                flag = true;
                if (accountSetting == null) {
                    accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());
                }
            } else {
                flag = brandSetting.getOpenBrandAccount() == 1;

            }
        }
        redisService.setnxTime(orderId + "actionPrintForNewPos", 1,60);
        orderMapper.updateByPrimaryKeySelective(order);


        log.info("======[订单号："+ orderId +"]订单打印成功, 当前状态为orderState：" + order.getOrderState() + "、productionStatus：" + order.getProductionStatus());

        //判断是否已经记录过该订单
        BrandAccountLog brandAccountLog = brandAccountLogService.selectOneBySerialNumAndBrandId(order.getId(), order.getBrandId());
        if (brandAccountLog != null) {
            jsonResult.setSuccess(true);
            jsonResult.setData(order);
            return jsonResult;
        } else {
            updateBrandAccount(order, flag, accountSetting);
            jsonResult.setSuccess(true);
            jsonResult.setData(order);
            return jsonResult;
        }
    }

    /**
     * yz 2017 07/31 计费系统 下单成功后发送短信--
     * 下单成功后 根据计费系统的设置 更新账户余额 和记录账户流水日志
     *
     * @param order
     * @param openBrandAccount
     * @param accountSetting
     */
    private void updateBrandAccount(Order order, Boolean openBrandAccount, AccountSetting accountSetting) {
//		if(order.getPayType()==PayType.NOPAY&&order.getOrderState()==1){//后付会走两次paySuccess 所以如果是后付 并且支付状态为1的时候就不记录
//			return;
//		}
        //在外层已经判断过 同一个品牌的同一订单不会记录两次 所以不用考虑后付打印两次的情况

        BigDecimal money = BigDecimal.ZERO;
        BrandAccountLog blog = new BrandAccountLog();
        BrandAccount brandAccount = brandAccountService.selectByBrandId(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());

        if (accountSetting == null) {
            BrandSetting bt = brandSettingService.selectByBrandId(order.getBrandId());
            accountSetting = accountSettingService.selectByBrandSettingId(bt.getId());
            openBrandAccount = bt.getOpenBrandAccount() == 1;
        }

        //根据用户id查询 用户已经消费的订单
        List<Order> list = orderMapper.selectOrderListByCustomerIdAndShopId(order.getShopDetailId(), order.getCustomerId());
        Boolean flag = false;//flag的标记是用于判断是否是回头用户 //默认不是
        if (!list.isEmpty()) {//这个人在这个店铺有过订单 说明是回头用户
            flag = true;
        }
        if (openBrandAccount) {//说明开启了品牌账户设置
            //就算出应扣费金额
            money = getJifeiMoney(order, accountSetting, flag);
            //BigDecimal remain = brandAccount.getAccountBalance().subtract(money);//剩余账户余额 = 账户余额减去-扣除的余额
            //这里不计算 剩余扣费的金额 因为在扣费的时候可能存在 其它人已经扣费了，这样账户余额就不是此时的账户余额

            blog.setSerialNumber(order.getId());
            blog.setCreateTime(new Date());
            blog.setBrandId(order.getBrandId());
            blog.setShopId(order.getShopDetailId());
            blog.setFoundChange(money.negate());
            blog.setGroupName(shopDetail.getName());
            blog.setAccountId(brandAccount.getId());
            //blog.setRemain(remain);
            blog.setOrderMoney(order.getOrderMoney());

            if (order.getParentOrderId() == null) {
                blog.setIsParent(true);
            }


            if (accountSetting.getOpenAllOrder() == 1) {//所有订单抽成
                blog.setDetail(DetailType.ORDER_SELL);
            }
            if (accountSetting.getOpenAllOrder() == 2) { //所有订单实际支付抽成
                blog.setDetail(DetailType.ORDER_REAL_SELL);
            }

            if (accountSetting.getOpenBackCustomerOrder() == 1) { //回头用户订单抽成
                blog.setDetail(DetailType.BACK_CUSTOMER_ORDER_SELL);
                //是要计算回头用户订单 如果该订单不是回头用户的订单则直接结束该方法
                if (!flag) {
                    return;
                }
            }

            if (accountSetting.getOpenBackCustomerOrder() == 2) {
                blog.setDetail(DetailType.BACK_CUSTOMER_ORDER_REAL_SELL);//回头用户订单实付抽成
                if (!flag) {
                    return;
                }
            }

            blog.setBehavior(BehaviorType.SELL);

            // 创建账户日志流水 和更新账户
//			Integer id = brandAccount.getId();
//			brandAccount = new BrandAccount();
//			brandAccount.setId(id);
//			brandAccount.setUpdateTime(new Date());

            brandAccountLogService.updateBrandAccountAndLog(blog, brandAccount.getId(), money);

            /**
             * 拉取最新的 品牌账户信息
             */
            BrandAccount brandAccount2 = brandAccountService.selectById(brandAccount.getId());

			/*
             判断是否要发短信
			**/
            Brand brand = brandService.selectByPrimaryKey(order.getBrandId());

            List<AccountNotice> noticeList = accountNoticeService.selectByAccountId(brandAccount.getId());

            Result result = BrandAccountSendUtil.sendSms(brandAccount2, noticeList, brand.getBrandName(), accountSetting);
            if (result.isSuccess()) {
                Long accountSettingId = accountSetting.getId();
                AccountSetting as = new AccountSetting();
                as.setId(accountSettingId);
                as.setType(1);
                accountSettingService.update(as);
                //发送消息队列通知 消费者24小时后再次查询账户余额情况 如果不符合要求则更改发短信为可以发状态
                log.info("有订单产生计费并且该品牌账户已经欠费---");
                log.info("开始发送延时消息队列--");
                mqMessageProducer.sendBrandAccountSms(brand.getId(), MQSetting.DELAY_TIME);
            }

        }
    }

    /**
     * 获取 所有订单的计费 / 回头用户消费订单计费
     *
     * @param order
     * @param accountSetting
     * @return
     */
    private BigDecimal getJifeiMoney(Order order, AccountSetting accountSetting, Boolean flag) {

        //定义抽成比率后的价格
        BigDecimal money = BigDecimal.ZERO;

        //定义订单的总额(实际支付的金额)
        BigDecimal jifeiMoney = BigDecimal.ZERO;

        if (accountSetting.getOpenAllOrder() == BrandAccountPayType.ALL_ORDER_MONEY) {//说明是 所有订单是/订单总额 抽成
//			if(order.getPayType()==PayType.PAY){//如果是先付
//				jifeiMoney = order.getOrderMoney();
//			}else if(order.getPayType()==PayType.NOPAY){//如果是后付
//				if(order.getAmountWithChildren().compareTo(BigDecimal.ZERO)>0){
//					jifeiMoney = order.getAmountWithChildren();
//				}else {
//					jifeiMoney = order.getOrderMoney();
//				}
//			}
            //不用考虑先付还是后付款
            jifeiMoney = order.getOrderMoney();
            money = jifeiMoney.multiply(new BigDecimal(accountSetting.getAllOrderValue())).divide(new BigDecimal(JifeiType.STATISH), JifeiType.NUM, BigDecimal.ROUND_HALF_UP);
            System.err.println("计算出订单总额抽成金额为：" + money);
        } else if (accountSetting.getOpenAllOrder() == BrandAccountPayType.REAL_ORDER_MONEY) {//说明是 所有订单/实际支付金额抽成
            List<OrderPaymentItem> orderPaymentItems = orderPaymentItemService.selectByOrderId(order.getId());
            if (orderPaymentItems != null && !orderPaymentItems.isEmpty()) {
                //实际支付 1.充值 2.微信 3支付宝 4刷卡 5现金 6闪慧 7会员
                for (OrderPaymentItem oi : orderPaymentItems) {
                    if (oi.getPaymentModeId() == PayMode.WEIXIN_PAY || oi.getPaymentModeId() == PayMode.WEIXIN_PAY ||
                            oi.getPaymentModeId() == PayMode.ALI_PAY || oi.getPaymentModeId() == PayMode.BANK_CART_PAY ||
                            oi.getPaymentModeId() == PayMode.CHARGE_PAY || oi.getPaymentModeId() == PayMode.SHANHUI_PAY ||
                            oi.getPaymentModeId() == PayMode.INTEGRAL_PAY
                            ) {
                        jifeiMoney = jifeiMoney.add(oi.getPayValue());
                    }
                }
            }
            log.info("订单id为：" + order.getId() + "jifeimoney为" + jifeiMoney);
            money = (jifeiMoney.multiply(new BigDecimal(accountSetting.getAllOrderValue()))).divide(new BigDecimal(JifeiType.STATISH), JifeiType.NUM, BigDecimal.ROUND_HALF_UP);
            System.err.println("计算出订单实付抽成金额为：" + money);
        } else if (accountSetting.getOpenBackCustomerOrder() == BrandAccountPayType.ALL_ORDER_MONEY && flag) {//回头用户订单  /订单总额抽成
            //是回头用户才会计算金额
            if (order.getPayType() == 0) {//如果是先付
                jifeiMoney = order.getOrderMoney();
            } else if (order.getPayType() == 1) {//如果是后付
                if (order.getAmountWithChildren().compareTo(BigDecimal.ZERO) > 0) {
                    jifeiMoney = order.getAmountWithChildren();
                } else {
                    jifeiMoney = order.getOrderMoney();
                }
            }
            money = (jifeiMoney.multiply(new BigDecimal(accountSetting.getAllOrderValue()))).divide(new BigDecimal(JifeiType.STATISH), JifeiType.NUM, BigDecimal.ROUND_HALF_UP);
            System.err.println("回头用户订单总额：" + money);
        } else if (accountSetting.getOpenBackCustomerOrder() == BrandAccountPayType.REAL_ORDER_MONEY && flag) {//回头用户 /实际支付总额抽成
            //是回头用户才会计算金额
            List<OrderPaymentItem> orderPaymentItems = orderPaymentItemService.selectByOrderId(order.getId());
            if (!orderPaymentItems.isEmpty()) {
                //实际支付 1.充值 2.微信 3支付宝 4刷卡 5现金 6闪慧 7会员
                for (OrderPaymentItem oi : orderPaymentItems) {
                    if (oi.getPaymentModeId() == PayMode.CHARGE_PAY || oi.getPaymentModeId() == PayMode.WEIXIN_PAY ||
                            oi.getPaymentModeId() == PayMode.ALI_PAY || oi.getPaymentModeId() == PayMode.BANK_CART_PAY ||
                            oi.getPaymentModeId() == PayMode.CRASH_PAY || oi.getPaymentModeId() == PayMode.SHANHUI_PAY ||
                            oi.getPaymentModeId() == PayMode.INTEGRAL_PAY
                            ) {
                        jifeiMoney = jifeiMoney.add(oi.getPayValue());
                    }
                }
            }
            money = (jifeiMoney.multiply(new BigDecimal(accountSetting.getBackCustomerOrderValue()))).divide(new BigDecimal(JifeiType.STATISH), JifeiType.NUM, BigDecimal.ROUND_HALF_UP);
            System.err.println("回头用户实付订单总额：" + money);
        }
        return money;
    }


    @Override
    public int printUpdate(String orderId) {
        Order o = new Order();
        o.setId(orderId);
        //说明外卖已取餐
        o.setProductionStatus(ProductionStatus.GET_IT);
        int count = orderMapper.updateByPrimaryKeySelective(o);
        //yz 2017/08/03 计费系统 添加账户设置(简单版) ---resto+外卖订单
        try {
            o = orderMapper.selectByPrimaryKey(orderId);
            Brand brand = brandService.selectByPrimaryKey(o.getBrandId());
            BrandSetting brandSetting = brandSettingService.selectById(brand.getBrandSettingId());
//			BrandSetting brandSetting = brandSettingService.selectByBrandId(o.getBrandId());

            if (brandSetting != null && JifeiType.TOTAL_ORDER_DRAWAL.equals(brandSetting.getOpenBrandAccount())) {//说明开启了品牌账户
                //查询品牌账户设置
                AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());
                //定义抽成的金额
                BigDecimal money = BigDecimal.ZERO;

                if (JifeiType.TOTAL_ORDER_DRAWAL.equals(accountSetting.getOpenOutFoodOrder())) {//开启resto外卖订单 并且按订单总额抽成
                    //计算resto外卖 的 抽成金额 (外卖都是先付所以就直接计算)
                    money = o.getAmountWithChildren().compareTo(BigDecimal.ZERO) > 0 ? o.getAmountWithChildren() : o.getOrderMoney();
                } else if (JifeiType.TOTAL_ORDER_DRAWAL.equals(accountSetting.getOpenOutFoodOrder())) {//开启resto外卖订单 并且按实际支付 抽成
                    List<OrderPaymentItem> orderPaymentItemList = orderPaymentItemService.selectByOrderId(o.getId());
                    if (orderPaymentItemList != null && !orderPaymentItemList.isEmpty()) {
                        for (OrderPaymentItem oi : orderPaymentItemList) {
                            //实际支付 1.充值 2.微信 3支付宝 4刷卡 5现金 6闪慧 7会员
                            if (oi.getPaymentModeId() == PayMode.CHARGE_PAY || oi.getPaymentModeId() == PayMode.WEIXIN_PAY ||
                                    oi.getPaymentModeId() == PayMode.ALI_PAY || oi.getPaymentModeId() == PayMode.BANK_CART_PAY ||
                                    oi.getPaymentModeId() == PayMode.CRASH_PAY || oi.getPaymentModeId() == PayMode.SHANHUI_PAY ||
                                    oi.getPaymentModeId() == PayMode.INTEGRAL_PAY
                                    ) {
                                money = money.add(oi.getPayValue());
                            }
                        }
                    }
                }
                //记录日志 和更新账户
                BrandAccount brandAccount = brandAccountService.selectByBrandId(o.getBrandId());
                ShopDetail s = shopDetailService.selectByPrimaryKey(o.getShopDetailId());
                if (brandAccount == null || accountSetting == null) {
                    log.error("店铺" + s.getName() + "品牌账户异常或者品牌账户设置异常。。");
                    return count;
                } else {
                    BigDecimal remain = brandAccount.getAccountBalance().subtract(money);
                    BrandAccountLog blog = new BrandAccountLog();
                    blog.setSerialNumber(o.getSerialNumber());
                    blog.setCreateTime(new Date());
                    blog.setBrandId(o.getBrandId());
                    blog.setShopId(o.getShopDetailId());
                    blog.setFoundChange(money.negate());
                    blog.setGroupName(s.getName());
                    blog.setAccountId(brandAccount.getId());
                    blog.setRemain(remain);
                    blog.setOrderMoney(o.getOrderMoney());
                    if (o.getParentOrderId() != null) {
                        blog.setIsParent(true);
                    }
                    if (JifeiType.TOTAL_ORDER_DRAWAL.equals(accountSetting.getOpenOutFoodOrder())) {//Resto+外卖订单抽成
                        blog.setDetail(DetailType.RESTO_OUT_FOOD_ORDER_SELL);
                    }
                    if (JifeiType.ACTUAL_ORDER_DRAWAL.equals(accountSetting.getOpenOutFoodOrder())) { //Resto+外卖订单实付抽成
                        blog.setDetail(DetailType.RESTO_OUT_FOOD_ORDER_REAL_SELL);
                    }

                    blog.setBehavior(BehaviorType.SELL);

                    // 创建账户日志流水 和更新账户
                    Integer id = brandAccount.getId();
                    brandAccount = new BrandAccount();
                    brandAccount.setId(id);
                    brandAccount.setUpdateTime(new Date());
                    brandAccount.setAccountBalance(remain);
                    brandAccountLogService.insert(blog);
                    brandAccountService.update(brandAccount);
                    //yz TODO//判断品牌账户是否需要发送通知(账户不足通知)---
                    List<AccountNotice> noticeList = accountNoticeService.selectByAccountId(brandAccount.getId());
                    //拉取最新的brandAccount
                    BrandAccount brandAccount2 = brandAccountService.selectById(brandAccount.getId());

                    Result result = BrandAccountSendUtil.sendSms(brandAccount2, noticeList, brand.getBrandName(), accountSetting);
                    if (result.isSuccess()) {
                        Long accountSettingId = accountSetting.getId();
                        AccountSetting as = new AccountSetting();
                        as.setId(accountSettingId);
                        as.setType(1);
                        accountSettingService.update(as);
                        //发送消息队列通知 消费者24小时后再次查询账户余额情况 如果不符合要求则更改发短信为可以发状态
                        log.info("有resto+外卖订单产生计费并且该品牌账户已经欠费---");
                        log.info("开始发送延时消息队列--");
                        mqMessageProducer.sendBrandAccountSms(brand.getId(), MQSetting.DELAY_TIME);
                    }
                }
            }
        } catch (Exception e) {
            log.info("resto外卖订单抽成出错 " + e.getMessage());
            e.printStackTrace();
        } finally {
            return count;
        }
    }

    @Override
    public List<Order> selectTodayOrder(String shopId, int[] proStatus) {
        Date date = DateUtil.getDateBegin(new Date());
        List<Order> orderList = orderMapper.selectShopOrderByDateAndProductionStates(shopId, date, proStatus);
        return orderList;
    }

    @Override
    public List<Order> selectReadyOrder(String currentShopId) {
        List<Order> order = orderMapper.selectReadyList(currentShopId);
        return order;
    }

    @Override
    public List<Order> selectPushOrder(String currentShopId, Long lastTime) {
        return orderProductionStateContainer.getPushOrderList(currentShopId, lastTime);
    }

    @Override
    public List<Order> selectCallOrder(String currentBrandId, Long lastTime) {
        return orderProductionStateContainer.getCallNowList(currentBrandId, lastTime);
    }


    @Override
    public List<Map<String, Object>> printKitchen(Order order, List<OrderItem> articleList, Integer oldDistributionModeId) {
        //每个厨房 所需制作的   菜品信息
        Map<String, List<OrderItem>> kitchenArticleMap = new HashMap<String, List<OrderItem>>();
        //厨房信息
        Map<String, Kitchen> kitchenMap = new HashMap<String, Kitchen>();
        Map<String, List<String>> recommendMap = new HashMap<>();
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        //遍历 订单集合
        for (OrderItem item : articleList) {
            if (item.getStatus() != 1) {
                continue;
            }
            //得到当前菜品 所关联的厨房信息
            String articleId = item.getArticleId();

            if (articleId.length() > 32) {
                articleId = item.getArticleId().substring(0, 32);
            }

            Article article = articleService.selectById(articleId);
            if (article.getVirtualId() != null && item.getType() != OrderItemType.MEALS_CHILDREN) {
                VirtualProducts virtualProducts = virtualProductsService.getVirtualProductsById(article.getVirtualId());
                if (virtualProducts != null && virtualProducts.getIsUsed().equals(Common.NO)) {
                    //启用
                    List<VirtualProductsAndKitchen> virtualProductsAndKitchens =
                            virtualProductsService.getVirtualProductsAndKitchenById(article.getVirtualId());
                    for (VirtualProductsAndKitchen virtual : virtualProductsAndKitchens) {

                        String kitchenId = String.valueOf(virtual.getKitchenId());
                        Kitchen kitchen = kitchenService.selectById(virtual.getKitchenId());
                        kitchenMap.put(kitchenId, kitchen);//保存厨房信息
                        //判断 厨房集合中 是否已经包含当前厨房信息
                        if (!kitchenArticleMap.containsKey(kitchenId)) {
                            //如果没有 则新建
                            kitchenArticleMap.put(kitchenId, new ArrayList<OrderItem>());
                            kitchenArticleMap.get(kitchenId).add(item);
                        } else {
                            if (CollectionUtils.isEmpty(kitchenArticleMap.get(kitchenId).get(0).getChildren())) {
                                List<OrderItem> child = new ArrayList<>();
                                child.add(item);
                                kitchenArticleMap.get(kitchenId).get(0).setChildren(child);
                            } else {
                                List<OrderItem> child = kitchenArticleMap.get(kitchenId).get(0).getChildren();
                                child.add(item);
                                kitchenArticleMap.get(kitchenId).get(0).setChildren(child);
                            }

                        }

                    }
                    continue;
                }

            }

            if (item.getType() == OrderItemType.MEALS_CHILDREN) {  // 套餐子品
                Kitchen kitchen = kitchenService.getItemKitchenId(item);
                if (kitchen == null) {
                    continue;
                }
                String kitchenId = kitchen.getId().toString();
                Printer printer = printerService.selectById(kitchen.getPrinterId());
                //判断订单出单模式
                switch (oldDistributionModeId) {
                    case DistributionType.RESTAURANT_MODE_ID:
                        if (printer.getSupportTangshi() == Common.NO) {
                            continue;
                        }
                        break;
                    case DistributionType.TAKE_IT_SELF:
                        if (printer.getSupportWaidai() == Common.NO) {
                            continue;
                        }
                        break;
                    case DistributionType.DELIVERY_MODE_ID:
                        if (printer.getSupportWaimai() == Common.NO) {
                            continue;
                        }
                        break;
                    default:
                        break;
                }
                if (printer.getTicketType().equals(TicketType.PRINT_TICKET) && shopDetail.getPrintType().equals(PrinterType.TOTAL)) { //总单出
                    continue;
                } else {
                    if (kitchen != null) {
                        kitchenMap.put(kitchenId, kitchen);
                        if (!kitchenArticleMap.containsKey(kitchenId)) {
                            //如果没有 则新建
                            kitchenArticleMap.put(kitchenId, new ArrayList<OrderItem>());
                        }
                        if (printer.getTicketType().equals(TicketType.PRINT_LABEL)) {
                            OrderItem parent = orderItemService.selectById(item.getParentId());
                            if (!kitchenArticleMap.get(kitchenId).contains(parent)) {
                                kitchenArticleMap.get(kitchenId).add(parent);
                            }
                        }
                        kitchenArticleMap.get(kitchenId).add(item);
                    } else {
//                        item.setPrintFailFlag(PrintStatus.PRINT_SUCCESS);
//                        orderItemService.update(item);
                    }
                }
            }

            if (OrderItemType.SETMEALS == item.getType()) { //如果类型是套餐那么continue
                if (shopDetail.getPrintType().equals(PrinterType.TOTAL)) { //总单出
                    Kitchen kitchen = kitchenService.selectMealKitchen(item);
                    if (kitchen != null) {
                        String kitchenId = kitchen.getId().toString();
                        kitchenMap.put(kitchenId, kitchen);
                        if (!kitchenArticleMap.containsKey(kitchenId)) {
                            //如果没有 则新建
                            kitchenArticleMap.put(kitchenId, new ArrayList<OrderItem>());
                        }
                        kitchenArticleMap.get(kitchenId).add(item);
                    }
                } else {
//                    item.setPrintFailFlag(PrintStatus.PRINT_SUCCESS);
//                    orderItemService.update(item);
                    continue;
                }
            } else {
                if (item.getType() != OrderItemType.MEALS_CHILDREN) {
                    if (item.getType() == OrderItemType.RECOMMEND) {
                        ArticleRecommend articleRecommend = articleRecommendMapper.getRecommendById(item.getRecommendId());
                        if (articleRecommend != null && articleRecommend.getPrintType() == PrinterType.KITCHEN) {
                            String kitchenId = articleRecommend.getKitchenId();
                            Kitchen kitchen = kitchenService.selectById(Integer.valueOf(kitchenId));
                            kitchenMap.put(kitchenId, kitchen);
                            if (!recommendMap.containsKey(kitchenId)) {
                                recommendMap.put(kitchenId, new ArrayList<String>());
                            }
                            if (!recommendMap.get(kitchenId).contains(item.getRecommendId())) {
                                recommendMap.get(kitchenId).add(item.getRecommendId());
                            }

                        } else {
                            List<Kitchen> kitchenList = kitchenService.selectInfoByArticleId(articleId);
                            for (Kitchen kitchen : kitchenList) {
                                String kitchenId = kitchen.getId().toString();
                                kitchenMap.put(kitchenId, kitchen);//保存厨房信息
                                //判断 厨房集合中 是否已经包含当前厨房信息
                                if (!kitchenArticleMap.containsKey(kitchenId)) {
                                    //如果没有 则新建
                                    kitchenArticleMap.put(kitchenId, new ArrayList<OrderItem>());
                                }
                                kitchenArticleMap.get(kitchenId).add(item);
                            }
                            if (CollectionUtils.isEmpty(kitchenList)) {
//                                item.setPrintFailFlag(PrintStatus.PRINT_SUCCESS);
//                                orderItemService.update(item);
                            }
                        }
                    } else {
                        List<Kitchen> kitchenList = kitchenService.selectInfoByArticleId(articleId);
                        for (Kitchen kitchen : kitchenList) {
                            String kitchenId = kitchen.getId().toString();
                            kitchenMap.put(kitchenId, kitchen);//保存厨房信息
                            //判断 厨房集合中 是否已经包含当前厨房信息
                            if (!kitchenArticleMap.containsKey(kitchenId)) {
                                //如果没有 则新建
                                kitchenArticleMap.put(kitchenId, new ArrayList<OrderItem>());
                            }
                            if (shopDetail.getSplitKitchen().equals(Common.YES)) {
                                int count = item.getCount();
                                for (int i = 0; i < count; i++) {
                                    item.setCount(1);
                                    kitchenArticleMap.get(kitchenId).add(item);
                                }
                            } else {
                                kitchenArticleMap.get(kitchenId).add(item);
                            }

                        }
                        if (CollectionUtils.isEmpty(kitchenList)) {
//                            item.setPrintFailFlag(PrintStatus.PRINT_SUCCESS);
//                            orderItemService.update(item);
                        }

                    }
                }


            }
        }
        Boolean check = true;
        for (OrderItem item : articleList) {
            if (item.getPrintFailFlag() == PrintStatus.PRINT_ERROR || item.getPrintFailFlag() == PrintStatus.UNPRINT) {
                check = false;
            }
        }
        if (check) {
            order.setPrintKitchenFlag(PrintStatus.PRINT_SUCCESS);
            update(order);
        }


        //打印线程集合
        List<Map<String, Object>> printTask = new ArrayList<Map<String, Object>>();

        //编列 厨房菜品 集合
        for (String kitchenId : kitchenArticleMap.keySet()) {
            Kitchen kitchen = kitchenMap.get(kitchenId);//得到厨房 信息
            if (kitchen == null) {
                continue;
            }
            Printer printer = printerService.selectById(kitchen.getPrinterId());//得到打印机信息
            if (printer == null) {
                continue;
            }
            //判断订单出单模式
            switch (oldDistributionModeId) {
                case DistributionType.RESTAURANT_MODE_ID:
                    if (printer.getSupportTangshi() == Common.NO) {
                        continue;
                    }
                    break;
                case DistributionType.TAKE_IT_SELF:
                    if (printer.getSupportWaidai() == Common.NO) {
                        continue;
                    }
                    break;
                case DistributionType.DELIVERY_MODE_ID:
                    if (printer.getSupportWaimai() == Common.NO) {
                        continue;
                    }
                    break;
                default:
                    break;
            }
            //生成厨房小票
            Map<String, Integer> countMap = new HashMap<>();
            for (OrderItem article : kitchenArticleMap.get(kitchenId)) {

                if (printer.getTicketType().equals(TicketType.PRINT_TICKET)) {
                    //小票
                    if (shopDetail.getIsPosNew() == Common.POS_NEW) {
                        getKitchenModelNew(article, order, printer, shopDetail, printTask);
                    } else {
                        getKitchenModel(article, order, printer, shopDetail, printTask);
                    }

                } else {
                    //贴纸
                    for (int i = 0; i < article.getCount(); i++) {
                        if (article.getType() == OrderItemType.SETMEALS) {
                            countMap.put(article.getArticleId(), 0);
                        } else if (article.getType() == OrderItemType.MEALS_CHILDREN) {
                            OrderItem parent = orderItemService.selectById(article.getParentId());
                            countMap.put(parent.getArticleId(), countMap.get(parent.getArticleId()) + 1);
                            countMap.put(article.getArticleId(), countMap.get(parent.getArticleId()));
                        } else if (countMap.containsKey(article.getArticleId())) {
                            countMap.put(article.getArticleId(), countMap.get(article.getArticleId()) + 1);
                        } else {
                            countMap.put(article.getArticleId(), 1);
                        }
                        if (shopDetail.getIsPosNew() == Common.POS_NEW) {
                            getKitchenLabelNew(article, order, printer, shopDetail, printTask, countMap, kitchenArticleMap.get(kitchenId));
                        } else {
                            getKitchenLabel(article, order, printer, shopDetail, printTask, countMap, kitchenArticleMap.get(kitchenId));
                        }

                    }

                }
            }
        }

        for (String kitchenId : recommendMap.keySet()) {
            Kitchen kitchen = kitchenMap.get(kitchenId);//得到厨房 信息
            Printer printer = printerService.selectById(kitchen.getPrinterId());//得到打印机信息
            if (printer == null) {
                continue;
            }
            //判断订单出单模式
            switch (oldDistributionModeId) {
                case DistributionType.RESTAURANT_MODE_ID:
                    if (printer.getSupportTangshi() == Common.NO) {
                        continue;
                    }
                    break;
                case DistributionType.TAKE_IT_SELF:
                    if (printer.getSupportWaidai() == Common.NO) {
                        continue;
                    }
                    break;
                case DistributionType.DELIVERY_MODE_ID:
                    if (printer.getSupportWaimai() == Common.NO) {
                        continue;
                    }
                    break;
                default:
                    break;
            }

            //生成厨房小票
            for (String recommendId : recommendMap.get(kitchenId)) {
                //保存 菜品的名称和数量
                if (shopDetail.getIsPosNew().equals(Common.YES)) {
                    getRecommendModelNew(recommendId, order, printer, shopDetail, printTask);
                } else {
                    getRecommendModel(recommendId, order, articleList, printer, shopDetail, printTask);
                }

            }
        }
        Brand brand = brandService.selectById(order.getBrandId());
        JSONArray json = new JSONArray(printTask);
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", shopDetail.getName());
        map.put("type", "posAction");
        map.put("content", "订单:" + order.getId() + "返回打印厨打模版" + json.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        if (printTask.size() != 0) {
            //从缓存里取当前订单的厨打打印次数， 如果为空则是第一次打印则当前打印次数为1
//            Integer printTimes = redisService.get(order.getId() + "KITCHEN") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "KITCHEN").toString()) + 1;
            Integer printTimes = redisService.get(order.getId() + "KITCHEN") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "KITCHEN").toString()) + 1;
            //订单返回厨打单打印模板视为已打印成功
//            redisService.set(order.getId() + "KITCHEN", printTimes);
            redisService.set(order.getId() + "KITCHEN", printTimes);
        }
        return printTask;
    }


    @Override
    public List<Map<String, Object>> printTurnTable(Order order, String oldtableNumber) {
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        List<Map<String, Object>> printTask = new ArrayList<>();
        List<Printer> ticketPrinter = new ArrayList<>();
        if (shopDetail.getTurntablePrintType() == 3) {
            ticketPrinter.addAll(printerService.selectPrintByType(order.getShopDetailId(), PrinterType.KITCHEN));
            ticketPrinter.addAll(printerService.selectPrintByType(order.getShopDetailId(), PrinterType.RECEPTION));
        } else if (shopDetail.getTurntablePrintType() == 1) {
            ticketPrinter.addAll(printerService.selectPrintByType(order.getShopDetailId(), PrinterType.KITCHEN));
        } else if (shopDetail.getTurntablePrintType() == 2) {
            ticketPrinter.addAll(printerService.selectPrintByType(order.getShopDetailId(), PrinterType.RECEPTION));
        }
        for (Printer printer : ticketPrinter) {
//            if (shopDetail.getIsPosNew().equals(Common.YES)) {
//                getTurnTableModelNew(order, printer,shopDetail,printTask,oldtableNumber);
//            } else {
            getTurnTableModel(order, printer, printTask, oldtableNumber);
//            }
        }
        Brand brand = brandService.selectById(order.getBrandId());
        JSONArray json = new JSONArray(printTask);
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", shopDetail.getName());
        map.put("type", "posAction");
        map.put("content", "订单:" + order.getId() + "返回打印换桌模版" + json.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        return printTask;
    }

    private void getTurnTableModel(Order order, Printer printer, List<Map<String, Object>> printTask, String oldtableNumber) {
        String tableNumber = order.getTableNumber() != null ? order.getTableNumber() : "";//桌号
        String serialNumber = order.getSerialNumber();//序列号
        String modeText = "转台";

        //保存基本信息
        Map<String, Object> print = new HashMap<String, Object>();
        print.put("PORT", printer.getPort());
        print.put("OID", order.getId());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_TASK_ID", print_id);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("ORDER_ID", serialNumber);
        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的转台单打印次数， 如果为空则是第一次打印则当前打印次数为1
//        Integer printTimes = redisService.get(order.getId() + "TURNTABLE") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "TURNTABLE").toString()) + 1;
        Integer printTimes = redisService.get(order.getId() + "TURNTABLE") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "TURNTABLE").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        data.put("DISTRIBUTION_MODE", modeText);
        data.put("TABLE_NUMBER", oldtableNumber);
        //添加当天打印订单的序号
        TableQrcode tableQrcode = tableQrcodeService.selectByTableNumberShopId(order.getShopDetailId(), Integer.valueOf(order.getTableNumber()));
        if (tableQrcode == null) {
            data.put("ORDER_NUMBER", "---");
        } else {
            if (tableQrcode.getAreaId() == null) {
                data.put("ORDER_NUMBER", "---");
            } else {
                Area area = areaService.selectById(tableQrcode.getAreaId());
                if (area == null) {
                    data.put("ORDER_NUMBER", "---");
                } else {
                    data.put("ORDER_NUMBER", area.getName());
                }
            }

        }
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        Map<String, Object> itemOld = new HashMap<String, Object>();
        itemOld.put("ARTICLE_COUNT", "台号");
        itemOld.put("ARTICLE_NAME", "               " + oldtableNumber);
        items.add(itemOld);
        Map<String, Object> itemNew = new HashMap<String, Object>();
        itemNew.put("ARTICLE_COUNT", "转至");
        itemNew.put("ARTICLE_NAME", "               " + tableNumber);
        items.add(itemNew);
        data.put("ITEMS", items);
        data.put("CUSTOMER_SATISFACTION", "");
        data.put("CUSTOMER_SATISFACTION_DEGREE", "");
        data.put("CUSTOMER_PROPERTY", "");
        print.put("DATA", data);
        print.put("STATUS", "0");
        print.put("TICKET_TYPE", TicketType.KITCHEN);
        //添加到 打印集合
        printTask.add(print);
//        redisService.set(print_id, print);
        redisService.set(print_id, print);
        //订单返回转台单打印模板视为已打印成功
//        redisService.set(order.getId() + "TURNTABLE", printTimes);
        redisService.set(order.getId() + "TURNTABLE", printTimes);
    }

    private void getTurnTableModelNew(Order order, Printer printer, ShopDetail shopDetail, List<Map<String, Object>> printTask, String oldtableNumber) {
        String tableNumber = order.getTableNumber() != null ? order.getTableNumber() : "";//桌号
        String serialNumber = order.getSerialNumber();//序列号
        String modeText = "转台";

        //保存基本信息
        Map<String, Object> print = new HashMap<String, Object>();
        print.put("ADD_TIME", System.currentTimeMillis());
        print.put("PORT", printer.getPort());
        print.put("OID", order.getId());
        print.put("IP", printer.getIp());
        print.put("PRINT_STATUS", 0);
        print.put("TABLE_NO", tableNumber);
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_TASK_ID", print_id);
        print.put("TASK_ID", ApplicationUtils.randomUUID());
        print.put("TASK_ORDER_ID", order.getId());
        print.put("LINE_WIDTH", shopDetail.getPageSize() == 0 ? 48 : 42);
        Map<String, Object> data = new HashMap<String, Object>();
        print.put("ORDER_ID", serialNumber);
        data.put("ORDER_ID", serialNumber);
        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的转台单打印次数， 如果为空则是第一次打印则当前打印次数为1
//        Integer printTimes = redisService.get(order.getId() + "TURNTABLE") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "TURNTABLE").toString()) + 1;
        Integer printTimes = redisService.get(order.getId() + "TURNTABLE") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "TURNTABLE").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        data.put("DISTRIBUTION_MODE", modeText);
        data.put("TABLE_NUMBER", oldtableNumber);
        //添加当天打印订单的序号
        TableQrcode tableQrcode = tableQrcodeService.selectByTableNumberShopId(order.getShopDetailId(), Integer.valueOf(order.getTableNumber()));
        if (tableQrcode == null) {
            data.put("ORDER_NUMBER", "---");
        } else {
            if (tableQrcode.getAreaId() == null) {
                data.put("ORDER_NUMBER", "---");
            } else {
                Area area = areaService.selectById(tableQrcode.getAreaId());
                if (area == null) {
                    data.put("ORDER_NUMBER", "---");
                } else {
                    data.put("ORDER_NUMBER", area.getName());
                }
            }

        }
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> itemOld = new HashMap<String, Object>();
        itemOld.put("ARTICLE_COUNT", "台号");
        itemOld.put("ARTICLE_NAME", "" + oldtableNumber);
        items.add(itemOld);
        Map<String, Object> itemNew = new HashMap<String, Object>();
        itemNew.put("ARTICLE_COUNT", 3);
        itemNew.put("ARTICLE_NAME", "" + tableNumber);
        items.add(itemNew);
        data.put("ITEMS", items);
        data.put("CUSTOMER_SATISFACTION", "");
        data.put("CUSTOMER_SATISFACTION_DEGREE", 0);
        data.put("CUSTOMER_PROPERTY", "");
        print.put("DATA", data);
        print.put("STATUS", "0");
        print.put("TICKET_TYPE", TicketTypeNew.TICKET);
        print.put("TICKET_MODE", TicketTypeNew.KITCHEN_TICKET);
        //添加到 打印集合
        printTask.add(print);
        redisService.set(print_id, print);
        //订单返回转台单打印模板视为已打印成功
        redisService.set(order.getId() + "TURNTABLE", printTimes);
    }


    private void getRecommendModel(String recommendId, Order order, List<OrderItem> articleList, Printer printer, ShopDetail shopDetail, List<Map<String, Object>> printTask) {
        //桌号
        String tableNumber = order.getTableNumber() != null ? order.getTableNumber() : "";
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
//        List<OrderItem> orderItems = orderitemMapper.getOrderItemByRecommendId(recommendId, order.getId());
        StringBuilder sb = new StringBuilder();
//        for (OrderItem orderItem : orderItems) {
//            String articleId = orderItem.getArticleId();
//
//            if (articleId.length() > 32) {
//                articleId = orderItem.getArticleId().substring(0, 32);
//            }
//            Article article = articleService.selectById(articleId);
//            if (article.getVirtualId() == null) {
//                Map<String, Object> item = new HashMap<String, Object>();
//                item.put("ARTICLE_NAME", orderItem.getArticleName());
//                sb.append(orderItem.getArticleName() + " ");
//                item.put("ARTICLE_COUNT", orderItem.getCount());
//                items.add(item);
//            }
//
//        }
        for (OrderItem orderItem : articleList) {
            String articleId = orderItem.getArticleId();

            if (articleId.length() > 32) {
                articleId = orderItem.getArticleId().substring(0, 32);
            }
            Article article = articleService.selectById(articleId);
            if (article.getVirtualId() == null) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("ARTICLE_NAME", orderItem.getArticleName());
                sb.append(orderItem.getArticleName() + " ");
                item.put("ARTICLE_COUNT", orderItem.getCount());
                items.add(item);
            }

        }

        String serialNumber = order.getSerialNumber();//序列号
        String modeText = getModeText(order);//就餐模式

        //保存基本信息
        Map<String, Object> print = new HashMap<String, Object>();
        print.put("PORT", printer.getPort());
        print.put("OID", order.getId());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_TASK_ID", print_id);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("ORDER_ID", serialNumber);
        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的转台单打印次数， 如果为空则是第一次打印则当前打印次数为1
        Integer printTimes = redisService.get(order.getId() + "KITCHEN") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "KITCHEN").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        data.put("DISTRIBUTION_MODE", modeText);
        data.put("TABLE_NUMBER", tableNumber);
        //添加当天打印订单的序号

        data.put("ORDER_NUMBER", redisService.get(order.getId() + "orderNumber"));
//        data.put("ORDER_NUMBER", nextNumber(order.getShopDetailId(), order.getId()));
        data.put("ITEMS", items);
        com.resto.api.appraise.entity.Appraise appraise = newAppraiseService.selectAppraiseByCustomerId(order.getBrandId(),order.getCustomerId(), order.getShopDetailId());
        StringBuilder star = new StringBuilder();
        BigDecimal level = new BigDecimal(0);
        if (appraise != null) {
            star.append("★★★★★☆☆☆☆☆".substring(5 - appraise.getLevel(), 10 - appraise.getLevel()));
            Map<String, Object> appriseCount = newAppraiseService.selectCustomerAppraiseAvg(order.getBrandId(),order.getCustomerId());
            level = new BigDecimal(Integer.valueOf(appriseCount.get("sum").toString()))
                    .divide(new BigDecimal(Integer.valueOf(appriseCount.get("count").toString())), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            star.append("☆☆☆☆☆");
        }
        StringBuilder gao = new StringBuilder();
        if (shopDetail.getIsUserIdentity().equals(1)) {
            //得到有限制的情况下用户的订单数
            int gaoCount = orderMapper.selectByCustomerCount(order.getCustomerId());
            if (gaoCount>1) {
                gao.append("消费" + gaoCount + "次");
            } else {
                gao.append("新顾客");
            }
        }
        data.put("CUSTOMER_SATISFACTION", star.toString());
        data.put("CUSTOMER_SATISFACTION_DEGREE", level);
        Account account = accountService.selectAccountAndLogByCustomerId(order.getCustomerId());
        StringBuffer customerStr = new StringBuffer();
        if (account != null) {
            customerStr.append("余额：" + account.getRemain() + " ");
        } else {
            customerStr.append("余额：0 ");
        }
        customerStr.append("" + gao.toString() + " ");
        Customer customer = customerService.selectById(order.getCustomerId());
        if (customer != null) {
            CustomerDetail customerDetail = customerDetailMapper.selectByPrimaryKey(customer.getCustomerDetailId());
            if (customerDetail != null) {
                if (customerDetail.getBirthDate() != null) {
                    if (DateUtil.formatDate(customerDetail.getBirthDate(), "MM-dd")
                            .equals(DateUtil.formatDate(new Date(), "MM-dd"))) {
                        customerStr.append("★" + DateUtil.formatDate(customerDetail.getBirthDate(), "yyyy-MM-dd") + "★");
                    }
                }
            }
        }
        data.put("CUSTOMER_PROPERTY", customerStr.toString());
        print.put("DATA", data);
        print.put("STATUS", "0");
        print.put("TICKET_TYPE", TicketType.KITCHEN);
        //保存打印配置信息
//                print.put("ORDER_ID", serialNumber);
//                print.put("KITCHEN_NAME", kitchen.getName());
//                print.put("TABLE_NO", tableNumber);
        //添加到 打印集合
        printTask.add(print);
        redisService.set(print_id, print);
        List<String> printList = (List<String>) redisService.get(shopDetail.getId() + "printList");
        if (printList == null) {
            printList = new ArrayList<>();
        }
        printList.add(print_id);
        if (customer != null) {
            redisService.set(print_id + "customer", customer);
        }
        redisService.set(print_id + "article", sb.toString());
        redisService.set(shopDetail.getId() + "printList", printList);
    }

    private void getRecommendModelNew(String recommendId, Order order, Printer printer, ShopDetail shopDetail, List<Map<String, Object>> printTask) {
        //桌号
        String tableNumber = order.getTableNumber() != null ? order.getTableNumber() : "";
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        List<OrderItem> orderItems = orderitemMapper.getOrderItemByRecommendId(recommendId, order.getId());
        StringBuilder sb = new StringBuilder();
        for (OrderItem orderItem : orderItems) {
            String articleId = orderItem.getArticleId();

            if (articleId.length() > 32) {
                articleId = orderItem.getArticleId().substring(0, 32);
            }
            Article article = articleService.selectById(articleId);
            if (article.getVirtualId() == null) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("ARTICLE_NAME", orderItem.getArticleName());
                sb.append(orderItem.getArticleName() + " ");
                item.put("ARTICLE_COUNT", orderItem.getCount());
                items.add(item);
            }

        }
        String serialNumber = order.getSerialNumber();//序列号
        String modeText = getModeText(order);//就餐模式

        //保存基本信息
        Map<String, Object> print = new HashMap<String, Object>();
        print.put("PORT", printer.getPort());
        print.put("OID", order.getId());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_STATUS", 0);
        ArticleRecommend articleRecommend = articleRecommendMapper.getRecommendById(recommendId);
        print.put("PRINT_TASK_ID", ApplicationUtils.randomUUID());
        print.put("TASK_ID", recommendId);

        print.put("TASK_ORDER_ID", order.getId());
        print.put("LINE_WIDTH", shopDetail.getPageSize() == 0 ? 48 : 42);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("ORDER_ID", serialNumber);
        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的转台单打印次数， 如果为空则是第一次打印则当前打印次数为1
        Integer printTimes = redisService.get(order.getId() + "KITCHEN") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "KITCHEN").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        data.put("DISTRIBUTION_MODE", modeText);
        data.put("TABLE_NUMBER", tableNumber);
        //添加当天打印订单的序号

        data.put("ORDER_NUMBER", redisService.get(order.getId() + "orderNumber"));
//        data.put("ORDER_NUMBER", nextNumber(order.getShopDetailId(), order.getId()));
        data.put("ITEMS", items);
        com.resto.api.appraise.entity.Appraise appraise = newAppraiseService.selectAppraiseByCustomerId(order.getBrandId(),order.getCustomerId(), order.getShopDetailId());
        StringBuilder star = new StringBuilder();
        BigDecimal level = new BigDecimal(0);
        if (appraise != null) {
            star.append("★★★★★☆☆☆☆☆".substring(5 - appraise.getLevel(), 10 - appraise.getLevel()));
            Map<String, Object> appriseCount = newAppraiseService.selectCustomerAppraiseAvg(order.getBrandId(),order.getCustomerId());
            level = new BigDecimal(Integer.valueOf(appriseCount.get("sum").toString()))
                    .divide(new BigDecimal(Integer.valueOf(appriseCount.get("count").toString())), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            star.append("☆☆☆☆☆");
        }
        StringBuilder gao = new StringBuilder();
        if (shopDetail.getIsUserIdentity().equals(1)) {
            //得到有限制的情况下用户的订单数
            int gaoCount = orderMapper.selectByCustomerCount(order.getCustomerId());
            if (gaoCount>1) {
                gao.append("消费" + gaoCount + "次");
            } else {
                gao.append("新顾客");
            }
        }
        data.put("CUSTOMER_SATISFACTION", star.toString());
        data.put("CUSTOMER_SATISFACTION_DEGREE", level);
        Account account = accountService.selectAccountAndLogByCustomerId(order.getCustomerId());
        StringBuffer customerStr = new StringBuffer();
        if (account != null) {
            customerStr.append("余额：" + account.getRemain() + " ");
        } else {
            customerStr.append("余额：0 ");
        }
        customerStr.append("" + gao.toString() + " ");
        Customer customer = customerService.selectById(order.getCustomerId());
        if (customer != null) {
            CustomerDetail customerDetail = customerDetailMapper.selectByPrimaryKey(customer.getCustomerDetailId());
            if (customerDetail != null) {
                if (customerDetail.getBirthDate() != null) {
                    if (DateUtil.formatDate(customerDetail.getBirthDate(), "MM-dd")
                            .equals(DateUtil.formatDate(new Date(), "MM-dd"))) {
                        customerStr.append("★" + DateUtil.formatDate(customerDetail.getBirthDate(), "yyyy-MM-dd") + "★");
                    }
                }
            }
        }
        data.put("CUSTOMER_PROPERTY", customerStr.toString());
        print.put("DATA", data);
        print.put("STATUS", "0");
        print.put("TICKET_TYPE", TicketTypeNew.TICKET);
        print.put("TICKET_MODE", TicketTypeNew.KITCHEN_TICKET);
        //保存打印配置信息
//                print.put("ORDER_ID", serialNumber);
//                print.put("KITCHEN_NAME", kitchen.getName());
//                print.put("TABLE_NO", tableNumber);
        //添加到 打印集合
        printTask.add(print);
        redisService.set(print_id, print);
        List<String> printList = (List<String>) redisService.get(shopDetail.getId() + "printList");
        if (printList == null) {
            printList = new ArrayList<>();
        }
        printList.add(print_id);
        if (customer != null) {
            redisService.set(print_id + "customer", customer);
        }
        redisService.set(print_id + "article", sb.toString());
        redisService.set(shopDetail.getId() + "printList", printList);
    }


    private void getKitchenLabel(OrderItem article, Order order, Printer printer,
                                 ShopDetail shopDetail, List<Map<String, Object>> printTask, Map map, List<OrderItem> orderItems) {
        int currentCount = (int) map.get(article.getArticleId());
        int i = 0;

        for (OrderItem orderItem : orderItems) {
            if (article == null) {
                continue;
            }

            if (article.getType() == OrderItemType.SETMEALS
                    && orderItem.getParentId() != null && orderItem.getParentId().equals(article.getId())) {
                i++;
            } else if (article.getType() == OrderItemType.MEALS_CHILDREN
                    && article.getParentId().equals(orderItem.getParentId())) {
                i++;
            } else if (article.getType() != OrderItemType.SETMEALS
                    && article.getType() != OrderItemType.MEALS_CHILDREN && article.getArticleId().equals(orderItem.getArticleId())) {
                i += article.getCount().intValue();
            }
        }


        String tableNumber = order.getTableNumber() != null ? order.getTableNumber() : "";
        String modeText = getModeText(order);//就餐模式
        Map<String, Object> print = new HashMap<String, Object>();
        print.put("TABLE_NO", tableNumber);
        print.put("OID", order.getId());
        print.put("KITCHEN_NAME", printer.getName());
        print.put("PORT", printer.getPort());
        print.put("ORDER_ID", order.getSerialNumber());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_TASK_ID", print_id);
        print.put("ADD_TIME", new Date());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("ORDER_ID", order.getSerialNumber());
        data.put("ARTICLE_NAME", article.getArticleName());
        data.put("ARTICLE_NUMBER", currentCount + "/" + i);
        data.put("DISTRIBUTION_MODE", modeText);
        data.put("ORIGINAL_AMOUNT", order.getOriginalAmount());
        data.put("RESTAURANT_ADDRESS", shopDetail.getAddress());
        data.put("REDUCTION_AMOUNT", order.getOriginalAmount().subtract(order.getAmountWithChildren().doubleValue() == 0.0 ? order.getOrderMoney() : order.getAmountWithChildren()));
        Customer customer = customerService.selectById(order.getCustomerId());
        String phone = order.getVerCode();
        if (customer != null) {
            phone = StringUtils.isEmpty(customer.getTelephone()) ? order.getVerCode() : customer.getTelephone();
        }

        data.put("CUSTOMER_TEL", phone);
        data.put("TABLE_NUMBER", order.getTableNumber());
        data.put("PAYMENT_AMOUNT", order.getOrderMoney());
        data.put("RESTAURANT_NAME", shopDetail.getName());

        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的厨打打印次数， 如果为空则是第一次打印则当前打印次数为1
        Integer printTimes = redisService.get(order.getId() + "KITCHEN") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "KITCHEN").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        data.put("ARTICLE_COUNT", order.getArticleCount());
        print.put("DATA", data);
        print.put("STATUS", 0);
        print.put("TICKET_TYPE", TicketType.RESTAURANTLABEL);
        printTask.add(print);
        redisService.set(print_id, print);
        if (customer != null) {
            redisService.set(print_id + "customer", customer);
        }
        redisService.set(print_id + "article", article.getArticleName());
        List<String> printList = (List<String>) redisService.get(shopDetail.getId() + "printList");
        if (printList == null) {
            printList = new ArrayList<>();
        }
        printList.add(print_id);
        redisService.set(shopDetail.getId() + "printList", printList);

    }


    private void getKitchenLabelNew(OrderItem article, Order order, Printer printer,
                                    ShopDetail shopDetail, List<Map<String, Object>> printTask, Map map, List<OrderItem> orderItems) {
        int currentCount = (int) map.get(article.getArticleId());
        int i = 0;

        for (OrderItem orderItem : orderItems) {
            if (article == null) {
                continue;
            }

            if (article.getType() == OrderItemType.SETMEALS
                    && article.getId().equals(orderItem.getParentId())) {
                i++;
            } else if (article.getType() == OrderItemType.MEALS_CHILDREN
                    && article.getParentId() != null && article.getParentId().equals(orderItem.getParentId())) {
                i++;
            } else if (article.getType() != OrderItemType.SETMEALS
                    && article.getType() != OrderItemType.MEALS_CHILDREN && article.getArticleId().equals(orderItem.getArticleId())) {
                i += article.getCount().intValue();
            }
        }


        String tableNumber = order.getTableNumber() != null ? order.getTableNumber() : "";
        String modeText = getModeText(order);//就餐模式
        Map<String, Object> print = new HashMap<String, Object>();
        print.put("PRINT_STATUS", order.getPrintKitchenFlag());
        print.put("PRINT_TASK_ID", ApplicationUtils.randomUUID());
        print.put("TASK_ID", article.getId());
        print.put("TASK_ORDER_ID", article.getOrderId());
        print.put("TABLE_NO", tableNumber);
        print.put("OID", order.getId());
        print.put("KITCHEN_NAME", printer.getName());
        print.put("PORT", printer.getPort());
        print.put("ORDER_ID", order.getSerialNumber());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("ADD_TIME", System.currentTimeMillis());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("ORDER_ID", order.getSerialNumber());
        data.put("ARTICLE_NAME", article.getArticleName());
        data.put("ARTICLE_NUMBER", currentCount + "/" + i);
        data.put("DISTRIBUTION_MODE", modeText);
        data.put("ORIGINAL_AMOUNT", order.getOriginalAmount());
        data.put("RESTAURANT_ADDRESS", shopDetail.getAddress());
        data.put("REDUCTION_AMOUNT", order.getOriginalAmount().subtract(order.getAmountWithChildren().doubleValue() == 0.0 ? order.getOrderMoney() : order.getAmountWithChildren()));
        Customer customer = customerService.selectById(order.getCustomerId());
        String phone = order.getVerCode();
        if (customer != null) {
            phone = StringUtils.isEmpty(customer.getTelephone()) ? order.getVerCode() : customer.getTelephone();
        }

        data.put("CUSTOMER_TEL", phone);
        data.put("TABLE_NUMBER", order.getTableNumber());
        data.put("PAYMENT_AMOUNT", order.getOrderMoney());
        data.put("RESTAURANT_NAME", shopDetail.getName());

        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的厨打打印次数， 如果为空则是第一次打印则当前打印次数为1
        Integer printTimes = redisService.get(order.getId() + "KITCHEN") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "KITCHEN").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        data.put("ARTICLE_COUNT", order.getArticleCount());
        print.put("DATA", data);
        print.put("STATUS", 0);
        print.put("TICKET_TYPE", TicketTypeNew.LABEL);
        print.put("TICKET_MODE", TicketTypeNew.RESTAURANT_LABEL);
        printTask.add(print);
        redisService.set(print_id, print);
        if (customer != null) {
            redisService.set(print_id + "customer", customer);
        }
        redisService.set(print_id + "article", article.getArticleName());
        List<String> printList = (List<String>) redisService.get(shopDetail.getId() + "printList");
        if (printList == null) {
            printList = new ArrayList<>();
        }
        printList.add(print_id);
        redisService.set(shopDetail.getId() + "printList", printList);

    }


    private void getKitchenModel(OrderItem article, Order order, Printer printer, ShopDetail shopDetail, List<Map<String, Object>> printTask) {
        //保存 菜品的名称和数量
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        StringBuilder sb = new StringBuilder();
        Map<String, Object> item = new HashMap<String, Object>();
        sb.append(article.getArticleName() + " ");
        item.put("ARTICLE_NAME", article.getArticleName());
        item.put("ARTICLE_COUNT", article.getCount());
        items.add(item);
        String serialNumber = order.getSerialNumber();//序列号
        String modeText = getModeText(order);//就餐模式


        if (article.getType() == OrderItemType.SETMEALS) {
            if (article.getChildren() != null && !article.getChildren().isEmpty()) {
                List<OrderItem> list = orderitemMapper.getListBySort(article.getId(), article.getArticleId());
                for (OrderItem obj : list) {
                    Map<String, Object> child_item = new HashMap<String, Object>();
                    child_item.put("ARTICLE_NAME", obj.getArticleName());
                    sb.append(obj.getArticleName() + " ");
                    if (order.getIsRefund() != null && order.getIsRefund().equals(Common.YES)) {
                        child_item.put("ARTICLE_COUNT", obj.getRefundCount());
                    } else {
                        child_item.put("ARTICLE_COUNT", obj.getCount());
                    }

                    items.add(child_item);
                }
            }
        } else {
            if (!CollectionUtils.isEmpty(article.getChildren())) {
                for (OrderItem obj : article.getChildren()) {
                    Map<String, Object> child_item = new HashMap<String, Object>();
                    child_item.put("ARTICLE_NAME", obj.getArticleName());
                    sb.append(obj.getArticleName() + " ");
                    if (order.getIsRefund() != null && order.getIsRefund().equals(Common.YES)) {
                        child_item.put("ARTICLE_COUNT", obj.getRefundCount());
                    } else {
                        child_item.put("ARTICLE_COUNT", obj.getCount());
                    }

                    items.add(child_item);
                }
            }
        }

        //保存基本信息
        Map<String, Object> print = new HashMap<String, Object>();
        print.put("PORT", printer.getPort());
        print.put("OID", order.getId());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_TASK_ID", print_id);
        print.put("ORDER_ID", serialNumber);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("ORDER_ID", serialNumber);
        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的厨打打印次数， 如果为空则是第一次打印则当前打印次数为1
        Integer printTimes = redisService.get(order.getId() + "KITCHEN") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "KITCHEN").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        data.put("DISTRIBUTION_MODE", modeText);
        data.put("TABLE_NUMBER", order.getTableNumber());
        if (StringUtils.isNotBlank(order.getRemark())) {
            data.put("MEMO", "备注：" + order.getRemark());
        }
//        data.put("ORDER_NUMBER", nextNumber(order.getShopDetailId(), order.getId()));
        data.put("ORDER_NUMBER", redisService.get(order.getId() + "orderNumber"));
        data.put("ITEMS", items);
        Appraise appraise = newAppraiseService.selectAppraiseByCustomerId(order.getBrandId(),order.getCustomerId(), order.getShopDetailId());
        StringBuilder star = new StringBuilder();
        BigDecimal level = new BigDecimal(0);
        if (appraise != null) {
            star.append("★★★★★☆☆☆☆☆".substring(5 - appraise.getLevel(), 10 - appraise.getLevel()));
            Map<String, Object> appriseCount = newAppraiseService.selectCustomerAppraiseAvg(order.getBrandId(),order.getCustomerId());
            level = new BigDecimal(Integer.valueOf(appriseCount.get("sum").toString()))
                    .divide(new BigDecimal(Integer.valueOf(appriseCount.get("count").toString())), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            star.append("☆☆☆☆☆");
        }
        StringBuilder gao = new StringBuilder();
        if (shopDetail.getIsUserIdentity().equals(1)) {
            //得到有限制的情况下用户的订单数
            int gaoCount = orderMapper.selectByCustomerCount(order.getCustomerId());
            if (gaoCount>1) {
                gao.append("消费" + gaoCount + "次");
            } else {
                gao.append("新顾客");
            }
        }
        data.put("CUSTOMER_SATISFACTION", star.toString());
        data.put("CUSTOMER_SATISFACTION_DEGREE", level);
        Account account = accountService.selectAccountAndLogByCustomerId(order.getCustomerId());
        StringBuffer customerStr = new StringBuffer();
        if (account != null) {
            customerStr.append("余额：" + account.getRemain() + " ");
        } else {
            customerStr.append("余额：0 ");
        }
        customerStr.append("" + gao.toString() + " ");
        Customer customer = customerService.selectById(order.getCustomerId());
        if (customer != null) {
            CustomerDetail customerDetail = customerDetailMapper.selectByPrimaryKey(customer.getCustomerDetailId());
            if (customerDetail != null) {
                if (customerDetail.getBirthDate() != null) {
                    if (DateUtil.formatDate(customerDetail.getBirthDate(), "MM-dd")
                            .equals(DateUtil.formatDate(new Date(), "MM-dd"))) {
                        customerStr.append("★" + DateUtil.formatDate(customerDetail.getBirthDate(), "yyyy-MM-dd") + "★");
                    }
                }
            }
        }
        data.put("CUSTOMER_PROPERTY", customerStr.toString());
        print.put("DATA", data);
        print.put("STATUS", "0");
        print.put("TICKET_TYPE", TicketType.KITCHEN);
        //保存打印配置信息
//                print.put("ORDER_ID", serialNumber);
//                print.put("KITCHEN_NAME", kitchen.getName());
//                print.put("TABLE_NO", tableNumber);
        //添加到 打印集合
        printTask.add(print);

        redisService.set(print_id, print);
        if (customer != null) {
            redisService.set(print_id + "customer", customer);
        }
        redisService.set(print_id + "article", sb.toString());
        List<String> printList = (List<String>) redisService.get(shopDetail.getId() + "printList");
        if (printList == null) {
            printList = new ArrayList<>();
        }
        printList.add(print_id);
        redisService.set(shopDetail.getId() + "printList", printList);
    }


    private void getKitchenModelNew(OrderItem article, Order order, Printer printer, ShopDetail shopDetail, List<Map<String, Object>> printTask) {
        //保存 菜品的名称和数量
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        StringBuilder sb = new StringBuilder();
        Map<String, Object> item = new HashMap<String, Object>();
        sb.append(article.getArticleName() + " ");
        item.put("ARTICLE_NAME", article.getArticleName());
        item.put("ARTICLE_COUNT", article.getCount());
        items.add(item);
        String serialNumber = order.getSerialNumber();//序列号
        String modeText = getModeText(order);//就餐模式


        if (article.getType() == OrderItemType.SETMEALS) {
            if (article.getChildren() != null && !article.getChildren().isEmpty()) {
                List<OrderItem> list = orderitemMapper.getListBySort(article.getId(), article.getArticleId());
                for (OrderItem obj : list) {
                    Map<String, Object> child_item = new HashMap<String, Object>();
                    child_item.put("ARTICLE_NAME", obj.getArticleName());
                    sb.append(obj.getArticleName() + " ");
                    if (order.getIsRefund() != null && order.getIsRefund().equals(Common.YES)) {
                        child_item.put("ARTICLE_COUNT", obj.getRefundCount());
                    } else {
                        child_item.put("ARTICLE_COUNT", obj.getCount());
                    }

                    items.add(child_item);
                }
            }
        } else {
            if (!CollectionUtils.isEmpty(article.getChildren())) {
                for (OrderItem obj : article.getChildren()) {
                    Map<String, Object> child_item = new HashMap<String, Object>();
                    child_item.put("ARTICLE_NAME", obj.getArticleName());
                    sb.append(obj.getArticleName() + " ");
                    if (order.getIsRefund() != null && order.getIsRefund().equals(Common.YES)) {
                        child_item.put("ARTICLE_COUNT", obj.getRefundCount());
                    } else {
                        child_item.put("ARTICLE_COUNT", obj.getCount());
                    }

                    items.add(child_item);
                }
            }
        }

        //保存基本信息
        Map<String, Object> print = new HashMap<String, Object>();
        print.put("PORT", printer.getPort());
        print.put("TABLE_NO", order.getTableNumber());
        print.put("OID", order.getId());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_STATUS", order.getPrintKitchenFlag());
        print.put("PRINT_TASK_ID", ApplicationUtils.randomUUID());
        print.put("TASK_ID", article.getId());
        print.put("TASK_ORDER_ID", article.getOrderId());
        print.put("LINE_WIDTH", shopDetail.getPageSize() == 0 ? 48 : 42);
        print.put("ADD_TIME", System.currentTimeMillis());
        print.put("ORDER_ID", serialNumber);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("ORDER_ID", serialNumber);
        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的厨打打印次数， 如果为空则是第一次打印则当前打印次数为1
        Integer printTimes = redisService.get(order.getId() + "KITCHEN") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "KITCHEN").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        data.put("DISTRIBUTION_MODE", modeText);
        data.put("TABLE_NUMBER", order.getTableNumber());
        if (StringUtils.isNotBlank(order.getRemark())) {
            data.put("MEMO", "备注：" + order.getRemark());
        }
//        data.put("ORDER_NUMBER", nextNumber(order.getShopDetailId(), order.getId()));
        data.put("ORDER_NUMBER", redisService.get(order.getId() + "orderNumber"));
        data.put("ITEMS", items);
        com.resto.api.appraise.entity.Appraise appraise = newAppraiseService.selectAppraiseByCustomerId(order.getBrandId(),order.getCustomerId(), order.getShopDetailId());
        StringBuilder star = new StringBuilder();
        BigDecimal level = new BigDecimal(0);
        if (appraise != null) {
            star.append("★★★★★☆☆☆☆☆".substring(5 - appraise.getLevel(), 10 - appraise.getLevel()));
            Map<String, Object> appriseCount = newAppraiseService.selectCustomerAppraiseAvg(order.getBrandId(),order.getCustomerId());
            level = new BigDecimal(Integer.valueOf(appriseCount.get("sum").toString()))
                    .divide(new BigDecimal(Integer.valueOf(appriseCount.get("count").toString())), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            star.append("☆☆☆☆☆");
        }
        StringBuilder gao = new StringBuilder();
        if (shopDetail.getIsUserIdentity().equals(1)) {
            //得到有限制的情况下用户的订单数
            int gaoCount = orderMapper.selectByCustomerCount(order.getCustomerId());
            if (gaoCount > 1) {
                gao.append("消费" + gaoCount + "次");
            } else {
                gao.append("新顾客");
            }
        }
        data.put("CUSTOMER_SATISFACTION", star.toString());
        data.put("CUSTOMER_SATISFACTION_DEGREE", level);
        Account account = accountService.selectAccountAndLogByCustomerId(order.getCustomerId());
        StringBuffer customerStr = new StringBuffer();
        if (account != null) {
            customerStr.append("余额：" + account.getRemain() + " ");
        } else {
            customerStr.append("余额：0 ");
        }
        customerStr.append("" + gao.toString() + " ");
        Customer customer = customerService.selectById(order.getCustomerId());
        if (customer != null) {
            CustomerDetail customerDetail = customerDetailMapper.selectByPrimaryKey(customer.getCustomerDetailId());
            if (customerDetail != null) {
                if (customerDetail.getBirthDate() != null) {
                    if (DateUtil.formatDate(customerDetail.getBirthDate(), "MM-dd")
                            .equals(DateUtil.formatDate(new Date(), "MM-dd"))) {
                        customerStr.append("★" + DateUtil.formatDate(customerDetail.getBirthDate(), "yyyy-MM-dd") + "★");
                    }
                }
            }
        }
        data.put("CUSTOMER_PROPERTY", customerStr.toString());
        print.put("DATA", data);
        print.put("STATUS", "0");
        print.put("TICKET_TYPE", TicketTypeNew.TICKET);
        print.put("TICKET_MODE", TicketTypeNew.KITCHEN_TICKET);
        //保存打印配置信息
//                print.put("ORDER_ID", serialNumber);
//                print.put("KITCHEN_NAME", kitchen.getName());
//                print.put("TABLE_NO", tableNumber);
        //添加到 打印集合
        printTask.add(print);

        redisService.set(print_id, print);
        if (customer != null) {
            redisService.set(print_id + "customer", customer);
        }
        redisService.set(print_id + "article", sb.toString());
        List<String> printList = (List<String>) redisService.get(shopDetail.getId() + "printList");
        if (printList == null) {
            printList = new ArrayList<>();
        }
        printList.add(print_id);
        redisService.set(shopDetail.getId() + "printList", printList);
    }


    private String getModeText(Order order) {
        if (order == null) {
            return "";
        }
        String text = DistributionType.getModeText(order.getDistributionModeId());
        if (order.getParentOrderId() != null && !order.getDistributionModeId().equals(DistributionType.REFUND_ORDER)) {  //如果是加菜的订单，会出现加的字样
            text += " (加)";
        }
        return text;
    }

    @Override
    public Map<String, Object> printReceipt(String orderId, Integer selectPrinterId) {
        // 根据id查询订单
        Order order = selectById(orderId);
        //如果是 未打印状态 或者  异常状态则改变 生产状态和打印时间
        if (ProductionStatus.HAS_ORDER == order.getProductionStatus() || ProductionStatus.NOT_PRINT == order.getProductionStatus()) {
            order.setProductionStatus(ProductionStatus.PRINTED);
            order.setPrintOrderTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        //查询店铺
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        Map<String, String> param = new HashMap<>();
        param.put("orderId", order.getId());
        // 查询订单菜品
        List<OrderItem> orderItems = orderItemService.listByOrderId(param);

//        if (order.getOrderMode() == ShopMode.HOUFU_ORDER) {
        List<OrderItem> child = orderItemService.listByParentId(orderId);
        for (OrderItem orderItem : child) {
            order.setOriginalAmount(order.getOriginalAmount().add(orderItem.getOriginalPrice().multiply(BigDecimal.valueOf(orderItem.getCount()))));
//            order.setOrderMoney(order.getOrderMoney().add(orderItem.getFinalPrice()));
        }
        order.setOrderMoney(order.getAmountWithChildren().doubleValue() > 0 ? order.getAmountWithChildren() : order.getOrderMoney());
        child.addAll(orderItems);
        Map logMap = new HashMap(4);
        Brand brand = brandService.selectById(order.getBrandId());
        logMap.put("brandName", brand.getBrandName());
        logMap.put("fileName", shopDetail.getName());
        logMap.put("type", "posAction");
        logMap.put("content", "订单:" + order.getId() + "被商家手动打印总单，请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(logMap);
        List<Printer> printer = printerService.selectByShopAndType(shopDetail.getId(), PrinterType.RECEPTION);
        if (selectPrinterId == null) {
            if (printer.size() > 0) {
                if (shopDetail.getIsPosNew().equals(Common.YES)) {
                    return printTicketPosNew(order, child, shopDetail, printer.get(0));
                } else {
                    return printTicket(order, child, shopDetail, printer.get(0));
                }

            }
        } else {
            Printer p = printerService.selectById(selectPrinterId);
            if (shopDetail.getIsPosNew().equals(Common.YES)) {
                return printTicketPosNew(order, child, shopDetail, printer.get(0));
            } else {
                return printTicket(order, child, shopDetail, p);
            }

        }
        return null;
    }


    public Map<String, Object> printTicket(Order order, List<OrderItem> orderItems, ShopDetail shopDetail, Printer printer) {
        if (printer == null) {
            return null;
        }
        switch (order.getDistributionModeId()) {
            case DistributionType.RESTAURANT_MODE_ID:
                if (printer.getSupportTangshi() == Common.NO) {
                    return null;
                }
                break;
            case DistributionType.TAKE_IT_SELF:
                if (printer.getSupportWaidai() == Common.NO) {
                    return null;
                }
                break;
            case DistributionType.DELIVERY_MODE_ID:
                if (printer.getSupportWaimai() == Common.NO) {
                    return null;
                }
                break;
            default:
                break;
        }
        if (shopDetail.getIsPosNew() == Common.POS_NEW) {
            //如果是新版本pos
            return printTicketPosNew(order, orderItems, shopDetail, printer);
        }

        List<Map<String, Object>> items = new ArrayList<>(); //存储菜品项
        List<Map<String, Object>> refundItems = new ArrayList<>(); //存储退掉的菜品
        List<String> articleIds = new ArrayList<>();
        for (OrderItem article : orderItems) {
            if (!article.getType().equals(OrderItemType.MEALS_CHILDREN)) {
                if (article.getArticleId().contains("@")) {
                    articleIds.add(article.getArticleId().substring(0, article.getArticleId().indexOf("@")));
                } else {
                    articleIds.add(article.getArticleId());
                }
            }
        }
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("articleIds", articleIds);
        //查询订单项中菜品的排序  排序规则是：分类排序->菜品排序
        List<String> articleSort = new ArrayList<>();
        if (!articleIds.isEmpty()) {
            articleSort = articleService.selectArticleSort(selectMap);
        }
        //根据查询出来的菜品顺序封装数据
        for (String articleId : articleSort) {
            for (OrderItem article : orderItems) {
                if (article.getType().equals(OrderItemType.SETMEALS) && articleId.equalsIgnoreCase(article.getArticleId())) { //如果是套餐
                    getOrderItems(article, items, refundItems);
                    getOrderItemMeal(orderItems, items, refundItems, article.getId());
                } else if (!article.getType().equals(OrderItemType.MEALS_CHILDREN)) { //如果不是套餐子品
                    if (article.getArticleId().contains("@")) { //发现是老规格
                        if (articleId.equalsIgnoreCase(article.getArticleId().substring(0, article.getArticleId().indexOf("@")))) {
                            getOrderItems(article, items, refundItems);
                        }
                    } else {
                        if (articleId.equalsIgnoreCase(article.getArticleId())) {
                            getOrderItems(article, items, refundItems);
                        }
                    }
                }
            }
        }
        BrandSetting brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
        Brand brand = brandService.selectBrandBySetting(brandSetting.getId());

        if (order.getDistributionModeId() == 1) { //如果是堂吃
            if (order.getBaseCustomerCount() != null && order.getBaseCustomerCount() != 0
                    && StringUtils.isBlank(order.getParentOrderId())) {
                if (order.getIsUseNewService().equals(Common.YES)) { //用的是新版服务费
                    if (shopDetail.getIsOpenSauceFee().equals(Common.YES)) {
                        Map<String, Object> item = new HashMap<>();
                        if (order.getSauceFeeCount() > order.getBaseCustomerCount()) {
                            item.put("SUBTOTAL", order.getSauceFeePrice());
                            item.put("ARTICLE_NAME", shopDetail.getSauceFeeName());
                            item.put("ARTICLE_COUNT", order.getSauceFeeCount());
                            items.add(item);
                        } else {
                            item.put("SUBTOTAL", shopDetail.getSauceFeePrice().multiply(new BigDecimal(order.getBaseCustomerCount())));
                            item.put("ARTICLE_NAME", shopDetail.getSauceFeeName());
                            item.put("ARTICLE_COUNT", order.getBaseCustomerCount());
                            items.add(item);
                            if (order.getSauceFeeCount() < order.getCustomerCount()) { //减掉或退掉了
                                Map<String, Object> refundItem = new HashMap<>();
                                refundItem.put("SUBTOTAL", -shopDetail.getSauceFeePrice().multiply(new BigDecimal((order.getBaseCustomerCount() - order.getSauceFeeCount()))).doubleValue());
                                refundItem.put("ARTICLE_NAME", shopDetail.getSauceFeeName() + "(退)");
                                refundItem.put("ARTICLE_COUNT", -(order.getBaseCustomerCount() - order.getSauceFeeCount()));
                                refundItems.add(refundItem);
                            }
                        }
                    }
                    if (shopDetail.getIsOpenTowelFee().equals(Common.YES)) {
                        Map<String, Object> item = new HashMap<>();
                        if (order.getTowelFeeCount() > order.getBaseCustomerCount()) {
                            item.put("SUBTOTAL", order.getTowelFeePrice());
                            item.put("ARTICLE_NAME", shopDetail.getTowelFeeName());
                            item.put("ARTICLE_COUNT", order.getTowelFeeCount());
                            items.add(item);
                        } else {
                            item.put("SUBTOTAL", shopDetail.getTowelFeePrice().multiply(new BigDecimal(order.getBaseCustomerCount())));
                            item.put("ARTICLE_NAME", shopDetail.getTowelFeeName());
                            item.put("ARTICLE_COUNT", order.getBaseCustomerCount());
                            items.add(item);
                            if (order.getTowelFeeCount() < order.getBaseCustomerCount()) { //减掉或退掉了
                                Map<String, Object> refundItem = new HashMap<>();
                                refundItem.put("SUBTOTAL", -shopDetail.getTowelFeePrice().multiply(new BigDecimal((order.getBaseCustomerCount() - order.getTowelFeeCount()))).doubleValue());
                                refundItem.put("ARTICLE_NAME", shopDetail.getTowelFeeName() + "(退)");
                                refundItem.put("ARTICLE_COUNT", -(order.getBaseCustomerCount() - order.getTowelFeeCount()));
                                refundItems.add(refundItem);
                            }
                        }
                    }
                    if (shopDetail.getIsOpenTablewareFee().equals(Common.YES)) {
                        Map<String, Object> item = new HashMap<>();
                        if (order.getTablewareFeeCount() > order.getBaseCustomerCount()) {
                            item.put("SUBTOTAL", order.getTablewareFeePrice());
                            item.put("ARTICLE_NAME", shopDetail.getTablewareFeeName());
                            item.put("ARTICLE_COUNT", order.getTablewareFeeCount());
                            items.add(item);
                        } else {
                            item.put("SUBTOTAL", shopDetail.getTablewareFeePrice().multiply(new BigDecimal(order.getBaseCustomerCount())));
                            item.put("ARTICLE_NAME", shopDetail.getTablewareFeeName());
                            item.put("ARTICLE_COUNT", order.getBaseCustomerCount());
                            items.add(item);
                            if (order.getTablewareFeeCount() < order.getBaseCustomerCount()) {
                                Map<String, Object> refundItem = new HashMap<>();
                                refundItem.put("SUBTOTAL", -shopDetail.getTablewareFeePrice().multiply(new BigDecimal((order.getBaseCustomerCount() - order.getTablewareFeeCount()))).doubleValue());
                                refundItem.put("ARTICLE_NAME", shopDetail.getTablewareFeeName() + "(退)");
                                refundItem.put("ARTICLE_COUNT", -(order.getBaseCustomerCount() - order.getTablewareFeeCount()));
                                refundItems.add(refundItem);
                            }
                        }
                    }
                } else {
                    Map<String, Object> item = new HashMap<>();
                    if (order.getCustomerCount() > order.getBaseCustomerCount()) { //如果服务费加了则直接现在当前服务费信息
                        item.put("SUBTOTAL", order.getServicePrice());
                        item.put("ARTICLE_NAME", shopDetail.getServiceName());
                        item.put("ARTICLE_COUNT", order.getCustomerCount());
                        items.add(item);
                    } else {
                        item.put("SUBTOTAL", shopDetail.getServicePrice().multiply(new BigDecimal(order.getBaseCustomerCount())));
                        item.put("ARTICLE_NAME", shopDetail.getServiceName());
                        item.put("ARTICLE_COUNT", order.getBaseCustomerCount());
                        items.add(item);
                        if (order.getCustomerCount() < order.getBaseCustomerCount()) { //如果当前customerCount小于原始customerCount则说明退掉或编辑了服务费
                            Map<String, Object> refundItem = new HashMap<>();
                            refundItem.put("SUBTOTAL", -shopDetail.getServicePrice().multiply(new BigDecimal((order.getBaseCustomerCount() - order.getCustomerCount()))).doubleValue());
                            refundItem.put("ARTICLE_NAME", shopDetail.getServiceName() + "(退)");
                            refundItem.put("ARTICLE_COUNT", -(order.getBaseCustomerCount() - order.getCustomerCount()));
                            refundItems.add(refundItem);
                        }
                    }
                }
            }
        } else if (order.getDistributionModeId() == 3 || order.getDistributionModeId() == 2) { //如果是外带或外卖
            if (order.getBaseMealAllCount() != null && order.getBaseMealAllCount() != 0) {
                Map<String, Object> item = new HashMap<>();
                List<String> childs = orderMapper.selectChildIdsByParentId(order.getId());
                BigDecimal mealCount = new BigDecimal(order.getBaseMealAllCount());
                BigDecimal mealAllNumber = BigDecimal.valueOf(order.getMealAllNumber());
                if (!CollectionUtils.isEmpty(childs)) {
                    for (String c : childs) {
                        Order childOrder = selectById(c);
                        mealCount = mealCount.add(BigDecimal.valueOf(childOrder.getBaseMealAllCount()));
                        mealAllNumber = mealAllNumber.add(BigDecimal.valueOf(childOrder.getMealAllNumber()));
                    }
                }
                item.put("SUBTOTAL", shopDetail.getMealFeePrice().multiply(mealCount));
                item.put("ARTICLE_NAME", shopDetail.getMealFeeName());
                item.put("ARTICLE_COUNT", mealCount);
                items.add(item);
                if (mealCount.doubleValue() != mealAllNumber.doubleValue()) {
                    Map<String, Object> refundItem = new HashMap<>();
                    refundItem.put("SUBTOTAL", -shopDetail.getMealFeePrice().multiply(new BigDecimal(order.getBaseMealAllCount() - order.getMealAllNumber())).doubleValue());
                    refundItem.put("ARTICLE_NAME", shopDetail.getMealFeeName() + "(退)");
                    refundItem.put("ARTICLE_COUNT", -(order.getBaseMealAllCount() - order.getMealAllNumber()));
                    refundItems.add(refundItem);
                }
            }
        }
        Map<String, Object> print = new HashMap<>();
        String tableNumber = order.getTableNumber() != null ? order.getTableNumber() : "";
        print.put("TABLE_NO", tableNumber);
        print.put("PRINT_STATE", order.getPrintFailFlag());
        print.put("KITCHEN_NAME", printer.getName());
        print.put("PORT", printer.getPort());
        print.put("ORDER_ID", order.getSerialNumber());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_TASK_ID", print_id);
        print.put("PRINT_STATUS", order.getPrintFailFlag());
        print.put("ADD_TIME", new Date());

        Map<String, Object> data = new HashMap<>();
        if (StringUtils.isNotBlank(order.getRemark())) {
            data.put("MEMO", "备注：" + order.getRemark());
        }
        data.put("ORDER_ID", order.getSerialNumber() + "-" + order.getVerCode());
        String orderNumber = (String) redisService.get(order.getId() + "orderNumber");
        Integer orderTotal = (Integer) redisService.get(order.getShopDetailId() + "orderCount");
        if (orderTotal == null) {
            orderTotal = 1;
        } else if (orderNumber == null) {
            orderTotal++;
        }
        redisService.set(order.getShopDetailId() + "orderCount", orderTotal);


        String number;
        if (orderTotal < 10) {
            number = "00" + orderTotal;
        } else if (orderTotal < 100) {
            number = "0" + orderTotal;
        } else {
            number = "" + orderTotal;
        }

        if (StringUtils.isEmpty(orderNumber)) {
            orderNumber = number;
        }
        redisService.set(order.getId() + "orderNumber", orderNumber);

//        nextNumber(order.getShopDetailId(), order.getId())
        if (!"da7ffe9e6f74447f880d82a284a11cae".equals(brand.getId())) {
            data.put("ORDER_NUMBER", orderNumber);
        }
        if (refundItems.size() != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < refundItems.size(); i++) {
                map = refundItems.get(i);
                items.add(map);
            }
        }
        data.put("ITEMS", items);
        com.resto.api.appraise.entity.Appraise appraise = newAppraiseService.selectAppraiseByCustomerId(order.getBrandId(),order.getCustomerId(), order.getShopDetailId());
        StringBuilder star = new StringBuilder();
        BigDecimal level = new BigDecimal(0);
        if (appraise != null) {
            star.append("★★★★★☆☆☆☆☆".substring(5 - appraise.getLevel(), 10 - appraise.getLevel()));
            Map<String, Object> appriseCount = newAppraiseService.selectCustomerAppraiseAvg(order.getBrandId(),order.getCustomerId());
            level = new BigDecimal(Integer.valueOf(appriseCount.get("sum").toString()))
                    .divide(new BigDecimal(Integer.valueOf(appriseCount.get("count").toString())), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            star.append("☆☆☆☆☆");
        }
        StringBuilder gao = new StringBuilder();
        if (shopDetail.getIsUserIdentity().equals(1)) {
            //得到有限制的情况下用户的订单数
            int gaoCount = orderMapper.selectByCustomerCount(order.getCustomerId());
            if (gaoCount>1) {
                gao.append("消费" + gaoCount + "次");
            } else {
                gao.append("新顾客");
            }
        }
        String modeText = getModeText(order);
        data.put("DISTRIBUTION_MODE", modeText);
        if (order.getAmountWithChildren().doubleValue() > 0.0 && order.getPrintTimes() == 1 && order.getOrderMode() == ShopMode.HOUFU_ORDER) {
            data.put("ORIGINAL_AMOUNT", order.getAmountWithChildren());
        } else {
            data.put("ORIGINAL_AMOUNT", order.getOriginalAmount());
        }
        data.put("RESTAURANT_ADDRESS", shopDetail.getAddress());
        data.put("REDUCTION_AMOUNT", order.getOriginalAmount().subtract(order.getAmountWithChildren().doubleValue() == 0.0 ? order.getOrderMoney() : order.getAmountWithChildren()));
        data.put("RESTAURANT_TEL", shopDetail.getPhone());
        data.put("TABLE_NUMBER", order.getTableNumber());
        data.put("CUSTOMER_COUNT", order.getCustomerCount() == null ? "-" : order.getCustomerCount());
        data.put("PAYMENT_AMOUNT", order.getOrderMoney());
        if (order.getPayType() == PayType.NOPAY && (order.getOrderState() == OrderState.PAYMENT || order.getOrderState() == OrderState.CONFIRM)) {
            //如果带打印机是区域打印机，判断他是否开启打结账单
            if (printer.getRange().equals(Common.YES) && printer.getBillOfAccount().equals(Common.NO)) {
                return null;
            }
            data.put("RESTAURANT_NAME", shopDetail.getName() + " (结账单)");
        } else if (order.getPayType() == PayType.NOPAY && order.getPayMode() != OrderPayMode.YUE_PAY && order.getPayMode() != OrderPayMode.WX_PAY
                && order.getPayMode() != OrderPayMode.ALI_PAY && order.getOrderState() == OrderState.SUBMIT) {
            //如果带打印机是区域打印机，判断他是否开启打结账单
            if (printer.getRange().equals(Common.YES) && printer.getBillOfAccount().equals(Common.NO)) {
                return null;
            }
            data.put("RESTAURANT_NAME", shopDetail.getName() + " (结账单)");
        } else if (order.getOrderState() == OrderState.SUBMIT && order.getPayType() == PayType.NOPAY) {
            //如果带打印机是区域打印机，判断他是否开启打消费单
            if (printer.getRange().equals(Common.YES) && printer.getBillOfConsumption().equals(Common.NO)) {
                return null;
            }
            data.put("RESTAURANT_NAME", shopDetail.getName() + " (消费清单)");
        } else {
            if (order.getParentOrderId() != null) {
                //加菜的话  判断他主订单  如果主订单是后付  则显示(结账单)
                Order faOrder = orderMapper.selectByPrimaryKey(order.getParentOrderId());
                if (faOrder.getPayType() == PayType.NOPAY) {
                    //如果带打印机是区域打印机，判断他是否开启打结账单
                    if (printer.getRange().equals(Common.YES) && printer.getBillOfAccount().equals(Common.NO)) {
                        return null;
                    }
                    data.put("RESTAURANT_NAME", shopDetail.getName() + " (结账单)");
                } else {
                    data.put("RESTAURANT_NAME", shopDetail.getName());
                }
            } else {
                data.put("RESTAURANT_NAME", shopDetail.getName());
            }
        }

        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的总单打印次数， 如果为空则是第一次打印则当前打印次数为1
        Integer printTimes = redisService.get(order.getId() + "TICKET") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "TICKET").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        BigDecimal articleCount = new BigDecimal(order.getArticleCount());
        if (order.getParentOrderId() == null) {
            articleCount = articleCount.add(new BigDecimal(order.getCustomerCount() == null ? 0
                    : order.getCustomerCount()));
            articleCount = articleCount.add(new BigDecimal(order.getMealAllNumber() == null ? 0
                    : order.getMealAllNumber()));
        }

        List<Order> childList = orderMapper.selectListByParentId(order.getId());
        for (Order child : childList) {
            articleCount = articleCount.add(BigDecimal.valueOf(child.getArticleCount()));
            articleCount = articleCount.add(BigDecimal.valueOf(child.getMealAllNumber() == null ? 0 : child.getMealAllNumber()));
        }

        data.put("ARTICLE_COUNT", articleCount);
        List<Map<String, Object>> patMentItems = new ArrayList<Map<String, Object>>();
        List<OrderPaymentItem> orderPaymentItems = orderPaymentItemService.selectPaymentCountByOrderId(order.getId());
        List<String> child = orderMapper.selectChildIdsByParentId(order.getId());
        if (!CollectionUtils.isEmpty(child)) {
            for (String childId : child) {
                List<OrderPaymentItem> childPay = orderPaymentItemService.selectPaymentCountByOrderId(childId);
                orderPaymentItems.addAll(childPay);
            }
        }

        Map<Integer, BigDecimal> map = new HashMap<>();
        for (OrderPaymentItem orderPaymentItem : orderPaymentItems) {
            if (map.containsKey(orderPaymentItem.getPaymentModeId())) {
                BigDecimal newValue = map.get(orderPaymentItem.getPaymentModeId()).add(orderPaymentItem.getPayValue());
                map.put(orderPaymentItem.getPaymentModeId(), newValue);
            } else {
                map.put(orderPaymentItem.getPaymentModeId(), orderPaymentItem.getPayValue());
            }
        }

        if (!map.isEmpty()) {
            for (Integer key : map.keySet()) {
                Map<String, Object> patMentItem = new HashMap<String, Object>();
                patMentItem.put("SUBTOTAL", map.get(key));
                patMentItem.put("PAYMENT_MODE", PayMode.getPayModeName(key));
                patMentItems.add(patMentItem);
            }
        } else {
            Map<String, Object> patMentItem = new HashMap<String, Object>();
            patMentItem.put("SUBTOTAL", 0);
            patMentItem.put("PAYMENT_MODE", "");
            patMentItems.add(patMentItem);
        }
        data.put("PAYMENT_ITEMS", patMentItems);
        data.put("CUSTOMER_SATISFACTION", star.toString());
        data.put("CUSTOMER_SATISFACTION_DEGREE", level);
        Account account = accountService.selectAccountAndLogByCustomerId(order.getCustomerId());
        StringBuffer customerStr = new StringBuffer();
        if (account != null) {
            customerStr.append("余额：" + account.getRemain() + " ");
        } else {
            customerStr.append("余额：0 ");
        }
        customerStr.append("" + gao.toString() + " ");
        Customer customer = customerService.selectById(order.getCustomerId());
        if (customer != null) {
            CustomerDetail customerDetail = customerDetailMapper.selectByPrimaryKey(customer.getCustomerDetailId());
            if (customerDetail != null) {
                if (customerDetail.getBirthDate() != null) {
                    if (DateUtil.formatDate(customerDetail.getBirthDate(), "MM-dd")
                            .equals(DateUtil.formatDate(new Date(), "MM-dd"))) {
                        customerStr.append("★" + DateUtil.formatDate(customerDetail.getBirthDate(), "yyyy-MM-dd") + "★");
                    }
                }
            }
        }
        data.put("CUSTOMER_PROPERTY", customerStr.toString());
        print.put("DATA", data);
        print.put("STATUS", 0);
        print.put("TICKET_TYPE", TicketType.RECEIPT);

        JSONObject json = new JSONObject(print);
        Map logMap = new HashMap(4);
        logMap.put("brandName", brand.getBrandName());
        logMap.put("fileName", shopDetail.getName());
        logMap.put("type", "posAction");
        logMap.put("content", "订单:" + order.getId() + "返回打印总单模版" + json.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(logMap);
        redisService.set(print_id, print);
        List<String> printList = (List<String>) redisService.get(shopDetail.getId() + "printList");
        if (printList == null) {
            printList = new ArrayList<>();
        }
        printList.add(print_id);
        redisService.set(shopDetail.getId() + "printList", printList);
        //订单返回总单打印模板视为已打印成功
        redisService.set(order.getId() + "TICKET", printTimes);
        return print;
    }


    public Map<String, Object> printTicketPosNew(Order order, List<OrderItem> orderItems, ShopDetail shopDetail, Printer printer) {
        switch (order.getDistributionModeId()) {
            case DistributionType.RESTAURANT_MODE_ID:
                if (printer.getSupportTangshi() == Common.NO) {
                    return null;
                }
                break;
            case DistributionType.TAKE_IT_SELF:
                if (printer.getSupportWaidai() == Common.NO) {
                    return null;
                }
                break;
            case DistributionType.DELIVERY_MODE_ID:
                if (printer.getSupportWaimai() == Common.NO) {
                    return null;
                }
                break;
            default:
                break;
        }
        List<Map<String, Object>> items = new ArrayList<>();
        List<Map<String, Object>> refundItems = new ArrayList<>();
        List<String> articleIds = new ArrayList<>();
        for (OrderItem article : orderItems) {
            if (!article.getType().equals(OrderItemType.MEALS_CHILDREN)) {
                if (article.getArticleId().contains("@")) {
                    articleIds.add(article.getArticleId().substring(0, article.getArticleId().indexOf("@")));
                } else {
                    articleIds.add(article.getArticleId());
                }
            }
        }
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("articleIds", articleIds);
        List<String> articleSort = new ArrayList<>();
        if (!articleIds.isEmpty()) {
            articleSort = articleService.selectArticleSort(selectMap);
        }
        for (String articleId : articleSort) {
            for (OrderItem article : orderItems) {
                if (article.getType().equals(OrderItemType.SETMEALS) && articleId.equalsIgnoreCase(article.getArticleId())) {
                    getOrderItems(article, items, refundItems);
                    getOrderItemMeal(orderItems, items, refundItems, article.getId());
                } else if (!article.getType().equals(OrderItemType.MEALS_CHILDREN)) {
                    if (article.getArticleId().contains("@")) {
                        if (articleId.equalsIgnoreCase(article.getArticleId().substring(0, article.getArticleId().indexOf("@")))) {
                            getOrderItems(article, items, refundItems);
                        }
                    } else {
                        if (articleId.equalsIgnoreCase(article.getArticleId())) {
                            getOrderItems(article, items, refundItems);
                        }
                    }
                }
            }
        }
        BrandSetting brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
        Brand brand = brandService.selectBrandBySetting(brandSetting.getId());

        if (order.getDistributionModeId() == 1) {
            if (order.getBaseCustomerCount() != null && order.getBaseCustomerCount() != 0
                    && StringUtils.isBlank(order.getParentOrderId())) {
                if (order.getIsUseNewService().equals(Common.YES)) { //用的是新版服务费
                    if (shopDetail.getIsOpenSauceFee().equals(Common.YES)) {
                        Map<String, Object> item = new HashMap<>();
                        if (order.getSauceFeeCount() > order.getBaseCustomerCount()) {
                            item.put("SUBTOTAL", order.getSauceFeePrice());
                            item.put("ARTICLE_NAME", shopDetail.getSauceFeeName());
                            item.put("ARTICLE_COUNT", order.getSauceFeeCount());
                        } else {
                            item.put("SUBTOTAL", shopDetail.getSauceFeePrice().multiply(new BigDecimal(order.getBaseCustomerCount())));
                            item.put("ARTICLE_NAME", shopDetail.getSauceFeeName());
                            item.put("ARTICLE_COUNT", order.getBaseCustomerCount());
                            items.add(item);
                            if (order.getSauceFeeCount() < order.getCustomerCount()) { //减掉或退掉了
                                Map<String, Object> refundItem = new HashMap<>();
                                refundItem.put("SUBTOTAL", -shopDetail.getSauceFeePrice().multiply(new BigDecimal((order.getBaseCustomerCount() - order.getSauceFeeCount()))).doubleValue());
                                refundItem.put("ARTICLE_NAME", shopDetail.getSauceFeeName() + "(退)");
                                refundItem.put("ARTICLE_COUNT", -(order.getBaseCustomerCount() - order.getSauceFeeCount()));
                                refundItems.add(refundItem);
                            }
                        }
                    }
                    if (shopDetail.getIsOpenTowelFee().equals(Common.YES)) {
                        Map<String, Object> item = new HashMap<>();
                        if (order.getTowelFeeCount() > order.getBaseCustomerCount()) {
                            item.put("SUBTOTAL", order.getTowelFeePrice());
                            item.put("ARTICLE_NAME", shopDetail.getTowelFeeName());
                            item.put("ARTICLE_COUNT", order.getTowelFeeCount());
                        } else {
                            item.put("SUBTOTAL", shopDetail.getTowelFeePrice().multiply(new BigDecimal(order.getBaseCustomerCount())));
                            item.put("ARTICLE_NAME", shopDetail.getTowelFeeName());
                            item.put("ARTICLE_COUNT", order.getBaseCustomerCount());
                            items.add(item);
                            if (order.getTowelFeeCount() < order.getBaseCustomerCount()) { //减掉或退掉了
                                Map<String, Object> refundItem = new HashMap<>();
                                refundItem.put("SUBTOTAL", -shopDetail.getTowelFeePrice().multiply(new BigDecimal((order.getBaseCustomerCount() - order.getTowelFeeCount()))).doubleValue());
                                refundItem.put("ARTICLE_NAME", shopDetail.getTowelFeeName() + "(退)");
                                refundItem.put("ARTICLE_COUNT", -(order.getBaseCustomerCount() - order.getTowelFeeCount()));
                                refundItems.add(refundItem);
                            }
                        }
                    }
                    if (shopDetail.getIsOpenTablewareFee().equals(Common.YES)) {
                        Map<String, Object> item = new HashMap<>();
                        if (order.getTablewareFeeCount() > order.getBaseCustomerCount()) {
                            item.put("SUBTOTAL", order.getTablewareFeePrice());
                            item.put("ARTICLE_NAME", shopDetail.getTablewareFeeName());
                            item.put("ARTICLE_COUNT", order.getTablewareFeeCount());
                        } else {
                            item.put("SUBTOTAL", shopDetail.getTablewareFeePrice().multiply(new BigDecimal(order.getBaseCustomerCount())));
                            item.put("ARTICLE_NAME", shopDetail.getTablewareFeeName());
                            item.put("ARTICLE_COUNT", order.getBaseCustomerCount());
                            items.add(item);
                            if (order.getTablewareFeeCount() < order.getBaseCustomerCount()) {
                                Map<String, Object> refundItem = new HashMap<>();
                                refundItem.put("SUBTOTAL", -shopDetail.getTablewareFeePrice().multiply(new BigDecimal((order.getBaseCustomerCount() - order.getTablewareFeeCount()))).doubleValue());
                                refundItem.put("ARTICLE_NAME", shopDetail.getTablewareFeeName() + "(退)");
                                refundItem.put("ARTICLE_COUNT", -(order.getBaseCustomerCount() - order.getTablewareFeeCount()));
                                refundItems.add(refundItem);
                            }
                        }
                    }
                } else {
                    Map<String, Object> item = new HashMap<>();
                    if (order.getCustomerCount() > order.getBaseCustomerCount()) { //如果服务费加了则直接现在当前服务费信息
                        item.put("SUBTOTAL", order.getServicePrice());
                        item.put("ARTICLE_NAME", shopDetail.getServiceName());
                        item.put("ARTICLE_COUNT", order.getCustomerCount());
                    } else {
                        item.put("SUBTOTAL", shopDetail.getServicePrice().multiply(new BigDecimal(order.getBaseCustomerCount())));
                        item.put("ARTICLE_NAME", shopDetail.getServiceName());
                        item.put("ARTICLE_COUNT", order.getBaseCustomerCount());
                        items.add(item);
                        if (order.getCustomerCount() < order.getBaseCustomerCount()) { //如果当前customerCount小于原始customerCount则说明退掉或编辑了服务费
                            Map<String, Object> refundItem = new HashMap<>();
                            refundItem.put("SUBTOTAL", -shopDetail.getServicePrice().multiply(new BigDecimal((order.getBaseCustomerCount() - order.getCustomerCount()))).doubleValue());
                            refundItem.put("ARTICLE_NAME", shopDetail.getServiceName() + "(退)");
                            refundItem.put("ARTICLE_COUNT", -(order.getBaseCustomerCount() - order.getCustomerCount()));
                            refundItems.add(refundItem);
                        }
                    }
                }
            }
        } else if (order.getDistributionModeId() == 3 || order.getDistributionModeId() == 2) {
            if (order.getBaseMealAllCount() != null && order.getBaseMealAllCount() != 0) {
                Map<String, Object> item = new HashMap<>();
                List<String> childs = orderMapper.selectChildIdsByParentId(order.getId());
                BigDecimal mealCount = new BigDecimal(order.getBaseMealAllCount());
                BigDecimal mealAllNumber = BigDecimal.valueOf(order.getMealAllNumber());
                if (!CollectionUtils.isEmpty(childs)) {
                    for (String c : childs) {
                        Order childOrder = selectById(c);
                        mealCount = mealCount.add(BigDecimal.valueOf(childOrder.getBaseMealAllCount()));
                        mealAllNumber = mealAllNumber.add(BigDecimal.valueOf(childOrder.getMealAllNumber()));
                    }
                }
                item.put("SUBTOTAL", shopDetail.getMealFeePrice().multiply(mealCount));
                item.put("ARTICLE_NAME", shopDetail.getMealFeeName());
                item.put("ARTICLE_COUNT", mealCount);
                items.add(item);
                if (mealCount.doubleValue() != mealAllNumber.doubleValue()) {
                    Map<String, Object> refundItem = new HashMap<>();
                    refundItem.put("SUBTOTAL", -shopDetail.getMealFeePrice().multiply(new BigDecimal(order.getBaseMealAllCount() - order.getMealAllNumber())).doubleValue());
                    refundItem.put("ARTICLE_NAME", shopDetail.getMealFeeName() + "(退)");
                    refundItem.put("ARTICLE_COUNT", -(order.getBaseMealAllCount() - order.getMealAllNumber()));
                    refundItems.add(refundItem);
                }
            }
        }
        Map<String, Object> print = new HashMap<>();
        String tableNumber = order.getTableNumber() != null ? order.getTableNumber() : "";
        print.put("TABLE_NO", tableNumber);
        print.put("TASK_ORDER_ID", order.getId());
        print.put("KITCHEN_NAME", printer.getName());
        print.put("PORT", printer.getPort());
        print.put("PRINT_STATUS", order.getPrintFailFlag());
        print.put("LINE_WIDTH", 42);
        print.put("ORDER_ID", order.getSerialNumber());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_TASK_ID", print_id);
        print.put("ADD_TIME", System.currentTimeMillis());

        Map<String, Object> data = new HashMap<>();
        if (StringUtils.isNotBlank(order.getRemark())) {
            data.put("MEMO", "备注：" + order.getRemark());
        }
        data.put("ORDER_ID", order.getSerialNumber() + "-" + order.getVerCode());
        String orderNumber = (String) redisService.get(order.getId() + "orderNumber");
        Integer orderTotal = (Integer) redisService.get(order.getShopDetailId() + "orderCount");
        if (orderTotal == null) {
            orderTotal = 1;
        } else if (orderNumber == null) {
            orderTotal++;
        }
        redisService.set(order.getShopDetailId() + "orderCount", orderTotal);


        String number;
        if (orderTotal < 10) {
            number = "00" + orderTotal;
        } else if (orderTotal < 100) {
            number = "0" + orderTotal;
        } else {
            number = "" + orderTotal;
        }

        if (StringUtils.isEmpty(orderNumber)) {
            orderNumber = number;
        }
        redisService.set(order.getId() + "orderNumber", orderNumber);

//        nextNumber(order.getShopDetailId(), order.getId())
        data.put("ORDER_NUMBER", orderNumber);
        if (refundItems.size() != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < refundItems.size(); i++) {
                map = refundItems.get(i);
                items.add(map);
            }
        }
        data.put("ITEMS", items);
        com.resto.api.appraise.entity.Appraise appraise = newAppraiseService.selectAppraiseByCustomerId(order.getBrandId(),order.getCustomerId(), order.getShopDetailId());
        StringBuilder star = new StringBuilder();
        BigDecimal level = new BigDecimal(0);
        if (appraise != null) {
            star.append("★★★★★☆☆☆☆☆".substring(5 - appraise.getLevel(), 10 - appraise.getLevel()));
            Map<String, Object> appriseCount = newAppraiseService.selectCustomerAppraiseAvg(order.getBrandId(),order.getCustomerId());
            level = new BigDecimal(Integer.valueOf(appriseCount.get("sum").toString()))
                    .divide(new BigDecimal(Integer.valueOf(appriseCount.get("count").toString())), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            star.append("☆☆☆☆☆");
        }
        StringBuilder gao = new StringBuilder();
        if (shopDetail.getIsUserIdentity().equals(1)) {
            //得到有限制的情况下用户的订单数
            int gaoCount = orderMapper.selectByCustomerCount(order.getCustomerId());
            if (gaoCount>1) {
                gao.append("消费" + gaoCount + "次");
            } else {
                gao.append("新顾客");
            }
        }
        String modeText = getModeText(order);
        data.put("DISTRIBUTION_MODE", modeText);
        if (order.getAmountWithChildren().doubleValue() > 0.0 && order.getPrintTimes() == 1 && order.getOrderMode() == ShopMode.HOUFU_ORDER) {
            data.put("ORIGINAL_AMOUNT", order.getAmountWithChildren());
        } else {
            data.put("ORIGINAL_AMOUNT", order.getOriginalAmount());
        }
        data.put("RESTAURANT_ADDRESS", shopDetail.getAddress());
        data.put("REDUCTION_AMOUNT", order.getOriginalAmount().subtract(order.getAmountWithChildren().doubleValue() == 0.0 ? order.getOrderMoney() : order.getAmountWithChildren()));
        data.put("RESTAURANT_TEL", shopDetail.getPhone());
        data.put("TABLE_NUMBER", order.getTableNumber());
        data.put("CUSTOMER_COUNT", order.getCustomerCount() == null ? "-" : order.getCustomerCount());
        data.put("PAYMENT_AMOUNT", order.getOrderMoney());
        if (order.getPayType() == PayType.NOPAY && (order.getOrderState() == OrderState.PAYMENT || order.getOrderState() == OrderState.CONFIRM)) {
            //如果带打印机是区域打印机，判断他是否开启打结账单
            if (printer.getRange().equals(Common.YES) && printer.getBillOfAccount().equals(Common.NO)) {
                return null;
            }
            data.put("RESTAURANT_NAME", shopDetail.getName() + " (结账单)");
        } else if (order.getPayType() == PayType.NOPAY && order.getPayMode() != OrderPayMode.YUE_PAY && order.getPayMode() != OrderPayMode.WX_PAY &&
                order.getPayMode() != OrderPayMode.ALI_PAY && order.getOrderState() == OrderState.SUBMIT) {
            if (printer.getRange().equals(Common.YES) && printer.getBillOfAccount().equals(Common.NO)) {
                return null;
            }
            data.put("RESTAURANT_NAME", shopDetail.getName() + " (结账单)");
        } else if (order.getOrderState() == OrderState.SUBMIT && order.getPayType() == PayType.NOPAY) {
            //如果带打印机是区域打印机，判断他是否开启打消费单
            if (printer.getRange().equals(Common.YES) && printer.getBillOfConsumption().equals(Common.NO)) {
                return null;
            }
            data.put("RESTAURANT_NAME", shopDetail.getName() + " (消费清单)");
        } else {
            if (order.getParentOrderId() != null) {
                //加菜的话  判断他主订单  如果主订单是后付  则显示(结账单)
                Order faOrder = orderMapper.selectByPrimaryKey(order.getParentOrderId());
                if (faOrder.getPayType() == PayType.NOPAY) {
                    if (printer.getRange().equals(Common.YES) && printer.getBillOfAccount().equals(Common.NO)) {
                        return null;
                    }
                    data.put("RESTAURANT_NAME", shopDetail.getName() + " (结账单)");
                } else {
                    data.put("RESTAURANT_NAME", shopDetail.getName());
                }
            } else {
                data.put("RESTAURANT_NAME", shopDetail.getName());
            }
        }

        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的总单打印次数， 如果为空则是第一次打印则当前打印次数为1
        Integer printTimes = redisService.get(order.getId() + "TICKET") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "TICKET").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        BigDecimal articleCount = new BigDecimal(order.getArticleCount());
        if (order.getParentOrderId() == null) {
            articleCount = articleCount.add(new BigDecimal(order.getCustomerCount() == null ? 0
                    : order.getCustomerCount()));
            articleCount = articleCount.add(new BigDecimal(order.getMealAllNumber() == null ? 0
                    : order.getMealAllNumber()));
        }

        List<Order> childList = orderMapper.selectListByParentId(order.getId());
        for (Order child : childList) {
            articleCount = articleCount.add(BigDecimal.valueOf(child.getArticleCount()));
            articleCount = articleCount.add(BigDecimal.valueOf(child.getMealAllNumber() == null ? 0 : child.getMealAllNumber()));
        }

        data.put("ARTICLE_COUNT", articleCount);
        List<Map<String, Object>> patMentItems = new ArrayList<Map<String, Object>>();
        List<OrderPaymentItem> orderPaymentItems = orderPaymentItemService.selectPaymentCountByOrderId(order.getId());
        List<String> child = orderMapper.selectChildIdsByParentId(order.getId());
        if (!CollectionUtils.isEmpty(child)) {
            for (String childId : child) {
                List<OrderPaymentItem> childPay = orderPaymentItemService.selectPaymentCountByOrderId(childId);
                orderPaymentItems.addAll(childPay);
            }
        }

        Map<Integer, BigDecimal> map = new HashMap<>();
        for (OrderPaymentItem orderPaymentItem : orderPaymentItems) {
            if (map.containsKey(orderPaymentItem.getPaymentModeId())) {
                BigDecimal newValue = map.get(orderPaymentItem.getPaymentModeId()).add(orderPaymentItem.getPayValue());
                map.put(orderPaymentItem.getPaymentModeId(), newValue);
            } else {
                map.put(orderPaymentItem.getPaymentModeId(), orderPaymentItem.getPayValue());
            }
        }

        if (!map.isEmpty()) {
            for (Integer key : map.keySet()) {
                Map<String, Object> patMentItem = new HashMap<String, Object>();
                patMentItem.put("SUBTOTAL", map.get(key));
                patMentItem.put("PAYMENT_MODE", PayMode.getPayModeName(key));
                patMentItems.add(patMentItem);
            }
        } else {
            Map<String, Object> patMentItem = new HashMap<String, Object>();
            patMentItem.put("SUBTOTAL", 0);
            patMentItem.put("PAYMENT_MODE", "");
            patMentItems.add(patMentItem);
        }
        data.put("PAYMENT_ITEMS", patMentItems);
        data.put("CUSTOMER_SATISFACTION", star.toString());
        data.put("CUSTOMER_SATISFACTION_DEGREE", level);
        Account account = accountService.selectAccountAndLogByCustomerId(order.getCustomerId());
        StringBuffer customerStr = new StringBuffer();
        if (account != null) {
            customerStr.append("余额：" + account.getRemain() + " ");
        } else {
            customerStr.append("余额：0 ");
        }
        customerStr.append("" + gao.toString() + " ");
        Customer customer = customerService.selectById(order.getCustomerId());
        if (customer != null) {
            CustomerDetail customerDetail = customerDetailMapper.selectByPrimaryKey(customer.getCustomerDetailId());
            if (customerDetail != null) {
                if (customerDetail.getBirthDate() != null) {
                    if (DateUtil.formatDate(customerDetail.getBirthDate(), "MM-dd")
                            .equals(DateUtil.formatDate(new Date(), "MM-dd"))) {
                        customerStr.append("★" + DateUtil.formatDate(customerDetail.getBirthDate(), "yyyy-MM-dd") + "★");
                    }
                }
            }
        }
        data.put("CUSTOMER_PROPERTY", customerStr.toString());
        print.put("DATA", data);
        print.put("STATUS", 0);
        print.put("TICKET_TYPE", TicketTypeNew.TICKET);
        print.put("TICKET_MODE", TicketTypeNew.RESTAURANT_RECEIPT);
        JSONObject json = new JSONObject(print);
        Map logMap = new HashMap(4);
        logMap.put("brandName", brand.getBrandName());
        logMap.put("fileName", shopDetail.getName());
        logMap.put("type", "posAction");
        logMap.put("content", "订单:" + order.getId() + "返回打印总单模版" + json.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(logMap);
        redisService.set(print_id, print);
        List<String> printList = (List<String>) redisService.get(shopDetail.getId() + "printList");
        if (printList == null) {
            printList = new ArrayList<>();
        }
        printList.add(print_id);
        redisService.set(shopDetail.getId() + "printList", printList);
        //订单返回总单打印模板视为已打印成功
        redisService.set(order.getId() + "TICKET", printTimes);
        return print;
    }


    public void getOrderItems(OrderItem article, List<Map<String, Object>> items, List<Map<String, Object>> refundItems) {
        Map<String, Object> item = new HashMap<>();
        item.put("SUBTOTAL", article.getOriginalPrice().multiply(new BigDecimal(article.getChangeCount() != null && article.getChangeCount().compareTo(0) > 0 ? article.getChangeCount() : article.getOrginCount())));
        item.put("ARTICLE_NAME", article.getArticleName());
        item.put("ARTICLE_COUNT", article.getChangeCount() != null && article.getChangeCount().compareTo(0) > 0 ? article.getChangeCount() : article.getOrginCount());
        items.add(item);
        if (article.getRefundCount() != 0) {
            Map<String, Object> refundItem = new HashMap<>();
            refundItem.put("SUBTOTAL", -article.getOriginalPrice().multiply(new BigDecimal(article.getRefundCount())).doubleValue());
            if (article.getArticleName().contains("加")) {
                article.setArticleName(article.getArticleName().substring(0, article.getArticleName().indexOf("(") - 1));
            }
            refundItem.put("ARTICLE_NAME", article.getArticleName() + "(退)");
            refundItem.put("ARTICLE_COUNT", -article.getRefundCount());
            refundItems.add(refundItem);
        }
    }

    public void getOrderItemMeal(List<OrderItem> orderItems, List<Map<String, Object>> items, List<Map<String, Object>> refundItems, String articleId) {
        for (OrderItem article : orderItems) {
            if (article.getParentId() != null && article.getParentId().equals(articleId)) {
                Map<String, Object> item = new HashMap<>();
                item.put("SUBTOTAL", article.getOriginalPrice().multiply(new BigDecimal(article.getOrginCount())));
                item.put("ARTICLE_NAME", article.getArticleName());
                item.put("ARTICLE_COUNT", article.getOrginCount());
                items.add(item);
                if (article.getRefundCount() != 0) {
                    Map<String, Object> refundItem = new HashMap<>();
                    refundItem.put("SUBTOTAL", -article.getOriginalPrice().multiply(new BigDecimal(article.getRefundCount())).doubleValue());
//                    if (article.getArticleName().contains("加")) {
//                        article.setArticleName(article.getArticleName().substring(0, article.getArticleName().indexOf("(") - 1));
//                    }
                    refundItem.put("ARTICLE_NAME", article.getArticleName() + "(退)");
                    refundItem.put("ARTICLE_COUNT", -article.getRefundCount());
                    refundItems.add(refundItem);
                }
            }
        }
    }

    @Override
    public JSONResult confirmOrder(Order order) {
        JSONResult jsonResult = new JSONResult();
        order = selectById(order.getId());
        if (order == null || order.getOrderState() != OrderState.PAYMENT) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单不存在或者订单不是已支付的状态不符合确认要求");
            return jsonResult;
        }
        if (order.getParentOrderId() != null) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单为子订单不符合确认要求");
            return jsonResult;
        }
        if (order.getProductionStatus() == ProductionStatus.REFUND_ARTICLE) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单生成状态为全部已退菜状态不符合确认要求");
            return jsonResult;
        }
        Brand brand = brandService.selectById(order.getBrandId());
        log.info("开始确认订单:" + order.getId());
        Integer orginState = order.getOrderState();//订单开始确认的状体
        if (order.getConfirmTime() == null && !order.getClosed()) {
            order.setOrderState(OrderState.CONFIRM);
            if ((order.getProductionStatus() == ProductionStatus.HAS_ORDER || order.getPrintOrderTime() != null) && order.getProductionStatus() != ProductionStatus.HAS_CALL) {
                order.setProductionStatus(ProductionStatus.PRINTED);
            }
            if (order.getDataOrigin() == 1 && order.getProductionStatus() != ProductionStatus.HAS_CALL && order.getProductionStatus() != ProductionStatus.PRINTED) {
                order.setProductionStatus(ProductionStatus.PRINTED);
            }
            order.setConfirmTime(new Date());
            order.setAllowCancel(false);
            BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
            if (order.getParentOrderId() == null) {
                if (setting.getAppraiseMinMoney().compareTo(order.getOrderMoney()) <= 0 || setting.getAppraiseMinMoney().compareTo(order.getAmountWithChildren()) <= 0) {
                    order.setAllowAppraise(true);
                }
            } else {
                log.info("最小评论金额为:" + setting.getAppraiseMinMoney() + ", oid:" + order.getId());
                order.setAllowAppraise(false);
            }
            if (order.getAllowContinueOrder() && order.getPayType() == PayType.NOPAY) {
                log.info("后付订单付款后不允许加菜:" + order.getId());
                order.setAllowContinueOrder(false);
            }
            update(order);
            /**
             * 记录订单自动确认2-10过程
             */
            LogTemplateUtils.getConfirmByOrderType(brand.getBrandName(), order, orginState, "confirmOrder");
            jsonResult.setSuccess(true);
            jsonResult.setData(order);
            return jsonResult;
        } else {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单状态不符");
            return jsonResult;
        }
    }

    @Override
    public JSONResult confirmWaiMaiOrder(Order order) {
        JSONResult jsonResult = new JSONResult();
        order = selectById(order.getId());
        if (order == null || order.getParentOrderId() != null) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单为子订单不符合确认要求");
            return jsonResult;
        }
        if (order.getProductionStatus() == ProductionStatus.REFUND_ARTICLE) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单生成状态为全部已退菜状态不符合确认要求");
            return jsonResult;
        }
        Brand brand = brandService.selectById(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        log.info("开始确认订单:" + order.getId());
        Integer orginState = order.getOrderState();//订单开始确认的状体
        if (order.getConfirmTime() == null && !order.getClosed()) {
            order.setOrderState(OrderState.CONFIRM);
            order.setConfirmTime(new Date());
            order.setAllowCancel(false);
            BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
            if (order.getParentOrderId() == null) {
                log.info("如果订单金额大于 评论金额 则允许评论" + order.getId());
                if (setting.getAppraiseMinMoney().compareTo(order.getOrderMoney()) <= 0 || setting.getAppraiseMinMoney().compareTo(order.getAmountWithChildren()) <= 0) {
                    order.setAllowAppraise(true);
                }
            } else {
                log.info("最小评论金额为:" + setting.getAppraiseMinMoney() + ", oid:" + order.getId());
                order.setAllowAppraise(false);
            }
            update(order);
            /**
             * 记录订单自动确认2-10过程
             */
            LogTemplateUtils.getConfirmByOrderType(brand.getBrandName(), order, orginState, "confirmOrder");
            jsonResult.setSuccess(true);
            jsonResult.setData(order);
            return jsonResult;
        } else {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单状态不符");
            return jsonResult;
        }
    }

    @Override
    public JSONResult confirmBossOrder(Order order) {
        JSONResult jsonResult = new JSONResult();
        order = selectById(order.getId());
        if (order == null || order.getOrderState() != OrderState.PAYMENT) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单不存在或者订单不是已支付的状态不符合确认要求");
            return jsonResult;
        }
        if (order.getParentOrderId() != null) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单为子订单不符合确认要求");
            return jsonResult;
        }
        if (order.getProductionStatus() == ProductionStatus.REFUND_ARTICLE) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单生成状态为全部已退菜状态不符合确认要求");
            return jsonResult;
        }
        Integer orginState = order.getOrderState();
        Brand brand = brandService.selectById(order.getBrandId());
        log.info("开始确认订单:" + order.getId());
        if (!order.getClosed() && (order.getConfirmTime() == null || order.getIsPosPay() == 1)) {
            order.setOrderState(OrderState.CONFIRM);
            if ((order.getProductionStatus() == ProductionStatus.HAS_ORDER || order.getPrintOrderTime() != null) && order.getProductionStatus() != ProductionStatus.HAS_CALL) {
                order.setProductionStatus(ProductionStatus.PRINTED);
            }
            if (order.getDataOrigin() == 1 && order.getProductionStatus() != ProductionStatus.HAS_CALL && order.getProductionStatus() != ProductionStatus.PRINTED) {
                order.setProductionStatus(ProductionStatus.PRINTED);
            }
            order.setConfirmTime(new Date());
            order.setAllowCancel(false);
            BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
            if (order.getParentOrderId() == null) {
                if (setting.getAppraiseMinMoney().compareTo(order.getOrderMoney()) <= 0 || setting.getAppraiseMinMoney().compareTo(order.getAmountWithChildren()) <= 0) {
                    order.setAllowAppraise(true);
                }
            } else {
                log.info("最小评论金额为:" + setting.getAppraiseMinMoney() + ", oid:" + order.getId());
                order.setAllowAppraise(false);
            }
            //如果是后付订单 则支付后不允许加菜
            if(order.getPayType() == PayType.NOPAY && order.getAllowContinueOrder()){
                order.setAllowContinueOrder(false);
            }
            update(order);
            LogTemplateUtils.getConfirmByOrderType(brand.getBrandName(), order, orginState, "confirmBossOrder");
            jsonResult.setSuccess(true);
            jsonResult.setData(order);
            return jsonResult;
        } else {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("订单状态不符");
            return jsonResult;
        }
    }

    @Override
    public Order getOrderInfo(String orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            return null;
        }
        if (order.getBaseCustomerCount() != null && order.getBaseCustomerCount() > 0) {//订单有原始就餐人数
            List<com.alibaba.fastjson.JSONObject> objectList = new ArrayList<>();
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());//查询出该笔订单所在店铺
            if (order.getIsUseNewService().equals(Common.YES)) { //如果该笔订单产生的是新版服务费
                if (shopDetail.getIsOpenTablewareFee().equals(Common.YES)) { //开通了餐具费
                    jsonObject.put("id", shopDetail.getId() + "10");
                    jsonObject.put("type", 10);
                    jsonObject.put("name", shopDetail.getTablewareFeeName());
                    jsonObject.put("count", order.getTablewareFeeCount() == null ? 0 : order.getTablewareFeeCount());//产生数量
                    jsonObject.put("unitPrice", Integer.valueOf(jsonObject.get("count").toString()) > 0 ? order.getTablewareFeePrice().divide(new BigDecimal(jsonObject.get("count").toString())) : 0);//单价
                    objectList.add(jsonObject);
                }
                if (shopDetail.getIsOpenTowelFee().equals(Common.YES)) { //开通了纸巾费
                    jsonObject = new com.alibaba.fastjson.JSONObject();
                    jsonObject.put("id", shopDetail.getId() + "11");
                    jsonObject.put("type", 11);
                    jsonObject.put("name", shopDetail.getTowelFeeName());
                    jsonObject.put("count", order.getTowelFeeCount() == null ? 0 : order.getTowelFeeCount());//产生数量
                    jsonObject.put("unitPrice", Integer.valueOf(jsonObject.get("count").toString()) > 0 ? order.getTowelFeePrice().divide(new BigDecimal(jsonObject.get("count").toString())) : 0);//单价
                    objectList.add(jsonObject);
                }
                if (shopDetail.getIsOpenSauceFee().equals(Common.YES)) { //开通了酱料费
                    jsonObject = new com.alibaba.fastjson.JSONObject();
                    jsonObject.put("id", shopDetail.getId() + "12");
                    jsonObject.put("type", 12);
                    jsonObject.put("name", shopDetail.getSauceFeeName());
                    jsonObject.put("count", order.getSauceFeeCount() == null ? 0 : order.getSauceFeeCount());//产生数量
                    jsonObject.put("unitPrice", Integer.valueOf(jsonObject.get("count").toString()) > 0 ? order.getSauceFeePrice().divide(new BigDecimal(jsonObject.get("count").toString())) : 0);//单价
                    objectList.add(jsonObject);
                }
            } else { //旧版服务费
                jsonObject.put("id", shopDetail.getId());
                jsonObject.put("type", 0);
                jsonObject.put("name", shopDetail.getServiceName());
                jsonObject.put("count", order.getCustomerCount());//产生数量
                jsonObject.put("unitPrice", shopDetail.getServicePrice());//单价
                objectList.add(jsonObject);
            }
            order.setServiceList(objectList);
        }
        Map<String, String> param = new HashMap<>();
        param.put("orderId", order.getId());
        List<OrderItem> orderItems = orderItemService.listByOrderId(param);
        order.setOrderItems(orderItems);
        Customer cus = customerService.selectById(order.getCustomerId());
        order.setCustomer(cus);
        return order;
    }

    @Override
    public Order getOrderInfoPos(String orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            return null;
        }
        if (order.getBaseCustomerCount() != null && order.getBaseCustomerCount() > 0) {//订单有原始就餐人数
            List<com.alibaba.fastjson.JSONObject> objectList = new ArrayList<>();
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());//查询出该笔订单所在店铺
            if (order.getIsUseNewService().equals(Common.YES)) { //如果该笔订单产生的是新版服务费
                if (shopDetail.getIsOpenSauceFee().equals(Common.YES)) { //开通了餐具费
                    jsonObject.put("id", shopDetail.getId() + "10");
                    jsonObject.put("type", 10);
                    jsonObject.put("name", shopDetail.getSauceFeeName());
                    jsonObject.put("count", order.getSauceFeeCount() == null ? 0 : order.getSauceFeeCount());//产生数量
                    jsonObject.put("unitPrice", Integer.valueOf(jsonObject.get("count").toString()) > 0 ? order.getSauceFeePrice().divide(new BigDecimal(jsonObject.get("count").toString())) : 0);//单价
                    objectList.add(jsonObject);
                }
                if (shopDetail.getIsOpenTowelFee().equals(Common.YES)) { //开通了纸巾费
                    jsonObject = new com.alibaba.fastjson.JSONObject();
                    jsonObject.put("id", shopDetail.getId() + "11");
                    jsonObject.put("type", 11);
                    jsonObject.put("name", shopDetail.getTowelFeeName());
                    jsonObject.put("count", order.getTowelFeeCount() == null ? 0 : order.getTowelFeeCount());//产生数量
                    jsonObject.put("unitPrice", Integer.valueOf(jsonObject.get("count").toString()) > 0 ? order.getTowelFeePrice().divide(new BigDecimal(jsonObject.get("count").toString())) : 0);//单价
                    objectList.add(jsonObject);
                }
                if (shopDetail.getIsOpenTablewareFee().equals(Common.YES)) { //开通了酱料费
                    jsonObject = new com.alibaba.fastjson.JSONObject();
                    jsonObject.put("id", shopDetail.getId() + "12");
                    jsonObject.put("type", 12);
                    jsonObject.put("name", shopDetail.getTablewareFeeName());
                    jsonObject.put("count", order.getTablewareFeeCount() == null ? 0 : order.getTablewareFeeCount());//产生数量
                    jsonObject.put("unitPrice", Integer.valueOf(jsonObject.get("count").toString()) > 0 ? order.getTablewareFeePrice().divide(new BigDecimal(jsonObject.get("count").toString())) : 0);//单价
                    objectList.add(jsonObject);
                }
            } else { //旧版服务费
                jsonObject.put("id", shopDetail.getId());
                jsonObject.put("type", 0);
                jsonObject.put("name", shopDetail.getServiceName());
                jsonObject.put("count", order.getCustomerCount());//产生数量
                jsonObject.put("unitPrice", shopDetail.getServicePrice());//单价
                objectList.add(jsonObject);
            }
            order.setServiceList(objectList);
        }
        Map<String, String> param = new HashMap<>();
        param.put("orderId", order.getId());
        List<OrderItem> orderItems = orderItemService.listByOrderIdPos(param);
        order.setOrderItems(orderItems);
        Customer cus = customerService.selectById(order.getCustomerId());
        order.setCustomer(cus);
        return order;
    }

    @Override
    public List<Order> selectHistoryOrderList(String currentShopId, Date date, Integer shopMode) {
        return orderMapper.listHoufuFinishedOrder(currentShopId, shopMode);
    }

    @Override
    public List<Order> selectErrorOrderList(String currentShopId, Date date) {
        Date begin = DateUtil.getDateBegin(date);
        Date end = DateUtil.getDateEnd(date);
        return orderMapper.selectErrorOrderList(currentShopId, begin, end);
    }

    @Override
    public List<Order> selectErrorOrder(Date date) {
        Date begin = DateUtil.getDateBegin(date);
        Date end = DateUtil.getDateEnd(date);
        return orderMapper.selectErrorOrder(begin, end);
    }

    @Override
    public List<Order> getOrderNoPayList(String currentShopId, Date date) {
        Date begin = DateUtil.getDateBegin(date);
        Date end = DateUtil.getDateEnd(date);
        return orderMapper.getOrderNoPayList(currentShopId, begin, end);
    }

    @Override
    public Order cancelOrderPos(String orderId) throws AppException {
        Order order = selectById(orderId);
        Brand brand = brandService.selectById(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
//        if (order.getClosed()) {
//            throw new AppException(AppException.ORDER_IS_CLOSED);
//        } else {
            order.setClosed(true);
            order.setAllowAppraise(false);
            order.setAllowContinueOrder(false);
            order.setAllowCancel(false);
            order.setOrderState(OrderState.CANCEL);
            update(order);
            refundOrder(order);
            log.info("取消订单成功:" + order.getId());

            //拒绝订单后还原库存
            Boolean addStockSuccess = false;
            addStockSuccess = addStock(getOrderInfo(orderId));
            if (!addStockSuccess) {
                log.info("库存还原失败:" + order.getId());
            }
//            orderMapper.setStockBySuit(order.getShopDetailId());//自动更新套餐数量
//            Map map = new HashMap(4);
//            map.put("brandName", brand.getBrandName());
//            map.put("fileName", shopDetail.getName());
//            map.put("type", "posAction");
//            map.put("content", "店铺:"+shopDetail.getName()+"在pos端执行拒绝订单:" + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
//            doPostAnsc(url, map);
            LogTemplateUtils.cancelOrderByOrderType(brand.getBrandName(), orderId);
            LogTemplateUtils.cancleOrderByPosType(brand.getBrandName(), shopDetail.getName(), order.getId());

//            Map orderMap = new HashMap(4);
//            orderMap.put("brandName", brand.getBrandName());
//            orderMap.put("fileName", orderId);
//            orderMap.put("type", "orderAction");
//            orderMap.put("content", "订单:" + order.getId() + "在pos端被拒绝,请求服务器地址为:" + MQSetting.getLocalIP());
//            doPostAnsc(url, map);
//        }
        if(order.getBeforeId() != null){
            orderBeforeService.updateState(order.getBeforeId(), 2);
        }
        return order;
    }

    @Override
    public void changePushOrder(Order order) {
        order = selectById(order.getId());
        if (order.getProductionStatus() == ProductionStatus.HAS_ORDER) { //如果还是已下单状态，则将订单状态改为未下单,并且订单改为可以取消
            orderMapper.clearPushOrder(order.getId(), ProductionStatus.NOT_ORDER);
        }
    }

    @Override
    public List<Map<String, Object>> printOrderAll(String orderId) {
        List<Map<String, Object>> printTask = new ArrayList<>();
        log.info("打印订单全部:" + orderId);
        Order order = selectById(orderId);
        if (order.getDistributionModeId() == DistributionType.DELIVERY_MODE_ID) {
            return printTask;
        }
        if (order.getPrintTimes() != 1) {
            if (!redisService.set(orderId, 1)) {
                return printTask;
            }
            if (order.getProductionStatus() >= ProductionStatus.PRINTED) {
                return printTask;
            }
        } else {
            if (!redisService.set(orderId + "print", 1)) {
                return printTask;
            }
        }
        Map<String, String> param = new HashMap<>();
        param.put("orderId", order.getId());
        List<OrderItem> items = orderItemService.listByOrderId(param);


        TableQrcode tableQrcode = tableQrcodeService.selectByTableNumberShopId(order.getShopDetailId(), Integer.valueOf(order.getTableNumber()));


        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
        List<Printer> ticketPrinter = new ArrayList<>();
        if (tableQrcode == null || order.getDistributionModeId() == DistributionType.TAKE_IT_SELF) {
            ticketPrinter = printerService.selectByShopAndType(shopDetail.getId(), PrinterType.RECEPTION);
        } else {
            if (tableQrcode.getAreaId() == null) {
                ticketPrinter = printerService.selectQiantai(shopDetail.getId(), PrinterRange.QIANTAI);
            } else {
                Area area = areaService.selectById(tableQrcode.getAreaId());
                ticketPrinter = printerService.selectQiantai(shopDetail.getId(), PrinterRange.QIANTAI);
                if (area != null && area.getPrintId() != null) {
                    Printer printer = printerService.selectById(area.getPrintId().intValue());
                    ticketPrinter.add(printer);
                }
            }

        }
        if (order.getOrderMode() == ShopMode.BOSS_ORDER && order.getPrintTimes() == 1) {

            List<OrderItem> child = orderItemService.listByParentId(orderId);
            for (OrderItem orderItem : child) {
                order.setOriginalAmount(order.getOriginalAmount().add(orderItem.getOriginalPrice().multiply(BigDecimal.valueOf(orderItem.getCount()))));
//                order.setOrderMoney(order.getOrderMoney().add(orderItem.getFinalPrice()));
            }
            order.setOrderMoney(order.getAmountWithChildren().doubleValue() > 0 ? order.getAmountWithChildren() : order.getOrderMoney());
            child.addAll(items);

            for (Printer printer : ticketPrinter) {
                Map<String, Object> ticket = null;
                if (shopDetail.getIsPosNew().equals(Common.YES)) {
                    ticket = printTicketPosNew(order, child, shopDetail, printer);
                } else {
                    ticket = printTicket(order, child, shopDetail, printer);
                }

                if (ticket != null) {
                    printTask.add(ticket);
                }
            }
            return printTask;
        }


        if ((order.getPrintOrderTime() != null || order.getProductionStatus() >= 2) && order.getOrderMode() != ShopMode.HOUFU_ORDER) {
            return printTask;
        }


        if (setting.getAutoPrintTotal().intValue() == 0 && shopDetail.getAutoPrintTotal() == 0 &&
                (order.getOrderMode() != ShopMode.HOUFU_ORDER || (order.getOrderState() == OrderState.SUBMIT && order.getOrderMode() == ShopMode.HOUFU_ORDER))) {
            List<OrderItem> child = orderItemService.listByParentId(orderId);
            for (OrderItem orderItem : child) {
                order.setOriginalAmount(order.getOriginalAmount().add(orderItem.getOriginalPrice().multiply(BigDecimal.valueOf(orderItem.getCount()))));
//                order.setPaymentAmount(order.getPaymentAmount().add(orderItem.getFinalPrice()));
            }
            child.addAll(items);
            for (Printer printer : ticketPrinter) {
                Map<String, Object> ticket = null;
                if (shopDetail.getIsPosNew().equals(Common.YES)) {
                    ticket = printTicketPosNew(order, child, shopDetail, printer);
                } else {
                    ticket = printTicket(order, child, shopDetail, printer);
                }
                if (ticket != null) {
                    printTask.add(ticket);
                }

            }
        }

        if ((order.getOrderMode().equals(ShopMode.HOUFU_ORDER)) && order.getOrderState().equals(OrderState.PAYMENT)
                && setting.getIsPrintPayAfter().equals(Common.YES) && shopDetail.getIsPrintPayAfter().equals(Common.YES)) {
            List<OrderItem> child = orderItemService.listByParentId(orderId);
            for (OrderItem orderItem : child) {
                order.setOriginalAmount(order.getOriginalAmount().add(orderItem.getFinalPrice()));
//                order.setPaymentAmount(order.getPaymentAmount().add(orderItem.getFinalPrice()));
            }
            child.addAll(items);

            for (Printer printer : ticketPrinter) {
                Map<String, Object> ticket = null;
                if (shopDetail.getIsPosNew().equals(Common.YES)) {
                    ticket = printTicketPosNew(order, child, shopDetail, printer);
                } else {
                    ticket = printTicket(order, child, shopDetail, printer);
                }
                if (ticket != null) {
                    printTask.add(ticket);
                }
            }
        }

        //如果是外带，添加一张外带小票
        if (order.getDistributionModeId().equals(DistributionType.TAKE_IT_SELF) && setting.getIsPrintPayAfter().equals(Common.NO)) {
            List<Printer> packagePrinter = printerService.selectByShopAndType(order.getShopDetailId(), PrinterType.PACKAGE); //查找外带的打印机
            for (Printer printer : packagePrinter) {
                Map<String, Object> packageTicket = null;
                if (shopDetail.getIsPosNew().equals(Common.YES)) {
                    packageTicket = printTicketPosNew(order, items, shopDetail, printer);
                } else {
                    packageTicket = printTicket(order, items, shopDetail, printer);
                }
                if (packageTicket != null) {
                    printTask.add(packageTicket);
                }
            }
        }

        param.put("typeGroup", "true");
        items = orderItemService.listByOrderId(param);
        List<Map<String, Object>> kitchenTicket = printKitchen(order, items, order.getDistributionModeId());

        if (kitchenTicket != null && !kitchenTicket.isEmpty() && order.getOrderMode() == ShopMode.HOUFU_ORDER && order.getProductionStatus() == ProductionStatus.HAS_ORDER) {
            printTask.addAll(kitchenTicket);
        }
        if (kitchenTicket != null && !kitchenTicket.isEmpty() && order.getOrderMode() != ShopMode.HOUFU_ORDER) {
            printTask.addAll(kitchenTicket);
        }
        return printTask;
    }

    @Override
    public void setTableNumber(String orderId, String tableNumber) {
        orderMapper.setOrderNumber(orderId, tableNumber);
    }

    @Override
    public List<Order> selectOrderByVercode(String vercode, String shopId) {
        List<Order> orderList = orderMapper.selectOrderByVercode(vercode, shopId);
        return orderList;
    }

    @Override
    public List<Order> selectOrderByTableNumber(String tableNumber, String shopId) {
        List<Order> orderList = orderMapper.selectOrderByTableNumber(tableNumber, shopId);
        return orderList;
    }

    @Override
    public void updateDistributionMode(Integer modeId, String orderId) {
        Order order = selectById(orderId);
        order.setDistributionModeId(modeId);
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public void clearNumber(String currentShopId) {
        orderProductionStateContainer.clearMap(currentShopId);

    }

    @Override
    public List<Order> listOrderByStatus(String currentShopId, Date begin, Date end, int[] productionStatus,
                                         int[] orderState) {
        return orderMapper.listOrderByStatus(currentShopId, begin, end, productionStatus, orderState);
    }

    @Override
    public void updateAllowContinue(String id, boolean b) {
        orderMapper.changeAllowContinue(id, b);
        Order order = selectById(id);
        if (order != null && !StringUtils.isEmpty(order.getGroupId())) {
            //如果订单是在组里的
            //禁止加菜后，组释放，并且删除所有 人与组的关系，并且删除该组的购物车
            TableGroup tableGroup = tableGroupService.selectByGroupId(order.getGroupId());
            tableGroup.setState(TableGroup.FINISH);
            tableGroupService.update(tableGroup);
            customerGroupService.removeByGroupId(order.getGroupId());
            shopCartService.resetGroupId(order.getGroupId());
        }
    }

    @Override
    public Order findCustomerNewPackage(String currentCustomerId, String currentShopId) {
        String oid = orderMapper.selectNewCustomerPackageId(currentCustomerId, currentShopId);
        Order order = null;
        if (StringUtils.isNoneBlank(oid)) {
            Date beginDate = DateUtil.getAfterDayDate(new Date(), -15);
            order = findCustomerNewOrder(beginDate, null, null, oid);
        }
        return order;
    }

//	@Override
//	public SaleReportDto selectArticleSumCountByData(String beginDate,String endDate,String brandId) {
//		Date begin = DateUtil.getformatBeginDate(beginDate);
//		Date end = DateUtil.getformatEndDate(endDate);
//		List<ShopDetail> list_shopDetail = shopDetailService.selectByBrandId(brandId);
//		int totalNum = 0;
//		for(ShopDetail shop : list_shopDetail){
//			int sellNum = orderMapper.selectArticleSumCountByData(begin, end, shop.getId());
//			totalNum += sellNum;
//			shop.setArticleSellNum(sellNum);
//		}
//		SaleReportDto saleReport = new SaleReportDto(list_shopDetail.get(0).getBrandName(), totalNum, list_shopDetail);
//		return saleReport;
//	}

    @Override
    public List<ArticleSellDto> selectShopArticleSellByDate(String beginDate, String endDate, String shopId, String sort) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if ("0asc".equals(sort)) {
            sort = "r.peference ,r.sort";
        } else if ("2".equals(sort.substring(0, 1))) {
            sort = "r.shopSellNum" + " " + sort.substring(1, sort.length());
        } else if ("3".equals(sort.subSequence(0, 1))) {
            sort = "r.brandSellNum" + " " + sort.substring(1, sort.length());
        }
        //ShopDetail shop = shopDetailService.selectById(shopId);


//		else if("4".equals(sort.substring(0,1))){
//			sort="salesRatio"+" "+sort.substring(1,sort.length());
//		}

        List<ArticleSellDto> list = orderMapper.selectShopArticleSellByDate(begin, end, shopId, sort);


        //计算总菜品销售额,//菜品总销售额
        double num = 0;

        BigDecimal temp = BigDecimal.ZERO;
        for (ArticleSellDto articleSellDto : list) {
            //计算总销量 不能加上套餐的数量
            if (articleSellDto.getType() != 3) {
                num += articleSellDto.getShopSellNum().doubleValue();
            }
            //计算总销售额
            temp = add(temp, articleSellDto.getSalles());
        }

        for (ArticleSellDto articleSellDto : list) {

            if (articleSellDto.getType() == 3) {
                articleSellDto.setTypeName("套餐");
            } else {
                articleSellDto.setTypeName("单品");
            }

            //销售额占比
            BigDecimal d = articleSellDto.getSalles().divide(temp, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            articleSellDto.setSalesRatio(d + "%");

            if (num != 0) {
                double d1 = articleSellDto.getShopSellNum().doubleValue();
                double d2 = d1 / num * 100;

                //保留两位小数
                BigDecimal b = new BigDecimal(d2);
                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                articleSellDto.setNumRatio(f1 + "%");
            }

        }

        return list;

    }

    @Override
    public List<ArticleSellDto> selectBrandArticleSellByDate(String beginDate, String endDate, String sort) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if ("0".equals(sort)) {
            sort = "f.peference ,a.sort";
        } else if ("desc".equals(sort)) {
            sort = "brand_report.brandSellNum desc";
        } else if ("asc".equals(sort)) {
            sort = "brand_report.brandSellNum asc";
        }
        List<ArticleSellDto> list = orderMapper.selectBrandArticleSellByDate(begin, end, sort);
        return list;
    }

    //根据店铺id和订单id获取订单序号的方法
    private String nextNumber(String sid, String oid) {
        //定义number
        int number;
        //先从订单map中查找
        String key = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
        //查看orderMap中是否有值
        Map<String, Integer> ordermap = NUMBER_ORDER_MAP.get(key);
        if (ordermap == null) {
            NUMBER_ORDER_MAP.clear();
            ordermap = new HashMap<>();
            NUMBER_ORDER_MAP.put(key, ordermap);
        }
        Map<String, Integer> shopmap = NUMBER_SHOP_MAP.get(key);
        if (shopmap == null) {
            NUMBER_SHOP_MAP.clear();
            shopmap = new HashMap<>();
            NUMBER_SHOP_MAP.put(key, shopmap);
        }
        //从ordermap里面找有没有number，有就返回
        //没有的话，找shopmap里面的数字是多少，如果没有就是1，如果有就+1 并分别存入shopmap和ordermap
        Integer num1 = ordermap.get(oid);
        if (num1 != null) {
            number = num1.intValue();
        } else {
            Integer num2 = shopmap.get(sid);
            if (num2 != null) {
                number = num2.intValue() + 1;
                ordermap.put(oid, number);
                shopmap.put(sid, number);
            } else {
                shopmap.put(sid, 1);
                ordermap.put(oid, 1);
                number = 1;
            }
        }
        return numberToString(number);
    }

    //int转String('001')
    public String numberToString(int num) {
        Format f = new DecimalFormat("000");
        return f.format(num);
    }

    @Override
    public List<ArticleSellDto> selectBrandArticleSellByDateAndArticleFamilyId(String beginDate, String endDate,
                                                                               String articleFamilyId, String sort) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if ("0".equals(sort)) {
            sort = "f.peference ,a.sort";
        } else if ("desc".equals(sort)) {
            sort = "brand_report.brandSellNum desc";
        } else if ("asc".equals(sort)) {
            sort = "brand_report.brandSellNum asc";
        }
        return orderMapper.selectBrandArticleSellByDateAndArticleFamilyId(begin, end, articleFamilyId, sort);
    }

    @Override
    public List<ArticleSellDto> selectShopArticleSellByDateAndArticleFamilyId(String beginDate, String endDate, String shopId, String articleFamilyId, String sort) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if ("0asc".equals(sort)) {
            sort = "f.peference ,a.sort";
        } else if ("2".equals(sort.substring(0, 1))) {
            sort = "shop_report.shopSellNum" + sort.substring(1, sort.length());
        } else if ("3".equals(sort.subSequence(0, 1))) {
            sort = "brand_report.brandSellNum" + sort.substring(1, sort.length());
        } else if ("4".equals(sort.substring(0, 1))) {
            sort = "salesRatio" + sort.substring(1, sort.length());
        }

        return orderMapper.selectShopArticleSellByDateAndArticleFamilyId(begin, end, shopId, articleFamilyId, sort);
    }

    @Override
    public List<ArticleSellDto> selectShopArticleByDate(String shopId, String beginDate, String endDate,
                                                        String sort) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if ("0".equals(sort)) {
            sort = "peference ,sort";
        } else if ("desc".equals(sort)) {
            sort = "shopSellNum desc";
        } else if ("asc".equals(sort)) {
            sort = "shopSellNum asc";
        }
        List<ArticleSellDto> list = orderMapper.selectShopArticleByDate(shopId, begin, end, sort);
        return list;

    }

    @Override
    public List<ArticleSellDto> selectShopArticleByDateAndArcticleFamilyId(String beginDate, String endDate, String shopId,
                                                                           String articleFamilyId, String sort) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if ("0".equals(sort)) {
            sort = "f.peference ,a.sort";
        } else if ("desc".equals(sort)) {
            sort = "shop_report.shopSellNum desc";
        } else if ("asc".equals(sort)) {
            sort = "shop_report.shopSellNum asc";
        }
        return orderMapper.selectShopArticleByDateAndArticleFamilyId(begin, end, shopId, articleFamilyId, sort);
    }


    @Override
    public Boolean checkShop(String orderId, String shopId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            return false;
        } else {
            return order.getShopDetailId().equals(shopId);
        }
    }


    /**
     * 查询菜品销售报表
     *
     * @param beginDate
     * @param endDate
     * @param brandId
     * @param brandName
     * @return
     */
    @Override
    public brandArticleReportDto callBrandArticleNum(String beginDate, String endDate, String brandId, String brandName) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        //菜品总数单独算是因为 要出去套餐的数量
        Integer totalNums = orderMapperReport.selectBrandArticleNum(begin, end, brandId);
        //查询菜品总额，退菜总数，退菜金额
        brandArticleReportDto bo = new brandArticleReportDto(brandName, 0, BigDecimal.ZERO, 0, BigDecimal.ZERO, BigDecimal.ZERO, 0, BigDecimal.ZERO);
        List<brandArticleReportDto> articleReportDto = orderMapperReport.selectConfirmMoney(begin, end, brandId);
        if (articleReportDto != null && !articleReportDto.isEmpty()) {
            for (brandArticleReportDto reportDto : articleReportDto) {
                bo.setSellIncome(bo.getSellIncome().add(reportDto.getSellIncome()));
                bo.setRefundCount(bo.getRefundCount() + reportDto.getRefundCount());
                bo.setDiscountTotal(bo.getDiscountTotal().add(reportDto.getDiscountTotal()));
                bo.setRefundTotal(bo.getRefundTotal().add(reportDto.getRefundTotal()));
                bo.setGrantCount(bo.getGrantCount()+ reportDto.getGrantCount());
                bo.setGrantTotal(bo.getGrantTotal().add(reportDto.getGrantTotal()));
            }
        }
        bo.setTotalNum(totalNums);
        return bo;
    }


    @Override
    public List<ShopArticleReportDto> callShopArticleDetails(String beginDate, String endDate, String brandId, List<ShopDetail> shopDetails) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if (shopDetails.isEmpty()) {
            shopDetails = shopDetailService.selectByBrandId(brandId);
        }
        //查询每个店铺的菜品销售的和
        List<ShopArticleReportDto> list = orderMapperReport.selectShopArticleSell(begin, end, brandId);
        List<ShopArticleReportDto> listArticles = new ArrayList<>();
        for (ShopDetail shop : shopDetails) {
            ShopArticleReportDto st = new ShopArticleReportDto(shop.getId(), shop.getName(), 0, BigDecimal.ZERO, "0.00%", 0, BigDecimal.ZERO, BigDecimal.ZERO, 0, BigDecimal.ZERO);
            listArticles.add(st);
        }
        //计算所有店铺的菜品销售的和
        BigDecimal sum = new BigDecimal(0);
        //计算所有店铺的菜品销售的和
        if (!list.isEmpty()) {
            for (ShopArticleReportDto shopArticleReportDto2 : list) {
                //计算减去退菜销售额
                sum = sum.add(shopArticleReportDto2.getSellIncome());
            }
            for (ShopArticleReportDto shopArticleReportDto : listArticles) {
                for (ShopArticleReportDto shopArticleReportDto2 : list) {
                    if (shopArticleReportDto2.getShopId().equals(shopArticleReportDto.getShopId())) {
                        shopArticleReportDto.setSellIncome(shopArticleReportDto.getSellIncome().add(shopArticleReportDto2.getSellIncome()));
                        shopArticleReportDto.setDiscountTotal(shopArticleReportDto.getDiscountTotal().add(shopArticleReportDto2.getDiscountTotal()));
                        shopArticleReportDto.setTotalNum(shopArticleReportDto.getTotalNum() + shopArticleReportDto2.getTotalNum());
                        shopArticleReportDto.setRefundCount(shopArticleReportDto.getRefundCount() + shopArticleReportDto2.getRefundCount());
                        shopArticleReportDto.setRefundTotal(shopArticleReportDto.getRefundTotal().add(shopArticleReportDto2.getRefundTotal()));
                        shopArticleReportDto.setGrantCount(shopArticleReportDto.getGrantCount() + shopArticleReportDto2.getGrantCount());
                        shopArticleReportDto.setGrantTotal(shopArticleReportDto.getGrantTotal().add(shopArticleReportDto2.getGrantTotal()));
                    }
                }
                String occupy = shopArticleReportDto.getSellIncome() == null ? "0" : shopArticleReportDto.getSellIncome().divide(sum, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP)
                        .toString();
                shopArticleReportDto.setOccupy(occupy + "%");
            }
        }
        return listArticles;
    }

    @Override
    public List<ShopArticleReportDto> callShopArticleDetailsNew(String beginDate, String endDate, String brandId, List<ShopDetail> shopDetails) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if (shopDetails.isEmpty()) {
            shopDetails = shopDetailService.selectByBrandId(brandId);
        }
        //查询每个店铺的菜品销售的和
        List<ShopArticleReportDto> list = orderMapperReport.selectShopArticleSellNew(begin, end, brandId);
        List<ShopArticleReportDto> listArticles = new ArrayList<>();
        for (ShopDetail shop : shopDetails) {
            ShopArticleReportDto st = new ShopArticleReportDto(shop.getId(), shop.getName(), 0, BigDecimal.ZERO, "0.00%", 0, BigDecimal.ZERO, BigDecimal.ZERO, 0, BigDecimal.ZERO);
            listArticles.add(st);
        }
        //计算所有店铺的菜品销售的和
        BigDecimal sum = new BigDecimal(0);
        //计算所有店铺的菜品销售的和
        if (!list.isEmpty()) {
            for (ShopArticleReportDto shopArticleReportDto2 : list) {
                //计算减去退菜销售额
                sum = sum.add(shopArticleReportDto2.getSellIncome());
            }
            for (ShopArticleReportDto shopArticleReportDto : listArticles) {
                for (ShopArticleReportDto shopArticleReportDto2 : list) {
                    if (shopArticleReportDto2.getShopId().equals(shopArticleReportDto.getShopId())) {
                        shopArticleReportDto.setSellIncome(shopArticleReportDto.getSellIncome().add(shopArticleReportDto2.getSellIncome()));
                        shopArticleReportDto.setDiscountTotal(shopArticleReportDto.getDiscountTotal().add(shopArticleReportDto2.getDiscountTotal()));
                        shopArticleReportDto.setTotalNum(shopArticleReportDto.getTotalNum() + shopArticleReportDto2.getTotalNum());
                        shopArticleReportDto.setRefundCount(shopArticleReportDto.getRefundCount() + shopArticleReportDto2.getRefundCount());
                        shopArticleReportDto.setRefundTotal(shopArticleReportDto.getRefundTotal().add(shopArticleReportDto2.getRefundTotal()));
                    }
                }
                String occupy = shopArticleReportDto.getSellIncome() == null ? "0" : shopArticleReportDto.getSellIncome().divide(sum, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP)
                        .toString();
                shopArticleReportDto.setOccupy(occupy + "%");
            }
        }
        return listArticles;
    }

    private BigDecimal add(BigDecimal temp, BigDecimal sellIncome) {
        // TODO Auto-generated method stub
        return temp.add(sellIncome);
    }

    @Override
    public List<ArticleSellDto> selectBrandArticleSellByDateAndFamilyId(String brandid, String beginDate, String endDate, String sort) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if ("0".equals(sort)) {
            sort = "peference";
        } else if ("desc".equals(sort)) {
            sort = "brandSellNum desc";
        } else if ("asc".equals(sort)) {
            sort = "brandSellNum asc";
        }
        List<ArticleSellDto> list = orderMapper.selectBrandArticleSellByDateAndFamilyId(brandid, begin, end, sort);
        //计算总菜品销售额
        BigDecimal temp = BigDecimal.ZERO;
        for (ArticleSellDto articleSellDto : list) {
            temp = add(temp, articleSellDto.getSalles());
        }
        for (ArticleSellDto articleSellDto : list) {
            double c = articleSellDto.getSalles().divide(temp, 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
            DecimalFormat myformat = new DecimalFormat("0.00");
            String str = myformat.format(c);
            str = str + "%";
            articleSellDto.setSalesRatio(str);
        }

        return list;
    }

    @Override
    public List<ArticleSellDto> selectBrandArticleSellByDateAndId(String brandId, String beginDate, String endDate, String sort) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if ("0".equals(sort)) {
            sort = "peference , sort";
        } else if ("desc".equals(sort)) {
            sort = "brandSellNum desc";
        } else if ("asc".equals(sort)) {
            sort = "brandSellNum asc";
        }
        List<ArticleSellDto> list = orderMapper.selectBrandArticleSellByDateAndId(brandId, begin, end, sort);
        //计算总菜品销售额,//菜品总销售额
        double num = 0;

        BigDecimal temp = BigDecimal.ZERO;
        for (ArticleSellDto articleSellDto : list) {
            //计算总销量 不能加上套餐的数量
            if (articleSellDto.getType() != 3) {
                //新增减去退菜的数量
                num += (articleSellDto.getBrandSellNum().doubleValue() - articleSellDto.getRefundCount());

            }
            //计算总销售额
            //新增减去退菜金额
            temp = add(temp, (articleSellDto.getSalles().subtract(articleSellDto.getRefundTotal())));
        }

        for (ArticleSellDto articleSellDto : list) {
            //判断菜品的类型

            if (articleSellDto.getType() == 3) {
                articleSellDto.setTypeName("套餐");
            } else {
                articleSellDto.setTypeName("单品");
            }

            //销售额占比
            BigDecimal d = articleSellDto.getSalles().divide(temp, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            articleSellDto.setSalesRatio(d + "%");

            if (num != 0) {
                double d1 = articleSellDto.getBrandSellNum().doubleValue();
                double d2 = d1 / num * 100;

                //保留两位小数
                BigDecimal b = new BigDecimal(d2);
                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                articleSellDto.setNumRatio(f1 + "%");
            }
        }

        return list;
    }

    @Override
    public List<ArticleSellDto> selectArticleFamilyByBrandAndFamilyName(String brandId, String beginDate, String endDate, String articleFamilyName) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        List<ArticleSellDto> list = orderMapper.selectArticleFamilyByBrandAndFamilyName(brandId, begin, end, articleFamilyName);
        //计算总菜品销售额
        BigDecimal temp = BigDecimal.ZERO;
        for (ArticleSellDto articleSellDto : list) {
            temp = add(temp, articleSellDto.getSalles());
        }
        for (ArticleSellDto articleSellDto : list) {
            double c = articleSellDto.getSalles().divide(temp, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
            DecimalFormat myformat = new DecimalFormat("0.00");
            String str = myformat.format(c);
            str = str + "%";
            articleSellDto.setSalesRatio(str);
        }

        return list;
    }

    @Override
    public Map<String, Object> callMoneyAndNumByDate(String beginDate, String endDate, String brandId, String brandName, List<ShopDetail> shopDetailList) {
        //封装品牌的数据
        BrandOrderReportDto brandOrderReportDto;
        Map<String, Object> selectBrandMap = new HashMap<>();
        selectBrandMap.put("beginDate", beginDate);
        selectBrandMap.put("endDate", endDate);
        selectBrandMap.put("brandId", brandId);
        brandOrderReportDto = orderMapperReport.procDayAllOrderItemBrand(selectBrandMap);
        if (brandOrderReportDto == null) {
            return null;
        }
        brandOrderReportDto.setBrandName(brandName);
        //计算单均
        if (brandOrderReportDto.getOrderCount() != 0 && brandOrderReportDto.getOrderPrice() != null) {
            BigDecimal singlePrice = new BigDecimal(brandOrderReportDto.getOrderPrice().doubleValue() / brandOrderReportDto.getOrderCount());
            brandOrderReportDto.setSinglePrice(new BigDecimal(singlePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
        } else {
            brandOrderReportDto.setSinglePrice(new BigDecimal("0.00"));
        }
        //计算人均
        if (brandOrderReportDto.getPeopleCount() != null && brandOrderReportDto.getPeopleCount() != 0 && brandOrderReportDto.getOrderPrice() != null) {
            BigDecimal perPersonPrice = new BigDecimal(brandOrderReportDto.getOrderPrice().doubleValue() / brandOrderReportDto.getPeopleCount());
            brandOrderReportDto.setPerPersonPrice(new BigDecimal(perPersonPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
        } else {
            brandOrderReportDto.setPerPersonPrice(new BigDecimal("0.00"));
        }
        brandOrderReportDto.setPeopleCount(Optional.ofNullable(brandOrderReportDto.getPeopleCount()).orElse(0));
        brandOrderReportDto.setOrderPrice(Optional.ofNullable(brandOrderReportDto.getOrderPrice()).orElse(BigDecimal.ZERO));
        brandOrderReportDto.setTangshiPrice(Optional.ofNullable(brandOrderReportDto.getTangshiPrice()).orElse(BigDecimal.ZERO));
        brandOrderReportDto.setWaidaiPrice(Optional.ofNullable(brandOrderReportDto.getWaidaiPrice()).orElse(BigDecimal.ZERO));
        brandOrderReportDto.setWaimaiPrice(Optional.ofNullable(brandOrderReportDto.getWaimaiPrice()).orElse(BigDecimal.ZERO));
        brandOrderReportDto.setOrderPosDiscountMoney(Optional.ofNullable(brandOrderReportDto.getOrderPosDiscountMoney()).orElse(BigDecimal.ZERO));
        brandOrderReportDto.setMemberDiscountMoney(Optional.ofNullable(brandOrderReportDto.getMemberDiscountMoney()).orElse(BigDecimal.ZERO));
        brandOrderReportDto.setRealEraseMoney(Optional.ofNullable(brandOrderReportDto.getRealEraseMoney()).orElse(BigDecimal.ZERO));
        List<ShopOrderReportDto> shopOrderReportDtoLists = getBossAppOrderReport(brandId, shopDetailList, beginDate, endDate);
        //封装返回Map集
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", shopOrderReportDtoLists);
        map.put("brandId", brandOrderReportDto);
        return map;
    }

    @Override
    public List<ArticleSellDto> selectShopArticleSellByDateAndFamilyId(String beginDate, String endDate, String shopId, String sort) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if ("0".equals(sort)) {
            sort = "ap.peference";
        } else if ("desc".equals(sort)) {
            sort = "ap.shopSellNum desc";
        } else if ("asc".equals(sort)) {
            sort = "ap.shopSellNum asc";
        }
        List<ArticleSellDto> list = orderMapper.selectShopArticleSellByDateAndFamilyId(shopId, begin, end, sort);
        //计算总菜品销售额
        BigDecimal temp = BigDecimal.ZERO;
        for (ArticleSellDto articleSellDto : list) {
            temp = add(temp, articleSellDto.getSalles());
        }
        for (ArticleSellDto articleSellDto : list) {
            //double c = articleSellDto.getSalles().divide(temp, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
            double c = articleSellDto.getSalles().divide(temp, 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
            DecimalFormat myformat = new DecimalFormat("0.00");
            String str = myformat.format(c);
            str = str + "%";
            articleSellDto.setSalesRatio(str);
        }
        return list;
    }

    @Override
    public Boolean setOrderPrintFail(String orderId) {
        return orderMapper.setOrderPrintFail(orderId) > 0;
    }

    @Override
    public List<ArticleSellDto> selectShopArticleSellByDateAndId(String beginDate, String endDate, String shopId,
                                                                 String sort) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if ("0".equals(sort)) {
            sort = "f.peference , a.sort";
        } else if ("desc".equals(sort)) {
            sort = "shop_report.shopSellNum desc";
        } else if ("asc".equals(sort)) {
            sort = "shop_report.shopSellNum asc";
        }
        List<ArticleSellDto> list = orderMapper.selectShopArticleSellByDateAndId(shopId, begin, end, sort);
        //计算总菜品销售额
        //计算总菜品数
        double num = 0;

        BigDecimal temp = BigDecimal.ZERO;
        for (ArticleSellDto articleSellDto : list) {
            //计算总销量 不能加上套餐的数量
            if (articleSellDto.getType() != 3) {
                num += articleSellDto.getShopSellNum().doubleValue();
            }
            temp = add(temp, articleSellDto.getSalles());
        }
        for (ArticleSellDto articleSellDto : list) {
            //销售额占比
            BigDecimal d = articleSellDto.getSalles().divide(temp, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            articleSellDto.setSalesRatio(d + "%");
            if (num != 0) {
                double d1 = articleSellDto.getShopSellNum().doubleValue();
                double d2 = d1 / num * 100;
                //保留三位小数
                BigDecimal b = new BigDecimal(d2);
                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                articleSellDto.setNumRatio(f1 + "%");
            }
        }

        return list;
    }

//    @Override
//    public List<Order> selectListByTime(String beginDate, String endDate, String shopId) {
////        Date begin = DateUtil.getformatBeginDate(beginDate);
////        Date end = DateUtil.getformatEndDate(endDate);
////        return orderMapper.selectListByTime(begin, end, shopId);
//
//
//    }

    @Override
    public List<Order> callListByTime(String beginDate, String endDate, String shopId, String customerId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapperReport.selectListByTime(begin, end, shopId, customerId);

    }

    @Override
    public Order selectOrderDetails(String orderId) {
        Order o = orderMapper.selectOrderDetails(orderId);
        if (o == null) {
            return null;
        }
        ShopDetail shop = shopDetailService.selectById(o.getShopDetailId());
        if (shop != null) {
            o.setShopName(shop.getName());
        }

        return o;
    }

    @Override
    public OrderPayDto selectBytimeAndState(String beginDate, String endDate, String brandId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectBytimeAndState(begin, end, brandId);
    }


    @Override
    public List<Order> selectListBybrandId(String beginDate, String endDate, String brandId, Integer type) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapperReport.selectListBybrandId(begin, end, brandId, type);
    }

    @Override
    public List<Order> selectListByShopId(String beginDate, String endDate, String shopId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapperReport.selectListByShopId(begin, end, shopId);
    }


    @Override
    public List<Order> selectAppraiseByShopId(String beginDate, String endDate, String shopId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectAppraiseByShopId(begin, end, shopId);
    }


    @Override
    public Order getOrderAccount(String shopId) {
        Order order = null;
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);
        if (shopDetail.getShopMode() == ShopMode.HOUFU_ORDER) {
            order = orderMapper.getOrderAccountHoufu(shopId);
        } else if (shopDetail.getShopMode() == ShopMode.BOSS_ORDER) {
            order = orderMapper.getOrderAccountBoss(shopId);
        } else {
            order = orderMapper.getOrderAccount(shopId);
        }

        return order;
    }

    @Override
    public void autoRefundMoney() {
        log.debug("开始退款");
    }


    @Override
    public List<Map<String, Object>> printTotal(String shopId, String beginDate, String endDate) {

        List<Map<String, Object>> printTask = new ArrayList<>();
        ShopDetail shop = shopDetailService.selectById(shopId);
        Brand brand = brandService.selectById(shop.getBrandId());
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
        List<Printer> ticketPrinter = printerService.selectByShopAndType(shop.getId(), PrinterType.RECEPTION);
        for (Printer printer : ticketPrinter) {
            Map<String, Object> ticket = printTotal(brandSetting, shop, printer, beginDate, endDate);
            if (ticket != null) {
                printTask.add(ticket);
            }

        }
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", shop.getName());
        map.put("type", "posAction");
        map.put("content", "店铺:" + shop.getName() + "在pos端打印了日结小票返回模版为:" + printTask.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        return printTask;
    }


    @Override
    public List<Map<String, Object>> printKitchenReceipt(String orderId) {
        log.info("打印订单全部:" + orderId);
        Order order = selectById(orderId);
        //如果是 未打印状态 或者  异常状态则改变 生产状态和打印时间
        if (ProductionStatus.HAS_ORDER == order.getProductionStatus() || ProductionStatus.NOT_PRINT == order.getProductionStatus()) {
            order.setProductionStatus(ProductionStatus.PRINTED);
            order.setPrintOrderTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        ShopDetail shop = shopDetailService.selectById(order.getShopDetailId());
        Map<String, String> param = new HashMap<>();
        param.put("orderId", order.getId());
        param.put("typeGroup", "true");
        List<OrderItem> items = orderItemService.listByOrderId(param);
        List<Map<String, Object>> printTask = new ArrayList<>();
//        List<Printer> ticketPrinter = printerService.selectByShopAndType(shop.getId(), PrinterType.RECEPTION);
//        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
//        if(setting.getAutoPrintTotal().intValue() == 0){
//            for (Printer printer : ticketPrinter) {
//                Map<String, Object> ticket = printTicket(order, items, shop, printer);
//                if (ticket != null) {
//                    printTask.add(ticket);
//                }
//
//            }
//        }

//        if (order.getOrderMode() == ShopMode.HOUFU_ORDER) {
        List<OrderItem> child = orderItemService.listByParentId(orderId);
        for (OrderItem item : child) {
            item.setArticleName(item.getArticleName() + "(加)");
        }
        items.addAll(child);
//        }
        List<OrderItem> list = new ArrayList<>();
        if (order.getPrintKitchenFlag().equals(Common.YES)) {
            for (OrderItem item : items) {
                if (item.getPrintFailFlag() == PrintStatus.PRINT_ERROR || item.getPrintFailFlag() == PrintStatus.UNPRINT) {
                    list.add(item);
                }
            }
        } else {
            list.addAll(items);
        }

        List<Map<String, Object>> kitchenTicket = printKitchen(order, list, order.getDistributionModeId());

        //如果是外带，添加一张外带小票
        if (order.getDistributionModeId().equals(DistributionType.TAKE_IT_SELF)) {
            List<Printer> packagePrinter = printerService.selectByShopAndType(order.getShopDetailId(), PrinterType.PACKAGE); //查找外带的打印机
            param = new HashMap<>();
            param.put("orderId", order.getId());
            items = orderItemService.listByOrderId(param);
            for (Printer printer : packagePrinter) {
                Map<String, Object> packageTicket = printTicket(order, items, shop, printer);
                if (packageTicket != null) {
                    printTask.add(packageTicket);
                }
            }
        }

//        }
        if (kitchenTicket != null && !kitchenTicket.isEmpty()) {
            printTask.addAll(kitchenTicket);
        }
        Map logMap = new HashMap(4);
        Brand brand = brandService.selectById(order.getBrandId());
        logMap.put("brandName", brand.getBrandName());
        logMap.put("fileName", shop.getName());
        logMap.put("type", "posAction");
        logMap.put("content", "订单:" + order.getId() + "被商家手动打印厨打，请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(logMap);
        return printTask;
    }

    /**
     * 订单菜品的数据(用于中间数据库)
     *
     * @param brandId
     * @return
     */
    @Override
    public List<OrderArticleDto> selectOrderArticle(String brandId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectOrderArticle(brandId, begin, end);
    }

    /**
     * 封装品牌菜品数据 用于中间数据库
     *
     * @param brandId
     * @param beginDate
     * @param endDate
     * @return
     */
    @Override
    public List<Map<String, Object>> selectBrandArticleSellList(String brandId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);

        List<Map<String, Object>> list = orderMapper.selectBrandArticleSellList(brandId, begin, end);
        //计算总菜品销售额,//菜品总销售额
        int num = 0;

        BigDecimal temp = BigDecimal.ZERO;
        for (Map<String, Object> map : list) {
            //计算总销量 不能加上套餐的数量
            if ((Integer) map.get("type") != 3) {
                // num += articleSellDto.getBrandSellNum().doubleValue();
                num += Integer.parseInt(map.get("salles").toString());
            }
            //计算总销售额
            temp = add(temp, new BigDecimal(map.get("sell").toString()));
        }

        for (Map<String, Object> map2 : list) {
            //销售额占比
            // BigDecimal d = new BigDecimal(map2.get("selles").toString()).divide(temp, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            BigDecimal sell = new BigDecimal(map2.get("sell").toString());
            BigDecimal d = sell.divide(temp, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            //map2.setSalesRatio(d + "%");
            map2.put("sell_occupies", d + "%");

            if (num != 0) {
                //double d1 = articleSellDto.getBrandSellNum().doubleValue();
                double d1 = Double.parseDouble(map2.get("salles").toString());
                double d2 = d1 / num * 100;

                //保留两位小数
                BigDecimal b = new BigDecimal(d2);
                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                // articleSellDto.setNumRatio(f1 + "%");
                map2.put("salles_occupies", f1 + "%");
            }
        }

        for (Map<String, Object> map3 : list) {

            map3.remove("type");
            map3.remove("sort");
            map3.remove("order_id");
        }


        return list;
    }

    @Override
    public List<Map<String, Object>> selectShopArticleSellList(String brandId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        List<ShopDetail> shops = shopDetailService.selectByBrandId(brandId);
        List<Map<String, Object>> list = new ArrayList<>();

        for (ShopDetail s : shops) {
            List<Map<String, Object>> list2 = orderMapper.selectShopArticleSellList(s.getId(), begin, end);
            if (list2 != null && list2.size() > 0) {
                //计算总菜品销售额,//菜品总销售额
                int num = 0;

                BigDecimal temp = BigDecimal.ZERO;
                for (Map<String, Object> map2 : list2) {
                    //计算总销量 不能加上套餐的数量
                    if ((Integer) map2.get("type") != 3) {
                        // num += articleSellDto.getBrandSellNum().doubleValue();
                        num += Integer.parseInt(map2.get("salles").toString());
                    }
                    //计算总销售额
                    temp = add(temp, new BigDecimal(map2.get("sell").toString()));
                }

                for (Map<String, Object> map3 : list2) {
                    //销售额占比
                    // BigDecimal d = new BigDecimal(map2.get("selles").toString()).divide(temp, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    BigDecimal sell = new BigDecimal(map3.get("sell").toString());
                    BigDecimal d = sell.divide(temp, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    //map2.setSalesRatio(d + "%");
                    map3.put("sell_occupies", d + "%");

                    if (num != 0) {
                        //double d1 = articleSellDto.getBrandSellNum().doubleValue();
                        double d1 = Double.parseDouble(map3.get("salles").toString());
                        double d2 = d1 / num * 100;

                        //保留两位小数
                        BigDecimal b = new BigDecimal(d2);
                        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        // articleSellDto.setNumRatio(f1 + "%");
                        map3.put("salles_occupies", f1 + "%");
                    }
                }
                for (Map<String, Object> map4 : list2) {
                    map4.remove("sort");
                    map4.remove("type");
                    map4.remove("order_id");
                }
                list.addAll(list2);

            }
        }

        return list;
    }

    /**
     * 查询订单详情的数据 用于中间数据库
     *
     * @param beginDate
     * @param endDate
     * @param brandId
     * @return
     */
    @Override
    public List<Order> selectListByTimeAndBrandId(String brandId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectListByTimeAndBrandId(brandId, begin, end);
    }

    @Override
    public List<Order> selectAllAlreadyConsumed(String brandId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectMoneyAndNumByDate(begin, end, brandId);
    }

    @Override
    public List<Order> getTableNumberAll(String shopId) {
        return orderMapper.getTableNumberAll(shopId);
    }


    public Map<String, Object> printTotal(BrandSetting setting, ShopDetail shopDetail, Printer printer, String beginDate, String endDate) {
        if (printer == null) {
            return null;
        }
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("shopId", shopDetail.getId());
        selectMap.put("beginDate", beginDate);
        selectMap.put("endDate", endDate);
        String orderAccountId = "";
        BigDecimal originalMoney = new BigDecimal(0);
        BigDecimal orderMoney = new BigDecimal(0);
        BigDecimal discountMoney = new BigDecimal(0);
        BigDecimal orderCount = new BigDecimal(0);
        BigDecimal customerCount = new BigDecimal(0);
        List<Order> orderList = orderMapper.getOrderAccountByTime(selectMap);
        for (Order orderAccount : orderList) {
            orderAccountId = orderAccountId.concat(orderAccount.getId()).concat(",");
            orderMoney = orderMoney.add(orderAccount.getOrderMoney());
            originalMoney = originalMoney.add(orderAccount.getOriginalAmount());
            discountMoney = discountMoney.add(orderAccount.getDiscountMoney());
            if (StringUtils.isBlank(orderAccount.getParentOrderId()) && orderAccount.getProductionStatus() != ProductionStatus.REFUND_ARTICLE) {
                orderCount = orderCount.add(new BigDecimal(1));
                customerCount = customerCount.add(new BigDecimal(orderAccount.getCustomerCount()));
            }
        }
        if (StringUtils.isNotBlank(orderAccountId)) {
            orderAccountId = orderAccountId.substring(0, orderAccountId.length() - 1);
        }
        Map<String, Object> print = new HashMap<>();
        print.put("KITCHEN_NAME", printer.getName());
        print.put("PORT", printer.getPort());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_TASK_ID", print_id);
        print.put("ADD_TIME", new Date());
        Map<String, Object> data = new HashMap<>();
        data.put("RESTAURANT_NAME", shopDetail.getName());
        if (beginDate.equalsIgnoreCase(endDate)) {
            data.put("DATE", beginDate);
        } else {
            data.put("DATE", beginDate + "至" + endDate);
        }
        data.put("ORIGINAL_AMOUNT", originalMoney);
        data.put("TOTAL_AMOUNT", orderMoney);
        data.put("ORDER_AMOUNT", orderCount);
        DecimalFormat df = new DecimalFormat("######0.00");
        double average = orderCount.equals(BigDecimal.ZERO) ? 0 :
                orderMoney.doubleValue() / orderCount.doubleValue();
        data.put("ORDER_AVERAGE", df.format(average));
        data.put("CUSTOMER_AMOUNT", customerCount);
        double customerAverage = customerCount.equals(BigDecimal.ZERO) ? 0 :
                orderMoney.doubleValue() / customerCount.doubleValue();
        data.put("CUSTOMER_AVERAGE", df.format(customerAverage));
        selectMap.put("type", PayMode.WEIXIN_PAY);
        BigDecimal wxPay = orderMapper.getPayment(selectMap);
        wxPay = wxPay == null ? BigDecimal.ZERO : wxPay;
        selectMap.put("type", PayMode.CHARGE_PAY);
        BigDecimal chargePay = orderMapper.getPayment(selectMap);
        chargePay = chargePay == null ? BigDecimal.ZERO : chargePay;
        selectMap.put("type", PayMode.ALI_PAY);
        BigDecimal aliPay = orderMapper.getPayment(selectMap);
        aliPay = aliPay == null ? BigDecimal.ZERO : aliPay;
        selectMap.put("type", PayMode.BANK_CART_PAY);
        BigDecimal bankPay = orderMapper.getPayment(selectMap);
        bankPay = bankPay == null ? BigDecimal.ZERO : bankPay;
        selectMap.put("type", PayMode.GIVE_CHANGE);
        BigDecimal givePay = orderMapper.getPayment(selectMap);
        givePay = givePay == null ? BigDecimal.ZERO : givePay;
        selectMap.put("type", PayMode.CRASH_PAY);
        BigDecimal crashPay = orderMapper.getPayment(selectMap);
        crashPay = crashPay == null ? BigDecimal.ZERO : crashPay.add(givePay);
        selectMap.put("type", PayMode.SHANHUI_PAY);
        BigDecimal shanhuiPay = orderMapper.getPayment(selectMap);
        shanhuiPay = shanhuiPay == null ? BigDecimal.ZERO : shanhuiPay;
        selectMap.put("type", PayMode.INTEGRAL_PAY);
        BigDecimal integralPay = orderMapper.getPayment(selectMap);
        integralPay = integralPay == null ? BigDecimal.ZERO : integralPay;
        List<Map<String, Object>> incomeItems = new ArrayList<>();
        Map<String, Object> wxItem = new HashMap<>();
        wxItem.put("SUBTOTAL", wxPay);
        wxItem.put("PAYMENT_MODE", "微信支付");
        incomeItems.add(wxItem);
        Map<String, Object> chargeItem = new HashMap<>();
        chargeItem.put("SUBTOTAL", chargePay);
        chargeItem.put("PAYMENT_MODE", "充值金额支付");
        incomeItems.add(chargeItem);
        Map<String, Object> aliPayment = new HashMap<>();
        aliPayment.put("SUBTOTAL", aliPay);
        aliPayment.put("PAYMENT_MODE", "支付宝支付");
        incomeItems.add(aliPayment);
        BigDecimal incomeAmount = wxPay.add(chargePay).add(aliPay);
        if ((setting.getOpenUnionPay().equals(Common.YES) && shopDetail.getOpenUnionPay().equals(Common.YES)) || bankPay.compareTo(BigDecimal.ZERO) > 0) {
            Map<String, Object> bankPayment = new HashMap<>();
            bankPayment.put("SUBTOTAL", bankPay);
            bankPayment.put("PAYMENT_MODE", "银联支付");
            incomeAmount = incomeAmount.add(bankPay);
            incomeItems.add(bankPayment);
        }
        if ((setting.getOpenMoneyPay().equals(Common.YES) && shopDetail.getOpenMoneyPay().equals(Common.YES)) || crashPay.compareTo(BigDecimal.ZERO) > 0) {
            Map<String, Object> crashPayment = new HashMap<>();
            crashPayment.put("SUBTOTAL", crashPay);
            crashPayment.put("PAYMENT_MODE", "现金实收");
            incomeAmount = incomeAmount.add(crashPay);
            incomeItems.add(crashPayment);
        }
        if ((setting.getOpenShanhuiPay().equals(Common.YES) && shopDetail.getOpenShanhuiPay().equals(Common.YES)) || shanhuiPay.compareTo(BigDecimal.ZERO) > 0) {
            Map<String, Object> shanhuiPayment = new HashMap<>();
            shanhuiPayment.put("SUBTOTAL", shanhuiPay);
            shanhuiPayment.put("PAYMENT_MODE", "新美大支付");
            incomeAmount = incomeAmount.add(shanhuiPay);
            incomeItems.add(shanhuiPayment);
        }
        if ((setting.getIntegralPay().equals(Common.YES) && shopDetail.getIntegralPay().equals(Common.YES)) || integralPay.compareTo(BigDecimal.ZERO) > 0) {
            Map<String, Object> integralPayMent = new HashMap<>();
            integralPayMent.put("SUBTOTAL", integralPay);
            integralPayMent.put("PAYMENT_MODE", "会员支付");
            incomeAmount = incomeAmount.add(integralPay);
            incomeItems.add(integralPayMent);
        }
        data.put("INCOME_AMOUNT", incomeAmount);
        data.put("INCOME_ITEMS", incomeItems);
        selectMap.put("type", PayMode.ACCOUNT_PAY);
        BigDecimal accountPay = orderMapper.getPayment(selectMap);
        accountPay = accountPay == null ? BigDecimal.ZERO : accountPay;
        selectMap.put("type", PayMode.COUPON_PAY);
        BigDecimal couponPay = orderMapper.getPayment(selectMap);
        couponPay = couponPay == null ? BigDecimal.ZERO : couponPay;
        selectMap.put("type", PayMode.REWARD_PAY);
        BigDecimal rewardPay = orderMapper.getPayment(selectMap);
        rewardPay = rewardPay == null ? BigDecimal.ZERO : rewardPay;
        selectMap.put("type", PayMode.WAIT_MONEY);
        BigDecimal waitMoney = orderMapper.getPayment(selectMap);
        waitMoney = waitMoney == null ? BigDecimal.ZERO : waitMoney;
        selectMap.put("type", PayMode.ARTICLE_BACK_PAY);
        BigDecimal articlePay = orderMapper.getPayment(selectMap);
        articlePay = articlePay == null ? BigDecimal.ZERO : articlePay;
        BigDecimal discountAmount = accountPay.add(couponPay).add(rewardPay).add(waitMoney).add(articlePay);
        List<Map<String, Object>> discountItems = new ArrayList<>();
        Map<String, Object> accountPayItem = new HashMap<>();
        accountPayItem.put("SUBTOTAL", accountPay == null ? 0 : accountPay);
        accountPayItem.put("PAYMENT_MODE", "红包支付");
        discountItems.add(accountPayItem);
        Map<String, Object> couponPayItem = new HashMap<>();
        couponPayItem.put("SUBTOTAL", couponPay == null ? 0 : couponPay);
        couponPayItem.put("PAYMENT_MODE", "优惠券支付");
        discountItems.add(couponPayItem);
        Map<String, Object> rewardPayItem = new HashMap<>();
        rewardPayItem.put("SUBTOTAL", rewardPay == null ? 0 : rewardPay);
        rewardPayItem.put("PAYMENT_MODE", "充值赠送支付");
        discountItems.add(rewardPayItem);
        Map<String, Object> waitMoneyItem = new HashMap<>();
        waitMoneyItem.put("SUBTOTAL", waitMoney == null ? 0 : waitMoney);
        waitMoneyItem.put("PAYMENT_MODE", "等位红包支付");
        discountItems.add(waitMoneyItem);
        Map<String, Object> articleBackPay = new HashMap<>();
        articleBackPay.put("SUBTOTAL", articlePay == null ? 0 : articlePay.abs());
        articleBackPay.put("PAYMENT_MODE", "退菜返还红包");
        discountItems.add(articleBackPay);
        if (originalMoney.compareTo(orderMoney) != 0 && shopDetail.getTemplateType().equals(Common.YES)) {
            Map<String, Object> discountMap = new HashMap<>();
            discountAmount = discountAmount.add(originalMoney.subtract(orderMoney));
            discountMap.put("SUBTOTAL", originalMoney.subtract(orderMoney));
            discountMap.put("PAYMENT_MODE", "粉丝价折扣");
            discountItems.add(discountMap);
        }
        data.put("DISCOUNT_AMOUNT", discountAmount == null ? 0 : discountAmount);
        data.put("DISCOUNT_ITEMS", discountItems);
        List<Map<String, Object>> chargeOrders = chargeOrderService.selectByShopToDay(selectMap);
        data.put("STORED_VALUE_COUNT", chargeOrders.size());
        BigDecimal chargeAmount = new BigDecimal(0);
        List<Map<String, Object>> storedValueItems = new ArrayList<Map<String, Object>>();
        if (chargeOrders.size() > 0) {
            for (Map<String, Object> chargeOrder : chargeOrders) {
                Map<String, Object> chargeMap = new HashMap<String, Object>();
                chargeMap.put("SUBTOTAL", chargeOrder.get("chargeMoney"));
                chargeMap.put("TEL", chargeOrder.get("telephone"));
                chargeAmount = chargeAmount.add(new BigDecimal(chargeOrder.get("chargeMoney").toString()));
                storedValueItems.add(chargeMap);
            }
        }
        data.put("STORED_VALUE_AMOUNT", chargeAmount);
        data.put("STORED_VALUE_ITEMS", storedValueItems);
        BigDecimal saledProductAmount = new BigDecimal(0);
        BigDecimal canceledProductCount = new BigDecimal(0);
        BigDecimal canceledProductAmount = new BigDecimal(0);
        BigDecimal canceledOrderCount = new BigDecimal(0);
        Map<String, Object> canceledOrderMap = new HashMap<>();
        List<Map<String, Object>> saledProducts = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> canceledProducts = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> canceledOrders = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotBlank(orderAccountId)) {
            BrandSetting brandSetting = brandSettingService.selectByBrandId(shopDetail.getBrandId());
            Brand brand = brandService.selectBrandBySetting(brandSetting.getId());
            String[] orderIds = orderAccountId.split(",");
            Map<String, Object> selectOrderMap = new HashMap<String, Object>();
            selectOrderMap.put("orderIds", orderIds);
            List<Order> orders = orderMapper.selectOrderByOrderIds(selectOrderMap);
            BigDecimal nowService = new BigDecimal(0);
            BigDecimal oldService = new BigDecimal(0);
            BigDecimal nowMeal = new BigDecimal(0);
            BigDecimal oldMeal = new BigDecimal(0);
            Map<String, Object> serviceMap = new HashMap<>();
            if ("27f56b31669f4d43805226709874b530".equals(brand.getId())) {
                serviceMap.put("serviceName", "就餐人数");
            } else if (StringUtils.isBlank(shopDetail.getServiceName())) {
                serviceMap.put("serviceName", "服务费");
            } else {
                serviceMap.put("serviceName", shopDetail.getServiceName());
            }
            Map<String, Object> mealMap = new HashMap<>();
            mealMap.put("mealName", shopDetail.getMealFeeName());
            for (Order orderAll : orders) {
                BigDecimal nowCustomerCount = new BigDecimal(orderAll.getCustomerCount() == null ? 0 : orderAll.getCustomerCount());
                BigDecimal oldCustomerCount = new BigDecimal(orderAll.getBaseCustomerCount() == null ? 0 : orderAll.getBaseCustomerCount());
                if (orderAll.getDistributionModeId().equals(DistributionType.RESTAURANT_MODE_ID)) {
                    if (StringUtils.isNotBlank(orderAll.getParentOrderId())) {
                        serviceMap.put(orderAll.getId(), 0);
                    } else {
                        nowService = nowService.add(nowCustomerCount);
                        oldService = oldService.add(oldCustomerCount);
                        serviceMap.put(orderAll.getId(), oldCustomerCount.subtract(nowCustomerCount));
                    }
                } else if (orderAll.getDistributionModeId().equals(DistributionType.TAKE_IT_SELF) || orderAll.getDistributionModeId().equals(DistributionType.DELIVERY_MODE_ID)) {
                    BigDecimal nowMealAllNumber = new BigDecimal(orderAll.getMealAllNumber() == null ? 0 : orderAll.getMealAllNumber());
                    BigDecimal oldMealAllNumber = new BigDecimal(orderAll.getBaseMealAllCount() == null ? 0 : orderAll.getBaseMealAllCount());
                    nowMeal = nowMeal.add(nowMealAllNumber);
                    oldMeal = oldMeal.add(oldMealAllNumber);
                    mealMap.put(orderAll.getId(), oldMealAllNumber.subtract(nowMealAllNumber));
                }
                selectOrderMap.clear();
                selectOrderMap.put("orderId", orderAll.getId());
                selectOrderMap.put("count", "refund_count != 0");
                List<OrderItem> canceledOrderItems = orderItemService.selectOrderItemByOrderId(selectOrderMap);
                BigDecimal refundPrice = new BigDecimal(0);
                if (canceledOrderItems.size() != 0) {
                    String orderId = "";
                    for (OrderItem orderItem : canceledOrderItems) {
                        if (!orderId.equals(orderItem.getOrderId())) {
                            refundPrice = BigDecimal.ZERO;
                        }
                        refundPrice = refundPrice.add(orderItem.getUnitPrice().multiply(new BigDecimal(orderItem.getRefundCount())));
                        if (orderAll.getDistributionModeId().equals(DistributionType.RESTAURANT_MODE_ID)) {
                            if (!orderId.equals(orderItem.getOrderId())) {
                                refundPrice = refundPrice.add(new BigDecimal(serviceMap.get(orderItem.getOrderId()).toString()).multiply(shopDetail.getServicePrice() == null ? BigDecimal.ZERO : shopDetail.getServicePrice()));
                            }
                        } else if (orderAll.getDistributionModeId().equals(DistributionType.TAKE_IT_SELF) || orderAll.getDistributionModeId().equals(DistributionType.DELIVERY_MODE_ID)) {
                            if (!orderId.equals(orderItem.getOrderId())) {
                                refundPrice = refundPrice.add(new BigDecimal(mealMap.get(orderItem.getOrderId()).toString()).multiply(shopDetail.getMealFeePrice() == null ? BigDecimal.ZERO : shopDetail.getMealFeePrice()));
                            }
                        }
                        canceledOrderMap.put(orderItem.getOrderId(), refundPrice);
                        orderId = orderItem.getOrderId();
                    }
                } else if (!oldCustomerCount.equals(nowCustomerCount)) {
                    refundPrice = refundPrice.add(oldCustomerCount.subtract(nowCustomerCount).multiply(shopDetail.getServicePrice()));
                    canceledOrderMap.put(orderAll.getId(), refundPrice);
                }
            }
            selectOrderMap.clear();
            selectOrderMap.put("orderIds", orderIds);
            selectOrderMap.put("count", "count != 0");
            List<OrderItem> saledOrderItems = orderItemService.selectOrderItemByOrderIds(selectOrderMap);
            if (shopDetail.getTemplateType().equals(Common.YES) || shopDetail.getTemplateType().equals(2)) { //升级版或简约版
                List<String> articleIds = new ArrayList<>();
                for (OrderItem item : saledOrderItems) {
                    if (item.getArticleId().indexOf("@") != -1) {
                        articleIds.add(item.getArticleId().substring(0, item.getArticleId().indexOf("@")));
                    } else {
                        articleIds.add(item.getArticleId());
                    }
                }
                //排序菜品销售   按照菜品分类进行排序
                List<ArticleFamily> articleFamilies = articleFamilyMapper.selectArticleSort(articleIds);
                for (ArticleFamily articleFamily : articleFamilies) {
                    List<Map<String, Object>> familyArticleMaps = new ArrayList<>();
                    BigDecimal familyCount = BigDecimal.ZERO;
                    for (Article article : articleFamily.getArticleList()) {
                        BigDecimal unitNewCount = BigDecimal.ZERO;
                        Map<String, Map<String, Integer>> unitMaps = new HashMap<>();
                        for (OrderItem orderItem : saledOrderItems) {
                            Map<String, Object> itemMap = new HashMap<>();
                            if (orderItem.getType().equals(OrderItemType.SETMEALS) && orderItem.getArticleId().equalsIgnoreCase(article.getId())) {
                                familyCount = familyCount.add(new BigDecimal(orderItem.getCount()));
                                saledProductAmount = saledProductAmount.add(new BigDecimal(orderItem.getCount()));
                                if (shopDetail.getTemplateType().equals(Common.YES)) {
                                    itemMap.put("PRODUCT_NAME", orderItem.getArticleName());
                                    itemMap.put("SUBTOTAL", orderItem.getCount());
                                    familyArticleMaps.add(itemMap);
                                    selectMap.clear();
                                    selectMap.put("articleId", orderItem.getArticleId());
                                    selectMap.put("beginDate", beginDate);
                                    selectMap.put("endDate", endDate);
                                    List<ArticleSellDto> articleSellDtos = mealAttrMapperReport.queryArticleMealAttr(selectMap);
                                    for (ArticleSellDto articleSellDto : articleSellDtos) {
                                        if (orderItem.getArticleId().equalsIgnoreCase(articleSellDto.getArticleId()) && articleSellDto.getBrandSellNum() != 0) {
                                            itemMap = new HashMap<>();
                                            itemMap.put("PRODUCT_NAME", "|_" + articleSellDto.getArticleName());
                                            itemMap.put("SUBTOTAL", articleSellDto.getBrandSellNum());
                                            familyArticleMaps.add(itemMap);
                                        }
                                    }
                                }
                            } else if (orderItem.getType().equals(OrderItemType.UNITPRICE) && orderItem.getArticleId().substring(0, orderItem.getArticleId().indexOf("@")).equalsIgnoreCase(article.getId())) {
                                Map<String, Integer> map = new HashMap<>();
                                if (unitMaps.containsKey(orderItem.getArticleId().substring(0, orderItem.getArticleId().indexOf("@")))) {
                                    map = unitMaps.get(orderItem.getArticleId().substring(0, orderItem.getArticleId().indexOf("@")));
                                }
                                String formName = orderItem.getArticleName().substring(orderItem.getArticleName().indexOf(article.getName().substring(article.getName().length() - 1)) + 1);
                                formName = formName.substring(1, formName.length() - 1);
                                map.put(formName, orderItem.getCount());
                                unitMaps.put(orderItem.getArticleId().substring(0, orderItem.getArticleId().indexOf("@")), map);
                            } else if (orderItem.getType().equals(OrderItemType.UNIT_NEW) && orderItem.getArticleId().equalsIgnoreCase(article.getId())) {
                                unitNewCount = unitNewCount.add(new BigDecimal(orderItem.getCount()));
                                Map<String, Integer> map = new HashMap<>();
                                if (unitMaps.containsKey(orderItem.getArticleId())) {
                                    map = unitMaps.get(orderItem.getArticleId());
                                }
                                String formName = orderItem.getArticleName().substring(orderItem.getArticleName().indexOf(article.getName().substring(article.getName().length() - 1)) + 1);
                                String[] formNames = formName.split("\\)");
                                for (String name : formNames) {
                                    if (name.length() > 1) {
                                        formName = name.substring(1);
                                    }
                                    if (map.containsKey(formName)) {
                                        Integer count = map.get(formName);
                                        count += orderItem.getCount();
                                        map.put(formName, count);
                                    } else {
                                        map.put(formName, orderItem.getCount());
                                    }
                                }
                                unitMaps.put(orderItem.getArticleId(), map);
                            } else if (orderItem.getArticleId().equalsIgnoreCase(article.getId())) {
                                familyCount = familyCount.add(new BigDecimal(orderItem.getCount()));
                                saledProductAmount = saledProductAmount.add(new BigDecimal(orderItem.getCount() - orderItem.getPackageNumber()));
                                if (shopDetail.getTemplateType().equals(Common.YES)) {
                                    itemMap.put("PRODUCT_NAME", orderItem.getArticleName());
                                    itemMap.put("SUBTOTAL", orderItem.getCount() + "(" + (orderItem.getCount() - orderItem.getPackageNumber()) + "+" + orderItem.getPackageNumber() + ")");
                                    familyArticleMaps.add(itemMap);
                                }
                            }
                        }
                        if (unitMaps.containsKey(article.getId())) {
                            Map<String, Integer> unitPriceMap = unitMaps.get(article.getId());
                            BigDecimal articleCount = unitNewCount.compareTo(BigDecimal.ZERO) > 0 ? unitNewCount : BigDecimal.ZERO;
                            List<Map<String, Object>> maps = new ArrayList<>();
                            for (Map.Entry<String, Integer> unitMap : unitPriceMap.entrySet()) {
                                if (shopDetail.getTemplateType().equals(Common.YES)) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("PRODUCT_NAME", "|_" + unitMap.getKey());
                                    map.put("SUBTOTAL", unitMap.getValue());
                                    maps.add(map);
                                }
                                if (unitNewCount.compareTo(BigDecimal.ZERO) == 0) {
                                    articleCount = articleCount.add(new BigDecimal(unitMap.getValue()));
                                }
                            }
                            familyCount = familyCount.add(articleCount);
                            saledProductAmount = saledProductAmount.add(articleCount);
                            if (shopDetail.getTemplateType().equals(Common.YES)) {
                                Map<String, Object> itemMap = new HashMap<>();
                                itemMap.put("PRODUCT_NAME", article.getName());
                                itemMap.put("SUBTOTAL", articleCount);
                                familyArticleMaps.add(itemMap);
                                familyArticleMaps.addAll(maps);
                            }
                        }
                    }
                    Map<String, Object> itemMap = new HashMap<>();
                    if (shopDetail.getTemplateType().equals(Common.YES)) {
                        BigDecimal strLength = new BigDecimal(articleFamily.getName().length()).multiply(new BigDecimal(2));
                        Integer length = new BigDecimal(40).subtract(strLength).divide(new BigDecimal(2)).intValue();
                        String string = "-";
                        for (int i = 1; i < length; i++) {
                            string = string.concat("-");
                        }
                        itemMap.put("PRODUCT_NAME", string.concat(articleFamily.getName()).concat(string));
                    } else {
                        itemMap.put("PRODUCT_NAME", articleFamily.getName());
                        itemMap.put("SUBTOTAL", familyCount);
                    }
                    saledProducts.add(itemMap);
                    if (shopDetail.getTemplateType().equals(Common.YES)) {
                        saledProducts.addAll(familyArticleMaps);
                    }
                }
            } else if (shopDetail.getTemplateType().equals(Common.NO)) { //经典版
                for (OrderItem orderItem : saledOrderItems) {
                    saledProductAmount = saledProductAmount.add(new BigDecimal(orderItem.getType().equals(OrderItemType.SETMEALS) ? 0 : orderItem.getCount()));
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("PRODUCT_NAME", orderItem.getArticleName());
                    itemMap.put("SUBTOTAL", orderItem.getCount());
                    saledProducts.add(itemMap);
                }
            }
            selectOrderMap.clear();
            selectOrderMap.put("orderIds", orderIds);
            selectOrderMap.put("count", "refund_count != 0");
            List<OrderItem> canceledOrderItems = orderItemService.selectOrderItemByOrderIds(selectOrderMap);
            for (OrderItem orderItem : canceledOrderItems) {
                canceledProductCount = canceledProductCount.add(new BigDecimal(orderItem.getRefundCount()));
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("PRODUCT_NAME", orderItem.getArticleName());
                itemMap.put("SUBTOTAL", orderItem.getRefundCount());
                canceledProducts.add(itemMap);
            }
            if (!nowService.equals(BigDecimal.ZERO) || !nowMeal.equals(BigDecimal.ZERO)) {
                Map<String, Object> itemMap = new HashMap<>();
                if (shopDetail.getTemplateType().equals(Common.YES)) {
                    String other = "其他销量";
                    BigDecimal strLength = new BigDecimal(other.length()).multiply(new BigDecimal(2));
                    Integer length = new BigDecimal(40).subtract(strLength).divide(new BigDecimal(2)).intValue();
                    String string = "-";
                    for (int i = 1; i < length; i++) {
                        string = string.concat("-");
                    }
                    itemMap.put("PRODUCT_NAME", string.concat(other).concat(string));
                    saledProducts.add(itemMap);
                }
//                服务费销量、销售额不计入菜品销量和销售额中
                if (!nowService.equals(BigDecimal.ZERO)) {
                    itemMap = new HashMap<>();
                    itemMap.put("PRODUCT_NAME", serviceMap.get("serviceName"));
                    itemMap.put("SUBTOTAL", nowService);
                    saledProducts.add(itemMap);
//                    saledProductAmount = saledProductAmount.add(nowService);
                }
                if (!nowMeal.equals(BigDecimal.ZERO)) {
                    itemMap = new HashMap<>();
                    itemMap.put("PRODUCT_NAME", mealMap.get("mealName"));
                    itemMap.put("SUBTOTAL", nowMeal);
                    saledProducts.add(itemMap);
                    //餐盒费销量不计入总销量
                    saledProductAmount = saledProductAmount.add(nowMeal);
                }
            }
            if (!oldService.subtract(nowService).equals(BigDecimal.ZERO)) {
                Map<String, Object> itemMap = new HashMap<String, Object>();
                itemMap.put("PRODUCT_NAME", serviceMap.get("serviceName"));
                itemMap.put("SUBTOTAL", oldService.subtract(nowService));
                canceledProducts.add(itemMap);
                canceledProductCount = canceledProductCount.add(oldService.subtract(nowService));
            }
            if (!oldMeal.subtract(nowMeal).equals(BigDecimal.ZERO)) {
                Map<String, Object> itemMap = new HashMap<String, Object>();
                itemMap.put("PRODUCT_NAME", mealMap.get("mealName"));
                itemMap.put("SUBTOTAL", oldMeal.subtract(nowMeal));
                canceledProducts.add(itemMap);
                canceledProductCount = canceledProductCount.add(oldMeal.subtract(nowMeal));
            }
            canceledOrderCount = canceledOrderCount.add(new BigDecimal(canceledOrderMap.size()));
            for (Map.Entry<String, Object> map : canceledOrderMap.entrySet()) {
                Map<String, Object> canceledMap = new HashMap<>();
                Order canceledOrder = orderMapper.selectOrderDetails(map.getKey());
                canceledMap.put("ORDER_NUMBER", map.getKey());
                if (canceledOrder.getCustomer() != null) {
                    canceledMap.put("TEL", StringUtils.isBlank(canceledOrder.getCustomer().getTelephone()) ? "--" : canceledOrder.getCustomer().getTelephone());
                } else {
                    canceledMap.put("TEL", "--");
                }
                canceledMap.put("SUBTOTAL", map.getValue());
                canceledOrders.add(canceledMap);
                canceledProductAmount = canceledProductAmount.add(new BigDecimal(map.getValue().toString()));
            }
        }
        data.put("SALED_PRODUCT_AMOUNT", saledProductAmount);
        data.put("SALED_PRODUCTS", saledProducts);
        data.put("CANCELED_PRODUCT_COUNT", canceledProductCount);
        data.put("CANCELED_PRODUCTS", canceledProducts);
        data.put("CANCELED_ORDER_AMOUNT", canceledProductAmount);
        data.put("CANCELED_ORDER_COUNT", canceledOrderCount);
        data.put("CANCELED_ORDERS", canceledOrders);
        print.put("DATA", data);
        print.put("STATUS", 0);
        print.put("TICKET_TYPE", TicketType.DAILYREPORT);
        return print;
    }

    @Override
    public Result checkArticleCount(String orderId) {
        Order order = getOrderInfo(orderId);
        if (order == null || CollectionUtils.isEmpty(order.getOrderItems())) {
            return new Result("订单数据异常,请速与服务员联系", false);
        }

        Boolean result = true;
        String msg = "";


        //订单菜品不可为空
        for (OrderItem orderItem : order.getOrderItems()) {
            //有任何一个菜品售罄则不能出单
            Result check = checkStock(orderItem, order.getOrderItems().size());
            if (!check.isSuccess()) {
                result = false;
                msg = check.getMessage();
                break;
            }
        }

        return new Result(msg, result);

    }

    private Result checkStock(OrderItem orderItem, int count) {
        Boolean result = false;
        int current = 0;
        String msg = "";
        int min = 0;
        int endMin = 10000;
        Integer ck = (Integer) redisService.get(orderItem.getArticleId() + Common.KUCUN);
        switch (orderItem.getType()) {
            case OrderItemType.ARTICLE:
                //如果是单品无规格，直接判断菜品是否有库存

                if (ck != null) {
                    current = ck;
                } else {
                    current = orderMapper.selectArticleCount(orderItem.getArticleId());
                }
                result = current >= count;
                msg = current == 0 ? orderItem.getArticleName() + "已售罄,请取消订单后重新下单" :
                        current >= count ? "库存足够" : orderItem.getArticleName() + "中单品库存不足,最大购买" + current + "个,请重新选购餐品";
                break;
            case OrderItemType.UNITPRICE:
                //如果是有规则菜品，则判断该规则是否有库存
                if (ck != null) {
                    current = ck;
                } else {
                    current = orderMapper.selectArticlePriceCount(orderItem.getArticleId());
                }

                result = current >= count;
                msg = current == 0 ? orderItem.getArticleName() + "已售罄,请取消订单后重新下单" :
                        current >= count ? "库存足够" : orderItem.getArticleName() + "中单品库存不足,最大购买" + current + "个,请重新选购餐品";
                break;
            case OrderItemType.SETMEALS:
                //如果是套餐,不做判断，只判断套餐下的子品是否有库存
                if (ck != null) {
                    current = ck;
                } else {
                    current = orderMapper.selectArticleCount(orderItem.getArticleId());
                }
                Map<String, Integer> order_items_map = new HashMap<String, Integer>();//用于保存套餐内的子菜品（防止套餐内出现同样餐品，检查库存出现异常）
                for (OrderItem oi : orderItem.getChildren()) {
                    //查询当前菜品，剩余多少份
                    Integer cck = (Integer) redisService.get(oi.getArticleId() + Common.KUCUN);
                    if (cck != null) {
                        min = cck;
                    } else {
                        min = orderMapper.selectArticleCount(oi.getArticleId());
                    }
                    if (order_items_map.containsKey(oi.getArticleId())) {
                        order_items_map.put(oi.getArticleId(), order_items_map.get(oi.getArticleId()) + oi.getCount());
                        min -= oi.getCount();
                    } else {
                        order_items_map.put(oi.getArticleId(), oi.getCount());
                    }
                    if (min < endMin) {
                        endMin = min;
                    }
                }
                //result = endMin>= count;
                result = endMin >= count;
                msg = endMin == 0 ? orderItem.getArticleName() + "套餐单品已售罄,请取消订单后重新下单" :
                        endMin >= count ? "库存足够" : orderItem.getArticleName() + "中单品库存不足,最大购买" + endMin + "个,请重新选购餐品";
                // 中单品库存不足,最大购买"+endMin+",个,请取消订单后重新下单
                break;
            case OrderItemType.MEALS_CHILDREN:
                //如果是套餐下的子品 当成单品来判断
                if (ck != null) {
                    current = ck;
                } else {
                    current = orderMapper.selectArticleCount(orderItem.getArticleId());
                }
//                current = orderMapper.selectArticleCount(orderItem.getArticleId());
                result = current >= orderItem.getCount();
                msg = current == 0 ? orderItem.getArticleName() + "已售罄,请取消订单后重新下单" :
                        current >= orderItem.getCount() ? "库存足够" : orderItem.getArticleName() + "库存不足,请重新选购餐品";
                break;
            case OrderItemType.UNIT_NEW:
                //如果是单品无规格，直接判断菜品是否有库存
                if (ck != null) {
                    current = ck;
                } else {
                    current = orderMapper.selectArticleCount(orderItem.getArticleId());
                }
//                current = orderMapper.selectArticleCount(orderItem.getArticleId());
                result = current >= count;
                msg = current == 0 ? orderItem.getArticleName() + "已售罄,请取消订单后重新下单" :
                        current >= count ? "库存足够" : orderItem.getArticleName() + "中单品库存不足,最大购买" + current + "个,请重新选购餐品";
                break;
            case OrderItemType.RECOMMEND:
                //如果是单品无规格，直接判断菜品是否有库存
                if (ck != null) {
                    current = ck;
                } else {
                    current = orderMapper.selectArticleCount(orderItem.getArticleId());
                }
//                current = orderMapper.selectArticleCount(orderItem.getArticleId());
                result = current >= count;
                msg = current == 0 ? orderItem.getArticleName() + "已售罄,请取消订单后重新下单" :
                        current >= count ? "库存足够" : orderItem.getArticleName() + "中单品库存不足,最大购买" + current + "个,请重新选购餐品";
                break;
            default:
                log.debug("未知菜品分类");
                break;
        }
        return new Result(msg, result);
    }


    @Override
    public Boolean updateStock(Order order) throws AppException {
        //首先验证订单信息
        if (order == null || CollectionUtils.isEmpty(order.getOrderItems())) {
            throw new AppException(AppException.ORDER_IS_NULL);
        }
        //遍历订单商品
        for (OrderItem orderItem : order.getOrderItems()) {
            String articleId = orderItem.getArticleId();
            Article article = articleService.selectById(articleId);
//            Integer articleCount = (Integer) redisService.get(articleId + Common.KUCUN);
            Integer articleCount = (Integer) redisService.get(articleId + Common.KUCUN);
            switch (orderItem.getType()) {
                case OrderItemType.UNITPRICE:
                    //如果是有规格的单品信息，那么更新该规格的单品库存以及该单品的库存
                    ArticlePrice articlePrice = articlePriceMapper.selectByPrimaryKey(orderItem.getArticleId());
                    List<ArticlePrice> articlePrices = articlePriceMapper.selectByArticleId(articlePrice.getArticleId());

                    if (articleCount == null) {
                        if (articlePrice.getCurrentWorkingStock() > orderItem.getCount()) {
//                            redisService.set(articleId + Common.KUCUN, articlePrice.getCurrentWorkingStock() - 1);
                            redisService.set(articleId + Common.KUCUN, articlePrice.getCurrentWorkingStock() - orderItem.getCount());
                        } else {
//                            redisService.set(articleId + Common.KUCUN, 0);
                            redisService.set(articleId + Common.KUCUN, 0);
                            articlePriceService.setArticlePriceEmpty(articlePrice.getArticleId());
                        }
                    } else {
                        if (articleCount > orderItem.getCount()) {
//                            redisService.set(articleId + Common.KUCUN, articleCount - 1);
                            redisService.set(articleId + Common.KUCUN, articleCount - orderItem.getCount());
                        } else {
//                            redisService.set(articleId + Common.KUCUN, 0);
                            redisService.set(articleId + Common.KUCUN, 0);
                            articlePriceService.setArticlePriceEmpty(articlePrice.getArticleId());
                        }
                    }
                    int sum = 0;
                    for (ArticlePrice price : articlePrices) {
//                        Integer count = (Integer) redisService.get(price.getId() + Common.KUCUN);
                        Integer count = (Integer) redisService.get(price.getId() + Common.KUCUN);
                        if (count != null) {
                            sum += count;
                        } else {
                            sum += price.getCurrentWorkingStock();
                        }
                    }
//                    redisService.set(articlePrice.getArticleId() + Common.KUCUN, sum);
                    redisService.set(articlePrice.getArticleId() + Common.KUCUN, sum);
                    if (sum == 0) {
                        articleService.setEmpty(articlePrice.getArticleId());
                    }
                    break;
                case OrderItemType.SETMEALS:
                    //如果是套餐，那么更新套餐库存
                case OrderItemType.MEALS_CHILDREN:
                    //如果是套餐子项，那么更新子项库存
                case OrderItemType.UNIT_NEW:
                    //如果是没有规格的单品信息,那么更新该单品的库存
                case OrderItemType.WEIGHT_PACKAGE_ARTICLE:
                    //如果是没有规格的单品信息,那么更新该单品的库存
                case OrderItemType.ARTICLE:
                    //如果是没有规格的单品信息,那么更新该单品的库存
                case OrderItemType.RECOMMEND:
                    //如果是没有规格的单品信息,那么更新该单品的库存
                    if (articleCount == null) {
                        if (article.getCurrentWorkingStock() > orderItem.getCount()) {
//                            redisService.set(articleId + Common.KUCUN, article.getCurrentWorkingStock() - 1);
                            redisService.set(articleId + Common.KUCUN, article.getCurrentWorkingStock() - orderItem.getCount());
                        } else {
//                            redisService.set(articleId + Common.KUCUN, 0);
                            redisService.set(articleId + Common.KUCUN, 0);
                            articleService.setEmpty(orderItem.getArticleId());
                        }
                    } else {
                        if (articleCount > orderItem.getCount()) {
//                            redisService.set(articleId + Common.KUCUN, articleCount - 1);
                            redisService.set(articleId + Common.KUCUN, articleCount - orderItem.getCount());
                        } else {
//                            redisService.set(articleId + Common.KUCUN, 0);
                            redisService.set(articleId + Common.KUCUN, 0);
                            articleService.setEmpty(orderItem.getArticleId());
                        }

                    }
                    break;
                default:
                    throw new AppException(AppException.UNSUPPORT_ITEM_TYPE, "不支持的餐品类型:" + orderItem.getType());
            }


        }
        //同时更新套餐库存(套餐库存为 最小库存的单品)
//        List<Article> taocan = orderMapper.getStockBySuit(order.getShopDetailId());
//        for(Article article : taocan){
//            redisService.set(article.getId()+Common.KUCUN,article.getCount());
//            if(article.getCount() == 0){
//                orderMapper.setEmpty(article.getId());
//            }
//        }
//        orderMapper.setStockBySuit(order.getShopDetailId());
        return true;
    }


    @Override
    public Boolean addStock(Order order) {
        //首先验证订单信息
        if (order == null || CollectionUtils.isEmpty(order.getOrderItems())) {
            //throw new AppException(AppException.ORDER_IS_NULL);
            return false;
        }
        //遍历订单商品
        for (OrderItem orderItem : order.getOrderItems()) {
            String articleId = orderItem.getArticleId();
            Article article = articleService.selectById(articleId);
//            Integer articleCount = (Integer) redisService.get(articleId + Common.KUCUN);
            Integer articleCount = (Integer) redisService.get(articleId + Common.KUCUN);
            switch (orderItem.getType()) {

                case OrderItemType.UNITPRICE:
                    //如果是有规格的单品信息，那么更新该规格的单品库存以及该单品的库存
                    ArticlePrice articlePrice = articlePriceMapper.selectByPrimaryKey(orderItem.getArticleId());
                    if (articleCount != null) {
//                        redisService.set(articleId + Common.KUCUN, articleCount + 1);
                        redisService.set(articleId + Common.KUCUN, articleCount + orderItem.getCount());
                        if (articleCount == 0) {
                            articlePriceService.setArticlePriceEmptyFail(articlePrice.getArticleId());
                        }
                    } else {
//                        redisService.set(articleId + Common.KUCUN, articlePrice.getCurrentWorkingStock() + 1);
                        redisService.set(articleId + Common.KUCUN, articlePrice.getCurrentWorkingStock() + orderItem.getCount());
                        if (articlePrice.getCurrentWorkingStock() == 0) {
                            articlePriceService.setArticlePriceEmptyFail(articlePrice.getArticleId());
                        }
                    }
                    Integer baseArticle = (Integer) redisService.get(articlePrice.getArticleId() + Common.KUCUN);
                    if (baseArticle != null) {
//                        redisService.set(articlePrice.getArticleId() + Common.KUCUN, baseArticle + 1);
                        redisService.set(articlePrice.getArticleId() + Common.KUCUN, baseArticle + orderItem.getCount());
                        if (baseArticle == 0) {
                            articleService.setEmptyFail(articlePrice.getArticleId());
                        }
                    } else {
                        Article base = articleService.selectById(articlePrice.getArticleId());
//                        redisService.set(articlePrice.getArticleId() + Common.KUCUN, base.getCurrentWorkingStock() + 1);
                        redisService.set(articlePrice.getArticleId() + Common.KUCUN, base.getCurrentWorkingStock() + orderItem.getCount());
                        if (base.getCurrentWorkingStock() == 0) {
                            articleService.setEmptyFail(articlePrice.getArticleId());
                        }
                    }
                    break;
                case OrderItemType.SETMEALS:
                    //如果是套餐，那么更新套餐库存
                case OrderItemType.MEALS_CHILDREN:
                    //如果是套餐子项，那么更新子项库存
                case OrderItemType.ARTICLE:
                    //如果是没有规格的单品信息,那么更新该单品的库存
                case OrderItemType.UNIT_NEW:
                    //如果是没有规格的单品信息,那么更新该单品的库存
                    if (articleCount != null) {
//                        redisService.set(articleId + Common.KUCUN, articleCount + 1);
                        redisService.set(articleId + Common.KUCUN, articleCount + orderItem.getCount());
                        if (articleCount == 0) {
                            articleService.setEmptyFail(orderItem.getArticleId());
                        }
                    } else {
//                        redisService.set(articleId + Common.KUCUN, article.getCurrentWorkingStock() + 1);
                        redisService.set(articleId + Common.KUCUN, article.getCurrentWorkingStock() + orderItem.getCount());
                        if (article.getCurrentWorkingStock() == 0) {
                            articleService.setEmptyFail(orderItem.getArticleId());
                        }
                    }
                    break;
                default:
                    //  throw new AppException(AppException.UNSUPPORT_ITEM_TYPE, "不支持的餐品类型:" + orderItem.getType());
                    return false;
            }

        }
        //同时更新套餐库存(套餐库存为 最小库存的单品)
//        List<Article> taocan = orderMapper.getStockBySuit(order.getShopDetailId());
//        for(Article article : taocan){
//            Integer suit = (Integer) redisService.get(article.getId()+Common.KUCUN);
//            if(suit != null){
//                if(suit == 0 && article.getCount() > 0){
//                    orderMapper.setEmptyFail(article.getId());
//                }
//                redisService.set(article.getId()+Common.KUCUN,article.getCount());
//            }else{
//                if(article.getIsEmpty() && article.getCount() > 0){
//                    orderMapper.setEmptyFail(article.getId());
//                }
//                redisService.set(article.getId()+Common.KUCUN,article.getCount());
//            }
//        }
        return true;
    }

    @Override
    public List<Order> selectByOrderSatesAndProductionStates(String shopId, String[] orderStates,
                                                             String[] productionStates) {
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);
        if (shopDetail.getShopMode() == ShopMode.HOUFU_ORDER) {
            return orderMapper.listHoufuUnFinishedOrder(shopId);

        } else if (shopDetail.getShopMode() == ShopMode.BOSS_ORDER) {
            return orderMapper.selectOrderByBoss(shopId);
        } else {
            return orderMapper.selectByOrderSatesAndProductionStates(shopId, orderStates, productionStates);
        }


    }

    @Override
    public List<Order> selectByOrderSatesAndProductionStatesTakeout(String shopId, String[] orderStates,
                                                                    String[] productionStates) {
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);
        if (shopDetail.getShopMode() == ShopMode.HOUFU_ORDER) {
            return orderMapper.listHoufuUnFinishedOrder(shopId);

        } else if (shopDetail.getShopMode() == ShopMode.BOSS_ORDER) {
            List<Order> order = orderMapper.selectOrderByBossTakeout(shopId);
            //return orderMapper.selectOrderByBossTakeout(shopId);
            return order;
        } else {
            List<Order> order = orderMapper.selectByOrderSatesAndProductionStatesTakeout(shopId, orderStates, productionStates);
            //return orderMapper.selectByOrderSatesAndProductionStatesTakeout(shopId, orderStates, productionStates);
            return order;
        }


    }


    @Override
    public Order payOrderModeFive(String orderId) {
        BigDecimal totalMoney = BigDecimal.ZERO;//计算订单原价，不使用任何优惠方式
        Order order = orderMapper.selectByPrimaryKey(orderId);
        totalMoney = totalMoney.add(order.getOriginalAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
//        totalMoney = totalMoney.add(order.getServicePrice() == null ? new BigDecimal(0): order.getServicePrice()).setScale(2, BigDecimal.ROUND_HALF_UP);

        if (order.getOrderState() < OrderState.PAYMENT) {
            order.setOrderState(OrderState.PAYMENT);
            order.setAllowCancel(false);
            order.setAllowContinueOrder(false);
            update(order);
        }

        List<Order> orders = orderMapper.selectByParentId(order.getId(), order.getPayType());
        for (Order child : orders) {
            if (child.getOrderState() < OrderState.PAYMENT) {
                child.setOrderState(OrderState.PAYMENT);
                child.setAllowCancel(false);
                child.setAllowContinueOrder(false);
                update(child);
                //插入 支付项
//                insertOrderPaymentItem(child, child.getOriginalAmount());
                //计算 订单总额
                totalMoney = totalMoney.add(child.getOriginalAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }

        //设置  【主订单 】 支付方式     子订单为计算
        insertOrderPaymentItem(order, totalMoney);

        return order;
    }

    @Override
    public Order payOrderWXModeFive(String orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);

        if (order.getOrderState() < OrderState.PAYMENT) {
            order.setOrderState(OrderState.PAYMENT);
            order.setAllowCancel(false);
            order.setIsPay(OrderPayState.PAYED);
            order.setAllowContinueOrder(false);
            update(order);
        }

        List<Order> orders = orderMapper.selectByParentId(order.getId(), order.getPayType());
        for (Order child : orders) {
            if (child.getOrderState() < OrderState.PAYMENT) {
                child.setOrderState(OrderState.PAYMENT);
                child.setIsPay(OrderPayState.PAYED);
                child.setAllowCancel(false);
                child.setAllowContinueOrder(false);
                update(child);

            }
        }


        return order;
    }

    public void insertOrderPaymentItem(Order order, BigDecimal totalMoney) {
        OrderPaymentItem item = new OrderPaymentItem();
        item.setId(ApplicationUtils.randomUUID());
        item.setOrderId(order.getId());
        item.setPaymentModeId(PayMode.MONEY_PAY);
        item.setPayTime(order.getCreateTime());
        item.setPayValue(totalMoney);
        item.setRemark("商家在POS端使用其他支付方式确认订单:" + item.getPayValue());
        item.setResultData("其他支付方式");
        orderPaymentItemService.insert(item);
        payContent(order.getId());

    }


    public void payContent(String orderId) {
        Order order = selectById(orderId);
        if (order != null && order.getOrderMode() == ShopMode.HOUFU_ORDER && order.getOrderState() == OrderState.PAYMENT
                && order.getProductionStatus() == ProductionStatus.PRINTED) {
            Customer customer = customerService.selectById(order.getCustomerId());
            Brand brand = brandService.selectById(order.getBrandId());
            ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
            WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
            List<OrderPaymentItem> paymentItems = orderPaymentItemService.selectByOrderId(order.getId());
            String money = "(";
            for (OrderPaymentItem orderPaymentItem : paymentItems) {
                money += PayMode.getPayModeName(orderPaymentItem.getPaymentModeId()) + "： " + orderPaymentItem.getPayValue() + " ";
            }
            StringBuffer msg = new StringBuffer();
            BigDecimal sum = order.getOrderMoney();
            List<Order> orders = selectByParentId(order.getId(), order.getPayType()); //得到子订单
            for (Order child : orders) { //遍历子订单
                sum = sum.add(child.getOrderMoney());
            }
            BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
//            if(setting.getIsUseServicePrice() == 1){
//                sum = sum.add(order.getServicePrice());
//            }
            msg.append("您的订单").append(order.getSerialNumber()).append("已于").append(DateFormatUtils.format(paymentItems.get(0).getPayTime(), "yyyy-MM-dd HH:mm"));
            msg.append("支付成功。订单金额：").append(sum).append(money).append(") ");
            String result = weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
            Map map = new HashMap(4);
            map.put("brandName", setting.getBrandName());
            map.put("fileName", customer.getId());
            map.put("type", "UserAction");
            map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
        }
    }

    @Override
    public Order payPrice(BigDecimal factMoney, String orderId) {
        //拿到订单
        Order order = orderMapper.selectByPrimaryKey(orderId);

        Customer customer = customerService.selectById(order.getCustomerId());

        if (order.getOrderState() < OrderState.PAYMENT) {
            accountService.payOrder(order, factMoney, customer, null, null);
            order.setOrderState(OrderState.PAYMENT);
            order.setAllowCancel(false);
            order.setPaymentAmount(new BigDecimal(0));
            order.setAllowContinueOrder(false);
            update(order);
            List<Order> orders = orderMapper.selectByParentId(order.getId(), order.getPayType());
            for (Order child : orders) {
                if (child.getOrderState() < OrderState.PAYMENT) {
                    child.setOrderState(OrderState.PAYMENT);
                    child.setAllowCancel(false);
                    child.setAllowContinueOrder(false);
                    update(child);
                }
            }

        }

        return order;

    }

    @Override
    public void useRedPrice(BigDecimal factMoney, String orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        Customer customer = customerService.selectById(order.getCustomerId());
        accountService.payOrder(order, factMoney, customer, null, null);
    }


    @Override
    public Order getOrderDetail(String orderId) {
        Order order = orderMapper.getOrderDetail(orderId);
        order.setOrderItems(orderMapper.selectOrderItems(orderId));
        order.setOrderPaymentItems(orderMapper.selectOrderPaymentItems(orderId));
        return order;
    }

    @Override
    public List<Order> getOrderByEmployee(String employeeId, String shopId) {
        List<Order> result = orderMapper.getOrderByEmployee(shopId, employeeId);
        return result;
    }


    /**
     * 服务员点餐
     *
     * @param order
     * @return
     * @throws AppException
     */
    @Override
    public JSONResult createOrderByEmployee(Order order) throws AppException {
        JSONResult jsonResult = new JSONResult();
        String orderId = ApplicationUtils.randomUUID();
        order.setId(orderId);
        Employee employee = employeeMapper.selectByPrimaryKey(order.getEmployeeId());
        if (employee == null) {
            throw new AppException(AppException.CUSTOMER_NOT_EXISTS);
        } else if (order.getOrderItems().isEmpty()) {
            throw new AppException(AppException.ORDER_ITEMS_EMPTY);
        }

        List<Article> articles = articleService.selectList(order.getShopDetailId());
        List<ArticlePrice> articlePrices = articlePriceService.selectList(order.getShopDetailId());
        Map<String, Article> articleMap = ApplicationUtils.convertCollectionToMap(String.class, articles);
        Map<String, ArticlePrice> articlePriceMap = ApplicationUtils.convertCollectionToMap(String.class,
                articlePrices);


        order.setId(orderId);
        order.setCreateTime(new Date());
        BigDecimal totalMoney = BigDecimal.ZERO;
        int articleCount = 0;
        for (OrderItem item : order.getOrderItems()) {
            Article a = null;
            BigDecimal org_price = null;
            BigDecimal price = null;
            BigDecimal fans_price = null;
            item.setId(ApplicationUtils.randomUUID());
            switch (item.getType()) {
                case OrderItemType.ARTICLE:
                    // 查出 item对应的 商品信息，并将item的原价，单价，总价，商品名称，商品详情 设置为对应的
                    a = articleMap.get(item.getArticleId());
                    item.setArticleName(a.getName());
                    org_price = a.getPrice();
                    price = a.getPrice();
                    fans_price = a.getFansPrice();
                    break;
                case OrderItemType.UNITPRICE:
                    ArticlePrice p = articlePriceMap.get(item.getArticleId());
                    a = articleMap.get(p.getArticleId());
                    item.setArticleName(a.getName() + p.getName());
                    org_price = p.getPrice();
                    price = p.getPrice();
                    fans_price = p.getFansPrice();
                    break;
                case OrderItemType.SETMEALS:
                    a = articleMap.get(item.getArticleId());
                    item.setArticleName(a.getName());
                    org_price = a.getPrice();
                    price = a.getPrice();
                    fans_price = a.getFansPrice();
                    Integer[] mealItemIds = item.getMealItems();
                    List<MealItem> items = mealItemService.selectByIds(mealItemIds);
                    item.setChildren(new ArrayList<OrderItem>());
                    for (MealItem mealItem : items) {
                        OrderItem child = new OrderItem();
                        Article ca = articleMap.get(mealItem.getArticleId());
                        child.setId(ApplicationUtils.randomUUID());
                        child.setArticleName(mealItem.getName());
                        child.setArticleId(ca.getId());
                        child.setCount(item.getCount());
                        child.setArticleDesignation(ca.getDescription());
                        child.setParentId(item.getId());
                        child.setOriginalPrice(mealItem.getPriceDif());
                        child.setStatus(1);
                        child.setSort(0);
                        child.setUnitPrice(mealItem.getPriceDif());
                        child.setType(OrderItemType.MEALS_CHILDREN);
                        BigDecimal finalMoney = child.getUnitPrice().multiply(new BigDecimal(child.getCount())).setScale(2, BigDecimal.ROUND_HALF_UP);
                        child.setFinalPrice(finalMoney);
                        child.setOrderId(orderId);
                        totalMoney = totalMoney.add(finalMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                        item.getChildren().add(child);
                    }
                    break;
                default:
                    throw new AppException(AppException.UNSUPPORT_ITEM_TYPE, "不支持的餐品类型:" + item.getType());
            }
            item.setArticleDesignation(a.getDescription());
            item.setOriginalPrice(org_price);
            item.setStatus(1);
            item.setSort(0);
            if (fans_price != null) {
                item.setUnitPrice(fans_price);
            } else {
                item.setUnitPrice(price);
            }
            BigDecimal finalMoney = item.getUnitPrice().multiply(new BigDecimal(item.getCount())).setScale(2, BigDecimal.ROUND_HALF_UP);
            articleCount += item.getCount();
            item.setFinalPrice(finalMoney);
            item.setOrderId(orderId);
            totalMoney = totalMoney.add(finalMoney).setScale(2, BigDecimal.ROUND_HALF_UP);

            Result check = new Result();
            if (item.getType() == OrderItemType.ARTICLE) {
                check = checkArticleList(item, item.getCount());
            } else if (item.getType() == OrderItemType.UNITPRICE) {
                check = checkArticleList(item, item.getCount());
            } else if (item.getType() == OrderItemType.SETMEALS) {
                check = checkArticleList(item, articleCount);
            }


            jsonResult.setMessage(check.getMessage());
            jsonResult.setSuccess(check.isSuccess());

            if (!check.isSuccess()) {
                break;
            }
        }


        if (!jsonResult.isSuccess()) {
            return jsonResult;
        }

        orderItemService.insertItems(order.getOrderItems());
        BigDecimal payMoney = totalMoney;


        if (payMoney.doubleValue() < 0) {
            payMoney = BigDecimal.ZERO;
        }
        order.setAccountingTime(order.getCreateTime()); // 财务结算时间
        order.setAllowCancel(true); // 订单是否允许取消
        order.setAllowAppraise(false);
        order.setArticleCount(articleCount); // 订单餐品总数
        order.setClosed(false); // 订单是否关闭 否
        order.setSerialNumber(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSSS")); // 流水号
        order.setOriginalAmount(totalMoney);// 原价
        order.setReductionAmount(BigDecimal.ZERO);// 折扣金额
        order.setPrintTimes(0);
        order.setOrderState(order.getPayMode().toString().equals(PayMode.WEIXIN_PAY) ?
                OrderState.SUBMIT : OrderState.PAYMENT);

        log.info("服务员点餐时修改订单production：" + ProductionStatus.HAS_ORDER);
        order.setProductionStatus(ProductionStatus.HAS_ORDER);
        ShopDetail detail = shopDetailService.selectById(order.getShopDetailId());
        order.setOrderMode(detail.getShopMode());
        if (order.getOrderMode() == ShopMode.CALL_NUMBER) {
            order.setTableNumber(order.getVerCode());
        }
        if (order.getParentOrderId() != null) {
            Order parentOrder = selectById(order.getParentOrderId());
            order.setTableNumber(parentOrder.getTableNumber());
        }
        orderMapper.insertSelective(order);
        customerService.changeLastOrderShop(order.getShopDetailId(), order.getCustomerId());


        jsonResult.setData(order);
        return jsonResult;
    }


    @Override
    public Order lastOrderByCustomer(String customerId, String shopId, String tableNumber) {
        log.info("进入service查询");
        //得到自己购买的最新的一比允许加菜的订单
        Order order = orderMapper.getLastOrderByCustomer(customerId, shopId, 43200, tableNumber);
        return order;
    }

    @Override
    public Order lastOrderByCustomerGroupId(String customerId, String shopId, String groupId, String tableNumber) {
        log.info("进入service查询1111111");
        //得到自己购买的最新的一比允许加菜的订单
        Order order = orderMapper.getGroupOrderByGroupId(groupId);
        //如果是存在组 则把该人的当前购物车添加进入组
        if (order != null && order.getGroupId() != null) {
            shopCartService.updateGroupNew(customerId, shopId, order.getGroupId());
        }
        return order;
    }

    @Override
    public Order getLastOrderBySevenMode(String customerId) {
        return orderMapper.getLastOrderBySevenMode(customerId);
    }

    @Override
    public boolean cancelWXPayOrder(String orderId) {
        Order order = selectById(orderId);
        if (order.getAllowCancel() && order.getProductionStatus() != ProductionStatus.PRINTED && (order.getOrderState().equals(OrderState.SUBMIT) || order.getOrderState() == OrderState.PAYMENT)) {
            order.setAllowCancel(false);
            order.setClosed(true);
            order.setAllowAppraise(false);
            order.setAllowContinueOrder(false);
            order.setOrderState(OrderState.CANCEL);
            update(order);
            refundWXPAYOrder(order);
            log.info("取消订单成功:" + order.getId());
            return true;
        } else {
            log.warn("取消订单失败，订单状态订单状态或者订单可取消字段为false" + order.getId());
            return false;
        }
    }

    private void refundWXPAYOrder(Order order) {
        List<OrderPaymentItem> payItemsList = orderPaymentItemService.selectByOrderId(order.getId());
        for (OrderPaymentItem item : payItemsList) {
            String newPayItemId = ApplicationUtils.randomUUID();
            switch (item.getPaymentModeId()) {
                case PayMode.WEIXIN_PAY:
                    WechatConfig config = wechatConfigService.selectByBrandId(DataSourceContextHolder.getDataSourceName());
                    JSONObject obj = new JSONObject(item.getResultData());
                    Map<String, String> result = WeChatPayUtils.refund(newPayItemId, obj.getString("transaction_id"),
                            obj.getInt("total_fee"), obj.getInt("total_fee"), config.getAppid(), config.getMchid(),
                            config.getMchkey(), config.getPayCertPath());
                    item.setResultData(new JSONObject(result).toString());
                    break;
                default:
                    break;
            }
            item.setId(newPayItemId);
            item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
            orderPaymentItemService.insert(item);
        }
    }

    @Override
    public List<Order> selectExceptionOrderListBybrandId(String beginDate, String endDate, String brandId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);

        List<Order> list = orderMapper.selectExceptionOrderListBybrandId(begin, end, brandId);

        return list;
    }

    @Override
    public List<Order> selectHasPayListOrderByBrandId(String beginDate, String endDate, String brandId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectHasPayListOrderByBrandId(begin, end, brandId);
    }

    @Override
    public List<Order> selectHasPayOrderPayMentItemListBybrandId(String beginDate, String endDate, String brandId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectHasPayOrderPayMentItemListBybrandId(begin, end, brandId);
    }

    @Override
    public Order getLastOrderByTableNumber(String tableNumber, String shopId) {
        return orderMapper.getLastOrderByTableNumber(tableNumber, shopId);
    }

    @Override
    public Order selectOrderByTableNumberNoPay(String tableNumber, String shopId) {
        return orderMapper.selectOrderByTableNumberNoPay(tableNumber, shopId);
    }

    @Override
    public List<Order> selectOrderListItemByBrandId(String beginDate, String endDate, String brandId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectOrderListItemByBrandId(begin, end, brandId);
    }

    @Override
    public List<Order> selectListByParentId(String orderId) {
        return orderMapper.selectListByParentId(orderId);
    }

    @Override
    public List<Order> selectHoufuOrderList(String beginDate, String endDate, String brandId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectMoneyAndNumByDate(begin, end, brandId);
    }


    @Override
    public List<Order> getChildItem(String orderId) {
        Order parent = orderMapper.selectByPrimaryKey(orderId);
        List<Order> childs = orderMapper.selectByParentId(orderId, parent.getPayType());
        List<Order> result = new ArrayList<>();
        for (Order child : childs) {
            Order order = getOrderInfoPos(child.getId());
            result.add(order);
        }

        return result;

    }

    @Override
    public Result updateOrderItem(String orderId, Integer count, String orderItemId, Integer type) {

        Result result = new Result();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        Integer oldDistributionModeId = order.getDistributionModeId();
        StringBuffer pushMessage = new StringBuffer();
        BigDecimal updateCount = new BigDecimal(0);
        List<Map<String, Object>> printTask = new ArrayList<>();
        Brand brand = brandService.selectById(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", shopDetail.getName());
        map.put("type", "posAction");
        map.put("content", "接收到修改菜品的请求,订单号为 " + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        BrandSetting setting = brandSettingService.selectByBrandId(order.getBrandId());
        if (type.equals(ArticleType.SERVICE_PRICE)) { //如果要修改的是服务费
            BigDecimal baseCustomerCount = new BigDecimal(order.getCustomerCount());
            order.setCustomerCount(count);
//            order.setBaseCustomerCount(count); //这是bug， 注掉它
            order.setPaymentAmount(order.getPaymentAmount().subtract(order.getServicePrice()));
            if (order.getAmountWithChildren().doubleValue() > 0) {
                order.setAmountWithChildren(order.getAmountWithChildren().subtract(order.getServicePrice()));
            }
            order.setOrderMoney(order.getOrderMoney().subtract(order.getServicePrice()));
            order.setOriginalAmount(order.getOriginalAmount().subtract(order.getServicePrice()));
            order.setServicePrice(shopDetail.getServicePrice().multiply(new BigDecimal(count)));
            order.setPaymentAmount(order.getPaymentAmount().add(order.getServicePrice()));
            order.setOrderMoney(order.getOrderMoney().add(order.getServicePrice()));
            if (order.getAmountWithChildren().doubleValue() > 0) {
                order.setAmountWithChildren(order.getAmountWithChildren().add(order.getServicePrice()));
            }
            order.setOriginalAmount(order.getOriginalAmount().add(order.getServicePrice()));

            update(order);
            updateCount = baseCustomerCount.subtract(new BigDecimal(count));
            String message = "";
            Map<String, Object> orderItemMap = new HashMap<>();
            String ARTICLE_NAME = shopDetail.getServiceName();
            BigDecimal ARTICLE_COUNT = updateCount;
            BigDecimal SUBTOTAL = updateCount.multiply(shopDetail.getServicePrice());
            if (updateCount.compareTo(BigDecimal.ZERO) > 0) {
                message = "减" + updateCount + "份";
                order.setDistributionModeId(DistributionType.REFUND_ORDER);
                ARTICLE_NAME = ARTICLE_NAME.concat("(退)");
                ARTICLE_COUNT = updateCount.multiply(new BigDecimal(-1));
                SUBTOTAL = ARTICLE_COUNT.multiply(shopDetail.getServicePrice());
            } else {
                message = "加" + updateCount.abs() + "份";
                order.setDistributionModeId(DistributionType.MODIFY_ORDER);
                ARTICLE_NAME = ARTICLE_NAME.concat("(加)");
                ARTICLE_COUNT = updateCount.abs();
                SUBTOTAL = ARTICLE_COUNT.multiply(shopDetail.getServicePrice());
            }
            orderItemMap.put("SUBTOTAL", SUBTOTAL);
            orderItemMap.put("ARTICLE_NAME", ARTICLE_NAME);
            orderItemMap.put("ARTICLE_COUNT", ARTICLE_COUNT);
            if (shopDetail.getModifyOrderPrintReceipt().equals(Common.YES)) {
                List<Printer> printer = printerService.selectByShopAndType(shopDetail.getId(), PrinterType.RECEPTION);
                for (Printer p : printer) {
                    Map<String, Object> ticket = modifyOrderPrintReceipt(order, orderItemMap, p, shopDetail);
                    if (ticket != null) {
                        printTask.add(ticket);
                    }
                }
            }
            pushMessage.append(shopDetail.getServiceName() + "  " + message);
            map.put("content", shopDetail.getName() + "修改了" + shopDetail.getServiceName() + "，数量修改为" + count + ",订单号为:" + order.getId()
                    + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
        } else if (type.equals(ArticleType.SAUCE_FEE_PRICE)) { //修改的是新版餐具费
            BigDecimal baseSauceFeeCount = new BigDecimal(order.getSauceFeeCount()); //修改前的餐具数量
            order.setSauceFeeCount(count);
            order.setSauceFeePrice(new BigDecimal(count).multiply(shopDetail.getSauceFeePrice()));
            Boolean flg = true; //用来标识此次编辑是加还是减  true：加  false：减
            updateCount = baseSauceFeeCount.subtract(new BigDecimal(count)); //此次修改的数量
            if (updateCount.compareTo(BigDecimal.ZERO) > 0) { //如果大于0说明现在的比原来的小为减
                flg = false;
            }
            String message = flg ? "加" + updateCount.abs() + "份" : "减" + updateCount + "份"; //推送信息
            Map<String, Object> orderItemMap = new HashMap<>();
            String ARTICLE_NAME = flg ? shopDetail.getSauceFeeName().concat("(加)") : shopDetail.getSauceFeeName().concat("(减)"); //菜品
            BigDecimal ARTICLE_COUNT = flg ? updateCount.abs() : updateCount.multiply(new BigDecimal(-1));
            BigDecimal SUBTOTAL = ARTICLE_COUNT.multiply(shopDetail.getSauceFeePrice());
            if (!flg) {
                BigDecimal refundMoney = shopDetail.getSauceFeePrice().multiply(ARTICLE_COUNT.abs()); //减掉的金额
                order.setServicePrice(order.getServicePrice().subtract(refundMoney)); //减去此次剪掉的餐具费
                order.setPaymentAmount(order.getPaymentAmount().subtract(refundMoney));
                order.setOrderMoney(order.getOrderMoney().subtract(refundMoney));
                order.setOriginalAmount(order.getOriginalAmount().subtract(refundMoney));
                if (order.getAmountWithChildren().doubleValue() > 0) {
                    order.setAmountWithChildren(order.getAmountWithChildren().subtract(refundMoney));
                }
            } else {
                BigDecimal addMoney = shopDetail.getSauceFeePrice().multiply(ARTICLE_COUNT); //增加的金额
                order.setServicePrice(order.getServicePrice().add(addMoney));
                order.setPaymentAmount(order.getPaymentAmount().add(addMoney));
                order.setOrderMoney(order.getOrderMoney().add(addMoney));
                order.setOriginalAmount(order.getOriginalAmount().add(addMoney));
                if (order.getAmountWithChildren().doubleValue() > 0) {
                    order.setAmountWithChildren(order.getAmountWithChildren().add(addMoney));
                }
            }
            update(order);
            if (flg) {
                order.setDistributionModeId(DistributionType.MODIFY_ORDER); //加菜
            } else {
                order.setDistributionModeId(DistributionType.REFUND_ORDER); //退菜
            }
            orderItemMap.put("SUBTOTAL", SUBTOTAL);
            orderItemMap.put("ARTICLE_NAME", ARTICLE_NAME);
            orderItemMap.put("ARTICLE_COUNT", ARTICLE_COUNT);
            if (shopDetail.getModifyOrderPrintReceipt().equals(Common.YES)) {
                List<Printer> printer = printerService.selectByShopAndType(shopDetail.getId(), PrinterType.RECEPTION);
                for (Printer p : printer) {
                    Map<String, Object> ticket = modifyOrderPrintReceipt(order, orderItemMap, p, shopDetail);
                    if (ticket != null) {
                        printTask.add(ticket);
                    }
                }
            }
            pushMessage.append(shopDetail.getSauceFeeName() + "  " + message);
            map.put("content", shopDetail.getName() + "修改了" + shopDetail.getServiceName() + "，数量修改为" + count + ",订单号为:" + order.getId()
                    + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
        } else if (type.equals(ArticleType.TOWEL_FEE_PRICE)) { //修改的是新版纸巾费
            BigDecimal baseTowelFeeCount = new BigDecimal(order.getTowelFeeCount()); //修改前的纸巾数量
            order.setTowelFeeCount(count);
            order.setTowelFeePrice(new BigDecimal(count).multiply(shopDetail.getTowelFeePrice()));
            Boolean flg = true; //用来标识此次编辑是加还是减  true：加  false：减
            updateCount = baseTowelFeeCount.subtract(new BigDecimal(count)); //此次修改的数量
            if (updateCount.compareTo(BigDecimal.ZERO) > 0) { //如果大于0说明现在的比原来的小为减
                flg = false;
            }
            String message = flg ? "加" + updateCount.abs() + "份" : "减" + updateCount + "份"; //推送信息
            Map<String, Object> orderItemMap = new HashMap<>();
            String ARTICLE_NAME = flg ? shopDetail.getTowelFeeName().concat("(加)") : shopDetail.getTowelFeeName().concat("(减)"); //菜品
            BigDecimal ARTICLE_COUNT = flg ? updateCount.abs() : updateCount.multiply(new BigDecimal(-1));
            BigDecimal SUBTOTAL = ARTICLE_COUNT.multiply(shopDetail.getTowelFeePrice());
            if (!flg) {
                BigDecimal refundMoney = shopDetail.getTowelFeePrice().multiply(ARTICLE_COUNT.abs());
                order.setServicePrice(order.getServicePrice().subtract(refundMoney));
                order.setPaymentAmount(order.getPaymentAmount().subtract(refundMoney));
                order.setOrderMoney(order.getOrderMoney().subtract(refundMoney));
                order.setOriginalAmount(order.getOriginalAmount().subtract(refundMoney));
                if (order.getAmountWithChildren().doubleValue() > 0) {
                    order.setAmountWithChildren(order.getAmountWithChildren().subtract(refundMoney));
                }
            } else {
                BigDecimal addMoney = shopDetail.getTowelFeePrice().multiply(ARTICLE_COUNT);
                order.setServicePrice(order.getServicePrice().add(addMoney));
                order.setPaymentAmount(order.getPaymentAmount().add(addMoney));
                order.setOrderMoney(order.getOrderMoney().add(addMoney));
                order.setOriginalAmount(order.getOriginalAmount().add(addMoney));
                if (order.getAmountWithChildren().doubleValue() > 0) {
                    order.setAmountWithChildren(order.getAmountWithChildren().add(addMoney));
                }
            }
            update(order);
            if (flg) {
                order.setDistributionModeId(DistributionType.MODIFY_ORDER); //加菜
            } else {
                order.setDistributionModeId(DistributionType.REFUND_ORDER); //退菜
            }
            orderItemMap.put("SUBTOTAL", SUBTOTAL);
            orderItemMap.put("ARTICLE_NAME", ARTICLE_NAME);
            orderItemMap.put("ARTICLE_COUNT", ARTICLE_COUNT);
            if (shopDetail.getModifyOrderPrintReceipt().equals(Common.YES)) {
                List<Printer> printer = printerService.selectByShopAndType(shopDetail.getId(), PrinterType.RECEPTION);
                for (Printer p : printer) {
                    Map<String, Object> ticket = modifyOrderPrintReceipt(order, orderItemMap, p, shopDetail);
                    if (ticket != null) {
                        printTask.add(ticket);
                    }
                }
            }
            pushMessage.append(shopDetail.getTowelFeeName() + "  " + message);
            map.put("content", shopDetail.getName() + "修改了" + shopDetail.getServiceName() + "，数量修改为" + count + ",订单号为:" + order.getId()
                    + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
        } else if (type.equals(ArticleType.TABLEWARE_FEE_PRICE)) { //修改的是新版酱料费
            BigDecimal baseTableWareFeeCount = new BigDecimal(order.getTablewareFeeCount()); //修改前的酱料数量
            order.setTablewareFeeCount(count);
            order.setTablewareFeePrice(new BigDecimal(count).multiply(shopDetail.getTablewareFeePrice()));
            Boolean flg = true; //用来标识此次编辑是加还是减  true：加  false：减
            updateCount = baseTableWareFeeCount.subtract(new BigDecimal(count)); //此次修改的数量
            if (updateCount.compareTo(BigDecimal.ZERO) > 0) { //如果大于0说明现在的比原来的小为减
                flg = false;
            }
            String message = flg ? "加" + updateCount.abs() + "份" : "减" + updateCount + "份"; //推送信息
            Map<String, Object> orderItemMap = new HashMap<>();
            String ARTICLE_NAME = flg ? shopDetail.getTablewareFeeName().concat("(加)") : shopDetail.getTablewareFeeName().concat("(减)"); //菜品
            BigDecimal ARTICLE_COUNT = flg ? updateCount.abs() : updateCount.multiply(new BigDecimal(-1));
            BigDecimal SUBTOTAL = ARTICLE_COUNT.multiply(shopDetail.getTablewareFeePrice());
            if (!flg) {
                BigDecimal refundMoney = shopDetail.getTablewareFeePrice().multiply(ARTICLE_COUNT.abs());
                order.setServicePrice(order.getServicePrice().subtract(refundMoney));
                order.setPaymentAmount(order.getPaymentAmount().subtract(refundMoney));
                if (order.getAmountWithChildren().doubleValue() > 0) {
                    order.setAmountWithChildren(order.getAmountWithChildren().subtract(refundMoney));
                }
                order.setOrderMoney(order.getOrderMoney().subtract(refundMoney));
                order.setOriginalAmount(order.getOriginalAmount().subtract(refundMoney));
            } else {
                BigDecimal addMoney = shopDetail.getTablewareFeePrice().multiply(ARTICLE_COUNT);
                order.setServicePrice(order.getServicePrice().add(addMoney));
                order.setPaymentAmount(order.getPaymentAmount().add(addMoney));
                order.setOrderMoney(order.getOrderMoney().add(addMoney));
                if (order.getAmountWithChildren().doubleValue() > 0) {
                    order.setAmountWithChildren(order.getAmountWithChildren().add(addMoney));
                }
                order.setOriginalAmount(order.getOriginalAmount().add(addMoney));
            }
            update(order);
            if (flg) {
                order.setDistributionModeId(DistributionType.MODIFY_ORDER); //加菜
            } else {
                order.setDistributionModeId(DistributionType.REFUND_ORDER); //退菜
            }
            orderItemMap.put("SUBTOTAL", SUBTOTAL);
            orderItemMap.put("ARTICLE_NAME", ARTICLE_NAME);
            orderItemMap.put("ARTICLE_COUNT", ARTICLE_COUNT);
            if (shopDetail.getModifyOrderPrintReceipt().equals(Common.YES)) {
                List<Printer> printer = printerService.selectByShopAndType(shopDetail.getId(), PrinterType.RECEPTION);
                for (Printer p : printer) {
                    Map<String, Object> ticket = modifyOrderPrintReceipt(order, orderItemMap, p, shopDetail);
                    if (ticket != null) {
                        printTask.add(ticket);
                    }
                }
            }
            pushMessage.append(shopDetail.getTablewareFeeName() + "  " + message);
            map.put("content", shopDetail.getName() + "修改了" + shopDetail.getServiceName() + "，数量修改为" + count + ",订单号为:" + order.getId()
                    + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
        } else { //修改的是菜品
            OrderItem orderItem = orderItemService.selectById(orderItemId); //找到要修改的菜品
            if (count > orderItem.getCount()) {
                result.setSuccess(false);
                result.setMessage("餐品修改数量有误");
                map.put("content", shopDetail.getName() + "修改菜品失败：餐品修改数量有误。 itemId：" + orderItemId + "" + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
                return result;
            }
            BigDecimal baseArticleCount = new BigDecimal(orderItem.getCount());
            if (orderItem.getType() == OrderItemType.MEALS_CHILDREN) {
                result.setSuccess(false);
                result.setMessage("套餐子品暂不支持修改");
                map.put("content", shopDetail.getName() + "修改菜品失败：套餐子品暂不支持修改。 itemId：" + orderItemId + "" + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
                return result;
            }

            if (order.getParentOrderId() == null) {
                if (order.getArticleCount() == 0 && count == 0) {
                    result.setSuccess(false);
                    result.setMessage("菜品数量不可为空");
                    map.put("content", shopDetail.getName() + "修改菜品失败：菜品数量不可为空。 itemId：" + orderItemId + "" + ",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                    return result;
                }
            }
            //查询套餐子品的信息
            List<OrderItem> list = orderitemMapper.getListBySort(orderItem.getId(), orderItem.getArticleId());
            for (OrderItem zpOrderItem : list) {
                //套餐主品的价格加上有差价的子品的价格
                orderItem.setFinalPrice(orderItem.getFinalPrice().add(zpOrderItem.getFinalPrice()));
            }
            order.setArticleCount(order.getArticleCount() - orderItem.getCount());
            order.setOrderMoney(order.getOrderMoney().subtract(orderItem.getFinalPrice()));
            order.setOriginalAmount(order.getOriginalAmount().subtract(new BigDecimal(orderItem.getCount()).multiply(orderItem.getOriginalPrice())));
            order.setPaymentAmount(order.getPaymentAmount().subtract(orderItem.getFinalPrice()));
            if (order.getAmountWithChildren().doubleValue() > 0) {
                order.setAmountWithChildren(order.getAmountWithChildren().subtract(orderItem.getFinalPrice()));
                order.setCountWithChild(order.getCountWithChild() - orderItem.getCount());
            }

            orderItem.setCount(count);
            orderItem.setChangeCount(count);
            orderItem.setFinalPrice(orderItem.getUnitPrice().multiply(new BigDecimal(count)));
            orderitemMapper.updateByPrimaryKeySelective(orderItem);
            for (OrderItem zpOrderItem : list) {
                zpOrderItem.setCount(count);
                zpOrderItem.setFinalPrice(zpOrderItem.getUnitPrice().multiply(new BigDecimal(count)));
                orderItem.setFinalPrice(orderItem.getFinalPrice().add(zpOrderItem.getFinalPrice()));
                orderitemMapper.updateByPrimaryKeySelective(zpOrderItem);
            }
            order.setArticleCount(order.getArticleCount() + orderItem.getCount());
            order.setOrderMoney(order.getOrderMoney().add(orderItem.getFinalPrice()));
            order.setOriginalAmount(order.getOriginalAmount().add(new BigDecimal(orderItem.getCount()).multiply(orderItem.getOriginalPrice())));
            order.setPaymentAmount(order.getPaymentAmount().add(orderItem.getFinalPrice()));
            if (order.getAmountWithChildren().doubleValue() > 0) {
                order.setAmountWithChildren(order.getAmountWithChildren().add(orderItem.getFinalPrice()));
                order.setCountWithChild(order.getCountWithChild() + orderItem.getCount());
            }

            if (orderItem.getCount() == 0) {
                orderitemMapper.deleteByPrimaryKey(orderItem.getId());
                for (OrderItem zpOrderItem : list) {
                    orderitemMapper.deleteByPrimaryKey(zpOrderItem.getId());
                }
            }

            update(order);
            updateCount = baseArticleCount.subtract(new BigDecimal(count));
            String message = "";
            Map<String, Object> orderItemMap = new HashMap<>();
            String ARTICLE_NAME = orderItem.getArticleName();
            BigDecimal ARTICLE_COUNT = updateCount;
            BigDecimal SUBTOTAL = updateCount.multiply(orderItem.getUnitPrice());
            if (updateCount.compareTo(BigDecimal.ZERO) > 0) {
                message = "减" + updateCount + "份";
                order.setDistributionModeId(DistributionType.REFUND_ORDER);
                ARTICLE_NAME = ARTICLE_NAME.concat("(退)");
                ARTICLE_COUNT = updateCount.multiply(new BigDecimal(-1));
                SUBTOTAL = ARTICLE_COUNT.multiply(orderItem.getUnitPrice());
            } else {
                message = "加" + updateCount.abs() + "份";
                order.setDistributionModeId(DistributionType.MODIFY_ORDER);
                ARTICLE_NAME = ARTICLE_NAME.concat("(加)");
                ARTICLE_COUNT = updateCount.abs();
                SUBTOTAL = ARTICLE_COUNT.multiply(orderItem.getUnitPrice());
            }
            orderItemMap.put("SUBTOTAL", SUBTOTAL);
            orderItemMap.put("ARTICLE_NAME", ARTICLE_NAME);
            orderItemMap.put("ARTICLE_COUNT", ARTICLE_COUNT);
            if (shopDetail.getModifyOrderPrintReceipt().equals(Common.YES)) {
                List<Printer> printer = printerService.selectByShopAndType(shopDetail.getId(), PrinterType.RECEPTION);
                for (Printer p : printer) {
                    Map<String, Object> ticket = modifyOrderPrintReceipt(order, orderItemMap, p, shopDetail);
                    if (ticket != null) {
                        printTask.add(ticket);
                    }
                }
            }
            if (shopDetail.getModifyOrderPrintKitchen().equals(Common.YES)) {
                List<OrderItem> orderItemList = new ArrayList<>();
                orderItem.setCount(ARTICLE_COUNT.abs().intValue());
                for (OrderItem zpOrderItem : list) {
                    zpOrderItem.setCount(ARTICLE_COUNT.abs().intValue());
                }
                orderItem.setChildren(list);
                orderItemList.add(orderItem);
                printTask.addAll(printKitchen(order, orderItemList, order.getDistributionModeId()));
            }
            pushMessage.append(orderItem.getArticleName() + "  " + message);
            map.put("content", shopDetail.getName() + "修改了菜品" + orderItem.getArticleName() + "，数量修改为" + count + ",订单号为:" + order.getId()
                    + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
        }

        if (order.getOrderMode() == ShopMode.HOUFU_ORDER || order.getOrderMode() == ShopMode.BOSS_ORDER) {
            if (order.getParentOrderId() != null) {  //子订单
                Order parent = selectById(order.getParentOrderId());
                int articleCountWithChildren = 0;


                if (parent.getLastOrderTime() == null || parent.getLastOrderTime().getTime() < order.getCreateTime().getTime()) {
                    parent.setLastOrderTime(order.getCreateTime());
                }
                Double amountWithChildren = 0.0;
                if (order.getOrderMode() == ShopMode.HOUFU_ORDER) {
                    articleCountWithChildren = selectArticleCountById(parent.getId(), order.getOrderMode());
                    amountWithChildren = orderMapper.selectParentAmount(parent.getId(), parent.getOrderMode());
                } else {
                    articleCountWithChildren = orderMapper.selectArticleCountByIdBossOrder(parent.getId());
                    amountWithChildren = orderMapper.selectParentAmountByBossOrder(parent.getId());
                }

                parent.setCountWithChild(articleCountWithChildren);
                parent.setAmountWithChildren(new BigDecimal(amountWithChildren));
                List<Order> needOrder = orderMapper.selectNeedWeightArticle(parent.getId());
                if (needOrder == null || needOrder.size() == 0) {
                    parent.setNeedConfirmOrderItem(0);
                }
                update(parent);

            }
        }
        result.setSuccess(true);
        result.setMessage(printTask.size() > 0 ? JSON.toJSONString(printTask) : null);
        Customer customer = customerService.selectById(order.getCustomerId());
        if (customer != null) {
            WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
            StringBuffer msg = new StringBuffer();
            msg.append("商家已在收银电脑处更新了您的订单信息：" + "\n");
            msg.append(pushMessage.toString());
//            weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
//            Map customerMap = new HashMap(4);
//            customerMap.put("brandName", brand.getBrandName());
//            customerMap.put("fileName", customer.getId());
//            customerMap.put("type", "UserAction");
//            customerMap.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
//            doPostAnsc(customerMap);
//            weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());

            if (order.getGroupId() == null || "".equals(order.getGroupId())) {
                weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                Map customerMap = new HashMap(4);
                customerMap.put("brandName", brand.getBrandName());
                customerMap.put("fileName", customer != null ? customer.getId() : "Pos下单 ");
                customerMap.put("type", "UserAction");
                customerMap.put("content", "系统向用户:" + (customer != null ? customer.getNickname() : "Pos下单不") + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(customerMap);
            } else {
                if (order.getParentOrderId() != null && !"".equals(order.getParentOrderId())) {
                    orderId = order.getParentOrderId();
                }
                List<Participant> participants = participantService.selectCustomerListByGroupIdOrderId(order.getGroupId(), orderId);
                for (Participant p : participants) {
                    Customer c = customerService.selectById(p.getCustomerId());
                    weChatService.sendCustomerMsg(msg.toString(), c.getWechatId(), config.getAppid(), config.getAppsecret());
                    Map map1 = new HashMap(4);
                    map1.put("brandName", brand.getBrandName());
                    map1.put("fileName", c.getId());
                    map1.put("type", "UserAction");
                    map1.put("content", "系统向用户:" + c.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map1);
                }
            }

        }


        Order parent = null;
        if (order.getParentOrderId() != null) {
            parent = orderMapper.selectByPrimaryKey(order.getParentOrderId());
        } else {
            parent = order;
        }

        StringBuffer msg = new StringBuffer();
        msg.append("商家已在收银电脑处更新了您的订单信息：" + "\n");
        msg.append(pushMessage.toString());

        Order newOrder = new Order();
        newOrder.setId(order.getId());
        newOrder.setDistributionModeId(oldDistributionModeId);
        if (Common.NO.equals(order.getArticleCount())
                && Optional.ofNullable(order.getCountWithChild()).orElse(Common.NO).equals(Common.NO)) {
            //后付情况下如果菜品被编辑光后，此时将该笔订单设置为取消状态
            newOrder.setOrderState(OrderState.CANCEL);
        }
        List<Order> needOrder = orderMapper.selectNeedWeightArticle(order.getId());
        if (needOrder == null || needOrder.size() == 0) {
            newOrder.setNeedConfirmOrderItem(0);
        }
        orderMapper.updateByPrimaryKeySelective(newOrder);
        return result;
    }

    public Map<String, Object> modifyOrderPrintReceipt(Order order, Map<String, Object> orderItem, Printer printer, ShopDetail shopDetail) {
        if (printer == null) {
            return null;
        }
        List<Map<String, Object>> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        Map<String, Object> print = new HashMap<>();
        String tableNumber = order.getTableNumber() != null ? order.getTableNumber() : "";
        print.put("TABLE_NO", tableNumber);
        print.put("KITCHEN_NAME", printer.getName());
        print.put("PORT", printer.getPort());
        print.put("ORDER_ID", order.getSerialNumber());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_TASK_ID", print_id);
        print.put("ADD_TIME", new Date());
        Map<String, Object> data = new HashMap<>();
        data.put("ORDER_ID", order.getSerialNumber() + "-" + order.getVerCode());
        data.put("ORDER_NUMBER", (String) redisService.get(order.getId() + "orderNumber"));
        data.put("ITEMS", orderItems);
        List<Map<String, Object>> patMentItems = new ArrayList<Map<String, Object>>();
        data.put("PAYMENT_ITEMS", patMentItems);
        com.resto.api.appraise.entity.Appraise appraise = newAppraiseService.selectAppraiseByCustomerId(order.getBrandId(),order.getCustomerId(), order.getShopDetailId());
        StringBuilder star = new StringBuilder();
        BigDecimal level = new BigDecimal(0);
        if (appraise != null) {
            star.append("★★★★★☆☆☆☆☆".substring(5 - appraise.getLevel(), 10 - appraise.getLevel()));
            Map<String, Object> appriseCount = newAppraiseService.selectCustomerAppraiseAvg(order.getBrandId(),order.getCustomerId());
            level = new BigDecimal(Integer.valueOf(appriseCount.get("sum").toString()))
                    .divide(new BigDecimal(Integer.valueOf(appriseCount.get("count").toString())), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            star.append("☆☆☆☆☆");
        }
        StringBuilder gao = new StringBuilder();
        if (shopDetail.getIsUserIdentity().equals(1)) {
            //得到有限制的情况下用户的订单数
            int gaoCount = orderMapper.selectByCustomerCount(order.getCustomerId());
            if (gaoCount>1) {
                gao.append("消费" + gaoCount + "次");
            } else {
                gao.append("新顾客");
            }
        }
        String modeText = getModeText(order);
        data.put("DISTRIBUTION_MODE", modeText);
        data.put("ORIGINAL_AMOUNT", orderItem.get("SUBTOTAL"));
        data.put("RESTAURANT_ADDRESS", shopDetail.getAddress());
        data.put("REDUCTION_AMOUNT", 0);
        data.put("RESTAURANT_TEL", shopDetail.getPhone());
        data.put("TABLE_NUMBER", order.getTableNumber());
        data.put("CUSTOMER_COUNT", order.getCustomerCount() == null ? "-" : order.getCustomerCount());
        data.put("PAYMENT_AMOUNT", new BigDecimal(orderItem.get("SUBTOTAL").toString()).compareTo(BigDecimal.ZERO) > 0 ? orderItem.get("SUBTOTAL") : 0);
        data.put("RESTAURANT_NAME", shopDetail.getName());
        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的编辑订单的总单打印次数， 如果为空则是第一次打印则当前打印次数为1
        Integer printTimes = redisService.get(order.getId() + "MODIFYTICKET") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "MODIFYTICKET").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        data.put("ARTICLE_COUNT", orderItem.get("ARTICLE_COUNT"));
        data.put("CUSTOMER_SATISFACTION", star.toString());
        data.put("CUSTOMER_SATISFACTION_DEGREE", level);
        Account account = accountService.selectAccountAndLogByCustomerId(order.getCustomerId());
        StringBuffer customerStr = new StringBuffer();
        if (account != null) {
            customerStr.append("余额：" + account.getRemain() + " ");
        } else {
            customerStr.append("余额：0 ");
        }
        customerStr.append("" + gao.toString() + " ");

        Customer customer = customerService.selectById(order.getCustomerId());
        if (customer != null) {
            CustomerDetail customerDetail = customerDetailMapper.selectByPrimaryKey(customer.getCustomerDetailId());
            if (customerDetail != null) {
                if (customerDetail.getBirthDate() != null) {
                    if (DateUtil.formatDate(customerDetail.getBirthDate(), "MM-dd")
                            .equals(DateUtil.formatDate(new Date(), "MM-dd"))) {
                        customerStr.append("★" + DateUtil.formatDate(customerDetail.getBirthDate(), "yyyy-MM-dd") + "★");
                    }
                }
            }
        }
        data.put("CUSTOMER_PROPERTY", customerStr.toString());
        print.put("DATA", data);
        print.put("STATUS", 0);
        print.put("TICKET_TYPE", TicketType.RECEIPT);
        //订单返回编辑订单打印模板视为已打印成功
        redisService.set(order.getId() + "MODIFYTICKET", printTimes);
        return print;
    }

    @Override
    public void refundArticle(Order order) {
        List<OrderPaymentItem> payItemsList = orderPaymentItemService.selectByOrderId(order.getId());
        //退款完成后变更订单项
        Order o = getOrderInfo(order.getId());
        if (o.getOrderState() == OrderState.SUBMIT) {
            return;
        }

        Brand brand = brandService.selectById(o.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(o.getShopDetailId());
        Customer customer = customerService.selectById(o.getCustomerId());
        if (customer == null) {
            OrderPaymentItem item = new OrderPaymentItem();
            item.setId(ApplicationUtils.randomUUID());
            item.setPayValue(new BigDecimal(-1).multiply(order.getRefundMoney()));
            item.setPayTime(new Date());
            item.setPaymentModeId(PayMode.REFUND_CRASH);
            item.setOrderId(o.getId());
            item.setRemark("线下现金退款:" + order.getRefundMoney());
            item.setResultData("线下现金退款" + order.getRefundMoney());
            orderPaymentItemService.insert(item);
            return;
        }


        int refundMoney = order.getRefundMoney().multiply(new BigDecimal(100)).intValue();

        //如果退菜订单是  后付情况下加菜后统一支付  则支付项是在主订单下    修改退菜金额改变的逻辑
        if (o.getParentOrderId() != null && o.getPayType() == PayType.NOPAY) {
            payItemsList = orderPaymentItemService.selectByOrderId(o.getParentOrderId());
        }

        BigDecimal maxWxRefund = new BigDecimal(0);
        for (OrderPaymentItem item : payItemsList) {
            if (item.getPaymentModeId() == PayMode.WEIXIN_PAY) {
                maxWxRefund = maxWxRefund.add(item.getPayValue());
            }
            if (item.getPaymentModeId() == PayMode.ALI_PAY) {
                maxWxRefund = maxWxRefund.add(item.getPayValue());
            }
        }
        if (RefundType.OFFLINE_PAY.equals(order.getRefundType())) {
            OrderPaymentItem back = new OrderPaymentItem();
            back.setId(ApplicationUtils.randomUUID());
            back.setOrderId(order.getId());
            back.setPaymentModeId(PayMode.REFUND_CRASH);
            back.setPayTime(new Date());
            back.setPayValue(new BigDecimal(-1).multiply(order.getRefundMoney()));
            back.setRemark("线下现金退款:" + order.getRefundMoney());
            back.setResultData("线下现金退款" + order.getRefundMoney());
            orderPaymentItemService.insert(back);
            if (customer != null) {
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", shopDetail.getName());
                map.put("type", "posAction");
                map.put("content", "订单:" + order.getId() + "在pos端执行退菜线下退款现金" + order.getRefundMoney() + "元,返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
                Map orderMap = new HashMap(4);
                orderMap.put("brandName", brand.getBrandName());
                orderMap.put("fileName", order.getId());
                orderMap.put("type", "orderAction");
                orderMap.put("content", "订单:" + order.getId() + "在pos端执行退菜线下退款现金" + order.getRefundMoney() + "元,返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(orderMap);
            }
        } else if (maxWxRefund.doubleValue() > 0) { //如果微信支付或者支付宝还有钱可以退
            for (OrderPaymentItem item : payItemsList) {
                String newPayItemId = ApplicationUtils.randomUUID();
                switch (item.getPaymentModeId()) {
                    case PayMode.WEIXIN_PAY:
                        if (item.getPayValue().doubleValue() > 0) {
                            WechatConfig config = wechatConfigService.selectByBrandId(DataSourceContextHolder.getDataSourceName());
                            JSONObject obj = new JSONObject(item.getResultData());
                            Map<String, String> result = new HashMap<>();
                            int total = obj.getInt("total_fee");
                            int maxWxPay = maxWxRefund.multiply(new BigDecimal(100)).intValue();
                            if (shopDetail.getWxServerId() == null) {
                                result = WeChatPayUtils.refund(newPayItemId, obj.getString("transaction_id"), total
                                        , maxWxPay > refundMoney ? refundMoney : maxWxPay
                                        , StringUtils.isEmpty(shopDetail.getAppid()) ? config.getAppid() : shopDetail.getAppid(),
                                        StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(),
                                        StringUtils.isEmpty(shopDetail.getMchkey()) ? config.getMchkey() : shopDetail.getMchkey(),
                                        StringUtils.isEmpty(shopDetail.getPayCertPath()) ? config.getPayCertPath() : shopDetail.getPayCertPath());
                            } else {
                                WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());

                                result = WeChatPayUtils.refundNew(newPayItemId, obj.getString("transaction_id"),
                                        total, maxWxPay > refundMoney ? refundMoney : maxWxPay, wxServerConfig.getAppid(), wxServerConfig.getMchid(),
                                        StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(), wxServerConfig.getMchkey(), wxServerConfig.getPayCertPath());
                            }
                            if (result.containsKey("ERROR")) {
                                log.error("微信退款失败！失败信息：" + new JSONObject(result).toString());
                                throw new RuntimeException(new JSONObject(result).toString());
                            }
                            BigDecimal realBack = maxWxRefund.doubleValue() > order.getRefundMoney().doubleValue() ? order.getRefundMoney() : maxWxRefund;
                            item.setResultData(new JSONObject(result).toString());
                            item.setId(newPayItemId);
                            item.setPayValue(realBack.multiply(new BigDecimal(-1)));
                            orderPaymentItemService.insert(item);
                            if (maxWxPay < refundMoney) { //如果要退款的金额 比实际微信支付要大
                                int charge = refundMoney - maxWxPay;
                                BigDecimal wxBack = new BigDecimal(maxWxPay).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                                BigDecimal backMoney = new BigDecimal(charge).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                                OrderPaymentItem back = new OrderPaymentItem();
                                back.setId(ApplicationUtils.randomUUID());
                                back.setOrderId(order.getId());
                                back.setPaymentModeId(PayMode.ARTICLE_BACK_PAY);
                                back.setPayTime(new Date());
                                back.setPayValue(new BigDecimal(-1).multiply(backMoney));
                                back.setRemark("退菜红包:" + backMoney);

                                back.setResultData("总退款金额" + order.getRefundMoney() + ",微信支付返回" + wxBack + ",余额返回" + backMoney);
                                orderPaymentItemService.insert(back);
                                accountService.addAccount(backMoney, customer.getAccountId(), "退菜红包", AccountLog.REFUND_ARTICLE_RED_PACKAGE, order.getShopDetailId());
                                RedPacket redPacket = new RedPacket();
                                redPacket.setId(ApplicationUtils.randomUUID());
                                redPacket.setRedMoney(backMoney);
                                redPacket.setCreateTime(new Date());
                                redPacket.setCustomerId(customer.getId());
                                redPacket.setBrandId(order.getBrandId());
                                redPacket.setShopDetailId(order.getShopDetailId());
                                redPacket.setRedRemainderMoney(backMoney);
                                redPacket.setRedType(RedType.REFUND_ARTICLE_RED);
                                redPacket.setOrderId(order.getId());
                                redPacketService.insert(redPacket);
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", shopDetail.getName());
                                map.put("type", "posAction");
                                map.put("content", "订单:" + order.getId() + "在pos端执行退菜返还红包" + backMoney + "元,返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                                Map orderMap = new HashMap(4);
                                orderMap.put("brandName", brand.getBrandName());
                                orderMap.put("fileName", order.getId());
                                orderMap.put("type", "orderAction");
                                orderMap.put("content", "订单:" + order.getId() + "在pos端执行退菜返还红包" + backMoney + "元,返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(orderMap);
                            }
                            Map map = new HashMap(4);
                            map.put("brandName", brand.getBrandName());
                            map.put("fileName", shopDetail.getName());
                            map.put("type", "posAction");
                            map.put("content", "订单:" + order.getId() + "在pos端执行退菜微信返还" + (maxWxPay > refundMoney ? refundMoney : maxWxPay) + "元回调返回信息:" + result.toString() + "" +
                                    ",返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(map);
                            Map orderMap = new HashMap(4);
                            orderMap.put("brandName", brand.getBrandName());
                            orderMap.put("fileName", order.getId());
                            orderMap.put("type", "orderAction");
                            orderMap.put("content", "订单:" + order.getId() + "在pos端执行退菜微信返还" + (maxWxPay > refundMoney ? refundMoney : maxWxPay) + "元回调返回信息:" + result.toString() + "" +
                                    ",返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(orderMap);
                        }
                        break;
                    case PayMode.ALI_PAY:
                        if (item.getPayValue().doubleValue() > 0) {
                            BigDecimal refundTotal = maxWxRefund.doubleValue() > order.getRefundMoney().doubleValue() ?
                                    order.getRefundMoney() : maxWxRefund;

                            BrandSetting brandSetting = brandSettingService.selectByBrandId(o.getBrandId());
                            AliPayUtils.connection(StringUtils.isEmpty(shopDetail.getAliAppId()) ? brandSetting.getAliAppId() : shopDetail.getAliAppId().trim(),
                                    StringUtils.isEmpty(shopDetail.getAliPrivateKey()) ? brandSetting.getAliPrivateKey().trim() : shopDetail.getAliPrivateKey().trim(),
                                    StringUtils.isEmpty(shopDetail.getAliPublicKey()) ? brandSetting.getAliPublicKey().trim() : shopDetail.getAliPublicKey().trim(),
                                    shopDetail.getAliEncrypt());
                            Map map = new HashMap();
                            map.put("out_trade_no", o.getId());
                            map.put("refund_amount", refundTotal);
                            map.put("out_request_no", newPayItemId);
                            Map<String, String> returnMap = AliPayUtils.refundPay(map);
                            if (returnMap.get("success").equalsIgnoreCase("false")) {
                                log.error("支付宝退款失败！失败信息：" + returnMap.toString());
                                throw new RuntimeException(returnMap.toString());
                            }
                            item.setId(newPayItemId);
                            item.setResultData(new JSONObject(returnMap.get("returnMsg")).toString());
                            item.setPayValue(refundTotal.multiply(new BigDecimal(-1)));
                            orderPaymentItemService.insert(item);
                            if (maxWxRefund.doubleValue() < order.getRefundMoney().doubleValue()) { //如果最大退款金额 比实际要退的小
                                BigDecimal backMoney = order.getRefundMoney().subtract(maxWxRefund);
                                OrderPaymentItem back = new OrderPaymentItem();
                                back.setId(ApplicationUtils.randomUUID());
                                back.setOrderId(order.getId());
                                back.setPaymentModeId(PayMode.ARTICLE_BACK_PAY);
                                back.setPayTime(new Date());
                                back.setPayValue(new BigDecimal(-1).multiply(backMoney));
                                back.setRemark("退菜红包:" + backMoney);

                                back.setResultData("总退款金额" + order.getRefundMoney() + ",支付宝支付返回" + refundTotal + ",余额返回" + backMoney);
                                orderPaymentItemService.insert(back);
                                accountService.addAccount(backMoney, customer.getAccountId(), "退菜红包", AccountLog.REFUND_ARTICLE_RED_PACKAGE, order.getShopDetailId());
                                RedPacket redPacket = new RedPacket();
                                redPacket.setId(ApplicationUtils.randomUUID());
                                redPacket.setRedMoney(backMoney);
                                redPacket.setCreateTime(new Date());
                                redPacket.setCustomerId(customer.getId());
                                redPacket.setBrandId(order.getBrandId());
                                redPacket.setShopDetailId(order.getShopDetailId());
                                redPacket.setRedRemainderMoney(backMoney);
                                redPacket.setRedType(RedType.REFUND_ARTICLE_RED);
                                redPacket.setOrderId(order.getId());
                                redPacketService.insert(redPacket);
                                Map accountMap = new HashMap(4);
                                accountMap.put("brandName", brand.getBrandName());
                                accountMap.put("fileName", shopDetail.getName());
                                accountMap.put("type", "posAction");
                                accountMap.put("content", "订单:" + order.getId() + "在pos端执行退菜返还红包" + backMoney + "元,返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(accountMap);
                                Map orderAccountMap = new HashMap(4);
                                orderAccountMap.put("brandName", brand.getBrandName());
                                orderAccountMap.put("fileName", order.getId());
                                orderAccountMap.put("type", "orderAction");
                                orderAccountMap.put("content", "订单:" + order.getId() + "在pos端执行退菜返还红包" + backMoney + "元,返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(orderAccountMap);
                            }
                            Map aliMap = new HashMap(4);
                            aliMap.put("brandName", brand.getBrandName());
                            aliMap.put("fileName", shopDetail.getName());
                            aliMap.put("type", "posAction");
                            aliMap.put("content", "订单:" + order.getId() + "在pos端执行退菜支付宝返还" + refundTotal + "元回调返回信息:" + returnMap.toString() + "" +
                                    ",返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(aliMap);
                            Map orderAliMap = new HashMap(4);
                            orderAliMap.put("brandName", brand.getBrandName());
                            orderAliMap.put("fileName", order.getId());
                            orderAliMap.put("type", "orderAction");
                            orderAliMap.put("content", "订单:" + order.getId() + "在pos端执行退菜支付宝返还" + refundTotal + "元回调返回信息:" + returnMap.toString() + "" +
                                    ",返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                            doPostAnsc(orderAliMap);
                        }
                        break;

                    default:
                        break;
                }
            }
        } else {
            OrderPaymentItem back = new OrderPaymentItem();
            back.setId(ApplicationUtils.randomUUID());
            back.setOrderId(order.getId());
            back.setPaymentModeId(PayMode.ARTICLE_BACK_PAY);
            back.setPayTime(new Date());
            back.setPayValue(new BigDecimal(-1).multiply(order.getRefundMoney()));
            back.setRemark("退菜红包:" + order.getRefundMoney());

            back.setResultData("总退款金额" + order.getRefundMoney() + "余额返回" + order.getRefundMoney());
            orderPaymentItemService.insert(back);
            accountService.addAccount(order.getRefundMoney(), customer.getAccountId(), "退菜红包", AccountLog.REFUND_ARTICLE_RED_PACKAGE, order.getShopDetailId());
            RedPacket redPacket = new RedPacket();
            redPacket.setId(ApplicationUtils.randomUUID());
            redPacket.setRedMoney(order.getRefundMoney());
            redPacket.setCreateTime(new Date());
            redPacket.setCustomerId(customer.getId());
            redPacket.setBrandId(order.getBrandId());
            redPacket.setShopDetailId(order.getShopDetailId());
            redPacket.setRedRemainderMoney(order.getRefundMoney());
            redPacket.setRedType(RedType.REFUND_ARTICLE_RED);
            redPacket.setOrderId(order.getId());
            redPacketService.insert(redPacket);
            Map map = new HashMap(4);
            map.put("brandName", brand.getBrandName());
            map.put("fileName", shopDetail.getName());
            map.put("type", "posAction");
            map.put("content", "订单:" + order.getId() + "在pos端执行退菜返还红包" + order.getRefundMoney() + "元,返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            Map orderMap = new HashMap(4);
            orderMap.put("brandName", brand.getBrandName());
            orderMap.put("fileName", order.getId());
            orderMap.put("type", "orderAction");
            orderMap.put("content", "订单:" + order.getId() + "在pos端执行退菜返还红包" + order.getRefundMoney() + "元,返还用户Id:" + customer.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(orderMap);
        }
    }

    public void refundArticleNoPay(Order order) {
        OrderPaymentItem back = new OrderPaymentItem();
        back.setId(ApplicationUtils.randomUUID());
        back.setOrderId(order.getId());
        back.setPaymentModeId(PayMode.REFUND_CRASH);
        back.setPayTime(new Date());
        back.setPayValue(new BigDecimal(-1).multiply(order.getRefundMoney()));
        back.setRemark("现金退款:" + order.getRefundMoney());

        back.setResultData("线下现金退款总金额：" + order.getRefundMoney());
        orderPaymentItemService.insert(back);
    }

    @Override
    public JSONResult refundItem(Order refundOrder) {
        //修改菜品数量
        Order order = getOrderInfo(refundOrder.getId());
        int customerCount = 0;
        BigDecimal newServicePrice = order.getServicePrice(); //用来记录新版服务费在执行此次退菜前的服务费金额
        BigDecimal servicePrice = BigDecimal.ZERO;
        Brand brand = brandService.selectById(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        //定义退菜支付项集合
        List<OrderPaymentItem> refundPayment = new ArrayList<>();
        //保存使用到了产品券的菜品订单信息
        Map<String, BigDecimal> itemMap = new HashMap<>();
        for (OrderItem orderItem : refundOrder.getOrderItems()) {
            if (orderItem.getType().equals(ArticleType.ARTICLE)) { //退的是菜
                OrderItem item = orderitemMapper.selectByPrimaryKey(orderItem.getId());
                if (item.getCount() < orderItem.getCount()) {
                    throw new RuntimeException("退菜数量有误！");
                }
                orderitemMapper.refundArticle(orderItem.getId(), orderItem.getCount());
                if (refundOrder.getRefundRemark() != null) {
                    OrderRefundRemark orderRefundRemark = new OrderRefundRemark();
                    orderRefundRemark.setOrderId(order.getId());
                    orderRefundRemark.setArticleId(orderItemService.selectById(orderItem.getId()).getArticleId());
                    orderRefundRemark.setRefundRemarkId(refundOrder.getRefundRemark().getId());
                    orderRefundRemark.setRefundRemark(refundOrder.getRefundRemark().getName());
                    orderRefundRemark.setRemarkSupply(refundOrder.getRemarkSupply());
                    orderRefundRemark.setCreateTime(new Date());
                    orderRefundRemark.setRefundCount(orderItem.getCount());
                    orderRefundRemark.setShopId(order.getShopDetailId());
                    orderRefundRemark.setBrandId(order.getBrandId());
                    orderRefundRemarkMapper.insertSelective(orderRefundRemark);
                }
                //查询到当前菜品在当前订单中是否使用到产品券
                OrderPaymentItem useProductCoupons = orderPaymentItemService.selectUseProductCoupons(orderItem.getOrderId(), item.getArticleId());
                if (useProductCoupons != null) {
                    Coupon coupon = couponService.selectById(useProductCoupons.getToPayId());
                    if (coupon.getDeductionType().equals(Common.NO)) {
                        itemMap.put(orderItem.getId(), useProductCoupons.getPayValue());
                        //退还产品券
                        couponService.refundArticleCoupon(useProductCoupons.getToPayId());
                        //插入退还支付项
                        newPosRefundArticleAddPayment(useProductCoupons.getPayValue(), refundPayment, order, PayMode.COUPON_PAY, useProductCoupons.getToPayId(),true);
                    }
                }
                Map articleMap = new HashMap(4);
                articleMap.put("brandName", brand.getBrandName());
                articleMap.put("fileName", shopDetail.getName());
                articleMap.put("type", "posAction");
                articleMap.put("content", "订单:" + order.getId() + "在pos端执行退菜释放" + orderItem.getCount() + "份菜品(" + orderItem.getArticleName() + "),请求服务器地址为:" + MQSetting.getLocalIP());
                Map orderArticleMap = new HashMap(4);
                orderArticleMap.put("brandName", brand.getBrandName());
                orderArticleMap.put("fileName", order.getId());
                orderArticleMap.put("type", "orderAction");
                orderArticleMap.put("content", "订单:" + order.getId() + "退菜释放" + orderItem.getCount() + "份菜品(" + orderItem.getArticleName() + "),请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(orderArticleMap);
                if (item.getType() == OrderItemType.SETMEALS) {
                    //如果退了套餐，清空子品
                    orderitemMapper.refundArticleChild(orderItem.getId());
                    Map articleFamilyMap = new HashMap(4);
                    articleFamilyMap.put("brandName", brand.getBrandName());
                    articleFamilyMap.put("fileName", shopDetail.getName());
                    articleFamilyMap.put("type", "posAction");
                    articleFamilyMap.put("content", "订单:" + order.getId() + "在pos端执行退菜释放" + orderItem.getCount() + "份套餐(" + orderItem.getArticleName() + ")下的子品项,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(articleFamilyMap);
                    Map orderArticleFamilyMap = new HashMap(4);
                    orderArticleFamilyMap.put("brandName", brand.getBrandName());
                    orderArticleFamilyMap.put("fileName", order.getId());
                    orderArticleFamilyMap.put("type", "orderAction");
                    orderArticleFamilyMap.put("content", "订单:" + order.getId() + "退菜释放" + orderItem.getCount() + "份套餐(" + orderItem.getArticleName() + ")下的子品项,请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(orderArticleFamilyMap);
                }

            } else if (orderItem.getType().equals(ArticleType.SERVICE_PRICE)) { //退的是老版服务费
                customerCount = order.getCustomerCount() - orderItem.getCount();
                servicePrice = order.getServicePrice().divide(new BigDecimal(order.getCustomerCount())).multiply(new BigDecimal(customerCount));
                orderMapper.refundServicePrice(order.getId(), servicePrice, customerCount);
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", shopDetail.getName());
                map.put("type", "posAction");
                map.put("content", "订单:" + order.getId() + "在pos端执行退菜退了" + orderItem.getCount() + "份服务费(" + shopDetail.getServiceName() + "),请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
                Map orderMap = new HashMap(4);
                orderMap.put("brandName", brand.getBrandName());
                orderMap.put("fileName", order.getId());
                orderMap.put("type", "orderAction");
                orderMap.put("content", "订单:" + order.getId() + "退菜退了" + orderItem.getCount() + "份服务费(" + shopDetail.getServiceName() + "),请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(orderMap);
            } else if (orderItem.getType().equals(ArticleType.SAUCE_FEE_PRICE)) { //餐具费
                Order serviceOrder = new Order();
                Integer refundCount = orderItem.getCount(); //退掉的数量
                BigDecimal refundPrice = new BigDecimal(refundCount).multiply(orderItem.getUnitPrice()); //退掉的金额
                serviceOrder.setId(refundOrder.getId());
                serviceOrder.setSauceFeeCount(order.getSauceFeeCount() - refundCount); //减去订单中的餐具数量
                serviceOrder.setSauceFeePrice(order.getSauceFeePrice().subtract(refundPrice)); //减去订单中的餐具费
                serviceOrder.setServicePrice(newServicePrice.subtract(refundPrice));
                newServicePrice = newServicePrice.subtract(refundPrice); //将本次退菜前的服务费在每次退菜后重新赋值(减去本次退掉的服务费)
                update(serviceOrder);
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", shopDetail.getName());
                map.put("type", "posAction");
                map.put("content", "订单:" + order.getId() + "在pos端执行退菜退了" + orderItem.getCount() + "份餐具费(" + shopDetail.getServiceName() + "),请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
                Map orderMap = new HashMap(4);
                orderMap.put("brandName", brand.getBrandName());
                orderMap.put("fileName", order.getId());
                orderMap.put("type", "orderAction");
                orderMap.put("content", "订单:" + order.getId() + "退菜退了" + orderItem.getCount() + "份餐具费(" + shopDetail.getServiceName() + "),请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(orderMap);
            } else if (orderItem.getType().equals(ArticleType.TOWEL_FEE_PRICE)) { //纸巾费
                Order serviceOrder = new Order();
                Integer refundCount = orderItem.getCount(); //退掉的数量
                BigDecimal refundPrice = new BigDecimal(refundCount).multiply(orderItem.getUnitPrice()); //退掉的金额
                serviceOrder.setId(order.getId());
                serviceOrder.setTowelFeeCount(order.getTowelFeeCount() - refundCount);//减去订单中的纸巾数量
                serviceOrder.setTowelFeePrice(order.getTowelFeePrice().subtract(refundPrice));//减去订单中的纸巾费
                serviceOrder.setServicePrice(newServicePrice.subtract(refundPrice));
                newServicePrice = newServicePrice.subtract(refundPrice); //将本次退菜前的服务费在每次退菜后重新赋值(减去本次退掉的服务费)
                update(serviceOrder);
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", shopDetail.getName());
                map.put("type", "posAction");
                map.put("content", "订单:" + order.getId() + "在pos端执行退菜退了" + orderItem.getCount() + "份纸巾费(" + shopDetail.getTowelFeeName() + "),请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
                Map orderMap = new HashMap(4);
                orderMap.put("brandName", brand.getBrandName());
                orderMap.put("fileName", order.getId());
                orderMap.put("type", "orderAction");
                orderMap.put("content", "订单:" + order.getId() + "退菜退了" + orderItem.getCount() + "份纸巾费(" + shopDetail.getTowelFeeName() + "),请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(orderMap);
            } else if (orderItem.getType().equals(ArticleType.TABLEWARE_FEE_PRICE)) { //酱料费
                Order serviceOrder = new Order();
                Integer refundCount = orderItem.getCount(); //退掉的数量
                BigDecimal refundPrice = new BigDecimal(refundCount).multiply(orderItem.getUnitPrice()); //退掉的金额
                serviceOrder.setId(order.getId());
                serviceOrder.setTablewareFeeCount(order.getTablewareFeeCount() - refundCount);//减去订单中的酱料数量
                serviceOrder.setTablewareFeePrice(order.getTablewareFeePrice().subtract(refundPrice));//减去订单中的酱料费
                serviceOrder.setServicePrice(newServicePrice.subtract(refundPrice));
                newServicePrice = newServicePrice.subtract(refundPrice); //将本次退菜前的服务费在每次退菜后重新赋值(减去本次退掉的服务费)
                update(serviceOrder);
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", shopDetail.getName());
                map.put("type", "posAction");
                map.put("content", "订单:" + order.getId() + "在pos端执行退菜退了" + orderItem.getCount() + "份酱料费(" + shopDetail.getTablewareFeeName() + "),请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
                Map orderMap = new HashMap(4);
                orderMap.put("brandName", brand.getBrandName());
                orderMap.put("fileName", order.getId());
                orderMap.put("type", "orderAction");
                orderMap.put("content", "订单:" + order.getId() + "退菜退了" + orderItem.getCount() + "份酱料费(" + shopDetail.getTablewareFeeName() + "),请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(orderMap);
            }
        }

        //修改支付项
        Map<String, BigDecimal> orders = new HashMap<>();//存放订单跟该笔订单所退掉的钱
        List<OrderItem> orderItems = refundOrder.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getType().equals(ArticleType.SERVICE_PRICE)
                    || orderItem.getType().equals(ArticleType.SAUCE_FEE_PRICE)
                    || orderItem.getType().equals(ArticleType.TOWEL_FEE_PRICE)
                    || orderItem.getType().equals(ArticleType.TABLEWARE_FEE_PRICE)) { //新老版服务费
                BigDecimal itemValue = new BigDecimal(orderItem.getCount()).multiply(orderItem.getUnitPrice());//退掉的新老服务费费用
                if (orders.containsKey(orderItem.getOrderId())) {
                    orders.put(orderItem.getOrderId(), orders.get(orderItem.getOrderId()).add(itemValue));//累加退掉的钱
                } else {
                    redisService.set(orderItem.getOrderId() + "ItemCount", 0);
                    orders.put(orderItem.getOrderId(), itemValue);//存储这个订单退了多少钱
                }
            } else {
                BigDecimal extraPrice = orderItem.getExtraPrice() != null ? orderItem.getExtraPrice() : BigDecimal.ZERO;
                BigDecimal itemValue = BigDecimal.valueOf(orderItem.getCount()).multiply(orderItem.getUnitPrice()).add(extraPrice);
                if (orders.containsKey(orderItem.getOrderId())) {
                    orders.put(orderItem.getOrderId(), orders.get(orderItem.getOrderId()).add(itemValue));
                    redisService.set(orderItem.getOrderId() + "ItemCount", Integer.parseInt(redisService.get(orderItem.getOrderId() + "ItemCount").toString()) + orderItem.getCount());
                } else {
                    redisService.set(orderItem.getOrderId() + "ItemCount", orderItem.getCount());
                    orders.put(orderItem.getOrderId(), itemValue);
                }
            }
        }
        if (refundOrder.getPosRefundArticleType() != null) {
            //pos2.0退菜
            BigDecimal refundMoney = BigDecimal.ZERO;
            for (Map.Entry<String, BigDecimal> map : orders.entrySet()){
                refundMoney = refundMoney.add(map.getValue());
            }
            if (itemMap.size() != 0) {
                for (Map.Entry<String, BigDecimal> map : itemMap.entrySet()) {
                    refundMoney = refundMoney.subtract(map.getValue());
                }
            }
            refundOrder.setRefundMoney(refundMoney);
            refundPayment.addAll(refundPosTwoArticle(refundOrder));
            refundOrder.setRefundPaymentList(refundPayment);
        }
        for (String id : orders.keySet()) {
            Order o = new Order();
            o.setId(id);
            o.setRefundMoney(orders.get(id)); //退掉的金额
            o.setOrderItems(refundOrder.getOrderItems()); //退菜明细
            o.setRefundType(refundOrder.getRefundType()); //退款方式
            //新增的更新newPos向线上的同步时间
            o.setSyncState(refundOrder.getSyncState());
            o.setLastSyncTime(refundOrder.getLastSyncTime());
            if (!refundOrder.getId().equalsIgnoreCase(id)) {
                //退的是子订单的菜
                o.setParentOrderId(refundOrder.getId());
            }
            if (refundOrder.getPosRefundArticleType() == null){
                //老版pos退菜
                refundArticle(o); //去退款
            }
            updateArticle(o); //去修改订单项
        }
        //退完菜后重新计算菜品金额
        refundArticleUpdateActual(order);
        JSONResult result = new JSONResult(order.getId());
        return result;
    }

    /**
     * 退菜时重新计算菜品实收
     * @param order
     */
    private void refundArticleUpdateActual(Order order) {
        //修改逻辑，由于无论什么店铺模式退菜。退菜所产生的支付项都是放在主订单上，所以父子订单的菜品实收在退菜后一起计算
        orderItemService.updateOrderItemActualAmount(order, true);
    }

    /**
     * newPos新版逻辑退款
     *
     * @param refundOrder
     * @return
     */
    public List<OrderPaymentItem> refundPosTwoArticle(Order refundOrder) {
        //查找到主订单的订单信息
        Order order = selectById(refundOrder.getId());
        //存储当前还需要退多少金额
        BigDecimal surplusRefundMoney = refundOrder.getRefundMoney();
        //存放退菜是的退款记录
        List<OrderPaymentItem> refundPaymentList = new ArrayList<>();
        //得到实际支付已到账的父子订单的微信支付金额总和
        BigDecimal onLineWxPayTotal = orderMapper.selectSurplusAmountByPayMode(order.getId(), PayMode.WEIXIN_PAY, Common.YES);
        //得到实际支付已到账的父子订单的支付宝支付金额总和
        BigDecimal onLineAliPayTotal = orderMapper.selectSurplusAmountByPayMode(order.getId(), PayMode.ALI_PAY, Common.YES);
        //得到实际支付未到账的父子订单的微信支付金额总和
        BigDecimal offLineWxPayTotal = orderMapper.selectSurplusAmountByPayMode(order.getId(), PayMode.WEIXIN_PAY, Common.NO);
        //得到实际支付未到账的父子订单的支付宝支付金额总和
        BigDecimal offLineAliPayTotal = orderMapper.selectSurplusAmountByPayMode(order.getId(), PayMode.ALI_PAY, Common.NO);
        //得到其余实际支付未到账的父子订单的金额总和
        BigDecimal otherPayTotal = orderMapper.selectSurplusAmountByPayMode(order.getId(), 3, Common.NO);
        //此次线上微信退款金额
        BigDecimal onLineWxRefundValue = onLineWxPayTotal.compareTo(surplusRefundMoney) >= 0 ? surplusRefundMoney : onLineWxPayTotal;
        surplusRefundMoney = surplusRefundMoney.subtract(onLineWxRefundValue);
        //此次线上支付宝退款金额
        BigDecimal onLineAliRefundValue = onLineAliPayTotal.compareTo(surplusRefundMoney) >= 0 ? surplusRefundMoney : onLineAliPayTotal;
        surplusRefundMoney = surplusRefundMoney.subtract(onLineAliRefundValue);
        //此次线下微信退款金额
        BigDecimal offLineWxRefundValue = offLineWxPayTotal.compareTo(surplusRefundMoney) >= 0 ? surplusRefundMoney : offLineWxPayTotal;
        surplusRefundMoney = surplusRefundMoney.subtract(offLineWxRefundValue);
        //此次线下支付宝退款金额
        BigDecimal offLineAliRefundValue = offLineAliPayTotal.compareTo(surplusRefundMoney) >= 0 ? surplusRefundMoney : offLineAliPayTotal;
        surplusRefundMoney = surplusRefundMoney.subtract(offLineAliRefundValue);
        //此次现金退款的金额
        BigDecimal cashRefundValue = otherPayTotal.compareTo(surplusRefundMoney) >= 0 ? surplusRefundMoney : otherPayTotal;
        //如果剩余还有可退金额已退菜红包的形式返还
        surplusRefundMoney = surplusRefundMoney.subtract(cashRefundValue);
        //有线上微信可退的
        if (onLineWxRefundValue.compareTo(BigDecimal.ZERO) > 0) {
            refundActualByMode(order, onLineWxRefundValue, PayMode.WEIXIN_PAY, refundPaymentList);
        }
        //有线上支付宝可退的
        if (onLineAliRefundValue.compareTo(BigDecimal.ZERO) > 0) {
            refundActualByMode(order, onLineAliRefundValue, PayMode.ALI_PAY, refundPaymentList);
        }
        //有线下微信可退的
        if (offLineWxRefundValue.compareTo(BigDecimal.ZERO) > 0) {
            //插入支付项
            newPosRefundArticleAddPayment(offLineWxRefundValue, refundPaymentList, order, PayMode.WEIXIN_PAY, null, true);
        }
        //有线下支付宝可退的
        if (offLineAliRefundValue.compareTo(BigDecimal.ZERO) > 0) {
            //插入支付项
            newPosRefundArticleAddPayment(offLineAliRefundValue, refundPaymentList, order, PayMode.ALI_PAY, null, true);
        }
        //线下退还现金
        if (cashRefundValue.compareTo(BigDecimal.ZERO) > 0) {
            //插入支付项
            cashRefund(cashRefundValue, refundPaymentList, order);
        }
        //退菜红包返还
        if (surplusRefundMoney.compareTo(BigDecimal.ZERO) > 0) {
            //插入支付项
            newPosRefundArticleAddPayment(surplusRefundMoney, refundPaymentList, order, PayMode.ARTICLE_BACK_PAY, null, true);
            //修改账户余额
            Customer customer = customerService.selectById(order.getCustomerId());
            accountService.addAccount(surplusRefundMoney, customer.getAccountId(), "退菜红包", AccountLog.REFUND_ARTICLE_RED_PACKAGE, order.getShopDetailId());
            //封装红包信息
            RedPacket redPacket = new RedPacket();
            redPacket.setId(ApplicationUtils.randomUUID());
            redPacket.setRedMoney(surplusRefundMoney);
            redPacket.setCreateTime(new Date());
            redPacket.setCustomerId(customer.getId());
            redPacket.setBrandId(order.getBrandId());
            redPacket.setShopDetailId(order.getShopDetailId());
            redPacket.setRedRemainderMoney(surplusRefundMoney);
            redPacket.setRedType(RedType.REFUND_ARTICLE_RED);
            redPacket.setOrderId(order.getId());
            redPacketService.insert(redPacket);
        }
        return refundPaymentList;
    }

    /**
     * 现金退款
     * @param refundMoney
     * @param refundPaymentList
     * @param order
     */
    private void cashRefund(BigDecimal refundMoney, List<OrderPaymentItem> refundPaymentList, Order order) {
        //当次退款集合
        List<OrderPaymentItem> cashRefundPaymentList = new ArrayList<>();
        //当次退款金额之和
        AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);
        List<OrderPaymentItem> paymentItemList = orderMapper.selectCashRefundInfo(order.getId());
        //支付项金额之和
        BigDecimal payMoney = paymentItemList.stream().map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        //按比例计算现金退款金额
        paymentItemList.forEach(orderPaymentItem -> {
            BigDecimal discount = orderPaymentItem.getPayValue().divide(payMoney, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal nowRefundMoney = refundMoney.multiply(discount).setScale(2, BigDecimal.ROUND_DOWN);
            //标识当前退款金额的来源
            order.setRefundSourceId(orderPaymentItem.getId());
            newPosRefundArticleAddPayment(nowRefundMoney, cashRefundPaymentList, order, PayMode.REFUND_CRASH, null, false);
            total.updateAndGet(bigDecimal -> bigDecimal.add(nowRefundMoney));
        });
        //检查退款金额是否有差值
        BigDecimal refundTotal = total.get();
        BigDecimal difference = refundMoney.subtract(refundTotal);
        cashRefundPaymentList.stream().findAny().ifPresent(orderPaymentItem -> {
            orderPaymentItem.setPayValue((orderPaymentItem.getPayValue().abs().add(difference)).multiply(new BigDecimal(-1)));
        });
        //最终插入数据
        cashRefundPaymentList.forEach(orderPaymentItem -> {
            orderPaymentItemService.insert(orderPaymentItem);
        });
        refundPaymentList.addAll(cashRefundPaymentList);
    }


    /**
     * NewPos退菜插入支付项
     *
     * @param refundMoney
     * @param refundPaymentList
     * @param order
     * @param payMode
     */
    private void newPosRefundArticleAddPayment(BigDecimal refundMoney, List<OrderPaymentItem> refundPaymentList, Order order
            , Integer payMode, String toPayId, boolean isInsert) {
        OrderPaymentItem refundPaymentItem = new OrderPaymentItem();
        refundPaymentItem.setId(ApplicationUtils.randomUUID());
        refundPaymentItem.setPayValue(refundMoney.multiply(new BigDecimal(-1)));
        refundPaymentItem.setRemark(PayMode.getRefundPayModeName(payMode) + refundPaymentItem.getPayValue());
        refundPaymentItem.setPaymentModeId(payMode);
        refundPaymentItem.setPayTime(new Date());
        refundPaymentItem.setOrderId(order.getId());
        refundPaymentItem.setToPayId(toPayId);
        if (payMode == PayMode.REFUND_CRASH) {
            refundPaymentItem.setRefundSourceId(order.getRefundSourceId());
        }
        if (isInsert) {
            orderPaymentItemService.insert(refundPaymentItem);
        }
        refundPaymentList.add(refundPaymentItem);
    }

    /**
     * newPos微信、支付宝退款
     *
     * @param order
     * @param refundValue
     * @param payMode
     * @param refundPaymentList
     */
    public void refundActualByMode(Order order, BigDecimal refundValue, Integer payMode, List<OrderPaymentItem> refundPaymentList) {
        //得到所有父子订单实际支付的记录
        List<OrderPaymentItem> payList = orderPaymentItemService.selectPayMentByPayMode(order.getId(), payMode, Common.YES);
        //得到所有父子订单实际支付的退款记录
        List<OrderPaymentItem> refundList = orderPaymentItemService.selectPayMentByPayMode(order.getId(), payMode, Common.NO);
        //退款的实体
        OrderPaymentItem refundPayment;
        //循环支付记录
        for (OrderPaymentItem paymentItem : payList) {
            //还有未退款金额，继续退款
            if (refundValue.compareTo(BigDecimal.ZERO) > 0) {
                JSONObject payInfo = new JSONObject(paymentItem.getResultData());
                //记录当前支付记录可退款金额
                BigDecimal remainPayMent = paymentItem.getPayValue();
                //循环退款记录
                for (OrderPaymentItem refundPayMentItem : refundList) {
                    //得到当前退款的回调信息
                    JSONObject refundInfo = new JSONObject(refundPayMentItem.getResultData());
                    if (payMode.equals(PayMode.WEIXIN_PAY)) {
                        //微信支付
                        if (paymentItem.getId().equalsIgnoreCase(refundInfo.getString("transaction_id"))) {
                            //减去此次退款的金额
                            remainPayMent = remainPayMent.add(refundPayMentItem.getPayValue());
                        }
                    } else {
                        //支付宝支付
                        JSONObject aliPayRefundInfo = refundInfo.getJSONObject("alipay_trade_refund_response");
                        if (paymentItem.getId().equalsIgnoreCase(aliPayRefundInfo.getString("trade_no"))) {
                            //减去此次退款的金额
                            remainPayMent = remainPayMent.add(refundPayMentItem.getPayValue());
                        }
                    }
                }
                //发现该笔支付还有可退金额
                if (remainPayMent.compareTo(BigDecimal.ZERO) > 0) {
                    //当前可退款金额
                    BigDecimal refund = refundValue.compareTo(remainPayMent) >= 0 ? remainPayMent : refundValue;
                    ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
                    refundPayment = new OrderPaymentItem();
                    refundPayment.setId(ApplicationUtils.randomUUID());
                    refundPayment.setPayValue(refund.multiply(new BigDecimal(-1)));
                    refundPayment.setPaymentModeId(payMode);
                    refundPayment.setOrderId(order.getId());
                    refundPayment.setPayTime(new Date());
                    if (payMode.equals(PayMode.WEIXIN_PAY)) {
                        WechatConfig config = wechatConfigService.selectByBrandId(DataSourceContextHolder.getDataSourceName());
                        Map<String, String> result;
                        //微信支付
                        if (shopDetail.getWxServerId() == null) {
                            //独立商户模式
                            String payCertPath = StringUtils.isEmpty(shopDetail.getPayCertPath()) ? config.getPayCertPath() : shopDetail.getPayCertPath();
                            if (StringUtils.isBlank(payCertPath)) {
                                if (shopDetail.getShopMode().equals(ShopMode.MEISHI)){
                                    //美食广场模式下无法退款则整单退款失败
                                    throw new RuntimeException("微信退款失败，无退款证书。请配置后从新发起！");
                                }
                                log.error("微信退款失败！失败信息：无退款证书");
                                refundPayment.setRemark("微信退还金额：" + refund + "失败(无退款证书)，以线下退还现金的形式返还");
                                refundPayment.setPaymentModeId(PayMode.REFUND_CRASH);
                                refundPayment.setRefundSourceId(paymentItem.getId());
                                orderPaymentItemService.insert(refundPayment);
                                refundPaymentList.add(refundPayment);
                                refundValue = refundValue.subtract(refund);
                                continue;
                            }
                            result = WeChatPayUtils.refund(refundPayment.getId(), payInfo.getString("transaction_id"), payInfo.getInt("total_fee")
                                    , refund.multiply(new BigDecimal(100)).intValue(), StringUtils.isEmpty(shopDetail.getAppid()) ? config.getAppid() : shopDetail.getAppid(),
                                    StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(),
                                    StringUtils.isEmpty(shopDetail.getMchkey()) ? config.getMchkey() : shopDetail.getMchkey(),
                                    payCertPath);
                        } else {
                            //服务商模式
                            WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());
                            result = WeChatPayUtils.refundNew(refundPayment.getId(), payInfo.getString("transaction_id"),
                                    payInfo.getInt("total_fee"), refund.multiply(new BigDecimal(100)).intValue(), wxServerConfig.getAppid(), wxServerConfig.getMchid(),
                                    StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(), wxServerConfig.getMchkey(), wxServerConfig.getPayCertPath());
                        }
                        if (result.containsKey("ERROR")) {
                            if (shopDetail.getShopMode().equals(ShopMode.MEISHI)){
                                //美食广场模式下无法退款则整单退款失败
                                throw new RuntimeException("微信退款失败，" + result.get("ERROR_MSG"));
                            }
                            log.error("微信退款失败！失败信息：" + new JSONObject(result).toString());
                            String remark = result.get("ERROR_MSG");
                            refundPayment.setRemark("微信退还金额：" + refund + "失败(" + remark + ")，以线下退还现金的形式返还");
                            refundPayment.setPaymentModeId(PayMode.REFUND_CRASH);
                            refundPayment.setRefundSourceId(paymentItem.getId());
                        } else {
                            refundPayment.setResultData(JSON.toJSONString(result));
                            refundPayment.setRemark("微信退款：" + refundPayment.getPayValue());
                        }
                    } else {
                        BrandSetting brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
                        //支付宝支付
                        AliPayUtils.connection(StringUtils.isNotBlank(shopDetail.getAliAppId()) ? shopDetail.getAliAppId() : brandSetting.getAliAppId(),
                                StringUtils.isNotBlank(shopDetail.getAliPrivateKey()) ? shopDetail.getAliPrivateKey() : brandSetting.getAliPrivateKey(),
                                StringUtils.isNotBlank(shopDetail.getAliPublicKey()) ? shopDetail.getAliPublicKey() : brandSetting.getAliPublicKey(),
                                shopDetail.getAliEncrypt());
                        Map map = new HashMap();
                        map.put("trade_no", paymentItem.getId());
                        map.put("refund_amount", refund);
                        map.put("out_request_no", refundPayment.getId());
                        Map<String, String> returnMap = AliPayUtils.refundPay(map);
                        //判断支付宝此次退款是否成功
                        if (returnMap.get("success").equalsIgnoreCase("false")) {
                            if (shopDetail.getShopMode().equals(ShopMode.MEISHI)){
                                //美食广场模式下无法退款则整单退款失败
                                throw new RuntimeException("支付宝退款失败，" + returnMap.get("errorMsg"));
                            }
                            log.error("支付宝退款失败！失败信息：" + returnMap.toString());
                            refundPayment.setPaymentModeId(PayMode.REFUND_CRASH);
                            refundPayment.setRemark("支付宝退还金额：" + refund + "失败("+ returnMap.get("errorMsg") +")，以线下退还现金的形式返还");
                            refundPayment.setRefundSourceId(paymentItem.getId());
                        } else {
                            refundPayment.setResultData(returnMap.get("returnMsg"));
                            refundPayment.setRemark("支付宝退款：" + refundPayment.getPayValue());
                        }
                    }
                    orderPaymentItemService.insert(refundPayment);
                    refundPaymentList.add(refundPayment);
                    refundValue = refundValue.subtract(refund);
                }
            }
        }
    }


    @Override
    public JSONResult afterPay(String orderId, String couponId, BigDecimal price, BigDecimal pay, BigDecimal waitMoney, Integer payMode, String customerId) {
        JSONResult jsonResult = new JSONResult();

        log.info(">>>>>>>>>>>>>>>>>>开始进入afterPay方法,同步机制>>>>>>>>>>>>>>>>>>>>");

        Order order = selectById(orderId);
        if (order.getPrintTimes() == 1) {
            return null;
        }

        if(!order.getCustomerId().equals(customerId)){
            order.setCustomerId(customerId);
            orderMapper.updateByPrimaryKeySelective(order);
        }

        if (order.getPayType() == PayType.NOPAY && "sb".equals(order.getOperatorId()) && !order.getIsPay().equals(OrderPayState.ALIPAYING)) {
            order.setIsPay(OrderPayState.NOT_PAY);
            orderMapper.updateByPrimaryKeySelective(order);
        }
        Brand brand = brandService.selectById(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());

        Customer customer = customerService.selectById(order.getCustomerId());
        BigDecimal totalMoney = order.getAmountWithChildren().doubleValue() == 0.0 ? order.getOrderMoney() : order.getAmountWithChildren();
        try {
            if (!StringUtils.isEmpty(couponId)) { //使用了优惠券
                Boolean usedCouponBefore = couponService.usedCouponBeforeByOrderId(orderId).size() > 0;
                if (!usedCouponBefore) {
                    order.setUseCoupon(couponId);
                    Coupon coupon = couponService.useCoupon(totalMoney, order);
                    OrderPaymentItem item = new OrderPaymentItem();
                    item.setId(ApplicationUtils.randomUUID());
                    item.setOrderId(orderId);
                    item.setPaymentModeId(PayMode.COUPON_PAY);
                    item.setPayTime(new Date());
                    item.setPayValue(coupon.getValue());
                    item.setRemark("优惠券支付:" + coupon.getValue());
                    price = price.subtract(item.getPayValue());
//                    item.setResultData(coupon.getId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
                    item.setToPayId(coupon.getId());
                    orderPaymentItemService.insert(item);
                } else {
//                    Coupon coupon = couponService.selectById(couponId);
//                    pay = pay.subtract(coupon.getValue());
//                    pay = pay.add(price);
//                    price = price.subtract(coupon.getValue());
                }
            }
            if (waitMoney.doubleValue() > 0) { //等位红包支付
                OrderPaymentItem item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(orderId);
                item.setPaymentModeId(PayMode.WAIT_MONEY);
                item.setPayTime(new Date());
                item.setPayValue(order.getWaitMoney());
                item.setRemark("等位红包支付:" + order.getWaitMoney());
//                item.setResultData(order.getWaitId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
                item.setToPayId(order.getWaitId());
                orderPaymentItemService.insert(item);
                GetNumber getNumber = getNumberService.selectById(order.getWaitId());
                getNumber.setState(WaitModerState.WAIT_MODEL_NUMBER_THREE);
                getNumberService.update(getNumber);
            }
            log.info("后付的情况下支付的余额为" + price);
            if (price.doubleValue() > 0) {  //余额支付
                accountService.payOrder(order, price, customer, brand, shopDetail);
            }
            log.info("后付的情况下还需支付" + pay);
            OrderPaymentItem item = new OrderPaymentItem();
            if (pay.doubleValue() > 0) { //还需要支付

                order.setPayMode(payMode);
                switch (payMode) {
                    case OrderPayMode.WX_PAY:
                        order.setPaymentAmount(pay);
//                        order.setPrintTimes(1);
                        break;
                    case OrderPayMode.ALI_PAY:
                        order.setPaymentAmount(pay);
                        order.setIsPay(OrderPayState.ALIPAYING);
                        break;
                    case OrderPayMode.YL_PAY:
                        order.setPaymentAmount(pay);
                        order.setOrderState(OrderState.SUBMIT);
                        order.setPrintTimes(1);
                        order.setAllowContinueOrder(false);
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(orderId);
                        item.setPaymentModeId(PayMode.BANK_CART_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(pay);
                        item.setRemark("银联支付:" + item.getPayValue());
                        orderPaymentItemService.insert(item);
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", order.getCustomerId());
                        map.put("type", "UserAction");
                        map.put("content", "用户:" + order.getCustomerId() + "发起银联支付：" + item.getPayValue() + ",订单号为：" + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                        break;
                    case OrderPayMode.XJ_PAY:
                        order.setPaymentAmount(pay);
                        order.setOrderState(OrderState.SUBMIT);
                        order.setPrintTimes(1);
                        order.setAllowContinueOrder(false);
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(orderId);
                        item.setPaymentModeId(PayMode.CRASH_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(pay);
                        item.setRemark("现金支付:" + item.getPayValue());
                        orderPaymentItemService.insert(item);
                        Map map1 = new HashMap(4);
                        map1.put("brandName", brand.getBrandName());
                        map1.put("fileName", order.getCustomerId());
                        map1.put("type", "UserAction");
                        map1.put("content", "用户:" + order.getCustomerId() + "发起现金支付：" + item.getPayValue() + ",订单号为：" + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map1);
                        break;
                    case OrderPayMode.JF_PAY:
                        order.setPaymentAmount(pay);
                        order.setOrderState(OrderState.SUBMIT);
                        order.setPrintTimes(1);
                        order.setAllowContinueOrder(false);
                        item.setId(ApplicationUtils.randomUUID());
                        item.setOrderId(orderId);
                        item.setPaymentModeId(PayMode.INTEGRAL_PAY);
                        item.setPayTime(new Date());
                        item.setPayValue(pay);
                        item.setRemark("会员支付:" + item.getPayValue());
                        orderPaymentItemService.insert(item);
                    default:
                        break;

                }
                update(order);

            } else { //支付完成
                List<OrderPaymentItem> items = orderPaymentItemService.selectByOrderId(order.getId());
                BigDecimal sum = new BigDecimal(0);
                for (OrderPaymentItem orderPaymentItem : items) {
                    sum = sum.add(orderPaymentItem.getPayValue());
                }
                if (order.getAmountWithChildren().doubleValue() > 0 && sum.doubleValue() < order.getAmountWithChildren().doubleValue()) {
                    throw new RuntimeException("支付异常,支付金额小于订单金额");
                }
                if (order.getAmountWithChildren().doubleValue() <= 0 && sum.doubleValue() < order.getOrderMoney().doubleValue()) {
                    throw new RuntimeException("支付异常,支付金额小于订单金额");
                }
                if (order.getOrderState() < OrderState.PAYMENT) {
                    order.setOrderState(OrderState.PAYMENT);
                    order.setAllowCancel(false);
                    order.setPrintTimes(1);
                    order.setPaymentAmount(BigDecimal.valueOf(0));
                    order.setPayMode(OrderPayMode.YUE_PAY);
                    update(order);
                    updateChild(order);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        //发放消费返利优惠券
        couponService.addConsumptionRebateCoupon(order);
        log.info(">>>>>>>>>>>>>>>>>>>>>>开始进入afterPay方法--->同步机制,开始进入支付同步机制方法>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        jsonResult.setSuccess(true);
        jsonResult.setData(order);
        return jsonResult;

    }


    private void updateChild(Order order) {
        List<Order> orders = orderMapper.selectByParentId(order.getId(), order.getPayType());
        for (Order child : orders) {
            if (child.getOrderState() < OrderState.PAYMENT) {
                child.setOrderState(OrderState.PAYMENT);
                child.setPaymentAmount(BigDecimal.valueOf(0));
                child.setAllowCancel(false);
                child.setAllowContinueOrder(false);
                update(child);
            }
        }
    }

    @Override
    public boolean checkOrder(Order order) {
        List<OrderPaymentItem> payItemsList = orderPaymentItemService.selectByOrderId(order.getId());
        BigDecimal sum = new BigDecimal(0);
        for (OrderPaymentItem item : payItemsList) {
            if (item.getPaymentModeId() == PayMode.WEIXIN_PAY) {
                sum = sum.add(item.getPayValue());
            }
        }

        return order.getRefundMoney().doubleValue() <= sum.doubleValue();
    }


    @Override
    public void updateArticle(Order order) {
        BigDecimal total = new BigDecimal(0);
        BigDecimal origin = new BigDecimal(0);
        Order o = getOrderInfo(order.getId());
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(o.getShopDetailId());
        BrandSetting brandSetting = brandSettingService.selectByBrandId(o.getBrandId());
        int base = 0;
        int sum = 0;
        BigDecimal mealPrice = BigDecimal.valueOf(0);
        int mealCount = 0;
        BigDecimal mealTotalPrice = BigDecimal.valueOf(0);
        for (OrderItem item : o.getOrderItems()) {
            origin = origin.add(item.getOriginalPrice().multiply(new BigDecimal(item.getCount())));
            if (item.getCount() > 0) {
                total = total.add(item.getFinalPrice());
            }
            if (o.getDistributionModeId() == DistributionType.TAKE_IT_SELF && brandSetting.getIsMealFee().equals(Common.YES) && shopDetail.getIsMealFee().equals(Common.YES)) {
                mealPrice = shopDetail.getMealFeePrice().multiply(new BigDecimal(item.getCount())).multiply(new BigDecimal(item.getMealFeeNumber())).setScale(2, BigDecimal.ROUND_HALF_UP);
                mealTotalPrice = mealTotalPrice.add(mealPrice);
                mealCount += item.getCount() * item.getMealFeeNumber();
                total = total.add(mealPrice);
            }
            if (item.getRefundCount() > 0 && item.getType() != OrderItemType.MEALS_CHILDREN) {
                sum += item.getRefundCount();
                sum += item.getRefundCount();
            }
            if (item.getType() != OrderItemType.MEALS_CHILDREN) {
                base += item.getOrginCount();
            }
        }

        if (o.getServicePrice() == null) {
            o.setServicePrice(new BigDecimal(0));
        }
        o.setMealFeePrice(mealTotalPrice);
        o.setMealAllNumber(mealCount);
        o.setArticleCount(o.getArticleCount() - Integer.parseInt(redisService.get(o.getId() + "ItemCount").toString()));
        o.setOriginalAmount(origin.add(o.getServicePrice()));
        o.setOrderMoney(total.add(o.getServicePrice()));
        if (o.getAmountWithChildren() != null && o.getAmountWithChildren().doubleValue() != 0.0) {
            o.setAmountWithChildren(o.getAmountWithChildren().subtract(order.getRefundMoney()));
            o.setCountWithChild(o.getCountWithChild() - Integer.parseInt(redisService.get(o.getId() + "ItemCount").toString()));
        }
        if (o.getParentOrderId() != null) {
            Order parent = selectById(o.getParentOrderId());
            parent.setAmountWithChildren(parent.getAmountWithChildren().subtract(order.getRefundMoney()));
            parent.setCountWithChild(parent.getCountWithChild() - Integer.parseInt(redisService.get(o.getId() + "ItemCount").toString()));
            update(parent);
            Map map = new HashMap(4);
            map.put("brandName", brandSetting.getBrandName());
            map.put("fileName", shopDetail.getName());
            map.put("type", "posAction");
            map.put("content", "订单:" + order.getId() + "在pos端执行退菜修改父订单:" + o.getParentOrderId() + "amountWithChildren字段的值为:" + parent.getAmountWithChildren().subtract(order.getRefundMoney()) + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
            Map orderMap = new HashMap(4);
            orderMap.put("brandName", brandSetting.getBrandName());
            orderMap.put("fileName", order.getId());
            orderMap.put("type", "orderAction");
            orderMap.put("content", "订单:" + order.getId() + "在pos端执行退菜修改父订单:" + o.getParentOrderId() + "amountWithChildren字段的值为:" + parent.getAmountWithChildren().subtract(order.getRefundMoney()) + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(orderMap);
        }
        update(o);
        Map map = new HashMap(4);
        map.put("brandName", brandSetting.getBrandName());
        map.put("fileName", shopDetail.getName());
        map.put("type", "posAction");
        map.put("content", "订单:" + order.getId() + "在pos端执行退菜修改订单项里的菜品数量,订单退掉的菜品数为:" + sum + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
//        Map orderMap = new HashMap(4);
//        orderMap.put("brandName", brandSetting.getBrandName());
//        orderMap.put("fileName", order.getId());
//        orderMap.put("type", "orderAction");
//        orderMap.put("content", "订单:" + order.getId() + "在pos端执行退菜修改订单项里的菜品数量,订单退掉的菜品数为:"+sum+",请求服务器地址为:" + MQSetting.getLocalIP());
//        doPostAnsc(url, orderMap);
        LogTemplateUtils.getBackArticleByOrderType(brandSetting.getBrandName(), order.getId(), o.getOrderItems());
    }

    @Override
    public JSONResult refundArticleMsg(Order order) {
        JSONResult jsonResult = new JSONResult(order.getId());
        Order o = getOrderInfo(order.getId());
        if (o.getCustomerId() == null) {
            return jsonResult;
        }
        List<Customer> customerList = new ArrayList<>();
        if (o.getGroupId() != null && !"".equals(o.getGroupId())) {
            List<CustomerGroup> customerGroups = customerGroupService.getGroupByGroupId(o.getGroupId());
            for (CustomerGroup customerGroup : customerGroups) {
                Customer customer = customerService.selectById(customerGroup.getCustomerId());
                if (customer != null) {
                    customerList.add(customer);
                }

            }
        } else {
            Customer customer = customerService.selectById(o.getCustomerId());
            if (customer == null) {
                return jsonResult;
            }
            if (customer != null) {
                customerList.add(customer);
            }
        }
        if (customerList.size() > 0) {
            Brand brand = brandService.selectById(o.getBrandId());
            WechatConfig config = wechatConfigService.selectByBrandId(o.getBrandId());
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(o.getShopDetailId());
            BrandSetting setting = brandSettingService.selectById(brand.getBrandSettingId());
            for (Customer customer : customerList) {
                if (setting.getTemplateEdition() == 0) {
                    StringBuilder msg = new StringBuilder("亲，您");
                    msg.append(DateFormatUtils.format(o.getCreateTime(), "yyyy-MM-dd HH:mm")).append("的订单已完成退菜，相关款项")
                            .append("会在24小时内退还至您的微信账户，请注意查收！\n");
                    msg.append("订单编号:\n");
                    msg.append(o.getSerialNumber()).append("\n");
                    msg.append("桌号:").append(o.getTableNumber()).append("\n");
                    msg.append("店铺名:").append(shopDetail.getName()).append("\n");
                    msg.append("订单时间:").append(DateFormatUtils.format(o.getCreateTime(), "yyyy-MM-dd HH:mm")).append("\n");
                    //        msg.append("订单明细:").append("\n");
//                    BrandSetting brandSetting = brandSettingService.selectByBrandId(o.getBrandId());
                    //        if (o.getCustomerCount() != null && o.getCustomerCount() != 0) {
                    //            msg.append("\t").append(brandSetting.getServiceName()).append("X").append(o.getBaseCustomerCount()).append("\n");
                    //        }
                    //
                    //        List<OrderItem> totalItem = new ArrayList<>();
                    //        List<String> childs = orderMapper.selectChildIdsByParentId(o.getId());
                    //        if (!CollectionUtils.isEmpty(childs)) {
                    //            List<OrderItem> item = orderitemMapper.listByOrderIds(childs);
                    //            totalItem.addAll(item);
                    //        }
                    //        totalItem.addAll(o.getOrderItems());
                    //


                    //        for (OrderItem orderItem : totalItem) {
                    //
                    //
                    //            if (orderItem.getCount() != 0)
                    //                msg.append("\t").append(orderItem.getArticleName()).append("X").append(orderItem.getCount()).append("\n");
                    //        }
                    //        msg.append("订单金额:").append(o.getAmountWithChildren().doubleValue() != 0.0 ? o.getAmountWithChildren() : o.getOrderMoney()).append("\n");
                    msg.append("退菜明细:").append("\n");

                    //        if (o.getBaseCustomerCount() != null && o.getBaseCustomerCount() != o.getCustomerCount()) {
                    //            msg.append("\t").append(brandSetting.getServiceName()).append("X").append(o.getBaseCustomerCount() - o.getCustomerCount()).append("\n");
                    //        }
                    //        for (OrderItem orderItem : o.getOrderItems()) {
                    //            if (orderItem.getRefundCount() > 0) {
                    //                msg.append("\t").append(orderItem.getArticleName()).append("X").append(orderItem.getRefundCount()).append("\n");
                    //            }
                    //        }
                    for (OrderItem orderItem : order.getOrderItems()) {
                        if (orderItem.getType().equals(ArticleType.ARTICLE)) {
                            OrderItem item = orderitemMapper.selectByPrimaryKey(orderItem.getId());
                            msg.append("\t").append(item.getArticleName()).append("X").append(orderItem.getCount()).append("\n");
                            if (item.getType() == OrderItemType.SETMEALS) {
                                List<OrderItem> child = orderitemMapper.getListByParentId(item.getId());
                                for (OrderItem c : child) {
                                    //                childItem.setArticleName("|__" + childItem.getArticleName());
                                    msg.append("\t").append("|__").append(c.getArticleName()).append("X").append(c.getRefundCount()).append("\n");
                                }
                            }

                        } else if (orderItem.getType().equals(ArticleType.SERVICE_PRICE)) { //老版服务费
                            msg.append("\t").append(shopDetail.getServiceName()).append("X").append(orderItem.getCount()).append("\n");
                        } else if (orderItem.getType().equals(ArticleType.SAUCE_FEE_PRICE)) { //餐具费
                            msg.append("\t").append(shopDetail.getSauceFeeName()).append("X").append(orderItem.getCount()).append("\n");
                        } else if (orderItem.getType().equals(ArticleType.TOWEL_FEE_PRICE)) { //纸巾费
                            msg.append("\t").append(shopDetail.getTowelFeeName()).append("X").append(orderItem.getCount()).append("\n");
                        } else if (orderItem.getType().equals(ArticleType.TABLEWARE_FEE_PRICE)) { //酱料费
                            msg.append("\t").append(shopDetail.getTablewareFeeName()).append("X").append(orderItem.getCount()).append("\n");
                        }
                    }
                    msg.append("退菜金额:").append(order.getRefundMoney()).append("\n");
                    log.info("tttt" + msg.toString());
                    log.info("tt" + customer.getWechatId());
                    log.info("t" + config.getAppid());
                    weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                } else {
                    List<TemplateFlow> templateFlowList = templateService.selectTemplateId(config.getAppid(), "OPENTM203022210");
                    BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(), "OPENTM203022210", TemplateSytle.BACL_FOOT);
                    if (templateFlowList != null && templateFlowList.size() != 0) {
                        String templateId = templateFlowList.get(0).getTemplateId();
                        String jumpUrl = "";
                        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                        Map<String, Object> first = new HashMap<String, Object>();

                        if (brandTemplateEdit != null) {
                            if (brandTemplateEdit.getBigOpen()) {
                                first.put("value", brandTemplateEdit.getStartSign());
                            } else {
                                first.put("value", "您好，您的订单已完成退菜！");
                            }
                        } else {
                            first.put("value", "您好，您的订单已完成退菜！");
                        }

                        first.put("color", "#00DB00");
                        Map<String, Object> keyword1 = new HashMap<String, Object>();
                        keyword1.put("value", shopDetail.getName());
                        keyword1.put("color", "#000000");
                        Map<String, Object> keyword2 = new HashMap<String, Object>();
                        keyword2.put("value", o.getTableNumber());
                        keyword2.put("color", "#000000");
                        Map<String, Object> keyword3 = new HashMap<String, Object>();
                        StringBuffer msg = new StringBuffer();
                        for (int i = 0; i < (order.getOrderItems().size() > 5 ? 6 : order.getOrderItems().size()); i++) {
                            OrderItem orderItem1 = order.getOrderItems().get(i);
                            if (orderItem1.getType().equals(ArticleType.ARTICLE)) {
                                OrderItem item = orderitemMapper.selectByPrimaryKey(orderItem1.getId());
                                msg.append("\t").append(item.getArticleName()).append("×").append(orderItem1.getCount()).append("\n");
                                if (item.getType() == OrderItemType.SETMEALS) {
                                    List<OrderItem> child = orderitemMapper.getListByParentId(item.getId());
                                    for (OrderItem c : child) {
                                        //                childItem.setArticleName("|__" + childItem.getArticleName());
                                        msg.append("\t").append("|__").append(c.getArticleName()).append("×").append(c.getRefundCount()).append("\n");
                                    }
                                }

                            } else if (orderItem1.getType().equals(ArticleType.SERVICE_PRICE)) {
                                msg.append("\t").append(shopDetail.getServiceName()).append("×").append(orderItem1.getCount()).append("\n");
                            } else if (orderItem1.getType().equals(ArticleType.SAUCE_FEE_PRICE)) { //餐具费
                                msg.append("\t").append(shopDetail.getSauceFeeName()).append("X").append(orderItem1.getCount()).append("\n");
                            } else if (orderItem1.getType().equals(ArticleType.TOWEL_FEE_PRICE)) { //纸巾费
                                msg.append("\t").append(shopDetail.getTowelFeeName()).append("X").append(orderItem1.getCount()).append("\n");
                            } else if (orderItem1.getType().equals(ArticleType.TABLEWARE_FEE_PRICE)) { //酱料费
                                msg.append("\t").append(shopDetail.getTablewareFeeName()).append("X").append(orderItem1.getCount()).append("\n");
                            }
                            if (order.getOrderItems().size() > 5) {
                                msg.append("...");
                            }

                        }
                        keyword3.put("value", msg.toString());
                        keyword3.put("color", "#000000");
                        Map<String, Object> keyword4 = new HashMap<String, Object>();
                        keyword4.put("value", "共" + order.getOrderItems().size() + "份");
                        keyword4.put("color", "#000000");
                        Map<String, Object> keyword5 = new HashMap<String, Object>();
                        keyword5.put("value", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        keyword5.put("color", "#000000");
                        Map<String, Object> remark = new HashMap<String, Object>();

                        if (brandTemplateEdit != null) {
                            if (brandTemplateEdit.getBigOpen()) {
                                remark.put("value", brandTemplateEdit.getEndSign());
                            } else {
                                remark.put("value", "相关款项会在24小时内退还至您的账户，请注意查收！");
                            }
                        } else {
                            remark.put("value", "相关款项会在24小时内退还至您的账户，请注意查收！");
                        }

                        remark.put("color", "#173177");
                        content.put("first", first);
                        content.put("keyword1", keyword1);
                        content.put("keyword2", keyword2);
                        content.put("keyword3", keyword3);
                        content.put("keyword4", keyword4);
                        content.put("keyword5", keyword5);
                        content.put("remark", remark);
                        String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                        //发送短信
                        if (setting.getMessageSwitch() == 1) {
                            com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                            smsParam.put("number", order.getSerialNumber());
                            smsParam.put("name", shopDetail.getName());
                            smsParam.put("tablenumber", o.getTableNumber());
                            smsParam.put("count", "共" + order.getOrderItems().size() + "份");
                            com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(), smsParam, "餐加", "SMS_105745031");
                        }
                    } else {
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    }
                }
            }
        }
        return jsonResult;
    }

    @Override
    public List<Order> selectWXOrderItems(Map<String, Object> map) {
        return orderMapper.selectWXOrderItems(map);
    }


    /**
     * 会员管理
     * 1订单管理
     */
    @Override
    public List<Order> getCustomerOrderList(String customerId, String beginDate, String endDate) {
        return orderMapper.getCustomerOrderList(customerId, beginDate, endDate);
    }

    @Override
    public Integer selectByCustomerCount(String customerId) {
        return orderMapper.selectByCustomerCount(customerId);
    }

    @Override
    public List<Order> selectOrderByOrderIds(Map<String, Object> orderIds) {
        return orderMapper.selectOrderByOrderIds(orderIds);
    }

    @Override
    public List<Map<String, Object>> refundOrderPrintReceipt(Order refundOrder) {
        // 根据id查询订单
        List<Map<String, Object>> printTask = new ArrayList<>();
        Order order = selectById(refundOrder.getId());
        Integer oldDistributionModeId = order.getDistributionModeId();
        //如果是 未打印状态 或者  异常状态则改变 生产状态和打印时间
        if (ProductionStatus.HAS_ORDER == order.getProductionStatus() || ProductionStatus.NOT_PRINT == order.getProductionStatus()) {
            order.setProductionStatus(ProductionStatus.PRINTED);
            order.setPrintOrderTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        order.setBaseCustomerCount(0); //不知道为什么要加这句
        order.setRefundMoney(refundOrder.getRefundMoney());
        order.setDistributionModeId(DistributionType.REFUND_ORDER);
        //查询店铺
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        // 查询订单菜品
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> orderItemIds = new ArrayList<String>();
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for (OrderItem item : refundOrder.getOrderItems()) {
            if (item.getType().equals(ArticleType.ARTICLE)) { //是菜
                orderItemIds.add(item.getId());
            } else if (item.getType().equals(ArticleType.SAUCE_FEE_PRICE)) {//餐具费
//                order.setBaseCustomerCount(item.getCount());
//                order.setCustomerCount(0);
                //将餐具费封装成一个菜品
                OrderItem refundItem = new OrderItem();
                refundItem.setArticleName(shopDetail.getSauceFeeName());
                refundItem.setRefundCount(item.getCount());
                refundItem.setUnitPrice(item.getUnitPrice());
                refundItem.setType(10);
                orderItems.add(refundItem);
            } else if (item.getType().equals(ArticleType.TOWEL_FEE_PRICE)) {//纸巾费
                OrderItem refundItem = new OrderItem();
                refundItem.setArticleName(shopDetail.getTowelFeeName());
                refundItem.setRefundCount(item.getCount());
                refundItem.setUnitPrice(item.getUnitPrice());
                refundItem.setType(11);
                orderItems.add(refundItem);
            } else if (item.getType().equals(ArticleType.TABLEWARE_FEE_PRICE)) {//酱料费
                OrderItem refundItem = new OrderItem();
                refundItem.setArticleName(shopDetail.getTablewareFeeName());
                refundItem.setRefundCount(item.getCount());
                refundItem.setUnitPrice(item.getUnitPrice());
                refundItem.setType(12);
                orderItems.add(refundItem);
            }
        }
        map.put("orderItemIds", orderItemIds);
        if (orderItemIds.size() != 0) {
            orderItems = orderItemService.selectRefundOrderItem(map);
        }
        List<Printer> printer = new ArrayList<>();
        TableQrcode tableQrcode = tableQrcodeService.selectByTableNumberShopId(order.getShopDetailId(), Integer.valueOf(order.getTableNumber()));
        if (tableQrcode == null || order.getDistributionModeId() == DistributionType.TAKE_IT_SELF) {
            printer = printerService.selectByShopAndType(shopDetail.getId(), PrinterType.RECEPTION);
        } else {
            if (tableQrcode.getAreaId() == null) {
                printer = printerService.selectQiantai(shopDetail.getId(), PrinterRange.QIANTAI);
            } else {
                Area area = areaService.selectById(tableQrcode.getAreaId());
                printer = printerService.selectQiantai(shopDetail.getId(), PrinterRange.QIANTAI);
                if (area == null) {

                } else {
                    Printer p = printerService.selectById(area.getPrintId().intValue());
                    printer.add(p);
                }
            }
        }


        for (Printer p : printer) {
            Map<String, Object> ticket = refundOrderPrintTicket(order, oldDistributionModeId, orderItems, shopDetail, p);
            if (ticket != null) {
                printTask.add(ticket);
            }
        }
        Order newOrder = new Order();
        newOrder.setId(refundOrder.getId());
        newOrder.setDistributionModeId(oldDistributionModeId);
        orderMapper.updateByPrimaryKeySelective(newOrder);
        return printTask;
    }

    @Override
    public List<Map<String, Object>> refundOrderPrintKitChen(Order refundOrder) {
        Order order = selectById(refundOrder.getId());
        Integer oldDistributionModeId = order.getDistributionModeId();
        //如果是 未打印状态 或者  异常状态则改变 生产状态和打印时间
        if (ProductionStatus.HAS_ORDER == order.getProductionStatus() || ProductionStatus.NOT_PRINT == order.getProductionStatus()) {
            order.setProductionStatus(ProductionStatus.PRINTED);
            order.setPrintOrderTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        order.setDistributionModeId(DistributionType.REFUND_ORDER);
        //得到退掉的订单明细
        List<String> orderItemIds = new ArrayList<String>();
        for (OrderItem item : refundOrder.getOrderItems()) {
            orderItemIds.add(item.getId());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderItemIds", orderItemIds);
        List<OrderItem> items = orderItemService.selectRefundOrderItem(map);
        for (OrderItem item : items) {
            if (item.getType() == OrderItemType.SETMEALS) {
                List<OrderItem> list = orderitemMapper.getListBySort(item.getId(), item.getArticleId());
                item.setChildren(list);
            }
        }

        //生成打印任务
        List<Map<String, Object>> printTask = new ArrayList<>();
        //得到打印任务
        order.setIsRefund(Common.YES);
        List<Map<String, Object>> kitchenTicket = printKitchen(order, items, oldDistributionModeId);
        if (kitchenTicket != null && !kitchenTicket.isEmpty()) {
            printTask.addAll(kitchenTicket);
        }
        Order newOrder = new Order();
        newOrder.setId(refundOrder.getId());
        newOrder.setDistributionModeId(oldDistributionModeId);
        orderMapper.updateByPrimaryKeySelective(newOrder);
        return printTask;
    }

    @Override
    public List<Order> selectHasPayNoChangeStatusByBrandId(String brandId) {
        //查询昨日的数据
        Date create = DateUtil.getAfterDayDate(new Date(), -1);
        Date beginDate = DateUtil.getDateBegin(create);
        Date endDate = DateUtil.getDateEnd(create);
        return orderMapper.selectHasPayNoChangeStatusByBrandId(brandId, beginDate, endDate);

    }

    @Override
    public Double selectAppraiseBybrandId(String brandId, Date beginDate, Date endDate) {
        return orderMapper.selectAppraiseBybrandId(brandId, beginDate, endDate);
    }

    @Override
    public BigDecimal selectOrderMoneyByShopIdAndTime(String shopId, Date beginDate, Date endDate) {
        return orderMapper.selectOrderMoneyByShopIdAndTime(shopId, beginDate, endDate);
    }

    @Override
    public Double selectAppraiseByshopId(String shopId, Date beginDate, Date endDate) {
        return orderMapper.selectAppraiseSumByShopId(shopId, beginDate, endDate);
    }


    public Map<String, Object> refundOrderPrintTicket(Order order, Integer oldDistributionModeId, List<OrderItem> orderItems, ShopDetail shopDetail, Printer printer) {
        if (printer == null) {
            return null;
        }
        switch (oldDistributionModeId) {
            case DistributionType.RESTAURANT_MODE_ID:
                if (printer.getSupportTangshi() == Common.NO) {
                    return null;
                }
                break;
            case DistributionType.TAKE_IT_SELF:
                if (printer.getSupportWaidai() == Common.NO) {
                    return null;
                }
                break;
            case DistributionType.DELIVERY_MODE_ID:
                if (printer.getSupportWaimai() == Common.NO) {
                    return null;
                }
                break;
            default:
                break;
        }
        List<Map<String, Object>> refundItems = new ArrayList<>();
        BigDecimal articleCount = new BigDecimal(0);
        BigDecimal orderMoney = new BigDecimal(0);
        for (OrderItem article : orderItems) {
            Map<String, Object> refundItem = new HashMap<>();
            refundItem.put("SUBTOTAL", -article.getUnitPrice().multiply(new BigDecimal(article.getRefundCount())).doubleValue());
            refundItem.put("ARTICLE_NAME", article.getArticleName() + "(退)");
            refundItem.put("ARTICLE_COUNT", -article.getRefundCount());
            refundItems.add(refundItem);
            articleCount = articleCount.add(new BigDecimal(article.getRefundCount()));
            orderMoney = orderMoney.add(article.getUnitPrice().multiply(new BigDecimal(article.getRefundCount())));
            if (article.getType() != OrderItemType.MEALS_CHILDREN && order.getBaseMealAllCount() != null && order.getBaseMealAllCount() != 0) {
                refundItem = new HashMap<>();
                String aid = article.getArticleId();
                if (aid.indexOf("@") > -1) {
                    aid = aid.substring(0, aid.indexOf("@"));
                }
                Article a = articleService.selectById(aid);
                refundItem.put("SUBTOTAL", -shopDetail.getMealFeePrice().multiply(
                        new BigDecimal(article.getRefundCount()).multiply(new BigDecimal(a.getMealFeeNumber()))).doubleValue());
                refundItem.put("ARTICLE_NAME", shopDetail.getMealFeeName() + "(退)");
                refundItem.put("ARTICLE_COUNT", -(new BigDecimal(article.getRefundCount()).multiply(new BigDecimal(a.getMealFeeNumber()))).doubleValue());
                refundItems.add(refundItem);
                articleCount = articleCount.add(new BigDecimal(article.getRefundCount()).multiply(new BigDecimal(a.getMealFeeNumber())));
                orderMoney = orderMoney.add(shopDetail.getMealFeePrice().multiply(
                        new BigDecimal(article.getRefundCount()).multiply(new BigDecimal(a.getMealFeeNumber()))));
            }
        }
        Map<String, Object> print = new HashMap<>();
        String tableNumber = order.getTableNumber() != null ? order.getTableNumber() : "";
        print.put("TABLE_NO", tableNumber);
        print.put("KITCHEN_NAME", printer.getName());
        print.put("PORT", printer.getPort());
        print.put("ORDER_ID", order.getSerialNumber());
        print.put("IP", printer.getIp());
        String print_id = ApplicationUtils.randomUUID();
        print.put("PRINT_TASK_ID", print_id);
        print.put("ADD_TIME", new Date());

        Map<String, Object> data = new HashMap<>();
        data.put("ORDER_ID", order.getSerialNumber() + "-" + order.getVerCode());
        data.put("ORDER_NUMBER", (String) redisService.get(order.getId() + "orderNumber"));
        data.put("ITEMS", refundItems);
        List<Map<String, Object>> patMentItems = new ArrayList<Map<String, Object>>();
        Map<String, Object> payMentItem = new HashMap<String, Object>();
        payMentItem.put("SUBTOTAL", -order.getRefundMoney().doubleValue());
        payMentItem.put("PAYMENT_MODE", "退菜返还金额");
        patMentItems.add(payMentItem);
        data.put("PAYMENT_ITEMS", patMentItems);
        com.resto.api.appraise.entity.Appraise appraise = newAppraiseService.selectAppraiseByCustomerId(order.getBrandId(),order.getCustomerId(), order.getShopDetailId());
        StringBuilder star = new StringBuilder();
        BigDecimal level = new BigDecimal(0);
        if (appraise != null) {
            star.append("★★★★★☆☆☆☆☆".substring(5 - appraise.getLevel(), 10 - appraise.getLevel()));
            Map<String, Object> appriseCount = newAppraiseService.selectCustomerAppraiseAvg(order.getBrandId(),order.getCustomerId());
            level = new BigDecimal(Integer.valueOf(appriseCount.get("sum").toString()))
                    .divide(new BigDecimal(Integer.valueOf(appriseCount.get("count").toString())), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            star.append("☆☆☆☆☆");
        }
        StringBuilder gao = new StringBuilder();
        if (shopDetail.getIsUserIdentity().equals(1)) {
            //得到有限制的情况下用户的订单数
            int gaoCount = orderMapper.selectByCustomerCount(order.getCustomerId());
            if (gaoCount > 1) {
                gao.append("消费" + gaoCount + "次");
            } else {
                gao.append("新顾客");
            }
        }
        String modeText = getModeText(order);
        data.put("DISTRIBUTION_MODE", modeText);
        data.put("ORIGINAL_AMOUNT", -orderMoney.doubleValue());
        data.put("RESTAURANT_ADDRESS", shopDetail.getAddress());
        data.put("REDUCTION_AMOUNT", 0);
        data.put("RESTAURANT_TEL", shopDetail.getPhone());
        data.put("TABLE_NUMBER", order.getTableNumber());
        data.put("CUSTOMER_COUNT", order.getCustomerCount() == null ? "-" : order.getCustomerCount());
        data.put("PAYMENT_AMOUNT", orderMoney.subtract(order.getRefundMoney()));
        data.put("RESTAURANT_NAME", shopDetail.getName());
        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //从缓存里取当前订单的退菜总单打印次数， 如果为空则是第一次打印则当前打印次数为1
        Integer printTimes = redisService.get(order.getId() + "REFUNDTICKET") == null ? 1 : Integer.valueOf(redisService.get(order.getId() + "REFUNDTICKET").toString()) + 1;
        data.put("PRINT_TIMES", printTimes); //打印次数
        data.put("ARTICLE_COUNT", -articleCount.intValue());
        data.put("CUSTOMER_SATISFACTION", star.toString());
        data.put("CUSTOMER_SATISFACTION_DEGREE", level);
        if (!"0".equals(order.getCustomerId())) {
            Account account = accountService.selectAccountAndLogByCustomerId(order.getCustomerId());
            StringBuffer customerStr = new StringBuffer();
            if (account != null) {
                customerStr.append("余额：" + account.getRemain() + " ");
            } else {
                customerStr.append("余额：0 ");
            }
            customerStr.append("" + gao.toString() + " ");
            Customer customer = customerService.selectById(order.getCustomerId());
            CustomerDetail customerDetail = customerDetailMapper.selectByPrimaryKey(customer.getCustomerDetailId());
            if (customerDetail != null) {
                if (customerDetail.getBirthDate() != null) {
                    if (DateUtil.formatDate(customerDetail.getBirthDate(), "MM-dd")
                            .equals(DateUtil.formatDate(new Date(), "MM-dd"))) {
                        customerStr.append("★" + DateUtil.formatDate(customerDetail.getBirthDate(), "yyyy-MM-dd") + "★");
                    }
                }
            }
            data.put("CUSTOMER_PROPERTY", customerStr.toString());
        }
        print.put("DATA", data);
        print.put("STATUS", 0);
        print.put("TICKET_TYPE", TicketType.RECEIPT);
        //订单返回退菜总单打印模板视为已打印成功
        redisService.set(order.getId() + "REFUNDTICKET", printTimes);
        return print;
    }

    @Override
    public List<Order> selectOrderHistoryList(String id, Date dateEnd) {


        return orderMapper.selectOrderHistoryList(id, dateEnd);
    }

    @Override
    public List<Order> selectListsmsByShopId(Date begin, Date end, String id) {

        return orderMapper.selectListsmsByShopId(begin, end, id);
    }


    @Override
    public Order getCustomerLastOrder(String customerId) {
        return orderMapper.getCustomerLastOrder(customerId);
    }

    @Override
    public JSONResult confirmOrderPos(String orderId) {
        JSONResult jsonResult = new JSONResult();

        Order order = selectById(orderId);
        //开始状态
        Integer originState = order.getOrderState();
        Brand brand = brandService.selectByPrimaryKey(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        int i = orderMapper.confirmOrderPos(orderId);
        if (i > 0) {
            order.setOrderState(OrderState.PAYMENT);
        }
        if (order.getPayType() == PayType.NOPAY) {
            confirmOrder(order);
        }
        if (order.getPayType() == PayType.PAY && (order.getPayMode() == OrderPayMode.YL_PAY || order.getPayMode() == OrderPayMode.XJ_PAY
                || order.getPayMode() == OrderPayMode.SHH_PAY || order.getPayMode() == OrderPayMode.JF_PAY)) {
            payOrderSuccess(order);
        }
        updateChild(order);
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", shopDetail.getName());
        map.put("type", "posAction");
        map.put("content", "订单:" + order.getId() + "在pos端已确认收款订单状态更改为10,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);

        Map map1 = new HashMap(4);
        map1.put("brandName", brand.getBrandName());
        map1.put("fileName", order.getCustomerId());
        map1.put("type", "UserAction");
        map1.put("content", "用户:" + order.getCustomerId() + "的订单：" + order.getId() + "在pos端已确认收款订单状态更改为10,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map1);
        LogTemplateUtils.getConfirmOrderPosByOrderType(brand.getBrandName(), order, originState);
        couponService.addConsumptionRebateCoupon(order);
        jsonResult.setData(order);
        jsonResult.setSuccess(true);
        return jsonResult;
    }

    @Override
    public BigDecimal selectPayBefore(String orderId) {
        return orderMapper.selectPayBefore(orderId);
    }

    @Override
    public List<Order> getTodayFinishOrder(String shopId, String beginTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate;
        Date endDate;
        try {
            beginDate = DateUtil.getDateBegin(sdf.parse(beginTime));
            endDate = DateUtil.getDateEnd(sdf.parse(endTime));
        } catch (ParseException e) {
            beginDate = DateUtil.getDateBegin(new Date());
            endDate = DateUtil.getDateEnd(new Date());
        }
        return orderMapper.getTodayFinishOrder(shopId, beginDate, endDate);
    }


    @Override
    public List<String[]> getThirdData(List<Order> orderList, int size, String brandSign) {
        List<String[]> result = new ArrayList<>();

        for (Order o : orderList) {
            Map<String, String> param = new HashMap<>();
            param.put("orderId", o.getId());
            List<OrderItem> orderItems = orderItemService.listByOrderId(param);
//            Order order = getOrderInfo(o.getId());
            String[] data = new String[size];
            switch (brandSign) {
                case "test":
                    luroufanModel(data, o);
                    result.add(data);

                    for (OrderItem orderItem : orderItems) {
                        result.add(luroufanArticleModel(orderItem, size));
                    }
                    break;
                case "luroufan":
                    luroufanModel(data, o);
                    result.add(data);
                    for (OrderItem orderItem : orderItems) {
                        result.add(luroufanArticleModel(orderItem, size));
                    }
                    break;
                default:
                    break;
            }


        }
        return result;
    }

    private String[] luroufanArticleModel(OrderItem orderItem, int size) {
        String[] data = new String[size];
        data[LuroufanExcelModel.POSDATE] = "";
        data[LuroufanExcelModel.ADDTIME] = "";
        data[LuroufanExcelModel.ADDNAME] = "";
        data[LuroufanExcelModel.POSID] = "";
        data[LuroufanExcelModel.TABLENO] = "";
        data[LuroufanExcelModel.PFNAME] = "";
        data[LuroufanExcelModel.DEPARTMENT] = "";
        data[LuroufanExcelModel.DEPUTY] = "";
        data[LuroufanExcelModel.MENU_TYPE] = OrderItemType.getPayModeName(orderItem.getType());
        data[LuroufanExcelModel.MENU_CODE] = orderItem.getArticleId();
        data[LuroufanExcelModel.MENU_NAME] = orderItem.getArticleName();
        data[LuroufanExcelModel.QUANTITY] = String.valueOf(orderItem.getCount());
        data[LuroufanExcelModel.AMOUNT_1] = "";
        data[LuroufanExcelModel.AMOUNT_2] = "";
        data[LuroufanExcelModel.ACCOUNT_NAME] = "";
        data[LuroufanExcelModel.PAY_METHOD] = "";
        data[LuroufanExcelModel.REMARK] = "";
        return data;
    }


    private void luroufanModel(String[] data, Order o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        data[LuroufanExcelModel.POSDATE] = sdf.format(o.getCreateTime());
        data[LuroufanExcelModel.ADDTIME] = o.getPrintOrderTime() == null ? "" : sdf.format(o.getPrintOrderTime());
        data[LuroufanExcelModel.ADDNAME] = "";
        data[LuroufanExcelModel.POSID] = o.getShopDetailId();
        data[LuroufanExcelModel.TABLENO] = o.getTableNumber();
        data[LuroufanExcelModel.PFNAME] = o.getSerialNumber();
        data[LuroufanExcelModel.DEPARTMENT] = "";
        data[LuroufanExcelModel.DEPUTY] = "";
        data[LuroufanExcelModel.MENU_TYPE] = "";
        data[LuroufanExcelModel.MENU_CODE] = "";
        data[LuroufanExcelModel.MENU_NAME] = "";
        data[LuroufanExcelModel.QUANTITY] = String.valueOf(o.getArticleCount());
        data[LuroufanExcelModel.AMOUNT_1] = String.valueOf(o.getPaymentAmount());
        data[LuroufanExcelModel.AMOUNT_2] = String.valueOf(o.getOriginalAmount());
        List<OrderPaymentItem> payItemsList = orderPaymentItemService.selectByOrderId(o.getId());
        StringBuilder accountName = new StringBuilder("(");
        for (OrderPaymentItem payment : payItemsList) {
            accountName.append(payment.getRemark()).append(" ");
        }
        accountName.append(")");
        data[LuroufanExcelModel.ACCOUNT_NAME] = accountName.toString();
        data[LuroufanExcelModel.PAY_METHOD] = OrderPayMode.getPayModeName(o.getPayMode());
        data[LuroufanExcelModel.REMARK] = "";
    }

    @Override
    public List<Order> selectByOrderSatesAndNoPay(String shopId) {
        return orderMapper.selectByOrderSatesAndNoPay(shopId);
    }

    @Override
    public List<Order> selectBaseToThirdList(String brandId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectBaseToThirdList(begin, end, brandId);
    }

    @Override
    public List<Order> selectBaseToThirdListByShopId(String shopId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectBaseToThirdListByShopId(begin, end, shopId);
    }

    @Override
    public List<Order> selectBaseToKCList(String brandId, String beginDate, String endDate) {

        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectBaseToKCList(brandId, begin, end);

    }

    @Override
    public List<Order> selectBaseToKCListByShopId(String shopId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return orderMapper.selectBaseToKCListByShopId(shopId, begin, end);
    }


    @Override
    public void changeOrderMode(String orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order.getPayMode() == OrderPayMode.YL_PAY) {
            order.setPayMode(OrderPayMode.XJ_PAY);
        } else if (order.getPayMode() == OrderPayMode.XJ_PAY) {
            order.setPayMode(OrderPayMode.YL_PAY);
        }
        update(order);

        List<OrderPaymentItem> list = orderPaymentItemService.selectByOrderId(orderId);
        for (OrderPaymentItem paymentItem : list) {
            if (paymentItem.getPaymentModeId() == PayMode.CRASH_PAY
                    && paymentItem.getPayValue().doubleValue() > 0) {
                paymentItem.setPaymentModeId(PayMode.BANK_CART_PAY);
                paymentItem.setRemark("银联支付:" + paymentItem.getPayValue());
                orderPaymentItemService.update(paymentItem);
            } else if (paymentItem.getPaymentModeId() == PayMode.BANK_CART_PAY
                    && paymentItem.getPayValue().doubleValue() > 0) {
                paymentItem.setPaymentModeId(PayMode.CRASH_PAY);
                paymentItem.setRemark("现金:" + paymentItem.getPayValue());
                orderPaymentItemService.update(paymentItem);
            }
        }

    }


    @Override
    public Order posPayOrder(String orderId, Integer payMode, String couponId, BigDecimal payValue, BigDecimal giveChange, BigDecimal remainValue, BigDecimal couponValue) {

        log.info(">>>>>>>>>>>>>>>>>>开始进入posPayOrder方法,同步机制>>>>>>>>>>>>>>>>>>>>");

        Order order = selectById(orderId);
        updateChild(order);
        Customer customer = customerService.selectById(order.getCustomerId());
        Brand brand = brandService.selectById(order.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        Order newOrder = new Order();
        newOrder.setId(order.getId());
        newOrder.setOrderState(OrderState.PAYMENT);
        newOrder.setPayMode(payMode);
        newOrder.setPrintTimes(1);
        newOrder.setGiveChange(giveChange);
        newOrder.setIsPosPay(Common.YES);
        newOrder.setPaymentAmount(order.getOrderMoney().subtract(couponValue));
        int update = update(newOrder);
        if (update > 0) {
            order.setOrderState(OrderState.PAYMENT);
        }
        OrderPaymentItem paymentItem = new OrderPaymentItem();
        if (payValue.compareTo(BigDecimal.ZERO) > 0) {
            paymentItem.setId(ApplicationUtils.randomUUID());
            paymentItem.setPayTime(new Date());
            paymentItem.setPayValue(payValue);
            Integer orderPayMode = 0;
            String remark = "";
            switch (payMode) {
                case 1:
                    orderPayMode = PayMode.WEIXIN_PAY;
                    remark = "微信支付:" + payValue;
                    break;
                case 2:
                    orderPayMode = PayMode.ALI_PAY;
                    remark = "支付宝支付:" + payValue;
                    break;
                case 3:
                    orderPayMode = PayMode.BANK_CART_PAY;
                    remark = "银联支付:" + payValue;
                    break;
                case 4:
                    orderPayMode = PayMode.CRASH_PAY;
                    remark = "现金支付:" + payValue;
                    break;
                case 5:
                    orderPayMode = PayMode.SHANHUI_PAY;
                    remark = "新美大支付:" + payValue;
                    break;
                case 6:
                    orderPayMode = PayMode.INTEGRAL_PAY;
                    remark = "积分支付:" + payValue;
                    break;
                default:
                    break;
            }
            paymentItem.setPaymentModeId(orderPayMode);
            paymentItem.setRemark(remark);
            paymentItem.setOrderId(orderId);
            orderPaymentItemService.insert(paymentItem);
        }
        if (remainValue.compareTo(BigDecimal.ZERO) > 0) {
            accountService.payOrder(order, remainValue, customer, brand, shopDetail);
        }
        if (couponValue.compareTo(BigDecimal.ZERO) > 0) {
            couponService.useCouponById(orderId, couponId);
        }
        if (giveChange.compareTo(BigDecimal.ZERO) > 0) {
            paymentItem = new OrderPaymentItem();
            paymentItem.setId(ApplicationUtils.randomUUID());
            paymentItem.setPayTime(new Date());
            paymentItem.setPayValue(giveChange.multiply(new BigDecimal(-1)));
            paymentItem.setRemark("pos端结算订单找零:" + giveChange);
            paymentItem.setPaymentModeId(PayMode.GIVE_CHANGE);
            paymentItem.setOrderId(orderId);
            orderPaymentItemService.insert(paymentItem);
        }
        if (!payMode.equals(PayMode.WEIXIN_PAY) && !payMode.equals(PayMode.ACCOUNT_PAY)) {
            orderMapper.confirmOrderPos(orderId);
        }
        //发放消费返利优惠券
        couponService.addConsumptionRebateCoupon(order);
        //yz 计费系统 后付款 pos端 结算时计费
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
        //日志记录
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", order.getCustomerId());
        map.put("type", "UserAction");
        StringBuffer msg = new StringBuffer("用户:" + order.getCustomerId() + "订单在pos端被结算，结算方式为:");
        List<OrderPaymentItem> orderPaymentItems = orderPaymentItemService.selectByOrderId(order.getId());
        for (OrderPaymentItem orderPaymentItem : orderPaymentItems) {
            msg.append(orderPaymentItem.getRemark() + "\n");
        }
        msg.append("请求服务器地址为:" + MQSetting.getLocalIP());
        map.put("content", msg.toString());
        doPostAnsc(map);
        //yz 2017/07/29计费系统
        //判断是否已经记录过该订单
        BrandAccountLog brandAccountLog = brandAccountLogService.selectOneBySerialNumAndBrandId(order.getId(), order.getBrandId());
        //--
        if (brandAccountLog != null) {
            return order;
        }

        if (brandSetting.getOpenBrandAccount() == 1) {//说明开启了品牌账户
            AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());
            updateBrandAccount(order, true, accountSetting);
        }
        return order;
    }

    @Override
    public List<Order> selectMonthIncomeDto(Map<String, Object> selectMap) {
        return orderMapper.selectMonthIncomeDto(selectMap);
    }

    @Override
    public Order colseOrder(String orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order.getOrderState() == OrderState.SUBMIT) {
            orderMapper.colseOrder(orderId);
            order.setOrderState(OrderState.CANCEL);
        }
        return order;
    }

    @Override
    public List<Map<String, Object>> reminder(String orderItemIds, String orderId) {
        String[] itemIds = orderItemIds.split(",");
        List<Map<String, Object>> printTask = new ArrayList<>();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        Integer oldDis = order.getDistributionModeId();
        order.setDistributionModeId(DistributionType.REMINDER_ORDER);
        for (String itemId : itemIds) {
            OrderItem orderItem = orderitemMapper.selectByPrimaryKey(itemId);
            List<OrderItem> orderItems = orderitemMapper.getListByParentId(itemId);
            orderItems.add(orderItem);
            List<OrderItem> orderItemList = getOrderItemsWithChild(orderItems);
            printTask.addAll(printKitchen(order, orderItemList, order.getDistributionModeId()));
        }
        Order newOrder = new Order();
        newOrder.setId(orderId);
        newOrder.setDistributionModeId(oldDis);
        orderMapper.updateByPrimaryKeySelective(newOrder);
        return printTask;
    }

    @Override
    public void uploadLocalPosOrderList(List<Map<String, Object>> orderList) {
        if (CollectionUtils.isEmpty(orderList)) {
            return;
        }
        Order o = JSON.parseObject(new JSONObject(orderList.get(0)).toString(), Order.class);
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(o.getShopDetailId());
        for (Map orderMap : orderList) {
            Order order = JSON.parseObject(new JSONObject(orderMap).toString(), Order.class);
            order.setOperatorId("localPosOrder");
            order.setCustomerId("0");
            order.setVerCode(generateString(5));
            order.setAllowAppraise(true);
            order.setOrderMode(1);
            order.setReductionAmount(BigDecimal.valueOf(0));
            order.setBrandId(shopDetail.getBrandId());
            if(shopDetail.getShopMode() != ShopMode.MEISHI && shopDetail.getShopMode() != ShopMode.CALL_NUMBER)
            order.setAllowContinueOrder(true);
            orderMapper.insertSelective(order);
        }
    }

    List<OrderItem> getOrderItemsWithChild(List<OrderItem> orderItems) {
        log.debug("这里查看套餐子项: ");
        Map<String, OrderItem> idItems = ApplicationUtils.convertCollectionToMap(String.class, orderItems);
        for (OrderItem item : orderItems) {
            if (item.getType() == OrderItemType.MEALS_CHILDREN) {
                OrderItem parent = idItems.get(item.getParentId());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<OrderItem>());
                }
                parent.getChildren().add(item);
                idItems.remove(item.getId());
            }
        }
        List<OrderItem> items = new ArrayList<>();
        for (OrderItem orderItem : idItems.values()) {
            items.add(orderItem);
            if (orderItem.getChildren() != null && !orderItem.getChildren().isEmpty()) {
//				for (OrderItem childItem:orderItem.getChildren()) {
                List<OrderItem> item = orderitemMapper.getListBySort(orderItem.getId(), orderItem.getArticleId());
                for (OrderItem obj : item) {
                    obj.setArticleName("|_" + obj.getArticleName());
                    items.add(obj);
                }
//                childItem.setArticleName("|__" + childItem.getArticleName());
//                items.add(childItem);
//				}
            }
        }
        return items;
    }


    @Override
    public void fixErrorOrder() {
        orderMapper.fixAllowContinueOrder(new Date());
//        List<Order> orders = orderMapper.getAllowAppraise();
//        for (Order order : orders) {
//            confirmOrder(order);
//        }
    }

    @Override
    public void fixErrorGroup() {
        tableGroupService.releaseTableGroup(new Date());
//        List<Order> orders = orderMapper.getAllowAppraise();
//        for (Order order : orders) {
//            confirmOrder(order);
//        }
    }

    @Override
    public List<ShopIncomeDto> callProcDayAllOrderItem(Map<String, Object> selectMap) {
        return orderMapperReport.callProcDayAllOrderItem(selectMap);
    }

    @Override
    public List<ShopIncomeDto> callProcDayAllOrderPayMent(Map<String, Object> selectMap) {
        return orderMapperReport.callProcDayAllOrderPayMent(selectMap);
    }

    @Override
    public Order customerByOrderForMyPage(String customerId, String shopId) {
        Order order = orderMapper.customerByOrderForMyPage(customerId, shopId);
        return order;
    }

    @Override
    public List<RefundArticleOrder> addRefundArticleDto(String beginDate, String endDate, String shopId) {
        return orderMapperReport.addRefundArticleDto(beginDate, endDate, shopId);
    }

    @Override
    public Summarry selctSummaryShopData(String beginDate, String endDate, String shopId) {

        Summarry s = new Summarry();
        //定义时间
        Date begin = DateUtil.getDateBegin(DateUtil.fomatDate(beginDate));
        Date end = DateUtil.getDateEnd(DateUtil.fomatDate(endDate));

        BigDecimal offLineOrderMoney = BigDecimal.ZERO;

        List<OffLineOrder> offLineOrderList = offLineOrderMapper.selectByShopIdAndTime(shopId, begin, end);
        if (!offLineOrderList.isEmpty()) {
            for (OffLineOrder offLineOrder : offLineOrderList)
                offLineOrderMoney = offLineOrderMoney.add(offLineOrder.getEnterTotal());
        }
        s.setLineOffOrderMoney(offLineOrderMoney);//线下消费金额

        //查询该段时间内的新增用户订单
        List<Order> newCustomerOrders = orderMapper.selectNewCustomerOrderByShopIdAndTime(shopId, begin, end);

        //新增用户的订单总数
        int newCustomerOrderNum = 0;

        //分享用户的订单总数
        int newShareCustomerOrderNum = 0;

        //新增用户的订单金额
        BigDecimal newCustomerOrderMoney = BigDecimal.ZERO;

        //分享用户的订单金额
        BigDecimal newShareCustomerOrderMoney = BigDecimal.ZERO;


        if (!newCustomerOrders.isEmpty()) {
            for (Order o : newCustomerOrders) {
                newCustomerOrderNum++;
                newCustomerOrderMoney = newCustomerOrderMoney.add(getOrderMoney(o.getPayType(), o.getOrderMoney(), o.getAmountWithChildren()));
                if (o.getCustomer() != null && StringUtil.isNotEmpty(o.getCustomer().getShareCustomer())) {
                    //说明是分享用户
                    newShareCustomerOrderNum++;
                    newShareCustomerOrderMoney = newShareCustomerOrderMoney.add(getOrderMoney(o.getPayType(), o.getOrderMoney(), o.getAmountWithChildren()));
                }
            }
        }
        //获取到 新用户消费笔数 + 分享用户消费笔数

        s.setNewCustomerOrderNum(newCustomerOrderNum);
        s.setNewShareCustomerOrderNum(newShareCustomerOrderNum);
        s.setNewCustomerOrderMoney(newCustomerOrderMoney);
        s.setNewShareCustomerMoney(newShareCustomerOrderMoney);


        //查询回头用户
        List<BackCustomerDto> backCustomerDtos = orderMapper.selectBackCustomerByShopIdAndTime(shopId, begin, end);
        Set<String> backCustomerId = new HashSet<>();
        if (!backCustomerDtos.isEmpty()) {
            for (BackCustomerDto b : backCustomerDtos) {
                backCustomerId.add(b.getCustomerId());
            }
        }


        //定义回头用户消费笔数
        int backCustomerOrderNum = 0;
        //回头用户消费金额
        BigDecimal backCustomerOrderMoney = BigDecimal.ZERO;

        //查询 出 所有的订单 根据该订单是否是回头用户来判断 回头用户订单
        List<Order> orders = orderMapper.selectCompleteByShopIdAndTime(shopId, begin, end);
        if (!orders.isEmpty()) {
            for (Order o : orders) {
                if (backCustomerId.contains(o.getCustomerId())) {
                    backCustomerOrderNum++;
                    backCustomerOrderMoney = backCustomerOrderMoney.add(getOrderMoney(o.getPayType(), o.getOrderMoney(), o.getAmountWithChildren()));
                }
            }
        }
        s.setBackCustomerMoney(backCustomerOrderMoney);

        s.setBackCustomerOrder(backCustomerOrderNum);
        //用户消费笔数= 新用户消费笔数+回头用户消费笔数

        s.setCustomerOrderNum(backCustomerOrderNum + newCustomerOrderNum);

        s.setCustomerOrderMoney(backCustomerOrderMoney.add(newCustomerOrderMoney));


        //折扣比率
        String discountRatio = "";

        //resto订单总额
        BigDecimal restoTotal = BigDecimal.ZERO;
        //红包
        BigDecimal redPackTotal = BigDecimal.ZERO;
        //优惠券
        BigDecimal couponTotal = BigDecimal.ZERO;
        //充值赠送
        BigDecimal chargeReturn = BigDecimal.ZERO;
        //折扣合计
        BigDecimal discountTotal = BigDecimal.ZERO;


        if (!orders.isEmpty()) {
            for (Order o : orders) {
                //resto订单总额
                restoTotal = restoTotal.add(getOrderMoney(o.getPayType(), o.getOrderMoney(), o.getAmountWithChildren()));
                if (!o.getOrderPaymentItems().isEmpty()) {
                    //订单支付项
                    for (OrderPaymentItem oi : o.getOrderPaymentItems()) {
                        switch (oi.getPaymentModeId()) {
                            case PayMode.ACCOUNT_PAY:
                                redPackTotal = redPackTotal.add(oi.getPayValue());
                                break;
                            case PayMode.COUPON_PAY:
                                couponTotal = couponTotal.add(oi.getPayValue());
                                break;
                            case PayMode.REWARD_PAY:
                                chargeReturn = chargeReturn.add(oi.getPayValue());
                                break;
                            default:
                                break;
                        }

                    }

                }
                discountTotal = redPackTotal.add(couponTotal).add(chargeReturn);
                discountRatio = discountTotal.divide(restoTotal.add(discountTotal), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();

            }
        }

        s.setRestoTotalMoney(restoTotal);

        s.setStatisfaction(discountRatio);

        //评论数
        int fiveStar = 0;

        int fourStar = 0;

        int oneToThreeStar = 0;

        //总评价数
        int appraiseNum = 0;
        //总分数

        double appraiseSum = 0;


        /**
         * 评价 和 满意度 错误的原因 用户可能今天 下单 但是隔天
         * 去评价 而现在 是查当天下单当天评价所以需要单独查询
         *
         *
         */

        ShopDetail  shopDetail = shopDetailService.selectById(shopId);
        //单独查询评价和分数
        List<Appraise> appraises = newAppraiseService.selectByTimeAndShopId(shopDetail.getBrandId(),shopId, DateTimeUtils.dateToString(begin), DateTimeUtils.dateToString(end));
        if (!appraises.isEmpty()) {
            for (Appraise a : appraises) {
                appraiseNum++;
                appraiseSum += a.getLevel() * 20;
                if (a.getLevel() == 5) {
                    fiveStar++;
                } else if (a.getLevel() == 4) {
                    fourStar++;
                } else {
                    oneToThreeStar++;
                }
            }
        }

        //评论数
        s.setFiveStar(fiveStar);
        s.setFourStar(fourStar);
        s.setOneToThree(oneToThreeStar);
        return s;
    }

    @Override
    public List<Map<String, Object>> selectMealServiceSales(Map<String, Object> selectMap) {
        return orderMapperReport.selectMealServiceSales(selectMap);
    }

    @Override
    public List<Map<String, Object>> badAppraisePrintOrder(String orderId) {
        List<Map<String, Object>> printTask = new ArrayList<>();
        //得到订单信息
        Order order = selectById(orderId);
        //得到店铺信息
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        //得到所有打印机信息
        List<Printer> printerList = new ArrayList<>();
        if (shopDetail.getBadAppraisePrintReceipt()) {
            printerList.addAll(printerService.selectPrintByType(order.getShopDetailId(), 2));
        }
        if (shopDetail.getBadAppraisePrintKitchen()) {
            printerList.addAll(printerService.selectPrintByType(order.getShopDetailId(), 1));
        }
        //得到桌号
        TableQrcode tableQrcode = tableQrcodeService.selectByTableNumberShopId(order.getShopDetailId(), Integer.valueOf(order.getTableNumber()));
        //得到该笔订单的评论信息
        com.resto.api.appraise.entity.Appraise appraise = newAppraiseService.selectDeatilByOrderId(order.getBrandId(),order.getId(), null);
        //得到该笔订单给差评的菜品Id
        String[] articleIds = appraise.getArticleId().split(",");
        //得到差评菜品的订单信息
        List<OrderItem> orderItems = orderItemService.selectByArticleIds(articleIds);
        OrderItem[] orderItemList = new OrderItem[orderItems.size()];
        for (int i = 0; i < orderItems.size(); i++) {
            orderItemList[i] = orderItems.get(i);
        }
        //封装打印模板
        for (Printer printer : printerList) {
            badAppraiseOrderGetKitchenModel(orderItemList, order, printer, appraise, tableQrcode, printTask);
        }
        return printTask;
    }

    @Override
    public Summarry selctSummaryBrandData(String beginDate, String endDate, String brandId) {
        //定义时间
        Date begin = DateUtil.getDateBegin(DateUtil.fomatDate(beginDate));
        Date end = DateUtil.getDateEnd(DateUtil.fomatDate(endDate));

        //查询该段时间内的新增用户订单
        List<Order> newCustomerOrders = orderMapper.selectNewCustomerOrderByBrandIdAndTime(brandId, begin, end);

        //新增用户的订单总数
        int newCustomerOrderNum = 0;

        //分享用户的订单总数
        int newShareCustomerOrderNum = 0;

        if (!newCustomerOrders.isEmpty()) {
            for (Order o : newCustomerOrders) {
                newCustomerOrderNum++;
                if (o.getCustomer() != null && StringUtil.isNotEmpty(o.getCustomer().getShareCustomer())) {
                    //说明是分享用户
                    newShareCustomerOrderNum++;
                }
            }
        }
        //获取到 新用户消费笔数 + 分享用户消费笔数
        Summarry s = new Summarry();


        //查询回头用户
        List<BackCustomerDto> backCustomerDtos = orderMapper.selectBackCustomerByBrandIdAndTime(brandId, begin, end);
        Set<String> backCustomerId = new HashSet<>();
        if (!backCustomerDtos.isEmpty()) {
            for (BackCustomerDto b : backCustomerDtos) {
                backCustomerId.add(b.getCustomerId());
            }
        }


        //定义回头用户消费笔数
        int backCustomerOrderNum = 0;
        List<Order> orders = orderMapper.selectCompleteByBrandIdAndTime(brandId, begin, end);
        if (!orders.isEmpty()) {
            for (Order o : orders) {
                if (backCustomerId.contains(o.getCustomerId())) {
                    backCustomerOrderNum++;
                }
            }
        }
        //用户消费笔数= 新用户消费笔数+回头用户消费笔数


        //折扣比率
        String discountRatio = "";

        //resto订单总额
        BigDecimal restoTotal = BigDecimal.ZERO;
        //红包
        BigDecimal redPackTotal = BigDecimal.ZERO;
        //优惠券
        BigDecimal couponTotal = BigDecimal.ZERO;
        //充值赠送
        BigDecimal chargeReturn = BigDecimal.ZERO;
        //折扣合计
        BigDecimal discountTotal = BigDecimal.ZERO;


        List<Order> orderList = orderMapper.selectListsmsByBrandId(begin, end, brandId);
        if (!orderList.isEmpty()) {
            for (Order o : orderList) {
                //resto订单总额
                restoTotal = restoTotal.add(getOrderMoney(o.getPayType(), o.getOrderMoney(), o.getAmountWithChildren()));
                if (!o.getOrderPaymentItems().isEmpty()) {
                    //订单支付项
                    for (OrderPaymentItem oi : o.getOrderPaymentItems()) {
                        if (oi.getPaymentModeId() == PayMode.ACCOUNT_PAY) {
                            redPackTotal = redPackTotal.add(oi.getPayValue());
                        } else if (oi.getPaymentModeId() == PayMode.COUPON_PAY) {
                            couponTotal = couponTotal.add(oi.getPayValue());
                        } else if (oi.getPaymentModeId() == PayMode.REWARD_PAY) {
                            chargeReturn = chargeReturn.add(oi.getPayValue());
                        }
                    }
                }
                discountTotal = redPackTotal.add(couponTotal).add(chargeReturn);
                discountRatio = discountTotal.divide(restoTotal.add(discountTotal), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();

            }
        }

        s.setStatisfaction(discountRatio);

        //评论数
        int fiveStar = 0;

        int fourStar = 0;

        int oneToThreeStar = 0;

        //总评价数
        int appraiseNum = 0;
        //总分数

        double appraiseSum = 0;


        /**
         * 评价 和 满意度 错误的原因 用户可能今天 下单 但是隔天
         * 去评价 而现在 是查当天下单当天评价所以需要单独查询
         *
         *
         */

        //单独查询评价和分数
        List<Appraise> appraises = newAppraiseService.selectByTimeAndBrandId(brandId,begin, end);
        if (!appraises.isEmpty()) {
            for (Appraise a : appraises) {
                appraiseNum++;
                appraiseSum += a.getLevel() * 20;
                if (a.getLevel() == 5) {
                    fiveStar++;
                } else if (a.getLevel() == 4) {
                    fourStar++;
                } else {
                    oneToThreeStar++;
                }
            }
        }

        //评论数
        s.setFiveStar(fiveStar);
        s.setFourStar(fourStar);
        s.setOneToThree(oneToThreeStar);
        return s;

    }


    /**
     * 得到差评订单的打印模板
     *
     * @param orderItemList
     * @param order
     * @param printer
     * @param appraise
     * @param tableQrcode
     * @param printTask
     */
    private void badAppraiseOrderGetKitchenModel(OrderItem[] orderItemList, Order order, Printer printer, com.resto.api.appraise.entity.Appraise  appraise, TableQrcode tableQrcode, List<Map<String, Object>> printTask) {
        //保存 菜品的名称和数量
        List<Map<String, Object>> items = new ArrayList<>();
        //封装菜品信息
        OrderItem orderItem;
        for (int i = 1; i < orderItemList.length; i++) {
            for (int j = 0; j < orderItemList.length - i; j++) {
                if (orderItemList[j].getArticleName().length() > orderItemList[j + 1].getArticleName().length()) {
                    orderItem = orderItemList[j];
                    orderItemList[j] = orderItemList[j + 1];
                    orderItemList[j + 1] = orderItem;
                }
            }
        }
        //得到最小的菜品名称的长度
        Integer minLength = orderItemList.length > 0 ? orderItemList[0].getArticleName().length() : 0;
        Map<String, Object> item = new HashMap<>();
        for (OrderItem article : orderItemList) {
            if (article.getArticleName().length() > minLength) {
                item.put("ARTICLE_NAME", getSpaceNumber(10 - ((article.getArticleName().length() - minLength) * 2)).concat(article.getCount().toString()));
            } else {
                item.put("ARTICLE_NAME", getSpaceNumber(10).concat(article.getCount().toString()));
            }
            item.put("ARTICLE_COUNT", article.getArticleName());
            items.add(item);
            item = new HashMap<>();
        }
        String serialNumber = order.getSerialNumber();//序列号
        String modeText = DistributionType.getModeText(DistributionType.BAD_APPRAISE_ORDER);//就餐模式
        //保存打印信息
        Map<String, Object> print = new HashMap<>();
        print.put("PORT", printer.getPort());
        print.put("OID", order.getId());
        print.put("IP", printer.getIp());
        print.put("PRINT_TASK_ID", ApplicationUtils.randomUUID());
        print.put("ORDER_ID", serialNumber);
        Map<String, Object> data = new HashMap<>();
        //订单Id
        data.put("ORDER_ID", serialNumber);
        //订单时间
        data.put("DATETIME", DateUtil.formatDate(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); //下单时间
        //一个订单只会被评论一次， 所以该笔订单的差评打印次数衡为1
        data.put("PRINT_TIMES", 1); //打印次数
        //订单模式
        data.put("DISTRIBUTION_MODE", modeText);
        //桌号
        data.put("TABLE_NUMBER", order.getTableNumber());
        //该桌号所在区域
        data.put("ORDER_NUMBER", tableQrcode != null && StringUtils.isNotBlank(tableQrcode.getAreaName()) ? tableQrcode.getAreaName() : "-");
        data.put("ITEMS", items);
        //得到当前评论星级
        StringBuilder star = new StringBuilder();
        if (appraise != null) {
            star.append("★★★★★☆☆☆☆☆".substring(5 - appraise.getLevel(), 10 - appraise.getLevel()));
        } else {
            star.append("☆☆☆☆☆");
        }
        data.put("CUSTOMER_SATISFACTION", star.toString());
        //声明对象存储差评用户信息
        StringBuilder customerInfo = new StringBuilder();
        //得到该订单用户信息
        Customer customer = customerService.selectById(order.getCustomerId());
        //判断用户手机号是否为空
        if (StringUtils.isNotBlank(customer.getTelephone())) {
            customerInfo.append("[" + customer.getTelephone() + "]");
        }
        //判断用户性别
        if (!customer.getSex().equals(Common.NO)) {
            if (customer.getSex().equals(Common.YES)) {
                customerInfo.append("[先生]");
            } else {
                customerInfo.append("[女士]");
            }
        }
        data.put("CUSTOMER_PROPERTY", customerInfo.toString());
        //评价内容
        String[] feedBacks = appraise.getFeedback().split(",");
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (String feedBack : feedBacks) {
            if (feedBack.indexOf("差") != -1) {
                builder.append(feedBack).append(",");
            }
        }
        String content = builder.toString();
        content = content.substring(0, content.length() - 1).concat(")").concat(appraise.getContent());
        data.put("MEMO", "评价内容：" + content);
        print.put("DATA", data);
        print.put("STATUS", "0");
        print.put("TICKET_TYPE", TicketType.KITCHEN);
        //添加到 打印集合
        printTask.add(print);
    }

    /**
     * 得到空格字符串
     *
     * @param spaceNumber
     * @return
     */
    private String getSpaceNumber(Integer spaceNumber) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spaceNumber; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }

    @Override
    public List<Map<String, String>> selectCustomerOrderCount(List<String> customerIds) {
        return orderMapper.selectCustomerOrderCount(customerIds);
    }

    @Override
    public List<Order> selectHasPayNoChangeStatus(String shopId, Date dateBegin, Date dateEnd) {
        return orderMapper.selectHasPayNoChangeStatus(shopId, dateBegin, dateEnd);
    }

    @Override
    public List<Order> selectNewCustomerOrderByShopIdAndTime(String shopId, Date begin, Date end) {
        return orderMapper.selectNewCustomerOrderByShopIdAndTime(shopId, begin, end);
    }

    @Override
    public List<BackCustomerDto> selectBackCustomerByShopIdAndTime(String shopId, Date begin, Date end) {
        return orderMapper.selectBackCustomerByShopIdAndTime(shopId, begin, end);
    }

    @Override
    public List<Order> selectCompleteByShopIdAndTime(String shopId, Date begin, Date end) {
        return orderMapper.selectCompleteByShopIdAndTime(shopId, begin, end);
    }

    @Override
    public Order selectAfterValidOrderByCustomerId(String customerId) {
        return orderMapper.selectAfterValidOrderByCustomerId(customerId);
    }

    /**
     * @param orderId
     * @param discount        折扣的比例   (不包含 抹去的金额  跟  不需要折扣的金额)
     * @param orderItems
     * @param eraseMoney      抹去的金额
     * @param noDiscountMoney 不需要折扣的金额
     * @param type
     * @return
     */
    @Override
    public Order posDiscount(String orderId, BigDecimal discount, List<OrderItem> orderItems, BigDecimal eraseMoney, BigDecimal noDiscountMoney, Integer type, BigDecimal orderPosDiscountMoney) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order.getPosDiscount().compareTo(new BigDecimal(1)) == 0
                && order.getEraseMoney().compareTo(new BigDecimal(0)) == 0
                && order.getNoDiscountMoney().compareTo(new BigDecimal(0)) == 0) {
            order.setBaseOrderMoney(order.getAmountWithChildren().doubleValue() > 0 ? order.getAmountWithChildren() : order.getOrderMoney());
        }
        BigDecimal orderMoney = order.getBaseOrderMoney() == null ? order.getOrderMoney() : order.getBaseOrderMoney();
        BigDecimal shijiMoney = (orderMoney.subtract(eraseMoney).subtract(noDiscountMoney)).multiply(discount).add(noDiscountMoney);
        BigDecimal posDiscount = shijiMoney.divide(orderMoney, 2, BigDecimal.ROUND_HALF_UP);
        //整单折扣统计菜品项
        if (type.equals(PosDiscount.ZHENGDAN)) {
            boolean flag = false;
            List<Order> pOrder = orderMapper.selectListByParentId(orderId);
            if (pOrder.size() > 0) {
                flag = true;
            }
            Map map = new HashMap();
            map.put("orderId", orderId);
            map.put("count", "1=1");
            List<OrderItem> oItems = orderItemService.selectOrderItemByOrderId(map);
            order = posDiscountAction(oItems, discount, posDiscount, order, eraseMoney, noDiscountMoney, shijiMoney, flag, orderPosDiscountMoney);
            shijiMoney = shijiMoney.subtract(order.getOrderMoney());
            if (flag) {
                BigDecimal sum = new BigDecimal(0);
                for (int i = 0; i < pOrder.size(); i++) {
                    Order oP = pOrder.get(i);
                    map.clear();
                    map.put("orderId", oP.getId());
                    map.put("count", "1=1");
                    if ((i + 1) == pOrder.size()) {
                        oP = posDiscountAction(orderItemService.selectOrderItemByOrderId(map), discount, posDiscount, oP, eraseMoney, noDiscountMoney, shijiMoney, false, orderPosDiscountMoney);
                    } else {
                        oP = posDiscountAction(orderItemService.selectOrderItemByOrderId(map), discount, posDiscount, oP, eraseMoney, noDiscountMoney, shijiMoney, true, orderPosDiscountMoney);
                    }
                    shijiMoney = shijiMoney.subtract(oP.getOrderMoney());
                    sum = sum.add(oP.getOrderMoney());
                }
                //修改主订单
                order.setAmountWithChildren(order.getOrderMoney().add(sum));
                order.setOrderPosDiscountMoney(order.getBaseOrderMoney().subtract(order.getAmountWithChildren()));
                if (order.getOrderPosDiscountMoney().doubleValue() <= 0 && orderPosDiscountMoney.doubleValue() > 0) {
                    order.setOrderPosDiscountMoney(orderPosDiscountMoney);
                }
                orderMapper.updateByPrimaryKeySelective(order);
            }
        }
        return order;
    }

    public Order posDiscountAction(List<OrderItem> orderItems, BigDecimal discount, BigDecimal posDiscount, Order order, BigDecimal eraseMoney,
                                   BigDecimal noDiscountMoney, BigDecimal shijiMoney, boolean flag, BigDecimal orderPosDiscountMoney) {
        ShopDetail shop = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        BrandSetting brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
        BigDecimal sum = new BigDecimal(0);
        //修改菜品项
        for (OrderItem oItem : orderItems) {
            oItem.setUnitPrice((oItem.getBaseUnitPrice() == null ? oItem.getUnitPrice() : oItem.getBaseUnitPrice()).multiply(posDiscount).setScale(2, BigDecimal.ROUND_HALF_UP));
            oItem.setPosDiscount(posDiscount.multiply(new BigDecimal(100)) + "%");
            oItem.setFinalPrice(oItem.getUnitPrice().multiply(new BigDecimal(oItem.getCount())));
            sum = sum.add(oItem.getFinalPrice());
            orderItemService.update(oItem);
        }
        //修改子订单
        if (order.getParentOrderId() != null && !"".equals(order.getParentOrderId())) {
            if (flag) {
                order.setOrderMoney(sum);
                order.setPaymentAmount(sum);
            } else {
                order.setOrderMoney(shijiMoney);
                order.setPaymentAmount(shijiMoney);
            }
            order.setPosDiscount(discount);
            orderMapper.updateByPrimaryKeySelective(order);
        }
        //修改主订单
        if (order.getParentOrderId() == null || "".equals(order.getParentOrderId())) {
            if (shop.getServicePrice().doubleValue() > 0 && shop.getIsUseServicePrice() == 1 && brandSetting.getIsUseServicePrice() == 1 && order.getCustomerCount() > 0) {
                order.setServicePrice(posDiscount.multiply(shop.getServicePrice()).multiply(new BigDecimal(order.getCustomerCount())).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if (order.getMealFeePrice().doubleValue() > 0) {
                order.setMealFeePrice(posDiscount.multiply(shop.getMealFeePrice()).multiply(new BigDecimal(order.getMealAllNumber())).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if (flag) {
                order.setOrderMoney((shijiMoney.compareTo(sum) >= 0 ? sum : shijiMoney).add(order.getServicePrice()).add(order.getMealFeePrice()));
                order.setPaymentAmount((shijiMoney.compareTo(sum) >= 0 ? sum : shijiMoney).add(order.getServicePrice()).add(order.getMealFeePrice()));
            } else {
                order.setOrderMoney(shijiMoney);
                order.setPaymentAmount(shijiMoney);
            }
            order.setEraseMoney(eraseMoney);
            order.setNoDiscountMoney(noDiscountMoney);
            BigDecimal value = orderMapper.selectPayBefore(order.getId());
            if (value != null && value.doubleValue() > 0) {
                order.setPaymentAmount(sum.subtract(value));
            }
            order.setPosDiscount(discount);
            if(order.getBaseOrderMoney() == null){
                order.setOrderPosDiscountMoney(new BigDecimal(0));
            }else{
                order.setOrderPosDiscountMoney(order.getBaseOrderMoney().subtract(order.getOrderMoney()));
            }

            if (order.getOrderPosDiscountMoney().doubleValue() <= 0 && orderPosDiscountMoney.doubleValue() > 0) {
                order.setOrderPosDiscountMoney(orderPosDiscountMoney);
            }
            orderMapper.updateByPrimaryKeySelective(order);
        }
        return order;
    }

    @Override
    public List<Map<String, Object>> callBossAppOrdrReport(String brandId, List<ShopDetail> shopDetailList, String beginDate, String endDate) {

        List<Map<String, Object>> list = new ArrayList<>();
        List<ShopOrderReportDto> shopOrderReportDtoList = getBossAppOrderReport(brandId, shopDetailList, beginDate, endDate);
        if (shopOrderReportDtoList != null && !shopOrderReportDtoList.isEmpty()) {
            for (ShopOrderReportDto so : shopOrderReportDtoList) {
                //查询线下订单数据
                OffLineOrder offLineOrder = offLineOrderMapper.selectByTimeSourceAndShopId(OfflineOrderSource.OFFLINE_POS, so.getShopDetailId(), DateUtil.getformatBeginDate(beginDate), DateUtil.getformatEndDate(endDate));
                //线下订单数
                int offlineOrderNum = 0;
                //线下订单金额
                BigDecimal offlienOrderPrice = BigDecimal.ZERO;
                if (offLineOrder != null) {
                    offlineOrderNum = offLineOrder.getEnterCount();
                    offlienOrderPrice = offLineOrder.getEnterTotal();
                }

                Map<String, Object> map = new HashMap<>();
                map.put("brandId", brandId);
                map.put("shopId", so.getShopDetailId());
                map.put("dateTime", beginDate);
                //订单总数
                map.put("orderNum", so.getShop_orderCount());
                //订单总额
                map.put("orderMoney", so.getShop_orderPrice());
                //就餐人数
                map.put("customerCount", so.getShop_peopleCount());
                //糖吃订单数
                map.put("tangshiOrderNum", so.getShop_tangshiCount());
                //堂吃订单金额
                map.put("tanshiOrderPrice", so.getShop_tangshiPrice());
                //外卖订单总数
                map.put("outFoodOrderNum", so.getShop_waimaiCount());
                //外卖订单总额
                map.put("outFoodOrderPrice", so.getShop_waimaiPrice());
                //外带订单总数
                map.put("outAwayOrderNum", so.getShop_waidaiCount());
                //外带订单总额
                map.put("outAwaryOrdrPrice", so.getShop_waidaiPrice());

                //线下订单总数
                map.put("offLineOrderNum", offlineOrderNum);
                //线下订单朕
                map.put("offLineOrderPrice", offlienOrderPrice);
                list.add(map);
            }
        }

        return list;

    }

    @Override
    public Order posSyncSelectById(String orderId) {
        return orderMapper.posSyncSelectById(orderId);
    }

    @Override
    public Integer selectCompleteOrderCount(String shopId, String customerId) {
        return orderMapper.selectCompleteOrderCount(shopId, customerId);
    }

    @Override
    public List<String> posSelectNotCancelledOrdersIdByDate(String shopId, String beginDate, String endDate) {
        return orderMapper.posSelectNotCancelledOrdersIdByDate(shopId, beginDate, endDate);
    }

    @Override
    public Order selectBySerialNumber(String serialNumber) {
        return orderMapper.selectBySerialNumber(serialNumber);
    }

    private List<ShopOrderReportDto> getBossAppOrderReport(String brandId, List<ShopDetail> shopDetailList, String beginDate, String endDate) {
        List<ShopOrderReportDto> shopOrderReportDtoLists = new ArrayList<>();
        for (int i = 0; i < shopDetailList.size(); i++) {
            ShopDetail shopDetail = shopDetailList.get(i);
            Date begin = DateUtil.getformatBeginDate(beginDate);
            Date end = DateUtil.getformatEndDate(endDate);
            ShopOrderReportDto shopOrderReportDtos = orderMapperReport.procDayAllOrderItemShop(begin, end, shopDetail.getId());
            //防止当日没有数据，封装基础信息返回
            Optional.ofNullable(shopOrderReportDtos).ifPresent(shopOrderReportDto -> {
                //计算单均
                if (shopOrderReportDto.getShop_orderCount() != 0 && shopOrderReportDto.getShop_orderPrice() != null) {
                    BigDecimal ssinglePrice = new BigDecimal(shopOrderReportDto.getShop_orderPrice().doubleValue() / shopOrderReportDto.getShop_orderCount());
                    shopOrderReportDto.setShop_singlePrice(new BigDecimal(ssinglePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                } else {
                    shopOrderReportDto.setShop_singlePrice(BigDecimal.ZERO);
                }
                //计算人均
                if (shopOrderReportDto.getShop_peopleCount() != null && shopOrderReportDto.getShop_peopleCount() != 0
                        && shopOrderReportDto.getShop_orderPrice() != null) {
                    BigDecimal speopleCount = new BigDecimal(shopOrderReportDto.getShop_orderPrice().doubleValue() / shopOrderReportDto.getShop_peopleCount());
                    shopOrderReportDto.setShop_perPersonPrice(new BigDecimal(speopleCount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                } else {
                    shopOrderReportDto.setShop_perPersonPrice(BigDecimal.ZERO);
                }
                //就餐人数
                shopOrderReportDto.setShop_peopleCount(Optional.ofNullable(shopOrderReportDto.getShop_peopleCount()).orElse(0));
                shopOrderReportDto.setShop_orderPrice(Optional.ofNullable(shopOrderReportDto.getShop_orderPrice()).orElse(BigDecimal.ZERO));
                shopOrderReportDto.setShop_tangshiPrice(Optional.ofNullable(shopOrderReportDto.getShop_tangshiPrice()).orElse(BigDecimal.ZERO));
                shopOrderReportDto.setShop_waidaiPrice(Optional.ofNullable(shopOrderReportDto.getShop_waidaiPrice()).orElse(BigDecimal.ZERO));
                shopOrderReportDto.setShop_waimaiPrice(Optional.ofNullable(shopOrderReportDto.getShop_waimaiPrice()).orElse(BigDecimal.ZERO));
                shopOrderReportDto.setOrderPosDiscountMoney(Optional.ofNullable(shopOrderReportDto.getOrderPosDiscountMoney()).orElse(BigDecimal.ZERO));
                shopOrderReportDto.setMemberDiscountMoney(Optional.ofNullable(shopOrderReportDto.getMemberDiscountMoney()).orElse(BigDecimal.ZERO));
                shopOrderReportDto.setRealEraseMoney(Optional.ofNullable(shopOrderReportDto.getRealEraseMoney()).orElse(BigDecimal.ZERO));
                shopOrderReportDto.setShopDetailId(shopDetail.getId());
                shopOrderReportDto.setShopName(shopDetail.getName());
                shopOrderReportDtoLists.add(shopOrderReportDto);
            });
        }
        return shopOrderReportDtoLists;

    }

    @Override
    public List<OrderNumDto> selectOrderNumByTimeAndBrandId(String brandId, String begin, String end) {
        Date beginDate = DateUtil.getformatBeginDate(begin);
        Date endDate = DateUtil.getformatEndDate(end);
        return orderMapperReport.selectOrderNumByTimeAndBrandId(brandId, beginDate, endDate);
    }

    @Override
    public JSONResult afterPayShareBenefits(String orderId) {
        JSONResult jsonResult = new JSONResult();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        jsonResult.setSuccess(true);
        jsonResult.setData(order);
        return jsonResult;
    }

    @Override
    public void sendPosNewOrder(String shopId, Order order) {
        //  查询最新的店铺设置，防止缓存作怪
        ShopDetail shopDetail = shopDetailService.selectById(shopId);
        log.info("\n\n  【sendPosNewOrder】" + shopDetail.getName() + "\n     PosVersion：" + shopDetail.getPosVersion() + "\n    " + order.getId());
        if (shopDetail.getPosVersion() == PosVersion.VERSION_2_0) {
            sendInfoToNewPos(order);
        }
    }


    /**
     * 下单或者买单向NewPos推送订单
     * @param order
     */
    private void sendInfoToNewPos(Order order) {
        ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
        if (Common.YES.equals(shopDetail.getIsOpenEmqPush())) {
            //开启了新版推送
            mqttMQMessageProducer.sendCreateOrderToNewPosMessage(order, null);
        } else {
            mqMessageProducer.sendCreateOrderMessage(order, null);
        }
    }

    @Override
    public ShopIncomeDto selectWechatMoney(String beginDate, String endDate, List<String> shopIdlist) {
        return orderMapperReport.selectWechatMoney(beginDate, endDate, shopIdlist);
    }

    @Override
    public List<WeChatBill> uploadWeChatBill(String beginDate, String endDate, String shopId) {
        return orderMapperReport.uploadWeChatBill(beginDate, endDate, shopId);
    }

    @Override
    public List<Order> selectLaifuShi() {
        return orderMapper.selectLaifuShi();
    }

    /**
     * @Description:查询所有可以开票的订单
     * @Author:disvenk.dai
     * @Date:19:44 2018/6/4 0004
     */
    @Override
    public List<EnabelTicketOrderDto> getEnableTicketOrder(String customerId) {
        List<EnabelTicketOrderDto> orders = orderMapper.selectEnableTicketOrder(customerId);
        List<Receipt> receipts = new ArrayList<>();
        if (!orders.isEmpty()) {
            orders.forEach(n -> {
                try {
                    Receipt receipt = new Receipt();
                    receipt.setOrderNumber(n.getSerialNumber());
                    receipt.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(n.getCreateTime()));
                    receipt.setOrderMoney(n.getOrderMoney());
                    receipt.setReceiptMoney(n.getPayValue());
                    receipt.setTicketType(1);
                    receipt.setState(0);
                    receipt.setCreateTime(new Date());
                    receipts.add(receipt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
            receiptMapper.addTickets(receipts);
        }
        return orders;
    }

    @Override
    public Order selectOrderInfo(String orderId) {
        Order order = orderMapper.selectByOrderId(orderId);
        return order;
    }

    @Override
    public brandArticleReportDto callBrandArticleNumNew(String beginDate, String endDate, String brandId, String brandName) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        //菜品总数单独算是因为 要出去套餐的数量
        Integer totalNums = orderMapperReport.selectBrandArticleNumNew(begin, end, brandId);
        //查询菜品总额，退菜总数，退菜金额
        brandArticleReportDto bo = new brandArticleReportDto(brandName, 0, BigDecimal.ZERO, 0, BigDecimal.ZERO, BigDecimal.ZERO, 0, BigDecimal.ZERO);
        List<brandArticleReportDto> articleReportDto = orderMapperReport.selectConfirmMoneyNew(begin, end, brandId);
        if (articleReportDto != null && !articleReportDto.isEmpty()) {
            for (brandArticleReportDto reportDto : articleReportDto) {
                bo.setSellIncome(bo.getSellIncome().add(reportDto.getSellIncome()));
                bo.setRefundCount(bo.getRefundCount() + reportDto.getRefundCount());
                bo.setDiscountTotal(bo.getDiscountTotal().add(reportDto.getDiscountTotal()));
                bo.setRefundTotal(bo.getRefundTotal().add(reportDto.getRefundTotal()));
            }
        }
        bo.setTotalNum(totalNums);
        return bo;
    }

    @Override
    public Integer selectGrantCountByDate(String beginDate, String endDate) {
        return orderMapper.selectGrantCountByDate(beginDate, endDate);
    }

    @Override
    public List<OrderPaymentItem> selectBusinessReport(Map<String, Object> selectMap) {
        return orderMapperReport.selectBusinessReport(selectMap);
    }
}
