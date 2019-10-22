<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki">添加商家用户</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form class="form-horizontal" id="brandUserForm" role="form" action="{{m.id?'branduser/modify':'branduser/create'}}" @submit.prevent="save">
				  		<div class="form-body">
							<div class="form-group">
							  <label class="col-sm-3 control-label">登录账号：</label>
							  <div class="col-sm-8">
							    <input type="text" class="form-control" id="userName" name="username" required v-model="m.username" v-if="m.id" readonly>
							    <input type="text" class="form-control" id="userName" name="username" required v-model="m.username" v-if="!m.id">
							  </div>
							</div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">姓名：</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" id="name" name="name" v-model="m.name">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">性别：</label>
                                <div class="col-md-8">
                                    <label class="radio-inline">
                                        <input type="radio" name="sex" v-model="m.sex" value="1">男
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="sex" v-model="m.sex" value="2">女
                                    </label>
                                </div>
                            </div>
							<div class="form-group">
							  <label class="col-sm-3 control-label">密&nbsp;&nbsp;码：</label>
							  <div class="col-sm-8">
							    <input type="text" class="form-control" name="password" required v-model="m.password">
							  </div>
							</div>
							<div class="form-group">
							  <label class="col-sm-3 control-label">邮&nbsp;&nbsp;箱：</label>
							  <div class="col-sm-8">
							    <input type="email" class="form-control" name="email" v-model="m.email">
							  </div>
							</div>
							<div class="form-group">
							  <label class="col-sm-3 control-label">电&nbsp;&nbsp;话：</label>
							  <div class="col-sm-8">
							    <input type="text" class="form-control" name="phone" v-model="m.phone">
							  </div>
							</div>
							<div class="form-group">
							  <label class="col-sm-3 control-label">所属品牌：</label>
							  <div class="col-sm-8">
									<span v-if="m.id">
										<select class="form-control" name="brandId" id="select_brandId"  v-model="m.brandId">
											<option v-for="brand in brandSelect" :value="brand.id">
												{{brand.brandName}}
											</option>
										</select>
									</span>
									<span v-if="!m.id">
										<input type="hidden" name="brandId" :value="brandId">
										<input type="text" class="form-control"
											placeholder="请输入品牌名检索" v-model="inputName" required="required" id="select_brandId"/>
										<ul v-show="flg">
											<li v-for="brand in brandList | filterBy inputName in 'brandName'"><a @click="selectBrand(brand)">{{brand.brandName}}</a></li>
										</ul>
									</span>

							  </div>
							</div>
							<div class="form-group">
							  <label class="col-sm-3 control-label">店铺名称：</label>
							  <div class="col-sm-8">
									<span v-if="m.id">
										<select class="form-control" name="shopDetailId" id="select_shopDetailId"  v-model="m.shopDetailId">
											<option v-for="shop in selection" :value="shop.id">
												{{shop.name}}
											</option>
										</select>
									</span>
									<span v-if="!m.id">
										<select class="form-control" v-model="shopDetailId" name="shopDetailId" id="select_shopDetailId"
												required="required">
									  		<option v-for="shop in shopList" value="{{shop.id}}">{{ shop.name }}</option>
								  		</select>
									</span>
							  </div>
							</div>
							<div class="form-group">
							  <label class="col-sm-3 control-label">用户角色：</label>
							  <div class="col-sm-8">
							  	<select class="form-control" name="roleId" id="select_roleId" required v-model="m.roleId">
								    <option v-for="role in roleSelect" :value="role.id">
								        {{ role.roleName }}
								    </option>
								  </select>
							  </div>
							</div>
							<div class="form-group text-center" v-if="errorShow">
								<label class="control-label"><font color="red">{{ errorMsg }}</font></label>
							</div>
							<div class="form-group text-center">
								<input type="hidden" name="id" v-model="m.id" />
							  	<input class="btn green" type="submit" value="保存"/>
							  	<a class="btn default" @click="cancel">取消</a>
							</div>
						</div>
					</form>
	            </div>
	        </div>
		</div>
	</div>
	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="branduser/add">
			<button class="btn green pull-right" @click="create">新建</button>
			</s:hasPermission>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter">&nbsp;</div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered "></table>
		</div>
	</div>
</div>


