<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<div id="control">
    <a class="btn btn-info ajaxify" href="brandMarketing/couponList">
        <span class="glyphicon glyphicon-circle-arrow-left"></span>
        返回
    </a>
    <h2 class="text-center">
        <strong>
            ${!empty couponType ? "产品券" : "现金券"}发放详情
        </strong>
    </h2>
    <br />
    <div class="row">
        <div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 50px;">
                    <label>发放周期：
                        <input type="text" class="form-control form_datetime" :value="grantSearchDate.beginDate" v-model="grantSearchDate.beginDate" readonly="readonly">
                    </label>
                    &nbsp;至&nbsp;
                    <label>
                        <input type="text" class="form-control form_datetime" :value="grantSearchDate.endDate" v-model="grantSearchDate.endDate" readonly="readonly">
                    </label>
                </div>
                <div class="form-group" style="margin-right: 50px;">
                    <button type="button" class="btn btn-primary" @click="today"> 今日</button>
                    <button type="button" class="btn btn-primary" @click="yesterDay">昨日</button>
                    <button type="button" class="btn btn-primary" @click="week">本周</button>
                    <button type="button" class="btn btn-primary" @click="month">本月</button>
                    <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>&nbsp;
                    <button type="button" class="btn btn-primary" @click="download">下载报表</button>
                </div>
            </form>
        </div>
    </div>
    <br />
    <br />
    <div class="panel panel-info">
        <div class="panel-heading text-center" style="font-size: 22px;">
            <strong>
                ${!empty couponType ? "产品券" : "现金券"}发放记录
            </strong>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-bordered table-hover"
                   id="couponInfoTable">
            </table>
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

    var couponTableAPI = null;
    var newCustomCouponId = "${newCustomCouponId}";
    var beginDate = "${beginDate}";
    var endDate = "${endDate}";
    //创建vue对象
    var vueObj =  new Vue({
        el:"#control",
        data:{
            grantSearchDate : {
                beginDate : "",
                endDate : ""
            },
            couponInfoTable:{},
            couponInfoList:[]
        },
        created : function() {
            this.grantSearchDate.beginDate = beginDate;
            this.grantSearchDate.endDate = endDate;
            this.createCouponInfoTable();
            this.searchInfo();
        },
        methods:{
            createCouponInfoTable : function(){
                var that = this;
                this.couponInfoTable = $("#couponInfoTable").DataTable({
                    lengthMenu : [ [ 50, 75, 100, -1 ], [ 50, 75, 100, "All" ] ],
                    columns : [
                        {
                            title : "发放渠道",
                            data : "couponSource",
                            orderable : false
                        },
                        {
                            title : "名称",
                            data : "couponName",
                            orderable : false
                        },
                        {
                            title : "使用状态",
                            data : "couponState",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "发放金额",
                            data : "couponValue"
                        },
                        {
                            title : "用户手机号",
                            data : "telephone",
                            orderable : false
                        },
                        {
                            title : "领取时间",
                            data : "addTime"
                        },
                        {
                            title : "使用时间",
                            data : "useTime"
                        },
                        {
                            title : "优惠券使用店铺",
                            data : "useShopName",
                            orderable : false
                        }
                        <c:if test="${!empty couponType}">
                        ,
                        {
                            title : "抵用产品",
                            data : "articleName"
                        }
                        </c:if>
                        ,
                        {
                            title : "抵用金额",
                            data : "useCouponValue"
                        }
                    ],
                    initComplete: function () {
                        couponTableAPI = this.api();
                        that.couponTableColumn();
                    }
                });
            },
            searchInfo : function() {
                var that = this;
                var timeCha = new Date(that.grantSearchDate.endDate).getTime() - new Date(that.grantSearchDate.beginDate).getTime();
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
                    $.post("brandMarketing/couponInfoList", this.getDate(), function (result) {
                        if(result.success) {
                            toastr.clear();
                            toastr.success("查询成功");
                            var API = couponTableAPI;
                            API.search('');
                            var couponState = API.column(2);
                            couponState.search('',true,false);
                            that.couponInfoList = result.data.couponInfoList;
                            that.couponInfoTable.clear();
                            that.couponInfoTable.rows.add(result.data.couponInfoList).draw();
                            that.couponTableColumn();
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
            download : function(){
                var that = this;
                var object = that.getDate();
                object.couponInfoList = that.couponInfoList;
                $.post("brandMarketing/createCouponInfoExcel", object, function (result) {
                    if (result.success) {
                        toastr.clear();
                        toastr.success("下载成功");
                        window.location.href = "brandMarketing/downloadCouponDto?path="+result.data+"";
                    }else {
                        toastr.clear();
                        toastr.error("下载失败");
                    }
                });
            },
            getDate : function(){
                var data = {
                    beginDate : this.grantSearchDate.beginDate,
                    endDate : this.grantSearchDate.endDate,
                    newCustomCouponId : newCustomCouponId
                };
                return data;
            },
            today : function(){
                var date = new Date().format("yyyy-MM-dd");
                this.grantSearchDate.beginDate = date
                this.grantSearchDate.endDate = date
                this.searchInfo();
            },
            yesterDay : function(){
                this.grantSearchDate.beginDate = GetDateStr(-1);
                this.grantSearchDate.endDate  = GetDateStr(-1);
                this.searchInfo();
            },
            week : function(){
                this.grantSearchDate.beginDate  = getWeekStartDate();
                this.grantSearchDate.endDate  = new Date().format("yyyy-MM-dd")
                this.searchInfo();
            },
            month : function(){
                this.grantSearchDate.beginDate  = getMonthStartDate();
                this.grantSearchDate.endDate  = new Date().format("yyyy-MM-dd")
                this.searchInfo();
            },
            couponTableColumn : function () {
                var api = couponTableAPI;
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
