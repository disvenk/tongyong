<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki">新建菜品类型</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form role="form" class="form-horizontal" action="{{m.id?'brandArticleFamily/modify':'brandArticleFamily/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
			           			<label class="col-sm-3 control-label">类型名称：</label>
							    <div class="col-sm-8">
									<input type="text" class="form-control" required name="name" v-model="m.name">
							    </div>
							</div>
							<div class="form-group">
			           			<label class="col-sm-3 control-label">序&nbsp;&nbsp;号：</label>
							    <div class="col-sm-8">
							    <input type="number" class="form-control" required placeholder="请输入数字！" min="0" name="peference" v-model="m.peference">
							    </div>
							</div>
							<div class="form-group">
			           			<label class="col-sm-3 control-label">就餐模式：</label>
							    <div class="col-sm-8">
							    <select class="form-control" name="distributionModeId" required v-model="selected">
							    	<option v-for="temp in distributionMode" v-bind:value="temp.id">
							    		{{ temp.name }}
							    	</option>
							    </select>
							    </div>
							</div>
							<!--图片状态-->
							<div class="form-group">
								<label class="col-md-3 control-label">是否开启类型图片: </label>
								<div class="col-md-8">
									<div class="md-radio-inline">
										<div class="md-radio">
											<!-- 判断是否 绑定的对象是否有值，如果没有则不绑定 -->
											<input type="radio" class="md-radiobtn" id="isStatus_yes" name="openPictureSwitch" value="1" v-model="m.openPictureSwitch" v-if="m.id">
											<input type="radio" class="md-radiobtn" id="isStatus_yes" name="openPictureSwitch" value="1" v-model="m.openPictureSwitch"  v-if="!m.id" >
											<label for="isStatus_yes">
												<span></span>
												<span class="check"></span>
												<span class="box"></span>开启
											</label>
										</div>
										<div class="md-radio">
											<input type="radio" class="md-radiobtn" id="isStatus_no" name="openPictureSwitch" value="0" v-model="m.openPictureSwitch" v-if="m.id">
											<input type="radio" class="md-radiobtn" id="isStatus_no" name="openPictureSwitch" value="0" v-if="!m.id" v-model="m.openPictureSwitch" checked="checked">
											<label for="isStatus_no">
												<span></span>
												<span class="check"></span>
												<span class="box"></span>关闭
											</label>
										</div>
									</div>
								</div>
							</div>
							<!--菜品类型图片-->
							<div class="form-group" v-if="m.openPictureSwitch == 1">
								<label class="col-sm-3 control-label">菜品类型图片：</label>
								<div class="col-sm-8">
			                        <input type="hidden" name="pictureUrl" v-model="m.pictureUrl">
			                        <img-file-upload class="form-control" @success="uploadSuccess"
			                                         @error="uploadError"></img-file-upload>
			                        <img v-if="m.pictureUrl" :src="m.pictureUrl" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
			                    </div>
							</div>	
							
							<div class="text-center">
								<input type="hidden" name="id" v-model="m.id" />
								<input class="btn green"  type="submit"  value="保存"/>
								<a class="btn default" @click="cancel" >取消</a>
							</div>
						</div>
					</form>
	            </div>
	        </div>
		</div>
	</div>
	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="brandArticleFamily/add">
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
				url : "brandArticleFamily/list_all",
				dataSrc : ""
			},
			columns : [
				{
					title : "序号",
					data : "level",
					createdCell:function(td,tdData,rowData,row){
						var num = row + 1;
						$(td).html(num);
					}
				},
                {
                    title : "销售类型",
                    data : "distributionModeId",
                    createdCell:function(td,tdData,rowData,row){
                        var str = "";
                        if("1" == tdData){
                            str = "堂食" ;
                        }else if("2"==tdData){
                            str = "外卖" ;
                        }else if("4"==tdData){
                            str = "堂食/外卖" ;
                        }
                        $(td).html(str);
                    }
                },
                {
					title : "菜品类别",
					data : "name",
				},
				{                 
					title : "是否启用图片",
					data : "openPictureSwitch",
					createdCell:function(td,tdData){
						if(tdData == 1){
							$(td).html("已启用");
						}else{
							$(td).html("未启用");
						}
					}
				},
				{                 
					title : "类型图片",
					data : "pictureUrl",
					createdCell:function(td,tdData){
						if(tdData == null){
							$(td).html("未上传");
						}else{
							$(td).html("<img src=\"" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:40px;width:80px;\"/>");
						}
					}
				},
				{
					title : "排序",
					data : "peference",
				},
				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="brandArticleFamily/delete">
							C.createDelBtn(tdData,"brandArticleFamily/delete"),
							</s:hasPermission>
							<s:hasPermission name="brandArticleFamily/edit">
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		
		var C = new Controller(cid,tb);
//		var vueObj = C.vueObj();
		
		var vueObj = new Vue({
			el:cid,
			mixins:[C.formVueMix],
			data:{				
				m:{},
                showform: false,
                distributionMode:[],
                selected:null
            },
            created:function(){
            	this.getDistributionMode();
            },
            methods:{
            	getDistributionMode:function(){
            		//获取 就餐模式
            		var that = this;
					$.ajax({
						type:"post",
						url:"brandArticleFamily/querydistributionMode",
						dataType:"json",
						success:function(data){
							that.distributionMode = data.data;
							that.selected = data.data[0].id;
						}
					})
            	},
            	openFrom:function(){
            		this.showform = true;
            	},
            	closeFrom:function(){
            		this.showform = false;
            	},           	
            	create:function(){
            		this.openFrom();
            		this.m = {};
            	},
            	save:function(e){
                	var that = this;
                    var url = that.m.id?'brandArticleFamily/modify':'brandArticleFamily/create';
                    var formDom = e.target;
					console.log($(formDom).serialize());
					if(that.m.openPictureSwitch == 1 && !that.m.pictureUrl){
						toastr.error("请先上传图片再保存~");
						return;
					}
					$.ajax({
                        url : url,
                        data : $(formDom).serialize(),
                        success : function(result) {
                            if (result.success) {
                                that.closeFrom();
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
				},		
            	edit:function(model){
            		this.m = model;
            		console.log(this.m);
            		this.openFrom();
            	},
            	uploadSuccess: function (url) {
                    console.log(url);
                    $("[name='pictureUrl']").val(url).trigger("change");
                    C.simpleMsg("上传成功");
                    $("#activityImg").attr("src", "/" + url);
                },
                uploadError: function (msg) {
                    C.errorMsg(msg);
                },
            }
		})
		C.vue=vueObj;
	}());
	
</script>
