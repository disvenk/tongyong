<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<style>
dt, dd {
	line-height: 35px;
}
</style>
<div id="control">
<div class="table-div">
	<div class="table-operator">
		<button type="button" class="btn green-meadow" disabled> 短信余额：{{brandInfo.remainderNum}} 条 </button>&nbsp;&nbsp;&nbsp;
		<s:hasPermission name="notice/add">
			<button type="button" class="btn green" data-toggle="modal"
				data-target="#createChargeOrder" id="btn_smsCharge">短信充值</button>
		</s:hasPermission>
	</div>
	<div class="clearfix"></div>
	<div class="table-filter"></div>
	<div class="table-body">
		<table class="table table-striped table-hover table-bordered"></table>
	</div>
</div>

<!-- 短信充值 -->
<div class="modal fade" id="createChargeOrder" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title text-center">
					<strong>短信充值</strong>
				</h4>
			</div>
			<div class="modal-body">
				<div>
				  <ul class="nav nav-tabs" role="tablist">
				    <li role="presentation" class="active"><a href="#onlinePay" aria-controls="home" role="tab" data-toggle="tab">第三方支付</a></li>
				    <li role="presentation"><a href="#bankPay" aria-controls="profile" role="tab" data-toggle="tab">银行转账</a></li>
				  </ul>
				  <div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="onlinePay">
				    	<form role="form" class="form-horizontal"
							action="smschargeorder/smsCharge" method="post" target="_blank" @submit="showChargeModal('createChargeOrder')">
							<div class="form-body">
								<div class="form-group">
									<label class="col-sm-3 control-label">充值品牌：</label>
									<div class="col-sm-8">
										<p class="form-control-static">{{brandInfo.brandName}}</p>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">短信单价：</label>
									<div class="col-sm-8">
										<p class="form-control-static">{{brandInfo.smsUnitPrice}}&nbsp;元</p>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">充值金额：</label>
									<div class="col-sm-4">
										<div class="input-group">
											<input type="number" class="form-control"
												placeholder="请输入要充值的金额" required name="chargeMoney" min="100" v-model="chargeMoney">
											<div class="input-group-addon"><span class="glyphicon glyphicon-yen"></span></div>
										</div>
									</div>
									<div class="col-sm-5 text-center">
										<a class="btn btn-info" @click="changeMoney(500)">500</a>&nbsp;
										<a class="btn btn-info" @click="changeMoney(1000)">1000</a>&nbsp;
										<a class="btn btn-info" @click="changeMoney(2000)">2000</a>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">短信条数：</label>
									<div class="col-sm-8">
										<div class="input-group">
											<p class="form-control-static">{{smsNumber}}&nbsp;条</p>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">支付方式：</label>
									<div class="col-sm-8">
										<div class="md-radio-list">
											<div class="md-radio">
												<input type="radio" id="alipay" name="paytype"
													checked="checked" class="md-radiobtn" value="1"> <label
													for="alipay"> <span></span> <span class="check"></span>
													<span class="box"></span>&nbsp;<img alt="支付宝支付"
													src="assets/pages/img/alipay.png" width="23px" height="23px">&nbsp;支付宝支付
												</label>
											</div>
											<div class="md-radio">
												<input type="radio" id="wxpay" name="paytype"
													class="md-radiobtn" value="2"> <label for="wxpay">
													<span></span> <span class="check"></span> <span class="box"></span>&nbsp;<img
													alt="微信支付" src="assets/pages/img/wxpay.png" width="23px"
													height="23px">&nbsp;微信支付
												</label>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="text-center">
								<a class="btn default" data-dismiss="modal">取消</a> <input
									class="btn green" type="submit" value="充值"/>
							</div>
						</form>
				    </div>
				    <div role="tabpanel" class="tab-pane" id="bankPay">
						<form  role="form" class="form-horizontal" action="smschargeorder/smsChargeByBank" @submit.prevent="saveBankOrder">
