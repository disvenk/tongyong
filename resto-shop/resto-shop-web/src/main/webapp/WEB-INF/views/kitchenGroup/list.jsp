<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style>
	.printerClass {
		display: inline-block;
		padding: 0 10px;
		margin-right: 20px;
		border: 1px solid #eee;
	}
	.radio-inline .radio {
		padding: 0;
	}
</style>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki">新建厨房组配置</span>
					</div>
				</div>
				<div class="portlet-body">
					<form role="form" class="form-horizontal" action="{{m.id?'kitchenGroup/modify':'kitchenGroup/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
								<label class="col-sm-3 control-label">厨房租名称：</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" required name="name" v-model="m.name">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">排序：</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" name="sort" v-model="m.sort">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">备注信息：</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" name="remark" v-model="m.remark">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">状态：</label>
								<div class="col-sm-6">
									<label class="radio-inline">
										<input type="radio" name="status" v-model="m.status" value="0"> 启用
									</label>
									<label class="radio-inline">
										<input type="radio" name="status" v-model="m.status" value="1"> 未启用
									</label>
								</div>
							</div>
							<div style="margin-bottom: 20px;">
								<div class="portlet light ">
									<div class="portlet-title">
										<div class="caption">
											<span class="caption-subject bold font-blue-hoki">关联厨房：</span>
										</div>
									</div>
									<label class="col-sm-3 control-label">厨房设置：</label>
									<div class="col-sm-6">
										<label class="radio-inline">
											<a @click="addPrinter">添加厨房</a>
										</label>
										<label class="radio-inline">
											<span name="choosePrinterLength" >已选{{showPrinterData.length}}个厨房</span>
										</label>
									</div>
								</div>
								<div style="padding: 0 50px 0 100px;">
									<div v-for="item in showPrinterData"  class="printerClass">
										<%--<span>【{{item.kitchenName}}】</span>--%>
										<span>{{item.name}}</span>
										<span style="color:red; padding: 0 5px;" @click="deletePrinnterBtn(item)">X</span>
									</div>
								</div>
							</div>
							<div class="text-center">
								<input type="hidden" name="id" v-model="m.id" />
								<input class="btn green"  type="submit" value="保存"/>
								<a class="btn default" @click="cancel" >取消</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="row form-div" v-if="printerModel">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki">选择厨房</span>
					</div>
				</div>
				<div class="portlet-body">
					<%--<div class="form-group">--%>
						<%--<div class="col-sm-12">--%>
							<%--<input type="text" class="form-control" required name="name" v-model="" placeholder="请输入打印机名称">--%>
						<%--</div>--%>
					<%--</div>--%>
					<div class="form-group">
						<div class=" radio-list" >
							<label>
								<input type='checkbox' class='input-checkbox' :checked="choosePrinter.length === kitchenList.length" @click='checkedAll()'>
								&nbsp;&nbsp;全选
							</label>
						</div>
						<div  class=" radio-list" v-for = 'item in kitchenList'>
							<label>
								<%--<input type='checkbox' :checked="choosePrinter.indexOf(item.id)>=0"  name='checkboxinput' class='input-checkbox' @click='checkedOne(item)'>--%>
								<input type='checkbox' :checked="aa(item)"  name='checkboxinput' class='input-checkbox' @click='checkedOne(item)'>
								&nbsp;&nbsp;{{item.name}}
							</label>

						</div>
					</div>
					<div class="text-center">
						<input class="btn green"  @click="submitKitchen" value="保存"/>
						<a class="btn default" @click="cancelKitchen" >取消</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="kitchenGroup/add">
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
                url : "kitchenGroup/list_all",
                dataSrc : ""
            },
            columns : [
                {
                    title : "排序",
                    data : "sort",
                },
                {
                    title : "厨房组名称",
                    data : "name",
                },
                {
                    title : "厨房名称",
                    data : "kitchenName",
                    width: '400px',
                },

                {
                    title : "备注",
                    data : "remark",
                },
                {
                    title : "状态",
                    data : "statusName",

                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        var operator=[
                            <s:hasPermission name="kitchenGroup/delete">
                            C.createDelBtn(tdData,"kitchenGroup/delete"),
                            </s:hasPermission>
                            <s:hasPermission name="kitchenGroup/edit">
                            C.createEditBtn(rowData),
                            </s:hasPermission>
                        ];
                        $(td).html(operator);
                    }
                }],
        });
        $.ajax({
            type:"post",
            url:"kitchen/list_all",
            dataType:"json",
            success:function(data){
                if(data.length > 0){
                    vueObj.$set("kitchenList",data);
                }
            }
        });
        var C = new Controller(null,tb);

        var vueObj = new Vue({
            mixins: [C.formVueMix],
            el: "#control",
            data: {
                checkAll:false,
                showform:false,
                printerModel:false,
                choosePrinter:[],
                isCheckedAll: false,
                ciprinterList:[],
                enableDuodongxian:"",
                choosePrinterLength:0,
                choosePrinterwatch:0,
				names : [],
				showPrinterData: [] //所有选中的打印机数据
            },
            // create:function(){
            //
            //     this.m={
            //         beginTime : '10:00',
            //         endTime : '22:00',
            //         discount : 100
            //     };
            //     this.initTime();
            // },
            methods: {
                create:function(){
                    this.showform = true;
                    this.choosePrinter = []
                    this.showPrinterData = []
                    this.m={
                        beginTime : '10:00',
                        endTime : '22:00',
                        discount : 100
                    };
                    this.initTime();
                },

                initTime: function () {
                    $(".timepicker-no-seconds").timepicker({
                        autoclose: true,
                        showMeridian: false,
                        minuteStep: 5
                    });
                },
                edit:function(model){
                    var that = this;
                    this.m= model;
                    this.showform = true;
                    if( model.kitchens ) {
                        this.showPrinterData = JSON.parse(JSON.stringify(model.kitchens));

                    }else {
                        this.showPrinterData = [];
                    }

                    var temp = [];
                    model.kitchens && model.kitchens.length > 0 && model.kitchens.map(function (v,i) {
                        temp.push(v.id)
                    })
                    this.m.kitchenIds = temp;
                },

                cancel(){
                    this.showform = false;
                },
                submit(){
                    this.showform = false;
				},
                save: function (e) {
                    var re = /^[0-9]*$/;
                    if(!re.test(this.m.sort)){
                        C.errorMsg("排序请填写数字");
                        return;
                    }
                    var temp = [];
                    this.showPrinterData.length > 0 && this.showPrinterData.map(function (v,i) {
                        temp.push(v.id)
                    })
                    this.m.kitchenIds = temp;
                    var jsonData = JSON.stringify(this.m);
                    var action = $(e.target).attr("action");
                    var that = this
                    $.ajax({
                        contentType: "application/json",
                        type: "post",
                        url: action,
                        data: jsonData,
                        success: function (result) {
                            if (result.success) {
                                that.showform = false;
                                that.m = {};
                                C.simpleMsg("保存成功");
                                tb.ajax.reload(null, false);
                            } else {
                                C.errorMsg(result.message);
                            }
                        },
                        error: function (xhr, msg, e) {
                            var errorText = xhr.status + " " + xhr.statusText + ":" + action;
                            C.errorMsg(errorText);
                        }
                    });
                },
                addPrinter() {
                    this.printerModel = true;
                    this.choosePrinter = JSON.parse(JSON.stringify(this.showPrinterData));
                },
                deletePrinnterBtn (item){
                    var idIndex = null;
                    var obj = {id: item.id}
                    var key = Object.keys(obj)[0];
                    this.showPrinterData.every(function(value, i) {
                        if (value[key] === obj[key]) {
                            idIndex = i;
                            return false;
                        }
                        return true;
                    });
                    this.showPrinterData.splice(idIndex, 1)
                },
                checkedOne (item) {
                    var idIndex = null;
                    var obj = {id: item.id}
                    var key = Object.keys(obj)[0];
                    this.choosePrinter.every(function(value, i) {
                        if (value[key] === obj[key]) {
                            idIndex = i;
                            return false;
                        }
                        return true;
                    });
                    if (idIndex != null) {
                        // 如果已经包含了该id, 则去除(单选按钮由选中变为非选中状态)
                        this.choosePrinter.splice(idIndex, 1)
                    } else {

                        // 选中该checkbox
                        this.choosePrinter.push(item)
                    }

                },
                checkedAll () {
                    this.isCheckedAll = !this.isCheckedAll
                    if (this.isCheckedAll) {
                        // 全选时
                        this.choosePrinter = []
                        this.kitchenList.forEach(function (printer) {
                            this.choosePrinter.push(printer)
                        }, this)
                    } else {
                        this.choosePrinter = []
                    }
                },
                aa(item) {
                    if(JSON.stringify(this.choosePrinter).indexOf(JSON.stringify(item.id))>-1){
                        return true
                    }else {
                        return false
                    }
                },
                submitKitchen() {
                    this.showPrinterData = JSON.parse(JSON.stringify(this.choosePrinter));
                    var temp = [];
                    this.showPrinterData.length > 0 && this.showPrinterData.map(function (v,i) {
                        temp.push(v.id)
                    })
                    this.m.kitchenIds = temp;
                    this.printerModel = false;

                },
                cancelKitchen(){
                    this.printerModel = false;
                }
            }
        })
        C.vue=vueObj;
    }());

</script>
