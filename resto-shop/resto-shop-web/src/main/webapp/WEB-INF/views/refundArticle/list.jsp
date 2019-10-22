<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<div id="control">
    <h2 class="text-center">
        <strong>
            退菜明细表
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
                <button type="button" class="btn btn-primary" @click="createExcel">下载报表</button>
            </form>
        </div>
    </div>
    <br />
    <br />
    <div class="panel panel-info">
        <div class="panel-heading text-center" style="font-size: 22px;">
            <strong>
                退菜明细表
            </strong>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-bordered table-hover"
                   id="refundArticleTable">
            </table>
        </div>
    </div>

    <div class="modal fade" id="refundOrderDetail" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            @click="closeModal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title text-center">
                        <strong>退菜详情</strong>
                    </h4>
                </div>
                <div class="modal-body">
                    <dl class="dl-horizontal">
                        <dt>店铺：</dt>
                        <dd>{{refundOrderDetail.shopName}}</dd>
                        <dt>订单编号：</dt>
                        <dd>{{refundOrderDetail.orderId}}</dd>
                        <dt>下单时间：</dt>
                        <dd>{{refundOrderDetail.pushOrderTime}}</dd>
                        <dt>桌号：</dt>
                        <dd>{{refundOrderDetail.tableNumber}}</dd>
                        <dt>手机号：</dt>
                        <dd>{{refundOrderDetail.telephone}}</dd>
                        <dt>用户昵称：</dt>
                        <dd>{{refundOrderDetail.nickName}}</dd>
                        <dt>退菜数量：</dt>
                        <dd>{{refundOrderDetail.refundCount}}</dd>
                        <dt>退菜金额：</dt>
                        <dd>{{refundOrderDetail.refundMoney}}</dd>
                    </dl>
                </div>
                <div class="table-scrollable">
                    <table class="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>金额</th>
                                <th>退款方式</th>
                                <th>退款单号</th>
                            </tr>
                        </thead>
                        <tbody style="height: 300px;">
                            <tr v-for="payment in refundPayment">
                                <td>{{payment.payValue}}</td>
                                <td>{{payment.paymentModeVal}}</td>
                                <td>{{payment.id}}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="table-scrollable">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <th>菜品名称</th>
                            <th>单价</th>
                            <th>退菜数量</th>
                            <th>小计</th>
                            <th>退菜时间</th>
                            <th>退菜原因</th>
                        </tr>
                        </thead>
                        <tbody style="height: 300px;">
                            <tr v-for="item in refundItem">
                                <td>{{item.articleName}}</td>
                                <td>{{item.unitPrice}}</td>
                                <td>{{item.refundCount}}</td>
                                <td>{{item.refundMoney}}</td>
                                <td>{{item.refundTime}}</td>
                                <td>{{item.refundRemark}}</td>
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

    var refundOrderTableAPI = null;
    //创建vue对象
    var vueObj =  new Vue({
        el:"#control",
        data:{
            searchDate : {
                beginDate : "",
                endDate : "",
            },
            refundOrderTable : {},
            refundOrderDetail : {},
            refundArticleOrderList : [],
            refundPayment : [],
            refundItem : []
        },
        created : function() {
            var date = new Date().format("yyyy-MM-dd");
            this.searchDate.beginDate = date;
            this.searchDate.endDate = date;
            this.createRefundOrderTable();
            this.searchInfo();
        },
        methods:{
            createRefundOrderTable : function(){
                var that = this;
                this.refundOrderTable = $("#refundArticleTable").DataTable({
                    lengthMenu : [ [ 50, 75, 100, -1 ], [ 50, 75, 100, "All" ] ],
                    order: [[ 2, 'desc' ]],
                    columns : [
                        {
                            title : "店铺",
                            data : "shopName",
                            s_filter: true,
                            orderable : false
                        },
                        {
                            title : "订单编号",
                            data : "orderId",
                            orderable : false
                        },
                        {
                            title : "桌号",
                            data : "tableNumber",
                            orderable : false
                        }, {
                            title : "手机号",
                            data : "telephone",
                            orderable : false
                        }, {
                            title : "用户名称",
                            data : "nickName"
                        }, {
                            title : "退菜数量",
                            data : "refundCount"
                        }, {
                            title : "退菜金额",
                            data : "refundMoney"
                        },{
                            title : "操作",
                            data : "orderId",
                            orderable : false,
                            createdCell : function(td, tdData,rowData ) {
                                var button = $("<button class='btn green'>查看详情</button>");
                                button.click(function(){
                                    that.openOrderDetailModal(tdData, rowData);
                                });
                                $(td).html(button);
                            }
                        }
                    ],
                    initComplete: function () {
                        refundOrderTableAPI = this.api();
                        that.refundOrderTableColumn();
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
                    $.post("refundArticle/getRefundArticleList", this.getDate(), function (result) {
                        if(result.success) {
                            toastr.clear();
                            toastr.success("查询成功");
                            var API = refundOrderTableAPI;
                            API.search('');
                            var orderState = API.column(3);
                            orderState.search('',true,false);
                            that.refundArticleOrderList = result.data;
                            that.refundOrderTable.clear();
                            that.refundOrderTable.rows.add(result.data).draw();
                            that.refundOrderTableColumn();
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
            createExcel : function () {
                var that = this;
                var object = that.getDate();
                object.refundArticleOrderList = that.refundArticleOrderList;
                toastr.clear();
                toastr.success("下载中...");
                try{
                   $.post("refundArticle/createExcel", object, function (result) {
                        if (result.success){
                            toastr.clear();
                            toastr.success("下载成功");
                            location.href = "refundArticle/downloadExcel?path=" + result.data;
                        }else {
                            toastr.clear();
                            toastr.error("下载出错");
                        }
                   });
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            openOrderDetailModal : function (orderId, refundOrderDetail) {
                var that = this;
                that.refundOrderDetail = refundOrderDetail;
                try {
                    $.post("refundArticle/getRefundArticleDetail",{orderId: orderId},function (result) {
                        if(result.success){
                            that.refundItem = result.data.refundItem;
                            that.refundPayment = result.data.refundPayment;
                            $("#refundOrderDetail").modal();
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
            getDate : function(){
                var data = {
                    beginDate : this.searchDate.beginDate,
                    endDate : this.searchDate.endDate
                };
                return data;
            },
            today : function(){
                var date = new Date().format("yyyy-MM-dd");
                this.searchDate.beginDate = date;
                this.searchDate.endDate = date;
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
                $("#refundOrderDetail").modal("hide");
            },
            refundOrderTableColumn : function () {
                var api = refundOrderTableAPI;
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
            }
        }
    });
</script>
