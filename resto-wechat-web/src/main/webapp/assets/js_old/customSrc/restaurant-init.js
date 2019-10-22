$(document).ready(
	function() {
		
		var subpage = getParam("subpage");  //子页面采用参数定位
		if(!subpage){
			subpage = location.hash;
		}else{
			subpage = "#"+subpage;
		}
		var menuBtn = $("[data-tab='" + subpage + "']");
		if (menuBtn.length < 1) {
			menuBtn = $("#ul-menu>li:eq(1)");
		}
		menuBtn.click();
		setTimeout(function() {
			window.onhashchange = function() {
				var currentHash = location.hash.toString();
				if (currentHash.indexOf(".hidem") != -1) {
					$("#ul-menu").hide();
					currentHash = currentHash.substring(0, currentHash.indexOf(".hidem"));
				} else {
					$("#ul-menu").show();
					var menuBtn = $("#ul-menu>li[data-tab='" + currentHash+ "']");
					menuBtn.click();
				}
				$(".custom-dialog:not(.not-close)").hide();
			};
		}, 800);
});
homeInit();
var informationIsc;

// 主菜单样式切换
$("#ul-menu>li").click(function() {
	var tabId = $(this).data("tab");
	$(".tab-content").find(".tab-subpage").removeClass("active");
	$(tabId).addClass("active");
	currentModeDiv = $(tabId);
	$(this).parent().find(".cur").removeClass("cur");
	$(this).addClass("cur");
	location.hash = tabId;
	var title = $(this).data("title");
	$("head>title").text(title);
});

var mySwiper = null;
function homeBannerInit() {
	if (mySwiper) {
		mySwiper.update();
	}
	if (homeIsc) {
		homeIsc.refresh()
	}
}
function switchToInfo() {
	if (informationIsc) {
		informationIsc.refresh();
	}
	informationInit();
}
function informationInit() {
	cAjax("wx/customerInfo", "", function(info) {
		currentCustomer = info;
		var phone = info.TELEPHONE;
		$("#content-information").setAjaxParam(info);
		
		var ordersMap = new HashMap();
		$("*[data-state]").hide().each(function(index) {
			var state = $(this).data("state");
			ordersMap.put(state, []);
		});
		loadChargeItems(function(){
			location.reload();
		});
		cAjax("wx/listOrder", null, function(result) {
			for ( var i in result.list) {
				var o = result.list[i];
				var arr = ordersMap.get(o.ORDER_STATE);
				if (arr) {
					arr.push(o);
				}
			}
			for ( var i in ordersMap.getKeys()) {
				var k = ordersMap.getKeys()[i];
				var arr = ordersMap.get(k);
				if (arr.length > 0) {
					$("*[data-state='" + k + "']").show().html(arr.length);
				}
			}
			var paymentOrders = ordersMap.get(2);
			var orderDom = [];
			for ( var i in paymentOrders) {
				var order = paymentOrders[i];
				var li = order2li(order, phone);
				orderDom.push(li);
			}
			var evelOrders = ordersMap.get(10);
			for ( var i in evelOrders) {
				var order = evelOrders[i];
				var li = order2li(order, phone);
				orderDom.push(li);
			}
			$("#order-list-state2").html(orderDom);
			if (informationIsc) {
				informationIsc.refresh();
			}

		});
	});
}
function cancelOrder(obj) {
	if (confirm("你确定要取消该订单吗？已支付金额及优惠将原路返还！")) {
		var id = $(obj).parents(".order-item").data("id");
		cAjax("wx/cancelOrder", {
			ORDER_ID : id
		}, function(result) {
			if (result.result == "01") {
				alert("取消成功");
			} else {
				alert(result.msg);
			}
			location.reload();
		});
	}
}

