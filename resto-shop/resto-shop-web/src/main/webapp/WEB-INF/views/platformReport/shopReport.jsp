<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="controlShop">
    <a class="btn btn-info ajaxify" href="platformReport/list">
        <span class="glyphicon glyphicon-circle-arrow-left"></span>
        返回
    </a>
    <h2 class="text-center">
        <strong>${shopName}店铺外卖订单表</strong>
    </h2>
    <br />
    <div class="row">
        <div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 50px;">
                    <label for="beginDate">开始时间：</label>
                    <input type="text" class="form-control form_datetime" id="beginDate" v-model="searchDate.beginDate"   readonly="readonly">
                </div>
                <div class="form-group" style="margin-right: 50px;">
                    <label for="endDate">结束时间：</label>
                    <input type="text" class="form-control form_datetime" id="endDate" v-model="searchDate.endDate"   readonly="readonly">
                </div>
                <button type="button" class="btn btn-primary" @click="today"> 今日</button>
                <button type="button" class="btn btn-primary" @click="yesterDay">昨日</button>
                <button type="button" class="btn btn-primary" @click="week">本周</button>
                <button type="button" class="btn btn-primary" @click="month">本月</button>
                <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>&nbsp;
                <button type="button" class="btn btn-primary" @click="createExcel" v-if="state == 1">下载报表</button>
                <button type="button" class="btn btn-default" disabled="disabled" v-if="state == 2">下载数据过多，正在生成中。请勿刷新页面</button>
                <button type="button" class="btn btn-success" @click="download" v-if="state == 3">已完成，点击下载</button><br/>
            </form>
        </div>
    </div>
    <br /> <br />
    <!-- 店铺订单列表  -->
    <div class="panel panel-info">
        <div class="panel-heading text-center" style="font-size: 22px;">
            <strong>店铺外卖订单表</strong>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-bordered table-hover" id="shopAppraise">
            </table>
        </div>
    </div>
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content" style="margin-top: 170px;">
                <div class="modal-header" style="background-color:#E0EBF9;">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">×</span></button>
                    <h4 class="modal-title" id="exampleModalLabel" style="text-align:center">外卖订单详情</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <ul class="list-unstyled">
                            <li>店铺名称：{{platformOrderk.shopName}}</li>
                            <li v-if="platformOrderk.type == 1">订单来源：饿了么订单</li>
                            <li v-if="platformOrderk.type == 2">订单来源：美团外卖订单</li>
                            <li v-if="platformOrderk.type == 3">订单来源：百度外卖订单</li>
                            <li>订单编号：{{platformOrderk.platformOrderId}}</li>
                            <li>订单时间：{{platformOrderk.orderCreateTime}}</li>
                            <li>手机号：{{platformOrderk.phone}}</li>
                            <li>姓名：{{platformOrderk.name}}</li>
                            <li>收货地址：{{platformOrderk.address}}</li>
                            <li>订单金额：{{platformOrderk.originalPrice}}</li>
                            <li>支付金额：{{platformOrderk.totalPrice}}</li>
                            <li>状态：{{platformOrderk.payType}}</li>
                        </ul>
                    </div>
                    <div class="form-group">
                        <table class="table">
                            <tr>
                                <%--<td>餐品类别</td>--%>
                                <td>餐品名称</td>
                                <td>餐品单价（元）</td>
                                <td>餐品数量</td>
                                <td>小计（元）</td>
                            </tr>
                            <tr v-for="art in platformOrderk.platformOrderDetails">
                                <%--<td></td>--%>
                                <td>{{art.name}}</td>
                                <td>{{art.price}}</td>
                                <td>{{art.quantity}}</td>
                                <td>{{art.price*art.quantity}}</td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success btn-sm" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="assets/customer/date.js" type="text/javascript"></script>
