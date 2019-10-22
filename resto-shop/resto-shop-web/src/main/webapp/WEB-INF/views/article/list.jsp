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
</style>
<div>
    <%--<div class="row form-div"  id="printerModel" style="display: none;z-index: 1000000">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">选择厨房</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <div class="form-group">
                        <div >
                            <ul id="treeDemo" class="ztree"></ul>
                        </div>
                    </div>
                    <div class="text-center">
                        <a class="btn green"  id="submitKitchen" >保存</a>
                        <a class="btn default" id="cancelKitchen" >取消</a>
                    </div>
                </div>
            </div>
        </div>
    </div>--%>
<div id="control">

    <div class="modal fade" id="article-dialog" v-if="showform" @click="cleanRemark">
        <div class="modal-dialog " style="width:90%;">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">表单</h4>
                </div>
                <form class="form-horizontal" role="form " action="article/save" @submit.prevent="save">
                    <div class="modal-body auto-height">
                        <div class="form-body">
                            <div class="dish-setting cf">
                                <h3 class="title">菜品基础设置</h3>
                                <div class="dish-setting-left">
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">餐品类别</label>
                                        <div class="col-md-7">
                                            <select class="form-control" name="articleFamilyId" v-model="m.articleFamilyId"
                                                    required="required">
                                                <option :value="f.id" v-for="f in articlefamilys">
                                                    {{f.name}}
                                                </option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">餐品名称</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" name="name" v-model="m.name"
                                                   required="required">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">价格</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" name="price" v-model="m.price"
                                                   required="required" pattern="\d{1,10}(\.\d{1,2})?$"
                                                   title="价格只能输入数字,且只能保存两位小数！">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">餐品单位</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" name="unit" v-model="m.unit"
                                                   required="required">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">排序</label>
                                        <div class="col-md-7">
                                            <input type="number" class="form-control" name="sort" v-model="m.sort">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">餐盒数量</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" name="mealFeeNumber" v-model="m.mealFeeNumber">
                                        </div>
                                    </div>
                                </div>
                                <div class="dish-setting-right">
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">第三方外卖名称</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" name="elemeName" v-model="m.elemeName"
                                            >
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">粉丝价</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" name="fansPrice" v-model="m.fansPrice">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">称斤价格(单位：元/斤)</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" name="cattyMoney" v-model="m.cattyMoney">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">餐品编号</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" name="peference" v-model="m.peference">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">描述</label>
                                        <div class="col-md-7">
                                    <textarea rows="3" class="form-control" name="description"
                                              v-model="m.description"></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="dish-setting cf">
                                <h3 class="title">图片设置</h3>
                                <div class="cf">
                                    <div class="form-group " style="float: left;width: 50%;">
                                        <label class="col-md-5 control-label">显示</label>
                                        <div class="col-md-7 radio-list">
                                            <label class="radio-inline">
                                                <input type="checkbox" v-bind:true-value="true" v-bind:false-value="false"
                                                       v-model="m.showBig">大图
                                            </label>
                                            <label class="radio-inline">
                                                <input type="checkbox" v-bind:true-value="true" v-bind:false-value="false"
                                                       v-model="m.showDesc">描述
                                            </label>
                                        </div>
                                    </div>
                                    <div class="form-group " style="float: left;width: 50%;">
                                        <label class="col-md-5 control-label">按钮颜色</label>
                                        <div class="col-md-2">
                                            <input type="text" class="form-control color-mini" name="controlColor"
                                                   data-position="bottom left" v-model="m.controlColor">
                                        </div>
                                        <div class="col-md-5">
                                            <span class="btn dark" @click="changeColor('#000')">黑</span>
                                            <span class="btn btn-default" @click="changeColor('#fff')">白</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group ">
                                    <label class="control-label col-md-5" style="width: 20%;">菜品图片显示类型</label>
                                    <div  class="col-md-7">
                                        <label class="radio-inline">
                                            <input type="radio" name="photoType" v-model="m.photoType" value="1" > 大图
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" name="photoType" v-model="m.photoType" value="2"> 小图
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" name="photoType" v-model="m.photoType" value="3"> 无图
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" name="photoType" v-model="m.photoType" value="4"> 超大图
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" name="photoType" v-model="m.photoType" value="5"> 正方形图
                                        </label>
                                    </div>
                                </div>

                                <div class="cf">
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label" style="width: 20%;">gif动图</label>
                                        <div class="col-md-7">
                                            <input type="hidden" name="gifUrl" v-model="m.gifUrl">
                                            <img-file-upload cut="false" class="form-control" @success="uploadSuccessGif"
                                                             @error="uploadError"></img-file-upload>
                                            <img v-if="m.gifUrl" :src="m.gifUrl" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4" v-if="m.photoType == 1">
                                        <label class="col-md-5 control-label" style="width: 20%;">餐品图片(大图)</label>
                                        <div class="col-md-7">
                                            <input type="hidden" name="photoSmall" v-model="m.photoSmall">
                                            <img-file-upload class="form-control" @success="uploadSuccess"
                                                             @error="uploadError"></img-file-upload>
                                            <img v-if="m.photoSmall" :src="m.photoSmall" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
                                        </div>
                                    </div>

                                    <div class="form-group col-md-4" v-if="m.photoType == 2">
                                        <label class="col-md-5 control-label" style="width: 20%;">餐品图片(小图)</label>
                                        <div class="col-md-7">
                                            <input type="hidden" name="photoLittle" v-model="m.photoLittle">
                                            <img-file-upload class="form-control" @success="uploadSuccessLittle"
                                                             @error="uploadError"></img-file-upload>
                                            <img v-if="m.photoLittle" :src="m.photoLittle" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
                                        </div>
                                    </div>

                                    <div class="form-group col-md-4" v-if="m.photoType == 3">
                                        <label class="col-md-5 control-label" style="width: 20%;">餐品图片(无图)</label>
                                        <div class="col-md-7">无图</div>
                                    </div>

                                    <div class="form-group col-md-4" v-if="m.photoType == 4">
                                        <label class="col-md-5 control-label" style="width: 20%;">餐品图片(超大图)</label>
                                        <div class="col-md-7">
                                            <input type="hidden" name="photoSuper" v-model="m.photoSuper">
                                            <img-file-upload cut="false" class="form-control" @success="uploadSuccessSuper"
                                                             @error="uploadError"></img-file-upload>
                                            <img v-if="m.photoSuper" :src="m.photoSuper" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
                                        </div>
                                    </div>

                                    <div class="form-group col-md-4" v-if="m.photoType == 5">
                                        <label class="col-md-5 control-label" style="width: 20%;">餐品图片(正方形图)</label>
                                        <div class="col-md-7">
                                            <input type="hidden" name="photoSquareOriginal" v-model="m.photoSquareOriginal">
                                            <img-file-upload cut="false" class="form-control" @success="uploadSuccessSquareOriginal"
                                                             @error="uploadError"></img-file-upload>
                                            <img v-if="m.photoSquareOriginal" :src="m.photoSquareOriginal" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="dish-setting cf">
                                <h3 class="title">功能设置</h3>
                                <div class="form-group col-md-12">
                                    <label class="col-md-2 control-label">上架沽清</label>
                                    <div class="col-md-7 radio-list">
                                        <label class="radio-inline">
                                            <input type="checkbox" v-bind:true-value="true" v-bind:false-value="false"
                                                   v-model="m.activated">上架
                                        </label>

                                        <label class="radio-inline" v-if="m.articleType != 2">
                                            <input type="checkbox" v-bind:true-value="true" v-bind:false-value="false"
                                                   v-model="m.isEmpty">沽清
                                        </label>

                                        <label class="radio-inline">
                                            <input type="checkbox" v-bind:true-value="1" v-bind:false-value="0"
                                                   v-model="m.isHidden">隐藏
                                        </label>

                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-2 control-label">开启称斤买卖</label>
                                    <div class="col-md-7 radio-list">
                                        <label class="radio-inline">
                                            <input type="checkbox" v-bind:true-value="1" v-bind:false-value="0"
                                                   v-model="m.openCatty">开启
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-2 control-label">未点提示</label>
                                    <div class="col-md-7 radio-list">
                                        <label class="radio-inline" v-if="m.articleType==1">
                                            <input type="checkbox" v-bind:true-value="true" v-bind:false-value="false"
                                                   v-model="m.isRemind">提示
                                        </label>
                                        <label class="radio-inline" v-else>
                                            <input v-else type="checkbox" value="false" v-model="m.isRemind" disabled>提示
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-2 control-label">开启提醒(预点餐)</label>
                                    <div class="col-md-7 radio-list">
                                        <label class="radio-inline">
                                            <input type="checkbox" v-bind:true-value="1" v-bind:false-value="0"
                                                   v-model="m.needRemind">提示
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-2 control-label">标记为主菜</label>
                                    <div class="col-md-7 radio-list">
                                        <label class="radio-inline">
                                            <input type="checkbox" v-bind:true-value="1" v-bind:false-value="0"
                                                   v-model="m.isMainFood">是
                                        </label>
                                    </div>
                                </div>
                                <div class="row" v-if="m.articleType!=2">
                                    <div class="form-group  col-md-12">
                                        <label class="col-sm-2 control-label ">库存</label>
                                        <div class="col-md-8">
                                            <div class="form-group col-sm-4">
                                                <div class="input-group">
                                                    <div class="input-group-addon">工作日</div>
                                                    <input name="stockWorkingDay"
                                                           class="form-control" v-model="m.stockWorkingDay"
                                                           id="stockWorkingDay"/>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-4 ">
                                                <div class="input-group">
                                                    <div class="input-group-addon">假期</div>
                                                    <input name="stockWeekend"
                                                           class="form-control" v-model="m.stockWeekend"
                                                           id="stockWeekend"/>
                                                </div>
                                            </div>
                                            <%--<div class="form-group col-sm-4 ">
                                                <div class="input-group">
                                                    <div class="input-group-addon">菜品预警库存</div>
                                                    <input name="inventoryWarning"
                                                           class="form-control" v-model="m.inventoryWarning"
                                                           id="inventoryWarning"/>
                                                </div>
                                            </div>--%>
                                        </div>
                                    </div>
                                    <div class="form-group  col-md-12">
                                        <label class="col-sm-2 control-label ">菜品预警库存</label>
                                        <div class="col-md-8">
                                            <div class="form-group col-sm-4">
                                                <div class="input-group">
                                                    <input name="inventoryWarning" class="form-control" v-model="m.inventoryWarning" id="inventoryWarning"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group  col-md-12">
                                    <label class="col-md-2 text-right">供应时间</label>
                                    <div class="col-md-8">
                                        <label v-for="time in supportTimes">
                                            <input type="checkbox" name="supportTimes" :value="time.id"
                                                   v-model="m.supportTimes"> <span :class="{'text-danger':time.shopName}" >{{time.name}}({{time.discount+'%'}})</span> &nbsp;&nbsp;
                                        </label>
                                        <%--<label v-if="supportTimes.length>0">--%>
                                        <%--<input type="checkbox" @change="selectAllTimes(m,$event)"/> 全选--%>
                                        <%--</label>--%>
                                        <div id="supportTimeRemark" style="color: red;"></div>
                                    </div>
                                </div>
                                <div class="form-group col-md-12" v-if="enableDuodongxian">
                                    <label class="col-md-2 text-right">出餐设置</label>
                                    <div class="col-md-8">
                                        <div class="article-attr">
                                            <span class="article-units">
										        <label v-for="item in kitchenGroup">
                                                    <input type="checkbox" :checked="aa(item)"  @click='checkedOne(item)'> {{item.name}}
                                                </label>
									        </span>
                                        </div>
                                    </div>
                                </div>
                                <%--<div class="form-group  col-md-12" v-if="enableDuodongxian">--%>
                                    <%--<label class="col-md-2 text-right">出餐设置</label>--%>
                                    <%--<div class="col-md-8">--%>
                                        <%--<label class="col-md-4">--%>
                                            <%--<a @click="addKitchen">添加厨房</a>--%>
                                            <%--<span >已选{{selectData.length}}个厨房</span>--%>
                                        <%--</label>--%>
                                        <%--<label >--%>
                                            <%--<div v-for="item in selectData"  class="printerClass">--%>
                                                <%--<span>【{{item.kitchenName}}】</span>--%>
                                                <%--<span>{{item.name}}</span>--%>
                                                <%--<span style="color:red; padding: 0 5px;" @click="deletePrinter(item)">X</span>--%>
                                            <%--</div>--%>
                                        <%--</label>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <div class="form-group col-md-12" v-if="!enableDuodongxian">
                                    <label class="col-md-2 text-right">出餐厨房</label>
                                    <div class="col-md-8">
                                        <label v-for="kitchen in kitchenList">
                                            <input type="checkbox" name="kitchenList" :value="kitchen.id"
                                                   v-model="m.kitchenList"> {{kitchen.name}} &nbsp;&nbsp;
                                        </label>
                                        <div id="kitchenRemark" style="color: red;"></div>
                                    </div>
                                </div>
                                <div class="form-group col-md-12" v-if="m.articleType==1">
                                    <label class="col-md-2 text-right">推荐餐品包</label>
                                    <div class="col-md-10">
                                        <select name="recommendId" v-model="m.recommendId">
                                            <option value="">未选择餐品包</option>
                                            <option :value="f.id" v-for="f in recommendList">
                                                {{f.name}}
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-12" v-if="m.articleType==1">
                                    <label class="col-md-2 text-right">虚拟餐品包</label>
                                    <div class="col-md-10">
                                        <select name="virtualId" v-model="m.virtualId">
                                            <option value="-1">未选择餐品包</option>
                                            <option :value="v.id" v-for="v in virtualList">
                                                {{v.name}}
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-6" v-if="shop.posVersion == 1 && shop.allowAfterPay == 0">
                                    <label class="col-md-5 control-label">是否是称重菜品</label>
                                    <div class="col-md-7 radio-list">
                                        <label class="radio-inline">
                                            <input type="checkbox" v-bind:true-value="1" v-bind:false-value="0"
                                                   v-model="m.openCatty">开启
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group col-md-10" v-if="m.openCatty==1">
                                    <label class="col-md-2 text-right">重量包</label>
                                    <div class="col-md-10">
                                        <select name="weightPackageId" v-model="m.weightPackageId">
                                            <option value="">未选择重量包</option>
                                            <option :value="f.id" v-for="f in weightPackageList">
                                                {{f.name}}
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-10" v-if="m.articleType==1">
                                    <label class="col-md-3 text-right">规格属性（新）<label style="color:red">注：与旧版规格只能使用一个</label></label>
                                    <div class="col-md-9">
                                        <%--<div class="article-attr" v-for="attr in unitList">--%>
                                        <%--<label class="article-attr-label">{{attr.name}}:</label>--%>
                                        <span class="article-units">
										<label v-for="attr in unitList">
                                            <input type="checkbox"
                                                   v-model="attr.isUsed" v-bind:true-value="1" v-bind:false-value="0"
                                                   id="{{attr.id}}" @click="clickUnit(attr)">
                                            {{attr.name}}
                                        </label>
									</span>
                                        <%--</div>--%>
                                    </div>
                                </div>
                                <div class="form-group col-md-10" v-for="select in selectedUnit.unitList">
                                    <div class="col-md-10">
                                        <label class="col-md-2 ">规格属性: {{select.name}}</label>
                                        <label class="col-md-2 ">是否单选: <input type="checkbox" v-bind:true-value="0"
                                                                              v-bind:false-value="1"
                                                                              v-model="select.choiceType"></label>
                                        <%--<div class="col-md-7 radio-list">--%>


                                        <%--</div>--%>

                                    </div>
                                    <%--<div class="col-md-10">--%>


                                    <%--&lt;%&ndash;<label for="choice{{select.id}}">是否单选: </label>&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;<input type="checkbox" id="choice{{select.id}}"&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;v-model="select.choiceType" v-bind:true-value="0" v-bind:false-value="1"/>&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;</span>&ndash;%&gt;--%>
                                    <%--</div>--%>

                                    <div class="col-md-10">
                                        <div class="flex-row">
                                            <div class="flex-1 text-right">规格</div>
                                            <div class="flex-1">差价</div>
                                            <div class="flex-1">排序</div>
                                            <div class="flex-1">是否启用</div>

                                        </div>
                                        <div class="flex-row " v-for="detail in select.details">
                                            <label class="flex-1 control-label">{{detail.name}}</label>
                                            <div class="flex-1">
                                                <input type="text" class="form-control"
                                                       v-model="detail.price" :value="detail.price==null?0:detail.price" id="price{{detail.id}}" required="required"/>
                                            </div>
                                            <div class="flex-1">
                                                <input type="text" class="form-control" name="sort"
                                                       v-model="detail.sort" id="sort{{detail.id}}" required="required"
                                                />
                                            </div>
                                            <div class="flex-1">
                                                <input type="checkbox" v-bind:true-value="1" v-bind:false-value="0"
                                                       @click="changeUsed(select,detail)" v-model="detail.isUsed"
                                                       style="width:70px;height:30px">
                                                <%--<input type="radio" required name="type{{detail.id}}"--%>
                                                <%--checked v-model="detail.isUsed"     @click="changeUsed(select,detail,1)"> 是--%>
                                                <%--<input type="radio" required name="type{{detail.id}}"--%>
                                                <%--v-model="detail.isUsed"   @click="changeUsed(select,detail,0)"> 否--%>

                                                <%--<input type="checkbox" id="{{detail.id}}" checked="detail.isUsed == 1" v-model="detail.isUsed"--%>
                                                <%--@click="changeUsed(select,detail)" style="width:70px;height:30px">--%>
                                            </div>


                                        </div>
                                    </div>
                                </div>
                                <div class="form-group col-md-10" v-if="m.articleType==1">
                                    <label class="col-md-2 text-right">餐品规格</label>
                                    <div class="col-md-10">
                                        <div class="article-attr" v-for="attr in articleattrs" v-if="attr.articleUnits">
                                            <label class="article-attr-label">{{attr.name}}:</label>
                                            <span class="article-units">
										<label v-for="unit in attr.articleUnits">
                                            <input type="checkbox" :value="unit.id" v-model="checkedUnit"> {{unit.name}}
                                        </label>
									</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group col-md-10" v-if="allUnitPrice.length">
                                    <label class="col-md-2 control-label">规格价格</label>
                                    <div class="col-md-10">
                                        <div class="flex-row">
                                            <div class="flex-1 text-right">规格</div>
                                            <div class="flex-2">价格</div>
                                            <div class="flex-2">粉丝价</div>
                                            <div class="flex-2">编号</div>
                                            <div class="flex-2">工作日库存</div>
                                            <div class="flex-2">周末库存</div>
                                        </div>
                                        <div class="flex-row " v-for="u in unitPrices">
                                            <label class="flex-1 control-label">{{u.name}}</label>
                                            <div class="flex-2">
                                                <input type="hidden" name="unitNames" :value="u.name"/>
                                                <input type="hidden" name="unit_ids" :value="u.unitIds"/>
                                                <input type="text" class="form-control" name="unitPrices"
                                                       required="required" :value="u.price" v-model="u.price"/>
                                            </div>
                                            <div class="flex-2">
                                                <input type="text" class="form-control" name="unitFansPrices"
                                                       v-model="u.fansPrice"/>
                                            </div>
                                            <div class="flex-2">
                                                <input type="text" class="form-control" name="unitPeferences"
                                                       v-model="u.peference"/>
                                            </div>
                                            <div class="flex-2">
                                                <input type="text" class="form-control" name="stockWorkingDay"
                                                       id="workingDay" v-model="u.stockWorkingDay"
                                                       onchange="changeStockWeekend()"/>
                                            </div>
                                            <div class="flex-2">
                                                <input type="text" class="form-control" name="stockWeekend"
                                                       v-model="u.stockWeekend" onchange="changeStockWeekend()"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <%--<div class="form-group col-md-10" v-if="m.articleType==1">--%>
                            <%--<label class="col-md-2 text-right">选择规格包<label style="color: red">(将会覆盖原有规格)</label></label>--%>
                            <%--<div class="col-md-10">--%>
                            <%--<select  name="unitId"  v-model="m.unitId" >--%>
                            <%--<option value="selected">未选择规格包</option>--%>
                            <%--<option :value="f.id" v-for="f in unitList">--%>
                            <%--{{f.name}}--%>
                            <%--</option>--%>
                            <%--</select>--%>
                            <%--</div>--%>
                            <%--</div>--%>

                            <div class="col-md-12" v-if="m.articleType==2">
                                <div class="portlet light bordered">
                                    <div class="portlet-title">
                                        <div class="caption font-green-sharp">
                                            <i class="icon-speech font-green-sharp"></i>
                                            <span class="caption-subject bold uppercase"> 编辑套餐</span>
                                        </div>
                                        <div class="actions">
                                            <select class="form-control" @change="choiceMealTemp" v-model="choiceTemp">
                                                <option value="">不选择模板</option>
                                                <option :value="meal.id" v-for="meal in mealtempList">{{meal.name}}
                                                </option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="portlet-body">
                                        <div class="portlet box blue-hoki"
                                             v-for="attr in m.mealAttrs | orderBy  'sort'">
                                            <div class="portlet-title">
                                                <div class="caption">
                                                    <label class="control-label">&nbsp;</label>
                                                    <div class="pull-right">
                                                        <input class="form-control" type="text" v-model="attr.name"
                                                               required="required">
                                                    </div>
                                                </div>
                                                <div class="caption">
                                                    <label class="control-label col-md-4">排序&nbsp;</label>
                                                    <div class="col-md-4">
                                                        <input class="form-control" type="text" v-model="attr.sort"
                                                               required="required" lazy>
                                                    </div>

                                                </div>

                                                <div class="caption">
                                                    <label class="control-label col-md-4"
                                                           style="width:120px">打印排序&nbsp;</label>
                                                    <div class="col-md-4">
                                                        <input class="form-control" type="text" v-model="attr.printSort"
                                                               required="required" name="printSort" lazy
                                                               onblur="checkSort(this)">
                                                    </div>

                                                </div>


                                                <div class="caption">
                                                    <label class="control-label col-md-4"
                                                           style="width:120px">选择类型&nbsp;</label>
                                                    <div class="col-md-4">
                                                        <select class="form-control" style="width:150px"
                                                                name="choiceType" v-model="attr.choiceType"
                                                                v-on:change="choiceTypeChange(attr)">
                                                            <option value="0">必选</option>
                                                            <option value="1">任选</option>
                                                        </select>


                                                    </div>
                                                    <div class="col-md-4" v-if="attr.choiceType == 0">
                                                        <input class="form-control" type="text"
                                                               v-model="attr.choiceCount"
                                                               required="required">
                                                    </div>
                                                </div>

                                                <div class="tools">
                                                    <a href="javascript:;" class="remove"
                                                       @click="delMealAttr(attr)"></a>
                                                </div>
                                            </div>
                                            <div class="portlet-body">
                                                <div class="form-group col-md-12" v-if="attr.mealItems.length">
                                                    <div class="flex-row">
                                                        <div class="flex-1">餐品原名</div>
                                                        <div class="flex-2">餐品名称</div>
                                                        <div class="flex-2">差价</div>
                                                        <div class="flex-1">排序</div>
                                                        <div class="flex-1">默认</div>
                                                        <div class="flex-1">指定厨房出单</div>
                                                        <div class="flex-1">移除</div>
                                                    </div>
                                                    <div class="flex-row"
                                                         v-for="item in attr.mealItems | orderBy 'sort' ">
                                                        <div class="flex-1">
                                                            <p class="form-control-static">{{item.articleName}}</p>
                                                        </div>
                                                        <div class="flex-2">
                                                            <input type="text" class="form-control" v-model="item.name"
                                                                   required="required"/>
                                                        </div>
                                                        <div class="flex-2">
                                                            <input type="text" class="form-control"
                                                                   v-model="item.priceDif" required="required"/>
                                                        </div>
                                                        <div class="flex-1">
                                                            <input type="text" class="form-control" v-model="item.sort"
                                                                   required="required" lazy/>
                                                        </div>
                                                        <div class="flex-1 radio-list">
                                                            <label class="radio-inline">
                                                                <input type="checkbox" :name="attr.name" :value="true"  v-bind:disabled="attr.choiceType == 1"
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
                                                        <div class="flex-1">
                                                            <button class="btn red" type="button"
                                                                    @click="removeMealItem(attr,item)">移除
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-4 col-md-offset-8">
                                                    <button class="btn btn-block blue" type="button"
                                                            @click="addMealItem(attr)"><i class="fa fa-cutlery"></i>
                                                        添加{{attr.name}}
                                                    </button>
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>
                                        </div>
                                        <div class="col-md-4 col-md-offset-4">
                                            <button class="btn btn-block blue" type="button" @click="addMealAttr">
                                                <i class="fa fa-plus"></i>
                                                添加套餐属性
                                            </button>
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
                        <button type="submit" class="btn btn-primary" :disabled="!canSave">保存</button>
                    </div>
                </form>

            </div>
        </div>
    </div>

    <div class="modal fade" id="article-choice-dialog" v-if="showform&&choiceArticleShow.show">
        <div class="modal-dialog " style="width:90%;">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">添加 {{choiceArticleShow.mealAttr.name}} 菜品项</h4>
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
                                <tr v-for="art in choiceArticleCanChoice">
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
            <s:hasPermission name="article/add">
                <button class="btn blue" @click="create(2)">新建套餐</button>
                <button class="btn green" @click="create(1)">新建餐品</button>
            </s:hasPermission>
            <div class="clearfix"></div>
        </div>
        <div class="table-filter form-horizontal">
        </div>
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>
</div>
</div>