function homeInit() {
	// 初始化 banner图
	cAjax(
			"wx/server",
			{
				p : "/pictureslider/weixin/listPictureSlider"
			},
			function(result) {
				var picList = result.list;
				$("#pictureSlider").html("");
				for ( var i in picList) {
					var pic = picList[i];
					var div = $("<div class='swiper-slide' style='text-align:center'></div>");
					var img = $("<img />");
					if (pic.PICTURE_URL) {
						img.attr("src", IMAGE_SERVICE + pic.PICTURE_URL);
						div.html(img);
					}
					$("#pictureSlider").append(div);
				}
				if (picList.length <= 1) {
					mySwiper = new Swiper('.swiper-container', {
						direction : 'horizontal',
					});
				} else {
					mySwiper = new Swiper('.swiper-container', {
						direction : 'horizontal',
						loop : true,
						autoplay : 3000,
						// 分页器
						pagination : ".swiper-pagination",
					});
				}

			});

	// newCustomer(); //新用户活动
	// 初始化店铺信息
	cAjax("wx/getShopInfo", null,
			function(result) {
				var info = result.data;
				var address = info.ADDRESS;
				var resDiv = $("#restaurant-info");
				resDiv.find(".address").html(info.ADDRESS);
				resDiv.find(".location-icon").click(
						function() {
							wxOpenLocation(parseFloat(info.LATITUDE), parseFloat(info.LONGITUDE),info.SHOP_DES, info.ADDRESS);
						});
				resDiv.find(".restaurant-time").html(
						info.SHOP_OPEN_TIME + " - " + info.SHOP_CLOSE_TIME);
				resDiv.find(".phone-a").attr("href", "tel:" + info.TEL);
				resDiv.find(".shop_des").html(info.SHOP_DES);
				$("#new-customer-notice").html(info.NEW_CUSTOMER_NOTICE);

				for ( var i = 0; i < info.AVG_LEVEL; i++) {
					resDiv.find(".starLevel").find(".iconfont").eq(i).addClass(
							"star-cur");
				}
				var shopLi = $("#restaurant-li");
				$("#content-tangshi").find(".restaurant-name-span").text(
						info.SHOP_DES);
				shopLi.html(info.SHOP_DES);
				shopLi.attr("data-addr", info.ADDRESS);
				shopLi.attr("data-title", info.SHOP_DES);
				shopLi.click(function() {
					if (tangshiMap) {
						tangshiMap.toLocation(info.LATITUDE, info.LONGITUDE);
					}
				});
			});

	// 获取广告信息
	cAjax("wx/server", {
		p : "/advert/weixin/advertDetails"
	}, function(result) {
		var adv = result.data;
		if(adv){
			var slDiv = $("#slogan-div");
			var desc = $(adv.DESCRIPTION);
			desc.find("img").attr("style", "width:100%;height:auto");
			desc.find("img").each(function() {
				$(this).attr("src", IMAGE_SERVICE + $(this).attr("src"));
			});
			slDiv.find(".slogan").html(adv.SLOGAN);
			slDiv.find(".description").html(desc);
		}
	});

	// 获取评论总数
	cAjax("wx/server", {
		p : "/appraise/weixin/appraiseCount"
	}, function(result) {
		var count = result.data.COUNT;
		var score = 0;
		if(typeof result.data.AVG_SCORE != 'undefined'){
			score = result.data.AVG_SCORE.toFixed(1);
		}
		
		$("#appraise-count-span").html("(" + count + "条" + " " + score + "分)");
		var scoreList = $("#appraise-count-dialog").find(".appraise-score-list");
		scoreList.html("");
		for(var i in result.list){
			var sc = result.list[i];
			if(i==0){
				score = sc.AVG_SCORE.toFixed(1)||100;
			}
			scoreList.append($("<li>").html(sc.YEARMONTH+ "月评论 (" + sc.COUNT + "条" + " " + sc.AVG_SCORE.toFixed(1) + "分)"));
		}
		$("#score-span").html(score + "分");
		$("#evaluate-title").click(function(){
			$("#appraise-count-dialog").show();
		});
		$("#appraise-count-dialog").find(".appraise-scale li .btn").click(function(){
			var min = $(this).data("min");
			var max = $(this).data("max");
			MAXLEVEL=max;
			MINLEVEL=min;
			currentPage = 1;
			showCount = 3;
			nextPage = 1;
			isOver = false;
			isLoad = false;
			loadAppraise(currentPage, showCount, true);
			$("#appraise-count-dialog").hide();
		});
	});

	// 初始化公告信息
	cAjax(
			"wx/server",
			{
				p : "/notice/weixin/listNotice"
			},
			function(result) {
				var list = result.list;
				noticleList = list;
				$("#notice-dom").html("");
				for ( var i in list) {
					var notice = list[i];
					var li = $("<li class='notice-li'>");
					li
							.html("<p>"
									+ notice.TITLE
									+ "<i class='icon-chevron-right' style='float:right;color:#ccc'></i></p>");
					li.attr("data-title", notice.TITLE);
					li.attr("data-content", notice.CONTENT.replace(/\n/g,
							"<br/>"));
					li.attr("data-image", notice.NOTICE_IMAGE);
					li
							.click(function() {
								var noticeDialog = $("#notice-dialog");
								noticeDialog.find(".image").attr("src",
										IMAGE_SERVICE + $(this).data("image"));
								noticeDialog.find(".title").html(
										$(this).data("title"));
								noticeDialog.find(".content").html(
										$(this).data("content"));
								noticeDialog.show();
							});
					$("#notice-dom").append(li);
					if (notice.NOTICE_TYPE == 2) {
						li.addClass("new-customer")
						li.hide();
					}
				}
				if (list.length <= 0) {
					$("#notice-dom").hide();
				} else {

					var randomIndex = Math.ceil(Math.random() * list.length) - 1;
					$("#notice-dom").show();
					// $("#notice-dom").children().get(randomIndex).click();
				}
			});
}

var noticleList = new Array();

var currentModeDiv;
var DISTRIBUTION_MODE_ID = 1;
var NAME = "堂吃";
// 缓存的商品信息， key为商品类别，value该类别的商品列表
var articleListMap = new HashMap(); // key=ARTICLEFAMILY_ID value=articleArr
// 缓存的商品信息，key为商品ID，value为该商品
var articlesMap = new HashMap(); // key=articleId value=article
var shopCart = new ShopCart();// 购物车
var t_articleListMap = new HashMap();
var t_articlesMap = new HashMap();
var t_shopCart = new ShopCart();
var w_articleListMap = new HashMap();
var w_articlesMap = new HashMap();
var w_shopCart = new ShopCart();
var tangshiFamliyInit = true;
function choiceTangshi() {
	if (tangshiFamliyInit) {
		loadDistributionModeDiv();
	} else {
		currentModeDiv.find(".choice-div").hide();
		currentModeDiv.find(".article-div").show();
	}
	tangshiFamliyInit = false;
	smenu();
}
var tangShiInit = true;
function switchToTangshi() {
	DISTRIBUTION_MODE_ID = 1;
	NAME = "堂吃";
	currentModeDiv = $("#content-tangshi");
	articleListMap = t_articleListMap;
	articlesMap = t_articlesMap;
	shopCart = t_shopCart;
	if (tangShiInit) {
		tangShiInit = false;
		choiceTangshi();
	}

	if (currentModeDiv.find(".choice-div").is(":visible")) {
		hmenu();
	}
	if (ismenushow()) {
		currentModeDiv.find(".choice-div").hide();
		currentModeDiv.find(".payment-div").hide();
		currentModeDiv.find(".article-div").show();
	}
	initIsc();
}

function returnToChoice() {
	currentModeDiv.find(".article-div").hide();
	currentModeDiv.find(".choice-div").show();
	hmenu();
}

var deliveryTime = "";
var deliveryDay = "";
var deliveryPoint = "";
var deliveryName = "";


function createCoupon(coupons, amount) {
	var li = currentModeDiv.find(".coupon-li .accountAmount");
	var isChecked = currentModeDiv.find("input.use_account").is(":checked");
	var sle = $("<select></select>").css({
		width : "62%",
		border : 0
	});
	for ( var i in coupons) {
		var co = coupons[i];
		if (amount < co.MIN_AMOUNT) {
			continue;
		}
		if (isChecked) {
			if (co.COUPON_TYPE == 1) {
				sle.append("<option value=" + co.COUPON_ID + " data-value='"
						+ co.VALUE + "'>" + co.VALUE + "元" + (co.NAME || "")
						+ "</option>");
			}
		} else {
			sle.append("<option value=" + co.COUPON_ID + " data-value='"
					+ co.VALUE + "'>" + co.VALUE + "元" + (co.NAME || "")
					+ "</option>");
		}
	}
	if (!sle.find("option").length) {
		currentModeDiv.find(".coupon-li").hide();
	} else {
		currentModeDiv.find(".coupon-li").show();
		li.html(sle);
		sle.change(function() {
			updateTotalOrder();
		});
	}
}

