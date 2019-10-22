<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <div class="table-div">
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
            orderRemark : {}
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
                            createdCell: function (td, tdData) {
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
                                var updateButton = $("<button class='btn btn-"+(rowData.state == 1 ? "danger" : "info")+" btn-sm'>"+(rowData.state == 1 ? "关闭" : "开启")+"</button>");
                                updateButton.click(function () {
                                    that.save(tdData,rowData.state == 1 ? 0 : 1);
                                });
                                $(td).html(updateButton);
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
                        }else {
                            toastr.clear();
                            toastr.error("查询出错");
                        }
                        return;
                    });
                }catch(e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            save : function (boOrderRemarkId, type) {
                var that = this;
                try{
                    $.post("orderRemark/update",{boOrderRemarkId : boOrderRemarkId, type : type},function (result) {
                        toastr.clear();
                        if (result.success){
                            that.searchInfo();
                            toastr.success("修改成功");
                        }else{
                            toastr.error("修改失败");
                        }
                        return;
                    });
                }catch(e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            }
        }
    });
</script>
