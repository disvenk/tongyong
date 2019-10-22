<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="v-bind" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="v-on" uri="http://www.springframework.org/tags/form" %>
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


<div id="control" >
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
						<h3 style="text-align: center"> 审核申请</h3>
	            </div>
	            <div class="portlet-body">
	            	<form role="form" action="{{m.id?'examine/modify':'examine/create'}}" @submit.prevent="save">
						<div class="form-body">

					<div class="form-group">
						<label>审核类型</label>
						<%--<input type="text" class="form-control" name="type" v-model="m.type">--%>
						<input type="radio" name="type" v-model="type" value=1>品牌上线
						<input type="radio" name="type" v-model="type" value=2 @click="choiceTypeTicket()">开具发票
						<input type="radio" name="type" v-model="type" value=3>退款

					</div>

					<div class="form-group" v-if="type==2">
						<label>发票金额</label>
						<input type="text" class="form-control" name="money" v-model="m.money">
					</div>

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
						<label class="col-md-3 control-label">合同编号：</label>
						<div class="col-md-9">
							<template v-for="constractNum in constractNumArr">
								<input name="constractNum"  class="btn-def" type="button" id={{constractNum}}  v-model="constractNum" @click="choiceConstracNum(constractNum)">
							</template>
						</div>

						<input type="hidden" name="constractNum" v-model="constractNum">
					</div>


					<div class="form-group">
						<label>申请部门</label>
						<input type="text" class="form-control" name="department" v-model="m.department">
					</div>
					<div class="form-group">
						<label>申请人</label>
						<input type="text" class="form-control" name="applicant" v-model="m.applicant">
					</div>
					<div class="form-group">
						<label>邮箱</label>
						<input type="text" class="form-control" name="email" v-model="m.email">
					</div>
					<div class="form-group">
						<label>备注</label>
						<input type="text" class="form-control" name="remark" v-model="m.remark">
					</div>
						</div>
						<input type="hidden" name="id" v-model="m.id" />
						<input class="btn green"  type="submit"  value="保存"/>
						<a class="btn default" @click="cancel" >取消</a>
					</form>
	            </div>
	        </div>
		</div>
	</div>

	<div class="modal fade" tabindex="-1" role="dialog" id="examineModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h3 class="modal-title" style="text-align: center">审核详情</h3>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-xs-3">
								<span>审核状态</span>
							</div>
							<div class="col-xs-4">
								<span>{{examine.statusName}}</span>
							</div>
						</div>
					<br/>
						<div class="row">
							<div class="col-xs-3">
								<span>审核类型</span>
							</div>
							<div class="col-xs-4">
								<span>{{examine.typeName}}</span>
							</div>
						</div>
						<br/>

						<div class="row" v-if="examine.type==2">
							<div class="col-xs-3">
								<span>发票金额</span>
							</div>
							<div class="col-xs-4">
								<span>{{examine.money}}</span>
							</div>
						</div>

						<div class="row">
							<div class="col-xs-3">
								<span>品牌</span>
							</div>
							<div class="col-xs-4">
								<span>好菜湘</span>
							</div>
						</div>
						<br/>

						<div class="row">
							<div class="col-xs-3">
								<span>申请部门</span>
							</div>
							<div class="col-xs-4">
								<span>{{examine.department}}</span>
							</div>
						</div>
						<br/>

						<div class="row">
							<div class="col-xs-3">
								<span>申请人</span>
							</div>
							<div class="col-xs-4">
								<span>{{examine.applicant}}</span>
							</div>
						</div>
						<br/>

						<div class="row">
							<div class="col-xs-3">
								<span>联系邮箱</span>
							</div>
							<div class="col-xs-4">
								<span>{{examine.email}}</span>
							</div>
						</div>
						<br/>

						<div class="row">
							<div class="col-xs-3">
								<span>申请备注</span>
							</div>
							<div class="col-xs-7">
								<span>{{examine.remark}}</span>
							</div>
						</div>
						<br/>

						<div class="row" v-if="examine.status!=0">
							<div class="col-xs-3">
								<span>回复</span>
							</div>
							<div class="col-xs-4">
								<span>{{examine.review}}</span>
							</div>
						</div>

						<input type="hidden" v-model="examine.contractId">

					</div>


				</div>
				<div class="modal-footer" style="text-align: center"  v-if="buttonType==1">
					<button type="button" class="btn btn-primary" v-if="examine.status==-1" @click="createAgin(examine.id)" >再次申请</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>

				<div class="modal-footer" style="text-align: center"  v-if="buttonType==2">
					<button type="button" class="btn btn-danger" @click="reject(examine.contractId,examine.email,examine.id)">驳回</button>
					<button type="button" class="btn btn-primary" @click="aprroval(examine.contractId,examine.email,examine.id)">批准</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->

	<!--审核人回复弹窗 -->
	<div class="modal fade" tabindex="-1" role="dialog" id="reviewModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h3 v-if="review.status==-1" class="modal-title" style="text-align: center">确认驳回</h3>
					<h3 v-if="review.status==1"  class="modal-title" style="text-align: center">确认批准</h3>
				</div>
				<div class="modal-body">
					<span>回复</span>
					<br/>
					<input type="hidden" name="contractId" v-model="">
					<textarea name="review" v-model="review.review" cols="60" rows="5"></textarea>
					<br/>
					<span>邮件通知</span>
					<input type="checkbox" id="mike" value="Mike" v-model="checkedNames" checked>
					<label for="mike">{{review.email}}</label>

				</div>

				<div class="modal-footer" style="text-align: center" >
					<button type="button" class="btn btn-default" @click="returnModal" >返回</button>
					<button type="button" class="btn btn-primary" @click="confirmReview">确认</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->




	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="examine/add">
			<button class="btn green pull-right" @click="create">申请审核</button>
			</s:hasPermission>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter">&nbsp;</div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered "></table>
		</div>
	</div>
