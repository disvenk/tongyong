package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.BonusLogMapper;
import com.resto.shop.web.model.BonusLog;
import com.resto.shop.web.service.BonusLogService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
@Service
public class BonusLogServiceImpl extends GenericServiceImpl<BonusLog, String> implements BonusLogService {

    @Resource
    private BonusLogMapper bonuslogMapper;

    @Override
    public GenericDao<BonusLog, String> getDao() {
        return bonuslogMapper;
    }

    @Override
    public List<Map<String, Object>> selectAllBonusLog(String id) {
        return bonuslogMapper.selectAllBonusLog(id);
    }

    @Override
    public List<Map<String, Object>> selectBonusLogBySelectMap(Map<String, Object> selectMap) {
        return bonuslogMapper.selectBonusLogBySelectMap(selectMap);
    }
}
