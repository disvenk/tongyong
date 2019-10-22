<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<link rel="stylesheet" type="text/css" href="assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">

<h2 class="text-center">
	<strong>短信记录</strong>
</h2>
<br />
<div class="row">
     <div class="col-md-4 col-md-offset-2">
         <div class="dashboard-stat blue">
             <div class="visual">
                 <i class="fa fa-comments"></i>
             </div>
             <div class="details">
                 <div class="number">
                <span data-counter="counterup" id="isUsed"></span>条
                 </div>
                 <div class="desc"> 已经使用的短信数量 </div>
             </div>
         </div>
     </div>
     <div class="col-md-4">
         <div class="dashboard-stat green">
             <div class="visual">
                 <i class="fa fa-bar-chart-o"></i>
             </div>
             <div class="details">
                 <div class="number">
                     <span data-counter="counterup" id="remnant"></span>条 </div>
                 <div class="desc"> 剩余短信数量 </div>
             </div>
         </div>
     </div>
 </div>
     
<!-- vue对象开始 -->
<div id="control">                    
<div class="row">
	<div class="col-md-8 col-md-offset-2">
		<form class="form-horizontal" id="smsForm">
		  <div class="form-group">
		    <label for="inputEmail3" class="col-sm-2 control-label">开始时间：</label>
		    <div class="col-sm-4">
		   		<input type="text"
						class="form-control form_datetime" id="beginDate" name="beginDate"
						readonly="readonly">
		    </div>
		    <label for="inputPassword3" class="col-sm-2 control-label">结束时间：</label>
		    <div class="col-sm-4">
		    	<input type="text"
						class="form-control form_datetime" id="endDate" name="endDate"
						readonly="readonly">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="inputPassword3" class="col-sm-2 control-label">店铺列表：</label>
		    <div class="col-sm-10">
		    	<label class="checkbox-inline" v-for="shop in shops">
				  <input type="checkbox" name="shopId"  value="{{shop.id}}" > {{shop.name}}
				</label>
		    </div>
		  </div>
		  		<div class="col-md-6">
		  			<button type="button" class="btn btn-primary btn-sm btn-block" @click="querySms">查询</button>
		  		</div>
		</form>
		
	</div>
</div>
<br />
<br />

<!-- datatable开始 -->
<div class="panel panel-default">
	<div class="panel-heading">短信记录详情</div>
	<div class="panel-body">
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered"
				id="selectList"></table>
		</div>
	</div>
</div>
<!-- datatable结束 -->
</div>
<!-- vue对象结束 -->


<!-- <!-- 日期框 -->
<script src="assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
	
<script src="assets/global/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
	
<script>
	$(function() {
		//查询品牌已使用短信和剩余短信
		$.ajax({
			url:"smsacount/selectSmsAcount",
			success:function(result){
				console.log(result);
				$("#remnant").html(result.data.remainderNum);
				$("#isUsed").html(result.data.usedNum);
			}
		})
		
      	var cid = "#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			"order": [[ 3, 'desc' ]],
			ajax : {
				url : "smsloginfo/listByShopsAndDate",
				dataSrc : "",
				type : "POST",
				data : function(d) {
					d.begin = $("#beginDate").val();
					d.end = $("#endDate").val();
					var temp="";
					 $(":checkbox[name='shopId']:checked").each(
							function() {
 								if($(this).attr("checked")){
 									temp += $(this).val()+","
								}
						})
					d.shopIds=temp;
					return d;
				},
			},
			columns : [ {
				title : "手机号",
				data : "phone",
			}, {
				title : "内容",
				data : "content",
			}, {
				title : "发送类型",
				data : "smsLogTyPeName",
			},
			{
				title : "创建时间",
				data : "createTime",
				createdCell : function(td, tdData) {
					$(td).html(new Date(tdData).format("yyyy-MM-dd hh:ss"));
				}
			},
			{
				title : "是否成功",
				data : "isSuccess",
				createdCell : function(td,tdData){
					$(td).html(tdData==1?'是':'否')			
				}
			} ]

		})
		
		var C = new Controller(null,tb);
		var vueObj = new Vue({
			el:"#control",
			mixins:[C.formVueMix],
			data:{
				shops:{}
			},
			methods:{
				initTime : function(){
					$('.form_datetime').datetimepicker({
						endDate : new Date(),
						minView : "month",
						maxView : "month",
						autoclose : true,//选择后自动关闭时间选择器
						todayBtn : true,//在底部显示 当天日期
						todayHighlight : true,//高亮当前日期
						format : "yyyy-mm-dd",
						startView : "month",
						language : "zh-CN"
					});
				},
				initShopName : function(){
					$.ajax({
						url:"smsloginfo/shopName",
						success:function(data){
								vueObj.shops = data;
						}
					})
					
				},
				querySms : function(){
					var begin = $("#beginDate").val();
					var end = $("#endDate").val();
					//判断时间是否合法
					if(begin>end){
						toastr.error("开始时间不能大于结束时间");
						return ;
					}
					//检验是否选择了店铺
					var checkboxes =$("input[type='checkbox']");
					if(!checkboxes.is(":checked")){
						toastr.error("请至少选择一个店铺");
						return ;
					}
					tb.ajax.reload();
				},
				checked:function(){
					$("[name='shopId']").attr("checked","checked");
				}
				
			},
			
// 			vue实例化之后执行的方法
			created : function(){
				//初始化多选框按钮 和 时间插件
				//时间默认值
				$('.form_datetime').val(new Date().format("yyyy-MM-dd"));
				this.initTime();
				this.initShopName();
				//默认选中所有的店铺
				this.checked();
			},
		});
		C.vue=vueObj;
	})
</script>
