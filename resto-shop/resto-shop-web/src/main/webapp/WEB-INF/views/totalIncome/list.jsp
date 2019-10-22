<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<h2 class="text-center"><strong>营业总额报表</strong></h2>
<div id="control">
    <div class="row" id="searchTools">
        <div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 50px;">
                    <label for="beginDate">开始时间：</label>
                    <input type="text" class="form-control form_datetime" id="beginDate" v-model="searchDate.beginDate" readonly="readonly">
                </div>
                <div class="form-group" style="margin-right: 50px;">
                    <label for="endDate">结束时间：</label>
                    <input type="text" class="form-control form_datetime" id="endDate" v-model="searchDate.endDate" readonly="readonly">
                    <br></div>
                <button type="button" class="btn btn-primary" id="today" @click="today"> 今日</button>
                <button type="button" class="btn btn-primary" id="yesterDay" @click="yesterDay">昨日</button>
                <button type="button" class="btn btn-primary" id="week" @click="week">本周</button>
                <button type="button" class="btn btn-primary" id="month" @click="month">本月</button>
                <button type="button" class="btn btn-primary" id="searchReport">查询报表</button>&nbsp;
                <button type="button" class="btn btn-primary" id="brandreportExcel">下载报表</button><br/>
                <%--<a type="button" href="orderReport/historyOrderList?reportName=营业报表和订单报表" style="float: right;" class="btn green ajaxify">查询历史报表数据</a>--%>
                <%--<button type="button" style="float: right;" class="btn btn-primary" id="brandreportExcel">下载报表</button><br/>--%>
                <form>
                    <input type="hidden" id="brandDataTable">
                    <input type="hidden" id="shopDataTable">
                </form>&nbsp;&nbsp;&nbsp;
            </form>
        </div>
    </div>
    <br/>
    <div>
        <!-- 每日报表 -->
        <div id="report-editor">
            <div class="panel panel-success">
                <div class="panel-heading text-center">
                    <strong style="margin-right:100px;font-size:22px">品牌收入条目</strong>
                    <button type="button" style="float: right;" @click="openModal(0)" class="btn btn-primary">月报表</button>
                </div>
                <div class="panel-body">
                    <table id="brandReportTable" class="table table-striped table-bordered table-hover" width="100%"></table>
                </div>

                <div class="panel-heading text-center">
                    <strong style="margin-right:100px;font-size:22px">店铺收入条目</strong>
                    <button type="button" style="float: right;" @click="openModal(1)" class="btn btn-primary">月报表</button>
                </div>
                <div class="panel-body">
                    <table id="shopReportTable" class="table table-striped table-bordered table-hover" width="100%"></table>
                </div>
            </div>
        </div>

        <div class="modal fade" id="queryCriteriaModal" tabindex="-1" role="dialog" data-backdrop="static">
            <div class="modal-dialog modal-full">
                <div class="modal-content" style="width: 30em;margin: 15% auto;">
                    <div class="modal-header" style="border-bottom:initial;">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                        <h4 align="center"><b>下载月报表</b></h4>
                    </div>
                    <div class="modal-body" align="center">
                        <select style="padding: 5px 12px;" v-model="selectYear">
                            <option :value="year" v-for="year in years">{{year}}</option>
                        </select>
                        <span style="font-size: 16px;margin-left: 15px;font-weight: bold;">年</span>
                        <select style="padding: 5px 12px;" v-model="selectMonth">
                            <option :value="month" v-for="month in months">{{month}}</option>
                        </select>
                        <span style="font-size: 16px;margin-left: 15px;font-weight: bold;">月</span>
                    </div>
                    <div class="modal-footer" style="border-top:initial;">
                        <button type="button" class="btn btn-default" data-dismiss="modal" style="float: left;margin-left: 5em;">取消</button>
                        <button type="button" class="btn btn-primary" v-if="state == 1" @click="createMonthDto" style="float: right;margin-right: 5em;">生成并下载</button>
                        <button type="button" class="btn btn-success" v-if="state == 2" disabled="disabled" style="float: right;margin-right: 5em;">生成中...</button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
<script src="assets/customer/date.js" type="text/javascript"></script>
<script src="assets/customer/components/dateSearch.js" type="text/javascript"></script>

