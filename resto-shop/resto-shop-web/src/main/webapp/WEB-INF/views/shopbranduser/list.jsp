<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki"> 表单</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" action="{{m.id?'shopbranduser/modify':'shopbranduser/create'}}" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label>角色类型</label>
                                <%--<input type="text" class="form-control" name="roleName" v-model="m.roleName">--%>
                                <div>
                                    <select class="form-control" name="roleId"  v-model="m.roleId">
                                        <option v-for="role in roleList" :value="role.id">
                                            {{role.roleName}}
                                        </option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>品牌</label>
                                <input type="text" class="form-control" name="brandName" readonly v-model="brand.brandName">
                            </div>

                            <div class="form-group" v-if="roleActionScope">
                                <label>店铺</label>
                                <div v-if="!m.id">
                                    <select class="form-control" name="shopDetailId"  v-model="m.shopDetailId">
                                        <option v-for="shop in shopList" :value="shop.id">
                                            {{shop.name}}
                                        </option>
                                    </select>
                                </div>
                                <div v-if="m.id">
                                    <input type="text" class="form-control" name="shopName" readonly v-model="m.shopName">
                                </div>
                            </div>


                            <div class="form-group">
                                <label>账号名</label>
                                <div>
                                    <span class="col-md-5">
                                        <input type="text" class="form-control" name="nameLeft" v-model="m.nameLeft">
                                    </span>
                                    <span class="col-md-7">
                                        <input type="text" class="form-control" name="nameRight" readonly v-model="nameRight">
                                    </span>
                                </div>
                                <hr>
                            </div>

                            <div class="form-group">
                                <div>
                                    <label>密码</label>
                                </div>
                                <input type="text" class="form-control" name="password" v-model="m.password">
                            </div>

                            <div class="form-group">
                                <label>姓名</label>
                                <input type="text" class="form-control" name="name" v-model="m.name">
                            </div>

                            <div class="form-group">
                                <label>手机号码</label>
                                <input type="text" class="form-control" name="phone" v-model="m.phone">
                            </div>

                            <div class="form-group">
                                <label>邮箱</label>
                                <input type="text" class="form-control" name="email" v-model="m.email">
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">性别状态：</label>
                                <div class="col-md-8">
                                    <label>
                                        <input type="radio" name="sex" v-model="m.sex" value="1"> 男
                                    </label>
                                    <label>
                                        <input type="radio" name="sex" v-model="m.sex" value="2"> 女
                                    </label>
                                </div>
                            </div>

                        </div>
                        <input type="hidden" name="id" v-model="m.id" />
                        <input class="btn green"  type="submit"  value="保存"/>
                        <a class="btn default" @click="cancel" >取消</a>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="table-div">
        <div class="table-operator">
            <s:hasPermission name="shopbranduser/add">
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
    (function(){
        var cid="#control";
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            ajax : {
                url : "shopbranduser/list_all",
                dataSrc : ""
            },
            columns : [
                {
                    title : "用户角色",
                    data : "roleName",
                },
                {
                    title : "品牌",
                    data : "brandName",
                },
                {
                    title : "店铺",
                    data : "shopName",
                },
                {
                    title : "姓名",
                    data : "name",
                },
                {
                    title : "性别",
                    data : "sex",
                    createdCell: function (td, tdData) {
                        $(td).html(tdData == 1 ? "男" : "女");
                    }
                },
                {
                    title : "手机号码",
                    data : "phone",
                },
                {
                    title : "账号名",
                    data : "username",
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        var operator=[
                            <s:hasPermission name="shopbranduser/delete">
                            C.createDelBtn(tdData,"shopbranduser/delete"),
                            </s:hasPermission>
                            <s:hasPermission name="shopbranduser/modify">
                            C.createEditBtn(rowData),
                            </s:hasPermission>
                        ];
                        $(td).html(operator);
                    }
                }],
        });

        var C = new Controller(null,tb);
        var vueObj = new Vue({
            el:"#control",
            mixins:[C.formVueMix],
            data:{
                brand: {},
                shopList: [],
                roleList: [],
                nameRight: null,
                roleActionScope: true,
            },
            created : function () {
                this.initContent();
            },
            watch : {
                'm.roleId': 'hideShop',
            },
            methods:{
                initContent:function(){
                    var that = this;
                    $.ajax({
                        url:"shopbranduser/brandInfo",
                        type:"post",
                        dataType:"json",
                        success:function (resultData) {
                            that.brand = resultData;
                            that.nameRight = "@" + resultData.brandSign;
                            $.post("shopbranduser/queryShops", {
                                brandId : that.brand.id
                            }, function(result) {
                                if (result.success) {
                                    that.shopList = result.data;
                                } else {
                                    that.errorMsg(result.message);
                                }
                            });

                            $.post("shopbranduser/queryRoles", function(result) {
                                if (result.success) {
                                    that.roleList = result.data;
                                } else {
                                    that.errorMsg(result.message);
                                }
                            });
                        }
                    });
                },
                hideShop:function(){
                    var that = this;
                    $.post("erole/list_one", {id: that.m.roleId}, function(result) {
                        if (result.success) {
                            if(result.data.actionScope == 1){
                                that.roleActionScope = false;
                            }else{
                                that.roleActionScope = true;
                            }
                        }
                    });
                }
            }
        });
        C.vue=vueObj;



    }());
</script>
