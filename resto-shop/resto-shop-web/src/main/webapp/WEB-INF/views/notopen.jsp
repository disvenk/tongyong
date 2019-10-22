<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <style>
        html,body {
            position: relative;
            height: 100%;
            overflow-y: hidden;
            background: #364150;
        }

        .msg-dialog {
            margin: 0 auto;
            width: 30%;
            height: 30%;
            z-index: 15;
            font-family: "微软雅黑";
            font-size: 1.5rem;
            background: #fff;
            display: table;
        }
        .msg-dialog .center {
            display: block;
            text-align: center;
        }
        .weui_toast {
            position: fixed;
            border-radius: 5px;
            color: #000;
            display: table;
            width: 80%;
            height: 80%;
        }
        .middle {
            display: table-cell;
            vertical-align: middle;
            padding: 15px;
        }
        .leftText {
            display: block;
            text-align: left;
            margin-left: 8%;
        }
        .rightText {
            display: block;
            text-align: left;
            margin-left: 15%;
        }
    </style>
</head>
<body>
    <c:if test="${empty netOpen}">
        <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
        <center><h1>暂未开启此功能，请联系管理员开通！</h1></center>
    </c:if>
    <c:if test="${!empty netOpen}">
        <div class="weui_toast">
            <div class="middle">
                <div class="msg-dialog">
                    <p class="middle">
                        <span class="center">系统公告</span>
                        <span class="leftText">尊敬的用户：</span>
                        <span style="text-indent:15%;display: block;">为保证高峰期间稳定性，管理后台使用时间临时调整为：</span>
                        <span class="rightText">上午 0:00 - 11:00</span>
                        <span class="rightText">下午 13:00 - 17:00</span>
                        <span class="rightText">晚上 20:00 - 0:00</span>
                        <span class="leftText">在此期间带来的不便请您谅解！感谢您的配合！</span>
                        <span class="rightText">客服热线:400-805-1711</span>
                    </p>
                </div>
            </div>
        </div>
    </c:if>
</body>
</html>
