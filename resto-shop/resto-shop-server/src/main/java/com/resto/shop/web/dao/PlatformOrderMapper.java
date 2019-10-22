package com.resto.shop.web.dao;

import com.resto.brand.web.dto.PlatformReportDto;
import com.resto.shop.web.model.PlatformOrder;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PlatformOrderMapper  extends GenericDao<PlatformOrder,String> {
    int deleteByPrimaryKey(String id);

    int insert(PlatformOrder record);

    int insertSelective(PlatformOrder record);

    PlatformOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PlatformOrder record);

    int updateByPrimaryKeyWithBLOBs(PlatformOrder record);

    int updateByPrimaryKey(PlatformOrder record);

    PlatformOrder selectByPlatformOrderId(@Param("platformOrderId") String platformOrderId, @Param("type") Integer type);

    //直接sql查询
    PlatformReportDto selectByshopDetailId(Map map);

    /**
     * 查询某天的异常订单
     * @param currentShopId
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    List<PlatformOrder> selectPlatFormErrorOrderList(@Param("shopId")String currentShopId, @Param("dateBegin")Date dateBegin, @Param("dateEnd")Date dateEnd);

    BigDecimal sumPlatformOrderPrice(@Param("shopId")String shopId, @Param("beginDate")String beginDate, @Param("endDate")String endDate);
}
