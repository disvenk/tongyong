package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.QueueQrcode;

public interface QueueQrcodeService extends GenericService<QueueQrcode, String> {

    QueueQrcode selectByIdEndtime(String id);

    QueueQrcode selectLastQRcode(String shopId);

}
