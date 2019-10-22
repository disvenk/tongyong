package com.resto.shop.web.controller.business;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.resto.brand.core.util.AppendToExcelUtil;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.constant.OrderItemType;
import com.resto.shop.web.model.Menu;
import com.resto.shop.web.model.MenuShop;
import com.resto.shop.web.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ExcelUtil;
import com.resto.brand.web.dto.ArticleSellDto;
import com.resto.brand.web.dto.ShopArticleReportDto;
import com.resto.brand.web.dto.brandArticleReportDto;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.ArticleType;
import com.resto.shop.web.controller.GenericController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 菜品销售报表
 * @author lmx
 */
@Controller
@RequestMapping("/articleSell")
public class ArticleSellController extends GenericController{

    @Resource
    OrderService orderService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    BrandService brandServie;

    @Resource
    ArticleFamilyService articleFamilyService;

    @Resource
    ArticleService articleService;

    @Resource
    MealAttrService mealAttrService;

    @Resource
    MenuShopService menushopService;

    @Resource
    MenuService menuService;

    @Resource
    BrandSettingService brandSettingService;

    @RequestMapping("/list")
    public void list(){
    }

    @RequestMapping("/brandList")
    public String brandList(){
        return "articleSell/brandList";
    }

    @RequestMapping("/shopList")
    public String shopList(){
        return "articleSell/shopList";
    }

    @RequestMapping("/newBrandList")
    public ModelAndView newBrandList(){
        BrandSetting setting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if(setting != null && setting.getOpenArticleLibrary().equals(new Integer(1))){ //开启菜品库功能
            return new ModelAndView("articleSellNew/brandList");
        }else{
            return new ModelAndView("articleSellNew/none");
        }
    }

    @RequestMapping("/newShopList")
    public ModelAndView newShopList(){
        BrandSetting setting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if(setting != null && setting.getOpenArticleLibrary().equals(new Integer(1))){ //开启菜品库功能
            return new ModelAndView("articleSellNew/shopList");
        }else{
            return new ModelAndView("articleSellNew/none");
        }
    }


    @RequestMapping("/listShopMenu")
    @ResponseBody
    public List<MenuShop> listShopMenu(){
        List<MenuShop> list = menushopService.listShopMenu(getCurrentShopId());
        list.forEach(s->{
            Menu menu = menuService.selectById(s.getMenuId());
            s.setMenuName(menu.getMenuName());
        });
        return list;
    }

