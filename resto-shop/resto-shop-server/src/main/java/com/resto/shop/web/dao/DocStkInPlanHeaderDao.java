package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.DocStkInPlanHeaderDetailDo;
import com.resto.shop.web.model.DocStkInPlanHeader;
import org.apache.ibatis.annotations.Param;
import java.util.List;


public interface DocStkInPlanHeaderDao extends BaseDao<DocStkInPlanHeader> {

    List<DocStkInPlanHeaderDetailDo> queryJoin4Page(@Param("shopId") String shopId);

    String findMinBeginTime(@Param("shopId") String shopId);

    //入库单id
    DocStkInPlanHeader queryJoinSupplier(Long id);
}
