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

<div class="table-div">
    <div class="table-operator">
        <s:hasPermission name="scmSupplerPrice/add">
            <button class="btn green pull-right" @click="create">新增分类</button>
        </s:hasPermission>
    </div>
    <div class="clearfix"></div>
    <div class="table-filter"></div>
    <div class="table-body">
        <table class="table table-striped table-hover table-bordered "></table>
    </div>
</div>


<script>
    (function () {
        var cid="#control";
        var $table = $(".table-body>table");

        var tb = $table.DataTable({
            ajax : {
                url : "scmCategory/list_all",
                dataSrc : "data",
            },
            columns : [
                {
                    title : "编号",
                    data : "category_code",
                },
                {
                    title : "分类名称",
                    data : "category_name",
                },
                {
                    title: "级别",
                    data: "category_hierarchy",
                },
                {
                    title : "排序",
                    data : "sort",
                },
                {
                    title : "备注",
                    data : "",
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
                            var items = $.extend(true, {}, this.choiceArticleShow).items;
                            this.articles = [];
                            for (var i = 0; i < items.length; i++) {
                                this.articles.push(items[i]);
                            }

                            $("#article-choice-dialog").modal('hide');
                        },
                        removeMealItem: function (item) {
                            this.unit.detailList.$remove(item);

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
                                articleId: art.id,
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

                        },
                        addMealItem: function (arr) {
                            var len = this.unit.familyList.length;
                            var family = {
                                no: len,
                                name: null,
                                sort: null,
                                type: null,
                                detailList: []
                            }

                            this.unit.familyList.push(family);

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
                            var unit = {
                                detailList : []
                            }

                            this.unit = unit;
                            action = "create";
                            unitId = null;
                            this.m = {
                                articleFamilyId: this.articlefamilys[0].id,
                                articleList: [],
                                isRemind: false,
                                activated: true,
                                showDesc: true,
                                showBig: true,
                                isEmpty: false,
                            };
                            this.checkedUnit = [];
                            this.showform = true;
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
