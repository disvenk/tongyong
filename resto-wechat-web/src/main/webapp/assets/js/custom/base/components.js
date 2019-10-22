var mode={1:"堂食",2:"外卖",3:"外带"},
    orderState={1:"未付款",2:"已付款",9:"已取消",10:"已消费",11:"已评价"},
    classState={1:"gray",2:"red",9:"gray",10:"black",11:"black"},
    productState={0:"待下单",1:"待接单",2:"等待叫号",3:"已消费",4:"已取餐",5:"下单失败"};

var subpageMix={
    props:["shop","customer","otherdata","noticelist"]
};

var noticeMix={
    props:["noticelist"],
    data:function(){
        return {
            noticeNeedToShow:true
        }
    },
    computed:{
        hasNotice:function(){
            var noticeArr = [];
            for(var i=0;i<this.noticelist.length;i++){
                var n = this.noticelist[i];
                if(n.noticeType==1||n.noticeType==3){
                    noticeArr.push(n);
                }
            }
            return noticeArr;
        },
        newNotice:function(){
            var nArr=[];
            for(var i=0;i<this.hasNotice.length;i++){
                var nt= this.hasNotice[i];
                if(!nt.isRead){
                    nArr.push(nt);
                }
            }
            return nArr;
        },
    },
    methods:{
        showNotice:function(n){
            this.$dispatch("show-notice-list",n);
        },
    },
    created:function(){
        var that = this;
        getNoticeList(null,function(noticelist){
            that.noticelist = noticelist;
            if(that.newNotice.length>0&&that.noticeNeedToShow){
                if(!getParam("dialog")){
                    that.showNotice(that.newNotice);
                }
            }
        });
    }
}

var orderDetailed = {
    props:["order"],

    computed:{
        modeName:function(){
            return mode[this.order.distributionModeId];
        },
        statusText:function(){
            var state = this.order.orderState;
            var proState =this.order.productionStatus;
            var st = "";
            switch(state){
                case 1:
                    st = orderState[state];
                    break;
                case 2:
                    st = productState[proState];
                    break;
                case 9:
                    st = orderState[state];
                    break;
                case 10:
                    st = productState[proState];
                    break;
                case 11:
                    st=orderState[state];
                    break;
            }
            return st;
        }
    },
    methods:{
        cancelOrder:function(){
            this.$dispatch("cancel-order",this.order);
        },
        pushOrderClick:function(){
            this.pushOrder();
        },
        pushOrder:function(){
            this.vailedPushOrder&&this.vailedPushOrder();
            var order = this.order;
            var that = this;
            pushOrderRequest(order.id,function(){
                that.$dispatch("message","下单成功");
                order.productionStatus=1;
                that.pushOrderSuccess&&that.pushOrderSuccess(order);
            });
        },
        payOrder:function(){
            this.$dispatch("pay-order",'wechat',this.order);
        },
        helpOrder:function(){
            var msg = "支付成功,请到"+this.order.shopName+"吧台,出示交易码"+this.order.verCode+",即可消费";
            this.$dispatch("message",msg,300000);
        },
        receiveRedPapper:function(){
            var that = this;
            getCustomerNewOrder(this.order.id,function(o){
                that.order = $.extend(that.order,o);
                that.$dispatch("receive-red-papper",that.order);
            })
        },
        continueOrder:function(){
            this.$dispatch("continue-order",this.order);
        },
    },
};

var dialogMix = {
    props:["show"],
    methods:{
        close:function(){
            this.show=false;
        }
    },
    created:function(){
        this.$watch("show",function(){
            if(this.show){
                this.onShow&&this.onShow();
            }
        });
    }
};

Vue.component("notice-dialog",{
    mixins:[dialogMix],
    props:["notice"],
    template:
    '<div class="weui_dialog_alert sencond_mask" v-if="show">                                            '+
    '	<div class="weui_mask pop_up" @click="close"></div>                                 '+
    '	<div class="weui_dialog dish_details">                                              '+
    '<div class="full-height">'+
    '		<div class="weui_dialog_bd ">                                                   '+
    '			<img class="art-image" :src="notice.noticeImage"/>                         '+
    '		</div>                                                                          '+
    '		<div class="weui_article article-desc">                                         '+
    '			<div>                                                                       '+
    '				<h3 class="article-name">{{notice.title}}</h3>                          '+
    '				<p class="article-desc">{{{notice.content}}}</p>                          '+
    '			</div>                                                                      '+
    '		</div>                                                                          '+
    '</div>'+
    '		<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div> '+
    '	</div>                                                                              '+
    '</div>                                                                                 ',
    created:function(){
        this.$watch("notice",function(){
            this.notice.content = this.notice.content.replace(/\n/g,"<br/>");
        });
    }
});

Vue.component("noticelist-dialog",{
    mixins:[dialogMix],
    props:["noticelist"],
    template:
    '<div class="weui_dialog_alert third_mask" @touchstart="start" @touchend="end" v-if="show">                                            '+
    '	<div class="weui_mask pop_up" @click="close"></div>                                 '+
    '	<div class="weui_dialog notice-dialog big-dialog">                                              '+
    '<div class="full-height" v-if="notice.noticeType==1||notice.noticeType==2">'+
    '		<div class="weui_dialog_bd ">                                                   '+
    '			<img class="art-image" :src="notice.noticeImage"/>                         '+
    '		</div>                                                                          '+
    '		<div class="weui_article article-desc">                                         '+
    '			<div>                                                                       '+
    '				<h3 class="article-name">{{notice.title}}</h3>                          '+
    '				<p class="article-desc">{{{notice.content}}}</p>                          '+
    '			</div>                                                                      '+
    '		</div>                                                                          '+
    '</div>'+
    '<div class="full-height" v-if="notice.noticeType==3">'+
    '		<div class="weui_dialog_bd big-image">                                                   '+
    '			<img class="art-image" :src="notice.noticeImage"/>                         '+
    '		</div>                                                                          '+
    '</div>'+
    '		<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div> '+
    '	</div>                                                                              '+
    '</div>                                                                                 ',
    computed:{
        notice:function(){
            var notice = this.noticelist[this.current];
            notice.content = notice.content.replace(/\n/g,"<br/>");
            addNoticeHistory(notice.id);
            return notice;
        }
    },
    data:function(){
        return {
            current:0,
            startPoint:0,
        }
    },
    methods:{
        start:function(e){
            var touch = e.targetTouches[0];
            this.startPoint = touch.pageX;
        },
        end:function(e){
            var touch = e.changedTouches[0];
            var offset = this.startPoint-touch.pageX;
            if(offset>30){
                if(this.current<this.noticelist.length-1){
                    this.current+=1;
                }
            }else if(offset<-30){
                if(this.current>0){
                    this.current-=1;
                }
            }

        }
    },
});



var orderMiniMaxByTV = {
    mixins: [orderDetailed],

    methods:{
        showOrderDetailed:function(){
            var that = this;
            getCustomerNewOrder(that.order.id,function(o){
                that.order = $.extend(that.order,o);
                //that.$dispatch("show-custom-neworder",that.order);
                that.pushOrder();
            });
        }

    }

};


