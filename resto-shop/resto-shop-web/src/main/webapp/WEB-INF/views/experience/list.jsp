<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">

	<div class="table-div">
		<div class="table-operator">
			<%--<s:hasPermission name="experience/add">--%>
			<%--<button class="btn green pull-right" @click="create">新建</button>--%>
			<%--</s:hasPermission>--%>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter"></div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered "></table>
		</div>
	</div>
</div>


<script>
	var tb = null;
	(function(){
		var cid="#control";
		var $table = $(".table-body>table");
		tb = $table.DataTable({
			ajax : {
				url : "experience/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
					title : "体验类型",
					data : "showType",
					createdCell:function(td,tdData){
						if(tdData == 2){
							$(td).html("<td>好评</td>");
						}else{
							$(td).html("<td>差评</td>");
						}
					}
				},
				{
					title : "体验名称",
					data : "title",
				},
				{
					title : "序号",
					data : "showSort",
				},
				{
					title : "状态",
					data : "choiceIt",
					createdCell:function(td,tdData){
						if(tdData){
							$(td).html("<td>已启用</td>");
						}else{
							$(td).html("<td>未启用</td>");
						}
					}
				},
				{
					title : "操作",
					data : "choiceIt",
					createdCell:function(td,tdData,rowDate){
						if(tdData){
							$(td).html("<button class='btn btn-xs btn-danger' onclick='closeEx("+JSON.stringify(rowDate)+")'>关闭</button>");
						}else{
							$(td).html("<button class='btn btn-xs btn-primary' onclick='openEx("+JSON.stringify(rowDate)+")'>开启</button>");
						}
					}
				}],
		});
		
		var C = new Controller(null,tb);
		var vueObj = new Vue({
			el:"#control",
			mixins:[C.formVueMix]
		});
		C.vue=vueObj;
	}());

	function closeEx(rowDate){
		$.ajax({
			url: "experience/delete",
			data: {title: rowDate.title},
			type: "post",
			success:function(result){
				if(result.success){
					tb.ajax.reload(null, false);
				}
			}
		});
	}

	function openEx(rowDate){
		$.ajax({
			url: "experience/create",
			data: {showType: rowDate.showType, title: rowDate.title, showSort: rowDate.showSort},
			type: "post",
			success:function(result){
				if(result.success){
					tb.ajax.reload(null, false);
				}
			}
		});
	}
</script>
