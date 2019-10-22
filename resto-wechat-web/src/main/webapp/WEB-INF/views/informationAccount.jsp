<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<base href="<%=basePath%>">
	<title>账户余额</title>
	<!-- 禁止缩放 -->
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui">
	<link rel="stylesheet" href="assets/css_old/base.css">
	<link rel="stylesheet" href="assets/css_old/global.css">
	<link rel="stylesheet" href="assets/css_old/iconfont.css">
	<link rel="stylesheet" href="assets/css_old/information.css">
</head>
<body  style="height:100%;background:none;overflow: hidden;">

	<!-- 内容开始 -->
		<div class="content information informationAccount">
			<div class="balance" style="border-radius: 10px 10px 0px 0px;">
				<a href="javascript:()">
					<span class="text-main">当前余额</span>
					<span class="text-right priceText red" style="top:30%">${data.remain }</span>
				</a>
			</div>
		</div>
		
	<script src="assets/js/zepto.min.js"></script>
	<script src="//cdn.bootcss.com/iScroll/5.1.3/iscroll.min.js"></script>
	<script src="//cdn.bootcss.com/vue/1.0.17/vue.min.js"></script>
	<script>
		var h = $(".informationAccount").height();
		var bodyH = $("body").height();
	</script> 
	
	<div id="scroll-wapper" style="overflow: hidden;">
		<ul class="runningList">
			<c:forEach items="${data.accountLogs}" var="log">
				<li>
					<a>
						<span class="left" v-bind:class="[classA, ${log.paymentType==1} ? classB : '']">${log.paymentType==1?"+":"-"}${log.money}</span>
						<span class="left">${log.remark}</span>
						<span class="right"><fmt:formatDate value="${log.createTime }" pattern="yyyy-MM-dd HH:mm"/> </span>
	<%-- 					<span class="right">余额：${log.remain }</span> --%>
					</a>
				</li>
			</c:forEach>
		</ul>
	</div>
	
	<script>
		$("#scroll-wapper").height(bodyH-h);
		var isc = new IScroll("#scroll-wapper");
		document.addEventListener('touchmove', function(e) {
			e.preventDefault();
		}, false);
				
	</script>
	<script>
		new Vue({
		  el: '#scroll-wapper',
		  data: {
		    classA: 'plus',
  			classB: 'minus',
		  }
		})
	</script>
</body>
</html>