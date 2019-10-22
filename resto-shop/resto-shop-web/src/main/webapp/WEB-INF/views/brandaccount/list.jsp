<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<h3 style="text-align: center"><STRONG>账户管理</STRONG></h3>
	<div class="row">
		<div class="col-md-4">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div  style="line-height: 34px;font-size: 22px">
						账户余额
					</div>
				</div>
				<div class="panel-body">
					<h1 style="color: #ffc459">￥{{accountBalance}}</h1>
					<div style="text-align: right">
						<button type="button" class="btn btn-success" data-toggle="modal" data-target="#chargeModal"> 账户充值 </button>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-8">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div  style="line-height: 34px;font-size: 22px">
						今日记录
					</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-3">
							<h3 style="color: #ffc459">{{logInfo.registerCustomer}}</h3>
							<h3 style="color: #ffc459">注册用户</h3>
						</div>
						<div class="col-md-3">
							<h3 style="color: #ffc459">{{logInfo.smsSend}}</h3>
							<h3 style="color: #ffc459">发送短信</h3>
						</div>
						<div class="col-md-3">
							<h3 style="color: #ffc459">{{logInfo.orderNum}}</h3>
							<h3 style="color: #ffc459">订单数</h3>
						</div>
						<div class="col-md-3">
							<h3 style="color: #ffc459">￥{{logInfo.orderMoney}}</h3>
							<h3 style="color: #ffc459">订单额</h3>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div class="row">
						<div class="col-md-6" style="line-height: 34px;font-size: 22px">
							账户概要
						</div>
						<div class="col-md-6" style="text-align: right">
							<button class="btn btn-success" type="button" @click = "todayInfo(1)">今日</button>
							<button class="btn btn-success" type="button" @click="yesterDayInfo(2)">昨日</button>
							<button class="btn btn-success" type="button" @click="weekInfo(3)">本周</button>
							<button class="btn btn-success" type="button" @click="monthInfo(4)" >本月</button>
							<a class="btn btn-success ajaxify" href="brandaccountlog/list">查看详情</a>
						</div>
					</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-3">
							<h3 style="color: #ffc459">注册用户支出</h3>
							<h3 style="color: #ffc459">{{accountLog.registerCustomerOrder}}元</h3>
						</div>
						<div class="col-md-3">
							<h3 style="color: #ffc459"> 短信支出</h3>
							<h3 style="color: #ffc459">{{accountLog.smsOrder}}元</h3>
						</div>
						<div class="col-md-3">
							<h3 style="color: #ffc459"> 订单支出</h3>
							<h3 style="color: #ffc459">{{accountLog.orderMoney}}元</h3>
						</div>
						<div class="col-md-3">
							<h3 style="color: #ffc459">账户充值</h3>
							<h3 style="color: #ffc459">￥+{{accountLog.chargeMoney}}元</h3>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 充值 Modal -->
	<div class="modal fade" id="chargeModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">账号充值</h4>
				</div>
				<div class="modal-body">
					<form role="form" class="form-horizontal"
						  action="accountchargeorder/charge" method="post" target="_blank" @submit="showChargeModal('createChargeOrder')">
						<div class="form-body">

							<div class="form-group">
								<label class="col-sm-3 control-label">充值金额：</label>
								<div class="col-sm-4">
									<div class="input-group">
										<input type="number" class="form-control" name="chargeMoney"  v-model="changeMoney" min="1">
										<div class="input-group-addon"><span class="glyphicon glyphicon-yen"></span></div>
									</div>
								</div>
								<div class="col-sm-5 text-center">
									<a class="btn btn-info" @click="changeMoney = 500">500</a>&nbsp;
									<a class="btn btn-info" @click="changeMoney = 1000">1000</a>&nbsp;
									<a class="btn btn-info" @click="changeMoney = 2000">2000</a>
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-3 control-label">支付方式：</label>
								<div class="col-sm-8">
									<div class="md-radio-list">
										<div class="md-radio">
											<input type="radio" id="alipay" name="payType"
												   checked="checked" class="md-radiobtn" value="1"> <label
												for="alipay"> <span></span> <span class="check"></span>
											<span class="box"></span>&nbsp;<img alt="支付宝支付"
																				src="assets/pages/img/alipay.png" width="23px" height="23px">&nbsp;支付宝支付
										</label>
										</div>
										<div class="md-radio">
											<input type="radio" id="wxpay" name="payType"
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
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
							<input class="btn green" type="submit" value="充值"/>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>


