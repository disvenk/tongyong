<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form role="form">
	<input type="hidden" name="roleId" value="${roleId}"/>
	<div id="assign-tree">正在加载权限列表.....</div>
	<div id="assign-ids">
	</div>
</form>

<script>
	var C = new Controller();
	var roleId = $("[name='roleId']").val();
	
	C.ajax("role/assignData",{roleId:roleId},function(assignData){
		loadTree(assignData);
	});
	
	function loadTree(assignData){
		var allPermission = assignData.permissions;
		$('#assign-tree').jstree({
			'plugins' : [ "wholerow", "checkbox", "types" ],
			'core' : {
				"themes" : {
					"responsive" : false
				},
				'data' : allPermission
			},
			"types" : {
				"default" : {
					"icon" : "fa fa-folder icon-state-warning icon-lg"
				},
				"file" : {
					"icon" : "fa fa-file icon-state-warning icon-lg"
				}
			},
		});
		
		
		$('#assign-tree').on("changed.jstree",function(e,data){
			$("#assign-ids").html(idsToInputs(data.selected));
		});
		var hasPermissions = assignData.hasPermissions;
		$("#assign-tree").on("ready.jstree",function(e,data){
			data.instance.select_node(hasPermissions);
		});
	}
	
	function idsToInputs(perIds){
		var ids = new Array();
		for(var i in perIds){
			var id = perIds[i];
			ids.push($("<input>",{type:"hidden",name:"pids",value:id}))
		}
		return ids;
	}
	
</script>