var orderMiniMax = {
    mixins: [orderDetailed],
    template:
    '<div class="weui_cell item" v-if="order.orderState!=9">                                                      '+
    '	<div class="weui_cell_bd weui_cell_primary order-mini-detailed" @click="showOrderDetailed">                                          '+
    '		<p>[{{modeName}}]{{order.shopName}}</p>                                                                       '+
    '		<p><ver-code :order="order"></ver-code> <span class="font-mini">共计{{order.countWithChild||order.articleCount}}个餐品</span></p>                           '+
    '		<p>                                                                                                   '+
    '			<span class="font-mini">下单时间:{{new Date(order.lastOrderTime||order.createTime).format("yyyy-MM-dd hh:mm")}}</span> '+
    '		</p>                                                                                                  '+
    '	</div>                                                                                                    '+
    '	<div class="weui_cell_ft">                                                                                '+
    '		<span class="order-state" id="btnName">{{statusText}}</span>                                                             '+
    '		<a   class="weui_btn weui_btn_mini weui_btn_main order-operator" @click="continueOrder" v-if="order.allowContinueOrder">我要加菜</a>'+
    '		<a class="weui_btn weui_btn_mini weui_btn_main order-operator cancel-order-btn" @click="cancelOrder" v-if="(order.orderState==1||order.orderState==2)&&order.productionStatus==0">取消订单</a>   '+
    '		<a  class="weui_btn weui_btn_mini weui_btn_main order-operator"  @click="pushOrderClick" v-if="order.productionStatus==0&&order.orderState==2">{{pushOrderBtnName}}</a>       '+
    '		<a class="weui_btn weui_btn_mini weui_btn_main order-operator" @click="payOrder" v-if="order.orderState==1">微信支付</a>       '+
    '		<a class="weui_btn weui_btn_mini weui_btn_main order-operator" @click="receiveRedPapper"  v-if="order.allowAppraise">领取红包</a>       '+
    '	</div>                                                                                                    '+
    '</div>                                                                                                       ',
    data:function(){
        return {
            pushOrderBtnName:"立即下单"
        }
    },
    methods:{
        showOrderDetailed:function(){
            var that = this;
            getCustomerNewOrder(that.order.id,function(o){
                that.order = $.extend(that.order,o);
                if(o.productionStatus == 0 && o.orderState != 1){
                    that.pushOrder();
                }else{
                    that.$dispatch("show-custom-neworder",that.order);
                }
            });
        },
        autoPrint:function(o){
            var that = this;
            getCustomerNewOrder(o.id,function(order){
                that.order = $.extend(that.order,order);
                if(order.productionStatus == 0 && o.orderState != 1){
                    that.pushOrder();

                    location.href="/wechat/index?subpage=my";
                    //$(".order-state").html("等待叫号");
                    //$(".cancel-order-btn").attr("style","display:none");
                }else{
                    that.$dispatch("show-custom-neworder",that.order);
                }
            });
        },

        cancelOrder:function(){
            var that = this;
            getCustomerNewOrder(that.order.id,function(o){
                that.order = $.extend(that.order,o);
                that.$dispatch("show-custom-neworder",that.order,{
                    cancel:true
                });
            });
        }
    },
    components: {
        'ver-code': {
            props:["order"],
            template: '<span>消费码:{{order.verCode}} </span>'
        }
    },
};

var orderdetailMax=
{
    mixins: [orderDetailed],
    props:["order","show","option"],
    template:
    '<div class="weui_dialog_alert" v-if="show">                                                                    '+
    '		<div class="weui_mask pop_up" @click="close"></div>                                                                    '+
    '		<div class="weui_dialog order-desc">                                                                               '+
    '			 <div class="text-left padding-10 padding-top-20">                                                      '+
    '				<div><h4 class="pull-left mode-shop-name">[{{modeName}}]{{order.shopName}}</h4><div class="pull-right"><span class="order-state-span">{{statusText}}</span></div></div>                       '+
    '<div class="clearfix"></div>'+
    '				<p><span class="c-gray">下单时间: </span>{{new Date(order.lastOrderTime||order.createTime).format("yyyy-MM-dd hh:mm")}}</p>                       '+
    '				<div class="order-items">                                                           '+
    '					<div>                                                                   '+
    '<span class="item-name" v-if="item.type!=4" v-for="item in order.orderItems | orderBy \'createTime\'">{{item.articleName}} <span v-if="item.count>1">x{{item.count}}</span></span>'+
    '<div>'+
    '<span class="price sale font-bold font-em4">{{order.amountWithChildren||order.originalAmount}}</span>'+
    ' <span class="font-mini">共计{{order.countWithChild||order.articleCount}}个餐品</span>'+
    '</div>'+
    '					</div>                                                                                      '+
    '				</div>                                                                                          '+
    '				<div class="weui_cells no-border-bottom margin-top-0">                                                           '+
    '					<code-components :order.sync="order"></code-components>'+
    '				</div>                                                                                          '+
    '			 </div>                                                                                             '+
    '<div class="bottom-button">'+
    '<div class="order-remind" v-if="order.allowContinueOrder">加菜后，红包金额将提升</div>'+
    '			 <div class="weui_dialog_ft margin-top-0 no-border-top" v-if="option.cancel">                                                          '+
    '				<a   class="weui_btn_dialog primary cancel-order-btn" @click="cancelOrder">取消订单</a>                        '+
    '			 </div>                                                                                             '+
    '			 <div class="weui_dialog_ft margin-top-0 no-border-top" v-else>                                                          '+
    '<a  id="allowContinueOrder" class="weui_btn_dialog primary" @click="continueOrder" v-if="order.allowContinueOrder">我要加菜</a>'+
    '				<a   class="weui_btn_dialog primary" @click="pushOrderClick" id="cancelOrderBtn" v-if="order.productionStatus==0&&order.orderState==2">{{pushOrderBtnName}}</a>       '+
    '				<a  class="weui_btn_dialog primary" @click="payOrder" v-if="order.orderState==1">微信支付</a>       '+
    '				<a  class="weui_btn_dialog primary disabled" v-if="remainPackageTime.show&&!order.allowAppraise">领取红包({{remainPackageTime.time}})</a>       '+
    '				<a  class="weui_btn_dialog primary" @click="showReceiveRed"  v-if="order.allowAppraise">领取红包</a>       '+
    '			 </div>                                                                                             '+
    '<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div>'+
    '</div>'+
    '		</div>                                                                                                  '+
    '</div>		                                                                                                    ',
    components: {
        'code-components': {
            props:["order"],
            template: '<p v-if="order.orderState!=1" class="ver-code"><span class="c-gray" style="font-size:16px;line-height: 26px;">交易码:</span> <span class="ver-code">{{order.verCode}}</span></p>'
        }
    },
    data:function(){
        return {
            remainPackageTime:{show:false,time:0},
            pushOrderBtnName:"立即下单"
        }
    },
    methods:{
        close:function(){
            this.show=false;
        },
        showReceiveRed:function(){
            this.receiveRedPapper();
            this.close();
        },
        reflushOrderState:function(){
            var that = this;
            getOrderStates(that.order.id,function(o){

                if(that.order.orderState!=o.orderState){
                    that.orderStateChange&&that.orderStateChange(o.orderState);
                }
                if(that.order.productionStatus!=o.productionStatus){
                    console.log("订单状态改变:",o.productionStatus);
                    if(o.productionStatus==2){
                        getOrderById(that.order.id,function(od){
                            that.order = $.extend(that.order,od);
                            that.reflushRemainTime();
                        });
                    }
                    that.orderProductionStatusChange&&that.orderProductionStatusChange(o.productionStatus);

                }




                that.order = $.extend(that.order,o);
            })
        },
        reflushRemainTime:function(){
            var now = new Date().getTime();
            var printOrderTime = this.order.printOrderTime;
            if(!printOrderTime){
                clearInterval(this.remainTimeInt);
                this.remainTimeInt=null;
            }
            var remindTime= (now-printOrderTime)/1000;
            if(allSetting.autoConfirmTime>remindTime&&this.show){
                this.remainPackageTime.show=true;
                this.remainPackageTime.time=parseInt(allSetting.autoConfirmTime-remindTime);
                setTimeout(this.reflushRemainTime,1000);
            }else{
                this.remainPackageTime.show=false;
            }
        },
        startReflush:function(){
            this.reflushInt = setInterval(this.reflushOrderState,5000);
            this.reflushRemainTime();
        },
        stopReflush:function(){
            clearInterval(this.reflushInt);
            this.reflushInt=null;
        }
    },
    created:function(){
        var that = this;
        this.$watch("show",function(){
            if(this.show&&!this.reflushInt){
                this.startReflush();
            }else if(this.reflushInt){
                this.stopReflush();
            }
            if(this.show){
                Vue.nextTick(function(){
                    var dom = $(that.$el.nextSibling).find(".order-items").get(0);
                    new IScroll(dom);
                });
            }
        });

        this.$on("cancel-order-success",function(){
            this.show=false;
        });
    }
};





