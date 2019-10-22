package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.BonusLog;
import com.resto.service.shop.mapper.BonusLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BonusLogService extends BaseService<BonusLog, String> {

    @Autowired
    private BonusLogMapper bonuslogMapper;

    @Override
    public BaseDao<BonusLog, String> getDao() {
        return bonuslogMapper;
    }

    public List<Map<String, Object>> selectAllBonusLog(String id) {
        return bonuslogMapper.selectAllBonusLog(id);
    }

    public List<Map<String, Object>> selectBonusLogBySelectMap(Map<String, Object> selectMap) {
        return bonuslogMapper.selectBonusLogBySelectMap(selectMap);
    }

}
