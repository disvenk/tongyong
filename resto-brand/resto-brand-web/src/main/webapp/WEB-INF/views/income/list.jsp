<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>

<style>
	.btn-def{
		width: 80px;
		height: 30px;
		background-color: #FFFFFF;
		border-radius: 3px;
		border: 1px dashed  #6A7989;
		cursor: pointer;
	}
	.btn-suc{
		width: 80px;
		height: 30px;
		background-color: #0c91e5;
		border-radius: 3px;
		border: 1px dashed  #6A7989;
		cursor: pointer;
	}

</style>

<div id="control">
	<!-- 添加 Brand 信息  Modal  start -->
	<div class="modal fade" id="incomeModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-body">
					<form role="form" class="form-horizontal" id="incomeForm" action="{{income.id?'income/modify':'income/create'}}"  @submit.prevent="save">
						<h4 class="text-center"><strong>添加打款</strong></h4>
						<div class="form-group">
							<label class="col-sm-3 control-label">品牌名称：</label>
							<div class="col-sm-8">
								<select class="form-control" v-model="brand">
									<option v-for="brand in brands">
										{{brand.brandName}}
									</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">公司名称：</label>
							<div class="col-sm-8">
								<%--<input type="text" class="form-control" required  v-model="bCompanyName">--%>
								<span>{{bCompanyName}}</span>
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-3 control-label">合同编号：</label>
							<div class="col-md-9">
									<template v-for="constractNum in constractNumArr">
										<input name="constractNum"  class="btn-def" type="button" id={{constractNum}}  v-model="constractNum" @click="choiceConstracNum(constractNum)">
									</template>
							</div>

							<input type="hidden" name="constractNum" v-model="constractNum">
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">打款金额：</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" required name="payMoney" v-model="income.payMoney">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">打款方式：</label>
							<div class="col-sm-8">
								<%--<input type="text" class="form-control" required name="payType" v-model="income.payType">--%>
									<%--//1.银行 2.现金 3微信 4支付宝--%>
									<div>
									<label>
										<input type="radio" name="payType"  required v-model="income.payType" value="1">
										银行
									</label>
									<label>
										<input type="radio" name="payType"  required v-model="income.payType" value="2">
										现金
									</label>
									<label>
										<input type="radio" name="payType"  required v-model="income.payType" value="3">
										微信
									</label>
									<label>
										<input type="radio" name="payType"  required v-model="income.payType" value="4">
										支付宝
									</label>
								</div>

							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">打款账号：</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" required name="payAccount" v-model="income.payAccount">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">交易流水号：</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" required name="paySerialnumber" v-model="income.paySerialnumber">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">打款时间：</label>
							<div class="col-sm-8">
								<input type="text" class="form-control form_datetime" required name="payTime" v-model="income.payTime" readonly>
							</div>
						</div>

						<div class="text-center">
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
							<input type="hidden" name="contractId" v-model="contract.id">
							<input class="btn green" type="submit" value="保存"/>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>


	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="income/add">
			<button class="btn green pull-right" @click="create">新增进款</button>
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
				url : "income/list_all",
				dataSrc : ""
			},
			columns : [
					{
					title : "合同编号",
					data : "contract.constractNum",
					},
					{
						title : "公司名称",
						data : "contract.bCompanyName",
					},
					{
						title : "品牌名称",
						data : "contract.brandName",
					},

					{
					title : "支付方式",
					data : "payType",
					createdCell:function(td,tdData){
                        //打款方式 1.银行 2.现金 3微信 4支付宝
                        var temp ='';
                        switch(tdData)
                        {
                            case 1:
                            temp = '银行';
                            break;
                            case 2:
                                temp = '现金';
                                break;
                            case 3:
                                temp = '微信';
                                break;
                            case 4:
                                temp = '支付宝';
                                break;
                            default:
                               break;

                        }
                        $(td).html(temp);

                    }
					},
					{
					title : "支付金额",
					data : "payMoney",
					},
					{
					title : "支付流水号",
					data : "paySerialnumber",
					},
					{
					title : "支付时间",
					data : "payTime",
					createdCell:function (td,tdData) {
						$(td).html(vueObj.formatDate(tdData))
                    }
					},

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="income/delete">
							C.createDelBtn(tdData,"income/delete"),
							</s:hasPermission>
							<s:hasPermission name="income/modify">
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
            mixins:[C.formVueMix],
			data:{
			    brands:[],//所有的品牌列表
				brand:'',//当前选择的品牌
				bCompanyName:'',//公司名称
				constractNums:'',//多个合同编号字符串
                constractNum:'',//当前最终选择的那个合同
				income:{
                    contractId:'',
					payMoney:'',//打款金额
					payType:'',//打款方式
					payAccount:'',//打款账号
                    paySerialnumber:'',//流水号
					payTime:'',//交易时间
					payMoney:''
				},
			},
            watch: {
                'brand': function (val, oldVal) {
                    var that = this;
                 	for(var i=0;i<that.brands.length;i++){
                 	    if(val==that.brands[i].brandName){
                 	        that.bCompanyName = that.brands[i].bCompanyName;
							that.constractNums = that.brands[i].contractNums;
                 	        break;
						}
					}
					console.log(that.constractNums);
                },

            },
            computed:{
                constractNumArr:function(){//合同编号数组
                    return this.constractNums ? this.constractNums.split(',') : [];
                }

            },

			methods:{
			    create:function () {
			        var that = this;
					that.initBrand();
					that.showModal();
                },
				initBrand:function () {
			        var that = this;
					$.ajax({
						url:"contract/list_brands",
						type:'post',
						success:function (data) {
						    if(data.length==0){
						        toastr.error("还没有创建合同,请先创建合同")
							}else{
                               that.brands = data;
							}
                        },
                        error:function () {
							toastr.error("加载品牌列表出错")
                        }

					})
                },
				showModal:function () {//显示模态框
					$("#incomeModal").modal('show');
					this.initDateTimepicker();//初始化时间插件
                },
				hiddeModal:function () {
			        this.m={},
                    $("#incomeModal").modal("hide");
                },

                choiceConstracNum:function (constractNum) {
			        this.constractNum = constractNum;
					var id = "#"+constractNum;
					console.log(id);
                    $(":input[name='constractNum']").removeClass();
					$(id).addClass("btn-suc")
                },
                save:function(e){
                    var that = this;
                    var formDom = e.target;
                    C.ajaxFormEx(formDom,function(){
                        that.hiddeModal();
                        tb.ajax.reload();
                    });
                },
				formatDate:function (tdData) {
					return new Date(tdData).format("yyyy-MM-dd hh:mm:ss")
                },
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
                    })
                }
			}
		});
		C.vue=vueObj;
	}());
	
	

	
</script>
