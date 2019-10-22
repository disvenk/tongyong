
package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.DocStkInPlanHeaderDetailDo;
import com.resto.shop.web.dto.MdBillDo;
import com.resto.shop.web.model.MdBill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MdBillDao extends BaseDao<MdBill> {

    List<MdBillDo> queryJoin4Page(@Param("shopId") String shopId, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    List<DocStkInPlanHeaderDetailDo> queryJoin4PageAndBill(@Param("shopId") String shopId, @Param("beginDate") String beginDate, @Param("endDate") String endDate);
}


