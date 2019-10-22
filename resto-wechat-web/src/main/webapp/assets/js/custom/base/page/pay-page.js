//设置初始状态
var payBaseMix = {
		template:$("#pay-temp").html(),
		mixins:[subpageMix],
		data:function(){
			return {
				showBindPhone:false, 												
			}
		},
		computed:{
			articleList:function(){
				var artList = [];
				for(var i=0;i<this.familyList.length;i++){
					for(var j=0;j<this.familyList[i].articles.length;j++){
						var art = 	this.familyList[i].articles[j];
						artList.push(art);
						this.articleMap.put(art.id,art);
					}
				}
				return artList;
			},
			useAccount:function(){
				if(this.checkAccount){
					return this.customer.account;
				}else{
					return 0;
				}
			},
			unitPriceList:function(){
				var upList = [];
				for(var j=0;j<this.articleList.length;j++){
					var ups = this.articleList[j].articlePrices;
					for(var i=0;i<ups.length;i++){
						var up = ups[i];
						upList.push(up);
						this.articlePriceMap.put(up.id,up);
					}
				}
				return upList;
			},
			shopCart:function(){
				var cart = {
						items:[],
						totalNumber:0,
						totalPrice:0
				};
				var fam = this.familyList;
				for(var j=0;j<this.articleList.length;j++){
					var art = this.articleList[j];
					if(art.number>0&&art.articlePrices.length==0){
						var item= {};
						item.name = art.name;
						item.data = art;
						item.id = art.id;
						item.type=1;
						cart.totalNumber += art.number;
						cart.totalPrice += art.number*parseFloat(art.realPrice);
						cart.items.push(item);
					}
				}
				for(var j=0;j<this.unitPriceList.length;j++){
					var up = this.unitPriceList[j];
					if(up.number>0){
						var item= {};
						item.name = up.article.name+up.name
						item.data = up;
						item.id=up.id;
						item.type=2;
						cart.totalNumber += up.number;
						cart.totalPrice += up.number*parseFloat(up.realPrice);
						cart.items.push(item);
					}
				}
				var mealArticles = this.articleMealsMap.values||[];
				for(var j=0;j<mealArticles.length;j++){
					var art = mealArticles[j];
					if(art.number>0){
						var item = {};
						item.name=art.name;
						item.data=art;
						item.id=art.id;
						item.type=3;
						cart.totalNumber += art.number;
						cart.totalPrice += art.number*parseFloat(art.realPrice);
						cart.items.push(item);
					}
				}
				if(cart.items.length==0){
					this.isCreateOrder=false;
				}
				return cart;
			},
			useCouponAmount:function(){
				var totalPrice = this.shopCart.totalPrice;
				if(totalPrice >0){
					totalPrice -= this.useCoupon;
				}else{
					totalPrice=0;
				}
				return totalPrice;
			},
			finalAmount:function(){
				var totalPrice = this.useCouponAmount;
				if(totalPrice>0){
					totalPrice -= this.useAccount;					
				}
				return totalPrice>0?totalPrice:0;
			},
			canUseCoupon:function(){
				var canUse = [];
				for(var i=0;i<this.allCoupon.length;i++){
					var c = this.allCoupon[i];
					if(c.minAmount<=this.shopCart.totalPrice){
						if(!c.useWithAccount&&this.checkAccount){
							continue;
						}
						canUse.push(c);
					}
				}
				return canUse;
			},
			useCoupon:function(){
				for(var i=0;i<this.canUseCoupon.length;i++){
					var c = this.canUseCoupon[i];
					if(c.id==this.couponId){
						return c.value;
					}
				}
				return 0;
			},
			hasRemind:function(){
				console.log("未点提示");
				var arts = [];
				for(var i=0;i<this.articleList.length;i++){
					var art = this.articleList[i];
					console.log(art.name,art.isRemind);
					if(art.number==0&&art.isRemind){
						if(art.articlePrices.length==0){
							arts.push(art);
						}else{
							var needRemind=true;
							for(var j=0;j<art.articlePrices.length;j++){
								var price = art.articlePrices[j];
								if(price.number>0){
									needRemind=false;
									console.log("已有餐品点过，不需要提示");
									break;
								}
							}
							if(needRemind){
								console.log("没有餐品点过，需要提示");
								for(var j=0;j<art.articlePrices.length;j++){
									var price = art.articlePrices[j];
									arts.push(price);
								}
							}
						}
					}
				}
				return arts.length&&arts;
			},
		},
		methods:{			
			switchShop:function(){
				this.$dispatch("show-shoplist-dialog");
			},
			moveToAttr:function(attr){
				var aid = attr.id;
				var dom = $("[data-attr-id='"+aid+"']").get(0);
				if(this.articleIsc){
					this.articleIsc.scrollToElement(dom);
				}
			},
			findArticleById:function(id){
				if(id.indexOf("@")>-1){
					return this.articlePriceMap.get(id);
				}else{
					return this.articleMap.get(id);
				}
			},
			findUnitById:function(unitId){
				for(var i=0;i<this.allAttr.length;i++){
					var attr = this.allAttr[i];
					for(var n=0;n<attr.articleUnits.length;n++){
						var unit = attr.articleUnits[n];
						if(unit.id==unitId){
							return unit;
						}
					}
				}
			},
			closeShopCart:function(){
				this.isShowShopcart= false;
				this.shopCartIsc=null;
			},
			showShopCart:function(){
				this.isShowShopcart=true;
				var that  = this;
				Vue.nextTick(function(){
					var cartlist = $(that.$el).find(".shopcart-list");
					var parentH = cartlist.parent().height();
					cartlist.siblings().each(function(){
						parentH-=$(this).height();
					});
					cartlist.height(parentH);
					that.shopCartIsc = new IScroll(cartlist.get(0),{
						click:iScrollClick(),  
						scrollbars:true,
					});
				});
			},
			clearShopCart:function(){
				var shopcart = this.shopCart;
				for(var i=0;i<shopcart.items.length;i++){
					var art = shopcart.items[i];
					if(art.number){
						art.number=0;
					}
					if(art.data){
						art.data.number=0;
					}
				}
				clearShopcartApi();
			},
			updateToServerCart:function(unitOrArt){
				var id = unitOrArt.id;
				updateShopCart(id,1,unitOrArt.number);
			},
			addToShopCart:function(a){
				a.number++;
				this.updateToServerCart(a);
			},
			cutToShopCart:function(a){
				if(a.number>0){
					a.number--;
					this.updateToServerCart(a);
				}
				if(a.number==0){
					this.shopCartIsc&&this.shopCartIsc.refresh();
				}
			},
			saveMeals:function(art){
				var saveArt = $.extend(true,{},art);
				if(!saveArt.tempId){
					saveArt.tempId = saveArt.id+"@"+new Date().getTime();
					console.log("添加套餐");
				}
				saveArt.number=1;
				this.articleMealsMap.put(saveArt.tempId,saveArt);
				this.$set("articleMealsMap.values",this.articleMealsMap.getValues());
				this.closeAction();
				
				console.log(this.shopCart.totalNumber);
			},
			getMealsReadPrice:function(art){
				var og = art.fansPrice||art.price;
				for(var i=0;i<art.mealAttrs.length;i++){
					var attr = art.mealAttrs[i];
					og+=attr.currentItem.priceDif;
				}
				art.realPrice = og;
				return og;
			},
			selectMealItem:function(item,attr){
				attr.currentItem = item;
			},
			showAction:function(a){
				this.currentArticle = a;
				if(a.hasUnit.length>0||a.articleType==2){
					for(var i =0;i<this.allAttr.length;i++){
						var attr = this.allAttr[i];
						var units = attr.articleUnits;
						if(!units){
							continue;
						}
						var cAttr = {id:attr.id,name:attr.name,units:[]};
						for(var n=0;n<units.length;n++){
							var un = units[n];
							if($.inArray(un.id.toString(),a.hasUnit)>-1){
								un.attr_id = attr.id;
								cAttr.units.push(un);
							}
						}
						if(cAttr.units.length>0){
							this.currentAttrs.push(cAttr);
						}
					}
					var that = this;
					Vue.nextTick(function(){
						var dis = $(that.$el).find(".dish_size_bg");
						var totalHeight = dis.parent().height();
						var otherHeight = 0;
						dis.siblings().each(function(){
							var he = $(this).outerHeight(true);
							otherHeight+=he;
						})
						var remainHeight=totalHeight-otherHeight;
						var h = dis.height();
						dis.height(remainHeight);
						if(dis.height()<h){
							dis.css({
								overflow:"hidden"
							});
							that.articleIsc = new IScroll(dis.get(0),{
								probeType : 2,
								click:iScrollClick(),
							});
						}
					});
				}
			},
			changeCurrentFamily:function(f){
				this.currentFamily=f;
				var that = this;
				var fid = this.currentFamily.id;
				if(that.artListIsc){
					var fidDom = $(that.$el).find("[data-family-id='"+fid+"']");
					that.artListIsc.scrollToElement(fidDom.get(0));
				}
			},
			closeAction:function(){
				this.currentArticle =null;
				this.currentAttrs = [];
				this.currentUnits = null;
				this.currentUnitPrice = null;
			},
			hasUnit:function(unitId){
				if(this.currentUnits&&this.currentUnits.values.length>0&&this.currentUnits.containsValue(unitId)){
					return true;
				}
				return false;
			},
			hasFansPrice:function(article){
				var ups = article.articlePrices;
				if(ups.length==0){
					return false;
				}
				var fans_price=null;
				for(var i=0;i<ups.length;i++){
					var up = ups[i];
					var fans=0;
					if(fans_price==null&&up.fansPrice){
						fans_price = up.fansPrice;
					}else if(up.fansPrice&&fans_price!=null&&up.fansPrice<fans_price){
						fans_price= up.fansPrice;
					}
					
				}
				return fans_price;
			},
			choiceUnit:function(unit){
				if(!this.currentUnits){
					var map = new HashMap();
					map.price = 0;
					map.unit_price = 0;
					map.attr = 0;
					map.values=[];
					this.currentUnits =map;
				}
				this.currentUnits.put(unit.attr_id,unit.id.toString());
				this.currentUnits.values =this.currentUnits.getValues(); 
				for(var n=0;n<this.currentArticle.articlePrices.length;n++){
					var unit = this.currentArticle.articlePrices[n];
					var isThis = true;
					for(var i=0;i<unit.ids.length;i++){
						var unit_id = unit.ids[i];
						if(!this.currentUnits.containsValue(unit_id)){
							isThis = false;
							break;
						}
					}
					if(isThis){
						this.currentUnitPrice = unit;
						break;
					}else{
						this.currentUnitPrice = null;
					}
				}
			},
			showDialog:function(msg){
				this.$dispatch("message",msg);
			},
			closeRemindDialog:function(){
				this.remindDialog.show=false;
				this.remindDialog.articles = [];
			},
			remindDialogOk:function(){
				this.closeRemindDialog();
				this.showCreateOrder(false);
			},
			closeShopCartAndShowCreate:function(){
				//close shopcart
				if(this.isShowShopcart=true){
					this.isShowShopcart=false;
				}				
				//execute showCreateOrder(true)
				this.showCreateOrder();
				
			},
			showCreateOrder:function(showRemind,afterShow){
				if(this.shopCart.totalNumber==0){
					this.showDialog("您还没有点餐哦！");
					this.closeCreateOrder();
				}else{
					if(allSetting.isChoiceMode&&!this.choiceModeDialog.mode){  //是否手动选择模式
						this.choiceModeDialog.show=true;
						return false;
					}
					if(showRemind&&this.hasRemind){
						this.remindDialog.show=true;
						this.remindDialog.articles = this.hasRemind;
						this.$nextTick(function(){
							var list = $(this.$el).find(".remind-list");
							var pH = list.parent().height();
							list.siblings().each(function(){
								pH-=$(this).height();
							})
							list.height(pH);
							new IScroll(list.get(0),{
								probeType : 2,
								click:iScrollClick(),
							});
						});
						return false;
					}
					
					this.$dispatch("loading");
					var that = this;
					getCustomer(function(customer){
						that.customer=customer;
						//if(!that.customer.isBindPhone||!that.customer.telephone){
						//	that.$dispatch("bind-phone");
						//	that.$dispatch("loaded");
						//	return;
						//}
                        if(that.customer.isBindPhone && that.customer.telephone){
                            getCouponList(1,function(coupon){
                                that.$dispatch("loaded");
                                that.allCoupon = coupon;
                                that.isCreateOrder=true;
                                Vue.nextTick(function(){
                                    setTimeout(function(){
                                        that.reflushOrderList();
                                        if(that.canUseCoupon.length>0&&!that.couponId){
                                            that.couponId = that.canUseCoupon[0].id;
                                        }
                                    },50);
                                    afterShow&&afterShow();
                                });
                            });
                        }

					});
				}
			},
			closeCreateOrder:function(){
				this.isCreateOrder = false;
			},
			createOrder:function(){
				var orderForm = {};
				if(this.otherdata&&this.otherdata.event=="continue-order"){
					var parentOrder = this.otherdata;
					orderForm.parentOrderId = parentOrder.id;
					orderForm.tableNumber = parentOrder.tableNumber;
				}else if(getParam("tableNumber")){
					var tableNumber = getParam("tableNumber");
					orderForm.tableNumber = tableNumber;
				}
				orderForm.customerId = this.customer.id;
				orderForm.distributionModeId= 1;
				if(this.canUseCoupon&&this.canUseCoupon.length>0&&this.couponId){
					orderForm.useCoupon = this.couponId+"";
				}
				if(this.useAccount){
					orderForm.useAccount = true; 
				}else{
					orderForm.useAccount =false;
				}
				orderForm.orderItems=[];
				for(var i=0;i<this.shopCart.items.length;i++){
					var item = this.shopCart.items[i];
					var orderItem = {
							articleId : item.id,
							count : item.data.number,
							type:item.type
						};
					if(item.type==3){
						orderItem.mealItems = [];
						for(var n=0;n<item.data.mealAttrs.length;n++){
							var mealAttr = item.data.mealAttrs[n];
							orderItem.mealItems.push(mealAttr.currentItem.id);
						}
					}
					orderForm.orderItems.push(orderItem);
				}
				orderForm.paymentAmount = this.finalAmount;
				if(allSetting.isChoiceMode&&this.choiceModeDialog.mode){
					orderForm.distributionModeId=this.choiceModeDialog.mode;
				}
				if(this.finalAmount==0){
					this.$dispatch("save-order",orderForm);
				}else{
					console.log("需要支付金额大于0,打开微信支付弹窗");
					this.$dispatch("show-wechat-pay",orderForm);
				}
			},
			reflushArticleList:function(){
				if(this.artListIsc){
					this.artListIsc.reflush();
					this.famListIsc.reflush();
				}else{
					var dom = $(this.$el);
					this.artListIsc = new IScroll(dom.find(".article-list-wapper").get(0),{
						probeType : 2,
						click:iScrollClick(),
					});
					this.famListIsc = new IScroll(dom.find(".article-family-list").get(0),{
						click:iScrollClick(),
					});
					var that =this;
					var beforId = null;
					var familyId = null;
					this.artListIsc.on("scroll",function(){
						dom.find(".article-family-group").each(function(){
							var dom = $(this);
							var position = dom.position();
							if(position.top>=0){
								return false;
							}
							familyId = dom.data("family-id");
						});
						that.currentFamily={id:familyId};
					});
				}
			},
			reflushOrderList:function(){
				
				var shopCartItems = $(this.$el).find(".shop-cart-items");
				var wH = $(window).height();
				var opH = $(this.$el).find(".order-operator-div").height();
				var totalPrice = $(this.$el).find(".total-price");
				var pH = totalPrice.height();
				var maxH = wH-opH-10-pH;
				
				/**
				 * 如果高度不够，则添加bottom
				 */
				if(shopCartItems.children().height()<maxH){
					totalPrice.css({
						marginBottom:maxH-shopCartItems.children().height()
					});
				}else{
					shopCartItems.css({
						maxHeight:maxH
					});
				}
				
				new IScroll(shopCartItems.get(0),{
					click:iScrollClick(),
				});
				
//				var orderList = $(this.$el).find(".order-cart-list");
//				new IScroll(orderList.get(0),{
//					scrollbars:true
//				});
				
			},
			useChecked:function(e){
				this.checkAccount=!this.checkAccount;
			},
			getPrice:function(article){
				if(article.articlePrices.length==0){
					return article.price;
				}else{
					var min = null;
					for(var i=0;i<article.articlePrices.length;i++){
						var up = article.articlePrices[i];
						if(min==null||min>up.price){
							min = up.price;
						}
					}
					return min;
				}
			}
		},
		created:function(){
			var that = this;
			this.$on("pay-success",function(){
				getArticleFamily(function(fam){
					that.familyList = fam;
				},{modeid:1,attr:that.allAttr});
				that.closeCreateOrder();
			});
			
			this.$on("charge-success",function(){
				that.showCreateOrder();
			});
			
			this.$on("bind-success",function(){
				that.showCreateOrder(null);
			});
			
			this.$dispatch("loading");
			//加载所有规格
			getAllAttr(function(attr){
				that.allAttr = attr;
				//加载所有菜品
				getArticleFamily(function(fam){
					that.familyList = fam;
					that.currentFamily = fam[0]; 
					that.$dispatch("loaded");
					//加载购物车
					loadShopCart(1,function(shopcart){
						for(var i=0;i<shopcart.length;i++){
							var c = shopcart[i];
							var art_id = c.articleId;
							var u_art = that.findArticleById(art_id);
							if(u_art){
								u_art.number = c.number;
							}
						}
					});
					setTimeout(function(){
						that.reflushArticleList();
					},50);
				},{modeid:1,attr:attr});
			});
			
			getChargeList(function(chargeRules){
				that.chargeList = chargeRules;
			});
		},
		attached :function(){
			var dom = $(this.$el);
			setTimeout(function(){
				var parentHeight = dom.parent().height();
				var logoHeight = dom.find(".menuLogo").height();
				$(".article-container").height(parentHeight-logoHeight);
			},50);
			var that = this;
		},
		events:{
			"choiceMode":function(mode){
				console.log("choiceSuccess");
				this.choiceModeDialog.mode=mode;
				this.choiceModeDialog.show=false;
				this.isCreateOrder=true;
			}
		},
		components:{
			"choice-mode-dialog":{
				mixins:[dialogMix],
				template:
					'<div class="weui_dialog_confirm" v-if="show">                                    '+
					'	<div class="weui_mask" @click="close"></div>                                                '+
					'	<div class="weui_dialog transparent choice-mode">                                  '+
					'		<a  class="weui_btn weui_btn_default" @click="choiceMode(3)">外带</a>'+
					'		<a  class="weui_btn weui_btn_primary" @click="choiceMode(1)">堂吃</a>'+
					'	</div>                                                                              '+
					'</div>                                                                                 ',
				methods:{
					choiceMode:function(mode){
						this.$dispatch("choiceMode",mode);
					}
				}
				
			},
			"article-operator":{
				props:["article","style"],
				template:
				'<span class="numberControl" :style="style" v-if="!article.isEmpty">                                                                                  '+
				'	<span v-if="article.number" @click.stop="cutA" class="cut-span">   <i class="icon iconfont minus">&#xe751;</i></span>'+
				'	<span v-if="article.number" class="art-number">{{article.number}}</span>                                   '+
				'	<span class="plus-span" @click.stop="addArticle"><i class="icon iconfont plus">&#xe6cd;</i></span>                         '+      
				'</span>                                                                                                       '+
				'<span class="numberControl font-15" :style="style" v-else>已售罄</span>'	,          
				created:function(){
					if(this.article.isEmpty){
						this.article.number=0;
					}
				},
				methods:{
					cutA:function(e){
						this.$dispatch('cut',this.article);
					},
					addArticle:function(e){
						var clickAnt = $("<span>1</span>");
						clickAnt.css({
							fontSize : "18px",
							padding : "0px 10px",
							backgroundColor : "#CDCDCD",
							color : "white",
							borderRadius : "50%",
							zIndex : 99999
						});
						var s = $(".art-count").offset();
						var a = $(e.target).offset();
						var speed = 2 + 2 / (s.top - a.top) * 20;
						clickAnt.fly({
							start : {
								left : a.left,
								top : a.top
							},
							end : {
								left : s.left,
								top : s.top,
							},
							autoPlay : true,
							speed : speed,
							vertex_Rtop : a.top - 50,
							onEnd : function() {
								clickAnt.remove();
							}
						});
						
						this.$dispatch('add',this.article);
					}
				}
			},
		}
		
	};