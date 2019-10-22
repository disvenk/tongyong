package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.WeBrandScore;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.service.WeBrandScoreService;
import com.resto.shop.web.constant.OfflineOrderSource;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.dao.WeOrderDetailMapper;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


/**
 *
 */
@Component
@Service
public class WeOrderDetailServiceImpl extends GenericServiceImpl<WeOrderDetail, Integer> implements WeOrderDetailService {

    @Resource
    private WeOrderDetailMapper weorderdetailMapper;


    @Resource
    OrderService orderService;

    @Resource
    WeBrandScoreService weBrandScoreService;

    @Resource
    ShopDetailService shopDetailService;


    @Resource
    WeShopScoreService weShopScoreService;

    @Resource
    OffLineOrderService offLineOrderService;

    @Resource
    ChargeOrderService chargeOrderService;

    @Resource
    WeChargeLogService weChargeLogService;

    @Resource
    WeItemService weItemService;

    @Resource
    WeReturnItemService weReturnItemService;

    @Resource
    WeReturnCustomerService weReturnCustomerService;

    @Override
    public GenericDao<WeOrderDetail, Integer> getDao() {
        return weorderdetailMapper;
    }

    @Override
    public WeOrderDetail selectWeOrderByShopIdAndTime(String shopId, String createTime) {
        return weorderdetailMapper.selectWeOrderByShopIdAndTime(shopId, DateUtil.fomatDate(createTime));
    }

