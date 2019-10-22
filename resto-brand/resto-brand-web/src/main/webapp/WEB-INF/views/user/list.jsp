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
	            	<form role="form" action="{{m.id?'user/modify':'user/create'}}" @submit.prevent="save">
						<input type="hidden" name="id" v-model="m.id">
						<div class="form-body">
							<div class="form-group">
								<label>用户名：</label>
							    <input type="text" class="form-control" name="username" v-model="m.username" v-if="!m.id" required="required">
							    <input type="text" class="form-control" name="username" v-model="m.username" v-if="m.id" disabled>
							</div>
							<div class="form-group">
								<label>密码：</label>
								<input type="password" class="form-control" name="password" v-model="m.password">
							</div>
						</div>
						<input class="btn green"  type="submit"  value="保存"/>
						<a class="btn default" @click="cancel" >取消</a>
					</form>
	            </div>
	        </div>
		</div>
	</div>
	
	<div class="table-div">
		<div class="table-operator">
			<button class="btn green pull-right" @click="create">新建用户</button>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter">&nbsp;</div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered "></table>
		</div>
	</div>
	<form id="role-form" hidden v-if="showrole">
		<input type="hidden" name="id" v-model="m.id"/>
		<div class="form-body">
			<div class="form-group" >
				<label v-for="r in allRoles">
					<input type="checkbox"  name="roleIds" value="{{r.id}}">{{r.roleName}}
				</label>
			</div>
		</div>
	</form>
</div>


<script>
	(function(){
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "user/list_all",
				dataSrc : ""
			},
			order:[[1,"desc"]],
			columns : [
				{
					title:"用户名",
					data:"username"
				},
				{
					title:"创建时间",
					data:"createTime",
					createdCell:function(td,tdData){
						$(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm"));
					}
				}
				, {
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var delBtn =C.createDelBtn(tdData,"user/delete");
						var editBtn = C.createEditBtn(rowData);
						<s:hasPermission name="user/role">
							var assignRoleBtn = $("<button class='btn blue btn-xs'>角色设置</button>").click(function(){
								C.ajax("user/role_has_data",{id:tdData},function(result){
									var hasRoles = result.data;
									var hasIds = [];
									for(var i in hasRoles){
										var role = hasRoles[i];
										var roleId = role.id;
										hasIds.push(roleId);
									}
									vueObj.$set("m",rowData);
									vueObj.$set("showrole",true);
									Vue.nextTick(function(){
										var formDom = $("#role-form").clone();
										var checked = formDom.find("input[type='checkbox']");
										checked.each(function(){
											var id = parseInt($(this).val());
											if($.inArray(id,hasIds)>=0){
												$(this).prop("checked",true);
											}
										});
										formDom.removeAttr("id");
										formDom.removeAttr("hidden");
										var formDialog = C.loadForm({
											title:"分配角色",
											formDom:formDom,
											formaction:"user/role_data_edit",
											aftersuccess:function(){
												tb.ajax.reload();	
											}
										});
										formDialog.addEventListener("remove",function(){
											vueObj.showrole=false;
										});
									});
								});
							});
						</s:hasPermission>
						var operator = [editBtn,assignRoleBtn,delBtn];
						$(td).html(operator);
					}
				} ],
		});
		
		function loadAllRoles(successCbk){
			C.ajax("user/role_all_data",null,function(result){
				console.log(result);
				if(result.success){
					successCbk(result.data);
				}
			})
		}
		
		var C = new Controller(cid,tb);
		var vueObj = C.vueObj();
		loadAllRoles(function(allRoles){
			if(allRoles.length>0){
				console.log(allRoles);
				vueObj.$set("allRoles",allRoles);
			}
		});
	}());
	
	

	
</script>