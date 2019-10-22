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
                    <h4 class="modal-title">重量属性</h4>
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
                                            </div>
                                            <div class="portlet-body">
                                                <div class="form-group col-md-12" v-for="item in unit.detailList">
                                                    <div class="flex-row" style="text-align: center">
                                                        <div class="flex-2">重量(斤)</div>
                                                        <div class="flex-2">名称</div>
                                                        <div class="flex-2">排序</div>
                                                        <div class="flex-2">移除</div>
                                                    </div>
                                                    <div class="flex-row" style="text-align: center">
                                                        <div class="flex-2">
                                                            <input type="text" class="form-control"
                                                                   v-model="item.weight" name="weight"
                                                                   required/>
                                                        </div>
                                                        <div class="flex-2">
                                                            <input type="text" class="form-control"
                                                                   v-model="item.name" name="name"
                                                                   required/>
                                                        </div>
                                                        <div class="flex-2">
                                                            <input type="text" class="form-control" name="sort"
                                                                   v-model="item.sort"
                                                                   required lazy/>
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
                                                        添加重量
                                                    </button>
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>
                                        </div>
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
            <s:hasPermission name="weightPackage/add">
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
        var action;
        var unitId = null;

        var $table = $(".table-body>table");
        var articleList = [];
        var all = [];


        var tb = $table.DataTable({
            ajax: {
                url: "weightPackage/list_all",
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
                            <s:hasPermission name="weightPackage/delete">
                            C.createDelBtn(tdData, "weightPackage/delete"),
                            </s:hasPermission>
                            <s:hasPermission name="weightPackage/edit">
                            C.createEditBtn(rowData),
                            </s:hasPermission>
                        ];
                        $(td).html(operator);
                    }
                }]
        });

        var C = new Controller(null, tb);
        var vueObj = new Vue({
                    el: "#control",
                    mixins: [C.formVueMix],
                    data: {
                        checkedUnit: [],
                        articles: [],
                        unit: new HashMap(),
                        articleunits: {},
                        unitPrices: [],
                        articleList: [],
                        choiceArticleShow: {show: false, mealAttr: null, items: [], itemsLess: [], currentFamily: ""},
                        btnflag: true,
                    },
                    methods: {
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
                        addItem: function () {
                            if(!this.unit.detailList){
                                this.unit.detailList = [{
                                    name : null,
                                    weight : null,
                                    sort : null
                                }];
                            }else{
                                this.unit.detailList.push({
                                    name : null,
                                    weight : null,
                                    sort : null
                                });
                            }

                        },
                        create: function (article_type) {
                            var unit = {
                                detailList : []
                            }
                            this.unit = unit;

                            action = "create";
                            unitId = null;
                            this.m = {
                                articleList: [],
                            };
                            this.checkedUnit = [];
                            this.showform = true;
                            this.articles = [];
                            articleList = all.concat();
                        },
                        edit: function (model) {
                            var that = this;
                            action = "edit";

                            unitId = model.id;
                            that.showform = true;
                            $.post("weightPackage/getWeightPackageById", {id: model.id}, function (result) {
                                $('#uName').val(result.name);
                                $('#uSort').val(result.sort);
                                var arr = [];
                                for (var i = 0; i < result.details.length; i++) {
                                    var detail = {
                                        id:result.details[i].id,
                                        no: i,
                                        name: result.details[i].name,
                                        weight: result.details[i].weight,
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

                        },
                        save: function (e) {
                            var that = this;
                            if(!that.btnflag){
                                return false;
                            }
                            that.btnflag = false;
                            setTimeout(function () {
                                that.btnflag = true;
                            },4000);
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
                                url = "weightPackage/modify";
                            } else {
                                url = "weightPackage/create";
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
                created: function () {
                    tb.search("").draw();
                    var that = this;
                    this.$watch("showform", function () {
                        if (this.showform) {
                            $("#article-dialog").modal("show");
                            $("#article-dialog").on("hidden.bs.modal", function () {
                                that.showform = false;
                            });
                        } else {
                            $("#article-dialog").modal("hide");
                            $(".modal-backdrop.fade.in").remove();
                        }
                    });
                }
                })
                ;
        C.vue = vueObj;

    }());


</script>
