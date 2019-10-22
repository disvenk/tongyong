package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.Unit;
import com.resto.service.shop.entity.UnitArticle;
import com.resto.service.shop.mapper.UnitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitService extends BaseService<Unit, String> {

    @Autowired
    private UnitMapper unitMapper;

    @Override
    public BaseDao<Unit, String> getDao() {
        return unitMapper;
    }

    public List<Unit> getUnitByArticleid(String articleId) {
        return unitMapper.getUnitByArticleid(articleId);
    }

    public List<Unit> getUnitByArticleidWechat(String articleId) {
        List<Unit> units = unitMapper.getUnitByArticleidWechat(articleId);
        return units;
    }

    public List<UnitArticle> selectUnitDetail(String id) {
        return unitMapper.selectUnitDetail(id);
    }
}
