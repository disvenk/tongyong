<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div id="print-order-compon">
    <print-order-component></print-order-component>
</div>
<script>

    var printOrder = new Vue({
        el:"#print-order-compon",
    });
    //时间插件
    $('.form_datetime').datetimepicker({
        endDate: new Date(),
        minView: "month",
        maxView: "month",
        autoclose: true,//选择后自动关闭时间选择器
        todayBtn: true,//在底部显示 当天日期
        todayHighlight: true,//高亮当前日期
        format: "yyyy-mm-dd",
        startView: "month",
        language: "zh-CN"
    });

    //文本框默认值
    $('.form_datetime').val(new Date().format("yyyy-MM-dd"));

    var beginClosingDate = new Date().format("yyyy-MM-dd");
    var endClosingDate = new Date().format("yyyy-MM-dd");

    function isEmpty(str) {
        return str == null || str == "" ? true : false;
    }


    var query = "dayReportTable";//默认查询和下载的是第一个tab 营业收入

    var tb1 = $("#dayReportTable").DataTable({
        language: language,
        ordering: false,
        columns : [
            {
                title : "店铺名称",
                data : "shopName"
            },
            {
                title : "订单总数",
                data : "orderCount"
            },
            {
                title : "订单总额",
                data : "totalIncome"
            },
            {
                title : "微信支付",
                data : "wechatIncome"
            },
            {
                title : "支付宝支付",
                data : "aliPayment"
            },
            {
                title : "现金实收",
                data : "moneyPay"
            },
            {
                title : "银联支付",
                data : "backCartPay"
            },
            {
                title : "充值支付",
                data : "chargeAccountIncome"
            },
            {
                title : "红包",
                data : "redIncome"
            },
            {
                title : "优惠券",
                data : "couponIncome"
            },

            {
                title : "充值赠送支付",
                data : "chargeGifAccountIncome"
            },
            {
                title : "等位红包",
                data : "waitNumberIncome"
            },
            {
                title : "退菜返还红包",
                data : "articleBackPay"
            },
//            {
//                title : "找零",
//                data : "giveChangePayment"
//            },
            {
                title : "闪惠支付",
                data : "shanhuiPayment"
            },
            {
                title : "会员支付",
                data : "integralPayment"
            },
            {
                title : "现金退款",
                data : "refundCrashPayment"
            }
        ]

    });

    var tbApi = null;
    var isFirst = true;
    var sort = "desc";//默认按销量排序
    var select;

    var tb2 = $("#articleSaleTable").DataTable({
        language: language,
        "dom": '<"toolbar">frtip',
    	"scrollY": "200px",
    	"scrollCollapse": "true",
        ordering: false,
        columns: [
            {
                title: "菜品分类",
                data: "articleFamilyName",
                createdCell: function (td, tdData, rowData) {
                    if (isEmpty(tdData)) {
                        var lab = $("<span>");
                        if (isEmpty(rowData.articleFamilyId)) {
                            lab.html("分类不详").addClass("label label-warning");
                        } else {
                            lab.html("该分类已不存在").addClass("label label-warning");
                        }
                        $(td).html(lab);
                    }

                }
            },
            {
                title: "菜品名称",
                data: "articleName",
                createdCell: function (td, tdData, rowData) {
                    if (isEmpty(rowData.articleFamilyId) && isEmpty(rowData.articleFamilyName)) {
                        $(td).html(tdData + "&nbsp;<span class='label label-danger'>已被删除</span>");
                    }
                }
            },
            {
                title: "菜品销量(份)",
                data: "shopSellNum",
            },
            {
                title: "退菜数量(份)",
                data: "refundCount",
            },
            {
                title: "退菜金额(元)",
                data: "refundTotal",
            },
        ],
        initComplete: function () {//列筛选
            tbApi = this.api();
            appendSelect(tbApi);
            isFirst = false;
        }
    });

    //添加按钮
    $("div.toolbar").html('<button type="button" class="btn btn-primary" id="sortById">按菜品序号排序</button>&nbsp;&nbsp<button type="button" class="btn btn-primary" id="sortByNum">按销量排序</button>');
    //添加分类下拉框
    function appendSelect(api) {
        api.columns().indexes().flatten().each(function (i) {
            if (i == 0) {
                var column = api.column(i);
                $(column.header()).html("菜品分类");
                var $span = $('<span class="addselect">▾</span>').appendTo($(column.header()))
                select = $('<select><option value="">全部</option></select>')
                        .appendTo($(column.header()))
                        .on('click', function (evt) {
                            evt.stopPropagation();
                            var val = $.fn.dataTable.util.escapeRegex(
                                    $(this).val()
                            );
                            column.search(val ? '^' + val + '$' : '', true, false).draw();
                        });
                column.data().unique().sort().each(function (d, j) {
                    if (d != null && d != "") {
                        select.append('<option value="' + d + '">' + d + '</option>')
                        $span.append(select)
                    }
                });

            }
        });
    }

    //搜索
    $("#searchReport").click(function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        beginClosingDate = beginDate;
        endClosingDate = endDate;
        var timeCha = new Date(endDate).getTime() - new Date(beginDate).getTime();
        //判断 时间范围是否合法
        if (beginDate > endDate) {
            toastr.error("开始时间不能大于结束时间")
            return;
        }else if (timeCha > 2678400000){
            toastr.error("暂时未开放大于一月以内的查询")
            return;
        }
        var nowDate = new Date().format("HH");
        nowDate = parseInt(nowDate);
        if (nowDate >= 11 && nowDate <= 13){
            toastr.clear();
            toastr.error("亲，报表查询功能正在维护中，请您多多谅解~维护时间段： 11:00-13:00 17:00-19:00");
            return false;
        }else if (nowDate >= 17 && nowDate <= 20){
            toastr.clear();
            toastr.error("亲，报表查询功能正在维护中，请您多多谅解~维护时间段： 11:00-13:00 17:00-19:00");
            return false;
        }
        var num = getNumActive();
        switch (num) {
            case 1:
                queryOrderPaymentItems();
                break;
            case 2:
                queryOrderArticleItems();
                break;
        }

    })

    //判断活动的选项卡是第几个
    function getNumActive() {
        var value = $("#ulTab li.active a").text();
        value = Trim(value)//去空格
        if (value == '营业收入报表') {
            return 1;
        } else if (value == "菜品销售报表") {
            return 2;
        }
    }

    $('#ulTab a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
        var num = getNumActive()
        switch (num) {
            case 1:
                queryOrderPaymentItems();
                break;
            case 2:
                queryOrderArticleItems();
                break;
        }
    })
    //下载报表
    //店铺报表下载
    $("#shopReport").click(function () {
        console.log($("#ulTab li.active a").text());
        var value = $("#ulTab li.active a").text();
        value = Trim(value)//去空格
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        if (value == '营业收入报表') {
            var data = {"beginDate": beginDate, "endDate": endDate};
            location.href = "order/income_excel?beginDate=" + beginDate + "&&endDate=" + endDate;
        } else if (value == "菜品销售报表") {
            var selectValue = select[0].value;
            var data = {"beginDate": beginDate, "endDate": endDate, "sort": sort}
            location.href = "order/article_excel?beginDate=" + beginDate + "&&endDate=" + endDate + "&&selectValue=" + selectValue + "&&sort=" + sort;
        }
    })



    //按菜品序号排序
    $("#sortById").click(function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        sort = "0";
        queryOrderArticleItems();
    })
    //按销量排序

    $("#sortByNum").click(function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        sort = "desc";
        queryOrderArticleItems();
    });

    function queryOrderPaymentItems() {
        toastr.clear();
        toastr.success("正在查询中...");
        try{
            $.post("order/orderPaymentItems",{beginDate : $("#beginDate").val(),
                endDate : $("#endDate").val()},function (result) {
                if (result.success) {
                    tb1.clear();
                    tb1.rows.add(result.data).draw();
                    toastr.clear();
                    toastr.success("查询成功");
                }else{
                    toastr.clear();
                    toastr.error("连接超时，请稍后重试");
                }
            });
        }catch(e){
            toastr.clear();
            toastr.error("网络异常，请刷新重试");
        }
    }

    function queryOrderArticleItems() {
        toastr.clear();
        toastr.success("正在查询中...");
        try{
            $.post("order/orderArticleItems",{beginDate : $("#beginDate").val(),
                endDate : $("#endDate").val(), sort : sort},function (result) {
                if (result.success) {
                    tb2.clear();
                    tb2.rows.add(result.data).draw();
                    appendSelect(tbApi);
                    toastr.clear();
                    toastr.success("查询成功");
                }else{
                    toastr.clear();
                    toastr.error("连接超时，请稍后重试");
                }
            });
        }catch(e){
            toastr.clear();
            toastr.error("网络异常，请刷新重试");
        }
    }

    $(function () {
        queryOrderPaymentItems();
    });

    //datatables语言设置
    var language = {
        "sProcessing": "处理中...",
        "sLengthMenu": "显示 _MENU_ 项结果",
        "sZeroRecords": "没有匹配结果",
        "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
        "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
        "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
        "sInfoPostFix": "",
        "sSearch": "搜索:",
        "sUrl": "",
        "sEmptyTable": "表中数据为空",
        "sLoadingRecords": "载入中...",
        "sInfoThousands": ",",
        "oPaginate": {
            "sFirst": "首页",
            "sPrevious": "上页",
            "sNext": "下页",
            "sLast": "末页"
        },
        "oAria": {
            "sSortAscending": ": 以升序排列此列",
            "sSortDescending": ": 以降序排列此列"
        }
    };
    function Trim(str) {
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }
</script>