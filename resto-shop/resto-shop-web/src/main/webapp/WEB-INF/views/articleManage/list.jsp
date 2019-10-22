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
</style>
<div>

<div id="control">
    <div class="row form-div" v-if="previewModal" style="z-index: 10051;">
        <div class="portlet light " style="text-align: center; background-color: rgba(0,0,0,0)">
            <div class="portlet-body">
                <img :src="previewImg" :width="width" alt="">
                <div class="form-body" style="margin-top: 20px;">
                    <div class="form-group text-center">
                        <a class="btn default" @click="cancelPreview">取消</a>
                    </div>
                </div>

            </div>
        </div>


    </div>
    <div class="row form-div" v-if="uploadModal">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">批量上传菜品</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <div class="form-body">
                        <div class="form-group ">
                            <input type="file" name="filename"  @change="uploadFile"/>
                        </div>
                        <div class="form-group text-center">
                            <a class="btn green"   @click="saveUpload">保存</a>
                            <a class="btn default" @click="cancelUploadModal">取消</a>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="article-dialog" v-if="showform" @click="cleanRemark">
        <div class="modal-dialog " style="width:90%;">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" style="text-align: center;">{{m.articleType==1 ? (isCreate ? '新建单品' : '编辑单品') : (isCreate ? '新建套餐' : '编辑套餐')}}</h4>
                </div>
                <form class="form-horizontal" role="form "  @submit.prevent="save">
                    <div class="modal-body auto-height">
                        <div class="form-body">
                            <div class="dish-setting cf">
                                <h3 class="title">菜品基础设置</h3>
                                <div class="dish-setting-left">
                                    <div class="form-group col-md-4">
                                        <label class="control-label col-md-5">销售类型</label>
                                        <div  class="col-md-7">
                                            <label class="radio-inline">
                                                <input type="radio" name="distributionModeId" v-model="m.distributionModeId" value="1" > 堂食
                                            </label>
                                            <label class="radio-inline">
                                                <input type="radio" name="distributionModeId" v-model="m.distributionModeId" value="2"> 外卖
                                            </label>
                                            <label class="radio-inline">
                                                <input type="radio" name="distributionModeId" v-model="m.distributionModeId" value="3"> 堂食和外卖
                                            </label>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">菜品类别</label>
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
                                        <label class="col-md-5 control-label">菜品名称</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" name="name" v-model="m.name"
                                                   required="required">
                                        </div>
                                    </div>

                                    <div class="form-group col-md-4" >
                                        <label class="col-md-5 control-label">菜品品牌定价</label>
                                        <div class="col-md-7" v-if="m.articleType==1">
                                            <input type="text" class="form-control" name="price" v-model="m.price"
                                                   required="required" pattern="\d{1,10}(\.\d{1,2})?$"
                                                   title="价格只能输入数字,且只能保存两位小数！">
                                        </div>
                                        <div class="col-md-7" v-if="m.articleType==2">
                                            <input type="text" class="form-control" name="price" v-model="0" readonly>
                                        </div>
                                    </div>

                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">粉丝价</label>
                                        <div class="col-md-7" v-if="m.articleType==1">
                                            <input type="text" class="form-control" name="fansPrice" v-model="m.fansPrice">
                                        </div>
                                        <div class="col-md-7" v-if="m.articleType==2">
                                            <input type="text" class="form-control" name="fansPrice" v-model="0" readonly>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">称斤价格(单位：元/斤)</label>
                                        <div class="col-md-7" v-if="m.articleType==1">
                                            <input type="text" class="form-control" name="cattyMoney" v-model="m.cattyMoney">
                                        </div>
                                        <div class="col-md-7" v-if="m.articleType==2">
                                            <input type="text" class="form-control" name="cattyMoney" v-model="0" readonly>
                                        </div>
                                    </div>

                                </div>
                                <div class="dish-setting-right">
                                    <div class="form-group col-md-4">
                                        <label class="control-label col-md-5">状态</label>
                                        <div  class="col-md-7">
                                            <label class="radio-inline">
                                                <input type="radio" name="state" v-model="m.state" value="1" > 可用
                                            </label>
                                            <label class="radio-inline">
                                                <input type="radio" name="state" v-model="m.state" value="0"> 不可用
                                            </label>
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
                                        <label class="col-md-5 control-label">餐盒数量</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" name="mealFeeNumber" v-model="m.mealFeeNumber">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">排序</label>
                                        <div class="col-md-7">
                                            <input type="number" class="form-control" name="sort" v-model="m.sort">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">工作日库存</label>
                                        <div class="col-md-7" v-if="m.articleType==1">
                                            <input type="number" class="form-control" name="stockWorkingDay" v-model="m.stockWorkingDay">
                                        </div>
                                        <div class="col-md-7" v-if="m.articleType==2">
                                            <input type="number" class="form-control" name="stockWorkingDay" v-model="100" readonly>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">周末库存</label>
                                        <div class="col-md-7" v-if="m.articleType==1">
                                            <input type="text" class="form-control" name="stockWeekend" v-model="m.stockWeekend">
                                        </div>
                                        <div class="col-md-7" v-if="m.articleType==2">
                                            <input type="text" class="form-control" name="stockWeekend" v-model="100" readonly>
                                        </div>
                                    </div>

                                </div>
                            </div>
                            <div class="dish-setting cf">
                                <h3 class="title">图片设置</h3>
                                <div class="form-group ">
                                    <label class="control-label col-md-5" style="width: 20%;">菜品图片显示类型</label>
                                    <div  class="col-md-7">
                                        <label class="radio-inline">
                                            <input type="radio" name="photoType" v-model="m.photoType" value="4"> 超大图
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" name="photoType" v-model="m.photoType" value="1" > 大图
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" name="photoType" v-model="m.photoType" value="5"> 正方形图
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" name="photoType" v-model="m.photoType" value="2"> 小图
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" name="photoType" v-model="m.photoType" value="3"> 无图
                                        </label>


                                    </div>
                                </div>

                                <div class="cf">
                                    <div class="form-group col-md-4" v-if="m.photoType == 1" style="width: 180px;margin-left:150px; display: inline-block;" >
                                        <h5 style="text-align: center;">餐品图片(大图)</h5>
                                        <div class="" style="position: relative;text-align: center;">
                                            <input type="hidden" name="photoSmall" v-model="m.photoSmall">
                                            <img-file-upload style="height: 100px; opacity: 0;z-index: 10; position: relative;" class="form-control" @success="uploadSuccess"
                                                             @error="uploadError"></img-file-upload>
                                            <img v-if="m.photoSmall" :src="m.photoSmall" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                            <img v-else src="assets/pages/img/uploadImg.png"  width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                        </div>
                                        <h5 style="text-align: center; color:lightskyblue;" @click="preview(1)">图片规格预览</h5>
                                    </div>

                                    <div class="form-group col-md-4" v-if="m.photoType == 2" style="width: 180px;margin-left:150px; display: inline-block;">
                                        <h5 style="text-align: center;">餐品图片(小图)</h5>
                                        <div class="" style="position: relative;text-align: center;">
                                            <input type="hidden" name="photoLittle" v-model="m.photoLittle">
                                            <img-file-upload style="height: 100px; opacity: 0;z-index: 10; position: relative;" class="form-control" @success="uploadSuccessLittle"
                                                             @error="uploadError"></img-file-upload>
                                            <img v-if="m.photoLittle" :src="m.photoLittle" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                            <img v-else src="assets/pages/img/uploadImg.png"  width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                        </div>
                                        <h5 style="text-align: center; color:lightskyblue;" @click="preview(1)">图片规格预览</h5>
                                    </div>

                                    <div class="form-group col-md-4" v-if="m.photoType == 3" style="width: 180px;margin-left:150px; display: inline-block;">
                                        <h4 style="text-align: center;">餐品图片(无图)</h4>
                                        <%--<div class="col-md-7">无图</div>--%>
                                    </div>

                                    <div class="form-group col-md-4" v-if="m.photoType == 4" style="width: 180px;margin-left:150px; display: inline-block;">
                                        <h5 style="text-align: center;">餐品图片(超大图)</h5>
                                        <div class="" style="position: relative;text-align: center;">
                                            <input type="hidden" name="photoSuper" v-model="m.photoSuper">
                                            <img-file-upload style="height: 100px; opacity: 0;z-index: 10; position: relative;" cut="false" class="form-control" @success="uploadSuccessSuper"
                                                             @error="uploadError"></img-file-upload>
                                            <img v-if="m.photoSuper" :src="m.photoSuper" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                            <img v-else src="assets/pages/img/uploadImg.png"  width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                        </div>
                                        <h5 style="text-align: center; color:lightskyblue;" @click="preview(1)">图片规格预览</h5>
                                    </div>

                                    <div class="form-group col-md-4" v-if="m.photoType == 5" style="width: 180px;margin-left:150px; display: inline-block;">
                                        <h5 style="text-align: center;">餐品图片(正方形图)</h5>
                                        <div class="" style="position: relative;text-align: center;">
                                            <input type="hidden" name="photoSquareOriginal" v-model="m.photoSquareOriginal">
                                            <img-file-upload style="height: 100px; opacity: 0;z-index: 10; position: relative;" cut="false" class="form-control" @success="uploadSuccessSquareOriginal"
                                                             @error="uploadError"></img-file-upload>
                                            <img v-if="m.photoSquareOriginal" :src="m.photoSquareOriginal" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                            <img v-else src="assets/pages/img/uploadImg.png"  width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                        </div>
                                        <h5 style="text-align: center; color:lightskyblue;" @click="preview(1)">图片规格预览</h5>
                                    </div>
                                    <div class="form-group col-md-4"  style="width: 180px;margin-left:150px; display: inline-block;">
                                        <h5 style="text-align: center;">上传菜品详情页图片</h5>
                                        <div class="" style="position: relative;text-align: center;">
                                            <input type="hidden" name="photoSmall" v-model="m.photoSmall">
                                            <img-file-upload style="height: 100px; opacity: 0;z-index: 10; position: relative;" cut="false" class="form-control" @success="uploadSuccess"
                                                                 @error="uploadError"></img-file-upload>
                                            <img v-if="m.photoSmall" :src="m.photoSmall" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                            <img v-else src="assets/pages/img/uploadImg.png"  width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                        </div>
                                        <h5 style="text-align: center; color:lightskyblue;" @click="preview(2)">图片规格预览</h5>
                                    </div>
                                    <div class="form-group col-md-4"  style="width: 180px;margin-left:150px; display: inline-block;">
                                        <h5 style="text-align: center;">上传菜品预点餐GIF图片</h5>
                                        <div class="" style="position: relative;text-align: center;">
                                            <input type="hidden" name="gifUrl" v-model="m.gifUrl">
                                            <img-file-upload style="height: 100px; opacity: 0;z-index: 10; position: relative;" cut="false" class="form-control" @success="uploadSuccessGif"
                                                                 @error="uploadError"></img-file-upload>
                                            <img v-if="m.gifUrl" :src="m.gifUrl" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                            <img v-else src="assets/pages/img/uploadImg.png"  width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                        </div>
                                        <h5 style="text-align: center; color:lightskyblue;" @click="preview(3)">图片规格预览</h5>
                                    </div>
                                </div>
                            </div>
                            <div class="dish-setting cf">
                                <h3 class="title">特色标签</h3>
                                <div class="form-group col-md-12">
                                    <label class="col-md-2 text-right">菜品推荐程度</label>
                                    <div class="col-md-10">
                                        <select name="recommendationDegree" v-model="m.recommendationDegree">
                                            <option value="">-</option>
                                            <option value="不推荐">不推荐</option>
                                            <option value="推荐">推荐</option>
                                            <option value="强烈推荐">强烈推荐</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-12" >
                                    <label class="col-md-2 text-right">预计出餐时长</label>
                                    <div class="col-md-7">
                                        <input type="text" style="width: 150px" name="mealOutTime" v-model="m.mealOutTime"
                                               required="required">
                                        <span>分钟</span>
                                    </div>
                                </div>
                                <div class="form-group col-md-12" >
                                    <label class="col-md-2 text-right">菜品类型</label>
                                    <div class="col-md-10">
                                        <select name="articleKind" v-model="m.articleKind">
                                            <option value="">-</option>
                                            <option value="主菜">主菜</option>
                                            <option value="汤羹">汤羹</option>
                                            <option value="主食">主食</option>
                                            <option value="甜点">甜点</option>
                                            <option value="小吃">小吃</option>
                                            <option value="酒水饮料">酒水饮料</option>
                                            <option value="汤底">汤底</option>
                                            <option value="调料">调料</option>
                                            <option value="冷盘">冷盘</option>
                                            <option value="餐具">餐具</option>

                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-2 text-right">菜品标签</label>
                                    <div class="col-md-10">
                                        <select name="articleLabel" v-model="m.articleLabel">
                                            <option value="">-</option>
                                            <option value="招牌菜">招牌菜</option>
                                            <option value="本月新菜">本月新菜</option>
                                            <option value="限时特价">限时特价</option>
                                            <option value="明星菜">明星菜</option>

                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-2 text-right">辣度</label>
                                    <div class="col-md-10">
                                        <select name="articleHot" v-model="m.articleHot">
                                            <option value="">-</option>
                                            <option value="不辣">不辣</option>
                                            <option value="中辣">中辣</option>
                                            <option value="重辣">重辣</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-2 text-right">份量</label>
                                    <div class="col-md-10">
                                        <select name="articleComponent" v-model="m.articleComponent">
                                            <option value="">-</option>
                                            <option value="小份量">小份量</option>
                                            <option value="标准菜量">标准菜量</option>
                                            <option value="特大量">特大量</option>

                                        </select>
                                    </div>
                                </div>

                            </div>
                            <div class="dish-setting cf">
                                <h3 class="title">功能设置</h3>
                                <div class="form-group col-md-12" v-if="m.articleType==1">
                                    <label class="col-md-2 control-label">开启称斤买卖</label>
                                    <div class="col-md-7 radio-list">
                                        <label class="radio-inline">
                                            <input type="radio"  v-model="m.openCatty" value="1"> 开启
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio"  v-model="m.openCatty" value="0" > 关闭
                                        </label>
                                    </div>
                                </div>

                                <div class="form-group  col-md-12">
                                    <label class="col-md-2 text-right">供应时间</label>
                                    <div class="col-md-8">
                                        <label v-for="time in supportTimes">
                                            <input type="checkbox" name="supportTimes" :value="time.id"
                                                   v-model="m.supportTimes"> <span :class="{'text-danger':time.shopName}" >{{time.name}}({{time.discount+'%'}})</span> &nbsp;&nbsp;
                                        </label>
                                        <div id="supportTimeRemark" style="color: red;"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="dish-setting cf" v-if="m.articleType==1">
                                <h3 class="title">规格属性</h3>
                                <div class="form-group col-md-12" v-if="m.articleType==1">
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

                                <div class="form-group col-md-12" >
                                    <label class="col-md-2 text-right">规格包</label>
                                    <div class="col-md-8">
										<label v-for="attr in unitList">
                                            <input type="checkbox" v-model="attr.isUsed" v-bind:true-value="1" v-bind:false-value="0"
                                                   id="{{attr.id}}" @click="clickUnit(attr)"><span>{{attr.name}}</span> &nbsp;&nbsp;
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group col-md-12" v-for="select in selectedUnit.unitList">
                                    <div class="form-group col-md-12">
                                        <label class="col-md-2 text-right">{{select.name}}:</label>
                                        <label class="col-md-2 ">是否单选: <input type="checkbox" v-bind:true-value="0"
                                                                              v-bind:false-value="1"
                                                                              v-model="select.choiceType"></label>

                                    </div>
                                    <div class="col-md-6" style="background-color: #eee; margin-left: 200px;">
                                        <div class="flex-row">
                                            <div class="flex-1 text-right" style="text-align: center;">规格</div>
                                            <div class="flex-1" style="text-align: center;">差价</div>
                                            <div class="flex-1" style="text-align: center;">排序</div>
                                            <div class="flex-1" style="text-align: center;">是否启用</div>

                                        </div>
                                        <div class="flex-row " v-for="detail in select.articleUnitDetails">
                                            <label class="flex-1 control-label" style="text-align: center;">{{detail.name}}</label>
                                            <div class="flex-1">
                                                <input type="text" class="form-control"
                                                       v-model="detail.price" :value="detail.price==null?0:detail.price" id="price{{detail.id}}" required="required"/>
                                            </div>
                                            <div class="flex-1" >
                                                <input type="text" class="form-control" name="sort"
                                                       v-model="detail.sort" id="sort{{detail.id}}" required="required"
                                                />
                                            </div>
                                            <div  class="flex-1" style="text-align: center;">
                                                <label>
                                                    <input type="checkbox" v-bind:true-value="1" v-bind:false-value="0"
                                                           @click="changeUsed(select,detail)" v-model="detail.isUsed"
                                                    >
                                                </label>

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div v-if="m.articleType==2">
                                <h3 class="sm-title">套餐子品</h3>
                                <div class="col-md-10" v-if="m.articleType==2">
                                    <div class="portlet light bordered cf">
                                        <div class="portlet-title">
                                            <div class="caption font-green-sharp">
                                                <i class="icon-speech font-green-sharp"></i>
                                                <span class="caption-subject bold uppercase"> 编辑套餐</span>
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
                                                               style="width:120px">选择类型&nbsp;</label>
                                                        <div class="col-md-4">
                                                            <select class="form-control" style="width:100px"
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
                                                <div class="portlet-body" >
                                                    <div class="form-group" v-if="attr.mealItems.length">
                                                        <div class="flex-row">
                                                            <div class="flex-1">餐品原名</div>
                                                            <div class="flex-1">餐品名称</div>
                                                            <div class="flex-1" v-if="attr.choiceType == 1">差价</div>
                                                            <div class="flex-1" v-if="attr.choiceType == 0">套餐内菜品价格</div>
                                                            <div class="flex-1">排序</div>
                                                            <div class="flex-1">默认</div>
                                                            <%--<div class="flex-1">指定厨房出单</div>--%>
                                                            <div class="flex-1">移除</div>
                                                        </div>
                                                        <div class="flex-row"
                                                             v-for="item in attr.mealItems | orderBy 'sort' ">
                                                            <div class="flex-1">
                                                                <p class="form-control-static">{{item.articleName}}</p>
                                                            </div>
                                                            <div class="flex-1">
                                                                <input type="text" class="form-control" v-model="item.name"
                                                                       required="required"/>
                                                            </div>
                                                            <div class="flex-1">
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
                                                            <%--<div class="flex-1 radio-list">
                                                                <select class="form-control" name="kitchenId"
                                                                        v-model="item.kitchenId">
                                                                    <option value="-1">(选择厨房)</option>
                                                                    <option :value="k.id" v-for="k in kitchenList">
                                                                        {{k.name}}
                                                                    </option>
                                                                </select>
                                                            </div>--%>
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
                                                            添加菜品
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
                    <button type="button" class="btn green" @click="updateAttrItems()">确定</button>
                </div>
            </div>
        </div>
    </div>
    <div class="table-div">
        <div class="table-operator">
            <button class="btn blue" @click="createExcel">模版下载</button>
            <button class="btn blue" @click="batchUpload">批量上传菜品</button>
            <s:hasPermission name="articleManage/create">
                <button class="btn blue" @click="create(2)">新建套餐</button>
                <button class="btn green" @click="create(1)">新建单品</button>
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
        };
        var mode = {
            1: "堂食",
            2: "外卖",
            3: "堂食/外卖",
        }
        var status = {
            1: "启用",
            0: "禁用",
        }
        var tb = $table.DataTable({
            "lengthMenu": [[50, 75, 100, 150], [50, 75, 100, "All"]],
            ajax: {
                //url: "article/list_all",
                url: "articleManage/list_all",
                dataSrc: ""
            },
            stateSave: true,
            deferRender: true,
            ordering: false,
            columns: [
                {
                    title: "序号",
                    data: null,
                    render:function(data,type,row,meta) {
                        return meta.row + 1 +
                            meta.settings._iDisplayStart;
                    }
                },
                {
                    title: "菜品编码",
                    data: "serialNumber",
                },
                {
                    title: "类型",
                    data: "distributionModeId",
                    createdCell: function (td, tdData) {
                        $(td).html(mode[tdData]);
                    },
                    s_filter: true,
                    s_render: function (d) {
                        return mode[d];
                    }
                },
                {
                    title: "餐品分类",
                    data: "articleFamilyName",
                    s_filter: true,
                },
                {
                    title: "菜品类别",
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
                    title: "菜品名称",
                    data: "name",
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
                    title: "菜品品牌价格",
                    data: "price",
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
                    title: "排序",
                    data: "sort",
                },
                {
                    title: "状态",
                    data: "state",
                    s_filter: true,
                    s_render: function (d) {
                        return status[d];
                    },
                    createdCell: function (td, tdData) {
                        $(td).html(status[tdData]);
                    }
                },
                {
                    title: "操作",
                    data: "id",
                    createdCell: function (td, tdData, rowData, row) {
                        var operator = [
                            <s:hasPermission name="articleManage/delete">
                            C.createDelBtn(rowData.id,"articleManage/delete"),
                            </s:hasPermission>
                            <s:hasPermission name="articleManage/edit">
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
                        types:[".xlsx",".xls",".jpg",".png",".gif",".bmp"],
                        file: null,
                        uploadModal: false,
                        previewModal: false,
                        previewImg: '',
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
                        jsonTree: [],
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
                        chooseKitchen:[],
                        isCreate: true
                    },
                    methods: {
//                        checkedOne: function(item) {
//                            var idIndex = null;
//                            var obj = {id: item.id}
//                            var key = Object.keys(obj)[0];
//                            this.chooseKitchen.every(function(value, i) {
//                                if (value[key] === obj[key]) {
//                                    idIndex = i;
//                                    return false;
//                                }
//                                return true;
//                            });
//                            if (idIndex != null) {
//                                // 如果已经包含了该id, 则去除(单选按钮由选中变为非选中状态)
//                                this.chooseKitchen.splice(idIndex, 1)
//                            } else {
//
//                                // 选中该checkbox
//                                this.chooseKitchen.push(item)
//                            }
//
//                        },
//                        aa: function(item) {
//                            if(JSON.stringify(this.chooseKitchen).indexOf(JSON.stringify(item))>-1){
//                                return true
//                            }else {
//                                return false
//                            }
//                        },


                        itemDefaultChange: function (attr, item) {
                            if(item.isDefault){
                                item.isDefault = false;
                            }else{
                                item.isDefault = true;
                            }


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
                                    for (var k = 0; k < this.selectedUnit.unitList[i].articleUnitDetails.length; k++) {
                                        if (this.selectedUnit.unitList[i].articleUnitDetails[k].id == item.id) {
                                            this.selectedUnit.unitList[i].articleUnitDetails[k].isUsed = use;
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
                                priceDif: this.choiceArticleShow.mealAttr.choiceType == 0 ? (!art.fansPrice ? art.price : art.fansPrice) : 0,
                                articleId: art.id,
                                //photoSmall: art.photoSmall,
                                isDefault: false,
                            };
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
                                for (var i = 0; i < attr.articleUnitDetails.length; i++) {
                                    attr.articleUnitDetails[i].isUsed = 1;
                                    attr.articleUnitDetails[i].price = null;
                                }
                                this.selectedUnit.unitList.push(attr);
                            }


                        },
                        addMealItem: function (meal) {
                        	var that = this;
                            $.ajax({
                            	//url:"article/selectsingleItem",
                            	url:"articleManage/getProductsItem",
                            	type: "post",
                            	dataType:"json",
                            	success:function(result){
                            	    console.log('tianjia______',result)
                                    that.singleItem = result;
                            		/*if(result.success){
                            			that.singleItem = result.data;
                            		}*/
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
                                choiceType: 0,
                                choiceCount:1,
                            });
                        },
                        /*choiceMealTemp: function (e) {
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
                        },
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
                        },*/
                        create: function (article_type) {
                            var that = this;
                            action = "create";
                            this.m = {
                                //articleFamilyId: this.articlefamilys[0].id,
//                                recommendId:this.recommendList[0].id,
//                                virtualId:this.virtualList[0].id,
                                state:1,
                                supportTimes: [],
                                mealAttrs: [],
                                isRemind: false,
                                showDesc: true,
                                showBig: true,
                                isEmpty: false,
                                stockWorkingDay: 100,
                                stockWeekend: 50,
                                sort: 0,
                                units: [],
                                articleType: article_type,
                            };


                            if(this.m.articleType == 1) {
                                var list = {
                                    unitList: []
                                }
                                this.selectedUnit = list;
                                //$.post("unit/list_all", null, function (data) {
                                $.post("articleManage/getUnitsPackage", null, function (data) {
                                    console.log('data1111111',data)
                                    if(data && data.length > 0) {
                                        data.map(function (v,i) {
                                            v.articleUnitDetails = v.details
                                            v.details && v.details.length > 0 && v.details.map(function (m,n) {
                                                m.unitDetailId = m.id
                                            })
                                        })
                                    }
                                    that.isCreate = true;
                                    that.showform = true;
                                    that.unitList = data;
                                });
                            } else if(this.m.articleType == 2){
                                that.isCreate = true;
                                that.showform = true;
                            }


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
                        },
                        edit: function (model) {
                            console.log('编辑', model)
                            var that = this;

                            this.m = model

                            action = "edit";
                            if(this.m.articleType == 1) {
                                var list = {
                                    unitList: []
                                }
                                this.selectedUnit = list;
                                $.post("articleManage/getUnitsPackage", null, function (resultData) {
                                    var tempSelectedUnitUnitList = [];
                                    var tempUnitList = [];
                                    if(resultData && resultData.length > 0) {
                                        resultData.map(function (v,i) {
                                            v.unitId = v.id
                                            v.choiceType = 1
                                            v.details && v.details.length > 0 && v.details.map(function (m,n) {
                                                m.unitDetailId = m.id
                                            })
                                            v.articleUnitDetails = v.details


                                        })
                                        console.log('resultData',resultData)
                                        resultData.map(function (v,i) {
                                            if(model.units && model.units.length > 0){
                                                model.units.map(function (k,j) {
                                                    if(v.id == k.unitId){
                                                        v = k
                                                    }
                                                })
                                            }
                                            tempUnitList.push(v)
                                            if(v.isUsed == 1) {
                                                tempSelectedUnitUnitList.push(v)
                                            }
                                        })
                                        that.selectedUnit.unitList = JSON.parse(JSON.stringify(tempSelectedUnitUnitList));
                                        console.log('that.selectedUnit.unitList2',that.selectedUnit.unitList)
                                        that.unitList = JSON.parse(JSON.stringify(tempUnitList));
                                        console.log('that.unitList',that.unitList)
                                    }
                                    that.showform = true;
                                    that.isCreate = false;
                                });
                            }else if(this.m.articleType == 2) {
                                that.showform = true;
                                that.isCreate = false;
                            }


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
                            if (this.m.supportTimes == null ||  this.m.supportTimes.length <= 0) {//供应时间 非空验证
                                $("#supportTimeRemark").html("请选择餐品供应时间！");
                                return true;
                            }

                            return false;
                        },
                        save: function (e) {
                            if(this.m.articleType == 2){
                                var attrs = this.m.mealAttrs;
                                var flag = true;
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
                                    if(attr.choiceType == 0){
                                        flag = false;
                                    }
                                }
                                if(flag){
                                    C.errorMsg("套餐不能没有必选选项");
                                    return;
                                }
                            }

                            if (this.checkNull()) {//验证必选项(出参厨房和供应时间)
                                return;
                            }
                            var that = this;
                            var action = this.isCreate ? 'articleManage/create' : 'articleManage/edit';
                            this.m.articlePrices = this.unitPrices;
                            this.m.hasUnit = this.checkedUnit.join() || " ";
                            this.m.units = [];
                            if(this.m.articleType == 1) {
                                for (var i = 0; i < that.selectedUnit.unitList.length; i++) {
                                    this.m.units.push({
                                        unitId: that.selectedUnit.unitList[i].unitId,
                                        choiceType: that.selectedUnit.unitList[i].choiceType,
                                        articleUnitDetails: that.selectedUnit.unitList[i].articleUnitDetails,
                                    });
                                }
                            }

                            console.log('参数', this.m)
                            var jsonData = JSON.stringify(this.m);
                            $.ajax({
                                contentType: "application/json",
                                type: "post",
                                url: action,
                                data: jsonData,
                                datatype: "json",
                                success: function (result) {
                                    if (result.success) {
                                        that.showform = false;
                                        that.m = {};
                                        C.simpleMsg("保存成功");
                                        tb.ajax.reload(null, false);
                                    } else {
                                        C.errorMsg("保存失败");
                                    }
                                },
                                error: function (xhr, msg, e) {
                                    //var errorText = xhr.status + " " + xhr.statusText + ":" + action;
                                    C.errorMsg("保存失败");
                                }
                            });
                        },
                        createExcel: function () {
                            var that = this;
                            toastr.clear();
                            toastr.success("下载中...");
                            try{
                                $.post("articleManage/downloadTemplate", function (result) {
                                    console.log(' 模板下载', result)
                                    if (result.success){
                                        toastr.clear();
                                        toastr.success("下载成功");
                                        location.href = "articleManage/downloadExcel?path=" + result.data;
                                    }else {
                                        toastr.clear();
                                        toastr.error("下载出错");
                                    }
                                });
                            }catch (e){
                                toastr.clear();
                                toastr.error("系统异常，请刷新重试");
                            }
                        },
                        batchUpload: function () {
                            this.uploadModal = true;
                        },
                        cancelUploadModal: function () {
                            this.uploadModal = false;
                        },
                        saveUpload: function () {
                            var that = this;
                            var file = this.file;
                            if(file){
                                var filename = file.name;
                                if(this.imageNameVailed(filename)){
                                    var formdata = new FormData();
                                    formdata.append("file",file);
                                    var cut = this.cut;
                                    if(cut=="false"){//不压缩   (如过未申明type属性，则默认为压缩)
                                        formdata.append("type","false");
                                        console.log("不压缩");
                                    }else{
                                        formdata.append("type","true");
                                        console.log("压缩");
                                    }
                                    $.ajax({
                                        type: 'POST',
                                        url:"articleManage/importExcel",
                                        data:formdata,
                                        contentType:false,
                                        processData:false
                                    }).then(
                                        function (result){
                                            console.log('result',result)
                                            if(result.success){
                                                that.uploadModal = false;
                                                toastr.clear();
                                                toastr.success("上传成功");
                                                //that.$dispatch("success",result.data);
                                            }else{
                                                toastr.clear();
                                                toastr.error("上传失败");
                                                //that.$dispatch("error","上传失败，请压缩后再上传！");
                                            }
                                        },
                                        function (){
                                            toastr.clear();
                                            toastr.error("上传失败");
                                            console.log('文件上传失败')
                                            //that.$dispatch("error","文件上传失败");
                                        }
                                    );
                                }else{
                                    toastr.clear();
                                    toastr.error("文件类型错误");
                                    //that.$dispatch("error","文件类型错误")
                                }
                            } else {
                                toastr.clear();
                                toastr.error("请先上传文件");
                            }

                        },
                        uploadFile:function(e){
                            console.log('eeeeeeeeeeeeee',e)
                            var that =this;
                            var obj = e.target;
                            //var file = obj.files[0];
                            that.file = obj.files[0];

                        },
                        imageNameVailed:function(name){
                            if(name.indexOf(".")!=-1){
                                var lastName = name.substring(name.indexOf("."));
                                for(var i in this.types){
                                    var fix = this.types[i];
                                    if(lastName==fix){
                                        return true;
                                    }
                                }
                            }
                            return false;
                        },
                        preview: function (model) {
                            if(model == 1){
                                this.previewImg = "assets/pages/img/preview.png";
                                this.width = 1000
                            }else if(model == 2) {
                                this.previewImg = "assets/pages/img/previewDetail.png";
                                this.width = 700
                            } else if(model == 3) {
                                this.previewImg = "assets/pages/img/previewGif.png";
                                this.width = 700
                            }
                            this.previewModal = true;
                        },
                        cancelPreview: function () {
                            this.previewModal = false;
                        },
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
                        /*allUnitPrice: function () {
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
                        }*/
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


                        //$.post("articlefamily/list_all", null, function (data) {
                        $.post("brandArticleFamily/list_all", null, function (data) {
                            that.articlefamilys = data;
                        });


                        //$.post("recommend/list_all", null, function (data) {
                        $.post("articleManage/getRecommendPackage", null, function (data) {
                            that.recommendList = data;
                        });

                        //$.post("weightPackage/list_all", null, function (data) {
                        $.post("articleManage/getWeightPackage", null, function (data) {
                            that.weightPackageList = data;
                        });

                        //$.post("supporttime/list_all", null, function (data) {
                        $.post("articleManage/getSupportTimePackage", null, function (data) {
                            that.supportTimes = data;
                        });
                        /*$.post("kitchen/list_allStatus", null, function (data) {
                            that.kitchenList = data;
                        });
                        $.post("mealtemp/list_all", null, function (data) {
                            that.mealtempList = data;
                        });
                        $.post("virtual/listAll", null, function (data) {
                            that.virtualList = data;
                        });*/
                        /*$.ajax({
                            url:"shopInfo/list_one",
                            type:"post",
                            dataType:"json",
                            success:function (resultData) {
                                that.shop = resultData.data.shop;
                                that.brand = resultData.data.brand;
                            }
                        });*/
                        /*$.post("articleattr/list_all", null, function (data) {
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
                        });*/

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
