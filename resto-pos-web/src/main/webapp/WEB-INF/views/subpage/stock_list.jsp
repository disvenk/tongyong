<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<style type="text/css">
	.shopCartCount {
	    position: absolute;
	    top: -7px;
	    right: -7px;
	}
	.shopCartCount .art-count {
	    height: 18px;
	    width: 18px;
	    border: 2px solid red;
	    display: inline-block;
	    border-radius: 50%;
	    line-height: 18px;
	    text-align: center;
	    font-style: normal;
	    color: #F9F5F5;
	    margin: 0 3px;
	    background-color: red;
	}
</style>
<div id="controlArticleSell">
    <div>
        <div class="row" >
            <div class="col-md-12">
                <div>
                    <div class="tab-content">
                        <!-- 菜品销售报表 -->
                        <div role="tabpanel" class="tab-pane active" >
                            <div class="panel panel-success">
                                <div id="articleFamily">
                                    <label style='padding-left: 20px'><h3>菜品类别:</h3></label>
                                    <button type='button' style='margin-bottom:10px;margin-left: 10px;width:100px'
                                            class='btn btn-success' @click="getStockByFamilyId(null)">全部</button>
                                    <button type='button' style='margin-bottom:10px;margin-left: 10px;position: relative;'
                                            class='btn btn-info' v-for="family in articleFamily" @click="getStockByFamilyId(family)">                                           
                                    	<span class="shopCartCount" v-for="a in warningList" v-if="a.articleFamilyId == family.id && family.inventoryWarningNum>0">
                                			<i class="art-count" v-if="family.inventoryWarningNum>0">{{family.inventoryWarningNum}}</i>	
				                        </span>                                        
                                        <span>{{family.name}}</span>
                                    </button>
                                    <button type='button' style='margin-bottom:10px;margin-left: 10px;width:80px'
                                            class='btn btn-warning' @click="getStockByEmpty">已沽清</button>
                                    <button type='button' style='margin-bottom:10px;margin-left: 10px;width:80px'
                                            class='btn btn-danger' @click="getActivated">已下架</button>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row" id="searchTools">
        <div class="col-md-12">
            <div class="tab-content">
                <!-- 菜品销售报表 -->
                <div role="tabpanel" class="tab-pane active" id="dayReport">
                    <div class="panel panel-success">
                        <div class="panel-body">
                            <table id="articleSellTable" class="table table-striped table-bordered table-hover"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



