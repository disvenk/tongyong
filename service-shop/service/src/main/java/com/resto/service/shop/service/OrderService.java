package com.resto.service.shop.service;

import com.resto.api.brand.define.api.*;
import com.resto.api.brand.dto.*;
import com.resto.api.brand.util.JSONResult;
import com.resto.api.brand.util.LogUtils;
import com.resto.api.brand.util.Result;
import com.resto.api.brand.util.WeChatPayUtils;
import com.resto.api.shop.util.Common;
import com.resto.conf.redis.RedisService;
import com.resto.conf.util.AliPayUtils;
import com.resto.conf.util.ApplicationUtils;
import com.resto.conf.util.DateUtil;
import com.resto.conf.util.MQSetting;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.constant.*;
import com.resto.service.shop.datasource.DataSourceContextHolder;
import com.resto.service.shop.entity.*;
import com.resto.service.shop.exception.AppException;
import com.resto.service.shop.mapper.ArticlePriceMapper;
import com.resto.service.shop.mapper.MealAttrMapper;
import com.resto.service.shop.mapper.OrderMapper;
import com.resto.service.shop.producer.MQMessageProducer;
import com.resto.service.shop.util.LogTemplateUtils;
import com.resto.service.shop.util.UserActionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

import static com.resto.conf.util.HttpClient.doPost;
import static com.resto.conf.util.HttpClient.doPostAnsc;

/**
 * Created by bruce on 2018-01-03 17:31
 */
@Service
public class OrderService extends BaseService<Order, String> {

    private static final String NUMBER = "0123456789";

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderPaymentItemService orderPaymentItemService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private GetNumberService getNumberService;
    @Autowired
    private BrandApi brandService;
    @Autowired
    private BrandApiShopDetail shopDetailService;
    @Autowired
    private BrandApiBrandSetting brandSettingService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticlePriceService articlePriceService;
    @Autowired
    private RedisService redis;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private MealItemService mealItemService;
    @Autowired
    private MealAttrMapper mealAttrMapper;
    @Autowired
    private RedPacketService redPacketService;
    @Autowired
    private ChargeOrderService chargeOrderService;
    @Autowired
    private BrandApiWxServerConfig wxServerConfigService;
    @Autowired
    private ArticlePriceMapper articlePriceMapper;
    @Autowired
    private BrandApiWechatConfig wechatConfigService;

    public BaseDao<Order, String> getDao() {
        return orderMapper;
    }

    public List<Order> listOrder(Integer start, Integer datalength, String shopId, String customerId, String orderState) {
        String[] states = null;
        if (orderState != null) {
            states = orderState.split(",");
        }
        return orderMapper.orderList(start, datalength, shopId, customerId, states);
    }

    public List<Order> selectReadyOrder(String currentShopId) {
        return orderMapper.selectReadyList(currentShopId);
    }

