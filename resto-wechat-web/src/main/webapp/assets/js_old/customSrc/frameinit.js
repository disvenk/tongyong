function hmenu(changeHash) {
	if (changeHash) {
		if(location.hash.indexOf(".hidem")>0){
			return true;
		}else{
			location.hash = location.hash + ".hidem";
		}
	} else {
		$("#ul-menu").hide();
	}
}

function ismenushow() {
	return $("#ul-menu").is(":visible");
}

function smenu() {
	$("#ul-menu").show();
}

// 界面初始化
var homeIsc, t_FamListIsc, t_ArtListIsc, w_FamListIsc, w_ArtListIsc, informationIsc, storeListIsc, t_PayListIsc, homeIsc;
function load() {
	homeIsc.refresh();
}

var MINLEVEL=4;
var MAXLEVEL=5;
var star = "";
var status = "";
var currentPage = 1;
var showCount = 3;
var nextPage = 1;
var isOver = false;
var isLoad = false;


$(function() {

	frameInit();
	
	homeIsc = new IScroll("#content-home", {
		probeType : 2,
		click:needClick()
	});
	informationIsc = new IScroll("#content-information", {
		bounce : false,
		click:needClick()
	});

	t_FamListIsc = new IScroll($("#content-tangshi").find(".family-list-div")
			.get(0), {
		bounce : false,
		click:needClick()
	});

	t_ArtListIsc = new IScroll($("#content-tangshi").find(
			".article-list-wapper").get(0), {
		probeType : 1,
		click:needClick(),
	});
	t_ArtListIsc.on("scrollEnd", function() {
		$(".article-list").trigger("scroll");
	});
	
//	w_FamListIsc = new IScroll($("#content-waimai")
//			.get(0), {
//		bounce : false,
//		click:needClick()
//	});
//
//	w_ArtListIsc = new IScroll($("#content-waimai")
//			.find(".article-list-wapper").get(0), {
//		probeType : 1,
//		click:needClick()
//	});
//	w_ArtListIsc.on("scrollEnd", function() {
//		$(".article-list").trigger("scroll");
//	});
	t_PayListIsc = new IScroll($("#content-tangshi").find(".payment-div")
			.get(0), {
	// 
		click:needClick()
	});
//	w_PayListIsc = new IScroll(
//			$("#content-waimai").find(".payment-div").get(0), {
//			// 
//				click:needClick()
//			});

	initIsc();

	homeIsc.on("scroll", function() {
		if (homeIsc.y > homeIsc.maxScrollY) {
			homeIsc.refresh();
			return false;
		}
		if (isOver) {
			$("#loading-div").html("已经是全部评论啦！").show();
			homeIsc.refresh();
			homeIsc.off("scroll");
		} else {
			$("#loading-div").html("正在加载...").show();
			loadAppraise(currentPage, showCount, false);
			isLoad = true;
		}
	});
	
	$("#loading-div").html("正在加载...").show();
	loadAppraise(currentPage, showCount, false);

	document.addEventListener('touchmove', function(e) {
		e.preventDefault();
	}, false);
});
function loadAppraise(page, count, isclear) {
	if (isLoad) {
		return false;
	}
	if (nextPage <= page) {
		cAjax("wx/listApparise", {
			LEVEL : star,
			STATUS : status,
			currentPage : page,
			showCount : count,
			MINLEVEL:MINLEVEL,
			MAXLEVEL:MAXLEVEL
		}, function(result) {
			$("#loading-div").hide();
			if (isclear) {
				$("#appraise-ul").html("");
			}
			for ( var i in result.list) {
				var apr = result.list[i];
				if (apr.NICKNAME.match(/\w+/g) && apr.NICKNAME.length > 6) {
					apr.NICKNAME = apr.NICKNAME.substr(0, 6) + "...";
				} else if (apr.NICKNAME.length > 3) {
					apr.NICKNAME = apr.NICKNAME.substr(0, 3) + "...";
				}
				var appItemDom = $("#tmpl-div>.appraise-item").clone();
				appItemDom.attr("data-articleid",apr.ARTICLE_ID);
				if(apr.LEVEL<=3){ //差评处理
					apr.TYPE="["+apr.FEEDBACK+"]";
				}
				appItemDom.setAjaxParam(apr);
				appItemDom.find(".starLevel").html("");
				for ( var n = 1; n <= 5; n++) {
					if (n <= apr.LEVEL) {
						appItemDom.find(".starLevel").append(
								"<i class='icon-star'></i>");
					} else {
						appItemDom.find(".starLevel").append(
								"<i class='icon-star-empty'></i>");
					}
				}
				if (!apr.PICTURE_URL) {
					appItemDom.find(".reviewListPhotoBox").addClass(
							"noImage");
				}
				appItemDom.click(function(){
					var articleId = $(this).data("articleid");
					if(articleId){
						showArticleDialogById(articleId,true);
					}
				});
				$("#appraise-ul").append(appItemDom);
			}
			nextPage++;
			if (result.list.length >= showCount) {
				currentPage++;
			} else {
				isOver = true;
			}
			homeIsc.refresh();
			isLoad = false;
		});
	}
}
var beginClick = false;

