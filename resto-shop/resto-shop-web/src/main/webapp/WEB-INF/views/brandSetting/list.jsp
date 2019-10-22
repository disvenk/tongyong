<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style>
    .tab,.headerTitle {
        height: 50px;
        border-bottom: 1px solid #eee;
    }
    .headerTitle span:first-child {
        font-size: 14px;
        line-height: 50px;
    }
    .headerTitle span:last-child {
        font-size: 18px;
        font-weight: 700;
        line-height: 50px;
    }
    .tab span {
        padding:0 10px;
        display: inline-block;
        line-height: 50px;
        /*color: #0b94ea;*/
        /*border-bottom: 2px solid #0b94ea;*/
    }
    .active1 {
        color: #0b94ea;
        border-bottom: 2px solid #0b94ea;
    }
    .container {
        position:absolute;
        width: 96%;
        height:500px;
        overflow-y :auto
    }
    .fileinput-con {
        margin-bottom: 10px;
    }
    .fileinput-button {
        position: relative;
        display: inline-block;
        overflow: hidden;
        background-color: #0b94ea;
        border-radius: 10px;
    }

    .fileinput-button input{
        position: absolute;
        left: 0px;
        top: 0px;
        opacity: 0;
        -ms-filter: 'alpha(opacity=0)';
    }
    .colorHidden {
        display: none;
    }
    .selectStyle span {
        display: inline-block;
        padding: 8px 20px;
        border: 1px solid #4d6b8a;
    }
    .selectStyle .active2 {
        border-color: #0b94ea;
        color: #0b94ea
    }
