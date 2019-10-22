<%@ page language="java" pageEncoding="utf-8"%>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">分红设置</span>
                    </div>
                </div>

                <div class="portlet-body">
                    <form role="form" class="form-horizontal" @submit.prevent="save">
                        <input type="hidden" name="id" v-model="bonusSetting.id"/>
                        <div class="form-body">
                            <div class="form-group">
                                <label  class="col-sm-2 control-label">分红比例：</label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <input class="form-control" type="number" name="chargeBonusRatio" @keyup="setChargeBonusRatio" v-model="bonusSetting.chargeBonusRatio" min="0"  max="100" required placeholder="请输入1-100整数值">
                                        <div class="input-group-addon">%</div>
                                    </div>
                                    <span class="help-block">请输入0-100整数值</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label  class="col-sm-2 control-label">店长分红：</label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <input class="form-control" type="number" name="shopownerBonusRatio" @keyup="setEmployeeBonusRatio" @change="setEmployeeBonusRatio" v-model="bonusSetting.shopownerBonusRatio" min="0"  max="100" required placeholder="请输入1-100整数值">
                                        <div class="input-group-addon">%</div>
                                    </div>
                                    <span class="help-block">请输入0-100整数值</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label  class="col-sm-2 control-label">员工分红：</label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <input class="form-control" type="number" name="employeeBonusRatio" @keyup="setShopownerBonusRatio" @change="setShopownerBonusRatio" v-model="bonusSetting.employeeBonusRatio" min="0"  max="100" required placeholder="请输入1-100整数值">
                                        <div class="input-group-addon">%</div>
                                    </div>
                                    <span class="help-block">请输入0-100整数值</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label  class="col-sm-2 control-label">红包祝福语：</label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <input v-if="bonusSetting.id == null" class="form-control" type="text" value="充值返利，再接再厉！" name="wishing" v-model="bonusSetting.wishing" required placeholder="请输入红包祝福语">
                                        <input v-else class="form-control" type="text" name="wishing" v-model="bonusSetting.wishing" required placeholder="请输入红包祝福语">
                                        <div class="input-group-addon"></div>
                                    </div>
                                    <span class="help-block">请输入红包祝福语默认为"充值返利，再接再厉！"</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">是否启用：</label>
                                <div  class="col-md-8">
                                    <label class="radio-inline">
                                        <input type="radio" name="state" value="1" v-model="bonusSetting.state"> 启用
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="state" value="0" v-model="bonusSetting.state"> 不启用
                                    </label>
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
			<%--<button class="btn green pull-right" @click="openShowForm">新建</button>--%>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter"></div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered" id = "bonusSettingTable"></table>
		</div>
	</div>
</div>


<script>
    var bonusTableAPI;
    var vueObj = new Vue({
        el : "#control",
        data : {
            showform : false,
            bonusSettingTable : {},
            bonusSetting : {}
        },
        created : function() {
            this.initDataTables();
            this.searchInfo();
        },
        methods : {
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                that.bonusSettingTable = $("#bonusSettingTable").DataTable({
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
                            title : "充值活动",
                            data : "chargeName",
                            orderable : false,
                        },
                        {
                            title : "充值分红比例",
                            data : "chargeBonusRatio",
                            createdCell: function (td, tdData) {
                                $(td).html(tdData+"%");
                            }
                        },
                        {
                            title : "店长分红比例",
                            data : "shopownerBonusRatio",
                            createdCell: function (td, tdData) {
                                $(td).html(tdData+"%");
                            }
                        },
                        {
                            title : "员工分红比例",
                            data : "employeeBonusRatio",
                            createdCell: function (td, tdData) {
                                $(td).html(tdData+"%");
                            }
                        },
                        {
                            title : "启用分红",
                            data : "state",
                            orderable : false,
                            createdCell: function (td, tdData) {
                                var state = "";
                                if (tdData == 0){
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
                                var updateButton = $("<button class='btn btn-info btn-sm'>设置</button>");
                                updateButton.click(function () {
                                    that.updateBonusSetting(rowData);
                                });
                                var operator = [updateButton];
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
                    $.post("bonusSetting/list_all",function (result) {
                        if (result.success){
                            var api = bonusTableAPI;
                            api.search('');
                            var column1 = api.column(0);
                            column1.search('', true, false);
                            that.bonusSettingTable.clear();
                            that.bonusSettingTable.rows.add(result.data).draw();
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
                var that = this;
                try{
                    that.bonusSetting.createTime = new Date(that.bonusSetting.createTime);
                    that.bonusSetting.updateTime = new Date(that.bonusSetting.updateTime);
                    $.post("bonusSetting/"+(that.bonusSetting.id != null ? "modify" : "create")+"",that.bonusSetting,function (result) {
                        if (result.success){
                            that.showform = false;
                            that.searchInfo();
                        } else{
                            toastr.error("网络异常，请刷新重试");
                        }
                    });
                }catch(e){
                    toastr.error("系统异常，请刷新重试");
                }
            },
            colseShowForm : function () {
                this.showform = false;
            },
            updateBonusSetting : function (bonusSetting) {
                this.showform = true;
                this.bonusSetting = bonusSetting;
            },
            setShopownerBonusRatio : function () {
                if (this.bonusSetting.employeeBonusRatio == ""){
                    this.bonusSetting.employeeBonusRatio = 0;
                }
                var employeeBonusRatio = parseInt(this.bonusSetting.employeeBonusRatio);
                if (employeeBonusRatio < 0){
                    this.bonusSetting.employeeBonusRatio = 0;
                } else if(employeeBonusRatio > 100){
                    this.bonusSetting.employeeBonusRatio = 100;
                }
                this.bonusSetting.shopownerBonusRatio = 100 - parseInt(this.bonusSetting.employeeBonusRatio);
            },
            setEmployeeBonusRatio : function () {
                if (this.bonusSetting.shopownerBonusRatio == ""){
                    this.bonusSetting.shopownerBonusRatio = 0;
                }
                var shopownerBonusRatio = parseInt(this.bonusSetting.shopownerBonusRatio);
                if (shopownerBonusRatio < 0){
                    this.bonusSetting.shopownerBonusRatio = 0;
                } else if(shopownerBonusRatio > 100){
                    this.bonusSetting.shopownerBonusRatio = 100;
                }
                this.bonusSetting.employeeBonusRatio = 100 - parseInt(this.bonusSetting.shopownerBonusRatio);
            },
            setChargeBonusRatio : function () {
                if (this.bonusSetting.chargeBonusRatio == ""){
                    this.bonusSetting.chargeBonusRatio = 0;
                }
                var chargeBonusRatio = parseInt(this.bonusSetting.chargeBonusRatio);
                if (chargeBonusRatio < 0){
                    this.bonusSetting.chargeBonusRatio = 0;
                } else if(chargeBonusRatio > 100){
                    this.bonusSetting.chargeBonusRatio = 100;
                }
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
