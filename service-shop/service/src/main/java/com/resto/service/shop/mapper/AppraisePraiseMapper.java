package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.AppraisePraise;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppraisePraiseMapper extends BaseDao<AppraisePraise,String> {

    int insert(AppraisePraise appraisePraise);

    int updateByPrimaryKey(AppraisePraise appraisePraise);

    int updateCancelPraise(@Param("appraiseId") String appraiseId, @Param("customerId") String customerId, @Param("isDel") Integer isDel);

    List<AppraisePraise> appraisePraiseList(String appraiseId);

    AppraisePraise selectByAppraiseIdCustomerId(@Param("appraiseId") String appraiseId, @Param("customerId") String customerId);

    int selectByCustomerCount(String customerId);
}
