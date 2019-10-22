<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style type="text/css">
	.weui_toast {
		width: 100%;
		height: 100%;
		text-align: center;
	}
	.imgJurisdiction {
		margin-top: 16vh;
		width: 20vw;
		height: 19vw;
	}
	.leftText {
		margin-top: 5vh;
		font-size: 18px;
	}
</style>

<div class="weui_toast">
    <img class="imgJurisdiction" src="assets/pages/img/noJurisdiction.png"/>
    <div class="leftText">尊敬的用户，您尚未开启相关权限，如需开启权限，请联系系统管理员，谢谢！</div>
</div>