    public Order pushOrder(String orderId) throws AppException {
        String time = DateUtil.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss");
        Order order = selectById(orderId);
        //如果是后付款模式 不验证直接进行修改模式
        if (order.getOrderMode() == ShopModeDto.HOUFU_ORDER) {
            logger.info("后付款模式：pushOrder修改生产状态：" + ProductionStatus.HAS_ORDER + "订单id为：" + orderId + "当前时间为：" + time);
            order.setProductionStatus(ProductionStatus.HAS_ORDER);
            order.setPushOrderTime(new Date());
            update(order);
//        }
        } else if (validOrderCanPush(order)) {
            logger.info("pushOrder时候支付宝支付修改状态：" + ProductionStatus.HAS_ORDER + "订单id为：" + orderId + "当前时间为：" + time);
            order.setProductionStatus(ProductionStatus.HAS_ORDER);
            order.setPushOrderTime(new Date());
            update(order);
            return order;
        }
        return order;
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
            case ShopModeDto.BOSS_ORDER:
                if (order.getPayType() == PayType.PAY) {
                    if (order.getOrderState() != OrderState.PAYMENT || ProductionStatus.NOT_ORDER != order.getProductionStatus()) {
                        logger.error("立即下单失败: " + order.getId());
                        throw new AppException(AppException.ORDER_STATE_ERR);
                    }
                } else if (order.getPayType() == PayType.NOPAY) {
                    if (order.getOrderState() != OrderState.SUBMIT || ProductionStatus.NOT_ORDER != order.getProductionStatus()) {
                        logger.error("立即下单失败: " + order.getId());
                        throw new AppException(AppException.ORDER_STATE_ERR);
                    }
                }
                break;
            case ShopModeDto.CALL_NUMBER:
                if (order.getOrderState() != OrderState.PAYMENT || ProductionStatus.NOT_ORDER != order.getProductionStatus()) {
                    logger.error("立即下单失败: " + order.getId());
                    throw new AppException(AppException.ORDER_STATE_ERR);
                }
                break;
            case ShopModeDto.MEISHI:
                if (order.getOrderState() != OrderState.PAYMENT || ProductionStatus.NOT_ORDER != order.getProductionStatus()) {
                    logger.error("立即下单失败: " + order.getId());
                    throw new AppException(AppException.ORDER_STATE_ERR);
                }
                break;
            case ShopModeDto.MANUAL_ORDER:
                if (order.getOrderState() != OrderState.PAYMENT || ProductionStatus.NOT_ORDER != order.getProductionStatus()) {
                    logger.error("立即下单失败: " + order.getId());
                    throw new AppException(AppException.ORDER_STATE_ERR);
                }
                break;
            default:
                break;
        }
        return true;
    }

    public Order selectOrderStatesById(String orderId) {
        return orderMapper.selectOrderStatesById(orderId);
    }

    public Order getOrderInfo(String orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            return null;
        }
        List<OrderItem> orderItems = orderItemService.listByOrderId(orderId);
        order.setOrderItems(orderItems);
        Customer cus = customerService.selectById(order.getCustomerId());
        order.setCustomer(cus);
        return order;
    }

    public List<Order> selectByParentId(String parentOrderId, Integer parentOrderPayType) {
        return orderMapper.selectByParentId(parentOrderId, parentOrderPayType);
    }

    public BigDecimal selectPayBefore(String orderId) {
        return orderMapper.selectPayBefore(orderId);
    }

    public JSONResult createOrder(Order order) throws AppException {
        JSONResult jsonResult = new JSONResult();
        String orderId = ApplicationUtils.randomUUID();
        order.setId(orderId);
        order.setPosDiscount(new BigDecimal(1));
        Customer customer = customerService.selectById(order.getCustomerId());

        if (customer == null && "wechat".equals(order.getCreateOrderByAddress())) {
            throw new AppException(AppException.CUSTOMER_NOT_EXISTS);
        } else if (order.getOrderItems().isEmpty()) {
            throw new AppException(AppException.ORDER_ITEMS_EMPTY);
        }
//        if(customer != null) {
//            if (!MemcachedUtils.add(customer.getId() + "createOrder", 1, 30)) {
//                jsonResult.setSuccess(false);
//                jsonResult.setMessage("下单过于频繁，请稍后再试！");
//                return jsonResult;
//            }
//        }

        if (!StringUtils.isEmpty(order.getTableNumber()) && order.getTableNumber().length() > 5) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("桌号异常,请扫码正确的二维码！");
            return jsonResult;
        }

        BrandDto brand = brandService.selectById(order.getBrandId());
        ShopDetailDto shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        BrandSettingDto brandSetting = brandSettingService.selectByBrandId(brand.getId());
        if (order.getOrderItems().isEmpty()) {
            throw new AppException(AppException.ORDER_ITEMS_EMPTY);
        }

        if (brandSetting.getIsUseServicePrice().equals(Common.YES) && shopDetail.getIsUseServicePrice().equals(Common.YES)
                && (order.getCustomerCount() == null || order.getCustomerCount() == 0)
                && order.getDistributionModeId() == DistributionType.RESTAURANT_MODE_ID) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("请输入就餐人数！");
            return jsonResult;
        }

        if (!StringUtils.isEmpty(order.getTableNumber())) { //如果存在桌号
            int orderCount = orderMapper.checkTableNumber(order.getShopDetailId(), order.getTableNumber(), order.getCustomerId(), brandSetting.getCloseContinueTime());
            if (orderCount > 0) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("不好意思，这桌有人了");
                return jsonResult;
            }
        } else if ((order.getDistributionModeId() == 3 && shopDetail.getContinueOrderScan() == 1 && StringUtils.isEmpty(order.getTableNumber()) && shopDetail.getShopMode() == ShopModeDto.BOSS_ORDER)
                || order.getDistributionModeId() == 1 && StringUtils.isEmpty(order.getTableNumber()) && shopDetail.getShopMode() == ShopModeDto.BOSS_ORDER) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("桌号不得为空");
            return jsonResult;
        }
        if (!StringUtils.isEmpty(order.getParentOrderId())) {  //如果是加菜订单
            Order farOrder = orderMapper.selectByPrimaryKey(order.getParentOrderId());
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
        } else if (customer == null && order.getOrderMode() == ShopModeDto.CALL_NUMBER && order.getTableNumber() != null) {
            order.setVerCode(order.getTableNumber());
        } else {
            if (StringUtils.isEmpty(order.getParentOrderId())) {
                if (StringUtils.isEmpty(order.getVerCode())) {
                    order.setVerCode(generateString(5));
                }

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
        doPostAnsc(LogUtils.url, map);
        if (customer != null) {
            Map customerMap = new HashMap(4);
            customerMap.put("brandName", brand.getBrandName());
            customerMap.put("fileName", customer.getId());
            customerMap.put("type", "UserAction");
            customerMap.put("content", "用户:" + customer.getNickname() + "创建了订单Id为:" + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(LogUtils.url, customerMap);
        }
        BigDecimal totalMoney = BigDecimal.ZERO;
        BigDecimal originMoney = BigDecimal.ZERO;
        int articleCount = 0;
        BigDecimal extraMoney = BigDecimal.ZERO;

//记录订单菜品-------------------------------
        for (OrderItem item : order.getOrderItems()) {
            Article a = null;
            BigDecimal org_price = null;
            int mealFeeNumber = 0;
            BigDecimal price = null;
            BigDecimal fans_price = null;
            item.setId(ApplicationUtils.randomUUID());
            String remark = "";
            switch (item.getType()) {
                case OrderItemType.ARTICLE://无规格单品
                    // 查出 item对应的 商品信息，并将item的原价，单价，总价，商品名称，商品详情 设置为对应的
                    a = articleMap.get(item.getArticleId());
                    item.setArticleName(a.getName());
                    org_price = a.getPrice();
                    if (a.getOpenCatty() == 0) {
                        price = discount(a.getPrice(), a.getDiscount(), item.getDiscount(), a.getName());                      //计算折扣
                        if (a.getDiscount() != 100) {
                            fans_price = discount(a.getPrice(), a.getDiscount(), item.getDiscount(), a.getName());       //计算折扣 （update：粉丝价 更改为 原价*折扣  2017年4月18日 14:08:04  ---lmx）
                        } else {
                            fans_price = a.getFansPrice();
                        }
                    } else {
                        price = item.getPrice();
                        fans_price = item.getPrice();
                    }
                    mealFeeNumber = a.getMealFeeNumber() == null ? 0 : a.getMealFeeNumber();
                    remark = a.getDiscount() + "%";          //设置菜品当前折扣
                    break;
                case OrderItemType.RECOMMEND://推荐餐品
                    // 查出 item对应的 商品信息，并将item的原价，单价，总价，商品名称，商品详情 设置为对应的
                    a = articleMap.get(item.getArticleId());
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
                    a = articleMap.get(item.getArticleId());
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
                    } else if (a.getFansPrice() != null && a.getFansPrice().doubleValue() > 0) {
                        org_price = item.getPrice().subtract(a.getFansPrice()).add(a.getPrice());
                    } else {
                        org_price = item.getPrice();
                    }
                    price = item.getPrice();
                    fans_price = item.getPrice();
                    mealFeeNumber = a.getMealFeeNumber() == null ? 0 : a.getMealFeeNumber();
                    break;
                case OrderItemType.SETMEALS://套餐主品
                    a = articleMap.get(item.getArticleId());
                    if (a.getIsEmpty()) {
                        jsonResult.setSuccess(false);
                        jsonResult.setMessage("菜品供应时间变动，请重新购买");
                        return jsonResult;
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
                    List<MealAttr> mealAttrs = mealAttrMapper.selectList(item.getArticleId());
                    boolean checkMeal = true;
                    for (MealAttr mealAttr : mealAttrs) {
                        if (mealAttr.getChoiceType() == 0) {
                            //必选
                            List<MealItem> mealItems = mealItemService.selectByAttrId(mealAttr.getId());
                            //找到这个属性下所有的菜品
                            int count = 0;
                            for (MealItem mealItem : mealItems) {
                                if(redis.get(mealItem.getArticleId() + Common.KUCUN)!=null){
                                    Integer redisCount = Integer.parseInt(redis.get(mealItem.getArticleId() + Common.KUCUN));
                                    if (redisCount == null) {
                                        Article article = articleService.selectById(mealItem.getArticleId());
                                        redisCount = article.getCurrentWorkingStock();
                                    }
                                    if (redisCount > 0) {
                                        count++;
                                    }
                                }
                            }
                            if (count < mealAttr.getChoiceCount()) {
                                checkMeal = false;
                            }
                        }
                    }
                    if (!checkMeal) {
                        jsonResult.setSuccess(false);
                        jsonResult.setMessage("万分抱歉,您购买的套餐" + item.getName() + "已售罄,请重新下单");
                        articleService.setEmpty(item.getArticleId());
                        if (customer != null) {
                            shopCartService.deleteCustomerArticle(customer.getId(), item.getArticleId());
                        }
                        return jsonResult;
                    }
                    List<MealItem> items = mealItemService.selectByIds(mealItemIds);

                    item.setChildren(new ArrayList<OrderItem>());
                    mealFeeNumber = a.getMealFeeNumber() == null ? 0 : a.getMealFeeNumber();
                    for (MealItem mealItem : items) {
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
                        child.setUnitPrice(mealItem.getPriceDif());
                        child.setBaseUnitPrice(mealItem.getPriceDif());
                        child.setType(OrderItemType.MEALS_CHILDREN);
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
            if (a.getOpenCatty() == 1 && OrderItemType.ARTICLE == item.getType()) {
                item.setOriginalPrice(item.getPrice());
            } else {
                item.setOriginalPrice(org_price);
            }
            item.setStatus(1);
            item.setSort(0);
            if ("0%".equals(remark)) {
                jsonResult.setSuccess(false);
                jsonResult.setMessage(a.getName() + "供应时间发生改变，请重新购买");
                return jsonResult;
            }
            item.setRemark(remark);
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

            Result check = new Result();
            if (item.getType() == OrderItemType.ARTICLE) {
                check = checkArticleList(item, item.getCount());
            } else if (item.getType() == OrderItemType.UNITPRICE) {
                check = checkArticleList(item, item.getCount());
            } else if (item.getType() == OrderItemType.SETMEALS) {
                check = checkArticleList(item, articleCount);
            } else if (item.getType() == OrderItemType.UNIT_NEW) {
                check = checkArticleList(item, item.getCount());
            } else if (item.getType() == OrderItemType.RECOMMEND) {
                check = checkArticleList(item, item.getCount());
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

        if (customer != null) {
            ShopDetailDto detail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
            if (order.getWaitId() != null && !"".equals(order.getWaitId())) {
                OrderPaymentItem item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(orderId);
                item.setPaymentModeId(PayMode.WAIT_MONEY);
                item.setPayTime(order.getCreateTime());
                item.setPayValue(order.getWaitMoney());
                item.setRemark("等位红包支付:" + order.getWaitMoney());
                item.setResultData(order.getWaitId());
                if (order.getWaitMoney().doubleValue() > 0) {
                    orderPaymentItemService.insert(item);
                }
//            UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
//                    "订单使用等位红包支付了：" + item.getPayValue());
//                Map waitPayMap = new HashMap(4);
//                waitPayMap.put("brandName", brand.getBrandName());
//                waitPayMap.put("fileName", order.getId());
//                waitPayMap.put("type", "orderAction");
//                waitPayMap.put("content", "订单:"+order.getId()+"使用等位红包支付了：" + item.getPayValue() +",请求服务器地址为:" + MQSetting.getLocalIP());
//                doPostAnsc(url, waitPayMap);
//                Map CustomerWaitPayMap = new HashMap(4);
//                CustomerWaitPayMap.put("brandName", brand.getBrandName());
//                CustomerWaitPayMap.put("fileName", customer.getId());
//                CustomerWaitPayMap.put("type", "UserAction");
//                CustomerWaitPayMap.put("content", "用户:"+customer.getNickname()+"使用等位红包支付了：" + item.getPayValue() +"订单Id为:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
//                doPostAnsc(url, CustomerWaitPayMap);
                LogTemplateUtils.getWaitMoneyLogByOrderType(brand.getBrandName(), order.getId(), item.getPayValue());
                LogTemplateUtils.getWaitMoneyLogByUserType(brand.getBrandName(), order.getId(), item.getPayValue(), customer.getNickname());

                GetNumber getNumber = getNumberService.selectById(order.getWaitId());
                logger.error(order.getWaitId() + "-----------222222222222222");
                getNumber.setState(WaitModerState.WAIT_MODEL_NUMBER_THREE);
                getNumberService.update(getNumber);
            }

            payMoney = payMoney.subtract(order.getWaitMoney());

            if (detail.getShopMode() != ShopModeDto.HOUFU_ORDER && order.getPayType() != PayType.NOPAY) {
                if (order.getUseCoupon() != null && order.getParentOrderId() == null) {
                    Coupon coupon = couponService.useCoupon(totalMoney, order);
                    OrderPaymentItem item = new OrderPaymentItem();
                    item.setId(ApplicationUtils.randomUUID());
                    item.setOrderId(orderId);
                    item.setPaymentModeId(PayMode.COUPON_PAY);
                    item.setPayTime(order.getCreateTime());
                    item.setPayValue(coupon.getValue());
                    item.setRemark("优惠券支付:" + item.getPayValue());
                    item.setResultData(coupon.getId());
                    orderPaymentItemService.insert(item);
//                UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
//                        "订单使用优惠券支付了：" + item.getPayValue());
//                    Map couponPaymap = new HashMap(4);
//                    couponPaymap.put("brandName", brand.getBrandName());
//                    couponPaymap.put("fileName", order.getId());
//                    couponPaymap.put("type", "orderAction");
//                    couponPaymap.put("content", "订单:"+order.getId()+"订单使用优惠券支付了：" + item.getPayValue() +",请求服务器地址为:" + MQSetting.getLocalIP());
//                    doPostAnsc(url, couponPaymap);
//                    Map CustomerCouponPaymap = new HashMap(4);
//                    CustomerCouponPaymap.put("brandName", brand.getBrandName());
//                    CustomerCouponPaymap.put("fileName", customer.getId());
//                    CustomerCouponPaymap.put("type", "UserAction");
//                    CustomerCouponPaymap.put("content", "用户:"+customer.getNickname()+"使用优惠券支付了：" + item.getPayValue() +"订单Id为:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
//                    doPostAnsc(url, CustomerCouponPaymap);
                    LogTemplateUtils.getCouponByOrderType(brand.getBrandName(), order.getId(), item.getPayValue());
                    LogTemplateUtils.getCouponByUserType(brand.getBrandName(), customer.getId(), customer.getNickname(), item.getPayValue());
                    payMoney = payMoney.subtract(item.getPayValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                // 使用余额
                if (payMoney.doubleValue() > 0 && order.isUseAccount() && order.getPayType() != PayType.NOPAY) {
                    BigDecimal payValue = accountService.payOrder(order, payMoney, customer, brand, shopDetail);
                    logger.info("这里用户使用了余额");
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
//            UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
//                    "订单使用银联支付了：" + item.getPayValue());
//                Map cartPayMap = new HashMap(4);
//                cartPayMap.put("brandName", brand.getBrandName());
//                cartPayMap.put("fileName", order.getId());
//                cartPayMap.put("type", "orderAction");
//                cartPayMap.put("content", "订单:"+order.getId()+"订单使用银联支付了：" + item.getPayValue() +",请求服务器地址为:" + MQSetting.getLocalIP());
//                doPostAnsc(url, cartPayMap);
                LogTemplateUtils.getBankByOrderType(brand.getBrandName(), order.getId(), item.getPayValue());

//                Map CustomerCartPayMap = new HashMap(4);
//                CustomerCartPayMap.put("brandName", brand.getBrandName());
//                CustomerCartPayMap.put("fileName", customer.getId());
//                CustomerCartPayMap.put("type", "UserAction");
//                CustomerCartPayMap.put("content", "用户:"+customer.getNickname()+"使用银联支付了：" + item.getPayValue() +"订单Id为:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
//                doPostAnsc(url, CustomerCartPayMap);
                LogTemplateUtils.getBankByUserType(brand.getBrandName(), customer.getId(), customer.getNickname(), item.getPayValue());
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
//            UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
//                    "订单使用银联支付了：" + item.getPayValue());
//                Map crashPayMap = new HashMap(4);
//                crashPayMap.put("brandName", brand.getBrandName());
//                crashPayMap.put("fileName", order.getId());
//                crashPayMap.put("type", "orderAction");
//                crashPayMap.put("content", "订单:"+order.getId()+"订单使用现金支付了：" + item.getPayValue() +",请求服务器地址为:" + MQSetting.getLocalIP());
//                doPostAnsc(url, crashPayMap);
                LogTemplateUtils.getMoneyByOrderType(brand.getBrandName(), order.getId(), item.getPayValue());
                LogTemplateUtils.getMoneyByUserType(brand.getBrandName(), customer.getId(), customer.getNickname(), item.getPayValue());

//                Map CustomerCrashPayMap = new HashMap(4);
//                CustomerCrashPayMap.put("brandName", brand.getBrandName());
//                CustomerCrashPayMap.put("fileName", customer.getId());
//                CustomerCrashPayMap.put("type", "UserAction");
//                CustomerCrashPayMap.put("content", "用户:"+customer.getNickname()+"使用现金支付了：" + item.getPayValue() +"订单Id为:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
//                doPostAnsc(url, CustomerCrashPayMap);
                order.setAllowContinueOrder(false);
            } else if (payMoney.compareTo(BigDecimal.ZERO) > 0 && order.getPayMode() == OrderPayMode.SHH_PAY) {
                OrderPaymentItem item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(orderId);
                item.setPaymentModeId(PayMode.SHANHUI_PAY);
                item.setPayTime(order.getCreateTime());
                item.setPayValue(payMoney);
                item.setRemark("闪惠支付:" + item.getPayValue());
                orderPaymentItemService.insert(item);
                UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
                        "订单使用银联支付了：" + item.getPayValue());
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
                doPostAnsc(LogUtils.url, crashPayMap);
                Map CustomerCrashPayMap = new HashMap(4);
                CustomerCrashPayMap.put("brandName", brand.getBrandName());
                CustomerCrashPayMap.put("fileName", customer.getId());
                CustomerCrashPayMap.put("type", "UserAction");
                CustomerCrashPayMap.put("content", "用户:" + customer.getNickname() + "使用会员支付了：" + item.getPayValue() + "订单Id为:" + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(LogUtils.url, CustomerCrashPayMap);
                order.setAllowContinueOrder(false);
            }

            if (payMoney.doubleValue() < 0) {
                payMoney = BigDecimal.ZERO;
            }
            //yz 记录订单支付项 2017-03-27
            order.setAccountingTime(order.getCreateTime()); // 财务结算时间

            order.setAllowCancel(true); // 订单是否允许取消
            order.setAllowAppraise(false);
            order.setArticleCount(articleCount); // 订单餐品总数
            order.setClosed(false); // 订单是否关闭 否
            order.setSerialNumber(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSSS")); // 流水号
            order.setOriginalAmount(originMoney.add(order.getServicePrice()).add(order.getMealFeePrice()).add(extraMoney));// 原价
            order.setReductionAmount(BigDecimal.ZERO);// 折扣金额
            order.setOrderMoney(totalMoney.add(order.getServicePrice()).add(order.getMealFeePrice())); // 订单实际金额
            order.setPaymentAmount(payMoney); // 订单剩余需要维修支付的金额
            order.setPrintTimes(0);

            order.setOrderMode(detail.getShopMode());
            if (order.getOrderMode() == ShopModeDto.CALL_NUMBER || order.getOrderMode() == ShopModeDto.MEISHI) {
                order.setTableNumber(order.getVerCode());
            }

//        if(!order.getOrderMode().equals(ShopModeDto.HOUFU_ORDER)){
            if (!StringUtils.isEmpty(order.getTableNumber())) {
                if (order.getParentOrderId() != null) {
                    Order parentOrder = selectById(order.getParentOrderId());
                    order.setTableNumber(parentOrder.getTableNumber());
                    order.setVerCode(parentOrder.getVerCode());
                    order.setCustomerCount(parentOrder.getCustomerCount());
                } else {
                    Order lastOrder = orderMapper.getLastOrderByCustomer(customer.getId(), order.getShopDetailId(), brandSetting.getCloseContinueTime());
                    if (lastOrder != null && lastOrder.getParentOrderId() != null) {
                        Order parent = orderMapper.selectByPrimaryKey(lastOrder.getParentOrderId());
                        if (parent != null && parent.getAllowContinueOrder()) {
                            order.setParentOrderId(parent.getId());
                            order.setTableNumber(parent.getTableNumber());
                            order.setVerCode(parent.getVerCode());
                            order.setCustomerCount(parent.getCustomerCount());
                        }
                    } else {
                        if (lastOrder != null && lastOrder.getAllowContinueOrder()) {
                            order.setParentOrderId(lastOrder.getId());
                            Order parentOrder = selectById(order.getParentOrderId());
                            order.setTableNumber(parentOrder.getTableNumber());
                            order.setVerCode(parentOrder.getVerCode());
                            order.setCustomerCount(parentOrder.getCustomerCount());
                        }
                    }
                }
            }
            //判断是否是后付款模式
            if (order.getOrderMode() == ShopModeDto.HOUFU_ORDER) {
                order.setOrderState(OrderState.SUBMIT);
                order.setProductionStatus(ProductionStatus.NOT_ORDER);
                if (order.getDistributionModeId() != 3) {
                    order.setAllowContinueOrder(true);
                }
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
            } else if (order.getDistributionModeId() != DistributionType.TAKE_IT_SELF && order.getOrderMode() == ShopModeDto.TABLE_MODE
                    && StringUtils.isEmpty(order.getTableNumber())) {
                order.setNeedScan(Common.YES);
            } else if (order.getDistributionModeId() != DistributionType.TAKE_IT_SELF && order.getOrderMode() == ShopModeDto.HOUFU_ORDER
                    && StringUtils.isEmpty(order.getTableNumber())) {
                order.setNeedScan(Common.YES);
            }

            if (order.getOrderMode() == ShopModeDto.MANUAL_ORDER) {
                order.setNeedScan(Common.YES);
            }
            insert(order);
            customerService.changeLastOrderShop(order.getShopDetailId(), order.getCustomerId());
            if (order.getPaymentAmount().doubleValue() == 0) {
                payOrderSuccess(order);
            }

            jsonResult.setData(order);
            if (order.getOrderMode() == ShopModeDto.HOUFU_ORDER) {
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
            } else if (order.getPayType() == PayType.NOPAY && order.getOrderMode() == ShopModeDto.BOSS_ORDER) {
                if (order.getParentOrderId() != null) {  //子订单
                    Order parent = selectById(order.getParentOrderId());
                    int articleCountWithChildren = orderMapper.selectArticleCountByIdBossOrder(parent.getId());
                    if (parent.getLastOrderTime() == null || parent.getLastOrderTime().getTime() < order.getCreateTime().getTime()) {
                        parent.setLastOrderTime(order.getCreateTime());
                    }
                    Double amountWithChildren = orderMapper.selectParentAmountByBossOrder(parent.getId());
                    parent.setCountWithChild(articleCountWithChildren);
                    parent.setAmountWithChildren(new BigDecimal(amountWithChildren));
                    update(parent);
                }
            }
        } else {
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

            order.setOrderMode(shopDetail.getShopMode());
            order.setProductionStatus(ProductionStatus.NOT_ORDER);
            insert(order);
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

    private int selectArticleCountById(String id, Integer shopMode) {
        return orderMapper.selectArticleCountById(id, shopMode);
    }

    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
        }
        return sb.toString();
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

    private Result checkStock(OrderItem orderItem, int count) {
        Boolean result = false;
        int current = 0;
        String msg = "";
        int min = 0;
        int endMin = 10000;
        Integer ck=null;
        if(redis.get(orderItem.getArticleId() + Common.KUCUN)!=null){
             ck = Integer.parseInt(redis.get(orderItem.getArticleId() + Common.KUCUN));
        }
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
                    Integer cck=null;
                    if(redis.get(oi.getArticleId() + Common.KUCUN)!=null){
                         cck = Integer.parseInt(redis.get(oi.getArticleId() + Common.KUCUN));
                    }
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
                result = endMin >= count && current >= count;
                msg = endMin == 0 ? orderItem.getArticleName() + "套餐单品已售罄,请取消订单后重新下单" :
                        endMin >= count && current >= count ? "库存足够" : orderItem.getArticleName() + "中单品库存不足,最大购买" + endMin + "个,请重新选购餐品";
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
                logger.debug("未知菜品分类");
                break;
        }
        return new Result(msg, result);
    }

    public Order payOrderSuccess(Order order) {
        if (order.getOrderMode() != ShopModeDto.HOUFU_ORDER) {
            order.setOrderState(OrderState.PAYMENT);
            order.setIsPay(OrderPayState.PAYED);
            update(order);
        }

        if (order.getOrderMode() == ShopModeDto.BOSS_ORDER && order.getParentOrderId() == null && order.getPayType() == PayType.NOPAY) {
            updateChild(order);
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
            try {
                order = pushOrder(order.getId());
                logger.info("子订单，自动下单成功：" + order.getId());
                MQMessageProducer.sendPlaceOrderMessage(order);
            } catch (AppException e) {
                e.printStackTrace();
                logger.error("子订单自动下单失败:" + e.getMessage());
                changePushOrder(order);
            }
        }
        return order;
    }

    public void changePushOrder(Order order) {
        order = selectById(order.getId());
        if (order.getProductionStatus() == ProductionStatus.HAS_ORDER) { //如果还是已下单状态，则将订单状态改为未下单,并且订单改为可以取消
            orderMapper.clearPushOrder(order.getId(), ProductionStatus.NOT_ORDER);
        }
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

    public Order posDiscount(String orderId, BigDecimal discount, List<OrderItem> orderItems, BigDecimal eraseMoney, BigDecimal noDiscountMoney, Integer type) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order.getPosDiscount().compareTo(new BigDecimal(1)) == 0
                && order.getEraseMoney().compareTo(new BigDecimal(0)) == 0
                && order.getNoDiscountMoney().compareTo(new BigDecimal(0)) == 0) {
            order.setBaseOrderMoney(order.getAmountWithChildren().doubleValue() > 0 ? order.getAmountWithChildren() : order.getOrderMoney());
        }
        BigDecimal orderMoney = order.getBaseOrderMoney();
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
            order = posDiscountAction(oItems, discount, posDiscount, order, eraseMoney, noDiscountMoney, shijiMoney, flag);
            shijiMoney = shijiMoney.subtract(order.getOrderMoney());
            if (flag) {
                BigDecimal sum = new BigDecimal(0);
                for (int i = 0; i < pOrder.size(); i++) {
                    Order oP = pOrder.get(i);
                    map.clear();
                    map.put("orderId", oP.getId());
                    map.put("count", "1=1");
                    if ((i + 1) == pOrder.size()) {
                        oP = posDiscountAction(orderItemService.selectOrderItemByOrderId(map), discount, posDiscount, oP, eraseMoney, noDiscountMoney, shijiMoney, false);
                    } else {
                        oP = posDiscountAction(orderItemService.selectOrderItemByOrderId(map), discount, posDiscount, oP, eraseMoney, noDiscountMoney, shijiMoney, true);
                    }
                    shijiMoney = shijiMoney.subtract(oP.getOrderMoney());
                    sum = sum.add(oP.getOrderMoney());
                }
                //修改主订单
                order.setAmountWithChildren(order.getOrderMoney().add(sum));
                orderMapper.updateByPrimaryKeySelective(order);
            }
        }
        return order;
    }

    public Order posDiscountAction(List<OrderItem> orderItems, BigDecimal discount, BigDecimal posDiscount, Order order, BigDecimal eraseMoney,
                                   BigDecimal noDiscountMoney, BigDecimal shijiMoney, boolean flag) {
        ShopDetailDto shop = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        BrandSettingDto brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
        BigDecimal sum = new BigDecimal(0);
        //修改菜品项
        for (OrderItem oItem : orderItems) {
            oItem.setUnitPrice(oItem.getBaseUnitPrice().multiply(posDiscount).setScale(2, BigDecimal.ROUND_HALF_UP));
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
            orderMapper.updateByPrimaryKeySelective(order);
        }
        return order;
    }

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
        ShopDetailDto detail = shopDetailService.selectByPrimaryKey(old.getShopDetailId());
        if (order.getWaitMoney().doubleValue() > 0) {
            OrderPaymentItem item = new OrderPaymentItem();
            item.setId(ApplicationUtils.randomUUID());
            item.setOrderId(order.getId());
            item.setPaymentModeId(PayMode.WAIT_MONEY);
            item.setPayTime(order.getCreateTime());
            item.setPayValue(order.getWaitMoney());
            item.setRemark("等位红包支付:" + order.getWaitMoney());
            item.setResultData(order.getWaitId());
            orderPaymentItemService.insert(item);

            GetNumber getNumber = getNumberService.selectById(order.getWaitId());
            getNumber.setState(WaitModerState.WAIT_MODEL_NUMBER_THREE);
            getNumberService.update(getNumber);

            payment.subtract(order.getWaitMoney());
        }
        Customer customer = customerService.selectById(old.getCustomerId());


        if (detail.getShopMode() != ShopModeDto.HOUFU_ORDER) {
            if (order.getUseCoupon() != null) {
                Coupon coupon = couponService.useCoupon(orderMoney, old);
                OrderPaymentItem item = new OrderPaymentItem();
                item.setId(ApplicationUtils.randomUUID());
                item.setOrderId(order.getId());
                item.setPaymentModeId(PayMode.COUPON_PAY);
                item.setPayTime(order.getCreateTime());
                item.setPayValue(coupon.getValue());
                item.setRemark("优惠券支付:" + item.getPayValue());
                item.setResultData(coupon.getId());
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
        if (old.getOrderMode() == ShopModeDto.HOUFU_ORDER) {
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

    public boolean cancelOrder(String orderId) throws Exception {
        Order order = selectById(orderId);
        if (order.getAllowCancel() && order.getProductionStatus() != ProductionStatus.PRINTED && (order.getOrderState().equals(OrderState.SUBMIT) || order.getOrderState() == OrderState.PAYMENT)) {
            order.setAllowCancel(false);
            order.setClosed(true);
            order.setAllowAppraise(false);
            order.setAllowContinueOrder(false);
            order.setOrderState(OrderState.CANCEL);
            update(order);
            refundOrder(order);
            logger.info("取消订单成功:" + order.getId());

            //拒绝订单后还原库存
            Boolean addStockSuccess = false;
            addStockSuccess = addStock(getOrderInfo(orderId));
            if (!addStockSuccess) {
                logger.info("库存还原失败:" + order.getId());
            }
//            orderMapper.setStockBySuit(order.getShopDetailId());//自动更新套餐数量
            return true;
        } else {
            throw new Exception("取消订单失败，订单状态订单状态或者订单可取消字段为false" + order.getId());
        }
    }

    private void refundOrder(Order order) {
        List<OrderPaymentItem> payItemsList = orderPaymentItemService.selectByOrderId(order.getId());
        BrandDto brand = brandService.selectById(order.getBrandId());
        ShopDetailDto shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
//        UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
//                "订单已取消！");
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
                    couponService.refundCoupon(item.getResultData());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.ACCOUNT_PAY:
                    accountService.addAccount(item.getPayValue(), item.getResultData(), "取消订单返还", AccountLog.SOURCE_CANCEL_ORDER, order.getShopDetailId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.APPRAISE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getResultData());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.SHARE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getResultData());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.REFUND_ARTICLE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getResultData());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.THIRD_MONEY_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getResultData());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.REBATE_MONEY_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getResultData());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.CHARGE_PAY:
                    chargeOrderService.refundCharge(item.getPayValue(), item.getResultData(), order.getShopDetailId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.REWARD_PAY:
                    chargeOrderService.refundReward(item.getPayValue(), item.getResultData(), order.getShopDetailId());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                case PayMode.WEIXIN_PAY:
                    WechatConfigDto config = wechatConfigService.selectByBrandId(DataSourceContextHolder.getDataSourceName());
                    JSONObject obj = new JSONObject(item.getResultData());
                    int refund = obj.getInt("total_fee") + refundTotal;
                    Map<String, String> result = null;
                    if (shopDetail.getWxServerId() == null) {
                        result = WeChatPayUtils.refund(newPayItemId, obj.getString("transaction_id"),
                                obj.getInt("total_fee"), refund, config.getAppid(), config.getMchid(),
                                config.getMchkey(), config.getPayCertPath());
                    } else {
                        WxServerConfigDto wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());

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
                    BrandSettingDto brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
                    AliPayUtils.connection(StringUtils.isEmpty(shopDetail.getAliAppId()) ? brandSetting.getAliAppId() : shopDetail.getAliAppId().trim(),
                            StringUtils.isEmpty(shopDetail.getAliPrivateKey()) ? brandSetting.getAliPrivateKey().trim() : shopDetail.getAliPrivateKey().trim(),
                            StringUtils.isEmpty(shopDetail.getAliPublicKey()) ? brandSetting.getAliPublicKey().trim() : shopDetail.getAliPublicKey().trim());
                    Map map = new HashMap();
                    map.put("out_trade_no", order.getId());
                    map.put("refund_amount", aliPay.add(aliRefund));
                    map.put("out_request_no", newPayItemId);
                    String resultJson = AliPayUtils.refundPay(map);
                    if (new JSONObject(resultJson).toString().indexOf("ERROR") != -1) {
                        throw new RuntimeException("支付宝退款异常！" + resultJson.toString());
                    }
                    item.setResultData(new JSONObject(resultJson).toString());
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
                    accountService.addAccount(item.getPayValue(), item.getResultData(), "取消订单返还", AccountLog.SOURCE_CANCEL_ORDER, order.getShopDetailId());
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
        doPostAnsc(LogUtils.url, map);
//        if (!MemcachedUtils.add(order.getCustomerId() + "createOrder", 1, 30)) {
//            MemcachedUtils.delete(order.getCustomerId() + "createOrder");
//        }
    }

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
//            Integer articleCount = (Integer) RedisUtil.get(articleId + Common.KUCUN);
            Integer articleCount=null;
            if(redis.get(articleId + Common.KUCUN)!=null){
                 articleCount = Integer.parseInt(redis.get(articleId + Common.KUCUN));
            }
            switch (orderItem.getType()) {

                case OrderItemType.UNITPRICE:
                    //如果是有规格的单品信息，那么更新该规格的单品库存以及该单品的库存
                    ArticlePrice articlePrice = articlePriceMapper.selectByPrimaryKey(orderItem.getArticleId());
                    if (articleCount != null) {
//                        RedisUtil.set(articleId + Common.KUCUN, articleCount + 1);
                        redis.set(articleId + Common.KUCUN, String.valueOf(articleCount + orderItem.getCount()));
                        if (articleCount == 0) {
                            articlePriceService.setArticlePriceEmptyFail(articlePrice.getArticleId());
                        }
                    } else {
//                        RedisUtil.set(articleId + Common.KUCUN, articlePrice.getCurrentWorkingStock() + 1);
                        redis.set(articleId + Common.KUCUN, String.valueOf(articlePrice.getCurrentWorkingStock() + orderItem.getCount()));
                        if (articlePrice.getCurrentWorkingStock() == 0) {
                            articlePriceService.setArticlePriceEmptyFail(articlePrice.getArticleId());
                        }
                    }
                    Integer baseArticle=null;
                    if(redis.get(articlePrice.getArticleId() + Common.KUCUN)!=null){
                         baseArticle = Integer.parseInt(redis.get(articlePrice.getArticleId() + Common.KUCUN));
                    }
                    if (baseArticle != null) {
//                        RedisUtil.set(articlePrice.getArticleId() + Common.KUCUN, baseArticle + 1);
                        redis.set(articlePrice.getArticleId() + Common.KUCUN, String.valueOf(baseArticle + orderItem.getCount()));
                        if (baseArticle == 0) {
                            articleService.setEmptyFail(articlePrice.getArticleId());
                        }
                    } else {
                        Article base = articleService.selectById(articlePrice.getArticleId());
//                        RedisUtil.set(articlePrice.getArticleId() + Common.KUCUN, base.getCurrentWorkingStock() + 1);
                        redis.set(articlePrice.getArticleId() + Common.KUCUN, String.valueOf(base.getCurrentWorkingStock() + orderItem.getCount()));
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
//                        RedisUtil.set(articleId + Common.KUCUN, articleCount + 1);
                        redis.set(articleId + Common.KUCUN, String.valueOf(articleCount + orderItem.getCount()));
                        if (articleCount == 0) {
                            articleService.setEmptyFail(orderItem.getArticleId());
                        }
                    } else {
//                        RedisUtil.set(articleId + Common.KUCUN, article.getCurrentWorkingStock() + 1);
                        redis.set(articleId + Common.KUCUN, String.valueOf(article.getCurrentWorkingStock() + orderItem.getCount()));
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
//            Integer suit = (Integer) RedisUtil.get(article.getId()+Common.KUCUN);
//            if(suit != null){
//                if(suit == 0 && article.getCount() > 0){
//                    orderMapper.setEmptyFail(article.getId());
//                }
//                RedisUtil.set(article.getId()+Common.KUCUN,article.getCount());
//            }else{
//                if(article.getIsEmpty() && article.getCount() > 0){
//                    orderMapper.setEmptyFail(article.getId());
//                }
//                RedisUtil.set(article.getId()+Common.KUCUN,article.getCount());
//            }
//        }
        return true;
    }

    public Order findCustomerNewOrder(String customerId, String shopId, String orderId) {
        Date beginDate = DateUtil.getDateBegin(new Date());
        return findCustomerNewOrder(beginDate, customerId, shopId, orderId);
    }

    public Order findCustomerNewOrder(Date beginDate, String customerId, String shopId, String orderId) {
        Integer[] orderState = new Integer[]{OrderState.SUBMIT, OrderState.PAYMENT, OrderState.CONFIRM};
        Order order = orderMapper.findCustomerNewOrder(beginDate, customerId, shopId, orderState, orderId);
        if (order != null) {
            if (order.getParentOrderId() != null && (order.getOrderState() != OrderState.SUBMIT || order.getOrderMode() == ShopModeDto.HOUFU_ORDER
                    || (order.getOrderMode() == ShopModeDto.BOSS_ORDER && order.getPayType() == PayType.NOPAY))) {
                return findCustomerNewOrder(customerId, shopId, order.getParentOrderId());
            }
            List<OrderItem> itemList = orderItemService.listByOrderId(order.getId());
            order.setOrderItems(itemList);
            if (order.getOrderState() != OrderState.SUBMIT || order.getOrderMode() == ShopModeDto.HOUFU_ORDER
                    || (order.getOrderMode() == ShopModeDto.BOSS_ORDER && order.getPayType() == PayType.NOPAY)) {
                List<String> childIds = selectChildIdsByParentId(order.getId());
                List<OrderItem> childItems = orderItemService.listByOrderIds(childIds);
                order.getOrderItems().addAll(childItems);
            }

        }
        return order;
    }

    private List<String> selectChildIdsByParentId(String id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order.getOrderMode() == ShopModeDto.HOUFU_ORDER ||
                (order.getOrderMode() == ShopModeDto.BOSS_ORDER && order.getPayType() == PayType.NOPAY)) {
            return orderMapper.selectChildIdsByParentIdByFive(id);
        } else {
            return orderMapper.selectChildIdsByParentId(id);
        }
    }

    public Order findCustomerNewPackage(String currentCustomerId, String currentShopId) {
        String oid = orderMapper.selectNewCustomerPackageId(currentCustomerId, currentShopId);
        Order order = null;
        if (StringUtils.isNoneBlank(oid)) {
            Date beginDate = DateUtil.getAfterDayDate(new Date(), -15);
            order = findCustomerNewOrder(beginDate, null, null, oid);
        }
        return order;
    }

    public Boolean checkShop(String orderId, String shopId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            return false;
        } else {
            return order.getShopDetailId().equals(shopId);
        }
    }

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

    public void setTableNumber(String orderId, String tableNumber) {
        orderMapper.setOrderNumber(orderId, tableNumber);
    }

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

    public void useRedPrice(BigDecimal factMoney, String orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        Customer customer = customerService.selectById(order.getCustomerId());
        accountService.payOrder(order, factMoney, customer, null, null);
    }

    public Result refundPaymentByUnfinishedOrder(String orderId) {
        Result result = new Result();
        Order order = selectById(orderId);
//        MemcachedUtils.delete(orderId + "WxPay");
//        if (MemcachedUtils.get(order.getCustomerId()+"createOrder") != null) {
//            MemcachedUtils.delete(order.getCustomerId() + "createOrder");
//        }
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
//        Brand brand = brandService.selectById(order.getBrandId());
//        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
//        UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
//                "微信点X取消订单！");
//      LogTemplateUtils.getRefundWechatByUserType(order,brand,shopDetail.getName());
        if (order.getOrderMode() == ShopModeDto.BOSS_ORDER && order.getProductionStatus() == ProductionStatus.PRINTED) {
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
                logger.info("库存还原失败:" + order.getId());
            }
//            orderMapper.setStockBySuit(order.getShopDetailId());//自动更新套餐数量
        }

        //用户取消微信支付记录UserAction日志
        BrandDto brand = brandService.selectByPrimaryKey(order.getBrandId());
        Map customerMap = new HashMap(4);
        customerMap.put("brandName", brand.getBrandName());
        customerMap.put("fileName", order.getCustomerId());
        customerMap.put("type", "UserAction");
        customerMap.put("content", "用户:" + order.getCustomerId() + "取消微信支付，订单Id:" + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPost(LogUtils.url, customerMap);
        return result;
    }

    private void refundOrderHoufu(Order order) {
        List<OrderPaymentItem> payItemsList = orderPaymentItemService.selectByOrderId(order.getId());
        List<String> chargeList = new ArrayList<>();
        for (OrderPaymentItem item : payItemsList) {
//            String newPayItemId = ApplicationUtils.randomUUID();
            switch (item.getPaymentModeId()) {
                case PayMode.ACCOUNT_PAY:
                    accountService.addAccount(item.getPayValue(), item.getResultData(), "取消订单返还", AccountLog.SOURCE_CANCEL_ORDER, order.getShopDetailId());
//                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
//                    item.setId(newPayItemId);
//                    orderPaymentItemService.insert(item);
                    break;
                case PayMode.CHARGE_PAY:
                    if (!chargeList.contains(item.getResultData())) {
                        chargeList.add(item.getResultData());
                    }
//                    chargeOrderService.refundCharge(item.getPayValue(), item.getResultData(), order.getShopDetailId());
//                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
//                    item.setId(newPayItemId);
//                    orderPaymentItemService.insert(item);
//                    BigDecimal chargeValue = (BigDecimal) RedisUtil.get(item.getResultData()+"chargeValue");
//                    if(!MemcachedUtils.add(item.getResultData()+"chargeValue", item.getPayValue(), 600)){
//                        MemcachedUtils.put(item.getResultData()+"chargeValue", item.getPayValue().add((BigDecimal) MemcachedUtils.get(item.getResultData()+"chargeValue")));
//                    }
                    break;
                case PayMode.REWARD_PAY:
                    if (!chargeList.contains(item.getResultData())) {
                        chargeList.add(item.getResultData());
                    }
//                    chargeOrderService.refundReward(item.getPayValue(), item.getResultData(), order.getShopDetailId());
//                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
//                    item.setId(newPayItemId);
//                    orderPaymentItemService.insert(item);
//                    BigDecimal rewardValue = (BigDecimal) RedisUtil.get(item.getResultData()+"rewardValue");
//                    if(rewardValue == null){
//                        rewardValue = item.getPayValue();
//                    }else{
//                        rewardValue = rewardValue.add(item.getPayValue());
//                    }
//                    if(!MemcachedUtils.add(item.getResultData()+"rewardValue", item.getPayValue(), 600)){
//                        MemcachedUtils.put(item.getResultData()+"rewardValue", item.getPayValue().add((BigDecimal) MemcachedUtils.get(item.getResultData()+"rewardValue")));
//                    }
                    break;
                case PayMode.APPRAISE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getResultData());
                    break;
                case PayMode.SHARE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getResultData());
                    break;
                case PayMode.REFUND_ARTICLE_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getResultData());
                    break;
                case PayMode.THIRD_MONEY_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getResultData());
                    break;
                case PayMode.REBATE_MONEY_RED_PAY:
                    redPacketService.refundRedPacket(item.getPayValue(), item.getResultData());
                    item.setPayValue(item.getPayValue().multiply(new BigDecimal(-1)));
                    break;
                default:
                    break;
            }
        }
//        if(!CollectionUtils.isEmpty(chargeList)){
//            for(String id : chargeList){
//                BigDecimal rewardValue = (BigDecimal) MemcachedUtils.get(id+"rewardValue");
//                BigDecimal chargeValue = (BigDecimal) MemcachedUtils.get(id+"chargeValue");
//                chargeOrderService.refundMoney(chargeValue,rewardValue,id,order.getShopDetailId());
//                MemcachedUtils.delete(id+"rewardValue");
//                MemcachedUtils.delete(id+"chargeValue");
//            }
//        }
        orderPaymentItemService.deleteByOrderId(order.getId());
        BrandDto brand = brandService.selectById(order.getBrandId());
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", order.getId());
        map.put("type", "orderAction");
        map.put("content", "订单:" + order.getId() + "已取消,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(LogUtils.url, map);
    }

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
            logger.info("自动退款成功:" + order.getId());
            return true;
        } else {
            logger.warn("款项自动退还到相应账户失败，订单状态不是已付款或商品状态不是已付款未下单" + order.getId());
            return false;
        }

    }

    public Order afterPay(String orderId, String couponId, BigDecimal price, BigDecimal pay, BigDecimal waitMoney, Integer payMode) {
        Order order = selectById(orderId);
        if (order.getPrintTimes() == 1) {
            return null;
        }
        if (order.getPayType() == PayType.NOPAY && "sb".equals(order.getOperatorId()) && !order.getIsPay().equals(OrderPayState.ALIPAYING)) {
            order.setIsPay(OrderPayState.NOT_PAY);
            orderMapper.updateByPrimaryKeySelective(order);
        }
        BrandDto brand = brandService.selectById(order.getBrandId());
        ShopDetailDto shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        JSONResult result = new JSONResult<>();
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
                    item.setRemark("优惠券支付:" + item.getPayValue());
                    price = price.subtract(item.getPayValue());
                    item.setResultData(coupon.getId());
                    orderPaymentItemService.insert(item);
                    UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
                            "订单使用优惠券支付了：" + item.getPayValue());
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
                item.setResultData(order.getWaitId());
                orderPaymentItemService.insert(item);
                UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
                        "订单使用等位红包支付了：" + item.getPayValue());
                GetNumber getNumber = getNumberService.selectById(order.getWaitId());
                getNumber.setState(WaitModerState.WAIT_MODEL_NUMBER_THREE);
                getNumberService.update(getNumber);
            }
            logger.info("后付的情况下支付的余额为" + price);
            if (price.doubleValue() > 0) {  //余额支付
                accountService.payOrder(order, price, customer, brand, shopDetail);
            }
            logger.info("后付的情况下还需支付" + pay);
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
//                        UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
//                                "订单使用银联支付了：" + item.getPayValue());
//                        updateChild(order);
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", order.getCustomerId());
                        map.put("type", "UserAction");
                        map.put("content", "用户:" + order.getCustomerId() + "发起银联支付：" + item.getPayValue() + ",订单号为：" + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                        doPost(LogUtils.url, map);
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
//                        UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
//                                "订单使用现金支付了：" + item.getPayValue());
//                        updateChild(order);
                        Map map1 = new HashMap(4);
                        map1.put("brandName", brand.getBrandName());
                        map1.put("fileName", order.getCustomerId());
                        map1.put("type", "UserAction");
                        map1.put("content", "用户:" + order.getCustomerId() + "发起现金支付：" + item.getPayValue() + ",订单号为：" + order.getId() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                        doPost(LogUtils.url, map1);
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
                    //后付 付款后立马不可加菜
                    order.setAllowContinueOrder(false);
                    update(order);
                    updateChild(order);
                    //后付 付款后直接确认订单  判断是否可以领取红包
                    confirmOrder(order);

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return order;

    }

    public Order confirmOrder(Order order) {
        order = selectById(order.getId());
        if (order == null || order.getOrderState() != OrderState.PAYMENT) {
            return null;
        }
        if (order.getParentOrderId() != null) {
            return null;
        }
        if (order.getProductionStatus() == ProductionStatus.REFUND_ARTICLE) {
            return null;
        }
        BrandDto brand = brandService.selectById(order.getBrandId());
        ShopDetailDto shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        logger.info("开始确认订单:" + order.getId());
        Integer orginState = order.getOrderState();//订单开始确认的状体
        if (order.getConfirmTime() == null && !order.getClosed()) {
            order.setOrderState(OrderState.CONFIRM);
            order.setConfirmTime(new Date());
            order.setAllowCancel(false);
            BrandSettingDto setting = brandSettingService.selectByBrandId(order.getBrandId());
            if (order.getParentOrderId() == null) {
                logger.info("如果订单金额大于 评论金额 则允许评论" + order.getId());
                if (setting.getAppraiseMinMoney().compareTo(order.getOrderMoney()) <= 0 || setting.getAppraiseMinMoney().compareTo(order.getAmountWithChildren()) <= 0) {
                    order.setAllowAppraise(true);
                }
            } else {
                logger.info("最小评论金额为:" + setting.getAppraiseMinMoney() + ", oid:" + order.getId());
                order.setAllowAppraise(false);
            }
            update(order);
            //Map orderMap = new HashMap(4);
//            orderMap.put("brandName", brand.getBrandName());
//            orderMap.put("fileName", order.getId());
//            orderMap.put("type", "orderAction");
//            orderMap.put("content", "订单:" + order.getId() + "被确认订单状态更改为10,请求服务器地址为:" + MQSetting.getLocalIP());
//            doPostAnsc(url, orderMap);
            /**
             * 记录订单自动确认2-10过程
             */
            LogTemplateUtils.getConfirmByOrderType(brand.getBrandName(), order, orginState, "confirmOrder");

            return order;
        }
        return null;
    }

    public Order customerByOrderForMyPage(String customerId, String shopId) {
        Order order = orderMapper.customerByOrderForMyPage(customerId, shopId);
        return order;
    }

    public Order orderWxPaySuccess(OrderPaymentItem item) {
        Order order = selectById(item.getOrderId());
        OrderPaymentItem historyItem = orderPaymentItemService.selectById(item.getId());
        if (historyItem == null) {
            orderPaymentItemService.insert(item);
            if (order.getOrderMode() == ShopModeDto.HOUFU_ORDER) {
                order.setPaymentAmount(item.getPayValue());
                update(order);
            }
            if (order.getPayMode() == OrderPayMode.ALI_PAY && order.getIsPay().equals(OrderPayState.ALIPAYING)) {
                order.setIsPay(OrderPayState.ALIPAYED);
                update(order);
            } else if (order.getPayMode() != OrderPayMode.WX_PAY && order.getIsPay().equals(OrderPayState.ALIPAYING)) {
                order.setIsPay(OrderPayState.PAYED);
                update(order);
            } else if (order.getPayMode() != OrderPayMode.ALI_PAY && order.getIsPay().equals(OrderPayState.ALIPAYING)) {
                order.setIsPay(OrderPayState.NOT_PAY);
                update(order);
            }
            payOrderSuccess(order);
        } else {
            logger.warn("该笔支付记录已经处理过:" + item.getId());
        }
        return order;
    }

    public Order getLastOrderByTableNumber(String tableNumber, String shopId) {
        return orderMapper.getLastOrderByTableNumber(tableNumber, shopId);
    }

    public List<Order> getChildItem(String orderId) {
        Order parent = orderMapper.selectByPrimaryKey(orderId);
        List<Order> childs = orderMapper.selectByParentId(orderId, parent.getPayType());
        List<Order> result = new ArrayList<>();
        for (Order child : childs) {
            Order order = getOrderInfo(child.getId());
            result.add(order);
        }

        return result;

    }

    public String fixedRefund(String brandId, String shopId,
                              int total, int refund, String transaction_id, String mchid, String id) {
        WechatConfigDto config = wechatConfigService.selectByBrandId(brandId);
        ShopDetailDto shopDetail = shopDetailService.selectByPrimaryKey(shopId);
        Map<String, String> result = null;
        String newPayItemId = ApplicationUtils.randomUUID();
        if (shopDetail.getWxServerId() == null) {
            result = WeChatPayUtils.refund(newPayItemId, transaction_id,
                    total, refund, config.getAppid(), config.getMchid(),
                    config.getMchkey(), config.getPayCertPath());
        } else {
            WxServerConfigDto wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());

            result = WeChatPayUtils.refundNew(newPayItemId, transaction_id,
                    total, refund, wxServerConfig.getAppid(), wxServerConfig.getMchid(),
                    StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(), wxServerConfig.getMchkey(), wxServerConfig.getPayCertPath());
        }
//        OrderPaymentItem orderPaymentItem = orderPaymentItemService.selectById(id);
//        orderPaymentItem.setResultData(new JSONObject(result).toString());
//        orderPaymentItemService.update(orderPaymentItem);
        return new JSONObject(result).toString();
    }

    public void alipayRefund(String orderId, BigDecimal refundTotal) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        ShopDetailDto shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        String newPayItemId = ApplicationUtils.randomUUID();
        BrandSettingDto brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
        AliPayUtils.connection(StringUtils.isEmpty(shopDetail.getAliAppId()) ? brandSetting.getAliAppId() : shopDetail.getAliAppId().trim(),
                StringUtils.isEmpty(shopDetail.getAliPrivateKey()) ? brandSetting.getAliPrivateKey().trim() : shopDetail.getAliPrivateKey().trim(),
                StringUtils.isEmpty(shopDetail.getAliPublicKey()) ? brandSetting.getAliPublicKey().trim() : shopDetail.getAliPublicKey().trim());
        Map map = new HashMap();
        map.put("out_trade_no", order.getId());
        map.put("refund_amount", refundTotal);
        map.put("out_request_no", newPayItemId);
        String resultJson = AliPayUtils.refundPay(map);

        OrderPaymentItem back = new OrderPaymentItem();
        back.setId(ApplicationUtils.randomUUID());
        back.setOrderId(order.getId());
        back.setPaymentModeId(PayMode.ARTICLE_BACK_PAY);
        back.setPayTime(new Date());
        back.setPayValue(new BigDecimal(-1).multiply(refundTotal));
        back.setRemark("退菜红包:" + refundTotal);

        back.setResultData("支付宝支付返回" + refundTotal + "金额");
        orderPaymentItemService.insert(back);
    }

    public List<Order> selectWXOrderItems(Map<String, Object> map) {
        return orderMapper.selectWXOrderItems(map);
    }

    public Order getLastOrderByCustomer(String customerId, String shopId) {
        ShopDetailDto shopDetail = shopDetailService.selectByPrimaryKey(shopId);
        BrandSettingDto brandSetting = brandSettingService.selectByBrandId(shopDetail.getBrandId());
        Order order = orderMapper.getLastOrderByCustomer(customerId, shopId, brandSetting.getCloseContinueTime());
        if (order != null && order.getParentOrderId() != null) {
            Order parent = orderMapper.selectByPrimaryKey(order.getParentOrderId());
            if (parent != null && parent.getAllowContinueOrder()) {
                return parent;
            }
        } else {
            return order;
        }
        return null;
    }

    public Order getLastOrderBySevenMode(String customerId) {
        return orderMapper.getLastOrderBySevenMode(customerId);
    }

}
