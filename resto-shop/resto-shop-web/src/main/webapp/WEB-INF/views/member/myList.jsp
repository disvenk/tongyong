<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <h2 class="text-center"><strong>会员信息报表</strong></h2>
    <div class="row" id="searchTools">
        <div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 50px;">
                    <label>开始时间：
                        <input type="text" id="beginDate" class="form-control form_datetime" :value="searchDate.beginDate" v-model="searchDate.beginDate" readonly="readonly">
                    </label>
                </div>
                <div class="form-group" style="margin-right: 50px;">
                    <label>结束时间：
                        <input type="text" id="endDate" class="form-control form_datetime" :value="searchDate.endDate" v-model="searchDate.endDate" readonly="readonly">
                    </label>
                    <button type="button" class="btn btn-primary" @click="today"> 今日</button>
                    <button type="button" class="btn btn-primary" @click="yesterDay">昨日</button>
                    <button type="button" class="btn btn-primary" @click="week">本周</button>
                    <button type="button" class="btn btn-primary" @click="month">本月</button>
                    <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>
                    <button type="button" class="btn btn-primary" @click="createExcel" v-if="state == 1">下载报表</button>
                    <button type="button" class="btn btn-default" disabled="disabled" v-if="state == 2">下载数据过多，正在生成中。请勿刷新页面</button>
                    <button type="button" class="btn btn-success" @click="download" v-if="state == 3">已完成，点击下载</button>
                    <a href="newcustomcoupon/goToGrant?intoType=1" class="btn btn-primary ajaxify">会员筛选</a>
                    <br/>
                </div>
            </form>
        </div>
    </div>
    <br/>
    <br/>

    <br/>
    <div role="tabpanel" class="tab-pane" id="orderReport">
        <div class="panel panel-primary" style="border-color:write;">
            <div class="panel panel-info">
                <div class="panel-heading text-center">
                    <strong style="margin-right:100px;font-size:22px">会员信息报表</strong>
                </div>
                <div class="panel-body">
                    <table class="table table-striped table-bordered table-hover" width="100%">
                        <thead>
                        <tr>
                            <th>品牌</th>
                            <th>用户总数	</th>
                            <th>注册用户数</th>
                            <th>未注册用户数</th>
                            <th>男性顾客</th>
                            <th>女性顾客</th>
                            <th>未知性别</th>
                        </tr>
                        </thead>
                        <tbody>
                        <template v-if="brandCustomerCount.brandName != null">
                            <tr>
                                <td><strong>{{brandCustomerCount.brandName}}</strong></td>
                                <td>{{brandCustomerCount.customerCount}}</td>
                                <td>{{brandCustomerCount.registeredCustomerCount}}</td>
                                <td>{{brandCustomerCount.unregisteredCustomerCount}}</td>
                                <td>{{brandCustomerCount.maleCustomerCount}}</td>
                                <td>{{brandCustomerCount.femaleCustomerCount}}</td>
                                <td>{{brandCustomerCount.unknownCustomerCount}}</td>
                            </tr>
                        </template>
                        <template v-else>
                            <tr>
                                <td align="center" colspan="7">
                                    暂时没有数据...
                                </td>
                            </tr>
                        </template>
                        </tbody>
                    </table>
                </div>
                <div class="panel-body">
                    <table id="customerInfoTable" class="table table-striped table-bordered table-hover"></table>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="orderModal" tabindex="-1" role="dialog" data-backdrop="static">
        <div class="modal-dialog modal-full">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true" @click="closeModal"></button>
                </div>
                <div class="modal-body" id="reportModal1"></div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-info btn-block" data-dismiss="modal" aria-hidden="true" @click="closeModal">关闭</button>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="couponModal" tabindex="-1" role="dialog" data-backdrop="static">
        <div class="modal-dialog modal-full">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true" @click="closeModal"></button>
                </div>
                <div class="modal-body"></div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-info btn-block" data-dismiss="modal" aria-hidden="true" @click="closeModal">关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="assets/customer/date.js" type="text/javascript"></script>
