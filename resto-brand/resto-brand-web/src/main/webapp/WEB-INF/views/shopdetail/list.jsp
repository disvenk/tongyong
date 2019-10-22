<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6">
            <div class="portlet light bordered">
                <div class="portlet-title text-center">
                    <div class="caption">
                        <strong>店铺详情</strong>
                    </div>
                    &nbsp; <a class="btn green" v-if="m.id" @click="cleanShopDetail(m.id)">清缓存</a>
                </div>
                <div class="portlet-body">
                    <form role="form" class="form-horizontal" action="{{m.id?'shopdetail/modify':'shopdetail/create'}}" @submit.prevent="save" >
                        <div class="form-body" >
                            <div class="form-group">
                                <label class="col-sm-4 control-label">请选择品牌：</label>
                                <div class="col-sm-6">
                                        <span v-if="m.id">
                                            <select class="form-control" name="brandId" :value="m.brandId">
                                                <option v-for="brand in brands" :value="brand.id">
                                                    {{brand.brandName}}
                                                </option>
                                            </select>
                                        </span>
                                        <span v-if="!m.id">
                                           <input type="hidden" name="brandId" :value="brandId">
                                            <input type="text" class="form-control"
                                                   placeholder="请输入品牌名检索" v-model="inputName" required="required"/>
                                            <ul v-show="flg">
                                                <li v-for="brand in brands | filterBy inputName in 'brandName'"><a @click="selectBrand(brand)">{{brand.brandName}}</a></li>
                                            </ul>
                                        </span>

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">选择店铺模式：</label>
                                <div class="col-sm-6">
                                    <select class="form-control" name="shopMode" :value="m.shopMode">
                                        <option v-for="mode in  allMode" :value="mode.id">
                                            {{mode.name}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">选择店铺类型：</label>
                                <div class="col-sm-6">
                                    <select class="form-control" name="shopType" :value="m.shopType">
                                        <option v-for="type in  allType" :value="type.id">
                                            {{type.name}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">店铺名称：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="name" v-model="m.name" placeholder="必填"  required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">店铺序号：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="shopDetailIndex" v-model="m.shopDetailIndex">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">是否开启店铺：</label>
                                <div class="col-sm-6">
                                    <select class="form-control" name="isOpen" :value="m.isOpen">
                                        <option v-for="isOpenName in  isOpenNames" :value="isOpenName.id">
                                            {{isOpenName.value}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">店铺电话：</label>
                                <div class="col-sm-6">
                                    <input type="tel" class="form-control" name="phone" v-model="m.phone">
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-sm-4 control-label">店铺标签：</label>
                                <div class="col-sm-6">
                                    <input type="hidden" class="form-control" name="shopTag" v-model="m.shopTag">
                                    <label v-for="shopTag in shopTags">&nbsp;
                                        <button type="button" class="btn btn-success">{{shopTag}}</button>
                                        <i class="glyphicon glyphicon-remove" @click="removeShopTag(shopTag)"></i>
                                    </label>
                                    &nbsp;<button type="button" class="btn btn-primary" @click="openShopTagModel">新增</button>
                                </div>
                            </div>



                            <div class="form-group">
                                <label class="col-sm-4 control-label">店铺地址：</label>
                                <div class="col-sm-6">
                                    <div class="row">
                                        <div class="col-sm-4">
                                            <select class="form-control" name="areaId" v-model="m.areaId" @change="getSelectedArea">
                                                <option value="" disabled selected v-if="!m.id">选择大区</option>
                                                <option v-for="item in  areaList" :value="item.id">
                                                    {{item.areaName}}
                                                </option>
                                            </select>
                                        </div>
                                        <div class="col-sm-4">
                                            <select class="form-control" name="provinceId" v-model="m.provinceId" @change="getSelectedProvince">
                                                <!-- <option value="" >选择省</option> -->
                                                <option v-for="item in  provinceList" :value="item.id">
                                                    {{item.provinceName}}
                                                </option>
                                            </select>
                                        </div>
                                        <div class="col-sm-4">
                                            <select class="form-control" name="cityId" v-model="m.cityId">
                                                <!-- <option value="" >选择市</option> -->
                                                <option v-for="item in  cityList" :value="item.id">
                                                    {{item.cityName}}
                                                </option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="" style="margin-top: 6px;">
                                        <input type="text" class="form-control col-sm-12" name="address" v-model="m.address"
                                                @blur="showjwd" placeholder="必填" required="required">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">所在城市：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="city" v-model="m.city" placeholder="必填"
                                           required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">经度：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" readonly name="longitude" v-model="m.longitude"
                                           placeholder="请填写完店铺地址跟所在城市后自动获取经纬度">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">纬度：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" readonly name="latitude" v-model="m.latitude"
                                           placeholder="请填写完店铺地址跟所在城市后自动获取经纬度">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">营业时间：</label>
                                <div class="col-sm-6">
                                    <div class="input-group">
                                        <input type="text" class="form-control timepicker timepicker-no-seconds" name="openTime" @focus="initTime" v-model="m.openTime" readonly="readonly">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button">
                                                <i class="fa fa-clock-o"></i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">关门时间：</label>
                                <div class="col-sm-6">
                                    <div class="input-group">
                                        <input type="text" class="form-control timepicker timepicker-no-seconds" name="closeTime" v-model="m.closeTime" @focus="initTime" readonly="readonly">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button">
                                                <i class="fa fa-clock-o"></i>
                                          </button>
	                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">店铺图片：</label>
                                <div class="col-sm-6">
                                    <input type="hidden" name="photo" v-model="m.photo">
                                    <cert-file-upload class="form-control" @success="uploadSuccess"
                                                      @error="uploadError"></cert-file-upload>
                                    <img v-if="m.photo" :src="m.photo" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
                                </div>
                            </div>
                            <div class="form-group" v-if="m.shopMode == 2">
                                <label class="col-sm-4 control-label">电视叫号推送方式：</label>
                                <div  class="col-md-6 radio-list">
                                    <label style="margin-left: 16px;">
                                        <input type="radio" name="tvMode" @change="tvCallMode(1)" v-model="m.tvMode" value="1" />
                                        &nbsp;&nbsp;友盟推送
                                    </label>
                                    <label style="margin-left: 16px;">
                                        <input type="radio" name="tvMode" @change="tvCallMode(2)" v-model="m.tvMode" value="2" />
                                        &nbsp;&nbsp;小米推送
                                    </label>
                                    <label style="margin-left: 16px;">
                                        <input type="radio" name="tvMode" @change="tvCallMode(3)" v-model="m.tvMode" value="3" />
                                        &nbsp;&nbsp;本地推送
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">饿了吗店铺id：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="restaurantId" v-model="m.restaurantId">
                                </div>
                            </div>

                            <h4 class="text-center"><strong>支付宝配置信息</strong></h4>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">支付宝appId：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="aliAppId" :value="m.aliAppId">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">支付宝私钥：</label>
                                <div class="col-sm-6">
                                     <textarea class="form-control" rows="2" name="aliPrivateKey"
                                               v-model="m.aliPrivateKey"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">支付宝公钥：</label>
                                <div class="col-sm-6">
                                    <textarea class="form-control" rows="2" name="aliPublicKey"
                                              v-model="m.aliPublicKey"></textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">合作伙伴身份（PID）：</label>
                                <div class="col-sm-6">
                                    <textarea class="form-control" rows="2" name="aliSellerId"
                                              v-model="m.aliSellerId"></textarea>
                                </div>
                            </div>

                            <h4 class="text-center"><strong>微信配置信息</strong></h4>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">App Id：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control"  name="appid"
                                           v-model="m.appid">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">App Secret：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control"  name="appsecret"  v-model="m.appsecret">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">支付ID：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control"  name="mchid" v-model="m.mchid">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">支付秘钥：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control"  name="mchkey" v-model="m.mchkey">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">payChartPath：</label>
                                <div class="col-sm-6">
                                    <input type="hidden" name="payCertPath" v-model="m.payCertPath">
                                    <cert-file-upload2 class="form-control" @success="uploadSuccess2" @error="uploadError"></cert-file-upload2>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">关联母账户：</label>
                                <div class="col-sm-6">
                                    <select class="form-control" name="wxServerId" :value="m.wxServerId">
                                        <option value="">无</option>
                                        <option v-for="wxServer in  wxServers" :value="wxServer.id">
                                            {{wxServer.name}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">公众号是否与服务商所在公众号一致：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isSub" v-model="m.isSub" value="0"> 不一致
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isSub" v-model="m.isSub" value="1"> 一致
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">启动新的pos打印机制：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isPosNew" v-model="m.isPosNew" value="0"> 否
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isPosNew" v-model="m.isPosNew" value="1"> 是
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">餐品预点餐：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="orderBefore" v-model="m.orderBefore" value="0"> 未开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="orderBefore" v-model="m.orderBefore" value="1"> 开启
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">是否开启多人点餐：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openManyCustomerOrder" v-model="m.openManyCustomerOrder" value="0"> 否
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openManyCustomerOrder" v-model="m.openManyCustomerOrder" value="1"> 是
                                    </label>
                                </div>
                            </div>

                            <!--    仅   混合支付模式（大Boos模式）  显示         begin-->
                            <div class="form-group"  v-show="m.shopMode == 6">
                                <label class="col-md-4 control-label">允许先付：</label>
                                <div  class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="allowFirstPay" v-model="m.allowFirstPay" value="0"> 允许
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="allowFirstPay" v-model="m.allowFirstPay" value="1"> 不允许
                                    </label>
                                </div>
                            </div>
                            <div class="form-group"  v-show="m.shopMode == 6">
                                <label class="col-md-4 control-label">允许后付：</label>
                                <div  class="col-md-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="allowAfterPay" v-model="m.allowAfterPay" value="0"> 允许
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="allowAfterPay" v-model="m.allowAfterPay" value="1"> 不允许
                                    </label>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-sm-4 control-label">打印尺寸：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="pageSize" v-model="m.pageSize" value="0"> 48
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="pageSize" v-model="m.pageSize" value="1"> 42
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">是否开启美团外卖：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="openMeituan" v-model="m.openMeituan" value="0"> 未开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="openMeituan" v-model="m.openMeituan" value="1"> 开启
                                    </label>
                                    <span class="help-block" v-if="!m.id && m.openMeituan == 1">
                                        如果开启美团外卖，记得创建完店铺过来进行外卖配置
                                    </span>
                                </div>
                            </div>
                            <div class="form-group" v-if="m.id && m.openMeituan == 1">
                                <label class="col-sm-4 control-label">美团配置专用：</label>
                                <div class="col-sm-3">
                                    <a class="btn green" :href="'https://open-erp.meituan.com/storemap?developerId=104993&businessId=1&ePoiId='+m.id+'&timestamp='+timestamp+'&sign='+shanHuiSign+''" target="_blank">闪惠支付</a>
                                </div>
                                <div class="col-sm-3">
                                    <a class="btn green" :href="'https://open-erp.meituan.com/storemap?developerId=104993&businessId=2&ePoiId='+m.id+'&timestamp='+timestamp+'&sign='+outFoodSign+''" target="_blank">美团外卖</a>
                                </div>
                                <span class="help-block">
                                    <a href="https://developer.meituan.com/openapi#4.1.1" target="_blank">配置文档</a>
                                </span>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">pos版本：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="posVersion" v-model="m.posVersion" value="0"> 在线
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="posVersion" v-model="m.posVersion" value="1"> 离线 + 在线
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">是否开启R+外卖：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isTakeout" v-model="m.isTakeout" value="0"> 未开启
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isTakeout" v-model="m.isTakeout" value="1"> 开启
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">支付宝支付加密：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="aliEncrypt" v-model="m.aliEncrypt" value="0"> RSA
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="aliEncrypt" v-model="m.aliEncrypt" value="1"> RSA2
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">是否开启多动线：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="enableDuoDongXian" v-model="m.enableDuoDongXian" value="0">是
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="enableDuoDongXian" v-model="m.enableDuoDongXian" value="1">否
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">生成美食广场店铺二维码：</label>
                                <div class="col-sm-6">
                                    <input class="btn green" type="button" value="生成二维码" @click="sevenModeQRCode"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">是否开启EMQ推送：</label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="isOpenEmqPush" v-model="m.isOpenEmqPush"  value="1">是
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isOpenEmqPush" v-model="m.isOpenEmqPush"  value="0">否
                                    </label>
                                </div>
                            </div>

                        </div>
                        <input type="hidden" name="id" v-model="m.id"/>
                        <div class="text-center">
                            <input class="btn green" type="submit" value="保存"/>&nbsp;&nbsp;&nbsp;
                            <a class="btn default" @click="cancel">取消</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row form-div" v-if="showPosCode">
    <div class="col-md-offset-3 col-md-6">
    <div class="portlet light bordered">
    <div class="portlet-title text-center">
    <div class="caption">
    <strong>pos激活码</strong>
    </div>
    </div>
    <div class="portlet-body">
    <div class="form-group">

    <label class="radio-inline">
    <input value="{{posCode.substring(0,4)}}">-
    <input value="{{posCode.substring(4,8)}}">-
    <input value="{{posCode.substring(8,12)}}">-
    <input value="{{posCode.substring(12,16)}}">
    </label>


    </div>
    </div>
    <div class="text-center">
    <a class="btn default" @click="cancelPosCode">取消</a>
    </div>
    </form>
    </div>
    </div>
    </div>

    <div class="table-div">
        <div class="table-operator">
            <s:hasPermission name="shopdetail/add">
                <button class="btn green pull-right" @click="create">新建</button>
            </s:hasPermission>
        </div>
        <div class="clearfix"></div>
        <div class="table-filter">&nbsp;</div>
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>


    <!-- 店铺标签的录入框Begin -->
    <div class="modal fade" id="orderDetail" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title text-center">
                        <strong>店铺标签</strong>
                    </h4>
                </div>
                <center>
                    <form class="form-inline">
                        <div class="form-group">
                            <label for="exampleInputName">店铺标签：</label>
                            <input type="text" class="form-control" v-model="shopTagValue" id="exampleInputName">
                        </div>
                    </form></center>
                <div class="modal-footer">
                    <button type="button" class="btn btn-block btn-primary" data-dismiss="modal" @click="addShopTag">新增</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 店铺标签的录入框End -->
</div>




</div>

<script>
    Vue.component('cert-file-upload', {
        mixins: [fileUploadMix],
        data: function () {
            return {
                types: [".jpg"],
                postUrl: "shopdetail/uploadFile"
            }
        }
    });
    Vue.component('cert-file-upload2',{
        mixins:[fileUploadMix],
        data:function(){
            return {
                types:[".p12"],

                // postUrl:"wechatconfig/uploadFile"
                postUrl:"upload/file"
            }
        }
    });
    $(document).ready(function () {
        var vueObj;
        var C
        var cid = "#control";
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            ajax: {
                url: "shopdetail/list_all",
                dataSrc: ""
            },
            columns: [
                {
                    title: "品牌",
                    data: "brandName",
                },
                {
                    title: "店铺名称",
                    data: "name",
                },
                {
                    title: "店铺模式",
                    data: "shopmodeName",
                },
                {
                    title: "是否开启",
                    data: "isOpen",
                    createdCell: function (td, tdData) {
                        $(td).html(tdData ? "是" : "否");
                    }
                },
                {
                    title: "店铺地址",
                    data: "address",
                },
                {
                    title: "所在城市",
                    data: "city",
                },
                {
                    title: "店铺电话",
                    data: "phone",
                },
                {
                    title: "店铺开门时间",
                    data: "openTime",
                    "render": function (data, td, row) {
                        var str = "暂无数据";
                        if (data != null) {
                            str = new Date(data).format("hh:mm");
                        }
                        return str;
                    },
                },
                {
                    title: "店铺关门时间",
                    data: "closeTime",
                    "render": function (data, td, row) {
                        var str = "暂无数据";
                        if (data != null) {
                            str = new Date(data).format("hh:mm");

                        }
                        return str;
                    },
                },
                {
                    title: "店铺序号",
                    data: "shopDetailIndex",
                },
                {
                    title: "操作",
                    data: "id",
                    createdCell: function (td, tdData, rowData, row) {
                        var operator = [
                            <s:hasPermission name="shopdetail/delete">
                            C.createDelBtn(tdData, "shopdetail/delete"),
                            </s:hasPermission>
                            <s:hasPermission name="shopdetail/edit">
                            C.createEditBtn(rowData),
                            </s:hasPermission>
                            C.createCodeBtn(rowData)
                        ];
                        $(td).html(operator);
                    }
                }],
        });

        C = new Controller(null, tb);

        var vueObj = new Vue({
            el: "#control",
            mixins: [C.formVueMix],
            data: {
                allMode: [],
                allType: [],
                brands: [],
                isOpenNames: [{"id": "true", "value": "是"}, {"id": "false", "value": "否"}],
                wxServers:[],
                allMq:[],
                showPosCode: false,
                posCode: null,
                shopTags: [],
                flg : false, //用来标识是否显示模糊查询,
                brandId : "31946c940e194311b117e3fff5327215",//默认选中  测试品牌
                areaList: [],       // 行政大区列表
                provinceListAll: [],   // 省列表
                cityListAll: [],        // 城市列表
                provinceList: [],   // 省列表
                cityList: [],        // 城市列表
                timestamp : null,
                shanHuiSign : null,
                outFoodSign : null
            },
            created: function () {
                var that = this;
                $.post("wxServer/list_all", null, function (data) {
                    that.wxServers = data;
                });
                $.post("province/list_area", null, function (data) {
                    that.areaList = data;
                });
                $.post("province/list_province", null, function (data) {
                    that.provinceList = that.provinceListAll = data;
                });
                $.post("province/list_city", null, function (data) {
                    that.cityList = that.cityListAll = data;
                });

                that.initShopMode();
                that.initBrandMode();
                that.initMq();
            },
            watch: {
                'm.address': 'findJWnumber',
                'm.city': 'findJWnumber',
                "inputName" : function (val) {
                    if (val != this.lastSelectName) {
                        this.flg = true;
                    }
                    if (!val){
                        this.flg = false;
                    }
                }
            },
            methods: {
                // 修改大区
                getSelectedArea() {
                    var that = this;
                    if (that.m.areaId ) {
                        this.provinceList = this.provinceListAll.filter( function(item) { 
                            return item.zipCode == that.m.areaId
                        })
                        this.m.provinceId = this.provinceList[0].id

                        this.cityList = this.cityListAll.filter(function(item) {
                            return item.provinceId == that.m.provinceId
                        })
                        this.m.cityId = this.cityList[0].id
                    }
                },
                // 修改省份
                getSelectedProvince() {
                    var that = this;
                    this.cityList = this.cityListAll.filter(function(item) {
                        return item.provinceId == that.m.provinceId
                    })
                    this.m.cityId = this.cityList[0].id
                },
                cleanShopDetail:function (id) {
                    $.ajax({
                        type: "post",
                        url: "shopdetail/cleanShopDetail",
                        data: {id: id},
                        success: function (result) {
                            if (result.success) {
                                C.simpleMsg("清除缓存成功");
                            } else {
                                C.errorMsg(result.message);
                            }
                        },
                        error: function (xhr, msg, e) {
                            var errorText = xhr.status + " " + xhr.statusText + ":" + action;
                            C.errorMsg(errorText);
                        }
                    });
                },
                tvCallMode: function(number){
                    var tv = document.getElementsByName('tvMode');
                    for (var i = 0; i < tv.length; i++) {
                        tv[i].checked = false;
                    }
                    tv[number-1].checked = true;
                },
                cancelPosCode:function(){
                    this.showPosCode = false;
                },
                findJWnumber: function () {
                    var that = this;
                    var myGeo = new BMap.Geocoder();
                    myGeo.getPoint(this.m.address, function (point) {
                        if (point) {
                            that.m.longitude = point.lng;
                            that.m.latitude = point.lat;
                        } else {
                            console.log("您选择地址没有解析到结果!");
                        }
                    }, this.m.city);
                },
                uploadSuccess: function (url) {
                    $("[name='photo']").val(url).trigger("change");
                    C.simpleMsg("上传成功");
                },
                uploadSuccess2: function (url) {
                    $("[name='payCertPath']").val(url.data).trigger("change");
                    C.simpleMsg("上传成功");
                },
                uploadError: function (msg) {
                    C.errorMsg(msg);
                },
                edit: function (model) {
                    var that = this;
                    that.timestamp = new Date().getTime();
                    this.m = model;
                    //获取闪惠支付sign
                    that.getMeiTuanSign(1);
                    //获取美团外卖sign
                    that.getMeiTuanSign(2);
                    // console.log(JSON.stringify(model), '---')
                    var tem1 = this.m.openTime;
                    var tem2 = this.m.closeTime;
                    var open;
                    var close;
                    open = new Date(tem1).format("hh:mm");
                    close = new Date(tem2).format("hh:mm");
                    if (open == 'aN:aN') {
                        open = tem1;
                    }
                    if (close == 'aN:aN') {
                        close = tem2;
                    }
                    this.m.openTime = open;
                    this.m.closeTime = close;

                    //加载店铺标签
                    that.shopTags = [];
                    if(that.m.shopTag != null){
                        var shopTagStrs = that.m.shopTag.split(","); //字符分割
                        for (var i=0;i<shopTagStrs.length ;i++ ){
                            if(shopTagStrs[i] != ""){
                                that.shopTags.push(shopTagStrs[i]); //分割后的字符输出
                            }
                        }

                    }
                    this.openForm();
                },
                getMeiTuanSign : function (type) {
                    var that = this;
                    $.post("shopdetail/getSign", that.getMeiTuanConfig(type), function (data) {
                        if (type == 1) {
                            that.shanHuiSign = data;
                        } else {
                            that.outFoodSign = data;
                        }
                    });
                },
                getMeiTuanConfig: function (type) {
                    var object = {
                        developerId : "104993",
                        businessId : type == 1 ? "1" : "2",
                        ePoiId : this.m.id,
                        timestamp : this.timestamp,
                        signKey : "523qkoc6r6ry38jg"
                    }
                    return object;
                },
                showjwd: function () {
                    //  百度地图API功能
                    var map = new BMap.Map("allmap");
                    var point = new BMap.Point(116.331398, 39.897445);
                    map.centerAndZoom(point, 12);
                    // 创建地址解析器实例
                    var myGeo = new BMap.Geocoder();
                    // 将地址解析结果显示在地图上,并调整地图视野
                    myGeo.getPoint("北京市海淀区上地10街", function (point) {
                        if (point) {
                            map.centerAndZoom(point, 16);
                            map.addOverlay(new BMap.Marker(point));
                        } else {
                            alert("您选择地址没有解析到结果!");
                        }
                    }, "上海市");
                    console.log(point);
                    this.m.longitude = point.lng;
                    this.m.latitude = point.lat;
                },

                initTime: function () {
                    $(".timepicker-no-seconds").timepicker({
                        autoclose: true,
                        showMeridian: false,
                        minuteStep: 5
                    });
                },
                initShopMode: function () {
                    var that = this;
                    $.ajax({
                        url: "shopmode/list_all",
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            that.allMode = data;
                        }

                    });
                    $.ajax({
                        url: "shoptype/list_all",
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            that.allType = data;
                        }

                    });

                },
                showCode: function (model) {
                    var that = this;
                    $.ajax({
                        url: "shopdetail/getCode",
                        type: "post",
                        data: {id: model.id},
                        dataType: "json",
                        success: function (data) {
                            console.log(data.message);
                            that.posCode = data.message;
                            that.showPosCode = true;
                        }

                    });
                },
                initMq: function () {
                    var that = this;
                    $.ajax({
                        url: "mqConfig/list_all",
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            that.allMq = data.data;
                        }

                    });

                },
                initBrandMode: function () {
                    var that = this;
                    $.ajax({
                        url: "brand/list_all",
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            that.brands = data;
                        }
                    })
                },
                selectBrand : function (brand) {
                    this.inputName = brand.brandName;
                    this.lastSelectName = brand.brandName;
                    this.flg = false;
                    this.brandId = brand.id;
                },
                sevenModeQRCode: function(){
                    $.ajax({
                        url: "shopdetail/sevenModeQRCode",
                        type: "post",
                        data: {"shopId": this.m.id},
                        success: function (data) {
                            window.open(data);
                        }
                    })
                },
                openShopTagModel : function () {
                    $("#orderDetail").modal();
                },
                addShopTag : function () {
                    if (!this.m.shopTag){
                        this.m.shopTag = this.shopTagValue;
                    }else {
                        this.m.shopTag = this.m.shopTag + "," + this.shopTagValue;
                    }
                    this.shopTags.push(this.shopTagValue);
                    this.shopTagValue = "";
                },
                removeShopTag : function (shopTag) {
                    var that = this;
                    that.showDialog(function () {
                        that.m.shopTag = "";
//                        var shopTagValue = that.m.shopTag;
//                        var shopTags = shopTagValue.split(",");
//                        var newValue = "";
                        for (var value in that.shopTags){
                            if (shopTag == that.shopTags[value]){
                                that.shopTags.splice(value,1);
                            }
                        }
                        for(var a in that.shopTags){
                            that.m.shopTag = that.m.shopTag + "," + that.shopTags[a];
                        }
//                        newValue = newValue.substring(1);
//                        that.m.shopTag = newValue;
                    });
                },
                showDialog : function (successcbk) {
                    var cDialog = new dialog({
                        title:"提示",
                        content:"确定要删除吗？",
                        width:350,
                        ok:function(){
                            if(typeof successcbk=="function"){
                                successcbk();
                            }
                        },
                        cancel:function(){
                            if(typeof cancelcbk=="function"){
                                cancelcbk();
                            }
                        }
                    });
                    cDialog.showModal();
                }
            },
        });

        C.vue = vueObj;

    }());

</script>
