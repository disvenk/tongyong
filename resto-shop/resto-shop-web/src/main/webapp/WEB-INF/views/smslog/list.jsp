<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	            </div>
	            <div class="portlet-body">
	            </div>
	        </div>
		</div>
	</div>
	
	<div class="table-div">
		<div class="clearfix"></div>
		<div class="table-filter"></div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered "></table>
		</div>
	</div>
</div>


<script>
	(function(){
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "smslog/list_all",
				dataSrc : ""
			},
			columns : [
								{                 
					title : "手机号",
					data : "phone",
				},                 
				{                 
					title : "发送内容",
					data : "content",
				},                 
				            
				{                 
					title : "发送的结果",
					data : "smsResult",
				},                 
				],
		});
		
		var C = new Controller(cid,tb);
		var vueObj = C.vueObj();
	}());
	
	

	
</script>
