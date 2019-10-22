/**
 * 后付款模式
 */
Vue.component("ready-order-component",{
	mixins:[posmix],
	data:function(){
		return {
//            showCustomerCount:true,//显示就餐人数
			showTableNumber:true,
			otherPayModeBtn:true,
			modeHouFu:true,//表示为后付款模式
			orderMoney:true,
			callBtn:false,
			unFinish:true,
			editHoufu:true
		} 
	},
	watch : {
		orderlist : function (orderList, oldValue) {
			var count = 0;
			for(var index in orderList){
				var o =orderList[index];
				if(this.showPayState && o.orderMode == 6 && o.orderState == 1 && (o.payMode == 3 || o.payMode == 4 || o.payMode == 5 || o.payMode == 6 )){
					count++;
				}
			}
			$("#menu_order_num").text(count>0?count:"");
		}
	},
	created:function(){
		var that = this;
		getBrandSetting(function(result){
			that.brandSetting = result;
		});


	},
	ready:function(){
		this.getPosOrder();//得到 POS 中新列表订单中，未处理的订单，防止页面刷新后，新订单不显示。
		this.startGetNewOrder();
		// this.startPrinterTask();
		//初始化自动打印异常订单按钮
	}
});

Vue.component("print-order-component",{
	mixins:[printOrderMix],
});





Vue.component("history-order-component",{
	mixins:[historyOrderMix],
	data:function(){
		return {
//            showCustomerCount:true,//显示就餐人数
			showTableNumber:true,
			callBtn:false,
			printBtn:true,
			orderMoney:true,
			unFinish:false,
			editHoufu:true
		}
	},
	created:function(){
		console.log("history-order-component");
		var that = this;
		listHistoryOrder(function(olist){
			that.orderlist= olist;
		});





		getBrandSetting(function(result){
			that.brandSetting = result;
		});

	}
});



Vue.component("out-food-component",{
	mixins:[outFoodMix],
	data:function(){
		return {
			showTableNumber:true,
			callBtn:false,
			printBtn:true,
			editHoufu:true
		}
	},
	created:function(){
		var that = this;
		getOutFoodList(function(olist){
			that.orderlist= olist;
		});
	}
});

var orderOperator = new Vue({
	el:"#new-order-tab",
});
