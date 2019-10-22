<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<c:set var="v" value="20160718"></c:set>
	<base href="<%=basePath%>">
	<title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui">
	<meta name="format-detection" content="telephone=no" />
	<%--<link rel="stylesheet" href="assets/css_old/base.css">--%>
	<%--<link rel="stylesheet" href="assets/css_old/global.css">--%>
	<%--<link rel="stylesheet" href="assets/css_old/iconfont.css">--%>
	<link rel="stylesheet" href="assets/css_old/information.css">
	<link href="assets/css/Font-Awesome-3.2.1/css/font-awesome.min.css" rel="stylesheet" >
	<link href="http://cdn.bootcss.com/Swiper/3.1.2/css/swiper.min.css" rel="stylesheet">
	<link href="assets/css/iconfont/iconfont.css" rel="stylesheet">
	<link href="assets/css/iconcode/iconcode.css" rel="stylesheet">

	<link rel="stylesheet" href="assets/css/weui.css?v=${v }">
	<link rel="stylesheet" href="assets/css/total.css?v=${v }">
	<link rel="stylesheet" href="assets/css/common.css?v=${v }">
	<link rel="stylesheet" href="assets/css/red_bag.css">
	<script>
		var IMAGE_SERVICE = "${IMAGE_SERVER}";
	</script>
	<style type="text/css">
		${setting.wechatCustomoStyle}
	</style>
</head>

<body>
<div id="main-page">
	<div id="content-home">
		<component :is="page+'-page'" :shop.sync="shop" :customer.sync="customer" :otherdata.sync="otherdata" :noticelist.sync="hasNotice"></component>
	</div>
	<div class="bottom-fxied" id="main-menu">
		<ul id="ul-menu" >
			<li v-for="menu in menuList" @click="changePage(menu)" :class="{cur:page==menu.name}"><i></i><span>{{menu.title}}</span></li>
		</ul>
	</div>
	<c:if test="${sessionScope.current_shop.shopMode!=null }">
		<share-dialog :show.sync="shareDialog.show" :isshare="shareDialog.isshare" :appraise="shareDialog.appraise" :setting="shareDialog.setting"></share-dialog>
		<bind-phone :show.sync="showBindPhone" :content="allSetting.customerRegisterTitle"></bind-phone>
		<weui_dialog_confirm :show.sync="confirmDialogShow" :option="dialogOptions"></weui_dialog_confirm>

		<charge-dialog :show.sync="chargeDialog.show" :charge-list="chargeDialog.chargeList"></charge-dialog>

		<pay-dialog :show.sync="payAlter.show" :order.sync="payAlter.order"></pay-dialog>

		<red-papper-dialog-registered :show.sync="redPapperRegisteredDialog.show" :name="redPapperRegisteredDialog.name" :title="redPapperRegisteredDialog.title" @open="openRegisteredPapper()"></red-papper-dialog-registered>

		<order-detailed-dialog :show.sync="customNewOrder.show" :order.sync="customNewOrder.order" :customer="customer" :shop="shop" :option="customNewOrder.option"></order-detailed-dialog>

		<order-appraise-dialog :show.sync="appraiseOrder.show" :order.sync="appraiseOrder.order"></order-appraise-dialog>

		<red-papper-dialog :show.sync="redPapperDialog.show" :name="redPapperDialog.name" :title="redPapperDialog.title" @open="redPapperOpen(redPapperDialog.order)"></red-papper-dialog>

		<red-papper-open :show.sync="redOpenDialog.show" :name="redOpenDialog.name" :title="redOpenDialog.title" :appraise="redOpenDialog.appraise" :money="redOpenDialog.money"></red-papper-open>

		<reward-dialog :show.sync="rewardDialog.show" :appraise="rewardDialog.appraise"></reward-dialog>

		<iframe-dialog :show.sync="iframeDialog.show" :src="iframeDialog.src"></iframe-dialog>

		<callnumber-dialog :show.sync="callNumberDialog.show" :order="callNumberDialog.order" :customer="customer"></callnumber-dialog>

		<notice-dialog :show.sync="noticeDialog.show" :notice="noticeDialog.notice"></notice-dialog>

		<noticelist-dialog :show.sync="noticeListDialog.show" :noticelist="noticeListDialog.noticelist"></noticelist-dialog>
	</c:if>


	<shoplist-dialog :show.sync="shopListDialog.show" :shoplist="shopListDialog.shoplist"></shoplist-dialog >
	<weui_alter :show.sync="wAlter.show" :title="wAlter.title" :content="wAlter.content"></weui_alter>
	<weui-dialog :show.sync="wMessage.show" :msg="wMessage.message" ></weui-dialog>
	<weui-loading :show.sync="loadShow"></weui-loading>
