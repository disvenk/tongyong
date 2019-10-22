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
	            	<form role="form" action="{{m.id?'svip/modify':'svip/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
    <label>chargeMoney</label>
    <input type="text" class="form-control" name="chargeMoney" v-model="m.chargeMoney">
</div>
<div class="form-group">
    <label>beSvipTime</label>
    <input type="text" class="form-control" name="beSvipTime" v-model="m.beSvipTime">
</div>
<div class="form-group">
    <label>svipExpire</label>
    <input type="text" class="form-control" name="svipExpire" v-model="m.svipExpire">
</div>
<div class="form-group">
    <label>beginDateTime</label>
    <input type="text" class="form-control" name="beginDateTime" v-model="m.beginDateTime">
</div>
<div class="form-group">
    <label>endDateTime</label>
    <input type="text" class="form-control" name="endDateTime" v-model="m.endDateTime">
</div>
<div class="form-group">
    <label>activityId</label>
    <input type="text" class="form-control" name="activityId" v-model="m.activityId">
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
			<s:hasPermission name="svip/add">
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
				url : "svip/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
	title : "chargeMoney",
	data : "chargeMoney",
},                 
{                 
	title : "beSvipTime",
	data : "beSvipTime",
},                 
{                 
	title : "svipExpire",
	data : "svipExpire",
},                 
{                 
	title : "beginDateTime",
	data : "beginDateTime",
},                 
{                 
	title : "endDateTime",
	data : "endDateTime",
},                 
{                 
	title : "activityId",
	data : "activityId",
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
							<s:hasPermission name="svip/delete">
							C.createDelBtn(tdData,"svip/delete"),
							</s:hasPermission>
							<s:hasPermission name="svip/modify">
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
