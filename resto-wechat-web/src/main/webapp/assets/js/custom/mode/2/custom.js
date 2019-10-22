Vue.component("order-mini-detailed",{
	mixins:[orderMiniMax],
	data:function(){
		return {
			//pushOrderBtnName:"我已到店"
			pushOrderBtnName:""
		}
	},
});

Vue.component("auto-print",{
	mixins:[orderMiniMaxByTV]

});

Vue.component("order-detailed-dialog",{
	mixins:[orderdetailMax],
	data:function(){
		return {
			pushOrderBtnName:"我已到店"
		}
	},
	methods:{
		//pushOrderClick:function(){
		//	var that = this;
		//	$.ajax({
		//			url: "order/checkArticleCount",
		//			data: {orderId: this.order.id},
		//			success: function (result) {
		//				//that.$dispatch("message", result.success, 9000);
		//				if(result.success){
		//					//that.$dispatch("message", 1, 9000);
		//					that.pushOrder();
		//				}else{
		//					that.$dispatch("message", result.message, 9000);
		//				}
		//			}
		//	});
        //
		//},

		pushOrderSuccess:function(order){
			this.$dispatch("message","正在下单中");
		},
		orderProductionStatusChange:function(pstate){
			console.log("订单状态改变",pstate);
			if(pstate==2){
				this.show=false;
				this.$dispatch("show-callnumber-dialog",this.order);
			}else if (pstate == 1){
				this.$dispatch("message","正在下单中");
			}
		},
	},
	created:function(){

		//this.pushOrder();
		//this.$dispatch("show-callnumber-dialog",this.order);
		//this.$watch("show",function(){
		//	if(this.show){
		//		if(this.order.productionStatus==2){
		//			this.show=false;
		//			this.$dispatch("show-callnumber-dialog",this.order);
		//		}else if(this.order.productionStatus==1){
		//			this.$dispatch("message","正在下单中");
		//		}
        //
		//	}
		//})
	}
});




var HomePage = Vue.extend({
	mixins:[homeBaseMix]
});

var MyPage = Vue.extend({
	mixins:[myBaseMix]
});

var TangshiPage = Vue.extend({
	mixins:[tangshiBaseMix]
});


var MainPage = new Vue({
	mixins:[mainBaseMix],
	components: {
	    'home-page': HomePage,
	    'tangshi-page':TangshiPage,
	    'my-page':MyPage
    },
    events:{
    	
    }
});