function findCouponValue(coupons, id) {
	for ( var i in coupons) {
		var c = coupons[i];
		if (c.COUPON_ID == id) {
			return c.VALUE;
		}
	}
	return 0;
}

// 创建单独商品的 dialog 并显示
function showSingleArticle(art) {
	var singleArticle = createShopCartItem(art);
	var cutItem = singleArticle.find(".minusNumber").removeAttr("onclick");
	singleArticle.find(".art-number").html(0);
	cutItem.click(function() {
		paymentCut(this, null, function() {
			showSingleArticle(art);
		});
	});
	singleArticle.find(".addNumber").click(function() {
		cutItem.show();
	});
	$("#single-article-dialog").find(".single-item").html(singleArticle);
	$("#single-article-dialog").find(".confrim-payment").unbind();
	$("#single-article-dialog").find(".confrim-payment").click(function() {
		toPayment(true);
	});
	$("#single-article-dialog").show();
}

// 查找所有餐品里面有没有米饭
function getRichArt() {
	var allArticles = articlesMap.getValues();
	var richArt = null;
	for ( var i in allArticles) {
		var art = allArticles[i];
		if (isRich(art.NAME)) {
			richArt = art;
		}
	}
	return richArt;
}

function isRich(artName) {
	return artName.match(/.*元气米饭.*/g);
}

var allAccountCoupon = new Array();
function toPayment(ignore) {
	if (shopCart.getAll().length <= 0) {
		showMessage("您还没有点餐哦！");
		return false;
	}
	cAjax("wx/customerInfo", null, function(customer) {
		currentCustomer = customer;
		if (!customer.TELEPHONE) {
			bindPhone("");
		} else {
			payFunction(ignore);
		}
	});
}

function payFunction(ignore) {
	var shopList = shopCart.getAll();
	var hasRice = false;
	for ( var i in shopList) {
		var art = shopList[i];
		if (isRich(art.NAME)) {
			hasRice = true;
		}
	}
	if (!hasRice && !ignore) { // 如果已点餐品里面没有米饭，而且不忽略，就提示
		var artRich = getRichArt(); // 获取所有餐品里面的米饭餐品
		if (artRich) { // 如果有米饭餐品
			showSingleArticle(artRich); // 单独显示某个餐品
			return false;
		}
	}
	if (DISTRIBUTION_MODE_ID == 2) {
		if (!isChoiceLocation()) {
			showLocationDialog();
			return false;
		}
		var selectTime = getSelectDate();
		if (!selectTime) {
			showTimeDialog();
			return false;
		} else {
			deliveryDay = selectTime.day;
			deliveryTime = selectTime.time.id;
		}
	}
	$(".save-order").prop("disabled", false).html("买单");
	cAjax("wx/listCoupon", {
		STATUS : 0,
		DISTRIBUTION_MODE_ID : DISTRIBUTION_MODE_ID
	}, function(result) {
		allAccountCoupon = result.list;
		currentModeDiv.find(".article-div").hide();
		hmenu(true);
		currentModeDiv.find(".payment-div").show().setAjaxParam(currentCustomer);
		var shopcartListDom = currentModeDiv.find(".payment-div").find(".shopcart-list");
		shopcartListDom.html(getShopCartItems());
		var totalOrderDom = getTmpl(".TotalOrder");
		currentModeDiv.find(".payment-div").find(
				".buyingConfirmDetails>.TotalOrder").remove();
		currentModeDiv.find(".payment-div").find(".buyingConfirmDetails")
				.append(totalOrderDom);
		var accountAmount = shopCart.getTotalPrice();
		createCoupon(allAccountCoupon, accountAmount);
		updateTotalOrder();
		cAjax("wx/customerInfo", null, function(customer) {
			currentCustomer = customer;
		});
		t_PayListIsc.refresh();
		w_PayListIsc.refresh();
	});
	loadChargeItems(function(){
		toPayment(true);
	});
}

function loadChargeItems(successCallback){
	cAjax("wx/server",{p:"/charge/weixin/rulesList"},function(result){
		var rules = result.list;
		var chargeBtn = currentModeDiv.find(".charge-btn");
		chargeBtn.unbind();
		if(rules.length>0){
			showToPayPage(rules,successCallback);
			chargeBtn.click(function(e){ 
				chargeDialogShow(rules,successCallback);
			});
			chargeBtn.addClass("active");
		}else{
			chargeBtn.removeClass("active");
		}
	});
}

function showToPayPage(rules,successCallback){
	currentModeDiv.find(".charge-btn-groups").html("");
	for(var i in rules){
		var r = rules[i];
		if(r.SHOW_IN){
			var btn = $("<button class='direct-chrage-btn'>");
			btn.text(r.LABEL_TEXT);
			btn.attr("data-id",r.CHARGE_ID);
			btn.click(function(e){
				var id = $(this).data("id");
				chargeWxRequest(id,successCallback);
				return false;
			});
			currentModeDiv.find(".charge-btn-groups").append(btn);
		}
	}
}
function chargeDialogShow(rules,successCallback){
	//TODO
	var chargeDialog = $("#charge-dialog");
	var chargeList = chargeDialog.find(".charge-list");
	chargeList.html("");
	for(var i in rules){
		var r = rules[i];
		var radioR = $('<input  type="radio" name="CHARGE_ID">');
		radioR.val(r.CHARGE_ID);
		radioR.attr("id",r.CHARGE_ID);
		var label = $("<label>");
		label.html(r.LABEL_TEXT);
		label.attr("for",r.CHARGE_ID);
		var li = $("<li class='weixin'>");
		li.append(radioR);
		label.append('<i class="iconfont"></i>');
		li.append(label);
		chargeList.append(li);
	}
	if(rules.length>0){
		var confrimBtn = chargeDialog.find(".confrim-charge");
		confrimBtn.prop("disabled",false).html("充值");
		confrimBtn.unbind();
		confrimBtn.click(function(){
			var chargeId = chargeDialog.find("[type='radio']:checked").val();
			if(!chargeId){
				showMessage("请选择充值金额");
				return false;
			}else{
				confrimBtn.prop("disabled",true).html("正在充值");
				chargeWxRequest(chargeId,successCallback);
			}
		});
		chargeDialog.show();
	}
}

