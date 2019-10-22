<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style type="text/css">
	.starColor {
		color: #E02612;
	}
</style> 
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
					<form role="form" method="post" enctype="multipart/form-data" action="{{m.id?'version/modify':'version/createPackage'}}" @submit.prevent="save">
						<div class="form-body">
							
							<div class="form-group" v-if="!isEdit">
								<label>package包</label>
								<input type="text" class="form-control" name="packageName" required v-model="m.packageName">
							</div>						
							<div class="form-group" v-else>
								<label>package包：</label>
								<span type="text" name="packageName">{{m.packageName}}</span>
							</div>
							
							<div class="form-group" style="margin-top: 15px;" v-if="!isEdit">
								<label>选择上传文件<span class="starColor">*</span></label>
								<input type="hidden" name="file" v-model="m.file">
								<txt-file-upload class="form-control" @success="uploadSuccess" @error="uploadError"></txt-file-upload>
							</div>
							<div class="form-group" v-else>
								<label>下载地址：</label>
								<span type="text" name="downloadAddress">{{m.downloadAddress}}</span>
							</div>
							
							<!--选择品牌-->
                            <div class="form-group">
			           			<label>关联品牌<span class="starColor">*</span></label>
			           			<button class="btn green" style="margin-left: 30px;" type="button" @click="showBrandList">关联品牌</button>		
							</div>
							<!--已关联的品牌-->
							<div class="form-group" v-if="this.brandNameList.length>0">
			           			<label>已关联品牌列表:</label>
			           			<div v-for="name in brandNameList" style="font-size: 16px;">{{name}}</div>
							</div>
						</div>
						<input type="hidden" name="id" v-model="m.id" />
						<input class="btn green" type="submit" value="保存" />
						<a class="btn default" @click="cancel">取消</a>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<!--关联的菜品-->
	<div class="row form-div" v-show="relationBrand">
        <div class="col-md-offset-3 col-md-6" style="background:#FFF;font-size: 18px;">
            <div class="text-center" style="padding: 20px 0">
                <span>关联品牌</span>
            </div>
            <div class="row" style="font-size: 16px;max-height: 250px;overflow: auto;padding:0 10%;">
            	<!--<div class="modal-body" style="padding: 0;">
                    <input type="text" id="search_ay" class="form-control" placeholder="请输入品牌名称" v-model="searchName">
                </div>-->
                <div style="margin: 20px 0;">
                	<label class="checkbox-inline" v-for="f in brandList" v-if="!isEdit">
						<input type="checkbox" class="brandBtn" v-model="f.status" @click="changeStatus(f)">{{f.brandName}}
					</label> 
					<label class="checkbox-inline" v-for="f in editList" v-if="isEdit">
						<input type="checkbox" class="brandBtn" v-model="f.status" @click="changeStatus(f)">{{f.brandName}}
					</label> 
                </div>       		
            </div>  
            <div class="text-center" style="padding: 20px 0">
	        	<button class="btn green" @click="saveRelationBrand">保存</button>
	        	<!--<button class="btn default" @click="closeRelationBrand">取消</button> -->                        
	        </div>
        </div>   
    </div>
	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="template/add">
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
		
	Vue.component('txt-file-upload', {
		mixins: [fileUploadMix],
		data: function() {
			return {
				types: [".json"],
				postUrl: "version/uploadPackage"
			}
		}
	});
	(function() {
		var cid = "#control";
		var vueObj = new Vue({
			el: "#control",
			data: {
				m: {},
				showform: false,
				tb: '',
				C: '',
				isEdit: false,
				brandList:[],			//所有品牌集合	
				editList:[],
				relationBrand: false,
				searchName:'',
				checkNames:[],			//已选择的品牌ID集合
				brandNameList:[]		
			},
			//mixins:[C.formVueMix],
			created: function() {
				var that = this;	
				$.ajax({
					url: "brand/list_all",
					type: "post",
					async: false,
					dataType: "json",
					success: function(res) {
						for(i in res){
							var item = res[i];
							item.status = false;
							that.brandList.push(item);
						}											
					}
				})
				that.createTb();
				this.C = new Controller(null, that.tb);
			},
			computed:{
				brandIdList:function(){
					var idList = [];
					if(this.isEdit){
						this.editList.forEach(function(item){
							if(item.status){
								var flag = {};
								flag.brandId = item.id;
								idList.push(flag);
							}						
						})
					}else{
						this.brandList.forEach(function(item){
							if(item.status){
								var flag = {};
								flag.brandId = item.id;
								idList.push(flag);
							}						
						})
					}
					
					return idList;
				}
			},
			methods: {
				changeStatus:function(f){
					f.status = !f.status;
				},
				showBrandList:function(){
					var that = this;		
					if(this.editList.length>0){
						this.brandNameList.forEach(function(temp){
							that.brandList.forEach(function(item){
								if(temp.brandName == item.brandName){
									item.status = true;								
								}
							})						
						})							
					}else{
						if(this.m.versionBrandPackage){
							this.m.versionBrandPackage.forEach(function(temp){
								that.brandList.forEach(function(item){
									if(temp.brandId == item.id){
										item.status = true;								
									}
								})						
							})
						}
					}					
											
					this.showform =false;
					this.relationBrand = true;
					if(this.isEdit){
						//this.editList=$.extend(true,[],that.brandList);
						this.editList=that.brandList;
					}
					console.log(this.editList);
				},
				saveRelationBrand:function(){
					var that = this;
					that.brandNameList = [];	
					that.brandList.forEach(function(item){
						if(item.status){
							that.brandNameList.push(item.brandName);
						}
					})									
					this.relationBrand = false;
					this.showform = true;
					console.log(that.brandList);
				},
				createTb: function() {
					var that = this;
					var $table = $(".table-body>table");					
					
					this.tb = $table.DataTable({
						ajax: {
							url: "version/list_package",
							dataSrc: ""
						},
						columns: [
							{
								title: "package号",
								data: "packageName",
							},
							{
								title: "下载地址",
								data: "downloadAddress",
								createdCell: function(td, tdData, rowData, row) {
									var a = $("<a href=\"" + rowData.downloadAddress + "\" target=\"_blank\">" + rowData.downloadAddress + "</a>");
									$(td).html(a);
								}
							},
							{
								title: "已关联品牌",
								data: "versionBrandPackage",
								createdCell: function(td, tdData, rowData, row) {
									var that = this;
									var nameList = [];
									if(rowData.versionBrandPackage.length>0){
										rowData.versionBrandPackage.forEach(function(temp){
											vueObj.brandList.forEach(function(item){
												if(temp.brandId == item.id){
													nameList.push(item.brandName);													
												}
											})
										})
										$(td).html(nameList.join(","));									
									}else{
										$(td).html("无");
									}																		
								}
							},
							{
                                title : "操作",
                                data : "id",
                                createdCell:function(td,tdData,rowData,row){                                  
                                    var operator=[
                                        createContactBtn(rowData)               
                        			];
                    				$(td).html(operator);
                                }
                            }
						]
					});					
				},
				cancel: function() {
					this.m = {};
					this.showform = false;
					this.checkNames = [];
					this.brandNameList = [];
				},
				create: function() {
					this.m = {};
					this.isEdit = false;
					this.showform = true;
					this.brandList.forEach(function(item){
						item.status = false;						
					})
				},
				showEditView:function(data){
					var that = this;
					this.m = data;
					console.log(data);
					//this.brandNameList = data.versionBrandPackage;					
					that.brandNameList = [];
					data.versionBrandPackage.forEach(function(temp){
						that.brandList.forEach(function(item){
							if(temp.brandId == item.id){
								that.brandNameList.push(item.brandName);
							}
						})
					})					
					this.isEdit = true;
					this.showform = true;
				},
				save: function(e) {
					var that = this;
					var pattern = /^\d+\.\d+\.\d+$/;
					var url = $("[name='file']").val();
					if(url) {
						that.m.downloadAddress = url;
					}
					if(!that.m.downloadAddress) {
						that.C.errorMsg("下载地址不能为空,请上传安装包!");
						return;
					}
					var message = $("#message").val();
					that.m.message = message;
					that.m.versionBrandPackage = this.brandIdList;					
					console.log(JSON.stringify(that.m));
					if(!this.isEdit){
						$.ajax({
							url: "version/createPackage",
							data: {
								version:JSON.stringify(that.m)
							},
							type: "post",
							dataType: "json",
							success: function() {
								that.cancel();
								that.tb.ajax.reload();
								that.C.simpleMsg("保存成功");
							},
							error: function() {
								that.C.errorMsg("保存失败");
							}
						})
					}else{
						$.ajax({
							url: "version/updatePackage",
							data: {
								version:JSON.stringify(that.m)
							},
							type: "post",
							dataType: "json",
							success: function() {
								that.cancel();
								that.tb.ajax.reload();
								that.C.simpleMsg("保存成功");
							},
							error: function() {
								that.C.errorMsg("保存失败");
							}
						})
					}										
				},
				uploadSuccess: function(url) {
					$("[name='file']").val(url).trigger("change");
					this.C.simpleMsg("上传成功");
				},
				uploadError: function(msg) {
					this.C.errorMsg(msg);
				},
				initEditor: function() {
//					Vue.nextTick(function() {
//						var editor = new wangEditor('message');
//						editor.config.uploadImgFileName = 'file';
//						editor.config.uploadImgUrl = 'upload/file';
//						editor.create();
//					});
				}
			}
		});
		//编辑
		function createContactBtn(rowData) {
			var button = $("<button class='btn btn-xs btn-primary'>编辑</button>");
			button.click(function(){
				vueObj.showEditView(rowData);												
			});
	     	return button;			         				
	 	}
	}());	
	
</script>