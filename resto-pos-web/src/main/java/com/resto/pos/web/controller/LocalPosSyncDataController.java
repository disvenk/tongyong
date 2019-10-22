package com.resto.pos.web.controller;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.Encrypter;
import com.resto.brand.core.util.HttpRequest;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.pos.web.rpcinterceptors.DataSourceTarget;
import com.resto.shop.web.constant.OrderState;
import com.resto.shop.web.constant.PosVersion;
import com.resto.shop.web.model.ArticleUnitNew;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.TableGroup;
import com.resto.shop.web.service.*;
import com.resto.shop.web.service.OrderRemarkService;
import com.resto.brand.web.service.TableQrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  【Pos 2.0】
 *  本地 Pos 数据同步接口
 *  数据交互以 Token 检测有效身份信息
 * Created by Lmx on 2017/6/13.
 */
@Controller
@RequestMapping("/LocalPosSyncData")
public class LocalPosSyncDataController extends GenericController {


    @Resource
    private AreaService areaService;
    @Resource
    private ArticleService articleService;
    @Resource
    private ArticleAttrService articleAttrService;
    @Resource
    private ArticleFamilyService articleFamilyService;
    @Resource
    private KitchenService kitchenService;
    @Resource
    private ArticlePriceService articlePriceService;
    @Resource
    private ArticleUnitService articleUnitService;
    @Resource
    private MealAttrService mealAttrService;
    @Resource
    private MealItemService mealItemService;
    @Resource
    private PrinterService printerService;
    @Resource
    private ShopDetailService shopDetailService;
    @Resource
    private TableQrcodeService tableQrcodeService;
    @Resource
    private UnitService unitService;
    @Resource
    private BrandUserService brandUserService;
    @Resource
    private OrderService orderService;
    @Autowired
    private SupportTimeService supportTimeService;
    @Autowired
    private OrderRemarkService orderRemarkService;
    @Autowired
    private RefundRemarkService refundRemarkService;
    @Autowired
    private PosService posService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BrandSettingService brandSettingService;
    @Autowired
    ShopTvConfigService shopTvConfigService;
    @Autowired
    BrandService brandService;
    @Autowired
    PosConfigService posConfigService;
    @Autowired
    WeightPackageService weightPackageService ;
    @Autowired
    WeightPackageDetailService weightPackageDetailService;
    @Autowired
    VirtualProductsService virtualProductsService;

    @Autowired
    private KitchenPrinterService kitchenPrinterService;

    @Autowired
    private ArticleKitchenPrinterService articleKitchenPrinterService;

    @Autowired
    private KitchenGroupDetailService kitchenGroupDetailService;

    @Autowired
    private KitchenGroupService kitchenGroupService;

    @Autowired
    private VersionPosBrandService versionPosBrandService;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private RecommendArticleService recommendArticleService;

    @Autowired
    VersionPosShopDetailService versionPosShopDetailService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/test")
    @ResponseBody
    public Result test(String shopId){
        System.out.println(shopId);
        return getSuccessResult(shopId);
    }

    @RequestMapping("/checkKey")
    @ResponseBody
    public Result checkKey(String key){
        System.out.println("key：" + key);
        ShopDetail shopDetail =  shopDetailService.selectByPosKey(key);
        if(shopDetail == null){
            return new Result("请输入正确的 Key 值 ! ",false);
        }else{
            if(shopDetail.getPosVersion() != PosVersion.VERSION_2_0){
                return new Result("请将 【" + shopDetail.getName() + "】 的 Pos 版本切换为 2.0 ！ ",false);
            }else if(shopDetail.getShopMode() != ShopMode.BOSS_ORDER && shopDetail.getShopMode() != ShopMode.CALL_NUMBER&& shopDetail.getShopMode() != ShopMode.MEISHI){
                return new Result("当前Pos只支持 大Boss模式 , 电视叫号模式 和 美食广场模式，请更换 【" + shopDetail.getName() + "】的店铺模式！ " ,false);
            }else if(shopDetail.getShopMode() == ShopMode.BOSS_ORDER && shopDetail.getAllowFirstPay() == 0 && shopDetail.getAllowAfterPay() == 0 ){
                return new Result("大Boos模式下：先付和后付不允许同时开启！" ,false);
            }
            Map info = new HashMap();
            info.put("brandId",shopDetail.getBrandId());
            info.put("shopId",shopDetail.getId());
            return getSuccessResult(info);
        }
    }

    @RequestMapping("/areaList")
    @ResponseBody
    public Result areaList (String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(areaService.getAreaList(shopId));
    }


