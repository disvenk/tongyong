<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<div id="control">
    <c:if test="${!empty shopId}">
        <a class="btn btn-info ajaxify" href="orderReport/list">
            <span class="glyphicon glyphicon-circle-arrow-left"></span>
            返回
        </a>
    </c:if>
    <c:if test="${!empty customerId}">
        <a class="btn btn-info ajaxify" href="member/myList">
            <span class="glyphicon glyphicon-circle-arrow-left"></span>
            返回
        </a>
    </c:if>
    <h2 class="text-center">
        <strong>
            <c:if test="${!empty shopName}">
                ${shopName}
            </c:if>
            <c:if test="${empty shopName}">
                订单记录
            </c:if>
        </strong>
    </h2>
    <br />
    <div class="row">
        <div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 50px;">
                    <label for="beginDate">开始时间：</label>
                    <input type="text" class="form-control form_datetime" id="beginDate" v-model="searchDate.beginDate" readonly="readonly">
                </div>
                <div class="form-group" style="margin-right: 50px;">
                    <label for="endDate">结束时间：</label>
                    <input type="text" class="form-control form_datetime" id="endDate" v-model="searchDate.endDate" readonly="readonly">
                </div>
                <button type="button" class="btn btn-primary" @click="today"> 今日</button>
                <button type="button" class="btn btn-primary" @click="yesterDay">昨日</button>
                <button type="button" class="btn btn-primary" @click="week">本周</button>
                <button type="button" class="btn btn-primary" @click="month">本月</button>
                <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>&nbsp;
                <button type="button" class="btn btn-primary" @click="uploadWeChatBill">下载微信对账单</button>&nbsp;
                <button type="button" class="btn btn-primary" @click="downloadShopOrder" v-if="state == 1">下载报表</button>
                <button type="button" class="btn btn-default" disabled="disabled" v-if="state == 2">下载数据过多，正在生成中。请勿刷新页面</button>
                <button type="button" class="btn btn-success" @click="download" v-if="state == 3">已完成，点击下载</button><br/>
            </form>
        </div>
    </div>
    <br />
    <br />
    <!-- 店铺订单列表  -->
    <div class="panel panel-info">
        <div class="panel-heading text-center" style="font-size: 22px;">
            <strong>
                <c:if test="${!empty shopId}">
                    店铺订单记录
                </c:if>
                <c:if test="${!empty customerId}">
                    会员订单记录
                </c:if>
            </strong>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-bordered table-hover"
                   id="shopOrder">
            </table>
        </div>
    </div>

    <div class="modal fade" id="orderDetail" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            @click="closeModal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title text-center">
                        <strong>订单详情</strong>
                    </h4>
                </div>
                <div class="modal-body">
                    <dl class="dl-horizontal">
                        <dt>店铺名称：</dt>
                        <dd>{{orderDetail.shopName}}</dd>
                        <dt>订单编号：</dt>
                        <dd>{{orderDetail.orderId}}</dd>
                        <dt>微信支付单号：</dt>
                        <dd>{{orderDetail.wechatPayId}}</dd>
                        <dt>订单时间：</dt>
                        <dd>{{orderDetail.orderTime}}</dd>
                        <dt>就餐模式：</dt>
                        <dd>{{orderDetail.modeText}}</dd>
                        <dt>验 证 码：</dt>
                        <dd>{{orderDetail.varCode}}</dd>
                        <dt>手 机 号：</dt>
                        <dd>{{orderDetail.telePhone}}</dd>
                        <dt>订单金额：</dt>
                        <dd>{{orderDetail.orderMoney}}</dd>
                        <dt>评&nbsp;&nbsp;价：</dt>
                        <dd>{{orderDetail.level}}</dd>
                        <dt>评价内容：</dt>
                        <dd>{{orderDetail.levelValue}}</dd>
                        <dt>状&nbsp;&nbsp;态：</dt>
                        <dd>{{orderDetail.orderState}}</dd>
                        <dt>菜品总价：</dt>
                        <dd>{{orderDetail.articleMoney}}</dd>
                        <dt>服&nbsp;务&nbsp;费：</dt>
                        <dd>{{orderDetail.servicePrice}}</dd>
                    </dl>
                </div>
                <div class="table-scrollable">
                    <table class="table table-condensed table-hover">
                        <thead>
                            <tr>
                                <th>餐品类别</th>
                                <th>餐品名称</th>
                                <th>餐品单价</th>
                                <th>餐品数量</th>
                                <th>小记</th>
                            </tr>
                        </thead>
                        <tbody style="height: 300px;">
                            <tr v-for="orderItem in orderDetail.orderItems">
                                <td>{{orderItem.articleFamily.name}}</td>
                                <td>{{orderItem.articleName}}</td>
                                <td>{{orderItem.unitPrice}}</td>
                                <td>{{orderItem.count}}</td>
                                <td>{{orderItem.finalPrice}}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-block btn-primary" data-dismiss="modal" @click="closeModal">关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="assets/customer/date.js" type="text/javascript"></script>
