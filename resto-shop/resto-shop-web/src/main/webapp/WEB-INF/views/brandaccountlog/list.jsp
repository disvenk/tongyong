<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<h2 class="text-center"><strong>账户流水</strong></h2><br/>
	<div class="row" id="searchTools">
		<div class="col-md-12">
			<form class="form-inline">
				<div class="form-group" style="margin-right: 50px;">
					<label for="beginDate">开始时间：</label>
					<input type="text" class="form-control form_datetime" id="beginDate" v-model="searchDate.beginDate"   readonly>
				</div>
				<div class="form-group" style="margin-right: 50px;">
					<label for="endDate">结束时间：</label>
					<input type="text" class="form-control form_datetime" id="endDate" v-model="searchDate.endDate"   readonly>
				</div>
				<button type="button" class="btn btn-primary" @click="today"> 今日</button>
				<button type="button" class="btn btn-primary" @click="yesterDay">昨日</button>
				<button type="button" class="btn btn-primary" @click="week">本周</button>
				<button type="button" class="btn btn-primary" @click="month">本月</button>
				<button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>&nbsp;
				<button type="button" class="btn btn-primary" @click="createAccountLogExcel">下载报表</button><br/>
			</form>

		</div>
	</div>
	</div>

	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="brandaccountlog/add">
				<button class="btn green pull-right" @click="create">新建</button>
			</s:hasPermission>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter">&nbsp;</div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered "></table>
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

    (function(){
        var brandAccountApi; //dataTable的 api对象
        var vueObj = new Vue({
            el:"#control",
            data:{
                searchDate : {
                    beginDate : '',
                    endDate : ''
                },
				brandAccountLogTable:{}
            },
            created : function() {
                var date = new Date().format("yyyy-MM-dd");
                this.searchDate.beginDate = date;
                this.searchDate.endDate = date;
                this.createdDataTable();//初始化创建dataTable
				this.searchInfo()
            },

			methods:{
                createdDataTable:function () {
                    //that代表 vue对象
                    var that = this;
                    //datatable对象
                    that.brandAccountLogTable=$(".table-body>table").DataTable({
                        lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
                        order: [[ 0, "desc" ],[6,"asc"]],
                        columns: [
                            {
                                title: "时间",
                                data: "createTime",
                                createdCell: function (td, tdData) {
                                    $(td).html(vueObj.formatDate(tdData))
                                }
                            },
                            {
                                title: "主体",
                                data: "groupName",
                            },
                            {
                                title: "行为",
                                data: "behavior",
                                createdCell: function (td, tdData) {
                                    $(td).html(vueObj.BehaviorName(tdData))
                                }
                            },
                            {
                                title: "流水号",
                                data: "serialNumber"
                            },
                            {
                                title: "详情",
                                data: "detail",
                                createdCell: function (td, tdData) {
                                    $(td).html(vueObj.DetailName(tdData))
                                }
                            },

                            {
                                title: "资金变动",
                                data: "foundChange",
                                createdCell: function (td, tdData) {
                                    var temp = tdData;
                                    if (tdData > 0) {
                                        temp = "+" + tdData;
                                    }
                                    $(td).html(temp);
                                }
                            },
                            {
                                title: "余额(￥)",
                                data: "remain"
                            },
//							{
//                                title: "订单额",
//                                data: "orderMoney"
//							}
                        ],
                        //给每一列添加下拉框搜索
                        initComplete: function () {
                     		brandAccountApi = this.api();
                     		that.selectTable();
                        }
                    })
                },
                createAccountLogExcel:function () {
                    var data = {
                        beginDate : this.searchDate.beginDate,
                        endDate : this.searchDate.endDate
                    };
                    try {
                        $.post("brandaccountlog/create_accountLog_excel",data,function (result) {
                            if(result.success){
                                window.location.href = "brandaccountlog/downloadAccountLogExcel?path="+result.data+"";
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

				selectTable:function () {
                    var api = brandAccountApi;
                    api.columns().indexes().flatten().each(function (i) {
                        switch (i) {
                            case 1: /*如果是第二列*/
                                var column = api.column(i);
                                var select = $('<select><option value="">主体</option></select>')
                                    .appendTo($(column.header()).empty())
                                    .on('change', function () {
                                        var val = $.fn.dataTable.util.escapeRegex(
                                            $(this).val()
                                        );
                                        column
                                            .search(val ? '^' + val + '$' : '', true, false)
                                            .draw();
                                    });
                                column.data().unique().sort().each(function (d, j) {
                                    select.append('<option value="' + d + '">' + d + '</option>')
                                });
                                break;

                            case 2:    /*如果是第三列*/
                                var column = api.column(i);
                                var select = $('<select><option value="">行为</option></select>')
                                    .appendTo($(column.header()).empty())
                                    .on('change', function () {
                                        var val = $.fn.dataTable.util.escapeRegex(
                                            $(this).val()
                                        );
                                        column
                                            .search(val ? '^' + val + '$' : '', true, false)
                                            .draw();
                                    });
                                column.data().unique().sort().each(function (d, j) {
                                    select.append('<option value="' + d + '">' + vueObj.BehaviorName(d) + '</option>')
                                });
                                break;
                            case 4:    /*如果是第五列*/
                                var column = api.column(i);
                                var select = $('<select><option value="">详情</option></select>')
                                    .appendTo($(column.header()).empty())
                                    .on('change', function () {
                                        var val = $.fn.dataTable.util.escapeRegex(
                                            $(this).val()
                                        );
                                        column
                                            .search(val ? '^' + val + '$' : '', true, false)
                                            .draw();
                                    });
                                column.data().unique().sort().each(function (d, j) {
                                    select.append('<option value="' + d + '">' + vueObj.DetailName(d) + '</option>')
                                });
                                break;
                            default:
                                break;
                        }
                    });
                },

				searchInfo:function () {
                    var that = this;
                    var timeCha = new Date(that.searchDate.endDate).getTime() - new Date(that.searchDate.beginDate).getTime();
                    if(timeCha < 0) {
                        toastr.clear();
                        toastr.error("开始时间应该少于结束时间！");
                        return false;
                    }
                    toastr.clear();
                    toastr.success("查询中...");
                    try {
                        $.post("brandaccountlog/list_all", this.getDate(), function (result) {
                            console.log(result)
                            if(result.success) {
                                that.brandAccountLogTable.clear();
                                that.brandAccountLogTable.rows.add(result.data).draw();
                                that.selectTable();
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

                formatDate:function (date) {
                    var temp = '';
                    if (date != null && date != "") {
                        temp = new Date(date);
                        temp = temp.format("yyyy-MM-dd hh:mm:ss");
                    }
                    return temp;
                },
				BehaviorName:function (data) {
                    var temp = "未知";
                    switch (data){
                        case 10:
                            temp = "注册";
                            break;
                        case 20:
                            temp = "消费";
                            break;
                        case 30:
                            temp = "短信";
                            break;
                        case 40:
                            temp = "充值";
                            break;
                        default:
                            break;
                    }
                    return  temp;
                },
				DetailName:function (data) {
                    var temp = "未知";
                    switch (data){
                        case 10:
                            temp = "新用户注册";
                            break;
                        case 20:
                            temp = "消费订单抽成";
                            break;
                        case 21:
                            temp = "消费订单实付抽成";
                            break;
                        case 22:
                            temp = "回头用户消费订单抽成";
                            break;
                        case 23:
                            temp = "回头用户消费实付订单抽成";
                            break;
                        case 24:
                            temp = "R+外卖订单抽成";
                            break;
                        case 25:
                            temp = "R+外卖实付抽成";
                            break;
                        case 26:
                            temp = "第三方外卖订单抽成";
                            break;
                        case 27:
                            temp = "第三方外卖订单实付抽成";
                            break;

                        case 30:
                            temp = "注册验证码";
                            break;
                        case 31:
                            temp = "结店短信";
                            break;
                        case 40:
                            temp = "账户充值";
                            break;
                        default:
                            break;
                    }
                    return  temp;
                }



			}
        });
    }());




</script>
