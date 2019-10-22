<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<div id="control">
    <h2 class="text-center"><strong>赠菜明细表</strong></h2><br/>
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

                <!--              <button type="button" class="btn btn-primary" @click="benxun">本询</button> -->

                <button type="button" class="btn btn-primary" @click="week">本周</button>
                <button type="button" class="btn btn-primary" @click="month">本月</button>

                <button type="button" class="btn btn-primary" @click="searchReport">查询报表</button>&nbsp;
                <button type="button" class="btn btn-primary" @click="brandreportExcel">下载报表</button><br/>
            </form>

        </div>
    </div>
    <br/>
    <br/>

    <!-- 品牌订单列表 -->
    

    <div role="tabpanel" class="tab-pane">
        <div class="panel panel-primary" style="border-color:write;">
            <!-- 品牌订单 -->
            <div class="panel panel-info">
                <div class="panel-heading text-center">
                    <strong style="margin-right:100px;font-size:22px">赠菜明细表</strong>
                    <!-- <button type="button" style="float: right;" @click="openModal(1)" class="btn btn-primary">月报表</button> -->
                </div>
                <div class="panel-body">
                    <table id="appraiseTable" class="table table-striped table-bordered table-hover" width="100%">
                    </table>
                </div>
            </div>
        </div>
    </div>
    <!-- <div class="modal fade" id="reportModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-full">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true" @click="closeModal"></button>
                </div>
                <div class="modal-body"> </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-info btn-block" data-dismiss="modal" aria-hidden="true" @click="closeModal" style="position:absolute;bottom:32px;">关闭</button>
                </div>
            </div>
        </div>
    </div> -->
    <!-- 查看详情弹窗 -->
    <div class="modal fade" id="queryCriteriaModal" tabindex="-1" role="dialog" data-backdrop="static">
        <div class="modal-dialog modal-full">
            <div class="modal-content" style="width: 40%;margin: 15% auto;">
                <div class="modal-header" style="border-bottom:initial;">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h4 align="center"><b>赠菜详情</b></h4>
                </div>
                <div class="modal-body" align="center">
                    <table class="table">
                        <tbody>
                            <tr>
                            <th scope="row">店铺：</th>
                            <td>{{currDetail.shopName}}</td>
                            </tr>
                            <tr>
                            <th scope="row">订单编号：</th>
                            <td>{{currDetail.orderId}}</td>
                            </tr>
                            <tr>
                            <th scope="row">下单时间：</th>
                            <td>{{new Date(currDetail.orderTime).format("yyyy-MM-dd hh:mm:ss")}}</td>
                            </tr>
                            <tr>
                            <th scope="row">桌号：</th>
                            <td>{{currDetail.tableNo}}</td>
                            </tr>
                            <tr>
                            <th scope="row">手机号：</th>
                            <td>{{currDetail.tel}}</td>
                            </tr>
                            <tr>
                            <th scope="row">用户昵称：</th>
                            <td>{{currDetail.nickName}}</td>
                            </tr>
                            <tr>
                            <th scope="row">赠菜数量：</th>
                            <td>{{currDetail.count}}</td>
                            </tr>
                            <tr>
                            <th scope="row">赠菜金额：</th>
                            <td>{{currDetail.money}}</td>
                            </tr>
                        </tbody>
                    </table>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                            <th>菜品名称</th>
                            <th>单价</th>
                            <th>赠菜数量</th>
                            <th>赠菜时间</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                            <td>{{currDetail.articleName}}</td>
                            <td>{{currDetail.unitPrice}}</td>
                            <td>{{currDetail.count}}</td>
                            <td>{{new Date(currDetail.grantTime).format("yyyy-MM-dd hh:mm:ss")}}</td>
                            </tr>
                            
                        </tbody>
                        </table>
                </div>
                <div class="modal-footer" style="border-top:initial;">
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<script src="assets/customer/date.js" type="text/javascript"></script>
<!-- <script src="assets/customer/components/switchBtn.js" type="text/javascript"></script> -->

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
            brandAppraise:{},
            shopAppraises:[],
            searchDate : {
                beginDate : "",
                endDate : "",
            },
            appraiseTable : {},
            years : [],
            months : ["01","02","03","04","05","06","07","08","09","10","11","12"],
            selectYear : new Date().format("yyyy"),
            selectMonth : new Date().format("MM"),
            type : 0,
            state : 1,
            currDetail: {}, // 当前行详情信息
            searchStatus: 1, // status：1今日，昨日2，本周3，本月4 ，其它0
        },
        created : function() {
            var date = new Date().format("yyyy-MM-dd");
            this.searchDate.beginDate = date;
            this.searchDate.endDate = date;
            this.createAppriseTable();
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
            // 查询当前行详情
            getDetails(itemId) {
                let that = this;
                $.post("grantArticleReport/getInfo", {itemId:itemId}, function (result) {
                    if (result ) {
                        that.currDetail = result;
                        toastr.clear();
                        toastr.success("查询成功");
                    } else {
                        toastr.clear();
                        toastr.error("查询出错");
                    }
                });
            },
            createAppriseTable : function () {
                var that = this;
                this.appraiseTable=$("#appraiseTable").DataTable({
                    lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
                    order: [[ 0, "asc" ]],
                    columns : [
                        {
                            title : "店铺名称",
                            data : "shopName"
                        },
                        {
                            title : "订单编号",
                            data : "orderId"
                        },
                        {
                            title : "桌号",
                            data : "tableNo",
                            // render:function(data){
                            //     return data+"%"
                            // }
                        },
                        {
                            title : "菜品类别",
                            data : "familyName"
                        },
                        {
                            title : "菜品名称",
                            data : "articleName"
                        },
                        {
                            title : "赠菜数量",
                            data : "count"
                        },
                        {
                            title : "赠菜金额",
                            data : "money"
                        },
                        {
                            title : "赠菜原因",
                            data : "refundMark"
                        },
                        {
                            title: "查看详情",
                            data: "itemId",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var button = $("<button class='btn green ajaxify '>查看详情</button>");
                                button.bind('click', function(e) {
                                    vueObj.getDetails(rowData.itemId);
                                    $("#queryCriteriaModal").modal();
                                    e.preventDefault();
                                    e.stopPropagation();
                                })
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
                    $.post("grantArticleReport/getGrantArticleList", this.getDate(null), function (result) {
                        if (result && result.length >= 0) {
                            
                            that.appraiseTable.clear();
                            that.appraiseTable.rows.add(result).draw();
                            toastr.clear();
                            toastr.success("查询成功");
                        } else {
                            toastr.clear();
                            toastr.error("查询出错");
                        }
                    });
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            getDate : function(shopId){
                var data = {
                    beginDate : this.searchDate.beginDate,
                    endDate : this.searchDate.endDate,
                    shopId : shopId,
                    status: this.searchStatus                   //status：1今日，昨日2，本周3，本月4 ，其它0
                };
                return data;
            },
            today : function(){
                date = new Date().format("yyyy-MM-dd");
                this.searchDate.beginDate = date
                this.searchDate.endDate = date
                this.searchStatus = 1;
                this.searchInfo();
            },
            yesterDay : function(){
                this.searchDate.beginDate = GetDateStr(-1);
                this.searchDate.endDate  = GetDateStr(-1);
                this.searchStatus = 2;
                this.searchInfo();
            },
            week : function(){
                this.searchDate.beginDate  = getWeekStartDate();
                this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
                this.searchStatus = 3;
                this.searchInfo();
            },
            month : function(){
                this.searchDate.beginDate  = getMonthStartDate();
                this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
                this.searchStatus = 4;
                this.searchInfo();
            },
            // 查询报表 按钮
            searchReport: function() {
                this.searchStatus = 0;
                this.searchInfo();
            },
            //下载
            brandreportExcel : function (){
                var that  = this;
                try{
                    var object = that.getDate(null);
                    object.brandAppraise = that.brandAppraise;
                    object.shopAppraises = that.shopAppraises;
                    // $.post("newAppraiseReport/create_brand_excel",object,function (result) {
                    //     if (result.success){
                    //         location.href="newAppraiseReport/downloadBrandExcel?path="+result.data+"";
                    //     }else{
                    //         toastr.clear();
                    //         toastr.error("下载报表出错！");
                    //     }
                    // });
                    location.href=`grantArticleReport/exprotActExcel?beginDate=\${object.beginDate}&endDate=\${object.endDate}&status=0`;
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
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
                    $.post("newAppraiseReport/createMonthDto",{year : that.selectYear, month : that.selectMonth, type : that.type},function (result) {
                        if (result.success){
                            that.state = 1;
                            window.location.href="newAppraiseReport/downloadBrandExcel?path="+result.data+"";
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
    })
</script>

