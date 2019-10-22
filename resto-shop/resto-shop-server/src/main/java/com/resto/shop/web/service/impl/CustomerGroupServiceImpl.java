package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.service.NewCustomerGroupService;
import com.resto.api.customer.service.NewTableGroupService;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.constant.DistributionType;
import com.resto.shop.web.dao.CustomerGroupMapper;
import com.resto.shop.web.model.CustomerGroup;
import com.resto.shop.web.model.ShopCart;
import com.resto.shop.web.model.TableGroup;
import com.resto.shop.web.service.CustomerGroupService;
import com.resto.shop.web.service.ShopCartService;
import com.resto.shop.web.service.TableGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by carl on 2017/9/25.
 */
@Component
@Service
public class CustomerGroupServiceImpl extends GenericServiceImpl<CustomerGroup, Long> implements CustomerGroupService {

    @Resource
    CustomerGroupMapper customerGroupMapper;


    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    ShopCartService shopCartService;

    @Autowired
    NewCustomerGroupService newCustomerGroupService;

    @Autowired
    NewTableGroupService newTableGroupService;

    @Autowired
    ShopDetailService shopDetailService;

    @Override
    public GenericDao<CustomerGroup, Long> getDao() {
        return customerGroupMapper;
    }

    @Override
    public void removeGroupByCustomerId(String shopId, String tableNumber, String customerId) {
        CustomerGroup group = getGroupByCustomerId(customerId,shopId,tableNumber);
        if(group != null){
            //说明这个人之前在一个组内
            //把这个人在这个组内的购物车都清空
            List<ShopCart> shopCarts = shopCartService.listUserShopCart(customerId,shopId, DistributionType.RESTAURANT_MODE_ID);
            for(ShopCart shopCart : shopCarts){
                shopCartService.clearGroupId(shopCart.getId());
            }
        }

        ShopDetail shopDetail = shopDetailService.selectById(shopId);
        //先把这个人在这个店铺这个桌子上的组关系全删了
        newCustomerGroupService.removeGroupByCustomerId(shopDetail.getBrandId(),shopId, tableNumber, customerId);


        //然后判断这个人是否在这个店铺这个桌子上创建过组
        com.resto.api.customer.entity.TableGroup t=new com.resto.api.customer.entity.TableGroup();
        t.setShopDetailId(shopId);
        t.setCreateCustomerId(customerId);
        t.setTableNumber(tableNumber);
        t.setState(false);
        com.resto.api.customer.entity.TableGroup  tableGroup = newTableGroupService.dbSelectOne(shopDetail.getBrandId(),t);
        if(tableGroup != null){
            //如果这个人是这个组的组长
            List<CustomerGroup> customerGroupList = getGroupByGroupId(tableGroup.getGroupId());
            if(!CollectionUtils.isEmpty(customerGroupList)){
                //如果这个组还有别的人，那么选取最早加入的一位 成为新的组长
                tableGroup.setCreateCustomerId(customerGroupList.get(0).getCustomerId());
                newTableGroupService.dbUpdate(shopDetail.getBrandId(),tableGroup);
                CustomerGroup customerGroup = customerGroupList.get(0);
                customerGroup.setIsLeader(Common.YES);
                update(customerGroup);
            }else{
                //如果这个组没有别的人了,则把这个组给删除
                newTableGroupService.dbDeleteByPrimaryKey(shopDetail.getBrandId(),tableGroup.getId());
            }
        }

    }




    @Override
    public List<CustomerGroup> getGroupByGroupId(String groupId) {
        return customerGroupMapper.getGroupByGroupId(groupId);
    }

    @Override
    public CustomerGroup getGroupByCustomerId(String customerId, String shopId, String tableNumber) {
            return customerGroupMapper.getGroupByCustomerId(customerId, shopId, tableNumber);
    }

    @Override
    public List<CustomerGroup> getGroupByShopCart(String groupId) {
        return customerGroupMapper.getGroupByShopCart(groupId);
    }

    @Override
    public void removeByGroupId(String groupId) {
        customerGroupMapper.removeByGroupId(groupId);
    }
}
