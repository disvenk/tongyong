<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="controlShop">
<a class="btn btn-info ajaxify" href="articleSell/shopList">
	<span class="glyphicon glyphicon-circle-arrow-left"></span>
	返回
</a>
<h2 class="text-center"><strong>${shopName}</strong></h2><br/>
<div class="row" id="searchTools">
	<div class="col-md-12">
		<form class="form-inline">
		  <div class="form-group" style="margin-right: 50px;">
		    <label>开始时间：
                <input type="text" id="beginDate" class="form-control form_datetime" :value="searchDate.beginDate" v-model="searchDate.beginDate" readonly="readonly">
            </label>
		  </div>
		  <div class="form-group" style="margin-right: 50px;">
		    <label>结束时间：
		    <input type="text" id="endDate" class="form-control form_datetime" :value="searchDate.endDate" v-model="searchDate.endDate" readonly="readonly">
            </label>
		 	 <button type="button" class="btn btn-primary" @click="today"> 今日</button>
             <button type="button" class="btn btn-primary" @click="yesterDay">昨日</button>
             <button type="button" class="btn btn-primary" @click="week">本周</button>
             <button type="button" class="btn btn-primary" @click="month">本月</button>
             <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>
             <button type="button" class="btn btn-primary" @click="shopreportExcel">下载报表</button>
             <%--&nbsp;--%>
			  <%--<a type="button" href="orderReport/historyOrderList?reportName=菜品销量报表" style="float: right;" class="btn green ajaxify">查询历史报表数据</a>--%>
             <%--<button type="button" style="float: right;" class="btn btn-primary" @click="shopreportExcel">下载报表</button>--%>
             <br/>
          </div>
		</form>
	<div>
<br/>
<br/>
<div>
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
	    	<!-- 店铺菜品销售表(单品)   -->
	    	<div class="panel panel-success">
			  <div class="panel-heading text-center">
			  	   <strong style="margin-right:100px;font-size:22px">店铺菜品销售表(单品)</strong>
			  </div>
			  <div class="panel-body">
			  	<table id="shopArticleUnitTable" class="table table-striped table-bordered table-hover"
			  		style="width: 100%;">
			  	</table>
			  </div>
			</div>
	    </div>
	
	    <!-- 套餐 -->
	    <div role="tabpanel" class="tab-pane" id="revenueCount">
	    	<div class="panel panel-primary" style="border-color:white;">
			  	<!-- 店铺菜品销售表(套餐) -->
	    	<div class="panel panel-success">
			  <div class="panel-heading text-center">
			  	<strong style="margin-right:100px;font-size:22px">店铺菜品销售表(套餐)</strong>
			  </div>
			  <div class="panel-body">
			  	<table id="shopArticleFamilyTable" class="table table-striped table-bordered table-hover"
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
                          <strong style="margin-right:100px;font-size:22px">店铺菜品销售表(类别)</strong>
                      </div>
                      <div class="panel-body">
                          <table id="shopArticleTypeTable" class="table table-striped table-bordered table-hover"
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
						  <strong style="margin-right:100px;font-size:22px">店铺菜品销售表(其他销量)</strong>
					  </div>
					  <div class="panel-body">
						  <table id="shopMealSealsTable" class="table table-striped table-bordered table-hover"
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
	<div id="articleMoeal">
		<div class="modal fade" id="mealAttrModal" tabindex="-1" role="dialog" data-backdrop="static">
	        <div class="modal-dialog modal-full">
	            <div class="modal-content">
	
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true" @click="closeModal"></button>
	                </div>
	
	                <div class="modal-body" id="reportModalMealAttr"></div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-info btn-block" data-dismiss="modal" aria-hidden="true" @click="closeModal">关闭</button>
	                </div>
	            </div>
	        </div>
	    </div>
    </div>
