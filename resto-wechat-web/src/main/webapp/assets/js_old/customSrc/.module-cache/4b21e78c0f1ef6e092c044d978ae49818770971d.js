//src
//babel --presets react F:\yunpan\Chintec\svnRepository\Dev\sourceCode\trunk\RPWX\src\main\webapp\js\reactSrc\reactOrderInit.js --watch -o F:\yunpan\Chintec\svnRepository\Dev\sourceCode\trunk\RPWX\src\main\webapp\js\custom\reactOrderInit.js

function informationInit() {
	cAjax("wx/customerInfo", "", function(info) {
		
		$("#content-information").setAjaxParam(info);
		var orders = new Array();
		var n = 0;
		$("*[data-state]").hide().each(function(index) {
			var state = $(this).data("state");
			var that = $(this);
			cAjax("wx/listOrder", {
				ORDER_STATE: state
			}, function(result) {
				for (var i in result.list) {
					orders.push(result.list[i]);
				}
				var l = result.list.length;
				if (l) { 
					that.show().html(l);
				}
				if (n++ == 2) { 
					showOrder(orders);
				}
			});
		});
	});
	
}
informationInit();

function showOrder(orders) {
	orders.sort(function (a,b){
		return a.ORDER_TIME>b.ORDER_TIME?-1:1;
	});
	
	var SUBMIT = 1;			
	var stateName = {
		1:"支付",
		2:"消费码",
		10:"评价"
	};
	
	var OrderList = React.createClass({displayName: "OrderList",
	  getInitialState: function() {
	    return {orderlist:orders};
	  },
	  createItem:function(o){
	  	var MODE_NAME= o.DISTRIBUTION_MODE_ID==1?"堂吃":"外卖";
	  	var RES_NAME = o.SHOP_DES;
	  	if(o.DISTRIBUTION_MODE_ID==2){
	  		RES_NAME = o.DELIVERY_POINT_NAME;
	  	} 
	  	var time = new Date(o.ORDER_TIME).format("yyyy-MM-dd");
	  	var sName = stateName[o.ORDER_STATE];
	  	return (
	      React.createElement("li", {key: o.ORDER_ID, className: "myOrderList"}, 
				React.createElement("a", {href: 'wx/informationOrder#'+o.ORDER_STATE}, 
	                React.createElement("span", {className: "text-main myOrderTitle"}, 
		                "[", React.createElement("span", null, MODE_NAME), "]", 
		                	RES_NAME, 
						React.createElement("br", null), 
						React.createElement("span", {className: "myOrderTitleInfo"}, time, React.createElement("span", {className: "priceText"}, o.PAYMENT_AMOUNT))
	                ), 
	                React.createElement("span", {className: "text-right red"}, sName), 
	                React.createElement("i", {className: "iconfont"}, "")
	            )
            )
	    );
	  },
	  render: function() {
	    return (React.createElement("ul", {className: "navList notIcon spaceMb20 myOrder"}, 
	    	React.createElement("li", null, 
				React.createElement("a", {href: "wx/informationOrder"}, 
	                React.createElement("i", {className: "mn iconfont"}, ""), 
	                React.createElement("span", {className: "text-main"}, "我的订单"), 
	                React.createElement("span", {className: "text-right graytext"}, "全部"), 
	                React.createElement("i", {className: "iconfont"}, "")
	            )
            ), 
	    this.state.orderlist.map(this.createItem)));
	  }
	});
	var nodeDom = ReactDOM.render(React.createElement(OrderList, null), document.getElementById("order-list-react"));
	informationIsc.refresh();
}

