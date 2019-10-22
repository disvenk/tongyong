<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki">添加轮播图片</span>
					</div>
				</div>
				<div class="portlet-body">
					<form role="form" class="form-horizontal" action="{{m.id?'pictureslider/modify':'pictureslider/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
								<label class="col-sm-3 control-label">图片名称：</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" name="title" v-model="m.title">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">图片路径：</label>
								<div class="col-sm-8">
								    <input type="hidden" name="pictureUrl" v-model="m.pictureUrl">
								    <img-file-upload  class="form-control" @success="uploadSuccess" @error="uploadError" cut="false"></img-file-upload>
								    <img v-if="m.pictureUrl" :src="m.pictureUrl" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">排序：</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" name="sort" v-model="m.sort">
								</div>
							</div>
						</div>
						<div class="text-center">
							<input type="hidden" name="id" v-model="m.id" />
							<input class="btn green" type="submit" value="保存" />
							<a class="btn default" @click="cancel">取消</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="pictureslider/add">
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
				url : "pictureslider/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
					title : "图片名称",
					data : "title",
				},                 
				{                 
					title : "图片预览",
					data : "pictureUrl",
					defaultContent:'',
					createdCell:function(td,tdData){
						if(tdData !=null && tdData.substring(0,4)=="http"){
							$(td).html("<img src=\"" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:40px;width:80px;\"/>");
						}else{
							$(td).html("<img src=\"/" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:40px;width:80px;\"/>");
						}
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
						var operator=[
							<s:hasPermission name="pictureslider/delete">
							C.createDelBtn(tdData,"pictureslider/delete"),
							</s:hasPermission>
							<s:hasPermission name="pictureslider/edit">
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
				uploadSuccess:function(url){
					$("[name='pictureUrl']").val(url).trigger("change");
					C.simpleMsg("上传成功");
				},
				uploadError:function(msg){
					C.errorMsg(msg);
				},
			}
		});
		C.vue=vueObj;
	}());
	
	

	
</script>
