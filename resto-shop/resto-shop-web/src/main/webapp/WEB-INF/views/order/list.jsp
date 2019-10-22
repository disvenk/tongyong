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
	            	<form role="form" action="{{m.id?'order/modify':'order/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
    <label>tableNumber</label>
    <input type="text" class="form-control" name="tableNumber" v-model="m.tableNumber">
</div>
<div class="form-group">
    <label>customerCount</label>
    <input type="text" class="form-control" name="customerCount" v-model="m.customerCount">
</div>
<div class="form-group">
    <label>accountingTime</label>
    <input type="text" class="form-control" name="accountingTime" v-model="m.accountingTime">
</div>
<div class="form-group">
    <label>orderState</label>
    <input type="text" class="form-control" name="orderState" v-model="m.orderState">
</div>
<div class="form-group">
    <label>productionStatus</label>
    <input type="text" class="form-control" name="productionStatus" v-model="m.productionStatus">
</div>
<div class="form-group">
    <label>originalAmount</label>
    <input type="text" class="form-control" name="originalAmount" v-model="m.originalAmount">
</div>
<div class="form-group">
    <label>reductionAmount</label>
    <input type="text" class="form-control" name="reductionAmount" v-model="m.reductionAmount">
</div>
<div class="form-group">
    <label>paymentAmount</label>
    <input type="text" class="form-control" name="paymentAmount" v-model="m.paymentAmount">
</div>
<div class="form-group">
    <label>orderMoney</label>
    <input type="text" class="form-control" name="orderMoney" v-model="m.orderMoney">
</div>
<div class="form-group">
    <label>articleCount</label>
    <input type="text" class="form-control" name="articleCount" v-model="m.articleCount">
</div>
<div class="form-group">
    <label>serialNumber</label>
    <input type="text" class="form-control" name="serialNumber" v-model="m.serialNumber">
</div>
<div class="form-group">
    <label>confirmTime</label>
    <input type="text" class="form-control" name="confirmTime" v-model="m.confirmTime">
</div>
<div class="form-group">
    <label>printTimes</label>
    <input type="text" class="form-control" name="printTimes" v-model="m.printTimes">
</div>
<div class="form-group">
    <label>allowCancel</label>
    <input type="text" class="form-control" name="allowCancel" v-model="m.allowCancel">
</div>
<div class="form-group">
    <label>closed</label>
    <input type="text" class="form-control" name="closed" v-model="m.closed">
</div>
<div class="form-group">
    <label>remark</label>
    <input type="text" class="form-control" name="remark" v-model="m.remark">
</div>
<div class="form-group">
    <label>operatorId</label>
    <input type="text" class="form-control" name="operatorId" v-model="m.operatorId">
</div>
<div class="form-group">
    <label>customerId</label>
    <input type="text" class="form-control" name="customerId" v-model="m.customerId">
</div>
<div class="form-group">
    <label>distributionDate</label>
    <input type="text" class="form-control" name="distributionDate" v-model="m.distributionDate">
</div>
<div class="form-group">
    <label>distributionTimeId</label>
    <input type="text" class="form-control" name="distributionTimeId" v-model="m.distributionTimeId">
</div>
<div class="form-group">
    <label>deliveryPointId</label>
    <input type="text" class="form-control" name="deliveryPointId" v-model="m.deliveryPointId">
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
			<s:hasPermission name="order/add">
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
				url : "order/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
	title : "tableNumber",
	data : "tableNumber",
},                 
{                 
	title : "customerCount",
	data : "customerCount",
},                 
{                 
	title : "accountingTime",
	data : "accountingTime",
},                 
{                 
	title : "orderState",
	data : "orderState",
},                 
{                 
	title : "productionStatus",
	data : "productionStatus",
},                 
{                 
	title : "originalAmount",
	data : "originalAmount",
},                 
{                 
	title : "reductionAmount",
	data : "reductionAmount",
},                 
{                 
	title : "paymentAmount",
	data : "paymentAmount",
},                 
{                 
	title : "orderMoney",
	data : "orderMoney",
},                 
{                 
	title : "articleCount",
	data : "articleCount",
},                 
{                 
	title : "serialNumber",
	data : "serialNumber",
},                 
{                 
	title : "confirmTime",
	data : "confirmTime",
},                 
{                 
	title : "printTimes",
	data : "printTimes",
},                 
{                 
	title : "allowCancel",
	data : "allowCancel",
},                 
{                 
	title : "closed",
	data : "closed",
},                 
{                 
	title : "remark",
	data : "remark",
},                 
{                 
	title : "operatorId",
	data : "operatorId",
},                 
{                 
	title : "customerId",
	data : "customerId",
},                 
{                 
	title : "distributionDate",
	data : "distributionDate",
},                 
{                 
	title : "distributionTimeId",
	data : "distributionTimeId",
},                 
{                 
	title : "deliveryPointId",
	data : "deliveryPointId",
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
							<s:hasPermission name="order/delete">
							C.createDelBtn(tdData,"order/delete"),
							</s:hasPermission>
							<s:hasPermission name="order/edit">
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
