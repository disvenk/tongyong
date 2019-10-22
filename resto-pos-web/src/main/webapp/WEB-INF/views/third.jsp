<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String resourcePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
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
    <link rel="stylesheet" type="text/css" href="assets/boot/bootstrap.min.css"/>
    <link rel="shortcut icon" href="assets/img/favicon.ico"/>
    <link rel="stylesheet" href="assets/css/jquery-ui.css">
    <!--引入样式css-->
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
            <div class="form-group">
                <label class="col-xs-4 col-sm-4 control-label text-center">开始时间</label>
                <div class="col-xs-7 col-sm-7">
                    <input  class="form-control" id="beginTime"
                           name="beginTime" required="required" placeholder="选择开始时间" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-xs-4 col-sm-4 control-label text-center">结束时间</label>
                <div class="col-xs-7 col-sm-7">
                    <input  class="form-control" id="endTime"
                            name="endTime" required="required" placeholder="选择结束时间" >
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary btn-block"
                    id="loginbtn" onclick="checkLogin()">导出数据
            </button>
        </div>
    </form>
    <br/>
    <br/>
    <p class="text-danger text-center">
        <span id="errorMsg"></span>
    </p>
</div>


<script src="assets/boot/jquery.min.js"></script>
<!-- <script src="//cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script> -->
<script src="assets/boot/bootstrap.min.js" />
<script src="assets/js/jquery-1.9.1.js"></script>
<script src="assets/js/jquery-ui.js"></script>
<!--添加datepicker支持-->

<script type="text/javascript">

    $(document).ready(function () {
        $( "#beginTime" ).datepicker( );
        $( "#beginTime" ).datepicker( "option", "dateFormat", "yy-mm-dd" );
        $( "#endTime" ).datepicker( );
        $( "#endTime" ).datepicker( "option", "dateFormat", "yy-mm-dd" );
    });


    function checkLogin() {
        if ($('#loginname').val() == '') {
            $('#loginname').focus();
            return;
        }
        if ($('#password').val() == '') {
            $('#password').focus();
            return;
        }

        $.ajax({
            url: "third/checkLogin",
            method: "post",
            data: {loginname: $('#loginname').val(), password: $('#password').val()},
            success: function (result) {
                if (result.success) {
                    $('#errorMsg').text("");
                    window.location.href = "/pos/third/export?brandSign=" + $('#loginname').val()+"&beginTime="+$('#beginTime').val()+"&endTime="+$('#endTime').val();
                } else {
                    $('#errorMsg').text(result.message);
                }
            }
        });
    }


</script>


</body>
</html>