<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<link rel="stylesheet" type="text/css" href="assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">

<h2 class="text-center"><strong>结算报表</strong></h2><br/>
<div class="row" id="searchTools">
	<div class="col-md-12">
		<form class="form-inline">
		  <div class="form-group" style="margin-right: 50px;">
		    <label for="beginDate">开始时间：</label>
		    <input type="text" class="form-control form_datetime" id="beginDate" readonly="readonly">
		  </div>
		  <div class="form-group" style="margin-right: 50px;">
		    <label for="endDate">结束时间：</label>
		    <input type="text" class="form-control form_datetime" id="endDate" readonly="readonly">
		  </div>
		  <button type="button" class="btn btn-primary" id="searchReport">查询报表</button>
		</form>
	</div>
</div>
<br/>
<p class="text-danger text-center" hidden="true"><strong>开始时间不能大于结束时间！</strong></p>
<br/>
<div>
  <!-- Nav tabs -->
  <ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#dayReport" aria-controls="dayReport" role="tab" data-toggle="tab"><strong>每日报表</strong></a></li>
  </ul>

  <!-- Tab panes -->
  <div class="tab-content">
  	<button class="btn btn-info" id="showPreview">查看报表预览</button>
  	<!-- 每日报表 -->
    <div role="tabpanel" class="tab-pane active" id="dayReport">
    	<div id="report-editor">
    		<!-- 收入条目 -->
	    	<div class="panel panel-success">
			  <div class="panel-heading text-center">
			  	<strong style="margin-right:100px;font-size:22px">收入条目</strong>
			  </div>
			  <div class="panel-body">
			  	<table id="dayReportTable" class="table table-striped table-bordered table-hover" width="100%"></table>
			  </div>
			</div>
			<!-- 菜品销售记录 -->
	    	<div class="panel panel-info">
			  <div class="panel-heading text-center">
			  	<strong style="margin-right:100px;font-size:22px">菜品销售记录</strong>
			  </div>
			  <div class="panel-body">
			  	<table id="articleSaleTable" class="table table-striped table-bordered table-hover" width="100%"></table>
			  </div>
			</div>
    	</div>
    	<div id="report-preview" class="row" style="display:none">
			  <div class="col-md-4"><p><strong>收入条目</strong></p></div>
			  <div class="col-md-8"><p><strong>菜品销售记录</strong></p></div>
    	</div>
    </div>
  </div>
</div>

<!-- <!-- 日期框 --> 
<script src="assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script src="assets/global/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>


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

//文本框默认值
$('.form_datetime').val(new Date().format("yyyy-MM-dd"));
var isFirst = true;
var orderPaymentItemsCount = 0;//用于统计总量，预览时使用
var tb1 = $("#dayReportTable").DataTable({
	ajax : {
		url : "report/orderPaymentItems",
		dataSrc : "data",
		data:function(d){
			d.beginDate=$("#beginDate").val();
			d.endDate=$("#endDate").val();
			return d;
		}
	},
	columns : [
		{ title : "支付类型", data : "paymentModeVal" },                 
		{ title : "支付金额", data : "payValue" ,
			createdCell:function(td,tdData,rowData){
				orderPaymentItemsCount += tdData;//计算 预览信息
				$("#report-preview > .col-md-4:first").append("<p>"+rowData.paymentModeVal+"："+tdData+" 元<p/>");
			}
		}
	],
	fnFooterCallback: function() {
		if(!isFirst){
			$("#report-preview > .col-md-4:first").append("---------------------<br/>");
			$("#report-preview > .col-md-4:first").append("<strong>统计实收："+orderPaymentItemsCount.toFixed(2)+" </strong><br/>");
			orderPaymentItemsCount = 0 ;//初始化
		}
	} 
});

var orderArticleItemsCount = 0;
var tb2 = $("#articleSaleTable").DataTable({
	ajax : {
		url : "report/orderArticleItems",
		dataSrc : "data",
		data:function(d){
			d.beginDate=$("#beginDate").val();
			d.endDate=$("#endDate").val();
			return d;
		}
	},
	columns : [
		{ title : "菜品名称", data : "articleName" },                 
		{ title : "菜品销量", data : "articleSum",
			createdCell:function(td,tdData,rowData){
				orderArticleItemsCount += tdData;
				$("#report-preview > .col-md-8:last").append(""+rowData.articleName+"："+tdData+"<br/>");
			}	
		}
	],
	fnFooterCallback: function() {
		if(!isFirst){
			$("#report-preview > .col-md-8:last").append("---------------------<br/>");
			$("#report-preview > .col-md-8:last").append("<strong>统计实收："+orderArticleItemsCount.toFixed(2)+"  份</strong><br/>");
			orderArticleItemsCount = 0;//初始化
		}
		isFirst = false;//用于判断是否为第一次加载
	} 
});

//搜索
$("#searchReport").click(function(){
	var beginDate = $("#beginDate").val();
	var endDate = $("#endDate").val();
	//判断 时间范围是否合法
	if(beginDate>endDate){
		toastr.error("开始时间不能大于结束时间");
		toastr.clear();
		return ;
	}
	
	var data = {"beginDate":beginDate,"endDate":endDate};
	//清空预览信息
	$("#report-preview > .col-md-4:first").html("<p><strong>收入条目</strong></p>");
	$("#report-preview > .col-md-8:last").html("<p><strong>菜品销售记录</strong></p>");
	//更新数据
	tb1.ajax.reload();
	tb2.ajax.reload();
})

//显示预览信息
$("#showPreview").click(function(){
	$("#report-editor").toggle();
	$("#report-preview").toggle();
})

</script>