<script>
    //时间插件
    $('.form_datetime').datetimepicker({
        endDate: new Date(),
        minView: "month",
        maxView: "month",
        autoclose: true,//选择后自动关闭时间选择器
        todayBtn: true,//在底部显示 当天日期
        todayHighlight: true,//高亮当前日期
        format: "yyyy-mm-dd",
        startView: "month",
        language: "zh-CN"
    });

    var customerTableAPI = null;
    var vueObj = new Vue({
        el : "#control",
        data : {
            searchDate : {
                beginDate : "",
                endDate : ""
            },
            state : 1,
            path : null,
            brandCustomerCount : {},
            brandCustomer : {},
            customerInfoTable : {},
            memberUserDtos : [],
            memberList : [],
            object : {},
            length : 0,
            start : 0,
            end : 200,
            startPosition : 206,
            index : 1
        },
        created : function() {
            var date = new Date().format("yyyy-MM-dd");
            this.searchDate.beginDate = date;
            this.searchDate.endDate = date;
            this.initDataTables();
            this.searchInfo();
        },
        methods : {
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                that.customerInfoTable = $("#customerInfoTable").DataTable({
                    lengthMenu: [[50, 75, 100, 150], [50, 75, 100, "All"]],
                    order: [[ 9, 'desc' ]],
                    columns: [
                        {
                            title: "用户类型",
                            data: "isBindPhone",
                            s_filter: true,
                            orderable : false
                        },
                        {
                            title: "储值",
                            data: "isCharge",
                            s_filter: true,
                            orderable : false
                        },
                        {
                            title: "昵称",
                            data: "nickname",
                            orderable : false
                        },
                        {
                            title: "头像",
                            data: "photo",
                            defaultContent: "",
                            orderable : false,
                            createdCell: function (td, tdData) {
                                if (tdData != null && tdData.substring(0, 4) == "http") {
                                    $(td).html("<img src=\"" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:60px;width:60px;\"/>");
                                } else {
                                    $(td).html("<img src=\"/" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:60px;width:60px;\"/>");
                                }
                            }
                        },
                        {
                            title: "性别",
                            data: "sex",
                            s_filter: true,
                            orderable : false
                        },
                        {
                            title: "手机号码",
                            data: "telephone",
                            orderable : false
                        },
                        {
                            title: "生日",
                            data: "birthday",
                            orderable : false
                        },
                        {
                            title: "省/市",
                            data: "province",
                            orderable : false
                        },
                        {
                            title: "城/区",
                            data: "city",
                            orderable : false
                        },
                        {
                            title: "账户余额",
                            data: "remain"
                        },
                        {
                            title: "充值金额",
                            data: "chargeRemain"
                        },
                        {
                            title: "充值赠送金额",
                            data: "presentRemain"
                        },
                        {
                            title: "红包金额",
                            data: "redRemain"
                        },
                        {
                            title: "优惠券",
                            data: "customerId",
                            createdCell: function (td, tdData) {
                                var button = $("<button class='btn green'>查看详情</button>");
                                button.click(function () {
                                    that.openCouponModal(tdData);
                                })
                                $(td).html(button);
                            }
                        },
                        {
                            title: "订单总额",
                            data: "sumMoney"
                        },
                        {
                            title: "订单总数",
                            data: "amount"
                        },
                        {
                            title: "订单平均金额",
                            data: "money"
                        },
                        {
                            title: "订单记录",
                            data: "customerId",
                            createdCell: function (td, tdData) {
                                var button = $("<a href='member/show/orderReport?beginDate="+that.searchDate.beginDate+"&&endDate="+that.searchDate.endDate+"&&customerId="+tdData+"' class='btn green ajaxify '>查看详情</a>");
                                $(td).html(button);
                            }
                        },
                    ],
                    initComplete: function () {
                        customerTableAPI = this.api();
                        that.customerTableColumn();
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
                try{
                    var API = customerTableAPI;
                    $.post("member/myConList", this.getDate(), function(result) {
                        if(result.success == true){
                            toastr.clear();
                            toastr.success("查询成功");
                            that.brandCustomerCount = result.data.brandCustomerCount;
                            that.memberUserDtos = result.data.memberUserDtos
                            API.search('');
                            var isBindPhone = API.column(0);
                            var isCharge = API.column(1);
                            var sex = API.column(4);
                            isBindPhone.search('',true,false);
                            isCharge.search('',true,false);
                            sex.search('',true,false);
                            that.customerInfoTable.clear();
                            that.customerInfoTable.rows.add(result.data.memberUserDtos).draw();
                            that.customerTableColumn();
                        }else{
                            toastr.clear();
                            toastr.error("查询失败");
                        }
                    });
                }catch(e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            createExcel　: function () {
                var that = this;
                try {
                    that.object = this.getDate();
                    that.brandCustomer = that.brandCustomerCount;
                    that.memberList = that.memberUserDtos;
                    that.object.brandCustomerCount = that.brandCustomer;
                    if (that.memberList.length <= 200){
                        that.object.memberUserDtos = that.memberList;
                        $.post("member/member_excel",that.object,function (result) {
                            if (result.success){
                                window.location.href = "member/downloadExcel?path="+result.data+"";
                            }else{
                                toastr.clear();
                                toastr.error("下载报表出错");
                            }
                        });
                    }else{
                        that.state = 2;
                        that.length = Math.ceil(that.memberList.length/200);
                        that.object.memberUserDtos = that.memberList.slice(that.start,that.end);
                        $.post("member/member_excel",that.object,function (result) {
                            if (result.success){
                                that.object.path = result.data;
                                that.start = that.end;
                                that.end = that.start + 200;
                                that.index++;
                                that.appendExcel();
                            }else{
                                that.state = 1;
                                that.start = 0;
                                that.end = 200;
                                that.startPosition = 206;
                                that.index = 1;
                                toastr.clear();
                                toastr.error("生成报表出错");
                            }
                        });
                    }
                } catch (e){
                    that.state = 1;
                    that.start = 0;
                    that.end = 200;
                    that.startPosition = 206;
                    that.index = 1;
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试")
                }
            },
            appendExcel : function () {
                var that = this;
                try {
                    if (that.index == that.length) {
                        that.object.memberUserDtos = that.memberList.slice(that.start);
                    } else {
                        that.object.memberUserDtos = that.memberList.slice(that.start, that.end);
                    }
                    that.object.startPosition = that.startPosition;
                    $.post("member/appendExcel", that.object, function (result) {
                        if (result.success) {
                            that.start = that.end;
                            that.end = that.start + 200;
                            that.startPosition = that.startPosition + 200;
                            that.index++;
                            if (that.index - 1 == that.length) {
                                that.state = 3;
                            } else {
                                that.appendExcel();
                            }
                        } else {
                            that.state = 1;
                            that.start = 0;
                            that.end = 200;
                            that.startPosition = 206;
                            that.index = 1;
                            toastr.clear();
                            toastr.error("生成报表出错");
                        }
                    });
                }catch (e){
                    that.state = 1;
                    that.start = 0;
                    that.end = 200;
                    that.startPosition = 206;
                    that.index = 1;
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            download : function () {
                window.location.href = "member/downloadExcel?path="+this.object.path+"";
                this.state = 1;
                this.start = 0;
                this.end = 200;
                this.startPosition = 1006;
                this.index = 1;
            },
            getDate : function(){
                var data = {
                    beginDate : this.searchDate.beginDate,
                    endDate : this.searchDate.endDate,
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
            openCouponModal : function (customerId) {
                try {
                    $.post("member/show/billReport",{customerId : customerId},function (result) {
                        var modal = $("#couponModal");
                        modal.find(".modal-body").html(result);
                        modal.modal()
                    });
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            customerTableColumn : function () {
                var api = customerTableAPI;
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
