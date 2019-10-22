package com.resto.shop.web.dao;



import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.MaterialDo;
import com.resto.shop.web.model.MdMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MdMaterialDao extends BaseDao<MdMaterial> {

   List<MaterialDo> findMaterialByThirdId(@Param("thirdId") Long thirdId,@Param("shopId") String shopId);

    List<MaterialDo> queryJoin4Page(String shopId);
}
