package com.resto.shop.web.container;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.resto.shop.web.model.Order;
import com.resto.shop.web.service.CustomerService;
import com.resto.shop.web.service.OrderService;


/**
 * 订单生产状态容器
 * @author Diamond
 * @date 2016年4月21日
 */
@Component
public class OrderProductionStateContainer {
	private static final Map<String,Map<String,Order>> PUSH_ORDER_MAP=new ConcurrentHashMap<>(); //已下单的队列
	private static final Map<String,Map<String,Order>> READY_ORDER_MAP = new ConcurrentHashMap<>();  //准备中的队列
	private static final Map<String,Map<String,Order>> CALL_NOW_MAP = new ConcurrentHashMap<>();     //正在叫号的队列
	private static final Map<String,Boolean> INIT_SHOP = new ConcurrentHashMap<>();
	
	
	@Resource
	OrderService orderService;
	@Resource
	CustomerService customerService;
	
	
	public List<Order> getPushOrderList(String shopId,Long lastTime){
		initShop(shopId);
		Map<String,Order> orderMap = getOrderMap(PUSH_ORDER_MAP, shopId);
		List<Order> orderList = new ArrayList<>(orderMap.values());
		if(lastTime==null){
			return orderList;
		}
		List<Order> newList = new ArrayList<>();
		for(Order order:orderList){
			if(order.getPushOrderTime().getTime()>lastTime){
				newList.add(order);
			}
		}
		return newList;
	}
	
	public List<Order> getReadyOrderList(String shopId,Long lastTime){
		initShop(shopId);
		Map<String,Order> orderMap = getOrderMap(READY_ORDER_MAP, shopId);
		List<Order> orderList = new ArrayList<>(orderMap.values());
		if(lastTime==null){
			return orderList;
		}
		List<Order> newList = new ArrayList<>();
		for(Order order:orderList){
			if(order.getPrintOrderTime().getTime()>lastTime){
				newList.add(order);
			}
		}
		return newList;
	}
	
	public List<Order> getCallNowList(String shopId, Long lastTime){
		Map<String,Order> orderMap = getOrderMap(CALL_NOW_MAP, shopId);
		List<Order> orderList = new ArrayList<>(orderMap.values());
		if(lastTime==null){
			return orderList;
		}
		List<Order> newList = new ArrayList<>();
		for(Order order:orderList){
			if(order.getCallNumberTime().getTime()>lastTime){
				newList.add(order);
			}
		}
		return newList;
	}
	
//	public void addOrder(Order order){
//		String shopId = order.getShopDetailId();
//		if(order.getProductionStatus()==ProductionStatus.HAS_ORDER){
//			Map<String,Order> orderMap = getOrderMap(PUSH_ORDER_MAP,shopId);
//			orderMap.put(order.getId(), order);
//		}else if(order.getProductionStatus()==ProductionStatus.HAS_CALL){
//			Map<String,Order> orderMap = getOrderMap(CALL_NOW_MAP, shopId);
//			orderMap.put(order.getId(), order);
//			if(order.getCustomer()==null){
//				Customer cus = customerService.selectById(order.getCustomerId());
//				order.setCustomer(cus);
//			}
//			Map<String,Order> readyOrder = getOrderMap(READY_ORDER_MAP, shopId);
//			if(readyOrder.containsKey(order.getId())){ //如果准备中的队列中有该订单，则删除该订单
//				readyOrder.remove(order.getId());
//			}
//		}else if(order.getProductionStatus()==ProductionStatus.PRINTED){
//			Map<String,Order> orderMap = getOrderMap(READY_ORDER_MAP,shopId);
//			orderMap.put(order.getId(), order);
//			Map<String,Order> pushOrderMap = getOrderMap(PUSH_ORDER_MAP, shopId);
//			if(pushOrderMap.containsKey(order.getId())){
//				pushOrderMap.remove(order.getId());
//			}
//		}
//	}


	private void initShop(String shopId) {
		if(!INIT_SHOP.containsKey(shopId)){
			INIT_SHOP.put(shopId, true);
//			List<Order> order = orderService.selectTodayOrder(shopId,new int[]{ProductionStatus.HAS_ORDER,ProductionStatus.PRINTED});
//			for (Order o: order) {
//				addOrder(o);
//			}
		}
	}

	private Map<String, Order> getOrderMap(Map<String, Map<String, Order>> map, String shopId) {
		Map<String,Order> ordermap =map.get(shopId);
		if(ordermap==null){
			ordermap = new LinkedHashMap<>();
			map.put(shopId, ordermap);
		}
		return ordermap;
	}

	public void removePushOrder(Order order) {
		getOrderMap(PUSH_ORDER_MAP, order.getShopDetailId()).remove(order.getId());
	}

	public void clearMap(String currentShopId) {
		PUSH_ORDER_MAP.remove(currentShopId);
		READY_ORDER_MAP.remove(currentShopId);
		CALL_NOW_MAP.remove(currentShopId);
		INIT_SHOP.remove(currentShopId);
	}
	
	

}
