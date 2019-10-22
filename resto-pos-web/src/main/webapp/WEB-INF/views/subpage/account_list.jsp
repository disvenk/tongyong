<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
	<link rel="stylesheet" href="assets/css/keyboard.css"/>
</head>
<div id="account-list">
	<h2 class="text-center"><strong>账户充值</strong></h2><br/>
	<div class="row">
		<div class="col-md-12"> 
			<form class="form-inline" id="accountForm" @submit.prevent="queryAccountInfo">
				<div class="form-group" style="margin-right: 30px;"> 
					<label>手机号码：</label>
					<input type="text" class="form-control numkeyboard" id="telephone" name="telephone"
							placeholder="请输入用户手机号" required="required" />
				</div> 
				<button type="submit" class="btn btn-primary">查询</button>
			</form>
		</div>
	</div><br/><br/>
	<div class="tab-content">
		<div role="tabpanel" class="tab-pane active">
			<div class="panel panel-success">
				<div class="panel-heading text-center">
					<strong style="margin-right:100px;font-size:22px">账户查询</strong>
				</div>
				<div class="panel-body">
					<table class="table table-bordered">
						<thead>
							<tr>
								<th>ID</th>
								<th>微信昵称</th>
								<th>手机号码</th>
								<th>账户余额</th>
								<th>操作</th>
							</tr>
					</thead>
					<tbody>
						<tr v-if="accountInfo.id != null">
							<td>{{accountInfo.id}}</td>
							<td>{{accountInfo.nickname}}</td>
							<td>{{accountInfo.telephone}}</td>
							<td>{{accountInfo.remain}}</td>
							<td>
								<button type='button' @click="chargeAccount" class='btn btn-primary'>账户充值</button>
							</td>
						</tr>
						<tr v-else>
							<th colspan="5" class="text-center">暂时没有数据...</th>
						</tr>
					</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 选择充值赠送活动弹窗 -->
	<div class="modal" style="display:block" v-if="chargeSettingShow">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" @click="close"><span >&times;</span></button>
					<h4 class="modal-title text-center"><strong>账户充值</strong></h4>
				</div>
				<div class="modal-body">
					请选择充值赠送活动：<br/>
					<div class="radio" style="left:32%;line-height: 22px;font-size: 18px;font-family: 微软雅黑;margin-bottom: 18px;" v-for="chargeSetting in chargeSettings">
					  <label>
					    <input type="radio" v-if="$index == 0" name="chargeSettingId" 
							   checked="checked" :value="chargeSetting.id" 
							   v-model="chargeSettingId">
						<input type="radio" v-else name="chargeSettingId"
							   :value="chargeSetting.id"
							   v-model="chargeSettingId">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					    <b>{{chargeSetting.labelText}}</b>
					  </label>
					</div>
					<br/>
					<div align="center">
						<button type="button" @click="cancel" class="btn btn-default btn-sm">
							取&nbsp;&nbsp;消
						</button>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button type="button" @click="next" class="btn btn-primary btn-sm">
							下一步
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 账户充值弹窗 -->
	<div class="modal" style="display:block" v-if="chargeShow">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" @click="close"><span >&times;</span></button>
					<h4 class="modal-title text-center"><strong>账户充值</strong></h4>
				</div>
				<div class="modal-body" style="left:20%;">
					<form class="form-inline" id="customerChargeForm" @submit.prevent="customerCharge">
						<div class="form-group" style="margin-right: 30px;"> 
							<input type="text" class="form-control numkeyboard"
									placeholder="请输入操作手机号" id="opertionPhone" required="required" />
						</div> 
						<button type="button" id="sendCode" @click="sendCode" v-if="waitTime==60" class="btn btn-primary">获取验证码</button>
						<button type="button" disabled="disabled" class="btn btn-default" v-else>{{waitTime}}秒后重新获取</button>
						<br/><br/>
						<div class="form-group"> 
							<input type="text" class="form-control numkeyboard"
									placeholder="请输入验证码" id="smsCodeMsg" required="required" />
						</div>
						<br/><br/><br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;
						<button type="button" @click="back" class="btn btn-default btn-sm">
							上一步
						</button>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button type="submit" :disabled="disabled" class="btn btn-primary btn-sm">
							确&nbsp;&nbsp;认
						</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!-- 充值成功弹窗 -->
	<div class="modal" style="display:block;" v-if="successShow">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header" style="position: relative;left: 39%;border:initial;">
					<img src="assets/img/checked.jpg" style="width: 100px;height: 100px;">
				</div>
				<div>
					<h4 class="modal-title text-center"><strong>充值成功!</strong></h4>
				</div>
				<div style="position: relative;left: 32%;font-size: 20px;font-family: 微软雅黑;margin-top: 20px;">
					<span>手机号码</span>
					<span style="margin-left: 20px;">{{accountInfo.telephone}}</span>
				</div>
				<div style="position: relative;left: 32%;font-size: 20px;font-family: 微软雅黑;margin: 10px 0px;">
					<span>充值金额</span>
					<span style="margin-left: 20px;color: red;">￥{{chargeSuccessMoney}}</span>
				</div>
				<div style="position: relative;left: 32%;font-size: 20px;font-family: 微软雅黑;margin: 10px 0px;">
					<span>赠送金额</span>
					<span style="margin-left: 20px;color: red;">￥{{rewardSuccessMoney}}</span>
				</div>
				<div style="position: relative;left: 46%;font-size: 20px;font-family: 微软雅黑;margin: 10px 0px;">
					<button type="button" @click="successColse" class="btn btn-default btn-sm">
						关闭
					</button>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="assets/js/keyboard.js"></script>
