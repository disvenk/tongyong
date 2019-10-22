<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="table-div">
		<div class="clearfix"></div>
		<div class="table-filter">&nbsp;</div>
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
				url : "modulelist/list_all",
				dataSrc : ""
			},
			 "order": [[ 1, 'desc' ]],
			columns : [
				{                 
					title : "模块名称",
					data : "name",
					createdCell:function(td,data,obj){
						$(td).html("<span style='font-size:18px'>"+data+"</span> ");
					}
				},                
				{
					title:"是否开通",
					data:"isOpen",
					createdCell:function(td,data){
						var content = {text:"未开通",style:"label label-danger"}
						if(data){
							content={text:"已开通",style:"label label-success"};
						}
						$(td).html($("<span>").html(content.text).addClass(content.style));
					}
				},
				{
					title : "功能设置",
					data : "sign",
					createdCell:function(td,data,obj){
						var btn = "";
						if(obj.isOpen){
							btn = $("<button>").html("编辑设置").addClass("btn btn green");
							btn.click(function(){
								C.loadForm({
									url:"modulelist/edit/"+data,
									width:800
								});
							});
						}
						$(td).html(btn);
					}
				}],
		});
		
		var C = new Controller(null,tb);
	}());
	
</script>