<script>

    var isc = null;

    var obj = new Vue({
        el : "#controlArticleSell",
        data : {
            articleSellTable:{},//dataTables对象
            articleSells:[],
            articleFamily:[],
            familyId : "",
            empty : 0,
            activated : null,
            warningList:[],
            inventoryList:[],
            editBtn:false
        },
        created : function() {
            this.initDataTables();
            this.searchInfo();
        },
        methods : {
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                //得到所有菜品类型		
                $.post("article/getFamily",function (result) {
                    if(result.success){
                        that.articleFamily = result.data;                        
                    }
                });
                // datatable对象
                that.articleSellTable =  $("#articleSellTable").DataTable({
                    "fnCreatedRow": function( nRow, aData, iDataIndex ) {                    	
                        if (aData.currentStock <= aData.inventoryWarning){                        	                       	                       	
                            $('td:eq(1)',nRow).css('color','#F36051')
                            $('td:eq(2)',nRow).css('color','#F36051')
                            $('td:eq(3)',nRow).css('color','#F36051')
                            $('td:eq(4)',nRow).css('color','#F36051')                             
                            that.articleFamily.forEach(function(article){
								if(article.id == aData.articleFamilyId){									
									that.warningList.push(aData);
									if(that.editBtn){
										return false;
									}else{
										article.inventoryWarningNum++;
									}																	
								}
					        });                         
                        }
                    },
                    "lengthMenu": [[50, 75, 100, 150], [50, 75, 100, "All"]],
                    "columnDefs": [
                        { "width": "60px", "targets": 0 },
                        { "width": "60px", "targets": 1 },
                        { "width": "60px", "targets": 2 },
                        { "width": "60px", "targets": 3 },
                        { "width": "60px", "targets": 4 },
                        { "width": "80px", "targets": 5 },
                    ],
                    ordering: false,
                    columns: [
                        {
                            title: "菜品类型",
                            data: "articleType",
                            createdCell: function (td, tdData) {
                                if(tdData == 1){
                                    $(td).html("<span class='label label-info'>单品</span>");
                                }else{
                                    $(td).html("<span class='label label-primary'>套餐</span>");
                                }
                            }
                        },
                        {
                            title: "菜品分类",
                            data: "familyName",
                            "render": function (data, type, full) {
                                if(full.currentStock != 0){
                                    return data;
                                }else{
                                    return "<label style='color: red'>"+data+"</label>"
                                }
                            }
                        },
                        {
                            title: "菜品名称",
                            data: "name",
                            "render": function (data, type, full) {
                                if(full.currentStock != 0){
                                    return data;
                                }else{
                                    return "<label style='color: red'>"+data+"</label>"
                                }
                            }
                        },
                        {
                            title: "默认库存",
                            data: "defaultStock",
                            "render": function (data, type, full) {
                                if(full.currentStock != 0){
                                    return data;
                                }else{
                                    return "<label style='color: red'>"+data+"</label>"
                                }
                            }
                        },
                        {
                            title: "当前库存",
                            data: "currentStock",
                            "render": function (data, type, full) {
                                var result;
                                if(data == 0 ){
                                    result ="<input type='text' style='border:none;background:no-repeat 0 0 scroll; color: red; outline:medium;width:60px' readonly='true' id='"+full.id+"' value='" + data + "'>";
                                }else{
                                    result ="<input type='text' style='border:none;background:no-repeat 0 0 scroll; outline:medium;width:60px' readonly='true' id='"+full.id+"' value='" + data + "'>";
                                }
                                return result;
                            }
                        },
                        {
                            title: "沽清备注",
                            data: "emptyRemark",
                            createdCell : function(td,tdData){
                                $(td).html("");
                            }
                        },
                        {
                            title: "操作",
                            data: "id",
                            createdCell: function (td, data, full) {
                                var editStockButton = "";
                                var clearStockButton = "";
                                var setActivatedButton = "";
                                var articleType = 0;
                                if(full.articleType == 1){	//单品
                                    editStockButton = $("<button type='button' class='btn btn-primary' id=\"" + data + "\">编辑</button>");
                                    if(full.currentStock == 0){
                                        clearStockButton = $("<button type='button' style='margin-left: 10px' class='btn btn-default'>已沽清</button>");
                                    }else{
                                        clearStockButton = $("<button type='button' style='margin-left: 10px' class='btn btn-warning'>沽&nbsp;&nbsp;&nbsp;清</button>");
                                        clearStockButton.click(function () {
                                            that.clearStock(data);
                                        });
                                    }
                                    if(full.id.indexOf("@") > -1){//判断是否是 多规格 单品
                                        if(full.activated == 1){
                                            setActivatedButton = $("<button type='button' style='margin-left: 10px; color:#F5F5F5;' class='btn' disabled='disabled'>下架</button>");
                                        }else{
                                            setActivatedButton = $("<button type='button' style='margin-left: 10px; color:#F5F5F5' class='btn' disabled='disabled'>上架</button>");
                                            articleType = 1;
                                        }
                                    }else{
                                        if(full.activated == 1){
                                            setActivatedButton = $("<button type='button' style='margin-left: 10px;' class='btn btn-default'>下架</button>");
                                        }else{
                                            setActivatedButton = $("<button type='button' style='margin-left: 10px;' class='btn btn-success'>上架</button>");
                                            articleType = 1;
                                        }
                                    }
                                }else{	//套餐
                                    editStockButton = $("<button type='button' class='btn' id=\"" + data + "\">编辑</button>");
                                    if(full.currentStock == 0){
                                        clearStockButton = $("<button type='button' style='margin-left: 10px;' class='btn'>已沽清</button>");
                                    }else{
                                        clearStockButton = $("<button type='button' style='margin-left: 10px; ' class='btn'>沽&nbsp;&nbsp;&nbsp;清</button>");
                                        clearStockButton.click(function () {
                                            that.clearStock(data);
                                        });
                                    }
                                    if(full.activated == 1){
                                        setActivatedButton = $("<button type='button' style='margin-left: 10px;' class='btn btn-default'>下架</button>");
                                    }else{
                                        setActivatedButton = $("<button type='button' style='margin-left: 10px;' class='btn btn-success'>上架</button>");
                                        articleType = 1;
                                    }
                                }
                                editStockButton.click(function () {
                                    that.editStock(data,full.id,editStockButton);
                                });
                                setActivatedButton.click(function () {
                                    that.setActivated(data, articleType);
                                });
                                var operator = [editStockButton,clearStockButton,setActivatedButton];
                                return $(td).html(operator);
                            }
                        }
                    ]
                }).on( 'init.dt', function () {//datatables  初始化时 的回调
                    $("#articleSellTable_wrapper>.row:eq(1)").attr("id","wrapper_stock");
                    $("#wrapper_stock").height($("body").height()-$("#articleFamily").height()-200);
                    $("#wrapper_stock").css({"overflow":"hidden"});
                    isc = new IScroll("#wrapper_stock",{scrollbars : true,scrollX: true,scrollY: true,interactiveScrollbars : true,shrinkScrollbars: 'clip',scrollbars: 'custom'});
//                  document.addEventListener('touchmove', function(e) {
//                      e.preventDefault();
//                  }, false);
                }).on( 'draw.dt', function () {//datatables 重绘 时 的回调		初始化 JS 键盘
                    //给 input 标签添加 js class 作为选择器
                    $("#articleSellTable input[type='text']").removeClass().addClass("numkeyboard");
                    //绑定 js 键盘
                    $(".numkeyboard").ioskeyboard({
                        keyboardRadix:80,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
                        keyboardRadixMin:40,//键盘大小的最小值，默认为60，实际大小为564X198
                        keyboardRadixChange:true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时才较好支持Safari内核浏览器
                        clickeve:true,//是否绑定元素click事件
                        colorchange:false,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
                        colorchangeStep:1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
                        colorchangeMin:154//按键背影颜色的最小值，默认为RGB(154,154,154)
                    });
                    $("tbody input").blur(function(){//隐藏 js键盘
                        $("#keyboard_5xbogf8c").css({"display":"none"});
                    });
                    isc && isc.refresh();//刷新滚动触屏
                });
            },
            searchInfo : function() {
                toastr.clear();
                toastr.success("正在查询中...");
                try{
                    var that = this;
                    $.post("article/getStock",{familyId : that.familyId, empty : that.empty, activated : that.activated},function (result) {
                        if (result.success){
                            toastr.clear();
                            that.articleSells = result.data;
                            that.articleSellTable.clear();
                            that.articleSellTable.rows.add(result.data).draw();
                            toastr.clear();
                            toastr.success("查询成功");
                        }else{
                            toastr.clear();
                            toastr.error("连接超时，请稍后重试");
                        }
                    });
                }catch(e){
                    toastr.clear();
                    toastr.error("网络异常，请刷新重试");
                }
            },
            getStockByEmpty : function () {
                this.familyId = "";
                this.empty = 1;
                this.activated = null;
                this.searchInfo();
            },
            getActivated : function () {
                this.familyId = "";
                this.empty = 0;
                this.activated = 0;
                this.searchInfo();
            },
            getStockByFamilyId : function (f) {      
                this.empty = 0;
                this.activated = null;
                this.editBtn = true;
                if(f.id == null || f.id == ""){
                    this.familyId = "";
                }else{
                    this.familyId = f.id;
                }
                this.searchInfo();            
            },
            editStock : function (articleId,name,t) {
                var that = this;
                if (t.text() == "编辑") {
                    t.text("保存");
                    t.removeClass("btn-primary").addClass("btn-success");
                    $('input:text[id="'+name+'"]').attr("readonly",false);
                    $('input:text[id="'+name+'"]').attr("style","" +
                            "width:80px");
                    //$('input:text[id="'+name+'"]').select();
                    $('input:text[id="'+name+'"]').focus();
                    $('input:text[id="'+name+'"]').val("");                   
                } else {
                    t.text("编辑");
                    t.removeClass("btn-success").addClass("btn-primary");
                    $('input:text[id="'+name+'"]').attr("readonly",true);
                    $('input:text[id="'+name+'"]').attr("style","border:none;background:no-repeat 0 0 scroll; outline:medium;");
                    var count = $('input:text[id="'+name+'"]').val();
                    var numberCheck = /^[1-9]*[1-9][0-9]*$/;	//正则表达式-- 验证正整数
                    if(!numberCheck.test(count)){
                        $('input:text[id="'+name+'"]').val("0");
                        count = 0 ;
                    }
                    $.ajax({
                        url: 'article/editStock',
                        method: 'post',
                        data: {articleId: articleId, count: $('input:text[id="'+name+'"]').val()},
                        type: 'json',
                        success: function (result) {
                            if (result.success) {
                                toastr.success("操作成功");
                                that.searchInfo();
                            }
                        }
                    });
                    that.articleSells.forEach(function(article){
                    	if(article.id == articleId){
                    		if (article.currentStock <= article.inventoryWarning){                        	                       	                       			                                                     
		                        that.articleFamily.forEach(function(f){
									if(f.id == article.articleFamilyId){									
										f.inventoryWarningNum--;																
									}
						        });                         
		                    }else{
		                    	that.articleFamily.forEach(function(f){
									if(f.id == article.articleFamilyId){									
										f.inventoryWarningNum++;																
									}
						        });
		                    }
                    	}
                    })                   
                }
            },
            clearStock : function (articleId) {
                var that = this;
                $.ajax({
                    url: 'article/clearStock',
                    method: 'post',
                    data: {articleId: articleId},
                    type: 'json',
                    success: function (result) {
                        if (result.success) {
                            toastr.success("沽清成功");
                            that.searchInfo();
                        }
                    }
                });
            },
            setActivated : function (articleId,activated) {
                var that = this;
                $.ajax({
                    url: 'article/setActivated',
                    method: 'post',
                    data: {articleId: articleId,activated : activated},
                    type: 'json',
                    success: function (result) {
                        if (result.success) {
                            toastr.success("操作成功！");
                            that.searchInfo();
                        }
                    }
                });
            }

        }
    });

    //datatables语言设置
    var language = {
        "sProcessing": "处理中...",
        "sLengthMenu": "显示 _MENU_ 项结果",
        "sZeroRecords": "没有匹配结果",
        "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
        "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
        "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
        "sInfoPostFix": "",
        "sSearch": "搜索:",
        "sUrl": "",
        "sEmptyTable": "暂时没有数据...",
        "sLoadingRecords": "载入中...",
        "sInfoThousands": ",",
        "oPaginate": {
            "sFirst": "首页",
            "sPrevious": "上页",
            "sNext": "下页",
            "sLast": "末页"
        },
        "oAria": {
            "sSortAscending": ": 以升序排列此列",
            "sSortDescending": ": 以降序排列此列"
        }
    };
    function Trim(str) {
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }

</script>

