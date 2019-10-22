<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<form class="form-horizontal" id="qrCodeForm">
	<div class="form-group">
		<label for="inputEmail3" class="col-sm-3 control-label">选择品牌：</label>
		<div class="col-sm-5">
			<select class="form-control" id="brandId">
				<option value="-1">---请选择---</option>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label for="inputEmail3" class="col-sm-3 control-label">选择店铺：</label>
		<div class="col-sm-5">
			<select class="form-control" id="shopId" name="shopId">
				<option value="-1">---请选择---</option>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label for="nputPassword3" class="col-sm-3 control-label">开始桌号：</label>
		<div class="col-sm-5">
			<input type="text" class="form-control" name="beginTableNumber">
		</div>
	</div>
	<div class="form-group">
		<label for="nputPassword3" class="col-sm-3 control-label">结束桌号：</label>
		<div class="col-sm-5">
			<input type="text" class="form-control" name="endTableNumber">
		</div>
	</div>
	<div class="form-group">
		<label for="nputPassword3" class="col-sm-3 control-label">忽略桌号：</label>
		<div class="col-sm-5">
			<input type="text" class="form-control" name="ignoreNumber">
		</div>
	</div>
	<input type="hidden" name="brandSign" id="brandSign">
	<input type="hidden" name="shopName" id="shopName">
	<div class="form-group">
		<div class="col-sm-offset-3 col-sm-3">
			<button type="button" id="run" class="btn green">执&nbsp;&nbsp;行</button>
		</div>
		<div class="col-sm-3" id="downQRFile">
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<p id="validataMsg" class="text-danger"></p>
		</div>
	</div>
</form>
<script>
	$(function() {
		//查询所有品牌信息
		$.post("shopqrcode/queryBrands", function(data) {
			$(data).each(
				function(i, item) {
					var brandName = item.brandName;
					var val = item.id+"#"+item.brandSign;
					$("#brandId").append( "<option value='"+val+"'>" + brandName + "</option>");
				})
		})

		//查询相应的店铺信息
		$("#brandId").change(function() {
			$("#shopId").empty();
			$("#validataMsg").empty();
			var brandId = $(this).val();
			var brandSign = brandId.substring(brandId.indexOf("#")+1,brandId.length);
			brandId = brandId.substring(0,brandId.indexOf("#"));
			$("#brandSign").val(brandSign);
			if(brandId != -1){
				$.post("shopqrcode/queryShops", {"brandId" : brandId }, function(data) {
					$(data).each(
						function(i, item) {
							var shopName = item.name;
							var shopId = item.id;
							$("#shopId").append("<option value='"+shopId+"'>" + shopName + "</option>");
						}
					)
				})
			}else{
				$("#shopId").append("<option value='-1'>---请选择---</option>");
			}
		})
		
		$("#run").click(function(){
			var shopId = $("#shopId option:selected").val();
			$("#shopName").val($("#shopId option:selected").text());
			if(shopId != -1){
				showMsg(true,"开始生成");
				$("#downQRFile").empty();
				$("#run").attr("disabled","disabled");
				var data = $("#qrCodeForm").serialize();
				$.post("shopqrcode/run",data,function(data){
					if(data.success){
						var href = "<a class='btn blue' href='shopqrcode/downloadFile?fileName="+data.message+"'>点我下载</a>";
						$("#downQRFile").html(href);
						showMsg(true,"生成成功");
					}else{
						$("#validataMsg").html("网络好像出了点问题，再试一次吧！");
						showMsg(false,"网络好像出了点问题，再试一次吧！");
					}
					$("#run").removeAttr("disabled");
				})
			}else{
				$("#validataMsg").html("请选择相应的店铺");
			}
		})
	})
	
	function showMsg(flage,msg){
		toastr.clear();
		if(flage){
			toastr.success(msg);
		}else{
			toastr.error(msg);
		}
	}
</script>