function chargeWxRequest(charge_id,successCallback){
	loading("body");
	cAjax("wx/chargeOrderWx",{CHARGE_ID:charge_id},function(js_api_params){
		wx.chooseWXPay({
			timestamp : js_api_params.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
			nonceStr : js_api_params.nonceStr, // 支付签名随机串，不长于 32 位
			"package" : js_api_params.prepay_id, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
			signType : js_api_params.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
			paySign : js_api_params.paySign, // 支付签名
			success : function(res) {
				alert("充值成功!");
				$("#charge-dialog").hide();
				currentOrder=null; //充值成功后，清空当前订单
				successCallback&&successCallback();
			}
		});
		loaded("body");
	});
}

function getShopCartItems() {
	var items = [];
	for ( var i in shopCart.getAll()) {
		var art = shopCart.getAll()[i];
		var shopCartItem = createShopCartItem(art);
		items.push(shopCartItem);
	}
	return items;
}
function createShopCartItem(article) {
	var shopCartItem = getTmpl(".shopcart-item");
	shopCartItem.setAjaxParam(article);
	shopCartItem.attr("data-id", article.ARTICLE_ID);
	return shopCartItem;
}

function btnTimeCount(btn, time) {
	if (time > 0) {
		$(btn).html(time + "秒");
		$(btn).prop("disabled", true);
		setTimeout(function() {
			btnTimeCount(btn, time - 1);
		}, 1000);
	} else {
		$(btn).html("获取验证码");
		$(btn).prop("disabled", false);
	}
}

function bindPhone(phone) {
	var dialog = $("#welcome-dialog");
	dialog.show();
	dialog.find(".btn-code").unbind();
	dialog.find(".btn-code").click(function() {
		var inputPhone = dialog.find("[name='PHONE']").val();
		if (inputPhone == phone) {
			return true;
		} else {
			sendMsg(inputPhone, this);
		}
	});
	dialog.find(".dialog-close").unbind();
	dialog.find(".dialog-close").click(function() {
		dialog.hide();
		history.go(-1);
	});
	dialog.find(".btn-submit").unbind();
	dialog.find(".btn-submit").click(function() {
		var that = this;
		$(that).prop("disabled", true);
		var inputPhone = dialog.find("[name='PHONE']").val();
		var inputCode = dialog.find("[name='CODE']").val();
		if (inputPhone == phone) {
			return true;
		} else {
			cAjax("wx/editPhone", {
				CODE : inputCode,
				PHONE : inputPhone
			}, function(result) {
				if (result.result == 01) {
					dialog.hide();
					if (!currentCustomer.BIND_PHONE) {
						$(".notice-li.new-customer").click();
					}
					toPayment(true);
				} else {
					dialog.find(".error").html(result.msg);
					dialog.find(".error").parent().show();
					$(that).removeAttr("disabled");
				}
			});
		}
	});
}

function sendMsg(inputPhone, obj) {
	$(obj).html("正在发送").attr("disabled", "disabled");
	cAjax("wx/sendCodeMsg", {
		PHONE : inputPhone
	}, function(result) {
		if (result.result == "01") {
			btnTimeCount(obj, 60);
		} else {
			$(obj).html("手机格式错误").attr("disabled", false);
		}
	});
}

function useAccountChecked() {
	updateTotalOrder();
}

function updateTotalOrder() {
	currentOrder=null;
	var totalDom = currentModeDiv.find(".TotalOrder");
	var accountAmout = shopCart.getTotalPrice();
	var paymentAmout = accountAmout;
	totalDom.find(".article-count-price").html(accountAmout);

	var couponValue = currentModeDiv.find(
			".coupon-li:visible .accountAmount select option:selected").data(
			"value");
	paymentAmout -= couponValue || 0;
	if (paymentAmout > 0) {
		var use_account = currentModeDiv.find(".use_account").is(":checked") ? 1
				: 0;
		if (use_account) {
			paymentAmout -= currentCustomer.ACCOUNT;
		}
	} else {
		currentModeDiv.find(".use_account").prop("checked", false);
	}
	paymentAmout = paymentAmout < 0 ? 0 : paymentAmout;
	changeTotalOrder(paymentAmout);
}

function changeTotalOrder(accountAmout) {
	currentModeDiv.find(".actually-pay").attr("data-money", accountAmout);
	currentModeDiv.find(".actually-pay").html(accountAmout.toFixed(2));
}

function saveOrder(btn) {
	$(btn).prop("disabled", true).html("正在提交..");
	submitOrderForm(btn);
	// cAjax("wx/customerInfo",null,function(customer){
	// currentCustomer=customer;
	// if(!customer.TELEPHONE){
	// bindPhone("");
	// }else{
	// submitOrderForm(btn);
	// }
	// });
}

function submitOrderForm(btn) {
	if (currentOrder) {
		showPayDialog(currentOrder);
		$(btn).html("买单").prop("disabled", false);
		return;
	}
	var orderForm = {};
	orderForm.customer_id = currentCustomer.CUSTOMER_ID;
	orderForm.distribution_mode_id = DISTRIBUTION_MODE_ID;
	orderForm.distribution_date = deliveryDay || null;
	orderForm.distribution_time_id = deliveryTime || null;
	orderForm.delivery_point_id = deliveryPoint || null;

	if (currentModeDiv.find(".coupon-li").is(":visible")) {
		var COUPON_ID = currentModeDiv.find(".coupon-li select").val();
		orderForm.coupon_id = COUPON_ID;
	}
	var USE_ACCOUNT = currentModeDiv.find(".use_account").is(":checked")?1:0;

	orderForm.use_account = USE_ACCOUNT;
	orderForm.order_item_list = new Array();

	var articles = shopCart.getAll();
	for ( var i in articles) {
		var a = articles[i];
		orderForm.order_item_list.push({
			article_id : a.ARTICLE_ID,
			count : a.number
		});
	}
	var orderformStr = JSON.stringify(orderForm);
	loading("body");
	cAjax("wx/saveOrder", {
		orderform : orderformStr
	}, function(result) {
		loaded("body");
		if (result.result == "01") {
			if (result.data.PAYMENT_AMOUNT == 0) {
				paySuccess();
				return true;
			}
			// $(btn).attr("onclick","");
			$(btn).html("请选择支付方式").prop("disabled", false);
			// $(btn).click(function(){
			//				
			// });
			currentOrder = result.data;
			showPayDialog(currentOrder);
		} else {
			alert("保存失败:" + (result.message || result.msg));
			history.go(-1);
			loadDistributionModeDiv(getSelectDate() == null ? ''
					: getSelectDate().day);
			$(btn).prop("disabled", false);
		}
	}, function() {
		loaded("body");
		alert("服务器出错！");
		$(btn).prop("disabled", false);
	});
	// ipt.val(orderformStr);
	// TODO 正在提交订单
	// currentModeDiv.find(".saveorder-form").html(ipt).submit();
}

