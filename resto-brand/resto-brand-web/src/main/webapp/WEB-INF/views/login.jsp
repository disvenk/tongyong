<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8"/>
		<base href="<%=basePath%>">
		<title>系统管理</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
		<!-- BEGIN GLOBAL MANDATORY STYLES -->
		<link href="assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
		<link href="assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
		<!-- END GLOBAL MANDATORY STYLES -->
		<!-- BEGIN PAGE LEVEL STYLES -->
		<link href="assets/pages/css/login.css" rel="stylesheet" type="text/css"/>
		<!-- END PAGE LEVEL SCRIPTS -->
		<!-- BEGIN THEME STYLES -->
		<link href="assets/global/css/components.css" id="style_components" rel="stylesheet" type="text/css"/>
		<link href="assets/global/css/plugins.css" rel="stylesheet" type="text/css"/>
		<!-- END THEME STYLES -->
		<link rel="shortcut icon" href="assets/pages/img/favicon.ico" />
	</head>
<!-- BEGIN BODY -->
	<body class="login">
		<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
		<div class="menu-toggler sidebar-toggler">
		</div>
		<!-- END SIDEBAR TOGGLER BUTTON -->
		<!-- BEGIN LOGO -->
		<div class="logo">
			<a href="index.html">
			<img src="assets/pages/img/Resto+.png" style="height: 40px;" alt=""/>
			</a>
		</div>
		<!-- END LOGO -->
		<!-- BEGIN LOGIN -->
		<div class="content">
			<!-- BEGIN LOGIN FORM -->
			<form class="login-form" method="post" action="user/login">
				<input type="hidden" name="redirect" value="${redirect }"/>
				<div class="form-title" style="text-align: center">
					<strong>欢迎使用上海餐加企业管理后台系统</strong>
				</div>
				<div class="alert alert-danger display-hide">
					<button class="close" data-close="alert"></button>
					<span>请输入用户名和密码</span>
				</div>
				<div class="form-group">
					<label class="control-label visible-ie8 visible-ie9">用户名</label>
					<input class="form-control form-control-solid placeholder-no-fix" type="text"  placeholder="用户名" name="username"/>
				</div>
				<div class="form-group">
					<label class="control-label visible-ie8 visible-ie9">密码</label>
					<input class="form-control form-control-solid placeholder-no-fix" type="password"  placeholder="密码" name="password"/>
				</div>
				<div class="form-actions">
					<button type="submit" class="btn btn-primary btn-block uppercase">登录</button>
				</div>
			</form>
			<!-- END LOGIN FORM -->
		</div>
		<div class="copyright">
			&copy;  上海餐加企业管理咨询有限公司 v2.1.0
		</div>
		<!-- END LOGIN -->
		<script src="assets/global/plugins/jquery.min.js" type="text/javascript"></script>
		<script src="assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
		<script src="assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
		<!-- END CORE PLUGINS -->
		<!-- BEGIN PAGE LEVEL PLUGINS -->
		<script src="assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
		<script src="assets/global/plugins/jquery-validation/js/localization/messages_zh.js" type="text/javascript" charset="utf-8"></script>
		<!-- END PAGE LEVEL PLUGINS -->
		<script>
			$('.login-form').validate({
	            errorElement: 'span', //default input error message container
	            errorClass: 'text-danger', // default input error message class
	            focusInvalid: false, // do not focus the last invalid input
	            rules: {
	                username: {
	                    required: true
	                },
	                password: {
	                    required: true
	                },
	                remember: {
	                    required: false
	                }
	            },	           
	
	            invalidHandler: function(event, validator) { //display error alert on form submit   
	                $('.alert-danger').html("请输入用户名或密码").show();
	            },
	        });
	
	        $('.login-form input').keypress(function(e) {
	            if (e.which == 13) {
	                if ($('.login-form').validate().form()) {
	                    $('.login-form').submit(); //form validation success, call ajax form submit
	                }
	                return false;
	            }
	        });
	        
	        var error='${error}';
	        if(error){
	        	 $('.alert-danger').html(error).show();
	        }
		</script>
		<!-- END JAVASCRIPTS -->
	</body>
<!-- END BODY -->
</html>