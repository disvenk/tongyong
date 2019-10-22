<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!doctype html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Resto+ Login</title>
    <%--<link rel="stylesheet" type="text/css" href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css"/>--%>
    <link rel="stylesheet" type="text/css" href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.min.css"/>
    <link rel="shortcut icon" href="assets/img/favicon.ico"/>
    <style type="text/css">

        #keyboard_5xbogf8c, #keyboard_5xbogf8c li {
            box-sizing: content-box;
            list-style: none;
            margin: 0;
            padding: 0;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        #keyboard_5xbogf8c {
            position: fixed;
            z-index: 10;
            width: 9.30em;
            background: rgba(158, 180, 185, 1);
            background: -webkit-gradient(linear, 0 50%, 100% 0, from(rgba(158, 180, 185, 1)), to(rgba(169, 156, 173, 1)), color-stop(50%, rgba(222, 157, 193, 1)));
            background: linear-gradient(60deg, rgba(158, 180, 185, 1), rgba(222, 157, 193, 1) 50%, rgba(169, 156, 173, 1) 100%);
            padding-left: 0.1em;
            border-radius: 0.05em;
            padding-top: 0.1em;
            box-shadow: 0 0.05em 0.1em rgba(0, 0, 0, .5);
            display: none;
            font-size: 100px;
            right: 0.05em;
            bottom: 0.05em;
            -webkit-text-size-adjust: none;
        }

        #keyboard_5xbogf8c_left {
            position: absolute;
            width: 0.2em;
            height: 0.2em;
            font-size: 1em;
            top: 0em;
            left: 0em;
            cursor: nw-resize;
        }

        #keyboard_5xbogf8c_right {
            position: absolute;
            width: 0.2em;
            height: 0.2em;
            font-size: 1em;
            top: 0em;
            right: 0em;
            cursor: ne-resize;
        }

        #keyboard_5xbogf8c ::after {
            content: "";
            display: table;
            clear: both;
        }

        #keyboard_5xbogf8c li {
            position: relative;
            font-family: arial, "Vrinda";
            width: 2.88em;
            height: 2.8em;
            line-height: 2.8em;
            background-color: rgba(255, 255, 255, .8);
            border-radius: 0.2em;
            float: left;
            text-align: center;
            font-size: 0.25em;
            vertical-align: text-top;
            margin-right: 0.4em;
            margin-bottom: 0.4em;
            box-shadow: 0 0.2em 0.4em rgba(0, 0, 0, .5);
            cursor: pointer;
            position: relative;
            overflow: visible;
        }

        #keyboard_5xbogf8c li:active {
            box-shadow: inset 0 0.04em 0 rgba(0, 0, 0, .5);
            top: 0.08em;
        }

        #keyboard_5xbogf8c li:nth-child(11), #keyboard_5xbogf8c li:nth-child(43), #keyboard_5xbogf8c li:nth-child(22) {
            background: rgba(188, 188, 188, .5);
            font-size: 0.20em;
            width: 5em;
            height: 3.5em;
            line-height: 3.5em;
            border-radius: 0.25em;
            margin-right: 0.5em;
            margin-bottom: 0.5em;
            box-shadow: 0 0.25em 0.5em rgba(0, 0, 0, .5);
        }

        #keyboard_5xbogf8c li:nth-child(43) {
            width: 3.6em;
        }

        #keyboard_5xbogf8c li:nth-child(12) {
            margin-left: 0.96em;
        }

        #keyboard_5xbogf8c li:nth-child(23) {
            margin-left: 1.92em;
        }

        #keyboard_5xbogf8c li:nth-child(22) {
            width: 3.6em;
            height: 4.2em;
            background-color: rgba(238, 238, 238, 1);
            border-bottom-right-radius: 0em;
            border-bottom-left-radius: 0em;
            position: absolute;
            top: 4.5em;
            right: 0em;
            box-shadow: inset 0 0em 0 rgba(0, 0, 0, .5);
        }

        #keyboard_5xbogf8c li:nth-child(32) {
            width: 5.04em;
            top: 0;
            background-color: rgba(238, 238, 238, 1);
            border-top-right-radius: 0em;
            padding-left: 0.32em;
            box-shadow: inset 0 0em 0 rgba(0, 0, 0, .5);

        }

        #keyboard_5xbogf8c li:nth-child(33) {
            font-size: 0.20em;
            width: 5em;
            height: 3.5em;
            line-height: 3.5em;
            border-radius: 0.25em;
            margin-right: 0.5em;
            margin-bottom: 0.5em;
            box-shadow: 0 0.25em 0.5em rgba(0, 0, 0, .5);
        }

        #keyboard_5xbogf8c li:nth-child(41) {
            box-sizing: border-box;
            padding-top: 0.6em;
        }

        #keyboard_5xbogf8c li:nth-child(41) span {
            display: block;
            line-height: 0.6;
        }

        #keyboard_5xbogf8c li:nth-child(42) {
            box-sizing: border-box;
            padding-top: 0.6em;
        }

        #keyboard_5xbogf8c li:nth-child(42) span {
            display: block;
            line-height: 0.6;
        }
    </style>
