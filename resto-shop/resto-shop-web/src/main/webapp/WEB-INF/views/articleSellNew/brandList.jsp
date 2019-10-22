<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="control">
    <h2 class="text-center"><strong>品牌菜品销售报表</strong></h2><br/>
    <div class="row" id="searchTools">
        <div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 20px;">
                <label>开始时间：
                <input type="text" id="beginDate" class="form-control form_datetime" :value="searchDate.beginDate" v-model="searchDate.beginDate" readonly="readonly">
                </label>
                </div>
                <div class="form-group" style="margin-right: 20px;">
                <label>结束时间：
                <input type="text" id="endDate" class="form-control form_datetime" :value="searchDate.endDate" v-model="searchDate.endDate" readonly="readonly">
                </label>
                <button type="button" class="btn btn-primary" @click="today"> 今日</button>
                <button type="button" class="btn btn-primary" @click="yesterDay">昨日</button>
                <button type="button" class="btn btn-primary" @click="week">本周</button>
                <button type="button" class="btn btn-primary" @click="month">本月</button>
                <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>
                <%--<a type="button" href="orderReport/historyOrderList?reportName=菜品销量报表" style="float: right;" class="btn green ajaxify">查询历史报表数据</a>--%>
                <button type="button" style="float: right;" class="btn btn-primary" @click="createBrnadArticleTotal" v-if="state == 1">下载报表</button>
                <button type="button" class="btn btn-default" disabled="disabled" v-if="state == 2">下载数据过多，正在生成中。请勿刷新页面</button>
                <button type="button" class="btn btn-success" @click="download" v-if="state == 3">已完成，点击下载</button>
                <br/>
        </div>
        </form>
        <div>
            <br/>
            <br/>
            <div>
                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane active">
                        <div class="panel panel-success">
                            <div class="panel-heading text-center">
                                <strong style="margin-right:100px;font-size:22px">
                                    品牌菜品销售表
                                </strong>
                            </div>
                            <div class="panel-body">
                                <table id="brandMarketing" class="table table-striped table-bordered table-hover" style="width: 100%">
                                    <thead>
                                    <tr>
                                        <th>品牌名称</th>
                                        <th>菜品总销量(份)</th>
                                        <th>菜品销售总额(元)</th>
                                        <th>折扣总额(元)</th>
                                        <th>退菜总数(份)</th>
                                        <th>退菜总额(元)</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <template v-if="brandReport.brandName != null">
                                        <tr>
                                            <td><strong>{{brandReport.brandName}}</strong></td>
                                            <td>{{brandReport.totalNum}}</td>
                                            <td>{{brandReport.sellIncome}}</td>
                                            <td>{{brandReport.discountTotal}}</td>
                                            <td>{{brandReport.refundCount}}</td>
                                            <td>{{brandReport.refundTotal}}</td>
                                        </tr>
                                    </template>
                                    <template v-else>
                                        <tr>
                                            <td align="center" colspan="6">暂时没有数据...</td>
                                        </tr>
                                    </template>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                </div>

                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist" id="ulTab">
                    <li role="presentation" class="active" @click="chooseType(1)">
                        <a href="#dayReport" aria-controls="dayReport" role="tab" data-toggle="tab">
                            <strong>单品</strong>
                        </a>
                    </li>
                    <li role="presentation" @click="chooseType(2)">
                        <a href="#revenueCount" aria-controls="revenueCount" role="tab" data-toggle="tab">
                            <strong>套餐</strong>
                        </a>
                    </li>
                    <li role="presentation" @click="chooseType(3)">
                        <a href="#articleType" aria-controls="articleType" role="tab" data-toggle="tab">
                            <strong>类别</strong>
                        </a>
                    </li>
                    <li role="presentation" @click="chooseType(4)">
                        <a href="#mealSeals" aria-controls="mealSeals" role="tab" data-toggle="tab">
                            <strong>其他销量</strong>
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <!-- 单品 -->
                    <div role="tabpanel" class="tab-pane active" id="dayReport">
                        <!-- 品牌菜品销售表(单品)   -->
                        <div class="panel panel-success">
                            <div class="panel-heading text-center">
                                <strong style="margin-right:100px;font-size:22px">品牌菜品销售表(单品)</strong>
                            </div>
                            <div class="panel-body">
                                <table id="brandArticleUnitTable" class="table table-striped table-bordered table-hover"
                                       style="width: 100%;">
                                </table>
                            </div>
                        </div>
                    </div>

                    <!-- 套餐 -->
                    <div role="tabpanel" class="tab-pane" id="revenueCount">
                        <div class="panel panel-primary" style="border-color:white;">
                            <!-- 品牌菜品销售表(套餐) -->
                            <div class="panel panel-success">
                                <div class="panel-heading text-center">
                                    <strong style="margin-right:100px;font-size:22px">品牌菜品销售表(套餐)</strong>
                                </div>
                                <div class="panel-body">
                                    <table id="brandArticleFamilyTable" class="table table-striped table-bordered table-hover"
                                           style="width: 100%;">
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 类别 -->
                    <div role="tabpanel" class="tab-pane" id="articleType">
                        <div class="panel panel-info" style="border-color:white;">
                            <!-- 品牌菜品销售表(类别) -->
                            <div class="panel panel-success">
                                <div class="panel-heading text-center">
                                    <strong style="margin-right:100px;font-size:22px">品牌菜品销售表(类别)</strong>
                                </div>
                                <div class="panel-body">
                                    <table id="brandArticleTypeTable" class="table table-striped table-bordered table-hover"
                                           style="width: 100%;">
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 其他销量 -->
                    <div role="tabpanel" class="tab-pane" id="mealSeals">
                        <div class="panel panel-info" style="border-color:white;">
                            <!-- 品牌菜品销售表(其他销量) -->
                            <div class="panel panel-success">
                                <div class="panel-heading text-center">
                                    <strong style="margin-right:100px;font-size:22px">品牌菜品销售表(其他销量)</strong>
                                </div>
                                <div class="panel-body">
                                    <table id="brandMealSealsTable" class="table table-striped table-bordered table-hover"
                                           style="width: 100%;">
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="mealAttrModal" tabindex="-1" role="dialog" data-backdrop="static">
    <div class="modal-dialog modal-full">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" @click="closeModal"></button>
            </div>

            <div class="modal-body" id="reportModal1"></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-info btn-block" data-dismiss="modal" aria-hidden="true" @click="closeModal">关闭</button>
                </button>
            </div>
        </div>
    </div>
