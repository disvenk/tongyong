package com.resto.service.appraise.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.api.appraise.entity.AppraisePraise;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppraisePraiseMapper extends MyMapper<AppraisePraise> {

    List<AppraisePraise> appraisePraiseList(String appraiseId);

    int updateCancelPraise(@Param("appraiseId") String appraiseId, @Param("customerId") String customerId, @Param("isDel") Integer isDel);

    AppraisePraise selectByAppraiseIdCustomerId(@Param("appraiseId") String appraiseId, @Param("customerId") String customerId);

    int selectByCustomerCount(String customerId);
}
