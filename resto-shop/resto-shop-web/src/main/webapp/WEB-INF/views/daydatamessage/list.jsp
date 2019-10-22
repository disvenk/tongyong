<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<h2 class="text-center"><strong>营运分析报表</strong></h2>
<div id="control">
    <div class="row" id="searchTools">
        <div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 50px;">
                    <label for="beginDate">选择日期</label>
                    <input type="text" class="form-control form_datetime" id="beginDate" v-model="date"  readonly="readonly" >
                </div>
                <button type="button" class="btn btn-primary" @click="today"> 日结报表</button>
                <button type="button" class="btn btn-primary" @click="yesterDay">昨日</button>
                <button type="button" class="btn btn-primary" @click="xun" v-if="key==2||key==3">旬结报表</button>
                <button type="button" class="btn btn-primary" @click="month" v-if="key==3">月结报表</button>
                <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>&nbsp;
                <button type="button" class="btn btn-primary" @click="downLoadExcel">下载报表</button><br/>
            </form>
        </div>
    </div>
    <br/>
    <div>
        <!-- 每日报表 -->
        <div id="report-editor">
            <div class="panel panel-success">
                <div class="panel-heading text-center">
                    <strong style="margin-right:100px;font-size:22px">店铺营运表</strong>
                </div>
                <div class="panel-body">
                    <table id="shopReportTable" class="table table-striped table-bordered table-hover" width="100%"></table>
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

    var vueObj = new Vue({
        el: "#control",
        data: {
                date:'',
                type:'',//向后台传参数
                key:'',//用来判断是否显示月或者旬
                tb:{}//datatable实例
        },
        created: function () {
            this.date = new Date().format("yyyy-MM-dd");
            this.type=1;
            //初始化datatable
            this.initDatatable();
        },
        watch:{
            date : function (newValue, oldValue) {
                this.key=1;//初始化
                console.log("date日期为："+newValue);
                //要显示旬则必须是月末 或者 日期是10和20
                //先判断本日的月份
                var todayMonth = newValue.substring(5,7);
                console.log("当前选择的月份："+todayMonth);

                var todayDay = newValue.substring(8,10);
                console.log("当前选择日期的日份:"+todayDay);

                //获取明日
                var tomorrow = this.getDateStr(newValue,1);
                console.log("第二天日期为："+tomorrow)
                //获取明日的月份
                var tomorrowMonth = tomorrow.substring(5,7);
                console.log("第二天的月份是："+tomorrowMonth);
                if(todayMonth!=tomorrowMonth){//
                    this.key=3
                }else if(todayDay==10||todayDay==20){//说明是旬
                    this.key=2
                }


                console.log("key值为:"+this.key);
            }
        },
        methods: {
            initDatatable:function () {
                var that = this;
                that.tb=$("#shopReportTable").DataTable({
                    ajax: {
                        url: "daydatamessage/getShopData",
                        data:function(d){
                                d.date=that.date,
                                d.type = that.type;
                        }
                    },
                    order: [[ 0, "asc" ]],
                    columns : [
                        {
                            title : "店铺名称",
                            data : "shopName"
                        },
                        {
                            title : "用户消费比率",
                            data : "customerOrderRatio"
                        },
                        {
                            title : "回头消费比率",
                            data : "backCustomerOrderRatio"
                        },
                        {
                            title : "新增用户比率",
                            data : "newCustomerOrderRatio"
                        },
                        {
                            title : "新用户消费",
                            data : "newCuostomerOrderNum"
                        },
                        {
                            title : "自然用户",
                            data : "newNormalCustomerOrderNum"
                        },
                        {
                            title : "分享用户",
                            data : "newShareCustomerOrderNum"
                        },

                        {
                            title : "回头用户消费",
                            data : "backCustomerOrderNum"
                        },
                        {
                            title : "二次回头",
                            data : "backTwoCustomerOrderNum"
                        },
                        {
                            title : "多次回头",
                            data : "backTwoMoreCustomerOrderNum"
                        }
                    ]
                });
            },
            searchInfo:function () {
                toastr.clear();
                toastr.success("查询中...");
                var that = this;
                try {
                    $.post("daydatamessage/getShopData", this.getParam(), function (result) {
                        if(result.success) {
                            that.tb.clear();
                            that.tb.rows.add(result.data).draw();
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
            downLoadExcel: function () {
                try {
                    $.post("daydatamessage/create_dayMessage",this.getParam(),function (result) {//生成营运分析报表
                        if(result.success){
                            window.location.href = "orderReport/downloadBrandOrderExcel?path="+result.data+"";//下载营运分析报表
                        }else{
                            toastr.clear();
                            toastr.error("生成报表出错");
                        }
                    });
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }

                $.post("daydatamessage/downLoadExcel", this.getParam(), function (result) {
                    if(result.success) {
                        toastr.clear();
                        toastr.success("下载成功");
                    }else{
                        toastr.clear();
                        toastr.error("下载出错");
                    }
                });
            },
            getParam:function () {
                var data = {
                    date : this.date,
                    type : this.type
                };
                return data;
            },
            today : function(){
                this.date = new Date().format("yyyy-MM-dd");
                this.type=1
                this.searchInfo();
            },
            yesterDay : function(){
                this.date =this.getDateStrDay(-1);
                this.type=1
                this.searchInfo();
            },
            xun : function(){
                this.type=2
                this.searchInfo();
            },
            month:function () {
               this.type=3
                this.searchInfo();
            },
            getDateStr:function (day,AddDayCount) {
                var dd = new Date(day);
                dd.setDate(dd.getDate()+AddDayCount+1);//获取AddDayCount天后的日期
                var y = dd.getFullYear();
                var m = dd.getMonth()+1;//获取当前月份的日期
                var d = dd.getDate();
                var r = y+"-"+m+"-"+d;
                r=new Date(r).format("yyyy-MM-dd");
                return r
            },
            getDateStrDay:function (AddDayCount) {
                var dd = new Date();
                dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
                var y = dd.getFullYear();
                var m = dd.getMonth()+1;//获取当前月份的日期
                var d = dd.getDate();
                var r = y+"-"+m+"-"+d;
                return new Date(r).format("yyyy-MM-dd");

            }

        }

    });



</script>