Vue.component("share-dialog",{
    mixins:[dialogMix],
    props:["appraise","setting","isshare"],
    template:
    '<div class="weui_dialog_alert sencond_mask" v-if="show">                                                                                           '+
    '   <div class="weui_mask"></div>                                                                                          '+
    '   <div class="weui_dialog big-dialog share-dialog">                                                                                 '+
    '	   <div class="full-height">                                                                                           '+
    '<div class="appraise-item-div">'+
    '	<div class="reviewListPhotoBox">                                  '+
    '		<img :src="appraise.pictureUrl">                              '+
    '	</div>                                                            '+
    '	<div class="reviewListInfo">                                      '+
    '		<div class="reviewListInfoTitle">                             '+
    '			<div class="avatar">                                      '+
    '				<img :src="appraise.headPhoto">                                         '+
    '			</div>                                                    '+
    '			<p>                                                       '+
    '				<span>{{appraise.nickName}}爱上了{{appraise.feedback}}</span>                       '+
    '			</p>                                                      '+
    '			<div class="comment-rst">                                 '+
    '				<span>{{new Date(appraise.createTime).format("MM-dd hh:mm")}}</span>                              '+
    '				<start-span :level="appraise.level"></start-span>    '+
    '			</div>                                                    '+
    '		</div>                                                        '+
    '		<div class="reviewListInfoContent">                           '+
    '			<p>{{appraise.content}}</p>                                                 '+
    '		</div>                                                        '+
    '	</div>                                                            '+
    '</div>'+
    '<div class="setting-content" v-if="isshare">'+
    '{{{setting.dialogText}}}'+
    '</div>'+
    '		<div v-if="!isshare && !setting.registered" class="bottom-button weui_dialog_ft"><a class="weui_btn_dialog primary" @click="showRegister">{{setting.registerButton}}</a></div>'+
    '		</div>                                                                                                             '+
    '		<div v-if="!isshare && setting.registered" class="bottom-button weui_dialog_ft"><a class="weui_btn_dialog primary" @click="hiddenRegister">{{setting.registerButton}}</a></div>'+
    '		<div v-if="isshare"><div class="share-line"><div class="share-point"><i class="icon-share"></i></div>'+
//			'<div class="share-alg"><p>(点击右上角分享按钮分享至朋友圈或好友)</p></div>'+
    '</div></div>                                                   '+
    ' 	<div class="dialog-close"><i  @click="close" class="icon-remove-sign"></i></div>'+
    '	</div>                                                                                                                 '+
    '</div>                          ',

    methods:{
        close:function(){
            this.show=false;
        },
        showRegister:function(){
            this.$dispatch("bind-phone");
            this.show=false;
        },
        hiddenRegister:function(){
            this.$dispatch("message","你已注册，感谢你的关注！",2000);
        },
        onShow:function(){
            var that = this;

            var setting = this.setting;
            if(this.isshare){
                var shareLink = allSetting.wechatWelcomeUrl+"?shareCustomer="+this.appraise.customerId+"&subpage=home&appraiseId="+this.appraise.id+"&dialog=sharefriend";
                console.log(shareLink);
                executeWxFunction(function(){
                    wx.onMenuShareTimeline({
                        title: setting.shareTitle, // 分享标题
                        link: shareLink, // 分享链接
                        imgUrl: setting.shareIcon, // 分享图标
                        success:function(){
                            that.show=false;
                        }
                    });
                    wx.onMenuShareAppMessage({
                        title: setting.shareTitle, // 分享标题
                        desc: "您的好友正在给您推荐 "+allSetting.brandName, // 分享描述
                        link: shareLink, // 分享链接
                        imgUrl: setting.shareIcon, // 分享图标
                        success:function(){
                            that.show=false;
                        }
                    });
                });
            }else{

            }
        }
    }
});

Vue.component("table-number-dialog",{
    mixins:[dialogMix],
    template:
    '<div class="weui_dialog_alert sencond_mask" v-if="show">                                                                                           '+
    '   <div class="weui_mask" @click="close"></div>                                                                                          '+
    '   <div class="weui_dialog ">                                                                                 '+
    '	   <div class="padding-10 ">                                                                                           '+
    '		   <h5 class="text-left">请输入座位号</h5>                                                                         '+
    '		   <div class="seat_content" style="padding:20px 40px;overflow:hidden;">                                           '+
    '			<a style="float:left;width:49%; "><li><i class="iconcode" style="font-size:30px;">&#xe647;</i></li>扫码输入</a>'+
    '			<a style="float:left;width:49%; "><li><i class="iconcode" style="font-size:35px;">&#xe63f;</i></li>键盘输入</a>'+
    '			</div>                                                                                                         '+
    '		</div>                                                                                                             '+
    '		<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div>                                                   '+
    '	</div>                                                                                                                 '+
    '</div>                                                                                                                    ',
});

Vue.component("start-span",{
    props:['level'],
    template:'<span class="starLevel">'+
    '<i class="icon-star" v-for="i of level"></i>'+
    '<i class="icon-star-empty" v-for="i of 5-level"></i></span>',
})
;

Vue.component("weui-dialog",{
    props:['msg'],
    mixins:[dialogMix],
    template:'<div class="weui_loading_toast" v-if="show">            '+
    '   <div class="weui_mask_transparent" @click="close"></div>'+
    '	<div class="weui_toast msg-dialog" @click="close">          '+
    '	   <p>{{msg}}</p>                         '+
    '	</div>                                   '+
    '</div>                                      ',
});

Vue.component("weui-loading",{
    props:["msg","show"],
    template:'<div class="weui_loading_toast" v-if="show">                     '+
    '	<div class="weui_mask_transparent" @click="close"></div>                         '+
    '	<div class="weui_toast">                                          '+
    '		<div class="weui_loading">                                    '+
    '			<div class="weui_loading_leaf weui_loading_leaf_0"></div> '+
    '			<div class="weui_loading_leaf weui_loading_leaf_1"></div> '+
    '			<div class="weui_loading_leaf weui_loading_leaf_2"></div> '+
    '			<div class="weui_loading_leaf weui_loading_leaf_3"></div> '+
    '			<div class="weui_loading_leaf weui_loading_leaf_4"></div> '+
    '			<div class="weui_loading_leaf weui_loading_leaf_5"></div> '+
    '			<div class="weui_loading_leaf weui_loading_leaf_6"></div> '+
    '			<div class="weui_loading_leaf weui_loading_leaf_7"></div> '+
    '			<div class="weui_loading_leaf weui_loading_leaf_8"></div> '+
    '			<div class="weui_loading_leaf weui_loading_leaf_9"></div> '+
    '			<div class="weui_loading_leaf weui_loading_leaf_10"></div>'+
    '			<div class="weui_loading_leaf weui_loading_leaf_11"></div>'+
    '		</div>                                                        '+
    '		<p class="weui_toast_content">{{msg||"数据加载中"}}</p>                  '+
    '	</div>                                                            '+
    '</div>                                                               ',
    methods:{
        close:function(){
            this.show=false;
        }
    }
});

Vue.component("weui_alter",{
    props:["show","title","content"],
    template:'<div class="weui_dialog_alert third_mask" v-if="show">                                                          '+
    '	<div class="weui_mask"></div>                                                         '+
    '	<div class="weui_dialog">                                                             '+
    '		<div class="weui_dialog_hd"><strong class="weui_dialog_title">{{title}}</strong></div>'+
    '		<div class="weui_dialog_bd">{{content}}</div>                                         '+
    '		<div class="weui_dialog_ft">                                                      '+
    '			<a class="weui_btn_dialog default" @click="close">确定</a>                         '+
    '		</div>                                                                            '+
    '	</div>                                                                                '+
    '</div>                                                                                   ',
    methods:{
        close:function(){
            this.show=false;
        }
    }
});

Vue.component("weui_dialog_confirm",{
    props:["show","option"],
    template:'<div class="weui_dialog_confirm sencond_mask" v-if="show">                                                              '+
    '	<div class="weui_mask"></div>                                                                           '+
    '	<div class="weui_dialog ">                                                                               '+
    '		<div class="weui_dialog_hd"><strong class="weui_dialog_title">{{option.title}}</strong></div>              '+
    '		<div class="weui_dialog_bd">{{option.content}}</div>                                                       '+
    '		<div class="weui_dialog_ft">                                                                        '+
    '			<a href="javascript:;" class="weui_btn_dialog default" @click="cancelFunction">{{option.cancalvalue||"否"}}</a>  '+
    '			<a href="javascript:;" class="weui_btn_dialog primary" @click="okFunction">{{option.okvalue||"是"}}</a>  '+
    '		</div>                                                                                              '+
    '	</div>                                                                                                  '+
    '</div>                                                                                                     ',
    methods:{
        okFunction:function(){
            var close = this.option.ok&&this.option.ok();
            this.show = close;
        },
        cancelFunction:function(){
            var close = this.option.cancel&&this.option.cancel();
            this.show = close;
        }
    }
});



