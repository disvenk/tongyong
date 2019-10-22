<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<div id="control">
    <h2 class="text-center"><strong>${brandName}-历史会员账户报表</strong></h2><br/>
    <br/>
    <br/>

    <!-- 品牌账户报表 -->
    <div class="tab-pane">
        <div class="panel panel-primary" style="border-color:write;">
            <!-- 品牌订单 -->
            <div class="panel panel-info">
                <div class="panel-heading text-center">
                    <strong style="margin-right:100px;font-size:22px">品牌账户报表</strong>
                    <button type="button" style="float: right;" class="btn btn-primary" @click="createExcel(1)">下载报表</button>
                </div>
                <div class="panel-body">
                    <table id="brandChargeTotalTable" class="table table-striped table-bordered table-hover" width="100%">
                        <thead>
                        <tr>
                            <th>
                                品牌历史储值总数量
                            </th>
                            <th>
                                品牌历史储值总额
                            </th>
                            <th>
                                品牌历史储值赠送总额
                            </th>
                            <th>
                                储值剩余总额
                            </th>
                            <th>
                                储值赠送剩余总额
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    {{brandChargeTotal.historyChargeCount}}
                                </td>
                                <td>
                                    {{brandChargeTotal.historyChargeMoney}}
                                </td>
                                <td>
                                    {{brandChargeTotal.historyRewardMoney}}
                                </td>
                                <td>
                                    {{brandChargeTotal.historyChargeBalance}}
                                </td>
                                <td>
                                    {{brandChargeTotal.historyRewardBalance}}
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="panel-heading text-center">
                    <strong style="margin-right:100px;font-size:22px">会员账户报表</strong>
                    <button type="button" style="float: right;" class="btn btn-primary" @click="createExcel(2)" v-if="state == 1">下载报表</button>
                    <button type="button" style="float: right;" disabled class="btn btn-primary" v-if="state == 2">会员数据下载中，请勿刷新或切换页面</button>
                </div>
                <div class="panel-body">
                    <table id="customerChargeTotalTable" class="table table-striped table-bordered table-hover" width="100%">
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    //创建vue对象
    var vueObj =  new Vue({
        el:"#control",
        data:{
            brandChargeTotal : {},
            customerDataTable : {},
            state : 1 //会员账户报表下载状态
        },
        created : function() {
            this.customerChargeTotalTable();
            this.getBrandChargeTotal();
        },
        methods:{
            customerChargeTotalTable : function(){
                //生成会员充值表
                this.customerDataTable = $("#customerChargeTotalTable").DataTable({
                    lengthMenu: [ [10, 20, 50, 80], [10, 20, 50, 80] ],
                    order: [[ 0, "asc" ]],
                    serverSide: true,
                    bSort: false, //排序功能
                    ajax: {
                        "url": 'chargeTotal/getCustomerChargeTotal',
                        "type": 'post'
                    },
                    columns : [
                        {
                            title : "昵称",
                            data : "nickname"
                        },
                        {
                            title : "手机号码",
                            data : "telephone"
                        },
                        {
                            title : "历史储值总数",
                            data : "historyChargeCount"
                        },
                        {
                            title : "历史储值总额",
                            data : "historyChargeMoney"
                        },
                        {
                            title : "储值赠送总额",
                            data : "historyRewardMoney"
                        },
                        {
                            title : "账户余额",
                            data : "remain"
                        },
                        {
                            title : "充值账户余额",
                            data : "historyChargeBalance"
                        },
                        {
                            title : "充值赠送余额",
                            data : "historyRewardBalance"
                        },
                        {
                            title : "红包余额",
                            data : "redBalance"
                        }
                    ]
                });
            },
            getBrandChargeTotal : function () {
                var that = this;
                try{
                    $.post("chargeTotal/getChargeTotalInfo", function (result) {
                        if (result.success){
                            that.brandChargeTotal = result.data;
                            toastr.success("查看品牌账户报表成功");
                        }else {
                            toastr.error("查看品牌账户报表服务器出错");
                        }
                    });
                }catch (e){
                    toastr.error("查看品牌账户报表出错");
                }
            },
            createExcel : function (type) {
                var that = this;
                try {
                    if (type == 1) {
                        //下载品牌充值汇总信息
                        $.post("chargeTotal/createBrandChargeTotalExcel", that.brandChargeTotal, function (result) {
                            if (result.success) {
                                that.downloadChargeTotalExcel(result.data);
                            }else {
                                toastr.error("下载报表失败");
                            }
                        });
                    }else if (type == 2) {
                        //将当前下载状态设为下载中
                        that.state = 2;
                        //下载会员充值信息
                        var search = that.customerDataTable.ajax.params().search.value;
                        //验证下载状态
                        var time = setInterval(function () {
                            that.verifyDownloadState();
                        }, 10000);
                        $.post("chargeTotal/createCustomerChargeExcel", {search : search}, function (result) {
                            if (result.success) {
                                that.downloadChargeTotalExcel(result.data);
                            }else {
                                toastr.error("下载报表失败");
                            }
                            that.state = 1;
                            clearInterval(time);
                        });
                    }
                } catch (e) {
                    toastr.error("下载报表出错");
                }
            },
            downloadChargeTotalExcel : function (path) {
                window.location.href = "chargeTotal/downloadExcel?path=" + path;
                toastr.success("下载报表成功");
            },
            verifyDownloadState : function () {
                if (this.state != 1) {
                    toastr.success("由于数据量过大，请耐心等待");
                }
            }
        }
    });
</script>

