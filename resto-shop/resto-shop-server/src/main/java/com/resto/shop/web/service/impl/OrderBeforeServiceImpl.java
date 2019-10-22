package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.dao.OrderBeforeMapper;
import com.resto.shop.web.model.CustomerGroup;
import com.resto.shop.web.model.OrderBefore;
import com.resto.shop.web.model.OrderItem;
import com.resto.shop.web.model.TableGroup;
import com.resto.shop.web.service.CustomerGroupService;
import com.resto.shop.web.service.OrderBeforeService;
import com.resto.shop.web.service.OrderItemService;
import com.resto.shop.web.service.TableGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KONATA on 2017/11/1.
 */
@Component
@Service
public class OrderBeforeServiceImpl extends GenericServiceImpl<OrderBefore, Long> implements OrderBeforeService {

    @Autowired
    private OrderBeforeMapper orderBeforeMapper;

    @Autowired
    private ShopDetailService shopDetailService;

    @Autowired
    private BrandSettingService brandSettingService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private CustomerGroupService customerGroupService;

    @Override
    public GenericDao<OrderBefore, Long> getDao() {
        return orderBeforeMapper;
    }


    @Override
    public OrderBefore getOrderNoPay(String tableNumber, String shopId, String customerId ){
        return orderBeforeMapper.getOrderNoPay(tableNumber, shopId, customerId);
    }

    @Override
    public List<OrderItem> getOrderBefore(String tableNumber, String shopId, String customerId) {
        ShopDetail shopDetail =  shopDetailService.selectById(shopId);
        if(shopDetail == null){
            return null;
        }
        BrandSetting brandSetting = brandSettingService.selectByBrandId(shopDetail.getBrandId());
        if(StringUtils.isEmpty(tableNumber)){
            return null;
        }else{
            if(brandSetting.getOpenManyCustomerOrder() == Common.YES
                    && shopDetail.getOpenManyCustomerOrder() == Common.YES){
                //多人
                //判断下这个人所在的组内的所有成员有没有生成过预点餐餐品

                TableGroup tableGroup = tableGroupService.getTableGroupByState(shopId,customerId,tableNumber,0);
                if(tableGroup == null){
                    //这个人不在组内
                    return orderItemService.getOrderBefore(tableNumber, shopId, customerId);
                }else{
                    //这个人在组内
                    List<OrderItem> orderItems = new ArrayList<>();
                    List<CustomerGroup> customerGroupList = customerGroupService.getGroupByGroupId(tableGroup.getGroupId());
                    if(!CollectionUtils.isEmpty(customerGroupList)){
                        for(CustomerGroup customerGroup : customerGroupList){
                            //循环组内所有成员
                            List<OrderItem> items = orderItemService.getOrderBefore(tableNumber, shopId, customerGroup.getCustomerId());
                            if(!CollectionUtils.isEmpty(items)){
                                orderItems.addAll(items);
                            }
                        }
                        return orderItems;
                    }
                }
            }else{
                //单人
                //得到这个人在这个店铺的这个桌子下生成的未支付的预点餐餐品列表
               return orderItemService.getOrderBefore(tableNumber, shopId, customerId);

            }
        }
        return null;
    }

    @Override
    public void updateState(String orderId,Integer state) {
        orderBeforeMapper.updateState(orderId,state);
    }
}