Vue.component("bind-phone", {
    props: ["show", "content", "couponList"],
    template: '<div class="weui_dialog_alert" v-if="show">                                                                                    ' +
    '	<div class="weui_mask" @click="close"></div>                                                                                ' +
    '	<div class="weui_dialog user_register" style="position:absolute;" :style="{ top: dialogTop }" >                                                                                     ' +
    '<div class="register_title">{{content}}</div>     ' +
    '		                                                                               ' +
    '		<div class="weui_cells weui_cells_form">                                                                                ' +
    '			<div class="weui_cell" :class="{\'weui_cell_warn\':errMsg}" >                                                                                             ' +
    '				<div class="weui_cell_bd weui_cell_primary">                                                                    ' +
    '					<input class="weui_input" type="number"  placeholder="{{errMsg||\'请输入手机号\'}}" id="phone-input" v-model="phone">                         ' +
    '				</div>                                                                                                          ' +
    '				<div class="weui_cell_ft">                                                                                      ' +
    '<i class="weui_icon_warn"></i>   ' +
    '					<button class="weui_btn weui_btn_mini weui_btn_main" @click="getCode" v-bind="{disabled:remainTime>0}" >{{remainTime||"获取验证码"}}</button>' +
    '				</div>                                                                                                          ' +
    '			</div>                                                                                                              ' +
    '			<div class="weui_cell" :class="{\'weui_cell_warn\':codeErr}">                                                                                             ' +
    '				<div class="weui_cell_bd weui_cell_primary">                                                                    ' +
    '					<input class="weui_input" type="number"  placeholder="{{codeErr||\'请输入验证码\'}}" id="code-input" v-model="code" v-bind="{readonly:!getCodeSuccess}">                     ' +
    '				</div>                                                                                                          ' +
    '				<div class="weui_cell_ft">                                                                                      ' +
    '<i class="weui_icon_warn"></i>   ' +
    '					<button class="weui_btn weui_btn_mini weui_btn_main" @click="bindPhone" v-bind="{disabled:!getCodeSuccess}">领取红包 </button>                        ' +
    '				</div>                                                                                                          ' +
    '			</div>                                                                                                              ' +
    '		</div>     ' +
    '<div class="content information informationCoupon">' +
    '<div class="tabnav CouponCategory list3" style="border-radius: 10px 10px 0px 0px;"  v-if="showCouponList" >' +
    '<div class="tabnav-item active" style="height:15px" data-status="0"></div>' +
    '</div></div>' +
    '<div id="scroll-wapper"  v-if="showCouponList" style="overflow: hidden;" >' +
    '<ul class="CouponList mt30" id="couponList" ' +
    '</ul>' +
    '</div>' +
    '<div id="tmpl" style="display: none;">' +
    '<li class="pepper-con coupon-item">' +
    '<div class="pepper-w">' +
    '<div class="pepper pepper-blue">' +
    '<div class="pepper-l">' +
    '<p class="pepper-l-num"><i>¥</i><span data-key="value">70</span><span data-key="name" style="font-size:12px"></span></p>' +
    '<p class="pepper-l-con">使用条件：<span data-key="MODE_name"></span></p>' +
    '<p class="pepper-l-tim">使用时间：<span data-key="time"></span></p>' +
    '</div><div class="pepper-r">' +
    '<span>有效期限</span><div data-key="DATE">2015.10.01<p class="pepper-line">至</p>2015.10.24</div>' +
    '</div></div><div class="pepper-b pepper-blue"><div class="pb-con"></div>' +
    '<div class="pb-border"></div></div></div></li></div>' +
    '<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div>' +
    '	</div>                                                                                                                      ' +
    '</div>                                                                                                                         ',
    data: function () {
        return {
            phone: "",
            code: "",
            errMsg: "",
            codeErr: "",
            remainTime: 0,
            getCodeSuccess: false,
            couponType:0,
            showCouponList:true,
            dialogTop:"2%",
        }
    },
    created: function () {
        var that = this;
        this.$watch("show", function () {
            if (this.show) {
                //if (/Android/i.test(navigator.userAgent)) {
                //    $("#phone-input").click().focus();
                //}
                var h = $(".informationCoupon").height();
                $("#scroll-wapper").height($(".weui_dialog ").height()-h);
                //$("#scroll-wapper").height(300);

                //根据 链接 判断 显示的优惠券(新用户注册或者邀请注册)
                that.couponType = getParam("dialog")?"1":"0";

                var isc = new IScroll("#scroll-wapper",{click:true});
                $.post({
                    url: "customer/showCoupon",
                    data:{"couponType":that.couponType},
                    success: function (result) {
                        if (result.success) {
                            $('#couponList').text("");
                            var modeText = {0:"通用",1:"堂吃",2:"外卖"};
                            if(result.data == null || result.data.length == 0){
                                //如果没有 优惠券 则不显示 优惠券列表
                                that.showCouponList = false;
                                that.dialogTop = "25%";
                                return;
                            }
                            for(var i = 0;i< result.data.length;i++){
                                var obj = result.data[i];
                                console.log(obj);
                                var DATE;
                                if(obj.timeConsType==1){
                                    DATE = new Date().format("yyyy-MM-dd")+"至"+that.getTimeAfterDays(obj.couponValiday);
                                }else if (obj.timeConsType==2){
                                    DATE = new Date(obj.beginDateTime).format("yyyy-MM-dd")+" 至 "+new Date(obj.endDateTime).format("yyyy-MM-dd");
                                }

                                // var DATE = new Date(obj.beginDateTime).format("yyyy-MM-dd")+" 至 "+new Date(obj.endDateTime).format("yyyy-MM-dd");
                                var MODE_name  = modeText[obj.distributionModeId];
                                var time = new Date(obj.beginTime).format("hh:mm:ss")+" - "+new Date(obj.endTime).format("hh:mm:ss");
                                if(obj.couponMinMoney>0){
                                    MODE_name +=" 消费满"+obj.couponMinMoney+"元可使用"
                                }
                                var coupon = '<li class="pepper-con coupon-item"><div class="pepper-w"><div class="pepper pepper-blue" style="margin: 0">'
                                    +'<div class="pepper-l" style="text-align: left;"><p class="pepper-l-num" style="text-align: left;"><i>¥</i><span >'+ obj.couponValue +'</span><span data-key="name" style="font-size:12px">'+ obj.name+'</span></p>'
                                    +'<p class="pepper-l-con">使用条件：<span >'+MODE_name+'</span></p><p class="pepper-l-tim">使用时间：<span >'+time+'</span></p>'
                                    +
                                    '</div><div class="pepper-r"><span>有效期限</span><div >'+DATE+'</div>'
                                    +'</div></div><div class="pepper-b pepper-blue"><div class="pb-con"></div><div class="pb-border"></div></div>'
                                    +'</div></li>';
                                for(var n =0 ; n<obj.couponNumber;n++){
                                    $('#couponList').append(coupon);
                                }
                            }
                            isc.refresh();
                        }
                    }
                });

                document.addEventListener('touchmove', function(e) {
                    e.preventDefault();
                }, false);
            }
        });



    },
    methods: {
        close: function () {
            this.show = false;
        },
        cutRemainTime: function () {
            if (this.remainTime > 0) {
                this.remainTime--;
                var that = this;
                setTimeout(function () {
                    that.cutRemainTime();
                }, 1000);
            }
        },
        getTimeAfterDays:function(tdData){
            var now=new Date();
            var time=now.getTime();
            time+=1000*60*60*24*tdData;//加上3天
            now.setTime(time);
            return now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
        },

        getCode: function () {
            if (this.remainTime > 0) {
                return false;
            } else if (this.phone.length == 0) {
                this.errMsg = "请输入手机号！";
                return false;
            }
            var that = this;
            sendCode(this.phone, function (result) {
                that.errMsg = null;
                if (result.success) {
                    that.getCodeSuccess = true;
                    that.remainTime = 60;
                    that.cutRemainTime();
                    if (/Android/i.test(navigator.userAgent)) {
                        $("#code-input").focus();
                    }
                } else {
                    that.errMsg = result.message;
                }
            });
        },
        bindPhone: function () {
            var that = this;
            editPhone(this.phone, this.code,that.couponType, function (result) {
                //alert(JSON.stringify(result));
                if (result.success) {
                    that.show = false;
                    var sum = 0;
                    $.ajax({
                        url: "customer/showCoupon",
                        data:{"couponType":that.couponType},
                        success: function (result) {
                            if(result.success){
                                //alert(JSON.stringify(result));
                                for(var i = 0;i< result.data.length;i++){
                                    sum += result.data[i].couponValue * result.data[i].couponNumber;
                                }
                                that.$dispatch("message", sum+"元红包已到账   可在我的优惠券中查看", 5000);
                                that.$dispatch("bindPhoneSuccess");
                            }else{
                                that.$dispatch("message", "优惠券异常", 5000);
                            }

                        }
                    });

                    that.$dispatch('bindPhoneSuccess');
                } else {
                    that.code = "";
                    that.codeErr = result.messageesult.message;
                }
            });
        }
    }
});

