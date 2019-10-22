<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<h2 class="text-center"><strong>菜品销售报表</strong></h2><br/>
<div class="row" id="searchTools">
	<div class="col-md-12">
		<form class="form-inline">
		  <div class="form-group" style="margin-right: 50px;">
		    <label for="beginDate">开始时间：</label>
		    <input type="text" class="form-control form_datetime" id="beginDate" readonly="readonly">
		  </div>
		  <div class="form-group" style="margin-right: 50px;">
		    <label for="endDate">结束时间：</label>
		    <input type="text" class="form-control form_datetime" id="endDate" readonly="readonly">
		  </div>
		  <button type="button" class="btn btn-primary" id="searchReport">查询报表</button>&nbsp;
		  <button type="button" class="btn btn-primary" id="shopArticleExcel">下载报表</button>&nbsp;
		</form>
	</div>
</div>

<br>
	<div>
		<button type="button" class="btn green-haze" id="sortById">按菜品序号排序</button>
		<button type="button" class="btn green-haze" id="sortByNum">按销量排序</button>
	</div>
	</br></br>
<div>
  <div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="dayReport">
    	<div id="report-editor">
			<!-- 菜品销售记录 -->
	    	<div class="panel panel-info">
			  <div class="panel-heading text-center">
			  	<strong style="margin-right:100px;font-size:22px">菜品销售记录</strong>
			  </div>
			  <div class="panel-body">
			  	<table id="articleSaleTable" class="table table-striped table-bordered table-hover" width="100%"></table>
			  </div>
			</div>
    	</div>
    	<div id="report-preview" class="row" style="display:none">
			  <div class="col-md-8"><p><strong>菜品销售记录</strong></p></div>
    	</div>
    </div>
  </div>
</div>

<script>
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

//文本框默认值
$('.form_datetime').val(new Date().format("yyyy-MM-dd"));
var isFirst = true;
var orderPaymentItemsCount = 0;//用于统计总量，预览时使用
var tb1 = $("#dayReportTable").DataTable({
	ajax : {
		url : "report/orderPaymentItems",
		dataSrc : "data",
		data:function(d){
			d.beginDate=$("#beginDate").val();
			d.endDate=$("#endDate").val();
			return d;
		}
	},
	ordering:false,
	columns : [
		{ title : "支付类型", data : "paymentModeVal" },                 
		{ title : "支付金额", data : "payValue"} ,
	],
});

var sort = "desc"
var tbApi=null;
var select;
var isFirst = true;//是否是第一次进入
var shoptable = $("#articleSaleTable").DataTable({
	ajax : {
		url : "report/orderArticleItems",
		dataSrc : "data",
		data:function(d){
			d.beginDate=$("#beginDate").val();
			d.endDate=$("#endDate").val();
			d.sort=sort;
			return d;
		}
	},
	
	ordering:false,
	columns : [
	   		{
	   			title : "菜品分类",
	   			data : "articleFamilyName",
	   			createdCell:function(td,tdData,rowData){
	   				if(isEmpty(tdData)){
	   					var lab = $("<span>");
	   					if(isEmpty(rowData.articleFamilyId)){
	   						lab.html("分类不详").addClass("label label-warning");
	   					}else{
	   						lab.html("该分类已不存在").addClass("label label-warning");
	   					}
	   					$(td).html(lab);
	   				}
	   			}
	   		},  
	   		{
	   			title : "菜品名称",
	   			data : "articleName",
	   			createdCell:function(td,tdData,rowData){
	   				if(isEmpty(rowData.articleFamilyId) && isEmpty(rowData.articleFamilyName)){
	   					$(td).html(tdData+"&nbsp;<span class='label label-danger'>已被删除</span>");
	   				}
	   			}
	   		},  
	   		{
	   			title : "菜品销量(份)",
	   			data : "shopSellNum",
	   		},
	   	],
	   	initComplete: function () {//列筛选
			tbApi = this.api();
	   		appendSelect(tbApi);
	   		isFirst =false;
	       },
}).on('xhr.dt',function(e){
	if(!isFirst){
		appendSelect(tbApi)		
	}
})


function isEmpty(str){
	return str == null || str == "" ? true:false;
}

function appendSelect(api){
	api.columns().indexes().flatten().each(function (i) {
        if (i == 0) {
            var column = api.column(i);
            $(column.header()).html("菜品分类");
            var $span =  $('<span class="addselect">▾</span>').appendTo($(column.header()))
             select = $('<select><option value="">全部</option></select>')
                    .appendTo($(column.header()))
                    .on('click', function (e) {
                    	e.stopPropagation();
                        var val = $.fn.dataTable.util.escapeRegex(
                                $(this).val()
                        );
                        column.search(val ? '^' + val + '$' : '', true, false).draw();
                    });
            	column.data().unique().sort().each(function (d, j) {
            	if(d!=null && d!=""){
            		select.append('<option value="' + d + '">' + d + '</option>')
                    $span.append(select)
            	}
            });
        }
    });
}

//搜索
$("#searchReport").click(function(){
	var beginDate = $("#beginDate").val();
	var endDate = $("#endDate").val();
	//判断 时间范围是否合法
	if(beginDate>endDate){
		toastr.error("开始时间不能大于结束时间");
		return ;
	}
	var data = {"beginDate":beginDate,"endDate":endDate,"sort":sort};
	//更新数据
	shoptable.ajax.reload();
	toastr.success("查询成功");
})

//店铺菜品报表下载
$("#shopArticleExcel").click(function(){
	var beginDate = $("#beginDate").val();
	var endDate = $("#endDate").val();
	var selectValue = select[0].value;
	var beginDate = $("#beginDate").val();
	var endDate = $("#endDate").val();
	location.href="report/article_excel?beginDate="+beginDate+"&&endDate="+endDate+"&&selectValue="+selectValue+"&&sort="+sort;
})

//按菜品序号排序
$("#sortById").click(function(){
	var beginDate = $("#beginDate").val();
	var endDate = $("#endDate").val();
	sort = "0";
	var data = {"beginDate":beginDate,"endDate":endDate,"sort":sort};
	shoptable.ajax.reload();
	toastr.success("排序成功");
})
//按销量排序

$("#sortByNum").click(function(){
	var beginDate = $("#beginDate").val();
	var endDate = $("#endDate").val();
	sort = "desc";
	var data = {"beginDate":beginDate,"endDate":endDate,"sort":sort};
	shoptable.ajax.reload();
	toastr.success("排序成功");
})

</script>
