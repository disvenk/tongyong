
package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.model.DocReturnDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocReturnDetailDao extends BaseDao<DocReturnDetail> {

    List<DocReturnDetail> queryJoin4Page(@Param("shopId") String shopId);
}