Vue.component("charge-rules",{
    props:["chargelist"],
    template:
    '<div class="weui_cell weui_cells_access charge-rules" @click.stop="showDialog" v-if="chargelist.length" >        '+
    '	<div class="weui_cell_hd"></div>                               '+
    '	<div class="weui_cell_bd weui_cell_primary">                   '+
    '		<p>充值赠送                                                '+
    '			<button class="weui_btn weui_btn_mini charge-btn red"  '+
    '			v-for="c in chargelist" v-if="c.showIn"               '+
    '			@click.stop="charge(c.id)">{{c.labelText}}</button> '+
    '		</p>                                                       '+
    '	</div>                                                         '+
    '	<div class="weui_cell_ft">                                     '+
    '	</div>                                                         '+
    '</div>                                                              ',
    data:function(){
        return {
            times:0
        }
    },
    methods:{
        showDialog:function(){
            this.$dispatch('show-charge-dialog',this.chargelist);
        },
        charge:function(chargeid){
            this.$dispatch('charge',chargeid);
        }
    }
});

Vue.component("charge-rules-jump",{
    props:["chargelist"],
    template:
    '<div class="weui_cell weui_cells_access charge-rules" @click.stop="charge" v-if="chargelist.length" >        '+
    '	<div class="weui_cell_hd"></div>                               '+
    '	<div class="weui_cell_bd weui_cell_primary">                   '+
    '		<p>充值赠送                                                '+
    '			<button class="weui_btn weui_btn_mini charge-btn red"  '+
    '			v-for="c in chargelist" v-if="c.showIn"               '+
    '			@click.stop="charge(c.id)">{{c.labelText}}</button> '+
    '		</p>                                                       '+
    '	</div>                                                         '+
    '	<div class="weui_cell_ft">                                     '+
    '	</div>                                                         '+
    '</div>                                                              ',
    data:function(){
        return {
            times:0
        }
    },
    methods:{
        showDialog:function(){
            this.$dispatch('show-charge-dialog',this.chargelist);
        },
        charge:function(){
            location.href="/wechat/index?subpage=my";
            //this.$dispatch('charge',chargeid);
        }
    }
});

Vue.component("charge-dialog",{
    props:["chargeList","show"],
    template:
    '<div class="weui_dialog_confirm sencond_mask" v-if="show">                                                                    '+
    '	<div class="weui_mask" @click="close"></div>                                                                                 '+
    '	<div class="weui_dialog shadow">                                                                                     '+
    '		<div class="weui_dialog_hd"><strong class="weui_dialog_title">充值列表</strong></div>                     '+
    '		<div class="charge-list">                                                                                 '+
    '			<label class="weui_cell chareg-item" v-for="c in chargeList">                                         '+
    '				<input type="radio" name="id" :value="c.id"  v-model="id"/>  {{c.labelText}}'+
    '			</label>                                                                                              '+
    '		</div>                                                                                                    '+
    '		<div class="weui_dialog_ft margin-top-0">                                                                              '+
    '			<a href="javascript:;" class="weui_btn_dialog primary" v-if="!id">充值</a>           '+
    '			<a href="javascript:;" class="weui_btn_dialog primary" @click.stop="this.$dispatch(\'charge\',id,close)" v-else >充值</a>    '+
    '		</div>                                                                                                    '+
    '<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div>'+
    '	</div>                                                                                                        '+
    '</div>                                                                                                           ',
    data:function(){
        return {
            id:null,
        }
    },
    methods:{
        close:function(){
            this.show=false;
        },
    },
    created:function(){
        var that = this;
        this.$on("charge-success",function(){
            that.close();
        });
    }
});


Vue.component("pay-dialog",{
    props:["order","show"],
    template:
    '<div class="weui_dialog_confirm sencond_mask" v-if="show">                                                               '+
    '	<div class="weui_mask pop_up" @click="close"></div>                                                          '+
    '	<div class="weui_dialog shadow middle-dialog">                                                                     '+
    '<div class="full-height">'+
    '		<p class="choice-pay-title">                                                              '+
    '			<span>实付金额</span>                                                                 '+
    '			<span class="sale price pull-right font-blod" >{{order.paymentAmount}}元</span>      '+
    '		</p>                                                                                      '+
    '		<p class="mode-title">                                                              '+
    '			请选择支付方式      '+
    '		</p>                                                                                      '+
    '		<div class="weui_dialog_bd">                                                              '+
    '			<label>                                                                               '+
    '				<input type="radio" checked />  微信支付                                          '+
    '				<img src="assets/img/wxpay.png" class="pull-right" style="width:25px;"/>                 '+
    '			</label>                                                                              '+
    '		</div>                                                                                    '+
    '<div class="bottom-button">'+
    '		<div class="weui_dialog_ft">                                                              '+
    '			<a href="javascript:;" class="weui_btn_dialog primary" @click="payorder">确认支付</a>'+
    '		</div>                                                                                    '+
    '</div>'+
    '</div>'+
    '<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div>'+
    '	</div>                                                                                        '+
    '</div>                                                                                           ',
    methods:{
        close:function(){
            this.show=false;
        },
        payorder:function(){
            console.log(this.order.id);
            if(this.order.id){
                this.$dispatch("pay-order","wechat",this.order);
            }else{
                console.log("订单为为保存的订单，保存订单先");
                var that = this;
                this.$dispatch("save-order",this.order,function(order){
                    that.order=order;
                    that.payorder();
                });
            }
        }
    },
    created:function(){
        var that = this;
        this.$on("pay-success",function(){
            that.close();
        });
    }
});


Vue.component("iframe-dialog",{
    mixins:[dialogMix],
    props:["src"],
    template:
    '<div class="weui_dialog_alert sencond_mask" v-if="show">                      '+
    '	<div class="weui_mask pop_up" @click="close"></div>'+
    '	<div class="weui_dialog iframe-dialog">            '+
    '		<iframe :src="src">            '+
    '		</iframe>                                      '+
    '<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div>'+
    '	</div>                                             '+
    '</div>                                                ',
});




