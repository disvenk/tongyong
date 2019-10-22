<%@ page language="java" pageEncoding="utf-8"%>
<style>
    .textRight {
        width: 20%;
        display: inline-block;
        text-align: right;
    }
    .textInCenter {
        width: 20%;
        display: inline-block;
        text-align: center;
    }
    .checkbox-inline {
        padding: inherit;
        position: relative;
        top: -7px;
    }
</style>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki"> <font color="black">分红详情</font></span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" class="form-horizontal">
                        <div class="form-body" style="font-size: 20px;font-family: 微软雅黑;border-bottom: 1px solid #eef1f5;">
                            <div style="border-bottom: 1px solid #eef1f5;padding-left: 10%;">
                                <p>
                                    <span class="textRight">充值方式：</span>
                                    <span>{{bonusLog.chargeType}}</span>
                                </p>
                                <p>
                                    <span class="textRight">充值店铺：</span>
                                    <span>{{bonusLog.shopName}}</span>
                                </p>
                                <p>
                                    <span class="textRight">手机号：</span>
                                    <span>{{bonusLog.telephone}}</span>
                                </p>
                                <p>
                                    <span class="textRight">充值金额：</span>
                                    <span><font color="#228b22">￥{{bonusLog.chargeMoney}}</font></span>
                                </p>
                                <p>
                                    <span class="textRight">充值时间：</span>
                                    <span>{{bonusLog.chargeTime}}</span>
                                </p>
                                <p>
                                    <span class="textRight">分红比例：</span>
                                    <span>{{bonusLog.chargeBonusRatio}}</span>
                                </p>
                                <p>
                                    <span class="textRight">分红金额：</span>
                                    <span><font color="red">￥{{bonusLog.bonusMoney}}</font></span>
                                </p>
                            </div>
                            <div style="margin-top: 3%;padding-left: 10%;">
                                <p>
                                    <span class="textRight">状态：</span>
                                    <span>
                                        <i v-if="bonusLog.state == 0" style="color: red;font-style: normal;">{{bonusLog.stateValue}}</i>
                                        <i v-if="bonusLog.state == 1" style="color: #0a6aa1;font-style: normal;">{{bonusLog.stateValue}}</i>
                                        <i v-if="bonusLog.state == 2" style="color: #228b22;font-style: normal;">{{bonusLog.stateValue}}</i>
                                        <i v-if="bonusLog.state == 3" style="color: #c69f07; font-style: normal;">{{bonusLog.stateValue}}</i>
                                    </span>
                                </p>
                                <p v-if="(bonusLog.state == 1 || bonusLog.state == 2 || bonusLog.state == 3) && (bonusLog.employeeBonusAmount > 0 || bonusLog.bonusMoney == 0)">
                                    <span class="textRight">{{bonusLog.employeeName}}：</span>
                                    <span v-if="bonusLog.employeeIssuingState == 1"><i style="color: #c69f07;font-style: normal;">发放异常</i></span>
                                    <span v-else><i style="color: #228b22;font-style: normal;">￥{{bonusLog.employeeBonusAmount}}</i></span>
                                </p>
                                <p v-if="(bonusLog.state == 1 || bonusLog.state == 2 || bonusLog.state == 3) && (bonusLog.shopownerBonusAmount > 0 || bonusLog.bonusMoney == 0)">
                                    <span class="textRight">{{bonusLog.shopownerName}}：</span>
                                    <span v-if="bonusLog.shopownerIssuingState == 1"><i style="color: #c69f07;font-style: normal;">发放异常</i></span>
                                    <span v-else><i style="color: #228b22;font-style: normal;">￥{{bonusLog.shopownerBonusAmount}}</i></span>
                                </p>
                            </div>
                        </div>
                        <div class="form-group text-center">
                            <button v-if="bonusLog.state == 0" type="button" class="btn btn-primary" @click="openShowEmployee">分红</button>
                            <button v-if="bonusLog.state == 1 && !disabled" type="button" class="btn btn-primary" @click="save">发放奖励</button>
                            <button v-if="bonusLog.state == 1 && disabled" type="button" class="btn btn-default" :disabled="disabled">发放奖励</button>
                            <button v-if="bonusLog.state == 3 && !disabled" type="button" class="btn btn-primary" @click="save">补发奖励</button>
                            <button v-if="bonusLog.state == 3 && disabled" type="button" class="btn btn-default" :disabled="disabled">补发奖励</button>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <button type="button" class="btn btn-default" @click="colseShowForm">关闭</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="row form-div" v-show="showEmployee">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki"> <font color="black">选择分红对象</font></span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" class="form-horizontal">
                        <div class="form-body" style="font-size: 24px;font-family: 微软雅黑;border-bottom: 1px solid #eef1f5;">
                            <div class="shopOwner" v-if="shopowners.length > 0 && bonusLog.shopownerBonusRatio !='0%'">
                                <p style="margin-left: 5%">选择店长</p>
                                <div style="margin-left: 10%">
                                    <p v-for="shopowner in shopowners">
                                        <span class="textInCenter">{{shopowner.name}}</span>
                                        <span class="textInCenter">{{bonusLog.shopownerBonusRatio}}</span>
                                        <span class="textInCenter"><font color="red">￥{{bonusLog.shopownerBonusAmount}}</font></span>
                                        <label class="checkbox-inline">
                                            <input type="radio" name="shopownerId" :value="shopowner.id" v-model="bonusLog.shopownerId">
                                        </label>
                                    </p>
                                </div>
                            </div>
                            <div v-if="shopowners.length == 0 && bonusLog.shopownerBonusRatio !='0%'">
                                <p style="margin-left: 5%">选择店长</p>
                                <div style="margin-left: 10%">
                                    暂无店长，无法发放奖励
                                </div>
                            </div>
                            <div class="staffOwner" v-if="employees.length > 0 && bonusLog.employeeBonusRatio !='0%'">
                                <p style="margin-left: 5%">选择员工</p>
                                <div style="margin-left: 10%">
                                    <p v-for="employee in employees">
                                        <span class="textInCenter">{{employee.name}}</span>
                                        <span class="textInCenter">{{bonusLog.employeeBonusRatio}}</span>
                                        <span class="textInCenter"><font color="red">￥{{bonusLog.employeeBonusAmount}}</font></span>
                                        <label class="checkbox-inline">
                                            <input type="radio" name="employeeId" :value="employee.id" v-model="bonusLog.employeeId">
                                        </label>
                                    </p>
                                </div>
                            </div>
                            <div v-if="employees.length == 0 && bonusLog.employeeBonusRatio !='0%'">
                                <p style="margin-left: 5%">选择员工</p>
                                <div style="margin-left: 10%">
                                    暂无员工，无法发放奖励
                                </div>
                            </div>
                        </div>
                        <div class="form-group text-center">
                            <button type="button" class="btn btn-default" @click="previousStep">上一步</button>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <button type="button" class="btn btn-primary" v-if="!disabled" @click="save">发放奖励</button>
                            <button type="button" class="btn btn-default" :disabled="disabled" v-else>发放奖励</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
	<div class="table-div">
		<div class="table-operator">
		</div>
		<div class="clearfix"></div>
		<div class="table-filter"></div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered" id = "bonusLogTable"></table>
		</div>
	</div>
