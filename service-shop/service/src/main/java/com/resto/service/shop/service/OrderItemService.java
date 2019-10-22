package com.resto.service.shop.service;

import com.resto.conf.util.ApplicationUtils;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.constant.OrderItemType;
import com.resto.service.shop.entity.OrderItem;
import com.resto.service.shop.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemService extends BaseService<OrderItem, String>{

    @Autowired
    private OrderItemMapper orderitemMapper;

    @Override
    public BaseDao<OrderItem, String> getDao() {
        return orderitemMapper;
    }

    public List<OrderItem> listByOrderId(String orderId) {
        List<OrderItem> orderItems = orderitemMapper.listByOrderId(orderId);

        List<OrderItem> other = orderitemMapper.listTotalByOrderId(orderId);

        orderItems.addAll(other);

        return getOrderItemsWithChild(orderItems);
    }

    List<OrderItem> getOrderItemsWithChild(List<OrderItem> orderItems) {
        logger.debug("这里查看套餐子项: ");
        Map<String, OrderItem> idItems = ApplicationUtils.convertCollectionToMap(String.class, orderItems);
        for (OrderItem item : orderItems) {
            if (item.getType() == OrderItemType.MEALS_CHILDREN) {
                OrderItem parent = idItems.get(item.getParentId());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<OrderItem>());
                }
                parent.getChildren().add(item);
                idItems.remove(item.getId());
            }
        }
        List<OrderItem> items = new ArrayList<>();
        for (OrderItem orderItem : idItems.values()) {
            items.add(orderItem);
            if (orderItem.getChildren() != null && !orderItem.getChildren().isEmpty()) {
//				for (OrderItem childItem:orderItem.getChildren()) {
                List<OrderItem> item = orderitemMapper.getListBySort(orderItem.getId(),orderItem.getArticleId());
                for(OrderItem obj : item){
                    obj.setArticleName("|_" + obj.getArticleName());
                    items.add(obj);
                }
//                childItem.setArticleName("|__" + childItem.getArticleName());
//                items.add(childItem);
//				}
            }
        }
        return items;
    }

    public void insertItems(List<OrderItem> orderItems) {
        //合并相同新规格的餐品
        for(int i = 0; i < orderItems.size(); i++){
            if(orderItems.get(i).getType() == OrderItemType.UNIT_NEW){
                for(int j = 0; j < orderItems.size(); j++){
                    if(orderItems.get(i).getName().equals(orderItems.get(j).getName()) && !orderItems.get(i).getId().equals(orderItems.get(j).getId())){
                        orderItems.get(i).setCount(orderItems.get(i).getCount() + orderItems.get(j).getCount());
                        orderItems.get(i).setFinalPrice(orderItems.get(i).getFinalPrice().add(orderItems.get(j).getFinalPrice()));
                        orderItems.remove(orderItems.get(j));
                    }
                }
            }
        }
        orderitemMapper.insertBatch(orderItems);
        List<OrderItem> allChildren = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getChildren() != null && !orderItem.getChildren().isEmpty()) {
                allChildren.addAll(orderItem.getChildren());
            }
        }
        if (!allChildren.isEmpty()) {
            orderitemMapper.insertBatch(allChildren);
        }
    }

    public List<OrderItem> selectOrderItemByOrderId(Map<String, Object> map) {
        return orderitemMapper.selectOrderItemByOrderId(map);
    }

    public List<OrderItem> listByOrderIds(List<String> childIds) {
        if (childIds == null || childIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<OrderItem> orderItems = orderitemMapper.listByOrderIds(childIds);

        return getOrderItemsWithChild(orderItems);
    }

}
