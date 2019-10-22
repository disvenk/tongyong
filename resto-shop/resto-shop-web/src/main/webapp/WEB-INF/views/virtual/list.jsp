<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki"> 表单</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" action="{{m.id?'virtual/modify':'virtual/create'}}" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label>虚拟餐品名称</label>
                                <input type="text" class="form-control" name="name" required v-model="m.name"  >
                            </div>
                            <div class="form-group">
                                <label>出餐厨房</label>
                                <div class="form-group" >
                                    <label v-for="kitchen in kitchenList">
                                         <input type="checkbox" name="kitchenList" :value="kitchen.id" v-model="m.kitchenList"> {{kitchen.name}} &nbsp;&nbsp;
                                    </label>
                                </div>
                            </div>
                            <div>
                                <label class="col-sm-1 control-label" style="padding-left:0px">是否启用</label>
                                <div class="col-sm-9" v-if="m.isUsed !=null">
                                    <div>
                                        <label>
                                            <input type="radio" name="isUsed" required v-model="m.isUsed" value="0">
                                            启用
                                        </label>
                                        <label>
                                            <input type="radio" name="isUsed" required v-model="m.isUsed" value="1">
                                            不启用
                                        </label>
                                    </div>
                                </div>
                                <div class="col-sm-9" v-if="m.isUsed ==null">
                                    <div>
                                        <label>
                                            <input type="radio" name="isUsed" required v-model="m.isUsed" value="0" checked="checked">
                                            启用
                                        </label>
                                        <label>
                                            <input type="radio" name="isUsed" required v-model="m.isUsed" value="1">
                                            不启用
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <input type="hidden" name="id" v-model="m.id" />
                        <input class="btn green"  type="submit"  value="保存"/>
                        <a class="btn default" @click="cancel" >取消</a>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="table-div">
        <div class="table-operator">
            <%--<s:hasPermission name="virtual/add">--%>
                <button class="btn green pull-right" @click="create">新建</button>
            <%--</s:hasPermission>--%>
        </div>
        <div class="clearfix"></div>
        <div class="table-filter"></div>
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>
</div>


<script>
    (function(){
        var cid="#control";
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            ajax : {
                url : "virtual/listAll",
                dataSrc : ""
            },
            columns : [
                {
                    title : "虚拟餐品名称",
                    data : "name",
                },
                {
                    title: "出餐厨房",
                    data: "kitchens",
                    defaultContent: "",
                    createdCell: function (td, tdData) {
                        $(td).html('');
                        for (var i in tdData) {
                            if (tdData[i].name) {
                                var span = $("<span class='btn blue btn-xs'></span>");
                                $(td).append(span.html(tdData[i].name));
                            }

                        }
                    }
                },
                {
                    title : "是否启用",
                    data : "isUsed",
                    createdCell:function(td,tdData){
                        if(tdData == 0){
                            $(td).html("<td>启用</td>");
                        }else{
                            $(td).html("<td>不启用</td>");
                        }
                    }
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        var operator=[
                            <%--<s:hasPermission name="virtual/delete">--%>
                            C.createDelBtn(tdData,"virtual/delete"),
                            <%--</s:hasPermission>--%>
                            <%--<s:hasPermission name="virtual/edit">--%>
                            C.createEditBtn(rowData),
                            <%--</s:hasPermission>--%>
                        ];
                        $(td).html(operator);
                    }
                }],
        });

        var C = new Controller(null,tb);
        var vueObj = new Vue({
            el:"#control",
            data:{
                typeNames:[{"id":"1","value":"启用"},{"id":"2","value":"不启用"}],
                kitchenList: [],
            },
            mixins:[C.formVueMix],
            methods:{
                uploadSuccess:function(url){
                    $("[name='isUsed']").val(url).trigger("change");
                    C.simpleMsg("提交成功");
                },
                uploadError:function(msg){
                    C.errorMsg(msg);
                },
                create:function(){
                    this.m={
                        kitchenList:[]
                    };
                    this.openForm();
                },
//                checkNull: function () {
//                    if (this.m.kitchenList.length <= 0) {//出餐厨房 非空验证
//                        if(!confirm("是否不选择出餐厨房！")){
//                            return true;
//                        }
//                    }
//                    return false;
//                },
                save:function(e){
                    var that = this;
                    var formDom = e.target;
                    var isUsed = e.isUsed;

//                    if (this.checkNull()) {//验证必选项(出参厨房)
//                        return;
//                    }
                    if (this.m.kitchenList.length <= 0) {//出餐厨房 非空验证
                        toastr.clear();
                        toastr.error("必须选择出餐厨房！");
                    }else{
                        C.ajaxFormEx(formDom,function(){
                            that.cancel();
                            tb.ajax.reload();
                        });
                    }
                },
                edit: function (model) {
                    var that = this;
                    action = "edit";
                    $.post("virtual/getVirtualById", {id: model.id}, function (result) {
                        var virtual = result.data;
                        that.showform = true;
                        that.m = virtual;
                        if (!virtual.kitchenList) {
                            virtual.kitchenList = [];
                        }
                    });
                },
            },
            created: function () {
                tb.search("").draw();
                var that = this;
                this.$watch("showform", function () {
                    if (this.showform) {
                        $("#article-dialog").modal("show");
                        var n = $('.color-mini').minicolors({
                            change: function (hex, opacity) {
                                if (!hex) return;
                                if (typeof console === 'object') {
                                    $(this).attr("value", hex);
                                }
                            },
                            theme: 'bootstrap'
                        });
                        $("#article-dialog").on("hidden.bs.modal", function () {
                            that.showform = false;
                        });
                    } else {
                        $("#article-dialog").modal("hide");
                        $(".modal-backdrop.fade.in").remove();
                    }
                });
                this.$watch("m", function () {
                    if (this.m.id) {
                        $('.color-mini').minicolors("value", this.m.controlColor);
                    }
                });

                $.post("kitchen/list_all", null, function (data) {
                    that.kitchenList = data;
                });
            }
        });
        C.vue=vueObj;
    }());

</script>
