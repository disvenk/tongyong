<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<style>
	.formBox{
		color: #5babe6;
	}
	i {
		font-style: normal;
	}
	.form-tab {
		padding: 12px 20px;
		font-size: 16px;
		border-bottom: 1px solid #eef1f5;
	}
	.form-tab span {
		margin-right: 20px;
		cursor: pointer;
	}
	.form-tab .active {
		color: #5babe6;
		padding-bottom: 12px;
		border-bottom: 3px solid #5babe6;
	}
	.switchRadio {
		padding: 6px 12px;
		height: 34px;
		display: inline-block;
		border: 1px solid #c2cad8;
		margin-left: 5px;
		cursor: pointer;
	}
	.switchRadio.active {
		border: 1px solid #5babe6;
		color: #5babe6;
	}
	.checkbox-inline+.checkbox-inline, .radio-inline+.radio-inline {
		margin-left: 0;
	}
	.checkbox-inline, .radio-inline {
		padding-left: 5px;
	}
	.col-md-2.control-label {
		width: 250px;
	}
	.btn {
		padding: 8px 18px;
		border-radius: 5px;
	}
	.mainContent {
		padding: 15px 0;
		max-height: 65vh;
		overflow-y: scroll;
		overflow-x: hidden;
	}

</style>
<div id="control" class="row">
	<div class="col-md-12">
		<div class="portlet light bordered" style="padding: 0;">
			<div class="portlet-title text-center" style="min-height: initial;padding: 10px 20px;margin: 0;">
				<div class="caption">
					<span class="caption-subject bold font-blue-hoki">
						<i style="font-size: 12px;color: #989898;">店铺设置</i><i style="padding: 5px;">/</i><strong style="color: #333;">店铺参数设置</strong>
					</span>
				</div>
			</div>

			<div class="portlet-body" style="font-size: 15px;padding-top: 0;">
				<form role="form" class="form-horizontal" action="{{'shopDetailManage/modify'}}" @submit.prevent="save">
					<!--设置切换-->
					<div class="form-tab">
						<span :class="{active:currentSettingType == f.type}" v-for="f in settingList" @click="changeSetting(f)">{{f.name}}</span>
					</div>
					<!--基础设置-->
					<div class="mainContent" v-show="currentSettingType == 1">
						<div class="form-group">
							<label class="col-md-2 control-label">店铺名称</label>
							<div class="col-md-4" style="width: 300px;">
								<input type="text" class="form-control" name="name" :value="m.name" placeholder="必填" required="required">
							</div>
						</div>
						<div class="form-group" v-if="m.shopMode == 6">
							<label class="col-md-2 control-label">店铺模式</label>
							<div class="col-md-4">
								<span class="switchRadio" data-name="allowFirstPay" :class="{active:m.allowFirstPay == 0}" @click="changeFirstPay" style="margin-left: 0;">先付款</span>
								<span class="switchRadio" data-name="allowAfterPay" :class="{active:m.allowAfterPay == 0}" @click="changeAfterPay">后付款</span>
								<input type="hidden" :value="m.allowFirstPay" name="allowFirstPay" />
								<input type="hidden" :value="m.allowAfterPay" name="allowAfterPay" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-2 control-label">粉丝价名称</label>
							<div class="col-md-4" style="width: 300px;">
								<input type="text" class="form-control" name="fansName" :value="m.fansName" placeholder="请输入粉丝价名称">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">取餐电视IP</label>
							<div class="col-md-4" style="width: 300px;">
								<input type="text" class="form-control" name="tvIp" :value="m.tvIp">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">等位电视IP</label>
							<div class="col-md-4" style="width: 300px;">
								<input type="text" class="form-control" name="waitIp" :value="m.waitIp">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">微信端支付项</label>
							<!--微信支付项-->
							<div>
								<label class="checkbox-inline">
									<input type="checkbox" checked="checked" disabled="disabled">微信支付
								</label>
								<label class="checkbox-inline" v-show="b.aliPay == 1">
									<input type="checkbox" id="aliPay" @change="weChatPaySetting('aliPay')" v-model="m.aliPay" value="1">
									<input type="hidden" name="aliPay" v-model="m.aliPay">支付宝支付
								</label>
								<label class="checkbox-inline" v-show="b.openUnionPay == 1 && m.shopMode == 6">
									<input type="checkbox" id="openUnionPay" @change="weChatPaySetting('openUnionPay')" v-model="m.openUnionPay" value="1">
									<input type="hidden" name="openUnionPay" v-model="m.openUnionPay">银联支付
								</label>
								<label class="checkbox-inline" v-show="b.openMoneyPay == 1 && m.shopMode == 6">
									<input type="checkbox" id="openMoneyPay" @change="weChatPaySetting('openMoneyPay')" v-model="m.openMoneyPay" value="1">
									<input type="hidden" name="openMoneyPay" v-model="m.openMoneyPay">现金支付
								</label>
								<label class="checkbox-inline" :class="{ formBox : m.openShanhuiPay == 1}" v-show="b.openShanhuiPay == 1 && m.shopMode == 6">
									<input type="checkbox" id="openShanhuiPay" @change="weChatPaySetting('openShanhuiPay')" v-model="m.openShanhuiPay" value="1">
									<input type="hidden" name="openShanhuiPay" v-model="m.openShanhuiPay">美团新美大支付
								</label>
								<label class="checkbox-inline" v-show="b.integralPay == 1 && m.shopMode == 6">
									<input type="checkbox" id="integralPay" @change="weChatPaySetting('integralPay')" v-model="m.integralPay" value="1">
									<input type="hidden" name="integralPay" v-model="m.integralPay">会员支付
								</label>
								<div class="form-group" style="padding-top: 20px;" v-if="b.openShanhuiPay == 1 && m.openShanhuiPay==1">
									<label class="col-md-2 control-label" :class="{ formBox : m.openShanhuiPay == 1}">大众点评店铺ID：</label>
									<div  class="col-md-4" style="width: 300px;">
										<input type="text" class="form-control" name="dazhongShopId" v-model="m.dazhongShopId" required="required">
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-2 control-label">POS端支付项</label>
							<!--POS支付项-->
							<div>
								<label class="checkbox-inline">
									<input type="checkbox" id="openPosWeChatPay" @change="posPaySetting('openPosWeChatPay')" v-model="m.openPosWeChatPay" value="1">
									<input type="hidden" name="openPosWeChatPay" v-model="m.openPosWeChatPay">微信支付
								</label>
								<label class="checkbox-inline" v-show="b.aliPay == 1">
									<input type="checkbox" id="openPosAliPay" @change="posPaySetting('openPosAliPay')" v-model="m.openPosAliPay" value="1">
									<input type="hidden" name="openPosAliPay" v-model="m.openPosAliPay">支付宝支付
								</label>
								<label class="checkbox-inline" v-show="b.openUnionPay == 1">
									<input type="checkbox" id="openPosUnionPay" @change="posPaySetting('openPosUnionPay')" v-model="m.openPosUnionPay" value="1">
									<input type="hidden" name="openPosUnionPay" v-model="m.openPosUnionPay">银联支付
								</label>
								<label class="checkbox-inline" v-show="b.openMoneyPay == 1">
									<input type="checkbox" id="openPosMoneyPay" @change="posPaySetting('openPosMoneyPay')" v-model="m.openPosMoneyPay" value="1">
									<input type="hidden" name="openPosMoneyPay" v-model="m.openPosMoneyPay">现金支付
								</label>
								<label class="checkbox-inline" v-show="b.openShanhuiPay == 1">
									<input type="checkbox" id="openPosShanhuiPay" @change="posPaySetting('openPosShanhuiPay')" v-model="m.openPosShanhuiPay" value="1">
									<input type="hidden" name="openPosShanhuiPay" v-model="m.openPosShanhuiPay">美团新美大支付
								</label>
								<label class="checkbox-inline" v-show="b.integralPay == 1">
									<input type="checkbox" id="openPosIntegralPay" @change="posPaySetting('openPosIntegralPay')" v-model="m.openPosIntegralPay" value="1">
									<input type="hidden" name="openPosIntegralPay" v-model="m.openPosIntegralPay">会员支付
								</label>
								<label class="checkbox-inline" v-show="b.openGroupBuy == 1">
									<input type="checkbox" id="openPosGroupBuy" @change="posPaySetting('openPosGroupBuy')" v-model="m.openPosGroupBuy" value="1">
									<input type="hidden" name="openPosGroupBuy" v-model="m.openPosGroupBuy">团购支付
								</label>
								<label class="checkbox-inline" v-show="b.openCashCouponBuy == 1">
									<input type="checkbox" id="openPosCashCouponBuy" @change="posPaySetting('openPosCashCouponBuy')" v-model="m.openPosCashCouponBuy" value="1">
									<input type="hidden" name="openPosCashCouponBuy" v-model="m.openPosCashCouponBuy">代金券支付
								</label>
								<label class="checkbox-inline" v-show="b.openRpay == 1 && m.shopMode == 7">
									<input type="checkbox" id="openRpay" @change="posPaySetting('openRpay')" v-model="m.openRpay" value="1">
									<input type="hidden" name="openRpay" v-model="m.openRpay">R+支付
								</label>

							</div>
						</div>
						<!--外卖配送距离-->
						<div class="form-group">
							<label class="col-md-2 control-label">R+外卖最大配送范围(单位km)</label>
							<div class="col-md-4" style="width: 300px;">
								<input type="text" class="form-control" name="apart" :value="m.apart">
								<span style="position: absolute;right: 20px;top: 6px;color: #adacac;">km</span>
							</div>
						</div>
						<!-- 第三方接口appid-->
						<div class="form-group" v-if="b.openThirdInterface==1">
							<label class="col-md-2 control-label">第三方接口appid</label>
							<div class="col-sm-4" style="width: 300px;">
								<input type="text" name="thirdAppid"  class="form-control" v-model="m.thirdAppid">
							</div>
						</div>
						<!--日结短信-->
						<div class="form-group">
							<label class="col-md-2 control-label" style="color: #5babe6;">日结短信通知</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.isOpenSms == 1}" @click="sendMsg" style="margin-left: 0;">发送</span>
								<span class="switchRadio" :class="{active:m.isOpenSms == 0}" @click="notSendMsg">不发送</span>
								<input type="hidden" :value="m.isOpenSms" name="isOpenSms" />
							</div>
						</div>
						<!--手机号-->
						<div class="form-group" v-if="m.isOpenSms==1">
							<label class="col-md-2 control-label" :class="{formBox:m.isOpenSms == 1}">接收手机号</label>
							<div class="col-sm-4">
								<input type="text" name="noticeTelephone" placeholder="多个手机号码以逗号隔开" class="form-control" v-model="m.noticeTelephone">
							</div>
						</div>
						<!--通知方式-->
						<div class="form-group" v-show="m.isOpenSms==1">
							<label class="col-md-2 control-label" :class="{formBox:m.isOpenSms == 1}">通知方式</label>
							<input type="hidden" name="daySmsType" v-model="getDaySmsType">
							<div>
								<label class="checkbox-inline">
									<input type="checkbox" :true-value="2" v-model="daySmsTypeWx" disabled>微信推送
								</label>
								<label class="checkbox-inline">
									<input type="checkbox" :true-value="1" v-model="daySmsTypeSms">短信推送
								</label>
							</div>
						</div>
					</div>
					<!--打印设置-->
					<div class="mainContent" v-show="currentSettingType == 2">
						<!--打印总单-->
						<div class="form-group">
							<label class="col-md-2 control-label">打印总单</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.autoPrintTotal == 0}" @click="autoPrint" style="margin-left: 0;">打印</span>
								<span class="switchRadio" :class="{active:m.autoPrintTotal == 1}" @click="notAutoPrint">不打印</span>
								<input type="hidden" :value="m.autoPrintTotal" name="autoPrintTotal" />
							</div>
						</div>
						<%--打印退菜总单--%>
						<div class="form-group">
							<label class="col-md-2 control-label">打印加菜总单</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.isOpenAddPrintTotal == 1}" @click="isOpenAddPrintTotal" style="margin-left: 0;">打印</span>
								<span class="switchRadio" :class="{active:m.isOpenAddPrintTotal == 0}" @click="notIsOpenAddPrintTotal">不打印</span>
								<input type="hidden" :value="m.isOpenAddPrintTotal" name="isOpenAddPrintTotal" />
							</div>
						</div>
						<!--打印消费清单-->
						<div class="form-group">
							<label class="col-md-2 control-label">下单打印消费清单</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.autoPrintConsumeOrder == 1}" @click="autoPrintOrder" style="margin-left: 0;">打印</span>
								<span class="switchRadio" :class="{active:m.autoPrintConsumeOrder == 0}" @click="notAutoPrintOrder">不打印</span>
								<input type="hidden" :value="m.autoPrintConsumeOrder" name="autoPrintConsumeOrder" />
							</div>
						</div>

						<!--是否打印退菜详情-->
						<div class="form-group">
							<label class="col-md-2 control-label">是否打印退菜详情</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.printOutDetails == 1}" @click="autoPrintOutDetails" style="margin-left: 0;">打印</span>
								<span class="switchRadio" :class="{active:m.printOutDetails == 0}" @click="notAutoPrintOutDetails">不打印</span>
								<input type="hidden" :value="m.printOutDetails" name="printOutDetails" />
							</div>
						</div>
						<!--结账打印结账单-->
						<div class="form-group">
							<label class="col-md-2 control-label">结账打印结账单</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.autoPrintCheckOutOrder == 1}" @click="autoPrintCheckOut" style="margin-left: 0;">打印</span>
								<span class="switchRadio" :class="{active:m.autoPrintCheckOutOrder == 0}" @click="notAutoPrintCheckOut">不打印</span>
								<input type="hidden" :value="m.autoPrintCheckOutOrder" name="autoPrintCheckOutOrder" />
							</div>
						</div>
						<!--套餐出单方式-->
						<div class="form-group">
							<label class="col-md-2 control-label">套餐出单方式</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.printType == 0}" @click="changePrintType" style="margin-left: 0;">合并打印</span>
								<span class="switchRadio" :class="{active:m.printType == 1}" @click="notChangePrintType">拆分打印</span>
								<input type="hidden" :value="m.printType" name="printType" />
							</div>
						</div>
						<!--厨打是否拆分打印-->
						<div class="form-group">
							<label class="col-md-2 control-label" >厨打是否拆分打印</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.splitKitchen == 0}" @click="changeSplitKitchen" style="margin-left: 0;">合并打印</span>
								<span class="switchRadio" :class="{active:m.splitKitchen == 1}" @click="notChangeSplitKitchen">拆分打印</span>
								<input type="hidden" :value="m.splitKitchen" name="splitKitchen" />
							</div>
						</div>
						<!--退菜打印订单-->
						<div class="form-group">
							<label class="col-md-2 control-label">退菜打印订单</label>
							<div>
								<label class="checkbox-inline">
									<input type="checkbox" name="printReceipt" v-model="m.printReceipt" value = "1">打印总单
								</label>
								<label class="checkbox-inline">
									<input type="checkbox" name="printKitchen" v-model="m.printKitchen" value = "1">打印厨打
								</label>
							</div>
						</div>
						<!--POS更新订单打印-->
						<div class="form-group">
							<label class="col-md-2 control-label">POS更新订单打印</label>
							<div>
								<label class="checkbox-inline">
									<input type="checkbox" name="modifyOrderPrintReceipt" v-model="m.modifyOrderPrintReceipt" value = "1">打印总单
								</label>
								<label class="checkbox-inline">
									<input type="checkbox" name="modifyOrderPrintKitchen" v-model="m.modifyOrderPrintKitchen" value = "1">打印厨打
								</label>
							</div>
						</div>
						<!--店铺订单宣传语-->
						<div class="form-group">
							<label class="col-md-2 control-label">总单店铺宣传语</label>
							<div class="col-md-4" style="width: 300px;">
								<input type="text" class="form-control" name="orderWelcomeMessage" v-model="m.orderWelcomeMessage"
									   required="required" maxlength="20">
								<span class="help-block">最大长度为 20 个汉字</span>
							</div>
						</div>
						<!--日结小票模板类型-->
						<div class="form-group">
							<label class="col-md-2 control-label">日结小票模板类型</label>
							<div class="col-md-2 radio-list">
								<select class="form-control" name="templateType" v-model="m.templateType">
									<option value="0">经典版</option>
									<option value="1">升级版</option>
									<option value="2">简约版</option>
								</select>
							</div>
						</div>
						<!--开启显示用户标识功能-->
						<div class="form-group">
							<label class="col-md-2 control-label" >开启显示用户标识功能</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.isUserIdentity == 1}" @click="notChangesUserIdentity" style="margin-left: 0;">开启</span>
								<span class="switchRadio" :class="{active:m.isUserIdentity == 0}" @click="changeisUserIdentity">不开启</span>
								<input type="hidden" :value="m.isUserIdentity" name="isUserIdentity" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-2 control-label" >开启VIP用户标识功能</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.isOpenUserSign == 1}" @click="notIsOpenUserSign" style="margin-left: 0;">开启</span>
								<span class="switchRadio" :class="{active:m.isOpenUserSign == 0}" @click="IsOpenUserSign">不开启</span>
								<input type="hidden" :value="m.isOpenUserSign" name="isOpenUserSign" />
							</div>
						</div>


					</div>
					<!--功能设置-->
					<div class="mainContent" v-show="currentSettingType == 3">
						<!--扫码进是否堂食-->
						<div class="form-group">
							<label class="col-md-2 control-label">扫码进是否堂食</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.sweepMode == 0}" @click="changeSweepMode" style="margin-left: 0;">默认堂食</span>
								<span class="switchRadio" :class="{active:m.sweepMode == 1}" @click="notChangeSweepMode">需选择模式</span>
								<input type="hidden" :value="m.sweepMode" name="sweepMode" />
							</div>
						</div>
						<!--外带是否需要扫码-->
						<div class="form-group">
							<label class="col-md-2 control-label">外带是否需要扫码</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.continueOrderScan == 1}" @click="continueOrderScan" style="margin-left: 0;">需要</span>
								<span class="switchRadio" :class="{active:m.continueOrderScan == 0}" @click="notContinueOrderScan">不需要</span>
								<input type="hidden" :value="m.continueOrderScan" name="continueOrderScan" />
							</div>
						</div>
						<!--是否启用服务费-->
						<div class="form-group">
							<label class="col-md-2 control-label" :class="{formBox:m.isUseServicePrice == 1}">启用服务费</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.isUseServicePrice == 1}" @click="useServicePrice" style="margin-left: 0;">需要</span>
								<span class="switchRadio" :class="{active:m.isUseServicePrice == 0}" @click="notUseServicePrice">不需要</span>
								<input type="hidden" :value="m.isUseServicePrice" name="isUseServicePrice" />
							</div>
						</div>
						<div class="form-group" v-show="m.isUseServicePrice == 1">
							<label class="col-md-2 control-label" :class="{formBox:m.isUseServicePrice == 1}">服务费版本</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.serviceType == 0}" @click="useServiceType" style="margin-left: 0;">经典版</span>
								<span class="switchRadio" :class="{active:m.serviceType == 1}" @click="notUseServiceType">升级版</span>
								<input type="hidden" :value="m.serviceType" name="serviceType" />
							</div>
						</div>
						<!-- 服务费经典版begin -->
						<div v-show="m.isUseServicePrice==1 && m.serviceType == 0">
							<div v-show="showp">
								<div class="form-group">
									<label class="col-md-2 control-label formBox">名称</label>
									<div class="col-md-4 radio-list">
										<input type="test" class="form-control" name="serviceName" v-if="!m.serviceName" value="服务费">
										<input type="test" class="form-control" name="serviceName" v-if="m.serviceName" v-model="m.serviceName">
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label formBox">服务费/每人</label>
									<div class="col-md-4">
										<input type="text" class="form-control" name="servicePrice" v-model="m.servicePrice" min="0">
									</div>
								</div>
							</div>
						</div>
						<!-- 服务费经典版end -->

						<!-- 服务费升级版begin -->
						<div class="form-group" v-show="m.isUseServicePrice == 1 && m.serviceType == 1">
							<label class="col-md-2 control-label" :class="{ formBox : m.isOpenTablewareFee == 1, gray : m.isOpenTablewareFee == 0}" style="margin-top: 20px;">餐具费：</label>
							<div class="col-md-6">
								<div class="row">
									<div class="col-md-4">
										<p style="text-align: center;margin: 5px 0;font-weight: bold">名称</p>
									</div>
									<div class="col-md-4">
										<p style="text-align: center;margin: 5px 0;font-weight: bold">价格</p>
									</div>
									<div class="col-md-4">
										<p style="text-align: center;margin: 5px 0;font-weight: bold">是否启用(勾选启用)</p>
									</div>
								</div>
								<div class="row">
									<div class="col-md-4">
										<input v-show="m.isOpenTablewareFee == 0" type="text" class="form-control" disabled>
										<input v-else type="text" class="form-control" name="tablewareFeeName" v-model="m.tablewareFeeName">
									</div>
									<div class="col-md-4">
										<input v-show="m.isOpenTablewareFee == 0" type="text" class="form-control" disabled>
										<input v-else type="text" class="form-control" name="tablewareFeePrice" v-model="m.tablewareFeePrice">
									</div>
									<div class="col-md-4" style="text-align: center;margin-top: 8px;">
										<input type="checkbox" class="form-control" value="1" name="isOpenTablewareFee" v-model="m.isOpenTablewareFee">
									</div>
								</div>
							</div>
						</div>
						<div class="form-group" v-show="m.isUseServicePrice == 1 && m.serviceType == 1">
							<label class="col-md-2 control-label" :class="{ formBox : m.isOpenTowelFee == 1, gray : m.isOpenTowelFee == 0}" style="margin-top: 20px;">纸巾费：</label>
							<div class="col-md-6">
								<div class="row">
									<div class="col-md-4">
										<p style="text-align: center;margin: 5px 0;font-weight: bold">名称</p>
									</div>
									<div class="col-md-4">
										<p style="text-align: center;margin: 5px 0;font-weight: bold">价格</p>
									</div>
									<div class="col-md-4">
										<p style="text-align: center;margin: 5px 0;font-weight: bold">是否启用(勾选启用)</p>
									</div>
								</div>
								<div class="row">
									<div class="col-md-4">
										<input v-show="m.isOpenTowelFee == 0" type="text" class="form-control" disabled>
										<input v-else type="text" class="form-control" name="towelFeeName" v-model="m.towelFeeName">
									</div>
									<div class="col-md-4">
										<input v-show="m.isOpenTowelFee == 0" type="text" class="form-control" disabled>
										<input v-else type="text" class="form-control" name="towelFeePrice" v-model="m.towelFeePrice">
									</div>
									<div class="col-md-4" style="text-align: center;margin-top: 8px;">
										<input type="checkbox" class="form-control" value="1" name="isOpenTowelFee" v-model="m.isOpenTowelFee">
									</div>
								</div>
							</div>
						</div>
						<div class="form-group" v-show="m.isUseServicePrice == 1 && m.serviceType == 1">
							<label class="col-md-2 control-label" :class="{formBox:m.isOpenSauceFee == 1, gray : m.isOpenSauceFee == 0}" style="margin-top: 20px;">酱料费：</label>
							<div class="col-md-6">
								<div class="row">
									<div class="col-md-4">
										<p style="text-align: center;margin: 5px 0;font-weight: bold">名称</p>
									</div>
									<div class="col-md-4">
										<p style="text-align: center;margin: 5px 0;font-weight: bold">价格</p>
									</div>
									<div class="col-md-4">
										<p style="text-align: center;margin: 5px 0;font-weight: bold">是否启用(勾选启用)</p>
									</div>
								</div>
								<div class="row">
									<div class="col-md-4">
										<input v-show="m.isOpenSauceFee == 0" type="text" class="form-control" disabled>
										<input v-else type="text" class="form-control" name="sauceFeeName" v-model="m.sauceFeeName">
									</div>
									<div class="col-md-4">
										<input v-show="m.isOpenSauceFee == 0" type="text" class="form-control" disabled>
										<input v-else type="text" class="form-control" name="sauceFeePrice" v-model="m.sauceFeePrice">
									</div>
									<div class="col-md-4" style="text-align: center;margin-top: 8px;">
										<input type="checkbox" class="form-control" value="1" name="isOpenSauceFee" v-model="m.isOpenSauceFee">
									</div>
								</div>
							</div>
						</div>
						<!-- 服务费升级版end -->
						<div class="form-group">
							<label class="col-md-2 control-label">点餐页面滑动效果</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.rollingSwitch == 1}" @click="useRollingSwitch" style="margin-left: 0;">滑动切换</span>
								<span class="switchRadio" :class="{active:m.rollingSwitch == 0}" @click="notUseRollingSwitch">直接跳转</span>
								<input type="hidden" :value="m.rollingSwitch" name="rollingSwitch" />
							</div>
						</div>
						<!--启用餐盒费-->
						<div class="form-group">
							<label class="col-md-2 control-label" :class="{formBox : m.isMealFee == 1}">启用餐盒费</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.isMealFee == 1}" @click="useIsMealFee" style="margin-left: 0;">启用</span>
								<span class="switchRadio" :class="{active:m.isMealFee == 0}" @click="notUseIsMealFee">不启用</span>
								<input type="hidden" :value="m.isMealFee" name="isMealFee" />
							</div>
						</div>
						<div class="form-group" v-if="m.isMealFee==1">
							<label class="col-md-2 control-label" :class="{formBox : m.isMealFee == 1}">名称</label>
							<div class="col-md-4">
								<input type="text" class="form-control" name="mealFeeName" v-if="!m.mealFeeName" value="餐盒费" required="required">
								<input type="text" class="form-control" name="mealFeeName" v-if="m.mealFeeName" v-model="m.mealFeeName" required="required">
							</div>
						</div>
						<div class="form-group" v-if="m.isMealFee==1">
							<label class="col-md-2 control-label" :class="{formBox : m.isMealFee == 1}">餐盒费元/个</label>
							<div class="col-md-4">
								<input type="number" class="form-control"
									   name="mealFeePrice" placeholder="(建议输入整数)"
									   v-model="m.mealFeePrice" required="required" min="0">
							</div>
						</div>
						<!--pos点单菜品价格-->
						<div class="form-group">
							<label class="col-md-2 control-label">pos点单菜品价格</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.posPlusType == 1}" @click="useNowPrice" style="margin-left: 0;">原价</span>
								<span class="switchRadio" :class="{active:m.posPlusType == 0}" @click="useFansPrice">粉丝价</span>
								<input type="hidden" :value="m.posPlusType" name="posPlusType" />
							</div>
						</div>
						<!--开启订单备注-->
						<div class="form-group" v-show="b.openOrderRemark == 1">
							<label class="col-md-2 control-label">开启订单备注</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.openOrderRemark == 1}" @click="openRemark" style="margin-left: 0;">启用</span>
								<span class="switchRadio" :class="{active:m.openOrderRemark == 0}" @click="notOpenRemark">不启用</span>
								<input type="hidden" :value="m.openOrderRemark" name="openOrderRemark" />
							</div>
						</div>
						<!--推荐菜品是否开启-->
						<div class="form-group">
							<label class="col-md-2 control-label" >启用推荐菜品</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.isRecommendCategory == 1}" @click="openRecommend" style="margin-left: 0;">启用</span>
								<span class="switchRadio" :class="{active:m.isRecommendCategory == 0}" @click="notOpenRecommend">不启用</span>
								<input type="hidden" :value="m.isRecommendCategory" name="isRecommendCategory" />
							</div>
						</div>
						<!--开启POS点单-->
						<div class="form-group" v-show="b.posOpenTable == 1">
							<label class="col-md-2 control-label">POS点单</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.posOpenTable == 1}" @click="openPos" style="margin-left: 0;">启用</span>
								<span class="switchRadio" :class="{active:m.posOpenTable == 0}" @click="notOpenPos">不启用</span>
								<input type="hidden" :value="m.posOpenTable" name="posOpenTable" />
							</div>
						</div>
						<!--POS端结算订单-->
						<div class="form-group" v-show="m.shopMode == 6 && m.allowAfterPay == 0">
							<label class="col-md-2 control-label">POS端结算订单功能</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.openPosPayOrder == 1}" @click="openPosPay" style="margin-left: 0;">启用</span>
								<span class="switchRadio" :class="{active:m.openPosPayOrder == 0}" @click="notOpenPosPay">不启用</span>
								<input type="hidden" :value="m.openPosPayOrder" name="openPosPayOrder" />
							</div>
						</div>
						<!--POS端折扣-->
						<div class="form-group">
							<label class="col-md-2 control-label">POS端折扣功能</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.openPosDiscount == 1}" @click="openPosDiscount" style="margin-left: 0;">启用</span>
								<span class="switchRadio" :class="{active:m.openPosDiscount == 0}" @click="notOpenPosDiscount">不启用</span>
								<input type="hidden" :value="m.openPosDiscount" name="openPosDiscount" />
							</div>
						</div>
						<!--POS端换桌功能-->
						<div class="form-group">
							<label class="col-md-2 control-label" :class="{formBox: m.isTurntable == 1}">POS端换桌功能</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.isTurntable == 1}" @click="openTurnTable" style="margin-left: 0;">启用</span>
								<span class="switchRadio" :class="{active:m.isTurntable == 0}" @click="notOpenTurnTable">不启用</span>
								<input type="hidden" :value="m.isTurntable" name="isTurntable" />
							</div>
						</div>
						<!--打印方式-->
						<div class="form-group" v-show="m.isTurntable == 1">
							<label class="col-md-2 control-label" :class="{formBox: m.isTurntable == 1}">打印方式</label>
							<div>
								<label class="checkbox-inline">
									<input type="checkbox" name="turntablePrintReceipt" :true-value="1" v-model="turntablePrintReceipt">前台打印
								</label>
								<label class="checkbox-inline">
									<input type="checkbox" name="turntablePrintKitchen" :true-value="1"  v-model="m.turntablePrintKitchen">厨房打印
								</label>
							</div>
						</div>
						<!--优惠券到期提醒-->
						<div class="form-group">
							<label class="col-md-2 control-label">优惠券到期提醒(天)</label>
							<div class="col-md-4">
								<input type="number" class="form-control"
									   name="recommendTime" placeholder="(输入整数)"
									   v-model="m.recommendTime" required="required" min="0">
							</div>
						</div>
						<!--就餐提醒-->
						<div class="form-group">
							<label class="col-md-2 control-label">开启就餐提醒</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.isPush == 1}" @click="needRemark" style="margin-left: 0;">需要</span>
								<span class="switchRadio" :class="{active:m.isPush == 0}" @click="notNeedRemark">不需要</span>
								<input type="hidden" :value="m.isPush" name="isPush" />
							</div>
						</div>
						<!--消息内容-->
						<div class="form-group" v-if="m.isPush==1">
							<label class="col-md-2 control-label" :class="{formBox: m.isPush == 1}">微信消息内容</label>
							<div class="col-md-4">
								<input type="text" class="form-control" placeholder="消息文案" name="pushContext" v-if="m.pushContext" v-model="m.pushContext"
									   required="required">
							</div>
						</div>
						<!--推送时间-->
						<div class="form-group" v-if="m.isPush==1" >
							<label class="col-md-2 control-label" :class="{formBox: m.isPush == 1}">推送时间(分钟)</label>
							<div class="col-md-4">
								<input type="number" class="form-control" name="pushTime" placeholder="(建议输入整数,以秒为单位)"
									   v-model="m.pushTime" required="required" min="0">
							</div>
						</div>
						<!--POS账户充值功能-->
						<div class="form-group" v-show="b.openPosCharge == 1">
							<label class="col-md-2 control-label">POS账户充值功能</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.openPosCharge == 1}" @click="openPosCharge" style="margin-left: 0;">启用</span>
								<span class="switchRadio" :class="{active:m.openPosCharge == 0}" @click="notOpenPosCharge">不启用</span>
								<input type="hidden" :value="m.openPosCharge" name="openPosCharge" />
							</div>
						</div>
						<!--差评通知-->
						<div class="form-group">
							<label class="col-md-2 control-label" :class="{formBox: m.openBadAppraisePrintOrder == 1}">差评通知</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.openBadAppraisePrintOrder == 1}" @click="openBadAppraise" style="margin-left: 0;">启用</span>
								<span class="switchRadio" :class="{active:m.openBadAppraisePrintOrder == 0}" @click="notOpenBadAppraise">不启用</span>
								<input type="hidden" :value="m.openBadAppraisePrintOrder" name="openBadAppraisePrintOrder" />
							</div>
						</div>
						<div class="form-group" v-show="m.openBadAppraisePrintOrder == 1">
							<label class="col-md-2 control-label" :class="{formBox: m.openBadAppraisePrintOrder == 1}">打印方式：</label>
							<div>
								<label class="checkbox-inline">
									<input type="checkbox" name="badAppraisePrintReceipt" :true-value="1" v-model="badAppraisePrintReceipt">前台打印
								</label>
								<label class="checkbox-inline">
									<input type="checkbox" name="badAppraisePrintKitchen" :true-value="1"  v-model="m.badAppraisePrintKitchen">厨房打印
								</label>
							</div>
						</div>
						<!--差评预警功能-->
						<div class="form-group">
							<label class="col-md-2 control-label" :class="{formBox: m.openBadWarning == 1}">差评预警功能</label>
							<div class="col-md-4">
								<span class="switchRadio" :class="{active:m.openBadWarning == 1}" @click="openBadWarning" style="margin-left: 0;">启用</span>
								<span class="switchRadio" :class="{active:m.openBadWarning == 0}" @click="notOpenBadWarning">不启用</span>
								<input type="hidden" :value="m.openBadWarning" name="openBadWarning" />
							</div>
						</div>
						<div class="form-group" v-show="m.openBadWarning==1">
							<label class="col-md-2 control-label" :class="{formBox: m.openBadWarning == 1}">差评预警功能</label>
							<div class="col-md-6 radio-list">
								<label class="radio-inline">
									<input type="checkbox" name="warningSms"v-model="m.warningSms" value="1">短信推送
								</label>
								<label class="radio-inline">
									<input type="checkbox" name="warningWechat" v-model="m.warningWechat" value="1">微信推送
								</label>
							</div>
						</div>
						<div class="form-group" v-show="m.openBadWarning == 1">
							<label class="col-md-2 control-label" :class="{ formBox : m.openBadWarning == 1}">差评预警关键词：</label>
							<div class="col-md-6">
								<textarea class="form-control" v-model="m.warningKey" name="warningKey">
								</textarea><font color="red">*多个关键词中间以英文状态下的逗号(,)隔开</font>
							</div>
						</div>

						<%--<div class="form-group" v-if="b.consumptionRebate==1">--%>
							<%--<label class="col-md-2 control-label">启用消费返利(1:1)</label>--%>
							<%--<div class="col-md-4">--%>
								<%--<span class="switchRadio" :class="{active:m.consumptionRebate == 1}" @click="openRebate" style="margin-left: 0;">启用</span>--%>
								<%--<span class="switchRadio" :class="{active:m.consumptionRebate == 0}" @click="notOpenRebate">不启用</span>--%>
								<%--<input type="hidden" :value="m.consumptionRebate" name="consumptionRebate" />--%>
							<%--</div>--%>
							<%--<!--<div class="col-md-4 radio-list">--%>
								<%--<label class="radio-inline">--%>
									<%--<input type="radio" name="consumptionRebate"v-model="m.consumptionRebate" value="1">是--%>
								<%--</label>--%>
								<%--<label class="radio-inline">--%>
									<%--<input type="radio" name="consumptionRebate" v-model="m.consumptionRebate" value="0"> 否--%>
								<%--</label>--%>
							<%--</div>-->--%>
						<%--</div>--%>

						<div class="form-group" v-if="m.consumptionRebate==1 && b.consumptionRebate==1">
							<label class="col-md-2 control-label">消费返利解冻时间:</label>
							<div class="col-md-4 radio-list">
								<div class="input-group date form_datetime">
									<input type="text" readonly class="form-control" name="rebateTime" v-model="m.rebateTime" @focus="initCouponTime">
									<span class="input-group-btn">
										<button class="btn default date-set" type="button">
											<i class="fa fa-calendar" @click="initCouponTime"></i>
										</button>
									</span>
								</div>
							</div>
						</div>


					</div>
					<div class="text-center" style="padding: 15px 0;">
						<input class="btn green" type="submit" value="保存" />
						<a class="btn default" @click="cancel" style="margin-left: 20px;">取消</a>
					</div>
					</form>
			</div>

		</div>
	</div>
