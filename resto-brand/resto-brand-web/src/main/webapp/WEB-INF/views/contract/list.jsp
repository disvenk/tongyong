<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="v-bind" uri="http://www.springframework.org/tags/form" %>
<style>
	.form-horizontal .control-label {
		text-align: left;
	}
</style>
<div id="control">
	<!-- Modal -->
	<div class="modal fade bs-example-modal-lg" id="contractModal" tabindex="-1" role="dialog" aria-labelledby="contractModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 id="myModalLabel" style=text-align:center; ><strong>合同管理</strong></h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" action="{{m.id?'contract/modify':'contract/create'}}"  @submit.prevent="save" id="contractForm">
						<h4>基本信息</h4>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">合同编号</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="constractNum" v-model="m.constractNum" required>
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">签约金额</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="signMoney" v-model="m.signMoney" required>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">品牌名称</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="brandName" v-model="m.brandName" required>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">签约门店数</label>
									<div class="col-sm-8">
										<input type="number" class="form-control" name="shopNum" v-model="m.shopNum">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">签约时间</label>
									<div class="col-sm-8">
										<input type="text" class="form-control form_datetime" id="signTime" name="signTime" v-model="m.signTime" readonly="readonly">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">签约门店</label>
									<div class="col-sm-9">
										<textarea name="shopNames" v-model="m.shopNames" cols="100" rows="3"></textarea>
									</div>
								</div>
							</div>
						</div>

						<h4>甲方</h4>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">公司名称</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="aCompanyName" v-model="m.aCompanyName" required>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">签约人</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="aSigntory" v-model="m.aSigntory" required>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">联系电话</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="aTelephone" v-model="m.aTelephone" required>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">电子邮箱</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="aEmail" v-model="m.aEmail">
									</div>
								</div>
							</div>
						</div>

						<h4>乙方</h4>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">公司名称</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="bCompanyName" v-model="m.bCompanyName">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">签约人</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="bSigntory" v-model="m.bSigntory">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">联系电话</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="bTelephone" v-model="m.bTelephone">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">电子邮箱</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="bEmail" v-model="m.bEmail">
									</div>
								</div>
							</div>
						</div>


						<h4>计费信息</h4>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="col-sm-3 control-label">计费方式：</label>
									<div class="col-sm-8">
										<label>
											<input type="radio" name="chargeMode" v-model="m.chargeMode"  value="1" checked > 按时间
										</label>
										<label>
											<input type="radio" name="chargeMode" v-model="m.chargeMode"  value="2"> 按效果
										</label>
									</div>
								</div>
							</div>


							<div class="col-md-6" v-if="m.chargeMode==1" >
								<div class="form-group">
									<label class="col-sm-3 control-label">每年收取</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="yearMoney" v-model="m.yearMoney">
									</div>
								</div>
							</div>
						</div>

						<div v-if="m.chargeMode==1">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label">有效期</label>
										<div class="col-sm-4">
											<input type="text" class="form-control" name="validity" v-model="m.validity">
										</div>
										<span class="col-sm-2"> 年</span>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label">上线时间</label>
										<div class="col-sm-8">
											<input type="text" class="form-control form_datetime" name="onlineTime" v-model="m.onlineTime" readonly="readonly">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label">到期时间</label>
										<div class="col-sm-8">
											<input type="text" class="form-control form_datetime" id="expirationTime" name="expirationTime" v-model="m.expirationTime" readonly="readonly">
											<%--<input type="text" class="form-control form_datetime" id="signTime" name="signTime" v-model="m.signTime" >--%>
										</div>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label">短信条数</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" name="smsNum" v-model="m.smsNum">
										</div>
									</div>
								</div>
							</div>
						</div>

						<div v-if="m.chargeMode==2">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label">账户余额</label>
										<div class="col-sm-8">
											<input type="text" v-if="m.id == null || m.id == ''" class="form-control" name="accountBalance" v-model="m.accountBalance">
											<input type="text" v-else disabled class="form-control" name="accountBalance" v-model="m.accountBalance">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label">已使用</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" name="usedBalance" disabled value="0" v-model="m.usedBalance">
										</div>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label">扣费方式：</label>
										<div class="col-sm-8">
											<input type="checkbox"  v-model="m.openNewCustomerRegister" > <strong>新用户注册</strong>
											<input type="hidden" name="openNewCustomerRegister" v-model="m.openNewCustomerRegister">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label">每个新用户</label>
										<div class="col-sm-7">
											<input type="text" class="form-control" name="newCustomerValue" v-model="m.newCustomerValue" pattern="^[0-9]+(.[0-9]{0,2})?$" required>
										</div>
										<span class="col-sm-1" >元</span>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label"></label>
										<div class="col-sm-8">
											<input type="checkbox"  v-model="m.openSendSms" > <strong>短信发送</strong>
											<input type="hidden" name="openSendSms" v-model="m.openSendSms">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label">每条短信</label>
										<div class="col-sm-7">
											<input type="text" class="form-control" name="sendSmsValue" v-model="m.sendSmsValue" pattern="^[0-9]+(.[0-9]{0,2})?$" required>
										</div>
										<span class="col-sm-1" >元</span>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label"></label>
										<div class="col-sm-8">
											<input type="radio"  v-model="m.openAllOrder" v-bind:true-value="1" v-bind:v-bind:false-value="0" value="1" @click="choseAllOrder"/><strong>所有订单</strong>
											<input type="hidden" name="openAllOrder" v-model="m.openAllOrder" >
										</div>
									</div>
								</div>
								<div class="col-md-6" v-if="m.openAllOrder==1">
									<div class="form-group">
										<div class="col-sm-3">
											<select class="form-control" v-model="m.openAllOrderType">
												<option value="1">订单总额</option>
												<option value="2">实付金额</option>
											</select>
										</div>
										<label class="col-md-1 control-label">的</label>
										<div class="col-md-4">
											<div class="input-group">
												<input type="number" class="form-control"  name="allOrderValue" v-model="m.allOrderValue" min="1" max="100">
												<div class="input-group-addon">‰</div>
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label"></label>
										<div class="col-sm-8">
											<input type="radio" name="openBackCustomerOrder" v-model="m.openBackCustomerOrder" v-bind:true-value="1" v-bind:false-value="0" value="1" @click="choseBackCustomerOrder"/><strong>回头用户消费订单</strong>
										</div>
									</div>
								</div>
								<div class="col-md-6" v-if="m.openBackCustomerOrder==1">
									<div class="form-group">
										<div class="col-sm-3">
											<select class="form-control" v-model="m.openBackCustomerOrderType">
												<option value="1">订单总额</option>
												<option value="2">实付金额</option>
											</select>
										</div>
										<label class="col-md-1 control-label">的</label>
										<div class="col-md-4">
											<div class="input-group">
												<input type="number" class="form-control"  name="backCustomerOrderValue" v-model="m.backCustomerOrderValue" min="1" max="100">
												<div class="input-group-addon">‰</div>
											</div>
										</div>
									</div>
								</div>
							</div>


							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label"></label>
										<div class="col-sm-8">
											<input type="checkbox"  v-model="m.openOutFoodOrder" v-bind:true-value="1" v-bind:false-value="0"> <strong>Resto+外卖订单</strong>
											<input type="hidden" v-model="m.openOutFoodOrder" name="openOutFoodOrder">
										</div>
									</div>
								</div>
								<div class="col-md-6" v-if="m.openOutFoodOrder==1">
									<div class="form-group">
										<div class="col-sm-3">
											<select class="form-control" v-model="m.openOutFoodOrderType" >
												<option value="1">订单总额</option>
												<option value="2">实付金额</option>
											</select>
										</div>
										<label class="col-md-1 control-label">的</label>
										<div class="col-md-4">
											<div class="input-group">
												<input type="number" class="form-control"  name="outFoodOrderValue" v-model="m.outFoodOrderValue" min="1" max="100">
												<div class="input-group-addon">‰</div>
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="col-sm-3 control-label"></label>
										<div class="col-sm-8">
											<input type="checkbox"  v-model="m.openThirdFoodOrder" v-bind:true-value="1" v-bind:false-value="0"> <strong>第三方外卖订单</strong>
											<input type="hidden" name="openThirdFoodOrder" v-model="m.openThirdFoodOrder">
										</div>
									</div>
								</div>
								<div class="col-md-6" v-if="m.openThirdFoodOrderType==1">
									<div class="form-group">
										<div class="col-sm-3">
											<select class="form-control" v-model="m.openThirdFoodOrderType" >
												<option value="1">订单总额</option>
												<option value="2">实付金额</option>
											</select>
										</div>
										<label class="col-md-1 control-label">的</label>
										<div class="col-md-4">
											<div class="input-group">
												<input type="number" class="form-control"  name="thirdFoodOrderValue" v-model="m.thirdFoodOrderValue" min="1" max="100">
												<div class="input-group-addon">‰</div>
											</div>
										</div>
									</div>
								</div>
							</div>

						</div>

						<h4>上传附件</h4>

						<div class="form-group">
							<div>
								<input type="hidden" class="form-control" name="picUrl"  v-model="m.picUrl">
								<img-file-upload class="form-control" :cut.sync="cut" @success="uploadSuccess" @error="uploadError">上传文件</img-file-upload>
							</div>
						</div>
						<a v-if="m.picUrl" href={{m.picUrl}} target="_blank" >{{m.brandName}}合同{{m.constractNum}}.pdf</a>
						<a v-if="m.picUrl" href={{m.picUrl}} download={{m.brandName}}合同{{m.constractNum}}.pdf >下载</a>
						<a v-if="m.picUrl" @click="deletePicUrl" >删除</a>


						<input type="hidden" name="id" v-model="m.id" />
						<div class="modal-footer" style="text-align: center;">
						<input class="btn green"  type="submit"  value="保存"/>
						<a class="btn default" @click="cancel" >取消</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="contract/add">
			<%--<button class="btn green pull-right" @click="create">新建</button>--%>
				<!-- Button trigger modal -->
				<button type="button" class="btn btn-primary pull-right"  @click="create">
					新增合同
				</button>
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
	(function(){
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "contract/list_all",
				dataSrc : ""
			},
			columns : [
				{
					title : "合同编号",
					data : "constractNum",
				},
                {
                    title : "公司名称",
                    data : "bCompanyName",
                },
				{
					title : "品牌名称",
					data : "brandName",
				},
				{
					title : "门店数",
					data : "shopNum",
				},
				{
					title : "签约时间",
					data : "signTime",
                    createdCell:function (td,tdData) {
                        $(td).html(vueObj.formatDate(tdData))
                    }
				},
				{
					title : "签约金额",
					data : "signMoney",
				},
                {
                    title : "已收金额",
                    data : "receiveMoney",
                },
                {
                    title : "未收金额",
                    data : "unreceiveMoney",
                },

				{
					title : "计费方式",
					data : "chargeMode",
					createdCell:function (td,tdData) {
                        var temp = '';
                        if(tdData==1){
                            temp = '按时间';
                        }else if(tdData==2){
                            temp = '按效果';
                        }
                        $(td).html(temp);
                    }
				},
                {
                    title : "上线时间",
                    data : "onlineTime",
                    createdCell:function (td,tdData,row,rowData) {
                        console.log(row);
                        if(row.chargeMode==1){
                            $(td).html(vueObj.formatDate(tdData))
                        }else if(row.chargeMode==2){
                            $(td).html("--------")
                        }
                    }
                },
                {
                    title : "到期时间",
                    data : "expirationTime",
                    createdCell:function (td,tdData,row) {
                        console.log(row);
                        if(row.chargeMode==1){
                            $(td).html(vueObj.formatDate(tdData))
                        }else if(row.chargeMode==2){
                            $(td).html("--------")
                        }
                    }
                },
                {
                    title : "剩余时间",
                    data : "expirationTime",
                    createdCell:function (td,tdData,row,rowData) {
                        console.log(row);
                        if(row.chargeMode==1){
                            var beginTime = row.onlineTime;
                            var endTime = tdData;
                            var day = Math.floor((endTime - beginTime)/1000/60/60/24);
                            $(td).html(day);
                        }else if(row.chargeMode==2){
                            $(td).html("--------")
                        }
                    }
                },
                {
                    title : "已使用金额",
                    data : "usedBalance",
                    createdCell:function (td,tdData,row) {
                        console.log(row);
                        if(row.chargeMode==1){
                            $(td).html("--------")
                        }
                    }
                },
                {
                    title : "剩余金额",
                    data : "accountBalance",
                    createdCell:function (td,tdData,row) {
                        console.log(row);
                        if(row.chargeMode==1){
                            $(td).html("--------")
                        }else {
                            $(td).html(tdData - row.usedBalance);
						}
                    }
                },
                {
                    title : "状态",
                    data : "status",
                    createdCell:function (td,tdData) {

                        //1.执行中 2.已签订 3审核中 4意外终止 5到期结束
                        var temp = '';
                        switch(tdData)
                        {
                            case 1:
                                temp = '执行中';
                                break;
                            case 2:
                                temp = '已签订';
                                break;
                            case 3:
                                temp = '审核中';
                                break;
                            case 4:
                                temp = '意外终止';
                                break;
                            case 5:
                                temp = '到期结束';
                                break;

                            default:
                                break;
                        }
                        $(td).html(temp);
                    }

                },
				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="contract/delete">
							C.createDelBtn(tdData,"contract/delete"),
							</s:hasPermission>
							<s:hasPermission name="contract/modify">
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		var C = new Controller(null,tb);
		var vueObj = new Vue({
			el:"#control",
			data:{
			  	cut : "false",//文件是否需要压缩
				m:{},
			},
			watch:{
			  'm.chargeMode':function (newVal,oldVal) {
				  console.log(oldVal+"-----"+newVal);
				  if(oldVal==1&&newVal==2){
				      this.initChargeMode();
				  }

              }

			},
			methods:{
			    initDateTimepicker:function () {
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
                create:function(){
                    this.m={
                        chargeMode:1,
                        aCompanyName : "上海餐加企业管理咨询有限公司"
					};
					this.showModal();
                },
                edit:function(model){
                    this.m= model;
                    this.initTime();
                    if(this.m.openAllOrder==1||this.m.openAllOrder==2){//如果选择了所有订单
                        this.m.openAllOrderType = this.m.openAllOrder;//
					}
                  	if(this.m.openBackCustomerOrder==1||this.m.openBackCustomerOrder==2){//如果选择了回头用户订单
                        this.m.openBackCustomerOrderType = this.m.openBackCustomerOrder;
					}

                    if(this.m.openOutFoodOrder==1||this.m.openOutFoodOrder==2){//如果选择了resto外卖
                        this.m.openOutFoodOrderType = this.m.openOutFoodOrder;
                    }

                    if(this.m.openThirdFoodOrder ==1||this.m.openThirdFoodOrder==2){//如果选择了第三方外卖
                        this.m.openThirdFoodOrderType = this.m.openThirdFoodOrder;
                    }

                    //判断
					this.showModal('show')
                },
                save:function(e){
                    var that = this;
                    var formDom = e.target;
                    C.ajaxFormEx(formDom,function(){
                        that.cancel();
                        tb.ajax.reload();
                    });

                },
				cancel:function () {
					this.hiddeModal();
                },

                choseAllOrder : function () {
                    console.log("选择所有订单")
                    var that = this;
                    if(!that.m.openAllOrder){
                        that.m.openAllOrder = 1;
                        that.m.openAllOrderType = 1;
                        that.m.allOrderValue = 3;
                        that.m.openBackCustomerOrder = 0 ;
                        that.m.openBackCustomerOrderType = null ;
                        that.m.backCustomerOrderValue = null ;
                    }
                },
                choseBackCustomerOrder : function () {
                    var that = this;
                    if(!that.m.openBackCustomerOrder){
                        that.m.openBackCustomerOrder = 1 ;
                        that.m.openBackCustomerOrderType = 1 ;
                        that.m.backCustomerOrderValue = 3 ;
                        that.m.openAllOrder = 0;
                        that.m.openAllOrderType = null;
                        that.m.allOrderValue = null;
                    }
                },

				showModal:function () {//模态框显示
					$('#contractModal').modal('show');
                    this.initDateTimepicker();
                },
				hiddeModal:function () {//模态框隐藏
					this.m={};
                    $('#contractModal').modal('hide')
                },
                uploadSuccess:function (data) {
                    C.simpleMsg("上传成功");
                    console.log(data);
                    this.m.picUrl = data;
                },
                uploadError: function (msg) {
                    C.errorMsg(msg);
                },
				initTime:function () {//初始化页面的时间
					this.m.signTime= this.formatDate(this.m.signTime);
                    this.m.expirationTime= this.formatDate(this.m.expirationTime);
                    this.m.onlineTime= this.formatDate(this.m.onlineTime);
                },
				formatDate:function (td) {
					return new Date(td).format("yyyy-MM-dd hh:mm:ss")
                },
				childExendParent:function (parent,child) {
                    var child = child || {};
                    for(var prop in parent) {
                        child[prop] = parent[prop];
                    }
                    return child;
                },
                deletePicUrl:function () {
					this.m.picUrl = ''
                },

                initChargeMode:function () { //选择计费方式为按效果时候的默认初始值
					 var child ={
						openNewCustomerRegister : 1,                  //  是否开启      新用户注册
						newCustomerValue : 1,                               //  每个新用户单价
						openSendSms : 1,                                       //  是否开启      短信发送
						sendSmsValue : 0.1,                                    //  每条短信的单价
						openAllOrder : 1,                                        //  是否使用   【所有订单】  计费模式            (和  【回头用户消费订单】 模式  二选一)
						openAllOrderType : 1,                                //  所有订单      计费模式    的计费类型  （1：订单总额，2：实付金额）
						allOrderValue : 3,                                        //  所有订单      下计费模式 的抽成比例
						openBackCustomerOrder : 0,                     //  是否使用  【回头用户消费订单】  计费模式    (和  【所有订单】 模式  二选一）
						openBackCustomerOrderType : 0,             //  回头用户消费订单     计费模式    的计费类型  （1：订单总额，2：实付金额）
						backCustomerOrderValue : 0,                    //  回头用户消费订单     下计费模式 的抽成比例
						openOutFoodOrder : 1,                             //   是否开启     Resto+外卖订单
						openOutFoodOrderType : 1,                     //   Resto+外卖订单      计费模式的计费类型  （1：订单总额，2：实付金额）
						outFoodOrderValue : 3,                             //   Resto+外卖订单     下计费模式 的抽成比例
						openThirdFoodOrder : 1,                           //   是否开启     第三方外卖订单
						openThirdFoodOrderType : 1,                   //   第三方外卖订单     计费模式    的计费类型  （1：订单总额，2：实付金额）
						thirdFoodOrderValue : 3
					};
					var parent = this.m;
					child = this.childExendParent(parent,child);
					this.m = child;
					console.log(this.m)
                }
			}
		});
		C.vue=vueObj;
	}());
	
	

	
</script>
