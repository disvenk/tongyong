<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Resto+ TV</title>
    <link rel="stylesheet" href="assets/css/tv.css?v=1" />
    <style>
        .order-item-name {
            display: block;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        @media screen and (min-width: 1360px){
            .tv,.tv_down{
                font-size: 70px;
            }
        }
    </style>
</head>
<body>
<div class="tv" id="div_tv">
    <div class="tv_top">
        <div class="left_tv">准备中</div>
        <div class="center_tv">请取餐</div>
        <div class="right_tv">当前叫号</div>
    </div>
    <div class="tv_center">
        <div class="left_box">
            <template v-if="readyOrder.length">
                <p style="color: #fff" v-for="order in readyOrder">
                    {{order.verCode}}</p>
            </template>
            <template v-else>
                <p style="color: #fff; font-size: 3vw;">&nbsp;</p>
            </template>
        </div>
        <div class="center_box">
            <p style="color: #fff" v-for="order in callNumberOrder | orderBy 'callTime' -1">
                {{order.verCode}}</p>
        </div>
        <div class="right_box">
            <ul class="express3">
                <li v-if="currentCallOrder">
                    <p>消费码：{{ currentCallOrder.verCode }}</p>
                    <p v-for="temp in currentCallOrder.orderItems"
                       class="order-item-name">
                        <span>{{ temp.articleName }}</span>
                    </p>
                    <p>餐品总计：{{ currentCallOrder.articleCount }} 份</p>
                </li>
                <li v-else>&nbsp;</li>
            </ul>
        </div>
    </div>
</div>
<div class="tv_down">请留意您的取餐信息...</div>

</body>
<script src="assets/boot/jquery.min.js"></script>
<script src="assets/boot/vue.min.js"></script>
<script src="assets/boot/sockjs.js"></script>
<script src="assets/js/customer.js"></script>
<script src="assets/js/service.js"></script>

<script type="text/javascript">
    var username="${sessionScope.USER_INFO.username}";
    var isready = false;
    document.addEventListener( "plusready",  function()
    {
        isready = true;
        var _BARCODE = 'speaker'
        var B = window.plus.bridge;
        var speaker =
        {
            voice : function (text , successCallback, errorCallback ){
                var success = function(args)
                {
                    successCallback(args);
                };
                var fail =function(code)
                {
                    errorCallback(code);
                };
                var callbackID = B.callbackId(success, fail);

                return B.exec(_BARCODE, "voice", [callbackID, text]);
            }
        };
        window.plus.speaker = speaker;
    }, true );



    var vue_ = new Vue(
            {
                el : '#div_tv',
                computed : {
                    currenItemCount : function() {
                        var count = 0;
                        $(this.currentOrder.ITEMS).each(function(i, item) {
                            count += item.COUNT;
                        });
                        return count;
                    }
                },
                data : {
                    readyOrder : [],
                    callNumberOrder : [],
                    currentCallOrder : null,
                    callOrderQueen:[],
                },
                methods : {
                    startGetNewOrder : function() {
                        this.connWebsock();
                        var that = this;
                        setInterval(function(){
                            var old_order = that.callNumberOrder[0];
                            if(old_order){
                                var nowTime=new Date().getTime();
                                var time = old_order.callTime.getTime();
                                var diff = (nowTime-time)/1000/60;
                                if(diff>5){
                                    that.callNumberOrder.$remove(old_order);
                                    if(that.currentCallOrder.id==old_order.id||that.callNumberOrder.length<=0){
                                        that.currentCallOrder=null;
                                    }
                                }
                            }
                        },2000);
                        this.callOrder();
                    },
                    callOrder:function(){
                        var currentOrder= this.callOrderQueen.shift();
                        if(currentOrder){
                            this.callOrderNumber(currentOrder);
                            setTimeout(this.callOrder,3000);
                        }else{
                            setTimeout(this.callOrder,1000);
                        }
                    },
                    connWebsock : function() {
                        var that = this;
                        var url = $("base").attr("href")+"sockjs/orderServer";
                        this.ws = new SockJS(url);
                        this.ws.onopen = function(event) {
                            that.wsOnopen()
                        }
                        this.ws.onmessage = function(event) {
                            that.wsOnmessage(event)
                        };
                        this.ws.onclose = function(event) {
                            that.wsOnclose()
                        };
                        this.ws.onerror = function(evt) {
                            that.wsOnerror()
                        };
                    },
                    wsOnopen : function() {
                        console.log("---> tv端开始连接");
                        var data = this.getJsonData("check", null);
                        this.ws.send(data);
                        console.log("---> tv端连接成功");

                    },
                    wsOnmessage : function(event) {
                        var data = JSON.parse(event.data);
                        console.log("事件"+data.dataType);
                        if(data.dataType == "current"){//后台推送的 实时订单
                            if(data.productionStatus == 2){//准备中
//                                alert(data.verCode + "1");
                                this.readyOrder.push(data);

                            }else if(data.productionStatus == 3){//已叫号
                                data.callTime=new Date();
                                this.callNumberOrder.push(data); //放入叫号列表显示框内

                                this.callOrderQueen.push(data);  //放入叫号队列内
                                this.cleanReadyOrder(data);
                            }
                        }else if(data.dataType == "history"){//后台推送的 历史订单
                            var orders = data.orders;
                            console.log(orders);
                            for(var i in orders){
                                var o = orders[i];
                                console.log(o);
                                if(o.productionStatus == 2){
//                                    alert(data.verCode + "2");
                                    this.readyOrder.push(o);
                                }else if(o.productionStatus == 3){
                                    this.callNumberOrder.push(o);
                                    this.setCurrentCallOrder(o);
                                    this.callOrderNumber(o);
                                    this.cleanReadyOrder(o);
                                }
                            }
                        }else if(data.dataType == "cancelOrder"){//后台推送的 取消订单
                            console.log("收到了 后台 取消订单的请求");
                            console.log(data);
                            this.cleanReadyOrder(data);
                            this.cleanCallNumberOrder(data);
                        }else if(data.dataType == "cancelReadyOrder"){//后台推送删除准备中的订单 yz 08-23
                            console.log("收到了 后台 删除准备中 取消的该订单的消息")
                            console.log(data);
                            this.cleanReadyOrder(data);
                        }
                    },
                    wsOnclose : function() {
                        window.setTimeout(this.connWebsock, 1000);
                    },
                    wsOnerror : function() {
                        window.setTimeout(this.connWebsock, 1000);
                    },
                    connWebsock : function() {
                        var that = this;
                        var url = $("base").attr("href")+"sockjs/orderServer";
                        this.ws = new SockJS(url);
                        this.ws.onopen = function(event) { that.wsOnopen() }
                        this.ws.onmessage = function(event) { that.wsOnmessage(event) };
                        this.ws.onclose = function(event) { that.wsOnclose() };
                        this.ws.onerror = function(evt) { that.wsOnerror() };
                    },
                    getJsonData : function(type,data){
                        var dataAPI = {};
                        dataAPI.shopId = "${current_shop_id}";
                        dataAPI.client = "tv";
                        dataAPI.type = type;
                        dataAPI.data = data;
                        return JSON.stringify(dataAPI);
                    },
                    cleanReadyOrder : function(order){
                        for(var i in this.readyOrder){
                            var o  = this.readyOrder[i];
                            if(o.id==order.id){
                                this.readyOrder.$remove(o);
                                break;
                            }
                        }
                    },
                    cleanCallNumberOrder : function(order){
                        for(var i in this.callNumberOrder){
                            var o  = this.callNumberOrder[i];
                            if(o.id==order.id){
                                this.callNumberOrder.$remove(o);
                                break;
                            }
                        }
                    },
                    setCurrentCallOrder : function(order){
                        var that = this;
                        $.post("order/getOrderItems",{"orderId":order.id},function(result){
                            order.orderItems = result.data;
                            that.currentCallOrder = order;
                        })
                    },
                    callOrderNumber : function(order) {  //叫号方法
                        console.log("订单叫号!");
                        this.setCurrentCallOrder(order);
                        var msg =  order.verCode;
                        if (isready) {
                            playMusic(msg);
                        } else {
                            setTimeout(function() {
                                playMusic(msg);
                            }, 1000);
                        }
                    }
                },
                created : function() {
                    this.startGetNewOrder();
                }
            })

    function sleep(n) {
        var start = new Date().getTime();
        while(true){
            if(new Date().getTime()-start > n)
                break;
        }
    }

    function playMusic(msg, succes) {

//        var src = document.getElementById("wav0").currentSrc;
//        var p = plus.audio.createPlayer("_www/music/0.wav");
//        p.play();

//
        if (typeof window.plus == "undefined" || typeof plus.speaker == "undefined") {
            console.error("非安卓平台语音叫号");
            console.log(msg);
            return false;
        }
//
//
////        succes && succes();
        for(var i = 0;i< msg.length;i++){
            var p = plus.audio.createPlayer("_www/music/"+msg[i]+".wav");
            p.play();
            sleep(466);
        }
//
//        var src = document.getElementById("wavPlay").currentSrc;
//
        var p = plus.audio.createPlayer("_www/music/play.wav");
        p.play();
//


//        if(username=="maidao"){
//            var src = "http://tts.baidu.com/text2audio?lan=zh&pid=101&ie=UTF-8&text="
//                    + encodeURI(msg) + "&spd=5";
//            var p = plus.audio.createPlayer(src);
//            p.play();
//            succes && succes();
//        }else{
//            if (typeof window.plus == "undefined" || typeof plus.speaker == "undefined") {
//                console.error("非安卓平台语音叫号");
//                console.log(msg);
//                return false;
//            }
//            plus.speaker.voice(msg);
//        }
    }

</script>
</html>

