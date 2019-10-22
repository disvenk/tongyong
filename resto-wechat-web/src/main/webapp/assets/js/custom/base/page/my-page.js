var myBaseMix = {
	mixins:[subpageMix,orderMiniMax],
	template:$("#my-temp").html(),
	data:function(){
		return {
			orderlist:[],
			chargeList:[],
			isc:null,
			load:false,
			over:false,
			pageOption:{start:0,datalength:5,orderState:"1,2,10,11",}
		};
	},
	methods:{
		reflushIsc:function(){
			if(this.isc){
				this.isc.refresh();
			}else{
				var that = this;
				this.isc = new IScroll(this.$el,{
					probeType : 2,
					click:iScrollClick(),
				});
				this.isc.on("scrollEnd",function(){
					if (this.y <= this.maxScrollY) {
						that.loadNextPage();
					}
				});
			}
		},
		loadNextPage:function(sbk){
			var that = this;
			if(!this.load&&!this.over){
				this.load=true;
				that.pageOption.start = that.pageOption.start+that.pageOption.datalength;
				getOrderList(that.pageOption,function(orderlist){
					for(var i in orderlist){
						that.orderlist.push(orderlist[i]);
					}
					if(orderlist.length<that.pageOption.datalength){
						that.over=true;
					}
					Vue.nextTick(function(){
						that.reflushIsc();
					});
					that.load=false;
				});
			}
		},
		getOrderAndBind:function(oid,callback){
			var that = this;
			getCustomerNewOrder(oid,function(o){
				if(!o){
					return;
				}
				for(var i in that.orderlist){
					var od = that.orderlist[i];
					if(od.id==o.id){
						o = $.extend(od,o);
						break;
					}
				}
				callback&&callback(o);
			});
		}
	},
	created:function(){
		var that = this;
		getChargeList(function(chargeRules){
			that.chargeList = chargeRules;
		});
		getOrderList(this.pageOption,function(orderlist){
			that.orderlist  = orderlist||[];
			Vue.nextTick(function(){
				that.reflushIsc();
			})
			
			var showDialogName = getParam("dialog");
			var oid = getParam("orderId");
			switch (showDialogName) {
			case "redpackage":
				that.getOrderAndBind(oid,function(o){
					if(o.orderState==10){
						that.$dispatch("receive-red-papper",o);
					}else{
						that.$dispatch("show-custom-neworder",o);
					}
				});
				break;
			case "callnumber":
				that.getOrderAndBind(oid,function(o){
					if(o.productionStatus==2){
						that.$dispatch("show-callnumber-dialog",o);
					}else if(o.productionStatus==0){
						that.$dispatch("push-order-api",o);
						that.$dispatch("show-callnumber-dialog",o);
					}else{
						that.$dispatch("show-custom-neworder",o);
					}
				});
				break;
			case "account":
				this.$dispatch('open-iframe','customer/informationAccount');
				break;
			default:
				that.getOrderAndBind(null,function(o){
					if(o.orderMode != 2){ //不是电视叫号
						that.$dispatch("show-custom-neworder",o);
					}else{ //电视叫号模式
						if(o.productionStatus == 0){
							that.autoPrint(o);
						}else{
							that.$dispatch("show-custom-neworder",o);
						}
					}
				});
				break;
			}
		});
	},
	detached:function(){
		this.$dispatch("my-page-detached");
	}
};