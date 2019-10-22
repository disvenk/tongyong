package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.Unit;
import com.resto.service.shop.entity.UnitArticle;

import java.util.List;

public interface UnitMapper extends BaseDao<Unit, String> {

    List<Unit> getUnitByArticleid(String articleId);

    List<Unit> getUnitByArticleidWechat(String articleId);

    List<UnitArticle> selectUnitDetail(String id);
}
