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
	            	<form role="form" action="{{m.id?'otherconfigdetailed/modify':'otherconfigdetailed/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
    <label>data</label>
    <input type="text" class="form-control" name="data" v-model="m.data">
</div>
<div class="form-group">
    <label>updateTime</label>
    <input type="text" class="form-control" name="updateTime" v-model="m.updateTime">
</div>
<div class="form-group">
    <label>otherConfigSign</label>
    <input type="text" class="form-control" name="otherConfigSign" v-model="m.otherConfigSign">
</div>
<div class="form-group">
    <label>shopDetailId</label>
    <input type="text" class="form-control" name="shopDetailId" v-model="m.shopDetailId">
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
			<s:hasPermission name="otherconfigdetailed/add">
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
				url : "otherconfigdetailed/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
	title : "data",
	data : "data",
},                 
{                 
	title : "updateTime",
	data : "updateTime",
},                 
{                 
	title : "otherConfigSign",
	data : "otherConfigSign",
},                 
{                 
	title : "shopDetailId",
	data : "shopDetailId",
},                 

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="otherconfigdetailed/delete">
							C.createDelBtn(tdData,"otherconfigdetailed/delete"),
							</s:hasPermission>
							<s:hasPermission name="otherconfigdetailed/edit">
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