function showPayDialog(orderResult) {
	$("#payment-dialog").setAjaxParam(orderResult);
	$("#payment-dialog").find(".confrim-payment").unbind();
	$("#payment-dialog").find(".confrim-payment").click(function() {
		payOrder(orderResult.ORDER_ID, this);
	});
	$("#payment-dialog").show();
}

function tangshiPayment() {
	$("#tangshiForm").html("");
	$("#tangshiForm").append(
			createInput("DISTRIBUTION_MODE_ID", DISTRIBUTION_MODE_ID));
	$("#tangshiForm").submit();
}

function createInput(name, value) {
	var ipt = $("<input />");
	ipt.attr("name", name);
	ipt.val(value);
	return ipt;
}

var currentOrder;
function loadDistributionModeDiv(time) {
	currentOrder = null;
	var getUrl = "";
	// 如果模式为2外卖，并且时间不为空
	if (time && DISTRIBUTION_MODE_ID == 2) {
		getUrl = "/article/weixin/listAllArticlesByDistributionId?DISTRIBUTION_MODE_ID="
				+ DISTRIBUTION_MODE_ID + "&DISTRIBUTION_DATE=" + time;
	} else {
		getUrl = "/article/weixin/listAllArticlesByDistributionId?DISTRIBUTION_MODE_ID="
				+ DISTRIBUTION_MODE_ID;
	}
	cAjax("wx/server", {
		p : encodeURI(getUrl)
	},
			function(result) {
				var listArticle = result.list;
				// 初始化商品列表
				articleListMap.clear();
				articlesMap.clear();
				for ( var i in listArticle) {
					var article = listArticle[i];
					if (article.NOT_SUPPLY || article.ISEMPTY) {
						article.ARTICLEFAMILY_ID = -1;
					} else if (DISTRIBUTION_MODE_ID == 2
							&& article.REMAIN_NUMBER <= 0) { // 如果是外卖模式，并且剩余数量小于0
																// 也设置为售完
						article.ARTICLEFAMILY_ID = -1;
					}
					var fam_id = article.ARTICLEFAMILY_ID;
					if (articleListMap.containsKey(fam_id)) {
						articleListMap.get(fam_id).push(article);
					} else {
						var arr = new Array();
						arr.push(article);
						articleListMap.put(fam_id, arr);
					}
					articlesMap.put(article.ARTICLE_ID, article);
				}
				initShopCart();
			});
	var shopc = currentModeDiv.find(".show-shop-cart");
	shopc.unbind();
	shopc.click(function() {
		showShopCartDialog();
	});
}

function appraiseOrder(obj) {
	var id = $(obj).parents(".order-item").data("id");
	location.href = "wx/informationEvaluate?ORDER_ID=" + id;
}
function showShopCartDialog() {
	var items = getShopCartItems();
	for ( var i in items) {
		var item = items[i];
		var cutItem = item.find(".minusNumber").removeAttr("onclick");
		cutItem.click(function() {
			paymentCut(this, function() {
				showShopCartDialog();
			});
		});
	}
	$("#shop-cart-dialog").show();
	if (items.length) {
		var ul = $("<ul></ul>");
		ul.html(items);
		var itemsDom = $("#shop-cart-dialog").find(".shop-cart-items").html(ul).get(0);
		var itemsScroll = new IScroll(itemsDom,{click:needClick()});
	} else {
		$("#shop-cart-dialog").find(".shop-cart-items").html(
				"<p class='empty'>" + "<i class=' icon-shopping-cart'></i>"
						+ "<span>购物车内还没有商品</span>" + "</p>")
	}
}

/**
 * 初始化购物车
 */
function initShopCart() {
	cAjax("wx/listCartDetailByUserId", {
		DISTRIBUTION_MODE_ID : DISTRIBUTION_MODE_ID
	}, function(result) {
		var shopCartList = result.list;
		shopCart && shopCart.clear();
		for ( var i in shopCartList) {
			var shopitem = shopCartList[i];
			var art = articlesMap.get(shopitem.ARTICLE_ID);
			if (!art || art.ISEMPTY || art.NOT_SUPPLY) {
				continue;
			}
			if (DISTRIBUTION_MODE_ID == 2 && art.REMAIN_NUMBER <= 0) { // 如果是外卖，并且剩余数量等于零，不加入购物车
				continue;
			}
			shopCart.add(art, shopitem.QTY);
		}
		initArticleFamily();
		updateAmoutPriview();
	});
}
function initIsc() {
	frameInit();
	setTimeout(function() {
		t_FamListIsc.refresh();
		t_ArtListIsc.refresh();
	}, 200);
}

/**
 * 初始化类别列表
 */
function initArticleFamily() {
	cAjax("wx/server", {
		p : "/articlefamily/weixin/getAllAppArticleFamilys.do"
	}, function(data) {
		var choiceDiv = currentModeDiv.find(".choice-div");
		choiceDiv.hide();
		var articleDiv = currentModeDiv.find(".article-div");
		var famArr = data.list;
		articleDiv.find(".article-family-list").html("");
		articleDiv.find(".article-list").html("");
		// 添加暂时售完类型
		famArr.push({
			ARTICLEFAMILY_ID : "-1",
			LEVEL : "1",
			NAME : "已售罄"
		})
		for ( var i in famArr) {
			var f = famArr[i];
			var fam_item = getTmpl(".art-fam-item");
			fam_item.find(".art-fam-name").html(f.NAME);
			fam_item.attr("data-id", f.ARTICLEFAMILY_ID);

			var articleList = articleListMap.get(f.ARTICLEFAMILY_ID);
			if (articleList) { // 2015年10月10日 添加
				addArtsToArticleListDom(articleList);
				articleDiv.find(".article-family-list").append(fam_item);
				fam_item.click(function() {
					showToArticles(this);
				});
			}
		}
		articleDiv.show();
		currentModeDiv.find(".art-fam-item").first().click();

		initIsc();

		articleDiv.find(".article-list").scroll(
			function(e) {
				var art = null;
				var mH = $(".menuLogo").height();
				$(this).find(".art-item").each(function() {
					var artTop = $(this).offset().top-mH;
					if (artTop <= 0) {
						art = $(this);
					} else {
						return true;
					}
				});
				if(!art){
					return true;
				}
				var famId = art.attr("data-family");
				var curFam = currentModeDiv.find(".article-family-list").find(".active");
				if (curFam.data("id") == famId) {
					return true;
				} else {
					var findFam = currentModeDiv.find(".article-family-list").find(	"[data-id='" + famId + "']");
					findFam.addClass("active");
					curFam.removeClass("active");
				}
			});
	});
}

