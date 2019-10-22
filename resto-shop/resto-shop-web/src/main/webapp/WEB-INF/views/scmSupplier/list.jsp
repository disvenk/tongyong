<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <div class="row form-div" v-show="showform">
        <div class="col-md-offset-3 col-md-6" style="text-align:center">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">新增供应商</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" class="form-horizontal" action="{{parameter.id?'scmSupplier/modify':'scmSupplier/create'}}" @submit.prevent="save" style="text-align:center">
                        <input type="hidden" name="id" v-model="parameter.id" />
                        <div class="form-body">
                            <div class="form-group row">
                                <label class="col-md-2 control-label">供应商类型<span style="color:#FF0000;">*</span></label>
                                <div class="col-md-3">
                                <select class="bs-select form-control" name="supplierType" v-model="parameter.supplierType">
                                    <option disabled="" selected="" value="">请选择</option>
                                    <option  v-for="supplierType in supplierTypes" value="{{supplierType.code}}">
                                        {{supplierType.name}}
                                    </option>
                                </select>
                                </div>
                                <label class="col-md-2 control-label">编码</label>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="supCode" v-model="parameter.supCode"
                                           readonly="readonly">
                                </div>
                            </div>
                            <div class="form-group row" >
                                <label class="col-md-2 control-label">公司名<span style="color:#FF0000;">*</span></label>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="supName" v-model="parameter.supName">
                                </div>

                                <label class="col-md-2 control-label">别名</label>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="supAliasName" v-model="parameter.supAliasName" >
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-md-2 control-label">序号</label>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="version" v-model="parameter.version">
                                </div>

                                <label class="col-md-2 control-label">纳税人识别号</label>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="taxNumber" v-model="parameter.taxNumber">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="col-md-2 control-label">产品(可多选)</label>
                                <div class="col-md-10 checkbox-list" id="checkboxs">
                                    <p class="col-md-3" v-for="materialType in productTypes">
                                    <label class="checkbox-inline" >
                                        <input type="checkbox" name="checkbox" v-model="parameter.materialTypes" value="{{materialType.id}}">
                                        <span>{{materialType.categoryName}}</span>
                                    </label>
                                    </p>
                                </div>
                            </div>
                                <table class="table table-bordered" id="supplierContacts">
                                    <thead >
                                    <tr>
                                        <th>编号</th>
                                        <th>姓名<span style="color:#FF0000;">*</span></th>
                                        <th>职位</th>
                                        <th>电话<span style="color:#FF0000;">*</span></th>
                                        <th>邮箱</th>
                                        <th>默认<span style="color:#FF0000;">*</span></th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr v-for="(index,item) in parameter.supplierContacts">
                                        <td>{{index+1}}</td>
                                        <td><input style="width: 80px" type="text" v-model="item.contact"></td>
                                        <td><input style="width: 80px" type="text" v-model="item.position"></td>
                                        <td><input style="width: 145px" type="text" v-model="item.mobile"></td>
                                        <td><input style="width: 145px" type="text" v-model="item.email"></td>
                                        <td><input name="isTop" type="radio" v-model="isTop" :value="item.isTop" @click="supplierContactsRadio(item)"></td>
                                        <td><span class="btn btn-xs red" @click="removeArticleItem(item)">移除</span></td>
                                    </tr>
                                    </tbody>
                            </table>
                            <div class="form-group text-center">
                                <span class="btn green" @click="addSupplierContacts">添加联系资料</span>
                            </div>
                            <div class="form-group row">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-sm-4">
                                    <textarea class="form-control" v-model="parameter.note" value="{{parameter.note?parameter.note:'内容'}}" name="note"></textarea>
                                </div>

                                <label class="col-md-2 control-label">是否启用<span style="color:#FF0000;">*</span></label>
                                <div class="col-md-2">
                                    是<input name="state" type="radio"  v-model="parameter.state"  :value="1">
                                    否<input name="state" type="radio"  v-model="parameter.state"  :value="0">
                                </div>
                            </div>
                        </div>
                        <div class="form-group text-center">
                            <input class="btn green"  type="submit"  value="保存"/>&nbsp;&nbsp;&nbsp;
                            <a class="btn default" @click="cancel">取消</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="table-div">
        <div class="table-operator">
            <s:hasPermission name="scmSupplier/add">
                <button class="btn green pull-right" @click="create">新增供应商</button>
            </s:hasPermission>
        </div>
        <div class="clearfix"></div>
        <div class="table-filter"></div>
        <div class="table-body" id="tableBodyList">
            <table class="table table-striped table-hover table-bordered"></table>
        </div>
    </div>
    <div id="tableBodyLists" v-show="tableBodyListsShow">
        <table border="1" cellpadding="2" cellspacing="0" align="center" width="100%">
        </table>
    </div>
