<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="full-height">
	<div>
		<order-detailed v-for="order in orderList" :order="order" :customer="customer"></order-detailed>
		<div style="line-height:50vh;text-align:center;color: #ccc;font-size:9vw;" v-if="orderList.length==0">
			<h3>暂无订单</h3>
		</div>
	</div>	
</div>