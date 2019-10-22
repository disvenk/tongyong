<%@ page language="java" pageEncoding="utf-8"%>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">{{newEmployee.id == null ? '新建' : '编辑'}}员工</span>
                    </div>
                </div>

                <div class="portlet-body">
                    <form role="form" class="form-horizontal" @submit.prevent="save">
                        <input type="hidden" name="id" v-model="newEmployee.id"/>
                        <div class="form-body">
                            <div class="form-group">
                                <label  class="col-sm-2 control-label">姓名：</label>
                                <div class="col-sm-8">
                                    <input class="form-control" type="text" name="name" v-model="newEmployee.name" required placeholder="请输入姓名">
                                </div>
                            </div>
                            <div class="form-group">
                                <label  class="col-sm-2 control-label">手机号：</label>
                                <div class="col-sm-8">
                                    <input class="form-control" type="text" name="telephone" v-model="newEmployee.telephone" required placeholder="请输入手机号">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">性别：</label>
                                <div  class="col-md-8">
                                    <label class="radio-inline">
                                        <input type="radio" name="sex" value="1" v-model="newEmployee.sex"> 男
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="sex" value="2" v-model="newEmployee.sex"> 女
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">分配角色：</label>
                                <div  class="col-md-8">
                                    <label class="radio-inline">
                                        <input type="radio" name="roleType" value="1" v-model="newEmployee.roleType"> 员工
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="roleType" value="2" v-model="newEmployee.roleType"> 店长
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">是否启用：</label>
                                <div  class="col-md-8">
                                    <label class="radio-inline">
                                        <input type="radio" name="state" value="1" v-model="newEmployee.state"> 启用
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="state" value="0" v-model="newEmployee.state"> 不启用
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label  class="col-sm-2 control-label">所属门店：</label>
                                <div class="col-sm-8">
                                    <select class="form-control" v-model="newEmployee.shopDetailId">
                                        <option value="0">全部门店</option>
                                        <option value="{{shopDetail.id}}" v-for="shopDetail in shopDetails">
                                            {{shopDetail.name}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="form-group text-center">
                            <input class="btn green"  type="submit"  value="保存"/>&nbsp;&nbsp;&nbsp;
                            <a class="btn default" @click="colseShowForm" >取消</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
	
	<div class="table-div">
		<div class="table-operator">
			<button class="btn green pull-right" @click="openShowForm">添加员工</button>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter"></div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered" id = "newEmployeeTable"></table>
		</div>
	</div>
</div>


<script>
    var newEmployeeTableAPI;
    var vueObj = new Vue({
        el : "#control",
        data : {
            showform : false,
            newEmployeeTable : {},
            newEmployee : {},
            shopDetails : []
        },
        created : function() {
            this.initDataTables();
            this.searchInfo();
        },
        methods : {
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                that.newEmployeeTable = $("#newEmployeeTable").DataTable({
                    lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
                    order: [[ 2, 'asc' ]],
                    columns : [
                        {
                            title : "店铺",
                            data : "shopName",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "角色",
                            data : "roleValue",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "姓名",
                            data : "name",
                            orderable : false
                        },
                        {
                            title : "性别",
                            data : "sexValue",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "手机号",
                            data : "telephone",
                            orderable : false
                        },
                        {
                            title : "微信昵称",
                            data : "nickName",
                            orderable : false
                        },
                        {
                            title : "状态",
                            data : "stateValue",
                            orderable : false,
                            s_filter: true,
                            createdCell: function (td, tdData) {
                                var state = "";
                                if (tdData == "未启用"){
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
                                    that.updateNewEmployee(rowData);
                                });
                                var deleteButton = $("<button class='btn btn-danger btn-sm'>删除</button>");
                                deleteButton.click(function () {
                                    that.deleteNewEmployee(tdData);
                                });
                                var operator = [updateButton,deleteButton];
                                $(td).html(operator);
                            }
                        }
                    ],
                    initComplete: function () {
                        newEmployeeTableAPI = this.api();
                        that.getSearchColumns();
                    }
                });
            },
            searchInfo : function() {
                toastr.clear();
                toastr.success("查询中...");
                var that = this;
                try{
                    $.post("newEmployee/list_all",function (result) {
                        if (result.success){
                            that.shopDetails = result.data.shopDetails;
                            var api = newEmployeeTableAPI;
                            api.search('');
                            var column1 = api.column(0);
                            column1.search('', true, false);
                            var column2 = api.column(1);
                            column2.search('', true, false);
                            var column3 = api.column(3);
                            column3.search('', true, false);
                            var column4 = api.column(6);
                            column4.search('', true, false);
                            that.newEmployeeTable.clear();
                            that.newEmployeeTable.rows.add(result.data.newEmployees).draw();
                            that.getSearchColumns();
                            toastr.clear();
                            toastr.success("查询成功");
                        } else{
                            toastr.clear();
                            toastr.error("网络异常，请刷新重试");
                        }
                    });
                }catch(e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            save : function () {
                toastr.clear();
                var that = this;
                try{
                    var reg = /^((13[\d])|(15[0-35-9])|(18[\d])|(145)|(147)|(17[0135678]))\d{8}$/;
                    if (!reg.test(that.newEmployee.telephone)){
                        toastr.error("手机号码格式错误");
                        return;
                    }
                    if (that.newEmployee.shopDetailId == "0"){
                        toastr.error("请选择所属店铺");
                        return;
                    }
                    if (that.newEmployee.id != null){
                        that.newEmployee.createTime = new Date(that.newEmployee.createTime);
                    }
                    $.post("newEmployee/"+(that.newEmployee.id != null ? "modify" : "create")+"",that.newEmployee,function (result) {
                        if (result.success){
                            that.showform = false;
                            that.searchInfo();
                        } else{
                            if (result.message != null){
                                toastr.error(result.message);
                            }else {
                                toastr.error("网络异常，请刷新重试");
                            }
                        }
                    });
                }catch(e){
                    toastr.error("系统异常，请刷新重试");
                }
            },
            deleteNewEmployee : function (newEmployeeId) {
                toastr.clear();
                var that = this;
                try{
                    that.showDialog(function () {
                        $.post("newEmployee/delete",{id : newEmployeeId},function (result) {
                            if (result.success){
                                that.searchInfo();
                            } else{
                                toastr.error("网络异常，请刷新重试");
                            }
                        });
                    });
                }catch(e){
                    toastr.error("系统异常，请刷新重试");
                }
            },
            openShowForm : function () {
                this.getOpenShowForm();
                this.showform = true;
            },
            colseShowForm : function () {
                this.showform = false;
            },
            getOpenShowForm : function () {
                this.newEmployee = {sex : 1, roleType : 1, state : 1, shopDetailId : "0"};
            },
            updateNewEmployee : function (newEmployee) {
                this.showform = true;
                this.newEmployee = newEmployee;
            },
            getSearchColumns : function () {
                var api = newEmployeeTableAPI;
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
            showDialog : function (successcbk) {
                var cDialog = new dialog({
                    title:"提示",
                    content:"确定要删除吗？",
                    width:350,
                    ok:function(){
                        if(typeof successcbk=="function"){
                            successcbk();
                        }
                    },
                    cancel:function(){
                        if(typeof cancelcbk=="function"){
                            cancelcbk();
                        }
                    }
                });
                cDialog.showModal();
            }
        }
    });
</script>
