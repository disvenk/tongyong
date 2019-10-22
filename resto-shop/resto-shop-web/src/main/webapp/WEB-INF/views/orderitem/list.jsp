<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki"> 表单</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form role="form" action="{{m.id?'orderitem/modify':'orderitem/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
    <label>articleName</label>
    <input type="text" class="form-control" name="articleName" v-model="m.articleName">
</div>
<div class="form-group">
    <label>articleDesignation</label>
    <input type="text" class="form-control" name="articleDesignation" v-model="m.articleDesignation">
</div>
<div class="form-group">
    <label>count</label>
    <input type="text" class="form-control" name="count" v-model="m.count">
</div>
<div class="form-group">
    <label>originalPrice</label>
    <input type="text" class="form-control" name="originalPrice" v-model="m.originalPrice">
</div>
<div class="form-group">
    <label>unitPrice</label>
    <input type="text" class="form-control" name="unitPrice" v-model="m.unitPrice">
</div>
<div class="form-group">
    <label>finalPrice</label>
    <input type="text" class="form-control" name="finalPrice" v-model="m.finalPrice">
</div>
<div class="form-group">
    <label>remark</label>
    <input type="text" class="form-control" name="remark" v-model="m.remark">
</div>
<div class="form-group">
    <label>sort</label>
    <input type="text" class="form-control" name="sort" v-model="m.sort">
</div>
<div class="form-group">
    <label>status</label>
    <input type="text" class="form-control" name="status" v-model="m.status">
</div>
<div class="form-group">
    <label>orderId</label>
    <input type="text" class="form-control" name="orderId" v-model="m.orderId">
</div>
<div class="form-group">
    <label>articleId</label>
    <input type="text" class="form-control" name="articleId" v-model="m.articleId">
</div>

						</div>
						<input type="hidden" name="id" v-model="m.id" />
						<input class="btn green"  type="submit"  value="保存"/>
						<a class="btn default" @click="cancel" >取消</a>
					</form>
	            </div>
	        </div>
		</div>
	</div>
	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="orderitem/add">
			<button class="btn green pull-right" @click="create">新建</button>
			</s:hasPermission>
		</div>
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
				url : "orderitem/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
	title : "articleName",
	data : "articleName",
},                 
{                 
	title : "articleDesignation",
	data : "articleDesignation",
},                 
{                 
	title : "count",
	data : "count",
},                 
{                 
	title : "originalPrice",
	data : "originalPrice",
},                 
{                 
	title : "unitPrice",
	data : "unitPrice",
},                 
{                 
	title : "finalPrice",
	data : "finalPrice",
},                 
{                 
	title : "remark",
	data : "remark",
},                 
{                 
	title : "sort",
	data : "sort",
},                 
{                 
	title : "status",
	data : "status",
},                 
{                 
	title : "orderId",
	data : "orderId",
},                 
{                 
	title : "articleId",
	data : "articleId",
},                 

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="orderitem/delete">
							C.createDelBtn(tdData,"orderitem/delete"),
							</s:hasPermission>
							<s:hasPermission name="orderitem/edit">
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		
		var C = new Controller(cid,tb);
		var vueObj = C.vueObj();
	}());
	
	

	
</script>