<script>

    var action;
    function checkSort(t) {
        if ($(t).val() == '') {
            return;
        }
        var v = $(t).val();
        var count = 0;
        var attr = document.getElementsByName("printSort");
        for (var i = 0; i < attr.length; i++) {
            if (v == attr[i].value) {
                count++;
            }
        }

        if (count > 1) {
            alert("打印排序添加重复");
            $(t).val('');
        }

    }


    Vue.config.debug = true;
    (function () {
        var cid = "#control";
        var $table = $(".table-body>table");
        var allArticles = [];
        var articleType = {
            1: "单品",
            2: "套餐"
        }
        var tb = $table.DataTable({
            "lengthMenu": [[50, 75, 100, 150], [50, 75, 100, "All"]],
            ajax: {
                url: "article/list_all",
                dataSrc: ""
            },
            stateSave: true,
            deferRender: true,
            ordering: false,
            columns: [
                {
                    title: "餐品类别",
                    data: "articleFamilyName",
                    s_filter: true,
                },
                {
                    title: "类型",
                    data: "articleType",
                    createdCell: function (td, tdData) {
                        $(td).html(articleType[tdData]);
                    },
                    s_filter: true,
                    s_render: function (d) {
                        return articleType[d];
                    }
                },
                {
                    title: "名称",
                    data: "name",
                },
                {
                    title: "价格",
                    data: "price",
                },
                {
                    title: "粉丝价",
                    data: "fansPrice",
                    defaultContent: "",
                },
                {
                    title: "图片",
                    data: "photoSmall",
                    defaultContent: "",
                    createdCell: function (td, tdData) {
                    	if(tdData !=null && tdData.substring(0,4)=="http"){
                			$(td).html("<img src=\"" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:40px;width:80px;\"/>");
                		}else{
                			$(td).html("<img src=\"/" + tdData + "\" class=\"img-rounded\" onerror=\"this.src='assets/pages/img/defaultImg.png'\" style=\"height:40px;width:80px;\"/>");
                		}
                    }
                },
                {
                    title: "排序",
                    data: "sort",
                },
                {
                    title: "上架",
                    data: "activated",
                    s_filter: true,
                    s_render: function (d) {
                        return d ? "是" : "否"
                    },
                    createdCell: function (td, tdData) {
                        $(td).html(tdData ? "是" : "否");
                    }
                },
                {
                    title: "沽清",
                    data: "isEmpty",
                    s_filter: true,
                    s_render: function (d) {
                        return d ? "是" : "否"
                    },
                    createdCell: function (td, tdData) {
                        $(td).html(tdData ? "是" : "否");
                    }
                },
                {
                    title: "工作日库存",
                    data: "stockWorkingDay",
                    defaultContent: "0"
                },
                {
                    title: "周末库存",
                    data: "stockWeekend",
                    defaultContent: "0"
                },
                {
                    title: "操作",
                    data: "id",
                    createdCell: function (td, tdData, rowData, row) {
                        var operator = [
                            <s:hasPermission name="article/delete">
                            C.createDelBtn(tdData, "article/delete"),
                            </s:hasPermission>
                            <s:hasPermission name="article/edit">
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
        // var zNodes=[];
        // var setting = {
        //     check: {
        //         enable: true,
        //         chkboxType: { "Y" : "ps", "N" : "ps" },
        //     },
        //     data: {
        //         simpleData: {
        //             enable: true
        //         }
        //     },
        //     view: {
        //         showIcon: false,
        //     }
        // };
        //
        // var tempSelect = []
        // function setCheck() {
        //     var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
        //         type = { "Y" : "ps", "N" : "ps" };
        //     zTree.setting.check.chkboxType = type;
        //     zTree.setting.check.enable = true;
        //     zTree.setting.callback.onCheck = zTreeOnCheck;
        //
        // };
        // function zTreeOnCheck(event, treeId, treeNode) {
        //     var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        //     var nodes = treeObj.getCheckedNodes(true);
        //     var arr = [];
        //     arr = nodes.filter(function (v,i) {
        //         return v.hasOwnProperty('children') == false;
        //     });
        //     //console.log('zNodes',zNodes)
        //     tempSelect = [];
        //     arr.map(function (v,i) {
        //         var obj = {
        //             kitchenId: v.kitchenId,
        //             jsonDtoId: v.jsonDtoId,
        //             kitchenName: v.kitchenName,
        //             name: v.name
        //         };
        //         tempSelect.push(obj);
        //     });
        //
        // };
        // $("#submitKitchen").click(function () {
        //     //保存
        //     $("#printerModel").css("display","none");
        //     vueObj.$set("selectData", tempSelect);
        // })
        // $("#cancelKitchen").click(function () {
        //     //取消
        //     var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        //     treeObj.checkAllNodes(false);
        //     $("#printerModel").css("display","none");
        // })

        var vueObj = new Vue({
                    el: "#control",
                    mixins: [C.formVueMix],
                    data: {
                        articlefamilys: [],
                        recommendList: [],
                        weightPackageList: [],
                        supportTimes: [],
                        kitchenList: [],
                        virtualList : [],
                        checkedUnit: [],
                        articleattrs: [],
                        unitList: [],
                        printerList:[],
                        chooseKitchen:[],
                        jsonTree: [],
                        kitchenGroup:[],
                        enableDuodongxian:false,
                        chooseKitchenLength:0,
                        printerModel:false,
                        selectedUnit: new HashMap(),
                        articleunits: {},
                        unitPrices: [],
                        mealtempList: [],
                        choiceTemp: "",
                        lastChoiceTemp: "",
                        allArticles: allArticles,
                        choiceArticleShow: {show: false, mealAttr: null, items: [], currentFamily: ""},
                        singleItem:[],
                        searchNameLike : "",
                        canSave : true, //用于判断是否可以点击保存按钮
                        shop: {},
                        brand: {},
                        selectData: [],
                        kitchenGroup: [],
                        chooseKitchen:[]
                    },
                    methods: {
                        checkedOne (item) {
                            var idIndex = null;
                            var obj = {id: item.id}
                            var key = Object.keys(obj)[0];
                            this.chooseKitchen.every(function(value, i) {
                                if (value[key] === obj[key]) {
                                    idIndex = i;
                                    return false;
                                }
                                return true;
                            });
                            if (idIndex != null) {
                                // 如果已经包含了该id, 则去除(单选按钮由选中变为非选中状态)
                                this.chooseKitchen.splice(idIndex, 1)
                            } else {

                                // 选中该checkbox
                                this.chooseKitchen.push(item)
                            }

                        },
                        aa(item) {
                            if(JSON.stringify(this.chooseKitchen).indexOf(JSON.stringify(item))>-1){
                                return true
                            }else {
                                return false
                            }
                        },
                        // addKitchen : function(){
                        //     $("#printerModel").css("display","block");
                        //
                        //     // var arr = []
                        //     // var treeObj1 = $.fn.zTree.getZTreeObj("treeDemo");
                        //     // this.selectData.map(function(v,i){
                        //     //     var arr1 = treeObj1.getNodesByParam("jsonDtoId", v.jsonDtoId, null);
                        //     //     arr = arr.concat(arr1)
                        //     // })
                        //     //
                        //     // for (var i=0, l=arr.length; i < l; i++) {
                        //     //     treeObj1.checkNode(arr[i], true, true);
                        //     // }
                        //
                        // },
                        // deletePrinter: function(item) {
                        //     var index = null;
                        //     var obj = {jsonDtoId: item.jsonDtoId}
                        //     var key = Object.keys(obj)[0];
                        //     this.selectData.every(function(value, i) {
                        //         if (value[key] === obj[key]) {
                        //             index = i;
                        //             return false;
                        //         }
                        //         return true;
                        //     });
                        //     this.selectData.splice(index,1);
                        //     var treeObj1 = $.fn.zTree.getZTreeObj("treeDemo");
                        //     var arr1 = treeObj1.getNodesByParam("jsonDtoId", item.jsonDtoId, null);
                        //     for (var i=0, l=arr1.length; i < l; i++) {
                        //         treeObj1.checkNode(arr1[i], false, true);
                        //     }
                        // },
                        // cancelKitchen : function(){
                        //     this.printerModel=false;
                        // },

                        itemDefaultChange: function (attr, item) {
                            if(item.isDefault){
                                item.isDefault = false;
                            }else{
                                item.isDefault = true;
                            }

//                            for (var i in attr.mealItems) {
//                                var m = attr.mealItems[i];
//                                if (m != item) {
//                                    m.isDefault = false;
//                                }
//                            }
                        },
                        updateAttrItems: function () {
                            this.choiceArticleShow.mealAttr.mealItems = $.extend(true, {}, this.choiceArticleShow).items;
                            $("#article-choice-dialog").modal('hide');
                        },
                        removeMealItem: function (attr, item) {
                            attr.mealItems.$remove(item);
                        },
                        removeArticleItem: function (mealItem) {
                            this.choiceArticleShow.items.$remove(mealItem);
                        },

                        changeUsed: function (select, item, type) {
                            var use;
                            if (item.isUsed == 0 || !item.isUsed) {
                                use = 1;
                                item.isUsed = 1;

                            } else {
                                use = 0;
                                item.isUsed = 0;
                            }
                            for (var i = 0; i < this.selectedUnit.unitList.length; i++) {
                                if (this.selectedUnit.unitList[i].id == select.id) {
                                    for (var k = 0; i < this.selectedUnit.unitList[i].details.length; k++) {
                                        if (this.selectedUnit.unitList[i].details[k].id == item.id) {
                                            this.selectedUnit.unitList[i].details[k].isUsed = use;
                                            break;
                                        }

                                    }
                                }
                            }
                        },
                        addArticleItem: function (art) {
                            var item = {
                                name: art.name,
                                sort: art.sort,
                                articleName: art.name,
                                priceDif: 0,
                                articleId: art.id,
                                photoSmall: art.photoSmall,
                                isDefault: false,
                            };
                            console.log(this.choiceArticleShow.items.length);
                            if (!this.choiceArticleShow.items.length) {
                                item.isDefault = true;
                            }
                            this.choiceArticleShow.items.push(item);
                        },
                        clickUnit: function (attr) {
                            if (!this.selectedUnit.unitList) {
                                this.selectedUnit.unitList = [];
                            }

                            var contains = false;
                            for (var i = 0; i < this.selectedUnit.unitList.length; i++) {

                                if (this.selectedUnit.unitList[i].id == attr.id) {
                                    contains = true;
                                    this.selectedUnit.unitList.$remove(attr);
                                    this.selectedUnit.unitList.$remove(this.selectedUnit.unitList[i]);
                                    break;
                                }
                            }

                            if (!contains) {
                                for (var i = 0; i < attr.details.length; i++) {
                                    attr.details[i].isUsed = 1;
                                    attr.details[i].price = null;
                                }
                                this.selectedUnit.unitList.push(attr);
                            }


                        },
                        addMealItem: function (meal) {
                        	var that = this;
                            $.ajax({
                            	url:"article/selectsingleItem",
                            	type: "post",
                            	dataType:"json",
                            	success:function(result){
                            		if(result.success){
                            			that.singleItem = result.data;
                            		}
                            	},
                            	error:function(){
                            		C.errorMsg("获取单品失败!");
                            	}
                            });
                            this.choiceArticleShow.show = true;
                            this.choiceArticleShow.mealAttr = meal;
                            this.choiceArticleShow.items = $.extend(true, {}, meal).mealItems || [];
                            this.$nextTick(function () {
                                $("#article-choice-dialog").modal('show');
                                var that = this;
                                $("#article-choice-dialog").on('hidden.bs.modal', function () {
                                    that.choiceArticleShow.show = false;
                                });
                            })
                        },

                        delMealAttr: function (meal) {
                            this.m.mealAttrs.$remove(meal);
                        }
                        ,
                        choiceTypeChange: function (attr) {
                            if (attr.choiceType == 1) {

                            }else{

                            }
                        },
                        addMealAttr: function () {
                            var sort = this.maxMealAttrSort + 1;
                            this.m.mealAttrs.push({
                                name: "套餐属性" + sort,
                                sort: sort,
                                mealItems: [],
                            });
                        }
                        ,
                        choiceMealTemp: function (e) {
                            var that = this;
                            C.confirmDialog("切换模板后，所有套餐编辑的内容将被清空，你确定要切换模板吗?", "提示", function () {
                                that.lastChoiceTemp = $(e.target).val();
                                var mealAttrs = [];
                                for (var i = 0; i < that.mealtempList.length; i++) {
                                    var temp = that.mealtempList[i];
                                    if (temp.id == that.lastChoiceTemp) {
                                        for (var n = 0; n < temp.attrs.length; n++) {
                                            var attr = temp.attrs[n];
                                            mealAttrs.push({
                                                name: attr.name,
                                                sort: attr.sort,
                                                mealItems: [],
                                            });
                                        }
                                        that.m.mealAttrs = mealAttrs;
                                        return false;
                                    }
                                }
                                that.m.mealAttrs = [];
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
                            var that = this;
                            action = "create";
                            this.m = {
                                articleFamilyId: this.articlefamilys[0].id,
//                                recommendId:this.recommendList[0].id,
//                                virtualId:this.virtualList[0].id,
                                supportTimes: [],
                                kitchenList: [],
                                mealAttrs: [],
                                isRemind: false,
                                activated: true,
                                showDesc: true,
                                showBig: true,
                                isEmpty: false,
                                stockWorkingDay: 100,
                                stockWeekend: 50,
                                sort: 0,
                                units: [],
                                articleType: article_type,
                            };

                            this.showform = true;
                            this.selectedUnit = [];
                            //this.selectData = [];
                            this.chooseKitchen = [];


                            var list = {
                                unitList: []
                            }
                            this.selectedUnit = list;
                            $.post("unit/list_all", null, function (data) {
                                that.unitList = data;
                            });

                        }
                        ,
                        uploadSuccess: function (url) {
                            console.log(url);
                            $("[name='photoSmall']").val(url).trigger("change");
                            C.simpleMsg("上传成功");
                            $("#photoSmall").attr("src", "/" + url);
                        }
                        ,
                        uploadSuccessLittle: function (url) {
                            console.log(url);
                            $("[name='photoLittle']").val(url).trigger("change");
                            C.simpleMsg("上传成功");
                            $("#photoLittle").attr("src", "/" + url);
                        }
                        ,
                        uploadSuccessSuper: function (url) {
                            console.log(url);
                            $("[name='photoSuper']").val(url).trigger("change");
                            C.simpleMsg("上传成功");
                            $("#photoSuper").attr("src", "/" + url);
                        }
                        ,
                        uploadSuccessSquareOriginal: function (url) {
                            console.log(url);
                            $("[name='photoSquareOriginal']").val(url).trigger("change");
                            C.simpleMsg("上传成功");
                            $("#photoSquareOriginal").attr("src", "/" + url);
                        }
                        ,
                        uploadSuccessGif: function (url) {
                            console.log(url);
                            $("[name='gifUrl']").val(url).trigger("change");
                            C.simpleMsg("上传成功");
                            $("#gifUrl").attr("src", "/" + url);
                        }
                        ,
                        uploadError: function (msg) {
                            C.errorMsg(msg);
                        }
                        ,
                        edit: function (model) {
                            this.selectedUnit = [];


                            var list = {
                                unitList: []
                            }
                            this.selectedUnit = list;

                            var that = this;


                            action = "edit";

                            $.post("article/list_one_full", {id: model.id}, function (result) {
                                var article = result.data;

                                that.checkedUnit = [];
                                that.showform = true;

                                that.selectedUnit.unitList = [];
                                for (var i = 0; i < article.units.length; i++) {
                                    that.selectedUnit.unitList.push(article.units[i]);
                                }

                                article.mealAttrs || (article.mealAttrs = []);
                                that.m = article;
                                if (article.hasUnit && article.hasUnit != " " && article.hasUnit.length) {
                                    var unit = article.hasUnit.split(",");
                                    unit = $.unique(unit);
                                    for (var i in  unit) {
                                        that.checkedUnit.push(parseInt(unit[i]));
                                    }
                                }
                                that.unitPrices = article.articlePrices;
                                if (!article.kitchenList) {
                                    article.kitchenList = [];
                                }
                                // var temp = []
                                // article.jsonTree && article.jsonTree.length > 0 && article.jsonTree.map(function (v,i) {
                                //     v.children && v.children.length > 0 &&v.children.map(function (k,j) {
                                //         temp.push(k)
                                //     })
                                // })
                                // that.selectData = temp
                                if (article.kitchenGroups){
                                    that.chooseKitchen = article.kitchenGroups
                                }else {
                                    that.chooseKitchen=[];
                                }
                            });
                            $.post("unit/list_all_id", {articleId: model.id}, function (data) {
                                that.unitList = data;
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
                        },
                        cleanRemark: function () {
                            $("#kitchenRemark").html("");
                            if($("#supportTimeRemark").html().indexOf("时间冲突") == -1){
                                $("#supportTimeRemark").html("");
                            }
                        },
                        checkNull: function () {
                            if (this.supportTimes.length <= 0) {//判断当前店铺是否创建了供应时间
                                $("#supportTimeRemark").html("请先创建至少一个菜品供应时间！");
                                return true;
                            }
                            if (this.m.supportTimes.length <= 0) {//供应时间 非空验证
                                $("#supportTimeRemark").html("请选择餐品供应时间！");
                                return true;
                            }
                            //if (this.kitchenList.length <= 0) {//判断当前店铺是否创建了出餐厨房

                            //    $("#kitchenRemark").html("<font color='red'>请先创建至少一个出餐厨房！</span>");
                            //    return true;
                            //}
                            if (this.m.kitchenList.length <= 0 && !this.enableDuodongxian) {//出餐厨房 非空验证
                                //$("#kitchenRemark").html("<font color='red'>请选择出餐厨房！</span>");
                                //return true;
                                if(!confirm("是否不选择出餐厨房！")){
                                    return true;
                                }
                            }
                            return false;
                        },
                        save: function (e) {

                            var attrs = this.m.mealAttrs;
                            for (var i = 0; i < attrs.length; i++) {
                                var attr = attrs[i];
                                var count = 0;
                                for (var k = 0; k < attr.mealItems.length; k++) {
                                    if (attr.mealItems[k].isDefault) {
                                        count++;
                                    }
                                }
                                if (attr.choiceType == 0) {
                                    if (attr.choiceCount != count) {
                                        C.errorMsg("默认选中项与必选数量不等");
                                        return;
                                    }
                                }
                            }


                            if (this.checkNull()) {//验证必选项(出参厨房和供应时间)
                                return;
                            }
                            var that = this;
                            var action = $(e.target).attr("action");
                            this.m.articlePrices = this.unitPrices;
                            this.m.hasUnit = this.checkedUnit.join() || " ";
                            this.m.units = [];
                            for (var i = 0; i < that.selectedUnit.unitList.length; i++) {
                                this.m.units.push({
                                    id: that.selectedUnit.unitList[i].id,
                                    choiceType: that.selectedUnit.unitList[i].choiceType,
                                    details: that.selectedUnit.unitList[i].details,
                                });
                            }

                            //this.m.jsonTree = this.selectData;
                            var temp = [];
                            this.chooseKitchen.length > 0 && this.chooseKitchen.map(function (v,i) {
                                temp.push(v.id)
                            })
                            this.m.kitchenGroupIdList = temp;
                            var m = this.m;

                            var jsonData = JSON.stringify(this.m);
                            $.ajax({
                                contentType: "application/json",
                                type: "post",
                                url: action,
                                data: jsonData,
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
                            for (var i in this.singleItem) {
                                var art = this.singleItem[i];
                                var has = false;
                                for (var n in this.choiceArticleShow.items) {
                                    var mealItem = this.choiceArticleShow.items[n];
                                    if (mealItem.articleId == art.id) {
                                        has = true;
                                        break;
                                    }
                                }
                                if (!has && (this.choiceArticleShow.currentFamily == art.articleFamilyName || this.choiceArticleShow.currentFamily == "")) {
                                    arts.push(art);
                                }
                            }
                            return arts;
                        }
                        ,
                        maxMealAttrSort: function () {
                            var sort = 0;
                            for (var i in this.m.mealAttrs) {
                                var meal = this.m.mealAttrs[i];
                                if (meal.sort > sort) {
                                    sort = meal.sort;
                                }
                            }
                            return parseInt(sort);
                        }
                        ,
                        allUnitPrice: function () {
                            var result = [];
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


                        $.post("articlefamily/list_all", null, function (data) {
                            that.articlefamilys = data;
                        });

                        $.post("recommend/list_all", null, function (data) {
                            that.recommendList = data;
                        });

                        $.post("weightPackage/list_all", null, function (data) {
                            that.weightPackageList = data;
                        });

                        $.post("supporttime/list_all", null, function (data) {
                            that.supportTimes = data;
                        });
                        $.post("kitchen/list_allStatus", null, function (data) {
                            that.kitchenList = data;
                        });
                        $.post("mealtemp/list_all", null, function (data) {
                            that.mealtempList = data;
                        });
                        $.post("virtual/listAll", null, function (data) {
                            that.virtualList = data;
                        });
                        $.ajax({
                            url:"shopInfo/list_one",
                            type:"post",
                            dataType:"json",
                            success:function (resultData) {
                                that.shop = resultData.data.shop;
                                that.brand = resultData.data.brand;
                            }
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
                        // $.ajax({
                        //     type:"post",
                        //     url:"printer/list_all",
                        //     dataType:"json",
                        //     success:function(data){
                        //         if(data.length > 0){
                        //             this.printerList=data
                        //
                        //         }
                        //     }
                        // });
                        // $.ajax({
                        //     type:"post",
                        //     url:"article/selectTree",
                        //
                        //     dataType:"json",
                        //     success:function(data){
                        //         if(data.length > 0){
                        //
                        //             // zNodes = data
                        //             // $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                        //             // setCheck();
                        //             // zTreeOnCheck()
                        //         }
                        //     }
                        // });
                        $.ajax({
                            type:"post",
                            url:"article/selectkitchenGroup",
                            dataType:"json",
                            success:function(data){
                                if(data.length > 0){
                                    that.kitchenGroup=data;
                                }
                            }
                        });
                        $.ajax({
                            type:"post",
                            url:"kitchen/selectShopStatus",
                            dataType:"json",
                            success:function(data){
                                if(data==1){
                                    that.enableDuodongxian=false;
                                }else{
                                    that.enableDuodongxian=true;
                                }
                            }
                        });
                    },
                    watch: {
                        'm.supportTimes': function(newVal, oldVal) {
                            var that = this;
                            $("#supportTimeRemark").html("");   //清除错误提示
                            this.canSave = true;//还原为可以保存的状态
                            var deleted = [];//无效的供应时间
                            //判断所选的时间是否有覆盖区间
                            for (var i in that.m.supportTimes) {
                                var itemX = getSupportTimesInfo(that.m.supportTimes[i]);
                                if(itemX == null){//如果为空，可能当前供应时间已被删除
                                    deleted.push(i);
                                    continue;
                                }
                                for (var y in that.m.supportTimes) {
                                    var itemY = getSupportTimesInfo(that.m.supportTimes[y]);
                                    if(itemY == null){//如果为空，可能当前供应时间已被删除
                                        continue;
                                    }
                                    if(i == y){//不和自己做对比
                                        continue;
                                    }
                                    if(strFormat(itemX.beginTime)>=strFormat(itemY.beginTime) && strFormat(itemX.beginTime)<=strFormat(itemY.endTime) ){      //X 开始时间    在       Y区间之间
                                        if(itemX.supportWeekBin&itemY.supportWeekBin){//如果两个供应时间，存在时间重叠，并且选中的星期也存在重叠，则不允许保存。
                                            showSupportTimeRemark(itemX,itemY);
                                            break;
                                        }
                                    }
                                    if(strFormat(itemX.endTime)>=strFormat(itemY.beginTime) && strFormat(itemX.endTime)<=strFormat(itemY.endTime) ){            //X 结束时间     在      Y区间之间
                                        if(itemX.supportWeekBin&itemY.supportWeekBin){
                                            showSupportTimeRemark(itemX,itemY);
                                            break;
                                        }
                                    }
                                    if(strFormat(itemX.beginTime)<=strFormat(itemY.beginTime) && strFormat(itemX.endTime)>=strFormat(itemY.endTime) ){        //  Y 在 X 区间内
                                        if(itemX.supportWeekBin&itemY.supportWeekBin){
                                            showSupportTimeRemark(itemX,itemY);
                                            break;
                                        }
                                    }
                                }
                            }

                            //删除无效的供应时间
                            for(var i in deleted){
                                that.m.supportTimes.splice(deleted[i], 1);
                            }

                            //根据ID获取供应时间的信息
                            function getSupportTimesInfo (id){
                                var supportItem = null;
                                $(that.supportTimes).each(function(index,item){
                                    if(item.id == id){
                                        supportItem = item;
                                        return false;
                                    }
                                })
                                return supportItem;
                            }

                            //显示供应时间冲突错误
                            function showSupportTimeRemark(x,y){
                                var str = "星期或时间冲突：<br/>";
                                str += "【"+x.name+"】 ("+x.beginTime+"--"+x.endTime+")<br/>";
                                str += "【"+y.name+"】 ("+y.beginTime+"--"+y.endTime+")<br/>";
                                $("#supportTimeRemark").html(str);
                                that.canSave = false;
                            }

                            function strFormat(str){
                                return parseInt(str.replace(":",""));
                            }
                        },

                    }
                })
                ;
        C.vue = vueObj;

    }());


</script>
