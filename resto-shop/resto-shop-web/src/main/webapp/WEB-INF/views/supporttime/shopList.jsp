<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki"> 菜品供应时间</span>
	                </div>
	            </div>
	            
	            <div class="portlet-body">
		            <form role="form" class="form-horizontal" action="{{m.id?'supporttime/modify':'supporttime/create'}}" @submit.prevent="save">
						<input type="hidden" name="id" v-model="m.id" />
						<div class="form-body">
								<div class="form-group">
									<label  class="col-sm-2 control-label">名称</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="name" required v-model="m.name">
									</div>
								</div>
								<div class="form-group">
									<label  class="col-sm-2 control-label">开始时间</label>
									<div class="col-sm-8">
										<div class="input-group">
											<input type="text" class="form-control timepicker timepicker-no-seconds" required name="beginTime" @focus="initTime" v-model="m.beginTime">
											<span class="input-group-btn">
											<button class="btn default" type="button">
												<i class="fa fa-clock-o"></i>
											</button>
										</span>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label  class="col-sm-2 control-label">结束时间</label>
									<div class="col-sm-8">
										<div class="input-group">
											<input type="text" class="form-control timepicker timepicker-no-seconds" required name="endTime" @focus="initTime" v-model="m.endTime">
											<span class="input-group-btn">
											<button class="btn default" type="button">
												<i class="fa fa-clock-o"></i>
											</button>
										</span>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label  class="col-sm-2 control-label">供应时间</label>
									<div class="col-sm-8">
										<label v-for="day in supportDay">
											<input type="checkbox" name="activated" :value="day[1]"  v-model="checkedValues"> {{day[0]}} &nbsp;&nbsp;
										</label>
										<input type="hidden" class="form-control" name="supportWeekBin" id="supportWeekBin" :value="getSum">
									</div>
								</div>
							<div class="form-group">
								<label  class="col-sm-2 control-label">折扣</label>
								<div class="col-sm-8">
									<div class="input-group">
										<input class="form-control" type="number" name="discount" min="1"  max="100" v-model="m.discount" required placeholder="请输入1-100整数值">
										<div class="input-group-addon">%</div>
									</div>
									<span class="help-block">请输入1-100整数值</span>
								</div>
							</div>
							<div class="form-group">
								<label  class="col-sm-2 control-label">备注</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" name="remark" v-model="m.remark">
								</div>
							</div>
					</div>
						<div class="form-group text-center">
							<input class="btn green"  type="submit"  value="保存"/>&nbsp;&nbsp;&nbsp;
							<a class="btn default" @click="cancel" >取消</a>
						</div>
					</form>
	            </div>
	        </div>
		</div>
	</div>
	
	<div class="table-div">
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

		var supportDay=[
			            ["周一",1<<0],			//	1			1
			            ["周二",1<<1],			//	2
			            ["周三",1<<2],			//	4
			            ["周四",1<<3],			//	8
			            ["周五",1<<4],			//	16
			            ["周六",1<<5],			//	32
			            ["周日",1<<6],			//	64
//			            ["工作日",1<<7],			不需要了
//			            ["非工作日",1<<8],		不需要了
			            ];
		function getWeekDayArr(weekBin){
			var arr = [];
			for(var i=0;i<supportDay.length;i++){
				var day = supportDay[i];
				if(weekBin&day[1]){
					arr.push(day);
				}
			}
			return arr;
		}
		
		var tb = $table.DataTable({
			ajax : {
				url : "supporttime/shop/list_all",
				dataSrc : ""
			},
			columns : [
				{
					title : "名称",
					data : "name",
				},
				{
					title : "折扣",
					data : "discount",
					createdCell : function(td,tdData){
						$(td).html("<span class='label label-primary'>"+tdData+"%</span>");
					}
				},
				{                 
					title : "开始时间",
					data : "beginTime",
				},                 
				{                 
					title : "结束时间",
					data : "endTime",
				},                 
				{                 
					title : "供应时间",
					data : "supportWeekBin",
					createdCell:function(td,tdData){
						var dayArr = getWeekDayArr(tdData);
						$(td).html("");
						for(var i=0;i<dayArr.length;i++){
							$(td).append(dayArr[i][0]);
						}
					}
				},                 
				{                 
					title : "备注",
					data : "remark",
				},                 
				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="supporttime/edit">
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		
		var C = new Controller(null,tb);
		var vueObj = new Vue({
			mixins:[C.formVueMix],
			el:"#control",
			data:{
				checkedValues: [],
				supportDay:supportDay
			},
			computed: {
			 	getSum: function () {
			 		var s=0;
			 		for(var i in this.checkedValues){
			 			var week = this.checkedValues[i];
			 			s+=parseInt(week);
			 		}
			      return s;
			    },
			},
			methods:{
				initTime :function(){
					$(".timepicker-no-seconds").timepicker({
						 autoclose: true,
						 showMeridian:false,
			             minuteStep: 5
					  });
				},
				closeForm:function(){
					this.m={};
					this.showform=false;
					this.checkedValues=[];
				},
				create:function(){
					this.m={
						beginTime : '10:00',
						endTime : '22:00',
						discount : 100
					};
					this.checkedValues=[];
					this.openForm();
					this.initTime();
				},
				edit:function(model){
					var that = this;
					this.m= model;
					this.openForm();
					this.checkedValues=[];
					var dayArr = getWeekDayArr(this.m.supportWeekBin);
					for(var i=0;i<dayArr.length ;i++){
						this.checkedValues.push(dayArr[i][1]);
					}
				},
				save:function(e){
					if($("input:checked").length>0){
						var that = this;
						var formDom = e.target;
						C.ajaxFormEx(formDom,function(){
							that.cancel();
							tb.ajax.reload();
						});
					}else {
						toastr.error("请选择供应时间！");
						return false;
					}
				}
			}
		});
		C.vue=vueObj;
	}());
	
</script>
