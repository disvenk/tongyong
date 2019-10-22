<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div>
    	<div class="panel panel-info">
	<div class="panel-heading text-center" style="font-size: 22px;">
		<strong>套餐属性列表</strong>
	</div>
	<div class="panel-body">
		<table class="table table-striped table-bordered table-hover"
			id="mealAttrList">
		</table>
	</div>
</div>
    </div>
 <script src="assets/customer/date.js" type="text/javascript"></script>
<script>
	var dataSource;
	var articleId = "${articleId}";
	var beginDate = "${beginDate}";
	var endDate = "${endDate}";
	$.ajax( {  
	    url:'articleSell/queryArticleMealAttr',
	    async:false,
	    data:{  
	    	'articleId':articleId,
	    	'beginDate':beginDate,
	    	'endDate':endDate
	    },  
	    success:function(result) { 
	    	if(result.success == true){
	    		dataSource=result.data;
	    	}else{
	    		dataSource=[];
	    	}
	     },  
	     error : function() {
			 toastr.clear();
             toastr.error("系统异常，请刷新重试");
	     }   
	});
	
	var tb = $("#mealAttrList").DataTable({
		lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
		data:dataSource,
		bSort:false,
		columns : [
			{
				title : "属性名称",
				data : "articleFamilyName"
			},
			{
				title : "菜品名称",
				data : "articleName"
			},{
				title : "销量",
				data : "brandSellNum"
			}
		]
	});
</script>