<script type="text/javascript">
	
	//绑定 js 键盘
	$(".numkeyboard").ioskeyboard({
	    keyboardRadix:80,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
	    keyboardRadixMin:40,//键盘大小的最小值，默认为60，实际大小为564X198
	    keyboardRadixChange:true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时才较好支持Safari内核浏览器
	    clickeve:true,//是否绑定元素click事件
	    colorchange:false,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
	    colorchangeStep:1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
	    colorchangeMin:154//按键背影颜色的最小值，默认为RGB(154,154,154)
	});
	$("input").blur(function(){//隐藏 js键盘
		$("#keyboard_5xbogf8c").css({"display":"none"});
	});
	
    var printOrder = new Vue({
        el:"#account-list",
        data:{
        	accountInfo:{},
        	chargeSettings:[],
        	chargeSettingShow:false,
        	chargeShow:false,
        	successShow:false,
        	flg:true,
        	chargeSettingId:"",
        	chargeSuccessMoney:0,
        	rewardSuccessMoney:0,
        	operation:{},
        	waitTime:60,
            disabled:false
        },
        created:function(){
        	this.getChargeSettings();
        },
        watch: {
        	chargeShow: function (val, oldVal) {
        		if(val){
        			//绑定 js 键盘
        			$(".numkeyboard").ioskeyboard({
        			    keyboardRadix:80,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
        			    keyboardRadixMin:40,//键盘大小的最小值，默认为60，实际大小为564X198
        			    keyboardRadixChange:true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时才较好支持Safari内核浏览器
        			    clickeve:true,//是否绑定元素click事件
        			    colorchange:false,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
        			    colorchangeStep:1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
        			    colorchangeMin:154//按键背影颜色的最小值，默认为RGB(154,154,154)
        			});
        			$("input").blur(function(){//隐藏 js键盘
        				$("#keyboard_5xbogf8c").css({"display":"none"});
        			});
        		}
            }
          },	
        methods:{
        	queryAccountInfo:function(){
        		var reg = /^((13[\d])|(15[0-35-9])|(18[\d])|(145)|(147)|(17[0135678]))\d{8}$/;
        		var telephone = $("#telephone").val();
        		if (!reg.test(telephone)) {
        			toastr.error("手机号码格式不正确！");
        			toastr.clear();
    				$("#telephone").focus();
					return;
				}
        		this.queryCustomerAccount(telephone);
        	},
        	getChargeSettings:function(){
        		var that = this;
        		$.ajax({
        			url:"chargesetting/list_all",
        			method: 'post',
        			success:function(result){
        				if(result.success){
        					that.chargeSettings = result.data;
        				}else{
        					toastr.error(result.message);
        					toastr.clear();
        				}
        			},
        			error:function(){
        				toastr.error("加载充值赠送活动出错!");
        				toastr.clear();
        			}
        		});
        	},
        	chargeAccount:function(){
        		var chargeSettings = this.chargeSettings;
        		if(chargeSettings == null || chargeSettings.length == 0){
        			toastr.error("暂无充值赠送活动!");
    				toastr.clear();
    				return;
        		}
        		this.chargeSettingShow = true;
        	},
        	cancel:function(){
        		this.chargeSettingShow = false;
        	},
        	next:function(){
        		this.chargeSettingShow = false;
        		this.chargeShow = true;
               	this.operation.smsCodeMsg = "";
               	this.operation.operationPhone = "";
        		this.flg = false;
               	this.waitTime = 60;
                this.disabled = false;
        	},
        	back:function(){
        		this.chargeSettingShow = true;
        		this.chargeShow = false;
        	},
        	sendCode:function(){
        		var opertionPhone = $("#opertionPhone").val().trim();
        		var reg = /^((13[\d])|(15[0-35-9])|(18[\d])|(145)|(147)|(17[0135678]))\d{8}$/;
        		if(opertionPhone =="" || opertionPhone == null){
        			toastr.error("请输入操作手机号!");
    				toastr.clear();
    				$("#opertionPhone").focus();
    				return;
        		}else if (!reg.test(opertionPhone)) {
        			toastr.error("手机号码格式不正确！");
        			toastr.clear();
    				$("#opertionPhone").focus();
					return;
				}
        		$("#sendCode").attr("disabled","true");
        		var that = this;
        		$.ajax({
        			url:"customer/sendCodeMsg",
        			method: "post",
        			data:"opertionPhone="+opertionPhone,
        			success:function(result){
        				if(result.success){
        					$("#sendCode").attr("disabled","false");
        					that.operation.operationPhone = opertionPhone;
        					that.operation.smsCodeMsg = result.data;
        					that.flg = true;
        					that.initWaitTime();
        					toastr.success("验证码已发送,请注意查收!");
            				toastr.clear();
        				}else{
        					$("#sendCode").attr("disabled","false");
        					toastr.error(result.message);
            				toastr.clear();
        				}
        			},
        			error:function(){
        				$("#sendCode").attr("disabled","false");
        				toastr.error("发送验证码出错!");
        				toastr.clear();
        			}
        		});
        	},
        	customerCharge:function(){
        		var smsCodeMsg = $("#smsCodeMsg").val().trim();
        		var opertionPhone = $("#opertionPhone").val().trim();
        		var reg = /^((13[\d])|(15[0-35-9])|(18[\d])|(145)|(147)|(17[0135678]))\d{8}$/;
        		var CodeMsgReg = /^[0-9]{4}$/;
        		if (!reg.test(opertionPhone)) {
        			toastr.error("手机号码格式不正确！");
        			toastr.clear();
    				$("#opertionPhone").focus();
					return;
				}
        		if(!CodeMsgReg.test(smsCodeMsg)){
        			toastr.error("验证码格式错误!");
    				toastr.clear();
    				$("#smsCodeMsg").val("");
    				$("#smsCodeMsg").focus();
        			return;
        		}else if((smsCodeMsg != this.operation.smsCodeMsg) || (opertionPhone != this.operation.operationPhone)){
        			toastr.error("验证码错误,请重新输入!");
    				toastr.clear();
    				$("#smsCodeMsg").val("");
    				$("#smsCodeMsg").focus();
        			return;
        		}
        		this.disabled = true;
        		var that = this;
        		$.ajax({
        			url:"customer/customerCharge",
        			method: "post",
        			data:"operationPhone="+opertionPhone+"&customerPhone="+that.accountInfo.telephone+"&chargeSettingId="+that.chargeSettingId+"&customerId="+that.accountInfo.id+"&accountId="+that.accountInfo.accountId,
        			success:function(result){
        				if(result.success){
                            that.disabled = false;
        					that.chargeSuccessMoney = result.data.chargeMoney;
        					that.rewardSuccessMoney = result.data.rewardMoney;
        					that.operation.smsCodeMsg = "";
        					that.operation.operationPhone = "";
        					that.flg = false;
        					that.waitTime = 60;
        					that.chargeShow = false;
        					that.successShow = true;
        					that.queryCustomerAccount(that.accountInfo.telephone);
        				}else{
                            that.disabled = false;
        					that.operation.smsCodeMsg = "";
        					that.operation.operationPhone = "";
        					toastr.error(result.message);
            				toastr.clear();
        				}
        			},
        			error:function(){
                        that.disabled = false;
        				that.operation.smsCodeMsg = "";
        				that.operation.operationPhone = "";
        				toastr.error("账户充值出错!");
        				toastr.clear();
        			}
        		});
        	},
        	successColse:function(){
        		this.successShow = false;
        		this.flg = false;
        		this.waitTime = 60;
               	this.operation.smsCodeMsg = "";
               	this.operation.operationPhone = "";
        	},
        	close:function(){
        	    this.disabled = false;
           		this.chargeSettingShow = false;
               	this.chargeShow = false;
               	this.successShow = false;
               	this.flg = false;
               	this.waitTime = 60;
               	this.operation.smsCodeMsg = "";
               	this.operation.operationPhone = "";
        	},
        	queryCustomerAccount:function(telephone){
        		var that = this;
        		$.ajax({
        			url:"customer/queryCustomer",
        			method: 'post',
        			type: 'json',
        			data:"telephone="+telephone,
        			success:function(result){
        				if(result.success){
        					that.accountInfo = result.data;
        				}else{
        					toastr.error(result.message);
        					toastr.clear();
        				}
        			},
        			error:function(){
        				toastr.error("查询失败");
        				toastr.clear();
        			}
        		});
        	},
			initWaitTime : function(){
				var that = this;
				if(this.flg){
					this.waitTime--;
					setTimeout(function() {
						if(that.waitTime!=1){
							that.initWaitTime();	
						}else{
							that.waitTime = 60;
						}
		            },1000)
				}
			}
        }
    });
</script>