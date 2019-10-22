<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>

<div>
	<!-- Nav tabs -->
	<ul class="nav nav-tabs" role="tablist">
		<li role="presentation" class="active"><a href="#shopQrCode"
			aria-controls="shopQrCode" role="tab" data-toggle="tab">店铺二维码</a></li>
		<li role="presentation"><a href="#qrCode" aria-controls="qrCode"
			role="tab" data-toggle="tab">二维码生成</a></li>
	</ul>

	<!-- Tab panes -->
	<div class="tab-content">
		<div role="tabpanel" class="tab-pane active" id="shopQrCode">
			<form class="form-horizontal" id="qrCodeForm">
				<div class="form-group">
					<label for="inputEmail3" class="col-sm-3 control-label">选择店铺：</label>
					<div class="col-sm-5">
						<select class="form-control" id="shopId" name="shopId">
							<!-- 						<option value="-1">---请选择---</option> -->
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="nputPassword3" class="col-sm-3 control-label">开始桌号：</label>
					<div class="col-sm-5">
						<input type="number" class="form-control" name="beginTableNumber"
							id="beginTableNumber">
					</div>
				</div>
				<div class="form-group">
					<label for="nputPassword3" class="col-sm-3 control-label">结束桌号：</label>
					<div class="col-sm-5">
						<input type="number" class="form-control" name="endTableNumber"
							id="endTableNumber">
					</div>
				</div>
				<div class="form-group">
					<label for="nputPassword3" class="col-sm-3 control-label">忽略桌号：</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" name="ignoreNumber" id="ignoreNumber"
							placeholder="如有多个桌号，请使用逗号分隔。">
					</div>
				</div>
				<input type="hidden" name="shopName" id="shopName">
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-3">
						<button type="button" id="btn_shop_run" class="btn green">执&nbsp;&nbsp;行</button>
					</div>
					<div class="col-sm-3" id="downQRFile"></div>
				</div>
				<div class="form-group">
					<div class=" text-center">
						<p id="validataMsg" class="text-danger"></p>
					</div>
				</div>
			</form>
		</div>
		<div role="tabpanel" class="tab-pane" id="qrCode">
			<form class="form-horizontal">
				<div class="form-group">
					<label for="inputEmail3" class="col-sm-3 control-label">二维码内容</label>
					<div class="col-sm-5">
						<textarea class="form-control" id="qrcontent"
							placeholder="请输入二维码的内容：如 RestoPlus"></textarea>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-3">
						<button type="button" id="btn_run" class="btn green">生&nbsp;&nbsp;成</button>
					</div>
				</div>
				<div class="text-center">
					<img src="" id="img">
				</div>
			</form>
		</div>
	</div>

</div>

<script>
	//店铺二维码生成
	$(function() {
		$.post("qrcode/queryShops", function(data) {
			$(data).each(function(i, item) {
				var shopName = item.name;
				var shopId = item.id;
				$("#shopId").append("<option value='"+shopId+"'>" + shopName + "</option>");
			})
		})

		$("#btn_shop_run").click(function() {
			var shopId = $("#shopId option:selected").val();
			$("#shopName").val($("#shopId option:selected").text());
			if($("#beginTableNumber").val() == $("#endTableNumber").val() && $("#beginTableNumber").val() == $("#ignoreNumber").val()){
				$("#validataMsg").html("开始桌号 和 结束桌号 和 忽略桌号 不能相同！");
				return ;
			}
			if (shopId != -1 && !jQuery.isEmptyObject($("#beginTableNumber").val()) && !jQuery.isEmptyObject($("#endTableNumber").val())) {
				showMsg(true, "开始生成,请稍后！");
				$("#downQRFile").empty();
				$("#btn_shop_run").attr("disabled", "disabled");
				var data = $("#qrCodeForm").serialize();
				$.post("qrcode/run",data,function(data) {
					if (data.success) {
						var href = "<a class='btn blue' href='qrcode/downloadFile?fileName=" + data.message + "'>点我下载</a>";
						$("#downQRFile").html(href);
						showMsg(true, "生成成功");
					} else {
						$("#validataMsg").html("网络好像出了点问题，再试一次吧！");
						showMsg(false,"网络好像出了点问题，再试一次吧！");
					}
					$("#btn_shop_run").removeAttr("disabled");
				})
			} else {
				$("#validataMsg").html("请选择相应的店铺 和  输入开始结束的桌号");
			}
		})
		//清空提示
		$("input").click(function() {
			$("#validataMsg").empty();
		})
	})

	function showMsg(flage, msg) {
		toastr.clear();
		if (flage) {
			toastr.success(msg);
		} else {
			toastr.error(msg);
		}
	}

	//二维码生成
	$(function() {
		$("#btn_run").click(
			function() {
				var keyword = $("#qrcontent").val() != null && $("#qrcontent").val() != "" ? $("#qrcontent") .val() : "RestoPlus";
				var url = "qrcode/qrRun?content=" + keyword;
				$("#img").attr("src", url);
			})
	})
</script>
