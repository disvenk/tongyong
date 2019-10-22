package com.resto.shop.web.service;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.shop.web.model.DailyLogOperation;
import com.resto.shop.web.model.StoreOperationsLog;
import com.resto.shop.web.model.PaymentReview;

import java.util.Date;
import java.util.List;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/10/20/0020 15:47
 * @Description:
 */
public interface OperationLogService {

    /**
     * 获取newpos结店审核数据
     * @return
     */
    Result insertOperationLog(DailyLogOperation dailyLogData);

    /**
     * 获取结店日志列表
     * @param shopId
     * @param logType
     * @param time
     * @return
     */
    List<StoreOperationsLog> selectListByShopIdAndTime(String shopId, Integer logType, String time);

    /**
     * 查询结店日志详情
     * @param shopId
     * @param logId
     * @param time
     * @return
     */
    List<PaymentReview> selectPaymentReviewByLogId(String shopId,String logId, String time);



    List<PaymentReview> selectFloatingMoneyLastDay(Date date,String dailyLogId);

    /**
     * 插入发送短信日志
     * @param operator
     * @param date
     * @param shopDetail
     * @param brand
     */
    void insertMessageLog(String operator,Date date,ShopDetail shopDetail,Brand brand);

    /**
     * 插入打印日志
     * @param printDate
     * @param brandId
     * @param shopId
     * @param operator
     * @return
     */
    Result insertPrintLog(String printDate,String brandId,String shopId,String operator);
}
