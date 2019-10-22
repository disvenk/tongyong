<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control" style="display: none">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki"> 新增表单 </span>
                    </div>
                </div>
                <div class="tab-pane active">
                    <form role="form" id="tForm" action="tableQrcode/create" @submit.prevent="save">
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">选择店铺：</label>
                                <div class="col-sm-5">
                                    <select name="shopId" id="shopId" class="form-control">
                                        <option v-model="shop.id" value="{{shop.id}}" v-for="shop in shopList">{{shop.name}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">开始桌号：</label>
                                <div class="col-sm-5">
                                    <input type="number" class="form-control" name="beginTableNumber"
                                           id="beginTableNumber">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">结束桌号：</label>
                                <div class="col-sm-5">
                                    <input type="number" class="form-control" name="endTableNumber"
                                           id="endTableNumber">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">忽略桌号：</label>
                                <div class="col-sm-5">
                                    <input type="text" class="form-control" name="ignoreNumber" id="ignoreNumber"
                                           placeholder="如有多个桌号，请使用逗号分隔。">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-3">
                                    <input class="btn green" type="button" @click="createTableNumber" value="生成"/>
                                    <span id="createDownLoad"></span>
                                    <a class="btn default" @click="cancel">关闭</a>
                                </div>
                                <div id="createFail" class="text-danger"></div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row form-div" v-if="showPlatform">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki"> 修改表单 </span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" action="tableQrcode/modify" @submit.prevent="save">
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><strong>桌号：</strong></label>
                                <div class="col-sm-5">
                                    <input type="number" class="form-control" name="tableNumber"
                                           v-model="m.tableNumber">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><strong>是否开启：</strong></label>
                                <div class="col-sm-8">
                                    <div class="md-radio-inline">
                                        <div class="md-radio">
                                            <!-- 判断是否 绑定的对象是否有值，如果没有则不绑定 -->
                                            <input type="radio" class="md-radiobtn" id="isAddRatio_yes" name="state" value="1" v-model="m.state" v-if="m.id">
                                            <input type="radio" class="md-radiobtn" id="isAddRatio_yes" name="state" value="1"  v-if="!m.id" checked="checked">
                                            <label for="isAddRatio_yes">
                                                <span></span>
                                                <span class="check"></span>
                                                <span class="box"></span>开启
                                            </label>
                                        </div>
                                        <div class="md-radio">
                                            <input type="radio" class="md-radiobtn" id="isAddRatio_no" name="state" value="0" v-model="m.state" v-if="m.id">
                                            <input type="radio" class="md-radiobtn" id="isAddRatio_no" name="state" value="0" v-if="!m.id">
                                            <label for="isAddRatio_no">
                                                <span></span>
                                                <span class="check"></span>
                                                <span class="box"></span>关闭
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-3">
                                    <input type="hidden" name="id" v-model="m.id"/>
                                    <input class="btn green" type="submit" value="保存"/>
                                    <a class="btn default" @click="cancel">取消</a>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="table-div">
        <div class="table-operator">
            <span id="validataMsg" class="text-danger"></span>
            <s:hasPermission name="tableQrcode/download">
                <button class="btn green" style="margin-right: 20px;" @click="download">下载</button>
            </s:hasPermission>
            <s:hasPermission name="tableQrcode/add">
                <button class="btn green pull-right" @click="create">新建</button>
            </s:hasPermission>
        </div>
        <div class="clearfix"></div>
        <div class="table-filter"></div>
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>
</div>

<script>
    (function () {
        var shopMap = new HashMap();
        var shopList = [];
        $.post("tableQrcode/shopList", null, function (data) {
            for (var i = 0; i < data.length; i++) {
                shopMap.put(data[i].id, data[i]);
                shopList.push(data[i]);
            }
            $("#control").show();
            var cid = "#control";
            var tableNumber = [];
            var $table = $(".table-body>table");
            var tb = $table.DataTable({
                "lengthMenu": [[50, 75, 100, 150], [50, 75, 100, "All"]],
                ajax: {
                    url: "tableQrcode/list_all",
                    dataSrc: ""
                },
                deferRender: true,
                ordering: false,
                columns: [
                    {
                        title: "全选 <input type='checkbox' id='checkAll' />",
                        data: "id",
                        width: '5%',
                        createdCell: function (td, tdData) {
                            $(td).html("<input type='checkbox' style='width:30px;height:30px' value=\"" + tdData + "\" name= 'tableNumberCheck' />");
                        },
                    },
                    {
                        title: "店铺名称",
                        data: "shopDetailId",
                        createdCell: function (td, tdData) {
                            $(td).html(shopMap.get(tdData).name);
                        },
                        s_filter: true,
                        s_render: function (d) {
                            return shopMap.get(d).name;
                        },
                    },
                    {
                        title: "桌号",
                        data: "tableNumber",
                    },
                    {
                        title: "创建时间",
                        data: "createTime",
                        createdCell: function (td, tdData) {
                            $(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm:ss"));
                        }
                    },
                    {
                        title: "修改时间",
                        data: "updateTime",
                        createdCell: function (td, tdData) {
                            if (tdData != null) {
                                $(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm:ss"));
                            }

                        }
                    },
                    {
                        title: "是否开启",
                        data: "state",
                        createdCell: function (td, tdData) {
                            if (tdData == 1) {
                                $(td).html("开启");
                            } else if (tdData == 0) {
                                $(td).html("未开启")
                            }
                        }
                    },

                    {
                        title: "操作",
                        data: "id",
                        createdCell: function (td, tdData, rowData, row) {
                            var operator = [];
                            <s:hasPermission name="tableQrcode/modify">
                            operator.push(C.createBtn(rowData).html("编辑"));
                            </s:hasPermission>
                            $(td).html(operator);
                        }
                    }],

                initComplete: function () {
                    var api = this.api();
                    api.search('');
                    var data = api.data();
                    for (var i = 0; i < data.length; i++) {
                        tableNumber.push(data[i]);
                    }

                    $("#checkAll").change(function () {
                        if ($("#checkAll").prop("checked")) {
                            $("input[name='tableNumberCheck']").prop("checked", true);

                        } else {
                            $("input[name='tableNumberCheck']").prop("checked", false);
                        }
                    });

                    var columnsSetting = api.settings()[0].oInit.columns;
                    $(columnsSetting).each(function (i) {
                        if (this.s_filter) {
                            var column = api.column(i);
                            var title = this.title;
                            var select = $('<select><option value="">' + this.title + '(全部)</option></select>');
                            var that = this;
                            column.data().unique().each(function (d) {
                                select.append('<option value="' + d + '">' + ((that.s_render && that.s_render(d)) || d) + '</option>')
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
            });

            var C = new Controller(null, tb);
            var vueObj = new Vue({
                el: "#control",
                mixins: [C.formVueMix],
                data: {
                    showPlatform: false,
                    showform: false,
                    m: null,
                    shopList: null,
                },
                created: function () {
                    this.shopList = shopList;
                },
                methods: {
                    cancel: function () {
                        this.showPlatform = false;
                        this.showform = false;
                    },
                    platform: function (model) {
                        this.m = model;
                        var that = this;
                        that.showPlatform = true;
                    },
                    download: function () {
                        var tableNumbers = null;
                        var temp = $(":checkbox[name='tableNumberCheck']");
                        for (var i = 0; i < temp.length; i++) {
                            if ($(temp[i]).prop("checked")) {
                                if (tableNumbers == null) {
                                    tableNumbers = parseInt($(temp[i]).prop("value"));
                                } else {
                                    tableNumbers = tableNumbers + "," + parseInt($(temp[i]).prop("value"));
                                }
                            }
                        }
                        $.post("tableQrcode/download", {ids: JSON.stringify(tableNumbers)}, function (data) {
                            if (data.success) {
                                window.location.href = "tableQrcode/downloadFile?fileName=" + data.message;
                            } else {
                                $("#validataMsg").html("网络好像出了点问题，再试一次吧！");
                            }
                        })
                    },
                    createTableNumber: function () {
                        var data = $("#tForm").serialize();
                        var beginIndex = parseInt($("#beginTableNumber").val());
                        var endIndex = parseInt($("#endTableNumber").val());
                        var ignoreIndex = parseInt($("#ignoreNumber").val());
                        if (beginIndex > endIndex) {
                            $("#createFail").html("开始桌号不能大于结束桌号！");
                        } else if (beginIndex == 0 && endIndex == 0 && ignoreIndex == 0){
                            $("#createFail").html("请输入开始桌号 和 结束桌号！");
                        }  else if (beginIndex == endIndex && beginIndex == ignoreIndex){
                            $("#createFail").html("开始桌号 和 结束桌号 和 忽略桌号 不能相同！");
                        } else {
                            $("#createFail").html("");
                            $.post("tableQrcode/create", data, function (data) {
                                if (data.success) {
                                    tb.ajax.reload();
                                    $("#createDownLoad").html("<span class='btn blue' onclick='downloadNow()' >下载</span>");
                                } else {
                                    $("#createFail").html("您输入的桌号全部存在，请核实后再次输入！");
                                }
                            });
                        }
                    }
                }
            });
            C.vue = vueObj;
        });
    }());

    function downloadNow(){
        var data = $("#tForm").serialize();
        $.post("tableQrcode/run", data, function (data) {
            if (data.success) {
                window.location.href = "tableQrcode/downloadFile?fileName=" + data.message;
            } else {
                $("#createFail").html("网络好像出了点问题，再试一次吧！");
            }
        })
    }
</script>