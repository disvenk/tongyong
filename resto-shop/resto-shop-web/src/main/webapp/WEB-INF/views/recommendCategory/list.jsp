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
</style>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">新建推荐类型</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form class="form-horizontal" role="form" action="{{m.id?'recommendCategory/modify':'recommendCategory/create'}}" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">推荐类型:</label>
                                <div class="col-sm-8">
                                    <label><input type="radio" style="cursor:pointer"  name="type" required v-model="m.type" value="0">热销</label>
                                    <label><input type="radio" style="cursor:pointer"  name="type" required v-model="m.type" value="1">新品</label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">类别名称:</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" style="margin-left: 15px;" name="name" required v-model="m.name">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">推荐排序:</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" style="margin-left: 15px;" name="sort" required v-model="m.sort">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">添加餐品:</label>
                                <div class="col-sm-8" style="border: 1px solid #c2cad8;padding: initial;overflow-y: scroll;max-height: 200px;margin-left: 30px;">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>编号</th>
                                            <th>品名</th>
                                            <th>排序</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr v-for="(index,attr) in articles">
                                            <td>
                                                {{index+1}}
                                            </td>
                                            <td>
                                                <input type="hidden" name="articleId" v-model="attr.articleId" required="required"/>
                                                <input type="text" style="text-align:center;"  class="form-control" v-model="attr.articleName" name="articleName" required="required" readonly/>
                                            </td>
                                            <td>
                                                <input type="text" style="text-align:center;" class="form-control" v-model="attr.recommendSort" name="recommendSort" required="required" lazy/>
                                            </td>
                                            <td><button class="btn red" type="button" @click="removeMealItem(attr)">移除</button></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <div style="position: relative;text-align: center;padding: 15px 0px;">
                                        <button type="button" class="btn btn-primary" @click="addMealItem()">
                                            <i class="fa fa-plus"></i>
                                            添加餐品
                                        </button>
                                    </div>
                                </div>
                             </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">是否启用:</label>
                                <div class="col-sm-8">
                                    <label><input type="radio"  style="cursor:pointer" class="form-control" name="state" required v-model="m.state" value="1">启用</label>
                                    <label><input type="radio"  style="cursor:pointer" class="form-control" name="state" required v-model="m.state" value="0">不启用</label>
                                </div>
                            </div>
                            <div class="form-group text-center">
                                <input type="hidden" name="id" v-model="m.id" />
                                <input class="btn green" type="submit" value="保存"/>
                                <a class="btn default" @click="cancel">取消</a>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="article-choice-dialog" v-if="showform&&choiceArticleShow.show">
        <div class="modal-dialog " style="width:90%;">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">添加 菜品项
                        <span style="float: right">
                            搜索：<input type="search" class="form-control input-sm input-small input-inline" v-model="searchNameLike" />
                        </span>
                    </h4>
                </div>
                <div class="modal-body auto-height">
                    <div class="row">
                        <div class="col-md-6">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>
                                        <select v-model="searchNameLike">
                                            <option value="">餐品分类(全部)</option>
                                            <option :value="f.name" v-for="f in articlefamilys">{{f.name}}</option>
                                        </select>
                                    </th>
                                    <th>餐品名称</th>
                                    <th>添加</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="art in  choiceArticleShow.itemsLess  | filterBy searchNameLike ">
                                    <td>{{art.articleFamilyName}}</td>
                                    <td>{{art.name}}</td>
                                    <td>
                                        <button class="btn blue" type="button" @click="addArticleItem(art)">添加</button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="col-md-6">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>餐品名称(已添加)</th>
                                    <th>移除</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="art in choiceArticleShow.items">
                                    <td>{{art.articleName}}</td>
                                    <td>
                                        <button class="btn red" type="button" @click="removeArticleItem(art)">移除
                                        </button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn green" @click="updateAttrItems">确定</button>
                </div>
            </div>
        </div>
    </div>

    <div class="table-div">
        <div class="table-operator">
            <s:hasPermission name="recommendCategory/add">
                <button class="btn blue pull-right" @click="create">新建推荐类别</button>
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
        var allArticles = [];
        var articleList = [];
        var all = [];
        var articleType = {
            1: "单品",
            2: "套餐"
        }
        var tb = $table.DataTable({
            ajax : {
                url : "recommendCategory/list_all",
                dataSrc : ""
            },
            columns : [
                {
                    title : "推荐类型",
                    data : "type",
                    createdCell:function(td,tdData){
                        var str = "未知"
                        if(tdData == "0"){
                            str = "热销"
                        }else if(tdData == "1"){
                            str = "新品"
                        }
                        $(td).html(str);
                    }
                },
                {
                    title : "类别名称",
                    data : "name",
                },
                {
                    title : "菜品",
                    data : "articles",
                    createdCell:function(td,tdData,rowData){
                        var str = "";
                        if(tdData){
                            $(tdData).each(function(i,item){
                                str += "<span class='label label-info' style='width: 50%'>"+item.articleName +"</span>&nbsp;&nbsp;"
                            })
                        }else{
                            str = "暂无数据"
                        }

                        $(td).html(str);
                    }
                },
                {
                    title : "推荐排序",
                    data : "sort",
                },
                {
                    title : "是否启用",
                    data : "state",
                    createdCell:function(td,tdData){
                        var str = "未知"
                        if(tdData == "0"){
                            str = "未启用"
                        }else if(tdData == "1"){
                            str = "启用"
                        }
                        $(td).html(str);
                    }
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        console.log(rowData);
                        var operator=[
                            <s:hasPermission name="recommendCategory/delete">
                            C.createDelBtn(tdData,"recommendCategory/delete"),
                            </s:hasPermission>
                            <s:hasPermission name="recommendCategory/edit">
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
                articlefamilys: [],
                supportTimes: [],
                kitchenList: [],
                checkedUnit: [],
                articles: [],
                articleunits: {},
                unitPrices: [],
                mealtempList: [],
                articleUnits:[],
                articleList: [],
                choiceArticleShow: {show: false, mealAttr: null, items: [], itemsLess: [], currentFamily: ""},
                searchNameLike : "",
            },
            methods:{
                addUnit:function(e){
                    this.articleUnits.push({ name:"", sort:0 });
                },
                removeUnit:function(unit){
                    this.articleUnits.$remove(unit);
                },
                edit:function(model){
                    debugger;
                    this.articles = model.articles == null ? [] : model.articles;
                    this.m= model;
                    this.openForm();
                },
                create:function(){
                    this.articleUnits = [];
                    this.m = {
                        articleFamilyId: this.articlefamilys[0].id,
                        articleList: [],
//                                supportTimes: [],
//                                kitchenList: [],

                        isRemind: false,
                        activated: true,
                        showDesc: true,
                        showBig: true,
                        isEmpty: false,
//                                sort: 0,
//                                articleType: article_type,
                    };
                    this.checkedUnit = [];
                    this.showform = true;
//                            articleList = [];
                    this.articles = [];
                    articleList = all.concat();

                    if(  $('#printType').val() == 1){
                        this.printType = 1;
                    }else{
                        this.printType = 0;
                    }
                    this.openForm();
                },
                itemDefaultChange: function (attr, item) {
                    for (var i in attr.mealItems) {
                        var m = attr.mealItems[i];
                        if (m != item) {
                            m.isDefault = false;
                        }
                    }
                },
                choiceTypeChange:function(){

                    if(  $('#printType').val() == 1){
                        this.printType = 1;
                        $('#articleKitchen').css("display","block");
                    }else{
                        $('#articleKitchen').css("display","none");
                        this.printType = 0;
                    }
                },
                updateAttrItems: function () {
                    var items = $.extend(true, {}, this.choiceArticleShow).items;
                    this.articles = [];
                    for (var i = 0; i < items.length; i++) {
                        this.articles.push(items[i]);
                    }
                    $("#article-choice-dialog").modal('hide');
                    console.log(this.articles);
                },
                removeMealItem: function (attr) {
                    this.articles.$remove(attr);

                    articleList.push(attr);

                    this.choiceArticleShow.items = this.articles;

                },
                removeArticleItem: function (mealItem) {
                    debugger;
                    console.log(mealItem);
                    this.choiceArticleShow.items.$remove(mealItem);
                    //articleList.push(mealItem);
                    this.articleList.$set(this.articleList.length,mealItem);
                },

                addArticleItem: function (art) {
                    var item = {
                        name: art.name,
                        sort: art.sort,
                        articleName: art.name,
//                                priceDif: 0,
                        articleId: art.id,
//                                photoSmall: art.photoSmall,
                        isDefault: false,
                        price: art.price,
                        articleFamilyName: art.articleFamilyName
                    };

                    for (var i = 0; i < articleList.length; i++) {
                        if (articleList[i].id == art.id) {
                            this.articleList.$remove(art);
                        }
                    }

                    if (!this.choiceArticleShow.items.length) {
                        item.isDefault = true;
                    }
                    this.choiceArticleShow.items.push(item);
                },
                addMealItem: function () {
                    this.choiceArticleShow.show = true;
                    this.choiceArticleShow.items = this.articles;
                    this.choiceArticleShow.itemsLess = this.articleList;

                    this.$nextTick(function () {
                        $("#article-choice-dialog").modal('show');
                        var that = this;
                        $("#article-choice-dialog").on('hidden.bs.modal', function () {
                            that.choiceArticleShow.show = false;
                        });
                    })
                },

                delMealAttr: function (meal) {
                    this.m.articles.$remove(meal);
                }
                ,
                addMealAttr: function () {
                    var sort = this.maxarticlesort + 1;
                    this.m.articles.push({
                        article_name: "餐品属性" + sort,
                        sort: sort,
                        mealItems: [],
                    });
                }
                ,
                choiceMealTemp: function (e) {
                    var that = this;
                    C.confirmDialog("切换模板后，所有套餐编辑的内容将被清空，你确定要切换模板吗?", "提示", function () {
                        that.lastChoiceTemp = $(e.target).val();
                        var articles = [];
                        for (var i = 0; i < that.mealtempList.length; i++) {
                            var temp = that.mealtempList[i];
                            if (temp.id == that.lastChoiceTemp) {
                                for (var n = 0; n < temp.attrs.length; n++) {
                                    var attr = temp.attrs[n];
                                    articles.push({
                                        name: attr.name,
                                        sort: attr.sort,
                                        mealItems: [],
                                    });
                                }
                                that.m.articles = articles;
                                return false;
                            }
                        }
                        that.m.articles = [];
                    }, function () {
                        that.choiceTemp = that.lastChoiceTemp.toString();
                    });
                }
                ,
            },
            computed: {
                choiceArticleCanChoice: function () {
                    var arts = [];
                    for (var i in this.allArticles) {
                        var art = this.allArticles[i];
                        var has = false;
                        for (var n in this.choiceArticleShow.items) {
                            var mealItem = this.choiceArticleShow.items[n];
                            if (mealItem.articleId == art.id) {
                                has = true;
                                break;
                            }
                        }
                        if (!has && art.articleType == 1 && (this.choiceArticleShow.currentFamily == art.articleFamilyName || this.choiceArticleShow.currentFamily == "")) {
                            arts.push(art);
                        }
                    }
                    return arts;
                }
                ,
                maxarticlesort: function () {
                    var sort = 0;
                    for (var i in this.m.articles) {
                        var meal = this.m.articles[i];
                        if (meal.sort > sort) {
                            sort = meal.sort;
                        }
                    }
                    return parseInt(sort);
                }
                ,
                allUnitPrice: function () {
                    var result = [];
                    console.log(this.checkedUnit);
                    for (var i = 0; i < this.articleattrs.length; i++) {
                        var attr = this.articleattrs[i];
                        var checked = [];
                        if (!attr.articleUnits) {
                            continue;
                        }
                        for (var j = 0; j < attr.articleUnits.length; j++) {
                            var c = attr.articleUnits[j];
                            for (var n in this.checkedUnit) {
                                if (c.id == this.checkedUnit[n]) {
                                    checked.push({
                                        unitIds: c.id,
                                        name: "(" + c.name + ")"
                                    })
                                    break;
                                }
                            }
                        }
                        checked.length && result.push(checked);
                    }


                    function getAll(allData) {
                        var root = [];
                        for (var i in allData) {
                            var currentData = allData[i];
                            if (i > 0) {
                                for (var p  in allData[i - 1]) {
                                    var parent = allData[i - 1][p];
                                    parent.children = currentData;
                                }
                            } else {
                                root = currentData;
                            }
                        }
                        var allItems = [];
                        for (var n in root) {
                            var r = root[n];
                            getTreeAll(r, allItems);
                        }
                        return allItems;
                    }

                    function getTreeAll(tree, allItems) {
                        tree = $.extend({}, tree);
                        if (!tree.children) {
                            allItems.push($.extend({}, tree));
                            return allItems;
                        }
                        for (var i in tree.children) {
                            var c = tree.children[i];
                            c = $.extend({}, c);
                            c.unitIds = tree.unitIds + "," + c.unitIds;
                            c.name = tree.name + c.name;
                            if (!c.children) {
                                allItems.push(c);
                            } else {
                                getTreeAll(c, allItems);
                            }
                        }
                        return allItems;
                    }

                    var allItems = getAll(result);
                    for (var i in allItems) {
                        var item = allItems[i];
                        for (var i in this.unitPrices) {
                            var p = this.unitPrices[i];
                            if (item.unitIds == p.unitIds) {
                                item = $.extend(item, p);
                            }
                        }
                    }
                    this.unitPrices = allItems;
                    return allItems;
                }
            }
            ,
            created: function () {
                tb.search("").draw();
                var that = this;
                this.$watch("showform", function () {
                    if (this.showform) {
                        $("#article-dialog").modal("show");
                        var n = $('.color-mini').minicolors({
                            change: function (hex, opacity) {
                                if (!hex) return;
                                if (typeof console === 'object') {
                                    $(this).attr("value", hex);
                                }
                            },
                            theme: 'bootstrap'
                        });
                        $("#article-dialog").on("hidden.bs.modal", function () {
                            that.showform = false;
                        });
                    } else {
                        $("#article-dialog").modal("hide");
                        $(".modal-backdrop.fade.in").remove();
                    }
                });
                this.$watch("m", function () {
                    if (this.m.id) {
                        $('.color-mini').minicolors("value", this.m.controlColor);
                    }
                });


               /* $.post("article/singo_article", null, function (data) {
                    that.articleList = data;
                    articleList = data;
                    all = data;
                });*/
                $.post("article/singo_article_all", null, function (data) {
                    that.articleList = data;
                    articleList = data;
                    all = data;
                });
                $.post("articlefamily/list_all", null, function (data) {
                    console.log(data);
                    that.articlefamilys = data;
                });
                $.post("supporttime/list_all", null, function (data) {
                    that.supportTimes = data;
                });
                $.post("kitchen/list_all", null, function (data) {
                    that.kitchenList = data;
                });
                $.post("mealtemp/list_all", null, function (data) {
                    that.mealtempList = data;
                });
                $.post("articleattr/list_all", null, function (data) {
                    var article_units = {};
                    for (var i in data) {
                        var attr = data[i];
                        attr.checkedUnit = [];
                        var units = attr.articleUnits;
                        for (var i in units) {
                            var unit = units[i];
                            unit.attr = attr;
                            article_units[unit.id] = unit;
                        }
                    }
                    that.articleunits = article_units;
                    that.articleattrs = data;
                });
            }
        });
        C.vue=vueObj;
    }());
</script>
