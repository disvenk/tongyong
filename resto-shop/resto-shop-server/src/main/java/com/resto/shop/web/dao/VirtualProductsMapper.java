package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.VirtualProducts;
import com.resto.shop.web.model.VirtualProductsAndKitchen;

import java.util.List;

/**
 * Created by yangwei on 2017/2/22.
 */
public interface VirtualProductsMapper extends GenericDao<VirtualProducts, String> {
    List<VirtualProducts> getAllProducuts(String shopId);

    VirtualProducts getVirtualProductsById(int id);

    List<VirtualProductsAndKitchen>getVirtualProductsAndKitchenById(int virtualId);

    void insertVirtualProducts(VirtualProducts virtualProducts);

    void insertVirtualProductsKitchen(VirtualProductsAndKitchen virtualProductsAndKitchen);

    void deleteById(Integer id);

    void deleteVirtualById(Integer virtualId);

    void updateVirtual(VirtualProducts virtualProducts);

    Integer selectMaxId();

    Integer[] getAllKitchenIdById(Integer virtualId);

    List<VirtualProductsAndKitchen> selectKitchenByShopId(String shopId);
}
