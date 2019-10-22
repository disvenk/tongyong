<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="controlShop">
    <a class="btn btn-info ajaxify" href="recharge/list">
        <span class="glyphicon glyphicon-circle-arrow-left"></span>
        返回
    </a>
    <h2 class="text-center">
        <strong>${shopName}</strong>
    </h2>
    <br />
    <div class="row">
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
                <button type="button" class="btn btn-primary" @click="createExcel">下载报表</button>
            </form>
        </div>
    </div>
    <br /> <br />
    <!-- 店铺订单列表  -->
    <div class="panel panel-info">
        <div class="panel-heading text-center" style="font-size: 22px;">
            <strong>店铺充值记录</strong>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-bordered table-hover" id="shopChargeTable">
            </table>
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

    var beginDate = "${beginDate}";
    var endDate = "${endDate}";
    var shopId = "${shopDetailId}";
    var vueObjShop = new Vue({
        el : "#controlShop",
        data : {
            searchDate : {
                beginDate : "",
                endDate : "",
            },
            shopChargeTable : {}
        },
        created : function() {
            this.searchDate.beginDate = beginDate;
            this.searchDate.endDate = endDate;
            this.initDataTables();
            this.searchInfo();
        },
        methods : {
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                that.shopChargeTable = $("#shopChargeTable").DataTable({
                    lengthMenu: [ [50, 75, 100, 150], [50, 75, 100, "All"] ],
                    order: [[ 0, "desc" ]],
                    columns : [ {
                        title : "店铺",
                        data : "chargelog.shopName"
                    },

                        {
                            title : "充值方式",
                            data : "type",
                            createdCell : function(td, tdData) {
                                if(tdData==1){
                                    $(td).html("<span >微信充值</span>");
                                }else{
                                    $(td).html("<span >pos充值</span>");
                                }
                            }
                        }, {
                            title : "充值手机",
                            data : "chargelog.customerPhone",
                            createdCell:function(td,tdData){
                                if(tdData=="" || tdData==null){
                                    $(td).html("<span class='label label-danger'>没有注册</span>");
                                }
                            }
                        }, {
                            title : "充值金额（元）",
                            data : "chargeMoney"
                        },
                        {
                            title : "充值赠送金额(元)",
                            data : "rewardMoney"
                        },
                        {
                            title : "充值时间",
                            data : "finishTime",
                            createdCell:function (td,tdData,row) {
                                $(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm:ss"));
                            }

                        },{
                            title : "操作人手机",
                            data : "chargelog.operationPhone",
                            createdCell:function(td,tdData){
                                if(tdData=="" || tdData==null){
                                    $(td).html("<span class='label label-danger'>没有填写</span>");
                                }
                            }
                        } ]
                });
            },
            searchInfo : function() {
                try{
                    var that = this;
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
                    $.post("recharge/queryShopchargecord", this.getDate(), function(result) {
                        if (result.success) {
                            that.shopChargeTable.clear();
                            that.shopChargeTable.rows.add(result.data.result).draw();
                            toastr.clear();
                            toastr.success("查询成功");
                        }else {
                            toastr.clear();
                            toastr.error("查询出错");
                        }
                    });
                }catch(e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            getDate : function(){
                var data = {
                    beginDate : this.searchDate.beginDate,
                    endDate : this.searchDate.endDate,
                    shopId : shopId
                };
                return data;
            },
            createExcel : function(){
                var that = this;
                try {
                    location.href ="recharge/shopDetail_excel?shopDetailId="+ shopId
                            +"&&beginDate="+ this.searchDate.beginDate+"&&endDate="+this.searchDate.endDate;
                }catch (e) {
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            download : function(){
                window.location.href = "ChargeReport/downloadShopExcel?path="+this.object.path+"";
                this.state = 1;
                this.start = 0;
                this.end = 200;
                this.startPosition = 205;
                this.index = 1;
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
            keysert: function (key) {
                return function(a,b){
                    var value1 = a[key];
                    var value2 = b[key];
                    return value1 - value2;
                }
            }
        }
    });
</script>

