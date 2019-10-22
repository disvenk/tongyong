package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.CustomerGroup;

import java.util.List;

/**
 * Created by carl on 2017/9/25.
 */
public interface CustomerGroupService extends GenericService<CustomerGroup, Long> {
    /**
     * 删除这个人在这个店铺这个桌子下面的组关系
     * @param shopId 店铺id
     * @param tableNumber 桌号
     * @param customerId 用户id
     */
    void removeGroupByCustomerId(String shopId,String tableNumber,String customerId);


    /**
     * 得到这个组下所有的人
     * @param groupId 组号
     * @return
     */
    List<CustomerGroup> getGroupByGroupId(String groupId);

    /**
     * 得到这个人在这个店铺这个桌子下的所有组关系
     * @param customerId 用户id
     * @param shopId 店铺id
     * @param tableNumber 桌号
     * @return
     */
    CustomerGroup  getGroupByCustomerId(String customerId,String shopId,String tableNumber);


    /**
     * 返回当前购物车组中所有人
     * @param groupId 组号
     * @return
     */
    List<CustomerGroup> getGroupByShopCart(String groupId);


    /**
     * 删除组内所有人
     * @param groupId 组号
     */
    void removeByGroupId(String groupId);

}
