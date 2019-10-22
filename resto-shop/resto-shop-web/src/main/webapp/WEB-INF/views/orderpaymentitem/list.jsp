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
	            	<form role="form" action="{{m.id?'orderpaymentitem/modify':'orderpaymentitem/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
    <label>payTime</label>
    <input type="text" class="form-control" name="payTime" v-model="m.payTime">
</div>
<div class="form-group">
    <label>payValue</label>
    <input type="text" class="form-control" name="payValue" v-model="m.payValue">
</div>
<div class="form-group">
    <label>remark</label>
    <input type="text" class="form-control" name="remark" v-model="m.remark">
</div>
<div class="form-group">
    <label>paymentModeId</label>
    <input type="text" class="form-control" name="paymentModeId" v-model="m.paymentModeId">
</div>
<div class="form-group">
    <label>orderId</label>
    <input type="text" class="form-control" name="orderId" v-model="m.orderId">
</div>
<div class="form-group">
    <label>resultData</label>
    <input type="text" class="form-control" name="resultData" v-model="m.resultData">
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
			<s:hasPermission name="orderpaymentitem/add">
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
				url : "orderpaymentitem/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
	title : "payTime",
	data : "payTime",
},                 
{                 
	title : "payValue",
	data : "payValue",
},                 
{                 
	title : "remark",
	data : "remark",
},                 
{                 
	title : "paymentModeId",
	data : "paymentModeId",
},                 
{                 
	title : "orderId",
	data : "orderId",
},                 
{                 
	title : "resultData",
	data : "resultData",
},                 

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="orderpaymentitem/delete">
							C.createDelBtn(tdData,"orderpaymentitem/delete"),
							</s:hasPermission>
							<s:hasPermission name="orderpaymentitem/edit">
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
