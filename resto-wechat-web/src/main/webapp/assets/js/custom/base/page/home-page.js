var homeBaseMix = {
		mixins:[subpageMix,noticeMix],
		template:$("#home-temp").html(),
		data:function(){
			return {
				notices:[],
				advert:{},
				appraiseCount:{},
				countList:[],
				appraiseList:[],
				pictureList:[],
				maxLevel:5,
				minLevel:4,        
				currentPage:0,
				showCount:3,
				isLoad:false,
				isOver:false,
				appraiseFilterDialog:{show:false}
			}
		},
		methods:{
			showFilter:function(){
				this.appraiseFilterDialog.show=true;
			},
			openshoplocation:function(){
				wxOpenLocation(parseFloat(this.shop.latitude), parseFloat(this.shop.longitude),this.shop.name, this.shop.address);
			},
			reloadAppraise:function(min,max){
				this.isLoad=false;
				this.isOver=false;
				this.currentPage=0;
				this.minLevel=min;
				this.maxLevel=max;
				this.appraiseList=[];
				this.loadNextPage();
			},
			reflushIsc:function(){
				if(!this.isc){
					var that = this;
					this.isc = new IScroll(this.$el,{
						probeType : 2,
						click:iScrollClick(),
					});
					this.isc.on("scrollEnd",function(){
						if (this.y <= this.maxScrollY) {
							that.loadNextPage();
							this.refresh();
						}
					});
				}
				this.isc.refresh();
			},
			loadNextPage:function(){
				var that = this;
				if(!that.isLoad&&!that.isOver){
					that.isLoad = true;
	        		var option = {
		        			currentPage:that.currentPage,
		        			minLevel:that.minLevel,
		        			maxLevel:that.maxLevel,
		        			showCount:that.showCount
		        		};
	        		getAppraiseList(function(appraiseList){
	        			for(var i in appraiseList){
		        			that.appraiseList.push(appraiseList[i]);
	        			}
	        			if(appraiseList.length<that.showCount){
	        				that.isOver=true;
	        			}
	        			that.isLoad = false;
	        		},option);
	        		that.currentPage = that.currentPage+that.showCount;
	        	}
			}
		},
		computed:{
			monthStar:function(){
				var monthStar= 5;
				if(this.countList.length>0){
					monthStar = this.countList[0].AVG_SCORE/100*5;
				}
				return Math.round(monthStar);
			},
			monthScore:function(){
				var monthScore = 100;
				if(this.countList.length>0){
					monthScore = this.countList[0].AVG_SCORE.toFixed(2);
				}
				return monthScore;
			},
		},
		ready:function(){
			var that = this;
			getShopAdvert(function(advert){
				that.advert = advert;
				Vue.nextTick(function(){
					that.reflushIsc();
				});
			});
			
			this.loadNextPage();
			
			this.$watch("appraiseList",function(){
				Vue.nextTick(function(){
					setTimeout(function(){
						that.reflushIsc();
					},50);
				});
			});
			getAppraiseCount(function(appraiseCount,countList){
				that.appraiseCount = appraiseCount;
				that.countList = countList;
				Vue.nextTick(function(){
					setTimeout(function(){
						that.reflushIsc();
					},50);
				});
			});
			getHomePicture(function(list){
				that.pictureList = list;
				if(list.length>1){
					Vue.nextTick(function () {
						new Swiper('.swiper-container', {
							direction : 'horizontal',
							loop : true,
							autoplay : 5000,
						});
					});
				}
			});
			
		},
		components:{
			"appraise-filter-dialog":{
				props:["show","countlist"],
				template:
					'<div class="weui_dialog_confirm" v-if="show">                                                             '+
					'	<div class="weui_mask pop_up" @click="close"></div>                                                       '+
					'	<div class="weui_dialog">                                                                  '+
					'		 <div class="weui_dialog_hd"><strong class="weui_dialog_title">评分列表</strong></div> '+
					'		 <div class="weui_dialog_bd appraise-count-bd">                                                          '+
					'			<p v-for="count in countlist">                                                     '+
					'				{{count.YEARMONTH}} 评论( {{count.COUNT}}条 {{count.AVG_SCORE.toFixed(2)}}分 )'+
					'			</p>                                                                               '+
					'		 </div>                                                                                '+
					'		 <div class="weui_dialog_ft">                                                          '+
					'			<a class="weui_btn_dialog primary" @click="filter(0,5)">全部评价</a>                                    '+
					'			<a class="weui_btn_dialog primary" @click="filter(5,5)">只看好评</a>                                    '+
					'			<a class="weui_btn_dialog primary" @click="filter(0,4)">只看差评</a>                                    '+
					'		 </div>                                                                                '+
					'	</div>                                                                                     '+
					'</div>                                                                                        ',
				created:function(){
				},
				methods:{
					close:function(){
						this.show=false;
					},
					filter:function(min,max){
						this.$dispatch("filter",min,max);
						this.show=false;
					}
				}
			}
		}
	};

