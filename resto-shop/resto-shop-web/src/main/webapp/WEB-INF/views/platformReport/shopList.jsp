<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<div id="control">
    <h2 class="text-center"><strong>第三方外卖报表</strong></h2><br/>
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

                <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>&nbsp;
                <button type="button" class="btn btn-primary" @click="brandreportExcel">下载报表</button><br/>
            </form>

        </div>
    </div>
    <br/>
    <br/>

    <div role="tabpanel" class="tab-pane">
        <div class="panel panel-primary" style="border-color:write;">
            <!-- 品牌订单 -->
            <div class="panel panel-info">
                <div class="panel-heading text-center">
                    <strong style="margin-right:100px;font-size:22px">店铺外卖订单表</strong>
                </div>
                <div class="panel-body">
                    <table id="appraiseTable" class="table table-striped table-bordered table-hover" width="100%">
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="reportModal" tabindex="-1" role="dialog" aria-hidden="true">
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
            shopAppraises:[],
            searchDate : {
                beginDate : "",
                endDate : "",
            },
            appraiseTable : {}
        },
        created : function() {
            var date = new Date().format("yyyy-MM-dd");
            this.searchDate.beginDate = date;
            this.searchDate.endDate = date;
            this.createAppriseTable();
            this.searchInfo();
        },
        methods:{
            createAppriseTable : function () {
                var that = this;
                this.appraiseTable=$("#appraiseTable").DataTable({
                    lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
                    order: [[ 0, "asc" ]],
                    columns : [
                        {
                            title : "店铺",
                            data : "shopName"
                        },
                        {
                            title : "外卖订单数",
                            data : "allCount"
                        },
                        {
                            title : "饿了么订单数",
                            data : "elmCount"
                        },
                        {
                            title : "美团外卖订单数",
                            data : "mtCount"
                        },
                        {
                            title : "外卖订单额",
                            data : "allPrice"
                        },
                        {
                            title : "外卖订单实收额",
                            data : "totalPrice"
                        },
                        {
                            title : "饿了么订单额",
                            data : "elmPrice"
                        },
                        {
                            title : "饿了么订单实收额",
                            data : "elmTotalPrice"
                        },
                        {
                            title : "美团外卖订单额",
                            data : "mtPrice"
                        },
                        {
                            title : "美团外卖订单实收额",
                            data : "mtTotalPrice"
                        },
                        {
                            title: "查看详情",
                            data: "shopId",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var button = $("<a href='platformReport/shopReport?beginDate="+that.searchDate.beginDate+"&&endDate="+that.searchDate.endDate+"&&shopId="+tdData+"&&type=1' class='btn green ajaxify '>查看详情</a>");
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
                    $.post("platformReport/datas", this.getDate(null), function (result) {
                        if (result.success) {
                            that.shopAppraises = result.data.platformlist;
                            that.appraiseTable.clear();
                            that.appraiseTable.rows.add(result.data.platformlist).draw();
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
                    shopId : shopId
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
            //下载
            brandreportExcel : function (){
                var that  = this;
                try{
                    var object = that.getDate(null);
                    object.shopAppraises = that.shopAppraises;
                    $.post("platformReport/create_brand_excel",object,function (result) {
                        if (result.success){
                            location.href="platformReport/downloadBrandExcel?path="+result.data+"";
                        }else{
                            toastr.clear();
                            toastr.error("下载报表出错！");
                        }
                    });
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            }
        }
    })
</script>

