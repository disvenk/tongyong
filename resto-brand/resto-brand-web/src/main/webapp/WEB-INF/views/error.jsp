<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<body>
 	<h1>ERROR DEBUG</h1>
 	<p style="color:red">errorType: ${errorType }</p>
 	<p>error: ${error }</p>
</body>
<!-- END BODY -->
</html>