<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<style>
	dt,dd{
		line-height: 35px;
	}
</style>

<div id="control">
	<!-- 申请发票	begin -->
	<div class="modal fade" id="applyInvoice" tabindex="-1" role="dialog"
		 aria-labelledby="myModlLabel" @click="removeValidated">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title text-center">
						<strong>申请发票</strong>
					</h4>
				</div>
				<div class="modal-body">
					<div>
						<!-- 选项卡 -->
						<ul class="nav nav-tabs" role="tablist">
							<li role="presentation" class="active"><a href="#general"
																	  aria-controls="general" role="tab" data-toggle="tab">普通发票</a></li>
							<li role="presentation"><a href="#increment"
													   aria-controls="increment" role="tab" data-toggle="tab">增值发票</a></li>
						</ul>
						<!-- 选项卡内容 -->
						<div class="tab-content">
							<div role="tabpanel" class="tab-pane active" id="general">
								<form class="form-horizontal" action="accountticket/create"  @submit.prevent="accountticketSave">
									<div class="form-group">
										<label  class="col-sm-3 control-label">发票抬头：</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" required name="header" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="accountTicket.header">
										</div>
									</div>
									<div class="form-group">
										<label  class="col-sm-3 control-label">发票内容：</label>
										<div class="col-sm-8">
											<div class="md-radio-inline">
												<div class="md-radio">
													<input type="radio" id="type_1" name="content"
														   checked="checked" class="md-radiobtn" value="明细">
													<label for="type_1">
													<span> </span> <span class="check"></span>
													<span class="box"></span> 明细
												</label>
												</div>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label  class="col-sm-3 control-label">发票金额：</label>
										<div class="col-sm-8">
											<input class="bs-select form-control" type="text" required name="money"  :value="invoiceMoney"/>
										</div>
									</div>
									<div class="form-group">
										<label  class="col-sm-3 control-label">收件地址：</label>
										<div class="col-sm-8">
											<select class="bs-select form-control" name="consigneceId" required v-model="consigneceId">
												<option v-for="item in addressInfo" value="{{item.id}}">{{item.address}}</option>
											</select>
											<input type="hidden" type="text" name="address" v-model="currentAddress.address"/>
											<span class="help-block text-danger" v-if="!currentAddress.id">您当前暂未添加任何地址，请点击右边的【管理】按钮，进行添加操作！</span>
										</div>
										<button type="button" class="col-sm-1 btn btn-sm green-meadow"
												data-toggle="modal" data-target="#addressInfoModal">
											管理</button>
									</div>
									<div class="form-group">
										<label  class="col-sm-3 control-label">收 件
											人：</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" required
												   name="name" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="currentAddress.name">
										</div>
									</div>
									<div class="form-group" :class="{'has-error':validated.invoice_create}">
										<label  class="col-sm-3 control-label">联系电话：</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" required
												   name="phone" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="currentAddress.phone">
											<span class="help-block text-danger" v-if="validated.invoice_create">请输入有效联系电话！</span>
										</div>
									</div>
									<div class="form-group">
										<label  class="col-sm-3 control-label">备&nbsp;&nbsp;注：</label>
										<div class="col-sm-8">
											<textarea class="form-control" name="remark" placeholder="写下你的备注，可不填！"></textarea>
										</div>
									</div>
									<input type="hidden" name="type" value="1" />
									<div class="text-center">
										<a class="btn default" data-dismiss="modal">取消</a> <input
											class="btn green" type="submit" value="申请"/>
									</div>
								</form>
							</div>
							<div role="tabpanel" class="tab-pane" id="increment">
								<form class="form-horizontal" action="accountticket/create"  @submit.prevent="accountticketSave">
									<div id="increment_info">
										<div class="form-group">
											<label  class="col-sm-3 control-label">单位名称：</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" required
													   name="header" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="invoice.header">
											</div>
											<a class="col-sm-1 btn btn-sm btn-danger" @click="cleanLastInvoice" v-if="invoice.id">清空</a>
										</div>
										<div class="form-group">
											<label  class="col-sm-3 control-label">纳税人识别码：</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" required
													   name="taxpayerCode" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="invoice.taxpayerCode">
											</div>
										</div>
										<div class="form-group">
											<label  class="col-sm-3 control-label">注册地址：</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" required
													   name="registeredAddress" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="invoice.registeredAddress">
											</div>
										</div>
										<div class="form-group">
											<label  class="col-sm-3 control-label">注册电话：</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" required
													   name="registeredPhone" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="invoice.registeredPhone">
											</div>
										</div>
										<div class="form-group">
											<label  class="col-sm-3 control-label">开户银行：</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" required
													   name="bankName" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="invoice.bankName">
											</div>
										</div>
										<div class="form-group">
											<label  class="col-sm-3 control-label">银行账户：</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" required
													   name="bankAccount" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="invoice.bankAccount">
											</div>
										</div>
										<div class="form-group">
											<label  class="col-sm-3 control-label">发票金额：</label>
											<div class="col-sm-8">
												<input class="bs-select form-control" type="text" required name="money"  :value="invoiceMoney"/>
											</div>
										</div>
										<div class="form-group">
											<label  class="col-sm-3 control-label">发票内容：</label>
											<div class="col-sm-8">
												<div class="md-radio-inline">
													<div class="md-radio">
														<input type="radio" id="type_1" name="content"
															   checked="checked" class="md-radiobtn" value="明细"> <label
															for="type_1"> <span> </span> <span class="check"></span>
														<span class="box"></span> 明细
													</label>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label  class="col-sm-3 control-label">收件地址：</label>
											<div class="col-sm-8">
												<select class="bs-select form-control" name="consigneceId" required v-model="consigneceId">
													<option v-for="item in addressInfo" value="{{item.id}}">{{item.address}}</option>
												</select>
												<input type="hidden" type="text" name="address" v-model="currentAddress.address"/>
												<span class="help-block text-danger" v-if="!currentAddress.id">您当前暂未添加任何地址，请点击右边的【管理】按钮，进行添加操作！</span>
											</div>
											<button type="button"
													class="col-sm-1 btn btn-sm green-meadow" data-toggle="modal"
													data-target="#addressInfoModal">管理</button>
										</div>
										<div class="form-group">
											<label  class="col-sm-3 control-label">收
												件 人：</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" required
													   name="name" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="currentAddress.name">
											</div>
										</div>
										<div class="form-group" :class="{'has-error':validated.invoice_create}">
											<label  class="col-sm-3 control-label">联系电话：</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" required
													   name="phone" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="currentAddress.phone">
												<span class="help-block text-danger" v-if="validated.invoice_create">请输入有效联系电话！</span>
											</div>
										</div>
										<div class="form-group">
											<label  class="col-sm-3 control-label">备&nbsp;&nbsp;注：</label>
											<div class="col-sm-8">
												<textarea class="form-control" name="remark" placeholder="写下你的备注，可不填！"></textarea>
											</div>
										</div>
										<input type="hidden" name="type" value="2" />
										<div class="text-center">
											<a class="btn default" data-dismiss="modal">取消</a> <input
												class="btn green" type="submit" value="申请"/>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 申请发票	end -->

	<!-- 地址管理 	begin-->
	<div class="modal fade" id="addressInfoModal" tabindex="-1"
		 role="dialog" aria-labelledby="addressInfoModal" @click="removeValidated">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title text-center">
						<strong>地址管理</strong>
					</h4>
				</div>
				<div class="modal-body">
					<div>
						<ul class="nav nav-tabs" role="tablist">
							<li role="presentation" :class="{active:currentAddress.id}" v-if="currentAddress.id"><a href="#address_update" aria-controls="address_update" role="tab" data-toggle="tab">修改地址</a></li>
							<li role="presentation" :class="{active:!currentAddress.id}"><a href="#address_add" aria-controls="profile" role="tab" data-toggle="tab">新增地址</a></li>
						</ul>
						<div class="tab-content">
							<div role="tabpanel" class="tab-pane" id="address_update" :class="{active:currentAddress.id}" v-if="currentAddress.id">
								<form class="form-horizontal" action="addressinfo/modify" @submit.prevent="addressSave">
									<div class="form-group">
										<label  class="col-sm-3 control-label">收件地址：</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" required name="address" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="currentAddress.address">
										</div>
									</div>
									<div class="form-group">
										<label  class="col-sm-3 control-label">收 件 人：</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" required name="name" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="currentAddress.name">
										</div>
									</div>
									<div class="form-group" :class="{'has-error':validated.addressinfo_modify}">
										<label  class="col-sm-3 control-label">联系电话：</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" required name="phone" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" v-model="currentAddress.phone">
											<span class="help-block text-danger" v-if="validated.addressinfo_modify">请输入有效联系电话！</span>
										</div>
									</div>
									<div class="row">
										<div class="col-sm-3 text-center"><a class="btn btn-danger" @click="accountAddressDelete">删除</a></div>
										<div class="col-sm-6 text-center">
											<a class="btn default" data-dismiss="modal">取消</a> <input
												class="btn green" type="submit" value="修改"/>
										</div>
									</div>
									<input type="hidden" name="brandId" v-model="currentAddress.brandId"/>
									<input type="hidden" name="id" v-model="currentAddress.id"/>
								</form>
							</div>
							<div role="tabpanel" class="tab-pane" :class="{active:!currentAddress.id}" id="address_add">
								<form class="form-horizontal" action="accountAddressInfo/create" @submit.prevent="accountAddressSave">
									<div class="form-group">
										<label  class="col-sm-3 control-label">收件地址：</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" required name="address" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 control-label">收 件 人：</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" required name="name" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
										</div>
									</div>
									<div class="form-group" :class="{'has-error':validated.addressinfo_create}">
										<label  class="col-sm-3 control-label">联系电话：</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" required name="phone" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
											<span class="help-block text-danger" v-if="validated.addressinfo_create">请输入有效联系电话！</span>
										</div>
									</div>
									<div class="text-center">
										<a class="btn default" data-dismiss="modal">取消</a> <input
											class="btn green" type="submit" value="添加" />
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 地址管理	end-->


	<!-- 发票详情   begin -->
	<div class="modal fade" id="detailInvoiceModal" tabindex="-1" role="dialog"
		 aria-labelledby="consigneeModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
					<h4 class="modal-title text-center">
						<strong>发票详情</strong>
					</h4>
				</div>
				<div class="modal-body">
					<dl class="dl-horizontal">
						<template v-if="brandAccountticketInfo.type==1">
							<dt>发票类型：</dt>
							<dd><span class='label' style='background-color: #3598dc'>普通发票</span></dd>
							<dt>发票抬头：</dt>
							<dd>{{brandAccountticketInfo.header}}</dd>
						</template>
						<template v-else>
							<dt>发票类型：</dt>
							<dd><span class='label' style='background-color: #26c281'>增值税发票</span></dd>
							<dt>单位名称：</dt>
							<dd>{{brandAccountticketInfo.header}}</dd>
							<dt>纳税人识别码：</dt>
							<dd>{{brandAccountticketInfo.taxpayerCode}}</dd>
							<dt>注册地址：</dt>
							<dd>{{brandAccountticketInfo.registeredAddress}}</dd>
							<dt>注册电话：</dt>
							<dd>{{brandAccountticketInfo.registeredPhone}}</dd>
							<dt>开户银行：</dt>
							<dd>{{brandAccountticketInfo.bankName}}</dd>
							<dt>银行账户：</dt>
							<dd>{{brandAccountticketInfo.bankAccount}}</dd>
						</template>
						<dt>发票金额：</dt>
						<dd>{{brandAccountticketInfo.money}}&nbsp;元</dd>
						<dt>申请时间：</dt>
						<dd>{{brandAccountticketInfo.createTime}}</dd>
						<dt>完成时间：</dt>
						<dd>{{brandAccountticketInfo.pushTime?brandAccountticketInfo.pushTime:'审核中'}}</dd>
						<dt>订单备注：</dt>
						<dd>{{brandAccountticketInfo.remark ? brandAccountticketInfo.remark : '无'}}</dd>
					</dl>
					<hr/>
					<h4 class="text-center"><strong>收件人信息</strong></h4>
					<dl class="dl-horizontal">
						<dt>收件人姓名：</dt>
						<dd>{{brandAccountticketInfo.name}}</dd>
						<dt>收件人电话：</dt>
						<dd>{{brandAccountticketInfo.phone}}</dd>
						<dt>收件人地址：</dt>
						<dd>{{brandAccountticketInfo.address}}</dd>
					</dl>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 发票详情   end -->


	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="notice/add">
				<button type="button" class="btn green-meadow" disabled> 当前可开发票金额&nbsp;<strong>{{invoiceMoney}}</strong>&nbsp;元 </button>&nbsp;&nbsp;&nbsp;
				<button type="button" class="btn green " data-toggle="modal"
						data-target="#applyInvoice">申请发票</button>
			</s:hasPermission>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter">&nbsp;</div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered " ></table>
		</div>
	</div>


