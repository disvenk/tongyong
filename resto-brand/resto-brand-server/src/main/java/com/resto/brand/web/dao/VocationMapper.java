package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.Vocation;

import java.util.List;

/**
 * Created by carl on 2016/12/28.
 */
public interface VocationMapper extends GenericDao<Vocation, Integer> {

    List<Vocation> selectList();
}
