productState[2] = productState[3];
var tableNumber = null;
var overTime = 99999999;
Vue.component("order-mini-detailed", {
    mixins: [orderMiniMax],
    methods: {
        vailedPushOrder: function () {
            if (this.order.tableNumber == null) {
                this.showOrderDetailed();
                this.$dispatch("message", "请扫描桌号二维码", 3000);
                throw new Error('没有桌号')
            }

        }
    },
    components: {
        'ver-code': {
            props: ["order"],
            template: '<span v-if="order.tableNumber">桌号:{{order.tableNumber}} </span>'
        }
    }
});


Vue.component("order-detailed-dialog", {
    mixins: [orderdetailMax],
    data: function () {
        return {
            pushOrderBtnName: "扫码下单"
        }
    },
    components: {
        'code-components': {
            props: ["order"],
            template:  '<div class="weui_cell weui_vcode table-number " >                                           ' +
            '	<div class="weui_cell_hd" ><label class="weui_label">交易码</label></div>  '+
            '	<div class="weui_cell_bd weui_cell_primary">                              ' +
            '		<input class="weui_input" type="number" id="verCode"   v-model="order.verCode" readonly="readonly" >   ' +
            '	</div>                                                                    ' +
            '</div>' +
            '<div class="weui_cell weui_vcode table-number " >                                           ' +
            '	<div class="weui_cell_hd " ><label class="weui_label">桌号</label></div>  ' +

            '	<div class="weui_cell_bd weui_cell_primary">                              ' +
            '		<input class="weui_input" type="number" id="tableNumber" placeholder="扫描桌号"  v-model="order.tableNumber" readonly="readonly" >   ' +
            '	</div>                                                                    ' +


            '	<div class="weui_cell_ft" >                                                ' +
                //'		<i class="iconcode icon-saoyisao"  v-if="order.productionStatus==0"></i>                                      '+
            '	</div>                                                                    ' +

            '</div>                                                                       ',

            //methods:{
            //    openScan:function(){
            //        var that = this;
            //        scanTableNumber(function(data){
            //            var reg_allNumber = /^[\d]+$/;
            //            var reg_tableNumber = /tableNumber=[\d]+/;
            //            if(reg_allNumber.test(data)){
            //                that.order.tableNumber=data;
            //                that.autoPushOrder();
            //            }else if(reg_tableNumber.test(data)){
            //                var tbNumber = data.match(reg_tableNumber)[0].match(/[\d]+/)[0];
            //                that.$set("order.tableNumber",tbNumber);
            //                that.autoPushOrder();
            //            }else{
            //                that.$dispatch("message","未识别改格式的数据:"+data,2000);
            //            }
            //            console.log("返回扫描结果,解析二维码数据，搞定桌号");
            //        });
            //    }
            //}
        }
    },
    methods: {
        pushOrderSuccess: function () {
            this.$dispatch("message", "正在下单中……请不要更换座位", overTime);
            console.log("下单成功");
        },
        orderStateChange: function (orderState) {
            console.log("订单状态改变" + orderState);
        },
        orderProductionStatusChange: function (pstate) {
            console.log("订单状态改变", pstate);
            if (pstate == 2) {
                this.$dispatch("message", "下单成功！请不要离开当前座位。", overTime);
            } else if (pstate == 1) {
                this.$dispatch("message", "正在下单中……请不要更换座位", overTime);
            } else if (pstate == 5) {
                this.$dispatch("message", "下单失败，请速与餐厅服务员联系", overTime);
            } else if (pstate == 0) {
                this.$dispatch("message", "正在自动下单", overTime);
            }
        },
        pushOrderClick: function () {
            this.openScan();
        },

        openScan: function () {
            var that = this;
            scanTableNumber(function (data) {
                var reg_allNumber = /^[\d]+$/;
                var reg_tableNumber = /tableNumber=[\d]+/;
                that.order.tableNumber = null;
                that.order.shopId = null;


                if (reg_allNumber.test(data)) {
                    that.order.tableNumber = data;
                    tableNumber = data;
                } else if (reg_tableNumber.test(data)) {
                    var tbNumber = data.match(reg_tableNumber)[0].match(/[\d]+/)[0];
                    that.order.tableNumber = tbNumber;
                    tableNumber = tbNumber;
                }
                var reg_shopId = /shopId=[a-zA-Z0-9]+/;
                if (reg_shopId.test(data)) {
                    that.order.shopId = data.match(reg_shopId)[0].split("=")[1];
                }

                if (that.order.shopId == null || that.order.tableNumber == null) {
                    that.$dispatch("message", "未识别改格式的数据:" + data, 2000);
                    return;
                }


                that.autoPushOrder();


                console.log("返回扫描结果,解析二维码数据，搞定桌号");
            });
        },
        pushOrder: function () {
            var that = this;


            if (this.order.tableNumber == null) {
                this.$dispatch("message", "请扫描桌号二维码", 3000);
                return;
            }

            //$.ajax({
            //    url: "order/checkArticleCount",
            //    data: {orderId: that.order.id},
            //    success: function (result) {
            //        //that.$dispatch("message", result.success, 3000);
            //        if (result.success) {
            if (that.order.shopId == null) {
                var o = that.order;
                setTableNumber(o.id, o.tableNumber, function (result) {
                    if (result.success) {
                        pushOrderRequest(o.id, function (re) {
                            if (re.success) {
                                that.$dispatch("message", "扫码完成");
                                that.order.productionStatus = 1;
                                that.pushOrderSuccess && that.pushOrderSuccess(that.order);
                            } else {
                                that.$dispatch(re.message);
                            }
                        });
                    } else {
                        that.$dispatch(result.message);
                    }
                });

            } else {
                $.ajax({
                    url: "order/checkShopId",
                    data: {shopId: that.order.shopId, orderId: that.order.id},
                    success: function (result) {
                        if (result.success) {
                            var o = that.order;
                            setTableNumber(o.id, o.tableNumber, function (result) {
                                if (result.success) {
                                    pushOrderRequest(o.id, function (re) {
                                        if (re.success) {
                                            that.order.productionStatus = 1;
                                            document.getElementById("tableNumber").value = tableNumber;
                                            that.pushOrderSuccess && that.pushOrderSuccess(that.order);
                                            //that.$set("order.tableNumber", o.tableNumber);
                                            that.order.tableNumber = null;

                                        } else {
                                            that.$dispatch(re.message);
                                        }
                                    });
                                } else {
                                    that.$dispatch(result.message);
                                }
                            });
                        } else {
                            that.order.tableNumber = null;
                            that.order.shopId = null;
                            that.$dispatch("message", result.message, 81000);
                        }
                    }

                });


            }
            //        } else {
            //            that.$dispatch("message", result.message, 9000);
            //            //that.$dispatch(result.message);
            //
            //        }
            //    }
            //});


        },
        autoPushOrder: function () {

            console.log("自动立即下单！");
            console.log(this.order);
            console.log(this.show, this.order.productionStatus == 0, this.order.orderState == 2, this.order.tableNumber
                , this.order.shopId);
            if (this.show && this.order.productionStatus == 0 && this.order.orderState == 2 && this.order.tableNumber != null) {
                this.$dispatch("message", "正在自动下单", overTime);
                this.pushOrder();
            } else if (this.show && this.order.productionStatus == 1 && this.order.orderState == 2 && this.order.tableNumber != null) {
                this.$dispatch("message", "正在下单中……请不要更换座位", overTime);
            } else if (this.show && this.order.productionStatus == 2 && this.order.orderState == 10 && this.order.tableNumber != null
                && this.order.allowContinueOrder) {
                this.$dispatch("message", "下单成功！请不要离开当前座位。", overTime);
            } else if (this.show && this.order.productionStatus == 2 && this.order.orderState == 2 && this.order.tableNumber != null
            ) {
                this.$dispatch("message", "下单成功！请不要离开当前座位。", overTime);
            } else if (this.show && this.order.productionStatus == 0 && this.order.orderState == 2 && this.order.tableNumber == null) {
                this.$dispatch("message", "请扫描桌号二维码", 1115000);
            } else if (this.show && this.order.productionStatus == 5 && this.order.orderState == 2 && this.order.tableNumber != null) {
                this.$dispatch("message", "下单失败，请速与餐厅服务员联系", overTime);
            }
        }
    },

    created: function () {
        this.$watch("show", function () {
            this.autoPushOrder();
        });
    }
});


var HomePage = Vue.extend({
    mixins: [homeBaseMix]
});

var MyPage = Vue.extend({
    mixins: [myBaseMix]
});

var TangshiPage = Vue.extend({
    mixins: [tangshiBaseMix]
});


var MainPage = new Vue({
    mixins: [mainBaseMix],
    components: {
        'home-page': HomePage,
        'tangshi-page': TangshiPage,
        'my-page': MyPage
    },
    events: {}
});


