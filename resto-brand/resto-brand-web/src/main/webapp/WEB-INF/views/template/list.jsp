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
                    <form role="form" action="{{m.id?'template/modify':'template/create'}}" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label>模板编号</label>
                                <input type="text" class="form-control" name="templateNumber" required v-model="m.templateNumber"  >
                            </div>
                            <div class="form-group">
                                <label>模板描述</label>
                                <input type="text" class="form-control" name="remark" required v-model="m.remark">
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
            <s:hasPermission name="template/add">
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
    (function(){
        var cid="#control";
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            ajax : {
                url : "template/list_all",
                dataSrc : ""
            },
            columns : [
                {
                    title : "模板编号",
                    data : "templateNumber",
                },
                {
                    title : "模板描述",
                    data : "remark",
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        var operator=[
                            <s:hasPermission name="template/delete">
                            C.createDelBtn(tdData,"template/delete"),
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
            },
            mixins:[C.formVueMix],
            methods:{
                save:function(e){
                    var that = this;
                    var formDom = e.target;
                    var showType = e.showType;
                    C.ajaxFormEx(formDom,function(){
                        that.cancel();
                        tb.ajax.reload();
                    });
                },
            }
        });
        C.vue=vueObj;
    }());

</script>
