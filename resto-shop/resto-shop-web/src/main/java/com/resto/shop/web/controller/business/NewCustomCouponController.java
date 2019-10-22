package com.resto.shop.web.controller.business;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.DistributionMode;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.TimeConsType;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.form.ProductCouponForm;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.model.NewCustomCoupon;
import com.resto.shop.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("newcustomcoupon")
public class NewCustomCouponController extends GenericController {

    @Resource
    NewCustomCouponService newcustomcouponService;

    @Resource
    DistributionModeService distributionmodeService;

    @Resource
    BrandService brandService;

    @Resource
    OrderService orderService;

    @Resource
    ChargeOrderService chargeOrderService;

    @Resource
    CustomerService customerService;

    @Resource
    CouponService couponService;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    BrandSettingService brandSettingService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/list")
    public void list() {
    }

    /**
     * 查询所有的优惠券设置
     *
     * @return
     */
    @RequestMapping("/list_all")
    @ResponseBody
    public List<NewCustomCoupon> listData() {
        return newcustomcouponService.selectListByBrandId(getCurrentBrandId(), getCurrentShopId());
    }

    /**
     * 查询所有的产品券券设置
     *
     * @return
     */
    @RequestMapping("/productCouponlist")
    @ResponseBody
    public List<NewCustomCoupon> productCouponlist() {
        return newcustomcouponService.selectProductCouponListByBrandId(getCurrentBrandId(), getCurrentShopId());
    }