    @RequestMapping("/articleList")
    @ResponseBody
    public Result articleList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(articleService.selectArticleByShopId(shopId));
    }

    @RequestMapping("/articleAttrList")
    @ResponseBody
    public Result articleAttrList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(articleAttrService.selectListByShopId(shopId));
    }

    @RequestMapping("/articleFamilyList")
    @ResponseBody
    public Result articleFamilyList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(articleFamilyService.selectList(shopId));
    }

    @RequestMapping("/articleKitchenList")
    @ResponseBody
    public Result articleKitchenList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(kitchenService.selectArticleKitchenByShopId(shopId));
    }

    @RequestMapping("articlePriceList")
    @ResponseBody
    public Result articlePriceList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(articlePriceService.selectList(shopId));
    }

    @RequestMapping("articleUnitList")
    @ResponseBody
    public Result articleUnitList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(articleUnitService.selectByShopId(shopId));
    }


    @RequestMapping("articleUnitDetailList")
    @ResponseBody
    public Result articleUnitDetailList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(articleUnitService.selectArticleUnitDetailByShopId(shopId));
    }

    @RequestMapping("/articleUnitNewList")
    @ResponseBody
    public Result articleUnitNewList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        List<ArticleUnitNew> test = articleUnitService.selectArticleUnitNewByShopId(shopId);
        return getSuccessResult(test);
    }

    @RequestMapping("/kitchenList")
    @ResponseBody
    public Result kitchenList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(kitchenService.selectListByShopId(shopId));
    }

    @RequestMapping("/mealAttrList")
    @ResponseBody
    public Result mealAttrList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(mealAttrService.selectMealAttrByShopId(shopId));
    }

    @RequestMapping("/mealItemList")
    @ResponseBody
    public Result mealItemList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(mealItemService.selectMealItemByShopId(shopId));
    }

    @RequestMapping("/printerList")
    @ResponseBody
    public Result printerList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(printerService.selectListByShopId(shopId));
    }

    @RequestMapping("/shopDetail")
    @ResponseBody
    public Result shopDetail(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        Brand brand = brandService.selectByPrimaryKey(brandId);
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brandId);
        String url = "http://";
        if(brandSetting.getOpenHttps() == 1){
            url = "https://";
        }
        url += brand.getBrandSign() + ".restoplus.cn";
        ShopDetail shopDetail = shopDetailService.posSelectById(shopId);
        shopDetail.setRemark(url);
        return getSuccessResult(shopDetail);
    }

    @RequestMapping("/tableQrcodeList")
    @ResponseBody
    public Result tableQrcodeList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        List<TableQrcode> tableQrcodes = tableQrcodeService.selectByShopId(shopId);
        for(TableQrcode tableQrcode: tableQrcodes){
            tableQrcode.setTablePass(Encrypter.encrypt(tableQrcode.getId().toString()));
        }
        return getSuccessResult(tableQrcodes);
    }

    @RequestMapping("/unitList")
    @ResponseBody
    public Result unitList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(unitService.selectUnitByShopId(shopId));
    }


    @RequestMapping("/supportTime")
    @ResponseBody
    public Result supportTime(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(supportTimeService.selectList(shopId));
    }


    @RequestMapping("/articleSupport")
    @ResponseBody
    public Result articleSupport(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(posService.syncArticleSupport(shopId));
    }


    @RequestMapping("/unitDetailList")
    @ResponseBody
    public Result unitDetailList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(unitService.selectUnitDetailByShopId(shopId));
    }

    @RequestMapping("/brandUserList")
    @ResponseBody
    public Result brandUserList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(brandUserService.selectByShopId(shopId));
    }

    @RequestMapping("/orderRemarkList")
    @ResponseBody
    public Result orderRemarkList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(orderRemarkService.selectOrderRemarkListByShopId(shopId));
    }

    @RequestMapping("/refundRemarkList")
    @ResponseBody
    public Result refundRemarkList(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(refundRemarkService.selectList());
    }


    /**
     * 本地 Pos 端数据上传，需要做数据检测和sql注入等措施
     * @param orderData
     * @return
     */
    @RequestMapping("/pushOrderList")
    @ResponseBody
    public Result pushOrderList(@RequestBody Map<String, Object> orderData){
        String brandId = orderData.get("brandId") != null ? orderData.get("brandId").toString() : null;
        String shopId = orderData.get("shopId") != null ? orderData.get("shopId").toString() : null;
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        orderService.uploadLocalPosOrderList((List<Map<String,Object>>) orderData.get("orderList"));

        return getSuccessResult();
    }

    /**
     * 修改桌位的状态  控制桌位 锁定 或者 释放
     * @param brandId
     * @param shopId
     * @param tableNumber
     * @param state
     * @return
     */
    @RequestMapping("/updateTableState")
    @ResponseBody
    public Result updateTableState(String brandId, String shopId, String tableNumber, Boolean state){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        if(StringUtils.isEmpty(tableNumber) || state == null){
            return new Result("请输入桌位号或更改状态，true：释放  ， false：锁定",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
//        posService.syncTableState(shopId, tableNumber, state);
        return getSuccessResult((state ? "释放" : "锁定") + "成功");
    }

    /**
     * 获取 用户消费信息
     * @param brandId
     * @param shopId
     * @param customerId
     * @return
     */
    @RequestMapping("/getCustomerConsumeInfo")
    @ResponseBody
    public Result getCustomerConsumeInfo(String brandId, String shopId, String customerId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        if(StringUtils.isEmpty(customerId)){
            return new Result("请输入用户ID", false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(customerService.getCustomerConsumeInfo(shopId, customerId));
    }

    /**
     * 获取 品牌设置表信息
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/getBrandSetting")
    @ResponseBody
    public Result getBrandSetting(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        return getSuccessResult(brandSettingService.posSelectByBrandId(brandId));
    }

    /**
     * 服务器异常，设置店铺不在线，不允许微信端下单
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/serverError")
    @ResponseBody
    public Result serverError(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        posService.serverError(brandId, shopId);
        return getSuccessResult();
    }

    /**
     * 获取服务器消费订单id
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/getServerOrderIds")
    @ResponseBody
    public Result getServerOrderIds(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(posService.getServerOrderIds(shopId));
    }

    /**
     * 获取店铺菜品的当前库存
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/getArticleStock")
    @ResponseBody
    public Result getArticleStock(String brandId, String shopId, String articleId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(posService.syncArticleStock(shopId));
    }

    /**
     * 发送模拟订单数据
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/sendMockMQMessage")
    @ResponseBody
    public Result sendMockMQMessage(String brandId, String shopId, String type, String orderId, Integer platformType){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }else if("?".equals(brandId)){
            return new Result("createOrder，platform，orderPay，cancelOrder，change",false);
        }else if(StringUtils.isEmpty(type) || StringUtils.isEmpty(orderId)){
            return new Result("请输入消息类型或者订单ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        posService.sendMockMQMessage(shopId, type, orderId, platformType);
        return getSuccessResult();
    }

    /**
     * 发送指定命令至 POS 端
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/serverCommand")
    @ResponseBody
    public Result serverCommand(String brandId, String shopId, String type, String sql){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }else if(StringUtils.isEmpty(type) || StringUtils.isEmpty(sql)){
            return new Result("请输入消息类型或者命令",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        posService.sendServerCommand(shopId, type, sql);
        return getSuccessResult();
    }


    /**
     * 获取当前店铺的 电视设置
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/getShopTvConfig")
    @ResponseBody
    public Result getShopTvConfig(String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(shopTvConfigService.selectByShopId(shopId));
    }

    /**
     * 更改库存
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/updateArticleEdit")
    @ResponseBody
    public Result updateArticleEdit(String brandId, String shopId, String articleId, Integer count){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        if(StringUtils.isEmpty(articleId) || count == null){
            return new Result("请输入菜品ID或更新的数量",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        posService.articleEdit(articleId, count);
        return getSuccessResult();
    }

    /**
     * 获取服务器地址
     * @param brandId
     * @return
     */
    @RequestMapping("/getServerAddress")
    @ResponseBody
    public Result getServerAddress(String brandId){
        if(StringUtils.isEmpty(brandId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        Brand brand = brandService.selectByPrimaryKey(brandId);
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brandId);
        String url = "http://";
        if(brandSetting.getOpenHttps() == 1){
            url = "https://";
        }
        url += brand.getBrandSign() + ".restoplus.cn/wechat/msg/getLocalIp";
        log.info("\n url：\n" + url + "\n");
        String localIp = HttpRequest.get(url.toString()).body();
        if(localIp.indexOf("<html><head>") == -1){
            PosConfig posConfig = posConfigService.getConfigByClientIp(localIp);
            return getSuccessResult(posConfig);
        }else{
            return new Result("获取地址失败，请联系管理员；" + url,false);
        }
    }


    /**
     * 重量包
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/weightPackageList")
    @ResponseBody
    public Result weightPackageList (String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(weightPackageService.selectWeightPackageByShopId(shopId));
    }

    /**
     * 重量包详情
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/weightPackageDetailList")
    @ResponseBody
    public Result weightPackageDetailList (String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(weightPackageDetailService.selectWeightPackageDetailByShopId(shopId));
    }


    /**
     * 虚拟餐包列表
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/virtualProductsList")
    @ResponseBody
    public Result virtualProductsList (String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(virtualProductsService.getAllProducuts(shopId));
    }

    /**
     * 虚拟餐包厨房列表
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/virtualProductsKitchenList")
    @ResponseBody
    public Result virtualProductsKitchenList (String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(virtualProductsService.selectKitchenByShopId(shopId));
    }
    /**
     * 根据店铺id查询厨房和打印机
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/selectKitchenAndPrinterList")
    @ResponseBody
    public Result selectKitchenAndPrinterListByShopId (String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(kitchenPrinterService.selectKitchenAndPriterByShopID(shopId));
    }
    /**
     * 根据店铺id查询菜品和厨房和打印机
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/selectArticleAndkitchenGroup")
    @ResponseBody
    public Result selectArticleAndkitchenGroup (String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(articleKitchenPrinterService.selectByArticleAndShopId(shopId));
    }

    /**
     * 根据店铺id查询厨房组和厨房id
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/selectKitchenGroupAndKitchen")
    @ResponseBody
    public Result selectKitchenGroupAndKitchen (String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(kitchenGroupDetailService.selectKitchenGroupDetailByShopId(shopId));
    }

    /**
     * 根据店铺id查询厨房组
     * @param brandId
     * @param shopId
     * @return
     */
    @RequestMapping("/selectKitchenGroup")
    @ResponseBody
    public Result selectKitchenGroup (String brandId, String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(kitchenGroupService.selectKitchenGroupByShopDetailId(shopId));
    }


    /**
     * 验证订单
     * @param orderId
     * @return
     */
    @RequestMapping("/verificationOfOrders")
    @ResponseBody
    public Result verificationOfOrders(String orderId, Integer type){
        if (type == null) {
            //判断当前订单是否在微信支付当中
            if (redisService.get(orderId + "WxPay") != null) {
                Order order = orderService.selectById(orderId);
                if (order.getOrderState() == OrderState.SUBMIT) {
                    return new Result("此订单正在微信支付，请勿加菜！", false);
                }
            }
        }else if (type == 1){
            //判断订单是否正在加菜中
            if (redisService.get(orderId.concat("ADDADISH")) != null
                    && (boolean)redisService.get(orderId.concat("ADDADISH"))){
                return new Result("此订单正在加菜中", false);
            }
        }
        Result result = new Result();
        result.setSuccess(true);
        return result;
    }

    /**
     *根据品牌查询品牌的版本号及下载地址及当前品牌下的店铺的版本号及下载地址
     * @param brandId
     * @return
     */
    @RequestMapping("/versionPosBrand")
    @ResponseBody
    public Result selectVersionBrand(String brandId,String shopId){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        Map<String, Object> map = versionPosBrandService.selectListByBrandId(brandId, shopId);
        return getSuccessResult(map);
    }

    /**
     *
     * @param brandId
     * @param shopId
     * @param shopVersion
     * @return
     */
    @RequestMapping("/updatePosVersion")
    @ResponseBody
    public Result updatePosVersion(String brandId,String shopId,String shopVersion){
        if(StringUtils.isEmpty(brandId) || StringUtils.isEmpty(shopId)||StringUtils.isEmpty(shopVersion)){
            return new Result("请输入品牌ID或店铺ID",false);
        }
        versionPosShopDetailService.updatePosVersion(brandId,shopId,shopVersion);
        Result result = new Result();
        result.setSuccess(true);
        return result;
    }

    /**
     * 查询推荐餐品
     * @param brandId
     * @return
     */
    @RequestMapping("/selectRecommend")
    @ResponseBody
    public Result selectRecommend(String brandId){
        if(StringUtils.isEmpty(brandId)){
            return new Result("请输入品牌ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(recommendService.selectList());
    }

    /**
     * 查询推荐菜品关联推荐餐品
     * @param brandId
     * @return
     */
    @RequestMapping("/selectRecommendArticle")
    @ResponseBody
    public Result selectRecommendArticle(String brandId){
        if(StringUtils.isEmpty(brandId)){
            return new Result("请输入品牌ID",false);
        }
        DataSourceTarget.setDataSourceName(brandId);
        return getSuccessResult(recommendArticleService.selectList());
    }


//
//
//    @RequestMapping("/testOrder")
//    @ResponseBody
//    public Result testOrder(@RequestBody Map<String, Object> orderInfo){
//        System.out.println(orderInfo.get("test"));
//        List<Map<String, String>> testList = (List<Map<String, String>>) orderInfo.get("orderList");
//        System.out.println(orderInfo.get("orderList"));
//        System.out.println("----");
//        return getSuccessResult();
//    }
}
