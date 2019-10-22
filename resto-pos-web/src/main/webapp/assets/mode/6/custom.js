/**
 * 坐下点餐模式
 */

Vue.component("ready-order-component",{
	mixins:[posmix],
	data:function(){
		return {
			showTableNumber:true,
			callBtn:false,
            unPrint:false,
            bossModel:true,
			showPayState:true
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
        this.getPrinterList();
		this.getPosOrder();//得到 POS 中新列表订单中，未处理的订单，防止页面刷新后，新订单不显示。
		this.startGetNewOrder();
        this.startPrinterTask();
        this.startGetErrorOrder();


	}
});

Vue.component("print-order-component",{
	mixins:[printOrderMix],
});

Vue.component("history-order-component",{
	mixins:[historyOrderMix],
	data:function(){
		return {
			showTableNumber:true,
			callBtn:false,
			printBtn:true,
			unFinish:true,
            unPrint:false,
            bossModel:true,
            showPayState:false
		}
	},
	created:function(){
		var that = this;
		listHistoryOrder(function(olist){
			that.orderlist= olist;
		});
		getBrandSetting(function(result){
			that.brandSetting = result;
		});
		getShopInfo(function(result){
			that.shop = result;
		});
	}
});




Vue.component("out-food-component",{
	mixins:[outFoodMix],
	data:function(){
		return {
			showTableNumber:true,
			callBtn:false,
			printBtn:true
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


	