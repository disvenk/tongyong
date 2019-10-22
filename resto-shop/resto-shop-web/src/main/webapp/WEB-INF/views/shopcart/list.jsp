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
	            	<form role="form" action="{{m.id?'shopcart/modify':'shopcart/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
    <label>number</label>
    <input type="text" class="form-control" name="number" v-model="m.number">
</div>
<div class="form-group">
    <label>customerId</label>
    <input type="text" class="form-control" name="customerId" v-model="m.customerId">
</div>
<div class="form-group">
    <label>articleId</label>
    <input type="text" class="form-control" name="articleId" v-model="m.articleId">
</div>
<div class="form-group">
    <label>shopDetailId</label>
    <input type="text" class="form-control" name="shopDetailId" v-model="m.shopDetailId">
</div>
<div class="form-group">
    <label>distributionModeId</label>
    <input type="text" class="form-control" name="distributionModeId" v-model="m.distributionModeId">
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
			<s:hasPermission name="shopcart/add">
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
				url : "shopcart/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
	title : "number",
	data : "number",
},                 
{                 
	title : "customerId",
	data : "customerId",
},                 
{                 
	title : "articleId",
	data : "articleId",
},                 
{                 
	title : "shopDetailId",
	data : "shopDetailId",
},                 
{                 
	title : "distributionModeId",
	data : "distributionModeId",
},                 

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="shopcart/delete">
							C.createDelBtn(tdData,"shopcart/delete"),
							</s:hasPermission>
							<s:hasPermission name="shopcart/edit">
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
