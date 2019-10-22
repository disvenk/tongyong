package com.resto.wechat.web.controller;

import com.resto.api.customer.service.NewCustomerGroupService;
import com.resto.api.customer.service.NewTableGroupService;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.shop.web.constant.*;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.*;
import com.resto.api.customer.entity.CustomerGroup;
import com.resto.api.customer.entity.TableGroup;
import com.resto.api.article.entity.Article;
import com.resto.shop.web.service.*;
import com.resto.wechat.web.rpcinterceptors.DataSourceTarget;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.MobilePackageBean;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by KONATA on 2017/9/26.
 */
@RequestMapping("/duoren")
@Controller
public class DuorenController extends GenericController {

    @Autowired
    NewCustomerGroupService newCustomerGroupService;

    @Autowired
    CustomerGroupService customerGroupService;

    @Autowired
    NewTableGroupService newTableGroupService;

    @Autowired
    ShopCartService shopCartService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ArticleService articleService;

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderBeforeService orderBeforeService;

    @RequestMapping("/checkRepeat")
    public void checkRepeat(String articleId, String groupId, String customerId, HttpServletRequest request, HttpServletResponse response) {
        //判断多人点餐是否菜品重复
        AppUtils.unpack(request, response);
        JSONObject json = new JSONObject();
        Integer count = shopCartService.checkRepeat(articleId, groupId, customerId);
        if (count > 0) {
            json.put("statusCode", "100");
            json.put("success", false);
            json.put("message", "菜品重复");
            json.put("count", count);
        } else {
            json.put("statusCode", "200");
            json.put("success", true);
            json.put("message", "请求成功");
        }

        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }


    @RequestMapping("/addGroup")
    public void addGroup(CustomerGroup customerGroup, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        //这里判断下如果加入的还是之前的组，则不做任何操作
        //先找到这个人在这个店铺的这个桌子下是哪个组的
        CustomerGroup groupBefore = newCustomerGroupService.getGroupByCustomerId(getCurrentBrandId(),customerGroup.getCustomerId(), customerGroup.getShopDetailId(), customerGroup.getTableNumber());
        if (groupBefore != null && groupBefore.getGroupId().equals(customerGroup.getGroupId())) {
            JSONObject json = new JSONObject();
            json.put("statusCode", "200");
            json.put("success", true);
            json.put("message", "请求成功");
            json.put("groupId", customerGroup.getGroupId());
            ApiUtils.setBackInfo(response, json); // 返回信息设置
            return;
        }
        //清空之前的组关系 以及组里的购物车
        newCustomerGroupService.removeGroupByCustomerId(getCurrentBrandId(),customerGroup.getShopDetailId(), customerGroup.getTableNumber(), customerGroup.getCustomerId());
        Customer customer = customerService.selectById(customerGroup.getCustomerId());
        //加入新的组
        CustomerGroup group = new CustomerGroup();
        group.setGroupId(customerGroup.getGroupId());
        group.setTableNumber(customerGroup.getTableNumber());
        group.setShopDetailId(customerGroup.getShopDetailId());
        group.setBrandId(getCurrentBrandId());
        group.setCustomerId(customer.getId());
        group.setHeadPhoto(customer.getHeadPhoto());
        group.setCustomerName(customer.getNickname());
        newCustomerGroupService.dbSave(getCurrentBrandId(),group);
        //把自己的购物车内容同步给新的组
        shopCartService.updateGroupNew(customerGroup.getCustomerId(), customerGroup.getShopDetailId(), customerGroup.getGroupId());
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("groupId", customerGroup.getGroupId());
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }


    @RequestMapping("/groupNew")
    public void groupNew(String shopDetailId, String tableNumber, String customerId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        //判断下用户之前是否已在一个组内，如果在的话 从组中退出
        customerGroupService.removeGroupByCustomerId(shopDetailId, tableNumber, customerId);
        //如果组未存在，则自动创建
        String groupId = ApplicationUtils.randomUUID();
        Customer customer = customerService.selectById(customerId);
        TableGroup group = new TableGroup();
        group.setCreateCustomerId(customerId);
        group.setBrandId(getCurrentBrandId());
        group.setGroupId(groupId);
        group.setShopDetailId(shopDetailId);
        group.setTableNumber(tableNumber);
        newTableGroupService.dbSave(getCurrentBrandId(),group);
        //创建人与组的关系表
        CustomerGroup customerGroup = new CustomerGroup();
        customerGroup.setBrandId(getCurrentBrandId());
        customerGroup.setShopDetailId(shopDetailId
        );
        customerGroup.setTableNumber(tableNumber);
        customerGroup.setIsLeader(Common.YES);
        customerGroup.setHeadPhoto(customer.getHeadPhoto());
        customerGroup.setCustomerName(customer.getNickname());
        customerGroup.setCustomerId(customerId);
        customerGroup.setGroupId(groupId);
        newCustomerGroupService.dbSave(getCurrentBrandId(),customerGroup);
        //把这个人之前的购物车里的东西都转移到新组中
        shopCartService.updateGroupNew(customerId, shopDetailId, groupId);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("groupId", groupId);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }


    @RequestMapping("/removeCustomer")
    public void removeCustomer(CustomerGroup customerGroup, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        customerGroupService.removeGroupByCustomerId(customerGroup.getShopDetailId(), customerGroup.getTableNumber(), customerGroup.getCustomerId());
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置

    }


