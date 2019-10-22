package com.resto.shop.web.dao;



import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.model.DocStkInPlanDetail;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Created by Administrator on 2017/9/4 0004.
 */
public interface DocStkInPlanDetailDao extends BaseDao<DocStkInPlanDetail> {

    List<DocStkInPlanDetail> selectMaterialId(@Param("materialId") Long materialId);

}
