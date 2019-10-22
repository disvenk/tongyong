package com.resto.shop.web.controller.business;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.RedisService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.NewAppraise;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.LogTemplateUtils;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by carl on 2016/12/5.
 */
@Controller
@RequestMapping("shopInfo")
public class ShopInfoController extends GenericController{

    @Resource
    ShopDetailService shopDetailService;
    @Resource
    BrandService brandService;
    @Resource
    BrandSettingService brandSettingService;
    @Autowired
    private PosService posService;

    @Autowired
    RedisService redisService;
    @RequestMapping("/list")
    public void list(){

    }
   /***
    * name:yjunay
    * 服务设置
    *
     **/

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(){
        JSONObject object = new JSONObject();
        ShopDetail shopDetail = shopDetailService.selectById(getCurrentShopId());
        if(shopDetail.getIsTurntable()==1){
           if(shopDetail.getTurntablePrintType()==0){
               shopDetail.setTurntablePrintKitchen(false);
               shopDetail.setTurntablePrintReceipt(false);
           }else if(shopDetail.getTurntablePrintType()==1){
               shopDetail.setTurntablePrintKitchen(true);
               shopDetail.setTurntablePrintReceipt(false);
           }else if(shopDetail.getTurntablePrintType()==2){
               shopDetail.setTurntablePrintKitchen(false);
               shopDetail.setTurntablePrintReceipt(true);
           }else{
               shopDetail.setTurntablePrintKitchen(true);
               shopDetail.setTurntablePrintReceipt(true);
           }
        }
        object.put("shop",shopDetail);
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        object.put("brand",brandSetting);
        return getSuccessResult(object);
    }


    /**
     * name:yjuany
     * @param shopDetail
     * @return
     */
    @RequestMapping("modify")
    @ResponseBody
    public Result modify(ShopDetail shopDetail){
        shopDetail.setId(getCurrentShopId());
        if(shopDetail.getIsUserIdentity()!=null && shopDetail.getIsUserIdentity()!=0){
//            switch (shopDetail.getConsumeConfineUnit()){
//                case 1 :
//                    shopDetail.setConsumeConfineTime(shopDetail.getConsumeConfineTime());
//                    break;
//                case 2 ://月
//                    shopDetail.setConsumeConfineTime(shopDetail.getConsumeConfineTime()*30);
//                    break;
//                case 3 :
//                    shopDetail.setConsumeConfineTime(Integer.MAX_VALUE);
//                    break;
//                default:
//                    break;
//            }
        }
        if(shopDetail.getIsTurntable()==1){
            if(shopDetail.getTurntablePrintKitchen()==null&&shopDetail.getTurntablePrintReceipt()==null){
                shopDetail.setTurntablePrintType(0);
            }else if(shopDetail.getTurntablePrintReceipt()==null&&shopDetail.getTurntablePrintKitchen()==true){
                shopDetail.setTurntablePrintType(1);
            }else if(shopDetail.getTurntablePrintKitchen()==null&&shopDetail.getTurntablePrintReceipt()==true){
                shopDetail.setTurntablePrintType(2);
            }else {
                shopDetail.setTurntablePrintType(3);
            }
        }else{
            shopDetail.setTurntablePrintType(0);
        }
        if(shopDetail.getPrintReceipt() == null){
            shopDetail.setPrintReceipt(0);
        }
        if(shopDetail.getPrintKitchen() == null){
            shopDetail.setPrintKitchen(0);
        }
        if (shopDetail.getModifyOrderPrintReceipt() == null){
            shopDetail.setModifyOrderPrintReceipt(0);
        }
        if (shopDetail.getModifyOrderPrintKitchen() == null){
            shopDetail.setModifyOrderPrintKitchen(0);
        }
        if (shopDetail.getBadAppraisePrintKitchen() == null){
            shopDetail.setBadAppraisePrintKitchen(false);
        }
        if (shopDetail.getBadAppraisePrintReceipt() == null){
            shopDetail.setBadAppraisePrintReceipt(false);
        }
        if(shopDetail.getIsOpenSms()==0){//表示是关闭日短信通知
            shopDetail.setNoticeTelephone("");
        }else  if(shopDetail.getIsOpenSms()==1){
            shopDetail.setNoticeTelephone(shopDetail.getNoticeTelephone().replace("，",","));
        }
        if (shopDetail.getWarningWechat() == null){
            shopDetail.setWarningWechat(Common.NO);
        }
        if (shopDetail.getWarningSms() == null){
            shopDetail.setWarningSms(Common.NO);
        }
        //服务费类型  0：经典版  1：升级版
        if(shopDetail.getIsOpenSauceFee() == null){ //如果关闭了餐具费
            shopDetail.setIsOpenSauceFee(0);
        }
        if(shopDetail.getIsOpenTablewareFee() == null){ //如果关闭了纸巾费
            shopDetail.setIsOpenTablewareFee(0);
        }
        if(shopDetail.getIsOpenTowelFee() == null){ //如果关闭了酱料费
            shopDetail.setIsOpenTowelFee(0);
        }
       if (Common.YES.equals(shopDetail.getServiceType())){ //如果是新版服务费
           BigDecimal servicePrice = BigDecimal.ZERO;
           if (Common.YES.equals(shopDetail.getIsOpenTablewareFee())){ //如果开通了餐具费
                servicePrice = servicePrice.add(shopDetail.getTablewareFeePrice()); //服务费累加餐盒费
           }
           if (Common.YES.equals(shopDetail.getIsOpenSauceFee())){ //如果开通了酱料费
               servicePrice = servicePrice.add(shopDetail.getSauceFeePrice()); //服务费累加酱料费
           }
           if (Common.YES.equals(shopDetail.getIsOpenTowelFee())){ //如果开通了纸巾费
                servicePrice = servicePrice.add(shopDetail.getTowelFeePrice());
           }
           //将新版服务费总值赋值到服务费价格字段(微信端还是走之前的那个逻辑：根据店铺开关和服务费价格前端计算)
           shopDetail.setServicePrice(servicePrice);
       }

//        shopDetailService.updateWithDatong(shopDetail,getCurrentBrandId(),getBrandName());
        shopDetailService.update(shopDetail);
        ShopDetail shopDetail1 =(ShopDetail) redisService.get(getCurrentShopId()+"info");
        if(shopDetail != null){
            redisService.remove(getCurrentShopId()+"info");
        }
        Brand brand = brandService.selectByPrimaryKey(getCurrentBrandId());
        shopDetail = shopDetailService.selectByPrimaryKey(getCurrentShopId());
        LogTemplateUtils.shopDeatilEdit(brand.getBrandName(), shopDetail.getName(), getUserName());
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.SHOPDETAIL);
        shopMsgChangeDto.setType("modify");
        shopMsgChangeDto.setId(shopDetail.getId());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Result.getSuccess();
    }




    @RequestMapping("list_all")
    @ResponseBody
    public Result listAll(){
        List<ShopDetail> lists = shopDetailService.selectByBrandId(getCurrentBrandId());
        return getSuccessResult(lists);
    }

}
