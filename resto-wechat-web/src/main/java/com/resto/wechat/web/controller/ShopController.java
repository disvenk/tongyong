package com.resto.wechat.web.controller;

import com.resto.api.appraise.service.NewAppraiseService;
import com.resto.api.article.service.NewArticleService;
import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.constant.OrderItemType;
import com.resto.shop.web.constant.PosVersion;
import com.resto.shop.web.model.*;
import com.resto.api.article.entity.Article;
import com.resto.api.customer.entity.Customer;
import com.resto.shop.web.service.*;
import com.resto.shop.web.service.OrderRemarkService;
import com.resto.shop.web.util.LogTemplateUtils;
import com.resto.wechat.web.config.Config;
import com.resto.wechat.web.config.SessionKey;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.util.*;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

@RestController
@RequestMapping("shop")
public class ShopController extends GenericController {

    @Resource
    ShopCartService shopCartService;

    @Resource
    AdvertService advertService;

    @Resource
    NoticeService noticeService;

    @Resource
    com.resto.brand.web.service.ShowPhotoService showPhotoService;

    @Resource
    ExperienceService experienceService;

    @Resource
    PictureSliderService pictureSliderService;

    @Resource
    NewCustomerService newCustomerService;

    @Resource
    NewAppraiseService newAppraiseService;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    BrandService brandService;

    @Resource
    NewArticleService newArticleService;

    @Resource
    ModuleListService moduleListService;

    @Resource
    NewCustomCouponService newCustomCouponService;

    @Resource
    CouponService couponService;

    @Resource
    BrandSettingService brandSettingService;

    @Resource
    OrderRemarkService orderRemarkService;

    @Autowired
    CustomerGroupService customerGroupService;

    @Autowired
    TableGroupService tableGroupService;

    @Resource
    ArticlePriceService articlePriceService;

    @Autowired
    RedisService redisService;

    @RequestMapping("currentshop")
    public Result currentshop() {
        ShopDetail detail = getCurrentShop();
        return getSuccessResult(detail);
    }

