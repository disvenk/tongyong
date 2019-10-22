<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">

                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">所有品牌版本：</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label">版本号：</label>
                    <div class="col-sm-4">
                        <span id="banbenhao" >{{banbenhao}}</span>
                    </div>
                </div>

                <div class="col-sm-12 portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">品牌版本设置：</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" action="{{m.id?'versionBrand/modify':'versionBrand/create'}}"
                          @submit.prevent="save">
                        <div class="form-body">
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-4 control-label">请选择品牌：</label>
                                <div class="col-sm-4">
                                    <span class="form-control" v-if="isEdit" v-model="m.brandId">{{m.brandName}}</span>
                                    <select v-else class="form-control" name="brandId" v-model="brandId"
                                            :disabled="isEdit" @change="selectBrand(brandId)">
                                        <option v-for="brand in brands" :value="brand.id">
                                            {{brand.brandName}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-4 control-label">版本号：</label>
                                <div class="col-sm-4">
                                    <%--<select class="form-control" name="versionId" :value="m.versionId">--%>
                                    <select class="form-control" name="versionId" v-model="versionId">
                                        <option v-for="version in versions" :value="version.id">
                                            {{version.versionNo}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="portlet light ">
                            <div class="portlet-title">
                                <div class="caption">
                                    <span class="caption-subject bold font-blue-hoki">店铺版本设置：</span>
                                </div>
                            </div>
                            <%--<table id="table" name="versionPosShopDetails" class="table table-striped table-hover table-bordered"></table>--%>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>店铺名称</th>
                                    <th>版本号</th>
                                    <th>更改版本号</th>
                                </tr>
                                </thead>
                            </table>
                            <div style="overflow-y:auto;height: 200px;" v-if="selectData.length > 0">
                                <%--<table id="table" class="table table-striped" >--%>
                                <table id="table" style="width: 100%;">
                                    <tbody style="width: 100%;">
                                    <tr style="width: 100%;" v-for="(i,item) in versionShopData">
                                        <td style="width: 33%;">{{item.shopName}}</td>
                                        <td style="width: 33%;">{{item.versionNo}}</td>
                                        <td style="width: 33%;">
                                            <%--<select class="form-control" name="versionId" :value="item.versionId">--%>
                                            <%--<a v-if="!selectShow" @click="selectShow=true">{{item.versionNo}}</a>--%>
                                            <select class="form-control" name="versionId" v-model="selectData[i].value"
                                                    @change="selectChange($event,i,selectData[i].value)">
                                                <option v-for="version in versions" :value="version.id">
                                                    {{version.versionNo}}
                                                </option>
                                            </select>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <input type="hidden" name="id" v-model="m.id"/>
                        <input class="btn green" type="submit" value="保存"/>
                        <a class="btn default" @click="cancel">取消</a>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="table-div">
        <div class="table-operator">
            <s:hasPermission name="version/create">
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
    (function () {
        var cid = "#control";
        // var $table2=$("#table");
        var data = [{"shopName": "shodhwq", "versionNo": "122", "downloadAddress": "www.baidu,com"}];
        var vueObj = new Vue({
            el: "#control",
            data: {
                allData: [],
                typeNames: [{"id": "1", "value": "餐品图片"}, {"id": "2", "value": "展示的图片"}, {"id": "4", "value": "差评"}],
                brands: [],
                versions: [],
                isEdit: false,
                showform: false,
                tb: '',
                C: '',
                versionShopData: [],
                versionId: '',
                brandId: '',
                downloadAddress: '',
                shopVersionId: '',
                selectData: [],
                selectShow: false,
                banbenhao: null,

                //shopVersionId0:2,
                //shopVersionId1:1,
            },
            created: function () {
                var that = this;
                // that.initBrandMode();
                that.initVersionMode();
                this.createTb();
                this.C = new Controller(null, that.tb);

                //添加所有版本号
                $.ajax({
                    url: "versionBrand/selectTotalversion",
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        console.log(JSON.stringify(data.data.versionNo))
                        that.banbenhao = data.data.versionNo;
                    }
                })
            },
            //mixins:[C.formVueMix],
            methods: {
                // inputChange: function (index,val) {
                //     console.log('11111111',index,val)
                //     //versionShopData[index].versionNo =
                // },
                selectChange: function (e, i, val) {
                    console.log('eeeeeee', e, i, val)
                    this['shopVersionId' + i] = val
                },
                selectBrand: function (brandId) {
                    var that = this;
                    console.log(brandId)
                    $.ajax({
                        url: "versionBrand/list_ShopDetail",
                        data: {"brandId": brandId},
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            that.versionShopData = data;
                            that.versionShopData.map(function (v, i) {
                                var value = {
                                    value: 'shopVersionId' + i
                                };
                                that.selectData.push(value);
                                that['shopVersionId' + i] = that.versionShopData[i].versionId;
                            });
                        }
                    })
                },
                createTb: function () {
                    var that = this;
                    var $table = $(".table-body>table");
                    this.tb = $table.DataTable({
                        ajax: {
                            url: "versionBrand/list_all",
                            dataSrc: ""
                        },
                        columns: [
                            {
                                title: "品牌名称",
                                data: "brandName",
                            },
                            {
                                title: "版本号",
                                data: "versionNo",
                            },
                            {
                                title: "操作",
                                data: "id",
                                createdCell: function (td, tdData, rowData, row) {
                                    var operator = [
                                        <s:hasPermission name="versionBrand/delete">
                                        that.C.createDelBtn(tdData, "versionBrand/delete"),
                                        </s:hasPermission>
                                        <s:hasPermission name="versionBrand/modify">
                                        that.createEditBtn(rowData),
                                        </s:hasPermission>
                                    ];
                                    $(td).html(operator);
                                }
                            }],
                    });
                },
                createEditBtn: function (rowData) {
                    var that = this;
                    var button = $("<button class='btn btn-xs btn-primary'>编辑</button>");
                    button.click(function () {
                        that.selectData = [];
                        that.edit(rowData);
                        that.versionShopData = rowData.versionPosShopDetails;
                        that.versionShopData.map(function (v, i) {
                            var value = {
                                value: 'shopVersionId' + i
                            };
                            that.selectData.push(value);
                            that['shopVersionId' + i] = that.versionShopData[i].versionId;
                        });

                    });

                    return button;
                },
                initBrandMode: function () {
                    var that = this;
                    $.ajax({
                        url: "versionBrand/selectBrandList",
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            console.log('datadata', data)
                            that.brands = data.data;
                        }
                    })
                },
                initVersionMode: function () {
                    var that = this;
                    $.ajax({
                        url: "version/list_all",
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            that.versions = data;
                        }
                    })

                },
                edit: function (rowData) {
                    this.m = rowData;
                    this.versionId = this.m.versionId
                    console.log('this.brandId', this.m.brandId)
                    this.brandId = this.m.brandId
                    this.downloadAddress = this.m.downloadAddress
                    this.isEdit = true;
                    this.showform = true;
                },
                cancel: function () {
                    this.m = {};
                    this.showform = false;
                    this.selectShow = false;
                },
                create: function () {
                    this.initBrandMode();
                    this.m = {};
                    this.versionId = '';
                    this.brandId = '';
                    this.downloadAddress = '';
                    this.versionShopData = '';
                    this.selectData = [];
                    this.isEdit = false;
                    this.showform = true;
                },
                save: function (e) {
                    var that = this;
                    this.selectShow = false;
                    this.versionShopData.map(function (v, i) {
                        v.versionId = that['shopVersionId' + i]
                    });
                    this.m.versionPosShopDetails = this.versionShopData;
                    this.m.versionId = this.versionId;
                    this.m.brandId = this.brandId;
                    this.m.downloadAddress = this.downloadAddress;
                    console.log('4444444444', this.m)
                    var data = JSON.stringify(that.m)
                    if (that.m.id) {
                        $.ajax({
                            url: "versionBrand/modify",
                            data: data,
                            type: "post",
                            dataType: "json",
                            contentType: "application/json",
                            success: function () {
                                that.cancel();
                                that.tb.ajax.reload();
                                that.C.simpleMsg("保存成功");
                            },
                            error: function () {
                                that.C.errorMsg("保存失败");
                            }
                        })
                    } else {
                        $.ajax({
                            url: "versionBrand/create",
                            data: data,
                            type: "post",
                            dataType: "json",
                            contentType: "application/json",
                            success: function () {
                                that.cancel();
                                that.tb.ajax.reload();
                                that.C.simpleMsg("保存成功");
                            },
                            error: function () {
                                that.C.errorMsg("保存失败");
                            }
                        })
                    }

                    //  var formDom = e.target;
                    //  var showType = e.showType;
                    // this.C.ajaxFormEx(formDom,function(){
                    //      that.cancel();
                    //      that.tb.ajax.reload();
                    //  });
                },
            }
        });
        // C.vue=vueObj;

    }());

</script>