    @RequestMapping("/getArticleBefore")
    public void getArticleBefore(String shopId, String tableNumber, String customerId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        List<Article> articleList = articleService.getArticleBefore(shopId, tableNumber, customerId);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        if (!CollectionUtils.isEmpty(articleList)) {
            json.put("data", new JSONArray(articleList));
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }


    @RequestMapping("/getOrderBefore")
    public void getOrderBefore(String tableNumber,String shopId,String customerId, HttpServletRequest request, HttpServletResponse response){

        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        List<OrderItem> result = orderBeforeService.getOrderBefore(tableNumber,shopId,customerId);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        if (!CollectionUtils.isEmpty(result)) {
            json.put("data", new JSONArray(result));
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/saveArticleBefore")
    public void saveArticleBefore(String orderItem, HttpServletRequest request, HttpServletResponse response) throws AppException {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        Order order = new Order();
        order.setDistributionModeId(DistributionType.RESTAURANT_MODE_ID);
        order.setOrderBefore(Common.YES);
        order.setWaitMoney(BigDecimal.ZERO);
        order.setPayType(PayType.NOPAY);
        order.setPayMode(OrderPayMode.YUE_PAY);
        order.setBrandId(queryParams.get("brandId").toString());
        order.setShopDetailId(queryParams.get("shopDetailId").toString());
        order.setCustomerId(queryParams.get("customerId").toString());
        order.setTableNumber(queryParams.get("tableNumber").toString());
        List<OrderItem> orderItems = com.alibaba.fastjson.JSONArray.parseArray(orderItem, OrderItem.class);
        order.setOrderItems(orderItems);
        order.setAllowContinueOrder(false);
        DataSourceTarget.setDataSourceName(order.getBrandId());
        JSONResult jsonResult = orderService.createOrder(order);
        order = (Order) jsonResult.getData();
        JSONObject json = new JSONObject();
        json.put("statusCode", "0");
        if (jsonResult.isSuccess() == true) {
            orderService.sendPosNewOrder(getCurrentShopId(), order);
            log.info("发送至NewPOS：" + jsonResult.toString());
            json.put("success", true);
            json.put("message", "请求成功");
            json.put("data", new JSONObject(order));
        } else {
            json.put("success", false);
            json.put("message", jsonResult.getMessage());
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }


    @RequestMapping("/shopGroupList")
    public void showGroupList(String customerId, String tableNumber, String shopDetailId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        //先判断用户是否在一个已支付的组里了
        TableGroup groupList = newTableGroupService.getTableGroupByState(getCurrentBrandId(),shopDetailId,customerId,tableNumber,1);

        JSONObject json = new JSONObject();
        if (groupList == null) {
            log.info("用户不存在于某个已支付的组里");
            List<TableGroup> tableGroups = newTableGroupService.getTableGroupByShopId(getCurrentBrandId(),shopDetailId, tableNumber);
            if (CollectionUtils.isEmpty(tableGroups)) {
                //如果组未存在，则自动创建
                String groupId = ApplicationUtils.randomUUID();
                TableGroup group = new TableGroup();
                group.setCreateCustomerId(customerId);
                group.setBrandId(getCurrentBrandId());
                group.setGroupId(groupId);
                group.setShopDetailId(shopDetailId);
                group.setTableNumber(tableNumber);
                newTableGroupService.dbSave(getCurrentBrandId(),group);
                tableGroups.add(group);
                //创建人与组的关系表
                CustomerGroup cg = new CustomerGroup();
                cg.setBrandId(getCurrentBrandId());
                cg.setShopDetailId(shopDetailId);
                cg.setTableNumber(tableNumber);
                cg.setIsLeader(Common.YES);
                cg.setHeadPhoto(getCurrentCustomer().getHeadPhoto());
                cg.setCustomerName(getCurrentCustomer().getNickname());
                cg.setCustomerId(customerId);
                cg.setGroupId(groupId);
                newCustomerGroupService.dbSave(getCurrentBrandId(),cg);
                //把用户购物车groupId是空的转移到该组
                shopCartService.updateShopCartByGroupId(groupId,shopDetailId,customerId);
            }
            //其次判断这个桌子上是否只存在一个组并且组长是自己
            if (tableGroups.size() == 1 && tableGroups.get(0).getCreateCustomerId().equals(customerId)) {
                List<CustomerGroup> customerGroupList = newCustomerGroupService.getGroupByGroupId(getCurrentBrandId(),tableGroups.get(0).getGroupId());
                if (customerGroupList.size() == 1) {
                    json.put("statusCode", "200");
                    json.put("success", true);
                    json.put("message", "组内一人");
                    json.put("data", new JSONArray(tableGroups));
                    json.put("groupId", tableGroups.get(0).getGroupId());
                    ApiUtils.setBackInfo(response, json); // 返回信息设置
                } else {
                    json.put("statusCode", "200");
                    json.put("success", true);
                    json.put("message", "组内多人");
                    json.put("data", new JSONArray(tableGroups));
                    json.put("groupId", tableGroups.get(0).getGroupId());
                    ApiUtils.setBackInfo(response, json); // 返回信息设置
                }
            } else {
                json.put("statusCode", "200");
                json.put("success", true);
                json.put("message", "存在多个组或组长不是自己" + tableGroups.size());
                json.put("data", new JSONArray(tableGroups));
                json.put("groupId", tableGroups.get(0).getGroupId());
                ApiUtils.setBackInfo(response, json); // 返回信息设置
            }

        } else {
            json.put("statusCode", "201");
            json.put("success", true);
            json.put("message", "用户在一个已支付的组内");
//            json.put("data", new JSONArray(groupList));
            json.put("groupId", groupList.getGroupId());
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
    }


    @RequestMapping("/new/checkOrderItems")
    public void checkOrderItems(List<OrderItem> orderItems, String groupId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        List<ShopCart> shopCarts = shopCartService.getListByGroupId(groupId);
        for (OrderItem orderItem : orderItems) {

        }
    }
}
