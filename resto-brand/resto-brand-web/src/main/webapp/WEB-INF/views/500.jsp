<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<title>品牌管理</title>
<link rel="shortcut icon" href="assets/pages/img/favicon.ico" />
</head>
<body style="text-align: center;">
	<h1>500</h1>
	<p style="color: red">ErrorType: ${errorType}</p>
	<p>error:${error}</p>
</body>
<!-- END BODY -->
</html>