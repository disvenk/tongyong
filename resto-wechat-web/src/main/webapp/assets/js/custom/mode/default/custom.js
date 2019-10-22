
//Vue.component("order-mini-detailed",{
//	mixins:[orderMiniMax],
//});
//
//Vue.component("order-detailed-dialog",{
//	mixins:[orderdetailMax],
//});




//var HomePage = Vue.extend({
//	mixins:[homeBaseMix]
//});
//
//var MyPage = Vue.extend({
//	mixins:[myBaseMix]
//});
//
//var TangshiPage = Vue.extend({
//	mixins:[tangshiBaseMix]
//});


var MainPage = new Vue({
	mixins:[mainBaseMix],
	created:function(){
		var that  = this;
		this.loadShow=true;
		getShopList(function(list){
			that.showShopListDialog(list);
			that.loadShow=false;
		});
		
	},
});


