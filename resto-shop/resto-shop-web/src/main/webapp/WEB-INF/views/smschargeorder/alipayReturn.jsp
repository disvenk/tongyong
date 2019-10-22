<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<base href="<%=basePath%>">
<title>餐加| 充值提示</title>
<link href="assets/global/plugins/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="assets/pages/img/favicon.ico" />
</head>
<body style='height: 100%; overflow: hidden; background-color: #DDD'>
	<div class="row" style="margin-top: 200px;">
		<div class="col-md-6 col-md-offset-3">
			<c:if test="${returnParams.restoResut}">
				<div class="panel panel-success">
					<div class="panel-heading text-center">
						<h1>充值成功</h1>
					</div>
					<div class="panel-body" style="font-size: 20px">
						<div class="col-md-offset-2">
							<dl class="dl-horizontal">
								<dt>订单编号：</dt>
								<dd>${returnParams.out_trade_no}</dd>
								<dt>商品名称：</dt>
								<dd>${returnParams.subject }</dd>
								<dt>充值金额：</dt>
								<dd>${returnParams.total_fee}</dd>
								<dt>订单状态：</dt>
								<dd id="successStatus"></dd>
							</dl>
						</div>
						<div class="col-md-4 col-md-offset-4">
							<button class="btn btn-danger btn-block" onclick="javascript:window.opener=null;window.open('','_self');window.close();">关闭页面</button>
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${!returnParams.restoResut}">
				<div class="panel panel-danger">
					<div class="panel-heading text-center">
						<h1>充值失败</h1>
					</div>
					<div class="panel-body" style="font-size: 20px">
						<div class="col-md-offset-2">
							<dl class="dl-horizontal">
							<dt>订单编号：</dt>
							<dd>${returnParams.out_trade_no}</dd>
							<dt>商品名称：</dt>
							<dd>${returnParams.subject }</dd>
							<dt>充值金额：</dt>
							<dd>${returnParams.total_fee}</dd>
							<dt>订单状态：</dt>
							<dd id="falseStatus"></dd>
						</dl>
						</div>
						<div class="col-md-4 col-md-offset-4">
							<button class="btn btn-danger btn-block" onclick="javascript:window.opener=null;window.open('','_self');window.close();">关闭页面</button>
						</div>
					</div>
				</div>
			</c:if>
		</div>
	</div>
	<script src="assets/global/plugins/jquery.min.js"
		type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){
			var status = "${returnParams.trade_status}";
			var str = "";
			if (status == "WAIT_BUYER_PAY") {
				str = "等待付款";
			} else if (status == "TRADE_CLOSED") {
				str = "取消交易";
			} else if (status == "TRADE_SUCCESS") {
				str = "交易成功";
			} else if (status == "TRADE_PENDING") {
				str = "等待卖家收款";
			} else if (status == "TRADE_FINISHED") {
				str = "交易完成";
			}
			$("#successStatus").html(str);
			$("#falseStatus").html(str);
		})
	</script>
</body>
</html>