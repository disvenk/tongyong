for(var i=1;i<=5;i++){initNumber(i)}function initNumber(i){cAjax("wx/server",{p:"/appraise/weixin/appraiseCount?LEVEL="+i},function(e){var a=e.data.COUNT;$("#comment-level"+i).html(a)})}var star="";var status="";var currentPage=1;var showCount=15;var nextPage=1;var isOver=false;initAppraise();function switchStarFilter(){if($("#star-fileter-div").hasClass("Hide")){$("#star-fileter-div").removeClass("Hide")}else{$("#star-fileter-div").addClass("Hide")}}function switchStatus(i){}$("#filter-star>a").click(function(){var i=$(this).data("star");if(0>=parseInt(i,10)){return false}$(this).parent().find(".on").removeClass("on");$(this).addClass("on");$("#star-fileter-div").addClass("Hide");$("#filter-star-name").html($(this).html());star=i;nextPage=1;initAppraise()});function initAppraise(){loadAppraise(1,15,true)}function loadAppraise(i,e,a){if(nextPage<=i){cAjax("wx/listApparise",{LEVEL:star,STATUS:status,currentPage:i,showCount:e},function(i){$("#loading-div").hide();if(a){$("#appraise-ul").html("")}for(var e in i.list){var t=i.list[e];var s=$("#tmpl-div>.appraise-item").clone();s.setAjaxParam(t);s.find(".starLevel").html("");for(var r=1;r<=5;r++){if(r<=t.LEVEL){s.find(".starLevel").append("<i class='iconfont star-cur'>&#xe630;</i>")}else{s.find(".starLevel").append("<i class='iconfont'>&#xe630;</i>")}}$("#appraise-ul").append(s)}nextPage++;if(i.list.length>=showCount){currentPage++}else{isOver=true}})}}$(document).scroll(function(){if($(this).scrollTop()>=$(document).height()-$(window).height()){if(isOver){$("#loading-div").html("已经到最底部了").show()}else{$("#loading-div").html("正在加载...").show();loadAppraise(currentPage,showCount,false)}}});