</head>
<body>
<div class="col-xs-offset-2 col-xs-8 col-md-offset-4 col-md-4" style="margin-top: 10%;">
    <form class="form-horizontal" method="post" name="loginForm"
          id="loginForm" style="border: 1px solid #eee;">
        <div class="modal-header" style="background-color: #eee">
            <h4 class="modal-title text-center">
                <img src="assets/img/resto_logo.png" style="height: 40px;" alt=""/>
            </h4>
        </div>
        <div class="modal-body">
            <input type="hidden" name="logintype" value="shopstartlogin"/>
            <br/>
            <div class="form-group">
                <label for="loginname" class="col-xs-4 col-sm-4 control-label text-center">账号：</label>
                <div class="col-xs-7 col-sm-7">
                    <input type="text" class="form-control" id="loginname"
                           name="loginname" required="required" placeholder="请输入账号">
                </div>
            </div>
            <br/>
            <div class="form-group">
                <label for="password" class="col-xs-4 col-sm-4 control-label text-center">密码：</label>
                <div class="col-xs-7 col-sm-7">
                    <input type="password" class="form-control" id="password"
                           name="password" required="required" placeholder="请输入密码">
                </div>
            </div>
            <div class="form-group" style="display: none" id="pwd">
                <label for="password" class="col-xs-4 col-sm-4 control-label text-center"></label>
                <div class="col-xs-7 col-sm-7 ">
                    <span style="padding-right: 20px"><label class="control-label">记住密码</label>&nbsp;<input
                            type="checkbox" id="savePwd" name="savePwd"></span>
                    <span><label class="control-label">自动登陆</label>&nbsp;<input type="checkbox" id="autoLogin"
                                                                                name="autoLogin"></span>
                </div>


            </div>

        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary btn-block"
                    id="loginbtn" onclick="login()">登录
            </button>
        </div>
        <%--<input type="hidden" name="redirect" value="${redirect }"/>--%>
    </form>
    <br/>
    <br/>
    <p class="text-danger text-center">
        <span id="errorMsg"></span>
    </p>
</div>

<div class="navbar-fixed-bottom text-center" style="z-index: 1;">
    &copy; 上海餐加企业管理咨询有限公司 v2.3.1 <br/><br/>
</div>