</div>

<script>
	$.ajax({
		url:"shopInfo/list_one",
		success:function(result){
			$(document).ready(function() {
				toastr.options = {
					"closeButton" : true,
					"debug" : false,
					"positionClass" : "toast-top-right",
					"onclick" : null,
					"showDuration" : "500",
					"hideDuration" : "500",
					"timeOut" : "3000",
					"extendedTimeOut" : "500",
					"showEasing" : "swing",
					"hideEasing" : "linear",
					"showMethod" : "fadeIn",
					"hideMethod" : "fadeOut"
				}

				var temp;
				var vueObj = new Vue({
					el : "#control",
					data : {
						settingList:[
							{
								type: 1,
								name: "基础设置"
							},
							{
								type: 2,
								name: "打印设置"
							},
							{
								type: 3,
								name: "功能设置"
							},
						],
						currentSettingType:1,
						m : result.data.shop,
						b : result.data.brand,
						showa:true,
						showlate:true,
						showp:true,
						getDaySmsType:null,
						daySmsTypeWx : 2,
						daySmsTypeSms : 0
					},
					watch: {
						'm.consumeConfineUnit': 'hideShowa',
						'm.isUseServicePrice':'hideServiceP',
					},
					computed : {
						getDaySmsType : function () {
							return this.daySmsTypeSms + this.daySmsTypeWx;
						}
					},
					created : function () {
						//初始化 日结短信   通知方式
						if(this.m.daySmsType==3){
							this.daySmsTypeWx = 2;
							this.daySmsTypeSms = 1;
						}else if(this.m.daySmsType==2){
							this.daySmsTypeWx=2;
							this.daySmsTypeSms=false;
						}
						this.m.rebateTime = new Date(this.m.rebateTime).format("yyyy-MM-dd");
					},
					methods : {
//						selectPayMode:function(e,val){
//							debugger;
//							if(e.target.dataset.name == "allowFirstPay" && val == 1){
//
//							}
//							switch (e.target.dataset.name)
//							{
//								case 'allowFirstPay':
//		                            this.m.allowFirstPay = val
//		                            break;
//		                        case 'allowAfterPay':
//		                            this.m.allowAfterPay = val
//		                            break;
//							}
//						},
						changeSetting:function(f){
							this.currentSettingType = f.type;
						},
						changeFirstPay:function(){
							if(this.m.allowFirstPay == 1 && this.m.allowAfterPay == 0){
								this.m.allowFirstPay = 0;
								this.m.allowAfterPay = 1;
							}else if(this.m.allowFirstPay == 1 && this.m.allowAfterPay == 1){
								this.m.allowFirstPay == 0;
							}else if(this.m.allowFirstPay == 0 && this.m.allowAfterPay == 1){
								toastr.error("亲，此项必选一个!");
							}
						},
						changeAfterPay:function(){
							if(this.m.allowFirstPay == 0 && this.m.allowAfterPay == 1){
								this.m.allowFirstPay = 1;
								this.m.allowAfterPay = 0;
							}else if(this.m.allowFirstPay == 1 && this.m.allowAfterPay == 1){
								this.m.allowAfterPay == 0;
							}else if(this.m.allowFirstPay == 1 && this.m.allowAfterPay == 0){
								toastr.error("亲，此项必选一个!");
							}
						},
						sendMsg:function(){
							if(this.m.isOpenSms == 0){
								this.m.isOpenSms = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notSendMsg:function(){
							if(this.m.isOpenSms == 1){
								this.m.isOpenSms = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						needRemark:function(){
							if(this.m.isPush == 0){
								this.m.isPush = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notNeedRemark:function(){
							if(this.m.isPush == 1){
								this.m.isPush = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						autoPrint:function(){
							if(this.m.autoPrintTotal == 1){
								this.m.autoPrintTotal = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notAutoPrint:function(){
							if(this.m.autoPrintTotal == 0){
								this.m.autoPrintTotal = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
                        isOpenAddPrintTotal:function(){
                   			 if(this.m.isOpenAddPrintTotal == 0){
                       			 this.m.isOpenAddPrintTotal = 1;
							 }else{
                       			 toastr.error("亲，此项为必选!");
                   			 }
						},
                        notIsOpenAddPrintTotal:function(){
                            if(this.m.isOpenAddPrintTotal == 1){
                                this.m.isOpenAddPrintTotal = 0;
                            }else{
                                toastr.error("亲，此项为必选!");
                            }
                        },
						autoPrintOrder:function(){
							if(this.m.autoPrintConsumeOrder == 0){
								this.m.autoPrintConsumeOrder = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notAutoPrintOrder:function(){
							if(this.m.autoPrintConsumeOrder == 1){
								this.m.autoPrintConsumeOrder = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
                         autoPrintOutDetails:function(){
							if(this.m.printOutDetails == 0){
								this.m.printOutDetails = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notAutoPrintOutDetails:function(){
							if(this.m.printOutDetails == 1){
								this.m.printOutDetails = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
                        	},
						autoPrintCheckOut:function(){
							if(this.m.autoPrintCheckOutOrder == 0){
								this.m.autoPrintCheckOutOrder = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notAutoPrintCheckOut:function(){
							if(this.m.autoPrintCheckOutOrder == 1){
								this.m.autoPrintCheckOutOrder = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						changePrintType:function(){
							if(this.m.printType == 1){
								this.m.printType = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notChangePrintType:function(){
							if(this.m.printType == 0){
								this.m.printType = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						changeSplitKitchen:function(){
							if(this.m.splitKitchen == 1){
								this.m.splitKitchen = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notChangeSplitKitchen:function(){
							if(this.m.splitKitchen == 0){
								this.m.splitKitchen = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
                        changeisUserIdentity:function(){
                            if(this.m.isUserIdentity == 1){
                                this.m.isUserIdentity = 0;
                            }else{
                                toastr.error("亲，此项为必选!");
                            }
                        },
                        notChangesUserIdentity:function(){
                            if(this.m.isUserIdentity == 0){
                                this.m.isUserIdentity = 1;
                            }else{
                                toastr.error("亲，此项为必选!");
                            }
                        },
                        IsOpenUserSign:function(){
                            if(this.m.isOpenUserSign == 1){
                                this.m.isOpenUserSign = 0;
                            }else{
                                toastr.error("亲，此项为必选!");
                            }
                        },
                        notIsOpenUserSign:function(){
                            if(this.m.isOpenUserSign == 0){
                                this.m.isOpenUserSign = 1;
                            }else{
                                toastr.error("亲，此项为必选!");
                            }
                        },
						changeSweepMode:function(){
							if(this.m.sweepMode == 1){
								this.m.sweepMode = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notChangeSweepMode:function(){
							if(this.m.sweepMode == 0){
								this.m.sweepMode = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						continueOrderScan:function(){
							if(this.m.continueOrderScan == 0){
								this.m.continueOrderScan = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notContinueOrderScan:function(){
							if(this.m.continueOrderScan == 1){
								this.m.continueOrderScan = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						useServicePrice:function(){
							if(this.m.isUseServicePrice == 0){
								this.m.isUseServicePrice = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notUseServicePrice:function(){
							if(this.m.isUseServicePrice == 1){
								this.m.isUseServicePrice = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						useServiceType:function(){
							if(this.m.serviceType == 1){
								this.m.serviceType = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notUseServiceType:function(){
							if(this.m.serviceType == 0){
								this.m.serviceType = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						useRollingSwitch:function(){
							if(this.m.rollingSwitch == 0){
								this.m.rollingSwitch = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notUseRollingSwitch:function(){
							if(this.m.rollingSwitch == 1){
								this.m.rollingSwitch = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						useIsMealFee:function(){
							if(this.m.isMealFee == 0){
								this.m.isMealFee = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notUseIsMealFee:function(){
							if(this.m.isMealFee == 1){
								this.m.isMealFee = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						useNowPrice:function(){
							if(this.m.posPlusType == 0){
								this.m.posPlusType = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						useFansPrice:function(){
							if(this.m.posPlusType == 1){
								this.m.posPlusType = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						openRemark:function(){
							if(this.m.openOrderRemark == 0){
								this.m.openOrderRemark = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notOpenRemark:function(){
							if(this.m.openOrderRemark == 1){
								this.m.openOrderRemark = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						openRecommend:function(){
							if(this.m.isRecommendCategory == 0){
								this.m.isRecommendCategory = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notOpenRecommend:function(){
							if(this.m.isRecommendCategory == 1){
								this.m.isRecommendCategory = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						openPos:function(){
							if(this.m.posOpenTable == 0){
								this.m.posOpenTable = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notOpenPos:function(){
							if(this.m.posOpenTable == 1){
								this.m.posOpenTable = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						openPosPay:function(){
							if(this.m.openPosPayOrder == 0){
								this.m.openPosPayOrder = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notOpenPosPay:function(){
							if(this.m.openPosPayOrder == 1){
								this.m.openPosPayOrder = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						openPosDiscount:function(){
							if(this.m.openPosDiscount == 0){
								this.m.openPosDiscount = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notOpenPosDiscount:function(){
							if(this.m.openPosDiscount == 1){
								this.m.openPosDiscount = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						openTurnTable:function(){
							if(this.m.isTurntable == 0){
								this.m.isTurntable = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notOpenTurnTable:function(){
							if(this.m.isTurntable == 1){
								this.m.isTurntable = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						openPosCharge:function(){
							if(this.m.openPosCharge == 0){
								this.m.openPosCharge = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notOpenPosCharge:function(){
							if(this.m.openPosCharge == 1){
								this.m.openPosCharge = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						openBadAppraise:function(){
							if(this.m.openBadAppraisePrintOrder == 0){
								this.m.openBadAppraisePrintOrder = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notOpenBadAppraise:function(){
							if(this.m.openBadAppraisePrintOrder == 1){
								this.m.openBadAppraisePrintOrder = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						openBadWarning:function(){
							if(this.m.openBadWarning == 0){
								this.m.openBadWarning = 1;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
						notOpenBadWarning:function(){
							if(this.m.openBadWarning == 1){
								this.m.openBadWarning = 0;
							}else{
								toastr.error("亲，此项为必选!");
							}
						},
//						openRebate:function(){
//							if(this.m.consumptionRebate == 0){
//								this.m.consumptionRebate = 1;
//							}else{
//								toastr.error("亲，此项为必选!");
//							}
//						},
//						notOpenRebate:function(){
//							if(this.m.consumptionRebate == 1){
//								this.m.consumptionRebate = 0;
//							}else{
//								toastr.error("亲，此项为必选!");
//							}
//						},
						weChatPaySetting: function (name) {
							var that = this;
							var checked = $("#"+name).prop("checked");
							if (checked){
								this.setWeChatSetting(name,1);
							}else{
								that.setWeChatSetting(name,0);
							}
						},
						posPaySetting: function (name) {
							var that = this;
							var checked = $("#"+name).prop("checked");
							if (checked){
								that.setPosPaySetting(name,1);
							}else{
								that.setPosPaySetting(name,0);
							}
						},
						hideServiceP  :function (){
							if(result.data.isUseServicePrice ==0){
								this.showp = false;
							}else{
								this.showp = true;
							}
						},
						hideShowa : function(){
							if(this.m.consumeConfineUnit == 3){
								this.showa = false;
								this.showlate=false;
							}else{
								this.showa = true;
								this.showlate=true;
							}
						},
						initTime : function() {
							$(".timepicker-no-seconds").timepicker({
								autoclose : true,
								showMeridian : false,
								minuteStep : 5
							});
						},
						save : function(e) {
							var formDom = e.target;
							var allowAfterPay = $("input[name='allowAfterPay']:checked").val();
							var allowFirstPay = $("input[name='allowFirstPay']:checked").val();
							if(allowAfterPay == 1 && allowFirstPay == 1){
								toastr.clear();
								toastr.error("混合支付模式下不可以同时关闭2种支付方式！");
								return;
							}

							var reg = new RegExp("^[0-9]*$"); //用来验证服务费数据是数字
							var serviceType = this.m.serviceType;
							if (serviceType == 0){ //经典版服务费
								var servicePrice = this.m.servicePrice;
								if (!reg.test(servicePrice)){
									toastr.clear();
									toastr.error("经典版服务费只能是整数");
									return;
								}
							}else { //升级版服务费
								var isOpenSauceFee = this.m.isOpenSauceFee;
								var isOpenTowelFee = this.m.isOpenTowelFee;
								var isOpenTablewareFee = this.m.isOpenTablewareFee;
								if (isOpenSauceFee == 1 || isOpenTowelFee == 1 || isOpenTablewareFee == 1){ //如果开通了餐具费、纸巾费、酱料费
									var tablewareFeePrice = this.m.tablewareFeePrice;
									var towelFeePrice = this.m.towelFeePrice;
									var sauceFeePrice = this.m.sauceFeePrice;
									if (!reg.test(tablewareFeePrice) || !reg.test(towelFeePrice) || !reg.test(sauceFeePrice)){ //如果有新版服务费不是数字的
										toastr.clear();
										toastr.error("升级版服务费的价格只能是整数");
										return;
									}
								}
							}
							$.ajax({
								url : "shopInfo/modify",
								data : $(formDom).serialize(),
								success : function(result) {
									debugger;
									console.log(result+"----");
									if (result.success) {
										toastr.clear();
										toastr.success("保存成功！");
									} else {
										toastr.clear();
										toastr.error("保存失败");
									}
								},
								error : function() {
									toastr.clear();
									toastr.error("保存失败");
								}
							})

						},
						initCouponTime: function(){
							$('.form_datetime').datetimepicker({
								format: "yyyy-mm-dd",
								autoclose: true,
								todayBtn: true,
								todayHighlight: true,
								showMeridian: true,
								language: 'zh-CN',//中文，需要引用zh-CN.js包
								startView: 2,//月视图
								minView: 2//日期时间选择器所能够提供的最精确的时间选择视图
							});
						},
						cancel : function() {
							this.initContent();

						},
						uploadSuccess:function(url){
							$("[name='photo']").val(url).trigger("change");
							toastr.success("上传成功！");
						},
						uploadError:function(msg){
							toastr.error("上传失败");
						},
						initContent:function(){
							var that = this;
							$.ajax({
								url:"shopInfo/list_one",
								type:"post",
								dataType:"json",
								success:function (resultData) {
									that.m = resultData.data.shop;
									that.b = resultData.data.brand;
								}
							});
						},
						setWeChatSetting : function (name,value) {
							switch (name){
								case "aliPay":
									this.m.aliPay = value;
									break;
								case "openUnionPay":
									this.m.openUnionPay = value;
									break;
								case "openMoneyPay":
									this.m.openMoneyPay = value;
									break;
								case "openShanhuiPay":
									this.m.openShanhuiPay = value;
									break;
								case "integralPay":
									this.m.integralPay = value;
									break;
							}
						},
						setPosPaySetting : function (name,value) {
							switch (name){
								case "openPosWeChatPay":
									this.m.openPosWeChatPay = value;
									break;
								case "openPosAliPay":
									this.m.openPosAliPay = value;
									break;
								case "openPosUnionPay":
									this.m.openPosUnionPay = value;
									break;
								case "openPosMoneyPay":
									this.m.openPosMoneyPay = value;
									break;
								case "openPosShanhuiPay":
									this.m.openPosShanhuiPay = value;
									break;
								case "openPosIntegralPay":
									this.m.openPosIntegralPay = value;
									break;
								case "openPosGroupBuy":
									this.m.openPosGroupBuy = value;
									break;
								case "openPosCashCouponBuy":
									this.m.openPosCashCouponBuy = value;
									break;
                                case "openRpay":
                                    this.m.openRpay = value;
                                    break;
							}
						},
						setTemplateType : function (value) {
							alert(value);
							this.m.templateType = value;
						}
					}
				});

			}());
		}

	})
</script>
