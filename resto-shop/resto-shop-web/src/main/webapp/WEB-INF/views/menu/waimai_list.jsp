<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style>
    .form-horizontal .checkbox, .form-horizontal .radio {
        min-height: 34px;
        margin-left: 16px;
    }
    table thead tr th,table tbody tr td {
        text-align: center;
        vertical-align: middle;
    }
    .table>tbody>tr>td, .table>tbody>tr>th, .table>thead>tr>td, .table>thead>tr>th {
        vertical-align: middle;
        max-width: 60%;
    }
    ul,li {
        list-style: none;
        font-size: 16px;
    }
    ul.shop-list-wrap {
        margin-top: 50px;
        height: 200px;
        max-height: 200px;
        overflow-y: auto;
    }
    div.table-wrap {
        margin-top: 15px;
        width: 100%;
        height: 200px;
        max-height: 200px;
        overflow-x:hidden;
        overflow-y: auto;
    }
</style>
<style>
  /* 菜品编辑样式 */
  #article-dialog-deit {
    padding-top: 0;
  } 
  .article-attr-label {
        min-width: 50px;
    }

    .article-units > label {
        display: inline-block;
        min-width: 70px;
    }

    .modal-body.auto-height {
        max-height: 80vh;
        overflow-y: auto;
    }
    .form-group.isHidden {
        visibility: hidden;
    }
    .print-sort {

    }
    .radio-inline .radio {
        padding: 0;
    }
    .cf:before,.cf:after {
        content:"";
        display:table;
    }
    .cf:after { clear:both; }
    .ztree li span {
        font-size:15px;
        margin-left: 4px;
    }
    #control .dish-setting .col-md-4{
        float:none;
        width: 100%;
    }
    #control .col-md-5 {
        width: 30%;
    }
    #control .col-md-7 {
        width: 70%;
    }
    .dish-setting {
        width: 100%;
    }
    .dish-setting .title {
        font-size: 18px;
        font-weight: bold;
        color:#000;
        margin-left: 60px;
    }
    .sm-title {
        font-size: 18px;
        font-weight: bold;
        color:#000;
        margin-left: 60px;
    }
    .dish-setting-left {
        width: 50%;
        float: left;
    }
    .dish-setting-right {
        width: 50%;
        float: right;
    }
    .printerClass {
        display: inline-block;
        padding: 0 10px;
        margin-right: 20px;
        border: 1px solid #eee;
    }
    .radio-inline {
        cursor: pointer;
    }
    [v-cloak] {
        display: none;
    }
