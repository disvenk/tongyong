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
	            	<form role="form" action="{{m.id?'coupon/modify':'coupon/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
    <label>name</label>
    <input type="text" class="form-control" name="name" v-model="m.name">
</div>
<div class="form-group">
    <label>value</label>
    <input type="text" class="form-control" name="value" v-model="m.value">
</div>
<div class="form-group">
    <label>minAmount</label>
    <input type="text" class="form-control" name="minAmount" v-model="m.minAmount">
</div>
<div class="form-group">
    <label>beginDate</label>
    <input type="text" class="form-control" name="beginDate" v-model="m.beginDate">
</div>
<div class="form-group">
    <label>endDate</label>
    <input type="text" class="form-control" name="endDate" v-model="m.endDate">
</div>
<div class="form-group">
    <label>beginTime</label>
    <input type="text" class="form-control" name="beginTime" v-model="m.beginTime">
</div>
<div class="form-group">
    <label>endTime</label>
    <input type="text" class="form-control" name="endTime" v-model="m.endTime">
</div>
<div class="form-group">
    <label>isUsed</label>
    <input type="text" class="form-control" name="isUsed" v-model="m.isUsed">
</div>
<div class="form-group">
    <label>usingTime</label>
    <input type="text" class="form-control" name="usingTime" v-model="m.usingTime">
</div>
<div class="form-group">
    <label>couponSource</label>
    <input type="text" class="form-control" name="couponSource" v-model="m.couponSource">
</div>
<div class="form-group">
    <label>useWithAccount</label>
    <input type="text" class="form-control" name="useWithAccount" v-model="m.useWithAccount">
</div>
<div class="form-group">
    <label>remark</label>
    <input type="text" class="form-control" name="remark" v-model="m.remark">
</div>
<div class="form-group">
    <label>distributionModeId</label>
    <input type="text" class="form-control" name="distributionModeId" v-model="m.distributionModeId">
</div>
<div class="form-group">
    <label>customerId</label>
    <input type="text" class="form-control" name="customerId" v-model="m.customerId">
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
			<s:hasPermission name="coupon/add">
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
				url : "coupon/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
					title : "name",
					data : "name",
				},                 
				{                 
					title : "value",
					data : "value",
				},                 
				{                 
					title : "minAmount",
					data : "minAmount",
				},                 
				{                 
					title : "beginDate",
					data : "beginDate",
				},                 
				{                 
					title : "endDate",
					data : "endDate",
				},                 
				{                 
					title : "beginTime",
					data : "beginTime",
				},                 
				{                 
					title : "endTime",
					data : "endTime",
				},                 
				{                 
					title : "isUsed",
					data : "isUsed",
				},                 
				{                 
					title : "usingTime",
					data : "usingTime",
				},                 
				{                 
					title : "couponSource",
					data : "couponSource",
				},                 
				{                 
					title : "useWithAccount",
					data : "useWithAccount",
				},                 
				{                 
					title : "remark",
					data : "remark",
				},                 
				{                 
					title : "distributionModeId",
					data : "distributionModeId",
				},                 
				{                 
					title : "customerId",
					data : "customerId",
				},                 

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="coupon/delete">
							C.createDelBtn(tdData,"coupon/delete"),
							</s:hasPermission>
							<s:hasPermission name="coupon/edit">
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
