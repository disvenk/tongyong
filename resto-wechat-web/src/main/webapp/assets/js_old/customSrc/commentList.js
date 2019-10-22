for (var i = 1; i <= 5; i++) {
	initNumber(i);
}

function initNumber(num) {
	cAjax("wx/server", {
		p: "/appraise/weixin/appraiseCount?LEVEL=" + num
	}, function(result) {
		var count = result.data.COUNT;
		$("#comment-level" + num).html(count);
	})
}


var star = "";
var status = "";
var currentPage = 1;
var showCount = 15;
var nextPage = 1;
var isOver = false;
initAppraise();


function switchStarFilter() {
	if ($("#star-fileter-div").hasClass("Hide")) {
		$("#star-fileter-div").removeClass("Hide");
	} else {
		$("#star-fileter-div").addClass("Hide");
	}
}

function switchStatus(obj) {

}

$("#filter-star>a").click(function() {
	var sr = $(this).data("star");

	if (0 >= parseInt(sr, 10)) {
		return false;
	}
	$(this).parent().find(".on").removeClass("on");
	$(this).addClass("on");
	$("#star-fileter-div").addClass("Hide");
	$("#filter-star-name").html($(this).html());
	star = sr;
	nextPage = 1;
	initAppraise();
});

function initAppraise() {
	loadAppraise(1, 15, true);
}

function loadAppraise(page, count, isclear) {
	if (nextPage <= page) {
		//获取评论列表
		cAjax("wx/listApparise", {
			LEVEL: star,
			STATUS: status,
			currentPage: page,
			showCount: count
		}, function(result) {
			$("#loading-div").hide();
			if (isclear) {
				$("#appraise-ul").html("");
			}
		 	for(var i in result.list){
		 		var apr = result.list[i];
		 		var appItemDom = $("#tmpl-div>.appraise-item").clone();
		 		appItemDom.setAjaxParam(apr);
		 		appItemDom.find(".starLevel").html("");
		 		for(var n=1;n<=5;n++){
		 			if(n<=apr.LEVEL){
		 				appItemDom.find(".starLevel").append("<i class='iconfont star-cur'>&#xe630;</i>");
		 			}else{
		 				appItemDom.find(".starLevel").append("<i class='iconfont'>&#xe630;</i>");
		 			}
		 		}
		 		$("#appraise-ul").append(appItemDom);
		 	}
			nextPage++;
			if (result.list.length >= showCount) {
				currentPage++;
			} else {
				isOver = true;
			}
		});
	}
}

$(document).scroll(function() {
	if ($(this).scrollTop() >= ($(document).height() - $(window).height())) {
		if (isOver) {
			$("#loading-div").html("已经到最底部了").show();
		} else {
			$("#loading-div").html("正在加载...").show();
			loadAppraise(currentPage, showCount, false);
		}
	}
});