package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.ElemeToken;

/**
 * Created by carl on 2017/5/26.
 */
public interface ElemeTokenMapper  extends GenericDao<ElemeToken, Long> {

    ElemeToken getTokenByShopId(String shopId);

    int updateSelectByShopId(ElemeToken elemeToken);
}
