package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.TableQrcode;

import java.util.List;

/**
 * Created by carl on 2016/12/16.
 */
public interface TableQrcodeService extends GenericService<TableQrcode, Long> {

    List<TableQrcode> selectByShopId(String shopId);




    TableQrcode selectByTableNumberShopId(String shopId, Integer tableNumber);
}