<script>
    //时间插件
    $('.form_datetime').datetimepicker({
        endDate : new Date(),
        minView : "month",
        maxView : "month",
        autoclose : true,//选择后自动关闭时间选择器
        todayBtn : true,//在底部显示 当天日期
        todayHighlight : true,//高亮当前日期
        format : "yyyy-mm-dd",
        startView : "month",
        language : "zh-CN"
    });

    var shopId = "${shopId}";
    var customerId = "${customerId}";
    var beginDate = "${beginDate}";
    var endDate = "${endDate}";
    var shopOrderTableAPI = null;
    //创建vue对象
    var vueObj =  new Vue({
        el:"#control",
        data:{
            searchDate : {
                beginDate : "",
                endDate : "",
            },
            shopOrderTable:{},
            shopOrderDetails:[],
            orderDetail:{},
            state:1,
            shopOrderList : [],
            object : {},
            length : 0,
            start : 0,
            end : 200,
            startPosition : 205,
            index : 1
        },
        created : function() {
            this.searchDate.beginDate = beginDate;
            this.searchDate.endDate = endDate;
            this.createShopOrderTable();
            this.searchInfo();
        },
        methods:{
            createShopOrderTable : function(){
                var that = this;
                this.shopOrderTable = $("#shopOrder").DataTable({
                    lengthMenu : [ [ 50, 75, 100, -1 ], [ 50, 75, 100, "All" ] ],
                    order: [[ 1, 'desc' ]],
                    columns : [
                        /*{
                            title : "店铺",
                            data : "shopName",
                            orderable : false
                        },*/
                        {
                            title : "订单类型",
                            data : "distributionModeId",
                            createdCell:function(td,tdData){
                                var str = "未知"
                                if(tdData == "1"){
                                    str = "堂吃"
                                }else if(tdData == "2"){
                                    str = "R+外卖"
                                }else if(tdData == "3"){
                                    str = "外带"
                                }
                                $(td).html(str);
                            }
                        },
                        {
                            title : "下单时间",
                            data : "createTime"
                        }, {
                            title : "手机号",
                            data : "telephone",
                            orderable : false
                        },
                        {
                            title : "订单状态",
                            data : "orderState",
                            s_filter: true,
                            orderable : false
                        },
                        {
                            title : "桌号",
                            data : "tableNumber",
                        },
                        {
                            title : "订单流水号",
                            data : "serialNumber",
                        },
                        {
                            title : "订单金额",
                            data : "orderMoney"
                        },
                        {
                            title : "POS端折扣金额",
                            data : "orderPosDiscountMoney",
                        },
                        {
                            title : "会员折扣金额",
                            data : "memberDiscountMoney"
                        }, {
                            title : "抹零金额",
                            data : "realEraseMoney"
                        }, {
                            title : "赠菜金额",
                            data : "grantMoney"
                        },{
                            title : "微信支付",
                            data : "weChatPay",
                            createdCell : function(td, tdData, rowData) {
                                $(td).html(rowData.offline == true ? tdData + "(线下)" : tdData);
                            }
                        }, {
                            title : "红包支付",
                            data : "accountPay"
                        }, {
                            title : "优惠券支付",
                            data : "couponPay"
                        }, {
                            title : "充值金额支付",
                            data : "chargePay"
                        }, {
                            title : "充值赠送金额支付",
                            data : "rewardPay"
                        },{
                            title : "等位红包支付",
                            data : "waitRedPay"
                        }
                        ,{
                            title : "支付宝支付",
                            data : "aliPayment"
                        }
                        ,{
                            title : "现金实收",
                            data : "moneyPay"
                        },
                        {
                            title : "银联支付",
                            data : "backCartPay"
                        },
                        {
                            title : "新美大支付",
                            data : "shanhuiPay"
                        },
                        {
                            title : "会员支付",
                            data : "integralPay"
                        },
                        {
                            title : "退菜返还红包",
                            data : "articleBackPay"
                        },
                        {
                            title : "现金退款",
                            data : "refundCrashPayment"
                        },
                        {
                            title : "免单金额",
                            data : "exemptionMoney"
                        },
                        {
                            title : "实体卡充值支付",
                            data : "cardRechargePay"
                        },
                        {
                            title : "实体卡赠送支付",
                            data : "cardRechargeFreePay"
                        },
                        {
                            title : "实体卡退款金额",
                            data : "refundCardPay"
                        },
                        {
                            title : "实体卡折扣",
                            data : "cardDiscountPay"
                        },
//                        {
//                            title : "找零",
//                            data : "giveChangePayment"
//                        },
                        /*{
                            title : "营销撬动率",
                            data : 'incomePrize'
                        },*/
                        {
                            title : "操作",
                            data : "orderId",
                            orderable : false,
                            createdCell : function(td, tdData) {
                                var button = $("<button class='btn green'>查看详情</button>");
                                button.click(function(){
                                    that.openOrderDetailModal(tdData);
                                });
                                $(td).html(button);
                            }
                        }
                    ],
                    initComplete: function () {
                        shopOrderTableAPI = this.api();
                        that.shopOrderTableColumn();
                    }
                });
            },
            searchInfo : function() {
                var that = this;
                var timeCha = new Date(that.searchDate.endDate).getTime() - new Date(that.searchDate.beginDate).getTime();
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
                toastr.success("查询中...");
                try {
                    $.post("orderReport/AllOrder", this.getDate(), function (result) {
                        if(result.success) {
                            toastr.clear();
                            toastr.success("查询成功");
                            var API = shopOrderTableAPI;
                            API.search('');
                            var orderState = API.column(3);
                            orderState.search('',true,false);
                            that.shopOrderDetails = result.data.result;
                            that.shopOrderTable.clear();
                            that.shopOrderTable.rows.add(result.data.result).draw();
                            that.shopOrderTableColumn();
                        }else{
                            toastr.clear();
                            toastr.error("查询出错");
                        }
                    });
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            openOrderDetailModal : function (orderId) {
                var that = this;
                try {
                    $.post("orderReport/detailInfo",{orderId: orderId},function (result) {
                        if(result.success){
                            that.orderDetail = result.data;
                            $("#orderDetail").modal();
                        }else{
                            toastr.clear();
                            toastr.error("查询出错");
                        }
                    });
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            downloadShopOrder : function(){
                var that = this;
                try {
                    that.object = that.getDate();
//                    that.shopOrderList = that.shopOrderDetails.sort(this.keysert('beginTime'));
//                    if (that.shopOrderList.length <= 200){
//                        that.object.shopOrderList = that.shopOrderList;
                        $.post("orderReport/create_shop_excel",that.object,function (result) {
                            if (result.success){
                                window.location.href = "orderReport/downShopOrderExcel?path="+result.data+"";
                            }else{
                                toastr.clear();
                                toastr.error("下载报表出错");
                            }
                        })
//                    }else{
//                        that.state = 2;
//                        that.length = Math.ceil(that.shopOrderList.length/200);
//                        that.object.shopOrderList = that.shopOrderList.slice(that.start,that.end);
//                        $.post("orderReport/create_shop_excel",that.object,function (result) {
//                            if (result.success){
//                                that.object.path = result.data;
//                                that.start = that.end;
//                                that.end = that.start + 200;
//                                that.index++;
//                                that.appendShopExcel();
//                            }else{
//                                that.state = 1;
//                                that.start = 0;
//                                that.end = 200;
//                                that.startPosition = 205;
//                                that.index = 1;
//                                toastr.clear();
//                                toastr.error("生成报表出错");
//                            }
//                        });
//                    }
                }catch (e) {
                    that.state = 1;
                    that.start = 0;
                    that.end = 200;
                    that.startPosition = 205;
                    that.index = 1;
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            appendShopExcel : function () {
                var that = this;
                try {
                    if (that.index == that.length) {
                        that.object.shopOrderList = that.shopOrderList.slice(that.start);
                    } else {
                        that.object.shopOrderList = that.shopOrderList.slice(that.start, that.end);
                    }
                    that.object.startPosition = that.startPosition;
                    $.post("orderReport/appendShopOrderExcel", that.object, function (result) {
                        if (result.success) {
                            that.start = that.end;
                            that.end = that.start + 200;
                            that.startPosition = that.startPosition + 200;
                            that.index++;
                            if (that.index - 1 == that.length) {
                                that.state = 3;
                            } else {
                                that.appendShopExcel();
                            }
                        } else {
                            that.state = 1;
                            that.start = 0;
                            that.end = 200;
                            that.startPosition = 205;
                            that.index = 1;
                            toastr.clear();
                            toastr.error("生成报表出错");
                        }
                    });
                }catch (e){
                    that.state = 1;
                    that.start = 0;
                    that.end = 200;
                    that.startPosition = 205;
                    that.index = 1;
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            download : function(){
                window.location.href = "orderReport/downShopOrderExcel?path="+this.object.path+"";
                this.state = 1;
                this.start = 0;
                this.end = 200;
                this.startPosition = 205;
                this.index = 1;
            },
            keysert: function (key) {
                return function(a,b){
                    var value1 = a[key];
                    var value2 = b[key];
                    return value1 - value2;
                }
            },
            getDate : function(){
                var data = {
                    beginDate : this.searchDate.beginDate,
                    endDate : this.searchDate.endDate,
                    shopId : shopId,
                    customerId : customerId
                };
                return data;
            },
            today : function(){
                date = new Date().format("yyyy-MM-dd");
                this.searchDate.beginDate = date
                this.searchDate.endDate = date
                this.searchInfo();
            },
            yesterDay : function(){

                this.searchDate.beginDate = GetDateStr(-1);
                this.searchDate.endDate  = GetDateStr(-1);
                this.searchInfo();
            },
            week : function(){
                this.searchDate.beginDate  = getWeekStartDate();
                this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
                this.searchInfo();
            },
            month : function(){
                this.searchDate.beginDate  = getMonthStartDate();
                this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
                this.searchInfo();
            },
            closeModal : function () {
                $("#orderDetail").modal("hide");
            },
            shopOrderTableColumn : function () {
                var api = shopOrderTableAPI;
                var columnsSetting = api.settings()[0].oInit.columns;
                $(columnsSetting).each(function (i) {
                    if (this.s_filter) {
                        var column = api.column(i);
                        var select = $('<select id=""><option value="">' + this.title + '(全部)</option></select>');
                        column.data().unique().each(function (d) {
                            select.append('<option value="' + d + '">' + d + '</option>')
                        });
                        select.appendTo($(column.header()).empty()).on('change', function () {
                            var val = $.fn.dataTable.util.escapeRegex(
                                    $(this).val()
                            );
                            column.search(val ? '^' + val + '$' : '', true, false).draw();
                        });
                    }
                });
            },
            uploadWeChatBill : function () {
                var that = this;
                try{
                    $.post("orderReport/uploadWeChatBill",that.getDate(),function (result) {
                        if (result.success){
                            window.location.href="orderReport/downloadBrandOrderExcel?path="+result.data+"";
                        }else{
                            toastr.clear();
                            toastr.error("下载微信对账单出错");
                        }
                    });
                }catch (e){
                    toastr.clear();
                    toastr.error("下载微信对账单出错");
                }
            }
        }
    });
</script>