<script>
	//定义全局变量
	var vueObj ;
	var operation = "create";//用于判断操作类型
	selectBrandInfo();	//加载下拉列表的信息
	(function(){
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "branduser/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
					title : "登录账号",
					data : "username",
				},
                {
                    title : "姓名",
                    data : "name",
                    createdCell:function(td,tdData){
                        var name = tdData;
                        if (tdData == null){
                            name = "--";
                        }
                        $(td).html(name);
                    }
                },
                {
                    title : "性别",
                    data : "sex",
                    createdCell:function(td,tdData){
                        var sex;
                        if (tdData != null){
                            sex = (tdData == 1 ? "男" : "女")
                        }else{
                            sex = "--";
                        }
                        $(td).html(sex);
                    }
                },
				{                 
					title : "邮&nbsp;&nbsp;箱",
					data : "email",
				},                 
				{                 
					title : "电&nbsp;&nbsp;话",
					data : "phone",
				},                 
				{                 
					title : "所属品牌",
					data : "brandName",
				},                 
				{                 
					title : "店铺名称",
					data : "shopName",
				},                 
				{                 
					title : "用户角色",
					data : "roleName",
				},
				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="branduser/delete">
							C.createDelBtn(tdData,"branduser/delete"),
							</s:hasPermission>
							<s:hasPermission name="branduser/edit">,
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		
		var C = new Controller(cid,tb);
		
		//重写 option
		var option = {
				el:cid,
				data:{
					m:{},
					showform:false,
					errorMsg:"请录入完整信息！",
					errorShow:false,
					brandSelect:{},
					roleSelect:{},
					selected:"",
                     brandList : [],
                     shopList : [],
                     brandId : "31946c940e194311b117e3fff5327215",//默认选中  测试品牌
                     flg : false, //用来标识是否显示模糊查询,
                     lastSelectName : ""
				},
				created : function() {
					this.getBrandList();
					this.getShopList(this.brandId);
				},
				watch : {
                     "brandId" : function(val, oldVal) {
                         this.getShopList(val);
                     },
					"inputName" : function (val) {
						if (val != this.lastSelectName) {
							this.flg = true;
						}
						if (!val){
							this.flg = false;
						}
					}
				},
				methods:{
					openForm:function(){
						this.showform = true;
					},
					closeForm:function(){
						this.m={};
						this.showform = false;
					},
					cancel:function(){
						this.errorMsg = "" ;
						this.errorShow = false ;
						this.m={};
						this.closeForm();
					},
					create:function(){
						operation = "create";
						this.m={};
						this.openForm();
					},
					edit:function(model){
						operation = "edit";
						this.m= model;
						this.openForm();
					},
					save:function(e){
						//接收返回值
						var result = isNullCheck();
						if(result == true){//如果通过 则 执行响应方法
							var that = this;
							var formDom = e.target;
							C.ajaxFormEx(formDom,function(){
								that.cancel();
								tb.ajax.reload();
							});
						}else{//如果为有反馈信息则给出提示
							this.errorMsg = result ;
							this.errorShow = true ;
						}
					},
                    selectBrand : function (brand) {
                        this.inputName = brand.brandName;
                        this.lastSelectName = brand.brandName;
                        this.flg = false;
                        this.brandId = brand.id;
                    },
                    getBrandList : function() {
                        var that = this;
                        $.post("userdata/queryBrands", function(result) {
                            if (result.success) {
                                that.brandList = result.data;
                                //that.brandId = result.data[0].id;
                            } else {
                                that.errorMsg(result.message);
                            }
                        });
                    },
                    getShopList : function(brandId) {
                        var that = this;
                        $.post("userdata/queryShops", {
                            brandId : brandId
                        }, function(result) {
                            if (result.success) {
                                that.shopList = result.data;
                                that.shopId = result.data[0].id;
                            } else {
                                that.errorMsg(result.message);
                            }
                        })
                    },
				},
				computed: {
				    selection: {
				      get: function() {
				    	 //判断 brandSelect 对象是否为空
				    	 if(this.brandSelect.length){
				    		 var selected_temp = this.m.brandId;
				    		 if(!selected_temp){
				    			 selected_temp = this.brandSelect[0].id;
				    		 }
					    	 var temp = this.brandSelect.filter(function(item) {
					             return item.id == selected_temp;
					         });
					    	 if(temp.length > 0){
					    		 return temp[0].shopDetail; 
					    	 }
				    	 }
				    	 return null;
				      }
				    }
				}
			};
		vueObj = C.vueObj(option);
	}());
	
	
	//判断表单中是否有空值
	function isNullCheck(){
		var flag = true;
		if(operation == "create" && isNull("password")){
			flag = "密码 不能为空！" ;
		}else if(isNull("email")){
			flag = "邮箱 不能为空！" ;
		}else if(isNull("phone")){
			flag = "电话 不能为空！" ; 
		}else if($("#select_brandId").val() == "" || $("#select_brandId").val() == null){
			flag = "请选择 所属品牌！" ;
		}else if($("#select_shopDetailId").val() == "" || $("#select_shopDetailId").val() == null){
			flag = "请选择 店铺名称！" ;
		}else if($("#select_roleId").val() == "" || $("#select_roleId").val() == null){
			flag = "请选择 用户角色！" ;
		}
		return flag;
	}
	
	//验证  文本框 是否为空
	function isNull(str){
		return $("#brandUserForm input[name='"+str+"']").val().replace(/(^s*)|(s*$)/g, "").length == 0;
	}
	
	//ajax 查询商家用户关联的信息
	function selectBrandInfo(){
		//加载 品牌 和 店铺 信息
		$.ajax({
			   type: "POST",
			   url: "brand/queryBrandAndShop",
			   success: function(data){
				   vueObj.$set("brandSelect",data);
			   }
			});
		//加载 商家组的 角色 信息
		$.ajax({
			   type: "POST",
			   data:"userGroupId=2",
			   url: "role/listData",
			   success: function(data){
				   vueObj.$set("roleSelect",data);
			   }
		});
	}
	
</script>
