<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki"> 表单</span>
					</div>
				</div>
				<div class="portlet-body">
					<form role="form" action="{{m.id?'shopmode/modify':'shopmode/create'}}"	@submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
								<label>模式ID</label> 
								<input type="text" class="form-control"	name="id" :value="m.id" placeholder="必填" required="required">
							</div>
							<div class="form-group">
								<label>店铺模式</label> 
								<input type="text" class="form-control" name="name" v-model="m.name" placeholder="必填" required="required">
							</div>
							<div class="form-group">
								<label>店铺模式描述</label> <input type="text" class="form-control"
									name="remark" v-model="m.remark">
							</div>
						</div>
						 <input	class="btn green" type="submit" value="保存" /> 
						 <a class="btn default" @click="cancel">取消</a>
				</form>
				</div>
			</div>
		</div>
	</div>

	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="shopmode/add">
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
				url : "shopmode/list_all",
				dataSrc : ""
			},
			columns : [
						{                 
							title : "模式ID",
							data : "id",
						}, 
						{                 
							title : "店铺模式",
							data : "name",
						},                 
						{                 
							title : "店铺描述",
							data : "remark",
						},
						{
							title : "操作",
							data : "id",
							createdCell:function(td,tdData,rowData,row){
								var operator=[
									<s:hasPermission name="shopmode/delete">
									C.createDelBtn(tdData,"shopmode/delete"),
									</s:hasPermission>
									<s:hasPermission name="shopmode/edit">
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