function showArticleDetails(obj) {
	var artItem = $(obj).parents(".art-item");
	var artId = artItem.data("id");
	showArticleDialogById(artId);
}
function showArticleDialogById(artId, hideNumber) {
	cAjax("wx/server", {
		p : "/article/weixin/getArticleInfo?ARTICLE_ID=" + artId
	}, function(result) {
		var dialog = $("#food-dialog").clone();
		dialog.attr("id", "");
		dialog.find(".dialog-close").click(function() {
			$(this).parents(".custom-dialog").remove();
		});

		dialog.find(".art-item").attr("data-id", artId);
		var artItem = findArtItemById(artId);
		dialog.find(".foodList-content-price").html(
				artItem.find(".numberControl").clone());

		var articleData = result.data;
		articleData.DESCRIPTION = articleData.DESCRIPTION.replace(/\n/g,
				"<br/>");
		if (noticleList.length > 0) {
			var l = Math.floor(Math.random() * noticleList.length);
			articleData.TITLE = noticleList[l].TITLE;
			dialog.find("[data-key='TITLE']").show();
		} else {
			dialog.find("[data-key='TITLE']").hide();
		}
		dialog.setAjaxParam(articleData);
		dialog.appendTo($("body"));
		dialog.show();
	});
}

// 将购物车内的数据显示到支付页面上
function showShopCartToPayTab() {
	var arts = shopCart.getAll();
	var artItemArr = new Array();
	for ( var i in arts) {
		var at = arts[i];
		artItemArr.push(convertArtToPayArtItem(at));
	}
	if (artItemArr.length <= 0) {
		$("#pay-art-list").html("暂时没有需要支付的选项");
	} else {
		$("#pay-art-list").html(artItemArr);
	}
}

function calculatePayCount() {
	var payCountItem = $("#pay-art-count");
	payCountItem.find(".pay-count-price>span").html(shopCart.getTotalPrice());
	var expressPrice = 0; // 配送费 暂时设置成0
	payCountItem.find(".pay-express-price>span").html(expressPrice);
	payCountItem.find(".pay-should-pay>span").html(
			shopCart.getTotalPrice() + expressPrice);
}

function calculateActualPayMent() {
	if ($("#order-pay-div").hasClass("active")) {
		var expressPrice = 0; // 配送费 暂时设置成0
		var discountPrice = 0; // 优惠券 暂时设置成0
		var actualPay = shopCart.getTotalPrice() - expressPrice - discountPrice;
		$("#pay-actual").html("￥" + actualPay);
		return actualPay;
	} else if ($("#self-pay-div").hasClass("active")) {
		var inputMoney = $("#self-pay-money-input").val();
		var discountPrice = 0; // 优惠券 暂时设置成0
		var actualPay = inputMoney - discountPrice;
		$("#self-pay-money-span").html(actualPay);
		return inputMoney;
	}
}

function convertArtToPayArtItem(art) {
	var payArtItem = getTmpl(".pay-art-item");
	payArtItem.find(".pay-art-number").html(art.number);
	payArtItem.find(".pay-art-name").html(art.NAME);
	payArtItem.find(".pay-price").html(art.PRICE);
	payArtItem.find(".pay-unit").html(art.UNIT);
	return payArtItem;
}

function submitPaySelect() {
	var actualPay = calculateActualPayMent();
	$("#do-pay-div").find(".pay-real-price-number").html(actualPay);
	$("#do-pay-money-input").val("消费总额￥" + actualPay + ".00");
	$("#pay-select-div").hide();
	$("#do-pay-div").show();
}

function showPayDetails() {
	$("#pay-select-div").show();
	$("#do-pay-div").hide();
}

function showToArticles(fam_item) {
	var artFamId = $(fam_item).data("id");
	currentModeDiv.find(".art-fam-item.active").removeClass("active");
	$(fam_item).addClass("active");
	var artListDom = currentModeDiv.find(".article-list");
	var art = artListDom.find("[data-family='" + artFamId + "']").eq(0);
	var currentTop = art.position().top;
	t_ArtListIsc.scrollToElement(art.get(0),400,0,1);
}

/**
 * 将商品对象添加到 商品列表显示
 * 
 * @param {Object}
 *            artArr
 */
function addArtsToArticleListDom(artArr) {
	currentModeDiv.find(".article-list").append(convertArtArr2ArtItems(artArr));
	var pW = currentModeDiv.find(".article-list .art-image").width();
	currentModeDiv.find(".article-list .art-item p").width(pW);
	var notImage = currentModeDiv.find(".article-list .not-img").attr("src",
			"img/black-img.png");
	$(".art-image").lazyload({
		 placeholder:"img/article_loading.png",
		 threshold : 1000
	});
}

/**
 * 更新某个商品的数量显示
 * 
 * @param {Object}
 *            art
 */
function updateArtNumber(art) {
	var artItem = findArtItemById(art.ARTICLE_ID);
	artItem.find(".art-number").html(art.number);
	if (art.number > 0) {
		artItem.find(".minusNumber").show();
		artItem.find(".buyNumber").show();
	} else {
		artItem.find(".buyNumber").hide();
		artItem.find(".minusNumber").hide();
	}
}

/**
 * 通过ARTICLE_FAMILY_ID 找到 该节点
 * 
 * @param {Object}
 *            artFamilyId
 */
function findArtFamItemById(artFamilyId) {
	return currentModeDiv.find(".art-fam-item[data-id='" + artFamilyId + "']");
}

/**
 * 通过 ARTICLE_ID 找到该节点
 * 
 * @param {Object}
 *            artId
 */
function findArtItemById(artId) {
	return currentModeDiv.find(".art-item[data-id='" + artId + "']");
}

/**
 * 更新商品总数量已经总价格
 */
function updateAmoutPriview() {
	var totalNumber = shopCart.getTotalNumber();
	var totalPrice = shopCart.getTotalPrice();
	currentModeDiv.find(".art-count").html(totalNumber);
	currentModeDiv.find(".art-total-money").html(totalPrice);
}

