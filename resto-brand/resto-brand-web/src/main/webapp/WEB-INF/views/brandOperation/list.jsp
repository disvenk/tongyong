<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="control">
    <h2 class="text-center"><strong>品牌运营报表</strong></h2><br/>
    <div class="row" id="searchTools">
        <div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 50px;">
                    <label>开始时间：
                        <input type="text" class="form-control form_datetime" :value="searchDate.beginDate" v-model="searchDate.beginDate" readonly="readonly">
                    </label>
                </div>
                <div class="form-group" style="margin-right: 50px;">
                    <label>结束时间：
                        <input type="text" class="form-control form_datetime" :value="searchDate.endDate" v-model="searchDate.endDate" readonly="readonly">
                    </label>
                    <button type="button" class="btn btn-primary" :disabled="disabled" @click="today"> 今日</button>
                    <button type="button" class="btn btn-primary" :disabled="disabled" @click="yesterDay">昨日</button>
                    <button type="button" class="btn btn-primary" :disabled="disabled" @click="week">本周</button>
                    <button type="button" class="btn btn-primary" :disabled="disabled" @click="month">本月</button>
                    <button type="button" class="btn btn-primary" :disabled="disabled" @click="searchInfo">查询报表</button>
                    <button type="button" class="btn btn-primary" :disabled="disabled" @click="downloadDto">下载报表</button>
                    <br/>
                </div>
            </form>
            <div>
                <br/>
                <br/>
                <div>
                    <div class="tab-content">
                        <div role="tabpanel" class="tab-pane active">
                            <div class="panel panel-success">
                                <div class="panel-heading text-center">
                                    <strong style="margin-right:100px;font-size:22px">
                                        品牌总计
                                    </strong>
                                </div>
                                <div class="panel-body">
                                    <table class="table table-striped table-bordered table-hover" style="width: 100%">
                                        <thead>
                                        <tr>
                                            <th>品牌数</th>
                                            <th>门店数</th>
                                            <th>订单总数</th>
                                            <th>订单总额</th>
                                            <th>微信支付</th>
                                            <th>支付宝支付</th>
                                            <th>就餐人数</th>
                                            <th>新增用户</th>
                                            <th>新注册用户</th>
                                            <th>充值次数</th>
                                            <th>充值金额</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr v-if="brandOperationCount.brandCount != null">
                                            <td>{{brandOperationCount.brandCount}}</td>
                                            <td>{{brandOperationCount.shopCount}}</td>
                                            <td>{{brandOperationCount.orderCount}}</td>
                                            <td>{{brandOperationCount.orderMoney}}</td>
                                            <td>{{brandOperationCount.weChatPay}}</td>
                                            <td>{{brandOperationCount.aliPay}}</td>
                                            <td>{{brandOperationCount.customerCount}}</td>
                                            <td>{{brandOperationCount.newCustomerCount}}</td>
                                            <td>{{brandOperationCount.newRegiestCustomerCount}}</td>
                                            <td>{{brandOperationCount.chargeCount}}</td>
                                            <td>{{brandOperationCount.chargeMoney}}</td>
                                        </tr>
                                        <tr v-else>
                                            <td align="center" colspan="11">暂时没有数据...</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                        </div>
                    </div>

                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs" role="tablist" id="ulTab">
                        <li role="presentation" class="active" @click="chooseType(1)">
                            <a href="#dayReport" aria-controls="dayReport" role="tab" data-toggle="tab">
                                <strong>品牌运营表</strong>
                            </a>
                        </li>
                        <li role="presentation" @click="chooseType(2)">
                            <a href="#revenueCount" aria-controls="revenueCount" role="tab" data-toggle="tab">
                                <strong>店铺运营表</strong>
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div role="tabpanel" class="tab-pane active" id="dayReport">
                            <div class="panel panel-success">
                                <div class="panel-heading text-center">
                                    <strong style="margin-right:100px;font-size:22px">品牌运营表</strong>
                                </div>
                                <div class="panel-body">
                                    <table id="brandOperationDtoTable" class="table table-striped table-bordered table-hover"
                                           style="width: 100%;">
                                    </table>
                                </div>
                            </div>
                        </div>

                        <div role="tabpanel" class="tab-pane" id="revenueCount">
                            <div class="panel panel-primary" style="border-color:white;">
                                <div class="panel panel-info">
                                    <div class="panel-heading text-center">
                                        <strong style="margin-right:100px;font-size:22px">店铺运营表</strong>
                                    </div>
                                    <div class="panel-body">
                                        <table id="shopOperationDtoTable" class="table table-striped table-bordered table-hover"
                                               style="width: 100%;">
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
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

    var shopOperationAPI = null;
    var brandOperationAPI = null;
    var vueObj = new Vue({
        el : "#control",
        data : {
            brandOperationCount : {},
            searchDate : {
                beginDate : "",
                endDate : "",
            },
            currentType:1,//当前选中页面
            brandOperationDtoTable:{},
            shopOperationDtoTable:{},
            brandOperationDtos:[],
            shopOperationDtos:[],
            disabled:true
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
                that.brandOperationDtoTable=$("#brandOperationDtoTable").DataTable({
                    lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
                    columns : [
                        {
                            title : "品牌",
                            data : "brandName",
                            orderable : false
                        },
                        {
                            title : "订单总数",
                            data : "orderCount"
                        },
                        {
                            title : "订单总额",
                            data : "orderMoney"
                        },
                        {
                            title : "微信支付",
                            data : "weChatPay"
                        },
                        {
                            title : "支付宝支付",
                            data : "aliPay"
                        },
                        {
                            title : "就餐人数",
                            data : "customerCount"
                        },
                        {
                            title : "新增用户",
                            data : "newCustomerCount"
                        },
                        {
                            title : "新注册用户",
                            data : "newRegiestCustomerCount"
                        },
                        {
                            title:"充值次数" ,
                            data:"chargeCount"
                        },
                        {
                            title:"充值金额" ,
                            data:"chargeMoney"
                        }
                    ],
                    initComplete: function () {
                        brandOperationAPI = this.api();
                    }
                });

                that.shopOperationDtoTable=$("#shopOperationDtoTable").DataTable({
                    lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
                    columns : [
                        {
                            title : "品牌",
                            data : "brandName",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "店铺",
                            data : "shopName",
                            orderable : false
                        },
                        {
                            title : "订单总数",
                            data : "orderCount"
                        },
                        {
                            title : "订单总额",
                            data : "orderMoney"
                        },
                        {
                            title : "微信支付",
                            data : "weChatPay"
                        },
                        {
                            title : "支付宝支付",
                            data : "aliPay"
                        },
                        {
                            title : "就餐人数",
                            data : "customerCount"
                        },
                        {
                            title : "新增用户",
                            data : "newCustomerCount"
                        },
                        {
                            title : "新注册用户",
                            data : "newRegiestCustomerCount"
                        },
                        {
                            title:"充值次数" ,
                            data:"chargeCount"
                        },
                        {
                            title:"充值金额" ,
                            data:"chargeMoney"
                        }
                    ],
                    initComplete: function () {
                        shopOperationAPI = this.api();
                        that.shopOperationColumns();
                    }
                });
            },
            chooseType:function (type) {
                this.currentType= type;
            },
            searchInfo : function(flg) {
                this.disabled = true;
                toastr.clear();
                toastr.success("查询中...");
                try{
                    var that = this;
                    $.post("brandOperation/selectBrandOperationData", this.getDate(), function(result) {
                        if(result.success == true){
                            that.disabled = false;
                            var shopAPI = shopOperationAPI;
                            var brandAPI = brandOperationAPI;
                            //清空datatable的搜索条件
                            shopAPI.search('');
                            brandAPI.search('');
                            var column = shopAPI.column(0);
                            column.search('', true, false);
                            //清空表格
                            that.shopOperationDtoTable.clear().draw();
                            that.brandOperationDtoTable.clear().draw();
                            that.shopOperationDtoTable.rows.add(result.data.shopOperationDtos).draw();
                            that.shopOperationDtos = result.data.shopOperationDtos;
                            that.brandOperationDtoTable.rows.add(result.data.brandOperationDtos).draw();
                            that.brandOperationDtos = result.data.brandOperationDtos;
                            that.brandOperationCount = result.data.brandOperationCount;
                            //重绘搜索列
                            that.shopOperationColumns();
                            toastr.clear();
                            toastr.success("查询成功");
                        }else{
                            that.disabled = false;
                            toastr.clear();
                            toastr.error("查询品牌运营数据出错!");
                        }
                    });
                }catch(e){
                    that.disabled = false;
                    toastr.clear();
                    toastr.error("查询品牌运营数据出错!");
                    return;
                }
            },
            getDate : function(){
                var data = {
                    beginDate : this.searchDate.beginDate,
                    endDate : this.searchDate.endDate
                };
                return data;
            },
            downloadDto : function(){
                var that = this;
                switch(that.currentType){
                    case 1:
                        $.post("brandOperation/createBrandOperationDto",{
                            "beginDate" : that.searchDate.beginDate,
                            "endDate" : that.searchDate.endDate,
                            "brandOperationCount" : that.brandOperationCount,
                            "brandOperationDtos" : that.brandOperationDtos
                        },function(result){
                            if (result.success){
                                window.location.href = "brandOperation/downloadBrandOperationDto?path="+result.data+"";
                            }else{
                                toastr.clear();
                                toastr.error("下载品牌运营报表出错!");
                            }
                        });
                        break;
                    case 2:
                        $.post("brandOperation/createShopOperationDto",{
                            "beginDate" : that.searchDate.beginDate,
                            "endDate" : that.searchDate.endDate,
                            "brandOperationCount" : that.brandOperationCount,
                            "shopOperationDtos" : that.shopOperationDtos
                        },function(result){
                            if (result.success){
                                window.location.href = "brandOperation/downloadShopOperationDto?path="+result.data+"";
                            }else{
                                toastr.clear();
                                toastr.error("下载品牌运营报表出错!");
                            }
                        });
                        break;
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
            shopOperationColumns : function(){
                var api = shopOperationAPI;
                api.search('');
                var data = api.data();
                var columnsSetting = api.settings()[0].oInit.columns;
                $(columnsSetting).each(function (i) {
                    if (this.s_filter) {
                        var column = api.column(i);
                        var title = this.title;
                        var select = $('<select id=""><option value="">' + this.title + '(全部)</option></select>');
                        var that = this;
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

    function Trim(str)
    {
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }
</script>

