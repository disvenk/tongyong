<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>WebSocket/SockJS Echo Sample (Adapted from Tomcat's echo sample)</title>
    <style type="text/css">
        #connect-container {
            float: left;
            width: 400px
        }

        #connect-container div {
            padding: 5px;
        }

        #console-container {
            float: left;
            margin-left: 15px;
            width: 400px;
        }

        #console {
            border: 1px solid #CCCCCC;
            border-right-color: #999999;
            border-bottom-color: #999999;
            height: 170px;
            overflow-y: scroll;
            padding: 5px;
            width: 100%;
        }

        #console p {
            padding: 0;
            margin: 0;
        }
    </style>

<!--     <script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script> -->
	<script src="assets/boot/sockjs.js"></script>
	 
    <script type="text/javascript">
        var ws = null;
        var url = null;
        var transports = [];

        function setConnected(connected) {
            document.getElementById('connect').disabled = connected;
            document.getElementById('disconnect').disabled = !connected;
            document.getElementById('echo').disabled = !connected;
        }

        function connect() {
//             ws= new WebSocket("ws://localhost:8083/pos/orderServer");
            ws = new SockJS("http://localhost:8083/pos/sockjs/orderServer");
            //ws = (url.indexOf('sockjs') != -1) ?new SockJS(url, undefined, {protocols_whitelist: transports}) : new WebSocket(url);
//             if ('WebSocket' in window) {
//                 ws= new WebSocket("ws://localhost:8083/pos/orderServer");
//                 console.log("使用的是  websocket");
//             }else {
//                 ws = new SockJS("http://localhost:8083/pos/sockjs/orderServer");
//                 console.log("使用的是  sockJs");
//             }
            
            ws.onopen = function () {
                setConnected(true);
				console.log(ws);
                console.log("state："+ws.readyState);
                log('-----连接成功----- ');
                var data = {};
                data.type = "check@pos";
                data.id = "${current_shop_id}";
                console.log(data.id);
                ws.send(JSON.stringify(data));
            };
            ws.onmessage = function (event) {
                log('【收到信息】: ' + event.data);
            };
            ws.onclose = function (event) {
                setConnected(false);
                log('【INFO】: 链接断开');
                log(event);
//                 window.setTimeout("connect()",1000);
            };
        }
        //断开连接
        function disconnect() {
            if (ws != null) {
                ws.close();
                ws = null;
            }
            setConnected(false);
        }
        function echo() {
            if (ws != null) {
                var message = document.getElementById('message').value;
                log('【发送信息】: ' + message);
                
                ws.send(getData(message));
            } else {
                alert('连接有问题，请从新连接');
            }
        }
        
        function getData(msg){
        	var data = {};
        	data.type = "pos";
        	data.data = msg;
        	return JSON.stringify(data);
        }

       
        function log(message) {
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.appendChild(document.createTextNode(message));
            console.appendChild(p);
            while (console.childNodes.length > 25) {
                console.removeChild(console.firstChild);
            }
            console.scrollTop = console.scrollHeight;
        }
    </script>
</head>
<body>
<noscript><h2 style="color: #ff0000">您当前的浏览器不支持当前页面功能，请更换为最新的Chrome浏览器进行操作</h2></noscript>
<div>
    <div id="connect-container">
        <div>
            <button id="connect" onclick="connect();">Connect</button>
            <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
        </div>
        <div>
            <textarea id="message" style="width: 350px">Here is a message!</textarea>
        </div>
        <div>
            <button id="echo" onclick="echo();" disabled="disabled">Echo message</button>
        </div>
    </div>
    <div id="console-container">
        <div id="console"></div>
    </div>
</div>
</body>

</html>