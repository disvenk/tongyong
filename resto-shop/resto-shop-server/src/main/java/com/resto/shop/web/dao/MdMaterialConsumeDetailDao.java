
package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.model.MdMaterialConsumeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MdMaterialConsumeDetailDao extends BaseDao<MdMaterialConsumeDetail> {

    List<MdMaterialConsumeDetail> queryJoin4Page(@Param("shopId") String shopId);
}


