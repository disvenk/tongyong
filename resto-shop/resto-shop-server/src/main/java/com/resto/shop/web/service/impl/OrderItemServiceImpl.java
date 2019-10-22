package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.constant.OrderItemType;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.dao.CustomerMapper;
import com.resto.shop.web.constant.PayType;
import com.resto.shop.web.dao.OrderItemMapper;
import com.resto.shop.web.dto.GrantArticleInfoDto;
import com.resto.shop.web.dto.GrantInfoDto;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 *
 */
@Component
@Service
public class OrderItemServiceImpl extends GenericServiceImpl<OrderItem, String> implements OrderItemService {

    @Resource
    private OrderItemMapper orderitemMapper;

    @Autowired
    private OrderPaymentItemService orderPaymentItemService;

    @Autowired
    private OrderItemActualService orderItemActualService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    BrandSettingService brandSettingService;

    @Autowired
    OrderService orderService;

    @Autowired
    ShopDetailService shopDetailService;

    @Autowired
    CustomerMapper customerMapper;


    @Override
    public GenericDao<OrderItem, String> getDao() {
        return orderitemMapper;
    }

    @Override
    public List<OrderItem> listByOrderId(Map<String, String> param) {
        List<OrderItem> orderItems = orderitemMapper.listByOrderId(param);

        List<OrderItem> other = orderitemMapper.listTotalByOrderId(param.get("orderId"));

        orderItems.addAll(other);

        return getOrderItemsWithChild(orderItems);
    }

    @Override
    public List<OrderItem> listByOrderIdPos(Map<String, String> param) {
        List<OrderItem> orderItems = orderitemMapper.listByOrderIdPos(param);

        List<OrderItem> other = orderitemMapper.listTotalByOrderId(param.get("orderId"));

        orderItems.addAll(other);

        return getOrderItemsWithChild(orderItems);
    }

    @Override
    public List<OrderItem> listArticle(String orderId) {
        List<OrderItem> other = orderitemMapper.listArticle(orderId);
        return other;
    }


    @Override
    public List<OrderItem> listByParentId(String orderId) {
        return orderitemMapper.listByParentId(orderId);
    }

    @Override
    public int selectCountSum(String orderId) {

        return orderitemMapper.selectCountSum(orderId);
    }


    @Override
    public int selectRefundCount(String orderId) {

        return orderitemMapper.selectRefundCount(orderId);
    }

    @Override
    public int selectJingCount(String orderId) {
        return orderitemMapper.selectJingCount(orderId);
    }

    @Override
    public void updateOrderIdByBeforeId(String orderId, String beforeId) {
        orderitemMapper.updateOrderIdByBeforeId(orderId, beforeId);
    }

    List<OrderItem> getOrderItemsWithChild(List<OrderItem> orderItems) {
        log.debug("这里查看套餐子项: ");
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

    @Override
    public void insertItems(List<OrderItem> orderItems) {
        //不进行合并新规格
        orderitemMapper.insertBatch(orderItems);
        //得到套餐子品
        List<OrderItem> allChildren = new ArrayList<>();
        orderItems.forEach(orderItem -> {
            if (orderItem.getChildren() != null && !orderItem.getChildren().isEmpty()) {
                allChildren.addAll(orderItem.getChildren());
            }
        });
        //插入套餐子品
        if (!allChildren.isEmpty()) {
            orderitemMapper.insertBatch(allChildren);
        }
    }

    @Override
    public List<OrderItem> selectSaleArticleByDate(String shopId, String beginDate, String endDate, String sort) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        if ("0".equals(sort)) {
            sort = "f.peference ,a.sort";
        } else if ("desc".equals(sort)) {
            sort = "brand_report.brandSellNum desc";
        } else if ("asc".equals(sort)) {
            sort = "brand_report.brandSellNum asc";
        }
        return orderitemMapper.selectSaleArticleByDate(begin, end, shopId, sort);
    }

