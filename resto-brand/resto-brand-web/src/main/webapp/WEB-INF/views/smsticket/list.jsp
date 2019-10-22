<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<style>
	dt,dd{
		line-height: 35px;
	}	
</style>

<div id="control">
<!-- 发票详情模态框开始 -->
<div class="modal fade" id="detailInvoiceModal" tabindex="-1" role="dialog" 
  	 aria-labelledby="generalModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" 
               data-dismiss="modal" aria-hidden="true">
                  &times;
            </button>
            <h4 class="modal-title text-center">
          		   <strong>发票详情</strong>
            </h4>
         </div>
         <div class="modal-body">
           	<dl class="dl-horizontal">
           		<template v-if="smsticketInfo.type==1">
           			<dt>发票类型：</dt>
           				<dd><span class='label' style='background-color: #3598dc'>普通发票</span></dd>
           			<dt>发票抬头：</dt>
           				<dd>{{smsticketInfo.header?smsticketInfo.header:"无"}}</dd>
           			<dt>金额：</dt>
           				<dd>{{smsticketInfo.money?smsticketInfo.money:"无"}}元</dd>
           			<dt>内容：</dt>
           				<dd>{{smsticketInfo.content?smsticketInfo.content:"无"}}</dd>
           			<dt>物流单号：</dt>
						<dd>{{smsticketInfo.expersage?smsticketInfo.expersage:"无"}}</dd>
					<dt>备注：</dt>
						<dd>{{smsticketInfo.remark?smsticketInfo.remark:"无"}}</dd>
				</template>
				
           		<template v-if="smsticketInfo.type==2">
           			<dt>发票类型：</dt>
           				<dd><span class='label' style='background-color: #26c281'>增值税发票</span></dd>
           			<dt>发票抬头：</dt>
           				<dd>{{smsticketInfo.header?smsticketInfo.header:"无"}}</dd>
					<dt>金额：</dt>
						<dd>{{smsticketInfo.money?smsticketInfo.money:"无"}}元</dd>
					<dt>内容：</dt>
						<dd>{{smsticketInfo.content?smsticketInfo.content:"无"}}</dd>
					<dt>纳税人识别码：</dt>
						<dd>{{smsticketInfo.taxpayerCode?smsticketInfo.taxpayerCode:"无"}}</dd>
					<dt>注册地址：</dt>
						<dd>{{smsticketInfo.registeredAddress?smsticketInfo.registeredAddress:"无"}}</dd>
					<dt>注册电话：</dt>
						<dd>{{smsticketInfo.registeredPhone?smsticketInfo.registeredPhone:"无"}}</dd>
					<dt>开户银行：</dt>
						<dd>{{smsticketInfo.bankName?smsticketInfo.bankName:"无"}}</dd>
					<dt>银行账户：</dt>
						<dd>{{smsticketInfo.bankAccount?smsticketInfo.bankAccount:"无"}}</dd>
					<dt>物流单号：</dt>
						<dd>{{smsticketInfo.expersage?smsticketInfo.expersage:"无"}}</dd>
					<dt>备注：</dt>
						<dd>{{smsticketInfo.remark?smsticketInfo.remark:"无"}}</dd>	
				</template>
			</dl>
				<hr/>
					<h4 class="text-center"><strong>收件人信息</strong></h4>
					<dl class="dl-horizontal">
						<dt>收件人姓名：</dt>
						<dd>{{smsticketInfo.name?smsticketInfo.name:"无"}}</dd>
						<dt>收件人电话：</dt>
						<dd>{{smsticketInfo.phone?smsticketInfo.phone:"无"}}</dd>
						<dt>收件人地址：</dt>
						<dd>{{smsticketInfo.address?smsticketInfo.address:"无"}}</dd>
				</dl>
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default" 
               data-dismiss="modal">关闭
            </button>
         </div>
      </div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
<!-- 发票详情模态结束 -->

<!-- 制作发票模态框开始 -->
<div class="modal fade" id="makeTicketModal" tabindex="-1" role="dialog" 
  	 aria-labelledby="makeTicketModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" 
               data-dismiss="modal" aria-hidden="true">
                  &times;
            </button>
            <h4 class="modal-title text-center">
          		   <strong>制作发票</strong>
            </h4>
         </div>
         <div class="modal-body">
         	<form class="form-horizontal" action="smsticket/modify" id="exepersageForm" @submit.prevent="update">
			  <div class="form-group">
			    <label class="col-sm-3 control-label">物流单号:</label>
			    <div class="col-sm-8">
			      <input type="number" class="form-control" v-model="smsticketInfo.expersage" name="expersage">
			    </div>
			  </div>
			  <div class="text-center">
					<input type="hidden" name="id" v-model="smsticketInfo.id"/>
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<input class="btn green" type="submit" value="保存"/>
				</div>
			</form>
			</div>
      </div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
<!-- 制作发票模态框结束 -->

	<!-- 表格   start-->
	<div class="table-div">
		<div class="clearfix"></div>
		<div class="table-filter">&nbsp;</div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered "></table>
		</div>
	</div>
	<!-- 表格   end-->
