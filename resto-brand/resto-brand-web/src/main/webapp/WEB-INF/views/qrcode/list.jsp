<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<form class="form-horizontal">
	<div class="form-group">
		<label for="inputEmail3" class="col-sm-3 control-label">二维码内容</label>
		<div class="col-sm-5">
			<textarea class="form-control" id="qrcontent"></textarea>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-offset-3 col-sm-3">
			<button type="button" id="run" class="btn green">生&nbsp;&nbsp;成</button>
		</div>
	</div>
	<div class="text-center">
		<img src="" id="img">
	</div>
</form>
<script>
	$(function() {
		$("button").click(function(){
			var url = "qrcode/run?content=" + $("#qrcontent").val();
			$("#img").attr("src",url);
		})
	})
</script>
