package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.LogBase;

/**
 * Created by carl on 2016/11/14.
 */
public interface LogBaseMapper  extends GenericDao<LogBase,String> {

    int insert(LogBase logBase);

    int insertSelective(LogBase logBase);

}
