<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="v-bind" uri="http://www.springframework.org/tags/form" %>
<style>
    #brandAccountSettingModel .form-group{
        margin-bottom: 0px;
    }
    .padding-name {
    	padding-bottom: 5px;
    }
    .flex-container {
	    display: -webkit-flex;
	    display: flex;
	    -webkit-justify-content: space-around;
	    justify-content: space-around;
	    margin: 0 30%;
	}
</style>
<div id="control">
    <div class="row form-div" v-if="showPlatform">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">第三方平台</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" class="form-horizontal">
                        <div id="shops">

                            <div class="table-body">
                                <table id="shopList" class="table table-striped table-hover table-bordered "></table>
                            </div>
                        </div>
                        <div class="text-center">
                            <input type="hidden" name="id" v-model="m.id"/>
                            <input class="btn green" type="button" @click="savePlatform" value="分配"/>
                            <a class="btn default" @click="cancelPlatform">取消</a>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">
                <div class="portlet-title text-center">
                    <div class="caption" style="float: none">
                        <span class="caption-subject bold font-blue-hoki"><strong>品牌基本设置</strong></span>
                    </div>
                    &nbsp; <a class="btn green" v-if="m.id" @click="cleanBrandSetting(m.id)">清缓存</a>
                </div>
                <div class="portlet-body">
                    <form role="form" class="form-horizontal" action="{{m.id?'brandsetting/modify':'brandsetting/create'}}" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-4 control-label">短信签名：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="smsSign" v-model="m.smsSign">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">评论最小金额：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="appraiseMinMoney" v-model="m.appraiseMinMoney">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">新用户注册提醒标题：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="customerRegisterTitle" v-model="m.customerRegisterTitle">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信欢迎图片：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatWelcomeImg" v-model="m.wechatWelcomeImg">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信欢迎标题：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatWelcomeTitle" v-model="m.wechatWelcomeTitle">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信欢迎地址：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatWelcomeUrl" v-model="m.wechatWelcomeUrl">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信欢迎文本：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatWelcomeContent" v-model="m.wechatWelcomeContent">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">品牌标语：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="slogan" v-model="m.slogan">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信品牌名名称：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="wechatBrandName" v-model="m.wechatBrandName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信粉丝圈名称：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatHomeName" v-model="m.wechatHomeName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信堂食名称：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatTangshiName" v-model="m.wechatTangshiName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信我的名称：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatMyName" v-model="m.wechatMyName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信外卖名称：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatWaimaiName" v-model="m.wechatWaimaiName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">自动确认收货时间设置(秒)：</label>
                                <div class="col-md-6">
                                    <input type="number" class="form-control" name="autoConfirmTime" v-model="m.autoConfirmTime">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信自定义样式：</label>
                                <div class="col-md-6">
                                    <textarea class="form-control" rows="5" name="wechatCustomoStyle" v-model="m.wechatCustomoStyle"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">打印重新连接次数：</label>
                                <div class="col-md-6">
                                    <input type="number" class="form-control" name="reconnectTimes" v-model="m.reconnectTimes">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">打印重新连接间隔时间：</label>
                                <div class="col-md-6">
                                    <input type="number" class="form-control" name="reconnectSecond" v-model="m.reconnectSecond">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">工作日库存（量）：</label>
                                <div class="col-md-6">
                                    <input type="number" class="form-control" name="stockWorkingDay" v-model="m.stockWorkingDay">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">周末库存（量）：</label>
                                <div class="col-md-6">
                                    <input type="number" class="form-control" name="stockWeekend" v-model="m.stockWeekend">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">推荐餐包功能：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="recommendArticle" v-model="m.recommendArticle" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="recommendArticle" v-model="m.recommendArticle" value="0"> 关闭
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">推荐菜品功能：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="recommendCategory" v-model="m.recommendCategory" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="recommendCategory" v-model="m.recommendCategory" value="0"> 关闭
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">套餐出单方式：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="printType" v-model="m.printType" value="0"> 整单出单
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="printType" v-model="m.printType" value="1"> 分单出单
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">等位红包：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="waitRedEnvelope" v-model="m.waitRedEnvelope" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="waitRedEnvelope" v-model="m.waitRedEnvelope" value="0"> 关闭
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">等位红包每秒增加价：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="baseMoney" :value="m.baseMoney" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">等位红包上限价格：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="highMoney" :value="m.highMoney" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">geekpos菜品价格：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="geekPosPrice" required v-model="m.geekPosPrice" value="0"> 原价
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="geekPosPrice" v-model="m.geekPosPrice" value="1"> 粉丝价
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">biz地址：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="bizUrl" :value="m.bizUrl">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">饿了吗用户key：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="consumerKey" :value="m.consumerKey">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">饿了吗用户secret：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="consumerSecret" :value="m.consumerSecret">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">店铺切换时图片展现：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="switchMode" required v-model="m.switchMode" value="0">    大图
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="switchMode" v-model="m.switchMode" value="1"> 搜索图
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="switchMode" v-model="m.switchMode" value="2"> 双列展现
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">额外费用：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isAllowExtraPrice" v-model="m.isAllowExtraPrice" value="1">   开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isAllowExtraPrice" v-model="m.isAllowExtraPrice" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">开启第三方平台：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isOpenOutFood" v-model="m.isOpenOutFood" value="1">   开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isOpenOutFood" v-model="m.isOpenOutFood" value="0">   未开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">开启R+支付：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openRpay" v-model="m.openRpay" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openRpay" v-model="m.openRpay" value="0"> 未开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">开启支付宝支付：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="aliPay" v-model="m.aliPay" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="aliPay" v-model="m.aliPay" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">开启银联支付：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openUnionPay" v-model="m.openUnionPay" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openUnionPay" v-model="m.openUnionPay" value="0">  未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">开启美团闪惠支付：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openShanhuiPay" v-model="m.openShanhuiPay" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openShanhuiPay" v-model="m.openShanhuiPay" value="0">  未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">开启现金支付：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openMoneyPay" v-model="m.openMoneyPay" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openMoneyPay" v-model="m.openMoneyPay" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">开启会员支付：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="integralPay" v-model="m.integralPay" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="integralPay" v-model="m.integralPay" value="0">  未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">开启团购支付：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openGroupBuy" v-model="m.openGroupBuy" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openGroupBuy" v-model="m.openGroupBuy" value="0">  未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">开启代金券支付：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openCashCouponBuy" v-model="m.openCashCouponBuy" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openCashCouponBuy" v-model="m.openCashCouponBuy" value="0">  未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">买单后出总单（后付款模式）：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isPrintPayAfter" v-model="m.isPrintPayAfter" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isPrintPayAfter" v-model="m.isPrintPayAfter" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">开启评论自选照片：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isOptionalPhoto" v-model="m.isOptionalPhoto" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isOptionalPhoto" v-model="m.isOptionalPhoto" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否启用服务费：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isUseServicePrice" v-model="m.isUseServicePrice" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isUseServicePrice" v-model="m.isUseServicePrice" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">开启外带餐盒费：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isMealFee" v-model="m.isMealFee" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isMealFee" v-model="m.isMealFee" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">开启POS加菜：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isPosPlus" v-model="m.isPosPlus" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isPosPlus" v-model="m.isPosPlus" value="0"> 未开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">开启POS点单：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="posOpenTable" v-model="m.posOpenTable" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="posOpenTable" v-model="m.posOpenTable" value="0"> 未开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">开启POS账户充值：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openPosCharge" v-model="m.openPosCharge" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openPosCharge" v-model="m.openPosCharge" value="0"> 未开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">开启POS端催菜：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openPosReminder" v-model="m.openPosReminder" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openPosReminder" v-model="m.openPosReminder" value="0"> 未开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">进入页面是开启关注页面：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="intoShopSubscribe" v-model="m.intoShopSubscribe" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="intoShopSubscribe" v-model="m.intoShopSubscribe" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">品牌关注二维码：</label>
                                <div class="col-sm-6">
                                    <input type="hidden" class="form-control" required name="qrCodeBrand" v-model="m.qrCodeBrand">
                                    <img-file-upload  class="form-control" @success="uploadSuccessQrCode" @error="uploadError"></img-file-upload>
                                    <img :src="m.qrCodeBrand" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启关注才领取红包：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="allowAppraiseSubscribe" v-model="m.allowAppraiseSubscribe" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="allowAppraiseSubscribe" v-model="m.allowAppraiseSubscribe" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启安卓apk电视叫号：(该开关暂时废弃)</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openAndriodApk" v-model="m.openAndriodApk" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openAndriodApk" v-model="m.openAndriodApk" value="0"> 未开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">电视叫号模式：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="callTvType" v-model="m.callTvType" value="1"> 友盟
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="callTvType" v-model="m.callTvType" value="2"> 本地(lmx)
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启订单备注：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openOrderRemark" v-model="m.openOrderRemark" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openOrderRemark" v-model="m.openOrderRemark" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启https：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openHttps" v-model="m.openHttps" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openHttps" v-model="m.openHttps" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <!-- yz 2017-03-17-->
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启第三方接口：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openThirdInterface" v-model="m.openThirdInterface" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openThirdInterface" v-model="m.openThirdInterface" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group" v-if="m.openThirdInterface==1">
                                <label class="col-md-4 control-label">第三方接口对接appId：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="appid" :value="m.appid">
                                </div>
                            </div>


                               <!-- yz 2017-07-17-->
                          <div class="form-group">
                              <label class="col-md-4 control-label">是否开启品牌账户：</label>
                              <div class="col-md-6">
                                  <label class="radio-inline">
                                      <input type="radio" name="openBrandAccount" v-model="m.openBrandAccount" value="1"> 开启
                                  </label>
                                  <label class="radio-inline">
                                      <input type="radio" name="openBrandAccount" v-model="m.openBrandAccount" value="0"> 未开启
                                  </label>
                              </div>
                          </div>

                            <!-- xielc 2017-07-20-->
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启换桌：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="turntable" v-model="m.turntable" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="turntable" v-model="m.turntable" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启分红：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openBonus" v-model="m.openBonus" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openBonus" v-model="m.openBonus" value="0"> 未开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启pos折扣：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openPosDiscount" v-model="m.openPosDiscount" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openPosDiscount" v-model="m.openPosDiscount" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
`                            <div class="form-group">
                                <label class="col-md-4 control-label">微信消息模板：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="templateEdition" v-model="m.templateEdition" value="0"> 经典版
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="templateEdition" v-model="m.templateEdition" value="1"> 升级版
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">评价体系：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="appraiseEdition" v-model="m.appraiseEdition" value="0"> 经典版
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="appraiseEdition" v-model="m.appraiseEdition" value="1"> 升级版
                                    </label>
                                </div>
                            </div>
                            <div v-if="m.templateEdition==1" class="form-group">
                                <label class="col-md-4 control-label">是否推送短信</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="messageSwitch" v-model="m.messageSwitch" value="1">
                                        是
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="messageSwitch" v-model="m.messageSwitch" value="0">
                                        否
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启多人点餐：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openManyCustomerOrder" v-model="m.openManyCustomerOrder" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openManyCustomerOrder" v-model="m.openManyCustomerOrder" value="0"> 未开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启消费返利(1:1)：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="consumptionRebate" v-model="m.consumptionRebate" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="consumptionRebate" v-model="m.consumptionRebate" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启供应商：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isOpenScm" v-model="m.isOpenScm" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isOpenScm" v-model="m.isOpenScm" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启发票管理：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="receiptSwitch" v-model="m.receiptSwitch" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="receiptSwitch" v-model="m.receiptSwitch" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启电子发票：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openTicket" v-model="m.openTicket" value="1"> 阿里电子发票
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openTicket" v-model="m.openTicket" value="2"> 微信电子发票
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openTicket" v-model="m.openTicket" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启强制注册：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openCompulsoryRegister" v-model="m.openCompulsoryRegister" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openCompulsoryRegister" v-model="m.openCompulsoryRegister" value="0"> 未开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">是否开启强制充值：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openCompulsoryRecharge" v-model="m.openCompulsoryRecharge" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openCompulsoryRecharge" v-model="m.openCompulsoryRecharge" value="0"> 未开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">开启餐品预下单功能：</label>
                                <div class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="orderBefore" v-model="m.orderBefore" value="1"> 开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="orderBefore" v-model="m.orderBefore" value="0"> 未开启
                                    </label>
                                </div>
                            </div>
							<div class="form-group">
                                <label class="col-sm-4 control-label">是否开启菜品优惠券：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openProductCoupon" v-model="m.openProductCoupon" value="1">是
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openProductCoupon" v-model="m.openProductCoupon" value="0">否
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">是否启用小程序：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openSmallProgramPush" v-model="m.openSmallProgramPush" value="1">是
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openSmallProgramPush" v-model="m.openSmallProgramPush" value="0">否
                                    </label>
                                </div>
                            </div>
                            <div class="form-group" v-if="m.openSmallProgramPush == 1">
                                <label class="col-md-4 control-label">小程序链接：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="productCouponPushUrl" v-model="m.productCouponPushUrl">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">是否开启菜品库：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openArticleLibrary" v-model="m.openArticleLibrary" value="1">是
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openArticleLibrary" v-model="m.openArticleLibrary" value="0">否
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">是否开启新版权限：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openAutoPermission" v-model="m.openAutoPermission" value="1">是
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openAutoPermission" v-model="m.openAutoPermission" value="0">否
                                    </label>
                                </div>
                            </div>
                            <%--是否开启审核--%>
                            <%--<div class="form-group">--%>
                                <%--<label class="col-md-4 control-label">是否开启审核：</label>--%>
                                <%--<div class="col-md-6">--%>
                                    <%--<label class="radio-inline">--%>
                                        <%--<input type="radio" name="openAudit" v-model="m.openAudit" value="1"> 开启--%>
                                    <%--</label>--%>
                                    <%--<label class="radio-inline">--%>
                                        <%--<input type="radio" name="openAudit" v-model="m.openAudit" value="0"> 未开启--%>
                                    <%--</label>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<label class="col-md-4 control-label">礼品卷提醒方式：</label>--%>
                                <%--<div  class="col-md-6 radio-list checkbox">--%>
                                    <%--<label style="margin-left: 35px;">--%>
                                        <%--<input type="checkbox" name="wechatPushGiftCoupons" v-model="m.wechatPushGiftCoupons" value="1">--%>
                                        <%--&nbsp;&nbsp;微信推送--%>
                                    <%--</label>--%>
                                    <%--<label style="margin-left: 35px;">--%>
                                        <%--<input type="checkbox" name="smsPushGiftCoupons" v-model="m.smsPushGiftCoupons" value="1">--%>
                                        <%--&nbsp;&nbsp;短信推送--%>
                                    <%--</label>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        </div>
                        <input type="hidden" name="id" v-model="m.id"/>
                        <div class="text-center">
                            <input class="btn green" type="submit" value="保存"/>&nbsp;&nbsp;&nbsp;
                            <a class="btn default" @click="cancel">关闭</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>


    <div class="row form-div" v-if="showAliSetting">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">支付宝支付设置</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" action="{{m.id?'brandsetting/modify':'brandsetting/create'}}"
                          @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label>支付宝appId</label>
                                <input type="text" class="form-control" name="aliAppId" :value="m.aliAppId">
                            </div>
                            <div class="form-group">
                                <label>支付宝私钥</label>
                                 <textarea class="form-control" rows="5" name="aliPrivateKey"
                                           v-model="m.aliPrivateKey"></textarea>
                            </div>
                            <div class="form-group">
                                <label>支付宝公钥</label>
                                <textarea class="form-control" rows="5" name="aliPublicKey"
                                          v-model="m.aliPublicKey"></textarea>
                            </div>
                            <div class="form-group">
                                <label>合作伙伴身份（PID）</label>
                                <textarea class="form-control" rows="5" name="aliSellerId"
                                          v-model="m.aliSellerId"></textarea>
                            </div>
                            <div class="form-group">
                                <label>销售产品码</label>
                                <textarea class="form-control" rows="5" name="aliProductCode"
                                          v-model="m.aliProductCode"></textarea>
                            </div>
                        </div>
                        <input type="hidden" name="id" v-model="m.id"/>
                        <input class="btn green" type="submit" value="保存"/>
                        <a class="btn default" @click="closeAliSetting">关闭</a>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- yz 2017-07-19 计费 -->

    <%--<div class="row form-div" v-if="showBrandAccountSetting">--%>
        <%--<div class="col-md-offset-3 col-md-6">--%>
            <%--<div class="portlet light bordered">--%>
                <%--<div class="portlet-title">--%>
                    <%--<div class="caption">--%>
                        <%--<span class="caption-subject bold font-blue-hoki">品牌账户设置</span>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="portlet-body">--%>
                    <%--<form role="form" action="{{m.id?'accountsetting/modify':'accountsetting/create'}}"--%>
                          <%--@submit.prevent="saveBrandAccountSetting">--%>
                        <%--<div class="form-body">--%>
                            <%--<div class="form-group">--%>
                                <%--<label>品牌账户手机号</label>--%>
                                <%--<input type="text" class="form-control" name="telephone" v-model="m.telephone" placeholder="测试时先填13317182430">--%>
                            <%--</div>--%>

                            <%--<div class="form-group">--%>
                                <%--<label>新用户注册</label>--%>
								<%--<label>固定为1 代表选中</label>--%>
                                <%--<input type="text" class="form-control" name="openNewCustomerRegister" v-model="m.openNewCustomerRegister" placeholder="测试时先填1">--%>
                            <%--</div>--%>

                            <%--<div class="form-group">--%>
                                <%--<label>每个用户单价</label>--%>
                                <%--<input type="text" class="form-control" name="newCustomerValue" v-model="m.newCustomerValue" placeholder="测试时先填1">--%>
                            <%--</div>--%>

                            <%--<div class="form-group">--%>
                                <%--<label>短信发送</label>--%>
								<%--<label>固定为1 代表选中</label>--%>
                                <%--<input type="text" class="form-control" name="openSendSms" v-model="m.openSendSms" placeholder="测试时先填1">--%>
                            <%--</div>--%>

                            <%--<div class="form-group">--%>
                                <%--<label>短信单价</label>--%>
                                <%--<input type="text" class="form-control" name="sendSmsValue" v-model="m.sendSmsValue" placeholder="测试时先填0.1">--%>
                            <%--</div>--%>

                            <%--<div class="form-group">--%>
                                <%--<label>所有订单</label>--%>
								<%--<label>0:不选择 1.订单总额 2.实付金额</label>--%>
                                <%--<input type="text" class="form-control" name="openAllOrder" v-model="m.openAllOrder" placeholder="测试时先填1">--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<label>所有订单百分比</label>--%>
                                <%--<input type="text" class="form-control" name="allOrderValue" v-model="m.allOrderValue" placeholder="测试时先填3">--%>
                            <%--</div>--%>

                            <%--<div class="form-group">--%>
                                <%--<label>回头消费订单</label>--%>
								<%--<label>0:不选择 1.订单总额 2.实际支付金额 注意：和所有订单其中要有一个选择0</label>--%>
                                <%--<input type="text" class="form-control" name="openBackCustomerOrder" v-model="m.openBackCustomerOrder" placeholder="测试时先填0">--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<label>回头消费订单百分比</label>--%>
                                <%--<input type="text" class="form-control" name="backCustomerOrderValue" v-model="m.backCustomerOrderValue" placeholder="测试时先填3">--%>
                            <%--</div>--%>

                            <%--<div class="form-group">--%>
                                <%--<label>resto+外卖订单</label>--%>
                                <%--<input type="text" class="form-control" name="openOutFoodOrder" v-model="m.openOutFoodOrder" placeholder="测试时先填0">--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<label>resto+外卖订单百分比</label>--%>
                                <%--<input type="text" class="form-control" name="outFoodOrderValue" v-model="m.outFoodOrderValue" placeholder="测试时先填0">--%>
                            <%--</div>--%>

                            <%--<div class="form-group">--%>
                                <%--<label>第三方外卖订单</label>--%>
                                <%--<input type="text" class="form-control" name="openOutFoodOrder" v-model="m.openOutFoodOrder" placeholder="测试时先填0">--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<label>第三方外卖订单百分比</label>--%>
                                <%--<input type="text" class="form-control" name="outFoodOrderValue" v-model="m.outFoodOrderValue" placeholder="测试时先填0">--%>
                            <%--</div>--%>

                            <%--<div class="form-group">--%>
                                <%--<label>余额提醒</label>--%>
                                <%--<input type="text" class="form-control" name="remainAccount" v-model="m.remainAccount" placeholder="测试时先填100,0,-100">--%>
                            <%--</div>--%>

                        <%--</div>--%>
                        <%--<input type="hidden" name="id" v-model="m.id"/>--%>
                        <%--<input type="hidden" name="brandSettingId" v-model="m.brandSettingId"/>--%>

                        <%--<input class="btn green" type="submit" value="保存"/>--%>
                        <%--<a class="btn default" @click="closeAccountSetting">关闭</a>--%>
                    <%--</form>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>


	<div class="modal fade" tabindex="-1" role="dialog" v-if="showBrandAccountSetting" id="brandAccountSettingModel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" style="text-align: center">品牌账户设置</h4>
				</div>
				<div class="modal-body">
                    <form class="form-horizontal" @submit.prevent="saveBrandAccountSetting">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">扣费方式</h3>
                            </div>
                            <div class="panel-body">
                                <div class="form-group">
                                    <div class="col-md-offset-1 col-md-11">
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="openNewCustomerRegister" v-model="brandAccountSettings.openNewCustomerRegister" onclick="return false"> <strong>新用户注册</strong>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group" v-if="brandAccountSettings.openNewCustomerRegister">
                                    <label class="col-md-3 control-label">每个新用户</label>
                                    <div class="col-md-5">
                                        <div class="input-group">
                                            <input type="text" class="form-control" name="newCustomerValue" v-model="brandAccountSettings.newCustomerValue" pattern="^[0-9]+(.[0-9]{0,2})?$" required>
                                            <div class="input-group-addon">元</div>
                                        </div>
                                    </div>
                                </div>
                                <hr/>
                                <div class="form-group" style="margin-top: 10px">
                                    <div class="col-md-offset-1 col-md-11">
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="openSendSms" v-model="brandAccountSettings.openSendSms"  onclick="return false"> <strong>短信发送</strong>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group" v-if="brandAccountSettings.openSendSms">
                                    <label class="col-md-3 control-label">每条短信</label>
                                    <div class="col-md-5">
                                        <div class="input-group">
                                            <input type="text" class="form-control"  name="sendSmsValue" v-model="brandAccountSettings.sendSmsValue" pattern="^[0-9]+(.[0-9]{0,2})?$" required>
                                            <div class="input-group-addon">元</div>
                                        </div>
                                    </div>
                                </div>
                                <hr/>
                                <div class="form-group" style="margin-top: 10px">
                                    <div class="col-md-offset-1 col-md-11">
                                        <div style="margin-left: -20px">
                                                <label style="cursor: pointer">
                                                    <input type="radio" name="openAllOrder" v-model="brandAccountSettings.openAllOrder" v-bind:true-value="1" v-bind:false-value="0" value="1" @click="choseAllOrder"/>
                                                    <strong>所有订单</strong>
                                                </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group" v-if="brandAccountSettings.openAllOrder">
                                    <label class="col-md-3 control-label">所有订单</label>
                                    <div class="col-md-3">
                                        <select class="form-control" v-model="brandAccountSettings.openAllOrderType">
                                            <option value="1">订单总额</option>
                                            <option value="2">实付金额</option>
                                        </select>
                                    </div>
                                    <label class="col-md-1 control-label">的</label>
                                    <div class="col-md-3">
                                        <div class="input-group">
                                            <input type="number" class="form-control"  name="allOrderValue" v-model="brandAccountSettings.allOrderValue" min="1" max="100">
                                            <div class="input-group-addon">‰</div>
                                        </div>
                                    </div>
                                </div>
                                <hr/>
                                <div class="form-group" style="margin-top: 10px">
                                    <div class="col-md-offset-1 col-md-11">
                                        <div style="margin-left: -20px">
                                            <label style="cursor: pointer">
                                                <input type="radio" name="openBackCustomerOrder"  v-model="brandAccountSettings.openBackCustomerOrder" v-bind:true-value="1" v-bind:false-value="0" value="1" @click="choseBackCustomerOrder"/>
                                                <strong>回头用户消费订单</strong>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group" v-if="brandAccountSettings.openBackCustomerOrder">
                                    <label class="col-md-3 control-label">所有订单</label>
                                    <div class="col-md-3">
                                        <select class="form-control" v-model="brandAccountSettings.openBackCustomerOrderType">
                                            <option value="1">订单总额</option>
                                            <option value="2">实付金额</option>
                                        </select>
                                    </div>
                                    <label class="col-md-1 control-label">的</label>
                                    <div class="col-md-3">
                                        <div class="input-group">
                                            <input type="number" class="form-control"  name="backCustomerOrderValue" v-model="brandAccountSettings.backCustomerOrderValue"  min="1" max="100" required>
                                            <div class="input-group-addon">‰</div>
                                        </div>
                                    </div>
                                </div>
                                <hr/>
                                <div class="form-group" style="margin-top: 10px">
                                    <div class="col-md-offset-1 col-md-11">
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" v-model="brandAccountSettings.openOutFoodOrder" v-bind:true-value="1" v-bind:false-value="0"> <strong>Resto+外卖订单</strong>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group" v-if="brandAccountSettings.openOutFoodOrder">
                                    <label class="col-md-3 control-label">所有订单</label>
                                    <div class="col-md-3">
                                        <select class="form-control" v-model="brandAccountSettings.openOutFoodOrderType">
                                            <option value="1">订单总额</option>
                                            <option value="2">实付金额</option>
                                        </select>
                                    </div>
                                    <label class="col-md-1 control-label">的</label>
                                    <div class="col-md-3">
                                        <div class="input-group">
                                            <input type="number" class="form-control"  name="outFoodOrderValue" v-model="brandAccountSettings.outFoodOrderValue" min="1" max="100" required>
                                            <div class="input-group-addon">‰</div>
                                        </div>
                                    </div>
                                </div>
                                <hr/>
                                <div class="form-group" style="margin-top: 10px">
                                    <div class="col-md-offset-1 col-md-11">
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="openThirdFoodOrder" v-model="brandAccountSettings.openThirdFoodOrder" v-bind:true-value="1" v-bind:false-value="0"> <strong>第三方外卖订单</strong>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group" v-if="brandAccountSettings.openThirdFoodOrder">
                                    <label class="col-md-3 control-label">所有订单</label>
                                    <div class="col-md-3">
                                        <select class="form-control" v-model="brandAccountSettings.openThirdFoodOrderType">
                                            <option value="1">订单总额</option>
                                            <option value="2">实付金额</option>
                                        </select>
                                    </div>
                                    <label class="col-md-1 control-label">的</label>
                                    <div class="col-md-3">
                                        <div class="input-group">
                                            <input type="number" class="form-control"  name="thirdFoodOrderValue" v-model="brandAccountSettings.thirdFoodOrderValue" min="1" max="100" required>
                                            <div class="input-group-addon">‰</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">余额提醒</h3>
                            </div>
                            <div class="panel-body">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"><strong>余额提醒</strong></label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" name="remainAccount" v-model="brandAccountSettings.remainAccount" v-if="brandAccountSettings.remainAccount"  placeholder="多个提醒额度，请用 逗号 隔开">
                                    </div>
                                </div>
                            </div>
                        </div>

						<div class="panel panel-default">
							<div class="panel-heading">
								<h3 class="panel-title">手机号码</h3>
							</div>
							<div class="panel-body">
								<div class="form-group">
									<label class="col-sm-2 control-label"><strong>手机号码</strong></label>
									<div class="col-sm-10">
										<input type="text" class="form-control" name="telephone" v-model="brandAccountSettings.telephone" placeholder="多个手机号用,隔开">
									</div>
								</div>
							</div>
						</div>
                        <input class="btn green" type="submit" value="保存"/>
                        <a class="btn default" @click="closeAccountSetting">关闭</a>
                    </form>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->


	<div class="modal fade" tabindex="-1" role="dialog" v-if="showBrandTicketSetting" id="brandTicketSettingModel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" style="text-align: center">电子发票配置</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<label for="name" class="padding-name">公钥</label>
						<input type="text" class="form-control" name="appkey" v-model="m.appkey" placeholder="阿里发票必填，微信发票不必填，其它选项都必填" >
					</div>
					<div class="form-group">
						<label for="name" class="padding-name">私钥</label>
						<input type="text" class="form-control" name="appsecret" v-model="m.appsecret" placeholder="阿里发票必填，微信发票不必填，其它选项都必填">
					</div>
					<div class="form-group">
						<label for="name" class="padding-name">开票方地址</label>
						<input type="text" class="form-control" name="address" v-model="m.address" placeholder="请输入开票方地址">
					</div>
					<div class="form-group">
						<label for="name" class="padding-name">开票方名称</label>
						<input type="text" class="form-control" v-model="m.name" placeholder="请输入开票方名称">
					</div>
					<div class="form-group">
						<label for="name" class="padding-name">开票人</label>
						<input type="text" class="form-control" v-model="m.operator" placeholder="请输入开票人">
					</div>
					<div class="form-group">
						<label for="name" class="padding-name">开票方税务登记证号</label>
						<input type="text" class="form-control" v-model="m.payeeRegisterNo" placeholder="请输入开票方税务登记证号">
					</div>
                    <div class="form-group">
                        <label for="name" class="padding-name">开户银行</label>
                        <input type="text" class="form-control" v-model="m.bank" placeholder="微信发票必填，阿里发票不必填，其它选项都必填">
                    </div>
                    <div class="form-group">
                        <label for="name" class="padding-name">开户银行账号</label>
                        <input type="text" class="form-control" v-model="m.bankNum" placeholder="微信发票必填，阿里发票不必填，其它选项都必填">
                    </div>
                    <div class="form-group">
                        <label for="name" class="padding-name">微信支付商户号</label>
                        <input type="text" class="form-control" v-model="m.wechatPayNum" placeholder="微信发票必填，阿里发票不必填，其它选项都必填">
                    </div>
					<div class="form-group">
						<label for="name" class="padding-name">复核人</label>
						<input type="text" class="form-control" v-model="m.payeeChecker" placeholder="请输入复核人">
					</div>
					<div class="form-group">
						<label for="name" class="padding-name">收款人</label>
						<input type="text" class="form-control" v-model="m.payeeReceiver" placeholder="请输入收款人">
					</div>
					<div class="form-group">
						<label for="name" class="padding-name">收款方电话</label>
						<input type="text" class="form-control" v-model="m.payeePhone" placeholder="请输入收款方电话">
					</div>
                    <div class="form-group">
                        <label for="name" class="padding-name">邮箱账号(只支持腾讯或网易)</label>
                        <input type="text" class="form-control" v-model="m.email" placeholder="阿里发票必填，微信发票不必填，其它选项都必填">
                    </div>
                    <div class="form-group">
                        <label for="name" class="padding-name">邮箱授权码</label>
                        <input type="text" class="form-control" v-model="m.authorizationKey" placeholder="阿里发票必填，微信发票不必填，其它选项都必填">
                    </div>
                    <div class="form-group">
                        <label for="name" class="padding-name">邮箱授权码</label>
                        <input type="text" class="form-control" v-model="m.authorizationKey" placeholder="阿里发票必填，微信发票不必填，其它选项都必填">
                    </div>
					<div class="flex-container">
						<button type="button" class="btn btn-info" @click="saveTicketSetting">保存</button>
						<button type="button" class="btn btn-default" @click="closeTicketSettingModel">取消</button>
					</div>					
				</div>	
			</div>
		</div>
	</div>

    <div class="row form-div" v-if="showChargeSetting">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">微信充值账户设置</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" action="{{m.wechatChargeConfig.id?'wechatChargeConfig/modify':'wechatChargeConfig/create'}}"
                          @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label>App Id：</label>
                                <input type="text" class="form-control"  name="wechatChargeConfig.appid"
                                       v-model="m.wechatChargeConfig.appid">
                            </div>
                            <div class="form-group">
                                <label>App Secret：</label>
                                <input type="text" class="form-control"  name="wechatChargeConfig.appsecret"
                                       v-model="m.wechatChargeConfig.appsecret">
                            </div>
                            <div class="form-group">
                                <label>支付ID：</label>
                                <input type="text" class="form-control" required="true"  name="wechatChargeConfig.mchid"
                                       v-model="m.wechatChargeConfig.mchid">
                            </div>
                            <div class="form-group">
                                <label>支付秘钥：</label>
                                <input type="text" class="form-control"  name="wechatChargeConfig.mchkey"
                                       v-model="m.wechatChargeConfig.mchkey">
                            </div>
                            <div class="form-group">
                                <label>payChartPath：</label>
                                <input type="hidden" name="wechatChargeConfig.payCertPath" v-model="m.wechatChargeConfig.payCertPath">
                                <cert-file-upload class="form-control" @success="uploadSuccess"
                                                   @error="uploadError"></cert-file-upload>
                            </div>
                            <div class="form-group">
                                <!-- 							<label>是否开启店铺</label>  -->
                                <!-- 							<input type="text" class="form-control" name="remark" v-model="m.remark"> -->
                                <div label for="isOpen" class="control-label">关联母账户</div>
                                <div>
                                    <select class="form-control" name="wechatChargeConfig.wxServerId" :value="m.wechatChargeConfig.wxServerId">
                                        <option value="">无</option>
                                        <option v-for="wxServer in  wxServers" :value="wxServer.id">
                                            {{wxServer.name}}
                                        </option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="control-label">公众号是否与服务商所在公众号一致</div>
                                <label>
                                    <input type="radio" name="wechatChargeConfig.isSub" v-model="m.wechatChargeConfig.isSub" value="0">
                                    不一致
                                </label>
                                <label>
                                    <input type="radio" name="wechatChargeConfig.isSub"
                                           v-model="m.wechatChargeConfig.isSub" value="1">
                                    一致
                                </label>
                            </div>
                        </div>
                        <input type="hidden" name="id" v-model="m.id"/>
                        <input type="hidden" name="wechatChargeConfig.id" v-model="m.wechatChargeConfig.id"/>
                        <input class="btn green" type="submit" value="保存"/>
                        <a class="btn default" @click="closeChargeSetting">关闭</a>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row form-div" v-if="showThirdMoney">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">第三方账户导入</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form enctype="multipart/form-data" id="batchUpload"  action="thirdUser/upload" class="form-horizontal">
                        <button class="btn btn-success" @click="uploadEventBtn" style="margin-bottom: 10px;" type="button" >选择文件</button>
                        <input type="file" name="file"  style="width:0px;height:0px;" id="uploadEventFile">
                        <input type="hidden" name="brandSettingId"  value="{{m.id}}">
                        <input id="uploadEventPath"  disabled="disabled"  type="text" placeholder="请选择excel表" style="border: 1px solid #e6e6e6; height: 26px;width: 200px;margin-bottom: 10px;" >

                        <span style="margin-top: 10px;">
                            <button type="button" class="btn btn-success"  @click="uploadBtn" >上传</button>
                            <a class="btn default" @click="closeThirdMoney">关闭</a>
                        </span>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="table-div">
        <div class="clearfix"></div>
        <div class="table-filter">&nbsp;</div>
        <div class="table-body">
            <table id="brand" class="table table-striped table-hover table-bordered "></table>
        </div>




    </div>
</div>


<script>

    var currentBrandId;
    Vue.component('cert-file-upload',{
        mixins:[fileUploadMix],
        data:function(){
            return {
                types:[".p12"],
                postUrl:"brandsetting/uploadFile"
            }
        }
    });

    (function () {
        var cid = "#control";
        var $table = $("#brand");
        var tb = $table.DataTable({
            ordering: false,
            ajax: {
                url: "brandsetting/list_all",
                dataSrc: ""
            },
            columns: [
                {
                    title: "品牌名称",
                    data: "brandName",
                },

                {
                    title: "操作",
                    data: "id",
                    createdCell: function (td, tdData, rowData, row) {
                        var operator = [];
                        <s:hasPermission name="brandsetting/edit">
                        operator.push(C.createEditBtn(rowData).html("编辑基本配置"));
                        operator.push(C.createChargeBtn(rowData).html("微信充值账户设置"));
                        operator.push(C.createThirdMoneyBtn(rowData).html("导入第三方会员储值"));
                        </s:hasPermission>

                        if (rowData.isOpenOutFood == 1) {
                            operator.push(C.createBtn(rowData).html("第三方平台设置"));
                        }

                        if (rowData.aliPay == 1) {
                            operator.push(C.createAliBtn(rowData).html("支付宝支付设置"));
                        }
                        if(rowData.openBrandAccount==1){
                            var button = $("<button class='btn btn-xs btn-primary' data-toggle='modal' data-target='#brandAccountSettingModel' >品牌账户设置</button>");
                                button.click(function(){
                                    vueObj.accountSetting(rowData.id)
                                });
                            operator.push(button)
                        }
						if (rowData.openTicket == 1 || rowData.openTicket==2) {
							var button = $("<button class='btn btn-xs btn-primary' data-toggle='modal' data-target='#brandTicketSettingModel' >电子发票配置</button>");
                                button.click(function(){                                	
                            		vueObj.ticketSetting(rowData.brandId)
                            		console.log(JSON.stringify(rowData));
                                });
                            operator.push(button)
                          	//operator.push(C.createAliBtn(rowData).html("电子发票配置"));
                        }
												
                        $(td).html(operator);
                    }
                }],
        });

        var C = new Controller(cid, tb);
        var vueObj = new Vue({
            el: "#control",
            mixins: [C.formVueMix],
            data: {
                platforms: [],
                showPlatform: false,
                showAliSetting:false,
                showChargeSetting:false,
                showThirdMoney:false,
                showBrandAccountSetting:false,
                showBrandTicketSetting:false,	//电子发票
                wxServers:[],
				//yz 2017/08/05 jifei
                choiceOrder:1,
                brandAccountSettings:{
                    openNewCustomerRegister : 1,                  //  是否开启      新用户注册
                    newCustomerValue : 1,                               //  每个新用户单价
                    openSendSms : 1,                                       //  是否开启      短信发送
                    sendSmsValue : 0.1,                                    //  每条短信的单价
                    openAllOrder : 1,                                        //  是否使用   【所有订单】  计费模式            (和  【回头用户消费订单】 模式  二选一)
                    openAllOrderType : 1,                                //  所有订单      计费模式    的计费类型  （1：订单总额，2：实付金额）
                    allOrderValue : 3,                                        //  所有订单      下计费模式 的抽成比例
                    openBackCustomerOrder : 0,                     //  是否使用  【回头用户消费订单】  计费模式    (和  【所有订单】 模式  二选一）
                    openBackCustomerOrderType : 0,             //  回头用户消费订单     计费模式    的计费类型  （1：订单总额，2：实付金额）
                    backCustomerOrderValue : 0,                    //  回头用户消费订单     下计费模式 的抽成比例
                    openOutFoodOrder : 0,                             //   是否开启     Resto+外卖订单
                    openOutFoodOrderType : 1,                     //   Resto+外卖订单      计费模式的计费类型  （1：订单总额，2：实付金额）
                    outFoodOrderValue : 3,                             //   Resto+外卖订单     下计费模式 的抽成比例
                    openThirdFoodOrder : 0,                           //   是否开启     第三方外卖订单
                    openThirdFoodOrderType : 1,                   //   第三方外卖订单     计费模式    的计费类型  （1：订单总额，2：实付金额）
                    thirdFoodOrderValue : 3,                            //   第三方外卖订单     计费模式 的抽成比例
					telephone:'',
					brandId:null
                }
            },
			methods: {
				closeTicketSettingModel:function(){					
					this.showBrandTicketSetting = false;
					$("#brandTicketSettingModel").modal('toggle');
				},
				saveTicketSetting:function(){
					var that = this;
					that.m.brandId = this.brandId;
					console.log(JSON.stringify(that.m));
					$.ajax({
                        type: "post",
                        url: "electronicticketconfig/modify",
                        data: that.m,
                        success: function (result) {
                            if (result.success) {
                                C.simpleMsg("保存成功");
                                that.showBrandTicketSetting = false;
								$("#brandTicketSettingModel").modal('toggle');
                            } else {
                                C.errorMsg(result.message);
                            }
                        },
                        error: function (xhr, msg, e) {
                            var errorText = xhr.status + " " + xhr.statusText + ":" + action;
                            C.errorMsg(errorText);
                        }
                    });
				},
                cleanBrandSetting:function (id) {
                    $.ajax({
                        type: "post",
                        url: "brandsetting/cleanBrandSetting",
                        data: {id: id},
                        success: function (result) {
                            if (result.success) {
                                C.simpleMsg("清除缓存成功");
                            } else {
                                C.errorMsg(result.message);
                            }
                        },
                        error: function (xhr, msg, e) {
                            var errorText = xhr.status + " " + xhr.statusText + ":" + action;
                            C.errorMsg(errorText);
                        }
                    });
                },
                cancelPlatform: function () {
                    this.showPlatform = false;
                },
                closeAliSetting:function(){
                    this.showAliSetting = false;
                },
                closeChargeSetting:function(){
                    this.showChargeSetting = false;
                },
                closeThirdMoney:function(){
                    this.showThirdMoney = false;
                },
                closeAccountSetting:function(){
                    $("#brandAccountSettingModel").modal('toggle');
                    this.showBrandAccountSetting = false;
                },

                closeForm:function(){
                    this.m={};
                    this.showform = false;
                    this.showAliSetting = false;
                    this.showChargeSetting=false;
                    this.showThirdMoney=false;
                },
                savePlatform:function(e){
                    var that = this;
                    var formDom = e.target;
                    var data = {
                        platform:[],
                    }

                    var temp =$(":checkbox[name='shopType']");
                    for(var i = 0;i < temp.length;i++){
                        if($(temp[i]).prop("checked")){
                            data.platform.push($(temp[i]).prop("value"));
                        }
                    }
                    $.ajax({
                        type: "post",
                        url: "brandsetting/savePlatform",
                        data: {platform:data.platform.toString(),id: currentBrandId},
                        success: function (result) {
                            if (result.success) {
                                that.showPlatform = false;
                                that.m = {};
                                C.simpleMsg("保存成功");
                                tb.ajax.reload(null, false);
                            } else {
                                C.errorMsg(result.message);
                            }
                        },
                        error: function (xhr, msg, e) {
                            var errorText = xhr.status + " " + xhr.statusText + ":" + action;
                            C.errorMsg(errorText);
                        }
                    });
                },
                aliPay:function(model){
                    var that = this;
                    that.m= model;
                    that.showAliSetting = true;
                },
                ticketSetting:function(ticketSettingId){
                	console.log(JSON.stringify(this.m)); 
                
                	var that = this;
                	that.brandId = ticketSettingId;
                    $.ajax({
                        type:"post",
                        url:"electronicticketconfig/selectByBrandId",
                        async:false,
                        data:{
                            brandId:that.brandId
                        },
                        success :function(result){
                        	if(result.data != null){                        		
                    			//that.electronicTicket = result.data;
                    			that.initTicketSetting(result.data);                     		
                        		console.log(JSON.stringify(that.m));
                        	}                         
                        }
                    })
                	this.showBrandTicketSetting = true;
                },
                initTicketSetting:function(data){
                	this.m = {
						id:data.id,
						brandId:data.brandId,
						appkey:data.appkey,
						appsecret:data.appsecret,
						address:data.address,
						name:data.name,
						operator:data.operator,
						payeeRegisterNo:data.payeeRegisterNo,
                        bank:data.bank,
                        bankNum:data.bankNum,
                        wechatPayNum:data.wechatPayNum,
						payeeChecker:data.payeeChecker,
						payeeReceiver:data.payeeReceiver,
						payeePhone:data.payeePhone,
                        email:data.email,
                        authorizationKey:data.authorizationKey
					}
                },
                //点击品牌账户设置的方法
                accountSetting:function(brandSettingId){
                    console.log("之前："+brandSettingId);
                    var that = this;
                    $.ajax({
                        type:"post",
                        url:"accountsetting/getOneBySettingId",
                        data:{
                            brandSettingId:brandSettingId
                        },
                        success :function(result){
                            console.log(result)
                            console.log(JSON.stringify(result))
                            console.log(null!=result.data);
                            if(null!=result.data){
                                that.initBrandAccountSettings(result.data);
//                                that.m.openAllOrder = result .data.openAllOrder ? true : false;
                            }
                        }

                    })
                    console.log("之后："+brandSettingId)
                    that.m.brandSettingId = brandSettingId;
                    that.showBrandAccountSetting = true;
                },
                initBrandAccountSettings : function (data) {
                    this.brandAccountSettings = {
                        id : data.id,
                        brandSettingId : data.brandSettingId,
                        openNewCustomerRegister : data.openNewCustomerRegister || 1,
                        newCustomerValue : data.newCustomerValue || 1,
                        openSendSms : data.openSendSms || 1,
                        sendSmsValue : data.sendSmsValue || 0.1,
                        openAllOrder : data.openAllOrder > 0 ? 1: 0,
                        openAllOrderType : data.openAllOrder || 1,
                        allOrderValue : data.allOrderValue || 3,
                        openBackCustomerOrder : data.openBackCustomerOrder > 0 ? 1: 0,
                        openBackCustomerOrderType : data.openBackCustomerOrder || 1,
                        backCustomerOrderValue : data.backCustomerOrderValue || 0,
                        openOutFoodOrder :  data.openOutFoodOrder > 0 ? 1: 0,
                        openOutFoodOrderType : data.openOutFoodOrder || 1,
                        outFoodOrderValue : data.outFoodOrderValue || 3,
                        openThirdFoodOrder : data.openThirdFoodOrder > 0 ? 1: 0,
                        openThirdFoodOrderType : data.openThirdFoodOrder || 1,
                        thirdFoodOrderValue : data.thirdFoodOrderValue || 3,
                        remainAccount : data.remainAccount || "500,100,0,-100,-500",
						telephone:data.telephone
                    };
                },
                saveBrandAccountSetting:function(){
                    if(this.brandAccountSettings.newCustomerValue <= 0){
                        C.errorMsg("新用户注册抽成 金额不能小于 0");
                        return ;
                    }
                    if(this.brandAccountSettings.sendSmsValue <= 0){
                        C.errorMsg("发送短信 金额不能小于 0");
                        return ;
                    }
                    if(this.brandAccountSettings.openAllOrder){
                        this.brandAccountSettings.openAllOrder = this.brandAccountSettings.openAllOrderType;
                    }
                    if(this.brandAccountSettings.openBackCustomerOrder){
                        this.brandAccountSettings.openBackCustomerOrder = this.brandAccountSettings.openBackCustomerOrderType;
                    }
                    if(this.brandAccountSettings.openOutFoodOrder){
                        this.brandAccountSettings.openOutFoodOrder = this.brandAccountSettings.openOutFoodOrderType;
                    }else{
                        this.brandAccountSettings.openOutFoodOrderType = null;
                        this.brandAccountSettings.outFoodOrderValue = null;
                    }
                    if(this.brandAccountSettings.openThirdFoodOrder){
                        this.brandAccountSettings.openThirdFoodOrder = this.brandAccountSettings.openThirdFoodOrderType;
                    }else{
                        this.brandAccountSettings.openThirdFoodOrderType = null;
                        this.brandAccountSettings.thirdFoodOrderValue = null;
                    }
                    console.log(this.brandAccountSettings);
                    var that = this;
                    $.ajax({
                        type:"post",
                        url: "accountsetting/modify",
                        data : this.brandAccountSettings,
                        success :function(result){
                            console.log(result);
                            tb.ajax.reload();
                            $("#brandAccountSettingModel").modal('toggle');
                            that.showBrandAccountSetting =false;
                            C.simpleMsg("修改成功");
                        }
                    });
                },
                choseAllOrder : function () {
                    if(!this.brandAccountSettings.openAllOrder){
                        this.brandAccountSettings.openAllOrder = 1;
                        this.brandAccountSettings.openAllOrderType = 1;
                        this.brandAccountSettings.allOrderValue = 3;

                        this.brandAccountSettings.openBackCustomerOrder = 0 ;
                        this.brandAccountSettings.openBackCustomerOrderType = null ;
                        this.brandAccountSettings.backCustomerOrderValue = null ;
                    }
                },
                choseBackCustomerOrder : function () {
                    if(!this.brandAccountSettings.openBackCustomerOrder){
                        this.brandAccountSettings.openBackCustomerOrder = 1 ;
                        this.brandAccountSettings.openBackCustomerOrderType = 1 ;
                        this.brandAccountSettings.backCustomerOrderValue = 3 ;

                        this.brandAccountSettings.openAllOrder = 0;
                        this.brandAccountSettings.openAllOrderType = null;
                        this.brandAccountSettings.allOrderValue = null;
                    }
                },
                chargeSetting:function(model){
                    var that = this;
                    that.m= model;
                    that.showChargeSetting = true;
                },
                thirdMoney:function(model){
                    var that = this;
                    that.m= model;
                    that.showThirdMoney = true;
                },
                uploadEventBtn:function(){
                    $("#uploadEventFile").click();
                    $("#uploadEventFile").bind("change",function(){
                        $("#uploadEventPath").attr("value",$("#uploadEventFile").val());
                    });
                },
                uploadBtn:function(){
                    var that = this;
                    var uploadEventFile = $("#uploadEventFile").val();
                    if(uploadEventFile == ''){
                        alert("请选择excel,再上传");
                    }else if(uploadEventFile.lastIndexOf(".xls")<0){//可判断以.xls和.xlsx结尾的excel
                        alert("只能上传Excel文件");
                    }else{
                        var formData = new FormData($('#batchUpload')[0]);
//                        var formData = $("#batchUpload").serialize();
//                        alert(formData);
                        $.ajax({
                            url : "thirdUser/upload",
                            type:"post",
                            data : formData,
                            success : function(result) {
                                C.simpleMsg("上传成功");
                                that.showThirdMoney = false;
                            },
                            error : function() {
                                alert( "excel上传失败");
                            },
                            cache : false,
                            contentType : false,
                            processData : false
                        });
                    };
                },
                uploadSuccess: function (url) {
                    $("[name='payCertPath']").val(url).trigger("change");
                    C.simpleMsg("上传成功");
                },
                uploadSuccessQrCode: function (url) {
                    $("[name='qrCodeBrand']").val(url).trigger("change");
                    C.simpleMsg("上传成功");
                },
                uploadError: function (msg) {
                    C.errorMsg(msg);
                },
                platform: function (model) {
                    var that = this;
                    that.showPlatform = true;
                    currentBrandId = model.id;
                    Vue.nextTick(function () {
                        var $shop = $("#shopList");
                        var shop = $shop.DataTable({
                            "lengthMenu": [[-1, 5, 10, 15], ["全部", 5, 10, 15]],
                            "scrollY": "300px",
                            ajax: {
                                url: "platform/list_data",
                                data: function(d){
                                	d.brandId = currentBrandId;
                                },
                                dataSrc: "data"
                            },

                            deferRender: true,
                            ordering: false,

                            columns: [
                                {
                                    title: "全选 <input type='checkbox' style='width:20px;height:20px' id='checkShop'   />",
                                    data: "id",
                                    width: '10%',
                                    createdCell: function (td, tdData, rowData) {
                                    	if(rowData.platformId != null && rowData.platformId !=""){
                                    		var platformIds = rowData.platformId.split(",");
                                        	for(var i in platformIds){
                                        		if(tdData == platformIds[i]){
                                        			$(td).html("<input type='checkbox' checked='true' style='width:20px;height:20px' name='shopType'  value=\" " + tdData + "\"  />");
                                        			break;
                                        		}else{
                                        			$(td).html("<input type='checkbox' style='width:20px;height:20px' name='shopType'  value=\" " + tdData + "\"  />");
                                        		}
                                        	}
                                    	}else{
                                        	$(td).html("<input type='checkbox' style='width:20px;height:20px' name='shopType'  value=\" " + tdData + "\"  />");
                                    	}
                                   	},
                                },
                                {
                                    title: "第三方平台名称",
                                    data: "name",
                                    s_filter: true,
                                }
                            ],
                            initComplete: function () {
                                $("#checkShop").change(function () {

                                    if ($("#checkShop").prop("checked")) {
                                        $("input[name='shopType']").prop("checked", true);

                                    } else {
                                        console.log("-----");
                                        $("input[name='shopType']").prop("checked", false);
                                    }
                                });
                            }
                        });
                    })
                },

            }, created: function () {
                var that = this;
                $.post("platform/list_all", null, function (data) {
                    that.platforms = data;
                });
                $.post("wxServer/list_all", null, function (data) {
                    that.wxServers = data;
                });
            }
        });

        C.vue = vueObj;
    }());

</script>
