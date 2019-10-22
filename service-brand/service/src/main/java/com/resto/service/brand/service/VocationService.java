package com.resto.service.brand.service;


import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.Vocation;
import com.resto.service.brand.mapper.VocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by carl on 2016/12/28.
 */
@Service
public class VocationService extends BaseService<Vocation, Integer> {

    @Autowired
    private VocationMapper vocationMapper;

    public BaseDao<Vocation, Integer> getDao() {
        return vocationMapper;
    }

}