    @RequestMapping("/new/currentshop")
    public void currentshopNew(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        Brand brand = getCurrentBrand();
        BrandSetting setting = brandSettingService.selectByBrandId(brand.getId());
        ShopDetail detail = shopDetailService.selectById(getCurrentShopId());
        net.sf.json.JSONObject shopJson = new net.sf.json.JSONObject().fromObject(detail);
        if (detail.getServiceType().equals(Common.YES)){//开通了新版服务费
            List<net.sf.json.JSONObject> newServiceList = new ArrayList<>();
            if (detail.getIsOpenSauceFee().equals(Common.YES)){ //开启了餐具费
                net.sf.json.JSONObject sauceFee = new net.sf.json.JSONObject();
                sauceFee.put("serviceName", detail.getSauceFeeName());
                sauceFee.put("unitPrice", detail.getSauceFeePrice());
                newServiceList.add(sauceFee);
            }
            if (detail.getIsOpenTowelFee().equals(Common.YES)){ //开启了纸巾费
                net.sf.json.JSONObject towelFee = new net.sf.json.JSONObject();
                towelFee.put("serviceName", detail.getTowelFeeName());
                towelFee.put("unitPrice", detail.getTowelFeePrice());
                newServiceList.add(towelFee);
            }
            if (detail.getIsOpenTablewareFee().equals(Common.YES)){ //开启了酱料费
                net.sf.json.JSONObject tablewareFee = new net.sf.json.JSONObject();
                tablewareFee.put("serviceName", detail.getTablewareFeeName());
                tablewareFee.put("unitPrice", detail.getTablewareFeePrice());
                newServiceList.add(tablewareFee);
            }
            shopJson.put("newServiceList", newServiceList);
        }
        Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),getCurrentCustomer().getId());
        if (customer.getIsBindPhone()) {
            Map<String, Object> selectMap = new HashMap<>();
            selectMap.put("shopId", getCurrentShopId());
            selectMap.put("nowDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            if (org.apache.commons.lang3.StringUtils.isNotBlank(customer.getRealTimeCouponIds())) {
                selectMap.put("realTimeCouponIds", customer.getRealTimeCouponIds());
            }
            List<NewCustomCoupon> newCustomCoupons = newCustomCouponService.selectRealTimeCoupon(selectMap);
            List<Coupon> coupons = couponService.addRealTimeCoupon(newCustomCoupons, customer);
            if (coupons.size() > 0) {
                shopJson.put("currentTimeCoupon", true);
                shopJson.put("currentCouponList", coupons);
            } else {
                shopJson.put("currentTimeCoupon", false);
            }
        } else {
            shopJson.put("currentTimeCoupon", false);
        }
        if (setting.getOpenOrderRemark().equals(Common.YES) && detail.getOpenOrderRemark().equals(Common.YES)){
            List<com.alibaba.fastjson.JSONObject> orderRemarks = orderRemarkService.getShopOrderRemark(detail.getId());
            shopJson.put("orderRemarkShow", true);
            shopJson.put("orderRemarkList", orderRemarks);
        }else{
            shopJson.put("orderRemarkShow",false);
        }
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("success", true);
        json.put("data", shopJson);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("list")
    public Result list() {
        return getSuccessResult(getShopList());
    }

    @RequestMapping("/new/list")
    public void listNew(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        String requestURL = request.getRequestURL().toString();
        String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
        Brand brand = brandService.selectBySign(brandSign);
        List<ShopDetail> shopList = shopDetailService.selectByBrandId(brand.getId());
        for (ShopDetail sd : shopList) {
            List<Map<String, Object>> result = newAppraiseService.appraiseMonthCount(sd.getBrandId(),sd.getId());
            if (!CollectionUtils.isEmpty(result)) {
                double res = Double.parseDouble(result.get(0).get("AVG_SCORE").toString());
                DecimalFormat df = new DecimalFormat("#.00");
                sd.setCountAppraise(Double.parseDouble(df.format(res)));
                sd.setNumAppraise(Math.ceil(res / 100 * 5));
            }
        }
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("success", true);
        json.put("data", shopList);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("switch/{id}")
    public Result switchShop(@PathVariable String id, HttpSession session) {
        if (getShopMap().containsKey(id)) {
            String baseShop = getCurrentShop().getName();
            session.setAttribute(SessionKey.CURRENT_SHOP, getShopMap().get(id));
            String nowShop = getCurrentShop().getName();
            return getSuccessResult();
        } else {
            Result result = new Result();
            result.setSuccess(false);
            result.setMessage("没有找到该店铺");
            return result;
        }
    }

    @RequestMapping("/new/switch/{id}")
    public void switchShopNew(@PathVariable String id, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        if (getShopMap().containsKey(id)) {
            String baseShop;
            if(getCurrentShop() == null){
                baseShop = "没有进入过店铺";
            }else{
                baseShop = getCurrentShop().getName();
            }

            session.setAttribute(SessionKey.CURRENT_SHOP, getShopMap().get(id));
            String nowShop = getCurrentShop().getName();
            Customer customer = (Customer) session.getAttribute(SessionKey.CURRENT_CUSTOMER);
            customer.setLastOrderShop(getShopMap().get(id).getId());
            newCustomerService.dbUpdate(getCurrentBrandId(),customer);
            session.setAttribute(SessionKey.CURRENT_CUSTOMER, customer);

            JSONObject json = new JSONObject();
            json.put("statusCode", "0");
            json.put("message", "");
            json.put("success", true);
            json.put("data", new JSONObject(getShopMap().get(id)));
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        } else {

            JSONObject json = new JSONObject();
            json.put("statusCode", "0");
            json.put("message", "没有找到该店铺");
            json.put("success", false);
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
    }

    @RequestMapping("listShopcart")
    public Result listCartDetailById(ShopCart shopcart, HttpSession session) {
        //通过session内的当前店铺，以及当前客户 查询该客户 指定模式下的所有购物车项
        String shopId = getCurrentShopId();
        String customerId = getCurrentCustomerId();
        shopcart.setShopDetailId(shopId);
        shopcart.setCustomerId(customerId);
        List<ShopCart> list = shopCartService.listUserAndShop(shopcart);
        return getSuccessResult(list);
    }

    @RequestMapping("/new/listShopcart")
    public void listCartDetailByIdNew(ShopCart shopcart, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        //通过session内的当前店铺，以及当前客户 查询该客户 指定模式下的所有购物车项
        String shopId = getCurrentShopId();
        String customerId = getCurrentCustomerId();
        shopcart.setShopDetailId(shopId);
        shopcart.setCustomerId(customerId);
        List<ShopCart> shopCarts = new ArrayList<>();
        List<ShopCart> shopCartsTypeThird = new ArrayList<>();
        Result result = checkGroup(shopcart.getGroupId(),customerId);
        if(!result.isSuccess()){
            JSONObject json = new JSONObject();
            json.put("success",false);
            json.put("message",result.getMessage());
            ApiUtils.setBackInfo(response, json); // 返回信息设置
            return;
        }
        List<ShopCart> list = shopCartService.listUserAndShop(shopcart);
        shopCartService.updateShopCartByGroupId(shopcart.getGroupId(), getCurrentShopId(), getCurrentCustomerId());
        if (!CollectionUtils.isEmpty(list)) {
            for (ShopCart scart : list) {
                if (Config.ARTICLE.equals(scart.getShopType())) {
                    if(redisService.get(scart.getArticleId() + Common.KUCUN) != null){
                        if((Integer)redisService.get(scart.getArticleId() + Common.KUCUN) != 0 ){
                            shopCarts.add(scart);
                        }
                    }else{
                        Integer number = 0;
                        if(scart.getArticleId().indexOf("@") == -1){
                            Article a = newArticleService.dbSelectByPrimaryKey(getCurrentBrandId(),scart.getArticleId());
                            if(a!=null&&!a.getIsEmpty()){
                                number = a.getCurrentWorkingStock();
                            }
                        }else{
                            ArticlePrice a = articlePriceService.selectById(scart.getArticleId());
                            number = a.getCurrentWorkingStock();
                        }
                        if(number > 0){
                            shopCarts.add(scart);
                        }
                    }
                } else if (Config.TOTAL_ARTICLE.equals(scart.getShopType())) {
                    List<ShopCart> scchild = new ArrayList<>();
                    for (ShopCart sc : list) {
                        if (sc.getPid().equals(scart.getId())) {
                            scchild.add(sc);
                        }
                    }
                    scart.setCurrentItem(scchild);
                    shopCarts.add(scart);
                } else if (Config.CHILD_ARTICLE.equals(scart.getShopType())) {
                    shopCartsTypeThird.add(scart);
                } else if (Config.SINGLE_ARTICLE.equals(scart.getShopType())) {
                    List<ShopCart> scchild = new ArrayList<>();
                    for (ShopCart sc : list) {
                        if (sc.getPid().equals(scart.getId())) {
                            scchild.add(sc);
                        }
                    }
                    scart.setCurrentItem(scchild);
                    shopCarts.add(scart);
                } else if (Config.RECOMMEND_ARTICLE.equals(scart.getShopType())) {
                    shopCartsTypeThird.add(scart);
                } else if (Config.NEW_UNIT_ARTICLE.equals(scart.getShopType())) {
                    List<ShopCart> unit = new ArrayList<>();
                    List<ShopCart> currentItem = new ArrayList<>();
                    for (ShopCart sc : list) {
                        if (sc.getPid().equals(scart.getId()) && sc.getUnitNewId() != null) {
                            unit.add(sc);
                        }
                        if (sc.getPid().equals(scart.getId()) && sc.getUnitNewId() == null) {
                            currentItem.add(sc);
                        }
                    }
                    scart.setUnitList(unit);
                    scart.setCurrentItem(currentItem);
                    shopCarts.add(scart);
                } else if (Config.NEW_UNIT_DETAIL.equals(scart.getShopType())) {
                    shopCartsTypeThird.add(scart);
                } else if (Config.WEIGHT_PACKAGE_ARTICLE.equals(scart.getShopType())) {
                    List<ShopCart> weight = new ArrayList<>();
                    List<ShopCart> currentItem = new ArrayList<>();
                    for (ShopCart sc : list) {
                        if (sc.getPid().equals(scart.getId()) && Config.WEIGHT_PACKAGE_ARTICLE_DETAIL.equals(sc.getShopType())) {
                            weight.add(sc);
                        }
                        if (sc.getPid().equals(scart.getId()) && Config.RECOMMEND_ARTICLE.equals(sc.getShopType())) {
                            currentItem.add(sc);
                        }
                    }
                    scart.setWeightList(weight);
                    scart.setCurrentItem(currentItem);
                    shopCarts.add(scart);
                } else if (Config.WEIGHT_PACKAGE_ARTICLE_DETAIL.equals(scart.getShopType())) {
                    shopCartsTypeThird.add(scart);
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("message", "");
        json.put("success", true);
        if(!StringUtils.isEmpty(shopcart.getGroupId())){
            //如果开启多人点餐并且组号存在
            List<CustomerGroup> customerGroups  = customerGroupService.getGroupByShopCart(shopcart.getGroupId());
            int sum = 0;
            for(CustomerGroup customerGroup : customerGroups){
                sum += customerGroup.getArticleCount();
            }
            json.put("totalArticleCount",sum);
            json.put("customerGroup",new JSONArray(customerGroups));
            AppUtils.unpack(request, response);

//            if(!MemcachedUtils.add(customerId+shopcart.getGroupId(),true)){
//                MemcachedUtils.delete(customerId+shopcart.getGroupId());
//                MemcachedUtils.add(customerId+shopcart.getGroupId(),true);
//            }
            if(!redisService.setnx(customerId+shopcart.getGroupId(),true)){
                redisService.remove(customerId+shopcart.getGroupId());
                redisService.setnx(customerId+shopcart.getGroupId(),true);
            }
            redisService.set(customerId+shopcart.getGroupId(),true);
            log.info("让"+customerId+"的check 变成true");
        }
        json.put("data", shopCarts);
//        json.put("typeThird", shopCartsTypeThird);

        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    private Result checkGroup(String groupId,String customerId){
        Result result = new Result();
        if(!StringUtils.isEmpty(groupId)){
            //如果是多人点餐的购物车
            //需要判断下这个人是否还在这个组内 以及 这个组是否已经被释放了
            TableGroup tableGroup = tableGroupService.selectByGroupId(groupId);
            List<CustomerGroup> customerGroupList = customerGroupService.getGroupByGroupId(groupId);
            if(tableGroup.getState() == TableGroup.FINISH || CollectionUtils.isEmpty(customerGroupList)){
                return new Result("该组已释放，请重新选择要加入的组",false);
            }
            boolean check = false;
            for(CustomerGroup customerGroup : customerGroupList){
                if(customerGroup.getCustomerId().equals(customerId)){
                    check = true;
                }
            }
            if(!check){
                log.info("trffef");
                return new Result("您已不在组内，请重新选择要加入的组",false);
            }
        }
        return new Result(true);
    }


    /**
     * 根据当前店铺id 查询出 店铺的广告信息
     *
     * @return
     */
    @RequestMapping("shopAdvert")
    public Result getadvertDetails() {
        String shopDetailId = getCurrentShopId();
        List<Advert> list = advertService.selectListByShopId(shopDetailId);
        return getSuccessResult(list);
    }

    @RequestMapping("/new/shopAdvert")
    public void getadvertDetailsNew(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        String shopDetailId = getCurrentShopId();
        List<Advert> list = advertService.selectListByShopId(shopDetailId);

        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    /**
     * 根据当前店铺id 查询出店铺的通知列表
     *
     * @return
     */
    @RequestMapping("noticeList")
    public Result getlistNotice(Integer noticeType) {
        String shopDetailId = getCurrentShopId();
        List<Notice> list = noticeService.selectListByShopId(shopDetailId, getCurrentCustomerId(), noticeType);
        return getSuccessResult(list);
    }

    @RequestMapping("/new/noticeList")
    public void getlistNoticeNew(Integer noticeType, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        String shopDetailId = getCurrentShopId();
        List<Notice> list = noticeService.selectListByShopId(shopDetailId, getCurrentCustomerId(), noticeType);

        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("message", "");
        json.put("success", true);
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("addNoticeHistory")
    public Result addNoticeHistory(String noticeId) {
        String customerId = getCurrentCustomerId();
        noticeService.addNoticeHistory(customerId, noticeId);
        return getSuccessResult();
    }

    @RequestMapping("/new/addNoticeHistory")
    public void addNoticeHistoryNew(String noticeId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        String customerId = getCurrentCustomerId();
        noticeService.addNoticeHistory(customerId, noticeId);

        JSONObject json = new JSONObject();
        json.put("success", true);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    /**
     * 接受参数 articleId,distributionModeId,number 餐品id,模式id,数量
     * 先查询当前客户是否有该商品的 购物车的条目，如果有 则更新，如果没有则新建，如果number等于0 则删除
     *
     * @param shopCart
     * @return
     */
    @RequestMapping("updateToShopcart")
    public Result updateToShopcart(ShopCart shopCart) {
        shopCart.setShopDetailId(getCurrentShopId());
        shopCart.setCustomerId(getCurrentCustomerId());
        log.info(shopCart.getShopType());
        shopCartService.updateShopCart(shopCart);
//        Map map = new HashMap(4);
//        map.put("brandName", getCurrentBrand().getBrandName());
//        map.put("fileName", getCurrentCustomerId());
//        map.put("type", "UserAction");
//        map.put("content", "用户:"+getCurrentCustomer().getNickname()+"将菜品Id为:"+shopCart.getArticleId()+"的菜加入了购物车,请求服务器地址为:" + MQSetting.getLocalIP());
//        doPostAnsc(url, map);

        return Result.getSuccess();
    }

    /**
     * 添加单品和单品老规格
     * @param shopCart
     * @param request
     * @param response
     */
    @RequestMapping("/new/updateToShopcart")
    public void updateToShopcartNew(ShopCart shopCart, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        String aid = shopCart.getArticleId();
        Result result = checkGroup(shopCart.getGroupId(),getCurrentCustomerId());
        if(!result.isSuccess()){
            JSONObject json = new JSONObject();
            json.put("success",false);
            json.put("message",result.getMessage());
            ApiUtils.setBackInfo(response, json); // 返回信息设置
            return;
        }
        if (aid.indexOf("@") >= 0) {
            aid = aid.substring(0, aid.indexOf("@"));
        }
        Article article = newArticleService.dbSelectByPrimaryKey(getCurrentBrandId(),aid);
        JSONObject json = new JSONObject();
        if (article != null) {
            shopCart.setShopDetailId(getCurrentShopId());
            shopCart.setCustomerId(getCurrentCustomerId());
            if(shopCart.getShopType() == null){
                shopCart.setShopType(Config.ARTICLE);
            }

            shopCart.setPid(0);
            Integer number = shopCartService.updateShopCart(shopCart);
            json.put("statusCode", "200");
            json.put("message", "请求成功");
            json.put("success", true);
        } else {
            json.put("statusCode", "100");
            json.put("message", "菜品不存在");
            json.put("success", false);
        }
        if(!StringUtils.isEmpty(shopCart.getGroupId())){
            //如果在开启了多人点餐的情况下 修改菜品
            List<CustomerGroup> customerGroupList = customerGroupService.getGroupByGroupId(shopCart.getGroupId());
            log.info("这里得到该组全部的人");
            for(CustomerGroup customerGroup : customerGroupList){
                if(!customerGroup.getCustomerId().equals(getCurrentCustomerId())){
                    log.info("这里得到该组不是自己的人");
//                    if(!MemcachedUtils.add(customerGroup.getCustomerId()+shopCart.getGroupId(),false)){
//                        MemcachedUtils.delete(customerGroup.getCustomerId()+shopCart.getGroupId());
//                        MemcachedUtils.add(customerGroup.getCustomerId()+shopCart.getGroupId(),false);
//                    }
                    if(!redisService.setnx(customerGroup.getCustomerId()+shopCart.getGroupId(),false)){
                        redisService.remove(customerGroup.getCustomerId()+shopCart.getGroupId());
                        redisService.setnx(customerGroup.getCustomerId()+shopCart.getGroupId(),false);
                    }
                    log.info("让"+customerGroup.getCustomerId()+"的check 变成false");
                    redisService.set(customerGroup.getCustomerId()+shopCart.getGroupId(),false);
                }
            }
        }

//        Map map = new HashMap(4);
//        map.put("brandName", getCurrentBrand().getBrandName());
//        map.put("fileName", getCurrentCustomerId());
//        map.put("type", "UserAction");
//        map.put("content", "用户:"+getCurrentCustomer().getNickname()+"将菜品Id为:"+shopCart.getArticleId()+"的菜加入了购物车,请求服务器地址为:" + MQSetting.getLocalIP());
//        doPostAnsc(url, map);
       // String brandName, Customer customer, String shopName, Article article, Integer type
        LogTemplateUtils.getUpdateShopcart(getCurrentBrand().getBrandName(),getCurrentCustomer(),getCurrentShop().getName(),article,shopCart);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }


    /**
     * 添加套餐到购物车
     * @param shopcart
     * @param request
     * @param response
     */
    @RequestMapping("/new/updateToShopcartOne")
    public void updateToShopcartOneNew(String shopcart, String groupId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        Result result = checkGroup(groupId,getCurrentCustomerId());
        if(!result.isSuccess()){
            JSONObject json = new JSONObject();
            json.put("success",false);
            json.put("message",result.getMessage());
            ApiUtils.setBackInfo(response, json); // 返回信息设置
            return;
        }
        JSONObject cartJson = new JSONObject(shopcart);
        String articleType = cartJson.optString("articleType");
        String mealAttrs = cartJson.optString("mealAttrs");
        String shopCartId = cartJson.optString("shopCartId");
        JSONArray mealAttrsJson = new JSONArray(mealAttrs);
        Integer farId;
        if (Config.TOTAL_ARTICLE.equals(articleType)) {
            Map map = new HashMap(4);
            map.put("brandName", getCurrentBrand().getBrandName());
            map.put("fileName", getCurrentCustomerId());
            map.put("type", "UserAction");
            String articleId = "";
            String content = "用户:"+getCurrentCustomer().getNickname()+"";
            if (StringUtils.isEmpty(shopCartId)) {
                ShopCart shopCartInfo = new ShopCart();
                shopCartInfo.setNumber(Integer.parseInt(cartJson.optString("remainNumber")));
                shopCartInfo.setArticleId(cartJson.optString("id"));
                shopCartInfo.setDistributionModeId(1);
                shopCartInfo.setShopDetailId(getCurrentShopId());
                shopCartInfo.setCustomerId(getCurrentCustomerId());
                shopCartInfo.setShopType(Config.TOTAL_ARTICLE);
                shopCartInfo.setGroupId(groupId);
                shopCartInfo.setPid(0);
                farId = shopCartService.updateShopCart(shopCartInfo);
                content += "将菜品(套餐)Id为:"+shopCartInfo.getArticleId()+"的菜加入了购物车,请求店铺为:"+getCurrentShop().getName()+"请求服务器地址为:"+MQSetting.getLocalIP();
            } else {
                farId = Integer.valueOf(shopCartId);
            }
            if (!StringUtils.isEmpty(shopCartId)) { //修改套餐
                shopCartService.delMealItem(shopCartId);
            }
            for (int a = 0; a < mealAttrsJson.length(); a++) {
                JSONObject meal = mealAttrsJson.getJSONObject(a);
                JSONObject mealAttr = new JSONObject(meal.toString());
                Integer attrId = Integer.parseInt(mealAttr.optString("id"));
                if (!StringUtils.isEmpty(mealAttr.optString("currentItem"))) {
                    JSONArray currentItemList = new JSONArray(mealAttr.optString("currentItem"));
                    for (int c = 0; c < currentItemList.length(); c++) {
                        JSONObject currentItem = currentItemList.getJSONObject(c);
                        ShopCart shopCartChild = new ShopCart();
                        shopCartChild.setNumber(Integer.parseInt(cartJson.optString("remainNumber")));
                        shopCartChild.setArticleId(currentItem.optString("articleId"));
                        shopCartChild.setDistributionModeId(1);
                        shopCartChild.setShopDetailId(getCurrentShopId());
                        shopCartChild.setCustomerId(getCurrentCustomerId());
                        shopCartChild.setShopType(Config.CHILD_ARTICLE);
                        shopCartChild.setGroupId(groupId);
                        shopCartChild.setPid(farId);
                        shopCartChild.setAttrId(attrId);
                        shopCartService.updateShopCart(shopCartChild);
                        articleId = articleId.concat(shopCartChild.getArticleId() +"、");
                    }
                }
            }

            content += "将套餐子品Id为:"+articleId+"的菜品加入购物车,请求服务器地址为:" + MQSetting.getLocalIP()+"";
            map.put("content",content);
            doPostAnsc(map);
            JSONObject json = new JSONObject();
            json.put("statusCode", "0");
            json.put("id", farId);
            json.put("message", "");
            json.put("success", true);

            if(StringUtils.isNotEmpty(groupId)){
                //如果在开启了多人点餐的情况下 修改菜品
                List<CustomerGroup> customerGroupList = customerGroupService.getGroupByGroupId(groupId);
                for(CustomerGroup customerGroup : customerGroupList){
                    if(!customerGroup.getCustomerId().equals(getCurrentCustomerId())){
//                        if(!MemcachedUtils.add(customerGroup.getCustomerId()+groupId,false)){
//                            MemcachedUtils.delete(customerGroup.getCustomerId()+ groupId);
//                            MemcachedUtils.add(customerGroup.getCustomerId()+groupId,false);
//                        }
                        if(!redisService.setnx(customerGroup.getCustomerId()+groupId,false)){
                            redisService.remove(customerGroup.getCustomerId()+ groupId);
                            redisService.setnx(customerGroup.getCustomerId()+groupId,false);
                        }
                        redisService.set(customerGroup.getCustomerId()+groupId,false);
                    }
                }
            }
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
    }

    /**
     * 添加单品推荐包到购物车
     * @param shopcart
     * @param request
     * @param response
     */
    @RequestMapping("/new/updateToShopcartTwo")
    public void updateToShopcartTwoNew(String shopcart, String groupId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        Result result = checkGroup(groupId,getCurrentCustomerId());
        if(!result.isSuccess()){
            JSONObject json = new JSONObject();
            json.put("success",false);
            json.put("message",result.getMessage());
            ApiUtils.setBackInfo(response, json); // 返回信息设置
            return;
        }
        JSONObject cartJson = new JSONObject(shopcart);
        String articleType = cartJson.optString("articleType");
        String recommendList = cartJson.optString("recommendList");
        String shopCartId = cartJson.optString("shopCartId");
        String uuid = cartJson.optString("uuid");
        JSONArray recommendListJson = new JSONArray(recommendList);
        Integer farId = null;
        if (articleType.equals("1")) {
            if (StringUtils.isEmpty(shopCartId)) {
                ShopCart shopCartInfo = new ShopCart();
                shopCartInfo.setNumber(Integer.parseInt(cartJson.optString("number")));
                shopCartInfo.setArticleId(cartJson.optString("id"));
                shopCartInfo.setDistributionModeId(1);
                shopCartInfo.setShopDetailId(getCurrentShopId());
                shopCartInfo.setCustomerId(getCurrentCustomerId());
                shopCartInfo.setShopType(Config.SINGLE_ARTICLE);
                shopCartInfo.setGroupId(groupId);
                shopCartInfo.setUuid(uuid);
                shopCartInfo.setPid(0);
                farId = shopCartService.updateShopCart(shopCartInfo);
            } else {
                farId = Integer.valueOf(shopCartId);
            }
            if (!StringUtils.isEmpty(shopCartId)) { //修改套餐
                shopCartService.delMealItem(shopCartId);
            }
            for (int a = 0; a < recommendListJson.length(); a++) {
                ShopCart shopCartChild = new ShopCart();
                JSONObject recommend = recommendListJson.getJSONObject(a);
                JSONObject recommendAttr = new JSONObject(recommend.toString());
                if(Integer.parseInt(recommendAttr.optString("count")) > 0) {
                    shopCartChild.setArticleId(recommendAttr.optString("articleId"));
                    shopCartChild.setNumber(Integer.parseInt(recommendAttr.optString("count")));
                    shopCartChild.setDistributionModeId(1);
                    shopCartChild.setShopDetailId(getCurrentShopId());
                    shopCartChild.setCustomerId(getCurrentCustomerId());
                    shopCartChild.setShopType(Config.RECOMMEND_ARTICLE);
                    shopCartChild.setGroupId(groupId);
                    shopCartChild.setPid(farId);
                    shopCartService.updateShopCart(shopCartChild);
                    //记录将单品推荐包中的菜加入购物车
                    LogTemplateUtils.getUpdateShopcartTwo(getCurrentBrand().getBrandName(), getCurrentCustomer(), getCurrentShop().getName(), OrderItemType.RECOMMEND, recommendAttr.opt("articleId").toString(), recommend.opt("articleName").toString());
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("id", farId);
        json.put("message", "");
        json.put("success", true);
        if(StringUtils.isNotEmpty(groupId)){
            //如果在开启了多人点餐的情况下 修改菜品
            List<CustomerGroup> customerGroupList = customerGroupService.getGroupByGroupId(groupId);
            for(CustomerGroup customerGroup : customerGroupList){
                if(!customerGroup.getCustomerId().equals(getCurrentCustomerId())){
//                    if(!MemcachedUtils.add(customerGroup.getCustomerId()+groupId,false)){
//                        MemcachedUtils.delete(customerGroup.getCustomerId()+ groupId);
//                        MemcachedUtils.add(customerGroup.getCustomerId()+groupId,false);
//                    }
                    if(!redisService.setnx(customerGroup.getCustomerId()+groupId,false)){
                        redisService.remove(customerGroup.getCustomerId()+ groupId);
                        redisService.setnx(customerGroup.getCustomerId()+groupId,false);
                    }
                    redisService.set(customerGroup.getCustomerId()+groupId,false);
                }
            }
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/updateToShopcartThree")
    public void updateToShopcartThree(String shopcart, String groupId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        JSONObject cartJson = new JSONObject(shopcart);

        String uuid = cartJson.optString("id");
        ShopCart shopCartUuid = shopCartService.selectByUuId(uuid);
        if (shopCartUuid != null) {
            shopCartService.delMealArticle(shopCartUuid.getId().toString());

        }
        if(Integer.parseInt(cartJson.optString("count")) > 0){
            ShopCart shopCartInfo = new ShopCart();
            shopCartInfo.setNumber(Integer.parseInt(cartJson.optString("count")));
            shopCartInfo.setArticleId(cartJson.optString("articleId"));
            shopCartInfo.setDistributionModeId(1);
            shopCartInfo.setShopDetailId(getCurrentShopId());
            shopCartInfo.setCustomerId(getCurrentCustomerId());
            shopCartInfo.setShopType(Config.NEW_UNIT_ARTICLE);
            shopCartInfo.setUuid(uuid);
            shopCartInfo.setGroupId(groupId);
            shopCartInfo.setPid(0);
            Result result = checkGroup(groupId,shopCartInfo.getCustomerId());
            if(!result.isSuccess()){
                JSONObject json = new JSONObject();
                json.put("success",false);
                json.put("message",result.getMessage());
                ApiUtils.setBackInfo(response, json); // 返回信息设置
                return;
            }
            Integer farId = shopCartService.updateShopCart(shopCartInfo);
            //处理规格
            String unitList = cartJson.optString("unitList");
            JSONArray unitJson = new JSONArray(unitList);
            for (int b = 0; b < unitJson.length(); b++) {
                JSONObject unitInfo = unitJson.getJSONObject(b);
                String currentItemList = unitInfo.optString("currentItem");
                JSONArray currentItemJson = new JSONArray(currentItemList);
                for (int c = 0; c < currentItemJson.length(); c++) {
                    JSONObject currentItemInfo = currentItemJson.getJSONObject(c);
                    ShopCart shopCartUnit = new ShopCart();
                    shopCartUnit.setArticleId("");
                    shopCartUnit.setNumber(1);
                    shopCartUnit.setUnitNewId(currentItemInfo.optString("id"));
                    shopCartUnit.setDistributionModeId(1);
                    shopCartUnit.setShopDetailId(getCurrentShopId());
                    shopCartUnit.setCustomerId(getCurrentCustomerId());
                    shopCartUnit.setShopType(Config.NEW_UNIT_DETAIL);
                    shopCartUnit.setGroupId(groupId);
                    shopCartUnit.setPid(farId);
                    shopCartService.updateShopCart(shopCartUnit);
                }
            }
            //处理推荐餐包
            String recommendList = cartJson.optString("recommendList");
            JSONArray recommendJson = new JSONArray(recommendList);
            for (int d = 0; d < recommendJson.length(); d++) {
                JSONObject recommendInfo = recommendJson.getJSONObject(d);
                ShopCart shopCartChild = new ShopCart();
                shopCartChild.setArticleId(recommendInfo.optString("articleId"));
                shopCartChild.setNumber(Integer.parseInt(recommendInfo.optString("count")));
                shopCartChild.setDistributionModeId(1);
                shopCartChild.setShopDetailId(getCurrentShopId());
                shopCartChild.setCustomerId(getCurrentCustomerId());
                shopCartChild.setShopType(Config.RECOMMEND_ARTICLE);
                shopCartChild.setGroupId(groupId);
                shopCartChild.setPid(farId);
                shopCartService.updateShopCart(shopCartChild);
            }
            JSONObject json = new JSONObject();
            json.put("statusCode", "0");
            json.put("message", "");
            json.put("success", true);
            if(StringUtils.isNotEmpty(groupId)){
                //如果在开启了多人点餐的情况下 修改菜品
                List<CustomerGroup> customerGroupList = customerGroupService.getGroupByGroupId(groupId);
                for(CustomerGroup customerGroup : customerGroupList){
                    if(!customerGroup.getCustomerId().equals(getCurrentCustomerId())){
//                        if(!MemcachedUtils.add(customerGroup.getCustomerId()+groupId,false)){
//                            MemcachedUtils.delete(customerGroup.getCustomerId()+ groupId);
//                            MemcachedUtils.add(customerGroup.getCustomerId()+groupId,false);
//                        }
                        if(!redisService.setnx(customerGroup.getCustomerId()+groupId,false)){
                            redisService.remove(customerGroup.getCustomerId()+ groupId);
                            redisService.setnx(customerGroup.getCustomerId()+groupId,false);
                        }
                        redisService.set(customerGroup.getCustomerId()+groupId,false);
                    }
                }
            }
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }else{
            JSONObject json = new JSONObject();
            json.put("statusCode", "0");
            json.put("message", "");
            json.put("success", true);
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
    }

    /**
     * 添加单品推荐包到购物车
     * @param shopcart
     * @param request
     * @param response
     */
    @RequestMapping("/new/updateToShopcartFour")
    public void updateToShopcartFourNew(String shopcart, String groupId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        JSONObject cartJson = new JSONObject(shopcart);

        String uuid = cartJson.optString("id");
        ShopCart shopCartUuid = shopCartService.selectByUuId(uuid);
        if (shopCartUuid != null) {
            shopCartService.delMealArticle(shopCartUuid.getId().toString());
        }
        if(Integer.parseInt(cartJson.optString("count")) > 0){
            ShopCart shopCartInfo = new ShopCart();
            shopCartInfo.setNumber(Integer.parseInt(cartJson.optString("count")));
            shopCartInfo.setArticleId(cartJson.optString("articleId"));
            shopCartInfo.setDistributionModeId(1);
            shopCartInfo.setShopDetailId(getCurrentShopId());
            shopCartInfo.setCustomerId(getCurrentCustomerId());
            shopCartInfo.setShopType(Config.WEIGHT_PACKAGE_ARTICLE);
            shopCartInfo.setUuid(uuid);
            shopCartInfo.setWeightPackageId(Long.valueOf(cartJson.optString("weightPackageId")));
            shopCartInfo.setGroupId(groupId);
            shopCartInfo.setPid(0);
            Result result = checkGroup(groupId,shopCartInfo.getCustomerId());
            if(!result.isSuccess()){
                JSONObject json = new JSONObject();
                json.put("success",false);
                json.put("message",result.getMessage());
                ApiUtils.setBackInfo(response, json); // 返回信息设置
                return;
            }

            Integer farId = shopCartService.updateShopCart(shopCartInfo);

            //处理规格
            String weightPackageList = cartJson.optString("weightPackageList");
            if(StringUtils.isNotEmpty(weightPackageList)){
                JSONObject weightPackageJson = new JSONObject(weightPackageList);
                JSONArray currentItemList = new JSONArray(weightPackageJson.optString("currentItem"));
                for (int b = 0; b < currentItemList.length(); b++) {
                    JSONObject currentItemInfo = currentItemList.getJSONObject(b);
                    ShopCart shopCartUnit = new ShopCart();
                    shopCartUnit.setArticleId("");
                    shopCartUnit.setNumber(1);
                    shopCartUnit.setWeightPackageId(Long.valueOf(currentItemInfo.optString("id")));
                    shopCartUnit.setDistributionModeId(1);
                    shopCartUnit.setShopDetailId(getCurrentShopId());
                    shopCartUnit.setCustomerId(getCurrentCustomerId());
                    shopCartUnit.setShopType(Config.WEIGHT_PACKAGE_ARTICLE_DETAIL);
                    shopCartUnit.setGroupId(groupId);
                    shopCartUnit.setPid(farId);
                    shopCartService.updateShopCart(shopCartUnit);
                }
            }

            //处理推荐餐包
            String recommendList = cartJson.optString("recommendList");
            if(StringUtils.isNotEmpty(recommendList)){
                JSONArray recommendJson = new JSONArray(recommendList);
                for (int d = 0; d < recommendJson.length(); d++) {
                    JSONObject recommendInfo = recommendJson.getJSONObject(d);
                    if(Integer.parseInt(recommendInfo.optString("count")) > 0) {
                        ShopCart shopCartChild = new ShopCart();
                        shopCartChild.setArticleId(recommendInfo.optString("articleId"));
                        shopCartChild.setNumber(Integer.parseInt(recommendInfo.optString("count")));
                        shopCartChild.setDistributionModeId(1);
                        shopCartChild.setShopDetailId(getCurrentShopId());
                        shopCartChild.setCustomerId(getCurrentCustomerId());
                        shopCartChild.setShopType(Config.RECOMMEND_ARTICLE);
                        shopCartChild.setGroupId(groupId);
                        shopCartChild.setPid(farId);
                        shopCartService.updateShopCart(shopCartChild);
                    }
                }
            }
            JSONObject json = new JSONObject();
            json.put("statusCode", "0");
            json.put("message", "");
            json.put("success", true);
            if(StringUtils.isNotEmpty(groupId)){
                //如果在开启了多人点餐的情况下 修改菜品
                List<CustomerGroup> customerGroupList = customerGroupService.getGroupByGroupId(groupId);
                for(CustomerGroup customerGroup : customerGroupList){
                    if(!customerGroup.getCustomerId().equals(getCurrentCustomerId())){
//                        if(!MemcachedUtils.add(customerGroup.getCustomerId()+groupId,false)){
//                            MemcachedUtils.delete(customerGroup.getCustomerId()+ groupId);
//                            MemcachedUtils.add(customerGroup.getCustomerId()+groupId,false);
//                        }
                        if(!redisService.setnx(customerGroup.getCustomerId()+groupId,false)){
                            redisService.remove(customerGroup.getCustomerId()+ groupId);
                            redisService.setnx(customerGroup.getCustomerId()+groupId,false);
                        }
                        redisService.set(customerGroup.getCustomerId()+groupId,false);
                    }
                }
            }
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }else{
            JSONObject json = new JSONObject();
            json.put("statusCode", "0");
            json.put("message", "");
            json.put("success", true);
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
    }

    /**
     * 更新推荐菜为单品
     * @param request
     * @param id
     * @param response
     */
    @RequestMapping("/new/updateShopCartRecommend")
    public void updateShopCartRecommend(HttpServletRequest request, Integer id, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        shopCartService.updateShopCartRecommend(id);

        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("message", "");
        json.put("success", true);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    /**
     * 查询当前店铺的首页图片列表
     *
     * @return
     */
    @RequestMapping("pictureslider")
    public Result listPictureslider() {
        List<PictureSlider> list = pictureSliderService.selectListByShopId(getCurrentShopId());
        return getSuccessResult(list);
    }

    @RequestMapping("/new/pictureslider")
    public void listPicturesliderNew(HttpServletRequest request, HttpServletResponse response) {
        List<PictureSlider> list = pictureSliderService.selectListByShopId(getCurrentShopId());
        AppUtils.unpack(request, response);

        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("listShowPhoto")
    public Result listShowPhoto() {
        List<com.resto.brand.web.model.ShowPhoto> list = showPhotoService.selectList();
        return getSuccessResult(list);
    }

    @RequestMapping("/new/listShowPhoto")
    public void listShowPhotoNew(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

//        List<com.resto.brand.web.model.ShowPhoto> list = showPhotoService.selectList();
//        List<com.resto.brand.web.model.ShowPhoto> goodList = new ArrayList<>();
//        List<com.resto.brand.web.model.ShowPhoto> badList = new ArrayList<>();

        List<Experience> list = experienceService.selectListByShopId(getCurrentShopId());
        List<Experience> goodList = new ArrayList<>();
        List<Experience> badList = new ArrayList<>();
        for(Experience showPhoto : list){
            if(showPhoto.getShowType() == 2){
                goodList.add(showPhoto);
            }else if(showPhoto.getShowType() == 4){
                badList.add(showPhoto);
            }
        }
        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("message", "");
        json.put("success", true);
        json.put("goodList", goodList);
        json.put("badList", badList);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("emptyShopCart")
    public Result emptyShopCart() {
        shopCartService.clearShopCart(getCurrentCustomerId(), getCurrentShopId());
        return Result.getSuccess();
    }

    @RequestMapping("/new/emptyShopCart")
    public void emptyShopCartNew(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        shopCartService.clearShopCart(getCurrentCustomerId(), getCurrentShopId());

        Map map = new HashMap(4);
        map.put("brandName", getCurrentBrand().getBrandName());
        map.put("fileName", getCurrentCustomerId());
        map.put("type", "UserAction");
        map.put("content", "用户:"+getCurrentCustomer().getNickname()+"清空了购物车,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);

        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("message", "");
        json.put("success", true);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/selectByShopCity")
    public void selectByShopCity(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        String brandId = getCurrentBrandId();
        List<ShopDetail> list = shopDetailService.selectByShopCity(brandId);

        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("message", "");
        json.put("success", true);
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/selectByCityOrName")
    public void selectByCityOrName(String name, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        String brandId = getCurrentBrandId();
        List<ShopDetail> list = shopDetailService.selectByCityOrName(name, brandId);

        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        json.put("message", "");
        json.put("success", true);
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/selectByCity")
    public void selectByCity(String city, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        String brandId = getCurrentBrandId();
        List<ShopDetail> list = shopDetailService.selectByCity(city, brandId);

        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "");
        json.put("success", true);
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/orderByIndex")
    public void orderByIndex(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        List<ShopDetail> list = shopDetailService.selectOrderByIndex(getCurrentBrandId());

        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "");
        json.put("success", true);
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }


    @RequestMapping("/new/delMealArticle")
    public void delMealArticle(String id, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        JSONObject json = new JSONObject();

        shopCartService.delMealArticle(id);
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("success", true);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/moduleSetting")
    public void moduleSetting(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        List<Integer> hasModule = moduleListService.selectBrandHasModule(getCurrentBrandId());

        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("success", true);
        json.put("data", hasModule);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/checkShopOpenNewPosNow")
    public void checkShopOpenNewPosNow(String shopId, Integer payMode, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        ShopDetail shop =shopDetailService.selectByPrimaryKey(shopId);
        JSONObject json = new JSONObject();

        Boolean loginFlag = (Boolean) redisService.get(shopId + "loginStatus");
        if(shop.getPosVersion() == PosVersion.VERSION_2_0){
            if (loginFlag == null || loginFlag == false) {
                json.put("message", "当前店铺暂未开启在线点餐，请联系服务员详询，谢谢！");
                json.put("success", false);
                json.put("code", 50);
                ApiUtils.setBackInfo(response, json);
                return;
            }
        }

        BrandSetting brandSetting = brandSettingService.selectByBrandId(shop.getBrandId());
        if((brandSetting.getAliPay() != 1 || shop.getAliPay() != 1) && payMode == 2){
            json.put("message", "当前店铺暂未开启支付宝支付，请联系服务员详询，谢谢");
            json.put("success", false);
            json.put("code", 50);
            ApiUtils.setBackInfo(response, json);
            return;
        }
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("success", true);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
}
