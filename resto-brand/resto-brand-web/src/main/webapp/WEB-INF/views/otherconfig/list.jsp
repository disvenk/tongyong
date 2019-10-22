<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki">添加其他配置</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form role="form" action="{{m.id?'otherconfig/modify':'otherconfig/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
							    <label>配置标识</label>
							    <input type="text" class="form-control" name="configSign" v-model="m.configSign">
							</div>
							<div class="form-group">
							    <label>配置名称</label>
							    <input type="text" class="form-control" name="configName" v-model="m.configName">
							</div>
							<div class="form-group">
							    <label>配置备注</label>
							    <input type="text" class="form-control" name="configRemark" v-model="m.configRemark">
							</div>
							<div class="form-group text-center" v-if="errorShow">
								<label class="control-label"><font color="red">{{ errorMsg }}</font></label>
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
			<s:hasPermission name="otherconfig/add">
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
				url : "otherconfig/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
					title : "配置标识",
					data : "configSign",
				},                 
				{                 
					title : "配置名称",
					data : "configName",
				},                 
				{                 
					title : "配置备注",
					data : "configRemark",
				},                 

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="otherconfig/delete">
							C.createDelBtn(tdData,"otherconfig/delete"),
							</s:hasPermission>
							<s:hasPermission name="otherconfig/edit">
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		
		var C = new Controller(cid,tb);
		//重写 option
		var option = {
				el:cid,
				data:{
					m:{},
					showform:false,
					errorMsg:"",
					errorShow:false,
					oldConfigName:""
				},
				methods:{
					openForm:function(){
						this.showform = true;
					},
					closeForm:function(){
						this.m={};
						this.showform = false;
						this.errorShow=false;
						this.oldConfigName = "";
					},
					cancel:function(){
						this.m={};
						this.closeForm();
						this.oldConfigName = "";
					},
					create:function(){
						this.m={};
						this.openForm();
						this.errorShow=false;
						this.oldConfigName = "";
					},
					edit:function(model){
						this.m= model;
						this.openForm();
						this.errorShow=false;
						this.oldConfigName = this.m.configName;
					},
					save:function(e){
						var that = this;
						//验证 配置名称 是否重复
						if(that.oldConfigName != that.m.configName){
							$.ajax({
							   type: "POST",
							   data:"otherConfigName="+that.m.configName,
							   url: "otherconfig/checkNameExits",
							   success: function(data){
								   if(data.data != null){
									   that.errorMsg= "配置名 "+that.m.configName+" 已存在！"
									   that.errorShow=true;
								   }else{
										var formDom = e.target;
										C.ajaxFormEx(formDom,function(){
											that.cancel();
											tb.ajax.reload();
										});
								   }
							   }
							});
						}else{
							var formDom = e.target;
							C.ajaxFormEx(formDom,function(){
								that.cancel();
								tb.ajax.reload();
							});
						}
					},
				},
			};
		
		var vueObj = C.vueObj(option);
		
	}());
	
</script>