<!-- 						<h3 class="bg-grey text-center">请确保转账金额大于一百元</h3><br/> -->
						<div class="form-group">
							<label class="col-sm-3 control-label">短信单价：</label>
							<div class="col-sm-8">
								<p class="form-control-static">{{brandInfo.smsUnitPrice}}&nbsp;元</p>
							</div>
						</div>
						  <div class="form-group">
								<label class="col-md-3 control-label">转账流水号：</label>
								<div class="col-md-8">
									<input type="text" class="form-control" required name="serialNumber">
									<span class="help-block">银行卡转账需官方确认后，充值才会到账，耗时可能较长，静候佳音！</span>
								</div>
						</div>
						  	<div class="text-center">
								<a class="btn default" data-dismiss="modal">取消</a> <input
									class="btn green" type="submit" value="提交" />
							</div>
						</form>
				    </div>
				  </div>
				</div>
			</div>
		</div>
	</div>
</div>



<!--短信充值---end -->




<!-- 立即支付 -->
<div class="modal fade" id="payAgainModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title text-center">
					<strong>立即支付</strong>
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" class="form-horizontal"
					action="smschargeorder/payAgain" method="post" target="_blank" @submit="showChargeModal('payAgainModal')">
					<div class="form-body">
						<div class="form-group">
							<label class="col-sm-3 control-label">充值品牌：</label>
							<div class="col-sm-8">
								<p class="form-control-static">{{brandInfo.brandName}}</p>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">短信单价：</label>
							<div class="col-sm-8">
								<p class="form-control-static">{{orderInfo.smsUnitPrice}}&nbsp;元</p>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">充值金额：</label>
							<div class="col-sm-8">
								<p class="form-control-static">{{orderInfo.chargeMoney}}&nbsp;元</p>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">短信条数：</label>
							<div class="col-sm-8">
								<div class="input-group">
									<p class="form-control-static">{{orderInfo.number}}&nbsp;条</p>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">支付方式：</label>
							<div class="col-sm-8">
								<div class="md-radio-list">
									<div class="md-radio">
										<input type="radio" id="alipay_Again" name="paytype"
											checked="checked" class="md-radiobtn" value="1"> <label
											for="alipay_Again"> <span></span> <span class="check"></span>
											<span class="box"></span>&nbsp;<img alt="支付宝支付"
											src="assets/pages/img/alipay.png" width="23px" height="23px">&nbsp;支付宝支付
										</label>
									</div>
									<div class="md-radio">
										<input type="radio" id="wxpay_Again" name="paytype"
											class="md-radiobtn" value="2"> <label for="wxpay_Again">
											<span></span> <span class="check"></span> <span class="box"></span>&nbsp;<img
											alt="微信支付" src="assets/pages/img/wxpay.png" width="23px"
											height="23px">&nbsp;微信支付
										</label>
									</div>
								</div>
							</div>
						</div>
						<input type="hidden" name="chargeOrderId" v-model="orderInfo.id">
					</div>
					<div class="text-center">
						<a class="btn default" data-dismiss="modal">取消</a> <input
							class="btn green" type="submit" value="支付" />
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<!-- 订单详情 -->
<div class="modal fade" id="orderInfoModal" tabindex="-1" role="dialog" aria-labelledby="orderInfoModal">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title text-center"><strong>订单详情</strong></h4>
      </div>
      <div class="modal-body">
      	<dl class="dl-horizontal">
		  <dt>充值品牌:</dt>
		  <dd>{{ brandInfo.brandName }}</dd>
		  <dt>充值金额:</dt>
		  <dd>{{ orderInfo.chargeMoney?orderInfo.chargeMoney+'&nbsp;元':'未完成' }}</dd>
		  <dt>短信单价:</dt>
		  <dd>{{ orderInfo.smsUnitPrice }}&nbsp;元</dd>
		  <dt>充值数量:</dt>
		  <dd>{{ orderInfo.number?orderInfo.number+'&nbsp;条':'未完成' }}</dd>
		  <dt>支付类型:</dt>
		  <dd>{{{ orderInfo.payType }}}</dd>
		  <dt>创建时间:</dt>
		  <dd>{{ orderInfo.createTime }}</dd>
		  <dt>完成时间:</dt>
		  <dd>{{ orderInfo.pushOrderTime }}</dd>
		</dl>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-info btn-block" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>
