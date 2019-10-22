<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <!--查看详情-->
    <div class="row form-div" v-show="details" @click="detailsCli">
        <div class="col-md-offset-3 col-md-6" style="background: #FFF;">
            <div class="text-center" style="padding: 20px 0">
                <span class="caption-subject bold font-blue-hoki">查看详情</span>
                <button type="button" class="close" @click="closeThis"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="form-group row">
                        <label class="col-md-2 control-label">入库单号</label>
                        <div class="col-md-4">
                            {{detailsArr.orderCode}}
                        </div>
                        <label class="col-md-2 control-label">入库时间</label>
                        <div class="col-md-4">
                            {{detailsArr.gmtCreate | moment}}
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-md-2 control-label">类型</label>
                        <div class="col-md-4">
                            {{detailsArr.materialTypes}}
                        </div>
                        <label class="col-md-2 control-label">供应商</label>
                        <div class="col-md-4">
                            {{detailsArr.supName}}
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-md-2 control-label">联系人</label>
                        <div class="col-md-4">
                            {{detailsArr.topContact}}
                        </div>
                        <label class="col-md-2 control-label">备注</label>
                        <div class="col-md-4">
                            {{detailsArr.note}}
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-md-2 control-label">入库人</label>
                        <div class="col-md-4">
                            {{detailsArr.publishedName}}
                        </div>
                        <label class="col-md-2 control-label">审核人</label>
                        <div class="col-md-4">
                            {{detailsArr.auditName}}
                        </div>
                    </div>
                    <div class="form-group row" style="max-height: 400px;overflow-y: scroll;">
                        <table class="table table-bordered" >
                            <thead>
                            <tr>
                                <th>类型</th>
                                <th>一级类别</th>
                                <th>二级类别</th>
                                <th>品牌名</th>
                                <th>材料名</th>
                                <th>编码</th>
                                <th>规格</th>
                                <th>产地</th>
                                <th>数量</th>
                                <th>报价</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="item in detailsArr.docStkInPlanDetailDoList">
                                <td>{{item.materialType}}</td>
                                <td>{{item.categoryOneName}}</td>
                                <td>{{item.categoryTwoName}}</td>
                                <td>{{item.categoryThirdName}}</td>
                                <td>{{item.materialName}}</td>
                                <td>{{item.materialCode}}</td>
                                <td>{{item.measureUnit+item.unitName+"/"+item.specName}}</td>
                                <td>{{item.provinceName+item.cityName+item.districtName}}</td>
                                <td>{{item.actQty}}</td>
                                <td>{{item.purchaseMoney}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <%--<div class="text-center" style="padding: 20px 0">
                <a class="btn default" @click="detailsCli" v-if="detailsBtn">取消</a>
            </div>--%>
            <div class="text-center" style="padding: 20px 0" v-if="approveBtn">
                <a class="btn default" @click="approveCli1" >驳回</a>
                <a class="btn blue pull-center" @click="approveCli2" >批准</a>
            </div>
        </div>
    </div>
    <!--查看详情-->
    <div class="table-div">
        <div class="table-operator">

        </div>
        <div class="clearfix"></div>
        <div class="table-filter"></div>
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>
</div>
<script>
    (function(){
        var cid="#control";
        var $table = $(".table-body>table");

        var tb = $table.DataTable({
            ajax : {
                url : "scmStockPlan/list_all",
                dataSrc : "data"
            },
            columns : [
                {
                    title : "入库单号",
                    data : "orderCode",
                },
                {
                    title : "入库单名",
                    data : "orderName",
                },
                {
                    title: "入库日期",
                    data: "gmtCreate",
                    createdCell:function (td,tdData) {//td中的数据
                        $(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm:ss"));
                    }
                },
                {
                    title : "入库人",
                    data : "publishedName",
                },
                {
                    title : "审核人",
                    data : "auditName",
                },
                {
                    title : "供应商名称 ",
                    data : "supName",
                },
                {
                    title : "联系人",
                    data : "topContact",
                },
                {
                    title : "联系电话",
                    data : "topMobile",
                },
                {
                    title : "备注",
                    data : "note",
                },
                {
                    title : "状态",
                    data : "orderStatus",
                    createdCell:function(td,tdData,rowData){
                        switch(tdData){
                            case '11':tdData='待审核';break;
                            case '12':tdData='审核通过';break;
                            case '13':tdData='已驳回';break;
                            case '14':tdData='审核失败';break;
                            case '15':tdData='已失效';break;
                        }
                        $(td).html(tdData);
                    }
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData){
                        var operator=[
                            <s:hasPermission name="scmStockPlan/approve">

                            C.createApproveBtn(rowData),
                            </s:hasPermission>
                            <s:hasPermission name="scmStockPlan/showDetails">
                            C.findBtn(rowData),
                            </s:hasPermission>
                        ];

                        if(rowData.orderStatus==12||rowData.orderStatus==13||rowData.orderStatus==15){
                            operator=[
                                <s:hasPermission name="scmStockPlan/showDetails">
                                C.findBtn(rowData),
                                </s:hasPermission>
                            ];
                        }
                        $(td).html(operator);
                    }
                },
            ],
        });
        var C = new Controller(null,tb);
        var vueObj = new Vue({
            mixins:[C.formVueMix],
            el:"#control",
            data:{
                details:false,//查看详情
                detailsBtn:false,//查看详情返回按钮
                approveBtn:false,//查看详情（审核）-审核按钮
                detailsArr:{},//查看详情对象
            },
            methods:{
                approve:function (data) { //开始审核
                    this.details=true;
                    this.detailsArr=data;
                    this.approveBtn=true;
                },
                approveCli1:function () { //驳回审核
                    this.details=false;
                    this.approveBtn=false;
                    C.systemButton('scmStockPlan/approve',{id:this.detailsArr.id,orderStatus:'13'},['驳回成功','驳回失败']);
                },
                approveCli2:function () { //批准审核
                    this.details=false;
                    this.approveBtn=false;
                    C.systemButton('scmStockPlan/approve',{id:this.detailsArr.id,orderStatus:'12'},['审核成功','审核失败']);
                },
                showDetails:function (data) { //查看详情
                    this.details=true;
                    this.detailsArr=data;
//                    for(var i=0;i<this.detailsArr.docStkInPlanDetailDoList.length;i++){
//                        switch(this.detailsArr.docStkInPlanDetailDoList[i].materialType){
//                            case 'INGREDIENTS':this.detailsArr.docStkInPlanDetailDoList[i].materialType='主料';break;
//                            case 'ACCESSORIES':this.detailsArr.docStkInPlanDetailDoList[i].materialType='辅料';break;
//                            case 'SEASONING':this.detailsArr.docStkInPlanDetailDoList[i].materialType='配料';break;
//                            case 'MATERIEL':this.detailsArr.docStkInPlanDetailDoList[i].materialType='物料';break;
//                        }
//                    }

                    this.detailsBtn=true;
                },
                detailsCli:function () { //关闭查看详情
                    this.details=false;
                    this.detailsBtn=false;
                },
                closeThis:function () {//关闭
                    this.details=false;
                },
            },
        });
        Vue.filter('moment', function (value, formatString) {
            formatString = formatString || 'YYYY-MM-DD HH:mm:ss';
            return moment(value).format(formatString);
        });
        C.vue=vueObj;
    }());


</script>