    @Override
    public List<OrderItem> listByOrderIds(List<String> childIds) {
        if (childIds == null || childIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<OrderItem> orderItems = orderitemMapper.listByOrderIds(childIds);

        return getOrderItemsWithChild(orderItems);
    }

	public List<Map<String, Object>> selectOrderItems(String beginDate, String endDate) {
		Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
		return orderitemMapper.selectOrderItems(begin, end);
	}

	@Override
	public List<OrderItem> selectOrderItemByOrderIds(Map<String, Object> map) {
		return orderitemMapper.selectOrderItemByOrderIds(map);
	}

	@Override
	public List<OrderItem> selectOrderItemByOrderId(Map<String, Object> map) {
		return orderitemMapper.selectOrderItemByOrderId(map);
	}
	
	@Override
	public List<OrderItem> selectRefundOrderItem(Map<String, Object> map) {
		return orderitemMapper.selectRefundOrderItem(map);
	}

    @Override
    public List<OrderItem> getListByParentId(String parentId) {
        return orderitemMapper.getListByParentId(parentId);
    }

    @Override
    public List<OrderItem> getListByRecommendId(String recommendId,String orderId) {
        return orderitemMapper.getListByRecommendId(recommendId,orderId);
    }

    @Override
    public List<OrderItem> selectRefundArticleItem(String orderId) {
        return orderitemMapper.selectRefundArticleItem(orderId);
    }

    @Override
    public List<OrderItem> selectByArticleIds(String[] articleIds) {
        return orderitemMapper.selectByArticleIds(articleIds);
    }

    @Override
    public void posSyncDeleteByOrderId(String orderId) {
        orderitemMapper.posSyncDeleteByOrderId(orderId);
    }

    @Override
    public List<OrderItem> getOrderBefore(String tableNumber, String shopId, String customerId) {
        return orderitemMapper.getOrderBefore(tableNumber, shopId, customerId);
    }

    @Override
    public List<OrderItem> posSyncListByOrderId(String orderId) {
        return orderitemMapper.posSyncListByOrderId(orderId);
    }

    /**
     * 插入或者更新
     * @param orderItem
     */
    @Override
    public void updateOrinsertOrderItem(OrderItem orderItem) {
        orderitemMapper.updateOrinsertOrderItem(orderItem);
    }

    @Override
    public void insertOrUpdateOrderItems(List<OrderItem> orderItemList) {
        if(orderItemList != null && !orderItemList.isEmpty() && orderItemList.size() > 0){
            orderitemMapper.insertOrUpdateOrderItems(orderItemList);
        }
    }

    @Override
    public void updateOrderItemActualAmount(Order order, Boolean refundArticle) {
        log.info("订单已支付，修改菜品项实际支付金额。订单Id：" + order.getId());
        BrandSetting brandSetting = brandSettingService.selectByBrandId(order.getBrandId());
        //最终执行插入的集合
        List<OrderItemActual> orderItemActualList = new ArrayList<>();
        List<OrderPaymentItem> paymentItemList = orderPaymentItemService.selectByOrderId(order.getId());
        if (refundArticle) {
            //订单执行了退菜，父子订单菜品一起计算实收
            List<OrderPaymentItem> parentPaymentItemList = orderPaymentItemService.selectParentPaymentByOrderId(order.getId());
            paymentItemList.addAll(parentPaymentItemList);
        }
        //微信+支付宝+银联+现金+充值金额+闪惠+团购+卡充值
        BigDecimal actualAmount = paymentItemList.stream().filter(item -> item.getPaymentModeId().equals(PayMode.WEIXIN_PAY)
                || item.getPaymentModeId().equals(PayMode.ALI_PAY) || item.getPaymentModeId().equals(PayMode.BANK_CART_PAY)
                || item.getPaymentModeId().equals(PayMode.CRASH_PAY) || item.getPaymentModeId().equals(PayMode.CHARGE_PAY)
                || item.getPaymentModeId().equals(PayMode.SHANHUI_PAY) || item.getPaymentModeId().equals(PayMode.GROUP_PAY)
                || item.getPaymentModeId().equals(PayMode.CARD_CHARGE_PAY) || item.getPaymentModeId().equals(PayMode.REFUND_CRASH))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO,BigDecimal::add);
        //订单金额
        BigDecimal orderMoney = order.getAmountWithChildren().compareTo(BigDecimal.ZERO) > 0 ? order.getAmountWithChildren() : order.getOrderMoney();
        List<OrderItem> orderItemList = posSyncListByOrderId(order.getId());
        //判断订单支付模式
        if ((order.getPayType().equals(PayType.NOPAY) && StringUtils.isBlank(order.getParentOrderId())) || refundArticle) {
            //当前订单为后付模式，且是支付主订单或者父子订单一起支付
            List<OrderItem> parentOrderItemList;
            //如果是先付产生退菜，需要找到已支付订单的菜品项计算实收
            if (order.getPayType().equals(PayType.PAY)) {
                parentOrderItemList = selectParentItemByOrderId(order.getId());
            } else {
                parentOrderItemList = listByParentId(order.getId());
            }
            orderItemList.addAll(parentOrderItemList);
        }
        //菜品库中套餐逻辑已变更，不需要再去计算子品价格
        if (brandSetting.getOpenArticleLibrary().equals(Common.NO)) {
            //计算套餐子品金额
            calculationOfProducts(orderItemList);
        }
        //当前订单的实际支付/订单金额比率
        BigDecimal discount = orderMoney.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : actualAmount.divide(orderMoney,2, BigDecimal.ROUND_HALF_UP);
        //计算实际价格
        AtomicReference<Integer> index = new AtomicReference<>(1);
        AtomicReference<BigDecimal> indexBigdec = new AtomicReference<>(BigDecimal.ZERO);
        //计算当前订单菜品项实际支付
        orderItemList.forEach(orderItem -> {
            OrderItemActual orderItemActual = setOrderItemActual(orderItem, discount, orderItemList, orderItemActualList);
            index.updateAndGet(v -> v + 1);
            if (!orderItem.getType().equals(OrderItemType.SETMEALS)) {
                //套餐主品金额实收总计另算
                indexBigdec.updateAndGet(bigDecimal -> bigDecimal.add(orderItemActual.getActualAmount()));
            }
        });
        finalCalculate(orderItemActualList, indexBigdec, actualAmount);
    }