</div>
<script>
    (function(){
        var cid="#control";
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            ajax : {
                url : "scmSupplier/list_all",
                dataSrc : "data"
            },
            //ordering: false,//取消上下排序
            columns : [
                {
                    title : "序号",
                    data : "id",
                    orderable:true,
                },
                {
                    title : "编码",
                    data : "supCode",
                    orderable:false,
                },
                {
                    title : "供应商类型",
                    data : "supplierTypeShow",
                    orderable:false,
                }
                ,
                {
                    title : "公司全称",
                    data : "supName",
                    orderable:false,
                },
                {
                    title : "别称",
                    data : "supAliasName",
                    orderable:false,
                },
                {
                    title : "联系人 ",
                    data : "topContact",
                    orderable:false,
                },
                {
                    title : "职务 ",
                    data : "topPosition",
                    orderable:false,
                },
                {
                    title : "电话",
                    data : "topMobile",
                    orderable:false,
                },
                {
                    title : "邮件",
                    data : "topEmail",
                    orderable:false,
                },
                {
                    title : "产品",
                    data : "materialTypes",
                    orderable:false,
                },
                {
                    title : "是否启用",
                    data : "state",
                    createdCell:function (td,tdData) {//td中的数据
                        switch (tdData){
                            case 0:tdData='未启用';break;
                            case 1:tdData='启用';break;
                        }
                        $(td).html(tdData);
                    },
                },
                {
                    title : "备注",
                    data : "note",
                    orderable:false,
                },
                {
                    title : "操作",
                    data : "id",
                    orderable:false,
                    createdCell:function(td,tdData,rowData){
                        var operator=[
                            <s:hasPermission name="scmSupplier/create">
                            C.createEditBtn(rowData),
                            </s:hasPermission>
                            <s:hasPermission name="scmSupplier/delete">
                            C.createDelSupplier(tdData,"scmSupplier/delete",'scmSupplerPrice/findEffectiveSupPriceIds'),
                            </s:hasPermission>
                        ];
                        $(td).html(operator);
                    }
                }]
        });
        var C = new Controller(null,tb);
        var vueObj = new Vue({
            mixins:[C.formVueMix],
            el:"#control",
            data:{
                supplierTypes: [
                    {code:"1",name:"物料类"},
                    {code:"2",name:"服务类"},
                    {code:"3" , name:"工程类"}
                ],
                productTypes:[],//接收所有的产品分类
                isTop:'0',//单选框绑定
                isTopWatch:0,//移除默认联系人使用借助监控
                parameter:{
                    supCode: "",
                    supplierType: "",
                    materialTypes:[],
                    materialIds:[],
                    supAliasName: "",
                    supName: "",
                    note:'',//备注
                    bankName: "",
                    bankAccount: "",
                    version: "",
                    taxNumber:"",
                    topContact: "",
                    topMobile: "",
                    topEmail: "",
                    position:"",
                    state:"",
                    supplierContacts:[],//详情
                    supContactIds:[]
                }
            },
            methods:{
                addSupplierContacts:function () { //添加供应商联系资料
                   if(this.parameter.supplierContacts.length==0){
                       this.parameter.supplierContacts.push({contact:'',position:'',mobile:'',email:'',isTop:'0'});
                   }else {
                       this.parameter.supplierContacts.push({contact:'',position:'',mobile:'',email:'',isTop:'1'});
                   }
                },
                removeArticleItem:function (data) { //移除供应商联系资料
                    this.parameter.supplierContacts.$remove(data);
                    this.parameter.supContactIds.push(data.id);
                    if(data.isTop=='0'&&this.parameter.supplierContacts.length!=0){
                        this.isTopWatch++
                    }
                },
                supplierContactsRadio:function(item){ //供应商联系默认值
                    $('#supplierContacts div').removeClass('radio');
                    this.parameter.supplierContacts.forEach(function(element) {
                        element.isTop='1';
                    });
                    item.isTop='0';
                    this.isTop='0';
                },
                closeForm:function(){ //关闭新增弹窗
                    this.showform=false;
                    this.parameter={};
                },
                create:function(){ //打开新增弹窗
                    var that = this;
                    $.get('scmCategory/list_all',function (jsonData) {
                        that.productTypes=jsonData.data;
                    });
                    this.parameter.materialTypes=[];
                    this.showform=true;
                    this.parameter={
                        supCode: "",
                        supplierType: "",
                        materialTypes:[],
                        materialIds:[],
                        supAliasName: "",
                        supName: "",
                        note:'',//备注
                        bankName: "",
                        bankAccount: "",
                        version: "1",
                        taxNumber:"",
                        topContact: "",
                        topMobile: "",
                        topEmail: "",
                        state: "",
                        supplierContacts:[],//详情
                        supContactIds:[],
                    };
                },
                edit:function(model){ //编辑打开弹窗
                    var that = this;
                    this.parameter= model;
                    $.get('scmCategory/list_all',function (jsonData) {
                        that.productTypes=jsonData.data;
                    });
                    if(model.materialIds=='') this.parameter.materialTypes=[];
                    else this.parameter.materialTypes=model.materialIds.split(',');

                    this.showform=true;
                    setTimeout(function () {
                        $('#supplierContacts div').removeClass('radio');
                    },200)
                },

                save:function(){//提交
                    var _this=this;
                    var saveObj={};
                    saveObj.id=this.parameter.id;
                    saveObj.supCode=this.parameter.supCode;
                    saveObj.supplierType=this.parameter.supplierType;
                    saveObj.materialTypes=this.parameter.materialTypes;
                    saveObj.supAliasName=this.parameter.supAliasName;
                    saveObj.supName=this.parameter.supName;
                    saveObj.note=this.parameter.note;
                    saveObj.bankName=this.parameter.bankName;
                    saveObj.bankAccount=this.parameter.bankAccount;
                    saveObj.supplierContacts=[];
                    saveObj.supContactIds =this.parameter.supContactIds;
                    saveObj.version =this.parameter.version;
                    saveObj.taxNumber =this.parameter.taxNumber;
                    saveObj.state =this.parameter.state;
                    var parSup=this.parameter.supplierContacts;
                    for(var i=0;i<parSup.length;i++){
                        saveObj.supplierContacts[i]={
                            id:parSup[i].id,
                            contact:parSup[i].contact,
                            mobile:parSup[i].mobile,
                            email:parSup[i].email,
                            isTop:parSup[i].isTop,
                            position:parSup[i].position
                        }
                    }
                    var url='scmSupplier/modify';
                    saveObj.materialTypes=saveObj.materialTypes.toString();
                    if(!this.parameter.id) {
                        url='scmSupplier/create';
                        _this.parameter;
                    }
                    var submit=false;
                    var message='';
                    if(!this.parameter.supplierType) message='供应商类型';
                    else if(!this.parameter.supName) message='公司名';
                    else if(_this.parameter.supplierContacts.length<=0) message='联系人';
                    else  submit=true;
                    for(var i=0;i<_this.parameter.supplierContacts.length;i++) {
                        if(!_this.parameter.supplierContacts[i].contact){
                            submit=false;
                            message='联系人姓名';
                        }
                        else if(!_this.parameter.supplierContacts[i].mobile){
                            submit=false;
                            message='联系人电话';
                        }
                    }
                    if(this.parameter.state ='' ||!this.parameter.state){
                        this.parameter.state =1;
                    }else{
                        this.parameter.state =0;
                    }
                    if(submit){
                        $.ajax({
                            type:"POST",
                            url:url,
                            contentType:"application/json",
                            datatype: "json",
                            data:JSON.stringify(saveObj),
                            success:function(data){ //成功后返回
                                C.systemButtonNo('success','成功');
                                _this.showform=false;
                            },
                            error: function(){ //失败后执行
                                C.systemButtonNo('error','失败');
                                _this.showform=false;
                            }
                        });
                    }else {
                        C.systemButtonNo('error','请填写'+message);
                    }
                }

            },
            watch:{
                isTopWatch:function () {//借助isTopWatch监控移除默认选项
                    this.parameter.supplierContacts[0].isTop='0';
                    var supplierContactsTwo=this.parameter.supplierContacts;
                    this.parameter.supplierContacts=[];
                    this.parameter.supplierContacts=supplierContactsTwo;
                }
            },
       });
        C.vue=vueObj;
    }());
</script>



