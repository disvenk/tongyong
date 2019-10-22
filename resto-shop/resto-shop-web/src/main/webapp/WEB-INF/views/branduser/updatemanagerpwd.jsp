<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>

<div class="panel panel-success">
	<div class="panel-heading">
		<strong>操作口令</strong>
	</div>
	<div class="panel-body">
		<form class="form-horizontal" onsubmit="return check()">

			<div class="form-group">
				<label class="col-md-3 control-label">原口令：</label>
				<div class="col-md-5">
					<input id="oldpwd" type="password" required class="form-control"
						   placeholder="请输入原口令！" />
				</div>
			</div>

			<div class="form-group">
				<label class="col-md-3 control-label" >新口令：</label>
				<div class="col-md-5">
					<input id="password" type="password" required class="form-control"
						placeholder="请输入新口令！" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3  control-label">再次输入新口令:</label>
				<div class="col-md-5">
					<input id="passwordAgain" type="password" required
						class="form-control" placeholder="再次输入新口令！" />
				</div>
			</div>
			<div class="col-md-11">
				<p class="text-danger text-center" style="font-size: 16px;" id="validataMsg"></p>
			</div>
			<div class="col-md-3 col-md-offset-5">
				<input type="submit" class="btn green" value="修改"/>
			</div>
		</form>
	</div>
</div>
<script>
	function check(){

		$.post("branduser/checkPwd",{password:$("#oldpwd").val()},function(result){
			toastr.clear();
			$("#validataMsg").html("");
			if(result.success){
				var password = $("#password").val();
				var passwordAgain = $("#passwordAgain").val();
				if(password.length>0 && passwordAgain.length>0 && password == passwordAgain){
					$.post("branduser/updateSuperPwd",{password:password},function(result){
						toastr.clear();
						$("#validataMsg").html("");
						if(result.success){
							toastr.success("修改成功！");
						}else{
							toastr.error("修改失败！");
						}
					})
				}else{
					$("#validataMsg").html("<strong>两次输入的口令不一致！</strong>");
				}
			}else{
				toastr.error("原始口令错误！");
			}
		})
		return false;

	}
</script>
