package com.resto.shop.web.dao;

import com.resto.shop.web.model.WeReturnCustomer;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WeReturnCustomerMapper  extends GenericDao<WeReturnCustomer,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(WeReturnCustomer record);

    int insertSelective(WeReturnCustomer record);

    WeReturnCustomer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeReturnCustomer record);

    int updateByPrimaryKey(WeReturnCustomer record);

    List<WeReturnCustomer> selectByShopIdAndTime(@Param("shopId") String shopId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    void deleteByIds(List<Long> ids);
}
