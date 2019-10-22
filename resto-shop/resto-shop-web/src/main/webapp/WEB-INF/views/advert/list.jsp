<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>

<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki">新增店铺介绍</span>
					</div>
				</div>
				<div class="portlet-body">
					<form role="form" class="form-horizontal" action="{{m.id?'advert/modify':'advert/create'}}" @submit.prevent="save">
						<div class="form-body" style="overflow-y: hidden; overflow-x: hidden;" >
							<div class="form-group">
								<label class="col-sm-2 control-label">标题：</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" required name="slogan" v-model="m.slogan">
								</div>
							</div>
							<div class="form-group " >
								<label class="col-sm-2 control-label">描述：</label>
								<div class="col-sm-8" >
									<textarea id="description" name="description" style="height:300px;" v-model="m.description">
									</textarea>
								</div>
							</div>
						</div>
						<div class="text-center">
							<input type="hidden" name="id" v-model="m.id" />
							<input class="btn green"  type="submit"  value="保存"/>
							<a class="btn default" @click="cancel" >取消</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="advert/add">
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

<div class="modal fade">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title text-center"><strong></strong></h4>
			</div>
			<div class="modal-body" style="word-wrap: break-word;">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
<script>

	(function(){

		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "advert/list_all",
				dataSrc : ""
			},
			columns : [
				{
					title : "标题",
					data : "slogan",
				},
				{
					title : "详情",
					defaultContent:"",
					createdCell:function(td,tdData,rowData){
						var button = $("<button class='btn green'>详情</button>");
						button.click(function(){
							showDetails(rowData);
						})
						$(td).html(button);
					}
				},
				{
					title : "状态",
					data : "state",
				},
				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="advert/delete">
							C.createDelBtn(tdData,"advert/delete"),
							</s:hasPermission>
							<s:hasPermission name="advert/edit">
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
				openForm:function(){
					this.showform = true;
				},
				closeForm:function(){
					this.m={};
					this.showform = false;
				},
				cancel:function(){
					this.m={};
					this.closeForm();
				},
				create:function(){
					this.m={};
					this.openForm();
					this.initEditor();
				},
				edit:function(model){
					this.m= model;
					this.openForm();
					this.initEditor();
				},
				initEditor : function () {
					Vue.nextTick(function(){
						var editor = new wangEditor('description');
						editor.config.uploadImgFileName = 'file';
						editor.config.uploadImgUrl = 'upload/file';
						editor.create();
					});
				}
			}
		});
		C.vue=vueObj;
	}());

	//用于显示描述详情
	function showDetails(obj){
		$(".modal-title > strong").html(obj.slogan);
//		var html='';
//		for(var i=0;i<obj.length;i++){
//            html+='<tr><td>'+obj[i].name+'</td><td></td></tr>'
//		}
//        $(".modal-body").html(html);
		$(".modal-body").html(obj.description);
		$(".modal").modal();
	}

</script>