<!-- 充值订单    点击充值后显示订单信息-->
<div class="modal fade" id="chargeInfoModal" tabindex="-1" role="dialog" aria-labelledby="chargeInfoModal" data-backdrop="static">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title text-center"><strong>充值详情</strong></h4>
      </div>
      <div class="modal-body">
      	<dl class="dl-horizontal">
		  <dt>充值品牌:</dt>
		  <dd>{{ brandInfo.brandName }}</dd> 
		  <dt>充值金额:</dt>
		  <dd>{{ chargeMoney }}&nbsp;元</dd>
		  <dt>短信单价:</dt>
		  <dd>{{ brandInfo.smsUnitPrice }}&nbsp;元</dd>
		  <dt>充值数量:</dt>
		  <dd>{{ smsNumber }}&nbsp;条</dd>
<!-- 		  <dt>创建时间:</dt> -->
<!-- 		  <dd>{{ orderInfo.createTime }}</dd> -->
		</dl>
		<p class="text-center text-danger"><strong>充值完成后，半小时内到账，请勿急躁！</strong></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn green btn-block" data-dismiss="modal" @click="refresh">充值完成</button>
      </div>
    </div>
  </div>
</div>
</div>
<script>
	var tb;
	(function() {
		var cid = "#control";
		var $table = $(".table-body>table");
		tb = $table.DataTable({
			bAutoWidth: false,//禁止自适应列宽
// 			aoColumnDefs: [
// 				{"sWidth": "220px", "aTargets": [ 6 ]},
// 				{"sWidth": "130px", "aTargets": [ 8 ]},
//             ],
			ajax : {
				url : "smschargeorder/list_all",
				dataSrc : "data"
			},
			columns : [
					{
						title : "品牌名称",
						data : "brandName",
					},
					{
						title : "充值金额（元）",
						data : "chargeMoney",
						defaultContent:"未完成"
					},
					{
						title : "充值条数",
						data : "number",
						defaultContent:"未完成"
					},
					{
						title : "短信单价（元）",
						data : "smsUnitPrice",
					},
					{
						title : "创建时间",
						data : "createTime",
						createdCell : function(td, tdData) {
							$(td).html(vueObj.formatDate(tdData));
						}
					},
					{
						title : "完成时间",
						data : "pushOrderTime",
						createdCell : function(td, tdData) {
							$(td).html(vueObj.formatDate(tdData));
						}
					},
					{
						title : "支付类型",
						data : "payType",
						createdCell : function(td, tdData) {
							var payType = "";
							if(tdData!=null){
								payType = vueObj.getPayType(tdData);
							}else{
								payType = "<img alt=\"未支付\" src=\"assets/pages/img/wait.png\" width=\"23px\" height=\"23px\">&nbsp;未 支 付";
							}
							$(td).html(payType);
						}
					},
					{
						title : "交易状态",
						data : "orderStatus",
						createdCell : function(td, tdData) {
							var str = "";
							if(tdData == 0){
								str = "<span class='label label-danger'>待 支 付</span>"
							}else if(tdData == 1){
								str = "<span class='label label-success'>已 完 成</span>"
							}else if(tdData == 3){
								str = "<span class='label label-info'>审 核 中</span>"
							}
							$(td).html(str);
						}
					},
					{
						title : "操作",
						data : "id",
						createdCell : function(td, tdData, rowData) {
							var info = [];
							if (rowData.orderStatus == 1 || rowData.orderStatus == 3) {//订单已完成
								var btn = createBtn(null, "查看详情", "btn-sm btn-primary", function() {
									vueObj.orderInfo = rowData;
									vueObj.orderInfo.payType = vueObj.getPayType(rowData.payType);
									vueObj.orderInfo.createTime = vueObj.formatDate(rowData.createTime)
									vueObj.orderInfo.pushOrderTime = vueObj.formatDate(rowData.pushOrderTime)
									$("#orderInfoModal").modal();
								})
								info.push(btn);
							} else if(rowData.orderStatus == 0){//订单未完成
								var btn = createBtn(null, "立即支付","btn-sm btn-success",function() {
											vueObj.orderInfo = rowData;
											$("#payAgainModal").modal();
										})
								info.push(btn);
							}
							if(rowData.orderStatus == 0){	//只有未支付的订单才可以删除
								var btn = createBtn(null,"删除订单","btn-sm red-sunglo",function() {
									C.confirmDialog("确定要删除么","提示",
											function() {
												var data = {"id":tdData};
												$.post("smschargeorder/deleteOrder",data,function(result){
													if(result){
														toastr.success("删除成功！");
													}else{
														toastr.error("删除失败！");
													}
													tb.ajax.reload();//刷新
												})
											});
								})
								info.push(btn);
							}
							$(td).html(info);
						}
					} ],
		});
	}());

	var C = new Controller("#control", tb);

	var vueObj = new Vue({
		el : "#control",
		data : {
			brandInfo:{},
			chargeMoney:100,
			smsNumber:0,
			orderInfo:{}
		},
		methods : {
			getbrandInfo : function(){
				var that = this;
				$.post("smsacount/selectSmsAcount", function(result) {
					that.brandInfo = result.data;
					that.smsNumber = Math.round(that.chargeMoney / that.brandInfo.smsUnitPrice);//四舍五入
				})
			},
			getPayType : function(type){
				var str = ""
				if(type==1){
					str = "<img alt=\"支付宝支付\" src=\"assets/pages/img/alipay.png\" width=\"23px\" height=\"23px\">&nbsp;支 付 宝";
				}else if(type==2){
					str = "<img alt=\"微信支付\" src=\"assets/pages/img/wxpay.png\" width=\"23px\" height=\"23px\">&nbsp;微&nbsp;&nbsp;信";
				}else if(type==3){
					str = "<img alt=\"银行转账\" src=\"assets/pages/img/bank.png\" width=\"23px\" height=\"18px\">&nbsp;银行转账";
				}else{
					str = type;
				}
				return str;
			},
			formatDate : function(date){
				var temp = "未完成";
				if (date != null && date != "") {
					temp = new Date(date);
					temp = temp.format("yyyy-MM-dd hh:mm:ss");
				}
				return temp;
			},
			showChargeModal : function(modalId){
				$("#"+modalId).modal("hide");
				$("#chargeInfoModal").modal();
			},
			saveBankOrder : function(e){
				var that = this;
				var formDom = e.target;
				C.ajaxFormEx(formDom,function(){
					$("#createChargeOrder").modal("hide");
					tb.ajax.reload();
				});
			},
			changeMoney : function(money){
				this.chargeMoney = money; 
			},
			refresh : function(){
				this.getbrandInfo();
				tb.ajax.reload();
			}
		},
		created : function(){
			this.getbrandInfo();
		},
		watch : {
			chargeMoney : function(){
				this.smsNumber = Math.round(this.chargeMoney / this.brandInfo.smsUnitPrice);
			}
		}
	});

	//格式化时间
	function formatDate(date) {
		var temp = "";
		if (date != null && date != "") {
			temp = new Date(date);
			temp = temp.format("yyyy-MM-dd hh:mm:ss");
		}
		return temp;
	}

	//创建一个按钮
	function createBtn(btnName, btnValue, btnClass, btnfunction) {
		return $('<input />', {
			name : btnName,
			value : btnValue,
			type : "button",
			class : "btn " + btnClass,
			click : btnfunction
		})
	}
</script>
