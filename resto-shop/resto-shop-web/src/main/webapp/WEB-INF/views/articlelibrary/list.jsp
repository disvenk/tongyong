<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
    .modal-body::-webkit-scrollbar {
        width: 6px;
    }
    .modal-body::-webkit-scrollbar-thumb {
        background-color: #DCDFE6;
    }
    .modal-body::-webkit-scrollbar-track {
        background-color: #FFFFFF;
    }

</style>

<div id="control">
    <h2 class="text-center"><strong>菜品管理</strong></h2><br/>
    <div class="row" id="searchTools">
        <div>
            <div>
                <div class="tab-content">
                    <div class="panel panel-success">
                        <div class="panel-body">
                            <table id="articleTable" class="table table-striped table-bordered table-hover"
                                   style="width: 100%;">
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 菜品弹窗 -->
    <div class="modal fade bs-example-modal-lg" id = "articleModal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" style="overflow: hidden;">
        <div class="modal-dialog modal-lg" role="document" style="width:64%;">
            <div class="modal-content">
                <!-- 头部 -->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="gridSystemModalLabel">
                        {{article.articleType == 1 ? '单品' : '套餐'}}
                    </h4>
                </div>
                <!-- 中部 -->
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">
                                菜品名称<span style="color: red">*</span>
                            </label>
                            <div class="col-sm-6">
                                <input type="text" disabled class="form-control" id="name" :value="article.name">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">
                                菜品显示<span style="color: red">*</span>
                            </label>
                            <div class="col-sm-6">
                                <label class="radio-inline">
                                    <input type="checkbox" v-bind:true-value="true" v-bind:false-value="false"
                                           v-model="article.showBig">大图
                                </label>
                                <label class="radio-inline">
                                    <input type="checkbox" v-bind:true-value="true" v-bind:false-value="false"
                                           v-model="article.showDesc">描述
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">
                                按钮颜色<span style="color: red">*</span>
                            </label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control color-mini" name="controlColor"
                                       data-position="bottom left" v-model="article.controlColor">
                            </div>
                            <div class="col-md-4">
                                <span class="btn dark" @click="changeColor('#000')">黑</span>
                                <span class="btn btn-default" @click="changeColor('#fff')">白</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">
                                上架沽清<span style="color: red">*</span>
                            </label>
                            <div class="col-sm-6">
                                <label class="radio-inline">
                                    <input type="checkbox" v-bind:true-value="true" v-bind:false-value="false"
                                           v-model="article.activated">上架
                                </label>

                                <label class="radio-inline" v-show="article.articleType != 2">
                                    <input type="checkbox" v-bind:true-value="true" v-bind:false-value="false"
                                           v-model="article.isEmpty">沽清
                                </label>

                                <label class="radio-inline">
                                    <input type="checkbox" v-bind:true-value="1" v-bind:false-value="0"
                                           v-model="article.isHidden">隐藏
                                </label>
                            </div>
                        </div>
                        <div class="form-group" v-show="article.articleType==1">
                            <label for="name" class="col-sm-2 control-label">
                                菜品未点提示<span style="color: red">*</span>
                            </label>
                            <div class="col-sm-6">
                                <label class="radio-inline">
                                    <input type="checkbox" v-bind:true-value="true" v-bind:false-value="false"
                                           v-model="article.isRemind">提示
                                </label>
                            </div>
                        </div>
                        <div class="form-group" v-show="article.articleType==1">
                            <label for="name" class="col-sm-2 control-label">
                                虚拟餐包<span style="color: red">*</span>
                            </label>
                            <div class="col-sm-4">
                                <select class="form-control" v-model="article.virtualId">
                                    <option value="-1">未选择虚拟餐包</option>
                                    <option :value="v.id" v-for="v in virtualList">
                                        {{v.name}}
                                    </option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">
                                出餐厨房<span style="color: red">*</span>
                            </label>
                            <div class="col-sm-6">
                                <label v-for="kitchen in kitchenList"  class="radio-inline">
                                    <input type="checkbox"  :name="kitchen.id" :value="kitchen.id"
                                           v-model="article.kitchenList"> {{kitchen.name}} &nbsp;&nbsp;
                                </label>
                            </div>
                        </div>
                        <!-- 套餐子品详情 -->
                        <div class="form-group" v-show="article.articleType==2">
                            <div class="portlet light bordered">
                                <div class="portlet-title">
                                    <div class="caption font-green-sharp">
                                        <i class="icon-speech font-green-sharp"></i>
                                        <span class="caption-subject bold uppercase"> 编辑套餐打印</span>
                                    </div>
                                </div>
                                <div class="portlet-body" style="height: 500px;overflow: auto;">
                                    <div class="portlet box blue-hoki" v-for="attr in article.mealAttrs | orderBy  'sort'">
                                        <div class="portlet-title">
                                            <div class="caption">
                                                <label class="control-label">&nbsp;</label>
                                                <div class="pull-right">
                                                    <input class="form-control" disabled type="text" v-model="attr.name"
                                                           required="required">
                                                </div>
                                            </div>
                                            <div class="caption">
                                                <label class="control-label col-md-4">排序&nbsp;</label>
                                                <div class="col-md-4">
                                                    <input class="form-control" disabled type="text" v-model="attr.sort"
                                                           required="required" lazy>
                                                </div>

                                            </div>

                                            <div class="caption">
                                                <label class="control-label col-md-4"
                                                       style="width:120px">打印排序&nbsp;</label>
                                                <div class="col-md-4">
                                                    <input class="form-control" disabled type="text" v-model="attr.printSort"
                                                           required="required" name="printSort" lazy
                                                           onblur="checkSort(this)">
                                                </div>

                                            </div>


                                            <div class="caption">
                                                <label class="control-label col-md-4"
                                                       style="width:120px">选择类型&nbsp;</label>
                                                <div class="col-md-4">
                                                    <select disabled class="form-control" style="width:150px"
                                                            name="choiceType" v-model="attr.choiceType">
                                                        <option value="0">必选</option>
                                                        <option value="1">任选</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="form-group col-md-12" v-if="attr.mealItems.length">
                                                <div class="flex-row">
                                                    <div class="flex-1">餐品原名</div>
                                                    <div class="flex-2">餐品名称</div>
                                                    <div class="flex-2">差价</div>
                                                    <div class="flex-1">排序</div>
                                                    <div class="flex-1">默认</div>
                                                    <div class="flex-1">指定厨房出单</div>
                                                </div>
                                                <div class="flex-row"
                                                     v-for="item in attr.mealItems | orderBy 'sort' ">
                                                    <div class="flex-1">
                                                        <p class="form-control-static">{{item.articleName}}</p>
                                                    </div>
                                                    <div class="flex-2">
                                                        <input type="text" disabled class="form-control" v-model="item.name"
                                                               required="required"/>
                                                    </div>
                                                    <div class="flex-2">
                                                        <input type="text" disabled class="form-control"
                                                               v-model="item.priceDif" required="required"/>
                                                    </div>
                                                    <div class="flex-1">
                                                        <input type="text" disabled class="form-control" v-model="item.sort"
                                                               required="required" lazy/>
                                                    </div>
                                                    <div class="flex-1 radio-list">
                                                        <label class="radio-inline">
                                                            <input type="checkbox" disabled :name="attr.name" :value="true"
                                                                   v-model="item.isDefault && attr.choiceType == 0"
                                                                   @change="itemDefaultChange(attr,item)"/>
                                                            设为默认
                                                        </label>
                                                    </div>
                                                    <div class="flex-1 radio-list">
                                                        <select class="form-control" name="kitchenId"
                                                                v-model="item.kitchenId">
                                                            <option value="-1">(选择厨房)</option>
                                                            <option :value="k.id" v-for="k in kitchenList">
                                                                {{k.name}}
                                                            </option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 套餐子品详情 -->
                    </form>
                </div>
                <!-- 尾部 -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" @click="save">保存</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="assets/customer/date.js" type="text/javascript"></script>