</div>

<script type="text/html" id="home-temp" >
	<jsp:include page="vue-comp/home.jsp"></jsp:include>
</script>

<script type="text/html" id="tangshi-temp">
	<jsp:include page="vue-comp/tangchi.jsp"></jsp:include>
</script>

<script type="text/html" id="my-temp">
	<jsp:include page="vue-comp/my.jsp"></jsp:include>
</script>

<script type="text/html" id="pay-temp">
	<jsp:include page="vue-comp/pay.jsp"></jsp:include>
</script>

<script type="text/html" id="waimai-temp">
	<div>
		<h1>waimai</h1>
	</div>
</script>

<script src="http://cdn.bootcss.com/jquery/2.2.1/jquery.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" ></script>

<script type="text/javascript">

	<c:if test="${!debug}">
	wx.config(${jsconfig});
	var readyFunction = [];
	var wxIsReady=false;
	wx.ready(function(){
		wxIsReady=true;
		for(var i in readyFunction){
			var n = readyFunction[i];
			if(typeof n=="function"){
				n();
				console.info("执行wxReady function:"+n);
			}
		}
	});
	//执行微信方法的接口
	function executeWxFunction(fun){
		if(typeof fun=="function"){
			if(wxIsReady){
				fun();
			}else{
				readyFunction.push(fun);
			}
		}
	}
	</c:if>
	var allSetting = ${settingJson};

</script>
<script src="assets/js/jquery.fly.min.js" ></script>
<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
<script src="//cdn.bootcss.com/Swiper/3.1.2/js/swiper.jquery.min.js" ></script>
<script src="//cdn.bootcss.com/vue/1.0.17/vue.min.js"></script>
<script src="//cdn.bootcss.com/iScroll/5.1.3/iscroll.min.js"></script>
<script src="//cdn.bootcss.com/iScroll/5.1.3/iscroll-probe.min.js"></script>
<script src="http://api.map.baidu.com/api?v=2.0&ak=IIH5pl4S3VE81wGVG3aYL3PWOirz2uCP"></script>
<script src="assets/js/custom/wxFunction.js?v=${v }"></script>
<script src="assets/js/custom/utils.js?v=${v }"></script>
<script src="assets/js/custom/service.js?v=${v }"></script>
<script src="assets/js/custom/base/components.js?v=${v }"></script>
<script src="assets/js/custom/base/page/home-page.js?v=${v }"></script>
<script src="assets/js/custom/base/page/tangshi-page.js?v=${v }1"></script>
<script src="assets/js/custom/base/page/waimai-page.js?v=${v }"></script>
<script src="assets/js/custom/base/page/my-page.js?v=${v }"></script>
<script src="assets/js/custom/base/main.js?v=${v }"></script>
<script src="assets/js/custom/base/page/pay-page.js?v=${v }"></script>
<c:if test="${sessionScope.current_shop.shopMode!=null }">
	<script src="assets/js/custom/mode/${sessionScope.current_shop.shopMode}/custom.js?v=${v}"></script>
</c:if>
<c:if test="${sessionScope.current_shop.shopMode==null }">
	<script src="assets/js/custom/mode/default/custom.js?v=${v}"></script>
</c:if>
<script type="text/javascript">
	function iScrollClick(){
		if (/iPhone|iPad|iPod|Macintosh/i.test(navigator.userAgent)) return false;
		if (/Chrome/i.test(navigator.userAgent)) return (/Android/i.test(navigator.userAgent));
		if (/Silk/i.test(navigator.userAgent)) return false;
		if (/Android/i.test(navigator.userAgent)) {
			var s=navigator.userAgent.substr(navigator.userAgent.indexOf('Android')+8,3);
			return parseFloat(s[0]+s[3]) < 44 ? false : true
		}
	}
	$(function() {
		FastClick.attach(document.body);
	});
	document.addEventListener('touchmove', function(e) {
		e.preventDefault();
	}, false);
</script>
</body>
</html>
