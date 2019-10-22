<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <!--查看详情-->
    <div class="row form-div" v-show="details" @click="close">
        <div class="col-md-offset-3 col-md-6" style="background: #FFF;">
            <div class="text-center" style="padding: 20px 0">
                <span class="caption-subject bold font-blue-hoki">查看详情</span>
                <button type="button" class="close" @click="closeThis"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="form-group row">
                        <label class="col-md-2 control-label">盘点单号</label>
                        <div class="col-md-4">
                            {{detailsArr.orderCode}}
                        </div>
                        <label class="col-md-2 control-label">盘点时间</label>
                        <div class="col-md-4">
                            {{detailsArr.publishedTime | moment}}
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-md-2 control-label">类型</label>
                        <div class="col-md-4">
                            {{detailsArr.materialTypes}}
                        </div>
                        <label class="col-md-2 control-label">物料种类</label>
                        <div class="col-md-4">
                            {{detailsArr.size}}种
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-md-2 control-label">盘点人</label>
                        <div class="col-md-4">
                            {{detailsArr.createrName}}
                        </div>
                        <label class="col-md-2 control-label">备注</label>
                        <div class="col-md-4">
                            {{detailsArr.orderStatus}}
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
                                <th>理论库存</th>
                                <th>盘点库存</th>
                                <th>差异数量</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="item in detailsArr.stockCountDetailList">
                                <td>{{item.materialType}}</td>
                                <td>{{item.categoryOneName}}</td>
                                <td>{{item.categoryTwoName}}</td>
                                <td>{{item.categoryThirdName}}</td>
                                <td>{{item.materialName}}</td>
                                <td>{{item.materialCode}}</td>
                                <td>{{item.measureUnit+item.unitName+"/"+item.specName}}</td>
                                <td>{{item.provinceName+item.cityName+item.districtName}}</td>
                                <td>{{item.theoryStockCount}}</td>
                                <td>{{item.actStockCount}}</td>
                                <td>{{item.actStockCount-item.theoryStockCount}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <%--<div class="text-center" style="padding: 20px 0">
                <a class="btn default" @click="detailsCli">取消</a>
            </div>--%>
            <div class="text-center" style="padding: 20px 0" v-if="approveBtn">
                <a class="btn default" @click="approveCli1" >驳回</a>
                <a class="btn blue pull-center" @click="approveCli2" >批准</a>
            </div>
        </div>
    </div>
    <!--查看详情-->
    <div class="table-div">
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>
</div>
<script>
    (function(){
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            ajax : {
                url : "scmStockCount/list_all",
                dataSrc : "data"
            },
            columns : [
                {
                    title: "盘点单号",
                    data: "id",
                },
                {
                    title:"盘点单名",
                    data:"orderName"
                },
                {
                    title:"盘点日期",
                    data:"publishedTime",
                    createdCell:function (td,tdData) {//td中的数据
                        $(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm:ss"));
                    }
                },
                {
                    title:"盘点人",
                    data:"createrName"
                },
                {
                    title:"状态",
                    data:"orderStatusShow"
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData){
                        var operator=[
                            <s:hasPermission name="scmStockCount/approve">
                            C.createApproveBtn(rowData),
                            </s:hasPermission>
                            <s:hasPermission name="scmStockCount/showDetails">
                            C.findBtn(rowData),
                            </s:hasPermission>
                        ];

                        if(rowData.orderStatus==12||rowData.orderStatus==13||rowData.orderStatus==15){
                            operator=[
                                <s:hasPermission name="scmStockCount/showDetails">
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
                close:function () {
                  this.details = false;
                },
                closeThis:function () {//关闭
                    this.details=false;
                },
                showDetails:function (data) { //查看详情
                    this.details=true;
                    this.detailsArr=data;

                    for(var i=0;i<this.detailsArr.stockCountDetailList.length;i++){
                        switch(this.detailsArr.stockCountDetailList[i].materialType){
                            case 'INGREDIENTS':this.detailsArr.stockCountDetailList[i].materialType='主料';break;
                            case 'ACCESSORIES':this.detailsArr.stockCountDetailList[i].materialType='辅料';break;
                            case 'SEASONING':this.detailsArr.stockCountDetailList[i].materialType='配料';break;
                            case 'MATERIEL':this.detailsArr.stockCountDetailList[i].materialType='物料';break;
                        }
                    }

                },
                detailsCli:function () { //关闭查看详情
                    this.details=false;
                },

                approve:function (data) { //开始审核
                    this.details=true;
                    this.detailsArr=data;
                    this.approveBtn=true;
                },
                approveCli1:function () { //驳回审核
                    this.details=false;
                    this.approveBtn=false;
                    C.systemButton('scmStockCount/approve',{id:this.detailsArr.id,orderStatus:'13'},['驳回成功','驳回失败']);
                },
                approveCli2:function () { //批准审核
                    debugger
                    this.details=false;
                    this.approveBtn=false;
                    C.systemButton('scmStockCount/approve',{id:this.detailsArr.id,orderStatus:'12'},['审核成功','审核失败']);
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
