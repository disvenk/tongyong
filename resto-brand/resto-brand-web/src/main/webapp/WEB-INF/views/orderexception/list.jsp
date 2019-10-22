<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
            <h2 class="text-center"><strong>异常订单</strong></h2><br/>
            <div class="row" id="searchTools">
                <div class="col-md-12">
                    <form class="form-inline">
                        <div class="form-group" style="margin-right: 50px;">
                            <label for="beginDate">开始时间：</label>
                            <input type="text" class="form-control form_datetime" id="beginDate" v-model="searchDate.beginDate"  readonly="readonly">
                        </div>
                        <div class="form-group" style="margin-right: 50px;">
                            <label for="endDate">结束时间：</label>
                            <input type="text" class="form-control form_datetime" id="endDate"  v-model="searchDate.endDate"   readonly="readonly">
                        </div>
                        <button type="button" class="btn btn-primary" @click="doExcetpionOrder">执行</button><br/>
                    </form>
	      </div>
             </div>
	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="orderexception/add">
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
<script>

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
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "orderexception/list_all",
				dataSrc : ""
			},
            "order": [[ 5, 'asc' ], [ 1, 'asc' ]],
			columns : [
                {
                    title:'id',
                    data:"id",

                }  ,
                {
                  title:"订单时间",
                    data:"createTime",
                    createdCell:function (td,tdData) {
                        $(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm:ss"))
                    }
                },
				{                 
                    title : "订单id",
                    data : "orderId",
                },
                {
                    title : "订单金额",
                    data : "orderMoney",
                },
                {
                    title : "异常原因",
                    data : "why",
                },
                {
                    title : "品牌名称",
                    data : "brandName",
                },
                {
                    title : "店铺名称",
                    data : "shopName",
                },

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="orderexception/delete">
							C.createDelBtn(tdData,"orderexception/delete"),
							</s:hasPermission>
							<s:hasPermission name="orderexception/modify">
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		
		var C = new Controller(null,tb);
		var vueObj = new Vue({
			el:"#control",
            data:{
                searchDate : {
                    beginDate : "",
                    endDate : "",
                },
            },
			mixins:[C.formVueMix],
            created: function () {
                var date = new Date().format("yyyy-MM-dd");
                this.searchDate.beginDate = date;
                this.searchDate.endDate = date;
            },
            methods :{
                doExcetpionOrder : function () {
                    //用来执行插入异常订单的方法
                    var that = this;
                    $.ajax({
                        url:'orderexception/doExcetpionOrder',
                        data:{
                            beginDate:that.searchDate.beginDate,
                            endDate:that.searchDate.endDate,
                        },
                        success:function () {
                            tb.ajax.reload();
                            toastr.success("执行成功了");
                        }
                    })
                }
            }
		});
		C.vue=vueObj;
	}());
</script>