<%--<script src="//cdn.bootcss.com/jquery/2.2.1/jquery.min.js"></script>--%>
<script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<!-- <script src="//cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script> -->
<%--<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>--%>
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(function () {
        if(window.location.href.indexOf("tv") != -1){
            $('#pwd').css("display","block");
            $.ajax({
                url: 'config/loginConfig',
                method: 'post',
                type: 'json',
                success: function (result) {
                    if (result.success) {
                        $('#loginname').val(result.data.userName);
                        $('#password').val(result.data.passWord);
                        if (result.data.autoLogin == 1) {
                            $('#autoLogin').attr("checked", true);
                            login();
                        }
                        if (result.data.savePwd == 1) {
                            $('#savePwd').attr("checked", true);

                        }
                    }
                }
            });

        }



        //判断是否为安卓系统（Tv端不显示 js键盘）
        if (navigator.userAgent.indexOf("android") == -1 && navigator.userAgent.indexOf("Android") == -1) {
            $("input").addClass("numkeyboard");
            $(".numkeyboard").ioskeyboard({
                keyboardRadix: 60,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
                keyboardRadixMin: 40,//键盘大小的最小值，默认为60，实际大小为564X198
                keyboardRadixChange: true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时才较好支持Safari内核浏览器
                clickeve: true,//是否绑定元素click事件
                colorchange: false,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
                colorchangeStep: 1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
                colorchangeMin: 154//按键背影颜色的最小值，默认为RGB(154,154,154)
            });
            $("input").blur(function () {
                $("#keyboard_5xbogf8c").css({"display": "none"});
            });
        }
    })

    function login() {
        if(window.location.href.indexOf("tv") != -1){

        }
        $.ajax({
            url: 'login',
            method: 'post',
            type: 'json',
            data: {
                loginname: $('#loginname').val(),
                password: $('#password').val(),
                savePwd: window.location.href.indexOf("tv") != -1 ? ($('#savePwd').is(':checked') ? 1 : 0 ): null,
                autoLogin: window.location.href.indexOf("tv") != -1 ? ($('#autoLogin').is(':checked') ? 1 : 0):null,
            },
            success: function (result) {
                if(result.success){
                    if(window.location.href.indexOf("tv") != -1){
                        window.location.href = "/pos/tv";
                    }else{
                        window.location.href = "/pos";
                    }
                }else{
                    $('#errorMsg').text(result.message);
                }
            }
        });
    }

</script>

