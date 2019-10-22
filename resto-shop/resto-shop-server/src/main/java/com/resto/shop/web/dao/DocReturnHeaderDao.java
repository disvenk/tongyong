
package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.DocReturnHeaderDetailDo;
import com.resto.shop.web.model.DocReturnHeader;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DocReturnHeaderDao extends BaseDao<DocReturnHeader> {

    List<DocReturnHeaderDetailDo> queryJoin4Page(@Param("shopDetailId") String shopId);
}


