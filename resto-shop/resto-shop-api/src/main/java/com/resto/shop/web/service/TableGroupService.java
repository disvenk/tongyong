package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.TableGroup;

import java.util.Date;
import java.util.List;

/**
 * Created by carl on 2017/9/25.
 */
public interface TableGroupService extends GenericService<TableGroup, Long> {

    /**
     * 得到店铺桌位下的组
     * @param shopId 店铺id
     * @param tableNumber 桌号
     * @return 组列表
     */
    List<TableGroup> getTableGroupByShopId(String shopId,String tableNumber);


    /**
     * 得到这家店这个桌子上这个人有没有加入过已支付的组
     * @param shopId 店铺id
     * @param customerId 用户id
     * @param tableNumber 桌号
     * @param state 0； 未支付
     * @return
     */
    TableGroup getTableGroupByState(String shopId,String customerId,String tableNumber,Integer state);


    /**
     * 得到这个人这个桌子上有没有创建过组
     * @param tableNumber 桌号
     * @param customerId 用户id
     * @param shopId 店铺id
     * @return
     */
    TableGroup getTableGroupByCustomer(String tableNumber,String customerId,String shopId);

    TableGroup insertGroup(TableGroup tableGroup);

    TableGroup selectByGroupId(String groupId);

    void removeTableGroup(String groupId);

    void releaseTableGroup (Date time);

}
