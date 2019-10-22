<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form role="form">
	<input type="hidden" name="id" value="${m.id }">
	<input type="hidden" name="parentId" value="${parentId }">
	<div class="form-body">
		<div class="form-group">
			<label>权限名称</label>
			<input type="text" class="form-control" name="permissionName" value="${m.permissionName }">
		</div>
		<div class="form-group">
			<label>权限地址</label>
			<input type="text" class="form-control" name="permissionSign" value="${m.permissionSign }"/>
		</div>
	</div>
</form>