</div>
<script>
    $(document).ready( function () {
		//载入表格数据

		tb = $('.table-body>table').DataTable({
			//禁用dataTable默认排序
			ordering:false,
			ajax :{
			    url:"accountticket/list_all",
				dataSrc:"data"
			},
            columns:[
				{
				 title:"发票抬头",
				 data:"header"
				},
                {
                    title : "内容",
                    data : "content",
                },
                {
                    title : "金额(人民币)",
                    data : "money",
                },
                {
                    title : "快递单号",
                    data : "expersage",
                    createdCell : function(td, tdData) {
                        $(td).html(tdData != null ? "<a href='http://m.kuaidi100.com/result.jsp?com=&nu="+ tdData+ "' target='_blank'>"+ tdData+ "</a>": "未完成");
                    }

                },
                {
                    title : "发票类型",
                    data : "type",
                    createdCell : function(td, tdData) {
                        $(td).html(tdData == 1 ? "<span class='label' style='background-color: #3598dc'>普通发票</span>": "<span class='label' style='background-color: #26c281'>增值税发票</span>");
                    }

                },
                {
                    title : "状态",
                    data : "ticketStatus",
                    createdCell : function(td, tdData) {
                        $(td).html(tdData == 0 ? "<span class='label label-default'>申请中</span>": "<span class='label label-success'>已完成</label>");
                    }

                },
                {
                    title: "操作",
                    data: "id",
                    createdCell : function(td, tdData,rowData) {
                        var btn = createBtn("查看详情",function() {
                            vueObj.showDetailInfo(rowData);
                        })
                        $(td).html(btn);
                        if(rowData.type==2 && vueObj.invoice.id== null){
                            vueObj.invoice=rowData;
                        }
                    }
                }
			]

		})

        var C = new Controller("#control", tb);

		var vueObj = new Vue({
			el:"#control",
			data:{
                brandAccountticketInfo : {},//保存查看详情的信息
                invoice:{},//保存上次开发票的信息
                addressInfo:[],//保存当前品牌的所有地址
                currentAddress:{},//当前选中的地址信息
                consigneceId:"",//当前选中的地址的id
                invoiceMoney:0,//当前品牌可开发票的最大金额
                validated:{}//手机号码验证

			},
			methods:{
                accountAddressSave:function(e){//保存地址
                    var that = this;
                    var formDom = e.target;
                    if(this.checkPhone(formDom)){
                        C.ajaxFormEx(formDom,function(){
                            $("#addressInfoModal").modal("hide");
                            that.queryAccountAddress();
                            if($(formDom).attr('action').indexOf("create")>=0){
                                $(formDom)[0].reset();
                            }
                        });
                    }
                },
                accountticketSave : function(e){//申请发票
                    var that = this;
                    var formDom = e.target;
                    console.table(formDom);
                    if(this.checkPhone(formDom)){
                        C.ajaxFormEx(formDom,function(){
                            $("#applyInvoice").modal("hide");
                            that.queryInvoiceMoney();
                            tb.ajax.reload();
                        });
                    }
                },
                queryInvoiceMoney : function(){//查询可申请金额
                    var that=this;
                    $.post("accountticket/selectInvoiceMoney",function(result){
                        that.invoiceMoney = result.data.remainedInvoiceAmount;
                    })
                },
                queryAccountAddress : function(){//查询地址信息
                    var that=this;
                    $.post("accountAddressInfo/list_all",function(result){
                        console.log(result)
                        that.addressInfo = result.data;
                        that.consigneceId = result.data!=""?result.data[0].id:"";
                    })
                },

                showDetailInfo : function(rowData) {//显示详情
                    rowData.createTime = this.formatDate(rowData.createTime);
                    rowData.pushTime = this.formatDate(rowData.pushTime);
                    this.brandAccountticketInfo = rowData;
                    $("#detailInvoiceModal").modal();
                },
                formatDate : function(date){//格式化时间
                    var temp = "";
                    if (date != null && date != "") {
                        temp = new Date(date);
                        temp = temp.format("yyyy-MM-dd hh:mm:ss");
                    }
                    return temp;
                },

                checkPhone : function(dom){
                    var flag = true;
                    var phone = dom.phone.value;
                    var teleReg = /^((0\d{2,3})-)(\d{7,8})$/;
                    var mobileReg =/^1[3578]\d{9}$/;
                    if (!teleReg.test(phone) && !mobileReg.test(phone)){//验证手机号码
                        var src = $(dom).attr('action');
                        var m = {};
                        if(src == "addressinfo/create"){
                            m.addressinfo_create = true;
                        }else if(src == "invoice/create"){
                            m.invoice_create = true;
                        }else if(src == "addressinfo/modify"){
                            m.addressinfo_modify = true;
                        }
                        this.validated = m;
                        flag = false;
                    }
                    return flag;
                },
                accountAddressDelete:function () {
					//console.log("111111111111111111111111111")
                    var that = this;
                    $.post("accountAddressInfo/delete",{"id":this.consigneceId},function(result){
                        if(result){
                            $("#addressInfoModal").modal("hide");
                            that.queryAccountAddress();
                            C.simpleMsg("删除成功！");
                        }else{
                            C.errorMsg("删除失败！");
                        }
                    })
                }

			},
            created : function(){//创建时初始化信息
                this.queryInvoiceMoney();
                this.queryAccountAddress();
            },
            watch: {
                consigneceId: function(val) {//自动切换对应地址信息
                    var that = this;
                    if(this.addressInfo!=null&&this.addressInfo.length>0){
                        $(this.addressInfo).each(function(i,item){
                            if(item.id==val){
                                that.currentAddress = item;
                            }
                        })
                    }else{
                        that.currentAddress = {};
                    }
                }
            }

		});

        //创建一个按钮
        function createBtn(btnValue, btnfunction) {
            return $('<input />', {
                value : btnValue,
                type : "button",
                class : "btn btn-sm btn-info",
                click : btnfunction
            })
        }

    } );


</script>