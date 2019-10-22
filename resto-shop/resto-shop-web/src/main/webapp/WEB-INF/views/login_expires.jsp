<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="tag-head.jsp" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<base href="<%=basePath%>" />
	</head>
	<body>
		<h1>登陆过期。正在转跳至登录页面...<a id="login-page" href="user/login">登录链接</a></h1>
		<script type="text/javascript">
			location.href="user/login?redirect="+encodeURIComponent(location.href);
		</script>
	</body>
</html>