</div>


<script>
    var bonusTableAPI;
    var vueObj = new Vue({
        el : "#control",
        data : {
            showform : false,
            showEmployee : false,
            bonusLogTable : {},
            bonusLog : {},
            shopowners :[],
            employees : [],
            disabled : false
        },
        created : function() {
            this.initDataTables();
            this.searchInfo();
        },
        methods : {
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                that.bonusLogTable = $("#bonusLogTable").DataTable({
                    lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
                    order: [[ 0, 'asc' ]],
                    columns : [
                        {
                            title : "充值时间",
                            data : "chargeTime"
                        },
                        {
                            title : "店铺",
                            data : "shopName",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "充值方式",
                            data : "chargeType",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "手机号",
                            data : "telephone",
                            orderable : false
                        },
                        {
                            title : "充值金额",
                            data : "chargeMoney"
                        },
                        {
                            title : "分红金额",
                            data : "bonusMoney"
                        },
                        {
                            title : "店长分红金额",
                            data : "shopownerBonusAmount"
                        },
                        {
                            title : "员工分红金额",
                            data : "employeeBonusAmount"
                        },
                        {
                            title : "已发放金额",
                            data : "amountDisbursed"
                        },
                        {
                            title : "店长已发放金额",
                            data : "shopownerAmountDisbursed"
                        },
                        {
                            title : "员工已发放金额",
                            data : "employeeAmountDisbursed"
                        },
                        {
                            title : "状态",
                            data : "stateValue",
                            orderable : false,
                            s_filter: true,
                            createdCell: function (td, tdData) {
                                var state;
                                if (tdData == "已分红"){
                                    state = "<span class='label label-success'>"+tdData+"</span>";
                                }else if (tdData == "未分红"){
                                    state = "<span class='label label-danger'>"+tdData+"</span>";
                                }else if (tdData == "审核中"){
                                    state = "<span class='label label-primary'>"+tdData+"</span>";
                                }else{
                                    state = "<span class='label label-warning'>"+tdData+"</span>";
                                }
                                $(td).html(state);
                            }
                        },
                        {
                            title : "操作",
                            data : "id",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var state = rowData.stateValue;
                                var operatorButton = (state == "已分红" ? $("<button class='btn btn-primary btn-sm'>查看</button>") : $("<button class='btn btn-success btn-sm'>分红</button>"));
                                operatorButton.click(function () {
                                    that.operatorBonusLog(rowData);
                                });
                                var operator = [operatorButton];
                                $(td).html(operator);
                            }
                        }
                    ],
                    initComplete: function () {
                        bonusTableAPI = this.api();
                        that.bonusTable();
                    }
                });
            },
            searchInfo : function() {
                toastr.clear();
                toastr.success("查询中...");
                var that = this;
                try{
                    $.post("bonusLog/list_all",function (result) {
                        if (result.success){
                            var api = bonusTableAPI;
                            api.search('');
                            var column1 = api.column(1);
                            column1.search('', true, false);
                            var column2 = api.column(2);
                            column2.search('', true, false);
                            var column6 = api.column(6);
                            column6.search('', true, false);
                            that.bonusLogTable.clear();
                            that.bonusLogTable.rows.add(result.data).draw();
                            that.bonusTable();
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
                toastr.success("发放中，请稍后...");
                var that = this;
                that.disabled = true;
                try{
                    $.post("bonusLog/modify",{id : that.bonusLog.id, shopownerId : that.bonusLog.shopownerId, employeeId : that.bonusLog.employeeId},function (result) {
                        if (result.success){
                            if (that.bonusLog.state == 1 || that.bonusLog.state == 3){
                                that.bonusLog.state = 2;
                                that.bonusLog.employeeIssuingState = 0;
                                that.bonusLog.shopownerIssuingState = 0;
                                that.bonusLog.stateValue = "已分红";
                                toastr.clear();
                                toastr.success("发放成功");
                            }else if (that.bonusLog.state == 0){
                                that.colseShowEmployee(true);
                            }
                            that.bonusLog.state = 2;
                            if (result.message){
                                toastr.error(result.message);
                            }
                        } else{
                            that.disabled = false;
                            that.bonusLog.state = 3;
                            if (result.data.shopownerIssuingState == 1){
                                that.bonusLog.shopownerIssuingState = 1;
                            }
                            if (result.data.employeeIssuingState == 1){
                                that.bonusLog.employeeIssuingState = 1;
                            }
                            that.bonusLog.stateValue = "发放异常";
                            toastr.clear();
                            if (result.message != null && result.message != ""){
                                toastr.error(result.message);
                            }else {
                                toastr.error("网络异常，请刷新重试");
                            }
                        }
                    });
                }catch(e){
                    that.disabled = false;
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            openShowForm : function () {
                this.showform = true;
            },
            colseShowForm : function () {
                this.showform = false;
                this.disabled = false;
                this.searchInfo();
            },
            operatorBonusLog : function (bonusLog) {
                this.bonusLog = bonusLog;
                this.openShowForm();
            },
            openShowEmployee : function () {
                var that = this;
                try{
                    $.post("bonusLog/getShopEmployees",{shopId : that.bonusLog.shopId},function (result) {
                        if (result.success){
                            that.employees = result.data.employees;
                            that.shopowners = result.data.shopowners;
                            if (that.employees.length > 0){
                                that.bonusLog.employeeId = that.employees[0].id;
                            }else if (that.bonusLog.employeeBonusRatio != "0%"){
                                that.disabled = true;
                            }
                            if (that.shopowners.length > 0){
                                that.bonusLog.shopownerId = that.shopowners[0].id;
                            }else if (that.bonusLog.shopownerBonusRatio != "0%"){
                                that.disabled = true;
                            }
                            that.showform = false;
                            that.showEmployee = true;
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
            colseShowEmployee : function (flg) {
                this.showEmployee = false;
                this.disabled = false;
                if (flg) {
                    this.searchInfo();
                }
            },
            previousStep : function () {
                this.colseShowEmployee(false);
                this.openShowForm();
            },
            bonusTable : function () {
                var api = bonusTableAPI;
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
