<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <div class="row form-div" v-show="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">新增报价单</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" class="form-horizontal" action="{{parameter.id?'scmMaterial/modify':'scmMaterial/create'}}" @submit.prevent="save">
                        <input type="hidden" name="id" v-model="parameter.id" />
                        <div class="form-body">
                            <div class="form-group row">
                                <label class="col-md-2 control-label">报价单名称<span style="color:#FF0000;">*</span></label>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="priceName" v-model="parameter.priceName">
                                </div>
                                <label class="col-md-2 control-label">供应商类型<span style="color:#FF0000;">*</span></label>
                                <div class="col-md-3">
                                    <select class="bs-select form-control" name="supplierType" v-model="supplierType">
                                        <option disabled="" selected="" value="">请选择</option>
                                        <option  v-for="supplierType in supplierTypes" value="{{supplierType.name}}">
                                            {{supplierType.name}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row" >
                                <label class="col-md-2 control-label">供应商<span style="color:#FF0000;">*</span></label>
                                <div class="col-md-3">
                                    <select name="supplierId" v-model="parameter.supplierId" class="bs-select form-control">
                                        <option disabled="" selected="" value="">请选择</option>
                                        <option  v-for="supName in supNames" value="{{supName.id}}">
                                            {{supName.supName}}
                                        </option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label">联系人<span style="color:#FF0000;">*</span></label>
                                <div class="col-md-3">
                                    <select name="contactId" v-model="parameter.contactId" class="bs-select form-control" >
                                        <option disabled="" selected="" value="">请选择</option>
                                        <option  v-for="contact in contacts" value="{{contact.id}}" v-if="parameter.supplierId==contact.supplierId">
                                            {{contact.contact}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label for="inputEmail3" class="col-sm-2 control-label">开始时间<span style="color:#FF0000;">*</span></label>
                                <div class="col-sm-3">
                                    <input type="text" class="form-control form_datetime" id="beginDate" v-model="parameter.startEffect" name="startEffect" readonly="readonly">
                                </div>
                                <label for="inputPassword3" class="col-sm-2 control-label">结束时间<span style="color:#FF0000;">*</span></label>
                                <div class="col-sm-3">
                                    <input type="text" class="form-control form_datetime" id="endDate" v-model="parameter.endEffect" name="endEffect" readonly="readonly">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="inputEmail3" class="col-sm-2 control-label">备注</label>
                                <div class="col-sm-3">
                                    <input type="text" class="form-control" name="remark" v-model="parameter.remark">
                                </div>

                                <label for="inputEmail3" class="col-sm-2 control-label">税率</label>
                                <div class="col-md-3">
                                    <select class="bs-select form-control" name="tax" v-model="parameter.tax">
                                        <option disabled="" selected="" value="">请选择</option>
                                        <option  v-for="tax in taxs" value="{{tax.code}}">
                                        {{tax.name}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-md-2 control-label">原材料清单</label>
                                <div class="col-md-3">
                                    <input class="btn btn-default" type="button" value="添加原料" @click="closeTreeView"/>
                                </div>
                            </div>
                        </div>
                        <div style="max-height:200px;overflow: auto;">
                            <table class="table table-bordered">
                                <thead><tr>
                                    <th>类型</th><th>一级类别</th><th>二级类别</th>
                                    <th>品牌名</th><th>材料名</th>
                                    <%--<th>编码</th>--%>
                                    <th>规格</th><th>产地</th><th>单价<span style="color:#FF0000;">*</span></th><th>操作</th>
                                </tr></thead>
                                <tbody>

                                <tr v-for="(index,item) in parameter.mdSupplierPriceDetailDoList">
                                    <td>{{item.materialType}}</td>
                                    <td>{{item.categoryOneName}}</td>
                                    <td>{{item.categoryTwoName}}</td>
                                    <td>{{item.categoryThirdName}}</td>
                                    <td>{{item.name}}{{item.materialName}}</td>
                                    <%--<td>{{item.materialCode}}</td>--%>
                                    <td>{{item.measureUnit+item.unitName+"/"+item.specName}}</td>
                                    <td>{{item.provinceName+item.cityName+item.districtName}}</td>
                                    <td><input style="width: 50px;" type="number"  step="0.01" v-model="item.purchasePrice" value="1"></td>
                                    <td><button class="btn btn-xs red" @click="removeArticleItem(item)">移除</button></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="form-group text-center">
                            <input class="btn green"  type="submit"  value="保存"/>&nbsp;&nbsp;&nbsp;
                            <a class="btn default" @click="cancel" >取消</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!--树状图-->
    <div class="row form-div" v-show="treeView">
        <div class="col-md-offset-3 col-md-6" style="background: #FFF;">
            <div class="text-center" style="padding: 20px 0">
                <span class="caption-subject bold font-blue-hoki">添加原材料</span>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="modal-body">
                        <input type="input" class="form-control" id="search_ay" placeholder="请输入原材料名称">
                    </div>
                    <div id="assignTree" style="height: 300px;overflow: auto;"></div>
                    <div id="treeview-checkable" class="" style="height: 500px;overflow: auto;display: none"></div>
                </div>
            </div>
            <div class="text-center" style="padding: 20px 0">
                <input class="btn green"  @click="bomRawMaterialSub"  value="保存"/>&nbsp;&nbsp;&nbsp;
                <a class="btn default" @click="cancelTreeView" >取消</a>
            </div>
        </div>
    </div>
    <!--树状图结束-->
    <!--查看详情-->
    <div class="row form-div" v-show="details">
        <div class="col-md-offset-3 col-md-6" style="background: #FFF;">
            <div class="text-center" style="padding: 20px 0">
                <span class="caption-subject bold font-blue-hoki">查看详情</span>
                <button type="button" class="close" @click="closeThis"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="form-group row">
                        <label class="col-md-2 control-label">报价单号</label>
                        <div class="col-md-4">
                            {{detailsArr.priceNo}}
                        </div>
                        <label class="col-md-2 control-label">有效期</label>
                        <div class="col-md-4">
                            {{detailsArr.startEffect}}至{{detailsArr.endEffect}}
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
                        <div class="col-md-10">
                            {{detailsArr.contact}}({{detailsArr.position}}/{{detailsArr.mobile}}/{{detailsArr.email}})
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-md-2 control-label">备注</label>
                        <div class="col-md-4">
                            {{detailsArr.remark}}
                        </div>
                        <label class="col-md-2 control-label">税率</label>
                        <div class="col-md-4">
                            {{detailsArr.tax}}
                        </div>
                    </div>
                    <div class="form-group row" style="max-height:300px;overflow: auto;">
                        <table class="table table-bordered">
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
                                <th>单价</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="item in detailsArr.mdSupplierPriceDetailDoList">
                                <td>{{item.materialType}}</td>
                                <td>{{item.categoryOneName}}</td>
                                <td>{{item.categoryTwoName}}</td>
                                <td>{{item.categoryThirdName}}</td>
                                <td>{{item.materialName}}</td>
                                <td>{{item.materialCode}}</td>
                                <td>{{item.measureUnit+item.unitName+"/"+item.specName}}</td>
                                <td>{{item.provinceName+item.cityName+item.districtName}}</td>
                                <td>{{item.purchasePrice}}</td>
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
            <s:hasPermission name="scmSupplerPrice/add">
                <button class="btn green pull-right" @click="create">新建报价单</button>
            </s:hasPermission>
        </div>
        <div class="clearfix"></div>
        <div class="table-filter"></div>
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>
</div>

<!-- <!-- 日期框 -->
<script src="assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script src="assets/global/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<!--树状图-->
<%--<script src="assets/treeview/js/bootstrap-treeview.js"></script>--%>
<link href="assets/treeview/themes/default/style.min.css" rel="stylesheet">
<script src="assets/treeview/js/jstrestwo.js"></script>
<script>
    (function(){
        var $table = $(".table-body>table");

        var tb = $table.DataTable({
            ajax : {
                url : "scmSupplerPrice/list_all",
                dataSrc : "data"
            },
            ordering: false,//取消上下排序
            columns : [
                {
                    title : "报价单号",
                    data : "priceNo",
                },
                {
                    title : "报价单名称",
                    data : "priceName",
                },
                {
                    title: "开始日期",
                    data: "startEffect",
                },
                {
                    title : "结束日期",
                    data : "endEffect",
                },
                {
                    title : "物料类型",
                    data : "materialTypes",
                },
                {
                    title : "物料类别 ",
                    data : "materialSizes",
                },
                {
                    title : "供应商名称",
                    data : "supName",
                },
                {
                    title : "联系人",
                    data : "contact",
                },
                {
                    title : "联系邮箱",
                    data : "email",
                },
                {
                    title : "备注",
                    data : "remark",
                },
                {
                    title : "状态",
                    data : "supStatus",
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
                            <s:hasPermission name="scmSupplerPrice/approve">
                            C.createApproveBtn(rowData),
                            </s:hasPermission>
                            <s:hasPermission name="scmSupplerPrice/showDetails">
                            C.findBtn(rowData),
                            </s:hasPermission>
                        ];
                        if(rowData.supStatus==12||rowData.supStatus==13||rowData.supStatus==15){
                            operator=[
                                <s:hasPermission name="scmSupplerPrice/showDetails">
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
                showform:false,//弹窗
                treeView:false,//树状图弹窗是否隐藏
                bomRawMaterial:[],//树状图原材料
                detailsArr:'',//查看详情对象
                supplierTypes: [ //供应商类型数组
                    {code:"1",name:"物料类"},
                    {code:"2",name:"服务类"},
                    {code:"3" , name:"工程类"}
                ],
                taxs: [ //供应商类型数组
                    {code:"0",name:"0%"},
                    {code:"0.03",name:"3%"},
                    {code:"0.06" , name:"6%"},
                    {code:"0.07" , name:"7%"},
                    {code:"0.11" , name:"11%"}
                ],
                supplierType:'物料类',//供应商类型
                supNames:[],//供应商数组
                contacts:[],//联系人数组

                parameter:{
                    supplierId:'',//供应商id
                    startEffect:'',//生效日期
                    endEffect:'',//失效效日期
                    contactId:'',//联系人id
                    remark:'',//备注
                    priceName:'',//报价单名称
                    tax:'',
                    mdSupplierPriceDetailDoList:[],

                }
            },
            methods:{
                closeThis:function () {//关闭审核
                    this.details=false;
                },
                closeForm:function(){ //关闭新增弹窗
                    this.showform=false;
                },
                create:function(){ //打开新增弹窗
                    this.showform=true;
                    this.parameter={
                        supplierId:'',//供应商id
                        startEffect:'',//生效日期
                        endEffect:'',//失效效日期
                        contactId:'',//联系人id
                        remark:'',//备注
                        priceName:'',//报价单名称
                        tax:'',
                        mdSupplierPriceDetailDoList:[],

                    };
                },
                closeTreeView:function () { //添加原料打开
                    this.treeView=true;
                },
                approve:function (data) { //开始审核
                    this.details=true;
                    this.detailsArr=data;
                    this.approveBtn=true;
                },
                approveCli1:function () { //驳回审核
                    this.details=false;
                    this.approveBtn=false;
                    C.systemButton('scmSupplerPrice/approve',{id:this.detailsArr.id,supStatus:'13'},['驳回成功','驳回失败']);
                },
                approveCli2:function () { //批准审核
                    this.details=false;
                    this.approveBtn=false;
                    C.systemButton('scmSupplerPrice/approve',{id:this.detailsArr.id,supStatus:'12'},['审核成功','审核失败']);
                },
                showDetails:function (data) { //查看详情
                    this.details=true;
                    this.approveBtn=false;
                    this.detailsArr=data;
                    console.info(data);
                    var tax_new = this.detailsArr.tax.toString();
                    if (tax_new.indexOf("%") === -1){
                        this.detailsArr.tax = Math.round(parseFloat(tax_new)*100) + "%";
                    }
                    for(var i=0;i<this.detailsArr.mdSupplierPriceDetailDoList.length;i++){
                        switch(this.detailsArr.mdSupplierPriceDetailDoList[i].materialType){
                            case 'INGREDIENTS':this.detailsArr.mdSupplierPriceDetailDoList[i].materialType='主料';break;
                            case 'ACCESSORIES':this.detailsArr.mdSupplierPriceDetailDoList[i].materialType='辅料';break;
                            case 'SEASONING':this.detailsArr.mdSupplierPriceDetailDoList[i].materialType='配料';break;
                            case 'MATERIEL':this.detailsArr.mdSupplierPriceDetailDoList[i].materialType='物料';break;
                        }
                    }
                    this.detailsBtn=true;
                },
                detailsCli:function () { //关闭查看详情
                    this.details=false;
                    this.detailsBtn=false;
                },
                cancelTreeView:function () { //添加原料关闭
                    this.treeView=false;
                },
                removeArticleItem: function (mealItem) { //移除
                    this.parameter.mdSupplierPriceDetailDoList.$remove(mealItem);
                },
                bomRawMaterialSub:function () { //添加原材料保存
                    var originaldata=$('#assignTree').jstrestwo().get_bottom_checked(true);//拿到树状图中的数组
                    for(var i=0;i<originaldata.length;i++){
                        if(originaldata[i].original.materialType){
                            this.bomRawMaterial.push(originaldata[i].original);
                        }
                    }
                    for(var i=0;i<this.bomRawMaterial.length;i++){
                        var sta=true;
                        for(var j=0;j<this.parameter.mdSupplierPriceDetailDoList.length;j++){
                            if(this.bomRawMaterial[i].idTwo==this.parameter.mdSupplierPriceDetailDoList[j].idTwo||this.bomRawMaterial[i].idTwo==this.parameter.mdSupplierPriceDetailDoList[j].materialId) sta=false;
                        }
                        if(sta)this.parameter.mdSupplierPriceDetailDoList.push(this.bomRawMaterial[i]);
                    }
                    //this.parameter.mdSupplierPriceDetailDoList.push.apply(this.parameter.mdSupplierPriceDetailDoList,this.bomRawMaterial);//合并数组
                    for(var i=0;i<this.parameter.mdSupplierPriceDetailDoList.length;i++){
                        switch(this.parameter.mdSupplierPriceDetailDoList[i].materialType){
                            case 'INGREDIENTS':this.parameter.mdSupplierPriceDetailDoList[i].materialType='主料';break;
                            case 'ACCESSORIES':this.parameter.mdSupplierPriceDetailDoList[i].materialType='辅料';break;
                            case 'SEASONING':this.parameter.mdSupplierPriceDetailDoList[i].materialType='配料';break;
                            case 'MATERIEL':this.parameter.mdSupplierPriceDetailDoList[i].materialType='物料';break;
                        }
                    }
                    this.treeView=false;
                    $('#assignTree').jstrestwo('deselect_all');//关闭所有选中状态
                },
                save:function(e){
                    var _this=this;
                    var submit=false;
                    var message='';
                    if(!this.parameter.priceName) message='报价单名称';
                    else if(!this.supplierType) message='供应商类型';
                    else if(!this.parameter.supplierId) message='供应商';
                    else if(!this.parameter.contactId) message='联系人';
                    else if(!this.parameter.startEffect) message='开始时间';
                    else if(!this.parameter.endEffect) message='结束时间';
                    else if(_this.parameter.mdSupplierPriceDetailDoList.length<=0) message='原材料清单';
                    else  submit=true;
                    for(var i=0;i<_this.parameter.mdSupplierPriceDetailDoList.length;i++) {
                        if(!_this.parameter.mdSupplierPriceDetailDoList[i].purchasePrice){
                            submit=false;
                            message='单价';
                        }
                    }
                    if(submit){
                        var saveObj=[];
                        var parSup=this.parameter.mdSupplierPriceDetailDoList;
                        for(var i=0;i<parSup.length;i++){
                            saveObj[i]={
                                materialId:parSup[i].idTwo,
                                materialCode:parSup[i].materialCode,
                                purchasePrice:parSup[i].purchasePrice,
                            }
                        }
                        _this.parameter.mdSupplierPriceDetailDoList=saveObj;
                        $.ajax({
                            type:"POST",
                            url:'scmSupplerPrice/create',
                            contentType:"application/json",
                            datatype: "json",
                            data:JSON.stringify(_this.parameter),
                            beforeSend:function(){ //请求之前执行
                                _this.showform=false;
                            },
                            success:function(data){ //成功后返回
                                C.systemButtonNo('success','成功');
                            },
                            error: function(){ //失败后执行
                                C.systemButtonNo('error','失败');
                            }
                        });
                    }else {
                        C.systemButtonNo('error','请填写'+message);
                    }
                },
            },
            ready:function(){//钩子函数加载后
                var _this=this;
                $.get('scmSupplier/list_all?state=1',function (jsonData) { //供应商查询
                    var data=jsonData.data;
                    _this.supNames=data;//供应商
                    for(var i=0;i<data.length;i++){
                        if(data[i].supplierContacts){
                            _this.contacts=_this.contacts.concat(data[i].supplierContacts);
                        }
                    }
                });
                $.get('scmCategory/query',function (jsonData) { //加载树状图
                    var defaultData=jsonData.data;
                    for(var i=0;i<defaultData.length;i++){
                        defaultData[i].idTwo = defaultData[i].id;
                        delete defaultData[i].id;
                        if (defaultData[i].twoList) {
                            for(var j=0;j<defaultData[i].twoList.length;j++){
                                defaultData[i].twoList[j].idTwo = defaultData[i].twoList[j].id;
                                delete defaultData[i].twoList[j].id;
                                if (defaultData[i].twoList) {
                                    defaultData[i].twoList[j].twoList = defaultData[i].twoList[j].threeList;
                                    delete defaultData[i].twoList[j].threeList;
                                    if(defaultData[i].twoList[j].twoList){
                                        for(var k=0;k<defaultData[i].twoList[j].twoList.length;k++){
                                            defaultData[i].twoList[j].twoList[k].idTwo = defaultData[i].twoList[j].twoList[k].id;
                                            delete defaultData[i].twoList[j].twoList[k].id;
                                            defaultData[i].twoList[j].twoList[k].twoList = defaultData[i].twoList[j].twoList[k].materialList;
                                            delete defaultData[i].twoList[j].twoList[k].materialList;
                                            if(defaultData[i].twoList[j].twoList[k].twoList){
                                                for(var l=0;l<defaultData[i].twoList[j].twoList[k].twoList.length;l++){
                                                    defaultData[i].twoList[j].twoList[k].twoList[l].idTwo = defaultData[i].twoList[j].twoList[k].twoList[l].id;
                                                    delete defaultData[i].twoList[j].twoList[k].twoList[l].id;
                                                    defaultData[i].twoList[j].twoList[k].twoList[l].name = defaultData[i].twoList[j].twoList[k].twoList[l].materialName;
                                                    delete defaultData[i].twoList[j].twoList[k].twoList[l].materialName;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    $('#assignTree').jstrestwo(
                        {'plugins':["wholerow","checkbox","search"],
                            'core' :{
                                'data':defaultData
                            }
                        }
                    );
                    //输入框输入定时自动搜索
                    var to = false;
                    $('#search_ay').keyup(function () {
                        if(to) {
                            clearTimeout(to);
                        }
                        to = setTimeout(function () {
                            $('#assignTree').jstrestwo(true).search($('#search_ay').val());

                        }, 250);
                    });

                })
            },
            //vue实例化之后执行的方法
            created : function(){
                $("#beginDate").datetimepicker({
                    format: 'yyyy-mm-dd',
                    minView:'month',
                    language: 'zh-CN',
                    autoclose:true,
                    startDate:new Date()
                }).on("click",function(){
                    $("#beginDate").datetimepicker("setEndDate",$("#endDate").val())
                });
                $("#endDate").datetimepicker({
                    format: 'yyyy-mm-dd',
                    minView:'month',
                    language: 'zh-CN',
                    autoclose:true,
                    startDate:new Date()
                }).on("click",function(){
                    $("#endDate").datetimepicker("setStartDate",$("#beginDate").val())
                });
            },
        });
        C.vue=vueObj;
    }());


</script>