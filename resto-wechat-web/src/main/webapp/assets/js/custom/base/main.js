/**
 * 组件加载之前，首先确定用户位置，并选择对应的
 */
var mainBaseMix = {
		el:"#main-page",
		data:function(){
			return {
				wAlter:{title:"",show:0,content:""},
				wMessage:{show:0,message:""},
				showBindPhone:false,
				loadShow:false,
				orderSum:0,
				appraiseSum:0,
				confirmDialogShow:0,
				dialogOptions:{},
				page:"",
				shop:{},
				customer:{},
				tangshiArtList:[],
				menuList:[],
				chargeDialog:{show:false,chargeList:[]},
				payAlter:{show:false,order:null},
				customNewOrder:{show:false,order:null,customer:null,option:{}},
				appraiseOrder:{show:false,order:null},
				redPapperDialog:{show:false,name:"",title:"",order:{},appraise:null},
                redPapperRegisteredDialog: {show: false, name: "", title: "", order: {}, appraise: null},
				redOpenDialog:{show:false,name:"",title:"",money:0,appraise:null},
				iframeDialog:{show:false,src:""},
				callNumberDialog:{show:false,order:null},
				shopListDialog:{show:false,shoplist:null},
				noticeDialog:{show:false,notice:{}},
				noticeListDialog:{show:false,noticelist:[]},
				shareDialog:{show:false,isshare:true,appraise:null,setting:null},
				rewardDialog:{show:false,appraise:null},
				allSetting:allSetting,
				otherdata:null,
				isshowred:false,
			};
		},
		created:function(){
			var home = {name:"home",title:allSetting.wechatHomeName};
			var tangshi ={name:"tangshi",title:allSetting.wechatTangshiName};
			var my ={name:"my",title:allSetting.wechatMyName};
			this.menuList.push(home);
			this.menuList.push(tangshi);
			this.menuList.push(my);
		},
		ready:function(){

			var that  = this;
			var showDialogName = getParam("dialog");
			var aid = getParam("appraiseId");
			switch (showDialogName) {
				case "share":
					getShareDetailed(aid,function(result){
						var dialog = that.shareDialog;
						dialog.show=true;
						dialog.isshare=true;
						dialog.appraise = result.appraise;
						dialog.setting = result.setting;
					});
					console.log("显示分享弹窗");
					break;
				default:
					break;
			}

			getCustomer(function(customer){
				that.customNewOrder.customer=customer;
				that.customer=customer;
				switch(showDialogName){
					case "sharefriend":
						if(!customer.isBindPhone){
							getShareDetailed(aid,function(result){
								var dialog = that.shareDialog;
								dialog.show=true;
								dialog.isshare=false;
								dialog.appraise = result.appraise;
								dialog.setting = result.setting;
                                dialog.setting.registered = false;
							});
							console.log("被分享弹窗");
						}else{
                            getShareDetailed(aid,function(result){
                                var dialog = that.shareDialog;
                                dialog.show=true;
                                dialog.isshare=false;
                                dialog.appraise = result.appraise;
                                dialog.setting = result.setting;
                                dialog.setting.registered = true;
                            });
                            console.log("被分享弹窗");
                        }
						break;
				}
			});

			getShopInfo(function(shop){
				that.shop= shop;
			});
			setTimeout(function(){
		      	resetWindow();
		    },50);
			window.onhashchange=function(){
				var hash = location.hash;
				if("#"+that.page!=hash){
					var page = hash.substr(1);
					that.changePage(that.findMenu(page));
				}
				this.otherdata=null;
			}
			var default_page = location.hash;
			if(default_page){
				default_page = default_page.substr(1);
			}else{
				default_page = getParam("subpage");
				if(!default_page){
					default_page = "tangshi";
				}
			}
			this.changePage(this.findMenu(default_page));

		},
		methods:{
			showShopListDialog:function(shoplist){
				this.shopListDialog = {
						show:true,
						shoplist:shoplist
				}
			},
			findMenu:function(name){
				for(var i=0;i<this.menuList.length;i++){
					var menu = this.menuList[i];
					if(name==menu.name){
						return menu;
					}
				}
			},
			showCustomerNewOrder:function(order,op){
				this.customNewOrder.order = order;
				this.customNewOrder.customer=this.customer;
				this.customNewOrder.show=true;
				this.customNewOrder.option = op||{};
			},

			changePage:function(menu){
				if(typeof menu == "string"){
					menu = this.findMenu(menu);
				}
				var menuName = menu.name;
				this.beforeChangeMenu(menu);
				this.page = menuName;
				var that = this;
				location.hash=this.page;
				Vue.nextTick(function(){
					$("title").html(menu.title);
					that.afterChangeMenu&&that.afterChangeMenu(menu);
				});
			},
			beforeChangeMenu:function(menu){
				var menuName = menu.name;

				if((menuName=="tangshi"||menuName=="home")&&!this.isshowred&&allSetting.autoAlertAppraise){//查看是否有新红包
					this.showNewPackage();
				}
			},
			showNewPackage:function(){
				var showDialogName = getParam("dialog");
				if(showDialogName){  //如果有其他dialog，则不弹出红包
					return;
				}
				if(this.newNotice&&this.newNotice.length>0){ //如果有新通知，则不弹出红包弹窗
					return;
				}
				var that =this;
                console.log("如果是未注册用户，那么每次进入平台则提示红包领取");

                $.ajax({
                    url: "customer/checkRegistered",
                    type: 'post',
                    dataType: 'json',
                    success: function (result) {
                        if (result.success) {
                            //未注册
                            that.$emit("receive-red-papper-registered");
                        }
                    }
                });
				console.log("查找用户是否有未领取的红包");
				getCustomerNewPackage(function(order){
					that.isshowred=true;
					if(order&&order.id){
						console.log("有未领取的红包，显示未领取的红包");
						that.$emit("receive-red-papper",order);
					}else{
						console.log("查找到的订单为空，没有新红包");
					}
				});
			},
			showAlter:function(title,content){
				this.wAlter.title=title;
				this.wAlter.content=content;
				this.wAlter.show=1;
				this.loadShow=false;
			},
			showMessage:function(msg,time){
				this.wMessage.show=1;
				this.wMessage.message=msg;
				var that = this;
				setTimeout(function(){
					that.wMessage.show=0;
				},time||1000);
			},
			showDialog:function(option){
				this.dialogOptions = option;
				this.confirmDialogShow=1;
				this.loadShow=false;
			},
			paySuccess:function(order){
				if(this.otherdata&&this.otherdata.event=="continue-order"){
					this.showMessage("加菜成功，请稍等片刻",20000);
				}
				if(getParam("dialog")){
					location.href="/wechat/index?subpage=my";
					return false;
				}
				order.orderState=2;
				this.changePage("my");
				this.loadShow=false;
				this.$broadcast("pay-success");
			},
			payOrderWx:function(orderId){
				var that = this;
				that.loadShow=true;
				openWechatPay(orderId,function(result){
					that.paySuccess({orderId:orderId});
				},function(errMsg){
					that.showAlter("支付失败",errMsg);
				},function(){
					that.loadShow=false;
				});
			},
			payDialog:function(order){
				this.payAlter.show=true;
				this.payAlter.order=order;
			},
			closeOrder:function(order){
				var that  = this;
				cancelOrderRequest(order.id,function(result){
					if(result.success){
						that.showMessage("取消订单成功");
						order.orderState=9;
						that.$broadcast("cancel-order-success",order);
					}else{
						that.showAlter("取消失败",result.message);
					}
				});
			},
			redPapperOpen:function(order){
				this.showAppraiseOrder(order);
			},
            openRegisteredPapper: function () {
                this.showBindPhone = true;
                //this.$dispatch("toRegistered");
            },
			showAppraiseOrder:function(order){
				this.appraiseOrder.show=true;
				this.appraiseOrder.order=order;
			}
		},
		events:{
			"switch-shop":function(sid){
				if(!this.shop||sid!=this.shop.id){
					switchShopApi(sid,function(result){
						if(result.success){
							location.reload();
						}else{
							this.showMessage(result.message);
						}
					});
				}
			},
			"show-shoplist-dialog":function(){
				var that = this;
				getShopList(function(list){
					that.showShopListDialog(list);
					that.loadShow=false;
				});
			},
			"alter":function(title,content){
				this.showAlter(title,content);
			},
			"bind-phone":function(){
				this.showBindPhone=true;
			},
			"bindPhoneSuccess":function(){
				this.$broadcast("bind-success");
				var that = this;
				getCustomer(function(customer){//绑定手机后，更新当前账户信息
					that.customer=customer;
					getNoticeList(2,function(data){
						if(data&&data.length>0){
							that.$emit("show-notice-list",data);
						}else{
							//目前不显示 优惠券 页面
							//that.$emit("open-iframe","customer/informationCoupon");
						}
					});
				});
			},
			"show-wechat-pay":function(orderForm){
				this.payDialog(orderForm);
			},
			"save-order":function(formStr,successbck){
				var that = this;
				this.loadShow=true;
				saveOrderForm(formStr,function(result){
					that.$broadcast("save-order-success");
					if (result.success) {
						getCustomer(function(customer){
							that.customer=customer;
						});
						if (result.data.paymentAmount == 0) {
							that.paySuccess({id:result.data.id});
						}else{
							successbck&&successbck(result.data);
						}
						that.loadShow=false;
					}else{
						that.showAlter("买单失败",result.message);
					}

				},function(error){
					this.loadShow=false;
				});
			},
			"loading":function(){
				this.loadShow=true;
			},
			"loaded":function(){
				this.loadShow = false;
			},
			"close-message":function(){
				this.wMessage.show=false;
			},
			"message":function(msg,time){
				this.showMessage(msg,time);
			},
			"show-dialog":function(option){
				this.showDialog(option);
			},
			"total-push-order":function(sum){
				this.orderSum = sum;
			},
			"push-order":function(order){
				this.paySuccess(order);
			},
			"charge":function(charge_id,successCbk,cancelCbk){
				var that  = this;
				//if(!that.customer.isBindPhone||!that.customer.telephone){
				//	this.chargeDialog.show=false;
				//	that.$dispatch("bind-phone");
				//	that.$dispatch("loaded");
				//	return;
				//}
				that.loadShow=true;
				chargeWxRequest(charge_id,function(){
					that.$broadcast("charge-success");
					that.loadShow=false;
				},function(){
					cancelCbk&&cancelCbk();
					that.loadShow=false;
				});
			},
			"show-charge-dialog":function(chargeList){
				if(chargeList.length>0){
					this.chargeDialog.chargeList=chargeList;
					this.chargeDialog.show=true;
				}
			},
			"pay-order":function(paymode,order){
				if("wechat"==paymode){
					this.payOrderWx(order.id);
				}
			},
			"show-custom-neworder":function(order,op){
				this.showCustomerNewOrder(order,op);
			},
			"cancel-order":function(order){
				this.closeOrder(order);
			},
			"appraise-order":function(order){
				this.showAppraiseOrder(order);
			},
			"save-appraise":function(appraise){
				var that = this;
				this.loadShow = true;
				saveOrderAppraise(appraise,function(result){
					if(result.success){
						var money = result.data.redMoney;
						that.redOpenDialog = $.extend(that.redOpenDialog,that.redPapperDialog,{appraise:result.data});
						that.redOpenDialog.money=money;
						that.redOpenDialog.show=true;
						console.log(that.redOpenDialog.appraise);
						that.$broadcast("save-appraise-success");
						that.customer.account+=money;
					}else{
						that.showAlter(result.message);
					}
					that.loadShow=false;
				});
			},
			"my-page-detached":function(){
				this.customNewOrder.show=false;
			},
			"receive-red-papper":function(order){
				var that  = this;
				that.redPapperDialog.show=true;
				that.redPapperDialog.name=that.shop.name;
				that.redPapperDialog.title="完成消费评价即可领取!";
				that.redPapperDialog.order = order;
			},
            "receive-red-papper-registered": function () {
            	var that = this;
            	//根据 链接 判断 显示的优惠券(新用户注册或者邀请注册)
            	var couponType = getParam("dialog")?"1":"0";
            	showCouponList({"couponType":couponType},function(result){
            		//如果商家设定的有优惠券，则显示  【红包-开】 样式
            		if(result.success && result.data.length>0){
                      that.redPapperRegisteredDialog.show = true;
                      that.redPapperRegisteredDialog.name = that.shop.name;
                      that.redPapperRegisteredDialog.title = "完成注册即可领取!";
            		}else{//否则 直接显示注册页面
            			that.openRegisteredPapper();
            		}
            	})
            },
			"show-callnumber-dialog":function(order){
				this.callNumberDialog.order=order;
				this.callNumberDialog.show=true;
			},
			"open-iframe":function(src){
				this.iframeDialog.src=src;
				this.iframeDialog.show=true;
			},
			"show-notice":function(n){
				this.noticeDialog.show=true;
				this.noticeDialog.notice=n;
			},
			"show-notice-list":function(notices){
				this.noticeListDialog.show=true;
				this.noticeListDialog.noticelist=notices;
			},
			"continue-order":function(order){
				var menu = this.findMenu('tangshi');
				this.changePage(menu);
				this.otherdata=order;
				this.$set("otherdata.event","continue-order");
			},
			"open-reward-dialog":function(appraise){
				this.rewardDialog.show=true;
				this.rewardDialog.appraise=appraise;
			}
		},
	};

function resetWindow(){
	var main_menu = $("#main-menu")
	var content = $("#content-home");
	var height = $(window).height();
	content.height(height-main_menu.height());
	content.css({
		overflow:"hidden",
		position:"relative"
	});
}