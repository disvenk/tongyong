package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.VirtualProducts;
import com.resto.shop.web.model.VirtualProductsAndKitchen;

import java.util.List;

/**
 * Created by yangwei on 2017/2/22.
 */
public interface VirtualProductsService extends GenericService<VirtualProducts, String> {
    VirtualProducts getVirtualProductsById(int id);

    List<VirtualProductsAndKitchen> getVirtualProductsAndKitchenById(int virtualId);

    List<VirtualProducts> getAllProducuts(String shopId);

    void insertVirtualProducts(VirtualProducts virtualProducts);

    void insertVirtualProductsKitchen(VirtualProductsAndKitchen virtualProductsAndKitchen);

    void insertVirtualProductsAndKitchen(VirtualProductsAndKitchen virtualProductsAndKitchen);

    void deleteById(Integer id);

    void deleteVirtualById(Integer virtualId);

    void updateVirtual(VirtualProducts virtualProducts);

    Integer selectMaxId();

    Integer[] getAllKitchenIdById(Integer virtualId);

    /**
     * 根据 店铺ID 查询 虚拟餐包关联的厨房列表
     * Pos2.0   激活店铺时使用     by  lmx
     * @param shopId
     * @return
     */
    List<VirtualProductsAndKitchen> selectKitchenByShopId(String shopId);
}
