productState[2]="已消费";

Vue.component("order-mini-detailed",{
	mixins:[orderMiniMax],
	methods:{
		showOrderDetailed:function(){
			var that = this;
			getCustomerNewOrder(that.order.id,function(o){
				that.order = $.extend(that.order,o);
				that.$dispatch("show-custom-neworder",that.order);
			});
		},
		pushOrderClick:function(){
			var o  = this.order;
			if(o.parentOrderId&&o.tableNumber){
				this.pushOrder();
			}else{
				this.helpOrder();
			}
		},
	}
});

Vue.component("order-detailed-dialog",{
	mixins:[orderdetailMax],
	created:function(){
		this.$watch("show",function(){
			if(this.show){
				if(this.order&&this.order.orderState==2&&this.order.productionStatus==0&&!this.option.cancel&&!this.order.parentOrderId){
					this.helpOrder();
				}
			}
		})	
	},
	methods:{
		pushOrderClick:function(){
			var o  = this.order;
			if(o.parentOrderId&&o.tableNumber){
				this.pushOrder();
			}else{
				this.helpOrder();
			}
		},
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


