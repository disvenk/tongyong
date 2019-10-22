<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<div class="row order-operator" id="receipt_order">
    <div class="col-sm-4 padding-0 full-height border-1">
        <div class="order-detailed">
            <div class="title">
                <h4>发票信息</h4>
            </div>
            <div class="description">
                <p><span>订单编号:</span><span>{{orderlistDetail.orderNumber}}</span></p>
                <p><span>支付时间:</span><span v-if="orderlistDetail.payTime!=null">{{orderlistDetail.payTime|time}}</span></p><hr/>
                <p><span>名称:</span><span>{{orderlistDetail.name}}</span></p>
                <p><span>税号:</span><span>{{orderlistDetail.dutyParagraph}}</span></p><hr/>
                <p><span>订单总额:</span><span v-if="orderlistDetail.orderMoney!=null">¥{{orderlistDetail.orderMoney}}</span></p>
                <p><span>开票金额:</span><span v-if="orderlistDetail.receiptMoney!=null">¥{{orderlistDetail.receiptMoney}}</span></p><hr/>
                <p><span>桌号:</span><span>{{orderlistDetail.tableNumber}}</span></p>
                <p><span>手机号:</span><span>{{orderlistDetail.mobileNo}}</span></p>
                <p><img style="margin-left: 90px;width: 200px;height: 200px;display: none;" alt="二维码" src="order/qrcode?content=订单编号:{{orderlistDetail.orderNumber}}%0A支付时间:{{orderlistDetail.payTime|time}}%0A名称:{{orderlistDetail.name}}%0A税号:{{orderlistDetail.dutyParagraph}}%0A订单总额:{{orderlistDetail.orderMoney}}%0A开票金额:{{orderlistDetail.receiptMoney}}%0A桌号:{{orderlistDetail.tableNumber}}%0A手机号:{{orderlistDetail.mobileNo}}&width=250&height=250"/></p>
                <p><button @click="receiptOrderPrint(orderlistDetail.receiptId)" style="margin-left: 100px;width: 180px;display: none;">打印</button></p>
            </div>
        </div>
    </div>

    <div class="col-sm-8" id="iscroll_div2" style="overflow-y: scroll;height: 100%;">
        <table class="table table-hover call-number-table">
            <thead>
            <tr>
                <th>序号</th>
                <th>名称</th>
                <th>
                    <select style="height:35px;" @change="isStatus">
                        <option v-model="f.value" v-for="f in valueList">{{f.name}}</option>
                    </select>
                </th>
            </tr>
            </thead>
            <tbody style="overflow-y:scroll;" >
            <tr v-for="(index,o) in orderlist" @click="receiptOrderLists(o.receiptId)">
                <td>{{index+1}}</td>
                <td>{{o.name}}</td>
                <td v-if="o.state==0"><button @click="isSwitch(o.receiptId,1)" class="btnState" style="background-color:black;color:white;height:36px;width:60px;border-radius: 5px;">未开</button></td>
                <td v-if="o.state==1"><button @click="isSwitch(o.receiptId,0)" class="btnState" style="background-color:white;color:black;height:36px;width:60px;border-radius: 5px;">已开</button></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<script src="assets/js/basemix.js"></script>
<script>
    $().ready(function() {
        <!-- 自定义filter名称为'time' -->
        Vue.filter('time',
                <!-- value 格式为13位unix时间戳 -->
                <!-- 10位unix时间戳可通过value*1000转换为13位格式 -->
                function(value) {
                    var date = new Date(value);
                    Y = date.getFullYear(),
                            m = date.getMonth() + 1,
                            d = date.getDate(),
                            H = date.getHours(),
                            i = date.getMinutes(),
                            s = date.getSeconds();
                    if (m < 10) {
                        m = '0' + m;
                    }
                    if (d < 10) {
                        d = '0' + d;
                    }
                    if (H < 10) {
                        H = '0' + H;
                    }
                    if (i < 10) {
                        i = '0' + i;
                    }
                    if (s < 10) {
                        s = '0' + s;
                    }
                    <!-- 获取时间格式 2017-01-03 10:13:48 -->
                    var t = Y+'-'+m+'-'+d+' '+H+':'+i+':'+s;
                    <!-- 获取时间格式 2017-01-03 -->
                    //var t = Y + '-' + m + '-' + d;
                    return t;
                });
    });
    var c= new Vue({
        el : "#receipt_order",
        data : {
            orderlist: [],
            orderlistDetail: {},
            selected:'',
            valueList:[
                {
                    name:'状态',
                    value:''
                },
                {
                    name:'已开',
                    value:'1'
                },
                {
                    name:'未开',
                    value:'0'
                }
            ]
        },
        created : function() {
            var that = this;
            getReceiptList(function(result){
                that.orderlist= result;
                //console.log(JSON.stringify(that.orderlist));
            });
            Hub.$on('change',function(result){
                 //c.orderlist.push(result);
                c.orderlist.unshift(result);
            })
        },
        methods : {
            receiptOrderLists:function (id) {
                var that = this;
                receiptOrderList(id,function (result) {
                    that.orderlistDetail= result;
                    //console.log(JSON.stringify(that.orderlistDetail));
                    $('.description img').show();
                    $('.description button').show();
                })
            },
            receiptOrderPrint:function (id) {
                if(id!=null){
                    receiptOrderPrints(id,function (result) {
                        if (result.success){
                            printPlus(result.data,function () {
                                toastr.success("发票打印成功");
                            }, function (msg) {
                                toastr.clear();
                                toastr.error(msg);
                            });
                        }
                    })
                }
            },
            isSwitch:function (id,state) {
                debugger
                if(id!=null){
                    updateReceiptState(id,state,function(result){
                        if (result.success){
                            getReceiptList(function(result){
                                c.orderlist= result;
                                //console.log(JSON.stringify(c.orderlist));
                            });
                            toastr.success("执行成功");
                        }
                    })
                }
            },
            isStatus:function(ele) {
                this.selected = ele.target.value;
                var state="";
                if(this.selected=="状态"){
                    state="";
                }else if(this.selected=="已开"){
                    state="1"
                }else if(this.selected=="未开"){
                    state="0"
                }
                getReceiptStateList(state,function(result){
                    debugger
                    c.orderlist= result.data;
                    //console.log(JSON.stringify(c.orderlist));
                });
            }
        }
    });
    function getReceiptList(cbk){
        $.post("order/getReceiptList",function(result){
            cbk&&cbk(result.data);
        });
    }
    function getPosReceiptList(orderNumber,cbk){
        $.post("order/getPosReceiptList",{orderNumber:orderNumber},function(result){
            cbk&&cbk(result);
        });
    }
    function receiptOrderList(receiptId,cbk){
        $.post("order/getReceiptOrderList",{receiptId:receiptId},function(result){
            cbk&&cbk(result);
        });
    }
    function receiptOrderPrints(receiptId,cbk) {
        $.post("order/receiptOrderPrints",{receiptId:receiptId},function (result) {
            cbk&&cbk(result);
        });
    }
    function receiptOrderPosPrints(orderNumber,cbk) {
        $.post("order/receiptOrderPosPrints",{orderNumber:orderNumber},function (result) {
            cbk&&cbk(result);
        });
    }
    function updateReceiptState(receiptId,state,cbk) {
        $.post("order/updateReceiptState",{receiptId:receiptId,state:state},function (result) {
            cbk&&cbk(result);
        });
    }
    function getReceiptStateList(state,cbk) {
        $.post("order/getReceiptStateList",{state:state},function (result) {
            cbk&&cbk(result);
        });
    }
</script>