</div>
<script src="assets/customer/date.js" type="text/javascript"></script>
<script>
    //时间插件
    $('.form_datetime').datetimepicker({
        endDate:new Date(),
        minView:"month",
        maxView:"month",
        autoclose:true,//选择后自动关闭时间选择器
        todayBtn:true,//在底部显示 当天日期
        todayHighlight:true,//高亮当前日期
        format:"yyyy-mm-dd",
        startView:"month",
        language:"zh-CN"
    });

    var beginDate = "${beginDate}";
	var endDate = "${endDate}";
	var shopId = "${shopId}";
	var shopUnitAPI = null;
	var shopFamilyAPI = null;
    var shopTypeAPI = null;
	var shopMealSealsAPI = null;
	var vueObjShop = new Vue({
	    el : "#controlShop",
	    data : {
	        searchDate : {
	            beginDate : "",
	            endDate : "",
	        },
	        currentType:1,//当前选中页面
	        shopArticleUnitTable:{},//单品dataTables对象
	        shopArticleFamilyTable:{},//套餐datatables对象
            shopArticleTypeTable:{},//类别datatables对象
			shopMealSealsTable : {}, //其他销量datatable对象
	        api:{},
            shopArticleUnitDtos:[],
            shopArticleFamilyDtos:[],
            shopArticleTypeDtos:[],
			shopMealSealsDtos:[]
	    },
	    created : function() {
	        this.searchDate.beginDate = beginDate;
	        this.searchDate.endDate = endDate;
	        this.initDataTables();
	        this.searchInfo();
	    },
	    methods : {
	        initDataTables:function () {
	            //that代表 vue对象
	            var that = this;
	            //单品datatable对象
	            that.shopArticleUnitTable=$("#shopArticleUnitTable").DataTable({
	                lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
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
	                        data : "shopSellNum",
                            createdCell: function (td, tdData, rowData) {
                                var text = tdData;
                                if (rowData.weight > 0){
                                    text = text + "/" + rowData.weight + "斤";
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
	                	shopUnitAPI = this.api();
	                	that.shopUnitTable();
	                }
	            });
	            //套餐datatable对象
	            that.shopArticleFamilyTable=$("#shopArticleFamilyTable").DataTable({
	            	lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
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
	                        data : "shopSellNum",
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
	                            	openMealAttrModal(tdData);
	                            })
	                            $(td).html(button);
	                        }
	                    }
	                ],
	                initComplete: function () {
	                	shopFamilyAPI = this.api();
	                	that.shopFamilyTable();
	                }
	            });

                //类别datatable对象
                that.shopArticleTypeTable=$("#shopArticleTypeTable").DataTable({
                    lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
                    order: [[ 2, "desc" ]],
                    columns : [
                        {
                            title : "菜品类别",
                            data : "articleFamilyName",
                            orderable : false
                        },
                        {
                            title : "销量(份)",
                            data : "shopSellNum",
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
                        shopTypeAPI = this.api();
                    }
                });

				//其他销量datatable对象
				that.shopMealSealsTable=$("#shopMealSealsTable").DataTable({
					lengthMenu: [ [50, 75, 100, -1], [50, 75, 100, "All"] ],
					order: [[ 2, "desc" ]],
					columns : [
						{
							title : "菜品名称",
							data : "articleName",
							orderable : false
						},
						{
							title : "销量(份)",
							data : "shopSellNum",
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
						shopMealSealsAPI = this.api();
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
				}else if(timeCha > 2678400000){
					toastr.clear();
					toastr.error("暂时未开放大于一月以内的查询！");
					return false;
				}
                toastr.clear();
                toastr.success("查询中...");
	        	try{
		            var api1 = shopUnitAPI;
		            var api2 = shopFamilyAPI;
                    var api3 = shopTypeAPI;
					var api4 = shopMealSealsAPI;
                    $.post("articleSell/queryShopOrderArtcile", this.getDate(), function(result) {
                        if(result.success == true){//清空datatable的column搜索条件
                            toastr.clear();
                            toastr.success("查询成功");
                            that.shopArticleUnitDtos = result.data.shopArticleUnitDtos;
                            that.shopArticleFamilyDtos = result.data.shopArticleFamilyDtos;
                            that.shopArticleTypeDtos = result.data.shopArticleTypeDtos;
							that.shopMealSealsDtos = result.data.shopMealSalesDtos;
                            api1.search('');
                            var column1 = api1.column(1);
                            column1.search('', true, false);
                            //清空表格
                            that.shopArticleUnitTable.clear();
                            that.shopArticleUnitTable.rows.add(result.data.shopArticleUnitDtos).draw();
                            //重绘搜索列
                            that.shopUnitTable();
                            //清空datatable的column搜索条件
                            api2.search('');
                            var column2 = api2.column(1);
                            column2.search('', true, false);
                            //清空表格
                            that.shopArticleFamilyTable.clear();
                            that.shopArticleFamilyTable.rows.add(result.data.shopArticleFamilyDtos).draw();
                            //重绘搜索列
                            that.shopFamilyTable();
                            api3.search('');
                            that.shopArticleTypeTable.clear();
                            that.shopArticleTypeTable.rows.add(result.data.shopArticleTypeDtos).draw();
							//其他销量
							api4.search('');
							that.shopMealSealsTable.clear();
							that.shopMealSealsTable.rows.add(result.data.shopMealSalesDtos).draw();
                        }else{
							toastr.clear();
                            toastr.error("查询报表出错");
                        }
                    });
	        	}catch(e){
					toastr.clear();
                    toastr.error("系统异常，请刷新重试");
	        	}
	        },
	        getDate : function(){
	            var data = {
	                beginDate : this.searchDate.beginDate,
	                endDate : this.searchDate.endDate,
	                shopId : shopId
	            };
	            return data;
	        },
	        shopreportExcel : function(){
	            try {
                    var that = this;
                    var object = {
                        beginDate : that.searchDate.beginDate,
                        endDate : that.searchDate.endDate,
                        type : that.currentType,
                        shopId : shopId
                    }
                    switch (that.currentType) {
                        case 1:
                            object.shopArticleUnit = that.shopArticleUnitDtos;
                            $.post("articleSell/createShopArticle",object,function (result) {
                                if(result.success){
	                                window.location.href="articleSell/downloadShopArticle?path="+result.data+"";
                                }else{
									toastr.clear();
                                    toastr.error("下载报表出错");
                                }
                            });
                            break;
                        case 2:
                            object.shopArticleFamily = that.shopArticleFamilyDtos;
                            $.post("articleSell/createShopArticle",object,function (result) {
                                if(result.success){
                                    window.location.href="articleSell/downloadShopArticle?path="+result.data+"";
                                }else{
									toastr.clear();
                                    toastr.error("下载报表出错");
                                }
                            });
                            break;
                        case 3:
                            object.shopArticleType = that.shopArticleTypeDtos;
                            $.post("articleSell/createShopArticle",object,function (result) {
                                if(result.success){
                                    window.location.href="articleSell/downloadShopArticle?path="+result.data+"";
                                }else{
                                    toastr.clear();
                                    toastr.error("下载报表出错");
                                }
                            });
                            break;
						case 4:
							object.shopMealSeals = that.shopMealSealsDtos;
							$.post("articleSell/createShopArticle",object,function (result) {
								if(result.success){
									window.location.href="articleSell/downloadShopArticle?path="+result.data+"";
								}else{
									toastr.clear();
									toastr.error("下载报表出错");
								}
							});
						break;
                    }
                }catch (e){
					toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }
	        },
	        today : function(){
	            date = new Date().format("yyyy-MM-dd");
	            this.searchDate.beginDate = date
	            this.searchDate.endDate = date
	            this.searchInfo();
	        },
	        yesterDay : function(){
	            this.searchDate.beginDate = GetDateStr(-1);
	            this.searchDate.endDate  = GetDateStr(-1);
	            this.searchInfo();
	        },
	
	        week : function(){
	            this.searchDate.beginDate  = getWeekStartDate();
	            this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
	            this.searchInfo();
	        },
	        month : function(){
	            this.searchDate.beginDate  = getMonthStartDate();
	            this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
	            this.searchInfo();
	        },
	        shopUnitTable : function(){
	         	var api = shopUnitAPI;
	            api.search('');
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
	        shopFamilyTable : function(){
	       		var api = shopFamilyAPI;
	           	api.search('');
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
	        closeModal : function(){
	            var modal = $("#reportModalMealAttr");
	            modal.find(".modal-body").html("");
	            modal.modal("hide");
	        }
	    }
	});

	function openMealAttrModal(articleId) {
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

