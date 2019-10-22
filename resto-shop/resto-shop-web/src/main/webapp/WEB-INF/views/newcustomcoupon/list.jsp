<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">	<span class="caption-subject bold font-blue-hoki"> 表单</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" id="newcustomcoupn" class="newcustomform" action="{{m.id?'newcustomcoupon/modify':'newcustomcoupon/create'}}" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label>活动名称</label>
                                <input type="text" class="form-control" name="name" v-model="m.name">
                            </div>
                            <div class="form-group">
                                <label>优惠券价值</label>
                                <div class="input-group">
                                    <input type="number" class="form-control" name="couponValue" v-model="m.couponValue" placeholder="(建议输入整数)" required="required" min="1">
                                    <div class="input-group-addon">	<span class="glyphicon glyphicon-yen" aria-hidden="true"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>优惠券的数量</label>
                                <input type="number" class="form-control" name="couponNumber" v-model="m.couponNumber" placeholder="(建议输入整数)" required="required" min="1">
                            </div>
                            <div class="form-group">
                                <label class="control-label">优惠券有效日期类型</label>
                                <div class="radio-list" style="margin-left: 20px;">
                                    <template v-if="m.couponType==2 || m.timeConsType==1">
                                        <label class="radio-inline">
                                            <input  type="radio" class="md-radiobtn" name="timeConsType" checked="checked" value="1" v-model="m.timeConsType" @click="showNum">按天
                                        </label>
                                    </template>
                                    <template v-else>
                                        <label class="radio-inline">
                                            <input  type="radio" class="md-radiobtn" name="timeConsType" value="1" v-model="m.timeConsType" @click="showNum">按天
                                        </label>
                                    </template>
                                    <label class="radio-inline" v-if="m.couponType!=2">
                                        <input type="radio" class="md-radiobtn" name="timeConsType" value="2" v-model="m.timeConsType" @click="showTime">按时间范围</label>
                                </div>
                            </div>
                            <div class="form-group" v-if="this.m.timeConsType==1 || m.couponType==2">
                                <label>优惠券有效日期</label>
                                <input type="number" class="form-control" name="couponValiday" v-model="m.couponValiday" placeholder="请输入数字" required min="0">
                            </div>
                            <div class="form-group" v-if="this.m.timeConsType==2 && m.couponType!=2">
                                <div class="row">
                                    <label class="control-label col-md-2">开始日期</label>
                                    <div class="col-md-4">
                                        <div class="input-group date form_datetime">
                                            <input type="text" readonly class="form-control" name="beginDateTime" v-model="m.beginDateTime" @focus="initCouponTime"> <span class="input-group-btn">
												<button class="btn default date-set" type="button">
                                                    <i class="fa fa-calendar" @click="initCouponTime"></i>
                                                </button>
											</span>
                                        </div>
                                    </div>
                                    <label class="control-label col-md-2">结束日期</label>
                                    <div class="col-md-4">
                                        <div class="input-group date form_datetime">
                                            <input type="text" readonly class="form-control" @focus="initCouponTime" name="endDateTime" v-model="m.endDateTime"> <span class="input-group-btn">
												<button class="btn default date-set" type="button">
                                                    <i class="fa fa-calendar" @click="initCouponTime"></i>
                                                </button>
											</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>是否可以和余额一起使用</label>
                                <div class="radio-list" style="margin-left: 20px;">
                                    <label class="radio-inline">
                                        <input type="radio" name="useWithAccount" v-model="m.useWithAccount" value=1 v-if="m.id">
                                        <input type="radio" name="useWithAccount" value=1 v-if="!m.id" checked="checked">是</label>
                                    <label class="radio-inline">
                                        <input type="radio" name="useWithAccount" v-model="m.useWithAccount" value=0 v-if="m.id">
                                        <input type="radio" name="useWithAccount" value=0 v-if="!m.id">否</label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>优惠券名字</label>
                                <input type="text" class="form-control" name="couponName" v-model="m.couponName">
                            </div>
                            <div class="form-group">
                                <label>最低消费额度</label>
                                <input type="text" class="form-control" placeholder="请输入数字" min="0" required name="couponMinMoney" v-model="m.couponMinMoney">
                            </div>
                            <div class="form-group">
                                <label>开始时间</label>
                                <div class="input-group">
                                    <input type="text" class="form-control timepicker timepicker-no-seconds" name="beginTime" v-model="m.beginTime" @focus="initTime" readonly> <span class="input-group-btn">
										<button class="btn default" type="button">
                                            <i class="fa fa-clock-o"></i>
                                        </button>
									</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>结束时间</label>
                                <div class="input-group">
                                    <input type="text" class="form-control timepicker timepicker-no-seconds" name="endTime" v-model="m.endTime" @focus="initTime" readonly>	<span class="input-group-btn">
										<button class="btn default" type="button">
                                            <i class="fa fa-clock-o"></i>
                                        </button>
									</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>是否启用优惠券</label>
                                <div class="radio-list" style="margin-left: 20px;">
                                    <label class="radio-inline">
                                        <input type="radio" name="isActivty" v-model="m.isActivty" value=1 v-if="m.id">
                                        <input type="radio" name="isActivty" value=1 v-if="!m.id" checked="checked">是</label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isActivty" v-model="m.isActivty" value=0 v-if="m.id">
                                        <input type="radio" name="isActivty" value=0 v-if="!m.id">否</label>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="control-label">选择店铺模式</div>
                                <div>
                                    <select class="form-control" name="distributionModeId">
                                        <option v-for="distributionMode in allDistributionMode" :value="distributionMode.id">{{distributionMode.name}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>赠送方式：</label>
                                <br/>
                                <!--                                 <label class="radio-inline"> -->
                                <!-- 								  <input type="radio" name="couponType" id="inlineRadio1" value="-1" v-model="m.couponType">通&nbsp;用 -->
                                <!-- 								</label> -->
                                <input type="radio" name="couponType" id="inlineRadio1" value="-1" hidden v-model="m.couponType">
                                <label class="radio-inline">
                                    <input type="radio" name="couponType" id="inlineRadio2" value="0" v-model="m.couponType">新用户
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="couponType" id="inlineRadio5" value="3" v-model="m.couponType">分&nbsp;享
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="couponType" id="inlineRadio3" value="1" v-model="m.couponType">邀&nbsp;请
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="couponType" id="inlineRadio4" value="2" v-model="m.couponType">生&nbsp;日
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="couponType" id="inlineRadio6" value="4" v-model="m.couponType">实&nbsp;时
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="couponType" id="inlineRadio7" value="5" v-model="m.couponType">礼&nbsp;品
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="couponType" id="inlineRadio8" value="6" v-model="m.couponType">消费返利
                                </label>
                            </div>

                            <div class="form-group" v-if="m.couponType == 1">
                                <label >邀请优惠券延迟使用时间：（小时）</label>
                                <div class="input-group">
                                    <input type="number" class="form-control"
                                           name="recommendDelayTime" placeholder="(建议输入整数)"
                                           v-model="m.recommendDelayTime" required="required" min="0">
                                </div>
                            </div>

                            <div class="form-group" v-if="m.couponType == 2">
                                <label >生日优惠券发放时间：（日）</label>
                                <div class="input-group">
                                    距用户生日前
                                    <input type="number"
                                           name="distanceBirthdayDay" placeholder="(建议输入整数)"
                                           v-model="m.distanceBirthdayDay" required="required" min="0"> 日
                                </div>
                            </div>

                            <div class="form-group" v-if="m.couponType == 6">
                                <label>领取优惠券订单最低金额：
                                    <input type="number"
                                          name="minimumAmount" placeholder="(建议输入整数)"
                                          v-model="m.minimumAmount" required="required" min="0"> 元
                                </label>
                            </div>

                            <div class="form-group" v-if="m.couponType == 6">
                                <label>每个用户
                                    <input type="number"
                                           name="nextHour" placeholder="(建议输入整数)"
                                           v-model="m.nextHour" required="required" min="0"> 小时可领取一次
                                </label>
                            </div>

                            <div class="form-group" v-if="m.couponType == 4">
                                <div class="row">
                                    <label class="control-label col-md-2">领取时段：</label>
                                    <div class="col-md-4">
                                        <div class="input-group date form_datetime">
                                            <input type="text" readonly class="form-control" name="realTimeCouponBeginTime" v-model="m.realTimeCouponBeginTimeString" @focus="initCouponTime">
                                            <span class="input-group-btn">
												<button class="btn default date-set" type="button">
                                                    <i class="fa fa-calendar" @click="initCouponTime"></i>
                                                </button>
											</span>
                                        </div>
                                    </div>
                                    <label class="control-label col-md-1">至</label>
                                    <div class="col-md-4">
                                        <div class="input-group date form_datetime">
                                            <input type="text" readonly class="form-control" name="realTimeCouponEndTime" v-model="m.realTimeCouponEndTimeString" @focus="initCouponTime">
                                            <span class="input-group-btn">
												<button class="btn default date-set" type="button">
                                                    <i class="fa fa-calendar" @click="initCouponTime"></i>
                                                </button>
											</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>优惠券所属：</label>
                                <br/>
                                <label class="radio-inline">
                                    <input type="radio" name="isBrand"  value="1"  v-model="m.isBrand">品牌优惠券
                                </label>

                                <label class="radio-inline" v-if = "m.couponType != 2">
                                    <input type="radio" name="isBrand"  value="0" v-model="m.isBrand">店铺优惠券
                                </label>

                            </div>


                        </div>
                        <input type="hidden" name="id" v-model="m.id" />
                        <input class="btn green" type="submit" value="保存" /> <a class="btn default" @click="cancel">取消</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="table-div">
        <div class="table-operator">
            <s:hasPermission name="newcustomcoupon/add">
                <button class="btn green pull-right" @click="create">新建</button>
            </s:hasPermission>
        </div>
        <div class="clearfix"></div>
        <div class="table-filter"></div>
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>
</div>
<script>
    $(document).ready(function(){
        var C;
        var vueObj;
        var cid="#control";
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            "sServerMethod":"POST",
            ajax : {
                url : "newcustomcoupon/list_all",
                dataSrc : "",
            },
            columns : [
                {
                    title : "优惠券所属",//
                    data : "shopName",
                    createdCell:function (td,tdData) {
                        if(tdData!=null){
                            $(td).html(tdData+"用")
                        }else{
                            $(td).html("品牌用")
                        }
                    }
                }  ,
                {
                    title : "赠送方式",
                    data : "couponType",
                    createdCell:function(td,tdData){
                        if(tdData == -1){
                            $(td).html("<span class='label label-default'>通&nbsp;用</span>");
                        }else if(tdData == 0){
                            $(td).html("<span class='label label-primary'>新用户</span>");
                        }else if(tdData == 1){
                            $(td).html("<span class='label label-info'>邀&nbsp;请</span>");
                        }else if(tdData == 2){
                            $(td).html("<span class='label label-success'>生&nbsp;日</span>");
                        }else if(tdData == 3){
                            $(td).html("<span class='label label-warning'>分&nbsp;享</span>");
                        }else if(tdData == 4){
                            $(td).html("<span class='label label-danger'>实&nbsp;时</span>");
                        }else if (tdData == 5){
                            $(td).html("<span class='label label-default'>礼&nbsp;品</span>");
                        }else if (tdData == 6){
                            $(td).html("<span class='label' style='background-color: #666633;color: #FFFFFF'>消费返利</span>");
                        }
                    }
                },
                {
                    title : "活动名称",
                    data : "name",
                },
                {
                    title : "优惠券价值",
                    data : "couponValue",
                },
                {
                    title : "优惠券的类型",
                    data : "timeConsType",
                    createdCell: function(td,tdData){
                        switch(tdData)
                        {
                            case 1:
                                $(td).html("按天计算");
                                break;
                            case 2:
                                $(td).html("按日期计算");
                                break;
                            default:
                                $(td).html("未知");
                        }
                    }
                },
                {
                    title : "优惠券的有效时间",
                    data : "timeConsType",
                    createdCell : function(td,tdData,row,rowData){
                        if(tdData==1){
                            $(td).html(row.couponValiday+"天");
                        }else if(tdData==2){
                            $(td).html(new Date(row.beginDateTime).format("yyyy-MM-dd")+"到"+new Date(row.endDateTime).format("yyyy-MM-dd"));

                        }
                    }

                },

                {
                    title : "优惠券数量",
                    data : "couponNumber",
                },
                {
                    title : "优惠券名称",
                    data : "couponName",
                },
                {
                    title : "优惠券最低消费额度",//默认0.00
                    data : "couponMinMoney",
                },
                {
                    title : "是否启用",
                    data : "isActivty",
                    "render":function(data){
                        if(data==1){
                            data="<span class='label label-info'>启&nbsp;用</span>";
                        }else if(data==0){
                            data="<span class='label label-danger'>未启用</span>";
                        }else{
                            data="<span class='label label-warning'>未&nbsp;知</span>";
                        }
                        return data;
                    }
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        var operator=[
                            <%--<s:hasPermission name="newcustomcoupon/delete">
                            C.createDelBtn(tdData,"newcustomcoupon/delete"),
                            </s:hasPermission>--%>
                            <s:hasPermission name="newcustomcoupon/modify">
                            C.createEditBtn(rowData),
                            </s:hasPermission>
                            createGrantBtn(rowData)
                        ];
                        $(td).html(operator);
                    }
                }],
        });
        
        function createGrantBtn(rowData) {
            var button = (rowData.isActivty == 1 && rowData.couponType == 5) ? $("<a href='newcustomcoupon/goToGrant?couponId="+rowData.id+"&intoType=2' class='btn btn-xs btn-success ajaxify '>发放</a>") : "";
            return button;
        }

        C = new Controller(cid,tb);
        var option = {
            el:cid,
            data:{
                m:{},
                showform:false,
            },
            methods:{
                openForm:function(){
                    this.showform = true;
                },
                closeForm:function(){
                    this.m={};
                    this.m.couponType = -1;
                    this.showform = false;
                },
                cancel:function(){
                    this.m={};
                    this.m.couponType = -1;
                    this.closeForm();
                },
                create:function(){
                    this.m={
                        timeConsType:1,
                        useWithAccount:1,
                        couponType:-1,
                    };
// 					this.showDateNum=false;
// 					this.showDateTime=false;
                    this.openForm();
                    Vue.nextTick(function () {
                        vueObj.initdistributionMode();
                    })
                },
                edit:function(model){
                    this.m= model;
                    //格式时间
                    var tem1 = this.m.beginTime;
                    var tem2 = this.m.endTime;
                    var tem3 = this.m.beginDateTime;
                    var tem4 = this.m.endDateTime;

                    var begin;
                    var end;
                    var beginDate;
                    var endDate;
                    begin=new Date(tem1).format("hh:mm");
                    end = new Date(tem2).format("hh:mm");
                    beginDate = new Date(tem3).format("yyyy-MM-dd hh:mm:ss");
                    endDate = new Date(tem4).format("yyyy-MM-dd hh:mm:ss");
                    if(begin=='aN:aN'){
                        begin = tem1;
                    }
                    if(end=='aN:aN'){
                        end=tem2;
                    }
                    if(beginDate=='NaN-aN-aN aN:aN:aN'){
                        beginDate = tem3;
                    }
                    if(endDate=='NaN-aN-aN aN:aN:aN'){
                        endDate = tem4;
                    }
                    this.m.beginTime = begin;
                    this.m.endTime = end;
                    this.m.beginDateTime=beginDate;
                    this.m.endDateTime=endDate;

                    if(this.m.timeConsType==1){
                        this.showDateNum=true;
                    }else if(this.m.timeConsType==2){
                        this.showDateTime=true;
                    }

                    this.openForm();
                    Vue.nextTick(function(){
                        vueObj.initdistributionMode();

                    })
                },
                save:function(e){
                    var that = this;
                    var url = that.m.id?'newcustomcoupon/modify':'newcustomcoupon/create';
                    var formDom = e.target;
//                    C.ajaxFormEx(formDom,function(){
//                        that.cancel();
//                        tb.ajax.reload();
//                    });
                    if(parseInt(that.m.couponMinMoney) < parseInt(that.m.couponValue)){
                        toastr.clear();
                        toastr.error("优惠券最低消费额度不得小于优惠券价值！");
                        return;
                    }
                    $.ajax({
                        url : url,
                        data : $(formDom).serialize(),
                        success : function(result) {
                            if (result.success) {
                                that.cancel();
                                tb.ajax.reload();
                                toastr.clear();
                                toastr.success("保存成功！");
                            } else {
                                that.cancel();
                                tb.ajax.reload();
                                toastr.clear();
                                toastr.error("保存失败");
                            }
                        },
                        error : function() {
                            that.cancel();
                            tb.ajax.reload();
                            toastr.clear();
                            toastr.error("保存失败");
                        }
                    })
                },
                initdistributionMode :function(){
                    $.ajax({
                        url:"newcustomcoupon/distributionmode/list_all",
                        type:"post",
                        dataType:"json",
                        success:function(result){
                            if(result){
                                var allDistributionMode=[];
                                for(var i=0;i<result.length;i++){
                                    allDistributionMode[i]={"id":result[i].id,"name":result[i].name};
                                }
                                vueObj.$set("allDistributionMode",allDistributionMode)
                            }
                        }
                    })

                },

                initTime :function(){
                    $(".timepicker-no-seconds").timepicker({
                        autoclose: true,
                        showMeridian:false,
                        showInputs:false,
                        minuteStep: 5
                    });
                },
                showNum: function(){
                    //点击优惠券时间类型按天算 清空天数
                    vueObj.m.couponValiday='';
                },
                showTime : function(){

                    //如果是新建则默认选择当天，如果是编辑则选择清空
                    //点击优惠券的时间类型为时间范围  清空开始时间和结束时间
                    if(vueObj.m.brandId){
                        vueObj.m.beginDateTime='';
                        vueObj.m.endDateTime='';
                    }
                },
                initCouponTime: function(){

                    //初始化当前为当前时间
// 					$("").val(new Date().format("yyyy-MM-dd hh:mm:ss"))
// 					$("input[name='newsletter']") 

                    $('.form_datetime').datetimepicker({
                        format: "yyyy-mm-dd hh:ii:ss",
                        autoclose: true,
                        todayBtn: true,
                        todayHighlight: true,
                        showMeridian: true,
                        pickerPosition: "bottom-left",
                        language: 'zh-CN',//中文，需要引用zh-CN.js包
                        startView: 2,//月视图
                        //minView: 2//日期时间选择器所能够提供的最精确的时间选择视图
                    });
                }

            },
        };

        vueObj = C.vueObj(option);
    });

</script>
