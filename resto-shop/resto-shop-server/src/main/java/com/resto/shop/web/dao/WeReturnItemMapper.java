package com.resto.shop.web.dao;

import com.resto.shop.web.model.WeReturnItem;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WeReturnItemMapper  extends GenericDao<WeReturnItem,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(WeReturnItem record);

    int insertSelective(WeReturnItem record);

    WeReturnItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeReturnItem record);

    int updateByPrimaryKey(WeReturnItem record);

    List<WeReturnItem> selectByShopIdAndTime(@Param("shopId") String id, @Param("beginDate") Date beginDate,@Param("endDate") Date endDate);

    void deleteByIds(List<Long> ids);
}