function frameInit() {
	var hBottomFxied = $(".bottom-fxied").height();
	var hWindow = $(window).height();
	var hContent = hWindow - hBottomFxied;
	$("#content-div").height(hContent);
	var hCart = currentModeDiv.find(".menu-cart").outerHeight();
	var hMenuLogo = currentModeDiv.find('.menuLogo').outerHeight();
	var hAddress = currentModeDiv.find(".waimai-address").outerHeight();
	var hArticleList = hContent - hMenuLogo - hAddress;
	var hFamilyList = hArticleList - hCart;
	currentModeDiv.find(".family-list-div").height(hFamilyList);
	currentModeDiv.find(".article-list-wapper").height(hArticleList);
	$(".payment-div").height(hWindow - 42);
	
}

// 获取微信js-jdk配置
// var h = window.location.href;
// $.ajax({
// type : "get",
// url : "wx/wechat/weChatJsConfigHomePage",
// data : {
// href : h
// },
// async : true,
// success : function(jsonResult) {
// wx.config(jsonResult.data);
// }
// });

// 设置首页分享内容
function setHomePageShare(callback, successcallback) {
	cAjax("wx/getWeChatPromotionLink", null, function(result) {
		console.log(result);
		wxShareToMoments(result.title, result.link, result.imgUrl,
				successcallback, successcallback);
		if (callback) {
			callback();
		}
	});
}

//waimaiMap = new initMap("waimai-map", 31.239776, 121.499682, "我的位置", "当前所在地址");
// tangshiMap = new initMap("tangshi-map",31.239776,121.499682,"我的位置","当前所在地址");
function wxShareToMomentsBtn() {
	setHomePageShare(function() {
		$("#share-wechat-dialog").show();
	}, function() {
		$("#share-wechat-dialog").hide();
	});
}

// 调用腾讯地图
// 初始化地图函数 自定义函数名init
function initMap(id, lat, lng, name, addr) {
	var currentMaker;
	// var map = new AMap.Map(id, {
	// view : new AMap.View2D({
	// center : new AMap.LngLat(lng, lat),
	// zoom : 17
	// })
	// }); 
	var x = lng;
	var y = lat;
	var ggPoint = new BMap.Point(x, y);
//	var myIcon = new BMap.Icon("http://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25),{
//		 offset: new BMap.Size(10, 25), // 指定定位位置  
//         imageOffset: new BMap.Size(0,12) // 设置图片偏移  
//	});
	var bm = new BMap.Map(id);
	bm.centerAndZoom(ggPoint, 17);
	bm.addControl(new BMap.NavigationControl());
	// 坐标转换完之后的回调函数
	translateCallback = function(data) {
		if (data.status === 0) {
			var marker = new BMap.Marker(data.points[0]); 
			bm.addOverlay(marker);
			var point = data.points[0];
			bm.setCenter(point);
			bm.panTo(new BMap.Point(point.lng,point.lat));
			marker.setAnimation(BMAP_ANIMATION_DROP);
		}
	}

	
	var convertor = new BMap.Convertor();
	var pointArr = [];
	pointArr.push(ggPoint);
	convertor.translate(pointArr, 3, 5, translateCallback);
	var custMarker;
	this.toLocation = function(lat, lng) {
		var toPoint = new BMap.Point(lng, lat);
		var myIcon = new BMap.Icon("http://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25), {  
            offset: new BMap.Size(10, 25), // 指定定位位置  
            imageOffset: new BMap.Size(0, 0 - 10 * 25) // 设置图片偏移  
        });  
		if(custMarker){
			custMarker.hide();
			custMarker = new  BMap.Marker(toPoint,{icon:myIcon}); 
			bm.addOverlay(custMarker);
			bm.panTo(toPoint);
		}else{
			custMarker = new  BMap.Marker(toPoint,{icon:myIcon}); 
			bm.addOverlay(custMarker);
			bm.panTo(toPoint);
		}
	}
}