    /**
     * 查询当前店铺的优惠券
     *
     * @return
     */
    @RequestMapping("/list_all_shopId")
    @ResponseBody
    public List<NewCustomCoupon> listDataByShopId() {
        return newcustomcouponService.selectListShopId(getCurrentShopId());
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(Long id) {
        NewCustomCoupon newcustomcoupon = newcustomcouponService.selectById(id);
        return getSuccessResult(newcustomcoupon);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid NewCustomCoupon newCustomCoupon, HttpServletRequest request) {
        //选择优惠券时间类型1时,日期需要填写
        if (TimeConsType.TYPENUM == newCustomCoupon.getTimeConsType()) {
            if (newCustomCoupon.getCouponValiday() == null || "".equals(newCustomCoupon.getCouponValiday())) {
                log.debug("日期不能为空");
                return new Result("日期不能为空", false);
            }
            //选择优惠券时间类型2时，开始和结束时间必须填
        } else if (TimeConsType.TYPETIME == newCustomCoupon.getTimeConsType()) {
            if (newCustomCoupon.getBeginDateTime() == null || newCustomCoupon.getEndDateTime() == null) {
                log.debug("优惠券开始或者结束时间不能为空");
                return new Result("优惠券开始或者结束时间不能为空", false);
            }
        } else {
            if ((newCustomCoupon.getBeginTime() != null && newCustomCoupon.getEndTime() != null) || (newCustomCoupon.getBeginDateTime() != null && newCustomCoupon.getEndDateTime() != null)) {
                if (newCustomCoupon.getBeginTime().compareTo(newCustomCoupon.getEndTime()) > 0) {
                    log.debug("开始时间大于结束时间");
                    return new Result("开始时间大于结束时间", false);
                } else if (newCustomCoupon.getBeginDateTime().compareTo(newCustomCoupon.getEndDateTime()) > 0) {
                    log.debug("优惠券的开始时间不能大于结束时间");
                    return new Result("优惠券的开始时间不能大于结束时间", false);
                }
            }
        }
        newCustomCoupon.setBrandId(getCurrentBrandId());
        //如果是店铺优惠券
        if (newCustomCoupon.getIsBrand() == 0) {
            newCustomCoupon.setShopDetailId(getCurrentShopId());
        }
        newCustomCoupon.setCreateTime(new Date());
        newcustomcouponService.insertNewCustomCoupon(newCustomCoupon);
//        if (RedisUtil.get(getCurrentBrandId() + "newCustomCoupon") != null) {
//            RedisUtil.remove(getCurrentBrandId() + "newCustomCoupon");
//        }
        if (redisService.get(getCurrentBrandId() + "newCustomCoupon") != null) {
            redisService.remove(getCurrentBrandId() + "newCustomCoupon");
        }

        return Result.getSuccess();
    }

    @RequestMapping("createProductCoupon")
    @ResponseBody
    public Result createProductCoupon(@Valid ProductCouponForm productCouponForm, HttpServletRequest request) {
        JSONResult jsonResult = new JSONResult();

        if(productCouponForm.isBrand==null){
            return new Result("请选择品牌使用或者门店使用", false);
        }else if(productCouponForm.isBrand==0){
            if(productCouponForm.ShopId==null || productCouponForm.ShopId.equals("")) {
                return new Result("请选择所属店铺名称", false);
            }
        } else if(productCouponForm.couponName==null || productCouponForm.couponName.equals("")){
            return new Result("请输入产品券名称", false);
        }else if(productCouponForm.deductionType==null) {
            return new Result("请选择抵扣类型", false);
        }else if(productCouponForm.deductionType==1){
            if(productCouponForm.couponValue==null || BigDecimal.ZERO.compareTo(productCouponForm.couponValue)>0) {
                return new Result("最大抵扣金额不能为空和小于0", false);
            }
        }
//        else if(productCouponForm.couponPhoto==null || "".equals(productCouponForm.couponPhoto)){
//            return new Result("请上传图片后再保存", false);
//        }

        NewCustomCoupon newCustomCoupon = new NewCustomCoupon();
        //选择产品券时间类型1时,日期需要填写
        if (TimeConsType.TYPENUM == productCouponForm.timeConsType) {
            if (productCouponForm.couponValiday == null || "".equals(productCouponForm.couponValiday)) {
                log.debug("日期不能为空");
                return new Result("日期不能为空", false);
            }
            newCustomCoupon.setCouponValiday(productCouponForm.couponValiday);
            //选择优惠券时间类型2时，开始和结束时间必须填
        } else if (TimeConsType.TYPETIME == productCouponForm.timeConsType) {
            if (productCouponForm.beginDateTime == null || productCouponForm.endDateTime == null) {
                log.debug("产品券开始或者结束时间不能为空");
                return new Result("优惠券开始或者结束时间不能为空", false);
            }
            newCustomCoupon.setBeginDateTime(productCouponForm.beginDateTime);
            newCustomCoupon.setEndDateTime(productCouponForm.endDateTime);
        }

        newCustomCoupon.setCouponName(productCouponForm.couponName);
        newCustomCoupon.setDeductionType(productCouponForm.deductionType);
        newCustomCoupon.setCouponMinMoney(BigDecimal.ZERO);
        newCustomCoupon.setBrandId(getCurrentBrandId());
        newCustomCoupon.setUseWithAccount(true);
        newCustomCoupon.setCouponNumber(1);
        if(productCouponForm.deductionType==1){
            newCustomCoupon.setCouponValue(productCouponForm.couponValue);
        }
        newCustomCoupon.setCouponMinMoney(BigDecimal.ZERO);
        newCustomCoupon.setActivty(productCouponForm.isActivty);
        newCustomCoupon.setDistributionModeId(productCouponForm.distributionModeId);
        newCustomCoupon.setTimeConsType(productCouponForm.timeConsType);
        newCustomCoupon.setCouponType(7);
        newCustomCoupon.setCouponPhoto(productCouponForm.couponPhoto);
        //如果是店铺优惠券
        if (productCouponForm.isBrand== 0) {
            newCustomCoupon.setShopDetailId(productCouponForm.ShopId);
            newCustomCoupon.setIsBrand(0);
        }else {
            newCustomCoupon.setIsBrand(1);
        }
        newCustomCoupon.setCreateTime(new Date());
        newcustomcouponService.insertNewCustomCoupon(newCustomCoupon);
//        if (RedisUtil.get(getCurrentBrandId() + "newCustomCoupon") != null) {
//            RedisUtil.remove(getCurrentBrandId() + "newCustomCoupon");
//        }
        if (redisService.get(getCurrentBrandId() + "newCustomCoupon") != null) {
            redisService.remove(getCurrentBrandId() + "newCustomCoupon");
        }

        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid NewCustomCoupon newCustomCoupon) {
        if (TimeConsType.TYPENUM == newCustomCoupon.getTimeConsType()) {
            if (newCustomCoupon.getCouponValiday() == null) {
                log.info("日期不能为空");
                return new Result("日期不能为空", false);
            }

        } else if (TimeConsType.TYPETIME == newCustomCoupon.getTimeConsType()) {
            if (newCustomCoupon.getBeginDateTime() == null || newCustomCoupon.getEndDateTime() == null) {
                log.info("优惠券开始或者结束时间不能为空");
                return new Result("优惠券开始或者结束时间不能为空", false);
            }
        } else {

            if ((newCustomCoupon.getBeginTime() != null && newCustomCoupon.getEndTime() != null) || (newCustomCoupon.getBeginDateTime() != null && newCustomCoupon.getEndDateTime() != null)) {
                if (newCustomCoupon.getBeginTime().compareTo(newCustomCoupon.getEndTime()) > 0) {
                    log.info("开始时间大于结束时间");
                    return new Result("开始时间大于结束时间", false);
                } else if (newCustomCoupon.getBeginDateTime().compareTo(newCustomCoupon.getEndDateTime()) > 0) {
                    log.info("优惠券的开始时间不能大于结束时间");
                    return new Result("优惠券的开始时间不能大于结束时间", false);
                }
            }
        }
        newCustomCoupon.setBrandId(getCurrentBrandId());
        if (newCustomCoupon.getIsBrand() == 1) {//如果改成品牌则设置店铺id为空
            newCustomCoupon.setShopDetailId(null);
        } else if (newCustomCoupon.getIsBrand() == 0) {//如果是店铺专有
            newCustomCoupon.setShopDetailId(getCurrentShopId());
        }

        newcustomcouponService.update(newCustomCoupon);
//        if (RedisUtil.get(getCurrentBrandId() + "newCustomCoupon") != null) {
//            RedisUtil.remove(getCurrentBrandId() + "newCustomCoupon");
//        }
        if (redisService.get(getCurrentBrandId() + "newCustomCoupon") != null) {
            redisService.remove(getCurrentBrandId() + "newCustomCoupon");
        }
        return Result.getSuccess();
    }

    @RequestMapping("modifyProductCoupon")
    @ResponseBody
    public Result modifyProductCoupon(@Valid ProductCouponForm productCouponForm, HttpServletRequest request) {

        NewCustomCoupon newCustomCoupon = newcustomcouponService.selectById(productCouponForm.id);

        newCustomCoupon.setCouponValue(productCouponForm.couponValue);
        newCustomCoupon.setActivty(productCouponForm.isActivty);
        newCustomCoupon.setCouponPhoto(productCouponForm.couponPhoto);
        newcustomcouponService.update(newCustomCoupon);
//        if (RedisUtil.get(getCurrentBrandId() + "newCustomCoupon") != null) {
//            RedisUtil.remove(getCurrentBrandId() + "newCustomCoupon");
//        }
        if (redisService.get(getCurrentBrandId() + "newCustomCoupon") != null) {
            redisService.remove(getCurrentBrandId() + "newCustomCoupon");
        }

        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Long id) {
        newcustomcouponService.delete(id);
//        if (RedisUtil.get(getCurrentBrandId() + "newCustomCoupon") != null) {
//            RedisUtil.remove(getCurrentBrandId() + "newCustomCoupon");
//        }
        if (redisService.get(getCurrentBrandId() + "newCustomCoupon") != null) {
            redisService.remove(getCurrentBrandId() + "newCustomCoupon");
        }
        return Result.getSuccess();
    }

    @RequestMapping("distributionmode/list_all")
    @ResponseBody
    public List<DistributionMode> lists() {
        return distributionmodeService.selectList();
    }

    @RequestMapping("distributionMode/list_one")
    @ResponseBody
    public DistributionMode listOne(Integer id) {
        return distributionmodeService.selectById(id);
    }

    @RequestMapping("/goToGrant")
    public String goToGrant(String couponId, Integer intoType, HttpServletRequest request) {
        request.setAttribute("couponId", couponId);
        request.setAttribute("intoType", intoType);
        return "newcustomcoupon/grantCoupon";
    }

    /**
     * 根据条件查询发放流失唤醒优惠券的用户
     *
     * @param selectMap
     * @return
     */
    @RequestMapping("/selectCustomer")
    @ResponseBody
    public Result selectCustomer(@RequestParam Map<String, String> selectMap) {
        return getSuccessResult(getCustomer(selectMap));
    }


    private List<JSONObject> getCustomer(Map<String, String> selectMap) {
        List<JSONObject> array = new ArrayList<>();
        try {
            //封装最后的返回值

            JSONObject object = new JSONObject();
            //根据查询条件先得到所有用户
            Map<String, Object> objectMap = new HashMap<>();
            for (Map.Entry map : selectMap.entrySet()) {
                objectMap.put(map.getKey().toString(), map.getValue());
            }
            List<Customer> customers = customerService.selectBySelectMap(objectMap);
            //得到当前时间的字符串
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String newDateString = format.format(new Date());
            //初始差距天数
            Integer daysBetween;
            //声明标识判断该用户是否满足订单条件默认不满足
            boolean meetOrder;
            List<String> customerIds = new ArrayList<>();
            for (Customer customer : customers) {
                object.put("customerId", customer.getId());
                if (!"1".equalsIgnoreCase(selectMap.get("intoType"))){
                    object.put("customer", customer);
                }
                object.put("customerType", customer.getIsBindPhone() ? "注册" : "未注册");
                object.put("isValue", "否");
                object.put("nickname", customer.getNickname());
                String sex = "男";
                if (customer.getSex() == 2) {
                    sex = "女";
                } else if (customer.getSex() == 0) {
                    sex = "未知";
                }
                object.put("sex", sex);
                object.put("telephone", customer.getTelephone() == null ? "--" : customer.getTelephone());
                object.put("birthday", customer.getCustomerDetail() != null && customer.getCustomerDetail().getBirthDate() != null
                        ? new SimpleDateFormat("yyyy-MM-dd").format(customer.getCustomerDetail().getBirthDate()) : "--");
                object.put("orderCount", 0);
                object.put("orderMoney", 0);
                object.put("avgOrderMoney", 0);
                object.put("useOrder", customer.getUseOrder());
                object.put("chargeOrder", customer.getChargeOrder());
                array.add(object);
                object = new JSONObject();
                customerIds.add(customer.getId());
            }
            //获取所有下过单的用户
            List<Map<String, String>> orderList = orderService.selectCustomerOrderCount(customerIds);
            //获取充值过的所有用户
            List<String> chargeOrderList = chargeOrderService.selectCustomerChargeOrder(customerIds);
            Iterator<JSONObject> iterator = array.iterator();
            array = new ArrayList<>();
            while (iterator.hasNext()) {
                object = iterator.next();
                meetOrder = false;
                if (object.getBoolean("useOrder")) {
                    for (Map orderMap : orderList) {
                        if (object.get("customerId").toString().equalsIgnoreCase(orderMap.get("customerId").toString())) {
                            //判断该用户满不满足订单条件
                            if (StringUtils.isBlank(selectMap.get("text")) && selectMap.size() != 0) {
                                //判断消费次数比较类型Begin
                                if (selectMap.get("orderCountType").equalsIgnoreCase("1")) {//消费次数比较类型为大于
                                    if (StringUtils.isNotBlank(selectMap.get("orderCount"))) {
                                        if (Integer.valueOf(orderMap.get("orderCount").toString()).compareTo(Integer.valueOf(selectMap.get("orderCount"))) > 0) {
                                            meetOrder = true;
                                        }
                                    } else {
                                        meetOrder = true;
                                    }
                                } else if (selectMap.get("orderCountType").equalsIgnoreCase("2")) {//消费次数比较类型为小于
                                    if (StringUtils.isNotBlank(selectMap.get("orderCount"))) {
                                        if (Integer.valueOf(orderMap.get("orderCount").toString()).compareTo(Integer.valueOf(selectMap.get("orderCount"))) < 0) {
                                            meetOrder = true;
                                        }
                                    } else {
                                        meetOrder = true;
                                    }
                                } else if (selectMap.get("orderCountType").equalsIgnoreCase("3")) {//消费次数比较类型为介于
                                    if (StringUtils.isNotBlank(selectMap.get("orderCountBegin")) && StringUtils.isBlank(selectMap.get("orderCountEnd"))) {//如果只录入了前面的数
                                        if (Integer.valueOf(orderMap.get("orderCount").toString()).compareTo(Integer.valueOf(selectMap.get("orderCountBegin"))) >= 0) {
                                            meetOrder = true;
                                        }
                                    } else if (StringUtils.isNotBlank(selectMap.get("orderCountEnd")) && StringUtils.isBlank(selectMap.get("orderCountBegin"))) {//如果只录入了后面的数
                                        if (Integer.valueOf(orderMap.get("orderCount").toString()).compareTo(Integer.valueOf(selectMap.get("orderCountEnd"))) <= 0) {
                                            meetOrder = true;
                                        }
                                    } else if (StringUtils.isNotBlank(selectMap.get("orderCountEnd")) && StringUtils.isNotBlank(selectMap.get("orderCountBegin"))) {//如果前后两个数都录入了
                                        if (Integer.valueOf(orderMap.get("orderCount").toString()).compareTo(Integer.valueOf(selectMap.get("orderCountBegin"))) >= 0
                                                && Integer.valueOf(orderMap.get("orderCount").toString()).compareTo(Integer.valueOf(selectMap.get("orderCountEnd"))) <= 0) {
                                            meetOrder = true;
                                        }
                                    } else {
                                        meetOrder = true;
                                    }
                                } else if (selectMap.get("orderCountType").equalsIgnoreCase("4")) {//消费次数比较类型为不介于
                                    if (StringUtils.isNotBlank(selectMap.get("orderCountBegin")) && StringUtils.isBlank(selectMap.get("orderCountEnd"))) {//如果只录入了前面的数
                                        if (Integer.valueOf(orderMap.get("orderCount").toString()).compareTo(Integer.valueOf(selectMap.get("orderCountBegin"))) < 0) {
                                            meetOrder = true;
                                        }
                                    } else if (StringUtils.isNotBlank(selectMap.get("orderCountEnd")) && StringUtils.isBlank(selectMap.get("orderCountBegin"))) {//如果只录入了后面的数
                                        if (Integer.valueOf(orderMap.get("orderCount").toString()).compareTo(Integer.valueOf(selectMap.get("orderCountEnd"))) > 0) {
                                            meetOrder = true;
                                        }
                                    } else if (StringUtils.isNotBlank(selectMap.get("orderCountEnd")) && StringUtils.isNotBlank(selectMap.get("orderCountBegin"))) {//如果前后两个数都录入了
                                        if (Integer.valueOf(orderMap.get("orderCount").toString()).compareTo(Integer.valueOf(selectMap.get("orderCountBegin"))) < 0
                                                || Integer.valueOf(orderMap.get("orderCount").toString()).compareTo(Integer.valueOf(selectMap.get("orderCountEnd"))) > 0) {
                                            meetOrder = true;
                                        }
                                    } else {
                                        meetOrder = true;
                                    }
                                }
                                //判断消费次数比较类型END
                                //判断消费总额比较类型Begin
                                if (meetOrder) {//在前一个条件满足的情况下在进行消费金额的判断
                                    if (selectMap.get("orderTotalType").equalsIgnoreCase("1")) {//消费总额比较类型为大于
                                        if (StringUtils.isNotBlank(selectMap.get("orderTotal"))) {
                                            if (!(new BigDecimal(orderMap.get("orderTotal").toString()).compareTo(new BigDecimal(selectMap.get("orderTotal"))) > 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    } else if (selectMap.get("orderTotalType").equalsIgnoreCase("2")) {//消费总额比较类型为小于
                                        if (StringUtils.isNotBlank(selectMap.get("orderTotal"))) {
                                            if (!(new BigDecimal(orderMap.get("orderTotal").toString()).compareTo(new BigDecimal(selectMap.get("orderTotal"))) < 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    } else if (selectMap.get("orderTotalType").equalsIgnoreCase("3")) {//消费总额比较类型为介于
                                        if (StringUtils.isNotBlank(selectMap.get("orderTotalBegin")) && StringUtils.isBlank(selectMap.get("orderTotalEnd"))) {//如果只录入了前面的数
                                            if (!(new BigDecimal(orderMap.get("orderTotal").toString()).compareTo(new BigDecimal(selectMap.get("orderTotalBegin"))) >= 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("orderTotalEnd")) && StringUtils.isBlank(selectMap.get("orderTotalBegin"))) {//如果只录入了后面的数
                                            if (!(new BigDecimal(orderMap.get("orderTotal").toString()).compareTo(new BigDecimal(selectMap.get("orderTotalEnd"))) <= 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("orderTotalBegin")) && StringUtils.isNotBlank(selectMap.get("orderTotalEnd"))) {//如果前后两个数都录入了
                                            if (!(new BigDecimal(orderMap.get("orderTotal").toString()).compareTo(new BigDecimal(selectMap.get("orderTotalBegin"))) >= 0
                                                    && new BigDecimal(orderMap.get("orderTotal").toString()).compareTo(new BigDecimal(selectMap.get("orderTotalEnd"))) <= 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    } else if (selectMap.get("orderTotalType").equalsIgnoreCase("4")) {//消费总额比较类型为不介于
                                        if (StringUtils.isNotBlank(selectMap.get("orderTotalBegin")) && StringUtils.isBlank(selectMap.get("orderTotalEnd"))) {//如果只录入了前面的数
                                            if (!(new BigDecimal(orderMap.get("orderTotal").toString()).compareTo(new BigDecimal(selectMap.get("orderTotalBegin"))) < 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("orderTotalEnd")) && StringUtils.isBlank(selectMap.get("orderTotalBegin"))) {//如果只录入了后面的数
                                            if (!(new BigDecimal(orderMap.get("orderTotal").toString()).compareTo(new BigDecimal(selectMap.get("orderTotalEnd"))) > 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("orderTotalEnd")) && StringUtils.isNotBlank(selectMap.get("orderTotalBegin"))) {//如果前后两个数都录入了
                                            if (!(new BigDecimal(orderMap.get("orderTotal").toString()).compareTo(new BigDecimal(selectMap.get("orderTotalBegin"))) < 0
                                                    || new BigDecimal(orderMap.get("orderTotal").toString()).compareTo(new BigDecimal(selectMap.get("orderTotalEnd"))) > 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    }
                                }
                                //判断消费总额比较类型End
                                //判断平均消费金额比较类型Begin
                                if (meetOrder) {//在前一个条件满足的情况下在进行消费金额的判断
                                    if (selectMap.get("avgOrderMoneyType").equalsIgnoreCase("1")) {//平均消费总额比较类型为大于
                                        if (StringUtils.isNotBlank(selectMap.get("avgOrderMoney"))) {
                                            if (!(new BigDecimal(orderMap.get("avgOrderMoney").toString()).compareTo(new BigDecimal(selectMap.get("avgOrderMoney"))) > 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    } else if (selectMap.get("avgOrderMoneyType").equalsIgnoreCase("2")) {//平均消费总额比较类型为小于
                                        if (StringUtils.isNotBlank(selectMap.get("avgOrderMoney"))) {
                                            if (!(new BigDecimal(orderMap.get("avgOrderMoney").toString()).compareTo(new BigDecimal(selectMap.get("avgOrderMoney"))) < 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    } else if (selectMap.get("avgOrderMoneyType").equalsIgnoreCase("3")) {//平均消费总额比较类型为介于
                                        if (StringUtils.isNotBlank(selectMap.get("avgOrderMoneyBegin")) && StringUtils.isBlank(selectMap.get("avgOrderMoneyEnd"))) {//如果只录入了前面的数
                                            if (!(new BigDecimal(orderMap.get("avgOrderMoney").toString()).compareTo(new BigDecimal(selectMap.get("avgOrderMoneyBegin"))) >= 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("avgOrderMoneyEnd")) && StringUtils.isBlank(selectMap.get("avgOrderMoneyBegin"))) {//如果只录入了后面的数
                                            if (!(new BigDecimal(orderMap.get("avgOrderMoney").toString()).compareTo(new BigDecimal(selectMap.get("avgOrderMoneyEnd"))) <= 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("avgOrderMoneyEnd")) && StringUtils.isNotBlank(selectMap.get("avgOrderMoneyBegin"))) {//如果前后两个数都录入了
                                            if (!(new BigDecimal(orderMap.get("avgOrderMoney").toString()).compareTo(new BigDecimal(selectMap.get("avgOrderMoneyBegin"))) >= 0
                                                    && new BigDecimal(orderMap.get("avgOrderMoney").toString()).compareTo(new BigDecimal(selectMap.get("avgOrderMoneyEnd"))) <= 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    } else if (selectMap.get("avgOrderMoneyType").equalsIgnoreCase("4")) {//平均消费总额比较类型为不介于
                                        if (StringUtils.isNotBlank(selectMap.get("avgOrderMoneyBegin")) && StringUtils.isBlank(selectMap.get("avgOrderMoneyEnd"))) {//如果只录入了前面的数
                                            if (!(new BigDecimal(orderMap.get("avgOrderMoney").toString()).compareTo(new BigDecimal(selectMap.get("avgOrderMoneyBegin"))) < 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("avgOrderMoneyEnd")) && StringUtils.isBlank(selectMap.get("avgOrderMoneyBegin"))) {//如果只录入了后面的数
                                            if (!(new BigDecimal(orderMap.get("avgOrderMoney").toString()).compareTo(new BigDecimal(selectMap.get("avgOrderMoneyEnd"))) > 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("avgOrderMoneyEnd")) && StringUtils.isNotBlank(selectMap.get("avgOrderMoneyBegin"))) {//如果前后两个数都录入了
                                            if (!(new BigDecimal(orderMap.get("avgOrderMoney").toString()).compareTo(new BigDecimal(selectMap.get("avgOrderMoneyBegin"))) < 0
                                                    || new BigDecimal(orderMap.get("avgOrderMoney").toString()).compareTo(new BigDecimal(selectMap.get("avgOrderMoneyEnd"))) > 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    }
                                }
                                //判断平均消费总额比较类型End
                                //判断最后消费日期距今比较类型Begin
                                if (meetOrder) {//在前一个条件满足的情况下在进行消费金额的判断
                                    daysBetween = daysBetween(orderMap.get("lastOrderTime").toString(), newDateString);
                                    if (selectMap.get("lastOrderDayType").equalsIgnoreCase("1")) {//最后消费日期距今比较类型为大于
                                        if (StringUtils.isNotBlank(selectMap.get("lastOrderDay"))) {
                                            if (!(daysBetween.compareTo(Integer.valueOf(selectMap.get("lastOrderDay"))) > 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    } else if (selectMap.get("lastOrderDayType").equalsIgnoreCase("2")) {//最后消费日期距今比较类型为小于
                                        if (StringUtils.isNotBlank(selectMap.get("lastOrderDay"))) {
                                            if (!(daysBetween.compareTo(Integer.valueOf(selectMap.get("lastOrderDay"))) < 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    } else if (selectMap.get("lastOrderDayType").equalsIgnoreCase("3")) {//最后消费日期距今比较类型为介于
                                        if (StringUtils.isNotBlank(selectMap.get("lastOrderDayBegin")) && StringUtils.isBlank(selectMap.get("lastOrderDayEnd"))) {//如果只录入了前面的数
                                            if (!(daysBetween.compareTo(Integer.valueOf(selectMap.get("lastOrderDayBegin"))) >= 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("lastOrderDayEnd")) && StringUtils.isBlank(selectMap.get("lastOrderDayBegin"))) {//如果只录入了后面的数
                                            if (!(daysBetween.compareTo(Integer.valueOf(selectMap.get("lastOrderDayEnd"))) <= 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("lastOrderDayEnd")) && StringUtils.isNotBlank(selectMap.get("lastOrderDayBegin"))) {//如果前后两个数都录入了
                                            if (!(daysBetween.compareTo(Integer.valueOf(selectMap.get("lastOrderDayBegin"))) >= 0
                                                    && daysBetween.compareTo(Integer.valueOf(selectMap.get("lastOrderDayEnd"))) <= 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    } else if (selectMap.get("lastOrderDayType").equalsIgnoreCase("4")) {//最后消费日期距今比较类型为不介于
                                        if (StringUtils.isNotBlank(selectMap.get("lastOrderDayBegin")) && StringUtils.isBlank(selectMap.get("lastOrderDayEnd"))) {//如果只录入了前面的数
                                            if (!(daysBetween.compareTo(Integer.valueOf(selectMap.get("lastOrderDayBegin"))) < 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("lastOrderDayEnd")) && StringUtils.isBlank(selectMap.get("lastOrderDayBegin"))) {//如果只录入了后面的数
                                            if (!(daysBetween.compareTo(Integer.valueOf(selectMap.get("lastOrderDayEnd"))) > 0)) {
                                                meetOrder = false;
                                            }
                                        } else if (StringUtils.isNotBlank(selectMap.get("lastOrderDayEnd")) && StringUtils.isNotBlank(selectMap.get("lastOrderDayBegin"))) {//如果前后两个数都录入了
                                            if (!(daysBetween.compareTo(Integer.valueOf(selectMap.get("lastOrderDayBegin"))) < 0
                                                    || daysBetween.compareTo(Integer.valueOf(selectMap.get("lastOrderDayEnd"))) > 0)) {
                                                meetOrder = false;
                                            }
                                        }
                                    }
                                }
                                //判断平均消费总额比较类型End
                            }
                            object.put("orderCount", orderMap.get("orderCount").toString());
                            object.put("orderMoney", orderMap.get("orderTotal").toString());
                            object.put("avgOrderMoney", orderMap.get("avgOrderMoney").toString());
                            orderList.remove(orderMap);
                            break;
                        }
                    }
                }
                //如果录如过订单条件但该用户没有满足订单条件则将该用户从列表中移除掉
                if (!meetOrder && StringUtils.isBlank(selectMap.get("text")) && selectMap.size() != 0) {
                    //根据消费次数的比较类型，判断是否有录入查询条件
                    if (selectMap.get("orderCountType").equalsIgnoreCase("1") || selectMap.get("orderCountType").equalsIgnoreCase("2")) {
                        if (StringUtils.isNotBlank(selectMap.get("orderCount"))) {
                            if (!(selectMap.get("orderCountType").equalsIgnoreCase("2") && !object.getBoolean("useOrder") && Integer.valueOf(selectMap.get("orderCount")) != 0)) {
                                iterator.remove();
                                continue;
                            }
                        }
                    } else {
                        if (StringUtils.isNotBlank(selectMap.get("orderCountBegin")) || StringUtils.isNotBlank(selectMap.get("orderCountEnd"))) {
                            if (!(selectMap.get("orderCountType").equalsIgnoreCase("4") && !object.getBoolean("useOrder") && Integer.valueOf(selectMap.get("orderCountBegin")) != 0)) {
                                iterator.remove();
                                continue;
                            }
                        }
                    }
                    //根据消费总额的比较类型，判断是否有录入查询条件
                    if (selectMap.get("orderTotalType").equalsIgnoreCase("1")
                            || selectMap.get("orderTotalType").equalsIgnoreCase("2")) {
                        if (StringUtils.isNotBlank(selectMap.get("orderTotal"))) {
                            if (!(selectMap.get("orderTotalType").equalsIgnoreCase("2") && !object.getBoolean("useOrder") && Integer.valueOf(selectMap.get("orderTotal")) != 0)) {
                                iterator.remove();
                                continue;
                            }
                        }
                    } else {
                        if (StringUtils.isNotBlank(selectMap.get("orderTotalBegin")) || StringUtils.isNotBlank(selectMap.get("orderTotalEnd"))) {
                            if (!(selectMap.get("orderTotalType").equalsIgnoreCase("4") && !object.getBoolean("useOrder") && Integer.valueOf(selectMap.get("orderTotalBegin")) != 0)) {
                                iterator.remove();
                                continue;
                            }
                        }
                    }
                    //根据平均消费金额的比较类型，判断是否有录入查询条件
                    if (selectMap.get("avgOrderMoneyType").equalsIgnoreCase("1")
                            || selectMap.get("avgOrderMoneyType").equalsIgnoreCase("2")) {
                        if (StringUtils.isNotBlank(selectMap.get("avgOrderMoney"))) {
                            if (!(selectMap.get("avgOrderMoneyType").equalsIgnoreCase("2") && !object.getBoolean("useOrder") && Integer.valueOf(selectMap.get("avgOrderMoney")) != 0)) {
                                iterator.remove();
                                continue;
                            }
                        }
                    } else {
                        if (StringUtils.isNotBlank(selectMap.get("avgOrderMoneyBegin")) || StringUtils.isNotBlank(selectMap.get("avgOrderMoneyEnd"))) {
                            if (!(selectMap.get("avgOrderMoneyType").equalsIgnoreCase("4") && !object.getBoolean("useOrder") && Integer.valueOf(selectMap.get("avgOrderMoneyBegin")) != 0)) {
                                iterator.remove();
                                continue;
                            }
                        }
                    }
                    //根据最后消费日期距今的比较类型，判断是否有录入查询条件
                    if (selectMap.get("lastOrderDayType").equalsIgnoreCase("1")
                            || selectMap.get("lastOrderDayType").equalsIgnoreCase("2")) {
                        if (StringUtils.isNotBlank(selectMap.get("lastOrderDay"))) {
                            if (!(selectMap.get("lastOrderDayType").equalsIgnoreCase("2") && !object.getBoolean("useOrder") && Integer.valueOf(selectMap.get("lastOrderDay")) != 0)) {
                                iterator.remove();
                                continue;
                            }
                        }
                    } else {
                        if (StringUtils.isNotBlank(selectMap.get("lastOrderDayBegin")) || StringUtils.isNotBlank(selectMap.get("lastOrderDayEnd"))) {
                            if (!(selectMap.get("lastOrderDayType").equalsIgnoreCase("4") && !object.getBoolean("useOrder") && Integer.valueOf(selectMap.get("lastOrderDayBegin")) != 0)) {
                                iterator.remove();
                                continue;
                            }
                        }
                    }
                }
                //判断是否录如过储值条件，有则进行筛选
                if (object.getBoolean("chargeOrder")) {
                    for (String customerId : chargeOrderList) {
                        if (customerId.equalsIgnoreCase(object.get("customerId").toString())) {
                            object.put("isValue", "是");
                            chargeOrderList.remove(customerId);
                            break;
                        }
                    }
                }
                //判断如果有录入过是否储值的条件，如有则移除没有满足条件的用户
                if ("1".equalsIgnoreCase(selectMap.get("isValue")) && object.get("isValue").toString().equalsIgnoreCase("否")) {
                    iterator.remove();
                    continue;
                } else if ("0".equalsIgnoreCase(selectMap.get("isValue")) && object.get("isValue").toString().equalsIgnoreCase("是")) {
                    iterator.remove();
                    continue;
                }
                array.add(object);
                iterator.remove();
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());

        }
        return null;
    }

    /**
     * 向用户发放流失唤醒优惠券
     *
     * @param customerId
     * @param couponId
     * @return
     */
    @RequestMapping("/grantCoupon")
    @ResponseBody
    public Result grantCoupon(@RequestParam Map<String, String> selectMap, String txt, String coupinId) {
        try {
            if (!StringUtils.isEmpty(txt)) {
                selectMap.put("text", txt);
            }
            if (!StringUtils.isEmpty(coupinId)) {
                selectMap.put("coupinId", coupinId);
            }
            List<JSONObject> jsonObjects = getCustomer(selectMap);
            String couponId = selectMap.get("couponId");
            List<Customer> customerList = new ArrayList<>();
            for (JSONObject jsonObject : jsonObjects) {
                customerList.add((Customer) jsonObject.get("customer"));
            }
            //得到要发放的优惠券信息
            NewCustomCoupon newCustomCoupon = newcustomcouponService.selectById(Long.valueOf(couponId));

            couponService.addCouponBatch(customerList, newCustomCoupon, getCurrentBrandId());
            return new Result(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new Result(false);
        }
    }
}	
