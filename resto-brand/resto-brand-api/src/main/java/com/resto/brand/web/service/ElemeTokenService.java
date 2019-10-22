package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.ElemeToken;

/**
 * Created by carl on 2017/5/26.
 */
public interface ElemeTokenService  extends GenericService<ElemeToken, Long> {

    ElemeToken getTokenByShopId(String shopId);

    int updateSelectByShopId(ElemeToken elemeToken);

}
