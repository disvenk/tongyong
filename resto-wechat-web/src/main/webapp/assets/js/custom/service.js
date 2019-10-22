function getPicUrl(pic){
	if(pic){
		return IMAGE_SERVICE +pic;
	}else{
		return 'assets/img/resto.png';
	}
}
function getOrderList(option,s){
	$.ajax({
		url:"order/listOrder",
		data:option,
		success:function(result){
			s&&s(result.data);
		}
	});
}

function getOrderById(id,s){
	$.post("order/findOrderById",{id:id},function(result){
		s&&s(result.data);
	});
}

/**
 * 获得用户最新订单 或者 指定ID的订单
 */
function getCustomerNewOrder(oid,sbk){
	$.post("order/customerNewOrder",{orderId:oid},function(result){
		sbk&&sbk(result.data);
	});
}
function getCustomerNewPackage(sbk){
	$.post("order/customerNewPackage",null,function(result){
		sbk&&sbk(result.data);
	});
}

function findReadyOrderList(oid,sbk){
	$.post("order/readylistOrder",{orderId:(oid||'')},function(result){
		sbk&&sbk(result.data);
	});
}

function getShopList(sbk){
	$.post("shop/list",null,function(result){
		sbk&&sbk(result.data);
	});
}

/**
 * 获取当前店铺信息
 */
function getShopInfo(successBck) {
	$.ajax({
		url : "shop/currentshop",
		success : function(result) {
			successBck && successBck(result.data);
		}
	});
}

/**
 * 通过就餐模式获取购物车列表 获得当前店铺，当前用户的购物车信息
 */
function loadShopCart(distributionModeId, successbck) {
	$.ajax({
		url : "shop/listShopcart",
		data : {
			distributionModeId: distributionModeId,
		},
		success : function(result) {
			var shopCartList = result.data;
			successbck && successbck(shopCartList);
		}
	});
}

/**
 * 获取店铺的照片展示图片列表
 */
function getShowPhoto(sbk){
	$.post("shop/listShowPhoto",null,function(result){
		sbk&&sbk(result.data);
	});
}

/**
 * 查询店铺的广告信息
 */
function getShopAdvert(successBck) {
	$.ajax({
		url : "shop/shopAdvert",
		success : function(result) {
			var advert = result.data[0];
			if(advert){
				advert.description = advert.description.replace(/\.\.\/upload/g,IMAGE_SERVICE+"upload");
			}
			successBck && successBck(advert||{});
		}
	});
}

/**
 * 查询店铺通知列表 
 */
function getNoticeList(type,successBck) {
	$.ajax({
		url : "shop/noticeList",
		data:{noticeType:type},
		success : function(result) {
			var list = result.data;
			for(var i=0;i<list.length;i++){
				var notice = list[i];
				notice.noticeImage = getPicUrl( notice.noticeImage);
			}
			successBck && successBck(result.data);
		}
	})
}

function addNoticeHistory(id,sbk){
	$.post("shop/addNoticeHistory",{noticeId:id},function(result){
		sbk&&sbk(result);
	})
}

function switchShopApi(sid,sbk){
	$.post("shop/switch/"+sid,null,function(result){
		sbk&&sbk(result);
	});
}


/**
 * 更新购物车接口
 */
function updateShopCart(ARTICLE_ID, DISTRIBUTION_MODE_ID, NUMBER,sbk) {
	$.ajax({
		url : "shop/updateToShopcart",
		data : {
			articleId : ARTICLE_ID,
			distributionModeId: DISTRIBUTION_MODE_ID,
			number: NUMBER
		},
		success:function(result){
			sbk&&sbk(result);
		}
	});
}

/**
 * 更新购物车接口
 */
function updateShopCart2(ARTICLE_ID, DISTRIBUTION_MODE_ID, NUMBER,sbk) {
	$.ajax({
		url : "shop/updateToShopcart2",
		data : {
			articleId : ARTICLE_ID,
			distributionModeId: DISTRIBUTION_MODE_ID,
			number: NUMBER
		},
		success:function(result){
			sbk&&sbk(result);
		}
	});
}

/**
 * 清空购物车接口
 */
function clearShopcartApi(sbk){
	$.post("shop/emptyShopCart",null,function(result){
		sbk&&sbk(result);
	});
}

/**
 * 获取订单状态
 */
function getOrderStates(oid,sbk){
	$.post("order/getOrderStates",{orderId:oid},function(result){
		var order = result.data;
		sbk&&sbk(order);
	});
}

/**
 * 查询 店铺评论总数
 */
function getAppraiseCount(scbk) {
	$.ajax({
		url : "appraise/appraiseCount",
		success : function(result) {
			scbk && scbk(result.data.appraiseCount, result.data.appraiseMonthCount);
		}
	})
}

/**
 * 获取客户所有的优惠卷，通过状态和模式
 */
