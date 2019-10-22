<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<div id="control">
<h2 class="text-center"><strong>操作日志</strong></h2><br/>
<div class="row" id="searchTools">
	<div class="col-md-12">
		<form class="form-inline">
              <div class="form-group" style="margin-right: 50px;">
                <label for="beginDate">开始时间：</label>
                <input type="text" class="form-control form_datetime" id="beginDate" v-model="searchDate.beginDate"   readonly="readonly">
              </div>
              <button type="button" class="btn btn-primary" @click="searchInfo">查询</button>
			  <div style="display:inline-block;">
				<label>
				  <span>筛选：</span>
					<select id="logType" class="form-control" style="width: 173px;"
							:value="logType" v-model="logType">
						<option v-for="type in logTypeList" value="{{type.typeValue}}">{{type.typeName}}</option>
					</select>
				</label>
			  </div>
		</form>

	</div>
</div>
<br/>
<br/>

    <div role="tabpanel" class="tab-pane">
    	<div class="panel panel-primary" style="border-color:write;">
    		<div class="panel panel-info">
		  		<div class="panel-body">
		  			<table id="shopChargeLogTable" class="table table-striped table-bordered table-hover" width="100%" style="text-align: center;">
		  			</table>
		  		</div>
			</div>
		</div>
	</div>
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-2 col-md-8" >
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki">详情</span>
					</div>
				</div>
				<div class="portlet-body">
					<table id="detailTable" class="table table-striped table-bordered table-hover" width="100%" style="text-align: center;">
					</table>
				</div>
				<div class="text-center">
					<a class="btn default" @click="cancelPrinter" >取消</a>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="assets/customer/date.js" type="text/javascript"></script>

