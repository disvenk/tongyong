/**
 * 电视叫号模式
 */
Vue.component("ready-order-component", {
    mixins: [posmix],
    data: function () {
        return {
            hasCalled: false,
            unPrint: true
        }
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
    created: function () {
        this.getPrinterList();
        this.startPrinterTask();
        this.getPosOrder();//得到 POS 中新列表订单中，未处理的订单，防止页面刷新后，新订单不显示。
        this.startGetNewOrder();
        this.startGetErrorOrder();
        this.startCallOrder();
    }
});

Vue.component("refresh-tv", {
    mixins: [posmix],
    template:
        '<button class="btn btn-success" @click="refreshTvBtn" >重连电视</button>',
    methods: {
        refreshTvBtn: function () {
            window.location.reload();
        }
    },
});

Vue.component("history-order-component", {
    mixins: [historyOrderMix],
    data: function () {
        return {
            hasCalled: true,
            unFinish: true,
            unPrint: false
        }
    },
    created: function () {
        var that = this;
        listHistoryOrder(function (olist) {
            that.orderlist = olist;
        });
        getBrandSetting(function (result) {
            that.brandSetting = result;
        });
        getShopInfo(function(result){
            that.shop = result;
        });
    },
});


Vue.component("print-order-component", {
    mixins: [printOrderMix],
});


Vue.component("create-order", {
    mixins: [posmix],
    template:
    '<choice-type :show.sync="choiceDialog.show"></choice-type>	' +
    '<add_article_dialog :show.sync="addArticleDialog.show" :order.sync="addArticleDialog.order"></add_article_dialog>' +
    '<button class="btn btn-warning" @click="showCreateOrder" v-if="brandSetting.posOpenTable==1&&shop.posOpenTable==1">点单</button>'
    ,
    components: {
        "choice-type": {
            props: ["show"],
            template: '<div class="weui_dialog_alert" v-if="show">' +
            '<div class="weui_mask"></div>' +
            '<div class="weui_dialog_order" style="font-size: 22px;">' +
            '<div class="full-height">' +
            '<div class="order_header">' +
            '<span class="order_middle">点单</span>' +
            '<img src="assets/img/close.png" class="closeImg" alt="关闭" @click="close"/>' +
            '</div>' +
            '<div class="order_body">' +
            '<div class="order_content">' +
            '<span>交易码</span>' +
            '<span style="margin-left: 60px;" > {{verCode}} </span >' +
            '</div>' +
            '<div class="order_content">' +
            '<span> 就餐模式 </span>' +
            '<button style="margin-left: 40px;" class="btn btn-default" :class="{actives:distributionModeId == 1}" @click = "selectDistributionModeId(1)" > 堂食 </button>' +
            '<button style="margin-left: 15px;" class="btn btn-default" :class="{actives:distributionModeId == 3}" @click = "selectDistributionModeId(3)" > 外带 </button>' +
            '</div>' +
            '</div>' +
            '<div class="order_footer" >' +
            '<button class="btn btn-default" style="background: #fff;" @click = "close" > 取消 </button>' +
            '<button class="btn btn-primary" style = "margin-left: -40%;" @click = "showAdd" > 选择餐品 </button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div> ',
            data: function () {
                return {
                    verCode:null,
                    distributionModeId: 0,
                }
            },
            methods: {
                showAdd: function(){
                    if(this.distributionModeId == 0){
                        toastr.error("请先选择就餐模式！");
                    }else{
                        var order = {
                            verCode: this.verCode,
                            orderMode: 2,
                            distributionModeId: this.distributionModeId
                        };
                        this.show = false;
                        this.$dispatch("show-create-order-pos",order);
                    }
                },
                selectDistributionModeId: function(type){
                    this.distributionModeId = type;
                },
                close: function () {
                    this.show = false;
                },
                getVerCode: function(){
                    var rand = "";
                    for (var i = 0; i < 5; i++) {
                        var r = Math.floor(Math.random() * 10);
                        rand += r;
                    }
                    return rand;
                }
            },
            created: function () {
                var that = this;
                this.$watch("show", function () {
                    if(that.show){
                        this.distributionModeId = 1;
                        that.verCode = that.getVerCode();
                    }
                });
            }
        }
    },
    data: function () {
        return {
            choiceDialog:{show:false}
        }
    },
    methods: {
        showCreateOrder: function () {
            this.choiceDialog.show = true;
        },

    }
});


var orderOperator = new Vue({
    el: "#new-order-tab",
});

var createOrder = new Vue({
    el: "#createOrderBtn",
});

var refreshTv = new Vue({
    el: "#refreshBtn",
});

function clearMap() {
    clearMapRequest();
}

Vue.component("out-food-component", {
    mixins: [outFoodMix],
    data: function () {
        return {
            showTableNumber: false,

            callBtn: false,
            printBtn: true
        }
    },
    created: function () {
        var that = this;
        getOutFoodList(function (olist) {
            that.orderlist = olist;
        });
    }
});