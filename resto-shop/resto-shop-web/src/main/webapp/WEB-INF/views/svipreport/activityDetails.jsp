<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<a class="btn btn-info ajaxify" href="svipactivity/report">
        <span class="glyphicon glyphicon-circle-arrow-left"></span>返回
    </a>
    <h2 class="text-center"><strong>付费会员报表</strong></h2><br/>
    <div class="row" id="searchTools">
        <div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 50px;">
                    <label for="beginDate">开始时间：</label>
                    <input type="text" class="form-control form_datetime" id="beginDate" v-model="searchDate.beginDate" readonly="readonly">
                </div>
                <div class="form-group" style="margin-right: 50px;">
                    <label for="endDate">结束时间：</label>
                    <input type="text" class="form-control form_datetime" id="endDate" v-model="searchDate.endDate" readonly="readonly">
                </div>
                <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>&nbsp;
                <button type="button" class="btn btn-primary" @click="reportExcel">下载报表</button><br/>
            </form>

        </div>
    </div>
    <br/>
    <br/>

	<div class="table-div">
		<div class="clearfix"></div>
		<div class="table-filter"></div>
		<div class="table-body">
			<table id="svipTableInfo" class="table table-striped table-hover table-bordered"></table>
		</div>
	</div>
</div>

<script src="assets/customer/date.js" type="text/javascript"></script>
<script>
	var svipId = "${param.activityId}";
	var beginDate = "${param.beginDate}";
	var endDate = "${param.endDate}";
	console.log(svipId);
    //时间插件
    $('.form_datetime').datetimepicker({
        endDate:new Date(),
        minView:"month",
        maxView:"month",
        autoclose:true,//选择后自动关闭时间选择器
        todayBtn:true,//在底部显示 当天日期
        todayHighlight:true,//高亮当前日期
        format:"yyyy-mm-dd",
        startView:"month",
        language:"zh-CN"
    });

    var vueObj = new Vue({
        el: "#control",
        data: {
        	svipTable:{},
            searchDate: {
                beginDate: "",
                endDate: "",
            }
        },
        created: function () {
//      	var date = new Date().format("yyyy-MM-dd");
            this.searchDate.beginDate = beginDate;
            this.searchDate.endDate = endDate;
            this.createSvipTable();
        },
        methods:{
        	createSvipTable : function () {
                var that = this;
                this.svipTable=$("#svipTableInfo").DataTable({
                    lengthMenu: [ [100, 50, 10], [100, 50, 10] ],
                    order: [[ 0, "asc" ]],
                    columns : [
                        {
                            title : "支付店铺",
                            data : "shopName",
                        },
						{
							title : "会员昵称",
							data : "nickName",
						},
						{
							title : "手机号码",
							data : "tel",
							createdCell:function(td,tdData,rowData,row){
								if(tdData == null){
									$(td).html("未注册");
								}                                
							}
						},
						{
							title : "支付金额",
							data : "money",
						},
						{
							title : "支付时间",
							data : "dataTime",
							createdCell:function(td,tdData,rowData,row){
								var date = new Date(tdData).format("yyyy-MM-dd hh:mm:ss");   
								$(td).html(date);
							}
						}
					],
                }); 
                this.getSvipById();
            },  
            getSvipById:function(){
            	var that = this;
            	$.ajax({
	                url : "svip/selectListByActId",
	                data : {
	                	beginDate: this.searchDate.beginDate,
	                	endDate: this.searchDate.endDate,
	                	activityId:svipId
	                },
	                success : function(result) {
	                	//console.log(JSON.stringify(result));
	                	that.svipTable.clear();
	                	that.svipTable.rows.add(result).draw();
                        toastr.success("查询成功!");
	                }
	            })
            },
            searchInfo:function(){
           		var that = this;
                var timeCha = new Date(that.searchDate.endDate).getTime() - new Date(that.searchDate.beginDate).getTime();
                if(timeCha < 0) {
                    toastr.clear();
                    toastr.error("开始时间应该少于结束时间！");
                    return false;
                }
                // }else if(timeCha > 2678400000){
                //     toastr.clear();
                //     toastr.error("暂时未开放大于一月以内的查询！");
                //     return false;
                // }
                toastr.clear();
                try {
                    that.getSvipById();
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
           	},
            reportExcel:function(){
            	window.open("svip/exprotActSvipExcel?beginDate="+this.searchDate.beginDate+"&endDate="+this.searchDate.endDate+"&activityId="+svipId);
            }
        }
    })


	
</script>
