package com.resto.shop.web.dao;

import com.resto.shop.web.model.WeChargeLog;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeChargeLogMapper  extends GenericDao<WeChargeLog,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(WeChargeLog record);

    int insertSelective(WeChargeLog record);

    WeChargeLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeChargeLog record);

    int updateByPrimaryKey(WeChargeLog record);

     List<WeChargeLog> selectByShopIdAndTime(@Param("shopId") String shopId, @Param("beginTime") String begin);

    void deleteByIds(@Param("ids")List<Long> ids);
}