</style>
<div id="control" v-cloak>
    <!-- 新增|编辑菜单modal -->
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">{{m.id?'编辑菜单':'新建菜单'}}</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form class="form-horizontal" role="form" action="{{m.id?'menu/modify':'menu/create'}}" @submit.prevent="saveMenu">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">菜单名称:</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" style="margin-left: 15px;" name="menuName" required v-model="m.menuName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">当前菜单计划使用周期:</label>
                                <div class="col-sm-8">
                                    <label class="col-sm-4"><input type="radio"  style="cursor:pointer"  name="menuCycle" required v-model="m.menuCycle" value="1">长期有效</label>
                                    <label class="col-sm-2"><input type="radio"  style="cursor:pointer"  name="menuCycle" required v-model="m.menuCycle" value="2">周期</label>
                                    <div class="col-sm-6" >
                                        <input type="text" id="beginDate" class="form-control form_datetime col-sm-5" style="width:40%;" name="startTime" :value="m.startTime" v-model="m.startTime">
                                        <div class="col-sm-2" style="text-align: center;">至</div>
		                                <input type="text" id="endDate" class="form-control form_datetime col-sm-5" style="width:40%;"  name="endTime" :value="m.endTime" v-model="m.endTime">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">菜单备注:</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" style="margin-left: 15px;" name="remark" required v-model="m.remark">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">菜单状态:</label>
                                <div class="col-sm-8">
                                    <label><input type="radio" style="cursor:pointer"  name="menuState" required v-model="m.menuState" value="1">启用</label>
                                    <label><input type="radio" style="cursor:pointer"  name="menuState" required v-model="m.menuState" value="0">停用</label>
                                </div>
                            </div>
                            <div class="form-group text-center">
                                <input type="hidden" name="id" v-model="m.id" />
                                <input type="hidden" name="menuType" v-model="m.menuType" />
                                <input class="btn green" type="submit" value="保存"/>
                                <a class="btn default" @click="cancel">取消</a>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
    <!-- 已经关联菜品列表modal -->
    <div class="row form-div" v-if="showArticleList">
            <div class="col-md-offset-3 col-md-6" >
              <div class="portlet light bordered">
                <div class="portlet-title">
                  <div class="caption ">
                    <span class="caption-subject bold font-blue-hoki">选择菜品</span>
                  </div>
                </div>
                    <!-- <button type="button" class="close"><span>×</span></button> -->
                    <!-- <button type="button" class="btn btn-primary" @click="onShowArticle">
                        <i class="fa fa-plus"></i>
                        添加餐品
                    </button> -->
                    <div class="">
                        <div class="">
                            <h4 class="modal-title">
                                <select v-model="filterFamilyId">
                                    <option value="">餐品分类(全部)</option>
                                    <option :value="f.id" v-for="f in articlefamilyList">{{f.name}}</option>
                                </select>
                                <span style="float: right">
                                    搜索：<input type="search" class="form-control input-sm input-small input-inline" v-model="searchNameLike" />
                                </span>
                            </h4>
                        </div>
                      </div>
                    <div class="table-wrap">
                      <table class="table table-wrap">
                          <thead>
                              <tr>
                                  <th>序号</th>
                                  <th>销售类型</th>
                                  <th>菜品类别</th>
                                  <th>菜品类型</th>
                                  <th>菜品名称</th>
                                  <th>品牌定价</th>
                                  <th>门店售价</th>
                                  <th>菜品粉丝价</th>
                                  <th>排序</th>
                                  <th>操作</th>
                              </tr>
                          </thead>
                          <tbody>
                          <tr v-for="(index,art) in articleList" v-if="!art.checked && (art.distributionModeId==2 || art.distributionModeId==3)">
                              <td>{{index+1}}</td>
                              <td>{{art.distributionModeId | distributionModeFilter}}</td>
                              <td>{{art.articleFamilyId | articleFamilyFilter}}</td>
                              <td>{{art.articleType | articleTypeFilter}}</td>
                              <td>{{art.name}}</td>
                              <td>{{art.price}}</td>
                              <td>{{art.price}}</td>
                              <td>{{art.fansPrice}}</td>
                              <td>{{art.sort}}</td>
                              <td>
                                  <button class="btn blue" type="button" @click="addArticleItem(art)">添加</button>
                              </td>
                          </tr>
                          </tbody>
                      </table>
                    </div>
                    <div class="table-wrap">
                      <table class="table table-wrap" >
                          <thead>
                          <tr>
                              <th>序号</th>
                              <th>销售类型</th>
                              <th>菜品类别</th>
                              <th>菜品类型</th>
                              <th>菜品名称</th>
                              <th>品牌定价</th>
                              <th>门店售价</th>
                              <th>菜品粉丝价</th>
                              <th>排序</th>
                              <th>操作</th>
                          </tr>
                          </thead>
                          <tbody>
                          <tr v-for="(index,art) in menuArticleList">
                              <td>{{index+1}}</td>
                              <td>{{art.distributionModeId | distributionModeFilter}}</td>
                              <td>{{art.articleFamilyId | articleFamilyFilter}}</td>
                              <td>{{art.articleType | articleTypeFilter}}</td>
                              <td>{{art.name}}</td>
                              <td>{{art.price}}</td>
                              <td>{{art.shopPrice}}</td>
                              <td>{{art.fansPrice}}</td>
                              <td>{{art.sort}}</td>
                              <td>
                                  <button class="btn blue" type="button" @click="editMealItem(art)">编辑</button>
                                  <button class="btn red" type="button" @click="removeMealItem(art)">移除</button>
                              </td>
                          </tr>
                          </tbody>
                      </table>
                    </div>
                    <div class="portlet-body">
                      <div class="form-group text-center">
                          <input type="hidden" name="id" v-model="m.id" />
                          <a class="btn default" @click="showArticleList=false">完成</a>
                      </div>
                    </div>
                </div>
            </div>
        </div>
    <!-- 编辑菜品 -->
    <edit-article v-if="showArticleEdit" :article="currArticle" :showArticleEdit.sync="showArticleEdit" @close="closeArticleEdit"></edit-article>
    <!-- 启用门店 -->
    <div class="row form-div" v-if="showShopList">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">启用门店</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form class="form-horizontal" role="form" >
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">搜索:</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" style="margin-left: 15px;" name="shopName" v-model="m.shopName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">选择门店:</label>
                                <div class="col-sm-8">
                                    <div class="">
                                        <div class="col-sm-4">
                                            <select class="form-control" name="areaId" v-model="m.areaId" @change="getSelectedArea">
                                                <option value="" selected>选择大区</option>
                                                <option v-for="item in  areaList" :value="item.id">
                                                    {{item.areaName}}
                                                </option>
                                            </select>
                                        </div>
                                        <div class="col-sm-4">
                                            <select class="form-control" name="provinceId" v-model="m.provinceId" @change="getSelectedProvince">
                                                <option value="" >选择省</option>
                                                <option v-for="item in  provinceList" :value="item.id">
                                                    {{item.provinceName}}
                                                </option>
                                            </select>
                                        </div>
                                        <div class="col-sm-4">
                                            <select class="form-control" name="cityId" v-model="m.cityId">
                                                <option value="" >选择市</option>
                                                <option v-for="item in  cityList" :value="item.id">
                                                    {{item.cityName}}
                                                </option>
                                            </select>
                                        </div>
                                    </div>
                                    <ul class="shop-list-wrap">
                                        <li class="checkbox" v-for="item in shopList">
                                            <label>
                                                <input type="checkbox" :value="item.id" v-model="item.checked"> {{item.name}}
                                            </label>
                                        </li>
                                        
                                    </ul>
                                </div>
                            </div>
                            
                            
                            <div class="form-group text-center">
                                <input type="hidden" name="id" v-model="m.id" />
                                <input type="hidden" name="menuType" v-model="m.menuType" />
                                <input class="btn green" type="button" value="确定" @click="checkShopIds"/>
                                <a class="btn default" @click="showShopList=false">取消</a>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>

    <div class="table-div">
        <div class="col-md-6 row">
            <span>状态过滤：</span>
            <button class="btn btn-default" :class="{blue: state==1}" type="button" @click="filterByState('1')">已启用({{num.enableNum}})</button>
            <button class="btn btn-default" :class="{blue: state==0}" type="button" @click="filterByState('0')">已停用({{num.notEnableNum}})</button>
            <button class="btn btn-default" :class="{blue: state==-1}" type="button" @click="filterByState(-1)">全部({{num.allNum}})</button>
        </div>
        <div class="table-operator">
            <s:hasPermission name="menuTangshi/add">
                <button class="btn blue pull-right" @click="create">新建外卖菜单</button>
            </s:hasPermission>
        </div>
        <div class="clearfix"></div>
        <div class="table-filter"></div>
        <!-- 菜单列表 -->
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>
</div>

