package com.resto.shop.web.dao;

import com.resto.shop.web.model.QueueQrcode;
import com.resto.brand.core.generic.GenericDao;

public interface QueueQrcodeMapper  extends GenericDao<QueueQrcode,String> {
    int deleteByPrimaryKey(String id);

    int insert(QueueQrcode record);

    int insertSelective(QueueQrcode record);

    QueueQrcode selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(QueueQrcode record);

    int updateByPrimaryKey(QueueQrcode record);

    QueueQrcode selectByIdEndtime(String id);

    QueueQrcode selectLastQRcode(String shopId);
}
