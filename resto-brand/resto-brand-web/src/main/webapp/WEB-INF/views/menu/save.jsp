<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form role="form">
	<input type="hidden" name="id" value="${m.id }">
	<input type="hidden" name="isMenu" value="true"/>
	<div class="form-body">
		<div class="form-group">
			<label>父菜单</label> 
			<select class="form-control" name="parentId" data-selected="${m.parent.id }">
				<option value="">选择父菜单</option>
				<c:forEach items="${parentMenus }" var="p">
					<option value="${p.id }">${p.permissionName }</option>
				</c:forEach>
			</select>
		</div>
		<div class="form-group">
			<label>图标</label>
			<div class="input-group">
				<span class="input-group-addon">
					<i class="fa ${m.menuIcon==null?'fa-bars':m.menuIcon }" id="forMenuIcon"></i>
				</span>
				<input type="text" class="form-control" name="menuIcon" value="${m.menuIcon==null?'fa-bars':m.menuIcon }">
			</div> 
		</div>
		<div class="form-group">
			<label>菜单名称</label>
			<input type="text" class="form-control" name="permissionName" value="${m.permissionName }">
		</div>
		<div class="form-group">
			<label>菜单地址</label>
			<input class="form-control autocomplete2me" name="permissionSign" id="permissionSignInput" value="${m.permissionSign }"/>
			<div data-autocompletefor="permissionSignInput" hidden>
				<c:forEach items="${allUrls }" var="url">
					<i>${url }</i>					
				</c:forEach>
			</div>
		</div>
		<div class="form-group">
			<label>菜单排序</label>
			<input type="text" class="form-control" name="sort" value="${m.sort }">
		</div>
	</div>
</form>
<script>
	$("[name='menuIcon']").change(function(){
		$("#forMenuIcon").attr("class","fa "+$(this).val());
	});
	new Controller().handlerAutoComplete();
</script>