/**
 * 购物车类，定义了方法计算价格之类的参数
 */
function ShopCart() {
	var totalPrice = 0;
	var totalNumber = 0;
	var articles = new HashMap();
	var articleFamilyNumberMap = new HashMap();

	this.clear = function() {
		totalPrice = 0;
		totalNumber = 0;
		articles.clear();
		articleFamilyNumberMap.clear();
	}

	this.add = function(art, number) {
		if (!number && number != 0) {
			number = 1;
		}
		articleAddOrCut(art, true, number);
	}
	this.cut = function(art) {
		articleAddOrCut(art, false);
	}
	this.addList = function(artArr) {
		for ( var i in artArr) {
			add(artArr[i]);
		}
	}

	/**
	 * 获得全部商品
	 */
	this.getAll = function() {
		return articles.getValues();
	}

	this.getTotalPrice = function() {
		return totalPrice;
	}
	this.getTotalNumber = function() {
		return totalNumber;
	}

	/**
	 * 根据商品类别的ID, 获得该类别的商品总数
	 */
	this.getArticleFamilyNumber = function(afId) {
		return articleFamilyNumberMap.get(afId);
	}

	function articleAddOrCut(art, isAdd, number) {
		if (currentModeDiv.find(".payment-div:visible").length && currentOrder) {
			toPayment(true);
		}
		currentOrder = null; // 操作购物车 就清空当前订单

		var fmCount = 0;
		if (articleFamilyNumberMap.containsKey(art.ARTICLEFAMILY_ID)) {
			fmCount = articleFamilyNumberMap.get(art.ARTICLEFAMILY_ID);
		}
		if (isAdd) {
			if (number > art.REMAIN_NUMBER) {
				number = art.REMAIN_NUMBER;
			}
			if (!art.number || art.number <= 0) {
				art.number = number;
			} else {
				art.number += number;
			}
			art.REMAIN_NUMBER -= number;
			articles.put(art.ARTICLE_ID, art);
			totalNumber += number;
			totalPrice += Number(art.PRICE) * number;
			fmCount += number;

		} else {
			if (art.number || art.number > 0) {
				art.number--;
				totalNumber--;
				totalPrice -= art.PRICE;
				fmCount--;
				art.REMAIN_NUMBER++;
			}
			if (art.number <= 0) {
				art.number = 0;
				fmCount = 0;
				articles.remove(art.ARTICLE_ID);
			}

		}
		articleFamilyNumberMap.put(art.ARTICLEFAMILY_ID, fmCount);
	}
}

// 支付页面添加
function paymentAdd(artOpeartor) {

	var artItem = $(artOpeartor).parents(".shopcart-item");
	var artId = $(artItem).data("id");
	var art = articlesMap.get(artId);
	if (canAdd(art)) {
		shopCart.add(art);
		findArtItemById(artId).find(".remain-number").html(art.REMAIN_NUMBER);
		addToCartServer(artId, art.number);
		artItem.find(".art-number").html(art.number);
		updateTotalOrder();
		updateArtNumber(art);
		updateAmoutPriview();
	}
}

// 支付页面减少
function paymentCut(artOpeartor, emptyCallback, itemEmptyCallback) {
	var artItem = $(artOpeartor).parents(".shopcart-item");
	var artId = $(artItem).data("id");
	var art = articlesMap.get(artId);
	if (art.number > 0) {
		shopCart.cut(art);
		cutToCartServer(artId, art.number);

		artItem.find(".art-number").html(art.number);
		updateTotalOrder();

		updateArtNumber(art);
		updateAmoutPriview();
	}
	if (art.number == 0) {
		if (itemEmptyCallback) {
			itemEmptyCallback();
		} else {
			artItem.remove();
		}
	}
	if (shopCart.getAll().length == 0) {
		if (emptyCallback) {
			emptyCallback();
		} else {
			history.go(-1);
		}
	}
	findArtItemById(artId).find(".remain-number").html(art.REMAIN_NUMBER); // TODO
																			// update
}

/**
 * 添加商品到购物车
 * 
 * @param {Object}
 *            artOpeartor
 */
function addArticle2ShopCart(artOpeartor) {
	var clickAnt = $("<span>1</span>");
	clickAnt.css({
		fontSize : "18px",
		padding : "0px 10px",
		backgroundColor : "#CDCDCD",
		color : "white",
		borderRadius : "50%",
		zIndex : 99999
	});
	var s = currentModeDiv.find(".art-count").offset();
	var a = $(artOpeartor).offset();
	var speed = 2 + 2 / (s.top - a.top) * 20;
	clickAnt.fly({
		start : {
			left : a.left,
			top : a.top
		},
		end : {
			left : s.left,
			top : s.top,
		},
		autoPlay : true,
		speed : speed,
		vertex_Rtop : a.top - 50,
		onEnd : function() {
			clickAnt.remove();
		}
	});

	var artItem = $(artOpeartor).parents(".art-item");
	artItem.find(".buyNumber").show();
	artItem.find(".minusNumber").show();
	var artId = $(artItem).data("id");
	var art = articlesMap.get(artId);
	if (canAdd(art)) {
		shopCart.add(art);
		artItem.find(".remain-number").html(art.REMAIN_NUMBER);
		artItem.find(".art-number").html(art.number);
		addToCartServer(artId, art.number);
		updateArtNumber(art);
		updateAmoutPriview();
	}
}
function canAdd(art) {
	if (DISTRIBUTION_MODE_ID == 2 && art.REMAIN_NUMBER <= 0) {
		showMessage("最多就这么多啦，不能再买了");
		return false;
	}
	return true;
}

/**
 * 从购物车减少商品
 * 
 * @param {Object}
 *            artOpeartor
 */
function cutArticleFromShopCart(artOpeartor) {
	var artItem = $(artOpeartor).parents(".art-item");
	var artId = artItem.data("id");
	var art = articlesMap.get(artId);
	if (art.number > 0) {
		shopCart.cut(art);
		cutToCartServer(artId, art.number);
		updateArtNumber(art);
		updateAmoutPriview();
		if (art.number <= 0) {
			artItem.find(".buyNumber").hide();
			artItem.find(".minusNumber").hide();
		} else {
			artItem.find(".art-number").html(art.number);
		}
		artItem.find(".remain-number").html(art.REMAIN_NUMBER);
	}
}

/**
 * 添加到服务器购物车
 * 
 * @param {Object}
 *            ARTICLE_ID
 */
