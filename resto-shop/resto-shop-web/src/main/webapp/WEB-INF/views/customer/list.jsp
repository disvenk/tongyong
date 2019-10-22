<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
	<div class="table-div">
		<div class="clearfix"></div>
		<div class="table-filter"></div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered "></table>
		</div>
	</div>

<script>
	(function(){
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "customer/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
					title : "微信号",
					data : "wechatId",
				},                 
				{                 
					title : "昵称",
					data : "nickname",
				},                 
				{                 
					title : "手机号",
					data : "telephone",
				},                 
				],
		});
		
		var C = new Controller(cid,tb);
		var vueObj = C.vueObj();
	}());
	
	

	
</script>