Vue.component("order-appraise-dialog",{
    mixins: [orderDetailed],
    props:["show"],
    template:
    '<div class="weui_dialog_alert sencond_mask" v-if="show">                                                                           '+
    '	<div class="weui_mask pop_up" @click="close"></div>                                                                    '+
    '		<div class="weui_dialog order-desc">                                                                           '+
    '			<div class="text-left appraise-dialog" :class="currentClass">                                                   '+
    '				<div class="form-group">'+
//					'<p><span>{{new Date(order.createTime).format("yyyy-MM-dd hh:mm")}}</span></p>'+
    '<div >'+

    '<h4 class="shop-name-money"> {{order.shopName}} <span class="c-gray">账单消费{{order.amountWithChildren||order.orderMoney}}元</span></h4>'+
    '</div>'+
    '				</div>'+
    '				<div class="form-group">                                                                    '+
    '					<p><span id="satisfied">满意度</span></p>                                                                        '+
    '					<div class="appraise-level c-red">                                                      '+
    '						 <span v-for="n in 5" >                                                             '+
    '							<i :class="{\'icon-star-empty\':level<n,\'icon-star\':level>=n}" @click="selectLevel($index)"></i>                                                 '+
    '						 </span>                                                                            '+
    '					</div>                                                                                  '+
    '				</div>                                                                                      '+
    '				<div class="form-group">                                                                    '+
    '					<p><span>{{{appraiseTypeName}}}</span></p>                                                                       '+
    '					<div class="appraise-items text-left">                                                                                   '+
    '						<div>'+
    '<span class="item-name" v-for="item in showphotos" @click="choiceItem(item)" :class="{\'active\':isActive(item)}">{{item.articleName}}</span>'+
    '<span class="item-name" v-if="item.type!=4"  v-for="item in order.orderItems | orderBy \'createTime\'" @click="choiceItem(item)" :class="{\'active\':isActive(item)}">{{item.articleName}}</span></div>             '+
    '					</div>                                                                                  '+
    '				</div>                                                                                      '+
    '				<div class="form-group ">                                                                    '+
    '					<p><span>消费评价</span></p>                                                                         '+
    '					<div class="weui_cell_bd weui_cell_primary">                                            '+
    '						<textarea class="weui_textarea" placeholder="请输入评论" rows="6" v-model="content"></textarea> '+
    '					</div>                                                                                  '+
    '				</div>                                                                                      '+
    '			</div>                                                                                          '+
    '<div class="bottom-button">'+
    '			<div class="weui_dialog_ft margin-top-0">                                                       '+
    '				<a class="weui_btn_dialog default" v-if="level<0">请选择评分</a> '+
    '				<a class="weui_btn_dialog primary" @click="saveAppraise" v-if="level>=0">领取红包</a> '+
    '			</div>                                                                                          '+
    '<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div>'+
    '</div>'+
    '		</div>                                                                                              '+
    '	</div>                                                                                                  '+
    '</div>',
    data:function(){
        return {
            level:-1,
            currentItem:null,
            feedback:[],
            content:"",
            showphotos:[],
            itemsIsc:null
        }
    },
    computed:{
        currentClass:function(){
            if(this.isGood){
                return "good-appraise";
            }else{
                return "bad-appraise";
            }
        },
        appraiseTypeName:function(){
            if(this.isGood){
                return "<font style='color: red'>消费点赞</font>";
            }else{
                return "<font style='color: black'>消费投诉</font>";
            }
        },
        isGood:function(){
            if(this.level >= 4){
                return true;
            }else{
                return false;
            }
        },
        appraiseType:function(){
            if(this.isGood){
                return this.currentItem.showType||1;
            }else{
                return 4;
            }
        }
    },
    created:function(){
        var that = this;
        this.$on("save-appraise-success",function(){
            that.order.allowAppraise=false;
            that.order.orderState=11;
            that.close();
        });
        this.$watch("show",function(){
            if(this.show){
                var appraiseItems = $(that.$el.nextSibling).find(".appraise-items").get(0);
                that.itemsIsc = new IScroll(appraiseItems,{
                    click:iScrollClick()
                });
                getShowPhoto(function(list){
                    for(var i=0;i<list.length;i++){
                        var p = list[i];
                        p.id=p.id;
                        p.articleName=p.title;
                    }
                    that.showphotos = list;
                    Vue.nextTick(function(){
                        that.itemsIsc.refresh();
                    })
                });
            }else{
                this.level=-1;
                this.currentItem=null;
                this.feedback=[];
                this.content="";
            }
        });
    },
    methods:{
        selectLevel:function(index){
            this.level=index;
            if(index == 0){
                $('#satisfied').html("<label style='color: black'>非常差</label>");
            }else if (index == 1){
                $('#satisfied').html("<label style='color: black'>很差</label>");
            }else if (index == 2){
                $('#satisfied').html("<label style='color: black'>差</label>");
            }else if (index == 3){
                $('#satisfied').html("<label style='color: black'>需要改进</label>");
            }else if (index == 4){
                $('#satisfied').html("<label style='color: red'>非常满意</label>");
            }
        },
        choiceItem:function(item){
            if(this.isGood){
                this.currentItem= item;
            }else{
                for(var i=0;i<this.feedback.length;i++){
                    var fb  = this.feedback[i];
                    if(item.id==fb.id){
                        this.feedback.splice(i,1);
                        return false;
                    }
                }
                this.feedback.push(item);
            }
        },
        isActive:function(item){
            if(this.isGood){
                return this.currentItem.id==item.id;
            }else{
                for(var i=0;i<this.feedback.length;i++){
                    var bk = this.feedback[i];
                    if(bk.id==item.id){
                        return true;
                    }
                }
            }
            return false;
        },
        close:function(){
            this.show=false;
        },
        error:function(msg){
            this.$dispatch("message",msg);
        },
        saveAppraise:function(){
            if((this.isGood&&!this.currentItem)||(!this.isGood&&!this.feedback.length)){
                this.error("请选择要评价的菜品")
            }else if(this.level==-1){
                this.error("请选择评分");
            }else if(this.content.length<allSetting.goodAppraiseLength&&this.isGood){
                this.error("请输入"+allSetting.goodAppraiseLength+"字以上的评论");
            }else if(this.content.length<allSetting.badAppraiseLength&&!this.isGood){
                this.error("请输入"+allSetting.badAppraiseLength+"字以上的评论");
            }else{
                var type = this.appraiseType;
                var feedbackText = "";
                var id=null;
                if(this.isGood){
                    id = this.currentItem.id;
                    feedbackText = this.currentItem.articleName;
                }else{
                    for(var i=0;i<this.feedback.length;i++){
                        feedbackText += this.feedback[i].articleName +" ";
                    }
                }

                var that = this;

                var appraise = {
                    type:type,
                    articleId:id,
                    feedback:feedbackText,
                    level:this.level+1,
                    orderId:this.order.id,
                    content:this.content,
                };
                this.$dispatch("save-appraise",appraise);
            }
        }
    }
});

Vue.component("red-papper-dialog",{
    mixins:[dialogMix],
    props:["name","title","order"],
    template:
    '<div class="weui_dialog_confirm sencond_mask red_papper" v-if="show">                     '+
    '	<div class="weui_mask" @click="close"></div>                                 '+
    '	<div class="weui_dialog red_bg">                              '+
    '		<div class="topcontent">                                  '+
    '			<div class="red_avatar">                              '+
    '				<img :src="logo">                 '+
    '				<span class="close" @click="close">+</span>                      '+
    '			</div>                                                '+
    '			<h2>{{name}}</h2>                                         '+
    '			<span class="text">发了一个红包</span>                '+
    '			<div class="red_description">{{title}}</div> '+
    '		</div>                                                    '+
    '		<div class="chai" id="chai">                              '+
    '			<span @click="openPapper">開</span>                                       '+
    '		</div>                                                    '+
    '   </div>                                                        '+
    '</div>                                                           ',
    methods:{
        openPapper:function(e){
            this.show=false;
            this.$dispatch("open",{
                title:this.title,
                name:this.name,
                money:this.money
            });
        }
    },
    data:function(){
        return {
            logo:IMAGE_SERVICE+allSetting.redPackageLogo
        }
    }
});

