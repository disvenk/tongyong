<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-show="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki">新建备注</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form role="form" class="form-horizontal" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
			           			<label class="col-sm-3 control-label">备注名称：</label>
							    <div class="col-sm-8">
						    		<input type="text" class="form-control" maxlength="50" placeholder="建议输入五十个字以内" required v-model="orderRemark.remarkName">
							    </div>
							</div>
							<div class="form-group">
			           			<label class="col-sm-3 control-label">排序：</label>
							    <div class="col-sm-8">
							    	<input type="number" class="form-control"placeholder="建议输入正整数" required v-model="orderRemark.sort" min="1">
							    </div>
							</div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">是否启用：</label>
                                <div class="col-sm-8">
                                    <label class="radio-inline">
                                        <input type="radio" name="state" v-model="orderRemark.state" value="1"> 启用
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="state" v-model="orderRemark.state" value="0"> 不启用
                                    </label>
                                </div>
                            </div>
							<div class="text-center">
								<input type="hidden" name="id"/>
								<input class="btn green" type="submit" value="保存"/>
								<a class="btn default" @click="closeShowForm">取消</a>
							</div>
						</div>
					</form>
	            </div>
	        </div>
		</div>
	</div>
	
	<div class="table-div">
		<div class="table-operator">
			<button class="btn green pull-right" @click="createOrderRemark">新建</button>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter"></div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered" id = "orderRemarkTable"></table>
		</div>
	</div>
</div>


<script>
    var vueObj = new Vue({
        el : "#control",
        data : {
            orderRemarkTable : {},
            orderRemark : {state : 1},
            showform : false
        },
        created : function() {
            this.initDataTables();
            this.searchInfo();
        },
        methods : {
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                that.orderRemarkTable = $("#orderRemarkTable").DataTable({
                    lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
                    order: [[ 1, 'asc' ]],
                    columns : [
                        {
                            title : "备注名称",
                            data : "remarkName",
                            orderable : false
                        },
                        {
                            title : "排序",
                            data : "sort"
                        },
                        {
                            title : "是否启用",
                            data : "state",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var state = "";
                                if (tdData == 0){
                                    state = "<span class='label label-danger'>未启用</span>";
                                }else {
                                    state = "<span class='label label-primary'>启用</span>";
                                }
                                $(td).html(state);
                            }
                        },
                        {
                            title : "操作",
                            data : "id",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var updateButton = $("<button class='btn btn-info btn-sm'>编辑</button>");
                                updateButton.click(function () {
                                    that.updateOrderRemark(rowData);
                                });
                                var deleteButton = $("<button class='btn btn-danger btn-sm'>删除</button>");
                                deleteButton.click(function () {
                                    that.deleteOrderRemark(tdData);
                                });
                                var operator = [updateButton,deleteButton];
                                $(td).html(operator);
                            }
                        }
                    ]
                });
            },
            searchInfo : function() {
                toastr.clear();
                toastr.success("查询中...");
                var that = this;
                try{
                    $.post("orderRemark/selectAll",function(result){
                        if (result.success){
                            that.orderRemarkTable.clear();
                            that.orderRemarkTable.rows.add(result.data).draw();
                            toastr.clear();
                            toastr.success("查询成功");
                            return;
                        }else {
                            toastr.clear();
                            toastr.error("查询出错");
                            return;
                        }
                    });
                }catch(e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            save : function () {
                var that = this;
                if (that.orderRemark.id != null){
                    $.post("orderRemark/update",that.orderRemark,function (result) {
                        if (result.success){
                            toastr.clear();
                            toastr.success("修改成功");
                            that.getOrderRemark();
                            that.showform = false;
                            that.searchInfo();
                        }else{
                            toastr.clear();
                            toastr.error("修改失败");
                            that.getOrderRemark();
                            that.showform = false;
                        }
                    });
                }else{
                    $.post("orderRemark/create",that.orderRemark,function (result) {
                        if (result.success){
                            toastr.clear();
                            toastr.success("新增成功");
                            that.getOrderRemark();
                            that.showform = false;
                            that.searchInfo();
                        }else{
                            toastr.clear();
                            toastr.error("新增失败");
                            that.getOrderRemark();
                            that.showform = false;
                        }
                    });
                }
            },
            createOrderRemark : function () {
                this.showform = true;
            },
            closeShowForm : function () {
                this.showform = false;
                this.getOrderRemark();
            },
            updateOrderRemark : function (orderRemark) {
                this.orderRemark = orderRemark;
                this.showform = true;
            },
            deleteOrderRemark : function (orderRemarkId) {
                var that = this;
                $.post("orderRemark/deleteOrderRemark",{"orderRemarkId" : orderRemarkId}, function (result) {
                    if (result.success){
                        toastr.clear();
                        toastr.success("删除成功");
                        that.searchInfo();
                    }else{
                        toastr.clear();
                        toastr.error("系统异常，请刷新重试");
                    }
                });
            },
            getOrderRemark : function () {
                this.orderRemark = {state : 0};
            }
        }
    });

    function Trim(str)
    {
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }
</script>