<script>
    (function () {
        var vueObj = new Vue({
			el:"#control",
			data:{
                accountBalance:0,
				date:{
                  beginDate:'',
				  endDate:''
				},
				logInfo:{
                    registerCustomer:0,//注册用户数
					smsSend:0,//发短信数
					orderNum:0,//订单数
					orderMoney:0//订单总额
				},
				accountLog:{
                    registerCustomerOrder:0,//注册用户支出
					smsOrder:0, //短信支出
					orderMoney:0, //订单支出
					chargeMoney:0 //账户充值
				},
				changeMoney : 100
			},
			methods:{
                todayInfo:function(count){
					var that = this;
                    that.date.beginDate = new Date().format("yyyy-MM-dd");
                    that.date.endDate  = new Date().format("yyyy-MM-dd");
					that.querryAccountInfo(count);
				},
				yesterDayInfo:function (count) {
                    var that = this;
                    this.date.beginDate = that.GetDateStr(-1);
                    this.date.endDate  = that.GetDateStr(-1);
                    that.querryAccountInfo(count);
                },
				weekInfo:function (count) {
                    var that = this;
                    that.date.beginDate = that.getWeekStartDate();
                    that.date.endDate  = new Date().format("yyyy-MM-dd");
                    that.querryAccountInfo(count);
                },
				monthInfo:function (count) {
                    var that = this;
                    that.date.beginDate = that.getMonthStartDate();
                    that.date.endDate  = new Date().format("yyyy-MM-dd");
                    that.querryAccountInfo(count);
                },
                GetDateStr:function(AddDayCount){
                    var dd = new Date()
                    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
                    var y = dd.getFullYear();
                    var m = dd.getMonth()+1;//获取当前月份的日期
                    var d = dd.getDate();
                    return y+"-"+m+"-"+d;
				},
                getWeekStartDate:function () {
                    var now = new Date(); //当前日期
                    var nowYear = now.getYear(); //当前年
					var nowMonth = now.getMonth(); //当前月
					var nowDay = now.getDate(); //当前日
					var nowDayOfWeek = now.getDay(); //今天本周的第几天
                    var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);
                    return this.formatDate(weekStartDate);
                },
                getMonthStartDate:function () {
                    var now = new Date();//当前日期
					var nowYear = now.getYear(); //当前年
					var nowMonth = now.getMonth(); //当前月
                    var monthStartDate = new Date(nowYear, nowMonth, 1);
                    return this.formatDate(monthStartDate);
                },
                formatDate:function (date) {
                    var myyear = date.getFullYear();
                    var mymonth = date.getMonth()+1;
                    var myweekday = date.getDate();

                    if(mymonth < 10){
                        mymonth = "0" + mymonth;
                    }
                    if(myweekday < 10){
                        myweekday = "0" + myweekday;
                    }
                    return (myyear+"-"+mymonth + "-" + myweekday);
                },
                querryInfo:function () {
                    var that = this;
                    $.ajax({
                        url:"brandaccount/initData",
                        data:{
                            beginDate:that.date.beginDate,
                            endDate: that.date.endDate
                        },
                        success:function (result) {
                            var obj = result.data;
                            //初始化账户余额
                            that.accountBalance = obj.accountBalance;
                            //初始化今日记录
                            that.logInfo.registerCustomer = obj.registerCustoemrNum;
                            that.logInfo.smsSend = obj.smsNum;
                            that.logInfo.orderNum = obj.orderNum;
                            that.logInfo.orderMoney = obj.orderMoney;

                            //初始化账户概要
                            that.accountLog.registerCustomerOrder = obj.registerCustomerMoney;
                            that.accountLog.smsOrder = obj.smsMoney;
                            that.accountLog.orderMoney = obj.orderOutMoney;
                            that.accountLog.chargeMoney = obj.brandAccountCharge
                        }
                    });
                },
				querryAccountInfo:function (count) {
                    var that = this;
                    $.ajax({
                        url:"brandaccount/getAccountData",
                        data:{
                            beginDate:that.date.beginDate,
                            endDate: that.date.endDate
                        },
                        success:function (result) {
                            var obj = result.data;
                            //初始化账户概要
                            that.accountLog.registerCustomerOrder = obj.registerCustomerMoney;
                            that.accountLog.smsOrder = obj.smsMoney;
                            that.accountLog.orderMoney = obj.orderOutMoney;
                            that.accountLog.chargeMoney = obj.brandAccountCharge
							switch (count){
								case 1:
								    toastr.success("查询今日数据成功..")
									break;
                                case 2:
                                    toastr.success("查询昨日数据成功..")
                                    break;
                                case 3:
                                    toastr.success("查询本周数据成功..")
                                    break;
                                case 4:
                                    toastr.success("查询本月数据成功..")
                                    break;
							}

                        }
                    });
                }
			},
			created:function () {
                var that = this;
                that.date.beginDate = new Date().format("yyyy-MM-dd");
                that.date.endDate  = new Date().format("yyyy-MM-dd");
                that.querryInfo();
            }


		})



    }());

</script>
