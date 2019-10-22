
package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.model.MdMaterialConsumeOpt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MdMaterialConsumeOptDao extends BaseDao<MdMaterialConsumeOpt> {

    List<MdMaterialConsumeOpt> queryJoin4Page(@Param("shopId") String shopId);

    MdMaterialConsumeOpt findMdMaterialConsumeLimitOne(String shopId);
}


