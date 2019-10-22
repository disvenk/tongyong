package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.Vocation;

import java.util.List;

/**
 * Created by carl on 2016/12/28.
 */
public interface VocationMapper extends BaseDao<Vocation, Integer> {

    List<Vocation> selectList();
}