</div>
<script>
	$(document).ready( function () {
		$("#exepersageForm").validate();
		//载入 表格数据
	    tb = $('.table-body>table').DataTable({
	    	"order": [[1,'asc'],[4,'asc']],
	    	"columnDefs":[{"visible":false,"targets":1},
// 	    	              {"visible":false,"targets":2}
	    	 ],
	    	 "aoColumnDefs": [
	    	                  { "sWidth": "8%", "aTargets": [ 4 ] }
	    	                ],
	    	
			ajax : {
				url : "smsticket/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
					title : "发票抬头",
					data : "header",
				}, 
				{                 
					title : "状态",
					data : "ticketStatus",
					createdCell:function(td,tdData){
						if(tdData==0){
							$(td).html("<span class='label' style='color:#26c281'>待处理..</span>");
						}else if(tdData==1){
							$(td).html("已完成");
						}
					}
				}, 
				
				{                 
					title : "内容",
					data : "content",
				}, 
				{                 
					title : "金额",
					data : "money",
					createdCell:function(td,tdData){
						$(td).html(tdData+"  <span class='glyphicon glyphicon-yen' aria-hidden='true'></span>");
					}
				}, 
				{                 
					title : "申请时间",
					data : "createTime",
					createdCell:function(td,tdData){
						$(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm:ss"))
					}
				},
// 				{                 
// 					title : "完成时间",
// 					data : "pushTime",
// 					createdCell:function(td,tdData){
// 						if(tdData!=null){
// 							$(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm:ss"))
// 						}
// 					}
// 				},
				{                 
					title : "物流单号",
					data : "expersage",
					createdCell:function(td,tdData){
						$(td).html(tdData!=null?"<a href='http://m.kuaidi100.com/result.jsp?com=&nu="+tdData+"' target='_blank'>"+tdData+"</a>":"未完成");
					}
					
 				},
// 				{                 
// 					title : "备注",
// 					data : "remark",
// 				},
				
// 				{                 
// 					title : "申请人",
// 					data : "proposer",
// 				}, 
				
				{                 
					title : "发票类型",
					data : "type",
					createdCell:function(td,tdData){
						$(td).html(tdData == 1 ? "<span class='label' style='background-color: #3598dc'>普通发票</span>": "<span class='label' style='background-color: #26c281'>增值税发票</span>");
					}
					
				}, 
				
				{
					title : "详情",
					data : "id",
					createdCell : function(td, tdData, rowData) {
						console.log(rowData);
// 						var btn = null;
// 						if(rowData.type==2){
// 							btn = createBtn(null, "查看详情",
//  									"btn-sm btn-success",
//  									function() {
// 										vueObj.showDetailInfo(rowData);
// 										$("#consigneeModal").modal();
//  									})
// 						}else if(rowData.type==1){
// 							btn = createBtn(null,"查看详情","btn-sm btn-info",function(){
// 								vueObj.showDetailInfo(rowData);
// 								$("#generalModal").modal();									
// 							})
// 						}
						var btn = createBtn(null,"查看详情","btn-sm btn-info",function() {
							vueObj.showDetailInfo(rowData);
							$("#detailInvoiceModal").modal();
						})
						$(td).html(btn);
					}
				},
				
				{
					title : "操作",
					data : "id",
					createdCell : function(td, tdData,rowData) {
						var info=[];
						if(rowData.ticketStatus==0){
							var btn = createBtn(null, "未开",
 									"btn-sm btn-danger",
 									function() {
										vueObj.smsticketInfo = rowData;
										$("#makeTicketModal").modal();
 									})
						}else if(rowData.ticketStatus==1){
							var btn = createBtn(null,"修改物流单号","btn-sm btn-info ",function(){
								vueObj.smsticketInfo = rowData;
								$("#makeTicketModal").modal();
							})
						}
						<s:hasPermission name='smsticket/edit'>
							info.push(btn);
						</s:hasPermission>
						$(td).html(info);
					}
				}
			],
	    });
		
	    var C = new Controller("#control",tb);
		
		var vueObj = new Vue({
			el:"#control",
			data:{
				smsticketInfo:{},
			},
			methods:{
				showDetailInfo:function(smsticketInfo){
					this.smsticketInfo = smsticketInfo;
				},
			update:function(e){
				$("#exepersageForm").validate();
				Vue.nextTick(function(){
					$("#exepersageForm").validate();
				}) 
				
				
				var that = this;
				var formDom = e.target;
				C.ajaxFormEx(e.target,function(){
					$("#makeTicketModal").modal("hide");//关闭模态框
					tb.ajax.reload();
				});
			},
		}
	});
		
		
		
		//创建一个按钮
		function createBtn(btnName, btnValue, btnClass, btnfunction) {
			return $('<input />', {
				name : btnName,
				value : btnValue,
				type : "button",
				class : "btn " + btnClass,
				click : btnfunction
			})
		}	
		
		//给行添加双击事件
		tb.on('dblclick','tr', function () {
			vueObj.showDetailInfo(tb.row(this).data());
			$("#detailInvoiceModal").modal();

		} );
		

	} );
	
	
</script>