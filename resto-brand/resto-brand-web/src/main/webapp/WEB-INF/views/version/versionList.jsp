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
                    <form role="form" action="{{m.id?'versionPos/modify':'versionPos/create'}}" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">版本号：</label>
                                <div class="col-sm-6">
                                    <%--<select class="form-control" name="versionId" :value="m.versionId">--%>
                                    <select class="form-control" name="versionId" v-model ="m.versionId">
                                        <option v-for="version in versions" :value="version.id" >
                                            {{version.versionNo}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">

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
        <div class="table-operator" >
            <s:hasPermission name="versionPos/create">
                <button class="btn green pull-right" @click="create" v-if="isEdit">新建</button>
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
        var vueObj = new Vue({
            el:"#control",
            data:{
                showform:false,
                versions:[],
                isEdit:true,
                tb:'',
                C:'',
                m:{},
                versionNo:""
            },
            // mixins:[C.formVueMix],
            created: function () {
                var that = this;
                that.initVersionMode();
                that.createTb();
                this.C = new Controller(null,that.tb);
                // console.log(JSON.stringify(that.tb));
            },
            ready:function(){

            },
            methods:{
                save:function(e){
                    var that = this;
                    var formDom = e.target;
                    var showType = e.showType;
                    that.C.ajaxFormEx(formDom,function(){
                        that.cancel();
                        that.tb.ajax.reload();
                    });
                },
                create:function(){
                    this.m={};
                    this.showform = true;
                },
                createTb: function () {
                    var that = this;
                    var $table = $(".table-body>table");
                    this.tb = $table.DataTable({
                        ajax : {
                            url : "versionPos/list_all",
                            dataSrc : ""
                        },
                        columns : [
                            {
                                title : "版本号",
                                data : "versionNo",
                            },
                            {
                                title : "下载地址",
                                data : "downloadAddress",
                                createdCell:function(td,tdData,rowData,row){
                                    console.log(rowData);
                                    if(rowData.id){
                                        that.isEdit = false;
                                    }
                                    that.m = rowData;
                                    console.log(JSON.stringify(that.m));
                                    var a = $("<a href=\""+ rowData.downloadAddress + "\" target=\"_blank\">" + rowData.downloadAddress + "</a>");
                                    $(td).html(a);
                                }
                            },
                            {
                                title : "操作",
                                data : "id",
                                createdCell:function(td,tdData,rowData,row){
                                    var operator=[
                                        <s:hasPermission name="versionPos/modify">
                                        that.createEditBtn(rowData,"versionPos/modify"),
                                        </s:hasPermission>
                                    ];
                                    $(td).html(operator);
                                }
                        }],
                    });
                },
                createEditBtn: function (rowData){
                    var that = this;
                    var button = $("<button class='btn btn-xs btn-primary'>编辑</button>");
                    button.click(function(){
                        that.edit(rowData);
                    });
                    return button;
                },
                edit:function(rowData){
                    this.m= rowData;
                    this.versionNo=this.m.versionNo;
                    this.showform = true;
                },
                cancel:function(){
                    this.m={};
                    this.showform = false;
                },
                initVersionMode: function () {
                    var that = this;
                    $.ajax({
                        url: "version/list_all",
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            that.versions = data;
                        }
                    })
                }
            }
        });
    }());

</script>
