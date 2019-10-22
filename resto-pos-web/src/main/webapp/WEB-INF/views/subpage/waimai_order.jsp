<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="row search border-1">
	<div class="col-sm-5">
		<div class="row"><input type="text" class="form-control" placeholder="请输入手机尾号查询订单"/></div>
	</div>
	<div class="col-sm-6">
		<div class="row">
			<div class="col-sm-4 form-label">选择打印时间段:</div>
			<div class="col-sm-6"> 
				<select class="form-control" name="DISTRIBUTION_TIME_ID" id="DISTRIBUTION_TIME_ID" onchange="selectOrderByTime(this,null)">
				</select>
			</div>
		</div>
	</div>
</div>
<div class="row order-operator">
	<div class="col-sm-5 padding-0 full-height border-1">
		<div class="order-detailed">
			<div class="title">
				<h4>订单信息（外卖订单）</h4>
			</div>
			<div class="description">
				<p><span>订单编号:</span> <span id="orderSwiftPhone"></span> </p>
				<p><span>下单门店:</span> <span id="orderShop"></span></p>
				<p><span>下单时间:</span> <span id="orderTime"></span></p>
				<p><span>顾客电话:</span> <span id="orderPhone"></span></p>
				<p><span>就餐模式:</span> <span id="orderDistributeDate"></span></p>
				<p><span>订单状态:</span> <span id="orderState"></span></p>
			</div>
			<div class="title">
				<h4>
					<span>餐品名称</span>
					<span class="pull-right" id="itemNum">数量/份</span>
				</h4>
				<div class="article-items" id="itemInfo">
				</div>
				<h4><span>餐品总数</span><span class="pull-right count-number" id="itemTotals">0</span></h4>
			</div>
			
		</div>			
	</div>
	<div class="col-sm-7 padding-0 full-height tab" >
		<table class="table table-hover table-striped call-number-table">
			 <thead>
				<tr>
					<th>手机尾号</th>
					<th>订单状态</th>
					<th>操作 <span class="remain-number" id="showNum">0</span></th>
				</tr>
			</thead>
			<tbody id="Order_Table_Tbody">
			</tbody> 
		</table>
		<div class="print-all-div">
			<button class="btn btn-block btn-success" onclick="printAllOrder()">打印全部订单</button>
		</div>
	</div>
</div>
<script>
	
	selectTime();
	//加载查询所有订单时间段
	function selectTime(){
		$.ajax({
			url:"order/listOrderTime",
			success:function(data){
				var temp_id;
				$(data).each(function(i){
					$("#DISTRIBUTION_TIME_ID").append("<option value='"+data[i].DISTRIBUTION_TIME_ID+"'>"+data[i].BEGIN_TIME+"</option>");
					//默认加载第一个时间段
					if(i==0){
						selectOrderByTime(null, data[i].DISTRIBUTION_TIME_ID);
					}
				 });
				
			}
		});
	}
	
	var allOrderByTime = null; //用于保存当前选中的时间段的所有商品
	//根据时间段显示所有订单
	function selectOrderByTime(obj,id){
		//判断 赋值 的方式
		var DISTRIBUTION_TIME_ID ;
		if(id!=null && id!=""){
			DISTRIBUTION_TIME_ID = id;
		}else{
			DISTRIBUTION_TIME_ID = $(obj).val();
		}
		//清空原始值
		$("#orderSwiftPhone").empty();
		$("#orderShop").empty();
		$("#orderTime").empty();
		$("#orderPhone").empty();
		$("#orderDistributeDate").empty();
		$("#orderState").empty();
		$("#itemInfo").empty();
		$.ajax({
			url:"order/selectOrderByTime",
			data:"DISTRIBUTION_TIME_ID="+DISTRIBUTION_TIME_ID,
			success:function(data){
				allOrderByTime = data;//保存数据；
				$("#Order_Table_Tbody").empty();//清空订单的数据
				$("#showNum").html(data.length);
				$(data).each(function(i){
					var telephone = data[i].TELEPHONE.substr(data[i].TELEPHONE.length-4);	//截取手机号
					$("#Order_Table_Tbody").append("<tr onclick='queryOrderInfo(\""+data[i].ORDER_ID+
						"\")'><td>"+telephone+"</td><td>"+data[i].orderState_new
						+"</td><td><button class='btn btn-primary'>拒绝</button></td></tr>");
				 });
			}
		});
	}
	
	//点击订单，查询订单详情
	function queryOrderInfo(orderId){
		console.log("=="+orderId);
		$.ajax({
			url:"order/queryOrderInfo",
			data:"ORDER_ID="+orderId,
			success:function(data){
				console.log(data);
				$("#orderSwiftPhone").html(data.SWIFT_NUMBER);
				$("#orderShop").html(data.SHOP_DES);
				$("#orderTime").html(new Date(data.ORDER_TIME).format("yyyy年MM月dd日 hh:mm"));
				$("#orderPhone").html(data.TELEPHONE);
				var  orderType;
				if(data.ORDER_ID==1){
					orderType="堂吃";
				}else if(data.ORDER_ID==2){
					orderType="外卖";
				}else{
					orderType="外带";
				}
				$("#orderDistributeDate").html(orderType);	//订单的类型
				$("#orderState").html(data.orderState_new);
				//显示 订单的 所有商品
				var orderItems = data.orderItems;
				$("#itemInfo").empty();	//清空上次的商品数据
				var itemsCount = 0 ;	//商品总数
				$(orderItems).each(function(i){
					$("#itemInfo").append("<p><span>"+orderItems[i].DESIGNATION+"</span><span class='pull-right'>"
							+orderItems[i].COUNT+"</span></p>");
					itemsCount += orderItems[i].COUNT;
				});
				$("#itemTotals").html(itemsCount);
			}
		});
	}
	
	//打印出当前选中的时间段的所有商品
	function printAllOrder(){
		console.log(allOrderByTime);
	}
</script>