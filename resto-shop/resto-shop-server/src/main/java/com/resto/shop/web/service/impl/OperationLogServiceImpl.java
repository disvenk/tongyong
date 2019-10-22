package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.dao.PaymentReviewMapper;
import com.resto.shop.web.dao.ShopDictionaryMapper;
import com.resto.shop.web.dao.StoreOperationsLogMapper;
import com.resto.shop.web.model.DailyLogOperation;
import com.resto.shop.web.model.PaymentReview;
import com.resto.shop.web.model.StoreOperationsLog;
import com.resto.shop.web.posDto.AuditOrderItem;
import com.resto.shop.web.service.OperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/10/20/0020 15:47
 * @Description:
 */
@Component
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    PaymentReviewMapper paymentReviewMapper;

    @Autowired
    ShopDetailService shopDetailService;

    @Autowired
    ShopDictionaryMapper shopDictionaryMapper;

    @Autowired
    StoreOperationsLogMapper storeOperationsLogMapper;

    @Autowired
    BrandSettingService brandSettingService;

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Result insertOperationLog(DailyLogOperation dailyLogOperation) {
        Result result = new Result();
        List<AuditOrderItem> auditOrderItemList = dailyLogOperation.getAuditOrderItems();
        if (auditOrderItemList == null) {
            result.setSuccess(false);
            result.setMessage("审核支付项为空");
            return result;
        }
        StoreOperationsLog operationsLog = new StoreOperationsLog();
        operationsLog.setId(dailyLogOperation.getId());
        operationsLog.setBrandId(dailyLogOperation.getBrandId());
        operationsLog.setShopId(dailyLogOperation.getShopId());
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(dailyLogOperation.getShopId());
        operationsLog.setShopName(shopDetail.getName());
        operationsLog.setOperationPeople(dailyLogOperation.getOperator());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dailyLogOperation.getCreateTime());
            operationsLog.setOperationTime(date);
            operationsLog.setDailyTime(date);

            operationsLog.setCashAuditStatus(dailyLogOperation.getCloseStatus());
            operationsLog.setLogType(1);
            operationsLog.setCreateTime(new Date());
            storeOperationsLogMapper.insertSelective(operationsLog);
            //审核项
            for (AuditOrderItem auditOrderItem : auditOrderItemList) {
                PaymentReview paymentReview = new PaymentReview();
                paymentReview.setId(auditOrderItem.getId());
                paymentReview.setShopId(dailyLogOperation.getShopId());
                paymentReview.setOperator(auditOrderItem.getOperator());
                paymentReview.setPaymentModeId(auditOrderItem.getPaymentModeId());
                paymentReview.setReportMoney(auditOrderItem.getReportMoney().setScale(2));
                paymentReview.setAuditMoney(auditOrderItem.getAuditMoney().setScale(2));
                paymentReview.setDailyLogId(auditOrderItem.getDailyLogId());
                paymentReview.setCloseShopTime(sdf.parse(auditOrderItem.getCreateTime()));
                paymentReview.setCreateTime(new Date());
                paymentReview.setSerialNumber(auditOrderItem.getSort());
                paymentReviewMapper.insertSelective(paymentReview);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result.setSuccess(true);
        result.setMessage("插入审核支付项成功");
        return result;
    }

    @Override
    public List<StoreOperationsLog> selectListByShopIdAndTime(String shopId, Integer logType, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(time);
            List<StoreOperationsLog> storeOperationsLogs = storeOperationsLogMapper.selectListByShopIdAndTime(shopId, logType, DateUtil.getDateBegin(date), DateUtil.getDateEnd(date));
            return storeOperationsLogs;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PaymentReview> selectPaymentReviewByLogId(String shopId, String logId, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(time);
            List<PaymentReview> paymentReviews = paymentReviewMapper.selectPaymentReviewByLogId(shopId, logId, DateUtil.getDateBegin(date), DateUtil.getDateEnd(date));
            if (paymentReviews != null) {
                for (PaymentReview paymentReview : paymentReviews) {
                    String payModeName = PayMode.getPayModeName(paymentReview.getPaymentModeId());
                    paymentReview.setPayModeName(payModeName);
                }
            }
            return paymentReviews;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<PaymentReview> selectFloatingMoneyLastDay(Date date,String dailyLogId) {

            List<PaymentReview> paymentReviews = paymentReviewMapper.selectPaymentByLogId(dailyLogId);
            return paymentReviews;
    }

    @Override
    public void insertMessageLog(String operator, Date date, ShopDetail shopDetail, Brand brand) {
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());
        if (brandSetting!=null){
            if (brandSetting.getOpenAudit()==1){
                try {
                    StoreOperationsLog operationsLog = new StoreOperationsLog();
                    operationsLog.setId(ApplicationUtils.randomUUID());
                    operationsLog.setBrandId(brand.getId());
                    operationsLog.setShopId(shopDetail.getId());
                    operationsLog.setShopName(shopDetail.getName());
                    operationsLog.setOperationPeople(operator);
                    operationsLog.setOperationTime(date);
                    operationsLog.setMessageTime(date);
                    operationsLog.setLogType(3);
                    operationsLog.setCreateTime(new Date());
                    storeOperationsLogMapper.insertSelective(operationsLog);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(">>>>>>>>>>>短信记录日志失败>>>>>原因：" + e.getMessage());
                }
            }
        }
    }

    @Override
    public Result insertPrintLog(String printDate, String brandId, String shopId, String operator) {
        Result result = new Result();
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brandId);
        if (brandId!=null){
            if (brandSetting.getOpenAudit()!=1){
                result.setSuccess(false);
                result.setMessage("请开启开关");
                return result;
            }
        }
        try {
            StoreOperationsLog operationsLog = new StoreOperationsLog();
            operationsLog.setId(ApplicationUtils.randomUUID());
            operationsLog.setBrandId(brandId);
            operationsLog.setShopId(shopId);
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);
            operationsLog.setShopName(shopDetail.getName());
            operationsLog.setOperationPeople(operator);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            operationsLog.setOperationTime(sdf.parse(printDate));
            operationsLog.setPrintReportTime(sdf.parse(printDate));
            operationsLog.setLogType(4);
            operationsLog.setCreateTime(new Date());
            storeOperationsLogMapper.insertSelective(operationsLog);
            result.setSuccess(true);
            result.setMessage("插入成功");
        }catch (Exception e){
            e.printStackTrace();
            log.error(">>>>>>>>>>>>>>>>插入打印日志失败>>>>>>>原因："+e.getMessage());
            result.setSuccess(false);
            result.setMessage("插入打印日志失败");
        }
        return result;
    }

}
