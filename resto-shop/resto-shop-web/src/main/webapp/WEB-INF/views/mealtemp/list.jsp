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
	            	<form role="form" class="form-horizontal" action="{{m.id?'mealtemp/modify':'mealtemp/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
							    <label class="col-md-2 control-label">模板名称</label>
							    <div class="col-md-10">
							    	<input type="text" class="form-control" v-model="m.name">
							    </div>
							</div>
							<div class="form-group" v-for="attr in m.attrs">
								<label class="col-md-2 control-label">套餐项{{$index+1}}:</label>
								<div class="col-md-3">
									<input type="text" class="form-control" v-model="attr.name" required>
								</div>
								<label class="col-md-2 control-label">排序：</label>
								<div class="col-md-3">
									<input type="text" class="form-control" v-model="attr.sort">
								</div>
								<div class="col-md-2">
									<a class="btn red btn-block" @click="removeAttr(attr)">移除</a>
								</div>
							</div>
							<div class="form-group">
						  		<a class="btn blue btn-block" @click="addAttr">添加套餐项</a>
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
			<s:hasPermission name="mealtemp/add">
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
				url : "mealtemp/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
					title : "模板名称",
					data : "name",
				},                 
				{                 
					title : "模板项",
					data : "attrs",
					defaultContent:"",
					createdCell:function(td,tdData){
						$(td).html('');
						for(var i in tdData){
							var span = $("<span class='btn blue btn-xs'></span>");
							$(td).append(span.html(tdData[i].name));
						}
					}
				},                 

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="mealtemp/delete">
							C.createDelBtn(tdData,"mealtemp/delete"),
							</s:hasPermission>
							<s:hasPermission name="mealtemp/edit">
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
			mixins:[C.formVueMix],
			methods:{
				removeAttr:function(attr){
					this.m.attrs.$remove(attr);
				},
				edit:function(model){
					var that = this;
					$.post("mealtemp/list_one_full",{id:model.id},function(result){
						that.m = result.data;
						that.showform=true;
					});
				},
				addAttr:function(){
					if(!this.m.attrs){
						this.$set("m.attrs",[]);
					}
					this.m.attrs.push({name:"",sort:this.m.attrs.length+1});
				},
				save:function(e){
					var action = $(e.target).attr("action");
					var jsondata = JSON.stringify(this.m);
					var that = this;
					$.ajax({
						contentType : "application/json",
						type : "post",
						url : action,
						data:jsondata,
						success:function(result){
							if(result.success){
								that.showform=false;
								that.m={};
								C.simpleMsg("保存成功");
								tb.ajax.reload();
							}else{
								C.errorMsg(result.message);
							}
						},
						error:function(xhr,msg,e){
							var errorText = xhr.status+" "+xhr.statusText+":"+action;
							C.errorMsg(errorText);
						}
					});
				}
			}
		});
		C.vue=vueObj;
	}());
	
	

	
</script>
