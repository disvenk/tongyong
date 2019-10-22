<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <!--新增分类开始-->
    <div class="row form-div" v-show="showform" style="z-index:2;">
        <div class="col-md-offset-3 col-md-6" style="text-align:center">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">新增品牌</span>
                    </div>
                </div>
                <div class="portlet-body">
                        <div class="form-body">
                            <div class="form-group row" >
                                <label class="col-md-4 control-label">品牌名称<span style="color:#FF0000;">*</span></label>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="categoryName" v-model="parameter.categoryName" required="required">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-md-4 control-label">一级分类<span style="color:#FF0000;">*</span></label>
                                <div class="col-md-3">
                                    <select class="bs-select form-control" name="categoryOneId" v-model="parameter.categoryOneId" @change="categoryOneCh">
                                        <option disabled="" selected="" value="">请选择</option>
                                        <option  v-for="categoryOnes in categoryOne" value="{{categoryOnes.id}} ">
                                            {{categoryOnes.categoryName}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-md-4 control-label">二级分类<span style="color:#FF0000;">*</span></label>
                                <div class="col-md-3">
                                    <select name="parentId" v-model="parameter.parentId" class="bs-select form-control" >
                                        <option disabled="" selected="" value="">请选择</option>
                                        <option  v-for="categoryTwos in categoryTwo" value="{{categoryTwos.id}}" v-if="parameter.categoryOneId == categoryTwos.parentId">
                                            {{categoryTwos.categoryName}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row" >
                            <label class="col-md-4 control-label">排序</label>
                            <div class="col-md-3">
                                <input type="number" class="form-control" name="sort" v-model="parameter.sort"  required="required">
                            </div>
                            </div>
                            <div class="form-group row" >
                                <label class="col-md-4 control-label" >关键词</label>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="keyword" v-model="parameter.keyword"  required="required">
                                </div>
                            </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-md-2 control-label" >备注</label>
                                <div class="col-sm-8">
                                    <textarea class="form-control" name="categoryDesc" v-model="parameter.categoryDesc"></textarea>
                                </div>
                            </div>
                        <div class="form-group text-center">
                            <input class="btn green"  type="submit" @click="save"  value="提交"/>&nbsp;&nbsp;&nbsp;
                            <a class="btn default" @click="detailsCl">取消</a>
                        </div>
                </div>
            </div>
        </div>
    <!--新增分类结束-->
    </div>
    <!--查看品牌开始-->
    <div class="row form-div" v-show="Find2" style="z-index:1;">
        <div class="col-md-offset-3 col-md-6" style="background: #FFF;">
            <div class="text-center" style="padding: 20px 0">
                <span class="caption-subject bold font-blue-hoki">查看品牌</span>
            </div>
            <div style="margin-top: -30px;">
                    <span>{{showTwo}}</span>
                    <button @click="create" class="btn btn-xs btn-primary" style="margin-left:10px;margin-bottom:4px;">新增品牌</button>
            </div>
                    <div class="form-group">
                        <table class="table table-bordered" >
                            <thead>
                            <tr>
                                <th>编号</th>
                                <th>品牌名称</th>
                                <th>排序</th>
                                <th>关键词</th>
                                <th>备注</th>
                                <%--<th>操作</th>--%>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="finds in findsArrTwo">
                                <td>{{finds.categoryCode}}</td>
                                <td>{{finds.categoryName}}</td>
                                <td>{{finds.sort}}</td>
                                <td>{{finds.keyword}}</td>
                                <td>{{finds.categoryDesc}}</td>
                                <%--<td><button class="btn btn-xs btn-primary" style="margin-right:10px;">编辑</button><button class="btn btn-xs btn-danger">删除</button></td>--%>
                            </tr>
                            </tbody>
                        </table>
                    </div>
            <div class="text-center" style="padding: 20px 0">
                <a class="btn default" @click="close">关闭</a>
            </div>
            </div>

        </div>
    <!--查看品牌结束-->

<!--查看下级-->
    <div class="table-div">
        <div class="table-operator">
            <s:hasPermission name="scmSupplier/create">
                <button class="btn green pull-right" @click="create">新增品牌</button>
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

    <div style="padding: 20px 0;overflow: auto;max-height:350px;" v-show="find">
                <table class="table table-bordered table-condensed">
                    <thead >
                    <tr style="text-align:center">
                        <th>编号</th><th>分类名称</th><th>级别</th><th>排序</th><th>备注</th><th>设置</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="finds in findsArr">
                        <td>{{finds.categoryCode}}</td>
                        <td>{{finds.categoryName}}</td>
                        <td>{{finds.categoryHierarchy}}</td>
                        <td>{{finds.sort}}</td>
                        <td>{{finds.categoryDesc}}</td>
                        <td><button class="btn btn-xs btn-primary" @click="showFind2(finds)">查看品牌</button></td>
                    </tr>
                    </tbody>
                </table>
            </div>
</div>

<script>
    (function () {
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            ajax : {
                url : "scmCategory/list_all",
                dataSrc : "data",
            },
            ordering: false,//取消上下排序
            columns : [
                {
                    title : "编号",
                    data : "categoryCode",
                },
                {
                    title : "分类名称",
                    data : "categoryName",
                },
                {
                    title: "级别",
                    data: "categoryHierarchy",
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData){
                        var operator=[
                            <s:hasPermission name="scmCategory/showFind">//查看下级
                            C.showFind(rowData),
                            </s:hasPermission>
                            <%--<s:hasPermission name="scmCategory/createFind"> 新增下级--%>
                            <%--C.createFind(rowData),--%>
                            <%--</s:hasPermission>--%>
                        ];
                        $(td).html(operator);
                    }
                },
            ],
        });

        var C = new Controller(null, tb);
        var vueObj = new Vue({
                el: "#control",
                mixins: [C.formVueMix],
                data: {
                    find:false,//查看下级
                    findsArr:[],//查看下级对象
                    Find2:false,//查看品牌
                    findsArrTwo:[],//查看品牌对象
                    showform:false,
                    showOne:'',
                    showTwo:'',
                    categoryOne:[],//一级分类
                    categoryTwo:[],//二级分类
                    parameter:{},
                },
                methods: {
                    categoryOneCh:function(){
                        this.parameter.parentId='';
                    },
                    create:function () { //新增分类
                      this.showform=true;
                      this.parameter={
                          categoryName:'',//分类名称
                          parentId:'',//ID
                          sort:'1',//排序
                          keyword:'',//关键词
                          categoryOneId:'',
                          categoryDesc:"",//备注
                          categoryHierarchy:'3',
                      };

                    },
                    save:function(){//新增提交
                        var that=this;
                        var submit=false;
                        var message='';
                        if(!this.parameter.categoryName) message='分类名称';
                        else if(!this.parameter.categoryOneId) message='一级类别';
                        else if(!this.parameter.parentId) message='二级类别';
                        else submit=true;
                        if(submit){
                            delete this.parameter.categoryOneId;
                            $.ajax({
                                type:"POST",
                                url:'scmCategory/create',
                                contentType:"application/json",
                                datatype: "json",
                                data:JSON.stringify(that.parameter),
                                beforeSend:function(){ //请求之前执行
                                },
                                success:function(data){ //成功后返回
                                    C.systemButtonNo('success','成功');
                                    that.showform=false;
                                    that.Find2=false;
                                },
                                error: function(){ //失败后执行
                                    C.systemButtonNo('error','失败');
                                }
                            });
                        }else {
                            C.systemButtonNo('error','请填写'+message);
                        }
                    },
                    detailsCl:function () {//取消新增
                        this.showform=false;
                    },
                    showFind:function (data) { //查看下级
                       this.show1=data.categoryName;
                        this.find=true;
                        var that=this;
                        var data={
                            hierarchyId:data.categoryHierarchy,
                            id:data.id
                        };
                        $.post('scmCategory/look_down',data,function (data) {
                           that.findsArr=data.data;
                        })
                    },
                    detailsCli:function () { //关闭查看下级
                        this.find=false;
                    },
                    showFind2:function (data) {//查看品牌
                        this.showTwo=data.categoryName;
                        this.Find2=true;
                        var that=this;
                        var data={
                            hierarchyId:data.categoryHierarchy,
                            id:data.id
                        };
                        $.post('scmCategory/look_down',data,function (data) {
                            that.findsArrTwo=data.data;
                        })
                    },
                    close:function () { //关闭查看品牌
                        this.Find2=false;
                    },

                },
                computed: {
                }
                ,
            ready:function () {
                var that=this;
                $.post('scmCategory/list_categoryHierarchy?categoryHierarchy=1',null,function (data) {
                    that.categoryOne= data.data;
                });
                $.post("scmCategory/list_categoryHierarchy?categoryHierarchy=2", null, function (data) {
                    that.categoryTwo= data.data;
                });
            },
                created: function () {
                }
            })
        ;
        C.vue = vueObj;

    }());


</script>