</Div>


<script>

	(function(){
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "examine/list_all",
				dataSrc : ""
			},
			columns : [
                {
                    title : "合同编号",
                    data : "constractNum",
                },


			{
				title : "申请事项",
				data : "type",
				createdCell:function (td,tdData) {
					$(td).html(vueObj.typeName(tdData));
                }
			},
			{
				title : "品牌名称",
				data : "brandName",
			},
			{
				title : "申请时间",
				data : "createTime",
                createdCell:function(td,tdData){
                    $(td).html(new Date(tdData).format("yyyy-MM-dd hh:mm:ss"))
                }

			},
			{
				title : "部门",
				data : "department",
			},
			{
				title : "申请人",
				data : "applicant",
			},
			{
				title : "备注",
				data : "remark",
			},
			{
				title : "金额",
				data : "money",
				defaultContent:"---"


			},
			{
				title : "状态",
				data : "status",
				createdCell:function (td,tdData) {
					$(td).html(vueObj.statusName(tdData));
                }
			},
				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
					    var operator = [];
					    if(rowData.status!=0){
					        operator = [
                                <s:hasPermission name="examine/list_one">
                                vueObj.createShowButton(tdData),
                                </s:hasPermission>
							]
						}else {
					        operator = [
                                <s:hasPermission name="examine/modify">
                                vueObj.createModifyButton(tdData),
                                </s:hasPermission>
                                <s:hasPermission name="examine/list_one">
                                vueObj.createShowButton(tdData),
                                </s:hasPermission>
							]

						}
						$(td).html(operator);
					}
				}],
		});
		
		var C = new Controller(null,tb);
		var vueObj = new Vue({
			data:{
                brands:[],//所有的品牌列表
                brand:'',//当前选择的品牌
                constractNums:'',//多个合同编号字符串
                constractNum:'',//当前最终选择的那个合同
				type:0,
			    //brands:[],
				examine:{

				},
				review:{ //批准和驳回弹窗
					id:'',//
					status:'',//1.为显示批准 -1显示驳回
					email:'',//邮件
                    contractId:'',//合同id
					review:'',//回复内容
				},
				buttonType:'',//用来显示弹窗按钮的类型 1.再次申请 2.驳回和批准
				titleName:'',//回复弹窗的名称
			},


			el:"#control",
			mixins:[C.formVueMix],
            computed:{
                constractNumArr:function(){//合同编号数组
                    return this.constractNums ? this.constractNums.split(',') : [];
                }

            },
            watch: {
                'brand': function (val, oldVal) {
                    var that = this;
                    for(var i=0;i<that.brands.length;i++){
                        if(val==that.brands[i].brandName){
                            that.constractNums = that.brands[i].contractNums;
                            break;
                        }
                    }
                },

            },
			created:function () {
                var that = this;
                that.initBrand();
                that.showModal();
            },

			methods:{
                choiceTypeTicket:function () {
                    this.type =2;
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

                createShowButton:function (tdData) {//查看详情
					var that = this;
                    var button = $("<button class='btn btn-xs btn-primary'>查看详情</button>");
                    var id = tdData;
                    button.click(function(){
                     	$.ajax({
							url:'examine/list_one',
							data:{id:id},
							success:function (result) {
							    that.examine = result.data;
							    var temp = that.statusName(that.examine.status);
							    var typeName = that.typeName(that.examine.type);
								that.examine.statusName = temp;
								that.examine.typeName = typeName;
								that.buttonType = 1;
							    that.showModal("examineModal");
                            }

						})

                    });
                    return button;

                },

				//审核弹窗
                createModifyButton:function (tdData) {//审核
                    var that = this;
                    var button = $("<button class='btn btn-xs btn-primary'>审核</button>");
                    var id = tdData;
                    button.click(function(){
                        $.ajax({
                            url:'examine/list_one',
                            data:{id:id},
                            success:function (result) {
                                that.examine = result.data;
                                var temp = that.statusName(that.examine.status);
                                var typeName = that.typeName(that.examine.type);
                                that.examine.statusName = temp;
                                that.examine.typeName = typeName;
                                that.buttonType = 2;
                                that.constractId = result.data.constractId;
                                that.showModal("examineModal",result.data.contractId);
                            }

                        })

                    });
                    return button;
                },
                choiceConstracNum:function (constractNum) {
                    this.constractNum = constractNum;
                    var id = "#"+constractNum;
                    console.log(id);
                    $(":input[name='constractNum']").removeClass();
                    $(id).addClass("btn-suc")
                },

				showModal:function (id) {
					$('#'+id).modal();
                },
				closeModal:function (id) {
                    $('#'+id).modal('hide');
                },
				returnModal:function () {
                    this.closeModal("reviewModal");//关闭回复弹窗
                    this.showModal("examineModal");//打开审核弹窗
                },
				//驳回
				reject:function (contractId,email,id) {
                    this.review.contractId = contractId;
                    this.review.status = -1;
                    this.review.email = email;
                    this.review.id = id
                    console.log(this.review);
                    this.closeModal("examineModal");
					this.showModal("reviewModal")
                },
				//批准
                aprroval:function (contractId,email,id) {//批准
					console.log("批准。。")
                    this.review.contractId = contractId;
                    this.review.status = 1;
                    this.review.email = email;
                    this.review.id = id;
                    console.log(this.review);
                    this.closeModal("examineModal");
                    this.showModal("reviewModal")
                },

                statusName:function (tdData) {
                    var temp = '';
                    switch (tdData){
                        case 0 :
                            temp = '待审核';
                            break;
                        case 1:
                            temp = '已批准';
                            break;
                        case -1:
                            temp = '已驳回';
                            break;
                    }
                    return temp;
                },
				typeName:function (tdData) {
                    //上线 免金额开票 退款 续约
                    var temp = '';
                    switch(tdData)
                    {
                        case 1:
                            temp = '上线';
                            break;
                        case 2:
                            temp = '免金额开票';
                            break;
                        case 3:
                            temp = '退款';
                            break;
                        case 3:
                            temp = '续约';
                            break;
                        default:
                            break;
                    }
                    return temp;
                },
                confirmReview:function () {
                    var that = this;
					//确认 驳回或者批准
					$.ajax({
						url:'examine/modify',
						type:"post",
						data:that.review,
						success:function () {
                            that.closeModal("reviewModal");
                            tb.ajax.reload();
                        }

					})

                },
                createAgin:function (id) {
                    console.log("再次申请审核。。。")
					console.log(id);
                    var that = this;
                    $.ajax({
						url:'examine/modifyStatus',
						type:"post",
						data:{
                            id:id
						},
						success:function () {
							that.closeModal("examineModal");
							tb.ajax.reload();
                        }

					})

                },


			}
		});
		C.vue=vueObj;
	}());


	

	
</script>