<script>
    //时间插件
    $('.form_datetime').datetimepicker({
        endDate:new Date(),
        minView:"month",
        maxView:"month",
        autoclose:true,//选择后自动关闭时间选择器
        todayBtn:true,//在底部显示 当天日期
        todayHighlight:true,//高亮当前日期
        format:"yyyy-mm-dd",
        startView:"month",
        language:"zh-CN"
    });

    var brandUnitAPI = null;
    var beginDate = "${beginDate}";
	var endDate = "${endDate}";
	var shopId = "${shopId}";
	var vueObjShop = new Vue({
	    el : "#controlShop",
	    data : {
	        searchDate : {
	            beginDate : "",
	            endDate : "",
	        },
            shopAppraiseTable : {},
            appraiseShopDtos : [],
            appraiseShopList : [],
            platformOrderk:[],
            state:1,
            object : {},
            length : 0,
            start : 0,
            end : 200,
            startPosition : 205,
            index : 1
	    },
	    created : function() {
	        this.searchDate.beginDate = beginDate;
	        this.searchDate.endDate = endDate;
	        this.initDataTables();
	        this.searchInfo();
	    },
	    methods : {
	        initDataTables:function () {
	            //that代表 vue对象
	            var that = this;
                that.shopAppraiseTable = $("#shopAppraise").DataTable({
                    lengthMenu: [ [50, 75, 100, 150], [50, 75, 100, "All"] ],
                    order: [[ 0, "desc" ]],
                    columns : [
                        {
                            title:'店铺',
                            data:'shopName'
                        },
                        {
                            title : "外卖平台",
                            data : "type",
                            orderable : false,
                            createdCell:function(td,tdData){
                                var str = "未知"
                                if(tdData == "1"){
                                    str = "饿了么"
                                }else if(tdData == "2"){
                                    str = "美团"
                                }else if(tdData == "3"){
                                    str = "百度"
                                }
                                $(td).html(str);
                            },
                            s_filter: true
                        },
                        {
                            title:'下单时间',
                            data:'createTime',
                            createdCell:function(td,tdData){
                                $(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm:ss"));
                            }
                        },
                        {
                            title : "订单原价",
                            data : "originalPrice",
                        },
                        {
                            title :"支付金额",
                            data : "totalPrice"
                        },
                        {
                            title : "姓名",
                            data : "name",
                        },
                        {
                            title : "手机号",
                            data : "phone"
                        },
                        /*{
                            title : "评价",
                            data : "type",
                            createdCell:function(td,tdData){
                                var str = "未知"
                                if(tdData == "1"){
                                    str = ""
                                }else if(tdData == "2"){
                                    str = ""
                                }else if(tdData == "3"){
                                    str = ""
                                }
                                $(td).html(str);
                            }
                        },*/
                        {
                            title : "订单状态",
                            data : "payType",
                        },
                        {
                            title: "查看详情",
                            data: "platformOrderId",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var button = $("<button type='button' class='btn green'>查看详情 </button> ");
                                button.click(function(){
                                    that.platformOrderDetailModal(tdData);
                                });
                                $(td).html(button);
                            }
                        }
                    ],
                    initComplete: function () {
                        brandUnitAPI = this.api();
                        that.brandUnitTable();
                    }
                });
	        },
	        searchInfo : function() {
                var that = this;
                var timeCha = new Date(that.searchDate.endDate).getTime() - new Date(that.searchDate.beginDate).getTime();
                if(timeCha < 0){
                    toastr.clear();
                    toastr.error("开始时间应该少于结束时间！");
                    return false;
                }else if(timeCha > 2678400000){
                    toastr.clear();
                    toastr.error("暂时未开放大于一月以内的查询！");
                    return false;
                }
                toastr.clear();
                toastr.success("查询中...");
	        	try{
                    $.post("platformReport/shop_data", this.getDate(), function(result) {
                        if (result.success) {
                            toastr.clear();
                            toastr.success("查询成功");
                            that.appraiseShopDtos = result.data.platformOrders;
                            brandUnitAPI.search('');
                            var column1 = brandUnitAPI.column(1);
                            column1.search('', true, false);
                            that.shopAppraiseTable.clear();
                            that.shopAppraiseTable.rows.add(result.data.platformOrders).draw();
                            that.brandUnitTable();
                        }else {
                            toastr.clear();
                            toastr.error("查询出错");
                        }
                    });
	        	}catch(e){
					toastr.clear();
                    toastr.error("系统异常，请刷新重试");
	        	}
	        },
	        getDate : function(){
	            var data = {
	                beginDate : this.searchDate.beginDate,
	                endDate : this.searchDate.endDate,
	                shopId : shopId
	            };
	            return data;
	        },
	        createExcel : function(){
                var that = this;
                try {
                    that.object = that.getDate();
                    that.appraiseShopList = that.appraiseShopDtos.sort(this.keysert('level'));
                    if (that.appraiseShopList.length <= 200){
                        that.object.shopAppraises = that.appraiseShopList;
                        $.post("platformReport/create_shop_excel",that.object,function (result) {
                            if (result.success){
                                window.location.href = "platformReport/downloadShopExcel?path="+result.data+"";
                            }else{
                                toastr.clear();
                                toastr.error("下载报表出错");
                            }
                        })
                    }else{
                        that.state = 2;
                        that.length = Math.ceil(that.appraiseShopList.length/200);
                        that.object.shopAppraises = that.appraiseShopList.slice(that.start,that.end);
                        $.post("platformReport/create_shop_excel",that.object,function (result) {
                            if (result.success){
                                that.object.path = result.data;
                                that.start = that.end;
                                that.end = that.start + 200;
                                that.index++;
                                that.appendExcel();
                            }else{
                                that.state = 1;
                                that.start = 0;
                                that.end = 200;
                                that.startPosition = 205;
                                that.index = 1;
                                toastr.clear();
                                toastr.error("生成报表出错");
                            }
                        });
                    }
                }catch (e) {
                    that.state = 1;
                    that.start = 0;
                    that.end = 200;
                    that.startPosition = 205;
                    that.index = 1;
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
	        },
            appendExcel : function () {
                var that = this;
                try {
                    if (that.index == that.length) {
                        that.object.platformOrder = that.appraiseShopList.slice(that.start);
                    } else {
                        that.object.platformOrder = that.appraiseShopList.slice(that.start, that.end);
                    }
                    that.object.startPosition = that.startPosition;
                    $.post("platformReport/appendShopExcel", that.object, function (result) {
                        if (result.success) {
                            that.start = that.end;
                            that.end = that.start + 200;
                            that.startPosition = that.startPosition + 200;
                            that.index++;
                            if (that.index - 1 == that.length) {
                                that.state = 3;
                            } else {
                                that.appendExcel();
                            }
                        } else {
                            that.state = 1;
                            that.start = 0;
                            that.end = 200;
                            that.startPosition = 205;
                            that.index = 1;
                            toastr.clear();
                            toastr.error("生成报表出错");
                        }
                    });
                }catch (e){
                    that.state = 1;
                    that.start = 0;
                    that.end = 200;
                    that.startPosition = 205;
                    that.index = 1;
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            brandUnitTable : function(){
                var api = brandUnitAPI;
                var columnsSetting = api.settings()[0].oInit.columns;
                $(columnsSetting).each(function (i) {
                    if (this.s_filter) {
                        var column = api.column(i);
                        var select = $('<select id=""><option value="">' + this.title + '(全部)</option></select>');
                        column.data().unique().each(function (d) {;
                            var str = "美团";
                            if (d == "1"){
                                str = "饿了么"
                            }
                            select.append('<option value="' + d + '">' + str + '</option>')
                        });
                        select.appendTo($(column.header()).empty()).on('change', function () {
                            var val = $.fn.dataTable.util.escapeRegex(
                                $(this).val()
                            );
                            column.search(val ? '^' + val + '$' : '', true, false).draw();
                        });
                    }
                });
            },
            download : function(){
                window.location.href = "platformReport/downloadShopExcel?path="+this.object.path+"";
                this.state = 1;
                this.start = 0;
                this.end = 200;
                this.startPosition = 205;
                this.index = 1;
            },
	        today : function(){
	            date = new Date().format("yyyy-MM-dd");
	            this.searchDate.beginDate = date
	            this.searchDate.endDate = date
	            this.searchInfo();
	        },
	        yesterDay : function(){
	            this.searchDate.beginDate = GetDateStr(-1);
	            this.searchDate.endDate  = GetDateStr(-1);
	            this.searchInfo();
	        },
	        week : function(){
	            this.searchDate.beginDate  = getWeekStartDate();
	            this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
	            this.searchInfo();
	        },
	        month : function(){
	            this.searchDate.beginDate  = getMonthStartDate();
	            this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
	            this.searchInfo();
	        },
            keysert: function (key) {
                return function(a,b){
                    var value1 = a[key];
                    var value2 = b[key];
                    return value1 - value2;
                }
            },
            platformOrderDetailModal : function (platformOrderId) {
                var that = this;
                try {
                    $.post("platformReport/platformOrderDetail",{platformOrderId: platformOrderId},function (result) {
                        if(result.success){
                            that.platformOrderk = result.data[0];
                            $("#exampleModal").modal();
                        }else{
                            toastr.clear();
                            toastr.error("查询出错");
                        }
                    });
                }catch (e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
	    }
	});

    /*$('#exampleModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget) // 触发事件的按钮
        var recipient = button.data('whatever') // 解析出data-whatever内容
        $.post("platformReport/platformOrderDetail",{platformOrderId:recipient},function (result) {
            if (result.success){
                console.log(result.data[0])
                new Vue({
                    el : "#exampleModal",
                    data : {
                        platformOrderk:result.data[0]
                    }
                });
            }
        })
        var modal = $(this)
        modal.find('.modal-title').text('外卖订单详情')
    })*/
</script>