<script>
    //时间插件
    $('.form_datetime').datetimepicker({
        endDate:new Date(),
        // minView:"month",
        minView: 0,
        maxView:"month",
        autoclose:true,//选择后自动关闭时间选择器
        todayBtn:true,//在底部显示 当天日期
        todayHighlight:true,//高亮当前日期
        format:"yyyy-mm-dd hh:ii:ss",
        startView:"month",
        language:"zh-CN"
    });
    
    var obj = new Vue({
        el : "#control",
        mixins: [dateSearch],
        data : {
            years : [],
            months : ["01","02","03","04","05","06","07","08","09","10","11","12"],
            selectYear : new Date().format("yyyy"),
            selectMonth : new Date().format("MM"),
            type : 0,
            state : 1,
            searchDate : {
                beginDate : "",
                endDate : "",
            },
        },
        created : function(){
            this.getYears();
            var date0 = new Date().format("yyyy-MM-dd 00:00:00");
            var date = new Date().format("yyyy-MM-dd hh:mm:ss");
            this.searchDate.beginDate = date0;
            this.searchDate.endDate = date;
        },
        methods : {
            getYears : function() {
                var years = new Array();
                var year = 2016;
                var nowYear = parseInt(new Date().format("yyyy"));
                for (var i = 0;true;i++){
                    years[i] = year;
                    if (year == nowYear){
                        break;
                    }
                    year++;
                }
                this.years = years;
            },
            openModal : function (type) {
                $("#queryCriteriaModal").modal();
                this.type = type;
            },
            createMonthDto : function () {
                toastr.clear();
                toastr.success("生成中...");
                var that = this;
                that.state = 2;
                try {
                    $.post("totalIncome/createMonthDto",{year : that.selectYear, month : that.selectMonth, type : that.type},function (result) {
                        if (result.success){
                            that.state = 1;
                            window.location.href="totalIncome/downloadExcel?path="+result.data+"";
                        }else{
                            that.state = 1;
                            toastr.clear();
                            toastr.error("生成月报表出错");
                        }
                    });
                }catch (e){
                    that.state = 1;
                    toastr.clear();
                    toastr.error("生成月报表出错");
                    return;
                }
            },
            searchInfo: function(beginDate,endDate){
                var timeCha = new Date(endDate).getTime() - new Date(beginDate).getTime();
                if(timeCha < 0){
                    toastr.clear();
                    toastr.error("开始时间应该少于结束时间！");
                    return false;
                }else if(timeCha > 2678400000){
                    toastr.clear();
                    toastr.error("暂时未开放大于一月以内的查询！");
                    return false;
                }
                toastr.clear();
                toastr.success('查询中...');
                //更新数据源
                $.ajax( {
                    url:'totalIncome/reportIncome',
                    data:{
                        'beginDate':beginDate,
                        'endDate':endDate
                    },
                    success:function(result) {
                        dataSource=result;
                        tb1.clear().draw();
                        tb2.clear().draw();
                        tb1.rows.add(result.brandIncome).draw();
                        tb2.rows.add(result.shopIncome).draw();
                        toastr.clear();
                        toastr.success('查询成功');
                    },
                    error : function() {
                        toastr.clear();
                        toastr.error("系统异常，请刷新重试");
                    }
                });
            }
        }
    });
    //文本框默认值
    // $('.form_datetime').val(new Date().format("yyyy-MM-dd"));
    var beginDate = new Date().format("yyyy-MM-dd 00:00:00");
    var endDate = new Date().format("yyyy-MM-dd hh:mm:ss");
    var dataSource;
    toastr.success('查询中...');
    $.ajax( {
        url:'totalIncome/reportIncome',
        async:false,
        data:{
            'beginDate':beginDate,
            'endDate':endDate
        },
        success:function(data) {
            dataSource=data;
            toastr.clear();
            toastr.success('查询成功');
        },
        error : function() {
            toastr.clear();
            toastr.error("系统异常,请刷新重试");
        }
    });

    var tb1 = $("#brandReportTable").DataTable({
        ordering: false,
        data:dataSource.brandIncome,
//        dom:'i',
        columns : [
            {
                title : "品牌",
                data : "shopName",
            },
            {
                title : "原价销售总额",
                data : "originalAmount"
            },
            {
                title : "订单总额",
                data : "totalIncome",
            },
            {
                title : "实收金额",
                data : "amountCollected",
            },
            {
                title : "折扣金额",
                data : "discountTotalMoney",
            },
            {
                title : "退款金额",
                data : "refundTotalMoney",
            },
            {
                title : "线上微信支付",
                data : "onLineWechatIncome",
            },
            {
                title : "线上支付宝支付",
                data : "onLineAliPayment",
            },
            {
                title : "线下微信支付",
                data : "offLineWechatIncome",
            },
            {
                title : "线下支付宝支付",
                data : "offLineAliPayment",
            },
            {
                title : "充值账户支付",
                data : "chargeAccountIncome",
            },
            {
                title : "刷卡支付",
                data : "backCartPay"
            },
            {
                title : "现金支付",
                data : "moneyPay"
            },
            {
                title : "新美大支付",
                data : "shanhuiPayment"
            },
            {
                title : "团购支付",
                data : "groupPurchasePayment"
            },
            {
                title : "红包支付",
                data : "redIncome",
            },
            {
                title : "优惠券支付",
                data : "couponIncome",
            },

            {
                title : "充值赠送支付",
                data : "chargeGifAccountIncome",
            },
            {
                title : "等位红包支付",
                data : "waitNumberIncome",
            },
            {
                title : "会员支付",
                data : "integralPayment"
            },
            {
                title : "POS端折扣",
                data : "orderPosDiscountMoney",
            },
            {
                title : "会员折扣",
                data : "memberDiscountMoney",
            },
            {
                title : "抹零",
                data : "realEraseMoney",
            },
            {
                title : "免单",
                data : "exemptionMoney",
            },
            {
                title : "代金券支付",
                data : "cashCouponPayment",
            },
            {
                title : "退菜返还红包",
                data : "articleBackPay"
            },
            {
                title : "线下现金退款",
                data : "refundCrashPayment"
            },
            {
                title : "实体卡充值本金支付",
                data : "cardRechargePay"
            },
            {
                title : "实体卡充值赠送支付",
                data : "cardRechargeFreePay"
            },
            {
                title : "实体卡退款金额",
                data : "refundCardPay"
            },{
                title : "实体卡折扣",
                data : "cardDiscountPay"
            },{
                title : "赠菜金额",
                data : "grantArticleMoney"
            }

        ]

    });

    var tb2 = $("#shopReportTable").DataTable({
        data:dataSource.shopIncome,
        columns : [
            {
                title : "店铺名称",
                data : "shopName",
            },
            {
                title : "原价销售总额",
                data : "originalAmount"
            },
            {
                title : "订单总额",
                data : "totalIncome",
            },
            {
                title : "实收金额",
                data : "amountCollected",
            },
            {
                title : "折扣金额",
                data : "discountTotalMoney",
            },
            {
                title : "退款金额",
                data : "refundTotalMoney",
            },
            {
                title : "线上微信支付",
                data : "onLineWechatIncome",
            },
            {
                title : "线上支付宝支付",
                data : "onLineAliPayment",
            },
            {
                title : "线下微信支付",
                data : "offLineWechatIncome",
            },
            {
                title : "线下支付宝支付",
                data : "offLineAliPayment",
            },
            {
                title : "充值账户支付",
                data : "chargeAccountIncome",
            },
            {
                title : "刷卡支付",
                data : "backCartPay"
            },
            {
                title : "现金支付",
                data : "moneyPay"
            },
            {
                title : "新美大支付",
                data : "shanhuiPayment"
            },
            {
                title : "团购支付",
                data : "groupPurchasePayment"
            },
            {
                title : "红包支付",
                data : "redIncome",
            },
            {
                title : "优惠券支付",
                data : "couponIncome",
            },

            {
                title : "充值赠送支付",
                data : "chargeGifAccountIncome",
            },
            {
                title : "等位红包支付",
                data : "waitNumberIncome",
            },
            {
                title : "会员支付",
                data : "integralPayment"
            },
            {
                title : "POS端折扣",
                data : "orderPosDiscountMoney",
            },
            {
                title : "会员折扣",
                data : "memberDiscountMoney",
            },
            {
                title : "抹零",
                data : "realEraseMoney",
            },
            {
                title : "免单",
                data : "exemptionMoney",
            },
            {
                title : "代金券支付",
                data : "cashCouponPayment",
            },
            {
                title : "退菜返还红包",
                data : "articleBackPay"
            },
            {
                title : "线下现金退款",
                data : "refundCrashPayment"
            },
            {
                title : "实体卡充值本金支付",
                data : "cardRechargePay"
            },
            {
                title : "实体卡充值赠送支付",
                data : "cardRechargeFreePay"
            },
            {
                title : "实体卡退款金额",
                data : "refundCardPay"
            },{
                title : "实体卡折扣",
                data : "cardDiscountPay"
            },{
                title : "赠菜金额",
                data : "grantArticleMoney"
            },
            {
                title : "连接率",
                data : "connctRatio"
            }
        ]

    });
    



    //查询
    $("#searchReport").click(function(){
        searchInfo( $("#beginDate").val(),$("#endDate").val());
    });

    // //今日

    // $("#today").click(function(){
    //     date = new Date().format("yyyy-MM-dd");
    //     beginDate = date;
    //     endDate = date;
    //     searchInfo(beginDate,endDate);
    // });

    // //昨日
    // $("#yesterDay").click(function(){
    //     beginDate = GetDateStr(-1);
    //     endDate = GetDateStr(-1);
    //     $("#beginDate").val(beginDate);
    //     $("#endDate").val(endDate);
    //     searchInfo(beginDate,endDate);

    // });

    // //本周
    // $("#week").click(function(){
    //     beginDate = getWeekStartDate();
    //     endDate = new Date().format("yyyy-MM-dd");
    //     $("#beginDate").val(beginDate);
    //     $("#endDate").val(endDate);
    //     searchInfo(beginDate,endDate);

    // });

    // //本月
    // $("#month").click(function(){
    //     beginDate = getMonthStartDate();
    //     endDate = new Date().format("yyyy-MM-dd");
    //     $("#beginDate").val(beginDate);
    //     $("#endDate").val(endDate);
    //     searchInfo(beginDate,endDate);

    // });

    function searchInfo(beginDate,endDate){
        var timeCha = new Date(endDate).getTime() - new Date(beginDate).getTime();
        if(timeCha < 0){
            toastr.clear();
            toastr.error("开始时间应该少于结束时间！");
            return false;
        }else if(timeCha > 2678400000){
            toastr.clear();
            toastr.error("暂时未开放大于一月以内的查询！");
            return false;
        }
        toastr.clear();
        toastr.success('查询中...');
        //更新数据源
        $.ajax( {
            url:'totalIncome/reportIncome',
            data:{
                'beginDate':beginDate,
                'endDate':endDate
            },
            success:function(result) {
                dataSource=result;
                tb1.clear().draw();
                tb2.clear().draw();
                tb1.rows.add(result.brandIncome).draw();
                tb2.rows.add(result.shopIncome).draw();
                toastr.clear();
                toastr.success('查询成功');
            },
            error : function() {
                toastr.clear();
                toastr.error("系统异常，请刷新重试");
            }
        });
    }

    function getDays(strDateStart,strDateEnd){
        var strSeparator = "-"; //日期分隔符
        var oDate1;
        var oDate2;
        var iDays;
        oDate1= strDateStart.split(strSeparator);
        oDate2= strDateEnd.split(strSeparator);
        var strDateS = new Date(oDate1[0], oDate1[1]-1, oDate1[2]);
        var strDateE = new Date(oDate2[0], oDate2[1]-1, oDate2[2]);
        iDays = parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24)//把相差的毫秒数转换为天数
        return iDays ;
    }


    //导出品牌数据
    $("#brandreportExcel").click(function(){
        beginDate = $("#beginDate").val();
        endDate = $("#endDate").val();
        var object = {
            beginDate : beginDate,
            endDate : endDate,
            brandIncomeDtos : dataSource.brandIncome,
            shopIncomeDtos : dataSource.shopIncome
        }
        try {
            $.post("totalIncome/brandExprotExcel",object,function (result) {
                if (result.success){
                    location.href="totalIncome/downloadExcel?path="+result.data+"";
                }else{
                    toastr.clear();
                    toastr.error("生成报表出错");
                }
            });
        }catch (e){
            toastr.clear();
            toastr.error("系统异常，请刷新重试");
        }

    });
</script>
