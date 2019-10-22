<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="v-bind" uri="http://www.springframework.org/tags/form" %>
<style>
	li{list-style:none}
</style>
<div id="control">
	<div class="row">
		<form class="form-horizontal" action="userdata/queryUserData" id="formInfo" @submit.prevent="query">
			<div class="col-md-offset-1 col-md-3">
				<div class="form-group">
					<label for="inputEmail3" class="col-sm-4 control-label"><strong>选择品牌：</strong></label>
					<div class="col-sm-8">
						<input type="hidden" name="brandId" :value="brandId">
						<input type="text" class="form-control"
							   placeholder="请输入品牌名检索" v-model="inputName" required="required"/>
						<%--<select class="form-control" v-model="brandId" name="brandId"--%>
							<%--required="required">--%>
							<%--<option v-for="brand in brandList" v-bind:value="brand.id">--%>
								<%--{{ brand.brandName }}</option>--%>
						<%--</select>--%>
						<ul v-show="flg">
							<li v-for="brand in brandList | filterBy inputName in 'brandName'"><a @click="selectBrand(brand)">{{brand.brandName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="col-md-3">
				<div class="form-group">
					<label for="inputEmail3" class="col-sm-4 control-label"><strong>选择店铺：</strong></label>
					<div class="col-sm-8">
						<select class="form-control" v-model="shopId" name="shopId"
							required="required">
							<option v-for="shop in shopList" value="{{shop.id}}">
								{{ shop.name }}</option>
						</select>
					</div>
				</div>
			</div>
			<div class="col-md-3">
				<div class="form-group">
					<label for="inputEmail3" class="col-sm-4 control-label"><strong>手机号码：</strong></label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="telephone"
							placeholder="请输入手机号" required="required" />
					</div>
				</div>
			</div>
			<div class="col-md-1">
				<button type="submit" class="btn btn-primary btn-block">查询</button>
			</div>
		</form>
	</div>

	<div class="row">
		<div class="col-md-offset-2 col-md-8">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>ID</th>
						<th>微信昵称</th>
						<!-- 						<th>用户头像</th> -->
						<th>手机号码</th>
						<th>账户余额</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<tr v-if="userInfo.customerId">
						<th width="15%">{{userInfo.customerId}}</th>
						<td>{{userInfo.nickname}}</td>
						<!-- 						<td><img v-bind:src = userInfo.head_photo class="img-rounded" width="80px" height="80px"></td> -->
						<td>{{userInfo.telephone}}</td>
						<td>{{userInfo.remain}}</td>
						<td class="text-center">
							<button type="button" class="btn btn-primary" data-toggle="modal" data-target=".updateInfoModal">编辑</button>
							&nbsp;&nbsp;&nbsp;
							<button class="btn btn-danger" @click="deleteInfo">删除</button>
						</td>
					</tr>
					<tr v-else>
						<td colspan="5" class="text-center">暂时没有数据...</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<div class="row">
		<div class="bs-example col-md-6">
			<div class="panel panel-primary">
				<div class="panel-heading"  align="center">
					<strong>
						充值记录
					</strong>
				</div>
				<table class="table table-bordered" id="chargeOrderTable"></table>
			</div>
		</div>

		<div class="bs-example col-md-6">
			<div class="panel panel-success">
				<div class="panel-heading" align="center">
					<strong>
						余额使用记录
					</strong>
				</div>
				<table class="table table-bordered" id="accountLogTable"></table>
			</div>
		</div>

		<div class="bs-example col-md-12">
			<div class="panel panel-info">
				<div class="panel-heading" align="center">
					<strong>
						订单记录
					</strong>
				</div>
				<table class="table table-bordered" id="orderTable"></table>
			</div>
		</div>
	</div>

	<!-- 修改模态框 -->
	<div class="modal fade updateInfoModal" role="dialog"
		aria-labelledby="gridSystemModalLabel" id="updateInfoModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title text-center">
						<strong>修改信息</strong>
					</h4>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<form class="form-horizontal" id="updataForm" @submit.prevent="updateInfo">
							<input type="hidden" name="accountId" v-model="userInfo.accountId"/>
							<input type="hidden" name="brandId" v-model="brandId">
							<div class="form-group">
								<label class="col-sm-3 control-label">用户昵称：</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" v-model="userInfo.nickname" readonly="readonly">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">手机号码：</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" name="telephone" v-model="userInfo.telephone" >
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">账户余额：</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" name="remain" v-model="userInfo.remain">
								</div>
							</div>
							<div class="form-group" v-if="optionBrandId!=brandId">
								<label class="col-sm-3 control-label">迷之口令：</label>
								<div class="col-sm-7">
									<input type="password" class="form-control" name="pwd" v-model="userInfo.pwd" required="required">
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-5">
									<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
								</div>
								<div class="col-sm-5">
									<button type="submit" class="btn btn-primary">保存</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
</div>

<script>
	(function() {
		new Vue({
			el : '#control',
			data : {
				brandList : [],
				shopList : [],
				brandId : "31946c940e194311b117e3fff5327215",//默认选中  测试品牌
				shopId : "",
				userInfo : {},
				chargeOrderTable : {},
				accountLogTable : {},
				orderTable : {},
				optionBrandId:"31946c940e194311b117e3fff5327215",//可操作的brandId
                inputName : "测试专用品牌",
				flg : false, //用来标识是否显示模糊查询,
                lastSelectName : ""
			},
			created : function() {
 				this.getBrandList();
				this.getShopList(this.brandId);
				this.initDataTables();
			},
			watch : {
				"brandId" : function(val, oldVal) {
					this.getShopList(val);
				},
				"inputName" : function (val) {
				    if (val != this.lastSelectName) {
                        this.flg = true;
                    }
                    if (!val){
                        this.flg = false;
					}
                }
			},
			methods : {
				initDataTables:function () {
				    var that = this;
					this.chargeOrderTable = $("#chargeOrderTable").DataTable({
                        order: [[ 3, "desc" ]],
						columns : [
							{
								title : "充值金额",
								data : "chargeMoney"
							},
							{
								title : "赠送金额",
								data : "rewardMoney"
							},
							{
								title : "充值类型",
								data : "chargeType",
								orderable : false
							},
							{
								title : "完成时间",
								data : "finishTime"
							}
						]
					});
					this.accountLogTable = $("#accountLogTable").DataTable({
                        order: [[ 3, "desc" ]],
						columns : [
							{
								title : "使用金额",
								data : "money"
							},
							{
								title : "余额",
								data : "remain"
							},
							{
								title : "使用说明",
								data : "remark",
								orderable : false
							},
							{
								title : "使用时间",
								data : "createTime"
							}
						]
					});
					this.orderTable = $("#orderTable").DataTable({
                        order: [[ 8, "desc" ]],
								columns : [
									{
										title : "流水号",
										data : "serialNumber"
									},{
                                        title : "品牌Id",
                                        data : "brandId",
                                        visible : false
                                    },
									{
										title : "桌号",
										data : "tableNumber"
									},
									{
										title : "用餐人数",
										data : "customerCount"
									},
									{
										title : "餐盒数",
										data : "mealAllNumber",
									},
									{
										title : "订单状态",
										data : "orderState",
										orderable : false
									},
									{
										title : "生产状态",
										data : "productionStatus",
										orderable : false
									},
									{
										title : "订单金额",
										data : "orderMoney"
									},
									{
										title : "下单时间",
										data : "pushOrderTime"
									},
									{
										title:"打印时间" ,
										data:"printOrderTime"
									},
									{
										title:"叫号时间" ,
										data:"callNumberTime"
									},
									{
										title: "操作",
										data: "id",
										orderable : false,
										createdCell: function (td, tdData, rowData) {
										    var brandId = rowData.brandId;
											$(td).html("<a href='userdata/initOrderInfo?orderId="+tdData+"&brandId="+brandId+"' class='btn green ajaxify'>查看详情</a>");
										}
									}
								]
					});
				},
				getBrandList : function() {
					var that = this;
					$.post("userdata/queryBrands", function(result) {
						if (result.success) {
							that.brandList = result.data;
							//that.brandId = result.data[0].id;
						} else {
							that.errorMsg(result.message);
						}
					});
				},
				getShopList : function(brandId) {
					var that = this;
					$.post("userdata/queryShops", {
						brandId : brandId
					}, function(result) {
						if (result.success) {
							that.shopList = result.data;
							that.shopId = result.data[0].id;
						} else {
							that.errorMsg(result.message);
						}
					})
				},
				query : function() {
					var that = this;
					that.userInfo = {};
					that.chargeOrderTable.clear().draw();
					that.accountLogTable.clear().draw();
					that.orderTable.clear().draw();
					this.basePost("userdata/queryUserData", $("#formInfo").serialize(), function(data) {
						if (!data.userInfo.customerId) {
							that.errorMsg("该手机号码没有注册！");
						}
						that.userInfo = data.userInfo;
						that.chargeOrderTable.rows.add(data.chargeOrderList).draw();
						that.accountLogTable.rows.add(data.accountLogList).draw();
						that.orderTable.rows.add(data.orderList).draw();
					});
				},
				updateInfo : function() {
					var that = this;
					this.basePost("userdata/updateUserData",$("#updataForm").serialize(),function(data){
						$("#updateInfoModal").modal("hide");
						that.userInfo.pwd = "";
						that.successMsg("操作成功!");
					});
				},
				deleteInfo : function() {
					var that = this;
					var flag = confirm("确定要删除么？");
					if(flag){
						var data = {"brandId":this.brandId,"customerId":this.userInfo.customerId,"accountId":this.userInfo.accountId};
						this.basePost("userdata/deleteUserData",data,function(data){
							that.userInfo = {};
							that.chargeOrderTable.clear().draw();
							that.accountLogTable.clear().draw();
							that.orderTable.clear().draw();
							that.successMsg("操作成功!");
						})
					}
				},
				basePost : function(url, params, callback) {
					var that = this;
					$.post(url, params, function(result) {
						if (result.success) {
							callback(result.data);
						} else {
							that.errorMsg(result.message);
						}
					})
				},
				successMsg : function(message) {
					toastr.clear();
					toastr.success(message);
				},
				errorMsg : function(message) {
					toastr.clear();
					toastr.error(message);
				},
				selectBrand : function (brand) {
					this.inputName = brand.brandName;
                    this.lastSelectName = brand.brandName;
					this.flg = false;
					this.brandId = brand.id;
                }
			}
		})
	}());

	
</script>