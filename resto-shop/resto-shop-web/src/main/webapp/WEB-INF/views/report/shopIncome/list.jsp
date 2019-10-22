<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<h2 class="text-center"><strong>店铺收入报表</strong></h2><br/>
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
		  <button type="button" class="btn btn-primary" id="searchReport">查询报表</button>&nbsp;
		  <button type="button" class="btn btn-primary" id="shopIncomExcel">下载报表</button>&nbsp;
		</form>
	</div>
</div>
<br/>
<br/>
<div>
  <!-- Tab panes -->
  <div class="tab-content">
  	<!-- 每日报表 -->
    <div role="tabpanel" class="tab-pane active" id="dayReport">
    	<div id="report-editor">
    		<!-- 收入条目 -->
	    	<div class="panel panel-success">
			  <div class="panel-heading text-center">
			  	<strong style="margin-right:100px;font-size:22px">收入条目记录</strong>
			  </div>
			  <div class="panel-body">
			  	<table id="dayReportTable" class="table table-striped table-bordered table-hover" width="100%">
			  	</table>
			  </div>
			</div>
    	</div>
    	<div id="report-preview" class="row" style="display:none">
			  <div class="col-md-4"><p><strong>收入条目</strong></p></div>
    	</div>
    </div>
  </div>
</div>

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
	ordering:false,
	columns : [
		{ title : "支付类型", data : "paymentModeVal" },                 
		{ title : "支付金额", data : "payValue"} ,
	],
});


//搜索
$("#searchReport").click(function(){
	var beginDate = $("#beginDate").val();
	var endDate = $("#endDate").val();
	//判断 时间范围是否合法
	if(beginDate>endDate){
		toastr.error("开始时间不能大于结束时间");
		return ;
	}
	var data = {"beginDate":beginDate,"endDate":endDate};
	//更新数据
	tb1.ajax.reload();
	toastr.success("查询成功");
})

//店铺收入报表下载
$("#shopIncomExcel").click(function(){
	var beginDate = $("#beginDate").val();
	var endDate = $("#endDate").val();
	location.href="report/income_excel?beginDate="+beginDate+"&&endDate="+endDate;
})


</script>