    /**
     * 查询品牌销售报表
     * @param beginDate
     * @param endDate
     * @param type
     * @return
     */
    @RequestMapping("/queryOrderArtcile")
    @ResponseBody
    public Result queryOrderArtcile(String beginDate, String endDate, Integer type){
        JSONObject object = new JSONObject();
        try {
            Map<String, Object> selectMap = new HashMap<String, Object>();
            //得到当前品牌菜品的所有类别信息
            List<ArticleSellDto> brandArticleType = articleFamilyService.selectByShopId(null);
            BigDecimal brandSellNum = BigDecimal.ZERO;
            BigDecimal salles = BigDecimal.ZERO;
            //获取品牌总的销售信息
            brandArticleReportDto brandCount = orderService.callBrandArticleNum(beginDate, endDate, getCurrentBrandId(), getBrandName());
            object.put("brandReport", brandCount);
            selectMap.put("beginDate", beginDate);
            selectMap.put("endDate", endDate);
            selectMap.put("type", ArticleType.SIMPLE_ARTICLE);
            //查询卖出的菜品销售信息
            List<ArticleSellDto> articleUnitSellDtos = articleService.callOrderArtcile(selectMap);
            //查询所有的菜品销售信息
            List<ArticleSellDto> articleUnitSell = articleService.selectArticleByType(selectMap);
            //查出单品或套餐总的销售量及销售额
            selectMap.put("type", OrderItemType.ARTICLE + "," + OrderItemType.UNITPRICE + "," + OrderItemType.UNIT_NEW + "," + OrderItemType.RECOMMEND + "," + OrderItemType.WEIGHT_PACKAGE_ARTICLE);
            Map<String, Object> unitMap = articleService.callArticleOrderCount(selectMap);
            for (ArticleSellDto articleUnitSellDto : articleUnitSellDtos){
                //计算销售量占比
                if (unitMap.get("sellNum").toString().equalsIgnoreCase("0")){
                    articleUnitSellDto.setNumRatio("0.00%");
                }else{
                    articleUnitSellDto.setNumRatio(new BigDecimal(articleUnitSellDto.getBrandSellNum()).divide(new BigDecimal(
                            unitMap.get("sellNum").toString()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) +"%");
                }
                //计算销售额占比
                if (unitMap.get("salles").toString().equalsIgnoreCase("0")){
                    articleUnitSellDto.setSalesRatio("0.00%");
                }else{
                    articleUnitSellDto.setSalesRatio(articleUnitSellDto.getSalles().divide(new BigDecimal(
                            unitMap.get("salles").toString()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) +"%");
                }
                //循环所有菜品
                for (ArticleSellDto articleSellDto : articleUnitSell){
                    if (articleSellDto.getArticleId().equalsIgnoreCase(articleUnitSellDto.getArticleId())){
                        articleUnitSell.remove(articleSellDto);
                        break;
                    }
                }
                for (ArticleSellDto articleSellDto : brandArticleType){
                    if (articleSellDto.getArticleFamilyId().equalsIgnoreCase(articleUnitSellDto.getArticleFamilyId())){
                        articleSellDto.setBrandSellNum(new BigDecimal(articleSellDto.getBrandSellNum()).add(new BigDecimal(articleUnitSellDto.getBrandSellNum())).intValue());
                        brandSellNum = brandSellNum.add(new BigDecimal(articleUnitSellDto.getBrandSellNum()));
                        articleSellDto.setSalles(articleSellDto.getSalles().add(articleUnitSellDto.getSalles()));
                        salles = salles.add(articleUnitSellDto.getSalles());
                        articleSellDto.setDiscountMoney(articleSellDto.getDiscountMoney().add(articleUnitSellDto.getDiscountMoney()));
                        articleSellDto.setRefundCount(new BigDecimal(articleSellDto.getRefundCount()).add(new BigDecimal(articleUnitSellDto.getRefundCount())).intValue());
                        articleSellDto.setRefundTotal(articleSellDto.getRefundTotal().add(articleUnitSellDto.getRefundTotal()));
                        articleSellDto.setGrantCount(new BigDecimal(articleSellDto.getGrantCount() == null? 0 : articleSellDto.getGrantCount()).add(new BigDecimal(articleUnitSellDto.getGrantCount())).intValue());
                        articleSellDto.setGrantTotal((articleSellDto.getGrantTotal() == null ? BigDecimal.ZERO : articleSellDto.getGrantTotal()).add(articleUnitSellDto.getGrantTotal()));
                        articleSellDto.setLikes(new BigDecimal(articleSellDto.getLikes()).add(new BigDecimal(articleUnitSellDto.getLikes())).intValue());
                        //得到当前菜品所属类别名称
                        articleUnitSellDto.setArticleFamilyName(articleSellDto.getArticleFamilyName());
                        break;
                    }
                }
            }
            for (ArticleSellDto articleSellDto : articleUnitSell){
                articleSellDto.setBrandSellNum(0);
                articleSellDto.setNumRatio("0.00%");
                articleSellDto.setSalles(BigDecimal.ZERO);
                articleSellDto.setSalesRatio("0.00%");
                articleSellDto.setDiscountMoney(BigDecimal.ZERO);
                articleSellDto.setRefundCount(0);
                articleSellDto.setRefundTotal(BigDecimal.ZERO);
                articleSellDto.setWeight(BigDecimal.ZERO);
                articleSellDto.setGrantCount(0);
                articleSellDto.setGrantTotal(BigDecimal.ZERO);
                articleUnitSellDtos.add(articleSellDto);
            }
            object.put("brandArticleUnit", articleUnitSellDtos);
            selectMap.put("type", ArticleType.TOTAL_ARTICLE);
            List<ArticleSellDto> articleFamilySellDtos = articleService.callOrderArtcile(selectMap);
            List<ArticleSellDto> articleFamilySell = articleService.selectArticleByType(selectMap);
            selectMap.put("type", OrderItemType.SETMEALS + "," + OrderItemType.MEALS_CHILDREN );
            Map<String, Object> familyMap = articleService.callArticleOrderCount(selectMap);
            for (ArticleSellDto articleFamilySellDto : articleFamilySellDtos){
                if (familyMap.get("sellNum").toString().equalsIgnoreCase("0")){
                    articleFamilySellDto.setNumRatio("0.00%");
                }else{
                    articleFamilySellDto.setNumRatio(new BigDecimal(articleFamilySellDto.getBrandSellNum()).divide(new BigDecimal(
                            familyMap.get("sellNum").toString()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) +"%");
                }
                if (familyMap.get("salles").toString().equalsIgnoreCase("0")){
                    articleFamilySellDto.setSalesRatio("0.00%");
                }else{
                    if(new BigDecimal(
                            familyMap.get("salles").toString()).doubleValue() <= 0){
                        articleFamilySellDto.setSalesRatio("0%");
                    }else{
                        articleFamilySellDto.setSalesRatio(articleFamilySellDto.getSalles().divide(new BigDecimal(
                                familyMap.get("salles").toString()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) +"%");
                    }

                }
                for (ArticleSellDto articleSellDto : articleFamilySell){
                    if (articleSellDto.getArticleId().equalsIgnoreCase(articleFamilySellDto.getArticleId())){
                        articleFamilySell.remove(articleSellDto);
                        break;
                    }
                }
                for (ArticleSellDto articleSellDto : brandArticleType){
                    if (articleSellDto.getArticleFamilyId().equalsIgnoreCase(articleFamilySellDto.getArticleFamilyId())){
                        articleSellDto.setBrandSellNum(new BigDecimal(articleSellDto.getBrandSellNum()).add(new BigDecimal(articleFamilySellDto.getBrandSellNum())).intValue());
                        brandSellNum = brandSellNum.add(new BigDecimal(articleFamilySellDto.getBrandSellNum()));
                        articleSellDto.setSalles(articleSellDto.getSalles().add(articleFamilySellDto.getSalles()));
                        salles = salles.add(articleFamilySellDto.getSalles());
                        articleSellDto.setDiscountMoney(articleSellDto.getDiscountMoney().add(articleFamilySellDto.getDiscountMoney()));
                        articleSellDto.setRefundCount(new BigDecimal(articleSellDto.getRefundCount()).add(new BigDecimal(articleFamilySellDto.getRefundCount())).intValue());
                        articleSellDto.setRefundTotal(articleSellDto.getRefundTotal().add(articleFamilySellDto.getRefundTotal()));
                        articleSellDto.setGrantCount(new BigDecimal(articleSellDto.getGrantCount() == null ? 0 : articleSellDto.getGrantCount()).add(new BigDecimal(articleFamilySellDto.getGrantCount())).intValue());
                        articleSellDto.setGrantTotal((articleSellDto.getGrantTotal() == null ? BigDecimal.ZERO : articleSellDto.getGrantTotal()).add(articleFamilySellDto.getGrantTotal()));
                        articleSellDto.setLikes(new BigDecimal(articleSellDto.getLikes()).add(new BigDecimal(articleFamilySellDto.getLikes())).intValue());
                        articleFamilySellDto.setArticleFamilyName(articleSellDto.getArticleFamilyName());
                        break;
                    }
                }
            }
            for (ArticleSellDto articleSellDto : articleFamilySell){
                articleSellDto.setBrandSellNum(0);
                articleSellDto.setNumRatio("0.00%");
                articleSellDto.setSalles(BigDecimal.ZERO);
                articleSellDto.setSalesRatio("0.00%");
                articleSellDto.setDiscountMoney(BigDecimal.ZERO);
                articleSellDto.setRefundCount(0);
                articleSellDto.setRefundTotal(BigDecimal.ZERO);
                articleSellDto.setGrantCount(0);
                articleSellDto.setGrantTotal(BigDecimal.ZERO);
                articleSellDto.setWeight(BigDecimal.ZERO);
                articleFamilySellDtos.add(articleSellDto);
            }
            object.put("brandArticleFamily", articleFamilySellDtos);
            for (ArticleSellDto articleSellDto : brandArticleType){
                if (brandSellNum.equals(BigDecimal.ZERO)){
                    articleSellDto.setNumRatio("0.00%");
                }else{
                    articleSellDto.setNumRatio(new BigDecimal(articleSellDto.getBrandSellNum()).divide(brandSellNum,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString().concat("%"));
                }
                if (salles.equals(BigDecimal.ZERO)){
                    articleSellDto.setSalesRatio("0.00%");
                }else{
                    articleSellDto.setSalesRatio(articleSellDto.getSalles().divide(salles,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString().concat("%"));
                }
            }
            object.put("brandArticleType",brandArticleType);
            ShopDetail shopDetail = new ShopDetail();
            ArticleSellDto mealSellDto = new ArticleSellDto();
            List<Map<String, Object>> mealSalesList = orderService.selectMealServiceSales(selectMap);
            List<ArticleSellDto> mealSellList = new ArrayList<>();
            for (Map mealSales : mealSalesList) {
                shopDetail = shopDetailService.selectById(mealSales.get("shopId").toString());
                //判断店铺是否开启餐盒费，如开启了无论产没产生都要显示出来
                if ((StringUtils.isNotBlank(shopDetail.getMealFeeName()) && shopDetail.getIsMealFee().equals(Common.YES))) {
                    mealSellDto.setArticleName(shopDetail.getMealFeeName());
                    mealSellDto.setBrandSellNum(mealSales == null ? 0 : Integer.valueOf(mealSales.get("mealSellNum").toString()));
                    mealSellDto.setSalles(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("mealSalles").toString()));
                    mealSellDto.setRefundCount(mealSales == null ? 0 : Integer.valueOf(mealSales.get("refundMealNum").toString()));
                    mealSellDto.setRefundTotal(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("refundMealSalles").toString()));
                    mealSellList.add(mealSellDto);
                } else if (mealSales != null && mealSales.get("mealSellNum") != null && Integer.valueOf(mealSales.get("mealSellNum").toString()) != 0) {
                    //如店铺没有开启餐盒费但有餐盒售卖记录产生也显示出来
                    mealSellDto.setArticleName(shopDetail.getMealFeeName());
                    mealSellDto.setBrandSellNum(Integer.valueOf(mealSales.get("mealSellNum").toString()));
                    mealSellDto.setSalles(new BigDecimal(mealSales.get("mealSalles").toString()));
                    mealSellDto.setRefundCount(Integer.valueOf(mealSales.get("refundMealNum").toString()));
                    mealSellDto.setRefundTotal(new BigDecimal(mealSales.get("refundMealSalles").toString()));
                    mealSellList.add(mealSellDto);
                }
                //判断店铺是否开启服务费，如开启了无论产没产生都要显示出来
                if ((StringUtils.isNotBlank(shopDetail.getServiceName()) && shopDetail.getIsUseServicePrice().equals(Common.YES))) {
                    mealSellDto = new ArticleSellDto();
                    mealSellDto.setArticleName(shopDetail.getServiceName());
                    mealSellDto.setBrandSellNum(mealSales == null ? 0 : Integer.valueOf(mealSales.get("serviceSellNum").toString()));
                    mealSellDto.setSalles(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("serviceSalles").toString()));
                    mealSellDto.setRefundCount(mealSales == null ? 0 : Integer.valueOf(mealSales.get("refundServiceCount").toString()));
                    mealSellDto.setRefundTotal(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("refundServiceSalles").toString()));
                    mealSellList.add(mealSellDto);
                } else if (mealSales != null && mealSales.get("serviceSellNum") != null && Integer.valueOf(mealSales.get("serviceSellNum").toString()) != 0) {
                    mealSellDto = new ArticleSellDto();
                    //如店铺没有开启服务费但有服务费售卖记录产生也显示出来
                    mealSellDto.setArticleName(shopDetail.getServiceName());
                    mealSellDto.setBrandSellNum(Integer.valueOf(mealSales.get("serviceSellNum").toString()));
                    mealSellDto.setSalles(new BigDecimal(mealSales.get("serviceSalles").toString()));
                    mealSellDto.setRefundCount(Integer.valueOf(mealSales.get("refundServiceCount").toString()));
                    mealSellDto.setRefundTotal(new BigDecimal(mealSales.get("refundServiceSalles").toString()));
                    mealSellList.add(mealSellDto);
                }
                mealSellDto = new ArticleSellDto();
            }
            object.put("brandMealSalesDtos", mealSellList);
        }catch (Exception e){
            log.error("查询菜品销售报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(object);
    }

    /**
     * 菜品库查询品牌销售报表
     * @param beginDate
     * @param endDate
     * @return
     */
    @RequestMapping("/queryOrderArtcileNew")
    @ResponseBody
    public Result queryOrderArtcileNew(String beginDate, String endDate){
        JSONObject object = new JSONObject();
        try {
            Map<String, Object> selectMap = new HashMap<String, Object>();
            //得到当前品牌菜品的所有类别信息
            List<ArticleSellDto> brandArticleType = articleFamilyService.selectByShopId(null);
            BigDecimal brandSellNum = BigDecimal.ZERO;
            BigDecimal salles = BigDecimal.ZERO;
            //获取品牌总的销售信息
            brandArticleReportDto brandCount = orderService.callBrandArticleNumNew(beginDate, endDate, getCurrentBrandId(), getBrandName());
            object.put("brandReport", brandCount);
            selectMap.put("beginDate", beginDate);
            selectMap.put("endDate", endDate);
            selectMap.put("type", ArticleType.SIMPLE_ARTICLE);
            //查询卖出的菜品销售信息
            List<ArticleSellDto> articleUnitSellDtos = articleService.callOrderArtcileNew(selectMap);
            //查询所有的菜品销售信息
            List<ArticleSellDto> articleUnitSell = articleService.selectArticleByTypeNew(selectMap);
            //查出单品或套餐总的销售量及销售额
            selectMap.put("type", OrderItemType.ARTICLE + "," + OrderItemType.UNITPRICE + "," + OrderItemType.UNIT_NEW + "," + OrderItemType.RECOMMEND + "," + OrderItemType.WEIGHT_PACKAGE_ARTICLE);
            Map<String, Object> unitMap = articleService.callArticleOrderCount(selectMap);
            for (ArticleSellDto articleUnitSellDto : articleUnitSellDtos){
                //计算销售量占比
                if (unitMap.get("sellNum").toString().equalsIgnoreCase("0")){
                    articleUnitSellDto.setNumRatio("0.00%");
                }else{
                    articleUnitSellDto.setNumRatio(new BigDecimal(articleUnitSellDto.getBrandSellNum()).divide(new BigDecimal(
                            unitMap.get("sellNum").toString()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) +"%");
                }
                //计算销售额占比
                if (unitMap.get("salles").toString().equalsIgnoreCase("0")){
                    articleUnitSellDto.setSalesRatio("0.00%");
                }else{
                    articleUnitSellDto.setSalesRatio(articleUnitSellDto.getSalles().divide(new BigDecimal(
                            unitMap.get("salles").toString()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) +"%");
                }
                //循环所有菜品
                for (ArticleSellDto articleSellDto : articleUnitSell){
                    if (articleSellDto.getArticleId().equalsIgnoreCase(articleUnitSellDto.getArticleId())){
                        articleUnitSell.remove(articleSellDto);
                        break;
                    }
                }
                for (ArticleSellDto articleSellDto : brandArticleType){
                    if (articleSellDto.getArticleFamilyId().equalsIgnoreCase(articleUnitSellDto.getArticleFamilyId())){
                        articleSellDto.setBrandSellNum(new BigDecimal(articleSellDto.getBrandSellNum()).add(new BigDecimal(articleUnitSellDto.getBrandSellNum())).intValue());
                        brandSellNum = brandSellNum.add(new BigDecimal(articleUnitSellDto.getBrandSellNum()));
                        articleSellDto.setSalles(articleSellDto.getSalles().add(articleUnitSellDto.getSalles()));
                        salles = salles.add(articleUnitSellDto.getSalles());
                        articleSellDto.setDiscountMoney(articleSellDto.getDiscountMoney().add(articleUnitSellDto.getDiscountMoney()));
                        articleSellDto.setRefundCount(new BigDecimal(articleSellDto.getRefundCount()).add(new BigDecimal(articleUnitSellDto.getRefundCount())).intValue());
                        articleSellDto.setRefundTotal(articleSellDto.getRefundTotal().add(articleUnitSellDto.getRefundTotal()));
                        articleSellDto.setLikes(new BigDecimal(articleSellDto.getLikes()).add(new BigDecimal(articleUnitSellDto.getLikes())).intValue());
                        //得到当前菜品所属类别名称
                        articleUnitSellDto.setArticleFamilyName(articleSellDto.getArticleFamilyName());
                        break;
                    }
                }
            }
            for (ArticleSellDto articleSellDto : articleUnitSell){
                articleSellDto.setBrandSellNum(0);
                articleSellDto.setNumRatio("0.00%");
                articleSellDto.setSalles(BigDecimal.ZERO);
                articleSellDto.setSalesRatio("0.00%");
                articleSellDto.setDiscountMoney(BigDecimal.ZERO);
                articleSellDto.setRefundCount(0);
                articleSellDto.setRefundTotal(BigDecimal.ZERO);
                articleSellDto.setWeight(BigDecimal.ZERO);
                articleUnitSellDtos.add(articleSellDto);
            }
            object.put("brandArticleUnit", articleUnitSellDtos);
            selectMap.put("type", ArticleType.TOTAL_ARTICLE);
            List<ArticleSellDto> articleFamilySellDtos = articleService.callOrderArtcile(selectMap);
            List<ArticleSellDto> articleFamilySell = articleService.selectArticleByType(selectMap);
            selectMap.put("type", OrderItemType.SETMEALS + "," + OrderItemType.MEALS_CHILDREN );
            Map<String, Object> familyMap = articleService.callArticleOrderCount(selectMap);
            for (ArticleSellDto articleFamilySellDto : articleFamilySellDtos){
                if (familyMap.get("sellNum").toString().equalsIgnoreCase("0")){
                    articleFamilySellDto.setNumRatio("0.00%");
                }else{
                    articleFamilySellDto.setNumRatio(new BigDecimal(articleFamilySellDto.getBrandSellNum()).divide(new BigDecimal(
                            familyMap.get("sellNum").toString()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) +"%");
                }
                if (familyMap.get("salles").toString().equalsIgnoreCase("0")){
                    articleFamilySellDto.setSalesRatio("0.00%");
                }else{
                    if(new BigDecimal(
                            familyMap.get("salles").toString()).doubleValue() <= 0){
                        articleFamilySellDto.setSalesRatio("0%");
                    }else{
                        articleFamilySellDto.setSalesRatio(articleFamilySellDto.getSalles().divide(new BigDecimal(
                                familyMap.get("salles").toString()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) +"%");
                    }

                }
                for (ArticleSellDto articleSellDto : articleFamilySell){
                    if (articleSellDto.getArticleId().equalsIgnoreCase(articleFamilySellDto.getArticleId())){
                        articleFamilySell.remove(articleSellDto);
                        break;
                    }
                }
                for (ArticleSellDto articleSellDto : brandArticleType){
                    if (articleSellDto.getArticleFamilyId().equalsIgnoreCase(articleFamilySellDto.getArticleFamilyId())){
                        articleSellDto.setBrandSellNum(new BigDecimal(articleSellDto.getBrandSellNum()).add(new BigDecimal(articleFamilySellDto.getBrandSellNum())).intValue());
                        brandSellNum = brandSellNum.add(new BigDecimal(articleFamilySellDto.getBrandSellNum()));
                        articleSellDto.setSalles(articleSellDto.getSalles().add(articleFamilySellDto.getSalles()));
                        salles = salles.add(articleFamilySellDto.getSalles());
                        articleSellDto.setDiscountMoney(articleSellDto.getDiscountMoney().add(articleFamilySellDto.getDiscountMoney()));
                        articleSellDto.setRefundCount(new BigDecimal(articleSellDto.getRefundCount()).add(new BigDecimal(articleFamilySellDto.getRefundCount())).intValue());
                        articleSellDto.setRefundTotal(articleSellDto.getRefundTotal().add(articleFamilySellDto.getRefundTotal()));
                        articleSellDto.setLikes(new BigDecimal(articleSellDto.getLikes()).add(new BigDecimal(articleFamilySellDto.getLikes())).intValue());
                        articleFamilySellDto.setArticleFamilyName(articleSellDto.getArticleFamilyName());
                        break;
                    }
                }
            }
            for (ArticleSellDto articleSellDto : articleFamilySell){
                articleSellDto.setBrandSellNum(0);
                articleSellDto.setNumRatio("0.00%");
                articleSellDto.setSalles(BigDecimal.ZERO);
                articleSellDto.setSalesRatio("0.00%");
                articleSellDto.setDiscountMoney(BigDecimal.ZERO);
                articleSellDto.setRefundCount(0);
                articleSellDto.setRefundTotal(BigDecimal.ZERO);
                articleSellDto.setWeight(BigDecimal.ZERO);
                articleFamilySellDtos.add(articleSellDto);
            }
            object.put("brandArticleFamily", articleFamilySellDtos);
            for (ArticleSellDto articleSellDto : brandArticleType){
                if (brandSellNum.equals(BigDecimal.ZERO)){
                    articleSellDto.setNumRatio("0.00%");
                }else{
                    articleSellDto.setNumRatio(new BigDecimal(articleSellDto.getBrandSellNum()).divide(brandSellNum,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString().concat("%"));
                }
                if (salles.equals(BigDecimal.ZERO)){
                    articleSellDto.setSalesRatio("0.00%");
                }else{
                    articleSellDto.setSalesRatio(articleSellDto.getSalles().divide(salles,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString().concat("%"));
                }
            }
            object.put("brandArticleType",brandArticleType);
            ShopDetail shopDetail = new ShopDetail();
            ArticleSellDto mealSellDto = new ArticleSellDto();
            List<Map<String, Object>> mealSalesList = orderService.selectMealServiceSales(selectMap);
            List<ArticleSellDto> mealSellList = new ArrayList<>();
            for (Map mealSales : mealSalesList) {
                shopDetail = shopDetailService.selectById(mealSales.get("shopId").toString());
                //判断店铺是否开启餐盒费，如开启了无论产没产生都要显示出来
                if ((StringUtils.isNotBlank(shopDetail.getMealFeeName()) && shopDetail.getIsMealFee().equals(Common.YES))) {
                    mealSellDto.setArticleName(shopDetail.getMealFeeName());
                    mealSellDto.setBrandSellNum(mealSales == null ? 0 : Integer.valueOf(mealSales.get("mealSellNum").toString()));
                    mealSellDto.setSalles(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("mealSalles").toString()));
                    mealSellDto.setRefundCount(mealSales == null ? 0 : Integer.valueOf(mealSales.get("refundMealNum").toString()));
                    mealSellDto.setRefundTotal(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("refundMealSalles").toString()));
                    mealSellList.add(mealSellDto);
                } else if (mealSales != null && mealSales.get("mealSellNum") != null && Integer.valueOf(mealSales.get("mealSellNum").toString()) != 0) {
                    //如店铺没有开启餐盒费但有餐盒售卖记录产生也显示出来
                    mealSellDto.setArticleName(shopDetail.getMealFeeName());
                    mealSellDto.setBrandSellNum(Integer.valueOf(mealSales.get("mealSellNum").toString()));
                    mealSellDto.setSalles(new BigDecimal(mealSales.get("mealSalles").toString()));
                    mealSellDto.setRefundCount(Integer.valueOf(mealSales.get("refundMealNum").toString()));
                    mealSellDto.setRefundTotal(new BigDecimal(mealSales.get("refundMealSalles").toString()));
                    mealSellList.add(mealSellDto);
                }
                //判断店铺是否开启服务费，如开启了无论产没产生都要显示出来
                if ((StringUtils.isNotBlank(shopDetail.getServiceName()) && shopDetail.getIsUseServicePrice().equals(Common.YES))) {
                    mealSellDto = new ArticleSellDto();
                    mealSellDto.setArticleName(shopDetail.getServiceName());
                    mealSellDto.setBrandSellNum(mealSales == null ? 0 : Integer.valueOf(mealSales.get("serviceSellNum").toString()));
                    mealSellDto.setSalles(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("serviceSalles").toString()));
                    mealSellDto.setRefundCount(mealSales == null ? 0 : Integer.valueOf(mealSales.get("refundServiceCount").toString()));
                    mealSellDto.setRefundTotal(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("refundServiceSalles").toString()));
                    mealSellList.add(mealSellDto);
                } else if (mealSales != null && mealSales.get("serviceSellNum") != null && Integer.valueOf(mealSales.get("serviceSellNum").toString()) != 0) {
                    mealSellDto = new ArticleSellDto();
                    //如店铺没有开启服务费但有服务费售卖记录产生也显示出来
                    mealSellDto.setArticleName(shopDetail.getServiceName());
                    mealSellDto.setBrandSellNum(Integer.valueOf(mealSales.get("serviceSellNum").toString()));
                    mealSellDto.setSalles(new BigDecimal(mealSales.get("serviceSalles").toString()));
                    mealSellDto.setRefundCount(Integer.valueOf(mealSales.get("refundServiceCount").toString()));
                    mealSellDto.setRefundTotal(new BigDecimal(mealSales.get("refundServiceSalles").toString()));
                    mealSellList.add(mealSellDto);
                }
                mealSellDto = new ArticleSellDto();
            }
            object.put("brandMealSalesDtos", mealSellList);
        }catch (Exception e){
            log.error("查询菜品销售报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(object);
    }

    /**
     * 进入套餐子项详情页面
     * @param request
     * @param articleId
     * @param beginDate
     * @param endDate
     * @return
     */
    @RequestMapping("/showMealAttr")
    public String showMealAttr(HttpServletRequest request, String articleId, String beginDate, String endDate){
        request.setAttribute("articleId", articleId);
        request.setAttribute("beginDate", beginDate);
        request.setAttribute("endDate", endDate);
        return "articleSell/mealAttr";
    }

    /**
     * 进入店铺菜品销售表详情页面
     * @param request
     * @param beginDate
     * @param endDate
     * @param shopId
     * @return
     */
    @RequestMapping("/showShopArticle")
    public String showShopArticle(HttpServletRequest request, String beginDate, String endDate, String shopId, String shopName){
        request.setAttribute("beginDate", beginDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("shopId", shopId);
        request.setAttribute("shopName",shopName);
        return "articleSell/shopArticle";
    }

    /**
     * 进入店铺菜品销售表详情页面
     * @param request
     * @param beginDate
     * @param endDate
     * @param shopId
     * @return
     */
    @RequestMapping("/showShopArticleNew")
    public String showShopArticleNew(HttpServletRequest request, String beginDate, String endDate, String shopId, String shopName){
        request.setAttribute("beginDate", beginDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("shopId", shopId);
        request.setAttribute("shopName",shopName);
        return "articleSellNew/shopArticle";
    }

    /**
     * 查询套餐子项销量
     * @param articleId
     * @param beginDate
     * @param endDate
     * @return
     */
    @RequestMapping("/queryArticleMealAttr")
    @ResponseBody
    public Result queryArticleMealAttr(String articleId, String beginDate, String endDate){
        Map<String, Object> selectMap = new HashMap<String, Object>();
        selectMap.put("articleId", articleId);
        selectMap.put("beginDate", beginDate);
        selectMap.put("endDate", endDate);
        List<ArticleSellDto> articleSellDtos = mealAttrService.queryArticleMealAttr(selectMap);
        return getSuccessResult(articleSellDtos);
    }

    /**
     * 生成品牌菜品销售表(单品/套餐)
     */
    @RequestMapping("/createBrnadArticle")
    @ResponseBody
    public Result createBrnadArticle(HttpServletRequest request, ArticleSellDto articleSellDto,
                                     String beginDate, String endDate, Integer type){
        //导出文件名
        String fileName = null;
        //定义读取文件的路径
        String path = null;
        Brand brand = brandServie.selectById(getCurrentBrandId());//定义列
        String[] columns = {"typeName","articleFamilyName","articleName","brandSellNum","numRatio","salles","discountMoney","salesRatio","refundCount","refundTotal","likes"};
        String[][] headers = {{"品牌名称/菜品类型","25"},{"菜品类别","25"},{"菜品名称","25"},{"销量(份)","25"},{"销量占比","25"},{"销售额(元)","25"},{"折扣金额(元)","25"},{"销售额占比","25"},{"退菜数量","25"},{"退菜金额","25"},{"点赞数量","25"}};
        String[] columnsType = {"articleFamilyName","brandSellNum","numRatio","salles","discountMoney","salesRatio","refundCount","refundTotal","likes"};
        String[][] headersType = {{"品牌名称/菜品类别","25"},{"销量(份)","25"},{"销量占比","25"},{"销售额(元)","25"},{"折扣金额(元)","25"},{"销售额占比","25"},{"退菜数量","25"},{"退菜金额","25"},{"点赞数量","25"}};
        String[] columnsMeal = {"articleName","brandSellNum","salles","refundCount","refundTotal"};
        String[][] headersMeal = {{"品牌名称/菜品名称","25"},{"销量(份)","25"},{"销售额(元)","25"},{"退菜数量(份)","25"},{"退菜金额(元)","25"}};
        //获取店铺名称
        List<ShopDetail> shops = shopDetailService.selectByBrandId(getCurrentBrandId());
        String shopName="";
        for (ShopDetail shopDetail : shops) {
            shopName += shopDetail.getName()+",";
        }
        //去掉最后一个逗号
        shopName = shopName.substring(0, shopName.length()-1);
        //定义数据
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("brandReport");
        filter.getExcludes().add("brandArticleUnit");
        filter.getExcludes().add("brandArticleFamily");
        filter.getExcludes().add("shopArticleUnit");
        filter.getExcludes().add("shopArticleFamily");
        filter.getExcludes().add("brandArticleType");
        filter.getExcludes().add("shopArticleType");
        filter.getExcludes().add("shopMealSeals");
        filter.getExcludes().add("brandMealSeals");
        List<ArticleSellDto> result = new ArrayList<ArticleSellDto>();
        if (articleSellDto.getBrandReport() != null) {
            ArticleSellDto brandReport = new ArticleSellDto();
            Map<String, Object> brandReportMap = articleSellDto.getBrandReport();
            if (type.equals(ArticleType.SIMPLE_ARTICLE) || type.equals(ArticleType.TOTAL_ARTICLE)) {
                brandReport.setTypeName(brandReportMap.get("brandName").toString());
            }else if (type.equals(3)){
                brandReport.setArticleFamilyName(brandReportMap.get("brandName").toString());
            }else {
                brandReport.setArticleName(brandReportMap.get("brandName").toString());
            }
            brandReport.setBrandSellNum(Integer.valueOf(brandReportMap.get("totalNum").toString()));
            brandReport.setSalles(new BigDecimal(brandReportMap.get("sellIncome").toString()));
            brandReport.setRefundCount(Integer.valueOf(brandReportMap.get("refundCount").toString()));
            brandReport.setRefundTotal(new BigDecimal(brandReportMap.get("refundTotal").toString()));
            brandReport.setDiscountMoney(new BigDecimal(brandReportMap.get("discountTotal").toString()));
            result.add(brandReport);
        }
        Map<String,String> map = new HashMap<>();
        //定义一个map用来存数据表格的前四项,1.报表类型,2.品牌名称3,.店铺名称4.日期
        map.put("brandName", brand.getBrandName());
        map.put("shops", shopName);
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        if (type.equals(ArticleType.SIMPLE_ARTICLE) || type.equals(ArticleType.TOTAL_ARTICLE)) {
            map.put("num", "10");//显示的位置
        }else if (type.equals(3)){
            map.put("num","8");
        }else {
            map.put("num","4");
        }
        map.put("timeType", "yyyy-MM-dd");
        //如果是单品
        if(type.equals(ArticleType.SIMPLE_ARTICLE)){
            //导出文件名
            fileName = "品牌菜品销售报表(单品)"+beginDate+"至"+endDate+".xls";
            path = request.getSession().getServletContext().getRealPath(fileName);
            map.put("reportType", "品牌菜品销售报表(单品)");//表的头，第一行内容
            map.put("reportTitle", "品牌菜品销售报表(单品)");//表的名字
            //定义数据
            if(articleSellDto.getBrandArticleUnit() != null) {
                String json = JSON.toJSONString(articleSellDto.getBrandArticleUnit(), filter);
                List<ArticleSellDto> articleSellDtos = JSON.parseObject(json, new TypeReference<List<ArticleSellDto>>(){});
                result.addAll(articleSellDtos);
            }
        }else if(type.equals(ArticleType.TOTAL_ARTICLE)){
            //导出文件名
            fileName = "品牌菜品销售报表(套餐)"+beginDate+"至"+endDate+".xls";
            path = request.getSession().getServletContext().getRealPath(fileName);
            map.put("reportType", "品牌菜品销售报表(套餐)");//表的头，第一行内容
            map.put("reportTitle", "品牌菜品销售报表(套餐)");//表的名字
            //定义数据
            if(articleSellDto.getBrandArticleFamily() != null) {
                String json = JSON.toJSONString(articleSellDto.getBrandArticleFamily(), filter);
                List<ArticleSellDto> articleSellDtos = JSON.parseObject(json, new TypeReference<List<ArticleSellDto>>(){});
                result.addAll(articleSellDtos);
            }
        }else if (type.equals(3)){
            //导出文件名
            fileName = "品牌菜品销售报表(类别)"+beginDate+"至"+endDate+".xls";
            path = request.getSession().getServletContext().getRealPath(fileName);
            map.put("reportType", "品牌菜品销售报表(类别)");//表的头，第一行内容
            map.put("reportTitle", "品牌菜品销售报表(类别)");//表的名字
            //定义数据
            if (articleSellDto.getBrandArticleType() != null){
                String json = JSON.toJSONString(articleSellDto.getBrandArticleType(), filter);
                List<ArticleSellDto> articleSellDtos = JSON.parseObject(json, new TypeReference<List<ArticleSellDto>>(){});
                result.addAll(articleSellDtos);
            }
        }else {
            //导出文件名
            fileName = "品牌菜品销售报表(其他销量)"+beginDate+"至"+endDate+".xls";
            path = request.getSession().getServletContext().getRealPath(fileName);
            map.put("reportType", "品牌菜品销售报表(其他销量)");//表的头，第一行内容
            map.put("reportTitle", "品牌菜品销售报表(其他销量)");//表的名字
            //定义数据
            if (articleSellDto.getBrandMealSeals() != null){
                String json = JSON.toJSONString(articleSellDto.getBrandMealSeals(), filter);
                List<ArticleSellDto> articleSellDtos = JSON.parseObject(json, new TypeReference<List<ArticleSellDto>>(){});
                result.addAll(articleSellDtos);
            }
        }
        //定义excel工具类对象
        ExcelUtil<ArticleSellDto> excelUtil=new ExcelUtil<ArticleSellDto>();
        try{
            OutputStream out = new FileOutputStream(path);
            if (type.equals(ArticleType.SIMPLE_ARTICLE) || type.equals(ArticleType.TOTAL_ARTICLE)) {
                excelUtil.ExportExcel(headers, columns, result, out, map);
            }else if (type.equals(3)){
                excelUtil.ExportExcel(headersType, columnsType, result, out, map);
            }else {
                excelUtil.ExportExcel(headersMeal, columnsMeal, result, out, map);
            }
            out.close();
        }catch(Exception e){
            log.error("生成菜品销售报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(path);
    }

    /**
     * 向报表追加记录
     * @param path
     * @param startPosition
     * @param type
     * @param articleSellDto
     * @return
     */
    @RequestMapping("/appendToBrandExcel")
    @ResponseBody
    public Result appendToBrandExcel(String path, Integer startPosition, Integer type, ArticleSellDto articleSellDto){
        try{
            String[][] items = null;
            int i = 0;
            //如果是单品
            if(type.equals(ArticleType.SIMPLE_ARTICLE)){
                items = new String[articleSellDto.getBrandArticleUnit().size()][];
                //定义数据
                for(Map articleMap : articleSellDto.getBrandArticleUnit()){
                    items[i] = new String[11];
                    items[i][0] = articleMap.get("typeName").toString();
                    items[i][1] = articleMap.get("articleFamilyName").toString();
                    items[i][2] = articleMap.get("articleName").toString();
                    items[i][3] = articleMap.get("brandSellNum").toString();
                    items[i][4] = articleMap.get("numRatio").toString();
                    items[i][5] = articleMap.get("salles").toString();
                    items[i][6] = articleMap.get("salesRatio").toString();
                    items[i][7] = articleMap.get("discountMoney").toString();
                    items[i][8] = articleMap.get("refundCount").toString();
                    items[i][9] = articleMap.get("refundTotal").toString();
                    items[i][10] = articleMap.get("likes").toString();
                    i++;
                }
            }else if(type.equals(ArticleType.TOTAL_ARTICLE)){
                items = new String[articleSellDto.getBrandArticleFamily().size()][];
                //定义数据
                for(Map articleMap : articleSellDto.getBrandArticleFamily()){
                    items[i] = new String[11];
                    items[i][0] = articleMap.get("typeName").toString();
                    items[i][1] = articleMap.get("articleFamilyName").toString();
                    items[i][2] = articleMap.get("articleName").toString();
                    items[i][3] = articleMap.get("brandSellNum").toString();
                    items[i][4] = articleMap.get("numRatio").toString();
                    items[i][5] = articleMap.get("salles").toString();
                    items[i][6] = articleMap.get("salesRatio").toString();
                    items[i][7] = articleMap.get("discountMoney").toString();
                    items[i][8] = articleMap.get("refundCount").toString();
                    items[i][9] = articleMap.get("refundTotal").toString();
                    items[i][10] = articleMap.get("likes").toString();
                    i++;
                }
            }
            AppendToExcelUtil.insertRows(path,startPosition,items);
        }catch (Exception e){
            log.error("追加菜品销售报表出粗！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult();
    }

    /**
     * 下载品牌菜品销售表
     */
    @RequestMapping("/downloadBrnadArticle")
    @ResponseBody
    public void downloadBrnadArticle( HttpServletResponse response,String path){
        //定义excel工具类对象
        ExcelUtil<Object> excelUtil=new ExcelUtil<>();
        try{
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 查询当前店铺下的菜品销售详情
     * @param beginDate
     * @param endDate
     * @param shopId
     * @return
     */
    @RequestMapping("/queryShopOrderArtcile")
    @ResponseBody
    public Result queryShopOrderArtcile(String beginDate, String endDate, String shopId){
        JSONObject object = new JSONObject();
        try {
            Map<String, Object> selectMap = new HashMap<String, Object>();
            selectMap.put("beginDate", beginDate);
            selectMap.put("endDate", endDate);
            selectMap.put("type", ArticleType.SIMPLE_ARTICLE);
            selectMap.put("shopDetailId", shopId);
            List<ArticleSellDto> shopArticleType = articleFamilyService.selectByShopId(shopId);
            BigDecimal shopSellNum = new BigDecimal(0);
            BigDecimal salles = new BigDecimal(0);
            List<ArticleSellDto> articleUnitSellDtos = articleService.callOrderArtcile(selectMap);
            List<ArticleSellDto> articleUnitSell = articleService.selectArticleByType(selectMap);
            selectMap.put("type", OrderItemType.ARTICLE + "," + OrderItemType.UNITPRICE + "," + OrderItemType.UNIT_NEW + "," + OrderItemType.RECOMMEND);
            Map<String, Object> unitMap = articleService.callArticleOrderCount(selectMap);
            for (ArticleSellDto articleUnitSellDto : articleUnitSellDtos) {
                if (unitMap.get("sellNum").toString().equalsIgnoreCase("0") || Double.parseDouble(unitMap.get("sellNum").toString()) == 0) {
                    articleUnitSellDto.setNumRatio("0.00%");
                } else {
                    articleUnitSellDto.setNumRatio(new BigDecimal(articleUnitSellDto.getShopSellNum()).divide(new BigDecimal(
                            unitMap.get("sellNum").toString()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
                }
                if (unitMap.get("salles").toString().equalsIgnoreCase("0") || Double.parseDouble(unitMap.get("salles").toString()) == 0) {
                    articleUnitSellDto.setSalesRatio("0.00%");
                } else {
                    articleUnitSellDto.setSalesRatio(articleUnitSellDto.getSalles().divide(new BigDecimal(
                            unitMap.get("salles").toString()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
                }
                for (ArticleSellDto articleSellDto : articleUnitSell) {
                    if (articleSellDto.getArticleId().equalsIgnoreCase(articleUnitSellDto.getArticleId())) {
                        articleUnitSell.remove(articleSellDto);
                        break;
                    }
                }
                for (ArticleSellDto articleSellDto : shopArticleType){
                    if (articleSellDto.getArticleFamilyId().equalsIgnoreCase(articleUnitSellDto.getArticleFamilyId())){
                        articleSellDto.setShopSellNum(new BigDecimal(articleSellDto.getShopSellNum()).add(new BigDecimal(articleUnitSellDto.getShopSellNum())).intValue());
                        shopSellNum = shopSellNum.add(new BigDecimal(articleUnitSellDto.getShopSellNum()));
                        articleSellDto.setSalles(articleSellDto.getSalles().add(articleUnitSellDto.getSalles()));
                        salles = salles.add(articleUnitSellDto.getSalles());
                        articleSellDto.setDiscountMoney(articleSellDto.getDiscountMoney().add(articleUnitSellDto.getDiscountMoney()));
                        articleSellDto.setRefundCount(new BigDecimal(articleSellDto.getRefundCount()).add(new BigDecimal(articleUnitSellDto.getRefundCount())).intValue());
                        articleSellDto.setRefundTotal(articleSellDto.getRefundTotal().add(articleUnitSellDto.getRefundTotal()));
                        articleSellDto.setLikes(new BigDecimal(articleSellDto.getLikes()).add(new BigDecimal(articleUnitSellDto.getLikes())).intValue());
                        articleUnitSellDto.setArticleFamilyName(articleSellDto.getArticleFamilyName());
                        break;
                    }
                }
            }
            for (ArticleSellDto articleSellDto : articleUnitSell) {
                articleSellDto.setShopSellNum(0);
                articleSellDto.setNumRatio("0.00%");
                articleSellDto.setSalles(BigDecimal.ZERO);
                articleSellDto.setSalesRatio("0.00%");
                articleSellDto.setDiscountMoney(BigDecimal.ZERO);
                articleSellDto.setRefundCount(0);
                articleSellDto.setRefundTotal(BigDecimal.ZERO);
                articleUnitSellDtos.add(articleSellDto);
            }
            object.put("shopArticleUnitDtos", articleUnitSellDtos);
            selectMap.put("type", ArticleType.TOTAL_ARTICLE);
            List<ArticleSellDto> articleFamilySellDtos = articleService.callOrderArtcile(selectMap);
            List<ArticleSellDto> articleFamilySell = articleService.selectArticleByType(selectMap);
            selectMap.put("type", OrderItemType.SETMEALS + "," + OrderItemType.MEALS_CHILDREN );
            Map<String, Object> familyMap = articleService.callArticleOrderCount(selectMap);
            for (ArticleSellDto articleFamilySellDto : articleFamilySellDtos) {
                if (familyMap.get("sellNum").toString().equalsIgnoreCase("0") || Double.parseDouble(familyMap.get("sellNum").toString()) == 0) {
                    articleFamilySellDto.setNumRatio("0.00%");
                } else {
                    articleFamilySellDto.setNumRatio(new BigDecimal(articleFamilySellDto.getShopSellNum()).divide(new BigDecimal(
                            familyMap.get("sellNum").toString()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
                }
                if (familyMap.get("salles").toString().equalsIgnoreCase("0") || Double.parseDouble(familyMap.get("salles").toString()) == 0) {
                    articleFamilySellDto.setSalesRatio("0.00%");
                } else {
                    articleFamilySellDto.setSalesRatio(articleFamilySellDto.getSalles().divide(new BigDecimal(
                            familyMap.get("salles").toString()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
                }
                for (ArticleSellDto articleSellDto : articleFamilySell) {
                    if (articleSellDto.getArticleId().equalsIgnoreCase(articleFamilySellDto.getArticleId())) {
                        articleFamilySell.remove(articleSellDto);
                        break;
                    }
                }
                for (ArticleSellDto articleSellDto : shopArticleType){
                    if (articleSellDto.getArticleFamilyId().equalsIgnoreCase(articleFamilySellDto.getArticleFamilyId())){
                        articleSellDto.setShopSellNum(new BigDecimal(articleSellDto.getShopSellNum()).add(new BigDecimal(articleFamilySellDto.getShopSellNum())).intValue());
                        shopSellNum = shopSellNum.add(new BigDecimal(articleFamilySellDto.getShopSellNum()));
                        articleSellDto.setSalles(articleSellDto.getSalles().add(articleFamilySellDto.getSalles()));
                        salles = salles.add(articleFamilySellDto.getSalles());
                        articleSellDto.setDiscountMoney(articleSellDto.getDiscountMoney().add(articleFamilySellDto.getDiscountMoney()));
                        articleSellDto.setRefundCount(new BigDecimal(articleSellDto.getRefundCount()).add(new BigDecimal(articleFamilySellDto.getRefundCount())).intValue());
                        articleSellDto.setRefundTotal(articleSellDto.getRefundTotal().add(articleFamilySellDto.getRefundTotal()));
                        articleSellDto.setLikes(new BigDecimal(articleSellDto.getLikes()).add(new BigDecimal(articleFamilySellDto.getLikes())).intValue());
                        articleFamilySellDto.setArticleFamilyName(articleSellDto.getArticleFamilyName());
                        break;
                    }
                }
            }
            for (ArticleSellDto articleSellDto : articleFamilySell) {
                articleSellDto.setShopSellNum(0);
                articleSellDto.setNumRatio("0.00%");
                articleSellDto.setSalles(BigDecimal.ZERO);
                articleSellDto.setSalesRatio("0.00%");
                articleSellDto.setDiscountMoney(BigDecimal.ZERO);
                articleSellDto.setRefundCount(0);
                articleSellDto.setRefundTotal(BigDecimal.ZERO);
                articleFamilySellDtos.add(articleSellDto);
            }
            object.put("shopArticleFamilyDtos", articleFamilySellDtos);
            for (ArticleSellDto articleSellDto : shopArticleType){
                if (shopSellNum.equals(BigDecimal.ZERO) || shopSellNum.intValue() == 0){
                    articleSellDto.setNumRatio("0.00%");
                }else{
                    articleSellDto.setNumRatio(new BigDecimal(articleSellDto.getShopSellNum()).divide(shopSellNum,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString().concat("%"));
                }
                if (salles.equals(BigDecimal.ZERO) || salles.intValue() == 0){
                    articleSellDto.setSalesRatio("0.00%");
                }else{
                    articleSellDto.setSalesRatio(articleSellDto.getSalles().divide(salles,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString().concat("%"));
                }
            }
            object.put("shopArticleTypeDtos",shopArticleType);
            ShopDetail shopDetail = shopDetailService.selectById(shopId);
            ArticleSellDto mealSellDto = new ArticleSellDto();
            List<Map<String, Object>> mealSalesList = orderService.selectMealServiceSales(selectMap);
            List<ArticleSellDto> mealSellList = new ArrayList<>();
            for (Map mealSales : mealSalesList) {
                //判断店铺是否开启餐盒费，如开启了无论产没产生都要显示出来
                if ((StringUtils.isNotBlank(shopDetail.getMealFeeName()) && shopDetail.getIsMealFee().equals(Common.YES))) {
                    mealSellDto.setArticleName(shopDetail.getMealFeeName());
                    mealSellDto.setShopSellNum(mealSales == null ? 0 : Integer.valueOf(mealSales.get("mealSellNum").toString()));
                    mealSellDto.setSalles(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("mealSalles").toString()));
                    mealSellDto.setRefundCount(mealSales == null ? 0 : Integer.valueOf(mealSales.get("refundMealNum").toString()));
                    mealSellDto.setRefundTotal(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("refundMealSalles").toString()));
                    mealSellList.add(mealSellDto);
                } else if (mealSales != null && mealSales.get("mealSellNum") != null && Integer.valueOf(mealSales.get("mealSellNum").toString()) != 0) {
                    //如店铺没有开启餐盒费但有餐盒售卖记录产生也显示出来
                    mealSellDto.setArticleName(shopDetail.getMealFeeName());
                    mealSellDto.setShopSellNum(Integer.valueOf(mealSales.get("mealSellNum").toString()));
                    mealSellDto.setSalles(new BigDecimal(mealSales.get("mealSalles").toString()));
                    mealSellDto.setRefundCount(Integer.valueOf(mealSales.get("refundMealNum").toString()));
                    mealSellDto.setRefundTotal(new BigDecimal(mealSales.get("refundMealSalles").toString()));
                    mealSellList.add(mealSellDto);
                }
                //判断店铺是否开启服务费，如开启了无论产没产生都要显示出来
                if ((StringUtils.isNotBlank(shopDetail.getServiceName()) && shopDetail.getIsUseServicePrice().equals(Common.YES))) {
                    mealSellDto = new ArticleSellDto();
                    mealSellDto.setArticleName(shopDetail.getServiceName());
                    mealSellDto.setShopSellNum(mealSales == null ? 0 : Integer.valueOf(mealSales.get("serviceSellNum").toString()));
                    mealSellDto.setSalles(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("serviceSalles").toString()));
                    mealSellDto.setRefundCount(mealSales == null ? 0 : Integer.valueOf(mealSales.get("refundServiceCount").toString()));
                    mealSellDto.setRefundTotal(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("refundServiceSalles").toString()));
                    mealSellList.add(mealSellDto);
                } else if (mealSales != null && mealSales.get("serviceSellNum") != null && Integer.valueOf(mealSales.get("serviceSellNum").toString()) != 0) {
                    mealSellDto = new ArticleSellDto();
                    //如店铺没有开启服务费但有服务费售卖记录产生也显示出来
                    mealSellDto.setArticleName(shopDetail.getServiceName());
                    mealSellDto.setShopSellNum(Integer.valueOf(mealSales.get("serviceSellNum").toString()));
                    mealSellDto.setSalles(new BigDecimal(mealSales.get("serviceSalles").toString()));
                    mealSellDto.setRefundCount(Integer.valueOf(mealSales.get("refundServiceCount").toString()));
                    mealSellDto.setRefundTotal(new BigDecimal(mealSales.get("refundServiceSalles").toString()));
                    mealSellList.add(mealSellDto);
                }
                mealSellDto = new ArticleSellDto();
            }
            object.put("shopMealSalesDtos", mealSellList);
        }catch (Exception e){
            log.error("查询店铺菜品销售报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(object);
    }

    /**
     * 查询菜品库当前店铺下的菜品销售详情
     * @param beginDate
     * @param endDate
     * @param shopId
     * @return
     */
    @RequestMapping("/queryShopOrderArtcileNew")
    @ResponseBody
    public Result queryShopOrderArtcileNew(String beginDate, String endDate, String shopId,String menuId){
        JSONObject object = new JSONObject();
        try {
            Map<String, Object> selectMap = new HashMap<String, Object>();
            selectMap.put("beginDate", beginDate);
            selectMap.put("endDate", endDate);
            selectMap.put("type", ArticleType.SIMPLE_ARTICLE);
            selectMap.put("shopDetailId", shopId);
            selectMap.put("menuId", menuId);
            List<ArticleSellDto> shopArticleType = articleFamilyService.selectByShopIdNew(shopId);
            BigDecimal shopSellNum = new BigDecimal(0);
            BigDecimal salles = new BigDecimal(0);
            List<ArticleSellDto> articleUnitSellDtos = articleService.callOrderArtcileNew(selectMap);
            List<ArticleSellDto> articleUnitSell = articleService.selectArticleByTypeNew(selectMap);
            selectMap.put("type", OrderItemType.ARTICLE + "," + OrderItemType.UNITPRICE + "," + OrderItemType.UNIT_NEW + "," + OrderItemType.RECOMMEND);
            Map<String, Object> unitMap = articleService.callArticleOrderCountNew(selectMap);
            for (ArticleSellDto articleUnitSellDto : articleUnitSellDtos) {
                if (unitMap.get("sellNum").toString().equalsIgnoreCase("0") || Double.parseDouble(unitMap.get("sellNum").toString()) == 0) {
                    articleUnitSellDto.setNumRatio("0.00%");
                } else {
                    articleUnitSellDto.setNumRatio(new BigDecimal(articleUnitSellDto.getShopSellNum()).divide(new BigDecimal(
                            unitMap.get("sellNum").toString()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
                }
                if (unitMap.get("salles").toString().equalsIgnoreCase("0") || Double.parseDouble(unitMap.get("salles").toString()) == 0) {
                    articleUnitSellDto.setSalesRatio("0.00%");
                } else {
                    articleUnitSellDto.setSalesRatio(articleUnitSellDto.getSalles().divide(new BigDecimal(
                            unitMap.get("salles").toString()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
                }
                for (ArticleSellDto articleSellDto : articleUnitSell) {
                    if (articleSellDto.getArticleId().equalsIgnoreCase(articleUnitSellDto.getArticleId())) {
                        articleUnitSell.remove(articleSellDto);
                        break;
                    }
                }
                for (ArticleSellDto articleSellDto : shopArticleType){
                    if (articleSellDto.getArticleFamilyId().equalsIgnoreCase(articleUnitSellDto.getArticleFamilyId())){
                        articleSellDto.setShopSellNum(new BigDecimal(articleSellDto.getShopSellNum()).add(new BigDecimal(articleUnitSellDto.getShopSellNum())).intValue());
                        shopSellNum = shopSellNum.add(new BigDecimal(articleUnitSellDto.getShopSellNum()));
                        articleSellDto.setSalles(articleSellDto.getSalles().add(articleUnitSellDto.getSalles()));
                        salles = salles.add(articleUnitSellDto.getSalles());
                        articleSellDto.setDiscountMoney(articleSellDto.getDiscountMoney().add(articleUnitSellDto.getDiscountMoney()));
                        articleSellDto.setRefundCount(new BigDecimal(articleSellDto.getRefundCount()).add(new BigDecimal(articleUnitSellDto.getRefundCount())).intValue());
                        articleSellDto.setRefundTotal(articleSellDto.getRefundTotal().add(articleUnitSellDto.getRefundTotal()));
                        articleSellDto.setLikes(new BigDecimal(articleSellDto.getLikes()).add(new BigDecimal(articleUnitSellDto.getLikes())).intValue());
                        articleUnitSellDto.setArticleFamilyName(articleSellDto.getArticleFamilyName());
                        break;
                    }
                }
            }
            for (ArticleSellDto articleSellDto : articleUnitSell) {
                articleSellDto.setShopSellNum(0);
                articleSellDto.setNumRatio("0.00%");
                articleSellDto.setSalles(BigDecimal.ZERO);
                articleSellDto.setSalesRatio("0.00%");
                articleSellDto.setDiscountMoney(BigDecimal.ZERO);
                articleSellDto.setRefundCount(0);
                articleSellDto.setRefundTotal(BigDecimal.ZERO);
                articleUnitSellDtos.add(articleSellDto);
            }
            object.put("shopArticleUnitDtos", articleUnitSellDtos);
            selectMap.put("type", ArticleType.TOTAL_ARTICLE);
            List<ArticleSellDto> articleFamilySellDtos = articleService.callOrderArtcile(selectMap);
            List<ArticleSellDto> articleFamilySell = articleService.selectArticleByType(selectMap);
            selectMap.put("type", OrderItemType.SETMEALS + "," + OrderItemType.MEALS_CHILDREN );
            Map<String, Object> familyMap = articleService.callArticleOrderCount(selectMap);
            for (ArticleSellDto articleFamilySellDto : articleFamilySellDtos) {
                if (familyMap.get("sellNum").toString().equalsIgnoreCase("0") || Double.parseDouble(familyMap.get("sellNum").toString()) == 0) {
                    articleFamilySellDto.setNumRatio("0.00%");
                } else {
                    articleFamilySellDto.setNumRatio(new BigDecimal(articleFamilySellDto.getShopSellNum()).divide(new BigDecimal(
                            familyMap.get("sellNum").toString()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
                }
                if (familyMap.get("salles").toString().equalsIgnoreCase("0") || Double.parseDouble(familyMap.get("salles").toString()) == 0) {
                    articleFamilySellDto.setSalesRatio("0.00%");
                } else {
                    articleFamilySellDto.setSalesRatio(articleFamilySellDto.getSalles().divide(new BigDecimal(
                            familyMap.get("salles").toString()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
                }
                for (ArticleSellDto articleSellDto : articleFamilySell) {
                    if (articleSellDto.getArticleId().equalsIgnoreCase(articleFamilySellDto.getArticleId())) {
                        articleFamilySell.remove(articleSellDto);
                        break;
                    }
                }
                for (ArticleSellDto articleSellDto : shopArticleType){
                    if (articleSellDto.getArticleFamilyId().equalsIgnoreCase(articleFamilySellDto.getArticleFamilyId())){
                        articleSellDto.setShopSellNum(new BigDecimal(articleSellDto.getShopSellNum()).add(new BigDecimal(articleFamilySellDto.getShopSellNum())).intValue());
                        shopSellNum = shopSellNum.add(new BigDecimal(articleFamilySellDto.getShopSellNum()));
                        articleSellDto.setSalles(articleSellDto.getSalles().add(articleFamilySellDto.getSalles()));
                        salles = salles.add(articleFamilySellDto.getSalles());
                        articleSellDto.setDiscountMoney(articleSellDto.getDiscountMoney().add(articleFamilySellDto.getDiscountMoney()));
                        articleSellDto.setRefundCount(new BigDecimal(articleSellDto.getRefundCount()).add(new BigDecimal(articleFamilySellDto.getRefundCount())).intValue());
                        articleSellDto.setRefundTotal(articleSellDto.getRefundTotal().add(articleFamilySellDto.getRefundTotal()));
                        articleSellDto.setLikes(new BigDecimal(articleSellDto.getLikes()).add(new BigDecimal(articleFamilySellDto.getLikes())).intValue());
                        articleFamilySellDto.setArticleFamilyName(articleSellDto.getArticleFamilyName());
                        break;
                    }
                }
            }
            for (ArticleSellDto articleSellDto : articleFamilySell) {
                articleSellDto.setShopSellNum(0);
                articleSellDto.setNumRatio("0.00%");
                articleSellDto.setSalles(BigDecimal.ZERO);
                articleSellDto.setSalesRatio("0.00%");
                articleSellDto.setDiscountMoney(BigDecimal.ZERO);
                articleSellDto.setRefundCount(0);
                articleSellDto.setRefundTotal(BigDecimal.ZERO);
                articleFamilySellDtos.add(articleSellDto);
            }
            object.put("shopArticleFamilyDtos", articleFamilySellDtos);
            for (ArticleSellDto articleSellDto : shopArticleType){
                if (shopSellNum.equals(BigDecimal.ZERO) || shopSellNum.intValue() == 0){
                    articleSellDto.setNumRatio("0.00%");
                }else{
                    articleSellDto.setNumRatio(new BigDecimal(articleSellDto.getShopSellNum()).divide(shopSellNum,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString().concat("%"));
                }
                if (salles.equals(BigDecimal.ZERO) || salles.intValue() == 0){
                    articleSellDto.setSalesRatio("0.00%");
                }else{
                    articleSellDto.setSalesRatio(articleSellDto.getSalles().divide(salles,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString().concat("%"));
                }
            }
            object.put("shopArticleTypeDtos",shopArticleType);
            ShopDetail shopDetail = shopDetailService.selectById(shopId);
            ArticleSellDto mealSellDto = new ArticleSellDto();
            List<Map<String, Object>> mealSalesList = orderService.selectMealServiceSales(selectMap);
            List<ArticleSellDto> mealSellList = new ArrayList<>();
            for (Map mealSales : mealSalesList) {
                //判断店铺是否开启餐盒费，如开启了无论产没产生都要显示出来
                if ((StringUtils.isNotBlank(shopDetail.getMealFeeName()) && shopDetail.getIsMealFee().equals(Common.YES))) {
                    mealSellDto.setArticleName(shopDetail.getMealFeeName());
                    mealSellDto.setShopSellNum(mealSales == null ? 0 : Integer.valueOf(mealSales.get("mealSellNum").toString()));
                    mealSellDto.setSalles(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("mealSalles").toString()));
                    mealSellDto.setRefundCount(mealSales == null ? 0 : Integer.valueOf(mealSales.get("refundMealNum").toString()));
                    mealSellDto.setRefundTotal(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("refundMealSalles").toString()));
                    mealSellList.add(mealSellDto);
                } else if (mealSales != null && mealSales.get("mealSellNum") != null && Integer.valueOf(mealSales.get("mealSellNum").toString()) != 0) {
                    //如店铺没有开启餐盒费但有餐盒售卖记录产生也显示出来
                    mealSellDto.setArticleName(shopDetail.getMealFeeName());
                    mealSellDto.setShopSellNum(Integer.valueOf(mealSales.get("mealSellNum").toString()));
                    mealSellDto.setSalles(new BigDecimal(mealSales.get("mealSalles").toString()));
                    mealSellDto.setRefundCount(Integer.valueOf(mealSales.get("refundMealNum").toString()));
                    mealSellDto.setRefundTotal(new BigDecimal(mealSales.get("refundMealSalles").toString()));
                    mealSellList.add(mealSellDto);
                }
                //判断店铺是否开启服务费，如开启了无论产没产生都要显示出来
                if ((StringUtils.isNotBlank(shopDetail.getServiceName()) && shopDetail.getIsUseServicePrice().equals(Common.YES))) {
                    mealSellDto = new ArticleSellDto();
                    mealSellDto.setArticleName(shopDetail.getServiceName());
                    mealSellDto.setShopSellNum(mealSales == null ? 0 : Integer.valueOf(mealSales.get("serviceSellNum").toString()));
                    mealSellDto.setSalles(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("serviceSalles").toString()));
                    mealSellDto.setRefundCount(mealSales == null ? 0 : Integer.valueOf(mealSales.get("refundServiceCount").toString()));
                    mealSellDto.setRefundTotal(mealSales == null ? BigDecimal.ZERO : new BigDecimal(mealSales.get("refundServiceSalles").toString()));
                    mealSellList.add(mealSellDto);
                } else if (mealSales != null && mealSales.get("serviceSellNum") != null && Integer.valueOf(mealSales.get("serviceSellNum").toString()) != 0) {
                    mealSellDto = new ArticleSellDto();
                    //如店铺没有开启服务费但有服务费售卖记录产生也显示出来
                    mealSellDto.setArticleName(shopDetail.getServiceName());
                    mealSellDto.setShopSellNum(Integer.valueOf(mealSales.get("serviceSellNum").toString()));
                    mealSellDto.setSalles(new BigDecimal(mealSales.get("serviceSalles").toString()));
                    mealSellDto.setRefundCount(Integer.valueOf(mealSales.get("refundServiceCount").toString()));
                    mealSellDto.setRefundTotal(new BigDecimal(mealSales.get("refundServiceSalles").toString()));
                    mealSellList.add(mealSellDto);
                }
                mealSellDto = new ArticleSellDto();
            }
            object.put("shopMealSalesDtos", mealSellList);
        }catch (Exception e){
            log.error("查询店铺菜品销售报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(object);
    }

    /**
     * 生成店铺菜品销售表(单品/套餐)
     */
    @RequestMapping("/createShopArticle")
    @ResponseBody
    public Result createShopArticle(HttpServletRequest request, ArticleSellDto articleSellDto,
                                    String beginDate, String endDate, Integer type, String shopId){
        //导出文件名
        String fileName = null;
        //定义读取文件的路径
        String path = null;
        //定义列
        String[]columns={"typeName","articleFamilyName","articleName","shopSellNum","numRatio","salles","discountMoney","salesRatio","refundCount","refundTotal","likes"};
        String[][] headers = {{"菜品类型","25"},{"菜品类别","25"},{"菜品名称","25"},{"销量(份)","25"},{"销量占比","25"},{"销售额(元)","25"},{"折扣金额(元)","25"},{"销售额占比","25"},{"退菜数量","25"},{"退菜金额","25"},{"点赞数量","25"}};
        String[]columnsType={"articleFamilyName","shopSellNum","numRatio","salles","discountMoney","salesRatio","refundCount","refundTotal","likes"};
        String[][] headersType = {{"菜品类别","25"},{"销量(份)","25"},{"销量占比","25"},{"销售额(元)","25"},{"折扣金额(元)","25"},{"销售额占比","25"},{"退菜数量","25"},{"退菜金额","25"},{"点赞数量","25"}};
        String[]columnsMeals={"articleName","shopSellNum","salles","refundCount","refundTotal"};
        String[][] headersMeals = {{"菜品名称","25"},{"销量(份)","25"},{"销售额(元)","25"},{"退菜数量(份)","25"},{"退菜金额(元)","25"}};
        String shopName= shopDetailService.selectById(shopId).getName();
        //定义数据
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("brandReport");
        filter.getExcludes().add("brandArticleUnit");
        filter.getExcludes().add("brandArticleFamily");
        filter.getExcludes().add("shopArticleUnit");
        filter.getExcludes().add("shopArticleFamily");
        filter.getExcludes().add("brandArticleType");
        filter.getExcludes().add("shopArticleType");
        filter.getExcludes().add("shopMealSeals");
        filter.getExcludes().add("brandMealSeals");
        List<ArticleSellDto> result = new ArrayList<ArticleSellDto>();
        Map<String,String> map = new HashMap<>();
        //定义一个map用来存数据表格的前四项,1.报表类型,2.品牌名称3,.店铺名称4.日期
        map.put("brandName", getBrandName());
        map.put("shops", shopName);
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        if (type.equals(ArticleType.SIMPLE_ARTICLE) || type.equals(ArticleType.TOTAL_ARTICLE)) {
            map.put("num", "10");//显示的位置
        }else if (type.equals(3)){
            map.put("num","8");
        }else {
            map.put("num","4");
        }
        map.put("timeType", "yyyy-MM-dd");
        //如果是单品
        if(type.equals(ArticleType.SIMPLE_ARTICLE)){
            //导出文件名
            fileName = "店铺菜品销售报表(单品)"+beginDate+"至"+endDate+".xls";
            path = request.getSession().getServletContext().getRealPath(fileName);
            map.put("reportType", "店铺菜品销售报表(单品)");//表的头，第一行内容
            map.put("reportTitle", "店铺菜品销售报表(单品)");//表的名字
            //定义数据
            if (articleSellDto.getShopArticleUnit() != null){
                String json = JSON.toJSONString(articleSellDto.getShopArticleUnit(), filter);
                result = JSON.parseObject(json, new TypeReference<List<ArticleSellDto>>(){});
            }
        }else if(type.equals(ArticleType.TOTAL_ARTICLE)){
            //导出文件名
            fileName = "店铺菜品销售报表(套餐)"+beginDate+"至"+endDate+".xls";
            path = request.getSession().getServletContext().getRealPath(fileName);
            map.put("reportType", "店铺菜品销售报表(套餐)");//表的头，第一行内容
            map.put("reportTitle", "店铺菜品销售报表(套餐)");//表的名字
            //定义数据
            if (articleSellDto.getShopArticleFamily() != null){
                String json = JSON.toJSONString(articleSellDto.getShopArticleFamily(), filter);
                result = JSON.parseObject(json, new TypeReference<List<ArticleSellDto>>(){});
            }
        }else if(type.equals(3)){
            //导出文件名
            fileName = "店铺菜品销售报表(类别)"+beginDate+"至"+endDate+".xls";
            path = request.getSession().getServletContext().getRealPath(fileName);
            map.put("reportType", "店铺菜品销售报表(类别)");//表的头，第一行内容
            map.put("reportTitle", "店铺菜品销售报表(类别)");//表的名字
            //定义数据
            if (articleSellDto.getShopArticleType() != null){
                String json = JSON.toJSONString(articleSellDto.getShopArticleType(), filter);
                result = JSON.parseObject(json, new TypeReference<List<ArticleSellDto>>(){});
            }
        }else{
            //导出文件名
            fileName = "店铺菜品销售报表(其他销量)"+beginDate+"至"+endDate+".xls";
            path = request.getSession().getServletContext().getRealPath(fileName);
            map.put("reportType", "店铺菜品销售报表(其他销量)");//表的头，第一行内容
            map.put("reportTitle", "店铺菜品销售报表(其他销量)");//表的名字
            //定义数据
            if (articleSellDto.getShopMealSeals() != null){
                String json = JSON.toJSONString(articleSellDto.getShopMealSeals(), filter);
                result = JSON.parseObject(json, new TypeReference<List<ArticleSellDto>>(){});
            }
        }
        //定义excel工具类对象
        ExcelUtil<ArticleSellDto> excelUtil=new ExcelUtil<ArticleSellDto>();
        try{
            OutputStream out = new FileOutputStream(path);
            if (type.equals(ArticleType.SIMPLE_ARTICLE) || type.equals(ArticleType.TOTAL_ARTICLE)) {
                excelUtil.ExportExcel(headers, columns, result, out, map);
            }else if (type.equals(3)){
                excelUtil.ExportExcel(headersType, columnsType, result, out, map);
            }else {
                excelUtil.ExportExcel(headersMeals, columnsMeals, result, out, map);
            }
            out.close();
        }catch(Exception e){
            log.error("生成店铺菜品销售报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(path);
    }

    /**
     * 下载具体店铺菜品销售报表
     * @param path
     * @param response
     */
    @RequestMapping("/downloadShopArticle")
    public void downloadShopArticle(String path, HttpServletResponse response){
        ExcelUtil<ArticleSellDto> excelUtil=new ExcelUtil<ArticleSellDto>();
        try{
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "导出失败！");
            log.error("下载店铺菜品销售报表出错！");
            e.printStackTrace();
        }
    }

    /**
     * 查询店铺菜品销售报表
     * @param beginDate
     * @param endDate
     * @return
     */
    @RequestMapping("/list_shop")
    @ResponseBody
    public Result list_shop(String beginDate,String endDate){
        JSONObject object = new JSONObject();
        try {
            List<ShopArticleReportDto> list = orderService.callShopArticleDetails(beginDate, endDate, getCurrentBrandId(), getCurrentShopDetails());
            object.put("list",list);
        }catch (Exception e){
            log.error("查询店铺菜品销售报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(object);
    }

    /**
     * 查询菜品库店铺菜品销售报表
     * @param beginDate
     * @param endDate
     * @return
     */
    @RequestMapping("/list_shopMenu")
    @ResponseBody
    public Result list_shopMenu(String beginDate,String endDate){
        JSONObject object = new JSONObject();
        try {
            List<ShopArticleReportDto> list = orderService.callShopArticleDetailsNew(beginDate, endDate, getCurrentBrandId(), getCurrentShopDetails());
            object.put("list",list);
        }catch (Exception e){
            log.error("查询店铺菜品销售报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(object);
    }

    /**
     * 生成店铺销售报表
     * @param beginDate
     * @param endDate
     * @param request
     */
    @RequestMapping("/create_shop_article_excel")
    @ResponseBody
    public Result createShopArticleExcel(String beginDate,String endDate,ShopArticleReportDto shopArticleReportDto,
                                         HttpServletRequest request){
        //导出文件名
        String fileName = "店铺菜品销售记录报表"+beginDate+"至"+endDate+".xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns={"shopName","totalNum","sellIncome","occupy","refundCount","refundTotal"};
        //定义一个map用来存数据表格的前四项,1.报表类型,2.品牌名称3,.店铺名称4.日期
        Map<String,String> map = new HashMap<>();
        //获取店铺名称
        String shopName="";
        for (ShopDetail shopDetail : getCurrentShopDetails()) {
            shopName += shopDetail.getName()+",";
        }
        //去掉最后一个逗号
        shopName = shopName.substring(0, shopName.length()-1);
        map.put("brandName", getBrandName());
        map.put("shops", shopName);
        map.put("beginDate", beginDate);
        map.put("reportType", "店铺菜品销售报表");//表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "5");//显示的位置
        map.put("reportTitle", "店铺菜品销售");//表的名字
        map.put("timeType", "yyyy-MM-dd");
        //定义数据
        List<ShopArticleReportDto> result = new ArrayList<>();
        if (shopArticleReportDto.getShopArticleReportDtos() != null) {
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("shopArticleReportDtos");
            String json = JSON.toJSONString(shopArticleReportDto.getShopArticleReportDtos(), filter);
            result = JSON.parseObject(json, new TypeReference<List<ShopArticleReportDto>>() {});
        }
        String[][] headers = {{"店铺名称","25"},{"菜品销量(份)","25"},{"菜品销售额","25"},{"销售额占比","25"},{"退菜总数","25"},{"退菜金额","25"}};
        //定义excel工具类对象
        ExcelUtil<ShopArticleReportDto> excelUtil=new ExcelUtil<ShopArticleReportDto>();
        try{
            OutputStream out = new FileOutputStream(path);
            excelUtil.ExportExcel(headers, columns, result, out, map);
            out.close();
        }catch(Exception e){
            log.error("生成菜品销售报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(path);
    }

    /**
     * 下载店铺菜品销售报表
     * @param path
     * @param response
     */
    @RequestMapping("/downloadShopArticleExcel")
    public void downloadShopArticleExcel(String path, HttpServletResponse response){
        ExcelUtil<Object> excelUtil = new ExcelUtil<>();
        try{
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "导出失败！");
            log.error("下载菜品销售报表出错！");
            e.printStackTrace();
        }
    }

}
