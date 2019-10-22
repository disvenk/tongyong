package com.resto.brand.web.service;


import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.TableQrcode;

import java.util.List;

/**
 * Created by carl on 2016/12/16.
 */
public interface TableQrcodeService extends GenericService<TableQrcode, Long> {

    List<TableQrcode> selectByShopId(String shopId);

    List<TableQrcode> selectUsedByShopId(String shopId);

    List<Long> selectArea(String shopId);

    void updateTable(Long areaId,String areaName);

    void deleteArea(Long areaId);

    TableQrcode selectByTableNumberShopId(String shopId, Integer tableNumber);
}
