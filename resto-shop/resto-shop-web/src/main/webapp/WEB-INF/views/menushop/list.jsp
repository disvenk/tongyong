<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="control">
    <h2 class="text-center"><strong>菜单管理</strong></h2><br/>
    <div class="row" id="searchTools">
        <div>
            <div>
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist" id="ulTab">
                    <li role="presentation" class="active" @click="chooseType(1)">
                        <a href="#dayReport" aria-controls="dayReport" role="tab" data-toggle="tab">
                            <strong>启用({{enableListCount}})</strong>
                        </a>
                    </li>
                    <li role="presentation" @click="chooseType(2)">
                        <a href="#revenueCount" aria-controls="revenueCount" role="tab" data-toggle="tab">
                            <strong>禁用({{disableListCount}})</strong>
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <!-- 启用 -->
                    <div role="tabpanel" class="tab-pane active" id="dayReport">
                        <div class="panel panel-success">
                            <div class="panel-body">
                                <table id="enableTable" class="table table-striped table-bordered table-hover"
                                       style="width: 100%;">
                                </table>
                            </div>
                        </div>
                    </div>

                    <!-- 禁用 -->
                    <div role="tabpanel" class="tab-pane" id="revenueCount">
                        <div class="panel panel-primary" style="border-color:white;">
                            <div class="panel panel-success">
                                <div class="panel-body">
                                    <table id="disableTable" class="table table-striped table-bordered table-hover"
                                           style="width: 100%;">
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
<script src="assets/customer/date.js" type="text/javascript"></script>
<script>
    var enableTableAPI = null;

    var disableTableAPI = null;

    var vueObj = new Vue({
        el : "#control",
        data : {
            currentType : 1,//当前选中页面
            enableTable : {},
            disableTable : {},
            enableListCount : 0,
            disableListCount : 0
        },
        created : function() {
            this.initDataTables();
            this.searchInfo();
        },
        methods : {
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                //启用datatable对象
                that.enableTable=$("#enableTable").DataTable({
                    lengthMenu: [ [20, 75, 100, -1], [20, 75, 100, "All"] ],
                    order: [[ 0, "asc" ]],
                    columns : [
                        {
                            title : "序号",
                            data : "id"
                        },
                        {
                            title : "菜单类型",
                            data : "typeDescribe",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "店铺名称",
                            data : "shopName",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "菜单版本号",
                            data : "menuVersion",
                            orderable : false
                        },
                        {
                            title : "菜单名称",
                            data : "menuName",
                            orderable : false
                        },
                        {
                            title : "启用时间",
                            data : "createTime",
                            orderable : false,
                            createdCell: function (td, tdData) {
                                var text = new Date(tdData).format("yyyy-MM-dd");
                                $(td).text(text);
                            }
                        },
                        {
                            title : "计划使用周期",
                            data : "menuCycle",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var text = "长期有效";
                                if (tdData == 2) {
                                    var startTime = new Date(rowData.startTime).format("yyyy-MM-dd");
                                    var endTime = new Date(rowData.endTime).format("yyyy-MM-dd");
                                    text = startTime + "至" + endTime;
                                }
                                $(td).text(text);
                            }
                        },
                        {
                            title : "菜单状态",
                            data : "state",
                            orderable : false,
                            createdCell: function (td) {
                                $(td).text("启用");
                            }
                        }
                    ],
                    initComplete: function () {
                        enableTableAPI = this.api();
                        that.enableTableStructure();
                    }
                });
                //套餐datatable对象
                that.disableTable=$("#disableTable").DataTable({
                    lengthMenu: [ [20, 75, 100, -1], [20, 75, 100, "All"] ],
                    order: [[ 0, "asc" ]],
                    columns : [
                        {
                            title : "序号",
                            data : "id"
                        },
                        {
                            title : "菜单类型",
                            data : "typeDescribe",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "店铺名称",
                            data : "shopName",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "菜单版本号",
                            data : "menuVersion",
                            orderable : false
                        },
                        {
                            title : "菜单名称",
                            data : "menuName",
                            orderable : false
                        },
                        {
                            title : "启用时间",
                            data : "createTime",
                            orderable : false,
                            createdCell: function (td, tdData) {
                                var text = new Date(tdData).format("yyyy-MM-dd");
                                $(td).text(text);
                            }
                        },
                        {
                            title : "禁用时间",
                            data : "updateTime",
                            orderable : false,
                            createdCell: function (td, tdData) {
                                var text = new Date(tdData).format("yyyy-MM-dd");
                                $(td).text(text);
                            }
                        },
                        {
                            title : "菜单状态",
                            data : "state",
                            orderable : false,
                            createdCell: function (td) {
                                $(td).text("禁用");
                            }
                        }
                    ],
                    initComplete: function () {
                        disableTableAPI = this.api();
                        that.disableTableAPIStructure();
                    }
                });
            },
            //切换启用跟禁用两种状态
            chooseType:function (type) {
              this.currentType= type;
              this.searchInfo();
            },
            searchInfo : function() {
                var that = this;
                toastr.clear();
                toastr.success("查询中...");
                try{
                    var enableAPI = enableTableAPI;
                    var disableAPI = disableTableAPI;
                    $.post("menushop/list_all", function(result) {
                        if(result.success == true){
                            toastr.clear();
                            toastr.success("查询成功");
                            //清空搜索列
                            enableAPI.search('');
                            var type01 = enableAPI.column(1);
                            type01.search('', true, false);
                            var shopName01 = enableAPI.column(2);
                            shopName01.search('', true, false);
                            that.enableListCount = result.data.enableListCount;
                            that.enableTable.clear();
                            that.enableTable.rows.add(result.data.enableList).draw();
                            //重绘搜索列
                            that.enableTableStructure();
                            disableAPI.search('');
                            var type02 = enableAPI.column(1);
                            type02.search('', true, false);
                            var shopName02 = enableAPI.column(2);
                            shopName02.search('', true, false);
                            that.disableListCount = result.data.disableListCount;
                            that.disableTable.clear();
                            that.disableTable.rows.add(result.data.disableList).draw();
                            //重绘搜索列
                            that.disableTableAPIStructure();
                        }else{
                            toastr.clear();
                            toastr.error("查询失败");
                        }
                    });
                }catch(e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            enableTableStructure : function(){
                var api = enableTableAPI;
                var columnsSetting = api.settings()[0].oInit.columns;
                $(columnsSetting).each(function (i) {
                    if (this.s_filter) {
                        var column = api.column(i);
                        var select = $('<select id=""><option value="">' + this.title + '(全部)</option></select>');
                        column.data().unique().each(function (d) {
                            select.append('<option value="' + d + '">' + d + '</option>')
                        });
                        select.appendTo($(column.header()).empty()).on('change', function () {
                            var val = $.fn.dataTable.util.escapeRegex(
                                    $(this).val()
                            );
                            column.search(val ? '^' + val + '$' : '', true, false).draw();
                        });
                    }
                });
            },
            disableTableAPIStructure : function(){
                var api = disableTableAPI;
                var columnsSetting = api.settings()[0].oInit.columns;
                $(columnsSetting).each(function (i) {
                    if (this.s_filter) {
                        var column = api.column(i);
                        var select = $('<select id=""><option value="">' + this.title + '(全部)</option></select>');
                        column.data().unique().each(function (d) {
                            select.append('<option value="' + d + '">' + d + '</option>')
                        });
                        select.appendTo($(column.header()).empty()).on('change', function () {
                            var val = $.fn.dataTable.util.escapeRegex(
                                $(this).val()
                            );
                            column.search(val ? '^' + val + '$' : '', true, false).draw();
                        });
                    }
                });
            }
        }
    });
</script>

