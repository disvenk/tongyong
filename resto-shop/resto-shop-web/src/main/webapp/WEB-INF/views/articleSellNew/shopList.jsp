<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="control">
	<h2 class="text-center"><strong>店铺菜品销售报表</strong></h2><br/>
	<div class="row" id="searchTools">
		<div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 50px;">
                <label>开始时间：
                <input type="text" id="beginDate" class="form-control form_datetime" :value="searchDate.beginDate" v-model="searchDate.beginDate" readonly="readonly">
                </label>
                </div>
                <div class="form-group" style="margin-right: 50px;">
                <label>结束时间：
                <input type="text" id="endDate" class="form-control form_datetime" :value="searchDate.endDate" v-model="searchDate.endDate" readonly="readonly">
                </label>
                <button type="button" class="btn btn-primary" @click="today"> 今日</button>
                <button type="button" class="btn btn-primary" @click="yesterDay">昨日</button>
                <button type="button" class="btn btn-primary" @click="week">本周</button>
                <button type="button" class="btn btn-primary" @click="month">本月</button>
                <button type="button" class="btn btn-primary" @click="searchInfo()">查询报表</button>
                &nbsp;
                <button type="button" style="float: right;" class="btn btn-primary" @click="shopReportExcel">下载报表</button>
                <br/>
            </div>
            </form>
		</div>
	</div>
	<br/>
	<br/>
	<div>
		<div class="tab-content">
			    <!-- 店铺菜品销售表 -->
		    	<div class="panel panel-primary" style="border-color:white;">
				  	<!-- 店铺菜品销售表 -->
			    	<div class="panel panel-info">
					  <div class="panel-heading text-center">
					  	<strong style="margin-right:100px;font-size:22px">店铺菜品销售记录</strong>
					  </div>
					  <div class="panel-body">
					  	<table id="shopArticleTable" class="table table-striped table-bordered table-hover" width="100%">
					  	</table>
					  </div>
					</div>
				 </div>
				</div>
		</div>
</div>
<script src="assets/customer/date.js" type="text/javascript"></script>
<script src="assets/customer/components/dateSearch.js" type="text/javascript"></script>

<script>
    //时间插件
    $('.form_datetime').datetimepicker({
        endDate:new Date(),
        // minView:"month",
        minView: 0,
        maxView:"month",
        autoclose:true,//选择后自动关闭时间选择器
        todayBtn:true,//在底部显示 当天日期
        todayHighlight:true,//高亮当前日期
        format:"yyyy-mm-dd hh:ii:ss",
        startView:"month",
        language:"zh-CN"
    });
    var sort = "desc";
	var vueObj = new Vue({
		el : "#control",
		mixins: [dateSearch],
	    data : {
	        searchDate : {
	            beginDate : "",
	            endDate : "",
	        },
	        shopArticleTable : {},
            shopArticleReportDtos : []
	    },
	    created : function() {
	        var date0 = new Date().format("yyyy-MM-dd 00:00:00");
	        var date = new Date().format("yyyy-MM-dd hh:mm:ss");
	        this.searchDate.beginDate = date0;
	        this.searchDate.endDate = date;
	        this.initDataTables();
	        this.searchInfo();
	    },
	    methods : {
	    	initDataTables:function () {
	            //that代表 vue对象
	            var that = this;
	            //datatable对象
	            that.shopArticleTable=$("#shopArticleTable").DataTable({
	                lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
	                order: [[ 0, "asc" ]],
	                columns : [
	                    {
	                        title : "店铺名称",
	                        data : "shopName"
	                    },
	                    {
	                        title : "菜品销量(份)",
	                        data : "totalNum"
	                    },
	                    {
	                        title : "菜品销售额",
	                        data : "sellIncome"
	                    },
	                    {
	                        title : "折扣总额",
	                        data : "discountTotal"
	                    },
	                    {
	                        title : "品牌销售占比",
	                        data : "occupy"
	                    },
	                    {
	                        title : "退菜总数",
	                        data : "refundCount"
	                    },
	                    {
	                        title : "退菜总额",
	                        data : "refundTotal"
	                    },
	                    {
	                        title: "销售详情",
	                        data: "shopId",
	                        orderable : false,
	                        createdCell: function (td, tdData, rowData) {
	                            var shopName = rowData.shopName;
 	                            var button = $("<a href='articleSell/showShopArticleNew?beginDate="+that.searchDate.beginDate+"&&endDate="+that.searchDate.endDate+"&&shopId="+tdData+"&&shopName="+shopName+"' class='btn green ajaxify '>查看详情</a>");
 	                            $(td).html(button);
	                        }
	                    }
	                ]
	            });
	        },
	    	searchInfo : function(isInit) {
				var that = this;
				var timeCha = new Date(that.searchDate.endDate).getTime() - new Date(that.searchDate.beginDate).getTime();
				if(timeCha < 0){
					toastr.clear();
					toastr.error("开始时间应该少于结束时间！");
					return false;
				}else if(timeCha > 2678400000){
					toastr.clear();
					toastr.error("暂时未开放大于一月以内的查询！");
					return false;
				}
                toastr.clear();
                toastr.success("查询中...");
	        	try{
		            $.post("articleSell/list_shopMenu", this.getDate(), function(result) {
		                if(result.success) {
                            that.shopArticleTable.clear().draw();
                            that.shopArticleTable.rows.add(result.data.list).draw();
                            that.shopArticleReportDtos = result.data.list;
							toastr.clear();
                            toastr.success("查询成功");
                        }else{
							toastr.clear();
                            toastr.error("查询报表出错");
                        }
		            });
	        	}catch(e){
					toastr.clear();
                    toastr.error("系统异常，请刷新重试");
	        	}
	        },
	        getDate : function(){
	            var data = {
	                beginDate : this.searchDate.beginDate,
	                endDate : this.searchDate.endDate,
	            };
	            return data;
	        },
	        // today : function(){
	        //     date = new Date().format("yyyy-MM-dd");
	        //     this.searchDate.beginDate = date
	        //     this.searchDate.endDate = date
	        //     this.searchInfo();
	        // },
	        // yesterDay : function(){
	        //     this.searchDate.beginDate = GetDateStr(-1);
	        //     this.searchDate.endDate  = GetDateStr(-1);
	        //     this.searchInfo();
	        // },
	        // week : function(){
	        //     this.searchDate.beginDate  = getWeekStartDate();
	        //     this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
	        //     this.searchInfo();
	        // },
	        // month : function(){
	        //     this.searchDate.beginDate  = getMonthStartDate();
	        //     this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
	        //     this.searchInfo();
	        // },
	        shopReportExcel : function(){
	            var that = this;
                try {
                    var object = {
                        beginDate: that.searchDate.beginDate,
                        endDate: that.searchDate.endDate,
                        shopArticleReportDtos: that.shopArticleReportDtos
                    }
                    $.post("articleSell/create_shop_article_excel", object, function (result) {
                        if (result.success){
                            window.location.href = "articleSell/downloadShopArticleExcel?path="+result.data+"";
                        }else{
							toastr.clear();
                            toastr.error("下载报表出错");
                        }
                    });
                }catch (e){
					toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
	        }
	    }
	});

	function Trim(str)
	{ 
	    return str.replace(/(^\s*)|(\s*$)/g, ""); 
	}
</script>