function getCouponList(modeId, sbc) {
	$.ajax({
		url : "customer/listCoupon",
		data : {
			isUsed : 0,
			distributionModeId : modeId
		},
		success : function(result) {
			var couponList = result.data;
			sbc && sbc(couponList);
		}
	})
}

/**
 * 获取所有的菜品属性
 */
function getAllAttr(cbk) {
	$.ajax({
		url : "article/articleAttrList",
		success : function(result) {
			cbk && cbk(result.data);
		}
	});
}


/**
 * 查询首页图片列表
 */
function getHomePicture(scbk) {
	$.ajax({
		url : "shop/pictureslider",
		data:{
			phoneNo:1
		},
		success : function(result) {
			//(result.data[2].pictureUrl);
			for ( var i in result.data) {
				var pic = result.data[i];
					pic.pictureUrl = getPicUrl(pic.pictureUrl);
			}
			scbk && scbk(result.data);
		}
	});
}

/**
 * 查询评论列表 通过基本参数查询 
 */
function getAppraiseList(successCbk, options) {
	var defaultOptions = {
		maxLevel : 5,
		minLevel : 4,
		currentPage : 1,
		showCount : 3,
	};
	options = $.extend(defaultOptions, options);
	$.ajax({
		url : "appraise/listAppraise",
		data : options,
		success : function(result) {
			for ( var i in result.data) {
				var app = result.data[i];
				app.pictureUrl = getPicUrl(app.pictureUrl);
				app.createTime = new Date(app.createTime)
						.format("MM-dd hh:mm");
			}
			successCbk && successCbk(result.data);
		}
	});
}

/**
 * 查询餐品列表
 */
function getArticleFamily(scbk, option) {
	$.ajax({
		url : "article/articleFamilyList",
		data : {
			distributionModeId: option.modeid
		},
		success : function(result) {

			var allFamilyList = result.data;
			var emptyFamily = {
					id:"-99999999",
					name:"已售罄",
					peference:9999
				};
			allFamilyList.push(emptyFamily);
			var familyMap = {};
			for ( var i in allFamilyList) {
				var f = allFamilyList[i];
				f.articles = [];
				familyMap[f.id] = f;
			}
			
			$.ajax({
				url : "article/articleList",
				data : {
					distributionModeId: option.modeid
				},
				success : function(result) {

					var allAttr = option.attr;
					var articleList = result.data;
					for ( var i in articleList) {

						var a = articleList[i];

						a.photoSmall = getPicUrl(a.photoSmall);
						a.description&&(a.description = a.description.replace(/\n/g,"<br/>"));
						a.realPrice = a.fansPrice || a.price;
						a.number = 0;
						if(a.isEmpty){
							emptyFamily.articles.push(a);
							a.articlePrices=[];
							continue;
						}
						if(a.articleType==1){
							a.type = 1;
							a.hasUnit = [];
							if (a.articlePrices.length > 0) {
								for ( var n in a.articlePrices) {
									var unit = a.articlePrices[n];
									unit.number = 0;
									unit.article = a;
									unit.type = 2;
									unit.realPrice = unit.fansPrice
									|| unit.price;
									unit.ids = [];
									var ids = unit.unitIds;
									if (ids.indexOf(",") > -1) {
										ids = ids.split(",");
										for ( var d in ids) {
											a.hasUnit.push(ids[d]);
										}
										unit.ids = ids;
									} else {
										unit.ids.push(ids);
										a.hasUnit.push(ids);
									}
								} 
							}
						}else if(a.articleType==2){
							for(var j in a.mealAttrs){
								var attr = a.mealAttrs[j];
								var hasDefault = false;
								for(var n in attr.mealItems){
									var item = attr.mealItems[n];
									if(item.isDefault){
										attr.currentItem=[];
										attr.currentItem[0] = item;
										hasDefault=true;
										item.click =true;
									}else{
										item.click = false;
									}

									item.photoSmall = getPicUrl(item.photoSmall);
								}
								if(!hasDefault){
									if(attr.mealItems != null && attr.mealItems.length > 0 ){
										attr.currentItem = [];
										attr.currentItem[0] = attr.mealItems[0];
										attr.mealItems[0].click = true;
									}

								}
							}
						}
						var f = familyMap[a.articleFamilyId];
						if (f) {
							f.articles.push(a);
						}
					}
					scbk && scbk(allFamilyList);
				}
			})

		},
	})
}

/**
 * 从数据库获取当前用户信息 
 */
function getCustomer(sbc) {
	$.ajax({
		url : "customer/info",
		success : function(user) {
			sbc && sbc(user.data);
		}
	});
}

/**
 * 保存评论
 */
function saveOrderAppraise(appraise,sbk){
	$.post("appraise/saveAppraise",appraise,function(result){
		sbk&&sbk(result);
	});
}


/**
 * 根据优惠券类型查询对应的优惠券
 */
function showCouponList(couponType,sbk){
	$.post("customer/showCoupon",couponType,function(result){
		sbk&&sbk(result);
	});
}