var isChoice = false;
function waimaiSelectDateInit(){
	//加载就餐日期
	
	var dateArr = new Array();
	var dateName = {0:"今天",1:"明天",2:"后天"};
	for(var i = 0;i<3;i++){
		var toDay = new Date();
		toDay.setDate(toDay.getDate()+i);		
		var dateStr = toDay.format("yyyy-MM-dd");		
		dateArr.push({dateStr:dateStr,dateName:dateName[i]});
	}
	console.log(dateArr);
	var DateSelect = React.createClass({displayName: "DateSelect",
		getInitialState:function(){
			return {date:this.props.defaultTime,timeid:this.props.defaultId}
		},
		createDates:function(dateObj){
			
			return React.createElement("p", {key: dateObj.dateStr, onClick: this.selectDate, className: this.state.date==dateObj.dateStr?'active':'', "data-date": dateObj.dateStr}, dateObj.dateStr, " ", dateObj.dateName)
		},
		selectDate:function(e){
			this.setState({date:$(e.currentTarget).data("date")});
		},
		createTimes:function(timeObj){
			return React.createElement("p", {key: timeObj.DISTRIBUTION_TIME_ID, onClick: this.selectTime, className: this.state.timeid==timeObj.DISTRIBUTION_TIME_ID?'active':'', "data-id": timeObj.DISTRIBUTION_TIME_ID}, " ", timeObj.BEGIN_TIME, " ")
		},
		selectTime:function(e){
			this.setState({timeid:$(e.currentTarget).data("id")});
		},
		finishSelect:function(){
			$("#select-date-dialog").hide();
			var text = "取餐时间:"+this.state.date+" ";
			for(var i in this.props.times){
				var time = this.props.times[i];
				if(time.DISTRIBUTION_TIME_ID==this.state.timeid){
					text+=time.BEGIN_TIME;
					break;
				}
			}
			ReactDOM.render(React.createElement(DateBtn, {text: text, defaultId: this.state.timeid, defaultTime: this.state.date, times: this.props.times, dates: this.props.dates}),document.getElementById("select-time-btn"));
			isChoice=true;
			deliveryTime =this.state.timeid;
			deliveryDay =this.state.date;
		},
		render:function(){
			return (
				React.createElement("div", null, 
					React.createElement("div", {className: "select-date"}, 
						React.createElement("div", {className: "title"}, "请选择日期"), 
						this.props.dates.map(this.createDates)
					), 
					React.createElement("div", {className: "select-time"}, 
						React.createElement("div", {className: "title"}, "时间"), 
						this.props.times.map(this.createTimes)
					), 	
					React.createElement("div", {className: "clearify"}, 
						React.createElement("button", {type: "button", className: "yes", onClick: this.finishSelect}, "确认")
					)
				)
			);
		}
	});	
	var DateBtn = React.createClass(
		{displayName: "DateBtn",
			getInitialState:function(){
				return {dia:''};
			},			
			openDialog:function(){
				ReactDOM.render(React.createElement(DateSelect, {dates: this.props.dates, times: this.props.times, defaultId: this.props.defaultId, defaultTime: this.props.defaultTime}),document.getElementById("select-date-time"));
				$("#select-date-dialog").show();
			},
			render:function(){
				return(
					React.createElement("button", {onClick: this.openDialog}, this.props.text) 
				);
			}
		}
	);	
	ReactDOM.render(React.createElement(DateBtn, {text: "请选择取餐时间"}),document.getElementById("select-time-btn"));
	//加载就餐时段
	cAjax("wx/server",{p:"/distributiontime/weixin/waimaiTimes"},function(result){
		var timeList =result.list;
		for(var i in timeList){
			var t = timeList[i];
			console.log(t);
			var beginTime =t.BEGIN_TIME.length>5?t.BEGIN_TIME.substring(0,t.BEGIN_TIME.length-3):t.BEGIN_TIME;
			t.BEGIN_TIME=beginTime;
		}
		var defaultId = timeList[0].DISTRIBUTION_TIME_ID;
		var defaultTime = dateArr[0].dateStr;
		ReactDOM.render(React.createElement(DateBtn, {text: "请选择取餐时间", defaultId: defaultId, defaultTime: defaultTime, times: timeList, dates: dateArr}),document.getElementById("select-time-btn"));
	});
}
waimaiSelectDateInit();