function addToCartServer(ARTICLE_ID, NUMBER) {
	updateCartServer(ARTICLE_ID, NUMBER);
}

/**
 * 减少到服务器购物车
 * 
 * @param {Object}
 *            ARTICLE_ID
 */
function cutToCartServer(ARTICLE_ID, NUMBER) {
	updateCartServer(ARTICLE_ID, NUMBER);
}

function updateCartServer(ARTICLE_ID, NUMBER) {
	cAjax("wx/updateToShopcart", {
		ARTICLE_ID : ARTICLE_ID,
		DISTRIBUTION_MODE_ID : DISTRIBUTION_MODE_ID,
		QTY : NUMBER
	}, function(data) {
	});
}

/**
 * 将 articleList 转换为 dom
 * 
 * @param {Object}
 *            artArr
 */
function convertArtArr2ArtItems(artArr) {
	var items = new Array();
	for ( var i in artArr) {
		var art = artArr[i];
		articlesMap.put(art.ARTICLE_ID, art);
		var art_item = getTmpl(".art-item");
		art_item.find(".art-name").html(art.NAME);
		art_item.find(".art-price").html(art.PRICE);
		art_item.find(".art-unit").html(art.UNIT);
		if (art.PHOTOSMALL) {
//			art_item.find(".art-image").attr("src",	IMAGE_SERVICE + art.PHOTOSMALL);
			art_item.find(".art-image").attr("data-original",	IMAGE_SERVICE + art.PHOTOSMALL);
			if (art.PHOTOSMALL) {
				art_item.attr("data-bigimage", IMAGE_SERVICE + art.PHOTOSMALL);
			}
			art_item.find(".art-image").error(function() {
				notPicture(art_item);
			});
		} else {
			notPicture(art_item);
		}
		art_item.attr("data-id", art.ARTICLE_ID);
		art_item.attr("data-family", art.ARTICLEFAMILY_ID);
		if (DISTRIBUTION_MODE_ID == 2 && art.REMAIN_NUMBER <= 0
				&& (!art.number || art.number <= 0)) { // 如果是外卖，并且剩余数量小于等于0，
			art_item.find(".numberControl").html(
					"<span class='article-none'>已售罄</span>");
		} else if (art.ISEMPTY || art.NOT_SUPPLY) {
			art_item.find(".numberControl").html(
					"<span class='article-none'>已售罄</span>");
		} else {
			if (DISTRIBUTION_MODE_ID == 2) { // 如果正常售卖，并且是外卖模式
				var remainNumber = $("<span class='remain-number'>");
				remainNumber.html(art.REMAIN_NUMBER);
				art_item.find(".art-name").html([ art.NAME, remainNumber ]);
			}

			if (art.number > 0) {
				art_item.find(".art-number").show().html(art.number);
				art_item.find(".minusNumber").show();
				art_item.find(".buyNumber").show();
			} else {
				art_item.find(".minusNumber").hide();
				art_item.find(".buyNumber").hide();
			}
		}
		items.push(art_item);
	}
	return items;
}

function notPicture(art_item) {
	art_item.find(".art-image").addClass("not-img").attr("onclick", "");
	art_item.css({
		height : 100
	});
}

function getTmpl(selectorName) {
	return $("#tmpl-div>" + selectorName).clone();
}

/**
 * 检查用户是否还没有绑定手机，如果没有，则提示用户有红包
 */
function newCustomer() {
	cAjax("wx/customerInfo", "", function(info) {
		currentCustomer = info;
		if (!info.BIND_PHONE) {
			newCustomerCoupon();
		}
	});
}
function newCustomerCoupon() {
	cAjax("wx/listNewCoupon", null, function(result) {
		var list = result.list;
		list.length && showNewCustomerModal(list, function() {
			alert("绑定成功，优惠券已经发放到你的帐户，快到我的中心查看吧！");
			location.hash = "#content-tangshi";
		});
	});
}

function showNewCustomerModal(couponList, callback) {
	var dialog = $("#welcome-dialog");
	dialog.show();
	dialog.find(".btn-code").unbind();
	dialog.find(".btn-code").click(function() {
		var inputPhone = dialog.find("[name='PHONE']").val();
		sendMsg(inputPhone, this);
	});
	dialog.find(".dialog-close").unbind();
	dialog.find(".dialog-close").click(function() {
		dialog.hide();
	});
	dialog.find(".btn-submit").unbind();
	dialog.find(".btn-submit").click(function() {
		$(this).prop("disabled", true);
		var that = this;
		var inputPhone = dialog.find("[name='PHONE']").val();
		var inputCode = dialog.find("[name='CODE']").val();
		cAjax("wx/editPhone", {
			CODE : inputCode,
			PHONE : inputPhone
		}, function(result) {
			if (result.result == 01) {
				dialog.hide();
				callback && callback();
			} else {
				dialog.find(".error").html(result.msg);
				dialog.find(".error").parent().show();
				$(that).prop("disabled", false);
			}
		});
	});
}

function payOrder(orderid, btn) {
	var payMode = $("#payment-dialog [name='PAYMENT_MODE_ID']:checked").val();
	switch (payMode) {
	case "1":
		openWechatPay(orderid, btn);
		break;
	}
}

function openWechatPay(orderid, btn) {
	loading("body");
	cAjax("wx/payOrderWx", {
		ORDER_ID : orderid
	}, function(js_api_params) {
		if (js_api_params.NOT_INVENTORY) {
			loaded("body");
			alert(js_api_params.NOT_INVENTORY);
			loadWaimaiArticles(getSelectDate().day);
			history.go(-1);
			return false;
		}
		if (js_api_params.ERROR) {
			loaded("body");
			alert("错误：" + js_api_params.ERROR);
			location.href = location.href;
			return false;
		}
		wx.chooseWXPay({
			timestamp : js_api_params.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
			nonceStr : js_api_params.nonceStr, // 支付签名随机串，不长于 32 位
			"package" : js_api_params.prepay_id, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
			signType : js_api_params.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
			paySign : js_api_params.paySign, // 支付签名
			success : function(res) {
				paySuccess();
			}
		});
		loaded("body");
	}, function() {
		$(btn).html("重新提交");
	});
}
function paySuccess() {
	if (DISTRIBUTION_MODE_ID == 2) {
	} else {
		alert("支付成功"
				+ currentCustomer.TELEPHONE.substr(7) + "，即可消费");
		location.href = "wx/index?i=1#content-information";
	}
}