/**
 * 发送手机验证码
 */
function sendCode(phone, successCbk) {
	$.ajax({
		url : "customer/sendCodeMsg",
		data : {
			phone : phone
		},
		success : function(result) {
			successCbk && successCbk(result);
		}
	});
}

/**
 * 编辑用户手机信息
 */
function editPhone(phone, code, couponType, successCbk) {
	$.ajax({
		url : "customer/editPhone",
		data : {
			code : code,
			phone : phone,
			couponType : couponType,
		},
		success : function(result) {
			successCbk && successCbk(result);
		}
	})
}

function saveOrderForm(orderForm, sbk,error) {
	$.ajax({
		url : "order/saveOrder",
		contentType : "application/json",
		type : "post",
		data : JSON.stringify(orderForm),
		success : function(result) {
			sbk && sbk(result.data.orderItems);
		},
		error:function(a,b,c,d){

			error&&error(a,b,c,d);
		}
	});
}

function openWechatPay(orderid, successCbk, errorCbk,cancelCbk) {
	$.ajax({
		url : "order/payOrderWx",
		data : {
			orderId : orderid
		},
		success : function(result) {
			if(result.success){
				var js_api_params = result.data;
				console.log(js_api_params);
				wx.chooseWXPay({
					timestamp : js_api_params.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
					nonceStr : js_api_params.nonceStr, // 支付签名随机串，不长于 32 位
					"package" : js_api_params["package"], // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
					signType : js_api_params.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
					paySign : js_api_params.paySign, // 支付签名
					success : function(res) {
						successCbk && successCbk();
					},
					cancel:function(res){
						cancelCbk&&cancelCbk();
					},
					fail:function(res){
						alert("fail:"+res.errMsg);
					},
					error:function(res){
						alert("error"+res);
					}
				});
			}else{
				errorCbk && errorCbk(result.message);
			}
			
		}
	});
}

/**
 * 取消订单
 */
function cancelOrderRequest(oid,sbk){
	$.ajax({
		url:"order/cancelOrder",
		data:{orderId : oid},
		success:function(result){
			sbk&&sbk(result);
		}
	});
}

/**
 * 确认下单
 */
function pushOrderRequest(oid,sbk){
	$.ajax({
		url:"order/pushOrder",
		data:{orderId : oid},
		success:function(result){
			sbk&&sbk(result);
		}
	});
}


/**
 * 获取充值列表
 */
function getChargeList(sbk){
	$.post("charge/rulesList",null,function(result){
		var rules = result.data;
		sbk&&sbk(rules);
	});
}

/**
 * 生成微信充值支付请求
 */
function chargeWxRequest(charge_id,successCallback,cancelCbk){
	$.post("charge/chargeWx",{settingId:charge_id},function(result){
		if(result.success){
			var js_api_params = result.data;
			wx.chooseWXPay({
				timestamp : js_api_params.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
				nonceStr : js_api_params.nonceStr, // 支付签名随机串，不长于 32 位
				"package" : js_api_params["package"], // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
				signType : js_api_params.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
				paySign : js_api_params.paySign, // 支付签名
				success : function(res) {
					successCallback&&successCallback();
				},
				cancel:function(){
					cancelCbk&&cancelCbk();
				},
				fail:function(res){
					alert("失败"+res.errMsg);
				}
			});
		}else{
			alert(result.message);
		}
		
	});
}

function setTableNumber(oid,tnumber,cbk){
	$.post("order/settable",{orderId:oid,tableNumber:tnumber},function(result){
		cbk&&cbk(result);
	});
}

function scanTableNumber(sbk){
	wx.scanQRCode({
	    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
	    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
	    success: function (res) {
	    	var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
	    	sbk&&sbk(result);
	    }
	});
}
	
/**
 * 测试清空购物车
*/
	
function clearAllShopCart(sbk){
	$.post("shop/emptyShopCart",function(result){
		cbk&&cbk(result);
	});
}	

/**
 * 测试解绑手机号码
 */
function unbindPhone(sbk){
	$.post("customer/unBindPhone",function(result){
		cbk&&cbk(result);
	});
}

function updateNewNoticeTime(cbk){
	$.post("customer/updateNewNoticeTime",function(result){
		cbk&&cbk(result);
	});
}

function getShareDetailed(aid,cbk){
	$.post("appraise/getShareDetailed",{appraiseId:aid},function(result){
		var data = result.data;
		var appraise = data.appraise;
		var setting = data.setting;
		if(appraise){
			appraise.pictureUrl = getPicUrl(appraise.pictureUrl);
		}
		if(setting){
			setting.shareIcon = getPicUrl(setting.shareIcon);
		}
		cbk&&cbk(data);
	});
}
	
function getRewardDetailed(sbk){
	$.post("appraise/getRewardDetailed",null,function(result){
		sbk&&sbk(result.data);
	})
}

