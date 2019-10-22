<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="control">
<a class="btn btn-info ajaxify" href="userdata/list">
	<span class="glyphicon glyphicon-circle-arrow-left"></span>
	返回
</a>
<h2 class="text-center"><strong>订单详情</strong></h2><br/>
<div class="row" id="searchTools">
<br/>
<br/>
<div>
	  <!-- Nav tabs -->
	  <ul class="nav nav-tabs" role="tablist" id="ulTab">
	    <li role="presentation" class="active" @click="chooseType(1)">
	    	<a href="#dayReport" aria-controls="dayReport" role="tab" data-toggle="tab">
	            <strong>菜品项</strong>
	       </a>
	    </li>
	    <li role="presentation" @click="chooseType(2)">
	        <a href="#revenueCount" aria-controls="revenueCount" role="tab" data-toggle="tab">
	            <strong>支付项</strong>
	        </a>
	    </li>
	  </ul>
	  <div class="tab-content">
	    <div role="tabpanel" class="tab-pane active" id="dayReport">
	    	<div class="panel panel-success">
			  <div class="panel-heading text-center">
			  	   <strong style="margin-right:100px;font-size:22px">菜品项</strong>
			  </div>
			  <div class="panel-body">
			  	<table id="orderItemTable" class="table table-striped table-bordered table-hover"
			  		style="width: 100%;">
			  	</table>
			  </div>
			</div>
	    </div>
	
	    <div role="tabpanel" class="tab-pane" id="revenueCount">
	    	<div class="panel panel-primary" style="border-color:white;">
	    	<div class="panel panel-info">
			  <div class="panel-heading text-center">
			  	<strong style="margin-right:100px;font-size:22px">支付项</strong>
			  </div>
			  <div class="panel-body">
			  	<table id="paymentTable" class="table table-striped table-bordered table-hover"
			  		style="width: 100%;">
                    <thead>
                        <th>支付金额</th>
                        <th>支付方式</th>
                        <th>支付时间</th>
                        <th>备注</th>
                    </thead>
                    <tbody>
                        <template v-if="payMentList.length > 0">
                            <tr v-for="payment in payMentList">
                                <td>{{payment.payValue}}</td>
                                <td>{{payment.paymentMode}}</td>
                                <td>{{payment.payTime}}</td>
                                <td>{{payment.remark}}</td>
                            </tr>
                        </template>
                        <template v-else>
                            <td colspan="4" class="text-center">暂时没有数据...</td>
                        </template>
                    </tbody>
			  	</table>
			  </div>
			</div>
			  </div>
			</div>
	    </div>
	  </div>
	    </div>
	    </div>
	</div>
</div>
<script>
    var orderId = "${orderId}";
    var brandId = "${brandId}";
	var vueObjShop = new Vue({
	    el : "#control",
	    data : {
	        payMentList : [],
            orderItemList : [],
	    },
        created : function () {
            this.getData();
        }
        ,
	    methods : {
	        getData : function () {
	            var that = this;
                $.ajax({
                    url:"userdata/orderInfo",
                    type:"post",
                    dataType:"json",
                    data:"orderId="+orderId+"&brandId="+brandId,
                    success:function(result){
                        if(result.success == true){
                            that.payMentList = result.data.payMentList;
                            that.orderItemList = result.data.orderItemList;
                            that.initDataTables();
                        }else{
                            toastr.error("查询订单信息出错！");
                            toastr.clear();
                        }
                    },
                    error:function(){
                        toastr.error("查询订单信息出错！");
                        toastr.clear();
                    }
                });
            },
            initDataTables: function () {
                var that = this;
                $("#orderItemTable").DataTable({
                    data:that.orderItemList,
                    columns: [
                        {
                            title: "菜品名称",
                            data: "articleName",
                            orderable: false
                        },
                        {
                            title: "原始数量",
                            data: "orginCount"
                        },
                        {
                            title: "数量",
                            data: "count"
                        },
                        {
                            title: "退菜数量",
                            data: "refundCount"
                        },
                        {
                            title: "餐盒数量",
                            data: "mealFeeNumber"
                        },
                        {
                            title: "价格",
                            data: "finalPrice"
                        }
                    ]
                });
            }
        }
	});
</script>