</style>
<div id="control" class="row">
    <%--<div class="col-md-offset-3 col-md-6">--%>
    <div class="col-md-12">
        <div class="portlet light bordered" style="height: 700px;">
            <div class="headerTitle">
                <span>品牌设置</span>／
                <span>品牌参数设置</span>
            </div>
            <div class="tab">
                <span @click="tabChange(true)" :class="tabStatus ? 'active1' : ''">基础设置</span>
                <span @click="tabChange(false)" :class="!tabStatus ? 'active1' : ''">功能设置</span>
            </div>
            <div class="portlet-body">
                <form role="form" class="form-horizontal" action="{{m.id?'brandsetting/modify':'brandsetting/create'}}" @submit.prevent="save">
                    <div class="form-body container">
                        <div v-if="tabStatus">
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信欢迎标题：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatWelcomeTitle"
                                           v-model="m.wechatWelcomeTitle">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信欢迎地址：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatWelcomeUrl"
                                           v-model="m.wechatWelcomeUrl">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信欢迎图片：</label>
                                <div class="col-md-6">
                                    <input type="hidden" name="wechatWelcomeImg" v-model="m.wechatWelcomeImg">
                                    <div class="fileinput-con">
                                        <span class="btn btn-success fileinput-button">
                                            <span>上传</span>
                                            <img-file-upload class="form-control" @success="uploadSuccess"
                                                             @error="uploadError"></img-file-upload>
                                        </span>
                                    </div>
                                    <img v-if="m.wechatWelcomeImg" :src="m.wechatWelcomeImg"
                                         onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px"
                                         class="img-rounded">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">新用户注册提醒标题：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="customerRegisterTitle"
                                           v-model="m.customerRegisterTitle">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">领取会员卡地址：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="memberCardUrl"
                                           v-model="m.memberCardUrl">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信欢迎文本：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatWelcomeContent"
                                           v-model="m.wechatWelcomeContent">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信品牌名名称：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatBrandName" v-model="m.wechatBrandName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信堂食名称：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatTangshiName"
                                           v-model="m.wechatTangshiName">
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
                                    <input type="text" class="form-control" name="wechatWaimaiName"
                                           v-model="m.wechatWaimaiName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">微信粉丝圈名称：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="wechatHomeName" v-model="m.wechatHomeName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">品牌标语：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="slogan" v-model="m.slogan">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">等位提示：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="queueNotice" v-model="m.queueNotice">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">余额不足多少时提醒用户充值:</label>
                                <div class="col-md-6">
                                <input type="text" class="form-control" name="balanceReminder" :value="m.balanceReminder" >
                                </div>
                            </div>
                            <%--<div class="form-group">--%>
                                <%--<label class="col-md-4 control-label">loading页面的文字颜色：</label>--%>
                                <%--<div class="col-md-6">--%>
                                    <%--<input type="text" class="form-control color-mini" name="loadingTextColor"--%>
                                           <%--data-position="bottom left" v-model="m.loadingTextColor">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group ">--%>
                                <%--<label class="col-md-5 control-label">oading页面的文字颜色：</label>--%>
                                <%--<div class="col-md-2">--%>
                                    <%--<input type="text" class="form-control color-mini" name="loadingTextColor"--%>
                                           <%--data-position="bottom left" v-model="m.loadingTextColor">--%>
                                <%--</div>--%>
                                <%--<div class="col-md-5">--%>
                                    <%--<span class="btn dark" @click="changeColor('#000000')">黑</span>--%>
                                    <%--<span class="btn btn-default" @click="changeColor('#ffffff')">白</span>--%>
                                <%--</div>--%>
                            <%--</div>--%>

                            <div class="form-group">
                                <label class="col-md-4 control-label">loading页面的logo图：</label>
                                <div class="col-md-6">
                                    <input type="hidden" name="loadingLogo" v-model="m.loadingLogo">
                                    <div class="fileinput-con">
                                        <span class="btn btn-success fileinput-button">
                                            <span>上传</span>
                                            <img-file-upload class="form-control" @success="uploadSuccessLogo"
                                                             @error="uploadError"></img-file-upload>
                                        </span>
                                    </div>
                                    <img v-if="m.loadingLogo" :src="m.loadingLogo"
                                         onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px"
                                         class="img-rounded">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">loading页面的背景图片：</label>
                                <div class="col-md-6">
                                    <input type="hidden" name="loadingBackground" v-model="m.loadingBackground">
                                    <div class="fileinput-con">
                                        <span class="btn btn-success fileinput-button">
                                            <span>上传</span>
                                            <img-file-upload class="form-control" @success="uploadSuccessBackground"
                                                             @error="uploadError"></img-file-upload>
                                        </span>
                                    </div>
                                    <img v-if="m.loadingBackground" :src="m.loadingBackground"
                                         onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px"
                                         class="img-rounded">
                                </div>
                            </div>
                        </div>
                        <div v-else>
                            <div class="form-group">
                                <div class="col-md-4 control-label">是否红包弹窗：</div>
                                <div class="col-md-6 selectStyle">
                                    <span @click="select($event,1)" data-name="autoAlertAppraise" :class="m.autoAlertAppraise ? 'active2' : ''">是</span>
                                    <span @click="select($event,0)" data-name="autoAlertAppraise" :class="!m.autoAlertAppraise ? 'active2' : ''">否</span>
                                    <input type="hidden" name="autoAlertAppraise" v-model="m.autoAlertAppraise">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">红包Logo：</label>
                                <div class="col-md-6">
                                    <input type="hidden" name="redPackageLogo" v-model="m.redPackageLogo">
                                    <div class="fileinput-con">
                                        <span class="btn btn-success fileinput-button">
                                            <span>上传</span>
                                            <img-file-upload class="form-control" @success="setRedPackage"
                                                          @error="uploadError"></img-file-upload>
                                        </span>
                                    </div>
                                    <img v-if="m.redPackageLogo" :src="m.redPackageLogo"
                                         onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px"
                                         class="img-rounded">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-4 control-label">是否启动评论红包提醒：</div>
                                <div class="col-md-6 selectStyle">
                                    <span @click="select($event,1)" data-name="openCommentRecommend" :class="m.openCommentRecommend ? 'active2' : ''">是</span>
                                    <span @click="select($event,0)" data-name="openCommentRecommend" :class="!m.openCommentRecommend ? 'active2' : ''">否</span>
                                    <input type="hidden" name="openCommentRecommend" v-model="m.openCommentRecommend">
                                </div>
                            </div>
                            <div v-if="m.openCommentRecommend==1">
                                <div class="form-group">
                                    <div class="col-md-4 control-label" >提醒时间：(天）：</div>
                                    <div class="col-md-6">
                                        <input type="number" class="form-control" name="commentTime"
                                               v-model="m.commentTime" required="required" min="0">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-md-4 control-label" >是否推送短信：</div>
                                    <div class="col-md-6 selectStyle">
                                        <span @click="select($event,1)" data-name="isPushSms" :class="m.isPushSms ? 'active2' : ''">是</span>
                                        <span @click="select($event,0)" data-name="isPushSms" :class="!m.isPushSms ? 'active2' : ''">否</span>
                                        <input type="hidden" name="isPushSms" v-model="m.isPushSms">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">评论最小金额：</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="appraiseMinMoney"
                                           v-model="m.appraiseMinMoney">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">红包提醒倒计时(秒)：</label>
                                <div class="col-md-6">
                                    <input type="number" class="form-control" name="autoConfirmTime" v-model="m.autoConfirmTime"
                                           required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">评论订单红包延迟到账时间(秒)：</label>
                                <div class="col-md-6">
                                    <input type="number" class="form-control" name="delayAppraiseMoneyTime"
                                           v-model="m.delayAppraiseMoneyTime" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">好评最少字数：</label>
                                <div class="radio-inline">
                                    <input type="number" class="form-control" name="goodAppraiseLength"
                                           v-model="m.goodAppraiseLength">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4 control-label">差评最少字数：</label>
                                <div class="radio-inline">
                                    <input type="number" class="form-control" name="badAppraiseLength"
                                           v-model="m.badAppraiseLength">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">最迟加菜时间(秒)：</label>
                                <div class="col-md-6">
                                    <input type="number" class="form-control" name="closeContinueTime"
                                           v-model="m.closeContinueTime" required="required">
                                    <%--<div style="color: red" id="timeTips"></div>--%>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-4 control-label">是否开启进入店铺选择页面：</div>
                                <div class="col-md-6 selectStyle">
                                    <span @click="select($event,1)" data-name="openShoplist" :class="m.openShoplist ? 'active2' : ''">是</span>
                                    <span @click="select($event,0)" data-name="openShoplist" :class="!m.openShoplist ? 'active2' : ''">否</span>
                                    <input type="hidden" name="openShoplist" v-model="m.openShoplist">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-4 control-label">是否启用推荐餐包：</div>
                                <div class="col-md-6 selectStyle">
                                    <span @click="select($event,1)" data-name="isUseRecommend" :class="m.isUseRecommend ? 'active2' : ''">是</span>
                                    <span @click="select($event,0)" data-name="isUseRecommend" :class="!m.isUseRecommend ? 'active2' : ''">否</span>
                                    <input type="hidden" name="isUseRecommend" v-model="m.isUseRecommend">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-4 control-label">发送优惠券到期提醒短信：</div>
                                <div class="col-md-6 selectStyle">
                                    <span @click="select($event,1)" data-name="isSendCouponMsg" :class="m.isSendCouponMsg ? 'active2' : ''">是</span>
                                    <span @click="select($event,0)" data-name="isSendCouponMsg" :class="!m.isSendCouponMsg ? 'active2' : ''">否</span>
                                    <input type="hidden" name="isSendCouponMsg" v-model="m.isSendCouponMsg">
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-4 control-label">优惠券到期提醒时间：</label>
                                <div class="col-md-6">
                                    <input type="number" class="form-control"
                                           name="recommendTime" placeholder="(输入整数)"
                                           v-model="m.recommendTime" required="required" min="0">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">优惠券间隔使用时间（单位：小时）：</label>
                                <div class="col-md-6">
                                    <input type="number" class="form-control"
                                           name="couponCD" placeholder="(输入整数)"
                                           v-model="m.couponCD" required="required" min="0">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-4 control-label">礼品优惠券提醒方式：</div>
                                <div class="col-md-6 radio-list">
                                    <label class="radio-inline">
                                        <input type="checkbox" name="wechatPushGiftCoupons" v-model="m.wechatPushGiftCoupons" value="1" >
                                        微信推送
                                    </label>
                                    <label class="radio-inline">
                                        <input type="checkbox" name="smsPushGiftCoupons" v-model="m.smsPushGiftCoupons" value="1">
                                        短信推送
                                    </label>
                                </div>
                            </div>



                            <div class="form-group">
                                <div class="col-md-4 control-label">是否打印总单：</div>
                                <div class="col-md-6 selectStyle">
                                    <span @click="select($event,0)" data-name="autoPrintTotal" :class="!m.autoPrintTotal ? 'active2' : ''">是</span>
                                    <span @click="select($event,1)" data-name="autoPrintTotal" :class="m.autoPrintTotal ? 'active2' : ''">否</span>
                                    <input type="hidden" name="autoPrintTotal" v-model="m.autoPrintTotal">
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-md-4 control-label">套餐出单方式：</div>
                                <div class="col-md-6 selectStyle">
                                    <span @click="select($event,1)" data-name="printType" :class="m.printType ? 'active2' : ''">分单出单</span>
                                    <span @click="select($event,0)" data-name="printType" :class="!m.printType ? 'active2' : ''">整单出单</span>
                                    <input type="hidden" name="printType" v-model="m.printType">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-4 control-label">买单后出总单（后付款模式）：</div>
                                <div class="col-md-6 selectStyle">
                                    <span @click="select($event,1)" data-name="isPrintPayAfter" :class="m.isPrintPayAfter ? 'active2' : ''">开启</span>
                                    <span @click="select($event,0)" data-name="isPrintPayAfter" :class="!m.isPrintPayAfter ? 'active2' : ''">未开启</span>
                                    <input type="hidden" name="isPrintPayAfter" v-model="m.isPrintPayAfter">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-4 control-label">结店审核：</div>
                                <div class="col-md-6 selectStyle">
                                    <span @click="select($event,1)" data-name="openAudit" :class="m.openAudit ? 'active2' : ''">开启</span>
                                    <span @click="select($event,0)" data-name="openAudit" :class="!m.openAudit ? 'active2' : ''">未开启</span>
                                    <input type="hidden" name="openAudit" v-model="m.openAudit">
                                </div>
                            </div>
                        </div>
                        <!--<div class="form-group">
                            <label>短信签名</label>
                            <input type="text" class="form-control" name="smsSign" v-model="m.smsSign">
                        </div>-->

                        <%--<div class="form-group">--%>
                        <%--<label>微信自定义样式</label>--%>
                        <%--<textarea class="form-control" name="wechatCustomoStyle" v-model="m.wechatCustomoStyle"></textarea>--%>
                        <%--</div>--%>


                        <!--		以后	配送模式，都以店铺设置为准，品牌设置不再生效	2017年4月19日 17:51:05		—lmx		-->
                        <%--<div class="form-group">--%>
                        <%--<div class="control-label">是否选择配送模式</div>--%>
                        <%--<label >--%>
                        <%--<input type="radio" name="isChoiceMode" v-model="m.isChoiceMode" value="1">--%>
                        <%--是--%>
                        <%--</label>--%>
                        <%--<label>--%>
                        <%--<input type="radio" name="isChoiceMode" v-model="m.isChoiceMode" value="0">--%>
                        <%--否--%>
                        <%--</label>--%>
                        <%--</div>--%>

                        <div class="form-group" :class="!tabStatus ? 'colorHidden' : ''">
                            <label class="col-md-4 control-label">loading页面的文字颜色：</label>
                            <div class="col-md-2">
                                <input type="text" class="form-control color-mini" name="loadingTextColor"
                                       data-position="bottom left" v-model="m.loadingTextColor">
                            </div>
                            <div class="col-md-5">
                                <span class="btn dark" @click="changeColor('#000000')">黑</span>
                                <span class="btn btn-default" @click="changeColor('#ffffff')">白</span>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" name="id" v-model="m.id"/>
                    <div style=" width:96%;position: absolute;bottom: 40px">
                        <div class="text-center">
                            <input class="btn green" type="submit" value="保存"/>
                            <a class="btn default" @click="cancel">取消</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {


        toastr.options = {
            "closeButton": true,
            "debug": false,
            "positionClass": "toast-top-right",
            "onclick": null,
            "showDuration": "500",
            "hideDuration": "500",
            "timeOut": "3000",
            "extendedTimeOut": "500",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        }
        var temp;
        var vueObj = new Vue({
            el: "#control",
            data: {
                m: {},
                showp: true,
                tabStatus: true,
            },
            watch: {
                'm.autoConfirmTime': 'timeTips',
                'm.closeContinueTime': 'timeTips'

            },
            created: function () {
                var that = this;
                $.ajax({
                    url: "brandSetting/list_one",
                    success: function (result) {
                        console.log(result.data);
                        vueObj.m = result.data;
                        that.initEditor();
                    }
                });
                var n = $('.color-mini').minicolors({
                    change: function (hex, opacity) {
                        if (!hex) return;
                        if (typeof console === 'object') {
                            $(this).attr("value", hex);
                        }
                    },
                    theme: 'bootstrap'
                });
                this.$watch("m", function () {
                    if (this.m.id) {
                        $('.color-mini').minicolors("value", this.m.loadingTextColor);
                    }
                });
            },
            methods: {
                //单选框
                select: function (e,val){

                    switch (e.target.dataset.name)
                    {
                        case 'autoAlertAppraise':
                            this.m.autoAlertAppraise = val
                            break;
                        case 'openCommentRecommend':
                            this.m.openCommentRecommend = val
                            break;
                        case 'isPushSms':
                            this.m.isPushSms = val
                            break;
                        case 'openShoplist':
                            this.m.openShoplist = val
                            break;
                        case 'isUseRecommend':
                            this.m.isUseRecommend = val
                            break;
                        case 'isSendCouponMsg':
                            this.m.isSendCouponMsg = val
                            break;
                        case 'autoPrintTotal':
                            this.m.autoPrintTotal = val
                            break;
                        case 'printType':
                            this.m.printType = val
                            break;
                        case 'isPrintPayAfter':
                            this.m.isPrintPayAfter = val
                            break;
                        case 'openShoplist':
                            this.m.openShoplist = val
                            break;
                        case 'openAudit':
                            this.m.openAudit = val
                            break;
                    }
                },
                changeColor: function (val) {
                    $(".color-mini").minicolors("value", val);
                },
                tabChange: function (status) {
                    this.tabStatus = status
                },
                timeTips: function () {
                    var autoConfirmTime = $("input[name='autoConfirmTime']").val();
                    var closeContinueTime = $("input[name='closeContinueTime']").val();
                    if (parseInt(autoConfirmTime) <= parseInt(closeContinueTime)) {
                        $("#timeTips").html("* 红包提醒倒计时应该大于最迟加菜时间");
                    } else {
                        $("#timeTips").html("");
                    }
                },
                save: function (e) {
                    var formDom = e.target;
                    $.ajax({
                        url: "brandSetting/modify",
                        data: $(formDom).serialize(),
                        success: function (result) {
                            if (result.success) {
                                toastr.clear();
                                toastr.success("保存成功！");
                            } else {
                                toastr.clear();
                                toastr.error("保存失败");
                            }
                        },
                        error: function () {
                            toastr.clear();
                            toastr.error("保存失败");
                        }
                    })

                },

                cancel: function () {
                    var that = this;
                    $.ajax({
                        url: "brandSetting/list_one",
                        success: function (result) {
                            console.log(result.data);
                            vueObj.m = result.data;
                            that.initEditor();
                        }
                    });
                },
                uploadSuccess: function (url) {
                    $("[name='wechatWelcomeImg']").val(url).trigger("change");
                    toastr.success("上传成功！");
                },
                setRedPackage: function (url) {
                    $("[name='redPackageLogo']").val(url).trigger("change");
                    toastr.success("上传logo成功！");
                },
                uploadSuccessLogo: function (url) {
                    $("[name='loadingLogo']").val(url).trigger("change");
                    toastr.success("上传成功！");
                },
                uploadSuccessBackground: function (url) {
                    $("[name='loadingBackground']").val(url).trigger("change");
                    toastr.success("上传成功！");
                },
                uploadError: function (msg) {
                    toastr.error("上传失败");
                },
                initEditor : function () {
                    Vue.nextTick(function(){
//                        var editor = new wangEditor('shareText');
//                        editor.config.menus = [];
//                        editor.create();
                    });
                }
            }
        });
    }());

</script>
