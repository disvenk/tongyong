
package com.resto.shop.web.dao;

import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.DocPmsPoDetailDo;
import com.resto.shop.web.dto.DocPmsPoHeaderDetailDo;
import com.resto.shop.web.model.DocPmsPoHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DocPmsPoHeaderDao extends BaseDao<DocPmsPoHeader> {

    List<DocPmsPoHeaderDetailDo> queryJoin4Page(@Param("shopId") String shopId);

    List<DocPmsPoDetailDo> queryDocPmsPoDetailDos(@Param("scmDocPmsPoHeaderId") String scmDocPmsPoHeaderId);
}