Vue.component("reward-dialog",{
    mixins:[dialogMix],
    props:["appraise"],
    template:
    '<div  v-if="show">'+
    '<div class="weui_dialog_confirm">                     '+
    '	<div class="weui_mask" @click="close"></div>                                 '+
    '<div class="weui_dialog ex_dialog reward-dialog">'+
    '<div class="full-height">'+
    '<div class="red_bag_open">                                                                                 '+
    '	<div class="red_open">                                                                                              '+
    '		<div class="red_open_topcontent">                                                                               '+
    '		</div>                                                                                                          '+
    '		<div class="red_open_avatar">                                                                                   '+
    '			<img :src="logo">                                                                           '+
    '		</div>                                                                                                          '+
    '		<h2>{{allSetting.brandName}}</h2>                                                                                        '+
    '		<div class="red_open_description">{{rewardSetting.title}}</div>                                                               '+
    '	</div>                                                                                                              '+
    '	<div class="reward-detailed">                                                                            '+
    '		<div class="reward-area">'+
    '<p class="reward-t">打赏对象:</p>'+
    '<p class="reward-t-span"><span>对象名称{{appraise.feedback}}</span></p>'+
    '<p class="reward-sg2">（您的打赏是我们做得更好的动力源泉）</p>'+
    '</div>'+
    '		<div class="reward-area">'+
    '<div class="reward-value-div">'+
    '<span class="reward-value" v-for="n in 6" @click="paymentShow(moneyList[n])"><span class="reward-money">{{moneyList[n]}}</span></span>'+
    '</div>'+
    '<div class="other-money" @click="inputMoneyShow"><a >其他金额</a></div>'+
    '</div>'+
    '	</div>                                                                                                              '+
    '</div>                                                                                                                 '+
    '</div>'+
    '</div>'+
    '</div>'+
    '<input-number-dialog :show.sync="inputNumberDialog.show" @yes="inputYes"></input-number-dialog> '+
    '<payment-dialog :show.sync="paymentDialog.show" :appraise="appraise" :customer="customer" :money="paymentDialog.money"></payment-dialog> '+
    '</div>',
    data:function(){
        return {
            allSetting:allSetting,
            rewardSetting:{},
            inputNumberDialog:{show:false},
            paymentDialog:{show:false,money:0},
            customer:{},
        }
    },
    computed:{
        moneyList:function(){
            var moneyList = this.rewardSetting.moneyList;
            var reg = /[\d]{1,},[\d]{1,},[\d]{1,},[\d]{1,},[\d]{1,},[\d]{1,}/;
            if(moneyList&&moneyList.match(reg)){
                return moneyList.match(reg)[0].split(",");
            }else{
                return [1,2,8,12,18,28];
            }
        }
    },
    methods:{
        inputYes:function(number){
            this.paymentShow(number);
        },
        inputMoneyShow:function(){
            this.inputNumberDialog.show=true;
        },
        inputMoneyClose:function(){
            this.inputNumberDialog.show=false;
        },
        paymentShow:function(number){
            console.log("打赏 :"+number)	;
            this.paymentDialog.show=true;
            this.paymentDialog.money=parseFloat(number);
        },
        paymentClose:function(){
            this.paymentDialog.show=false;
        },
        onShow:function(){
            var that = this;
            getRewardDetailed(function(setting){
                that.rewardSetting=setting;
            });
            getCustomer(function(customer){
                that.customer=customer;
            });
        }
    },
    components: {
        'input-number-dialog': {
            mixins:[dialogMix],
            data:function(){
                return {
                    number:"",
                }
            },
            template:
            '<div class="weui_dialog_confirm sencond_mask" v-if="show">                     '+
            '	<div class="weui_mask" @click="close"></div>                                 '+
            '<div class="weui_dialog middle-top">'+
            '<div class="full-height">'+
            '<div class="weui_dialog_hd"><strong class="weui_dialog_title">其他金额</strong></div>'+
            '<div class="weui_dialog_bd">'+
            '<div class="weui_cell">                                                                  '+
            '	<div class="weui_cell_hd">金额(元)&nbsp;&nbsp;</div>                  '+
            '	<div class="weui_cell_bd weui_cell_primary">                                          '+
            '		<input class="weui_input" type="number" v-model="number">'+
            '	</div>                                                                                '+
            '</div>                                                                                   '+
            '</div>                       '+
            '<div class="weui_dialog_ft">                                                         '+
            '	<a class="weui_btn_dialog primary" @click="yes">确定</a>                   '+
            '</div>                                                                               '+
            '</div>'+
            '<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div>'+
            '</div>'+
            '</div>',
            methods:{
                yes:function(){
                    var money = parseFloat(this.number);
                    console.log(money);
                    if(!money||money=="NaN"||money<0.1){
                        this.$dispatch("message","请输入至少0.1元的打赏金额哦！",2000);
                        return false;
                    }
                    this.$dispatch("yes",this.number);
                    this.close();
                }
            }
        },
        'payment-dialog': {
            mixins:[dialogMix],
            props:["customer","money","appraise"],
            template:
            '<div class="weui_dialog_confirm sencond_mask" v-if="show">                     '+
            '	<div class="weui_mask" @click="close"></div>                                 '+
            '<div class="weui_dialog middle-top">'+
            '<div class="full-height">'+
            '<div class="weui_dialog_hd"><strong class="weui_dialog_title">打赏</strong></div>'+
            '<div class="weui_dialog_bd">'+
            '<div class="reward-hd-div">'+
            '<h1>￥{{money.toFixed(2)}}</h1>'+
            '</div>'+
            '<div class="weui_cells">                           '+
            '	<div class="weui_cell" v-if="customer.account>0">                         '+
            '		<div class="weui_cell_bd weui_cell_primary">'+
            '			<p>使用余额: {{customer.account}} 元</p>                         '+
            '		</div>                                      '+
            '       <div class="weui_cell_ft"><i class="icon-check"></i></div>'+
            '	</div>                                          '+
            '	<div class="weui_cell" v-if="needWechat">                         '+
            '		<div class="weui_cell_bd weui_cell_primary">'+
            '			<p>微信支付: {{needWechat}} 元</p>                         '+
            '		</div>                                      '+
            '	</div>                                          '+
            '</div>                                             '+
            '</div>                       '+
            '<div class="weui_dialog_ft">                                                         '+
            '	<a href="javascript:;" class="weui_btn_dialog primary" @click="createRewardOrder">确定</a>                   '+
            '</div>                                                                               '+
            '</div>'+
            '<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div>'+
            '</div>'+
            '</div>',
            computed:{
                needWechat:function(){
                    var m = this.money-this.customer.account;
                    if(m>0){
                        return m;
                    }else{
                        return 0;
                    }
                }
            },
            methods:{
                createRewardOrder:function(){

                    console.log(this.appraise.id,this.money);
                }
            }

        }
    },
});

//未注册则在每次进入页面时弹出此红包
Vue.component("red-papper-dialog-registered", {
    mixins: [dialogMix],
    props: ["name", "title"],
    template: '<div class="weui_dialog_confirm sencond_mask red_papper" v-if="show">                     ' +
    '	<div class="weui_mask" @click="close"></div>                                 ' +
    '	<div class="weui_dialog red_bg">                              ' +
    '		<div class="topcontent">                                  ' +
    '			<div class="red_avatar">                              ' +
    '				<img :src="logo">                 ' +
    '				<span class="close" @click="close">+</span>                      ' +
    '			</div>                                                ' +
    '			<h2>{{name}}</h2>                                         ' +
    '			<span class="text">发了一个红包</span>                ' +
    '			<div class="red_description">{{title}}</div> ' +
    '		</div>                                                    ' +
    '		<div class="chai" id="chai">                              ' +
    '			<span @click="openRegisteredPapper">開</span>                                       ' +
    '		</div>                                                    ' +
    '   </div>                                                        ' +
    '</div>                                                           ',
    methods: {
        openRegisteredPapper: function (e) {
            this.show = false;
            this.$dispatch("open", {
                title: this.title,
                name: this.name,
                money: this.money
            });
        }
    },
    data: function () {
        return {
            logo: IMAGE_SERVICE + allSetting.redPackageLogo
        }
    }
});

Vue.component("red-papper-open",{
    mixins:[dialogMix],
    props:["name","title","money","appraise"],
    template:
    '<div class="weui_dialog_confirm sencond_mask red_papper" v-if="show">                     '+
    '	<div class="weui_mask" @click="close"></div>                                 '+
    '<div class="weui_dialog order-desc">'+
    '<div class="red_bag_open">                                                                                 '+
    '	<div class="red_open">                                                                                              '+
    '		<div class="red_open_topcontent">                                                                               '+
    '<div class="red_open_avatar" style="position:absolute;top:5px;left:15px;transform: rotate(45deg);font-size: 2em"> '+
    '				<span class="close"   @click="close">+</span> </div>                     '+
    '		</div>                                                                                                          '+
    '		<div class="red_open_avatar">                                                                                   '+
    '			<img :src="logo">                                                                           '+
    '		</div>                                                                                                          '+
    '		<h2>{{name}}的红包</h2>                                                                                        '+
    '		<div class="red_open_description">恭喜发财，大吉大利！</div>                                                               '+
    '		<div class="red_open_get"> <span>{{money}}</span>元</div>                                                       '+
    '		<a class=" pocket_money"  @click="openAccount">已存入余额，可直接消费</a>                                                             '+
    '	</div>                                                                                                              '+
    '	<div class="" v-if="appraise.canReward">                                                                            '+
    '		<div class="reward-btn" @click="clickReward"> <span>赏</span> </div>                                              '+
    '		<div class="reward-bg"></div>'+
    '		<div class="reward-sg">客官，对我们的服务满意吗？打赏点零花钱吧！</div>'+
    '	</div>                                                                                                              '+
    '	<div class="weui_cells red_open_people" v-else>                                                                            '+
    '		<a class="words">余额可在付款时直接抵用现金</a>                                                 '+
    '	</div>                                                                                                              '+
    '</div>                                                                                                                 '+
    '</div>'+
    '</div>',
    data:function(){
        return {
            logo:IMAGE_SERVICE+allSetting.redPackageLogo
        }
    },
    methods:{
        openAccount:function(){
            this.show=false;
            this.$dispatch('open-iframe','customer/informationAccount');
        },
        onShow:function(){
            console.log(this.appraise.id);
        },
        clickReward:function(){
            this.$dispatch("open-reward-dialog",this.appraise)
            this.close();
        }
    }
});

