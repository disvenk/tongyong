<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<div id="control">
    <h2 class="text-center"><strong>订单报表</strong></h2><br/>
    <div class="row" id="searchTools">
        <div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 50px;">
                    <label for="beginDate">开始时间：</label>
                    <input type="text" class="form-control form_datetime" id="beginDate" v-model="searchDate.beginDate"   readonly="readonly">
                </div>
                <div class="form-group" style="margin-right: 50px;">
                    <label for="endDate">结束时间：</label>
                    <input type="text" class="form-control form_datetime" id="endDate" v-model="searchDate.endDate"   readonly="readonly">
                </div>
                <button type="button" class="btn btn-primary" @click="today"> 今日</button>
                <button type="button" class="btn btn-primary" @click="yesterDay">昨日</button>
                <button type="button" class="btn btn-primary" @click="week">本周</button>
                <button type="button" class="btn btn-primary" @click="month">本月</button>
                <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>&nbsp;
                <button type="button" class="btn btn-primary" @click="createOrderExcel">下载报表</button><br/>
            </form>

        </div>
    </div>
    <br/>
    <br/>

    <!-- 品牌订单列表 -->
    <div role="tabpanel" class="tab-pane" id="orderReport">
        <div class="panel panel-primary" style="border-color:write;">
            <!-- 品牌订单 -->
            <div class="panel panel-info">
                <div class="panel-heading text-center">
                    <strong style="margin-right:100px;font-size:22px">店铺订单报表</strong>
                    <button type="button" style="float: right;" @click="openModal(1)" class="btn btn-primary">月报表</button>
                </div>
                <div class="panel-body">
                    <table id="shopOrderTable" class="table table-striped table-bordered table-hover" width="100%">
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- 月报表弹窗 -->
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

<script src="assets/customer/date.js" type="text/javascript"></script>

<script>

    //时间插件
    $('.form_datetime').datetimepicker({
        endDate:new Date(),
        minView:"month",
        maxView:"month",
        autoclose:true,//选择后自动关闭时间选择器
        todayBtn:true,//在底部显示 当天日期
        todayHighlight:true,//高亮当前日期
        format:"yyyy-mm-dd",
        startView:"month",
        language:"zh-CN"
    });


    //创建vue对象
    var vueObj =  new Vue({
        el:"#control",
        data:{
            shopOrderList : [],
            searchDate : {
                beginDate : "",
                endDate : "",
            },
            shopOrderTable : {},
            years : [],
            months : ["01","02","03","04","05","06","07","08","09","10","11","12"],
            selectYear : new Date().format("yyyy"),
            selectMonth : new Date().format("MM"),
            type : 0,
            state : 1
        },
        created : function() {
            var date = new Date().format("yyyy-MM-dd");
            this.searchDate.beginDate = date;
            this.searchDate.endDate = date;
            this.createShopOrderTable();
            this.searchInfo();
            this.getYears();
        },
        methods:{
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
            createShopOrderTable : function(){
                //that代表 vue对象
                var that = this;
                //datatable对象
                that.shopOrderTable=$("#shopOrderTable").DataTable({
                    lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
                    order: [[ 0, "asc" ]],
                    columns : [
                        {
                            title : "店铺",
                            data : "shopName"
                        },
                        {
                            title : "订单总数",
                            data : "shop_orderCount"
                        },
                        {
                            title : "订单总额",
                            data : "shop_orderPrice"
                        },
                        {
                            title : "单均",
                            data : "shop_singlePrice"
                        },
                        {
                            title : "就餐人数",
                            data : "shop_peopleCount"
                        },
                        {
                            title : "人均",
                            data : "shop_perPersonPrice"
                        },
                        {
                            title : "堂吃订单数",
                            data : "shop_tangshiCount"
                        },
                        {
                            title : "堂吃订单额",
                            data : "shop_tangshiPrice"
                        },
                        {
                            title : "外带订单数",
                            data : "shop_waidaiCount"
                        },
                        {
                            title : "外带订单额",
                            data : "shop_waidaiPrice"
                        },
                        {
                            title : "R+外卖订单数",
                            data : "shop_waimaiCount"
                        },
                        {
                            title : "R+外卖订单额",
                            data : "shop_waimaiPrice"
                        },
                        {
                            title: "操作",
                            data: "shopDetailId",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var shopName = rowData.shopName;
                                var button = $("<a href='orderReport/show/shopReport?beginDate="+that.searchDate.beginDate+"&&endDate="+that.searchDate.endDate+"&&shopId="+tdData+"&&shopName="+shopName+"&&type=1' class='btn green ajaxify '>查看详情</a>");
                                $(td).html(button);
                            }
                        }
                    ]
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
                    $.post("orderReport/brand_data", this.getDate(), function (result) {
                        if(result.success) {
                            that.shopOrderTable.clear();
                            that.shopOrderTable.rows.add(result.data.result.shopId).draw();
                            that.shopOrderList = result.data.result.shopId;
                            toastr.clear();
                            toastr.success("查询成功");
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
            createOrderExcel : function () {
                var object = {
                    beginDate : this.searchDate.beginDate,
                    endDate : this.searchDate.endDate,
                    shopOrderDtos : this.shopOrderList
                };
                try {
                    $.post("orderReport/create_brand_excel",object,function (result) {
                        if(result.success){
                            window.location.href = "orderReport/downloadBrandOrderExcel?path="+result.data+"";
                        }else{
                            toastr.clear();
                            toastr.error("生成报表出错");
                        }
                    });
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
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
            getDays:function (beginDate,endDate) {
                var strSeparator = "-"; //日期分隔符
                var oDate1;
                var oDate2;
                var iDays;
                oDate1= beginDate.split(strSeparator);
                oDate2= endDate.split(strSeparator);
                var strDateS = new Date(oDate1[0], oDate1[1]-1, oDate1[2]);
                var strDateE = new Date(oDate2[0], oDate2[1]-1, oDate2[2]);
                iDays = parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24)//把相差的毫秒数转换为天数
                return iDays ;
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
                    $.post("orderReport/createMonthDto",{year : that.selectYear, month : that.selectMonth, type : that.type},function (result) {
                        if (result.success){
                            that.state = 1;
                            window.location.href="orderReport/downloadBrandOrderExcel?path="+result.data+"";
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
            }
        }
    });
</script>