<script>

    //时间插件
    $('.form_datetime').datetimepicker({
            endDate:new Date(),
            minView:"month",
            maxView:"month",
            autoclose:true,//选择后自动关闭时间选择器
            todayBtn:true,//在底部显示 当天日期
            todayHighlight:true,//高亮当前日期
            format:"yyyy-mm-dd hh:mm:ss",
            startView:"month",
            language:"zh-CN"
        });


    //创建vue对象
    var vueObj =  new Vue({
        el:"#control",
        data:{
            shopChargeLogs:[],
            shopChargeLogTable:{},
            detailTable:{},
            searchDate : {
                beginDate : "",
            },
            logTypeList:[
                {typeName:"结店",typeValue:1},
                {typeName:"jde上传",typeValue:2},
                {typeName:"短信",typeValue:3},
                {typeName:"打印报表",typeValue:4}
            ],
            logType:1,
            showform: false
        },
        created : function() {
            var date = new Date().format("yyyy-MM-dd hh:mm:ss");
            this.searchDate.beginDate = date;
            this.initDataTables();
            this.searchInfo();
        },
        methods:{
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                that.shopChargeLogTable = $("#shopChargeLogTable").DataTable({
                    lengthMenu: [ [50, 75, 100, 150], [50, 75, 100, "All"] ],
                    order: [[ 0, "asc" ]],
                    columns : [
                        {
                            title:"门店名称",
                            data:"shopName"
                        },
                        {
                            title :"操作人",
                            data : "operationPeople"
                        },
                        {
                            title : "操作时间",
                            data : "operationTime",
							render: function ( data, type, row, meta ) {
								return new Date(data).format("yyyy-MM-dd hh:mm:ss")
                            }
                        },
                        {
                            title : "日结时间",
                            data : "dailyTime",
                            render: function ( data, type, row, meta ) {
                                if(data) {
                                    return new Date(data).format("yyyy-MM-dd hh:mm:ss")

                                } else {
                                    return '--'
                                }
                            }
                        },
                        {
                            title : "现金审核状态",
                            data : "cashAuditStatus",
                            render: function ( data, type, row, meta ) {
                                var str = ''
                                if(data == 1) {
                                    str = '成功'
								} else if(data == null) {
                                    str = '--'
								} else {
                                    str = '失败'
								}
                                return str;
                            }
                        },
                        {
                            title : "上传JDE状态",
                            data : "uploadJdeStatus",
                            render: function ( data, type, row, meta ) {
                                var str = ''
                                if(data == 1) {
                                    str = '成功'
                                }else if(data == null) {
                                    str = '--'
                                } else {
                                    str = '失败'
                                }
                                return str;
                            }
                        },
                        {
                            title : "发送短信时间",
                            data : "messageTime",
                            render: function ( data, type, row, meta ) {
                                if(data) {
                                    return new Date(data).format("yyyy-MM-dd hh:mm:ss")

                                } else {
                                    return '--'
								}
                            }
                        },
                        {
                            title : "打印报表时间",
                            data : "printReportTime",
                            render: function ( data, type, row, meta ) {
                                if(data) {
                                    return new Date(data).format("yyyy-MM-dd hh:mm:ss")

                                } else {
                                    return '--'
                                }
                            }
                        },
                        {
                            title : "操作",
                            data : "shopId",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                console.log('data____',td, tdData, rowData)
                                var button = $("<button class='btn green'>查看详情</button>");
                                button.click(function(){
									that.lookDetail(rowData)
                                });
                                if( that.logType == 1) {
                                    $(td).html(button);
                                } else {
                                    $(td).html('');
								}
                                /*console.log('data____',td, tdData, rowData)
                                var shopName = rowData.shopName;
                                var button = $("<sapn @click='aaa' class='btn green ajaxify '>查看详情</sapn>");
                                $(td).html(button);*/
                            }
                        },
                    ]
                });
                that.detailTable = $("#detailTable").DataTable({
                    lengthMenu: [ [70, 75, 100, 150], [70, 75, 100, "All"] ],
                    //order: [[ 0, "asc" ]],
                    //bFilter: false,
                    //bLengthChange: false,
                    columns : [
                        {
                            title : "序号",
                            data : "serialNumber"
                        },
                        {
                            title : "项目",
                            data : "payModeName"
                        },
                        {
                            title :"上报金额",
                            data : "reportMoney"
                        },
                        {
                            title : "复核金额",
                            data : "auditMoney",
                        },
                        {
                            title : "更新人",
                            data : "operator",
                        },
                        {
                            title : "更新时间",
                            data : "closeShopTime",
                            render: function ( data, type, row, meta ) {
                                if(data) {
                                    return new Date(data).format("yyyy-MM-dd hh:mm:ss")
                                } else {
                                    return '--'
                                }
                            }
                        },
                    ],
                    // "fnRowCallback" : function(nRow, aData, iDisplayIndex){
                    //     $("td:first", nRow).html(iDisplayIndex +1); //设置序号位于第一列，并顺次加一
                    //     return nRow;
                    // }
                });
            },
            lookDetail:function(rowData){
                var that = this;
                var data = {
                    logId: rowData.id,
                    time: this.searchDate.beginDate
                }

                try {
                    $.post("operationLog/checkDetails", data, function (result) {
                        console.log('result',result)
                        if (result) {
                            //that.shopChargeLogs = result;
                            that.detailTable.clear();
                            that.detailTable.rows.add(result).draw();
                            that.showform = true
                            //toastr.clear();
                            //toastr.success("查询成功");
                        }else{
                            //toastr.clear();
                            //toastr.error("查询出错");
                        }
                    });
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
			},
            cancelPrinter:function() {
                this.showform = false;
			},
            searchInfo : function() {
                var that = this;
                toastr.clear();
                toastr.success("查询中...");
                try {
                    $.post("operationLog/list_all", this.getDate(), function (result) {
                        if (result) {
                            //that.shopChargeLogs = result;
                            that.shopChargeLogTable.clear();
                            that.shopChargeLogTable.rows.add(result).draw();
                            toastr.clear();
                            toastr.success("查询成功");
                        }else{
                            toastr.clear();
                            toastr.error("查询出错");
                        }
                    });
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            getDate : function(){
                var data = {
                    time : this.searchDate.beginDate,
                    logType : this.logType
                };
                return data;
            },
            openModal : function (type) {
                $("#queryCriteriaModal").modal();
                this.type = type;
            },
        }
    })


</script>
