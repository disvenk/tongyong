<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
	<link rel="stylesheet" type="text/css" href="assets/global/plugins/bootstrap-calendar-master/css/calendar.css">
	<style>
		.freeday{
			background: #FFF0F0;
			font-weight: bold;
		}
		.freeday:after{
			content: '休';
			background-color: red;
			color:white;
			padding: 2px;
			margin-top: 10px;
			margin-left: 10px;
		}
		.cal-day-weekend span[data-cal-date] {
		  color: darkred;
		  font-weight: bold;
		}
	</style>
	<div class="page-header">
		<div class="pull-right form-inline" style="margin-right:30px;">
			<div class="btn-group">
				<button class="btn btn-danger" onclick="setMonthWeekend()">将本月周末设置为休息日</button>
				<button class="btn btn-info" onclick="setYearWeekend()">将本年周末设置为休息日</button>
			</div>
			<div class="btn-group">
				<button class="btn btn-primary" data-calendar-nav="prev">&lt;&lt; 向前</button>
				<button class="btn" data-calendar-nav="today">今天</button>
				<button class="btn btn-primary" data-calendar-nav="next">向后  &gt;&gt;</button>
			</div>
			<div class="btn-group">
				<button class="btn btn-warning" data-calendar-view="year">年视图</button>
				<button class="btn btn-warning active" data-calendar-view="month">月视图</button>
			</div>
		</div>

		<h1 id="year-month">20xx年xx月</h1>
		<small>点击日期切换工作日/非工作日</small>
	</div>
	<div id="calendar"></div>
	
	<script src="assets/global/plugins/bootstrap-calendar-master/components/underscore/underscore-min.js" type="text/javascript"></script>
    <script src="assets/global/plugins/bootstrap-calendar-master/js/calendar.js" type="text/javascript"></script>
    <script src="assets/global/plugins/bootstrap-calendar-master/js/language/zh-CN.js" type="text/javascript"></script>
        
	<script type="text/javascript">
		/* if (top.hangge) {
			$(top.hangge());
		} 
		
		$.ajaxSetup({
			beforeSend:function(){
				top.jzts();
				return true;
			},
			complete:function(){
				top.hangge();
			}
		});  */
		
	    var currentView;
	    var calendar = $("#calendar").calendar(
        {
            tmpl_path: "assets/global/plugins/bootstrap-calendar-master/tmpls/",
            events_source: function () { 
				return [];
            },
            language:"zh-CN",
            onAfterViewLoad:function(events){
            	currentView = events;
            	showTitle(currentView);
            	if(calendar){
            		bindDay();
            	}
            }
        });    
	    
	     $('.btn-group button[data-calendar-nav]').each(function() {
			var $this = $(this);
			$this.click(function() {
				calendar.navigate($this.data('calendar-nav'));
			});
		}); 

		$('.btn-group button[data-calendar-view]').each(function() {
			var $this = $(this);
			$this.click(function() {
				calendar.view($this.data('calendar-view'));
			});
		});
		
	 	function bindDay(){
			$(".cal-day-inmonth").click(function(){
				var date = $(this).find("[data-cal-date]").data("cal-date");
				if($(this).hasClass("freeday")){
					removeFreeDay(date);
				}else{
					addFreeDay(date);
				}
			});
		} 
		
		function init(){
			showTitle(currentView);
		    calendar.options.views.day.enable=0;
			bindDay(); 
		}
		
		$(document).ready(function(){
			init();
		});
		
		 function freeDayList(begin,end,callback){
			$.ajax({
				type:"post",
				url:"freeday/freeDayList?begin="+begin+"&end="+end,
				async:true,
				success:function(result){
					if(callback){
						var str = [];
						$(result.data).each(function(i,item){
							var freeDay_temp = new Date(item.freeDay).format("yyyy-MM-dd");
							var temp = {"freeDay":freeDay_temp};
							str.push(temp);
						})
						callback(str);
					}
				}
			});	
		} 
		
		 function removeFreeDay(date){
			$.ajax({
				type:"post",
				url:"freeday/removeFreeDay?freeDay="+date,
				async:true,
				success:function(success){
					findDateDom(date).parent().removeClass("freeday");
				},
				error:function(){
					alert("删除失败---------");
				}
			});
		} 
		
		function addFreeDay(date){
			$.ajax({
				type:"post",
				url:"freeday/addFreeDay?freeDay="+date,
				async:true,
				success:function(result){
					if(result.success){
						var dom = findDateDom(date);
						if(!dom.parent().hasClass("freeday")){
							dom.parent().addClass("freeday");
						}
					}
				},
				error:function(){
					alert("添加失败-------");
				}
			});
		} 
		
		 function showTitle(view){
			if(calendar){
				$("#year-month").html(calendar.getTitle());
				if(view=="month"){
					var begin = $(".cal-day-inmonth").first().find("[data-cal-date]").data("cal-date");
					var end = $(".cal-day-inmonth").last().find("[data-cal-date]").data("cal-date");
					freeDayList(begin,end,function(result){
						for(var i in result){
							var date = result[i];
							var dom = findDateDom(date.freeDay);
							if(!dom.parent().hasClass("freeday")){
								dom.parent().addClass("freeday");
							}
						}
					});
				}
			}
		} 
		
		 function findDateDom(date){
			return $("[data-cal-date='"+date+"']");
		}; 
		
		function setMonthWeekend(){
			var date = calendar.options.position.start;
			var dateStr = date.getFullYear()+"-"+date.getMonthFormatted()+"-01";
			$.ajax({
				type:"post",
				url:"freeday/setMonthWeekend?freeDay="+dateStr,
				async:true,
				success:function(success){
					showTitle(currentView);
				},
				error:function(){
					alert("添加失败");
				}
			});
		} 
		
		 function setYearWeekend(){
			var date = calendar.options.position.start;
			var dateStr = date.getFullYear()+"-"+date.getMonthFormatted()+"-01";
			$.ajax({
				type:"post",
				url:"freeday/setYearWeekend?freeDay="+dateStr,
				async:true,
				success:function(success){
					showTitle(currentView);
				},
				error:function(){
					alert("添加失败");
				}
			});
		} 
	</script>
