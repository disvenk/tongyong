<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<!-- vue对象开始 -->
<div id="control">
<br>
<div class="row text-center" id="searchTools">
	<div class="col-md-12">
		<form role="form" class="form-inline">
		  <div class="form-group" style="margin-right: 50px;">
		    <label for="beginDate">开始时间：</label>
		    <input type="text" class="form-control form_datetime" id="beginDate" name="beginDate" readonly="readonly">
		  </div>
		  <div class="form-group" style="margin-right: 50px;">
		    <label for="endDate">结束时间：</label>
		    <input type="text" class="form-control form_datetime" id="endDate" name="endDate" readonly="readonly">
		  </div>
		  <button type="button" class="btn btn-primary" id="searchReport" @click="queryOrder">查询报表</button>
		</form>
	</div>
</div>
<br/>
<!-- datatable开始 -->
<div class="table-div">
	<div class="clearfix"></div>
	<div class="table-filter"></div>
	<div class="table-body">
		<table class="table table-striped table-hover table-bordered ">
		 <tfoot>
            <tr class="success">
                <th>当前页:</th><td colspan="2"></td>
                <th>总计:</th><td colspan="2"></td>
            </tr>
       	 </tfoot>
		</table>
	</div>
</div>
<!-- datatable结束 -->

</div>
<!-- vue对象结束 -->
<br/>


<script>
	(function(){
		//初始化时间插件
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
		//给开始和结束时间赋默认初始值
		$("#endDate").val(new Date().format("yyyy-MM-dd"));
		$("#beginDate").val(GetDateStr(-7));
		var cid="#control";
		//加载datatable
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "wechatCharge/list_all",
				dataSrc : "",
				data:function(d){
					d.beginDate= $("#beginDate").val();
					d.endDate=$("#endDate").val();
					return d;
				},
			},
			footerCallback: function () {
				var api = this.api();
	            // Total over all pages
	            total = api.column( 1 ).data().reduce( function (a, b) {
							return a+b;
						}, 0 );
	            // Total over this page
	            pageTotal = api.column( 1, { page: 'current'} ).data().reduce( function (a, b) {
								return a+b;
							}, 0 );
	            // Update footer
	            $(api.column(1).footer()).html(pageTotal+" 元");
	            $(api.column(4).footer()).html(total +' 元');
	        },
			columns : [
				{                 
					title : "充值时间",
					data : "createTime",
					createdCell:function(td,tdData){
						$(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm:ss"));
					}
				},                 
				{                 
					title : "充值金额(元)",
					data : "paymentMoney",
				},                 
				{                 
					title : "返还的金额(元)",
					data : "rewardMoney",
				},
				{
					title:"充值的手机",
					data:"telephone",
					defaultContent:'无'
				},
				{
					title:"充值的品牌",
					data:"brandName",
				},
				{
					title:"充值的店铺",
					data:"shopDetailName",
				},
				],
		});
	
		
 		var C = new Controller(cid,tb);
		var vueObj = new Vue({
			el:"#control",
			data:{
			},
			//保留原vue对象中的内容和方法
			mixins:[C.formVueMix],
			methods:{
	 			queryOrder :function(){
	 				var beginDate = $("#beginDate").val();
	 				var endDate = $("#endDate").val();
	 				//判断 时间范围是否合法
					var timeCha = new Date(endDate).getTime() - new Date(beginDate).getTime();
					if(timeCha < 0){
						toastr.clear();
						toastr.error("开始时间应该少于结束时间！");
						return ;
					}else if(timeCha > 2678400000){
						toastr.clear();
						toastr.error("暂时未开放大于一月以内的查询！");
						return ;
					}
	 				tb.ajax.reload();
	 			}
			},
		})
		
		function GetDateStr(AddDayCount){
			var dd = new Date();
			dd.setDate(dd.getDate()+AddDayCount);
			var y = dd.getFullYear(); 
			var m = dd.getMonth()+1;//获取当前月份的日期 
			var d = dd.getDate(); 
			return y+"-"+m+"-"+d; 
		}
		
	}());
	
	
</script>


