<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table id="sms-list-table" class="table table-striped table-bordered table-hover"></table>
<script>
	var smsListTable = $("#sms-list-table").DataTable({
		ajax:{url:"smsLog/smsLogList",dataSrc:"data"},
		pageLength:20,
		order:[[0,"desc"]],
		columns:[
		         {title:"发送时间",data:"createTime",
		        	 createdCell:function(td,time){
		        	 console.log(time);
		        	 $(td).html(new Date(time).format("yyyy-MM-dd hh:mm"));
		         },searchable:false},
		         {title:"手机号码",data:"phone"},
		         {title:"验证码",data:"content",searchable:false},
		         ]
	});
	$("#sms-list-table_filter input").attr("placeholder","请输入手机号搜索");
</script>