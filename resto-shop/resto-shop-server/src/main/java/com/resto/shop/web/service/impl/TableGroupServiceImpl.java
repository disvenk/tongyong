package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.TableGroupMapper;
import com.resto.shop.web.model.TableGroup;
import com.resto.shop.web.service.CustomerGroupService;
import com.resto.shop.web.service.ShopCartService;
import com.resto.shop.web.service.TableGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by carl on 2017/9/25.
 */
@Component
@Service
public class TableGroupServiceImpl extends GenericServiceImpl<TableGroup, Long> implements TableGroupService {

    @Resource
    private TableGroupMapper tableGroupMapper;

    @Autowired
    CustomerGroupService customerGroupService;

    @Autowired
    ShopCartService shopCartService;

    @Override
    public GenericDao<TableGroup, Long> getDao() {
        return tableGroupMapper;
    }

    @Override
    public List<TableGroup> getTableGroupByShopId(String shopId, String tableNumber) {
        return tableGroupMapper.getTableGroupByShopId(shopId, tableNumber);
    }

    @Override
    public TableGroup getTableGroupByState(String shopId, String customerId, String tableNumber, Integer state) {
        return tableGroupMapper.getTableGroupByState(shopId, customerId, tableNumber, state);
    }

    @Override
    public TableGroup getTableGroupByCustomer(String tableNumber, String customerId, String shopId) {
        return tableGroupMapper.getTableGroupByCustomer(tableNumber, customerId, shopId);
    }

    @Override
    public TableGroup insertGroup(TableGroup tableGroup) {
        log.error("当前的tableGroup的tableNumber为：" + tableGroup.getTableNumber());
         tableGroupMapper.insertSelective(tableGroup);
         return tableGroup;
    }

    @Override
    public void removeTableGroup(String groupId) {
        TableGroup tableGroup = tableGroupMapper.selectByGroupId(groupId);
        if(tableGroup != null && tableGroup.getState() == TableGroup.NOT_PAY ){
            //如果15分钟后 还没买单的组 自动取消
            tableGroup.setState(TableGroup.FINISH);
            update(tableGroup);
            //删除组和组员的关系
//            customerGroupService.removeByGroupId(tableGroup.getGroupId());
            //把组内所有人的购物车重置为自己的
            shopCartService.resetGroupId(tableGroup.getGroupId());
        }
    }

    @Override
    public void releaseTableGroup(Date time) {
        tableGroupMapper.releaseTableGroup(time);
    }

    @Override
    public TableGroup selectByGroupId(String groupId) {
        return tableGroupMapper.selectByGroupId(groupId);
    }
}