<script>
    var articleTableAPI = null;

    var vueObj = new Vue({
        el : "#control",
        data : {
            articleTable : {},
            article : {},
            virtualList : [],
            kitchenList: []
        },
        created : function() {
            var that = this;
            this.initDataTables();
            this.searchInfo();
            //初始化颜色控件
            $('.color-mini').minicolors({
                change: function (hex, opacity) {
                    if (!hex) return;
                    if (typeof console === 'object') {
                        $(this).attr("value", hex);
                    }
                },
                theme: 'bootstrap'
            });
            //加载虚拟餐包
            $.post("virtual/listAll", function (data) {
                that.virtualList = data;
            });
            //加载厨房列表
            $.post("kitchen/list_allStatus", function (data) {
                that.kitchenList = data;
            });
        },
        methods : {
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                //启用datatable对象
                that.articleTable=$("#articleTable").DataTable({
                    lengthMenu: [ [20, 75, 100, -1], [20, 75, 100, "All"] ],
                    order: [[ 0, "asc" ]],
                    columns : [
                        {
                            title : "菜品分类",
                            data : "articleFamilyName",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "菜品类型",
                            data : "articleTypeName",
                            orderable : false,
                            s_filter: true
                        },
                        {
                            title : "菜品名称",
                            data : "name",
                            orderable : false
                        },
                        {
                            title : "菜品图片",
                            data : "photoSmall",
                            orderable : false,
                            createdCell: function (td, tdData) {
                                if(tdData !=null && tdData.substring(0,4)=="http"){
                                    $(td).html("<img src=\"" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:40px;width:80px;\"/>");
                                }else{
                                    $(td).html("<img src=\"/" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:40px;width:80px;\"/>");
                                }
                            }
                        },
                        {
                            title : "菜品售价",
                            data : "price"
                        },
                        {
                            title : "粉丝价",
                            data : "fansPrice"
                        },
                        {
                            title : "排序",
                            data : "sort"
                        },
                        {
                            title : "上架",
                            data : "activatedName",
                            s_filter: true,
                            orderable : false
                        },
                        {
                            title : "操作",
                            data : "articleId",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var button = $("<button class='btn btn-xs btn-primary'>编辑</button>");
                                $(button).click(function () {
                                    that.openArticleModal(tdData);
                                });
                                $(td).html(button);
                            }
                        }
                    ],
                    initComplete: function () {
                        articleTableAPI = this.api();
                        that.articleTableStructure();
                    }
                });
            },
            searchInfo : function() {
                var that = this;
                toastr.clear();
                toastr.success("查询中...");
                try{
                    var articleAPI = articleTableAPI;
                    $.post("articlelibrary/list_all", function(result) {
                        if(result.success == true){
                            toastr.clear();
                            toastr.success("查询成功");
                            articleAPI.search('');
                            var articleFamilyName = articleAPI.column(0);
                            articleFamilyName.search('', true, false);
                            var articleTypeName = articleAPI.column(1);
                            articleTypeName.search('', true, false);
                            var activatedName = articleAPI.column(7);
                            activatedName.search('', true, false);
                            that.articleTable.clear();
                            that.articleTable.rows.add(result.data).draw();
                            //重绘搜索列
                            that.articleTableStructure();
                        }else{
                            toastr.clear();
                            toastr.error("查询失败");
                        }
                    });
                }catch(e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
            },
            save: function () {
                toastr.info("保存中");
                var jsonData = JSON.stringify(this.article);
                $.ajax({
                    contentType: "application/json",
                    type: "post",
                    url: "article/save",
                    data: jsonData,
                    success: function (result) {
                        if (result.success) {
                            toastr.success("保存成功");
                            $("#articleModal").modal("hide");
                        } else {
                            toastr.error("保存失败");
                        }
                    }
                });
            },
            openArticleModal : function(articleId){
                var that = this;
                $.post("articlelibrary/selectArticleByArticleId", {articleId: articleId}, function (result) {
                    var article = result.data;
                    that.article = article;
                    if (!article.virtualId) {
                        that.article.virtualId = "-1";
                    }
                    $('.color-mini').minicolors("value", article.controlColor);
                    $("#articleModal").modal();
                });
            },
            changeColor: function (val) {
                $(".color-mini").minicolors("value", val);
            },
            articleTableStructure : function(){
                var api = articleTableAPI;
                var columnsSetting = api.settings()[0].oInit.columns;
                $(columnsSetting).each(function (i) {
                    if (this.s_filter) {
                        var column = api.column(i);
                        var select = $('<select id=""><option value="">' + this.title + '(全部)</option></select>');
                        column.data().unique().each(function (d) {
                            select.append('<option value="' + d + '">' + d + '</option>')
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
        }
    });
</script>

