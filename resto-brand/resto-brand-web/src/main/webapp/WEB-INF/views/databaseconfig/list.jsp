<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki">数据库配置信息</span>
					</div>
				</div>
				<div class="portlet-body">
					<form class="form-horizontal" role="form" action="{{m.id?'databaseconfig/modify':'databaseconfig/create'}}" @submit.prevent="save">
				  		<div class="form-body">
				  			<div class="form-group">
							  <label class="col-sm-3 control-label">数据库名称：</label>
							  <div class="col-sm-8">
							    <input type="text" class="form-control" name="name" v-model="m.name">
							  </div>
							</div>
				  			<div class="form-group">
							  <label class="col-sm-3 control-label">数据库地址：</label>
							  <div class="col-sm-8">
							    <input type="text" class="form-control" name="url" v-model="m.url">
							  </div>
							</div>
				  			<div class="form-group">
							  <label class="col-sm-3 control-label">数据库驱动：</label>
							  <div class="col-sm-8">
							    <input type="text" class="form-control" name="driverClassName" v-model="m.driverClassName">
							  </div>
							</div>
				  			<div class="form-group">
							  <label class="col-sm-3 control-label">用&nbsp;户&nbsp;名：</label>
							  <div class="col-sm-8">
							    <input type="password" class="form-control" name="username" v-model="m.username">
							  </div>
							</div>
				  			<div class="form-group">
							  <label class="col-sm-3 control-label">密&nbsp;&nbsp;&nbsp;码：</label>
							  <div class="col-sm-8">
							    <input type="password" class="form-control" name="password" v-model="m.password">
							  </div>
							</div>
							<div class="form-group text-center" v-if="errorShow">
								<label class="control-label"><font color="red">{{ errorMsg }}</font></label>
							</div>
							<div class="form-group text-center">
							  <input type="hidden" name="id" v-model="m.id" /> 
							  <input class="btn green" type="submit" value="保存" />
							  <a class="btn default" @click="cancel">取消</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<div class="table-div">
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
				url : "databaseconfig/list_all",
				dataSrc : ""
			},
			columns : [  
				{                 
					title : "品牌名称",
					data : "brandName",
				},
				{                 
					title : "数据库名称",
					data : "name",
				},                 
				{                 
					title : "数据库连接地址",
					data : "url",
				},                 
				{                 
					title : "数据库驱动",
					data : "driverClassName",
				},
//				{
//					title : "用户名",
//					data : "username",
//				},
//				{
//					title : "密码",
//					data : "password",
//				},
				{                 
					title : "创建时间",
					data : "createTime",
					createdCell:function(td,tdData,rowData,row){
						//格式化时间
						$(td).html((new Date(tdData)).format("yyyy-MM-dd hh:mm:ss"));
					}
				},                 
				{                 
					title : "更新时间",
					data : "updateTime",
					createdCell:function(td,tdData,rowData,row){
						//格式化时间
						$(td).html((new Date(tdData)).format("yyyy-MM-dd hh:mm:ss"));
					}
				},                 
				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="databaseconfig/edit">
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
					errorMsg:"请录入完整信息！",
					errorShow:false
				},
				methods:{
					openForm:function(){
						this.showform = true;
					},
					closeForm:function(){
						this.m={};
						this.showform = false;
					},
					cancel:function(){
						this.errorMsg = "" ;
						this.errorShow = false ;
						this.m={};
						this.closeForm();
					},
					create:function(){
						this.m={};
						this.openForm();
					},
					edit:function(model){
						this.m= model;
						this.openForm();
					},
					save:function(e){
						//接收返回值
						var result = isNullCheck(this.m);
						if(result == true){//如果通过 则 执行响应方法
							var that = this;
							var formDom = e.target;
							C.ajaxFormEx(formDom,function(){
								that.cancel();
								tb.ajax.reload();
							});
						}else{//如果为有反馈信息则给出提示
							this.errorMsg = result ;
							this.errorShow = true ;
						}
					},
				},
			};
		var vueObj = C.vueObj(option);
	}());
	
	//判断表单中是否有空值
	function isNullCheck(obj){
		var flag = true;
		if(isNull(obj.name)){
			flag = "数据库名称 不能为空" ;
		}else if(isNull(obj.url)){
			flag = "数据库地址 不能为空！" ;
		}else if(isNull(obj.driverClassName)){
			flag = "数据库驱动 不能为空！" ;
		}else if(isNull(obj.username)){
			flag = "用户名 不能为空！" ;
		}else if(isNull(obj.password)){
			flag = "密码 不能为空！" ;
		}
		return flag;
	}
	
	//验证  字符串 是否为空
	function isNull(str){
		return str.replace(/(^s*)|(s*$)/g, "").length == 0 ;
	}
	
</script>
