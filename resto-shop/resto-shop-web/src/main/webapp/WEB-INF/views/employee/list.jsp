<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<div id="control">
    <img src="employee/QR?employeeId=3hfa78f74efa46c1b8e96ca99b419818" width=50 height=40 onMouseover="this.width=80; this.height=60" onMouseout="this.width=50;this.height=40">

    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki"> 表单</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" action="{{m.id?'employee/modify':'employee/addData'}}" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label>员工姓名</label>
                                <input type="text" class="form-control" name="name" v-model="m.name">
                            </div>
                            <div class="form-group">
                                <label class="control-label">员工性别</label>
                                <div class="radio-list" style="margin-left: 20px;">
                                    <label class="radio-inline">
                                        <input type="radio" class="md-radiobtn" name="sex" value="男" v-model="m.sex" >男</label>
                                    <label class="radio-inline">
                                        <input type="radio" class="md-radiobtn" name="sex" value="女" v-model="m.sex" >女</label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>手机号</label>
                                <input type="text" class="form-control" name="telephone" v-model="m.telephone" @blur="checkTelephone(m.telephone)">
                            </div>
                            <div class="form-group">
                                <label>额度</label>
                                <input type="number" class="form-control" name="money" v-model="m.money">
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

    <div class="modal fade" id="employeeRoModal" tabindex="-1" role="dialog" data-backdrop="static">
        <div class="modal-dialog modal-full">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true" @click="closeModal"></button>
                </div>
                <div class="modal-body">

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-info btn-block" data-dismiss="modal" aria-hidden="true" @click="closeModal">关闭</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>

    <%--<button class="btn green pull-right" @click="uploadFile">下载全部</button>--%>
    <%--<a href="employee/downloadFile">下载全部</a>--%>
    <a href="employee/downloadFile" class="btn btn-primary btn-lg active" role="button">下载全部二维码</a>

    <div class="table-div">
        <div class="table-operator">
            <s:hasPermission name="employee/add">
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
                url : "employee/list_all",
                dataSrc : ""
            },
            columns : [
                {
                    title:"姓名",
                    data:"name"
                },
                {
                    title:"性别",
                    data:"sex"
                },
                {
                    title:"手机号",
                    data:"telephone"
                }
                ,
                {
                    title : "额度",
                    data : "money",
                },
                {
                    title : "二维码",
                    data : "qrCode",
                    createdCell:function (td,tdData,row,rowData) {
                       var img = "<img src=\"employee/QR?employeeId="+tdData+"\" width=40 height=30 onMouseover=\"this.width=130; this.height=110\" onMouseout=\"this.width=50;this.height=40\">";
                        var str  = "employee/downloadFile?id="+tdData+"&&name="+row.name;
                        var download= "<a href="+str+"> 点击下载</a>";
                        $(td).html(img+"<br/>"+download);


                    }

                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        var operator=[
                            <s:hasPermission name="employee/delete">
                            C.createDelBtn(tdData,"employee/delete"),
                            </s:hasPermission>
                            <s:hasPermission name="employee/modify">
                            C.createEditBtn(rowData),
                            </s:hasPermission>
                            <s:hasPermission name="employee/assign">
                            vueObj.createAssignBtn(tdData,operator),
                            </s:hasPermission>
                        ];
                        $(td).html(operator);

                    }
                } ],
        });

        var C = new Controller(null,tb);

        var vueObj = new Vue({
            el:"#control",
            mixins:[C.formVueMix],
            methods:{
                    openModal : function(url, modalTitle,employeeId) {
                    $.post(url, {"employeeId":employeeId},function(result) {
                        var modal = $("#employeeRoModal");
                        modal.find(".modal-body").html(result);
                        modal.find(".modal-title > strong").html(modalTitle);
                        modal.modal()
                    })

                },
                showEmployeeRoleMoal : function(title,employeeId) {
                    $("#employeeRoModal").modal('show');
                    this.openModal("employee/employee_role", title,employeeId);
                },
                closeModal : function () {
                    $("#employeeRoModal").html();
                    $("#employeeRoModal").modal();
                },
                createBtn :function (btnName,btnValue,btnClass,btnfunction) {
                    return $('<input />', {
                    name : btnName,
                    value : btnValue,
                    type : "button",
                    class : "btn " + btnClass,
                    click : btnfunction
                    })
                },
                createAssignBtn : function (tdData,operator) {
                    var btn = vueObj.createBtn(null, "分配角色",
                    "btn-sm btn-info",
                    function() {
                    vueObj. showEmployeeRoleMoal("给用户分配店铺角色",tdData)
                    })
                   return btn;
                },
                uploadFile : function () {
                  $.ajax({
                      url:'employee/downloadFile',
                      data:{
                          "id":'',
                          "name":''
                      },
                      success:function (result) {
                          toastr.success("下载成功")
                      }

                          }
                  )
                },

                checkTelephone : function (tdData) {
                    //页面做判断

                    if(!(/^1[3|4|5|7|8]\d{9}$/.test(tdData))){
                        toastr.error("请填写正确的手机号码!");
                        return ;
                    }

                    $.post("employee/checkeTelephone",{"telephone":tdData},function (result) {
                        if(result.success){
                            toastr.error(result.message);
                            return ;
                        }
                    })

                }
                
                
                
             },
        });
        C.vue=vueObj;


    }());

</script>
