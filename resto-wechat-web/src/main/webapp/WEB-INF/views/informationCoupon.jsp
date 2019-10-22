<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<base href="<%=basePath%>">
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<meta charset="UTF-8">
	<title>优惠券</title>
	<!-- 禁止缩放 -->
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui">
	<link rel="stylesheet" href="assets/css_old/base.css">
	<link rel="stylesheet" href="assets/css_old/global.css">
	<link rel="stylesheet" href="assets/css_old/iconfont.css">
	<link rel="stylesheet" href="assets/css_old/information.css">
	<style>
		
	</style>
</head>
<body style="height:100%;background: none;overflow: hidden;">
	<!-- 内容开始 -->
	<div class="content information informationCoupon">
		<div class="tabnav CouponCategory list3" style="border-radius: 10px 10px 0px 0px;">
			<div class="tabnav-item active" data-status="0">未使用<span>0</span></div>
			<div class="tabnav-item"  data-status="2">已过期<span>0</span></div>
			<div class="tabnav-item"  data-status="1">已使用<span>0</span></div>
		</div>
	</div>
	<div id="scroll-wapper" style="overflow: hidden;">
		<ul class="CouponList mt30">
		</ul>
	</div>
	
	<div id="tmpl" style="display: none;">
		<li class="pepper-con coupon-item">
			<div class="pepper-w">
				<div class="pepper pepper-blue">
					<div class="pepper-l">
					<p class="pepper-l-num"><i>¥</i><span data-key="value">70</span><span data-key="name" style="font-size:12px"></span></p>
					<p class="pepper-l-con">使用条件：<span data-key="MODE_name"></span></p>
					<p class="pepper-l-tim">使用时间：<span data-key="time"></span></p>
				</div>
				<div class="pepper-r">
					<span>有效期限</span>
					<div data-key="DATE">2015.10.01<p class="pepper-line">至</p>2015.10.24</div>
				</div>
				</div>
				<div class="pepper-b pepper-blue">
					<div class="pb-con"></div>
					<div class="pb-border"></div>
				</div>
			</div>
		</li>
		<!--<li class="coupon-item">
			 <div class="CouponListInfo">
			 	<p>优惠券</p>
				<p>价值：<span class="priceText" data-key="VALUE">00</span>
				<span data-key="DATE"></span></span>
				</p>
			</div>
		</li>-->
	</div>
	<script src="http://cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
	<script src="//cdn.bootcss.com/iScroll/5.1.3/iscroll.min.js"></script>
	<script src="assets/js_old/customBin/utils.js"></script>
	<script>
	var h = $(".informationCoupon").height();
	$("#scroll-wapper").height($("body").height()-h);
	
		var isc = new IScroll("#scroll-wapper",{click:true});
	
		var listCoupon ={};
		var i =0;
		$("[data-status]").each(function(){
			var that = $(this);
			var status = $(this).data("status");
			cAjax("customer/listCouponByStatus",{status:status},function(result){
			 	that.find("span").html(result.data.length);
			 	listCoupon[status] = result.data;
			 	that.click(function(){
			 		showCoupon(status);
			 		that.parent().find(".active").removeClass("active");
			 		that.addClass("active");
			 	});
			 	$(".tabnav-item.active").click();
			});
		});
		
		function showCoupon(status){
			$(".CouponList").html("");
			var couponList = listCoupon[status];
			for(var i in couponList){
				var coupon = couponList[i];
				coupon.DATE = new Date(coupon.beginDate).format("yyyy.MM.dd")+" 至 "+new Date(coupon.endDate).format("yyyy.MM.dd");
				coupon.time = new Date(coupon.beginTime).format("hh:mm:ss")+" - "+new Date(coupon.endTime).format("hh:mm:ss");
				var item = $("#tmpl").find(".coupon-item").clone();
				var modeText = {0:"通用",1:"堂吃",2:"外卖"};
				coupon.MODE_name = modeText[coupon.distributionModeId];
				if(coupon.minAmount>0){
					coupon.MODE_name+=" 消费满"+coupon.minAmount+"元可使用"
				}
				item.setAjaxParam(coupon);
				$(".CouponList").append(item);
			}
			if(couponList.length==0){
				$(".CouponList").html("没有该类型优惠券");
			}
			isc.refresh();
		}
		
		document.addEventListener('touchmove', function(e) {
			e.preventDefault();
		}, false);
		
	</script>
</body>
</html>