    @Override
    public void insertDaydata(String brandId, String brandName) {
        //昨日
        Date yDay = DateUtil.getAfterDayDate(new Date(), -1);
        //前日
        Date bDay = DateUtil.getAfterDayDate(new Date(), -2);

        String zuoriDay = DateUtil.getAfterDayDateStr("-1");

        //格式化
        Date yesterDay = DateUtil.fomatDate(DateUtil.formatDate(yDay, "yyyy-MM-dd"));
        Date beforeYesterDay = DateUtil.fomatDate(DateUtil.formatDate(bDay, "yyyy-MM-dd"));
        //一.插入品牌的分数
        Date beginDate = DateUtil.getDateBegin(yesterDay);
        Date endDate = DateUtil.getDateEnd(yesterDay);
//---------------------第一页数据开始 ---------------------------------
        //查询品牌昨天的分数
        Double brandPrcent = orderService.selectAppraiseBybrandId(brandId, beginDate, endDate);

        //1.查询当前品牌昨日是否有值
        WeBrandScore scoreYesterDay = weBrandScoreService.selectByBrandIdAndCreateTime(brandId, yesterDay);
        if (null != scoreYesterDay) {
            weBrandScoreService.delete(scoreYesterDay.getId());
        }

        //1.查询当前品牌前日是否有值  分数后面默认是上升 如果前日有分数且比昨日的分数高则为下降的箭头
        WeBrandScore scoreBeforeYesterDay = weBrandScoreService.selectByBrandIdAndCreateTime(brandId, beforeYesterDay);
        WeBrandScore wbs = new WeBrandScore();
        wbs.setCreateTime(yesterDay);
        wbs.setBrandId(brandId);
        wbs.setBrandScore(brandPrcent.toString());
        if (scoreBeforeYesterDay != null && brandPrcent < Double.parseDouble(scoreBeforeYesterDay.getBrandScore())) {
            wbs.setFlag(false);
        }
        weBrandScoreService.insert(wbs);

        //-------------------------第二页数据开始---------------------------------------------

        List<ShopDetail> shopDetails = shopDetailService.selectByBrandId(brandId);
        for (ShopDetail shopDetail : shopDetails) {
            //查询店铺昨天的营业额(resto营业额+录入营业额)
            BigDecimal resto = orderService.selectOrderMoneyByShopIdAndTime(shopDetail.getId(), beginDate, endDate);
            //查询店铺昨天线下的营业额
            List<OffLineOrder> offLineOrders = offLineOrderService.selectByShopIdAndTime(shopDetail.getId(), beginDate, endDate);
            BigDecimal todayEnter = BigDecimal.ZERO;
            if (!offLineOrders.isEmpty()) {
                for (OffLineOrder offLineOrder : offLineOrders) {
                    todayEnter = todayEnter.add(offLineOrder.getEnterTotal());
                }
            }

            //查询店铺昨天的分数
            Double shopPrcent = orderService.selectAppraiseByshopId(shopDetail.getId(), beginDate, endDate);
            //查询当前店铺前日的营收状
            WeShopScore yesterDayBeforeweShopScore = weShopScoreService.selectByShopIdAndDate(shopDetail.getId(), beforeYesterDay);
            //查询当前店铺昨日的营收状态
            WeShopScore yesterDayweShopScore = weShopScoreService.selectByShopIdAndDate(shopDetail.getId(), beforeYesterDay);
            //如果有值则删除
            if (null != yesterDayweShopScore) {
                weShopScoreService.delete(yesterDayweShopScore.getId());
            }

            WeShopScore we = new WeShopScore();
            we.setShopId(shopDetail.getId());
            we.setCreateTime(yesterDay);
            we.setShopScore(shopPrcent.toString());
            we.setTotalIncome(todayEnter);
            if (null != yesterDayweShopScore) {
                if (todayEnter.compareTo(new BigDecimal(yesterDayweShopScore.getShopScore())) < 0) {
                    we.setScoreFlag(false);
                }
                if (new BigDecimal(shopPrcent).compareTo(yesterDayweShopScore.getTotalIncome()) < 0) {
                    we.setTotalFlag(false);
                }
            }
            //插入店铺分数和订单总额的数据
            weShopScoreService.insert(we);
            //-------------------------第三页数据开始---------------------------------------------
            WeOrderDetail wo = new WeOrderDetail();
            //封装1.
            wo.setShopId(shopDetail.getId());
            wo.setCreateTime(yesterDay);
            wo.setBrandName(brandName);
            wo.setShopName(shopDetail.getName());
            //2.昨日订单的数据

            //1.订单数据 放在下面封装

            //2.来客人数
            /**
             * todo
             * 目前先写死
             *
             */
            wo.setCustomerCount(10);
            wo.setAvgCustomerTotal(new BigDecimal(100));

            //3.封装实收金额/折扣金额   微信 支付宝 现金 刷卡 充值
            int wechatNum = 0;
            int alipayNum = 0;
            int cashNum = 0;
            int bankNum = 0;
            int chargeNum = 0;

            BigDecimal wechatTotal = BigDecimal.ZERO;
            BigDecimal aliPayTotal = BigDecimal.ZERO;
            BigDecimal cashTotal = BigDecimal.ZERO;
            BigDecimal bankTotal = BigDecimal.ZERO;
            BigDecimal chargeTotal = BigDecimal.ZERO;

            //3封装 折扣金额
            int redNum = 0;
            int couponNum = 0;
            int chargeGifNum = 0;
            int waitNum = 0;
            int articleReturnNum = 0;

            BigDecimal redTotal = BigDecimal.ZERO;
            BigDecimal couponTotal = BigDecimal.ZERO;
            BigDecimal chargeGifTotal = BigDecimal.ZERO;
            BigDecimal waitTotal = BigDecimal.ZERO;
            BigDecimal articleReturnTotal = BigDecimal.ZERO;

            List<Order> list = orderService.selectListByShopId(zuoriDay, zuoriDay, shopDetail.getId());
            if (!list.isEmpty()) {
                for (Order o : list) {
                    if (!o.getOrderPaymentItems().isEmpty()) {
                        for (OrderPaymentItem oi : o.getOrderPaymentItems()) {
                            switch (oi.getPaymentModeId()) {
                                //实际
                                case PayMode.WEIXIN_PAY:
                                    wechatNum++;
                                    wechatTotal = wechatTotal.add(oi.getPayValue());
                                    break;
                                case PayMode.ALI_PAY:
                                    alipayNum++;
                                    aliPayTotal = aliPayTotal.add(oi.getPayValue());
                                    break;
                                case PayMode.CRASH_PAY:
                                    cashNum++;
                                    cashTotal = cashTotal.add(oi.getPayValue());
                                    break;
                                case PayMode.BANK_CART_PAY:
                                    bankNum++;
                                    bankTotal = bankTotal.add(oi.getPayValue());
                                    break;
                                case PayMode.CHARGE_PAY:
                                    chargeNum++;
                                    chargeTotal = chargeTotal.add(oi.getPayValue());
                                    break;
                                //折扣：
                                case PayMode.ACCOUNT_PAY:
                                    redNum++;
                                    redTotal = redTotal.add(redTotal);
                                    break;
                                case PayMode.COUPON_PAY:
                                    couponNum++;
                                    couponTotal = couponTotal.add(oi.getPayValue());
                                    break;
                                case PayMode.REWARD_PAY:
                                    chargeGifNum++;
                                    chargeGifTotal = chargeGifTotal.add(chargeGifTotal);
                                    break;
                                case PayMode.WAIT_MONEY:
                                    waitNum++;
                                    waitTotal = waitTotal.add(oi.getPayValue());
                                    break;
                                case PayMode.ARTICLE_BACK_PAY:
                                    articleReturnNum++;
                                    articleReturnTotal = articleReturnTotal.add(oi.getPayValue());
                                    break;

                                default:
                                    break;
                            }
                        }

                    }

                }
            }
            wo.setWechatCount(wechatNum);
            wo.setWechatTotal(wechatTotal);//1.微信支付笔数 和金额
            wo.setAlipayCount(alipayNum);
            wo.setAlipayTotal(aliPayTotal);//2支付宝支付笔数 和金额
            wo.setCashCount(cashNum);
            wo.setCashTotal(cashTotal);//3现金
            wo.setBankCount(bankNum);
            wo.setBankTotal(bankTotal);//4.银行卡
            wo.setBankTotal(bankTotal);
            wo.setChargeCount(chargeNum);//5.充值
            wo.setCashTotal(chargeTotal);

            wo.setRedCount(redNum);
            wo.setRedTotal(redTotal);//1.红包
            wo.setCouponCount(couponNum);
            wo.setCouponTotal(couponTotal);//2优惠券
            wo.setChargeReturnCount(chargeGifNum);//3.充值赠送
            wo.setChargeReturnTotal(chargeGifTotal);
            wo.setWaitCount(waitNum);
            wo.setWaitTotal(waitTotal);//4等位红包
            wo.setReturnCount(articleReturnNum);//5退菜红包
            wo.setReturnTotal(articleReturnTotal);//

            List<ChargeOrder> chargeList = chargeOrderService.selectListByDateAndShopId(zuoriDay, zuoriDay, shopDetail.getId());
            if (!chargeList.isEmpty()) {
                for (ChargeOrder chargeOrder : chargeList) {
                    WeChargeLog wg = new WeChargeLog();
                    wg.setCreateTime(chargeOrder.getCreateTime());
                    wg.setChargeTelephone(chargeOrder.getTelephone());
                    wg.setChargeMoney(chargeOrder.getChargeMoney());
                    wg.setChargeType(chargeOrder.getType());
                    wg.setShopId(chargeOrder.getShopDetailId());
                    weChargeLogService.insert(wg);
                }
            }

            //封装菜品销量的的 //封装退菜统计 //封装退菜用户
            //查询是否有数据


            //查询菜品 group by oi.article_name 用来封装每道菜的具体销售信息
            List<OrderItem> orderItemList = orderService.selectListByShopIdAndTime(zuoriDay, shopDetail.getId());
            if (!orderItemList.isEmpty()) {
                for (OrderItem oi : orderItemList) {
                    WeItem wi = new WeItem();//封装菜品销售
                    wi.setCreateTime(yesterDay);
                    wi.setItemCount(oi.getCount());
                    wi.setItemName(oi.getArticleName());
                    wi.setItemTotal(oi.getFinalPrice());
                    wi.setShopId(oi.getShopId());
                    weItemService.insert(wi);
                    //封装退菜信息
                    if (oi.getRefundCount() > 0) {//说明有退菜信息
                        WeReturnItem wr = new WeReturnItem();
                        wr.setCreateTime(yesterDay);
                        wr.setReturnItemCount(oi.getRefundCount());
                        wr.setShopId(oi.getShopId());
                        wr.setReturnItemName(oi.getArticleName());
                        wr.setReturnItemTotal(oi.getUnitPrice().multiply(new BigDecimal(oi.getRefundCount())));
                        weReturnItemService.insert(wr);
                    }
                }
            }

            //封装退菜用户 group by oi.article_name 用来封装退菜用户的信息
            List<OrderItem> orderCustomerList = orderService.selectCustomerListByShopIdAndTime(zuoriDay, shopDetail.getId());
            if (!orderCustomerList.isEmpty()) {
                for (OrderItem oi : orderCustomerList) {
                    WeReturnCustomer wc = new WeReturnCustomer();
                    wc.setCreateTime(yesterDay);
                    wc.setShopId(oi.getShopId());
                    wc.setTelephone(oi.getTelephone());
                    wc.setMoney(oi.getUnitPrice().multiply(new BigDecimal(oi.getRefundCount())));
                    weReturnCustomerService.insert(wc);
                }

            }

            //----1.定义时间--- 所有的本月上旬 下旬参照时间都是昨日也就是说

            //本月的开始时间 本月结束时间
            String beginMonth = DateUtil.getMonthBeginByYesterDay();
            String endMonth = DateUtil.getEndOfMonthEndByYesterDay();
            Date begin = DateUtil.getDateBegin(DateUtil.fomatDate(beginMonth));
            Date end = DateUtil.getDateEnd(DateUtil.fomatDate(endMonth));
            //2定义顾客id集合
            //昨日之前出现的所有顾客的Id
            List<String> customerBeforeToday = new ArrayList<>();
            //本月上旬之前出现的顾客id
            List<String> customerBeforeFirstOfMonth = new ArrayList<>();
            //本月中旬之前出现的顾客id
            List<String> customerBeforeMiddleOfMonth = new ArrayList<>();
            //本月下旬之前出现的顾客id
            List<String> customerBeforeLastOfMonth = new ArrayList<>();
            //本月之前出现的顾客id
            List<String> customerBeforeMonth = new ArrayList<>();

            //昨日出现的顾客id(所有)
            Set<String> customerInToday = new HashSet<>();
            //本月上旬中出现的顾客id(所有)
            Set<String> customerInFirstOfMonth = new HashSet<>();
            //本月中旬中出现的顾客id(所有)
            Set<String> customerInMiddleOfMonth = new HashSet<>();
            //本月下旬中出现的顾客id(所有)
            Set<String> customerInLastOfMonth = new HashSet<>();
            //本月中出现的顾客Id(所有)
            Set<String> customerInMonth = new HashSet<>();

            //昨日分享注册的顾客id
            List<String> customerShareInToday = new ArrayList<>();
            //本月上旬分享注册的顾客id
            List<String> customerShareInFirstOfMonth = new ArrayList<>();
            //本月中旬出现的分享注册顾客Id
            List<String> customerShareInMiddleOfMonth = new ArrayList<>();
            //本月下旬出现的分享注册顾客id
            List<String> customerShareInLastOfMonth = new ArrayList<>();
            //本月出现的分享注册顾客id
            List<String> customerShareInMonth = new ArrayList<>();

            //本日回头用id
            List<String> backCustomerToday = new ArrayList<>();//直接用本日之前出现的 和 本日出现的交集
            //本月上旬回头用id
            List<String> backCustomerFirstOfMonth = new ArrayList<>();
            //本月中旬回头用id
            List<String> backCustomerMiddleOfMonth = new ArrayList<>();
            //本月下旬回头用id
            List<String> backCustomerLastOfMonth = new ArrayList<>();
            //本月回头用户id
            List<String> backCustomerMonth = new ArrayList<>();

            //昨日新增用户个数
            Set<String> todayNewCutomer = new HashSet<>();
            //上旬新增用户个数
            Set<String> firstOfMonthNewCustomer = new HashSet<>();
            //中旬新增用户个数
            Set<String> middleOfMonthNewCustomer = new HashSet<>();
            //下旬新增用户个数
            Set<String> lastOfMonthNewCustomer = new HashSet<>();
            //本月新增用户个数
            Set<String> monthNewCustomer = new HashSet<>();

            //昨日新增自然用户个数
            Set<String> todayNormalNewCustomer = new HashSet<>();//注意如果当order中的customerId被删除了 那么无法判断是分享注册还是自然注册 这是用总的新增-分享用户
            //上旬新增自然用户个数
            Set<String> firstOfMonthNormalCustomer = new HashSet<>();//上旬总新增-上旬新增(分享)
            //中旬新增自然用户个数
            Set<String> middleOfMonthNormalCustomer = new HashSet<>();
            //下旬新增自然用户个数
            Set<String> lastOfMonthNormalCustomer = new HashSet<>();
            //本月新增自然用户个数
            Set<String> monthNormalCustomer = new HashSet<>();

            //昨日新增分享用户个数
            Set<String> todayShareNewCutomer = new HashSet<>();
            //上旬新增分享用户个数
            Set<String> firstOfMonthShareCustomer = new HashSet<>();
            //中旬新增分享用户的个数
            Set<String> middleOfMonthShareCustomer = new HashSet<>();
            //下旬新增分享用户的个数
            Set<String> lastOfMonthShareCustomer = new HashSet<>();
            //本月新增分享用户个数
            Set<String> monthShareCustomer = new HashSet<>();

            //昨日回头用户个数(包括二次回头和多次回头)
            Set<String> todayBackCustomer = new HashSet<>();
            //上旬新增回头用户个数
            Set<String> firstOfMonthBackCustomer = new HashSet<>();
            //中旬新增回头用户个数
            Set<String> middleOfMonthBackCustomer = new HashSet<>();
            //下旬新增回头用户个数
            Set<String> lastOfMonthBackCustomer = new HashSet<>();
            //本月新增回头用户个数
            Set<String> monthBackCustomer = new HashSet<>();

            //昨日二次回头用户个数
            Set<String> todayBackTwoCustomer = new HashSet<>();
            //上旬二次回头用户个数
            Set<String> firstOfMonthBackTwoCustomer = new HashSet<>();
            //中旬二次回头用户个数
            Set<String> middleOfMonthBackTwoCustomer = new HashSet<>();
            //下旬二次回头用户个数
            Set<String> lastOfMonthBackTwoCustomer = new HashSet<>();
            //本月二次回头用户个数
            Set<String> monthBackTwoCustomer = new HashSet<>();

            //昨日多次回头用户个数
            Set<String> todayBackTwoMoreCustomer = new HashSet<>();
            //上旬多次回头用户个数
            Set<String> firstOfMonthBackTwoMoreCustomer = new HashSet<>();
            //中旬多次回头用户个数
            Set<String> middleOfMonthBackTwoMoreCustomer = new HashSet<>();
            //下旬多次回头用户个数
            Set<String> lastOfMonthBackTwoMoreCustomer = new HashSet<>();
            //本月多次回头用户个数
            Set<String> monthBackTwoMoreCustomer = new HashSet<>();

            //历史用户数
            Set<String> customerBeforeTodayEnd = new HashSet<>();

            //3定义满意度
            //本日满意度
            String todaySatisfaction = "";
            //上旬满意度
            String firstOfMonthSatisfaction = "";
            //中旬满意度
            String middleOfMonthSatisfaction = "";
            //下旬满意度
            String lastOfMonthSatisfaction = "";
            //本月满意度
            String monthSatisfaction = "";

            //4定义支付
            //本日resto订单总额
            BigDecimal todayRestoTotal = BigDecimal.ZERO;
            //上旬r订单总额
            BigDecimal firstOfMonthRestoTotal = BigDecimal.ZERO;
            //中旬r订单总额
            BigDecimal middleOfMonthRestoTotal = BigDecimal.ZERO;
            //下旬r订单总额
            BigDecimal lastOfMonthRestoTotal = BigDecimal.ZERO;
            //本月r订单总额
            BigDecimal monthRestoTotal = BigDecimal.ZERO;

            //本日线下订单总额
            BigDecimal todayEnterTotal = BigDecimal.ZERO;
            //上旬线下订单总额
            BigDecimal firstOfMonthEnterTotal = BigDecimal.ZERO;
            //中旬线下订单总额
            BigDecimal middleOfMonthEnterTotal = BigDecimal.ZERO;
            //下旬线下订单单总额
            BigDecimal lastOfMonthEnterTotal = BigDecimal.ZERO;
            //本月线下订单总额
            BigDecimal monthEnterTotal = BigDecimal.ZERO;

            //本日resto实收总额
            BigDecimal todayPayRestoTotal = BigDecimal.ZERO;
            //上旬resto实收总额
            BigDecimal firstOfMonthPayRestoTotal = BigDecimal.ZERO;
            //中旬resto实收总额
            BigDecimal middleOfMonthPayRestoTotal = BigDecimal.ZERO;
            //下旬resto实收总额
            BigDecimal lastOfMonthPayRestoTotal = BigDecimal.ZERO;
            //本月resto实收总额
            BigDecimal monthPayRestoTotal = BigDecimal.ZERO;

            //本日新增用户的订单总额
            BigDecimal todayNewCustomerRestoTotal = BigDecimal.ZERO;
            //上旬新增用户的订单总额
            BigDecimal firstOfMonthNewCustomerRestoTotal = BigDecimal.ZERO;
            //中旬新增用户的订单总额
            BigDecimal middleOfMonthNewCustomerRestoTotal = BigDecimal.ZERO;
            //下旬新增用户的订单总额
            BigDecimal lastOfMonthNewCustomerRestoTotal = BigDecimal.ZERO;
            //本月新增用户的订单总额
            BigDecimal monthNewCustomerRestoTotal = BigDecimal.ZERO;

            //本日新增分享用户的订单总额
            BigDecimal todayNewNormalCustomerRestoTotal = BigDecimal.ZERO;
            //上旬新增分享用户的订单总额
            BigDecimal firstOfMonthNewNormalCustomerRestoTotal = BigDecimal.ZERO;
            //中旬新增分享用户的订单总额
            BigDecimal middleOfMonthNewNormalCustomerRestoTotal = BigDecimal.ZERO;
            //下旬新增分享用户的订单总额
            BigDecimal lastOfMonthNewNormalCustomerRestoTotal = BigDecimal.ZERO;
            //本月新增分享用户的订单总额
            BigDecimal monthNewNormalCustomerRestoTotal = BigDecimal.ZERO;

            //本日新增自然用户的订单总额
            BigDecimal todayNewShareCustomerRestoTotal = BigDecimal.ZERO;
            //上旬新增自然用户的订单总额
            BigDecimal firstOfMonthNewShareCustomerRestoTotal = BigDecimal.ZERO;
            //中旬新增自然用户的订单总额
            BigDecimal middleOfMonthNewShareCustomerRestoTotal = BigDecimal.ZERO;
            //下旬新增自然用户的订单总额
            BigDecimal lastOfMonthNewShareCustomerRestoTotal = BigDecimal.ZERO;
            //本月新增自然用户的订单总额
            BigDecimal monthNewShareCustomerRestoTotal = BigDecimal.ZERO;

            //1.本日回头用户的订单总额
            BigDecimal todayBackCustomerRestoTotal = BigDecimal.ZERO;
            //2.上旬回头用户的订单总额
            BigDecimal firstOfMonthBackCustomerRestoTotal = BigDecimal.ZERO;
            //3.中旬回头用户的订单总额
            BigDecimal middleOfMonthBackCustomerRestoTotal = BigDecimal.ZERO;
            // 4.下旬回头用户的订单总额
            BigDecimal lastOfMonthBackCustomerRestoTotal = BigDecimal.ZERO;
            // 5.本月回头用户的订单总额
            BigDecimal monthBackCustomerRestoTotal = BigDecimal.ZERO;

            //1.本日二次回头用户的订单总额
            BigDecimal todayBackTwoCustomerRestoTotal = BigDecimal.ZERO;
            //2.上旬二次回头用户的订单总额
            BigDecimal firstOfMonthBackTwoCustomerRestoTotal = BigDecimal.ZERO;
            //3.中旬二次回头用户的订单总额
            BigDecimal middleOfMonthBackTwoCustomerRestoTotal = BigDecimal.ZERO;
            // 4.下旬二次回头用户的订单总额
            BigDecimal lastOfMonthBackTwoCustomerRestoTotal = BigDecimal.ZERO;
            // 5.本月二次回头用户的订单总额
            BigDecimal monthBackTwoCustomerRestoTotal = BigDecimal.ZERO;

            //1.本日多次回头用户的订单总额
            BigDecimal todayBackTwoMoreCustomerRestoTotal = BigDecimal.ZERO;
            //2.上旬多次回头用户的订单总额
            BigDecimal firstOfMonthBackTwoMoreCustomerRestoTotal = BigDecimal.ZERO;
            //3.中旬多次回头用户的订单总额
            BigDecimal middleOfMonthBackTwoMoreCustomerRestoTotal = BigDecimal.ZERO;
            // 4.下旬多次回头用户的订单总额
            BigDecimal lastOfMonthBackTwoMoreCustomerRestoTotal = BigDecimal.ZERO;
            // 5.本月多次次回头用户的订单总额
            BigDecimal monthBackTwoMoreCustomerRestoTotal = BigDecimal.ZERO;

            //本日resto订单总数
            Set<String> todayRestoCount = new HashSet<>();
            //上旬r订单总数
            Set<String> firstOfMonthRestoCount = new HashSet<>();
            //中询r订单总数
            Set<String> middleOfMonthRestoCount = new HashSet<>();
            //下旬r订单总数
            Set<String> lastOfMonthRestoCount = new HashSet<>();
            //本月r订单总数
            Set<String> monthRestoCount = new HashSet<>();

            //本日线下订单总数
            int todayEnterCount = 0;
            //上旬线下订单总数
            int firstOfMonthEnterCount = 0;
            //中旬线下订单总数
            int middleOfMonthEnterCount = 0;
            //下旬线下订单总数
            int lastOfMonthEnterCount = 0;
            //本月线下订单总数
            int monthEnterCount = 0;

            //查询pos端店铺录入信息
            List<OffLineOrder> offLineOrderList = offLineOrderService.selectlistByTimeSourceAndShopId(shopDetail.getId(), begin, end, OfflineOrderSource.OFFLINE_POS);

            if (!offLineOrderList.isEmpty()) {
                for (OffLineOrder of : offLineOrderList) {
                    List<Integer> getTime = DateUtil.getDayByYesterDay(of.getCreateTime());
                    if (getTime.contains(2)) {//本日中
                        todayEnterCount += of.getEnterCount();
                        todayEnterTotal = todayEnterTotal.add(of.getEnterTotal());
                    }

                    if (getTime.contains(4)) {//上旬中
                        firstOfMonthEnterCount += of.getEnterCount();
                        firstOfMonthEnterTotal = firstOfMonthEnterTotal.add(of.getEnterTotal());
                    }

                    if (getTime.contains(6)) {//中旬中
                        middleOfMonthEnterCount += of.getEnterCount();
                        middleOfMonthEnterTotal = middleOfMonthEnterTotal.add(of.getEnterTotal());
                    }

                    if (getTime.contains(8)) {//下旬中
                        lastOfMonthEnterCount += of.getEnterCount();
                        lastOfMonthEnterTotal = lastOfMonthRestoTotal.add(of.getEnterTotal());
                    }

                    if (getTime.contains(10)) {//本月中
                        monthEnterCount += of.getEnterCount();
                        monthEnterTotal = monthEnterTotal.add(of.getEnterTotal());
                    }
                }
            }

            //查询历史订单和人 目前是以店铺为基础 也就是 指用户在另一个店铺就餐，在当前店铺还是算第一次用户
            List<Order> orderHistoryList = orderService.selectOrderHistoryList(shopDetail.getId(), DateUtil.getDateEnd(new Date()));

            //yz 查询本月所有的已消费订单
            List<Order> orders = orderService.selectListsmsByShopId(begin, end, shopDetail.getId());
            if (!orders.isEmpty()) {
                for (Order order : orderHistoryList) {
                    List<Integer> getTime = DateUtil.getDayByYesterDay(order.getCreateTime());
                    if (getTime.contains(1)) {
                        customerBeforeToday.add(order.getCustomerId());
                    }
                    if (getTime.contains(2)) {
                        customerInToday.add(order.getCustomerId());
                        if (order.getCustomer() != null && !org.apache.commons.lang3.StringUtils.isEmpty(order.getCustomer().getShareCustomer())) {
                            customerShareInToday.add(order.getCustomerId());
                        }
                    }
                    if (getTime.contains(3)) {
                        customerBeforeFirstOfMonth.add(order.getCustomerId());
                    }
                    if (getTime.contains(4)) {
                        customerInFirstOfMonth.add(order.getCustomerId());
                        if (order.getCustomer() != null && order.getCustomer().getShareCustomer() != null) {
                            customerShareInFirstOfMonth.add(order.getCustomerId());
                        }
                    }
                    if (getTime.contains(5)) {
                        customerBeforeMiddleOfMonth.add(order.getCustomerId());
                    }
                    if (getTime.contains(6)) {
                        customerInMiddleOfMonth.add(order.getCustomerId());
                        if (order.getCustomer() != null && order.getCustomer().getShareCustomer() != null) {
                            customerShareInMiddleOfMonth.add(order.getCustomerId());
                        }
                    }
                    if (getTime.contains(7)) {
                        customerBeforeLastOfMonth.add(order.getCustomerId());
                    }
                    if (getTime.contains(8)) {
                        customerInLastOfMonth.add(order.getCustomerId());
                        if (order.getCustomer() != null && order.getCustomer().getShareCustomer() != null) {
                            customerShareInLastOfMonth.add(order.getCustomerId());
                        }
                    }
                    if (getTime.contains(9)) {
                        customerBeforeMonth.add(order.getCustomerId());
                    }
                    if (getTime.contains(10)) {
                        customerInMonth.add(order.getCustomerId());
                        if (order.getCustomer() != null && order.getCustomer().getShareCustomer() != null) {
                            customerShareInMonth.add(order.getCustomerId());
                        }
                    }
                    //历史用户数
                    customerBeforeTodayEnd.add(order.getId());
                }

            }


            //计算 新增 (分享注册+自然注册)
            //今日--

            if (!customerInToday.isEmpty()) {
                for (String s1 : customerInToday) {  //今日有 但是今日之前没有的
                    if (!customerBeforeToday.contains(s1)) {
                        todayNewCutomer.add(s1);
                    }
                }
            }

            if (!customerShareInToday.isEmpty()) {
                for (String s1 : customerShareInToday) {//今日有且是分享注册 但是今日之前没有
                    if (!customerBeforeToday.contains(s1)) {
                        todayShareNewCutomer.add(s1);
                    }
                }
            }

            if (!todayNewCutomer.isEmpty()) {
                for (String s1 : todayNewCutomer) {
                    if (!todayShareNewCutomer.contains(s1)) {
                        todayNormalNewCustomer.add(s1);
                    }
                }
            }

            //上旬---
            if (!customerInFirstOfMonth.isEmpty()) {
                for (String s1 : customerInFirstOfMonth) {  //上旬有 但是上旬之前没有的
                    if (!customerBeforeFirstOfMonth.contains(s1)) {
                        firstOfMonthNewCustomer.add(s1);
                    }
                }
            }

            if (!customerShareInFirstOfMonth.isEmpty()) {
                for (String s1 : customerShareInFirstOfMonth) {//上旬有且是分享注册 但是上旬之前没有
                    if (!customerBeforeFirstOfMonth.contains(s1)) {
                        firstOfMonthShareCustomer.add(s1);
                    }
                }
            }

            if (!firstOfMonthNewCustomer.isEmpty()) {
                for (String s1 : firstOfMonthNewCustomer) {
                    if (!firstOfMonthShareCustomer.contains(s1)) {
                        firstOfMonthNormalCustomer.add(s1);
                    }
                }
            }

            //中旬---
            if (!customerInMiddleOfMonth.isEmpty()) {
                for (String s1 : customerInMiddleOfMonth) {  //中旬有 但是中旬之前没有的
                    if (!customerBeforeMiddleOfMonth.contains(s1)) {
                        middleOfMonthNewCustomer.add(s1);
                    }
                }
            }

            if (!customerShareInMiddleOfMonth.isEmpty()) {
                for (String s1 : customerShareInMiddleOfMonth) {//中旬有且是分享注册 但是中旬之前没有
                    if (!customerBeforeMiddleOfMonth.contains(s1)) {
                        middleOfMonthShareCustomer.add(s1);
                    }
                }
            }

            if (!middleOfMonthNewCustomer.isEmpty()) {
                for (String s1 : middleOfMonthNewCustomer) {
                    if (!middleOfMonthShareCustomer.contains(s1)) {
                        middleOfMonthNormalCustomer.add(s1);
                    }
                }
            }

            if (!customerInLastOfMonth.isEmpty()) {
                //下旬---
                for (String s1 : customerInLastOfMonth) {  //下旬有 但是下旬之前没有的
                    if (!customerBeforeLastOfMonth.contains(s1)) {
                        lastOfMonthNewCustomer.add(s1);
                    }
                }
            }

            for (String s1 : customerShareInLastOfMonth) {//下旬有且是分享注册 但是下旬之前没有
                if (!customerBeforeLastOfMonth.contains(s1)) {
                    lastOfMonthShareCustomer.add(s1);
                }
            }

            if (!lastOfMonthNewCustomer.isEmpty()) {
                for (String s1 : lastOfMonthNewCustomer) {
                    if (!lastOfMonthShareCustomer.contains(s1)) {
                        lastOfMonthNormalCustomer.add(s1);
                    }
                }
            }

            //本月---

            if (!customerInMonth.isEmpty()) {
                for (String s1 : customerInMonth) {  //本月有 但是本月之前没有的
                    if (!customerBeforeMonth.contains(s1)) {
                        monthNewCustomer.add(s1);
                    }
                }
            }

            if (!customerShareInMonth.isEmpty()) {
                for (String s1 : customerShareInMonth) {//本月有且是分享注册 但是本月之前没有
                    if (!customerBeforeMonth.contains(s1)) {
                        monthShareCustomer.add(s1);
                    }
                }
            }


            if (!monthNewCustomer.isEmpty()) {
                for (String s1 : monthNewCustomer) {
                    if (!monthShareCustomer.contains(s1)) {
                        monthNormalCustomer.add(s1);
                    }
                }
            }


            //计算回头用户
            //今日回头
            if (!customerBeforeToday.isEmpty()) {
                for (String s1 : customerBeforeToday) {//以前有 今日也有
                    if (customerInToday.contains(s1)) {
                        todayBackCustomer.add(s1);//去重 直接用长度来代替今日回头用户的总数
                        backCustomerToday.add(s1);//不去重
                    }
                }
            }

            //定义一个map 来存放当日每个回头用户存在的次数
            Map<String, Integer> todayBackCount = new HashMap();
            if (!backCustomerToday.isEmpty()) {
                for (String s : backCustomerToday) {
                    Integer i = todayBackCount.get(s);
                    if (i == null) {
                        todayBackCount.put(s, 1);
                    } else {
                        todayBackCount.put(s, i + 1);
                    }
                }
            }
            if (!todayBackCount.isEmpty()) {
                for (String key : todayBackCount.keySet()) {//求算本日多次回头用户
                    if (todayBackCount.get(key) == 1) {
                        todayBackTwoCustomer.add(key);
                    } else {
                        todayBackTwoMoreCustomer.add(key);
                    }
                }
            }

            //上旬回头
            if (!customerBeforeFirstOfMonth.isEmpty()) {
                for (String s1 : customerBeforeFirstOfMonth) {//上旬有 今日也有
                    if (customerInFirstOfMonth.contains(s1)) {
                        firstOfMonthBackCustomer.add(s1);//去重 直接用长度来代替上旬回头用户的总数
                        backCustomerFirstOfMonth.add(s1);//不去重
                    }
                }
            }

            //定义一个map 来存放上旬每个回头用户存在的次数
            Map<String, Integer> firstOfMonthBackCount = new HashMap();
            if (!backCustomerFirstOfMonth.isEmpty()) {
                for (String s : backCustomerFirstOfMonth) {
                    Integer i = firstOfMonthBackCount.get(s);
                    if (i == null) {
                        firstOfMonthBackCount.put(s, 1);
                    } else {
                        firstOfMonthBackCount.put(s, i + 1);
                    }
                }
            }

            if (!firstOfMonthBackCount.isEmpty()) {
                for (String key : firstOfMonthBackCount.keySet()) {//求算上旬
                    // 多次回头用户
                    if (firstOfMonthBackCount.get(key) == 1) {
                        firstOfMonthBackTwoCustomer.add(key);
                    } else if (firstOfMonthBackCount.get(key) > 1) {
                        firstOfMonthBackTwoMoreCustomer.add(key);
                    }
                }
            }

            //中旬回头
            if (!customerBeforeMiddleOfMonth.isEmpty()) {
                for (String s1 : customerBeforeMiddleOfMonth) {//中旬有 今日也有
                    if (customerInMiddleOfMonth.contains(s1)) {
                        middleOfMonthBackCustomer.add(s1);//去重 直接用长度来代替上旬回头用户的总数
                        backCustomerMiddleOfMonth.add(s1);//不去重
                    }
                }
            }

            //定义一个map 来存放中旬每个回头用户存在的次数
            Map<String, Integer> middleOfMonthBackCount = new HashMap();
            if (!backCustomerMiddleOfMonth.isEmpty()) {
                for (String s : backCustomerMiddleOfMonth) {
                    Integer i = middleOfMonthBackCount.get(s);
                    if (i == null) {
                        middleOfMonthBackCount.put(s, 1);
                    } else {
                        middleOfMonthBackCount.put(s, i + 1);
                    }
                }
            }

            if (!middleOfMonthBackCount.isEmpty()) {
                for (String key : middleOfMonthBackCount.keySet()) {//求算中旬
                    // 多次回头用户
                    if (middleOfMonthBackCount.get(key) == 1) {
                        middleOfMonthBackTwoCustomer.add(key);
                    } else if (middleOfMonthBackCount.get(key) > 1) {
                        middleOfMonthBackTwoMoreCustomer.add(key);
                    }
                }
            }


            //下旬回头
            if (!customerBeforeLastOfMonth.isEmpty()) {
                for (String s1 : customerBeforeLastOfMonth) {//下旬有 下旬之前也有
                    if (customerInLastOfMonth.contains(s1)) {
                        lastOfMonthBackCustomer.add(s1);//去重 直接用长度来代替下旬回头用户的总数
                        backCustomerLastOfMonth.add(s1);//不去重
                    }
                }
            }

            //定义一个map 来存放下旬每个回头用户存在的次数
            Map<String, Integer> LastOfMonthBackCount = new HashMap();
            if (!backCustomerLastOfMonth.isEmpty()) {
                for (String s : backCustomerLastOfMonth) {
                    Integer i = LastOfMonthBackCount.get(s);
                    if (i == null) {
                        LastOfMonthBackCount.put(s, 1);
                    } else {
                        LastOfMonthBackCount.put(s, i + 1);
                    }
                }
            }

            if (!LastOfMonthBackCount.isEmpty()) {
                for (String key : LastOfMonthBackCount.keySet()) {//求算下旬
                    // 多次回头用户
                    if (LastOfMonthBackCount.get(key) == 1) {
                        lastOfMonthBackTwoCustomer.add(key);
                    } else if (LastOfMonthBackCount.get(key) > 1) {
                        lastOfMonthBackTwoMoreCustomer.add(key);
                    }
                }
            }

            //本月回头

            if (!customerBeforeMonth.isEmpty()) {
                for (String s1 : customerBeforeMonth) {//本月有 本月之前也有
                    if (customerInMonth.contains(s1)) {
                        monthBackCustomer.add(s1);//去重 直接用长度来代替本月回头用户的总数
                        backCustomerMonth.add(s1);//不去重
                    }
                }
            }

            //定义一个map 来存放本月每个回头用户存在的次数
            Map<String, Integer> monthBackCount = new HashMap();
            if (!backCustomerMonth.isEmpty()) {
                for (String s : backCustomerMonth) {
                    Integer i = monthBackCount.get(s);
                    if (i == null) {
                        monthBackCount.put(s, 1);
                    } else {
                        monthBackCount.put(s, i + 1);
                    }
                }
            }

            if (!monthBackCount.isEmpty()) {
                for (String key : monthBackCount.keySet()) {//求算本月
                    // 多次回头用户
                    if (monthBackCount.get(key) == 1) {
                        monthBackTwoCustomer.add(key);
                    } else if (monthBackCount.get(key) > 1) {
                        monthBackTwoMoreCustomer.add(key);
                    }
                }
            }

            int dayAppraiseNum = 0;//当日评价的总单数
            int firstOfMonthAppraiseNum = 0;//上旬评价的总单数
            int middleOfMonthAppraiseSum = 0;//中旬评价的单数
            int lastOfMonthAppraiseSum = 0;//下旬评价的单数
            int monthAppraiseSum = 0;//本月评价的单数

            int dayAppraiseSum = 0;//当日所有评价的总分数
            int firstOfMonthAppraiseSum = 0;//上旬所有评价的总分数
            int middleOfMonthAppraiseNum = 0;//中旬所有评价的总分数
            int lastOfMonthAppraiseNum = 0;//下旬所有评价的总分数
            int monthAppraiseNum = 0;//本月所有评价的总分数
            /**
             * 满意度重新封装
             */
            if (!orders.isEmpty()) {
                for (Order o : orders) {
                    //封装   1.resto订单总额     3.resto订单总数  4订单中的实收总额  5新增用户的订单总额  6自然到店的用户总额  7分享到店的用户总额
                    //8回头用户的订单总额  9二次回头用户的订单总额  10多次回头用户的订单总额
                    //本日 begin-----------------------
                    if (DateUtil.getDayByYesterDay(o.getCreateTime()).contains(2)) {
                        //1.resto订单总额
                        todayRestoTotal = todayRestoTotal.add(o.getOrderMoney());
                        wo.setRestoTotal(todayRestoTotal);

                        //3.resto的订单总数
                        if (o.getParentOrderId() == null) {
                            todayRestoCount.add(o.getId());
                        }
                        wo.setRedCount(todayRestoCount.size());
                        //4.订单中实收总额
                        if (o.getOrderPaymentItems() != null) {
                            for (OrderPaymentItem oi : o.getOrderPaymentItems()) {
                                if (oi.getPaymentModeId() == PayMode.WEIXIN_PAY || oi.getPaymentModeId() == PayMode.ALI_PAY || oi.getPaymentModeId() == PayMode.MONEY_PAY || oi.getPaymentModeId() == PayMode.ARTICLE_BACK_PAY) {
                                    todayPayRestoTotal = todayRestoTotal.add(oi.getPayValue());
                                }
                            }
                        }
                        //5.新增用户的订单总额
                        if (todayNewCutomer.contains(o.getCustomerId())) {
                            todayNewCustomerRestoTotal = todayNewCustomerRestoTotal.add(o.getOrderMoney());
                        }
                        //6自然到店的用户订单总额
                        if (todayNormalNewCustomer.contains(o.getCustomerId())) {
                            todayNewNormalCustomerRestoTotal = todayNewNormalCustomerRestoTotal.add(o.getOrderMoney());
                        }
                        //7.分享到店的用户订单总额
                        if (todayShareNewCutomer.contains(o.getCustomerId())) {
                            todayNewShareCustomerRestoTotal = todayNewShareCustomerRestoTotal.add(o.getOrderMoney());
                        }

                        //8.回头用户的订单总额
                        if (todayBackCustomer.contains(o.getCustomerId())) {
                            todayBackCustomerRestoTotal = todayBackCustomerRestoTotal.add(o.getOrderMoney());
                        }

                        //9.二次回头用户的订单总额
                        if (todayBackTwoCustomer.contains(o.getCustomerId())) {
                            todayBackTwoCustomerRestoTotal = todayBackTwoCustomerRestoTotal.add(o.getOrderMoney());
                        }

                        //10.多次回头用户的订单总额
                        if (todayBackTwoMoreCustomer.contains(o.getCustomerId())) {
                            todayBackTwoMoreCustomerRestoTotal = todayBackTwoMoreCustomerRestoTotal.add(o.getOrderMoney());
                        }
                        //本日end----------

                        //上旬begin------------------
                        if (DateUtil.getDayByYesterDay(o.getCreateTime()).contains(4)) {
                            //1.resto订单总额
                            firstOfMonthRestoTotal = firstOfMonthRestoTotal.add(o.getOrderMoney());

                            //3.resto的订单总数
                            if (o.getParentOrderId() == null) {
                                firstOfMonthRestoCount.add(o.getId());
                            }
                            //4.订单中实收总额
                            if (o.getOrderPaymentItems() != null) {
                                for (OrderPaymentItem oi : o.getOrderPaymentItems()) {
                                    if (oi.getPaymentModeId() == PayMode.WEIXIN_PAY || oi.getPaymentModeId() == PayMode.ALI_PAY || oi.getPaymentModeId() == PayMode.MONEY_PAY || oi.getPaymentModeId() == PayMode.ARTICLE_BACK_PAY) {
                                        firstOfMonthPayRestoTotal = firstOfMonthRestoTotal.add(oi.getPayValue());
                                    }
                                }
                            }
                            //5.新增用户的订单总额

                            if (firstOfMonthNewCustomer.contains(o.getCustomerId())) {
                                firstOfMonthNewCustomerRestoTotal = firstOfMonthNewCustomerRestoTotal.add(o.getOrderMoney());
                            }
                            //6自然到店的用户订单总额
                            if (firstOfMonthNormalCustomer.contains(o.getCustomerId())) {
                                firstOfMonthNewNormalCustomerRestoTotal = firstOfMonthNewNormalCustomerRestoTotal.add(o.getOrderMoney());
                            }
                            //7.分享到店的用户订单总额
                            if (firstOfMonthShareCustomer.contains(o.getCustomerId())) {
                                firstOfMonthNewShareCustomerRestoTotal = firstOfMonthNewShareCustomerRestoTotal.add(o.getOrderMoney());
                            }

                            //8.回头用户的订单总额
                            if (firstOfMonthBackCustomer.contains(o.getCustomerId())) {
                                firstOfMonthBackCustomerRestoTotal = firstOfMonthBackCustomerRestoTotal.add(o.getAmountWithChildren());
                            }

                            //9.二次回头用户的订单总额
                            if (firstOfMonthBackTwoCustomer.contains(o.getCustomerId())) {
                                firstOfMonthBackTwoCustomerRestoTotal = firstOfMonthBackTwoCustomerRestoTotal.add(o.getOrderMoney());
                            }

                            //10.多次回头用户的订单总额
                            if (firstOfMonthBackTwoMoreCustomer.contains(o.getCustomerId())) {
                                firstOfMonthBackTwoMoreCustomerRestoTotal = firstOfMonthBackTwoMoreCustomerRestoTotal.add(o.getOrderMoney());
                            }

                        }

                        //上旬end -------------------

                        //中旬begin------------------
                        if (DateUtil.getDayByYesterDay(o.getCreateTime()).contains(6)) {
                            //1.resto订单总额
                            middleOfMonthRestoTotal = middleOfMonthRestoTotal.add(o.getOrderMoney());

                            //3.resto的订单总数
                            if (o.getParentOrderId() == null) {
                                middleOfMonthRestoCount.add(o.getId());
                            }
                            //4.订单中实收总额
                            if (o.getOrderPaymentItems() != null) {
                                for (OrderPaymentItem oi : o.getOrderPaymentItems()) {
                                    if (oi.getPaymentModeId() == PayMode.WEIXIN_PAY || oi.getPaymentModeId() == PayMode.ALI_PAY || oi.getPaymentModeId() == PayMode.MONEY_PAY || oi.getPaymentModeId() == PayMode.ARTICLE_BACK_PAY) {
                                        middleOfMonthPayRestoTotal = middleOfMonthRestoTotal.add(oi.getPayValue());
                                    }
                                }
                            }
                            //5.新增用户的订单总额

                            if (middleOfMonthNewCustomer.contains(o.getCustomerId())) {
                                middleOfMonthNewCustomerRestoTotal = middleOfMonthNewCustomerRestoTotal.add(o.getOrderMoney());
                            }
                            //6自然到店的用户订单总额
                            if (middleOfMonthNormalCustomer.contains(o.getCustomerId())) {
                                middleOfMonthNewNormalCustomerRestoTotal = middleOfMonthNewNormalCustomerRestoTotal.add(o.getOrderMoney());
                            }
                            //7.分享到店的用户订单总额
                            if (middleOfMonthShareCustomer.contains(o.getCustomerId())) {
                                middleOfMonthNewShareCustomerRestoTotal = middleOfMonthNewShareCustomerRestoTotal.add(o.getOrderMoney());
                            }

                            //8.回头用户的订单总额
                            if (middleOfMonthBackCustomer.contains(o.getCustomerId())) {
                                middleOfMonthBackCustomerRestoTotal = middleOfMonthBackCustomerRestoTotal.add(o.getOrderMoney());
                            }

                            //9.二次回头用户的订单总额
                            if (middleOfMonthBackTwoCustomer.contains(o.getCustomerId())) {
                                middleOfMonthBackTwoCustomerRestoTotal = middleOfMonthBackTwoCustomerRestoTotal.add(o.getOrderMoney());
                            }

                            //10.多次回头用户的订单总额
                            if (middleOfMonthBackTwoMoreCustomer.contains(o.getCustomerId())) {
                                middleOfMonthBackTwoMoreCustomerRestoTotal = middleOfMonthBackTwoMoreCustomerRestoTotal.add(o.getOrderMoney());
                            }

                        }

                        //中旬end -------------------

                        //下旬begin------------------
                        if (DateUtil.getDayByYesterDay(o.getCreateTime()).contains(8)) {
                            //1.resto订单总额
                            lastOfMonthRestoTotal = lastOfMonthRestoTotal.add(o.getOrderMoney());

                            //3.resto的订单总数
                            if (o.getParentOrderId() == null) {
                                lastOfMonthRestoCount.add(o.getId());
                            }
                            //4.订单中实收总额
                            if (o.getOrderPaymentItems() != null) {
                                for (OrderPaymentItem oi : o.getOrderPaymentItems()) {
                                    if (oi.getPaymentModeId() == PayMode.WEIXIN_PAY || oi.getPaymentModeId() == PayMode.ALI_PAY || oi.getPaymentModeId() == PayMode.MONEY_PAY || oi.getPaymentModeId() == PayMode.ARTICLE_BACK_PAY) {
                                        lastOfMonthPayRestoTotal = lastOfMonthRestoTotal.add(oi.getPayValue());
                                    }
                                }
                            }
                            //5.新增用户的订单总额

                            if (lastOfMonthNewCustomer.contains(o.getCustomerId())) {
                                lastOfMonthNewCustomerRestoTotal = lastOfMonthNewCustomerRestoTotal.add(o.getOrderMoney());
                            }
                            //6自然到店的用户订单总额
                            if (lastOfMonthNormalCustomer.contains(o.getCustomerId())) {
                                lastOfMonthNewNormalCustomerRestoTotal = lastOfMonthNewNormalCustomerRestoTotal.add(o.getOrderMoney());
                            }
                            //7.分享到店的用户订单总额
                            if (lastOfMonthShareCustomer.contains(o.getCustomerId())) {
                                lastOfMonthNewShareCustomerRestoTotal = lastOfMonthNewShareCustomerRestoTotal.add(o.getOrderMoney());
                            }

                            //8.回头用户的订单总额
                            if (lastOfMonthBackCustomer.contains(o.getCustomerId())) {
                                lastOfMonthBackCustomerRestoTotal = lastOfMonthBackCustomerRestoTotal.add(o.getOrderMoney());
                            }

                            //9.二次回头用户的订单总额
                            if (lastOfMonthBackTwoCustomer.contains(o.getCustomerId())) {
                                lastOfMonthBackTwoCustomerRestoTotal = lastOfMonthBackTwoCustomerRestoTotal.add(o.getOrderMoney());
                            }

                            //10.多次回头用户的订单总额
                            if (lastOfMonthBackTwoMoreCustomer.contains(o.getCustomerId())) {
                                lastOfMonthBackTwoMoreCustomerRestoTotal = lastOfMonthBackTwoMoreCustomerRestoTotal.add(o.getOrderMoney());
                            }

                        }
                        //下旬end -------------------

                        //本月begin---------------
                        if (DateUtil.getDayByYesterDay(o.getCreateTime()).contains(10)) {
                            //1.resto订单总额
                            monthRestoTotal = monthRestoTotal.add(o.getOrderMoney());

                            //3.resto的订单总数
                            if (o.getParentOrderId() == null) {
                                monthRestoCount.add(o.getId());
                            }
                            //4.订单中实收总额
                            if (o.getOrderPaymentItems() != null) {
                                for (OrderPaymentItem oi : o.getOrderPaymentItems()) {
                                    if (oi.getPaymentModeId() == PayMode.WEIXIN_PAY || oi.getPaymentModeId() == PayMode.ALI_PAY || oi.getPaymentModeId() == PayMode.MONEY_PAY || oi.getPaymentModeId() == PayMode.ARTICLE_BACK_PAY) {
                                        monthPayRestoTotal = monthRestoTotal.add(oi.getPayValue());
                                    }
                                }
                            }
                            //5.新增用户的订单总额

                            if (monthNewCustomer.contains(o.getCustomerId())) {
                                monthNewCustomerRestoTotal = monthNewCustomerRestoTotal.add(o.getOrderMoney());
                            }
                            //6自然到店的用户订单总额
                            if (monthNormalCustomer.contains(o.getCustomerId())) {
                                monthNewNormalCustomerRestoTotal = monthNewNormalCustomerRestoTotal.add(o.getOrderMoney());
                            }
                            //7.分享到店的用户订单总额
                            if (monthShareCustomer.contains(o.getCustomerId())) {
                                monthNewShareCustomerRestoTotal = monthNewShareCustomerRestoTotal.add(o.getOrderMoney());
                            }

                            //8.回头用户的订单总额
                            if (monthBackCustomer.contains(o.getCustomerId())) {
                                monthBackCustomerRestoTotal = monthBackCustomerRestoTotal.add(o.getOrderMoney());
                            }

                            //9.二次回头用户的订单总额
                            if (monthBackTwoCustomer.contains(o.getCustomerId())) {
                                monthBackTwoCustomerRestoTotal = monthBackTwoCustomerRestoTotal.add(o.getOrderMoney());
                            }

                            //10.多次回头用户的订单总额
                            if (monthBackTwoMoreCustomer.contains(o.getCustomerId())) {
                                monthBackTwoMoreCustomerRestoTotal = monthBackTwoMoreCustomerRestoTotal.add(o.getOrderMoney());
                            }

                        }

                        //本月end---------------
                    }

                    //查询resto充值(微信充值+pos充值)  实收总额 = (微信支付+支付宝+其他支付)+(pos充值+微信充值)
                    List<ChargeOrder> chargeOrderList = chargeOrderService.selectByDateAndShopId(beginMonth, endMonth, shopDetail.getName());
                    if (!chargeOrderList.isEmpty()) {
                        for (ChargeOrder c : chargeOrderList) {
                            //本日
                            if (DateUtil.getDayByYesterDay(c.getCreateTime()).contains(11)) {
                                todayPayRestoTotal = todayRestoTotal.add(c.getChargeMoney());
                            }
                            //上旬
                            if (DateUtil.getDayByYesterDay(c.getCreateTime()).contains(4)) {
                                firstOfMonthPayRestoTotal = firstOfMonthRestoTotal.add(c.getChargeMoney());
                            }
                            //中旬
                            if (DateUtil.getDayByYesterDay(c.getCreateTime()).contains(6)) {
                                lastOfMonthPayRestoTotal = lastOfMonthRestoTotal.add(c.getChargeMoney());
                            }
                            //下旬
                            if (DateUtil.getDayByYesterDay(c.getCreateTime()).contains(8)) {
                                lastOfMonthPayRestoTotal = lastOfMonthRestoTotal.add(c.getChargeMoney());
                            }
                            //本月
                            if (DateUtil.getDayByYesterDay(c.getCreateTime()).contains(10)) {
                                monthPayRestoTotal = monthRestoTotal.add(c.getChargeMoney());
                            }
                        }
                    }

                }
                //4封装新增用户

                wo.setNewCustomerCount(todayNewCutomer.size());//新增用户
                wo.setNewCustomerTotal(todayNewCustomerRestoTotal);
                wo.setNewNormalCustomerCount(todayNormalNewCustomer.size());//新增自然到店
                wo.setNewNormalCustomerTotal(todayNewNormalCustomerRestoTotal);
                wo.setNewShareCustomerCount(todayShareNewCutomer.size());//分享到店
                wo.setNewShareCustomerTotal(todayNewShareCustomerRestoTotal);

                //5.封装回头用户
                wo.setBackCustomerCount(todayBackCustomer.size());
                wo.setBackCustomerToatal(todayBackCustomerRestoTotal);//回头
                wo.setBackTwoCustomerCount(todayBackTwoCustomer.size());
                wo.setBackTwoCustomerTotal(todayBackTwoCustomerRestoTotal);//二次回头
                wo.setBackTwoMoreCustomerCount(todayBackTwoMoreCustomer.size());
                wo.setBackTwoCustomerTotal(todayBackTwoCustomerRestoTotal);

                //封装上旬用户的数据

                //上旬开始---
                wo.setsAvgScore(firstOfMonthSatisfaction);//上旬满意度
                wo.setsRestoTotal(firstOfMonthRestoTotal);
                wo.setsEnterTotal(firstOfMonthEnterTotal);
                wo.setsAllTotal(firstOfMonthRestoTotal.add(firstOfMonthEnterTotal));
                //新增数据
                wo.setsNewCustomr(firstOfMonthNewCustomer.size());
                wo.setsNewCustomerTotal(firstOfMonthNewCustomerRestoTotal);
                wo.setsNewNormalCustomer(firstOfMonthNormalCustomer.size());
                wo.setsNewNormalCustomerTotal(firstOfMonthNewNormalCustomerRestoTotal);
                wo.setsNewShareCustomer(firstOfMonthShareCustomer.size());
                wo.setsNewShareCustomerTotal(firstOfMonthNewShareCustomerRestoTotal);
                //回头数据
                wo.setsBackCustomer(firstOfMonthBackCustomer.size());
                wo.setsBackCustomerTotal(firstOfMonthBackCustomerRestoTotal);
                wo.setsBackTwoCustomer(firstOfMonthBackTwoCustomer.size());
                wo.setsBackCustomerTotal(firstOfMonthBackCustomerRestoTotal);
                wo.setsBackTwoMoreCustomer(firstOfMonthBackTwoMoreCustomer.size());
                wo.setsBackTwoMoreCustomerTotal(firstOfMonthBackTwoMoreCustomerRestoTotal);


                //中旬开始-----------
                wo.setzAvgScore(middleOfMonthSatisfaction);//中旬满意
                wo.setzRestoTotal(middleOfMonthRestoTotal);
                wo.setzEnterTotal(middleOfMonthEnterTotal);
                wo.setzAllTotal(middleOfMonthRestoTotal.add(middleOfMonthEnterTotal));

                wo.setzNewCustomerCount(middleOfMonthNewCustomer.size());
                wo.setzNewCustomerTotal(middleOfMonthNewCustomerRestoTotal);
                wo.setsNewNormalCustomer(middleOfMonthNormalCustomer.size());
                wo.setsNewNormalCustomerTotal(middleOfMonthNewNormalCustomerRestoTotal);
                wo.setsNewShareCustomer(middleOfMonthShareCustomer.size());
                wo.setsNewShareCustomerTotal(middleOfMonthNewShareCustomerRestoTotal);

                wo.setzBackCustomer(middleOfMonthBackCustomer.size());
                wo.setzBackCustomerTotal(middleOfMonthBackCustomerRestoTotal);
                wo.setzBackTwoCustomer(middleOfMonthBackTwoCustomer.size());
                wo.setzBackCustomerTotal(middleOfMonthBackCustomerRestoTotal);
                wo.setzBackTwoMoreCustomer(middleOfMonthBackTwoMoreCustomer.size());
                wo.setzBackTwoMoreCustomerTotal(middleOfMonthBackTwoMoreCustomerRestoTotal);


                //下旬开始----------------------
                wo.setxAvgScore(lastOfMonthSatisfaction);//下旬满意
                wo.setxRestoTotal(lastOfMonthRestoTotal);
                wo.setxEnterTotal(lastOfMonthEnterTotal);
                wo.setxAllTotal(lastOfMonthRestoTotal.add(lastOfMonthEnterTotal));

                wo.setxNewCustomer(lastOfMonthNewCustomer.size());
                wo.setxNewCustomerTotal(lastOfMonthNewCustomerRestoTotal);
                wo.setxNewNormalCustomer(lastOfMonthNormalCustomer.size());
                wo.setxNewNormalCustomerTotal(lastOfMonthNewNormalCustomerRestoTotal);
                wo.setxNewShareCustomer(lastOfMonthShareCustomer.size());
                wo.setxNewShareCustomerTotal(lastOfMonthNewShareCustomerRestoTotal);

                wo.setxBackCustomer(lastOfMonthBackCustomer.size());
                wo.setxBackCustomerTotal(lastOfMonthBackCustomerRestoTotal);
                wo.setxBackTwoCustomer(lastOfMonthBackTwoCustomer.size());
                wo.setxBackCustomerTotal(lastOfMonthBackCustomerRestoTotal);
                wo.setxBackTwoMoreCustomer(lastOfMonthBackTwoMoreCustomer.size());
                wo.setxBackTwoMoreCustomerTotal(lastOfMonthBackTwoMoreCustomerRestoTotal);


                //本月开始---------------------------
                wo.setmAvgScore(monthSatisfaction);//本月满意
                wo.setmRestoTotal(monthRestoTotal);
                wo.setmEnterTotal(monthEnterTotal);
                wo.setmAllTotal(monthEnterTotal.add(monthEnterTotal));

                wo.setmNewCustomr(monthNewCustomer.size());
                wo.setmNewCustomerTotal(monthNewCustomerRestoTotal);
                wo.setmNewNormalCustomer(monthNormalCustomer.size());
                wo.setmNewNormalCustomerTotal(monthNewNormalCustomerRestoTotal);
                wo.setmNewShareCustomer(monthShareCustomer.size());
                wo.setmNewShareCustomerTotal(monthNewShareCustomerRestoTotal);

                wo.setmBackCustomer(monthBackCustomer.size());
                wo.setmBackCustomerTotal(monthBackCustomerRestoTotal);
                wo.setmBackTwoCustomer(monthBackTwoCustomer.size());
                wo.setmBackCustomerTotal(monthBackCustomerRestoTotal);
                wo.setmBackTwoMoreCustomer(monthBackTwoMoreCustomer.size());
                wo.setmBackTwoMoreCustomerTotal(monthBackTwoMoreCustomerRestoTotal);
                weorderdetailMapper.insert(wo);
            }
        }
    }
    @Override
    public void deleteYesterDayData(String currentBrandId) {
        String zuoriDay = DateUtil.getAfterDayDateStr("-1");
        List<ShopDetail> shopDetails = shopDetailService.selectByBrandId(currentBrandId);
        for (ShopDetail shopDetail : shopDetails) {
            //充值详情
            //1/查看昨日是否有数据
            List<WeChargeLog> weChargeLogList = weChargeLogService.selectByShopIdAndTime(shopDetail.getId(),zuoriDay);
            if(!weChargeLogList.isEmpty()){
                //删除已经有的数据 使用批量删除的方法
                List<Long> ids = new ArrayList<>();
                for(WeChargeLog weChargeLog:weChargeLogList){
                    ids.add(weChargeLog.getId());
                }
                if(ids.size()>0){
                    weChargeLogService.deleteByIds(ids);
                }

            }
            List<WeItem> weItemList = weItemService.selectByShopIdAndTime(zuoriDay,shopDetail.getId());
            List<WeReturnCustomer> weReturnCustomerList = weReturnCustomerService.selectByShopIdAndTime(zuoriDay,shopDetail.getId());
            List<WeReturnItem> weReturnItemList = weReturnItemService.selectByShopIdAndTime(zuoriDay,shopDetail.getId());
            if(!weItemList.isEmpty()){
                List<Long> ids = new ArrayList<>();
                for(WeItem weItem:weItemList){
                    ids.add(weItem.getId());
                }
                if(ids.size()>0){
                    weItemService.deleteByIds(ids);
                }

            }
            if(!weReturnCustomerList.isEmpty()){
                List<Long> ids = new ArrayList<>();
                for(WeReturnCustomer weReturnCustomer :weReturnCustomerList){
                    ids.add(weReturnCustomer.getId());
                }
                if(ids.size()>0){
                    weReturnCustomerService.deleteByIds(ids);
                }

            }
            if(!weReturnItemList.isEmpty()){
                List<Long> ids = new ArrayList<>();
                for(WeReturnItem weReturnItem:weReturnItemList){
                    ids.add(weReturnItem.getId());
                }
                if(ids.size()>0){
                    weReturnItemService.deleteByIds(ids);
                }

            }
        }
    }
}
