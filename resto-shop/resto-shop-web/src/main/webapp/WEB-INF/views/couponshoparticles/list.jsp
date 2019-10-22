<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki">表单</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form role="form" action="{{m.id?'newcustomcoupon/modifyProductCoupon':'newcustomcoupon/createProductCoupon'}}" @submit.prevent="save">
						<div class="form-body">
							<!--产品券所属-->
							<div class="form-group">
                                <label>产品券所属<span class="starColor">*</span></label>
                                <br/>
                                <label class="radio-inline">
                                    <input type="radio" name="isBrand" value="1"  v-model="m.isBrand">品牌产品券
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="isBrand" value="0" v-model="m.isBrand">店铺产品券
                                </label>
                            </div>
                            <!--选择店铺-->
                            <div class="form-group" v-if="m.isBrand == 0">
			           			<label>选择店铺<span class="starColor">*</span></label>
							    <div>
								    <select class="form-control" name="shopId" required v-model="selected">
								    	<option v-for="temp in shopList" v-bind:value="temp.id">
								    		{{ temp.name }}
								    	</option>
							    	</select>	
							    </div>
							</div>						
                            <!--产品券名称-->
                            <div class="form-group">
                                <label>产品券名称<span class="starColor">*</span></label>
                                <input type="text" class="form-control" name="couponName" v-model="m.couponName">
                            </div>
                            <!--产品券抵扣类型-->
							<div class="form-group">
                                <label>产品券抵扣类型<span class="starColor">*</span></label>
                                <br/>
                                <label class="radio-inline">
                                    <input type="radio" name="deductionType" value="0"  v-model="m.deductionType">抵扣菜品
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="deductionType" value="1" v-model="m.deductionType">抵扣金额
                                </label>
                            </div>
                            <!--产品券最大抵扣金额-->
                            <div class="form-group" v-if="m.deductionType == 1">
                                <label>最大抵扣金额<span class="starColor">*</span></label>
                                <input type="number" class="form-control" name="couponValue" v-model="m.couponValue" placeholder="请输入数字" required min="0">
                            </div>
                            <!--产品券有效期类型-->
                            <div class="form-group">
                                <label class="control-label">产品券有效日期类型<span class="starColor">*</span></label>
                                <div class="radio-list" style="margin-left: 20px;">
                                    <template v-if="m.timeConsType==1">
                                        <label class="radio-inline">
                                            <input  type="radio" class="md-radiobtn" name="timeConsType" checked="checked" value="1" v-model="m.timeConsType" @click="showNum">按天
                                        </label>
                                    </template>
                                    <template v-else>
                                        <label class="radio-inline">
                                            <input  type="radio" class="md-radiobtn" name="timeConsType" value="1" v-model="m.timeConsType" @click="showNum">按天
                                        </label>
                                    </template>
                                    <label class="radio-inline" v-if="m.couponType!=2">
                                        <input type="radio" class="md-radiobtn" name="timeConsType" value="2" v-model="m.timeConsType" @click="showTime">按时间范围
                                    </label>
                                </div>
                            </div>
                            <div class="form-group" v-if="m.timeConsType==1">
                                <label>产品券有效日期</label>
                                <input type="number" class="form-control" name="couponValiday" v-model="m.couponValiday" placeholder="请输入数字" required min="0">
                            </div>  
                            <div class="form-group" v-if="m.timeConsType==2">
                                <div class="row">
                                    <label class="control-label col-md-2">开始日期</label>
                                    <div class="col-md-4">
                                        <div class="input-group date form_datetime">
                                            <input type="text" readonly class="form-control" name="beginDateTime" v-model="m.beginDateTime" @focus="initCouponTime"> <span class="input-group-btn">
												<button class="btn default date-set" type="button">
                                                    <i class="fa fa-calendar" @click="initCouponTime"></i>
                                                </button>
											</span>
                                        </div>
                                    </div>
                                    <label class="control-label col-md-2">结束日期</label>
                                    <div class="col-md-4">
                                        <div class="input-group date form_datetime">
                                            <input type="text" readonly class="form-control" @focus="initCouponTime" name="endDateTime" v-model="m.endDateTime"> <span class="input-group-btn">
												<button class="btn default date-set" type="button">
                                                    <i class="fa fa-calendar" @click="initCouponTime"></i>
                                                </button>
											</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--菜品券图片-->
                            <div class="form-group">
                                <label style="width: 20%;padding: 0;">菜品券图片</label>
                                <div>
                                    <input type="hidden" name="couponPhoto" v-model="m.couponPhoto">
                                    <img-file-upload class="form-control" @success="uploadSuccess"
                                                     @error="uploadError"></img-file-upload>
                                    <img v-if="m.couponPhoto" :src="m.couponPhoto" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
                                </div>
                            </div>
                            <!--是否启用产品券-->
                            <div class="form-group">
                        		<label class="control-label">是否启用产品券<span class="starColor">*</span></label>
                                <div class="radio-list" style="margin-left: 20px;">
                                    <label class="radio-inline">
                                        <input type="radio" name="isActivty" v-model="m.isActivty" value=1 v-if="m.id">
                                        <input type="radio" name="isActivty" value=1 v-if="!m.id">是</label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isActivty" v-model="m.isActivty" value=0 v-if="m.id">
                                        <input type="radio" name="isActivty" value=0 v-if="!m.id">否</label>
                                </div>                           
                            </div>
                            <!--选择店铺模式-->
                            <div class="form-group">
                                <div class="control-label">
                                	<input type="hidden" name="distributionModeId" value="1"/>
                                	店铺模式: 堂食
                                </div>
                            </div>
                            <!--新建提示-->
                            <div class="form-group">
                                <div class="control-label" style="color: #f13535;font-size: 16px;">提示: 新建保存成功后只能编辑图片和启用状态，请不要误操作!谢谢</div>
                            </div>
						</div>
						<input type="hidden" name="id" v-model="m.id" />
						<div class="text-center" style="padding: 20px 0">
							<input class="btn green"  type="submit" value="保存"/>
							<a class="btn default" @click="cancel" >取消</a>
						</div>
					</form>
	            </div>
	        </div>
		</div>
	</div>
	
	<div class="row form-div" v-if="showEditform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki">表单</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form role="form" action="newcustomcoupon/modifyProductCoupon" @submit.prevent="saveEditForm">
						<div class="form-body">
							<!--产品券所属-->
							<div class="form-group">
                                <div>
                                	产品券所属：<span v-if="m.isBrand == 1">品牌</span>
                                	<span v-if="m.isBrand == 0">店铺</span>
                                </div>
                            </div>
                            <!--产品券类型-->
                            <div class="form-group">
                                <span v-if="m.deductionType == 0">产品券类型：抵扣菜品</span>
                                <span v-if="m.deductionType == 1">产品券类型：抵扣金额</span>
                            </div>
                            <!--产品券名称-->
                            <div class="form-group">
                                <span>产品券名称：{{m.couponName}}</span>
                            </div>
                            <!--产品券最大抵扣金额-->
                            <div class="form-group" v-if="m.deductionType == 1">
                                <span>最大抵扣金额：{{m.couponValue}}</span>
                            </div>
							<!--菜品券图片-->
                            <div class="form-group">
                                <label style="width: 20%;padding: 0;">菜品券图片<span class="starColor">*</span></label>
                                <div>
                                    <input type="hidden" name="couponPhoto" v-model="m.couponPhoto">
                                    <img-file-upload class="form-control" @success="uploadSuccess"
                                                     @error="uploadError"></img-file-upload>
                                    <img v-if="m.couponPhoto" :src="m.couponPhoto" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
                                </div>
                            </div>
                            <!--是否启用产品券-->
                            <div class="form-group">
                        		<label class="control-label">是否启用产品券<span class="starColor">*</span></label>
                                <div class="radio-list" style="margin-left: 20px;">
                                    <label class="radio-inline">
                                        <input type="radio" name="isActivty" v-model="m.isActivty" value=1 v-if="m.id">
                                        <input type="radio" name="isActivty" value=1 v-if="!m.id">是</label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isActivty" v-model="m.isActivty" value=0 v-if="m.id">
                                        <input type="radio" name="isActivty" value=0 v-if="!m.id">否</label>
                                </div>                           
                            </div>
                            
			            	<input type="hidden" name="id" v-model="m.id" />
							<div class="text-center" style="padding: 20px 0">
								<input class="btn green"  type="submit" value="保存"/>
								<a class="btn default" @click="closeEditForm" >取消</a>
							</div>
						</div>
				 	</form>		
            	</div>
        	</div>
        </div>
	</div>
	<!--关联的菜品-->
	<div class="row form-div" v-show="relationArticle">
        <div class="col-md-offset-3 col-md-6" style="background:#FFF;font-size: 18px;">
            <div class="text-center" style="padding: 20px 0">
                <span>关联菜品</span>
            </div>
            <div class="row" style="font-size: 16px;max-height: 250px;overflow: auto;">
                <div class="col-md-offset-1 col-md-11">
                	<div class="form-group">
	                    <label class="control-label name">产品券名称：</label>   
	                    <span>{{couponName}}</span>
                    </div>
                    <div class="form-group">
	                	<label class="control-label name">关联菜品<span class="starColor">*</span></label>
	                    <button class="btn green" style="" type="button" @click="showContactArticle">关联菜品</button>
	                </div>
	                <div class="form-group" v-for="f in couponArticleList" v-if="f.articleList.length>0">
	                	<!--<label class="control-label name"></label> -->
	                    <div style="display: inline-block;">
	                    	<span>{{f.shopName}}：</span>
	                    	<div class="color-list">
	                    		<div class="flex-item" v-for="color in f.articleList">
	                    			<button class="btn default" style="position: relative;">
								  		<span style="vertical-align: middle;">{{color.name}}</span>
								  		<img class="chaImg" src="assets/pages/img/cha.png" @click="deleteArticle(f,color)" alt="" />
								  	</button>
	                    		</div>							  	
							</div>
	                    </div>
	                </div>
                </div>           	
            </div>  
            <div class="text-center" style="padding: 20px 0">
	        	<button class="btn green" @click="saveRelationArticle(f)">保存</button>
	        	<button class="btn default" @click="closeRelationArticle">取消</button>                         
	        </div>
        </div>   
    </div>

	<!--菜品树状图-->
    <div class="row form-div" v-show="treeView">
        <div class="col-md-offset-3 col-md-6" style="background:#FFF;">
            <div class="text-center" style="padding: 20px 0">
                <span>添加关联菜品</span>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="modal-body">
                        <input type="text" id="search_ay" class="form-control" placeholder="请输入菜品名称" v-model="searchText">
                    </div>
                    <div id="menuContent" style="max-height: 250px;overflow: auto;padding: 0 10px;">
                        <ul id="assignTree" class="ztree"></ul>
                    </div>

                    <div id="treeview-checkable" style="height: 500px;overflow: auto;display: none"></div>
                </div>
  
            </div>
            <div class="text-center" style="padding: 20px 0">
                <button class="btn green" @click="saveZtreeData">保存</button>
                <a class="btn default" @click="closeTreeView" >取消</a>
            </div>
        </div>
    </div>
	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="newcustomcoupon/add">
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
<!--树状图-->
<link rel="stylesheet" href="assets/ztree/zTreeStyle.css" />
<script src="assets/ztree/jquery.ztree.all-3.5.js"></script>
<!--<script src="assets/customer/drag.js"></script>-->
<script>
	(function(){

		var C;
        var vueObj;
        var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			"sServerMethod":"POST",
			ajax : {
				url : "newcustomcoupon/productCouponlist",
				dataSrc : ""
			},
			columns : [
                {
                    title : "产品券所属",//
                    data : "shopName",
                    createdCell:function (td,tdData) {
                        if(tdData!=null){
                            //$(td).html(tdData+"用")
                            $(td).html("门店")
                        }else{
                            $(td).html("品牌")
                        }
                    }
                },
                {
                    title : "所属门店",
                    data : "shopName",
                    createdCell:function(td,tdData){
                        if(tdData==null){
                            $(td).html("无");
                        }                  
                    }
                },
                {
                    title : "产品券抵扣类型",
                    data : "deductionType",
                    createdCell:function(td,tdData){
                        if(tdData==0){
                            $(td).html("抵扣菜品");
                        }else if(tdData==1){
                        	$(td).html("抵扣金额");
                        }
                    }
                },
                {
                    title : "产品券名称",
                    data : "couponName",
                },
                {
                    title : "最大抵扣金额",
                    data : "couponValue",
                 	createdCell:function(td,tdData){
                 		if(tdData == 0){
                 			$(td).html("无");
                 		}else{
                 			$(td).html(tdData);
                 		}
                        console.log(tdData);
                    }
                },
                {
                    title : "有效期类型",
                    data : "timeConsType",
                    createdCell: function(td,tdData){
                        switch(tdData)
                        {
                            case 1:
                                $(td).html("按天计算");
                                break;
                            case 2:
                                $(td).html("按日期计算");
                                break;
                            default:
                                $(td).html("未知");
                        }
                    }
                },
                {
                    title : "有效时间",
                    data : "timeConsType",
                    createdCell : function(td,tdData,row,rowData){
                    	//console.log(tdData);
                        if(tdData==1){
                            $(td).html(row.couponValiday+"天");
                        }else if(tdData==2){
                            $(td).html(new Date(row.beginDateTime).format("yyyy-MM-dd")+"到"+new Date(row.endDateTime).format("yyyy-MM-dd"));
                        }
                    }
                },
                {
                    title : "是否启用",
                    data : "isActivty",
                    "render":function(data){
                        if(data==1){
                            data="<span class='label label-info'>启&nbsp;用</span>";
                        }else if(data==0){
                            data="<span class='label label-danger'>未启用</span>";
                        }else{
                            data="<span class='label label-warning'>未&nbsp;知</span>";
                        }
                        return data;
                    }
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                      	//console.log(rowData);
                        var operator=[
                        	<s:hasPermission name="couponshoparticles/modify">
                            	C.createEditBtn(rowData),
                           	</s:hasPermission>
                           	createContactBtn(rowData),                          	
                            createGrantBtn(rowData),                                                    
                        ];
                        $(td).html(operator);
                    }
                }],
		});
		//关联菜品
		function createContactBtn(rowData) {
			var button = $("<button class='btn btn-xs btn-primary'>关联菜品</button>");
			button.click(function(){
				vueObj.showRelationArticle(rowData);												
			});
         	return button;			         				
     	}
		//发放产品券
		function createGrantBtn(rowData) {
			var button = (rowData.isActivty == 1) ? $("<a href='couponshoparticles/goToGrant?couponId="+rowData.id+"' class='btn btn-xs btn-success ajaxify '>发放</a>") : "";
         	return button;        	
     	}
		
		var C = new Controller(cid,tb);
		var option = {
            el:cid,
            data:{
                m:{},
                showEditform:false,		//编辑弹窗
                showform:false,
                treeView:false,			//树状图
                relationArticle:false,	//关联菜品
                searchText:'',
                treeData:null,
                ztreeIdList:[],			//菜品ID	
                ztreeNameList:[],		//菜品名称
                ztreePageList:[],		//店铺的ID
                couponName:'',			//产品券的名称                
                isBrand:false,			//关联产品券是否为品牌
                couponId:null,			//关联产品券ID
                couponArticleList:[],	//产品券关联的菜品	
                shopList:[],
                selected:null
                //saveArticleList:[]	//保存的关联菜品列表	
            },   
            watch:{
            	'searchText':function(newVal,oldVal){
            		if(newVal){            		
            			//console.log(this.searchText);
            			this.autoWatch(this.searchText);
            		}
            		if(newVal == ''){
            			this.InitialZtree();
            		}
            	}
            },
            created:function(){
            	this.getShopList();
            },
            methods:{
            	getShopList:function(){
            		var that = this;
            		$.ajax({					
		                url : 'couponshoparticles/selectShopList',
		                contentType: 'application/json; charset=utf-8',
		                type: "post",
		                success : function(res) {
	                		that.shopList = res;
	                		that.selected = res[0].id;
	                	}
	            	})
            	},
            	closeEditForm:function(){
            		this.showEditform = false;
            	},
            	saveEditForm:function(e){
            		var that = this;
                    var formDom = e.target;
					console.log($(formDom).serialize());
                    $.ajax({
                        url : "newcustomcoupon/modifyProductCoupon",
                        data : $(formDom).serialize(),
                        success : function(result) {
                            if (result.success) {
                            	that.closeEditForm();
                                tb.ajax.reload();
                                toastr.clear();
                                toastr.success("保存成功!");
                                that
                            } else {
                                tb.ajax.reload();
                                toastr.clear();
                                toastr.error(result.message);
                            }
                        },
                        error : function() {
                        	that.closeEditForm();
                            tb.ajax.reload();
                            toastr.clear();
                            toastr.error("保存失败");
                        }
                    })
            	},
            	saveRelationArticle:function(){
		        	var that = this;
		        	
		        	var options = {
		        		couponId:this.couponId,
	        			shopUnionArticle:this.couponArticleList
	        		}
		    		$.ajax({					
		                url : 'couponshoparticles/UnionArticle',
		                contentType: 'application/json; charset=utf-8',
		                type: "post",
		                data : JSON.stringify(options),
		                success : function(res) {
	                		that.closeRelationArticle();
	                		toastr.success("保存成功!");
	                	}
	            	})
            	},
            	deleteArticle:function(f,a){
            		f.articleList.$remove(a);
            	},
            	showRelationArticle:function(data){
            		var that = this;
            		this.couponName = data.couponName;
            		this.isBrand = data.isBrand;
            		this.couponId = data.id;
            		$.ajax({					
	                    url : 'couponshoparticles/selectShop',
	                    type: "post",
	                    async: false,
	                    data : {
	                    	couponId:data.id
	                    },
	                    success : function(res) {	                    	
	                    	that.couponArticleList = res.data;
	                    	console.log(JSON.stringify(that.couponArticleList));		                
	                    }
	                })
            		this.relationArticle = true;           		
            	},
            	closeRelationArticle:function(){
            		this.relationArticle = false;
            	},
            	openForm:function(){
                    this.showform = true;
                },
                closeForm:function(){
                    this.m={};
                    this.m.couponType = -1;
                    this.showform = false;
                },
                cancel:function(){
                    this.m={};
                    this.m.couponType = -1;
                    this.closeForm();
                },
            	create:function(){
                    this.m={
                    	timeConsType:1,
                        couponType: -1,
                    };
                    this.openForm();
                },
                edit:function(model){                	
                	this.m= model;
                	this.showEditform = true;
                	console.log(this.m.timeConsType);
                },
                showNum: function(){
                    //点击产品券时间类型按天算 清空天数
                    vueObj.m.couponValiday='';
            	},
                showTime : function(){
                    //如果是新建则默认选择当天，如果是编辑则选择清空
                    //点击产品券的时间类型为时间范围  清空开始时间和结束时间
                    if(vueObj.m.brandId){
                        vueObj.m.beginDateTime='';
                        vueObj.m.endDateTime='';
                    }
                },
                initCouponTime: function(){
                    //初始化当前为当前时间
// 					$("").val(new Date().format("yyyy-MM-dd hh:mm:ss"))
// 					$("input[name='newsletter']") 

                    $('.form_datetime').datetimepicker({
                        format: "yyyy-mm-dd hh:ii:ss",
                        autoclose: true,
                        todayBtn: true,
                        todayHighlight: true,
                        showMeridian: true,
                        pickerPosition: "bottom-left",
                        language: 'zh-CN',//中文，需要引用zh-CN.js包
                        startView: 2,//月视图
                        //minView: 2//日期时间选择器所能够提供的最精确的时间选择视图
                    });
                },
                uploadSuccess: function (url) {
                    console.log(url);
                    $("[name='couponPhoto']").val(url).trigger("change");
                    C.simpleMsg("上传成功");
                    $("#couponPhoto").attr("src", "/" + url);
                },
                uploadError: function (msg) {
                    C.errorMsg(msg);
                },
                save:function(e){
                    var that = this;
                    var url = that.m.id?'newcustomcoupon/modifyProductCoupon':'newcustomcoupon/createProductCoupon';
                    var formDom = e.target;
					console.log($(formDom).serialize());
                    $.ajax({
                        url : url,
                        data : $(formDom).serialize(),
                        success : function(result) {
                            if (result.success) {
                                that.cancel();
                                tb.ajax.reload();
                                toastr.clear();
                                toastr.success("保存成功!");
                            } else {
                                //that.cancel();
                                tb.ajax.reload();
                                toastr.clear();
                                toastr.error(result.message);
                            }
                        },
                        error : function() {
                            //that.cancel();
                            tb.ajax.reload();
                            toastr.clear();
                            toastr.error("保存失败");
                        }
                    })
                },
                showContactArticle:function(){
                	var that = this;
                	$.ajax({					
	                    url : 'couponshoparticles/selectUnionArticle',
	                    type: "post",
	                    data : {
	                    	couponId:this.couponId
	                    },
	                    success : function(result) {
	                        vueObj.treeData = result.data;
							that.getNodeArticle(result);    
			            	that.treeView = true;
	                    }
	                })
                },
                getNodeArticle:function(result){
                	$.ajax({					
	                    url : 'couponshoparticles/selectUnionedArticle',
	                    type: "post",
	                    data : {
	                    	couponId:this.couponId
	                    },
	                    success : function(res) {	                    	
			            	$.fn.zTree.init($("#assignTree"), setting, result.data);
			            	var zTree_Menu = $.fn.zTree.getZTreeObj("assignTree");
							$(res.data).each(function(index,element){
							    if(element.pId!="0"){
							        var node = zTree_Menu.getNodeByParam("id",element.id);
							        var nodeP = zTree_Menu.getNodeByParam("id",element.pId);
							        /*zTree_Menu.selectNode(node,true);//指定选中ID的节点,背景选中
							        zTree_Menu.expandNode(node, true, false);//指定选中ID节点展开*/
							        zTree_Menu.checkNode(node, true, true);//框选中
							        zTree_Menu.expandNode(nodeP, true, true);//指定选中ID节点展开
							    }
							})    
	                    }
	                })
                },
                closeTreeView:function(){
                	this.treeView = false;
                },
                showMenu:function () {
	                var cityObj = $("#search_ay");
	                var cityOffset = $("#search_ay").offset();
	                //$("#menuContent").css({ left: cityOffset.left + "px", top: cityOffset.top + cityObj.outerHeight() + "px" }).slideDown("fast");
	                $("#menuContent").slideDown("fast");
	            },
	            initEvent:function () {
	                //鼠标获得焦点的时候，显示所有的树
	                $("#search_ay").focus(function(){
	                    $("#search_ay").css("background-color","#FFFFCC");
	                    vueObj.showMenu();
	                });
	                //鼠标失去焦点的时候，隐藏
	                /*$("#citySel").blur(function(){
                        $("#citySel").css("background-color","#fff");
                        hideMenu();
                    });*/
	            },
	            hideMenu:function () {
	                $("#menuContent").fadeOut("fast");
	            },
	            InitialZtree:function () {	                
                    $.fn.zTree.init($("#assignTree"), setting, this.treeData);
	            },	            	            
	            updateNodes:function(highlight,nodeList) {
				    var zTree = $.fn.zTree.getZTreeObj("assignTree");
				    console.log(JSON.stringify(nodeList));
				    parentNode = [];
				    for(var i=0, l=nodeList.length; i<l; i++) {
				      	nodeList[i].highlight = highlight;
				      	zTree.updateNode(nodeList[i]);
				      	if(!nodeList[i].isParent && !parentNode.contains(nodeList[i].getParentNode().pId)) {
					        zTree.expandNode(nodeList[i].getParentNode(), true, true, true);
					        parentNode.push(nodeList[i].getParentNode().pId);
					        console.log(nodeList[i].getParentNode().pId);
				      	}else{
				        	zTree.expandNode(nodeList[i], true, true, true);
				      	}
				    }
			  	},	            
	            autoWatch:function (txtObj) {
	            	//console.log(txtObj);
	                if (txtObj.length > 0) {
	                    this.InitialZtree();
	                    var zTree = $.fn.zTree.getZTreeObj("assignTree");
	                    var nodeList = zTree.getNodesByParamFuzzy("name", txtObj);
	                    //将找到的nodelist节点更新至Ztree内
	                    //$.fn.zTree.init($("#assignTree"), setting, nodeList);
	                    this.updateNodes(true,nodeList);
	                    this.showMenu();
	                } else {
	                    //隐藏树
	                    //hideMenu();
	                    this.InitialZtree();
	                }
	            },	            
	            saveZtreeData:function(){
	            	//获取ztree勾选的节点
					//先获得所有的菜单节点					
					var that = this;					
					var treeObj=$.fn.zTree.getZTreeObj("assignTree");
					//再获得已勾选的节点，勾选一个子节点，父节点会自动勾选
					var nodes=treeObj.getCheckedNodes(true);
					console.log(JSON.stringify(that.couponArticleList));
					if(that.couponArticleList.length == 0){
						var temp = new Object();
						temp.articleList = [];
						that.couponArticleList.push(temp);
					}
					that.couponArticleList.forEach(function(item){
						var temp = item;
						for(var i=0;i<nodes.length;i++){
							if(nodes[i].page != null){
								if(temp.id == nodes[i].page){
									var flag = false;
									if(temp.articleList.length>0){
										temp.articleList.forEach(function(res){
											if(res.id == nodes[i].id){
												flag = true;
											}
										})
									}
									
									if(!flag){
										var obj = new Object();
										obj.id = nodes[i].id;
										obj.pId = nodes[i].pId;
										obj.name = nodes[i].name;
										obj.page = nodes[i].page;
										temp.articleList.push(obj);
									}
								}
							}
						}
					})
					console.log(JSON.stringify(that.couponArticleList));
					this.closeTreeView();
	            },
            }           
        }
		
		//原型方法
		Array.prototype.contains = function (obj) {
		    var i = this.length;
		    while (i--) {
		      if (this[i] === obj) {
		        return true;
		      }
		    }
		    return false;
	  	}
		vueObj = C.vueObj(option);

		// 授权树初始化
        var setting = {
            data : {
                key : {
                    title : "t"
                },
                simpleData : {
                    enable : true
                }
            },
            view: {
		      	fontCss: getFontCss
		    },
            check : {
                enable : true,//让树形菜单前可以显示CheckBox
            }
        };		
		function getFontCss(treeId, treeNode) {
		    return (!!treeNode.highlight) ? {color:"#A60000","font-weight":"bold","font-style":"italic"} : {color:"#333", "font-weight":"normal"};
	  	}
	}());
	
</script>