<script type="text/javascript">
    //JS 键盘
    (function ($) {
        $.fn.ioskeyboard = function (options) {
            var defaults = {
                keyboardRadix: 100,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
                keyboardRadixMin: 60,//键盘大小的最小值，默认为60，实际大小为564X198
                keyboardRadixChange: true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时，完美支持Safari内核浏览器
                clickeve: true,//是否绑定元素click事件
                colorchange: true,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
                colorchangeStep: 1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
                colorchangeMin: 154//按键背影颜色的最小值，默认为RGB(154,154,154)
            }
            var options = $.extend(defaults, options);
            var numkeyboardcount = 0;
            var activeinputele;
            var keyboardRadix = options.keyboardRadix;
            var keyboardRadixMin = options.keyboardRadixMin;
            var colorchange = options.colorchange;
            var colorchangeStep = options.colorchangeStep;
            var colorchangeMin = options.colorchangeMin;
            var bMouse = false;
            var bToch = false;
            var MAction = false;
            var MTouch = false;
            var keyfixed = false;
            if (keyboardRadix < keyboardRadixMin) {
                keyboardRadix = keyboardRadixMin;
            }
            this.each(function () {
                numkeyboardcount++;
                //添加键盘
                if (numkeyboardcount < 2) {
                    $("body").append("<ul id='keyboard_5xbogf8c'>" +
                        "<li>1</li>" +
                        "<li>2</li>" +
                        "<li>3</li>" +
                        "<li>4</li>" +
                        "<li>5</li>" +
                        "<li>6</li>" +
                        "<li>7</li>" +
                        "<li>8</li>" +
                        "<li>9</li>" +
                        "<li>0</li>" +
                        "<li>←</li>" +
                        "<li>Q</li>" +
                        "<li>W</li>" +
                        "<li>E</li>" +
                        "<li>R</li>" +
                        "<li>T</li>" +
                        "<li>Y</li>" +
                        "<li>U</li>" +
                        "<li>I</li>" +
                        "<li>O</li>" +
                        "<li>P</li>" +
                        "<li></li>" +
                        "<li>A</li>" +
                        "<li>S</li>" +
                        "<li>D</li>" +
                        "<li>F</li>" +
                        "<li>G</li>" +
                        "<li>H</li>" +
                        "<li>J</li>" +
                        "<li>K</li>" +
                        "<li>L</li>" +
                        "<li>Exit</li>" +
                        "<li>CapsLock</li>" +
                        "<li>Z</li>" +
                        "<li>X</li>" +
                        "<li>C</li>" +
                        "<li>V</li>" +
                        "<li>B</li>" +
                        "<li>N</li>" +
                        "<li>M</li>" +
                        "<li><span>-</span><span>_</span></li>" +
                        "<li><span>/</span><span>.</span></li>" +
                        "<li>Clear</li>" +
                        "<div id='keyboard_5xbogf8c_left'></div>" +
                        "<div id='keyboard_5xbogf8c_right'></div>" +
                        "</ul>");
                }

                var inputele = $(this);
                var keyboard = $("#keyboard_5xbogf8c");
                var keys = keyboard.children("li");
                var hiddenbutton = keyboard.children("div");
                keyboard.css({"font-size": keyboardRadix + "px"});

                //keyboard.css({"position":"fixed","right":"0.05em","bottom":"0.05em"});
                exit();
                var shiftbool = false;
                if (numkeyboardcount < 2) {
                    if (options.keyboardRadixChange) {
                        BmouseDrag();
                        BtouchDrag();
                    }
                    /*		双击定位
                     keyboard.dblclick(function(){
                     if(keyfixed){
                     keyfixed = false;
                     }else{
                     keyboard.css({"position":"fixed","right":"0.05em","bottom":"0.05em"});
                     keyfixed = true;
                     }
                     });
                     */
                    keys.click(function (event) {
                        var keyele = $(this);
                        var keytext = keyele.text();
                        var evebool = true;
                        if (keytext === "CapsLock") {
                            activeinputele[0].focus();
                            if (shiftbool) {
                                keyele.css({background: "rgba(255,255,255,.9)"});
                                shiftbool = false;
                            } else {
                                //keyele.css({background:"rgba(188,188,188,.5)"});
                                keyele.css({background: "#d9534f"});
                                shiftbool = true;
                            }

                            evebool = false;
                        }
                        if (keytext === "Exit" || keytext.length < 1) {
                            simulateKeyEvent(activeinputele[0], 13);
                            exit();
                            evebool = false;
                        }
                        if (keytext === "←") {
                            activeinputele[0].focus();
                            backspace();
                            evebool = false;
                        }
                        if (keytext === "Clear") {
                            activeinputele[0].focus();
                            keyclear();
                            evebool = false;
                        }
                        if (evebool) {
                            if (shiftbool) {
                                if (keytext.length === 2) {
                                    keytext = keytext.substring(0, 1);
                                }
                            } else {
                                if (keytext.length === 2) {
                                    keytext = keytext.substring(1, 2);
                                } else {
                                    keytext = keytext.toLowerCase();
                                }

                            }
                            clickkey(keytext);
                            if (colorchange) {
                                var oldbabkground = $(this).css("background").split(',')[0].split('(')[1];
                                var newbabkground = oldbabkground - colorchangeStep;
                                if (newbabkground < colorchangeMin) {
                                    newbabkground = colorchangeMin;
                                    alert("min")
                                }
                                $(this).css("background", "rgba(" + newbabkground + "," + newbabkground + "," + newbabkground + ",.9)");
                            }
                        }
                    })
                    keyboard.children("li:eq(" + 21 + ")").mousedown(function (event) {
                        $(this).css({top: "4.6em", "box-shadow": "inset 0 0.04em 0 rgba(0,0,0,.5)"});
                        keyboard.children("li:eq(" + 31 + ")").css({
                            top: "0.1em",
                            "box-shadow": "inset 0 0em 0 rgba(0,0,0,.5)"
                        });
                    })
                        .mouseup(function (event) {
                            $(this).css({top: "4.5em", "box-shadow": " inset 0 0em 0 rgba(0,0,0,.5)"});
                            keyboard.children("li:eq(" + 31 + ")").css({
                                top: "0px",
                                "box-shadow": " inset 0 0em 0 rgba(0,0,0,.5)"
                            });
                        });
                    keyboard.children("li:eq(" + 31 + ")").mousedown(function (event) {
                        $(this).css({top: "0.1em", "box-shadow": "inset 0 0em 0 rgba(0,0,0,.5)"});
                        keyboard.children("li:eq(" + 21 + ")").css({
                            top: "4.6em",
                            "box-shadow": "inset 0 0.04em 0 rgba(0,0,0,.5)"
                        });
                    })
                        .mouseup(function (event) {
                            $(this).css({top: "0px", "box-shadow": " inset 0 0em 0 rgba(0,0,0,.5)"});
                            keyboard.children("li:eq(" + 21 + ")").css({
                                top: "4.5em",
                                "box-shadow": " inset 0 0em 0 rgba(0,0,0,.5)"
                            });
                        });
                }

                inputele.focus(function (event) {
                    activeinputele = inputele;
                    var p = GetScreenPosition(this);
                    if (keyboard.css("display") == "none") {
                        keyboard.css({"display": "block"});
                        mouseDrag();
                        touchDrag();
                    }
                });

                if (options.clickeve) {
                    inputele.click(function () {
                        activeinputele = inputele;
                        var p = GetScreenPosition(this);
                        if (keyboard.css("display") == "none") {
                            keyboard.css({"display": "block"});
                            mouseDrag();
                            touchDrag();
                        }
                    });
                }

                function GetScreenPosition(object) {
                    var position = {};
                    position.x = object.offsetLeft;
                    position.y = object.offsetTop;
                    while (object.offsetParent) {
                        position.x = position.x + object.offsetParent.offsetLeft;
                        position.y = position.y + object.offsetParent.offsetTop;
                        if (object == document.getElementsByTagName("body")[0]) {
                            break;
                        }
                        else {
                            object = object.offsetParent;
                        }
                    }
                    return position;
                }

                function keyclear() {
                    activeinputele.val("");
                }

                function backspace() {
                    var inputtext = activeinputele.val();
                    if (inputtext.length > 0) {
                        inputtext = inputtext.substring(0, inputtext.length - 1);
                        activeinputele.val(inputtext);
                    }
                }

                function clickkey(key) {
                    var inputtext = activeinputele.val();
                    inputtext = inputtext + key;
                    activeinputele.val(inputtext);
                    activeinputele[0].focus();
                }

                function exit() {
                    keyboard.css({"display": "none"});
                }


                function BmouseDrag() {
                    var eventEle = hiddenbutton;
                    var eventEleId;
                    var moveEle = keyboard;
                    var stx = etx = curX = 0;
                    var keyboardfontsize = +moveEle.css("font-size").split('px')[0];
                    var tempsize;
                    eventEle.mousedown(function (event) {
                        bMouse = true;
                        stx = event.pageX;
                        keyboardfontsize = +moveEle.css("font-size").split('px')[0];
                        eventEleId = $(this).attr('id');
                        event.preventDefault();
                    });
                    $("body").mousemove(function (event) {
                        if (bMouse) {
                            var curX = event.pageX - stx;
                            if (eventEleId === "keyboard_5xbogf8c_left") {
                                tempsize = keyboardfontsize - Math.ceil(curX / 10);
                            }
                            if (eventEleId === "keyboard_5xbogf8c_right") {
                                tempsize = keyboardfontsize + Math.ceil(curX / 10);
                            }
                            if (tempsize < keyboardRadixMin) {
                                tempsize = keyboardRadixMin;
                            }
                            moveEle.css({"font-size": tempsize});
                            event.preventDefault();
                        }
                    });
                    $("body").mouseup(function (event) {
                        stx = etx = curX = 0;
                        bMouse = false;
                    });
                }

                function BtouchDrag() {
                    var eventEle = hiddenbutton;
                    var moveEle = keyboard;
                    var eventEleId;
                    var stx = etx = curX = 0;
                    var keyboardfontsize = +moveEle.css("font-size").split('px')[0];
                    var tempsize;
                    eventEle.on("touchstart", function (event) { //touchstart
                        var event = event.originalEvent;
                        bToch = true;
                        curX = 0;
                        eventEleId = $(this).attr('id');
                        keyboardfontsize = +moveEle.css("font-size").split('px')[0];
                        stx = event.touches[0].pageX;
                        sty = event.touches[0].pageY;
                    });
                    eventEle.on("touchmove", function (event) {
                        if (bToch) {
                            var event = event.originalEvent;

                            curX = event.touches[0].pageX - stx;
                            if (eventEleId === "keyboard_5xbogf8c_left") {
                                tempsize = keyboardfontsize - Math.ceil(curX / 10);
                            }
                            if (eventEleId === "keyboard_5xbogf8c_right") {
                                tempsize = keyboardfontsize + Math.ceil(curX / 10);
                            }
                            if (tempsize < keyboardRadixMin) {
                                tempsize = keyboardRadixMin;
                            }
                            moveEle.css({"font-size": tempsize});
                            event.preventDefault();
                        }

                    });
                    eventEle.on("touchend", function (event) {
                        stx = etx = curX = 0;
                        bToch = false;

                    })

                }

                function mouseDrag() {
                    var eventEle = keyboard;
                    var stx = etx = curX = sty = ety = curY = 0;
                    var eleRight = +eventEle.css("right").split('px')[0];
                    var eleBottom = +eventEle.css("bottom").split('px')[0];
                    eventEle.mousedown(function (event) {
                        //console.log("down",+eventEle.css("right").split('px')[0]);
                        if (!keyfixed) {
                            MAction = true;
                        }
                        //alert(MAction);
                        stx = event.pageX;
                        sty = event.pageY;
                        //eleRight = +eventEle.css("left").split('px')[0];
                        //eleBottom = +eventEle.css("top").split('px')[0];
                        eleRight = +eventEle.css("right").split('px')[0];
                        eleBottom = +eventEle.css("bottom").split('px')[0];

                        event.preventDefault();
                    });
                    $("body").mousemove(function (event) {
                        if (MAction && !bMouse) {
                            var curX = event.pageX - stx;
                            var curY = event.pageY - sty;
                            eventEle.css({"right": eleRight - curX, "bottom": eleBottom - curY});
                            //console.log("move",+eventEle.css("right").split('px')[0]);
                            event.preventDefault();
                        }
                    });
                    $("body").mouseup(function (event) {
                        stx = etx = curX = sty = ety = curY = 0;
                        MAction = false;
                        //console.log("up",+eventEle.css("right").split('px')[0]);

                    });
                }

                function touchDrag() {
                    var eventEle = keyboard;
                    var stx = sty = etx = ety = curX = curY = 0;
                    var MTouch = false;
                    var eleRight = +eventEle.css("right").split('px')[0];
                    var eleBottom = +eventEle.css("bottom").split('px')[0];
                    eventEle.on("touchstart", function (event) { //touchstart
                        // alert(bToch);
                        var event = event.originalEvent;
                        MTouch = true;
                        curX = curY = 0;
                        // 元素当前位置
                        eleRight = +eventEle.css("right").split('px')[0];
                        eleBottom = +eventEle.css("bottom").split('px')[0];
                        // 手指位置
                        stx = event.touches[0].pageX;
                        sty = event.touches[0].pageY;
                        //console.log("up",+eventEle.css("right").split('px')[0]);
                    });
                    eventEle.on("touchmove", function (event) {

                        if (MTouch && !bToch) {

                            var event = event.originalEvent;
                            event.preventDefault();
                            curX = event.touches[0].pageX - stx;
                            curY = event.touches[0].pageY - sty;
                            //console.log("move",eleRight-curX);
                            //alert(eleRight+"-"+gundongX);
                            eventEle.css({"right": eleRight - curX, "bottom": eleBottom - curY});
                        }

                    });
                    eventEle.on("touchend", function (event) {
                        stx = etx = curX = sty = ety = curY = 0;
                        MTouch = false;

                    })

                }

                //模拟键盘事件,仅支持firefox，ie8-

                function simulateKeyEvent(target, keyCode) {
                    var customEvent = null;
                    var a = typeof document.createEvent;

                    if (typeof document.createEvent == "function") {//firefox
                        try {
                            customEvent = document.createEvent("KeyEvents");
                            customEvent.initKeyEvent("keypress", true, true, window, false, false, false, false, keyCode, keyCode);
                            target.dispatchEvent(customEvent);
                        } catch (ex) {
                            //console.log("This example is only demonstrating event simulation in firefox and IE.");
                        }


                    } else if (document.createEventObject) { //IE
                        customEvent = document.createEventObject();
                        customEvent.bubbles = true;
                        customEvent.cancelable = true;
                        customEvent.view = window;
                        customEvent.ctrlKey = false;
                        customEvent.altKey = false;
                        customEvent.shiftKey = false;
                        customEvent.metaKey = false;
                        customEvent.keyCode = keyCode;
                        target.fireEvent("onkeypress", customEvent);

                    }
                    else {
                        //console.log("This example is only demonstrating event simulation in firefox and IE.");
                    }
                }

            });
        };
    })(jQuery);
</script>
</body>
</html>