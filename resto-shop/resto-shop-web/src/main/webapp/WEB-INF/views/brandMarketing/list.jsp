<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div id="control">
	<h2 class="text-center"><strong>营销报表</strong></h2><br/><br/>
	<div class="col-md-12">
		<form class="form-inline">
		  <div class="form-group" style="margin-right: 50px;">
		    <label for="beginDate">开始时间：</label>
		    <input type="text" class="form-control form_datetime" :value="searchDate.beginDate" v-model="searchDate.beginDate" readonly="readonly">
		  </div>
		  <div class="form-group" style="margin-right: 50px;">
		    <label for="endDate">结束时间：</label>
		    <input type="text" class="form-control form_datetime" :value="searchDate.endDate" v-model="searchDate.endDate" readonly="readonly">
		  </div>
		  
		 	 <button type="button" class="btn btn-primary" @click="today"> 今日</button>
                 
             <button type="button" class="btn btn-primary" @click="yesterDay">昨日</button>
          
<!--              <button type="button" class="btn yellow" @click="benxun">本询</button> -->
             
             <button type="button" class="btn btn-primary" @click="week">本周</button>
             <button type="button" class="btn btn-primary" @click="month">本月</button>
             
             <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>&nbsp;
		  	 <button type="button" class="btn btn-primary" @click="brandreportExcel">下载报表</button><br/>
		  
		</form>
		<br/>
	</div>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<div>
		  <ul class="nav nav-tabs"></ul>
		  <div class="tab-content">
		    <div role="tabpanel" class="tab-pane active">
				
				<div class="panel panel-success">
				  <div class="panel-heading text-center">
				  	<strong style="margin-right:100px;font-size:22px">品牌营销报表
				  	</strong>
				  </div>
				  <div class="panel-body">
				  	<table id="brandMarketing" class="table table-striped table-bordered table-hover" style="width: 100%">
				  		<thead> 
							<tr>
								<th>品牌名称</th>
								<th>红包总额(元)</th>
								<th>评论红包(元)</th>
		                        <th>充值赠送红包(元)</th>
		                        <th>分享返利红包(元)</th>
		                        <th>等位红包(元)</th>
								<th>退菜红包(元)</th>
								<th>优惠券总额(元)</th>
		                        <th>注册优惠券(元)</th>
		                        <th>邀请优惠券(元)</th>
                                <th>生日优惠券(元)</th>
							</tr>
						</thead>
						<tbody>
							<tr v-if="brandInfo.brandName != null">
								<td><strong>{{brandInfo.brandName}}</strong></td>
								<td>{{brandInfo.redMoneyAll}}</td>
								<td>{{brandInfo.plRedMoney}}</td>
		                        <td>{{brandInfo.czRedMoney}}</td>
		                        <td>{{brandInfo.fxRedMoney}}</td>
		                        <td>{{brandInfo.dwRedMoney}}</td>
								<td>{{brandInfo.tcRedMoney}}</td>
								<td>{{brandInfo.couponAllMoney}}</td>
		                        <td>{{brandInfo.zcCouponMoney}}</td>
		                        <td>{{brandInfo.yqCouponMoney}}</td>
                                <td>{{brandInfo.birthCouponMoney}}</td>
							</tr>
							<tr v-else>
								<td align="center" colspan="11"><strong>加载中...</strong></td>
							</tr>
						</tbody>
				  	</table>
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

var vueObj = new Vue({
    el : "#control",
    data : {
        searchDate : {
            beginDate : "",
            endDate : ""
        },
        brandInfo : {}
    },
    created : function() {
        var date = new Date().format("yyyy-MM-dd");
        this.searchDate.beginDate = date;
        this.searchDate.endDate = date;
        this.loading();
    },
    methods : {
        searchInfo : function(isInit) {
        	this.brandInfo = null;
            var that = this;
            //判断 时间范围是否合法
            if (this.searchDate.beginDate > this.searchDate.endDate) {
                toastr.error("开始时间不能大于结束时间");
                toastr.clear();
                return false;
            }
            this.loading();
        },
        getDate : function(){
            var data = {
                beginDate : this.searchDate.beginDate,
                endDate : this.searchDate.endDate
            };
            return data;
        },
        brandreportExcel : function(){
        	location.href = "brandMarketing/downloadBrandExcel?brandJson="+JSON.stringify(this.brandInfo)+"&beginDate="+this.searchDate.beginDate+"&endDate="+this.searchDate.endDate;
        },
        today : function(){
            date = new Date().format("yyyy-MM-dd");
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
            this.searchDate.endDate  = new Date().format("yyyy-MM-dd");
            this.searchInfo();
        },
        month : function(){
            this.searchDate.beginDate  = getMonthStartDate();
            this.searchDate.endDate  = new Date().format("yyyy-MM-dd");
            this.searchInfo();
        },
		loading : function(){
			var that = this;
			$.ajax({
				url:"brandMarketing/selectAll",
				type:"post",
				data:this.getDate(),
				dataType:"json",
				success:function(result){
					if(result.success){
						that.brandInfo = result.data;
					}else{
						toastr.error("查询品牌营销报表出错!");
		                toastr.clear();
					}
				},
				error:function(){
					toastr.error("查询品牌营销报表出错!");
	                toastr.clear();
				}
			});
		}
    }
});
</script>