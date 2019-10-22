<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>排号</title>
		<link rel="stylesheet" href="assets/css/weui.css" />
		<link rel="stylesheet" href="assets/css/queue.css" />
	</head>
	<body>
		<div class="main" id="app">
			<!--顶部店铺名称-->
			<div class="header">
				<a class="round_break" v-if="!connStatus">●</a>
				<div class="middle">{{shopInfo.name}}</div>
			</div>
			<!--标题-->
			<div class="title">
				<p class="numTitle middle" style="background-color: #2ecc71;">当前叫号</p>
				<p class="waitingTitle middle" style="background-color: #3498d8;">正在排队</p>
			</div>
			<!--叫号信息区域-->
			<div class="main-content">
				
				<div class="presentNumber">					
					<div class="flex-container-column" style="background-color: #27ae60;">
						<div class="tableType">
							<span>小桌(1-2)人</span>
						</div>
						<div class="tableNumber">
							<span>A8</span>
						</div>
						<div class="tableMoney">
							<span>等待桌数 8</span>
						</div>
					</div>
				</div>
				
				<div class="isWaiting">								
					<div class="flex-container-row">
						<div class="waitingRow" style="border-right:2px solid #3498d8;">
							<div class="flex-container-column" style="background-color: #2980b9;">								
								<div class="tableType">
									<span>中桌(2-4)人</span>
								</div>
								<div class="tableNumber">
									<span>B8</span>
								</div>
								<div class="tableMoney">
									<span>等待桌数 35</span>
								</div>								
							</div>
						</div>
						<div class="waitingRow">
							<div class="flex-container-column" style="background-color: #2980b9;">
								<div class="tableType">
									<span>大桌(4-8)人</span>
								</div>
								<div class="tableNumber">
									<span>F8</span>
								</div>
								<div class="tableMoney">
									<span>等待桌数 5</span>
								</div>
							</div>							
						</div>												
					</div>					
				</div>			
				
				<!--<div class="isWaiting">								
					<div class="flex-container-row">
						<div class="waitingRow" style="border-right:2px solid #3498d8;">
							<div class="flex-container-column">								
								<div class="tableType">
									<span>小桌1-2人</span>
								</div>
								<div class="tableNumber">
									<span>A8</span>
								</div>
								<div class="tableMoney">
									<span>等位红包￥2.35</span>
								</div>								
							</div>
						</div>
						<div class="waitingRow">
							<div class="flex-container-column">
								<div class="flex-container-column" style="border-bottom:2px solid #3498d8;">
									<div class="tableType min">
										<span class="min">小桌1-2人</span>
									</div>
									<div class="tableNumber min">
										<span class="min">A8</span>
									</div>
									<div class="tableMoney min">
										<span class="min">等位红包￥2.35</span>
									</div>
								</div>								
								<div class="flex-container-column">
									<div class="tableType min">
										<span class="min">小桌1-2人</span>
									</div>
									<div class="tableNumber min">
										<span class="min">A8</span>
									</div>
									<div class="tableMoney min">
										<span class="min">等位红包￥2.35</span>
									</div>
								</div>
							</div>							
						</div>												
					</div>					
				</div>-->
				
				<!--<div class="isWaiting">								
					<div class="flex-container-row">
						<div class="waitingRow" style="border-right:2px solid #3498d8;">
							<div class="flex-container-column">
								<div class="flex-container-column" style="border-bottom:2px solid #3498d8;">
									<div class="table" style="font-size: 2rem;">
										<span>小桌1-2人</span>
									</div>
									<div class="table" style="font-size: 4rem;">
										<span>A8</span>
									</div>
									<div class="table" style="font-size: 2rem;">
										<span>等位红包￥2.35</span>
									</div>
								</div>								
								<div class="flex-container-column">
									<div class="table" style="font-size: 2rem;">
										<span>小桌1-2人</span>
									</div>
									<div class="table" style="font-size: 4rem;">
										<span>A8</span>
									</div>
									<div class="table" style="font-size: 2rem;">
										<span>等位红包￥2.35</span>
									</div>
								</div>
							</div>
						</div>
						<div class="waitingRow">
							<div class="flex-container-column">
								<div class="flex-container-column" style="border-bottom:2px solid #3498d8;">
									<div class="table" style="font-size: 2rem;">
										<span>小桌1-2人</span>
									</div>
									<div class="table" style="font-size: 4rem;">
										<span>A8</span>
									</div>
									<div class="table" style="font-size: 2rem;">
										<span>等位红包￥2.35</span>
									</div>
								</div>								
								<div class="flex-container-column">
									<div class="table" style="font-size: 2rem;">
										<span>小桌1-2人</span>
									</div>
									<div class="table" style="font-size: 4rem;">
										<span>A8</span>
									</div>
									<div class="table" style="font-size: 2rem;">
										<span>等位红包￥2.35</span>
									</div>
								</div>
							</div>							
						</div>						
					</div>					
				</div>-->
				
				<!--<div class="isWaiting">								
					<div class="flex-container-row" style="height: 50%;border-bottom:2px solid #3498d8;">
						<div class="flex-container-column" style="flex: 1;border-right:2px solid #3498d8;">
							<div class="table" style="font-size: 1.5rem;">
								<span>小桌1-2人</span>
							</div>
							<div class="table" style="font-size: 5rem;">
								<span>A8</span>
							</div>
							<div class="table" style="font-size: 1.5rem;">
								<span>等位红包￥2.35</span>
							</div>
						</div>
						<div class="flex-container-column" style="flex: 1;">
							<div class="table" style="font-size: 1.5rem;">
								<span>小桌1-2人</span>
							</div>
							<div class="table" style="font-size: 5rem;">
								<span>A8</span>
							</div>
							<div class="table" style="font-size: 1.5rem;">
								<span>等位红包￥2.35</span>
							</div>
						</div>
					</div>
					<div class="flex-container-row" style="height: 50%;">
						<div class="flex-container-column" style="flex: 1;border-right:2px solid #3498d8;">
							<div class="table" style="font-size: 1.5rem;">
								<span>小桌1-2人</span>
							</div>
							<div class="table" style="font-size: 5rem;">
								<span>A8</span>
							</div>
							<div class="table" style="font-size: 1.5rem;">
								<span>等位红包￥2.35</span>
							</div>
						</div>
						<div class="flex-container-column" style="flex: 1;border-right:2px solid #3498d8;">
							<div class="table" style="font-size: 1.5rem;">
								<span>小桌1-2人</span>
							</div>
							<div class="table" style="font-size: 5rem;">
								<span>A8</span>
							</div>
							<div class="table" style="font-size: 1.5rem;">
								<span>等位红包￥2.35</span>
							</div>
						</div>
						<div class="flex-container-column" style="flex: 1;">
							<div class="table" style="font-size: 1.5rem;">
								<span>小桌1-2人</span>
							</div>
							<div class="table" style="font-size: 5rem;">
								<span>A8</span>
							</div>
							<div class="table" style="font-size: 1.5rem;">
								<span>等位红包￥2.35</span>
							</div>
						</div>
					</div>
				</div>-->
				
				<!--<div class="isWaiting">								
					<div class="flex-container-row">
						<div class="waitingRow" style="border-right:2px solid #3498d8;">
							<div class="flex-container-column">
								<div class="flex-container-column" style="border-bottom:2px solid #3498d8;">
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">小桌1-2人</span>
									</div>
									<div class="table" style="font-size: 4rem;">
										<span class="sixth">A8</span>
									</div>
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">等位红包￥2.35</span>
									</div>
								</div>								
								<div class="flex-container-column">
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">小桌1-2人</span>
									</div>
									<div class="table" style="font-size: 4rem;">
										<span class="sixth">A8</span>
									</div>
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">等位红包￥2.35</span>
									</div>
								</div>
							</div>
						</div>
						<div class="waitingRow">
							<div class="flex-container-column">
								<div class="flex-container-column" style="border-bottom:2px solid #3498d8;">
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">小桌1-2人</span>
									</div>
									<div class="table" style="font-size: 4rem;">
										<span class="sixth">A8</span>
									</div>
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">等位红包￥2.35</span>
									</div>
								</div>								
								<div class="flex-container-column">
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">小桌1-2人</span>
									</div>
									<div class="table" style="font-size: 4rem;">
										<span class="sixth">A8</span>
									</div>
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">等位红包￥2.35</span>
									</div>
								</div>
							</div>							
						</div>
						<div class="waitingRow">
							<div class="flex-container-column">
								<div class="flex-container-column" style="border-bottom:2px solid #3498d8;">
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">小桌1-2人</span>
									</div>
									<div class="table" style="font-size: 4rem;">
										<span class="sixth">A8</span>
									</div>
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">等位红包￥2.35</span>
									</div>
								</div>								
								<div class="flex-container-column" style="background-color: #2980b9;">
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">小桌1-2人</span>
									</div>
									<div class="table" style="font-size: 4rem;">
										<span class="sixth">A8</span>
									</div>
									<div class="table" style="font-size: 2rem;">
										<span class="sixth">等位红包￥2.35</span>
									</div>
								</div>
							</div>							
						</div>
					</div>					
				</div>-->
				
			</div>						
			<!--底部提示区域-->
			<div class="footer">
				<div class="middle">{{shopInfo.notice}}</div>
			</div>
		</div>
		<script src="assets/js/jquery.min.js"></script>
		<script src="assets/js/vue.min.js"></script>
		<script src="assets/js/sockjs.min.js"></script>
	</body>
	<script>
		var vm = new Vue({
			el : "#app",
			data : {
				shopInfo : { 
					name : "简厨是一种生活方式",
					notice : "温馨提示:尊敬的顾客,请您保管好自己的贵重物品,请根据叫号信息就号取餐,谢谢!"
				} , //店铺信息
				currentQueue : {} , //当前叫号信息
				queueList: [] , //所有的等位信息
				connStatus : true , //socket 连接状态
			},
			created : function(){
				
			},
			methods : function(){
				
			}
		})
		
	</script>
</html>
