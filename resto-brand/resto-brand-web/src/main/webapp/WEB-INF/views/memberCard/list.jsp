<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<form class="form-horizontal">
	<div class="form-group">
		<label for="nputPassword3" class="col-sm-3 control-label">公众号appid：</label>
		<div class="col-sm-5">
			<input type="text" id="appid" class="form-control" name="appid">
		</div>
	</div>
	<div class="form-group">
		<label for="nputPassword3" class="col-sm-3 control-label">公众号secret：</label>
		<div class="col-sm-5">
			<input type="text" id="secret" class="form-control" name="secret">
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-offset-3 col-sm-3">
			<button type="button" id="token" class="btn green">生成access_token</button>
		</div>
	</div>
	<div class="form-group">
		<label for="nputPassword3" class="col-sm-3 control-label">微信会员卡card_id：</label>
		<div class="col-sm-5">
			<input type="text" id="cardId" class="form-control" name="cardId">
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-offset-3 col-sm-3">
			<button type="button" id="card" class="btn green">生成微信会员卡url</button>
		</div>
	</div>
	<div class="text-center">
		<p id="content"></p>
	</div>
</form>
<script>
	$(function() {
	})
	$("#token").click(function(){
		$.get("memberCard/token", { appid: $("#appid").val(), secret: $("#secret").val()},function(data){
			$("#content").html(data)
		});
	})
	$("#card").click(function(){
		$.get("memberCard/cardUrl", { appid: $("#appid").val(), secret: $("#secret").val(), cardId:$("#cardId").val()},function(data){
			$("#content").html(data)
		});
	})

</script>
