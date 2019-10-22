package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.QueueQrcodeMapper;
import com.resto.shop.web.model.QueueQrcode;
import com.resto.shop.web.service.QueueQrcodeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@Service
public class QueueQrcodeServiceImpl extends GenericServiceImpl<QueueQrcode, String> implements QueueQrcodeService {

    @Resource
    private QueueQrcodeMapper queueqrcodeMapper;

    @Override
    public GenericDao<QueueQrcode, String> getDao() {
        return queueqrcodeMapper;
    }

    @Override
    public QueueQrcode selectByIdEndtime(String id) {
        return queueqrcodeMapper.selectByIdEndtime(id);
    }

    @Override
    public QueueQrcode selectLastQRcode(String shopId) {
        return queueqrcodeMapper.selectLastQRcode(shopId);
    }
}
