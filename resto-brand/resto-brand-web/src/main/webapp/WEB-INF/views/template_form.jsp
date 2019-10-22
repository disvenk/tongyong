<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form role="form">
	<div class="form-body">
		<div class="form-group">
			<label>下拉框</label> 
			<select class="form-control">
				<option value="">1</option>
				<option value="">2</option>
				<option value="">3</option>
			</select>
		</div>
		<div class="form-group">
			<label>文本框</label>
			<input type="text" class="form-control">
		</div>
	</div>
	<div class="form-actions right">
		<button type="button" class="btn default">Cancel</button>
		<button type="submit" class="btn green">Submit</button>
	</div>
</form>