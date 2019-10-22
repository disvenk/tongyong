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
	            	<form role="form" action="{{m.id?'contractticket/modify':'contractticket/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
    <label>contractId</label>
    <input type="text" class="form-control" name="contractId" v-model="m.contractId">
</div>
<div class="form-group">
    <label>type</label>
    <input type="text" class="form-control" name="type" v-model="m.type">
</div>
<div class="form-group">
    <label>header</label>
    <input type="text" class="form-control" name="header" v-model="m.header">
</div>
<div class="form-group">
    <label>content</label>
    <input type="text" class="form-control" name="content" v-model="m.content">
</div>
<div class="form-group">
    <label>pushTime</label>
    <input type="text" class="form-control" name="pushTime" v-model="m.pushTime">
</div>
<div class="form-group">
    <label>ticketStatus</label>
    <input type="text" class="form-control" name="ticketStatus" v-model="m.ticketStatus">
</div>
<div class="form-group">
    <label>money</label>
    <input type="text" class="form-control" name="money" v-model="m.money">
</div>
<div class="form-group">
    <label>proposer</label>
    <input type="text" class="form-control" name="proposer" v-model="m.proposer">
</div>
<div class="form-group">
    <label>taxpayerCode</label>
    <input type="text" class="form-control" name="taxpayerCode" v-model="m.taxpayerCode">
</div>
<div class="form-group">
    <label>registeredAddress</label>
    <input type="text" class="form-control" name="registeredAddress" v-model="m.registeredAddress">
</div>
<div class="form-group">
    <label>registeredPhone</label>
    <input type="text" class="form-control" name="registeredPhone" v-model="m.registeredPhone">
</div>
<div class="form-group">
    <label>name</label>
    <input type="text" class="form-control" name="name" v-model="m.name">
</div>
<div class="form-group">
    <label>phone</label>
    <input type="text" class="form-control" name="phone" v-model="m.phone">
</div>
<div class="form-group">
    <label>address</label>
    <input type="text" class="form-control" name="address" v-model="m.address">
</div>
<div class="form-group">
    <label>expersage</label>
    <input type="text" class="form-control" name="expersage" v-model="m.expersage">
</div>
<div class="form-group">
    <label>remark</label>
    <input type="text" class="form-control" name="remark" v-model="m.remark">
</div>
<div class="form-group">
    <label>status</label>
    <input type="text" class="form-control" name="status" v-model="m.status">
</div>
<div class="form-group">
    <label>bankName</label>
    <input type="text" class="form-control" name="bankName" v-model="m.bankName">
</div>
<div class="form-group">
    <label>bankAccount</label>
    <input type="text" class="form-control" name="bankAccount" v-model="m.bankAccount">
</div>
<div class="form-group">
    <label>contract</label>
    <input type="text" class="form-control" name="contract" v-model="m.contract">
</div>
<div class="form-group">
    <label>constractNum</label>
    <input type="text" class="form-control" name="constractNum" v-model="m.constractNum">
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
			<s:hasPermission name="contractticket/add">
			<button class="btn green pull-right" @click="create">新建</button>
			</s:hasPermission>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter">&nbsp;</div>
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
				url : "contractticket/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
	title : "contractId",
	data : "contractId",
},                 
{                 
	title : "type",
	data : "type",
},                 
{                 
	title : "header",
	data : "header",
},                 
{                 
	title : "content",
	data : "content",
},                 
{                 
	title : "pushTime",
	data : "pushTime",
},                 
{                 
	title : "ticketStatus",
	data : "ticketStatus",
},                 
{                 
	title : "money",
	data : "money",
},                 
{                 
	title : "proposer",
	data : "proposer",
},                 
{                 
	title : "taxpayerCode",
	data : "taxpayerCode",
},                 
{                 
	title : "registeredAddress",
	data : "registeredAddress",
},                 
{                 
	title : "registeredPhone",
	data : "registeredPhone",
},                 
{                 
	title : "name",
	data : "name",
},                 
{                 
	title : "phone",
	data : "phone",
},                 
{                 
	title : "address",
	data : "address",
},                 
{                 
	title : "expersage",
	data : "expersage",
},                 
{                 
	title : "remark",
	data : "remark",
},                 
{                 
	title : "status",
	data : "status",
},                 
{                 
	title : "bankName",
	data : "bankName",
},                 
{                 
	title : "bankAccount",
	data : "bankAccount",
},                 
{                 
	title : "contract",
	data : "contract",
},                 
{                 
	title : "constractNum",
	data : "constractNum",
},                 

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="contractticket/delete">
							C.createDelBtn(tdData,"contractticket/delete"),
							</s:hasPermission>
							<s:hasPermission name="contractticket/modify">
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
