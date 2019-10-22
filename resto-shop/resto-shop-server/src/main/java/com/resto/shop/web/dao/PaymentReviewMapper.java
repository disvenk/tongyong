package com.resto.shop.web.dao;

import com.resto.shop.web.model.PaymentReview;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PaymentReviewMapper {
    int deleteByPrimaryKey(String id);

    int insert(PaymentReview record);

    int insertSelective(PaymentReview record);

    PaymentReview selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PaymentReview record);

    int updateByPrimaryKey(PaymentReview record);

    List<PaymentReview> selectPaymentReviewByLogId(@Param("shopId")String shopId, @Param("logId") String logId, @Param("staTime") Date staTime, @Param("endTime")Date endTime);

    List<PaymentReview> selectPaymentByLogId(@Param("logId")String logId);
}