<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<!-- 添加 Brand 信息  Modal  start -->
	<div class="modal fade" id="brandInfoModal" tabindex="-1" role="dialog">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header text-center">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" :class="{ 'text-success':brandInfo.brandName?true:false }"><strong>{{brandInfo.brandName?brandInfo.brandName:"新建商家信息"}}</strong></h3>
	      </div>
	      <div class="modal-body">
           	<form role="form" class="form-horizontal" id="brandForm" action="{{brandInfo.id?'brand/modify':'brand/create'}}"  @submit.prevent="save">
           		<h4 class="text-center"><strong>品牌基本信息</strong></h4>
           		<div class="form-group">
           			<label class="col-sm-3 control-label">品牌名称：</label>
				    <div class="col-sm-8">
				      <input type="text" class="form-control" required name="brandName" v-model="brandInfo.brandName">
				    </div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">所属队列：</label>
					<div class="col-sm-8">
						<select class="form-control" name="mqId" :value="brandInfo.mqId" required>
							<option v-for="mq in  allMq" :value="mq.id">
								{{mq.mqName}}
							</option>
						</select>
					</div>
				</div>
           		<div class="form-group">
           			<label class="col-sm-3 control-label">品牌标识：</label>
				    <div class="col-sm-8">
						<input type="text" class="form-control" required name="brandSign" v-model="brandInfo.brandSign">
				    </div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">图片路径：</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" required name="wechatImgUrl" v-model="brandInfo.wechatImgUrl">
					</div>
					<button type="button" class="btn btn-default tooltips" data-toggle="tooltip" data-placement="top" title="微信前端图片的显示路径"><span class="glyphicon glyphicon-question-sign"></span></button>
				</div>
				<%--<div class="form-group">--%>
					<%--<label class="col-sm-3 control-label">服务器IP：</label>--%>
					<%--<div class="col-sm-8">--%>
						<%--<input type="text" class="form-control" required name="serverIp" v-model="brandInfo.serverIp">--%>
					<%--</div>--%>
				<%--</div>--%>
				<div class="form-group">
           			<label class="col-sm-3 control-label">短信单价：</label>
				    <div class="col-sm-3">
						<input type="text" class="form-control" required name="smsAcount.smsUnitPrice" v-model="brandInfo.smsAcount.smsUnitPrice">
				    </div>
           			<label class="col-sm-2 control-label">短信数：</label>
				    <div class="col-sm-3">
						<input type="number" min="1" class="form-control" required name="smsAcount.remainderNum" v-model="brandInfo.smsAcount.remainderNum" :disabled="!brandInfo.editRemainderNum">
				    </div>
				    <button type="button" class="btn btn-default tooltips" data-toggle="tooltip" data-placement="top" title="品牌短信账户的初始余额"><span class="glyphicon glyphicon-question-sign"></span></button>
				</div>

				<div class="form-group">
           			<label class="col-sm-3 control-label">提醒额度：</label>
				    <div class="col-sm-8">
						<input type="text" class="form-control" required name="smsAcount.smsRemind" v-model="brandInfo.smsAcount.smsRemind" placeholder="如有多个值，请以逗号隔开。如 1000,500,0,-500,-1000">
				    </div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">预警手机号：</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" required name="smsAcount.smsNoticeTelephone" v-model="brandInfo.smsAcount.smsNoticeTelephone" placeholder="如有多个值，请以逗号隔开">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">合同信息：</label>
					<div class="col-sm-8">
						<select class="form-control" v-model="brandInfo.contractId" v-show="contracts.length > 0">
							<option value="{{contract.id}}" v-for="contract in contracts">{{contract.brandName}}</option>
						</select>
						<input v-else type="text" class="form-control" value="暂无合同信息" disabled>
					</div>
				</div>

				<p class="text-danger text-center" v-if="showMsg"><strong>品牌标识已经存在！</strong></p>
				<h4 class="text-center"><strong>微信配置信息</strong></h4>
           		<div class="form-group">
				    <label class="col-sm-3 control-label">App Id：</label>
				    <div class="col-sm-8">
						<input type="text" class="form-control" required name="wechatConfig.appid" v-model="brandInfo.wechatConfig.appid">
				    </div>
				</div>
				<div class="form-group">
				    <label class="col-sm-3 control-label">App Secret：</label>
				    <div class="col-sm-8">
						<input type="text" class="form-control" required name="wechatConfig.appsecret" v-model="brandInfo.wechatConfig.appsecret">
				    </div>
				</div>
				<%--<div class="form-group">
					<label class="col-sm-3 control-label">Card ID：</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="wechatConfig.cardId" v-model="brandInfo.wechatConfig.cardId">
					</div>
				</div>--%>
				<div class="form-group">
				    <label class="col-sm-3 control-label">支付ID：</label>
				    <div class="col-sm-8">
						<input type="text" class="form-control" required name="wechatConfig.mchid" v-model="brandInfo.wechatConfig.mchid">
				    </div>
				</div>
				<div class="form-group">
				    <label class="col-sm-3 control-label">支付秘钥：</label>
				    <div class="col-sm-8">
						<input type="text" class="form-control" required name="wechatConfig.mchkey" v-model="brandInfo.wechatConfig.mchkey">
				    </div>
				</div>
				<h4 class="text-center"><strong>数据库配置信息</strong></h4>
           		<div class="form-group">
				    <label class="col-sm-3 control-label">数据库名称：</label>
				    <div class="col-sm-8">
						<input type="text" class="form-control" required name="databaseConfig.name" v-model="brandInfo.databaseConfig.name">
				    </div>
				</div>
           		<div class="form-group">
				    <label class="col-sm-3 control-label">数据库URL：</label>
				    <div class="col-sm-8">
						<input type="text" class="form-control" required pattern="^((?!DBName).)*$" name="databaseConfig.url" v-model="brandInfo.databaseConfig.url">
				    </div>
				</div>
           		<div class="form-group">
				    <label class="col-sm-3 control-label">数据库驱动名称：</label>
				    <div class="col-sm-8">
						<input type="text" class="form-control" required name="databaseConfig.driverClassName" v-model="brandInfo.databaseConfig.driverClassName">
				    </div>
				</div>
           		<%--<div class="form-group">--%>
				    <%--<label class="col-sm-3 control-label">数据库用户名：</label>--%>
				    <%--<div class="col-sm-8">--%>
						<%--<input type="password" class="form-control" required name="databaseConfig.username" v-model="brandInfo.databaseConfig.username">--%>
				    <%--</div>--%>
				<%--</div>--%>
           		<%--<div class="form-group">--%>
				    <%--<label class="col-sm-3 control-label">数据库密码：</label>--%>
				    <%--<div class="col-sm-8">--%>
						<%--<input type="password" class="form-control" required name="databaseConfig.password" v-model="brandInfo.databaseConfig.password">--%>
				    <%--</div>--%>
				<%--</div>--%>
				<div class="text-center">
					<input type="hidden" name="id" v-model="brandInfo.id" />
					<input type="hidden" name="databaseConfig.id" v-model="brandInfo.databaseConfig.id" />
					<input type="hidden" name="wechatConfig.id" v-model="brandInfo.wechatConfig.id"/>
					<input type="hidden" name="smsAcount.id" v-model="brandInfo.smsAcount.id"/>
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<input class="btn green" type="submit" value="保存"/>
				</div>
           	</form>
	      </div>
	    </div>
	  </div>
	</div>
	<!-- 添加 Brand 信息  Modal  end -->

	<div class="form-div row" v-if="modulelistDialog.show">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki">功能开启设置</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form role="form" action="brand/modify_modulesetting" id="modify_modulesetting">
						<div class="form-body">
							<div class="row">
								<div class="col-md-4"  v-for="module in moduleList">
									<label >
								    	<input type="checkbox" name="moduleIds" :value="module.id"  v-model="modulelistDialog.hasModule" @click="saveModuleSetting"> {{module.name}}
								    </label>
								</div>
							</div>
						</div>
						<input type="hidden" name="brandId" v-model="modulelistDialog.id" />
						<a class="btn default btn-block" @click="cancellist" >关闭</a>
					</form>
	            </div>
	        </div>
		</div>
	</div>


	<div class="form-div row" v-if="blackDialog.show">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki">黑名单</span>
					</div>
				</div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">黑名单：</label>
                    <div class="col-sm-8">
                         <textarea class="form-control" rows="4" id="phoneList" name="phoneList" placeholder="逗号隔开，可填写wechat_id 或者手机号码"
                                   v-model="blackDialog.phoneList"></textarea>
                    </div>
                </div>
				<div class="portlet-body text-center">
                    <button type="button" class="btn btn-default" data-dismiss="modal" @click="cancelBlack" style="padding-right: 20px">取消</button>
                    <input class="btn green" type="button" value="保存" @click="savePhoneList"/>
					<%--<a class="btn default btn-block" @click="cancelBlack" >关闭</a>--%>
				</div>
			</div>
		</div>
	</div>


	<div class="form-div row" v-if="whiteDialog.show">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki">白名单</span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">白名单：</label>
					<div class="col-sm-8">
                         <textarea class="form-control" rows="4" id="whitePhoneList" name="whitePhoneList" placeholder="逗号隔开，可填写wechat_id 或者手机号码"
								   v-model="whiteDialog.phoneList"></textarea>
					</div>
				</div>
				<div class="portlet-body text-center">
					<button type="button" class="btn btn-default" data-dismiss="modal" @click="cancelWhite" style="padding-right: 20px">取消</button>
					<input class="btn green" type="button" value="保存" @click="saveWhitePhoneList"/>
					<%--<a class="btn default btn-block" @click="cancelBlack" >关闭</a>--%>
				</div>
			</div>
		</div>
	</div>



	<!-- 查看 数据库配置 详细信息  Modal  start-->
	<div class="modal fade" id="databaseConfigDetail" tabindex="-1" role="dialog">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title text-center"><strong>数据库配置 【<font color="#5cb85c">{{brandInfo.brandName}}</font>】</strong></h4>
	      </div>
	      <div class="modal-body">
	      	<dl class="dl-horizontal">
				<dt>数据库ID：</dt><dd>{{brandInfo.databaseConfig.id}}</dd><br/>
				<dt>数据库名称：</dt><dd>{{brandInfo.databaseConfig.name}}</dd><br/>
				<dt>数据库URL：</dt><dd style=" word-wrap: break-word;">{{brandInfo.databaseConfig.url}}</dd><br/>
				<dt>数据库驱动名称：</dt><dd>{{brandInfo.databaseConfig.driverClassName}}</dd><br/>
				<dt>数据库用户名：</dt><dd>*****</dd><br/>
				<dt>数据库密码：</dt><dd>*****</dd><br/>
				<dt>创建时间：</dt><dd>{{brandInfo.databaseConfig.createTime}}</dd><br/>
				<dt>上次修改时间：</dt><dd>{{brandInfo.databaseConfig.updateTime}}</dd><br/>
			</dl>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	      </div>
	    </div>
	  </div>
	</div>
	<!-- 查看 数据库配置 详细信息  Modal  end-->

	<!-- 查看 微信配置 详细信息  Modal  start-->
	<div class="modal fade" id="wechatConfigDetail" tabindex="-1" role="dialog">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title text-center"><strong>微信配置 【<font color="#5cb85c">{{brandInfo.brandName}}</font>】</strong></h4>
	      </div>
	      <div class="modal-body">
	      	<dl class="dl-horizontal">
				<dt>微信ID：</dt><dd>{{brandInfo.wechatConfig.id}}</dd><br/>
				<dt>AppID：</dt><dd>{{brandInfo.wechatConfig.appid}}</dd><br/>
				<dt>AppSecret：</dt><dd>{{brandInfo.wechatConfig.appsecret}}</dd><br/>
				<dt>支付ID：</dt><dd>{{brandInfo.wechatConfig.mchid}}</dd><br/>
				<dt>支付秘钥：</dt><dd>{{brandInfo.wechatConfig.mchkey}}</dd><br/>
			</dl>
	      </div>
	      <div class="modal-footer">
			  <div style="float: left">
				  <a :href="'brand/getWxMpQrCode?appId='+brandInfo.wechatConfig.appid+'&secret='+brandInfo.wechatConfig.appsecret" target="_blank" title="test" class="btn btn-primary">公众号二维码</a>
				  <a :href="'http://106.14.44.167:9999/token/getstr/'+brandInfo.wechatConfig.appid+'/'+brandInfo.wechatConfig.appsecret" target="_blank" class="btn btn-primary">AccessToken</a>
			  </div>
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	      </div>
	    </div>
	  </div>
	</div>
	<!-- 查看 微信配置 详细信息  Modal  end-->

	<!-- 表格   start-->
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="brand/add">
			<button class="btn green pull-right" @click="create">新建</button>
			</s:hasPermission>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter">&nbsp;</div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered "></table>
		</div>
	</div>
	<!-- 表格   end-->
