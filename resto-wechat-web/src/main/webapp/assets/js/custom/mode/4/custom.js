productState[2]=productState[3];
Vue.component("order-mini-detailed",{
	mixins:[orderMiniMax],
	methods:{
		vailedPushOrder:function(){
			if(!this.order.tableNumber){
				this.showOrderDetailed();
				this.$dispatch("message","请扫描桌号二维码",3000);
				throw new Error('没有桌号')
			}
		}
	},
	components: {
		'ver-code': {
			props:["order"],
			template: '<span v-if="order.tableNumber">桌号:{{order.tableNumber}} </span>'
		}
    },
});

Vue.component("order-detailed-dialog",{
	mixins:[orderdetailMax],
	data:function(){
		return{			
		}
	},
	components: {
		'code-components': {
			props:["order"],
			template:
			'<div class="weui_cell weui_vcode table-number">                                           '+
			'	<div class="weui_cell_hd"><label class="weui_label">桌号</label></div>  '+
			'	<div class="weui_cell_bd weui_cell_primary">                              '+
			'		<input class="weui_input" type="number" placeholder="扫描桌号" v-model="order.tableNumber" readonly="readonly" >   '+
			'	</div>                                                                    '+
			'	<div class="weui_cell_ft" >                                                '+
			'		<i class="iconcode icon-saoyisao" @click="openScan" v-if="order.productionStatus==0"></i>                                      '+
			'	</div>                                                                    '+
			'</div>                                                                       ',
			methods:{
				openScan:function(){
					var that = this;
					scanTableNumber(function(data){
						var reg_allNumber = /^[\d]+$/;
						var reg_tableNumber = /tableNumber=[\d]+/;
						if(reg_allNumber.test(data)){
							that.order.tableNumber=data;
							that.autoPushOrder();
						}else if(reg_tableNumber.test(data)){
							var tbNumber = data.match(reg_tableNumber)[0].match(/[\d]+/)[0];
							that.$set("order.tableNumber",tbNumber);
							that.autoPushOrder();
						}else{
							that.$dispatch("message","未识别改格式的数据:"+data,2000);
						}
						console.log("返回扫描结果,解析二维码数据，搞定桌号");
					});
				}
			}
		}
    },
	methods:{
		pushOrderSuccess:function(){
			console.log("下单成功");
		},
		pushOrder:function(){
			var that = this;
			if(!this.order.tableNumber){
				this.$dispatch("message","请扫描桌号二维码或手动输入桌号",3000);
			}else{
				var o = this.order;
				setTableNumber(o.id,o.tableNumber,function(result){
					if(result.success){
						pushOrderRequest(o.id,function(re){
							if(re.success){
								that.$dispatch("message","下单成功");
								that.order.productionStatus=1;
								that.pushOrderSuccess&&that.pushOrderSuccess(that.order);
							}else{
								that.$dispatch(re.message);
							}
						});
					}else{
						that.$dispatch(result.message);
					}
				});
			}
		},
		autoPushOrder:function(){
			console.log("自动立即下单！");
			if(this.show&&this.order.productionStatus==0&&this.order.orderState==2&&this.order.tableNumber){
				this.$dispatch("message","正在自动下单",2000);
				this.pushOrder();
			}
		}
	},
	created:function(){
		this.$watch("show",function(){
			this.autoPushOrder();
		});
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


