<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!doctype html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Resto+ POS</title>
    <link rel="stylesheet" type="text/css" href="assets/boot/bootstrap.min.css"/>
    <link rel="stylesheet" href="assets/boot/dataTables.bootstrap.min.css"/>
    <link rel="stylesheet" href="assets/boot/toastr.min.css"/>
    <link href="assets/boot/bootstrap-switch.min.css" rel="stylesheet">
    <link href="assets/boot/loaders.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/datetimepicker/bootstrap-datetimepicker.min.css"/><!-- 日期框 -->

    <link rel="stylesheet" href="assets/css/weui.css" />
    <link rel="stylesheet" href="assets/css/addDish.css" />
    <link rel="stylesheet" href="assets/css/custom.css"/>
    <link rel="stylesheet" href="assets/css/keyboard.css"/>
    <link rel="shortcut icon" href="assets/img/favicon.ico" />
    <style type="text/css">
        *{
            -webkit-user-select:none;
            -moz-user-select:none;
            -ms-user-select:none;
            user-select:none;
        }
        .ball-spin-fade-loader{
            top:-40px;
            left: 150px;
        }
        .ball-spin-fade-loader > div {
            background: #777;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div id="header">
        <div class="row">
            <div class="col-sm-4">
                <!-- <div class="logo"></div>
                <div class="time"></div>  -->
                【${sessionScope.current_brand.brandName}】${sessionScope.current_shop.name }
                <span class="glyphicon glyphicon-ok-sign" style="color: #1AFA29;" id="conn_ok">连接正常</span>
                <span class="glyphicon glyphicon-remove-sign" style="color: #D81E06;display: none;" id="conn_break">连接中断</span>
            </div>
            <div class="col-sm-8">
                <div class="operator pull-right">

                    <label id="tvState" style="color: red;display: none;" >电视状态：</label><label id="tvStatus" style="display: none;padding-right: 15px;color: red"></label>
                    <%--<label style="padding-right: 15px;">今日订单总数: <span style="color: red" id="orderCount"></span>份</label>--%>
                    <%--<label style="padding-right: 15px;">今日订单总额: <span style="color: red" id="orderTotal"></span>元</label>--%>
                    <%--<c:if test="${sessionScope.current_shop.shopMode==2}">--%>
                        <%--<button class="btn btn-warning" onclick="clearMap()">清空</button>--%>
                    <%--</c:if>--%>


                        <c:if test="${sessionScope.current_shop.shopMode==2}">
                            <%--<div>--%>
                            <%--<ready-order-component></ready-order-component>--%>
                            <%--</div>--%>
                            <span id="createOrderBtn">
                                <create-order></create-order>
                            </span>

                            <span id="refreshBtn">
                                <refresh-tv></refresh-tv>
                            </span>

                            <%--<button class="btn btn-warning" >点餐</button>--%>
                        </c:if>
                    <%--<button class="btn btn-primary" onclick="refresh()">刷新</button>--%>
                    <button class="btn btn-primary" onclick="loginOut()">登出</button>
                </div>
            </div>
        </div>
    </div>

    <div class="row page-container">
        <div class="col-sm-2 content-menu">
            <ul class="list-group">
                <li class="list-group-item active" data-tab="#new-order-tab" id="new-order-li" style="z-index: 0;">
                    <c:if test="${sessionScope.current_shop.shopMode !=5}">
                        新增订单<span class="badge" style="background-color: #d9534f;color: #FFFFFF" id="menu_order_num"></span>
                    </c:if>
                    <c:if test="${sessionScope.current_shop.shopMode ==5}">
                        未支付订单
                    </c:if>
                </li>

                <li class="list-group-item" data-url="subpage/history_orderlist">已完成订单</li>
                <li class="list-group-item" data-url="subpage/out_food_list">外卖订单</li>
                <!--<li class="list-group-item " data-url="pos/subpage/waimai_order">外卖订单</li> -->
                <!--<li class="list-group-item" data-url="subpage/setempty">沽清</li>-->
                <li class="list-group-item" data-url="subpage/stock_list">
                	<span class="badge" style="background-color: #d9534f;color: #FFFFFF" id="menu_sell_num"></span> 
                	库存列表
                </li>
                <c:if test="${sessionScope.current_brand_setting.openPosCharge == 1 && sessionScope.current_shop.openPosCharge == 1}">
                    <li class="list-group-item" data-url="subpage/account_list">账户充值</li>
                </c:if>
                <!--<li class="list-group-item" data-url="subpage/sms_list">短信列表</li>-->
                <li class="list-group-item" data-url="subpage/report_list">结算报表</li>
                <c:if test="${sessionScope.current_brand_setting.receiptSwitch == 1}">
                    <li v-if="" class="list-group-item" data-url="subpage/receipt_list">发票管理</li>
                </c:if>
                <%--<li class="list-group-item" data-url="subpage/print_list" id="errorList">未打印订单</li>--%>
            </ul>
        </div>
        <div class="col-sm-10 content-page">
            <div class="content-page-body full-height"></div>
            <div id="new-order-tab" class="tab full-height">
                <ready-order-component></ready-order-component>
            </div>
        </div>
    </div>

    <div class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title text-center"><strong>历史订单-----</strong></h4>
                </div>
                <div class="modal-body">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>消费码</th>
                            <th>菜品总数</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody style="font-size: 22px">
                        <tr>
                            <td>1564</td>
                            <td>5</td>
                            <td>
                                <button class="btn btn-info btn-block">打&nbsp;印</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <div class="col-md-2">
                        <button type="button" class="btn btn-bolck btn-danger" data-dismiss="modal">不处理</button>
                    </div>
                    <div class="col-md-8 col-md-offset-2">
                        <button type="button" class="btn btn-block btn-primary">打印全部</button>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->


    <div id="footer"></div>
</div>

<script src="assets/js/socket.io-1.3.5.min.js"></script>
<script src="assets/js/yunba-js-sdk.js"></script>
<script src="assets/boot/jquery.min.js"></script>
<script src="assets/boot/bootstrap.min.js"></script>
<script src="assets/boot/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="assets/boot/dataTables.bootstrap.min.js"></script>
<script src="assets/boot/loaders.css.min.js"></script>
<script src="assets/boot/toastr.min.js"></script>
<script src="assets/boot/vue.min.js"></script>
<script src="assets/boot/sockjs.js"></script>
<script src="assets/boot/iscroll.min.js"></script>
<script src="assets/boot/bootstrap-switch.min.js"></script>
<!-- 日期框 -->
<script type="text/javascript" src="assets/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="assets/js/datetimepicker/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="assets/js/keyboard.js"></script>
<script src="assets/js/customer.js"></script>
<script src="assets/js/service.js"></script>
<script src="assets/js/printer.js"></script>
<script src="assets/js/utils.js"></script>
<script src="assets/js/basemix.js"></script>
<script src="assets/js/numberKeyBoard.js"></script>
<script src="assets/js/customer.js"></script>
<script src="assets/mode/${sessionScope.current_shop.shopMode }/custom.js"></script>
<script>
    var App = function () {
        this.init = function () {
            resetWindow();
            handlerMenu();
        }
        this.resetWindow = function () {
            var header_height = $("#header").outerHeight();
            var footer_height = $("#footer").outerHeight();
            var window_height = $(window).height();
            var contentBody = $(".content-page");
            var body_height = window_height - header_height - footer_height;
            contentBody.height(body_height);
            var orderOperatorDiv = $(".order-operator");
            var searchDiv = $(".search");
            var orderTab_height = $("#new-order-tab").height();
            orderOperatorDiv.height(orderTab_height - searchDiv.height() - 2);
        }

        this.handlerMenu = function () {
            var contentBody = $(".content-page-body");
            var reloadType = true;
            $(".content-menu").on("click", "ul>li[data-url]", function (e) {
                $(this).parents("ul").find("li.active").removeClass("active");
                $(this).addClass("active");

                contentBody.parent().children().hide();
                contentBody.show();
                reloadType = false;
                var href = $(this).data("url");
                $.ajax({
                    url: href,
                    success: function (html) {
                        contentBody.html(html);
                        resetWindow();
                    }
                });
            });
            $(".content-menu").on("click", "ul>li[data-tab]", function (e) {
                $(this).parents("ul").find("li.active").removeClass("active");
                $(this).addClass("active");
                var tab = $(this).data("tab");
                var dynamicTab = $(tab);
                dynamicTab.parent().children().hide();
                dynamicTab.show();
//                if(!reloadType){
//
//                    window.location.reload();
//                }
            });
            $(".content-menu ul>li.active").click();
        }
        return this;
    }();
    var printReceiptTask=null;
    $(document).ready(function () {
        toastr.options = {
            "closeButton": true,
            "debug": false,
            "positionClass": "toast-top-center",
            "showDuration": "500",
            "hideDuration": "500",
            "timeOut": "3000",
            "extendedTimeOut": "500",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        }
        App.init();

        $.ajax({
            url: 'order/getOrderAccount',
            method: 'post',
            type: 'json',
            success: function (result) {
                if (result.success && result.data) {
                    $('#orderCount').text(result.data.orderCount);
                    $('#orderTotal').text(result.data.orderTotal == null ? 0 : result.data.orderTotal);
                }
            }
        });		
		
		$.post("article/getStock",{familyId : null, empty : 0, activated : null},function (result) {
            if (result.success){
            	var articleSells = result.data;
            	articleSells.forEach(function(article){
					if(article.currentStock <= article.inventoryWarning){
						$("#menu_sell_num").text("!");
					}
		        });
            }
        });
		
        //默然汉化所有database
        $.extend($.fn.dataTable.defaults, {
            language: {url: "//cdn.datatables.net/plug-ins/1.10.11/i18n/Chinese.json"},
        });
    });

	
	
    function showCreateOrder(){
        new Vue({
            el: '#a',
            data: exampleData
        })
    }

    function loginOut(){
        $.ajax({
            url: 'config/loginOut',
            method: 'post',
            type: 'json',
            success: function (result) {
                window.location.href = "/pos/login";
            }
        });
    }

//    function refresh() {
//        window.location.reload();
//    }
    var shopId = "${current_shop_id}";//保存shopId

    var shopName = "${current_shop.name}";

    var brandName = "${current_brand.brandName}";

    var brandId = "${current_brand.id}";

    var Hub = new Vue();  // 1) 中转站,其中不需要设置任何参数(全局)

</script>

</body>
</html>