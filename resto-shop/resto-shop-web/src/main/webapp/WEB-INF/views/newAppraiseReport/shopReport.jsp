<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="controlShop">
    <a class="btn btn-info ajaxify" href="newAppraiseReport/list">
        <span class="glyphicon glyphicon-circle-arrow-left"></span>
        返回
    </a>
    <h2 class="text-center">
        <strong>${shopName}</strong>
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
            <strong>店铺评论记录</strong>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-bordered table-hover" id="shopAppraise">
            </table>
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
                            title:'评论时间',
                            data:'createTime',
                        },
                        {
                            title:'用户手机号',
                            data:'telephone'
                        },
                        {
                            title:'订单金额',
                            data:'orderMoney'
                        },
                        {
                            title : "评论红包金额",
                            data : "redMoney"
                        },
                        {
                            title :"评分",
                            data : "level"
                        },
                        {
                            title : "服务",
                            data : "service"
                        },
                        {
                            title : "出品",
                            data : "exhibit"
                        },
                        {
                            title : "氛围",
                            data : "ambience"
                        },
                        {
                            title : "环境",
                            data : "conditions"
                        },
                        {
                            title : "性价比",
                            data : "price"
                        },
                        {
                            title : "评论内容",
                            data : "content"
                        },
                        {
                            title : "评论标签",
                            data : "feedback"
                        }
                        ,
                        {
                            title : "推荐菜品",
                            data : "zanArticle"
                        }
                        ,
                        {
                            title : "吐槽菜品",
                            data : "spitArticle"
                        }
                    ]
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
                    $.post("newAppraiseReport/shop_data", this.getDate(), function(result) {
                        if (result.success) {
                            toastr.clear();
                            toastr.success("查询成功");
                            that.appraiseShopDtos = result.data.appraiseShopDtos;
                            that.shopAppraiseTable.clear();
                            that.shopAppraiseTable.rows.add(result.data.appraiseShopDtos).draw();
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
                        that.object.appraiseShopDtos = that.appraiseShopList;
                        $.post("newAppraiseReport/create_shop_excel",that.object,function (result) {
                            if (result.success){
                                window.location.href = "newAppraiseReport/downloadShopExcel?path="+result.data+"";
                            }else{
                                toastr.clear();
                                toastr.error("下载报表出错");
                            }
                        })
                    }else{
                        that.state = 2;
                        that.length = Math.ceil(that.appraiseShopList.length/200);
                        that.object.appraiseShopDtos = that.appraiseShopList.slice(that.start,that.end);
                        $.post("appraiseReport/create_shop_excel",that.object,function (result) {
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
                        that.object.appraiseShopDtos = that.appraiseShopList.slice(that.start);
                    } else {
                        that.object.appraiseShopDtos = that.appraiseShopList.slice(that.start, that.end);
                    }
                    that.object.startPosition = that.startPosition;
                    $.post("appraiseReport/appendShopExcel", that.object, function (result) {
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
            download : function(){
                window.location.href = "appraiseReport/downloadShopExcel?path="+this.object.path+"";
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
            }
	    }
	});
</script>

