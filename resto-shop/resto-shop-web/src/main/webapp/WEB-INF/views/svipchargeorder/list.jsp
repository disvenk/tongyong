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
	            	<form role="form" action="{{m.id?'svipchargeorder/modify':'svipchargeorder/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
    <label>chargeMoney</label>
    <input type="text" class="form-control" name="chargeMoney" v-model="m.chargeMoney">
</div>
<div class="form-group">
    <label>orderState</label>
    <input type="text" class="form-control" name="orderState" v-model="m.orderState">
</div>
<div class="form-group">
    <label>finishTime</label>
    <input type="text" class="form-control" name="finishTime" v-model="m.finishTime">
</div>
<div class="form-group">
    <label>customerId</label>
    <input type="text" class="form-control" name="customerId" v-model="m.customerId">
</div>
<div class="form-group">
    <label>brandId</label>
    <input type="text" class="form-control" name="brandId" v-model="m.brandId">
</div>
<div class="form-group">
    <label>shopDetailId</label>
    <input type="text" class="form-control" name="shopDetailId" v-model="m.shopDetailId">
</div>
<div class="form-group">
    <label>activityId</label>
    <input type="text" class="form-control" name="activityId" v-model="m.activityId">
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
			<s:hasPermission name="svipchargeorder/add">
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
				url : "svipchargeorder/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
	title : "chargeMoney",
	data : "chargeMoney",
},                 
{                 
	title : "orderState",
	data : "orderState",
},                 
{                 
	title : "finishTime",
	data : "finishTime",
},                 
{                 
	title : "customerId",
	data : "customerId",
},                 
{                 
	title : "brandId",
	data : "brandId",
},                 
{                 
	title : "shopDetailId",
	data : "shopDetailId",
},                 
{                 
	title : "activityId",
	data : "activityId",
},                 

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="svipchargeorder/delete">
							C.createDelBtn(tdData,"svipchargeorder/delete"),
							</s:hasPermission>
							<s:hasPermission name="svipchargeorder/modify">
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
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
	
	

	
</script>
