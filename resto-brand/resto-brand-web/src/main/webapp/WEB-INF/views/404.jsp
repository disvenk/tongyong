<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>

<html>
	<head>
		<title>品牌管理</title>
		<link rel="shortcut icon" href="assets/pages/img/favicon.ico" />
	</head>
	<body style="text-align: center;">
		<h1>404</h1>
		<h2>没有找到该页面哦！！</h2>
		<p>3秒后将自动返回首页</p>
		<a href="<%=basePath%>">点此返回首页</a>
	</body>
	<script type="text/javascript">
		setTimeout(function(){
			window.location.href="<%=basePath%>"; 
		}, 3000);
	</script>
</html>