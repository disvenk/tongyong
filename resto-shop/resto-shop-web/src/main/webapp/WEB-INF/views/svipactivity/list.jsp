<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style type="text/css">
	.form-horizontal .control-label {
		width: 140px;
		text-align: left;
	}
	.control-padding {
		padding:7px 15px 0 15px;
		text-align: left;
	}
	.md-radio-inline .md-radio {
		margin: 0 10px;
	}
	.flex-container {
	    display: -webkit-flex;
	    display: flex;
	    -webkit-justify-content: flex-start;
	    justify-content: flex-start;
	}
	.banner {
		padding: 0 10px;
		width: 100px;
		height: 50px;
	}
	.flex-item {
	    width: 100px;
	}
</style>
<div id="app">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-2 col-md-8" >
			<div class="portlet light bordered">
			<div class="portlet-title">
				<div class="caption">
					<span class="caption-subject bold font-blue-hoki">新建活动配置</span>
				</div>
			</div>
            <div class="portlet-body">
            	<form role="form" class="form-horizontal" action="{{m.id?'svipactivity/modify':'svipactivity/create'}}" @submit.prevent="save">											
					<div class="form-group">
						<label class="col-md-3 control-label"><strong>活动名称 <span class="starColor">*</span></strong></label>
						<div class="col-md-8">
							<input type="text" class="form-control" name="activityName" v-model="m.activityName" maxlength="10">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-3 control-label"><strong>活动周期 <span class="starColor">*</span></strong></label>
                        <div class="row">
                            <div class="col-md-4">
                                <div class="input-group date form_datetime">
                                    <input type="text" readonly class="form-control" name="beginDateTime" v-model="m.beginDateTime" @focus="initCouponTime"> <span class="input-group-btn">
									<button class="btn default date-set" type="button">
                                        <i class="fa fa-calendar" @click="initCouponTime"></i>
                                   </button>
                                </div>
                            </div>
                            <div class="col-md-1 control-padding" style="width: 30px;">至</div>
                            <div class="col-md-4">
                                <div class="input-group date form_datetime">
                                    <input type="text" readonly class="form-control" @focus="initCouponTime" name="endDateTime" v-model="m.endDateTime"> <span class="input-group-btn">
									<button class="btn default date-set" type="button">
                                        <i class="fa fa-calendar" @click="initCouponTime"></i>
                                    </button>											
                                </div>
                            </div>
                        </div>
                    </div>
                    
					<div class="form-group">
						<label class="col-md-3 control-label"><strong>会员价格 <span class="starColor">*</span></strong></label>
                        <div class="col-md-8">
                        	<input type="number" class="form-control" name="svipPrice" v-model="m.svipPrice" placeholder="(建议输入整数)" required="required" min="1">
                       	</div> 	
                       	<div class="control-padding">元</div>
                    </div>
                    
                    <div class="form-group">
						<label class="col-md-3 control-label"><strong>会员有效周期 <span class="starColor">*</span></strong></label>
						<div class="col-md-8">
							<div class="md-radio-inline">
								<div class="md-radio">
									<!-- 判断是否 绑定的对象是否有值，如果没有则不绑定 -->
									<input type="radio" class="md-radiobtn" id="isAddRatio_yes" name="svipExpireType" value="0" v-model="m.svipExpireType">
									<label for="isAddRatio_yes" @click="changeExpire">
										<span></span>
										<span class="check"></span>
										<span class="box"></span>按周期
									</label>
								</div>
								<div class="md-radio" @click="changeExpire">
									<input type="radio" class="md-radiobtn" id="isAddRatio_no" name="svipExpireType" value="1" v-model="m.svipExpireType">
									<label for="isAddRatio_no" @click="changeExpire">
										<span></span>
										<span class="check"></span>
										<span class="box"></span>长期有效
									</label>
								</div>
							</div>
						</div>
					</div>
					
                    <div class="form-group" v-if="m.svipExpireType == 0">
                    	<div class="col-md-2 control-padding" style="text-align: right;">加入会员</div>
                    	<div class="col-md-4">
                    		<input type="number" class="form-control" name="svipExpire" v-model="m.svipExpire" placeholder="(建议输入整数)" required="required" min="1">
                    	</div>
                    	<div class="col-md-2 control-padding" style="text-align: left;">内有效</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-3 control-label"><strong>活动图片 <span class="starColor">*</span></strong></label>
						<div class="col-md-8">
	                        <input type="hidden" name="activityImg" v-model="m.activityImg">
	                        <img-file-upload class="form-control" @success="uploadSuccess"
	                                         @error="uploadError"></img-file-upload>
	                        <img v-if="m.activityImg" :src="m.activityImg" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
	                    </div>
					</div>	
					<!--活动状态-->
					<div class="form-group">
						<label class="col-md-3 control-label"><strong>活动状态 <span class="starColor">*</span></strong></label>
						<div class="col-md-8">
							<div class="md-radio-inline">
								<div class="md-radio">
									<!-- 判断是否 绑定的对象是否有值，如果没有则不绑定 -->
									<input type="radio" class="md-radiobtn" id="isStatus_yes" name="activityStatus" value="1" v-model="m.activityStatus" v-if="m.id">
									<input type="radio" class="md-radiobtn" id="isStatus_yes" name="activityStatus" value="1"  v-if="!m.id" checked="checked">
									<label for="isStatus_yes">
										<span></span>
										<span class="check"></span>
										<span class="box"></span>开启
									</label>
								</div>
								<div class="md-radio">
									<input type="radio" class="md-radiobtn" id="isStatus_no" name="activityStatus" value="0" v-model="m.activityStatus" v-if="m.id">
									<input type="radio" class="md-radiobtn" id="isStatus_no" name="activityStatus" value="0" v-if="!m.id">
									<label for="isStatus_no">
										<span></span>
										<span class="check"></span>
										<span class="box"></span>关闭
									</label>
								</div>
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
	
	<div class="portlet light bordered" style="padding: 0;">
		<div class="portlet-title text-center" style="min-height: initial;padding: 10px 20px;margin: 0;">
			<div class="caption">
				<span class="caption-subject bold font-blue-hoki">
					<i style="font-size: 12px;color: #989898;font-style: normal;">微信活动管理</i><i style="padding: 5px;">/</i><strong style="color: #333;">付费会员活动</strong>
				</span>
			</div>
		</div>
	</div>
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="svipactivity/add">
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
		var C;
		var vueObj;
		var cid="#app";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "svipactivity/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
					title : "活动名称",
					data : "activityName",
				},                 
				{                 
					title : "活动周期",
					data : "beginDateTime",
					createdCell : function(td,tdData,row,rowData){
                        $(td).html(new Date(row.beginDateTime).format("yyyy-MM-dd hh:mm:ss")+"到"+new Date(row.endDateTime).format("yyyy-MM-dd hh:mm:ss"));
                    }
				},                                
				{                 
					title : "付费会员价格",
					data : "svipPrice",
				},                 
				{                 
					title : "会员有效周期",
					data : "svipExpire",
					createdCell : function(td,tdData,row,rowData){
                        if(tdData == 0){
                        	$(td).html("长期有效");
                        }else{
                        	var dayNum = tdData+"天";
                        	$(td).html(dayNum);
                        }
                    }
				},                 
				{                 
					title : "活动图片",
					data : "activityImg",
					createdCell:function(td,tdData){
						if(tdData !=null){
							$(td).html("<img src=\"" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:40px;width:80px;\"/>");
						}
					}
				},                 
				{                 
					title : "活动状态",
					data : "activityStatus",
                    createdCell:function (td,tdData) {//td中的数据
                        switch (tdData){
                            case 0:tdData='未启用';break;
                            case 1:tdData='已启用';break;
                        }
                        $(td).html(tdData);
                    }
				},                 

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="svipactivity/modify">
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		
		C = new Controller(cid,tb);
		var vueObj = new Vue({
			el:cid,
			mixins:[C.formVueMix],
			data:{
				m:{
					activityName:"",	//活动名称
					svipExpireType:0,	//活动有效周期0按周期1永久有效
					beginDateTime:"",	//开始时间
	        		endDateTime:"",		//结束时间
	        		svipPrice:"",		//会员付费价格num
	        		svipExpire:"",		//会员有效期时间
	        		activityImg:"",		//图片路径
	        		activityStatus:""	//活动状态
				},
                showform: false
            },
            methods:{
            	openFrom:function(){
            		this.showform = true;
            	},
            	closeFrom:function(){
            		this.showform = false;
            	},
            	cancel:function(){
                	this.closeFrom();
                },            	
            	create:function(){
            		this.openFrom();
            		this.m = {
						activityName:"",	//活动名称
						svipExpireType:0,	//活动有效周期0按周期1永久有效
						beginDateTime:"",	//开始时间
		        		endDateTime:"",		//结束时间
		        		svipPrice:"",		//会员付费价格num
		        		svipExpire:"",		//会员有效期时间
		        		activityImg:"",		//图片路径
		        		activityStatus:""	//活动状态
					};
            	},
            	edit:function(model){
            		console.log(JSON.stringify(model));
            		this.m = model;
            		var temp1 = this.m.beginDateTime;
            		var temp2 = this.m.endDateTime;
            		var beginDate;
                    var endDate;
            		beginDate = new Date(temp1).format("yyyy-MM-dd hh:mm:ss");
                    endDate = new Date(temp2).format("yyyy-MM-dd hh:mm:ss");
                    if(beginDate=='NaN-aN-aN aN:aN:aN'){
                        beginDate = temp1;
                    }
                    if(endDate=='NaN-aN-aN aN:aN:aN'){
                        endDate = temp2;
                    }
                    this.m.beginDateTime = beginDate;
                    this.m.endDateTime = endDate;
            		this.openFrom();
            	},
            	changeExpire:function(){
            		if(this.m.svipExpireType == 0){
            			this.m.svipExpireType = 1;
            		}else{
            			this.m.svipExpireType = 0;
            		}        		
            	},
            	initCouponTime: function(){
                    //初始化当前为当前时间
                    $('.form_datetime').datetimepicker({
                        format: "yyyy-mm-dd hh:ii:ss",
                        autoclose: true,
                        todayBtn: true,
                        todayHighlight: true,
                        showMeridian: true,
                        pickerPosition: "bottom-left",
                        language: 'zh-CN',//中文，需要引用zh-CN.js包
                        startView: 2,//月视图
                        //minView: 2//日期时间选择器所能够提供的最精确的时间选择视图
                    });
                },
                uploadSuccess: function (url) {
                    console.log(url);
                    $("[name='activityImg']").val(url).trigger("change");
                    C.simpleMsg("上传成功");
                    $("#activityImg").attr("src", "/" + url);
                },
                uploadError: function (msg) {
                    C.errorMsg(msg);
                },              
                save:function(e){
                	var that = this;
                    var url = that.m.id?'svipactivity/modify':'svipactivity/create';
                    var formDom = e.target;
					console.log($(formDom).serialize());
                    $.ajax({
                        url : url,
                        data : $(formDom).serialize(),
                        success : function(result) {
                            if (result.success) {
                                that.cancel();
                                tb.ajax.reload();
                                toastr.clear();
                                toastr.success("保存成功!");
                            } else {
                                tb.ajax.reload();
                                toastr.clear();
                                toastr.error(result.message);
                            }
                        },
                        error : function() {
                            tb.ajax.reload();
                            toastr.clear();
                            toastr.error("保存失败");
                        }
                    })
                }
            }
		});
		C.vue=vueObj;
	}());
		
</script>
