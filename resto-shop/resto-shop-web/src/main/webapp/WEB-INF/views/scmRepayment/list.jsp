<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style>
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


</style>
<div id="control">

    <div class="modal fade" id="article-dialog" v-if="showform">
        <div class="modal-dialog " style="width:90%;">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">规格属性</h4>
                </div>
                <form class="form-horizontal" role="form"
                      @submit.prevent="save">

                    <div class="modal-body auto-height">
                        <div class="form-body">


                            <div class="col-md-12">
                                <div class="portlet light bordered">

                                    <div class="portlet-body">
                                        <div class="portlet box blue-hoki" >
                                            <div class="portlet-title">
                                                <div class="caption">
                                                    <label class="control-label col-md-4"
                                                           style="width:120px">属性名称&nbsp;</label>
                                                    <div class="col-md-6">
                                                        <input class="form-control" type="text" v-model="m.name"
                                                               id="uName" required="required" lazy>
                                                    </div>

                                                </div>


                                                <div class="caption">
                                                    <label class="control-label col-md-4"
                                                           style="width:200px">排序&nbsp;</label>
                                                    <div class="col-md-6">
                                                        <input class="form-control" type="text" v-model="m.sort"
                                                               id="uSort" required="required" name="sort" lazy
                                                        >
                                                    </div>

                                                </div>


                                                <%--<div class="tools">--%>
                                                    <%--<a href="javascript:;" class="remove"--%>
                                                       <%--@click="delMealAttr(attr)"></a>--%>
                                                <%--</div>--%>
                                            </div>
                                            <div class="portlet-body"> <!--v-for="attr in articles "-->
                                                <%--<div class="portlet-body" v-if="m.id != null" v-for="attr in m.articles "  >--%>
                                                <div class="form-group col-md-12" v-for="item in unit.detailList">
                                                    <div class="flex-row" style="text-align: center">

                                                        <div class="flex-2">名称</div>
                                                        <%--<div class="flex-2">差价</div>--%>
                                                        <div class="flex-2">排序</div>
                                                        <div class="flex-2">移除</div>
                                                    </div>
                                                    <div class="flex-row" style="text-align: center">


                                                        <div class="flex-2">
                                                            <input type="text" class="form-control"
                                                                   v-model="item.name" name="name"
                                                                   required="required"/>
                                                        </div>
                                                        <%--<div class="flex-2">--%>
                                                            <%--<input type="text" class="form-control"--%>
                                                                   <%--v-model="item.spread" name="spread"--%>
                                                                   <%--required="required"/>--%>
                                                        <%--</div>--%>
                                                        <div class="flex-2">
                                                            <input type="text" class="form-control" name="sort"
                                                                   v-model="item.sort"
                                                                   required="required" lazy/>
                                                        </div>

                                                        <div class="flex-2">
                                                            <button class="btn red" type="button"
                                                                    @click="removeMealItem(item)">移除
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-4 col-md-offset-8">
                                                    <button class="btn btn-block blue" type="button"
                                                            @click="addItem"><i class="fa fa-cutlery"></i>
                                                        添加规格
                                                    </button>
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>
                                        </div>
                                        <%--<div class="col-md-4 col-md-offset-4">--%>
                                            <%--<button class="btn btn-block blue" type="button"--%>
                                                    <%--@click="addMealItem(unit.familyList)">--%>
                                                <%--<i class="fa fa-plus"></i>--%>
                                                <%--添加规格包属性--%>
                                            <%--</button>--%>
                                        <%--</div>--%>
                                        <div class="clearfix"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                    <div class="modal-footer">
                        <input type="hidden" name="id" v-model="m.id"/>
                        <button type="button" class="btn btn-default" @click="cancel">取消</button>
                        <button type="submit" class="btn btn-primary">保存</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="modal fade" id="article-choice-dialog" v-if="showform&&choiceArticleShow.show">
        <div class="modal-dialog " style="width:90%;">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">添加 菜品项</h4>
                </div>
                <div class="modal-body auto-height">
                    <div class="row">
                        <div class="col-md-6">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>
                                        <select v-model="choiceArticleShow.currentFamily">
                                            <option value="">餐品分类(全部)</option>
                                            <option :value="f.name" v-for="f in articlefamilys">{{f.name}}</option>
                                        </select>
                                    </th>
                                    <th>餐品名称</th>
                                    <th>添加</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="art in choiceArticleShow.itemsLess">
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
            <s:hasPermission name="unit/add">
                <button class="btn green pull-right" @click="create(2)">新建</button>
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
    (function () {
        var cid = "#control";
        var action;
        var unitId = null;

        var $table = $(".table-body>table");
        var allArticles = [];
        var articleList = [];
        var all = [];
        var articleType = {
            1: "单品",
            2: "套餐"
        }


        var tb = $table.DataTable({
            ajax: {
                url: "unit/list_all",
                dataSrc: ""
            },
            columns: [
                {
                    title: "属性名称",
                    data: "name",
                },


                {
                    title: "属性明细",
                    data: "details",
                    defaultContent: "",
                    createdCell: function (td, tdData) {
                        $(td).html('');

                        for (var i in tdData) {
                            if (tdData[i].name) {
                                var span = $("<span class='btn blue btn-xs'></span>");
                                $(td).append(span.html(tdData[i].name));
                            }

                        }
                    }
                },

                {
                    title: "操作",
                    data: "id",
                    createdCell: function (td, tdData, rowData, row) {
                        var operator = [
                            <s:hasPermission name="unit/delete">
                            C.createDelBtn(tdData, "unit/delete"),
                            </s:hasPermission>
                            <s:hasPermission name="unit/edit">
                            C.createEditBtn(rowData),
                            </s:hasPermission>
                        ];
                        $(td).html(operator);
                    }
                }],
            initComplete: function () {
                var api = this.api();
                api.search('');
                var data = api.data();
                for (var i = 0; i < data.length; i++) {
                    allArticles.push(data[i]);
                }
                var columnsSetting = api.settings()[0].oInit.columns;
                $(columnsSetting).each(function (i) {
                    if (this.s_filter) {
                        var column = api.column(i);
                        var title = this.title;
                        var select = $('<select><option value="">' + this.title + '(全部)</option></select>');
                        var that = this;
                        column.data().unique().each(function (d) {
                            select.append('<option value="' + d + '">' + ((that.s_render && that.s_render(d)) || d) + '</option>')
                        });

                        select.appendTo($(column.header()).empty()).on('change', function () {
                            var val = $.fn.dataTable.util.escapeRegex(
                                    $(this).val()
                            );
                            column.search(val ? '^' + val + '$' : '', true, false).draw();
                        });
                    }
                });
            }
        });

        var C = new Controller(null, tb);
        var vueObj = new Vue({
                    el: "#control",
                    mixins: [C.formVueMix],
                    data: {
                        articlefamilys: [],
                        supportTimes: [],
                        kitchenList: [],
                        checkedUnit: [],
                        articleattrs: [],
                        articles: [],
                        unit: new HashMap(),

                        articleunits: {},
                        familyLis: [],

                        unitPrices: [],
                        mealtempList: [],
                        articleList: [],
                        choiceTemp: "",
                        lastChoiceTemp: "",
                        allArticles: allArticles,
                        choiceArticleShow: {show: false, mealAttr: null, items: [], itemsLess: [], currentFamily: ""}
                    },
                    methods: {
                        itemDefaultChange: function (attr, item) {
                            for (var i in attr.mealItems) {
                                var m = attr.mealItems[i];
                                if (m != item) {
                                    m.isDefault = false;
                                }
                            }
                        },

                        updateAttrItems: function () {
//                            this.choiceArticleShow.mealAttr.mealItems = $.extend(true, {}, this.choiceArticleShow).items;
                            var items = $.extend(true, {}, this.choiceArticleShow).items;
                            this.articles = [];
                            for (var i = 0; i < items.length; i++) {
                                this.articles.push(items[i]);
                            }

                            $("#article-choice-dialog").modal('hide');
                        },
                        removeMealItem: function (item) {
                            this.unit.detailList.$remove(item);
//                            this.unit.detailList.$remove(attr);

//                            articleList.push(attr);

//                            this.choiceArticleShow.items = this.articles;

                        },
                        removeArticleItem: function (mealItem) {
                            this.choiceArticleShow.items.$remove(mealItem);
                            articleList.push(mealItem);
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
                                    articleList.$remove(art);
                                }
                            }

                            if (!this.choiceArticleShow.items.length) {
                                item.isDefault = true;
                            }
                            this.choiceArticleShow.items.push(item);
                        },
                        addItem: function () {
                            if(!this.unit.detailList){
                                this.unit.detailList = [{
                                    name : null,
                                    sort : null
                                }];
                            }else{
                                this.unit.detailList.push({
                                    name : null,
                                    sort : null
                                });
                            }


//
//                            if(attr.detailList){
//                                attr.detailList.push({
//                                    name: null,
//                                    spread: null,
//                                    sort: null
//                                });
//                            }else{
//                                attr.detailList = [{ name: null,
//                                    spread: null,
//                                    sort: null}];
//                            }



                        },
                        addMealItem: function (arr) {
//                            this.choiceArticleShow.show = true;
//                            this.choiceArticleShow.items = this.articles;
//                            this.choiceArticleShow.itemsLess = articleList;
                            var len = this.unit.familyList.length;
                            var family = {
                                no: len,
                                name: null,
                                sort: null,
                                type: null,
                                detailList: []
                            }

                            this.unit.familyList.push(family);
//                            this.$nextTick(function () {
//                                $("#article-choice-dialog").modal('show');
//                                var that = this;
//                                $("#article-choice-dialog").on('hidden.bs.modal', function () {
//                                    that.choiceArticleShow.show = false;
//                                });
//                            })
                        },

                        delMealAttr: function (meal) {
                            this.unit.familyList.$remove(meal);
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
                        selectAllTimes: function (m, e) {
                            var isCheck = $(e.target).is(":checked");
                            if (isCheck) {
                                for (var i = 0; i < this.supportTimes.length; i++) {
                                    var t = this.supportTimes[i];
                                    m.supportTimes.push(t.id);
                                }
                            } else {
                                m.supportTimes = [];
                            }
                        }
                        ,
                        create: function (article_type) {


//                            this.unit = null;

                            var unit = {
                                detailList : []
                            }




                            this.unit = unit;

                            action = "create";
                            unitId = null;
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


                        }
                        ,
                        uploadSuccess: function (url) {
                            console.log(url);
                            $("[name='photoSmall']").val(url).trigger("change");
                            C.simpleMsg("上传成功");
                            $("#photoSmall").attr("src", "/" + url);
                        }
                        ,
                        uploadError: function (msg) {
                            C.errorMsg(msg);
                        }
                        ,
                        edit: function (model) {
                            var that = this;
                            action = "edit";

                            unitId = model.id;
                            that.showform = true;
                            $.post("unit/getUnitById", {id: model.id}, function (result) {
                                $('#uName').val(result.name);
                                $('#uSort').val(result.sort);
                                var arr = [];
                                for (var i = 0; i < result.details.length; i++) {

                                    var detail = {
                                        id:result.details[i].id,
                                        no: i,
                                        name: result.details[i].name,
                                        sort: result.details[i].sort

                                    }
                                    arr.push(detail);
                                }
                                that.unit = null;
                                var unit = {
                                    detailList : arr
                                }
                                that.unit = unit;
                            });

                        }
                        ,
                        filterTable: function (e) {
                            var s = $(e.target);
                            var val = s.val();
                            if (val == "-1") {
                                tb.search("").draw();
                                return;
                            }
                            tb.search(val).draw();
                        }
                        ,
                        changeColor: function (val) {
                            $(".color-mini").minicolors("value", val);
                        }
                        ,

                        save: function (e) {
                            var that = this;
                            this.m.prices = this.unitPrices;
                            this.m.hasUnit = this.checkedUnit.join() || " ";
                            var m = this.m;

                            var data = {
                                id: unitId,
                                name : $('#uName').val(),
                                sort : $('#uSort').val()
                            };

                            data.details = this.unit.detailList;
                            
//                            if($('#id').val())
                            var jsonData = JSON.stringify(this.data);
                            var url;
                            if (action == "edit") {
                                url = "unit/modify";
                            } else {
                                url = "unit/create";
                            }

                            $.ajax({
                                contentType: "application/json",
                                type: "post",
                                url: url,
                                data: JSON.stringify(data),
                                success: function (result) {
                                    if (result.success) {
                                        that.showform = false;
                                        that.m = {};
                                        C.simpleMsg("保存成功");
                                        tb.ajax.reload(null, false);
                                    } else {
                                        C.errorMsg(result.message);
                                    }
                                },
                                error: function (xhr, msg, e) {
                                    var errorText = xhr.status + " " + xhr.statusText + ":" + action;
                                    C.errorMsg(errorText);
                                }
                            });
                        }
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


                        $.post("article/singo_article", null, function (data) {
                            that.articleList = data;
                            articleList = data;
                            all = data;
                        });
                        $.post("articlefamily/list_all", null, function (data) {
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
                })
                ;
        C.vue = vueObj;

    }());


</script>
