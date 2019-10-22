package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.VirtualProductsMapper;
import com.resto.shop.web.model.VirtualProducts;
import com.resto.shop.web.model.VirtualProductsAndKitchen;
import com.resto.shop.web.service.VirtualProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yangwei on 2017/2/22.
 */
@Component
@Service
public class VirtualProductsServiceImpl extends GenericServiceImpl<VirtualProducts, String> implements VirtualProductsService{
    @Autowired
    private VirtualProductsMapper virtualProductsMapper;

    @Override
    public GenericDao<VirtualProducts, String> getDao() {
        return virtualProductsMapper;
    }

    @Override
    public VirtualProducts getVirtualProductsById(int id) {
        return virtualProductsMapper.getVirtualProductsById(id);
    }

    @Override
    public List<VirtualProductsAndKitchen> getVirtualProductsAndKitchenById(int virtualId) {
        return virtualProductsMapper.getVirtualProductsAndKitchenById(virtualId);
    }

    @Override
    public List<VirtualProducts> getAllProducuts(String shopId) {
        return virtualProductsMapper.getAllProducuts(shopId);
    }

    @Override
    public void insertVirtualProducts(VirtualProducts virtualProducts) {
        virtualProductsMapper.insertVirtualProducts(virtualProducts);
    }

    @Override
    public void insertVirtualProductsKitchen(VirtualProductsAndKitchen virtualProductsAndKitchen) {
        virtualProductsMapper.insertVirtualProductsKitchen(virtualProductsAndKitchen);
    }

    @Override
    public void insertVirtualProductsAndKitchen(VirtualProductsAndKitchen virtualProductsAndKitchen) {
        virtualProductsMapper.insertVirtualProductsKitchen(virtualProductsAndKitchen);
    }

    @Override
    public void deleteById(Integer id) {
        virtualProductsMapper.deleteById(id);
    }

    @Override
    public void deleteVirtualById(Integer virtualId) {
        virtualProductsMapper.deleteVirtualById(virtualId);
    }

    @Override
    public void updateVirtual(VirtualProducts virtualProducts) {
        virtualProductsMapper.updateVirtual(virtualProducts);
    }

    @Override
    public Integer selectMaxId() {
        return virtualProductsMapper.selectMaxId();
    }

    @Override
    public Integer[] getAllKitchenIdById(Integer virtualId) {
        return virtualProductsMapper.getAllKitchenIdById(virtualId);
    }

    @Override
    public List<VirtualProductsAndKitchen> selectKitchenByShopId(String shopId) {
        return virtualProductsMapper.selectKitchenByShopId(shopId);
    }
}
