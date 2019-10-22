/**
 * 已提交 未付款
 */
var SUBMIT = 1;
/**
 * 已付款
 */
var PAYMENT = 2;
/**
 * 厨房制作中
 */
var KITCHENPROCESS = 3;
/**
 * 厨房制作完成
 */
var KITCHENDONE = 4;
/**
 * 已准备好
 */
var READY = 5;
/**
 * 派送中
 */
var EXPRESS = 6;
/**
 * 已到达
 */
var ARRIVED = 7;
/**
 * 已过期
 */
var EXPIRED = 8;
/**
 * 已取消
 */
var CANCEL = 9; // 订单已取消

/**
 * 已确认
 */
var CONFIRM = 10;

/**
 * 已评价
 */
var HASAPPRAISE = 11;

/**
 * 已分享
 */
var SHARED = 12;
function order2li(order,phone) {
	var status = order.ORDER_STATE;
	var text = "";
	var styleCss = "";
	var button="";

	switch (status) {
	case SUBMIT:
		text = "等待付款";
		button = "<p data-remain='"
				+ order.REMAIN_TIME
				+ "' class='show-remain-time'></p>"
				+ "<button type='button' class='btn minor cancel-order-btn' >取消订单</button>"
				+ "<button type='button' class='btn major' onclick='payOrder(this)'>付款</button>";
		break;
	case PAYMENT:
		text = "待消费";
		button = "<p class='use-code'>消费码:" + phone.substr(7) + "</p>";
		if (order.DISTRIBUTION_MODE_ID == 2) {
			button += "<a class='btn minor' href='tel:" + order.EMP_PHONE
					+ "'><i class=' icon-phone'></i> "
					+ (order.EMP_NAME || "配送员") + "</a>";
		}
		button += "<button type='button' class='btn minor cancel-order-btn' >取消订单</button>";
		if(order.DISTRIBUTION_MODE_ID==1||order.DISTRIBUTION_MODE_ID==3){
			button+="<button type='button' class='btn minor push-order-btn'>确定下单</button>";
		}
		break;
	case CONFIRM:
		text = "等待评价";
		if (order.DISTRIBUTION_MODE_ID == 2) {
			button = "<p class='use-code'>消费码:" + phone.substr(7) + "</p>";
			button += "<a class='btn minor' href='tel:" + order.EMP_PHONE
					+ "'><i class=' icon-phone'></i> "
					+ (order.EMP_NAME || "配送员") + "</a>";
		}
		button += "<button type='button' class='btn major' onclick='appraiseOrder(this)'><i class=' icon-edit'></i>  写评价</button>";
		break;
	case CANCEL:
		text = "交易取消";
		styleCss = "cancel";
		break;
	case HASAPPRAISE:
		text = "已评价"
		button = "<button type='button' class='btn major' onclick='shareOrder(this)'>分享</button>";
		break;
	case SHARED:
		text = "已分享"
		break;
	default:
		text = "订单处理中"
		break;
	}
	order.ORDER_TIME = new Date(order.ORDER_TIME).format("yyyy-MM-dd hh:mm");
	var orderItem = $(
			'<li class="order-item">                                                                             '+
			'	<div class="orderListTitle">					                                                 '+
			'		<p>[<span data-key="MODE_NAME">外卖</span>]<span data-key="RESTAURANT_NAME"></span></p>      '+
			'		<p class="orderListTitleInfo" data-key="ADDRESS">就餐地址</p>                                '+
			'		<p class="orderListTitleInfo" data-key="TIME">取餐时间</p>                                   '+
			'		<p class="orderListTitleInfo" data-key="PHONE">消费手机</p>                                  '+
			'		<p class="orderListTitleInfo" data-key="ORDER_TIME">2015-09-08 12:16</p>                     '+
			'		<span class="orderStatus" data-key="STATE_TEXT">订单状态</span>                              '+
			'	</div>                                                                                           '+
			'	<p class="article-names"></p>                                                                    '+
			'	<div class="orderAmount">                                                                        '+
			'		<span class="priceText" data-key="PAYMENT_AMOUNT">188.00</span>                              '+
			'		<span>共计<i data-key="ARTICLE_COUNT">6</i>个餐品</span>                                     '+
			'	</div>		                                                                                     '+
			'	<div class="orderOperation">                                                                     '+
			'		<button type="button" class="btn major">分享</button>                                        '+
			'	</div>                                                                                           '+
			'</li>                                                                                               '		
	);
	
	order.PHONE = "消费手机: " + b(phone);
	order.STATE_TEXT = text;
	if (order.DISTRIBUTION_MODE_ID == 1||order.DISTRIBUTION_MODE_ID==3) {
		order.MODE_NAME = "堂吃";
		order.ADDRESS = "堂吃地址: " + b(order.ADDRESS);
		if(order.DISTRIBUTION_MODE_ID==3){
			order.MODE_NAME ="外带";
			order.ADDRESS = "外带地址: " + b(order.ADDRESS);
		}
		order.RESTAURANT_NAME = order.SHOP_DES;
		order.ORDER_TIME="下单时间: "+b(order.ORDER_TIME);
		$(orderItem).find("[data-key='TIME']").remove();
	} else {
		order.MODE_NAME = "外卖";
		order.ADDRESS = "取餐地址: " + b(order.DETAIL);
		order.RESTAURANT_NAME = order.DELIVERY_POINT_NAME;
		order.TIME = "取餐时间: "
				+ b(order.DISTRIBUTION_DATE + " " + order.DISTRIBUTION_TIME);
		$(orderItem).find("[data-key='ORDER_TIME']").remove();
	}
	orderItem.setAjaxParam(order);
	orderItem.attr("data-id", order.ORDER_ID);
	var nameArr = [];
	for ( var i in order.ARTICLE_NAMES) {
		var name = order.ARTICLE_NAMES[i];
		nameArr.push("<span>" + name + "</span>");
		// TODO getName
	}
	nameArr.push("<div style='clear:both'></div>")
	orderItem.find(".article-names").html(nameArr);
	orderItem.find(".orderStatus").addClass(styleCss);
	var btns = $(button);
	orderItem.find(".orderOperation").html(btns);
	handlerOrderItemLi(orderItem,order);
	
	return orderItem;
}

function handlerOrderItemLi(orderItem,order){
	var oid = order.ORDER_ID;
	orderItem.find(".push-order-btn").click(function(){
		OrderControl.pushOrder(oid,function(){
			wxToast.success("下单成功");
			OrderControl.flush();
		});
	});
	
	orderItem.find(".cancel-order-btn").click(function(){
		OrderControl.cancelOrder(oid,function(){
			wxToast.success("取消成功");
			OrderControl.flush();
		});
	});
}

/**
 * 给字符串添加上b标签
 */
function b(str) {
	return "<b>" + str + "</b>";
}