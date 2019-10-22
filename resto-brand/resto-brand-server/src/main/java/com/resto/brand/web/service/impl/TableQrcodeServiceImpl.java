package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.TableQrcodeMapper;
import com.resto.brand.web.model.TableQrcode;
import com.resto.brand.web.service.TableQrcodeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by carl on 2016/12/16.
 */
@Component
@Service
public class TableQrcodeServiceImpl extends GenericServiceImpl<TableQrcode, Long> implements TableQrcodeService {

    @Resource
    private TableQrcodeMapper tableQrcodeMapper;

    @Override
    public GenericDao<TableQrcode, Long> getDao() {
        return tableQrcodeMapper;
    }

    @Override
    public List<TableQrcode> selectByShopId(String shopId) {
        return tableQrcodeMapper.selectByShopId(shopId);
    }

    @Override
    public List<TableQrcode> selectUsedByShopId(String shopId) {
        return tableQrcodeMapper.selectUsedByShopId(shopId);
    }

    @Override
    public List<Long> selectArea(String shopId) {
        return tableQrcodeMapper.selectArea(shopId);
    }

    @Override
    public void updateTable(Long areaId, String areaName) {
        tableQrcodeMapper.updateTable(areaId,areaName);
    }

    @Override
    public void deleteArea(Long areaId) {
        tableQrcodeMapper.deleteArea(areaId);
    }

    @Override
    public TableQrcode selectByTableNumberShopId(String shopId, Integer tableNumber) {
        return tableQrcodeMapper.selectByTableNumberShopId(shopId, tableNumber);
    }
}