</div>
</div>
<script src="assets/customer/date.js" type="text/javascript"></script>
<script src="assets/customer/components/dateSearch.js" type="text/javascript"></script>
<script>
    //时间插件
    $('.form_datetime').datetimepicker({
        endDate:new Date(),
        // minView:"month",
        minView: 0,
        maxView:"month",
        autoclose:true,//选择后自动关闭时间选择器
        todayBtn:true,//在底部显示 当天日期
        todayHighlight:true,//高亮当前日期
        format:"yyyy-mm-dd hh:ii:ss",
        startView:"month",
        language:"zh-CN"
    });

var sort = "desc";
var brandUnitAPI = null;
var brandFamilyAPI = null;
var brandTypeAPI = null;
var brandMealSealsAPI = null;
var vueObj = new Vue({
    el : "#control",
    mixins: [dateSearch],
    data : {
        brandReport : {},
        brandInfo : {},
        searchDate : {
            beginDate : "",
            endDate : "",
        },
        currentType:1,//当前选中页面
        brandArticleUnitTable:{},//单品dataTables对象
        brandArticleFamilyTable:{},//套餐datatables对象
        brandArticleTypeTable:{},//类别datatablees对象
		brandMealSealsTable : {}, //其他销量datatable对象
        api:{},
        resultData:[],
        state : 1,
        brandArticleUnit :[],
        brandArticleFamily　:[],
        brandArticleType : [],
        brandArticleUnitList :[],
        brandArticleFamilyList　:[],
		brandMealSealsDtos:[],
        object : {},
        length : 0,
        start : 0,
        end : 200,
        startPosition : 206,
        index : 1
    },
    created : function() {
        var date0 = new Date().format("yyyy-MM-dd 00:00:00"); 
        var date = new Date().format("yyyy-MM-dd hh:mm:ss");
        this.searchDate.beginDate = date0;
        this.searchDate.endDate = date;
        this.initDataTables();
        this.searchInfo();
    },
    methods : {
        initDataTables:function () {
            //that代表 vue对象
            var that = this;
            //单品datatable对象
            that.brandArticleUnitTable=$("#brandArticleUnitTable").DataTable({
                lengthMenu: [ [20, 75, 100, -1], [20, 75, 100, "All"] ],
                order: [[ 3, "desc" ]],
                columns : [
                    {
                        title : "菜品类型",
                        data : "typeName",
                        orderable : false
                    },
                    {
                        title : "菜品类别",
                        data : "articleFamilyName",
                        orderable : false,
                        s_filter: true
                    },
                    {
                        title : "菜品名称",
                        data : "articleName",
                        orderable : false
                    },
                    {
                        title : "销量(份)",
                        data : "brandSellNum",
                        createdCell: function (td, tdData, rowData) {
                            var text = tdData;
                            if (rowData.weight > 0){
                                text = text + "/" + rowData.weight + "斤";
//                                var button = $("<button class='btn green btn-xs'>查看详情</button>");
//                                button.click(function () {
//                                    openModal(tdData);
//                                });
//                                text = [text, button];
							}
                            $(td).html(text);
                        }
                    },
                    {
                        title : "销量占比",
                        data : "numRatio",
                        orderable : false
                    },
                    {
                        title : "销售额(元)",
                        data : "salles"
                    },
                    {
                        title : "折扣金额(元)",
                        data : "discountMoney"
                    },
                    {
                        title : "销售额占比",
                        data : "salesRatio",
                        orderable : false
                    },
                    {
                        title:"退菜数量" ,
                        data:"refundCount"
                    },
                    {
                        title:"退菜金额" ,
                        data:"refundTotal"
                    },
                    {
                        title:"点赞数量" ,
                        data:"likes"
                    }
                ],
                initComplete: function () {
                	brandUnitAPI = this.api();
                	that.brandUnitTable();
                }
            });
            //套餐datatable对象
            that.brandArticleFamilyTable=$("#brandArticleFamilyTable").DataTable({
            	lengthMenu: [ [20, 75, 100, -1], [20, 75, 100, "All"] ],
                order: [[ 3, "desc" ]],
                columns : [
                    {
                        title : "菜品类型",
                        data : "typeName",
                        orderable : false
                    },
                    {
                        title : "菜品类别",
                        data : "articleFamilyName",
                        orderable : false,
                        s_filter: true
                    },
                    {
                        title : "菜品名称",
                        data : "articleName",
                        orderable : false
                    },
                    {
                        title : "销量(份)",
                        data : "brandSellNum",
                    },
                    {
                        title : "销量占比",
                        data : "numRatio",
                        orderable : false
                    },
                    {
                        title : "销售额(元)",
                        data : "salles"
                    },
                    {
                        title : "折扣金额(元)",
                        data : "discountMoney"
                    },
                    {
                        title : "销售额占比",
                        data : "salesRatio",
                        orderable : false
                    },
                    {
                        title:"退菜数量" ,
                        data:"refundCount"
                    },
                    {
                        title:"退菜金额" ,
                        data:"refundTotal"
                    },
                    {
                        title:"点赞数量" ,
                        data:"likes"
                    },
                    {
                        title: "套餐属性",
                        data: "articleId",
                        orderable : false,
                        createdCell: function (td, tdData, rowData) {
                            var button = $("<button class='btn green'>查看详情</button>");
                            button.click(function () {
                                openModal(tdData);
                            })
                            $(td).html(button);
                        }
                    }
                ],
                initComplete: function () {
                	brandFamilyAPI = this.api();
                	that.brandFamilyTable();
                }
            });

            //类别datatable对象
            that.brandArticleTypeTable=$("#brandArticleTypeTable").DataTable({
                lengthMenu: [ [20, 75, 100, -1], [20, 75, 100, "All"] ],
                order: [[ 2, "desc" ]],
                columns : [
                    {
                        title : "菜品类别",
                        data : "articleFamilyName",
                        orderable : false
                    },
                    {
                        title : "销量(份)",
                        data : "brandSellNum",
                    },
                    {
                        title : "销量占比",
                        data : "numRatio",
                        orderable : false
                    },
                    {
                        title : "销售额(元)",
                        data : "salles"
                    },
                    {
                        title : "折扣金额(元)",
                        data : "discountMoney"
                    },
                    {
                        title : "销售额占比",
                        data : "salesRatio",
                        orderable : false
                    },
                    {
                        title:"退菜数量" ,
                        data:"refundCount"
                    },
                    {
                        title:"退菜金额" ,
                        data:"refundTotal"
                    },
                    {
                        title:"点赞数量" ,
                        data:"likes"
                    }
                ],
                initComplete: function () {
                    brandTypeAPI = this.api();
                }
            });
			//其他销量datatable对象
			that.brandMealSealsTable=$("#brandMealSealsTable").DataTable({
				lengthMenu: [ [20, 75, 100, -1], [20, 75, 100, "All"] ],
				order: [[ 2, "desc" ]],
				columns : [
					{
						title : "菜品名称",
						data : "articleName",
						orderable : false
					},
					{
						title : "销量(份)",
						data : "brandSellNum",
					},
					{
						title : "销售额(元)",
						data : "salles"
					},
					{
						title:"退菜数量" ,
						data:"refundCount"
					},
					{
						title:"退菜金额" ,
						data:"refundTotal"
					}
				],
				initComplete: function () {
					brandMealSealsAPI = this.api();
				}
			});
        },
        //切换单品、套餐 type 1:单品 2:套餐 3:类别
        chooseType:function (type) {
          this.currentType= type;
        },
        searchInfo : function() {
            var that = this;
            var timeCha = new Date(that.searchDate.endDate).getTime() - new Date(that.searchDate.beginDate).getTime();
            if(timeCha < 0){
                toastr.clear();
                toastr.error("开始时间应该少于结束时间！");
                return false;
            }else if(timeCha > 2678200000){
                toastr.clear();
                toastr.error("暂时未开放大于一月以内的查询！");
                return false;
            }
            toastr.clear();
            toastr.success("查询中...");
        	try{
	            var api1 = brandUnitAPI;
	            var api2 = brandFamilyAPI;
                var api3 = brandTypeAPI;
				var api4 = brandMealSealsAPI;
                $.post("articleSell/queryOrderArtcileNew", this.getDate(), function(result) {
                    if(result.success == true){
                        toastr.clear();
                        toastr.success("查询成功");
                        that.brandReport = result.data.brandReport;
                        that.brandArticleUnit = result.data.brandArticleUnit;
                        that.brandArticleFamily = result.data.brandArticleFamily;
                        that.brandArticleType = result.data.brandArticleType;
						that.brandMealSealsDtos = result.data.brandMealSalesDtos;
                        //清空brandUnitDatatable的column搜索条件
                        api1.search('');
                        var column1 = api1.column(1);
                        column1.search('', true, false);
                        //清空brandFamilyDatatable的column搜索条件
                        api2.search('');
                        var column2 = api2.column(1);
                        column2.search('', true, false);
                        api3.search('');
                        that.brandArticleUnitTable.clear();
                        that.brandArticleUnitTable.rows.add(result.data.brandArticleUnit).draw();
                        //重绘搜索列
                        that.brandUnitTable();
                        that.brandArticleFamilyTable.clear();
                        that.brandArticleFamilyTable.rows.add(result.data.brandArticleFamily).draw();
                        //重绘搜索列
                        that.brandFamilyTable();
                        that.brandArticleTypeTable.clear();
                        that.brandArticleTypeTable.rows.add(result.data.brandArticleType).draw();
						//其他销量
						api4.search('');
						that.brandMealSealsTable.clear();
						that.brandMealSealsTable.rows.add(result.data.brandMealSalesDtos).draw();
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
        download : function(){
            window.location.href = "articleSell/downloadBrnadArticle?path="+this.object.path+"";
            this.state = 1;
            this.start = 0;
            this.end = 200;
            this.startPosition = 206;
            this.index = 1;
        },
        getDate : function(){
            var data = {
                beginDate : this.searchDate.beginDate,
                endDate : this.searchDate.endDate,
            };
            return data;
        },
        createBrnadArticleTotal : function(){
            try {
                var that = this;
                that.brandInfo = that.brandReport;
                that.object = that.getDate();
                that.object.type =  that.currentType;
                that.object.brandReport =  that.brandInfo;
                switch (that.currentType) {
                    case 1:
                        that.brandArticleUnitList = that.brandArticleUnit;
                        if (that.brandArticleUnitList.length <= 200) {
                            that.object.brandArticleUnit = that.brandArticleUnitList;
                            $.post("articleSell/createBrnadArticle",that.object,function(result){
                                if(result.success){
                                    window.location.href = "articleSell/downloadBrnadArticle?path="+result.data+"";
                                }else{
									toastr.clear();
                                    toastr.error("下载报表出错");
                                }
                            });
                        }else{
                            that.state = 2;
                            that.length = Math.ceil(that.brandArticleUnitList.length/200);
                            that.object.brandArticleUnit = that.brandArticleUnitList.slice(that.start,that.end);
                            $.post("articleSell/createBrnadArticle",that.object,function(result){
                                if(result.success){
                                    that.object.path = result.data;
                                    that.start = that.end;
                                    that.end = that.start + 200;
                                    that.index++;
                                    that.appendBrandUnitExcel();
                                }else{
                                    that.state = 1;
                                    that.start = 0;
                                    that.end = 200;
                                    that.startPosition = 206;
                                    that.index = 1;
                                    toastr.clear();
                                    toastr.error("生成报表出错");
                                }
                            });
                        }
                        break;
                    case 2:
                        that.brandArticleFamilyList = that.brandArticleFamily;
                        if (that.brandArticleFamilyList.length <= 200) {
                            that.object.brandArticleFamily = that.brandArticleFamilyList;
                            $.post("articleSell/createBrnadArticle",that.object,function(result){
                                if(result.success){
                                    window.location.href = "articleSell/downloadBrnadArticle?path="+result.data+"";
                                }else{
                                    toastr.clear();
                                    toastr.error("下载报表出错");
                                }
                            });
                        }else{
                            that.state = 2;
                            that.length = Math.ceil(that.brandArticleFamilyList.length/200);
                            that.object.brandArticleFamily = that.brandArticleFamilyList.slice(that.start,that.end);
                            $.post("articleSell/createBrnadArticle",that.object,function(result){
                                if(result.success){
                                    that.object.path = result.data;
                                    that.start = that.end;
                                    that.end = that.start + 200;
                                    that.index++;
                                    that.appendBrandFamilyExcel();
                                }else{
                                    that.state = 1;
                                    that.start = 0;
                                    that.end = 200;
                                    that.startPosition = 206;
                                    that.index = 1;
                                    toastr.clear();
                                    toastr.error("生成报表出错");
                                }
                            });
                        }
                        break;
                    case 3:
                            var brandArticleType = that.brandArticleType.sort(that.keysert("brandSellNum","desc"));
                            that.object.brandArticleType = brandArticleType;
                            $.post("articleSell/createBrnadArticle",that.object,function(result){
                                if(result.success){
                                    window.location.href = "articleSell/downloadBrnadArticle?path="+result.data+"";
                                }else{
                                    toastr.clear();
                                    toastr.error("下载报表出错");
                                }
                            });
                        break;
					case 4:
						that.object.brandMealSeals = that.brandMealSealsDtos;
						$.post("articleSell/createBrnadArticle",that.object,function (result) {
							if(result.success){
								window.location.href="articleSell/downloadBrnadArticle?path="+result.data+"";
							}else{
								toastr.clear();
								toastr.error("下载报表出错");
							}
						});
						break;
                }
            }catch (e){
                that.state = 1;
                that.start = 0;
                that.end = 200;
                that.startPosition = 206;
                that.index = 1;
				toastr.clear();
                toastr.error("系统异常，请刷新重试");
            }
        },
        appendBrandUnitExcel : function () {
            var that = this;
            try {
                if (that.index == that.length) {
                    that.object.brandArticleUnit = that.brandArticleUnitList.slice(that.start);
                } else {
                    that.object.brandArticleUnit = that.brandArticleUnitList.slice(that.start, that.end);
                }
                that.object.startPosition = that.startPosition;
                $.post("articleSell/appendToBrandExcel", that.object, function (result) {
                    if (result.success) {
                        that.start = that.end;
                        that.end = that.start + 200;
                        that.startPosition = that.startPosition + 200;
                        that.index++;
                        if (that.index - 1 == that.length) {
                            that.state = 3;
                        } else {
                            that.appendBrandUnitExcel();
                        }
                    } else {
                        that.state = 1;
                        that.start = 0;
                        that.end = 200;
                        that.startPosition = 206;
                        that.index = 1;
                        toastr.clear();
                        toastr.error("生成报表出错");
                    }
                });
            }catch (e){
                that.state = 1;
                that.start = 0;
                that.end = 200;
                that.startPosition = 206;
                that.index = 1;
                toastr.clear();
                toastr.error("系统异常，请刷新重试");
            }
        },
        appendBrandFamilyExcel :function () {
            var that = this;
            try {
                if (that.index == that.length) {
                    that.object.brandArticleFamily = that.brandArticleFamilyList.slice(that.start);
                } else {
                    that.object.brandArticleFamily = that.brandArticleFamilyList.slice(that.start, that.end);
                }
                that.object.startPosition = that.startPosition;
                $.post("articleSell/appendToBrandExcel", that.object, function (result) {
                    if (result.success) {
                        that.start = that.end;
                        that.end = that.start + 200;
                        that.startPosition = that.startPosition + 200;
                        that.index++;
                        if (that.index - 1 == that.length) {
                            that.state = 3;
                        } else {
                            that.appendBrandFamilyExcel();
                        }
                    } else {
                        that.state = 1;
                        toastr.clear();
                        toastr.error("生成报表出错");
                    }
                });
            }catch (e){
                that.state = 1;
                that.start = 0;
                that.end = 200;
                that.startPosition = 206;
                that.index = 1;
                toastr.clear();
                toastr.error("系统异常，请刷新重试");
            }
        },
        keysert: function (key,type) {
            return function(a,b){
                var value1 = a[key];
                var value2 = b[key];
                if (type == "asc") {
                    return value1 - value2;
                }else {
                    return value2 - value1;
                }
            }
        },
        // today : function(){
        //     date = new Date().format("yyyy-MM-dd");
        //     this.searchDate.beginDate = date
        //     this.searchDate.endDate = date
        //     this.searchInfo();
        // },
        // yesterDay : function(){
        //     this.searchDate.beginDate = GetDateStr(-1);
        //     this.searchDate.endDate  = GetDateStr(-1);
        //     this.searchInfo();
        // },
        // week : function(){
        //     this.searchDate.beginDate  = getWeekStartDate();
        //     this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
        //     this.searchInfo();
        // },
        // month : function(){
        //     this.searchDate.beginDate  = getMonthStartDate();
        //     this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
        //     this.searchInfo();
        // },
        brandUnitTable : function(){
         	var api = brandUnitAPI;
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
        },
        brandFamilyTable : function(){
       		var api = brandFamilyAPI;
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

function openModal(articleId) {
	var beginDate = $("#beginDate").val();
	var endDate = $("#endDate").val();
    $.ajax({
        url: 'articleSell/showMealAttr',
        data: {
            'articleId': articleId,
            'beginDate': beginDate,
            'endDate': endDate
        },
        success: function (result) {
            var modal = $("#mealAttrModal");
            modal.find(".modal-body").html(result);
            modal.modal()
        },
        error: function () {
			toastr.clear();
            toastr.error("系统异常，请刷新重试");
        }
    });
}
</script>

