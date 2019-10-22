<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form role="form">
	<input type="hidden" name="id" value="${m.id }">
	<div class="form-body">
		<div class="form-group">
			<label>角色名称</label>
			<input type="text" class="form-control" name="roleName" value="${m.roleName }">
		</div>
		<div class="form-group">
			<label>角色标示</label>
			<input type="text" class="form-control" name="roleSign" value="${m.roleSign }">
		</div>
		<div class="form-group">
			<label>角色描述</label>
			<input type="text" class="form-control" name="description" value="${m.description }">
		</div>
		<div class="form-group">
			<label>角色用户组</label>
			<select name="userGroupId" class="form-control" >
				<c:forEach items="${usergroup }" var="g">
					<option value="${g.id}" ${g.id==m.userGroupId?"selected":"" }>
						${g.name }
					</option>
				</c:forEach>
			</select>
		</div>
	</div>
</form>