</div>


<script>
	$(document).ready( function () {
		//载入 表格数据
	    tb = $('.table-body>table').DataTable({
	    	ordering: false,//禁止排序
			ajax : {
				url : "brand/list_all",
				dataSrc : ""
			},
			columns : [
				{
					title : "品牌名称",
					data : "brandName",
				},
				{
					title : "品牌标识",
					data : "brandSign",
				},
				{
					title : "短信条数（条）",
					data : "smsAcount.remainderNum",
				},
				{
					title : "短信单价（元）",
					data : "smsAcount.smsUnitPrice",
				},
				{
					title : "短信提醒设置",
					data : "smsAcount.smsRemind",
				},
				{
					title : "微信配置ID",
					data : "wechatConfigId",
					createdCell:function(td,tdData,rowData,row){
						var button = $("<button class='btn'>点击查看详情</button>");
						button.click(function(){
							vueObj.showDetailInfo(rowData);
							$("#wechatConfigDetail").modal();
						});
						$(td).html(button);
					}
				},
				{
					title : "数据库配置ID",
					data : "databaseConfigId",
					createdCell:function(td,tdData,rowData,row){
						var button = $("<button class='btn'>点击查看详情</button>");
						button.click(function(){
							//格式化 时间
							rowData.databaseConfig.createTime = new Date(rowData.databaseConfig.createTime).format("yyyy-MM-dd hh:mm:ss");
							rowData.databaseConfig.updateTime = new Date(rowData.databaseConfig.updateTime).format("yyyy-MM-dd hh:mm:ss");
							vueObj.showDetailInfo(rowData);
							$("#databaseConfigDetail").modal();
						});
						$(td).html(button);
					}
				},
				//{
				//	title : "品牌管理员ID",
				//	data : "brandUserId",
				//},
				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name='brand/delete'>,
							C.createDelBtn(tdData,"brand/delete"),
							</s:hasPermission>,
							<s:hasPermission name='brand/edit'>,
							createEditBtn(tdData,rowData),
							createModuleEditBtn(tdData),
							</s:hasPermission>
                          <s:hasPermission name='brand/distributionOrDelete'>,
                            distributionOrDelete(tdData,rowData),
                            </s:hasPermission>
                            <s:hasPermission name='brand/createToken'>,
                            createTokenBtn(rowData),//自定义方法生成token
                            </s:hasPermission>
                            createBlackPeopleBtn(tdData),
                            createWhitePeopleBtn(tdData)
						];
						$(td).html(operator);
					}
				}],
	    });

	    var C = new Controller("#control",tb);

		var vueObj = new Vue({
			el:"#control",
			data:{
				brandInfo:{},
				showMsg:false,
				modulelistDialog:{show:false,id:"",hasModule:[]},
				moduleList:[],
                allMq:[],
				blackDialog:{show:false,id:"",phoneList:[]},
				whiteDialog:{show:false,id:"",phoneList:[]},
                contracts : []
			},
			methods:{
				cancellist:function(){
					this.modulelistDialog.show=false;
					tb.ajax.reload();
				},
                cancelBlack:function(){
                    this.blackDialog.show=false;
                    tb.ajax.reload();
                },
                cancelWhite:function(){
                    this.whiteDialog.show=false;
                    tb.ajax.reload();
                },
                savePhoneList:function(){
                    var that = this;
                    $.post("brand/savePhoneList",{brandId:that.blackDialog.id,phoneList:$('#phoneList').val()},function(result){
                        that.blackDialog.show=false;
                        tb.ajax.reload();
                    });
                },
                saveWhitePhoneList:function(){
                    var that = this;
                    $.post("brand/saveWhitePhoneList",{brandId:that.whiteDialog.id,phoneList:$('#whitePhoneList').val()},function(result){
                        that.whiteDialog.show=false;
                        tb.ajax.reload();
                    });
                },
				create:function(){

					var brandInfo = {};
					brandInfo.editRemainderNum = true;//设置可以编辑短信账户余额
					//设置默认值
					brandInfo.databaseConfig = {};
					brandInfo.databaseConfig.url = "jdbc:mysql://rds64fw2qrd8q0eg95nmo.mysql.rds.aliyuncs.com:3306/DBName?useUnicode=true&characterEncoding=utf8";
					brandInfo.databaseConfig.driverClassName = "com.mysql.jdbc.Driver";
					brandInfo.smsAcount = {};
					brandInfo.smsAcount.smsRemind = "1000,500,0,-500,-1000";
					brandInfo.smsAcount.smsUnitPrice = "0.1";
					brandInfo.smsAcount.remainderNum = "20000";
					brandInfo.mqId = null;
                    this.brandInfo = brandInfo;
                    this.showMsg = false;
					$("#brandInfoModal").modal();

				},
				showDetailInfo:function(brandInfo){
					this.brandInfo = brandInfo;
				},
				edit:function(brandId,brandInfo){

                    this.brandInfo = brandInfo;

                    this.brandInfo.editRemainderNum = false;//设置不能编辑短信账户余额
                    this.showMsg = false;
                    $("#brandInfoModal").modal();

				},
				save:function(e){
					var brandSign = this.brandInfo.brandSign;
					var brandId = this.brandInfo.id;
					$.post("brand/validateInfo",{brandId:brandId,brandSign:brandSign},function(result){
						if(result.success){
							C.ajaxFormEx(e.target,function(){
								$("#brandInfoModal").modal("hide");//关闭模态框
								tb.ajax.reload();
							});
						}else{
							vueObj.showMsg = true;
						}
					});
				},
                getContractInfo : function () {
				    var that = this;
					$.post("brand/getContractInfo", function (result) {
						if (result.success){
						    that.contracts = result.data;
						    if (result.data.length > 0){
						        that.brandInfo.contractId = result.data[0].id;
							}
						}
                    });
                },
				saveModuleSetting:function(e){
// 					var data = $(e.target).serialize();
					var data = $("#modify_modulesetting").serialize();
					$.post("brand/modify_modulesetting",data,function(result){
						if(result.success){
// 							vueObj.modulelistDialog.show=false;
							toastr.success("保存成功");
							tb.ajax.reload();
						}else{
							toastr.error(result.message);
						}
					});
				},
			},
			created:function(){
				var that = this;
				$.post("modulelist/list_all",null,function(result){
					that.moduleList = result;
				});
                $.ajax({
                    url: "mqConfig/list_all",
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        that.allMq = data.data;
                    }

                });
                this.getContractInfo();
			}
		});

		//创建 修改按钮
		function createEditBtn(brandId,brandInfo){

			var button = $("<button class='btn btn-xs btn-primary'>编辑</button>");
			button.click(function(){
				vueObj.edit(brandId,brandInfo);
			});
			return button;
		}
		function createModuleEditBtn(brandId){
			var button = $("<button class='btn btn-xs green'>选择功能项</button>");
			button.click(function(){
				vueObj.modulelistDialog.id=brandId;
				vueObj.modulelistDialog.show=true;
				vueObj.modulelistDialog.hasModule=[];
				$.post("brand/list_hasmodule",{id:brandId},function(result){
					vueObj.modulelistDialog.hasModule=result.data;
					Vue.nextTick(function(){
						App.updateUniform();
					});
				});
				Vue.nextTick(function(){
					App.initUniform();
				});
			});
			return button;
		}
        function createBlackPeopleBtn(brandId){
            var button = $("<button class='btn btn-xs green'>黑名单</button>");
            button.click(function(){
                vueObj.blackDialog.id=brandId;
                vueObj.blackDialog.show=true;
                vueObj.blackDialog.phoneList=[];
                $.post("brand/list_one",{id:brandId},function(result){
                    vueObj.blackDialog.phoneList=result.data.phoneList;
                    Vue.nextTick(function(){
                        App.updateUniform();
                    });
                });
                Vue.nextTick(function(){
                    App.initUniform();
                });
            });
            return button;
        }
        function createWhitePeopleBtn(brandId){
            var button = $("<button class='btn btn-xs green'>白名单</button>");
            button.click(function(){
                vueObj.whiteDialog.id=brandId;
                vueObj.whiteDialog.show=true;
                vueObj.whiteDialog.phoneList=[];
                $.post("brand/list_one",{id:brandId},function(result){
                    vueObj.whiteDialog.phoneList=result.data.whitePhoneList;
                    Vue.nextTick(function(){
                        App.updateUniform();
                    });
                });
                Vue.nextTick(function(){
                    App.initUniform();
                });
            });
            return button;
        }
		function createTokenBtn (rowData) {
            var button = $("<button class='btn btn-xs red'>生成toKen</button>");
            button.click(function(){
                console.log(rowData)
                $.ajax({
                    url:"brand/createToken",
                    data:{
                        appid:rowData.wechatConfig.appid,
                        appsecret:rowData.wechatConfig.appsecret,
                        brandsign:brandSign,
                    }

                })
            });
            return button;
        }

        function distributionOrDelete(tbData,rowData){
		    var text = rowData.definedSelf? "关闭自定义消息模板":"开启自定义消息模板";
		    var definedSelf = rowData.definedSelf? 0:1;
            var button = $("<button class='btn btn-xs btn-primary'>"+text+"</button>");
            button.click(function(){

                $.ajax({
                    type: 'POST',
                    url: "/resto/brandtemplateedit/distributionOrDelete",
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8',
                    data:JSON.stringify({id:tbData,definedSelf:definedSelf,appId:rowData.wechatConfig.appid}),
                    success: function (result) {
                        if(result.success){
                            if(rowData.definedSelf){
                                C.simpleMsg("关闭成功");
                            }
                            else {
                                C.simpleMsg("开启成功");
                            }
//
                            tb.ajax.reload();
                        }else {
                            if (rowData.definedSelf) {
                                C.simpleMsg("关闭失败");
                            }
                            else {
                                C.simpleMsg("开启失败");


                            }
                        }
                        },
                    error: function (err) {
                        if(rowData.definedSelf){
                            C.simpleMsg("关闭失败");
                        }
                        else {
                            C.simpleMsg("开启失败");
                        }
                    }
                });


            });
            return button;
        }

	} );

</script>