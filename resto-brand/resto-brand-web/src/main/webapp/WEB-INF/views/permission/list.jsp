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
	            	<form role="form" action="{{m.id?'permission/modify':'permission/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
    <label>serialVersionUID</label>
    <input type="text" class="form-control" name="serialVersionUID" v-model="m.serialVersionUID">
</div>
<div class="form-group">
    <label>isMenu</label>
    <input type="text" class="form-control" name="isMenu" v-model="m.isMenu">
</div>
<div class="form-group">
    <label>menuIcon</label>
    <input type="text" class="form-control" name="menuIcon" v-model="m.menuIcon">
</div>
<div class="form-group">
    <label>permissionName</label>
    <input type="text" class="form-control" name="permissionName" v-model="m.permissionName">
</div>
<div class="form-group">
    <label>permissionSign</label>
    <input type="text" class="form-control" name="permissionSign" v-model="m.permissionSign">
</div>
<div class="form-group">
    <label>sort</label>
    <input type="text" class="form-control" name="sort" v-model="m.sort">
</div>
<div class="form-group">
    <label>parentId</label>
    <input type="text" class="form-control" name="parentId" v-model="m.parentId">
</div>
<div class="form-group">
    <label>menuType</label>
    <input type="text" class="form-control" name="menuType" v-model="m.menuType">
</div>
<div class="form-group">
    <label>userGroupId</label>
    <input type="text" class="form-control" name="userGroupId" v-model="m.userGroupId">
</div>
<div class="form-group">
    <label>children</label>
    <input type="text" class="form-control" name="children" v-model="m.children">
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
			<s:hasPermission name="permission/add">
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
				url : "permission/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
	title : "serialVersionUID",
	data : "serialVersionUID",
},                 
{                 
	title : "isMenu",
	data : "isMenu",
},                 
{                 
	title : "menuIcon",
	data : "menuIcon",
},                 
{                 
	title : "permissionName",
	data : "permissionName",
},                 
{                 
	title : "permissionSign",
	data : "permissionSign",
},                 
{                 
	title : "sort",
	data : "sort",
},                 
{                 
	title : "parentId",
	data : "parentId",
},                 
{                 
	title : "menuType",
	data : "menuType",
},                 
{                 
	title : "userGroupId",
	data : "userGroupId",
},                 
{                 
	title : "children",
	data : "children",
},                 

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="permission/delete">
							C.createDelBtn(tdData,"permission/delete"),
							</s:hasPermission>
							<s:hasPermission name="permission/edit">
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
