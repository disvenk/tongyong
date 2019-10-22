package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.ElemeToken;

/**
 * Created by carl on 2017/5/26.
 */
public interface ElemeTokenMapper  extends BaseDao<ElemeToken, Long> {

    ElemeToken getTokenByShopId(String shopId);

    int updateSelectByShopId(ElemeToken elemeToken);
}