    /**
     * 先付、后付计算菜品实收跟支付实收的差异
     * @param orderItemActualList
     * @param indexBigdec
     * @param actualAmount
     */
    private void finalCalculate(List<OrderItemActual> orderItemActualList, AtomicReference<BigDecimal> indexBigdec, BigDecimal actualAmount){
        //比较实收跟除子品外菜品计算下来的实收之间是否有差价
        BigDecimal differenceValue = actualAmount.subtract(indexBigdec.get());
        AtomicReference<BigDecimal> differenceValueAto = new AtomicReference<>(differenceValue);
        orderItemActualList.stream().filter(orderItemActual -> orderItemActual.getActualAmount().compareTo(BigDecimal.ZERO) > 0
                && !orderItemActual.getType().equals(OrderItemType.SETMEALS)).findAny().ifPresent(orderItemActual ->
                {
                    orderItemActual.setActualAmount(orderItemActual.getActualAmount().add(differenceValue));
                    differenceValueAto.updateAndGet(bigDecimal -> BigDecimal.ZERO);
                });
        if (differenceValueAto.get().compareTo(BigDecimal.ZERO) != 0) {
            //存在着套餐主品设置了价格，但是下面所有子品都没设置差价、且都没原价的菜品
            orderItemActualList.stream().filter(orderItemActual -> orderItemActual.getType().equals(OrderItemType.MEALS_CHILDREN)).findAny()
                    .ifPresent(orderItemActual -> {
                        orderItemActual.setActualAmount(orderItemActual.getActualAmount().add(differenceValue));
                    });
        }
        //执行插入
        orderItemActualList.forEach(orderItemActual -> {
            orderItemActualService.insert(orderItemActual);
        });
    }

