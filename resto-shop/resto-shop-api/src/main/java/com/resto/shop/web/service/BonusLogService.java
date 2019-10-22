package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.BonusLog;

import java.util.List;
import java.util.Map;

public interface BonusLogService extends GenericService<BonusLog, String> {

    List<Map<String, Object>> selectAllBonusLog(String id);

    List<Map<String, Object>> selectBonusLogBySelectMap(Map<String, Object> selectMap);
}
