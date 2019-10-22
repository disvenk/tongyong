<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki"> 编辑桌号</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" action="tablemanager/modify" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label>桌号</label>
                                <input type="text" class="form-control" name="tableNumber" v-model="m.tableNumber"
                                       readonly>
                            </div>

                            <div class="form-group">
                                <label>区域名称</label>
                                <select class="form-control" name="areaId" required v-if="areaList"
                                        v-model="m.areaId?m.areaId:selected">
                                    <option v-for="temp in areaList" v-bind:value="temp.id">
                                        {{ temp.name }}
                                    </option>
                                </select>
                                <input class="form-control" value="暂无可选区域" disabled="disabled" v-else/>
                            </div>
                            <div class="form-group">
                                <label>就餐人数</label>
                                <input type="number" class="form-control"
                                       name="customerCount" placeholder="(输入整数)"
                                       v-model="m.customerCount"  min="0">
                            </div>
                        </div>
                        <input type="hidden" name="id" v-model="m.id"/>
                        <input class="btn green" type="submit" value="保存"/>
                        <a class="btn default" @click="cancel">取消</a>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="table-div">

        <div class="clearfix"></div>
        <div class="table-filter"></div>
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>
</div>


<script>
    (function () {


        var cid = "#control";
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            "order": [[2, "asc"]],
            ajax: {
                url: "tablemanager/list_all",
                dataSrc: ""
            },
            columns: [
                {
                    title: "桌位编号",
                    data: "tableNumber",
                },
                {
                    title: "所属区域",
                    data: "areaName"
                },
                {
                    title: "就餐人数",
                    data: "customerCount"
                },
                {
                    title: "状态",
                    data: "seatState",
                    "render": function ( data, type, full, meta ) {
                        if (data) {
                            return '释放'
                        } else {
                            return '锁定'
                        }
                    }
                },

                {
                    title: "操作",
                    data: "id",
                    createdCell: function (td, tdData, rowData, row) {
                        var operator = [
                            C.createEditBtn(rowData)
                        ];
                        operator.push(C.createBtn(rowData).html("下载二维码"));
                        if ( ! rowData['seatState']) {
                            var button = $("<button class='btn btn-xs btn-primary'>释放服务</button>");
                            button.on('click', function() {
                                $.ajax({
                                    type: "post",
                                    url: "tablemanager/removeTable",
                                    dataType: "json",
                                    data:{state:rowData['seatState'],tableNumber:rowData['tableNumber']},
                                    success: function (result) {
                                        if(result.success) {
                                            tb.ajax.reload(null, false);
                                        }
                                    }
                                })
                            });
                            operator.push(button);
                        }


                        if ( ! rowData['seatState']) {
                            var button = $("<button class='btn btn-xs btn-primary'>释放本地</button>");
                            button.on('click', function() {
                                $.ajax({
                                    type: "post",
                                    url: "tablemanager/removeTableLocal",
                                    dataType: "json",
                                    data:{state:rowData['seatState'],tableNumber:rowData['tableNumber']},
                                    success: function (result) {
                                        if(result.success) {
                                            tb.ajax.reload(null, false);
                                        }else {
                                            alert("释放异常")
                                        }
                                    }
                                })
                            });
                            operator.push(button);
                        }

                        $(td).html(operator);
                    }
                }],
        });

        var C = new Controller(null, tb);
        var vueObj = new Vue({
            el: "#control",
            mixins: [C.formVueMix],
            methods: {
                platform: function (model) {
                    window.open("tablemanager/download?id="+model.id);
                }
            }

        });
        C.vue = vueObj;
        //获取 就餐模式
        $.ajax({
            type: "post",
            url: "printer/qiantai",
            dataType: "json",
            success: function (data) {
                if (data.length > 0) {
                    vueObj.$set("printerList", data);
                    vueObj.$set("selected", data[0].id);
                }
            }
        })
        $.ajax({
            type: "post",
            url: "area/list_all",
            dataType: "json",
            success: function (data) {
                if (data.length > 0) {
                    vueObj.$set("areaList", data);
                }
            }
        })
    }());


</script>
