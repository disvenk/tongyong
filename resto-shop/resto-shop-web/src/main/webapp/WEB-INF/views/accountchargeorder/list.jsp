<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki"> 表单</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form role="form" action="{{m.id?'accountchargeorder/modify':'accountchargeorder/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
                            <label>brandId</label>
                            <input type="text" class="form-control" name="brandId" v-model="m.brandId">
                        </div>
                        <div class="form-group">
                            <label>orderStatus</label>
                            <input type="text" class="form-control" name="orderStatus" v-model="m.orderStatus">
                        </div>
                        <div class="form-group">
                            <label>pushOrderTime</label>
                            <input type="text" class="form-control" name="pushOrderTime" v-model="m.pushOrderTime">
                        </div>
                        <div class="form-group">
                            <label>chargeMoney</label>
                            <input type="text" class="form-control" name="chargeMoney" v-model="m.chargeMoney">
                        </div>
                        <div class="form-group">
                            <label>tradeNo</label>
                            <input type="text" class="form-control" name="tradeNo" v-model="m.tradeNo">
                        </div>
                        <div class="form-group">
                            <label>payType</label>
                            <input type="text" class="form-control" name="payType" v-model="m.payType">
                        </div>
                        <div class="form-group">
                            <label>remark</label>
                            <input type="text" class="form-control" name="remark" v-model="m.remark">
                        </div>
                        <div class="form-group">
                            <label>status</label>
                            <input type="text" class="form-control" name="status" v-model="m.status">
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

	<div class="col-md-8 col-md-offset-2">

        <form role="form" class="form-horizontal"
              action="accountchargeorder/charge" method="post" target="_blank" @submit="showChargeModal('createChargeOrder')">
            <div class="form-body">

                <div class="form-group">
                    <label class="col-md-3 control-label">充值金额：</label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="number" class="form-control"
                                   placeholder="请输入要充值的金额"  name="chargeMoney"  v-model="chargeMoney">
                            <div class="input-group-addon"><span class="glyphicon glyphicon-yen"></span></div>
                        </div>
                    </div>
                    <div class="col-md-5 text-center">
                        <a class="btn btn-info" @click="changeMoney(500)">500</a>&nbsp;
                        <a class="btn btn-info" @click="changeMoney(1000)">1000</a>&nbsp;
                        <a class="btn btn-info" @click="changeMoney(2000)">2000</a>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-3 control-label">支付方式：</label>
                    <div class="col-md-8">
                        <div class="md-radio-list">
                            <div class="md-radio">
                                <input type="radio" id="alipay" name="payType"
                                       checked="checked" class="md-radiobtn" value="1"> <label
                                    for="alipay"> <span></span> <span class="check"></span>
                                <span class="box"></span>&nbsp;<img alt="支付宝支付"
                                                                    src="assets/pages/img/alipay.png" width="23px" height="23px">&nbsp;支付宝支付
                            </label>
                            </div>
                            <div class="md-radio">
                                <input type="radio" id="wxpay" name="payType"
                                       class="md-radiobtn" value="2"> <label for="wxpay">
                                <span></span> <span class="check"></span> <span class="box"></span>&nbsp;<img
                                    alt="微信支付" src="assets/pages/img/wxpay.png" width="23px"
                                    height="23px">&nbsp;微信支付
                            </label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="text-center">
                <a class="btn default" data-dismiss="modal">取消</a> <input
                    class="btn green" type="submit" value="充值"/>
            </div>
        </form>


    </div>


    <div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="accountchargeorder/add">
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
	(function(){
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "accountchargeorder/list_all",
				dataSrc : ""

			},
            "order": [[ 0, "desc" ]],
			columns : [
				{
                    title : "创建时间",
                    data : "createTime",
                    createdCell:function (td,tdData) {
                        $(td).html(vueObj.formatDate(tdData));
                    }
                },

                {
                    title : "流水号",
                    data : "id",
                },

                {
                    title : "充值金额",
                    data : "chargeMoney"
                },
                {
                    title : "支付方式",
                    data : "payType",
                    createdCell:function (td,tdData) {
                        $(td).html(vueObj.getPayType(tdData))
                    }
                },
                {
                    title : "交易号",
                    data : "tradeNo"
                },

				{
					title : "进度",
					data : "orderStatus",
                    createdCell:function (td,tdData) {
					    var temp = "";
					    if(tdData==1){
					        temp = "充值成功";
                        }else if(temp==2){
					        temp = "审核中"
                        }
                        $(td).html(temp);
                    }
				}]
		});
		
		var C = new Controller(null,tb);
		var vueObj = new Vue({
			el:"#control",
			data:{
				chargeMoney:100,
				payType:''
			},
			mixins:[C.formVueMix],
			methods:{
                showChargeModal:function (createChargeOrder) {

                },
                //格式化时间
                formatDate:function (date) {
                    var temp = "";
                    if (date != null && date != "") {
                        temp = new Date(date);
                        temp = temp.format("yyyy-MM-dd hh:mm:ss");
                    }
                    return temp;
                },
                getPayType : function(type){
                    var str = ""
                    if(type==1){
                        str = "<img alt=\"支付宝支付\" src=\"assets/pages/img/alipay.png\" width=\"23px\" height=\"23px\">&nbsp;支 付 宝";
                    }else if(type==2){
                        str = "<img alt=\"微信支付\" src=\"assets/pages/img/wxpay.png\" width=\"23px\" height=\"23px\">&nbsp;微&nbsp;&nbsp;信";
                    }else if(type==3){
                        str = "<img alt=\"银行转账\" src=\"assets/pages/img/bank.png\" width=\"23px\" height=\"18px\">&nbsp;银行转账";
                    }else{
                        str = type;
                    }
                    return str;
                },
                changeMoney:function (money) {
                    console.log("....")
					this.chargeMoney = money;
                }
			}
		});
		C.vue=vueObj;
	}());
	
	

	
</script>
