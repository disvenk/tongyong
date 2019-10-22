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
                            <label>权限名称</label>
                            <input type="text" class="form-control" name="permissionName" v-model="m.permissionName">
                        </div>
                        <div class="form-group">
                            <label>权限标识</label>
                            <input type="text" class="form-control" name="permissionSign" v-model="m.permissionSign">
                        </div>
                        <div class="form-group">
                            <label>排序</label>
                            <input type="text" class="form-control" name="sort" v-model="m.sort">
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
				url : "permission/list_all",
				dataSrc : ""
			},
			columns : [
                            {
                title : "权限名称",
                data : "permissionName",
            },
            {
                title : "权限标识",
                data : "permissionSign",
            },
            {
                title : "排序",
                data : "sort",
            },

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="permission/delete">
							C.createDelBtn(tdData,"permission/delete"),
							</s:hasPermission>
							<s:hasPermission name="permission/modify">
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
