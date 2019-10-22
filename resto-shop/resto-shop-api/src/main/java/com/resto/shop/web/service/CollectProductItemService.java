package com.resto.shop.web.service;

import com.resto.shop.web.model.CollectProductItem;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/8/17/0017 11:42
 * @Description:
 */
public interface CollectProductItemService {

    int insert(CollectProductItem record);

    int insertSelective(CollectProductItem record);
}
