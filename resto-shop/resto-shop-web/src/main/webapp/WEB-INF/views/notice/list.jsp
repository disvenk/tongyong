<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">新建通知</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" class="form-horizontal" action="{{m.id?'notice/modify':'notice/create'}}"
                          @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">通知名称：</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" name="title" v-model="m.title"
                                           required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">通知内容：</label>
                                <div class="col-sm-8">
                                    <textarea class="form-control" name="content" v-model="m.content"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">排序：</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" required name="sort" v-model="m.sort">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">显示图片：</label>
                                <div class="col-sm-8">
                                    <input type="hidden" name="noticeImage" v-model="m.noticeImage">
                                    <img-file-upload class="form-control" @success="uploadSuccess" @error="uploadError"
                                                     cut="false"></img-file-upload>
                                    <img v-if="m.noticeImage" :src="m.noticeImage"
                                         onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px"
                                         class="img-rounded">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">选择通知类型：</label>
                                <div class="col-sm-8 radio-list">
                                    <label class="radio-inline" v-for="(key, val) in noticeType">
                                        <input type="radio" name="noticeType" v-model="m.noticeType" :value="key">{{val}}
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">是否启用：</label>
                                <div class="col-sm-8 radio-list">
                                    <label class="radio-inline">
                                        <input type="radio" name="status" v-model="m.status" value="1"> 启用
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="status" v-model="m.status" value="0"> 不启用
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">显示次数：</label>
                                <div class="col-sm-8">
                                    <input type="number" min="1" class="form-control" required name="showTimes"
                                           v-model="m.showTimes">
                                </div>
                            </div>
                            <div class="form-group  col-md-12">
                                <label class="col-sm-3 control-label">显示时间</label>
                                <div class="col-md-8">

                                    <label v-for="time in supportTimes">
                                        <input type="checkbox" name="supportTimes" :value="time.id"  v-model="m.supportTimes"> <span
                                            :class="{'text-danger':time.shopName}">{{time.name}}</span>
                                        &nbsp;&nbsp;
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="text-center">
                            <input type="hidden" name="id" v-model="m.id"/>
                            <input class="btn green" type="submit" value="保存"/>
                            <a class="btn default" @click="cancel">取消</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="table-div">
        <div class="table-operator">
            <s:hasPermission name="notice/add">
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
        var cid = "#control";
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            ajax: {
                url: "notice/list_all",
                dataSrc: ""
            },
            order: [[6, "desc"], [0, "asc"]],
            columns: [
                {
                    title: "排序",
                    data: "sort",
                },
                {
                    title: "通知标题",
                    data: "title",
                },
                {
                    title: "通知内容",
                    data: "content",
                },
                {
                    title: "图片",
                    data: "noticeImage",
                    defaultContent: '',
                    createdCell: function (td, tdData) {
                        if (tdData != null && tdData.substring(0, 4) == "http") {
                            $(td).html("<img src=\"" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:40px;width:80px;\"/>");
                        } else {
                            $(td).html("<img src=\"/" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:40px;width:80px;\"/>");
                        }
                    }
                },
                {
                    title: "通知类型",
                    data: "noticeType",
                    createdCell: function (td, data) {
                        $(td).html(vueObj.noticeType[data]);
                    }
                },
                {
                    title: "创建时间",
                    data: "createDate",
                    createdCell: function (td, tdData, rowData, row) {
                        var temp = new Date(tdData);
                        temp = temp.format("yyyy-MM-dd hh:mm:ss");
                        $(td).html(temp);
                    }
                },
                {
                    title: "是否启用",
                    data: "status",
                    createdCell: function (td, tdData, rowData) {
                        var content = {
                            text: "禁用", style: "btn btn-danger", fun: function () {
                                var data = {id: rowData.id, status: "0"};
                                vueObj.chageStatus(data);
                            }
                        }
                        if (tdData == 0) {
                            content = {
                                text: "启用", style: "btn btn-success", fun: function () {
                                    var data = {id: rowData.id, status: "1"};
                                    vueObj.chageStatus(data);
                                }
                            };
                        }
                        var button = $("<button>").html(content.text).addClass(content.style).click(content.fun);
                        $(td).html(button);
                    }
                },
                {
                    title: "操作",
                    data: "id",
                    createdCell: function (td, tdData, rowData, row) {
                        var operator = [
                            <s:hasPermission name="notice/delete">
                            C.createDelBtn(tdData, "notice/delete"),
                            </s:hasPermission>
                            <s:hasPermission name="notice/edit">
                            C.createEditBtn(rowData),
                            </s:hasPermission>
                        ];
                        $(td).html(operator);
                    }
                }],
        });

        var C = new Controller(null, tb);
        var vueObj = new Vue({
            el: "#control",
            mixins: [C.formVueMix],
            data: {
                noticeType: {
                    1: "普通通知",
                    3: "大图弹窗",
                    2: "注册通知",
                },
                supportTimes: [],
            },
            methods: {
                create: function () {
                    this.m = {
                        sort: 0,
                        noticeType: 1,
                        title: "通知" + new Date().getTime(),
                        status: 1,
                        supportTimes:[]
                    };
                    this.showform = true;
                },

                edit:function(model){
                    var that = this;
                    $.post("notice/list_one", {id: model.id}, function (result) {
                        that.m= result.data;
                    });

                    that.openForm();
                },
                uploadSuccess: function (url) {
                    $("[name='noticeImage']").val(url).trigger("change");
                    C.simpleMsg("上传成功");
                },
                uploadError: function (msg) {
                    C.errorMsg(msg);
                },
                chageStatus: function (data) {
                    $.post("notice/chageStatus", data, function (result) {
                        if (result.success) {
                            C.simpleMsg("操作成功");
                        } else {
                            C.errorMsg("操作失败");
                        }
                        tb.ajax.reload();
                    })
                }
            }, created: function () {
                var that = this;
                $.post("supporttime/list_all", null, function (data) {
                    that.supportTimes = data;
                });
            }

        });
        C.vue = vueObj;
    }());
</script>
