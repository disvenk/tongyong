let template =
 `<div class="row form-div" id="article-dialog-deit" >
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

                                    <div class="form-group col-md-4">
                                        <label class="col-md-5 control-label">菜品品牌定价</label>
                                        <div class="col-md-7" v-if="m.articleType==1">
                                            <input type="text" class="form-control" name="price" v-model="m.price"
                                                   required="required" pattern="\\d{1,10}(\\.\\d{1,2})?$"
                                                   title="价格只能输入数字,且只能保存两位小数！">
                                        </div>
                                        <div class="col-md-7" v-if="m.articleType==2">
                                            <input type="text" class="form-control" name="price" v-model="0"
                                                   readonly>
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
                                        <label class="col-md-5 control-label">门店售价</label>
                                        <div class="col-md-7" v-if="m.articleType==1">
                                            <input type="text" class="form-control" name="shopPrice" v-model="m.shopPrice">
                                        </div>
                                        <div class="col-md-7" v-if="m.articleType==2">
                                            <input type="text" class="form-control" name="shopPrice" v-model="0" readonly>
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
                                    <div class="form-group col-md-4" v-if="m.photoType == 1" style="width: 180px;margin-left:150px; display: inline-block;">
                                        <h5 style="text-align: center;">餐品图片(大图)</h5>
                                        <div class="" style="position: relative;text-align: center;">
                                            <input type="hidden" name="photoSmall" v-model="m.photoSmall">
                                            <img-file-upload style="height: 100px; opacity: 0;z-index: 10; position: relative;" class="form-control" @success="uploadSuccess"
                                                             @error="uploadError"></img-file-upload>
                                            <img v-if="m.photoSmall" :src="m.photoSmall" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                            <img v-else src="assets/pages/img/uploadImg.png"  width="100px" height="100px" style="position: absolute; top:0; left: 25px">
                                        </div>
                                        <h6 style="text-align: center;color:lightskyblue;" @click="preview(1)">预览图片规格与效果</h6>
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
                                        <h6 style="text-align: center;color:lightskyblue;" @click="preview(1)">预览图片规格与效果</h6>
                                    </div>

                                    <div class="form-group col-md-4" v-if="m.photoType == 3" style="width: 180px;margin-left:150px; display: inline-block;">
                                        <h4 style="text-align: center;">餐品图片(无图)</h4>
                                        <!--<div class="col-md-7">无图</div>-->
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
                                        <h6 style="text-align: center;color:lightskyblue;" @click="preview(1)">预览图片规格与效果</h6>
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
                                        <h6 style="text-align: center;color:lightskyblue;" @click="preview(1)">预览图片规格与效果</h6>
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
                                        <h6 style="text-align: center;color:lightskyblue;" @click="preview(2)">预览图片规格与效果</h6>
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
                                        <h6 style="text-align: center;color:lightskyblue;" @click="preview(3)">预览图片规格与效果</h6>
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
                                                   v-model="time.checked"> <span :class="{'text-danger':time.shopName}" >{{time.name}}({{time.discount+'%'}})</span> &nbsp;&nbsp;
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
										<label v-for="attr in selectedUnit.unitList">
                                            <input type="checkbox" v-model="attr.isUsed" v-bind:true-value="1" v-bind:false-value="0"
                                                   id="{{attr.id}}" @click="clickUnit(attr)"><span>{{attr.name}}</span> &nbsp;&nbsp;
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group col-md-12" v-for="select in selectedUnit.unitList" v-if="select.isUsed==1">
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
                                            <div class="flex-1" style="text-align: center;">
                                                <input type="checkbox" v-bind:true-value="1" v-bind:false-value="0"
                                                       @click="changeUsed(select,detail)" v-model="detail.isUsed"
                                                       style="width:70px;height:30px">
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

    <div class="modal fade" id="article-choice-dialog" v-if="choiceArticleShow.show">
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
</div>`
// var C = new Controller(null,tb);
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
var vueObj = 
    {
    // el: "#control",
    // mixins: [C.formVueMix],
    data() {
        return {
            types:[".xlsx",".xls",".jpg",".png",".gif",".bmp"],
            file: null,
            uploadModal: false,
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
            isCreate: true,
            previewModal: false,    // 图片预览
        }
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

            let index = this.selectedUnit.unitList.findIndex(u => u.id == attr.id)
            this.selectedUnit.unitList[index].isUsed = Number(!this.selectedUnit.unitList[index].isUsed)
            // var contains = false;
            // for (var i = 0; i < this.selectedUnit.unitList.length; i++) {

            //     if (this.selectedUnit.unitList[i].id == attr.id) {
            //         contains = true;
            //         // this.selectedUnit.unitList.$remove(attr);
            //         // this.selectedUnit.unitList.$remove(this.selectedUnit.unitList[i])
            //         this.selectedUnit.unitList[i].isUsed = 0;
            //         break;
            //     }
            // }

            if (this.selectedUnit.unitList[index].isUsed == 1) {
                for (var i = 0; i < attr.articleUnitDetails.length; i++) {
                    attr.articleUnitDetails[i].isUsed = 1;
                    attr.articleUnitDetails[i].price = null;
                }
                // this.selectedUnit.unitList.push(attr);
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
            if (! this.m.mealAttrs) {
                this.m.mealAttrs = []
            }
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
//             this.m = {
//                 //articleFamilyId: this.articlefamilys[0].id,
// //                                recommendId:this.recommendList[0].id,
// //                                virtualId:this.virtualList[0].id,
//                 state:1,
//                 supportTimes: [],
//                 mealAttrs: [],
//                 isRemind: false,
//                 showDesc: true,
//                 showBig: true,
//                 isEmpty: false,
//                 stockWorkingDay: 100,
//                 stockWeekend: 50,
//                 sort: 0,
//                 units: [],
//                 articleType: article_type,
//             };

            // console.log(7777777, JSON.stringify(this.m),this.m.articleType)

            if(this.m.articleType == 1) {
                // var list = {
                //     unitList: []
                // }
                // this.selectedUnit = list;
                // //$.post("unit/list_all", null, function (data) {
                // $.post("articleManage/getUnitsPackage", null, function (data) {
                //     console.log('data1111111',data)
                //     if(data && data.length > 0) {
                //         data.map(function (v,i) {
                //             v.articleUnitDetails = v.details
                //             v.details && v.details.length > 0 && v.details.map(function (m,n) {
                //                 m.unitDetailId = m.id
                //             })
                //         })
                //     }
                //     that.isCreate = true;
                //     that.showform = true;
                //     that.unitList = data;
                // });
                var list = {
                    unitList: []
                }
                this.selectedUnit = list;
                $.post("articleManage/getUnitsPackage", null, function (resultData) {
                    // console.log('data222222',resultData)
                    if(resultData && resultData.length > 0) {
                        resultData.map(function (v,i) {
                            v.articleUnitDetails = v.details
                            v.details && v.details.length > 0 && v.details.map(function (m,n) {
                                m.unitDetailId = m.id
                            })
                        })

                        resultData.map(function (v,i) {
                            if(that.m.units && that.m.units.length > 0){
                                that.m.units.map(function (k,j) {
                                    if(v.id == k.unitId){
                                        resultData[i] = that.m.units[j]
                                    }
                                })
                            }
                        })
                    }
                    // that.showform = true;
                    that.isCreate = false;
                    that.unitList = JSON.parse(JSON.stringify(resultData));
                    // that.selectedUnit.unitList = JSON.parse(JSON.stringify(resultData));
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
                    unitList: new HashMap()
                }
                this.selectedUnit = list;
                $.post("articleManage/getUnitsPackage", null, function (resultData) {
                    console.log('data222222',resultData)
                    if(resultData && resultData.length > 0) {
                        resultData.map(function (v,i) {
                            v.articleUnitDetails = v.details
                            v.details && v.details.length > 0 && v.details.map(function (m,n) {
                                m.unitDetailId = m.id
                            })
                        })

                        resultData.map(function (v,i) {
                            if(model.units && model.units.length > 0){
                                model.units.map(function (k,j) {
                                    if(v.id == k.unitId){
                                        resultData[i] = model.units[j]
                                    }
                                })
                            }
                        })
                    }
                    that.showform = true;
                    that.isCreate = false;
                    that.unitList = JSON.parse(JSON.stringify(resultData));
                    // that.selectedUnit.unitList = JSON.parse(JSON.stringify(resultData));
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
                if(!flag){
                    C.errorMsg("套餐不能没有必选选项");
                    return;
                }
            }

            if (this.checkNull()) {//验证必选项(出参厨房和供应时间)
                return;
            }
            var that = this;
            // var action = this.isCreate ? 'articleManage/create' : 'articleManage/edit';
            var action = 'menuarticle/modify';
            this.m.articlePrices = this.unitPrices;
            this.m.hasUnit = this.checkedUnit.join() || " ";
            this.m.units = [];
            if(this.m.articleType == 1) {
                for (var i = 0; i < that.selectedUnit.unitList.length; i++) {
                    if (that.selectedUnit.unitList[i].isUsed == 1) {
                        this.m.units.push({
                            unitId: that.selectedUnit.unitList[i].id,
                            choiceType: that.selectedUnit.unitList[i].choiceType,
                            articleUnitDetails: that.selectedUnit.unitList[i].articleUnitDetails,
                        });
                    }
                }
            }

            console.log('参数', this.m)
            delete this.m.createTime
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

    watch: {
        'supportTimes': {
            handler: function(newVal, oldVal) {
                var that = this;
                $("#supportTimeRemark").html("");   //清除错误提示
                that.m.supportTimes = that.supportTimes.filter(t => t.checked == true).map(t => t.id)
                // console.log(that.m.supportTimes, '---------', that.supportTimes)
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
            deep: true
        },
    }
}


Vue.component('edit-article', {
    props: {
        article: {
            type: Object
        },
        showArticleEdit: {
            type: Boolean
        }
    },
    data() {
        return {
            m: {}
        }
    },
    created: function () {
        // console.log(3333333333)
        this.m = $.extend(true, {}, this.article);
        var that = this;

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
            that.supportTimes = data.map(st => {
                if (that.m.supportTimes.findIndex(t => t==st.id) !== -1) {
                    st = $.extend({}, st)
                    st.checked = true
                } else {
                    st.checked = false
                }
                return st;
            });

        });
        that.create()
    },
    template: template,
    mixins: [vueObj],
    methods: {
        // 部分数据初始化
        create: function (article_type) {
            var that = this;
            console.log(7777777, JSON.stringify(this.m))
            if(this.m.articleType == 1) {
                var list = {
                    unitList: new HashMap()
                }
                this.selectedUnit = list;
                $.post("articleManage/getUnitsPackage", null, function (resultData) {
                    // console.log('data222222',resultData)
                    if(resultData && resultData.length > 0) {
                        resultData.map(function (v,i) {
                            v.unitId = v.id
                            v.choiceType = 1
                            v.details && v.details.length > 0 && v.details.map(function (m,n) {
                                m.unitDetailId = m.id
                            })
                            v.articleUnitDetails = v.details
                        })

                        resultData.map(function (v,i) {
                            if(that.m.units && that.m.units.length > 0){
                                
                                that.m.units.map(function (k,j) {
                                    if(v.id == k.unitId) {
                                        resultData[i] = that.m.units[j]
                                    }
                                })
                            }
                        })
                    }
                    // that.showform = true;
                    that.isCreate = false;
                    // that.unitList = JSON.parse(JSON.stringify(resultData));
                    that.selectedUnit.unitList = JSON.parse(JSON.stringify(resultData));
                });

            } else if(this.m.articleType == 2){
                that.isCreate = true;
                that.showform = true;
            }


        },
        // 保存编辑
        save: function(e) {
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
                            toastr.clear();
		                    toastr.error("默认选中项与必选数量不等");
                            //C.errorMsg("默认选中项与必选数量不等");
                            return;
                        }
                        flag = false;
                    }
                    if(flag){
                        toastr.clear();
                        toastr.error("套餐不能没有必选选项");
                        return;
                    }
                }
            }

            if (this.checkNull()) {//验证必选项(出参厨房和供应时间)
                return;
            }
            var that = this;
            // var action = this.isCreate ? 'articleManage/create' : 'articleManage/edit';
            var action = 'menuarticle/modify';
            this.m.articlePrices = this.unitPrices;
            this.m.hasUnit = this.checkedUnit.join() || " ";
            this.m.units = [];
            if(this.m.articleType == 1) {
                // console.log(JSON.stringify(that.selectedUnit.unitList))
                for (var i = 0; i < that.selectedUnit.unitList.length; i++) {
                    if (that.selectedUnit.unitList[i].isUsed == 1) {
                        this.m.units.push({
                            unitId: that.selectedUnit.unitList[i].unitId,
                            choiceType: that.selectedUnit.unitList[i].choiceType,
                            articleUnitDetails: that.selectedUnit.unitList[i].articleUnitDetails,
                        });
                    }
                }
            }

            delete this.m.createTime
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
                        // that.showform = false;
                        // that.m = {};
                        // C.simpleMsg("保存成功");
                        toastr.clear();
                        toastr.success("保存成功");
                        that.$dispatch('close', '关闭编辑模态框')
                        // tb.ajax.reload(null, false);
                    } else {
                        // C.errorMsg("保存失败");
                        toastr.clear();
		                toastr.error("保存失败");
                    }
                },
                error: function (xhr, msg, e) {
                    //var errorText = xhr.status + " " + xhr.statusText + ":" + action;
                    // C.errorMsg("保存失败");
                    toastr.clear();
		            toastr.error ("保存失败");
                }
            });
        },
        cancel: function() {
            // alert(333)
            this.showArticleEdit = false;
            this.$dispatch('close', '关闭编辑模态框')
        }
    }
	
});
