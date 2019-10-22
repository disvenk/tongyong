<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>

<div id="control">
	<!-- 确定充值模态框开始 -->
	<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog"
		aria-labelledby="confirmModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title text-center">
						<strong>短信订单充值确认</strong>
					</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" action="smscharge/pay_success"
						@submit.prevent="save" id="showForm">
						<div class="form-group">
							<label class="col-sm-3 control-label">充值的流水号:</label>
							<div class="input-group col-sm-8">
								<input type="text" class="form-control" id="recipient-name"
									v-model="smsChargeOrder.tradeNo" disabled>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">充值的金额:</label>
							<div class="input-group col-sm-8">
								<input type="number" name="chargeMoney" class="form-control"
									v-model="smsChargeOrder.chargeMoney" required>
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-jpy"></span>
								</div>
							</div>
						</div>
						<div class="text-center">
							<input type="hidden" name="id" v-model="smsChargeOrder.id" />
							<button type="button" class="btn btn-default"
								data-dismiss="modal">关闭</button>
							<button type="submit" class="btn btn-primary">确认</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!-- 确定充值模态框结束 -->


	<!-- 表格   start-->
	<div class="table-div">
		<div class="clearfix"></div>
		<div class="table-filter">&nbsp;</div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered "></table>
		</div>
	</div>
	<!-- 表格   end-->
</div>


<script>
	$(document)
			.ready(
					function() {
						//载入 表格数据
						tb = $('.table-body>table')
								.DataTable(
										{
											"order" : [ [ 7, "desc" ] ],
											ajax : {
												url : "smscharge/list_all",
												dataSrc : ""
											},
											columns : [
													{
														title : "品牌名称",
														data : "brandName",
													},
													{
														title : "充值金额（元）",
														data : "chargeMoney",
													},
													// 						{
													// 							title : "订单流水号",
													// 							data : "tradeNo",
													// 						},
													{
														title : "充值数目(条)",
														data : "number",
													},
													{
														title : "短信单价（元）",
														data : "smsUnitPrice",
													},
													{
														title : "创建时间",
														data : "createTime",
														createdCell : function(
																td, tdData) {
															$(td)
																	.html(
																			formatDate(tdData));
														}
													},
													{
														title : "完成时间",
														data : "pushOrderTime",
														createdCell : function(
																td, tdData) {
															$(td)
																	.html(
																			tdData != null ? formatDate(tdData)
																					: "未完成");
														}
													},
													{
														title : "支付类型",
														data : "payType",
														createdCell : function(
																td, tdData) {
															var payType = "";
															if (tdData != null) {
																payType = vueObj
																		.getPayType(tdData);
															} else {
																payType = "<img alt=\"未支付\" src=\"assets/pages/img/wait.png\" width=\"23px\" height=\"23px\">&nbsp;未 支 付";
															}
															$(td).html(payType);

														}
													},
													{
														title : "交易状态",
														data : "orderStatus",
														createdCell : function(
																td, tdData) {
															var str = "";
															if (tdData == 0) {
																str = "<span class='label label-danger'>待 支 付</span>"
															} else if (tdData == 1) {
																str = "<span class='label label-success'>已 完 成</span>"
															} else if (tdData == 3) {
																str = "<span class='label label-info'>待处理</span>"
															}
															$(td).html(str);
														}
													},
													{
														title : "操作",
														data : "id",
														createdCell : function(
																td, tdData,
																rowData) {
															var info = [];
															if (rowData.orderStatus == 1) {//订单已完成
																var btn = createBtn(
																		null,
																		"订单已完成",
																		"btn-sm btn-primary disabled",
																		function() {
																		})
																info.push(btn);
															} else {//订单未完成
																var btn = createBtn(
																		null,
																		"充值",
																		"btn-sm btn-success",
																		function() {
																			vueObj.smsChargeOrder = rowData;
																			$(
																					"#confirmModal")
																					.modal();
																		})
																<s:hasPermission name='smscharge/update'>
																info.push(btn);
																</s:hasPermission>
															}
															$(td).html(info);
														}
													} ],
										});

						//创建一个按钮
						function createBtn(btnName, btnValue, btnClass,
								btnfunction) {
							return $('<input />', {
								name : btnName,
								value : btnValue,
								type : "button",
								class : "btn " + btnClass,
								click : btnfunction
							})
						}

						//格式化时间
						function formatDate(date) {
							var temp = "";
							if (date != null && date != "") {
								temp = new Date(date);
								temp = temp.format("yyyy-MM-dd hh:mm:ss");
							}
							return temp;
						}

						var C = new Controller("#control", tb);

						var vueObj = new Vue(
								{
									el : "#control",
									data : {
										smsChargeOrder : {},
									},
									methods : {
										save : function(e) {
											var that = this;
											var formDom = e.target;

											$("#confirmModal").modal('hide');
											C
													.confirmDialog(
															"确认订单后,短信就会到账！请确认充值的金额和订单的金额是否匹配！",
															"确认订单",
															function() {
																C
																		.ajaxFormEx(
																				formDom,
																				function() {
																					$(
																							"#confirmModal")
																							.modal(
																									'hide');
																					tb.ajax
																							.reload();
																				});
															})

											// 						var id = this.smsChargeOrder.id;
											// 						var chargeMoney = this.smsChargeOrder.chargeMoney;
											// 						var brandId = this.smsChargeOrder.brandId;
											// 						console.log(chargeMoney);
											// 						console.log(id);
											// 						console.log(brandId);

											// 						//关闭模态框并弹窗
											// 						$("#confirmModal").modal('hide');
											// 						C.confirmDialog("确认订单后,短信就会到账！请确认充值的金额和订单的金额是否匹配！","确认订单",function(){
											// 								$.post("smscharge/pay_success",{id:id,chargeMoney:chargeMoney,brandId:brandId},function(result){
											// 									if(result.success){
											// 											tb.ajax.reload();
											// 									}else{
											// 										toastr.error("系统异常");
											// 									}
											// 								});
											// 							})

										},

										getPayType : function(type) {
											var str = ""
											if (type == 1) {
												str = "<img alt=\"支付宝支付\" src=\"assets/pages/img/alipay.png\" width=\"23px\" height=\"23px\">&nbsp;支 付 宝";
											} else if (type == 2) {
												str = "<img alt=\"微信支付\" src=\"assets/pages/img/wxpay.png\" width=\"23px\" height=\"23px\">&nbsp;微&nbsp;&nbsp;信";
											} else if (type == 3) {
												str = "<img alt=\"银行转账\" src=\"assets/pages/img/bank.png\" width=\"23px\" height=\"18px\">&nbsp;银行转账";
											}
											return str;
										}

									}
								});

					});

	
</script>