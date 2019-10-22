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
	            	<form role="form" action="{{m.id?'erole/modify':'erole/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
								<label>角色名</label>
								<input type="text" class="form-control" name="roleName" v-model="m.roleName">
							</div>

							<div class="form-group">
								<label>角色标识</label>
								<input type="text" class="form-control" name="roleSign" v-model="m.roleSign">
							</div>

							<div class="form-group">
								<label class="col-md-2 control-label">状态：</label>
								<div class="col-md-8">
									<label>
										<input type="radio" name="status" v-model="m.status" value="1"> 开启
									</label>
									<label>
										<input type="radio" name="status" v-model="m.status" value="0"> 关闭
									</label>
								</div>
								<br>
							</div>

							<div class="form-group">
								<label class="col-md-2 control-label">作用域：</label>
								<div class="col-md-8">
									<label>
										<input type="radio" name="actionScope" v-model="m.actionScope" value="1"> 品牌
									</label>
									<label>
										<input type="radio" name="actionScope" v-model="m.actionScope" value="2"> 店铺
									</label>
								</div>
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
			<s:hasPermission name="erole/add">
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
				url : "erole/list_all",
				dataSrc : "",
			},
			columns : [
					{
						title : "角色",
						data : "roleName",
					},
					{
						title : "角色标识",
						data : "roleSign",
					},
                     {
						title : "状态",
						data : "status",
                         createdCell: function (td, tdData) {
                             $(td).html(tdData ? "开启" : "未开启");
                         }
                    	},
					{
						title : "作用域",
						data : "actionScope",
						createdCell: function (td, tdData) {
							$(td).html(tdData == 1 ? "品牌" : "店铺");
						}
					},
					{
						title : "操作",
						data : "id",
						createdCell:function(td,tdData,rowData,row){
                            	var operator = [];
                            	<s:hasPermission name="erole/modify">
                            		var editBtn = C.createEditBtn(rowData);
                            		operator.push(editBtn);
							</s:hasPermission>
							<s:hasPermission name="erole/permissionlist">
                            		operator.push(vueObj.createAssignBtn(tdData));
                            	</s:hasPermission>
                            	$(td).html(operator);
						}
					}],
		});

		var C = new Controller(null,tb);
		var vueObj = new Vue({
			el:"#control",
			mixins:[C.formVueMix],
            data:{
                permissionList: [],
            },
            methods:{
                createAssignBtn: function (roleId) {
                    return C.createFormBtn({
                        url:"erole/assign",
                        formaction:"erole/assign_form",
                        data:{roleId:roleId},
                        name:"分配权限"
                    });
                }
            }
		});
		C.vue=vueObj;



	}());
	
</script>
