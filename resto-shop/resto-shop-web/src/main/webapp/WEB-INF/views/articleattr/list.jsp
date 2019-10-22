<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki">新增菜品规格</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form class="form-horizontal" role="form" action="{{m.id?'articleattr/modify':'articleattr/create'}}" @submit.prevent="save">
				  		<div class="form-body">
							<div class="form-group">
							  <label class="col-sm-2 control-label">属&nbsp;性：</label>
							  <div class="col-sm-8">
							    <input type="text" class="form-control" name="name" required v-model="m.name">
							  </div>
							</div>
							<div class="form-group">
							  <label class="col-sm-2 control-label">排&nbsp;序：</label>
							  <div class="col-sm-8">
							    <input type="number" class="form-control" placeholder="请输入数字！" name="sort" required v-model="m.sort">
							  </div>
							</div>
							<div class="form-group" v-for="unit in articleUnits">
								<input type="hidden" name="unitIds" value="{{unit.id}}"/>
								<label class="col-sm-2 control-label">规 格{{$index+1}}：</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" name="units" value="{{unit.name}}" required>
								</div>
								<label class="col-sm-2 control-label">排&nbsp;序：</label>
								<div class="col-sm-3">
									<input type="number" class="form-control" placeholder="请输入数字！" name="unitSorts" value="{{unit.sort}}" required>
								</div>
								<div class="col-sm-1">
									<a class="btn red" @click="removeUnit(unit)">移除</a>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-8 col-md-offset-2">
							  		<a class="btn blue btn-block" @click="addUnit">添加规格</a>
							  	</div>
							</div>
							<div class="form-group text-center">
								<input type="hidden" name="id" v-model="m.id" />
							  	<input class="btn green" type="submit" value="保存"/>
							  	<a class="btn default" @click="cancel">取消</a>
							</div>
						</div>
					</form>
	            	
	            </div>
	        </div>
		</div>
	</div>
	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="articleattr/add">
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
				url : "articleattr/list_all",
				dataSrc : ""
			},
			columns : [
			{                 
				title : "属性",
				data : "name",
			},                 
			{                 
				title : "规格",
				data : "articleUnits",
				createdCell:function(td,tdData,rowData){
					var str = "";
					if(tdData){
						$(tdData).each(function(i,item){
							str += "<span class='label label-info'>"+item.name +"</span>&nbsp;&nbsp;"
						})
					}else{
						str = "暂无数据"
					}
					
					$(td).html(str);
				}
			},                 
			{                 
				title : "排序",
				data : "sort",
			}, 
			{
				title : "操作",
				data : "id",
				createdCell:function(td,tdData,rowData,row){
					console.log(rowData);
					var operator=[
						<s:hasPermission name="articleattr/delete">
						C.createDelBtn(tdData,"articleattr/delete"),
						</s:hasPermission>
						<s:hasPermission name="articleattr/edit">
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
			data:{
				articleUnits:[],
			},
			methods:{
				addUnit:function(e){
					this.articleUnits.push({ name:"", sort:0 });
				},
				removeUnit:function(unit){
					this.articleUnits.$remove(unit);
				},
				edit:function(model){
					this.articleUnits = model.articleUnits == null ? [] : model.articleUnits;
					this.m= model;
					this.openForm();
				},
				create:function(){
					this.articleUnits = [];
					this.m={};
					this.openForm();
				},
			}
		});
		C.vue=vueObj;
	}());
</script>