Vue.component("callnumber-dialog",{
    mixins:[dialogMix],
    props:["order","customer"],
    template:
    '<div class="weui_dialog_alert" v-if="show">                                                       '+
    '	<div class="weui_mask" @click="close"></div>                                                      '+
    '	<div class="weui_dialog order-desc callnumber">                                                          '+
    '		<div class="full-height">                                                       '+
    '			<div class="clearfix"><span class="callnumber-title">等待队列</span></div>                                              '+
    '			<div class="call-list" >                                                    '+
    '				<p v-for="o in list1" :class="{active:o.active}">{{o.text}}</p>                                                           '+
    '			</div>			                                                           '+
    '			<div class="call-list" >                                                    '+
    '				<p v-for="o in list2" :class="{active:o.active}">{{o.text}}</p>                                                           '+
    '			</div>			                                                           '+
    '			<div class="call-list" >                                                    '+
    '				<p v-for="o in list3" :class="{active:o.active}">{{o.text}}</p>                                                           '+
    '			</div>			                                                           '+
    '			<div class="clearfix"></div>'+
    '<div class="bottom-button text-center">'+
    '			<p v-if="orderlist!=null">您的前面还有 {{beforeNumber}} 位在等待</p>                              '+
    '			<p v-else><i class="icon-spinner icon-spin"></i>正在加载中...</p>'+
    '</div>'+
    '		</div>                                                                         '+
    '		<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div>'+
    '	</div>                                                                             '+
    '</div>                                                                                ',
    computed:{
        oid:function(){
            return this.order.id;
        },
        beforeNumber:function(){
            var orderlist = this.orderlist;
            for(var i=0;i<orderlist.length;i++){
                var od = orderlist[i];
                if(od.id==this.order.id){
                    return i;
                }
            }
            return orderlist.length;
        },
        currentCode:function(){
            return this.order.verCode;
        },
        list1:function(){
            var list = this.getList(0,10);
            return list;
        },
        list2:function(){
            var list = this.getList(10,10);
            return list;
        },
        list3:function(){
            var list = this.getList(20,10);
            if(list.length>=10){
                list[8] = {text:"...."};
                list[9] = {text:this.currentCode};
                console.log("larger");
            }
            return list;
        }
    },
    methods:{
        getList:function(begin,length){
            var arr = [];
            if(this.orderlist.length>begin){
                var listLength = begin+length;
                if(listLength>this.orderlist.length){
                    listLength=this.orderlist.length;
                }
                var hasCurrent = false;
                for(var i=begin;i<listLength;i++){
                    var order = this.orderlist[i];
                    var nickname = order.remark;

                    var showText={text:order.remark};
                    if(this.oid==order.id){
                        hasCurrent=true;
                        showText.active=true;
                    }
                    arr.push(showText);
                }
            }
            return arr;
        },
    },
    data:function(){
        return {
            orderlist:null,
            orderListIntr:null,
            orderStateIntr:null,
        }
    },
    created:function(){
        var that = this;
        this.$watch("show",function(){
            if(this.show){
                findReadyOrderList(null,function(orderlist){
                    that.orderlist = orderlist;
                });

                that.orderListIntr = setInterval(function(){
                    findReadyOrderList(null,function(orderlist){
                        that.orderlist = orderlist;

                    });
                },3000);
                that.orderStateIntr = setInterval(function(){
                    getOrderStates(that.oid,function(o){
                        that.order = $.extend(that.order,o);
                        if(o.productionStatus==3){
                            that.$dispatch("show-custom-neworder",that.order);
                            that.show=false;
                            that.$dispatch("message","已经叫号啦！请取餐！",300*3000);
                        }
                    });
                },3000);


            }else{
                if(that.orderListIntr){
                    clearInterval(that.orderListIntr);
                    that.orderListIntr=null;
                }
                if(that.orderStateIntr){
                    clearInterval(that.orderStateIntr);
                    that.orderStateIntr=null;
                }
            }
        });
    }
});

Vue.component("shoplist-dialog",{
    mixins:[dialogMix],
    props:["shoplist"],
    template:'<div class="weui_dialog_alert" v-if="show">'+
    '<div class="weui_mask pop_up" @click="close"></div>'+
    '<div class="weui_dialog map-dialog">'+
    '<div class="full-height">'+
    '<div class="weui_dialog_bd" id="map-div">map'+'</div>'+
    '<div class="shop-list">'+
    '<div class="weui_cells weui_cells_access">'+
    '<div class="weui_cell" v-for="shop in dis_shoplist | orderBy \'distance\'" @click="switchShop(shop.id)">'+
    '<div class="weui_cell_bd weui_cell_primary">'+
    '<p>{{shop.name}}</p>'+
    '</div>'+
    '<div class="weui_cell_ft">{{shop.distance}}米</div>'+
    '</div>'+
    '</div>'+
    '</div>'+
    '</div>'+
    '<div class="dialog-close" @click="close"><i class="icon-remove-sign"></i></div>'+
    '</div>'+
    '</div>',
    data:function(){

        return {
            map:null,
            lng:121.506377,
            lat:31.245105,
            myMarker:null,
        }
    },
    computed:{
        dis_shoplist:function(){
            var shoplist = [];
            var mypoint = new BMap.Point(this.lng,this.lat);
            for(var i =0;i<this.shoplist.length;i++){
                var s = this.shoplist[i];
                var shop = $.extend({},s);
                shoplist.push(shop);
                var pointB = new BMap.Point(shop.longitude,shop.latitude);
                var distance = this.map.getDistance(mypoint,pointB).toFixed(2);
                shop.distance = distance;
            }
            return shoplist;
        }
    },
    methods:{
        addBlueMarker:function(lng,lat){
            if(this.map){
                var toPoint = new BMap.Point(lng, lat);
                var myIcon = new BMap.Icon("http://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25), {
                    offset: new BMap.Size(10, 25), // 指定定位位置
                    imageOffset: new BMap.Size(0, 0 - 10 * 25) // 设置图片偏移
                });
                var marker = new  BMap.Marker(toPoint,{icon:myIcon});
                this.map.addOverlay(marker);
                return marker;
            }
        },
        addMyMarker:function(lng,lat){
            if(this.myMarker){
                this.myMarker.hide();
                this.myMarker = addBlueMarker(lng,lat);
                this.lng = lng;
                this.lat = lat;
            }
        },
        addMarker:function(lng,lat){
            if(this.map){
                var point = new BMap.Point(lng,lat);
                var marker = new BMap.Marker(point);
                this.map.addOverlay(marker);
                return marker;
            }
        },
        switchShop:function(sid){
            this.$dispatch("switch-shop",sid);
        }
    },
    ready:function(){
        this.$watch("show",function(){
            if(this.show){
                var map = new BMap.Map("map-div");            // 创建Map实例
                var point = new BMap.Point(this.lng,this.lat); // 创建点坐标
                map.centerAndZoom(point,16);
                map.enableScrollWheelZoom();
                map.addControl(new BMap.NavigationControl());
                this.map= map;
                console.log("加载成功");
                if(!this.myMarker){
                    var that  = this;
                    wx.getLocation({
                        type : 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                        success : function(res) {
                            var lng = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                            var lat = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                            var ggPoint = new BMap.Point(lng, lat);
                            var convertor = new BMap.Convertor();
                            var translateCallback = function(data) {
                                if (data.status === 0) {
                                    var point = data.points[0];
                                    that.addMyMarker(point.lng,point.lat);
                                    map.panTo(new BMap.Point(point.lng,point.lat));
                                    that.lng=point.lng;
                                    that.lat=point.lat;
                                }
                            }
                            var pointArr = [];
                            pointArr.push(ggPoint);
                            convertor.translate(pointArr, 3, 5, translateCallback);
                        }
                    });
                    console.log("获取本地地址");
                }
                if(this.shoplist){
                    for(var i =0;i<this.shoplist.length;i++){
                        var shop =this.shoplist[i];
                        console.log(shop.longitude,shop.latitude);
                        this.addMarker(shop.longitude,shop.latitude);
                    }
                }
            }else{
                this.map = null;
            }
        })
    }
});