    /**
     * 插入订单菜品项实际金额
     * @param orderItem
     * @param discount
     * @param orderItemList
     * @return
     */
    private OrderItemActual setOrderItemActual(OrderItem orderItem, BigDecimal discount, List<OrderItem> orderItemList
            , List<OrderItemActual> orderItemActualList){
        BigDecimal finalPrice = orderItem.getFinalPrice();
        if (orderItem.getType().equals(OrderItemType.SETMEALS)) {
            //当前菜品为套餐主品，计算实时需要将有差价子品金额加起来
            BigDecimal parentPrice = orderItemList.stream().filter(item -> Optional.ofNullable(item.getParentId()).orElse("")
                    .equalsIgnoreCase(orderItem.getId()) && item.getFinalPrice().compareTo(BigDecimal.ZERO) > 0 &&
                    !Optional.ofNullable(orderItem.getIsAssignment()).orElse(false)).map(OrderItem::getFinalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            finalPrice = finalPrice.add(parentPrice);
        }
        BigDecimal itemActualAmount = finalPrice.multiply(discount).setScale(2, BigDecimal.ROUND_DOWN);
        OrderItemActual orderItemActual = new OrderItemActual();
        orderItemActual.setOrderId(orderItem.getOrderId());
        orderItemActual.setOrderItemId(orderItem.getId());
        orderItemActual.setActualAmount(itemActualAmount);
        orderItemActual.setCount(orderItem.getCount());
        orderItemActual.setType(orderItem.getType());
        orderItemActualList.add(orderItemActual);
        return orderItemActual;
    }

    /**
     * 计算套餐子品价格
     * @param orderItemList
     */
    private void calculationOfProducts(List<OrderItem> orderItemList){
        //计算套餐下子品的金额
        List<OrderItem> setMealsItemList = orderItemList.stream().filter(orderItem -> orderItem.getType().equals(OrderItemType.SETMEALS)).collect(Collectors.toList());
        setMealsItemList.forEach(setMealsItem -> {
            //无差价的子品集合
            List<OrderItem> childrenItemList = orderItemList.stream().filter(orderItem -> Optional.ofNullable(orderItem.getParentId()).orElse("")
                    .equalsIgnoreCase(setMealsItem.getId()) && orderItem.getFinalPrice().compareTo(BigDecimal.ZERO) == 0).collect(Collectors.toList());
            List<String> childrenItemListArticleIds = childrenItemList.stream().map(OrderItem::getArticleId)
                    .collect(Collectors.toList());
            //筛选出无差价但菜品有设置原价或粉丝价
            List<String> articleList = new ArrayList<>();
            Map<String, BigDecimal> map = new HashMap<>();
            AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);
            for (String articleId : childrenItemListArticleIds) {
                Article article = articleService.selectById(articleId);
                BigDecimal price = Optional.ofNullable(article.getFansPrice()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0
                            ? article.getFansPrice() : Optional.ofNullable(article.getPrice()).orElse(BigDecimal.ZERO);
                if (price.compareTo(BigDecimal.ZERO) > 0) {
                    articleList.add(article.getId());
                    map.put(article.getId(), price);
                    total.updateAndGet(bigDecimal -> bigDecimal.add(price));
                }
            }
            List<OrderItem> finalChildrenItemList = childrenItemList.stream().filter(orderItem -> articleList.stream()
                    .anyMatch(articleId -> articleId.equalsIgnoreCase(orderItem.getArticleId()))).collect(Collectors.toList());
            //计算无差价但菜品有设置原价或粉丝价的菜品实收(用套餐主品价格计算)
            AtomicReference<BigDecimal> totalFinalPrice = new AtomicReference<>(BigDecimal.ZERO);
            finalChildrenItemList.forEach(orderItem -> {
                BigDecimal discount = map.get(orderItem.getArticleId()).divide(total.get(), 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal finalPrice = setMealsItem.getFinalPrice().multiply(discount).setScale(2, BigDecimal.ROUND_DOWN);
                orderItem.setFinalPrice(finalPrice);
                orderItem.setIsAssignment(true);
                totalFinalPrice.updateAndGet(bigDecimal -> bigDecimal.add(finalPrice));
            });
            BigDecimal difference = setMealsItem.getFinalPrice().subtract(totalFinalPrice.get());
            finalChildrenItemList.stream().findAny().ifPresent(orderItem -> orderItem.setFinalPrice(orderItem.getFinalPrice().add(difference)));
        });
    }

    @Override
    public List<OrderItem> selectParentItemByOrderId(String orderId) {
        return orderitemMapper.selectParentItemByOrderId(orderId);
    }

    @Override
    public List<GrantArticleInfoDto> getGrantArticleList(String beginDate, String endDate) {
        beginDate = beginDate+" 00:00:00";
        endDate = endDate+" 23:59:59";
        List<GrantArticleInfoDto> grantArticleInfoDtos = orderitemMapper.grantArticleList(beginDate, endDate);

        grantArticleInfoDtos.forEach(n->{
            String refundRemark = "";
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(n.getShopDetailId());
            String familyName = orderitemMapper.selectFamilyName(n.getArticleId());
            List<String> list = orderitemMapper.selectRefundRemark(n.getArticleId(), n.getOrderId());
            if(list!=null && !list.isEmpty()){
                 refundRemark = orderitemMapper.selectRefundRemark(n.getArticleId(), n.getOrderId()).get(0);
            }
            n.setShopName(shopDetail.getName());
            n.setMoney(n.getUnitPrice().multiply(new BigDecimal(n.getCount())));
            n.setFamilyName(familyName);
            n.setRefundMark(refundRemark);
        })  ;
        return grantArticleInfoDtos;
    }

    @Override
    public GrantInfoDto getGrantInfo(String itemId) {
        GrantInfoDto grantInfo = orderitemMapper.getGrantInfo(itemId);
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(grantInfo.getShopDetailId());
        Customer customer = customerMapper.selectByPrimaryKey(grantInfo.getCusotmerId());
        grantInfo.setMoney(grantInfo.getUnitPrice().multiply(new BigDecimal(grantInfo.getCount())));
        if(customer!=null){
            grantInfo.setNickName(customer.getNickname());
            grantInfo.setTel(customer.getTelephone()==null?"未注册":customer.getTelephone());
        }else {
            grantInfo.setNickName("newPos开台订单");
            grantInfo.setTel("---");
        }
        grantInfo.setShopName(shopDetail.getName());
        return grantInfo;
    }
}