<script src="assets/customer/date.js" type="text/javascript"></script>
<script src="assets/customer/components/editArticle.js" type="text/javascript"></script>
<script>
    $(document).ready(function() {
    

        var cid="#control";
        var $table = $(".table-body>table");
        var menuType = 2; // 堂食1；外卖2
        var tb = $table.DataTable({
            ajax : {
                url : "menu/waimaiListAll?menuState=1",
                dataSrc : function(json) {
                    vueObj && vueObj._getMenuNum();
                    return json.map(function(item, index) {
                        item.index = index+1;
                        return item;
                    })
                } 
            },
            "ordering": false,
            columns : [
            {
                    title : "序号",
                    data : "index",
                },
                {
                    title : "菜单类型",
                    data : "id",
                    createdCell:function(td,tdData){
                        var str = "外卖菜单"
                        $(td).html(str);
                    }
                },
                {
                    title : "版本号",
                    data : "menuVersion",
                    createdCell:function(td,tdData,rowData,row){
                        $(td).html('V'+tdData);
                    }
                },
                {
                    title : "菜单名称",
                    data : "menuName",
                },
                {
                    title : "创建时间",
                    data : "createTime",
                    createdCell: function(td, tdData, rowData, row) {
                        var str = ''
                        if (tdData) {
                            str = tdData.substr(0, tdData.length-2)
                        } else {
                            str = '-'
                        }
                        $(td).html(str)
                    }
                },
                {
                    title : "创建人",
                    data : "createUser",
                },
                {
                    title : "更新时间",
                    data : "updateTime",
                    // defaultContent: '-',
                    createdCell: function(td, tdData, rowData, row) {
                        var str = ''
                        if (tdData) {
                            str = tdData.substr(0, tdData.length-2)
                        } else {
                            str = '-'
                        }
                        $(td).html(str)
                    }
                },
                {
                    title : "更新人",
                    data : "updateUser",
                    defaultContent: '-'
                },
                {
                    title : "计划使用周期",
                    data : "menuCycle",
                    createdCell: function(td, tdData, rowData, row) {
                        var str = ''
                        if (tdData == 1) {
                            str = '长期有效'
                        } else if (tdData == 2) {
                            str = rowData.startTime.substr(0, 10) + '至' + rowData.endTime.substr(0, 10)
                        }
                        $(td).html(str);
                    }
                },
                {
                    title: '状态',
                    data: 'menuState',
                    createdCell: function(td, tdData) {
                        var str = ''
                        if (tdData == 1) {
                            str = '已启用'
                        } else if (tdData == 0) {
                            str = '已停用'
                        }
                        $(td).html(str)
                    } 
                },
                {
                    title : "关联菜品",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        // console.log(rowData);
                        var button = $('<button type="button" class="btn green">关联菜品</button>')
                        button.click(function() {
                            vueObj.chooseArticles(rowData)
                        });
                        if (rowData.menuState == 0) {
                          button = $('<button type="button" class="btn green disabled">关联菜品</button>')
                        }
                        $(td).html(button);
                    }
                },
                {
                    title : "启用门店",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        var button = $('<button type="button" class="btn green">启用门店</button>')
                        button.click(function() {
                            vueObj.chooseShop(rowData)
                        });
                        if (rowData.menuState ==0) {
                          button = $('<button type="button" class="btn green disabled">启用门店</button>')
                        }
                        $(td).html(button);
                    }
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        // console.log(rowData);
                        let editBtn = C.createEditBtn(rowData);
                        if (rowData.menuState == 0) {
                          editBtn = $("<button class='btn btn-xs btn-primary disabled'>编辑</button>");
                        }
                        let copyBtn = $("<button class='btn btn-xs btn-primary'>复制</button>");
                        copyBtn.click(function() {
                            vueObj.copyMenu(rowData)
                        });
                        var operator=[
                            <s:hasPermission name="menuTangshi/edit">
                            editBtn,
                            </s:hasPermission>
                            copyBtn,
                        ];
                        $(td).html(operator);
                    }
                }
                ],
        });

        var C = new Controller(null,tb);
        var vueObj = new Vue({
            el:"#control",
            mixins:[C.formVueMix],
            data:{
                articlefamilyList: [],  // 菜品分类列表
                articleListAll: [],     // 菜品列表
                menuArticleList: [],    // 当前菜单的菜品列表
                filterFamilyId: '',      // 当前选中的菜品分类id,用于过滤
                searchNameLike : "",    // 搜索菜品
                showArticleList: false, // 显示关联菜品列表
                m: {},                  // 菜单model
                state: 1,               // 过滤的状态 1:启用；0:不启用 
                num: {},                 // 不同状态数量
                showShopList: false,    // ‘启用门店’modal显示开关
                areaList: [],       // 行政大区列表
                provinceListAll: [],   // 省列表
                cityListAll: [],        // 城市列表
                provinceList: [],   // 省列表
                cityList: [],        // 城市列表
                shopListAll: [],        // 店铺列表
                chooseShops: [],     // 之前选中的关联店铺
                showArticleEdit: false, // 编辑菜品显示
                currArticle: {},      // 当前被选中编辑的article
            },
            computed: {

                provinceList: function() {
                    let provinceList = this.provinceListAll;
                    provinceList = provinceList.filter(item => {
                        return item.zipCode==this.m.areaId
                    })
                    let index = provinceList.findIndex(p => p.id==this.m.provinceId)
                    if (index == -1) this.m.provinceId = ''
                    return provinceList
                },
                cityList: function() {
                    let cityList = this.cityListAll;
                    cityList = cityList.filter(item => {
                        return item.provinceId == this.m.provinceId
                    })
                    let index = cityList.findIndex(c => c.id==this.m.cityId)
                    if (index == -1) this.m.cityId = ''

                    return cityList;
                },
                // 过滤后的菜品列表 
                articleList: function() {
                    let articleList = this.articleListAll;
                    // 分类过滤
                    if (this.filterFamilyId.length > 0) {
                        articleList = articleList.filter(a => a.articleFamilyId ==this.filterFamilyId)
                    }
                    
                    // 搜索
                    if (this.searchNameLike.length > 0) {
                        articleList = articleList.filter(a => a.name.indexOf(this.searchNameLike) !==-1)
                    }

                    return articleList;
                },
                // 过滤后的店铺列表
                shopList: function() {
                    var shopList = []
                    var that = this;
                    shopList = this.shopListAll;
                    if (this.m ) {
                        
                        // shopName过滤
                        if (this.m.shopName && this.m.shopName.length > 0) {
                            shopList = shopList.filter(function(item) {
                                return item.name.indexOf(that.m.shopName) !== -1
                            })
                        }
                        // 大区过滤
                        if (this.m.areaId) {
                            shopList = shopList.filter(function(item) {
                                return item.areaId == that.m.areaId
                            })
                        }
                        // 省份过滤
                        if (this.m.provinceId) {
                            shopList = shopList.filter(function(item) {
                                return item.provinceId == that.m.provinceId                               
                            })
                        }
                        // 城市过滤
                        if (this.m.cityId) {
                            shopList = shopList.filter(function(item) {
                                return item.cityId == that.m.cityId
                            })
                        }
                    }
                    shopList = shopList.map(item => {
                        var index = this.chooseShops.findIndex(chooseShop => chooseShop.shopDetailId == item.id)
                        if (index == -1) {
                            item.checked = false;
                        } else {    
                            item.checked = true;
                        }
                        item = $.extend({}, item)
                        return item;
                    })
                    // console.log(JSON.stringify(shopList))
                    return shopList;
                }
            },
            created: function () {
                tb.search("").draw();
                var that = this;
                this.$watch("m", function () {
                    that.$nextTick(function() {
                        //时间插件
                        $('.form_datetime').datetimepicker({
                            // endDate:new Date(),
                            minView:"month",
                            maxView:"month",
                            autoclose:true,//选择后自动关闭时间选择器
                            todayBtn:true,//在底部显示 当天日期
                            todayHighlight:true,//高亮当前日期
                            format:"yyyy-mm-dd",
                            startView:"month",
                            language:"zh-CN"
                        });
                    })
                });
                // 不同状态，统计数据
                this._getMenuNum();
                $.post("province/list_area", null, function (data) {
                    that.areaList = data;
                });
                $.post("province/list_province", null, function (data) {
                    that.provinceListAll = data;
                });
                $.post("province/list_city", null, function (data) {
                    that.cityListAll = data;
                });
                // 所有的店铺列表
                $.post("menu/byShop", null, function (data) {
                    that.shopListAll = data;
                });
                $.post("brandArticleFamily/list_all", null, function (data) {
                    that.articlefamilyList = data;
                });
                //http://localhost:8380/shop/articleManage/list_all?_=1542594655979
                $.post("articleManage/list_all", null, function (data) {
                    that.articleListAll = data;
                });
            },
            methods:{
                _getMenuNum: function() {
                  var that = this;
                  if (menuType == 1) {
                    $.post("menu/tangshiNum", null, function (data) {
                      that.num = data;
                    });
                  } else if (menuType == 2) {
                    $.post("menu/waimaiNum", null, function (data) {
                      that.num = data;
                    });
                  }
                },
                // 根据菜单状态过滤 1:启用；0：不启用
                filterByState: function(state) {
                    this.state = state;
                    tb.ajax.url('menu/waimaiListAll?menuState='+state).load();
                },
                // 新建菜单,初始化model
                create: function() {
                    this.m = {
                        menuName: '',
                        menuType: menuType,   // 1堂食  2外卖
                        menuCycle: 1,   //1长期有效    2时间周期
                        startTime: null,
                        endTime: null,
                        remark: '',      //菜单备注 
                        menuState: 1,    //1启动   0不启用
                    };
                    // this.checkedUnit = [];
                    this.openForm();
                },
                // （显示）编辑菜单
                edit:function(model) {
                    this.m = $.extend({}, model);
                    this.openForm();
                },
                // 编辑|保存 
                saveMenu(e) {
                    if (!this.m.id) {
                        // 新建
                        this.save(e)
                    } else {
                        // 编辑
                        var that = this;
                        if (this.m.menuState == 0) {
                            // 停用
                            $.post("menushop/listMenuShop", {menuId: this.m.id}, function (data) {
                                if (data.length > 0) {
                                    C.confirmDialog( "你确定要停用菜单吗?", "提醒", function () {
                                        that.save(e)
                                    });
                                } else {
                                    that.save(e)
                                }
                            });
                        } else {
                            that.save(e)
                        }
                    }
                },
                // 复制菜单
                copyMenu: function(rowData) {
                    var that = this;
                    $.post("menu/copy", {id: rowData.id}, function (data) {
                        tb.ajax.reload();
                        that._getMenuNum();
                    });
                },
                // (显示)关联菜品
                chooseArticles: function(rowData) {
                    var that = this;
                    this.m = rowData;

                    $.post("menushop/listMenuShop", {menuId: rowData.id}, function (data) {
                          if (data.length > 0) {
                            C.errorMsg('当前菜单已被门店启用，不能再关联菜品');
                          } else {
                            that._syncArticles(that.m.id)
                          }
                      });
                },
                // 获取到最新 关联菜品
                _syncArticles: function(menuId) {
                    var that = this;
                    $.post("menuarticle/listMenuId", {menuId: menuId}, function (data) {
                        that.showArticleList = true;
                        that.menuArticleList = data;
                        that.articleListAll = that.articleListAll.map(art => {
                            let index = data.findIndex(a => a.articleId == art.id)
                            if (index !== -1) {
                                art.checked = true;
                                // art = $.extend(true, art, data[index]) // 合并编辑后的属性
                            } else {
                                art.checked = false;
                            }
                            art = $.extend({}, art)
                            return art;
                        })
                      
                    });
                },
                // 添加
                addArticleItem(article) {
                    var that = this;
                    var currArticle = $.extend({}, article) // 避免改动原来的结构
                    currArticle.menuId = this.m.id;
                    currArticle.shopPrice = currArticle.price;  //默认门店价格和品牌价格一样
                    currArticle.articleId = currArticle.id;
                    delete currArticle.id
                    let params = [
                        currArticle
                    ]
                    $.ajax({  
                        type:"POST",  
                        url: "menuarticle/create",  
                        // async:false,
                        dataType:"json",  
                        contentType:"application/json",  
                        data:JSON.stringify(params),  
                        success:function(data){
                            if (data.success) {
                                // article.checked = true;
                                // article = $.extend({}, article)
                                that._syncArticles(currArticle.menuId)
                            }   			            						 					
                        }  
                    }); 
                },
                // 移除
                removeMealItem(article) {
                    var that = this
                    $.post("menuarticle/deleteArticleId", {articleId: article.articleId}, function (data) {
                        // let index = that.articleListAll.findIndex(a => a.id==article.id)
                        if (data.success) {
                            // article.checked = false;
                            // article = $.extend({}, article)
                            that._syncArticles(that.m.id)
                        }
                    }); 
                },
                // （显示）编辑
                editMealItem(article) {
                    // todo
                    article.menuId = this.m.id;
                    article.shopPrice = article.shopPrice || article.price  // todo可以已经设置过了
                    this.currArticle = article;
                    this.showArticleEdit = true;
                    
                },
                closeArticleEdit() {
                  this._syncArticles(this.m.id)
                  this.showArticleEdit = false;
                },
               
                // 启用门店
                chooseShop: function(rowData) {
                    var that = this;
                    this.m = rowData;
                    this.m.areaId = ''
                    this.m.provinceId = ''
                    this.m.cityId = ''
                    // this.chooseShops = [];

                    
                    $.post("menuarticle/listMenuId", {menuId: this.m.id}, function (data) {
                      // 判断菜单是否有关联的菜品
                      if (data.length === 0) {
                        C.errorMsg('当前菜单没有菜品，请先关联菜品');
                      } else {
                        $.post("menushop/listMenuShop", {menuId: rowData.id}, function (data) {
                            // tb.ajax.reload();
                            that.chooseShops = data;
                            that.showShopList = true;
                        });
                      }
                    });

                },
                // 选择大区
                getSelectedArea() {
                    var that = this;
                    that.m = $.extend({}, that.m)
                    if (that.m.areaId ) {
                    }
                },
                // 选择省份
                getSelectedProvince() {
                    var that = this;
                    that.m = $.extend({}, that.m)
                },
                // 检查
                checkShopIds() {
                    var that = this;
                    var params = {
                        type: menuType,  
                      menuId: this.m.id,
                      shops: []
                    }
                    this.shopList.forEach(shop => {
                        if (shop.checked) {
                            params.shops.push(shop.id)
                        }
                    })
                    // check选中的门店，是否是之前就是勾选过的
                    let hasNewCheck = false
                    params.shops.forEach(shopId => {
                      let  index = this.chooseShops.findIndex(oldChoose => oldChoose.shopDetailId == shopId)
                      if (index === -1) {
                        hasNewCheck = true;
                      }
                    })


                    if (params.shops.length == 0 || !hasNewCheck) { // 断开菜单和门店关联
                      this._saveShopids()
                    } else {
                      $.ajax({  
                              type:"POST",  
                              url: "menushop/checkShopMenu",  
                              // async:false,
                              dataType:"json",  
                              contentType:"application/json",  
                              data:JSON.stringify(params),  
                              success:function(data){   			            						 					
                                  if ( ! data.success) {
                                      C.confirmDialog( data.message, "提醒", function () {
                                          that._saveShopids()
                                      });
                                  } else {
                                      that._saveShopids();
                                  }
                              }  
                          });
                    }
                },
                // 分配至门店 
                _saveShopids() {
                    var that = this;
                    var params = [
                    ]
                    this.shopList.forEach(shop => {
                        if (shop.checked) {
                            params.push({
                                shopDetailId: shop.id,
                                menuId: that.m.id,
                                type: menuType,
                            })
                        }
                    })
                    if (params.length === 0) {
                        $.post("menushop/removeShop", {menuId: this.m.id}, function (data) {
                            that.showShopList = false;
                        });
                    } else {
                        $.ajax({  
                            type:"POST",  
                            url: "menushop/create",  
                            // async:false,
                            dataType:"json",  
                            contentType:"application/json",  
                            data:JSON.stringify(params),  
                            success:function(data){   			            						 					
                                // tb.ajax.reload();
                                that.showShopList = false;
                            }  
                        }); 
                    }

                }
            },
            filters: {
                distributionModeFilter(type) {
                    let cfg = {
                        1: '堂食', 
                        2: '外卖',
                        3: '堂食/外卖'
                    }
                    return cfg[type]
                },
                articleFamilyFilter(id) {
                    let index = this.articlefamilyList.findIndex(item => item.id ==id)
                    let familyName = ''
                    if (index != -1) {
                        familyName = this.articlefamilyList[index].name
                    }
                    return familyName;
                },
                articleTypeFilter(type) {
                    let cfg = {
                        1: '普通餐品',
                        2: '套餐餐品'
                    }
                    return cfg[type]
                }
            }
            
        });
        C.vue=vueObj;
});
</script>
