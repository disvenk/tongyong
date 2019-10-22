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
					<form class="form-horizontal" role="form" id="tForm" @submit.prevent="save">
				  		<div class="form-body">
							<div class="form-group">
								<label class="col-sm-3 control-label">选择品牌：</label>
								<div class="col-sm-5">
									<select name="brandId" id="brandId" class="form-control" v-model="brandId">
										<option v-bind:value="brand.id" v-for="brand in brandList">
											{{ brand.brandName }}
										</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">选择店铺：</label>
								<div class="col-sm-5">
									<select name="shopId" id="shopId" class="form-control">
										<option v-bind:value="shop.id" v-for="shop in shopList">
											{{shop.name}}
										</option>
									</select>
								</div>
							</div>
				  			<div class="form-group">
							  <label class="col-sm-3 control-label">当前token：</label>
							  <div class="col-sm-8">
							    <input type="text" class="form-control" name="accessToken" v-model="m.accessToken">
							  </div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">刷新token：</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" name="refreshToken" v-model="m.refreshToken">
								</div>
							</div>
							<div class="form-group text-center">
								<input type="hidden" name="id" v-model="m.id" />
								<input class="btn green" type="submit" value="保存" />
								<a class="btn default" @click="cancel">取消</a>
								<div id="createFail" class="text-danger"></div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="elemetoken/add">
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
		var brandList;
		$.post("elemetoken/brandList", function (result) {
			brandList = result.data;
			var cid="#control";
			var $table = $(".table-body>table");
			var tb = $table.DataTable({
				ajax : {
					url : "elemetoken/list_all",
					dataSrc : ""
				},
				columns : [
					{
						title : "品牌名称",
						data : "brandName",
					},
					{
						title : "店铺名称",
						data : "shopName",
					},
					{
						title : "当前token",
						data : "accessToken",
					},
					{
						title : "更新时间",
						data : "updateTime",
						createdCell:function(td,tdData,rowData,row){
							//格式化时间
							$(td).html((new Date(tdData)).format("yyyy-MM-dd hh:mm:ss"));
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
					shopList: null,
					brandList: null,
					brandId:null,
				},
				created: function () {
					this.brandList = brandList;
				},
				watch : {
					"brandId" : function(val, oldVal) {
						this.getShopList(val);
					}
				},
				methods:{
					getShopList: function(brandId){
						var that = this;
						$.post("elemetoken/shopList",{brandId:brandId}, function (result) {
							that.shopList = result.data;
							console.log(JSON.stringify(this.shopList));
						});
					},
					openForm:function(){
						this.showform = true;
					},
					closeForm:function(){
						this.m={};
						this.showform = false;
					},
					cancel:function(){
						this.closeForm();
					},
					create:function(){
						this.m={};
						this.openForm();
					},
					save:function(e){
						var data = $("#tForm").serialize();
						var that = this;
						$.post("elemetoken/create", data, function (data) {
							if (data.success) {
								that.closeForm();
								tb.ajax.reload();
							}else{
								C.errorMsg(data.message);
							}
						});
					},
				},
			};
			var vueObj = C.vueObj(option);
		});
	}());

</script>
