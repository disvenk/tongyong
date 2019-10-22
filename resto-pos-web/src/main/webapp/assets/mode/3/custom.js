/**
 * 验证码下单模式
 */
Vue.component("ready-order-component",{
	mixins:[posmix],
	template:
		'<div">                                                                                          '+
			'<div class="modal" style="display:block" v-if="muliOrderDialog.show">                                                                                                                 '+
			'  <div class="modal-dialog">                                                                                                        '+
			'	<div class="modal-content">                                                                                                      '+
			'	  <div class="modal-header">                                                                                                     '+
			'		<button type="button" class="close" @click="closeMuliDialog"><span >&times;</span></button> '+
			'		<h4 class="modal-title">找到多个订单</h4>                                                                                     '+
			'	  </div>                                                                                                                         '+
			'	  <div class="modal-body" style="max-height: 500px; overflow: auto;">                                                                                                       '+
					'<div>'+
				'		 <button @click="choiceOrder(o)" class="btn btn-block" :class="{\'btn-success\':$index%2==1,\'btn-info\':$index%2==0}" v-for="o in muliOrderDialog.orderlist | orderBy \'createTime\' -1" style="text-align:left;margin-bottom:10px;">'+
							'<div class="flex-row"><div class="flex-1">订单编号:{{o.serialNumber}}</div> <div class="flex-1">订单时间:{{o.createTime&&new Date(o.createTime).format("yyyy-MM-dd hh:mm")}}</div></div>'+
							'<div class="flex-row"><div class="flex-1">订单金额:{{o.orderMoney}}</div> <div class="flex-1">餐品总数:{{o.articleCount}}</div></div>'+
							'<div class="flex-row"><div class="flex-1">电话号码:{{o.customer.telephone}}</div> <div class="flex-1">微信昵称:{{o.customer.nickname}}</div></div>'+
						'</button>   '+
					'</div>'+
			'	  </div>                                                                                                                         '+
			'	  <div class="modal-footer">                                                                                                     '+
			'		<button type="button" class="btn btn-default" @click="closeMuliDialog">关闭</button>                                            '+
			'	  </div>                                                                                                                         '+
			'	</div>                                                                                                                           '+
			'  </div>                                                                                                                            '+
			'</div>                                                                                                                              '+
		'	<div class="row order-operator">                                                                                       '+
		'		<div class="col-sm-4 padding-0 full-height border-1">                                                              '+
		'			<div class="order-detailed">                                                                                   '+
		'				<div class="title">                                                                                        '+
		'					<h4>订单信息</h4>                                                                                      '+
		'				</div>                                                                                                     '+
		'				<div class="description">                                                                                  '+
		'					<p><span>订单编号:</span> <span>{{order.serialNumber}}</span></p>                                       '+
		'					<p>	<span>下单门店:</span> <span>{{order.shopName}}</span></p>                                            '+
		'					<p>	<span>下单时间:</span> <span>{{order.createTime&&new Date(order.createTime).format("yyyy-MM-dd hh:mm")}}</span></p>                                            '+
		'					<p>	<span>顾客电话:</span> <span>{{order.customer.telephone}}</span></p>                                           '+
		'					<p>	<span>就餐模式:</span> <span>{{modeText}}</span></p>                                  '+
		'					<p>	<span>关联餐位:</span> <span>{{order.tableNumber}}</span></p>                                  '+
		'				</div>                                                                                                     '+
		'				<div class="title">                                                                                        '+
		'					<h4><span>餐品名称</span> <span class="pull-right" id="itemNum">数量/份</span></h4>                    '+
		'					<div class="article-items">                                                                            '+
		'						<p v-for="item in order.orderItems"><span>{{item.articleName}}</span><span class="pull-right">{{item.count}}</span></p>                                         '+
								'<div class="print-ticket"><button class="btn-info btn btn-block" @click="printTicketClick" v-if="order.id">打印小票</button></div>'+
		'					</div>                                                                                                 '+
		'				</div>                                                                                                     '+
		'			</div>                                                                                                         '+
		'		</div>                                                                                                             '+
		'		<div class="col-sm-8 padding-0 full-height tab">                                                                   '+
					'<div class="flex-row full-height operator-div">'+
			'			 <div class="flex-2 number-input full-height">'+
							'<div class="flex-row">'+
							  '<div class="flex-2"><input placeholder="请输入验证码" id="vercode" type="text" class="ver-code-input" v-model="verCode" @click="clearInput"></div>'+
							  '<div class="flex-1" @click="backspaceOne"><button class="btn btn-default">&lt;</button></div>'+
							'</div>'+
							'<div class="flex-row " v-for="n in number ">'+
								'<div class="flex-1" v-for="num in n" @click="inputNumber(num)"> <button class="btn btn-default" type="button">{{num}}</button></div>'+
							'</div>'+
			'            </div>'+
			'			 <div class="flex-1 number-input full-height">'+
							'<div class="flex-row">'+
							  '<div class="flex-2" @click="selectOrder"><button class="btn btn-info">查询订单</button></div>'+
							'</div>'+
							'<div class="flex-row">'+
								'<div class="flex-2" @click="bindAndPushOrder"><button class="btn btn-success">关联并下单</button></div>'+
							'</div>'+
							'<div class="flex-row">'+
								'<div class="flex-2" @click="bindTableNumber"><button class="btn btn-info">关联餐位</button></div>'+
							'</div>'+
							'<div class="flex-row">'+
								'<div class="flex-2 " @click="swtichMode(\'1\')"><button class="btn btn-default">堂吃</button></div>'+
							'</div>'+
							'<div class="flex-row">'+
								'<div class="flex-2 " @click="swtichMode(\'3\')"><button class="btn btn-default">外带</button></div>'+
							'</div>'+
						'</div>'+
					'</div>'+
		'		</div>		                                                                                                       '+
		'	</div>                                                                                                                 '+
		'</div>                                                                                                                    ',
		data:function(){
			return {
				number:[[7,8,9],[4,5,6],[1,2,3],[0,"00","."]],
				verCode:"",
				muliOrderDialog:{show:false,orderlist:[]},
				modeCode:true,//标识为验证码下单模式-->在basemix中用于区分是否需要设置触屏滑动
                unPrint:true
			}
		},
		methods:{
			clearInput:function(){
				this.verCode="";
			},
			printTicketClick:function(){
				printReceipt(this.order.id,function(task){
					printPlus([task],function(){
						toastr.success("打印成功");
					},function(msg){
						toastr.error(msg);
					});
				});
			},
			inputNumber:function(n){
				this.verCode+=n;
			},
			backspaceOne:function(){
				if(this.verCode.length>0){
					this.verCode= this.verCode.substring(0,this.verCode.length-1);
				}
			},
			swtichMode:function(mode){
				if(!this.order.id){
					toastr.info("当前没有订单！");
				}else{
					var that  = this;
					swtichDistrubitionMode(this.order.id,mode,function(result){
						if(result.success){
							that.order.distributionModeId=mode;
							toastr.info("切换模式成功");
						}else{
							toastr.error(result.message);
						}
					});
				}
				console.log(mode);
			},
			closeMuliDialog:function(){
				this.muliOrderDialog={show:false,orderlist:[]};
			},
			showOrderInfo:function(orderlist){
				if(orderlist.length==1){
					this.choiceOrder(orderlist[0]);
				}else if(orderlist.length>1){
					this.muliOrderDialog.orderlist=orderlist;
					this.muliOrderDialog.show=true;
				}else{
					this.order={};
					toastr.info("没有找到 "+this.verCode+" 的订单");
				}
			},
			choiceOrder:function(order){
				var that = this;
				getOrderInfo(order.id,function(o){
					that.order = o;
					that.closeMuliDialog();
					if(that.verCode.length==4){
						that.verCode="";
						$("#vercode").attr("placeholder","请输入桌号");
					}
					if(that.verCode.length==5){
						that.verCode="";
						$("#vercode").attr("placeholder","请输入桌号");
					}
				});
			},
			selectOrder:function(){
				var that = this;
				if(this.verCode.length==2){
					getOrderListByTableNumber(this.verCode,function(orderlist){
						that.showOrderInfo(orderlist);
					});
				}else{
					getOrderListByVercode(this.verCode,function(orderlist){
						that.showOrderInfo(orderlist);
					});
				}
			},
			bindTableNumber:function(sbk){
				if(this.verCode.length==0){
					toastr.info("请输入要关联的餐位号");
					return false;
				}
				if(!this.order.id){
					toastr.info("当前没有订单");
					return false;
				}
				var that  = this;
				var vCode = this.verCode+"";
				getOrderListById(this.order.id,function(orderlist){
					if(orderlist.orderState != 9 ){
						bindingTableNumber(that.order.id,vCode,function(result){
							if(result.success){
								that.$set("order.tableNumber",vCode);
								toastr.success("关联桌号 "+vCode+" 成功");
								that.verCode="";
								$("#vercode").attr("placeholder","请输入验证码");
								if(typeof sbk=="function"){
									sbk();
								}
							}else{
								toastr.error(result.message);
							}
						});
					}else{
						toastr.error("该订单已被取消，请重新下单！");
						that.verCode="";
						$("#vercode").attr("placeholder","请输入验证码");
					}
				});
			},
			bindAndPushOrder:function(){
				var that = this;
				this.bindTableNumber(function(){
					if(isPrintSupport()){
						toastr.info("正在打印...");
						printOrderAllAndSuccess(that.order.id,function(result){
							if(result.success){
								if(that.shop.isPosNew == 1){
                                    for (var i = 0; i < result.data.length; i++) {
                                        printPlusNew(result.data[i]);
                                    }
								}else{
                                    printPlus(result.data);
                                    toastr.success("打印订单成功");
								}

							}else{
								toastr.error(result.message);
							}
						});
					}else{
						toastr.error("不支持打印插件！");
					}
					
				});
				that.verCode="";
			},
			wsOnmessage:function(event){
				var that = this;
				var data = JSON.parse(event.data);
				console.log("接收到websocket消息:",data);
				if(data.dataType == "current"){
					if(data.parentOrderId&&data.tableNumber){
						toastr.info("接收到 "+data.tableNumber+" 加菜请求，正在自动打印");
						that.printTask.push(data);
					}else if(data.parentId){
						console.log("没有桌号");
					}
				}else if (data.dataType == "platform") { //第三方订单
					that.printPlatformTask.push(data);
				}else if (data.dataType == "badAppraisePrintOrder") {
                    //接收到差评订单
                    badAppraisePrintOrder(data.id, function (data) {
                        printPlus(data);
                        toastr.success("差评订单已打印");
                    }, function (errorMessage) {
                        //提示错误信息
                        toastr.error(errorMessage);
                    });
                }
			},
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
			console.log("开启打印线程");
			this.connWebsock();
			this.startGetNewOrder();
            this.startPrinterTask();
            this.startGetErrorOrder();
		}
		
	
});

Vue.component("history-order-component",{
	mixins:[historyOrderMix],
	data:function(){
		return {
			callBtn:false,
			showTableNumber:true,
			unFinish:false,
			outFood:false,
			unPrint:false
		}
	},
	created:function(){
		var that = this;
		listHistoryOrder(function(olist){
			that.orderlist= olist;
		});

	},
	methods:{
		showOrderInfo:function(oid){
			var that  =this;
			getOrderInfo(oid, function (order) {
				that.order = order;
				// if (that.order.orderMode == 5) { //如果是后付模式

				getChildItem(oid, function (result) {
					that.childList = [];
					that.childList = result;

				});
				// }
			});
		},
	}
});



Vue.component("print-order-component",{
	mixins:[printOrderMix],
});




var orderOperator = new Vue({
	el:"#new-order-tab",
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