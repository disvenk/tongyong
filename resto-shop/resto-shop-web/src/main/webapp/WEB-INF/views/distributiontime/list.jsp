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
	            	<form role="form" action="{{m.id?'distributiontime/modify':'distributiontime/create'}}" @submit.prevent="save">
						<div class="form-body">
						
						<div class="form-group">
						   <labe>点餐开始时间</label>
							  <div class="input-group">
							<input type="text" class="form-control timepicker timepicker-no-seconds" name="beginTime" @focus="initTime" v-model="m.beginTime">
							<span class="input-group-btn">
								<button class="btn default" type="button">
									<i class="fa fa-clock-o"></i>
								</button>
							</span>
							</div>
				        </div>
				        
						<div class="form-group">
						   <labe>停止点餐时间</label>
							  <div class="input-group">
							<input type="text" class="form-control timepicker timepicker-no-seconds" name="stopOrderTime" @focus="initTime" v-model="m.stopOrderTime">
							<span class="input-group-btn">
								<button class="btn default" type="button">
									<i class="fa fa-clock-o"></i>
								</button>
							</span>
							</div>
				        </div>
				        
						<div class="form-group">
						    <label>描述</label>
						    <input type="text" class="form-control" name="remark" v-model="m.remark">
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
			<s:hasPermission name="distributiontime/add">
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
	
	$(document).ready(function(){
		var C;
		var vueObj;
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "distributiontime/list_all",
				dataSrc : ""
			},
			columns : [
								{                 
					title : "点餐开始时间",
					data : "beginTime",
					"render":function(data){
						return (new Date(data).format('hh:mm:ss'))
					}
				},                 
				{                 
					title : "点餐结束时间",
					data : "stopOrderTime",
					"render":function(data){
						return (new Date(data).format('hh:mm:ss'))
					}
				},                 
				{                 
					title : "描述",
					data : "remark",
				},                 
				/* {                 
					title : "店铺",
					data : "shopDetailId",
				},      */            

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="distributiontime/delete">
							C.createDelBtn(tdData,"distributiontime/delete"),
							</s:hasPermission>
							<s:hasPermission name="distributiontime/edit">
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		
		var option = {
				el:cid,
				data:{
					m:{},
					showform:false,
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
						this.m={};
						this.closeForm();
					},
					create:function(){
						this.m={};
						this.openForm();
					},
					edit:function(model){
						this.m= model;
						//格式时间
						var tem1 = this.m.beginTime;
						var tem2 = this.m.stopOrderTime;
						var beginTime;
						var stopOrderTime;
						beginTime= new Date(tem1).format("hh:mm");
						stopOrderTime =  new Date(tem2).format("hh:mm");
						if(beginTime=='aN:aN'){
							beginTime=tem1;
						}
						if(stopOrderTime=='aN:aN'){
							stopOrderTime=tem2;
						}
						this.m.beginTime=beginTime;
						this.m.stopOrderTime=stopOrderTime;
						this.openForm();
					},
					save:function(e){
						var that = this;
							var formDom = e.target;
							C.ajaxFormEx(formDom,function(){
								that.cancel();
								tb.ajax.reload();
							});
					},
					
					initTime :function(){
						$(".timepicker-no-seconds").timepicker({
							 autoclose: true,
							 showMeridian:false,
							 showInputs:false,
				             minuteStep: 5
						    });
					},
					
				},
			};
		 C = new Controller(cid,tb);
		vueObj = C.vueObj(option);
	}());
	
	

	
</script>
