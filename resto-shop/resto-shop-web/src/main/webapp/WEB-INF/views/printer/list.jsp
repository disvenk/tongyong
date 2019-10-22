<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki">新建打印机</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form role="form" class="form-horizontal" action="{{m.id?'printer/modify':'printer/create'}}" @submit.prevent="save">
						<div class="form-body">
			           		<div class="form-group">
			           			<label class="col-sm-3 control-label">打印机名称：</label>
							    <div class="col-sm-8">
							    	<input type="text" class="form-control" required name="name" v-model="m.name">
							    </div>
							</div>
			           		<div class="form-group">
			           			<label class="col-sm-3 control-label">I&nbsp;P&nbsp;地址：</label>
							    <div class="col-sm-8">
							   		<input type="text" class="form-control" required name="ip" v-model="m.ip" onclick="$('#validateMsg').hide()">
							    </div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">I&nbsp;P&nbsp;备用地址：</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" name="spareIp" v-model="m.spareIp" onclick="$('#validateMsg').hide()">
								</div>
							</div>
			           		<div class="form-group">
			           			<label class="col-sm-3 control-label">端&nbsp;口&nbsp;号：</label>
							    <div class="col-sm-8">
<!-- 							    	<input type="number" class="form-control" required placeholder="请输入数字!" name="port" v-model="m.port"> -->
							    	<input type="text" class="form-control" required  name="port" v-model="m.port">
							    </div>
							</div>
							<div class="form-group">
								<div class="col-sm-3 control-label">打印机类型：</div>
								<div class="col-sm-9 radio-list">
								    <label class="radio-inline">
								    	<input type="radio"  name="printType" required v-model="m.printType" value="1">
								    	厨房</label> 
							    	<label class="radio-inline">
								    	<input type="radio"  name="printType" v-model="m.printType" value="2">
								    	前台</label>
							    	<%--<label class="radio-inline">--%>
								    	<%--<input type="radio"  name="printType" v-model="m.printType" value="3"> --%>
								    	<%--打包</label>--%>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-3 control-label">小票类型：</div>
								<div class="col-sm-9 radio-list">
									<label class="radio-inline">
										<input type="radio"  name="ticketType" required v-model="m.ticketType" value="1">
										贴纸</label>
									<label class="radio-inline">
										<input type="radio"  name="ticketType" v-model="m.ticketType" value="0">
										小票</label>
								</div>
							</div>
							<div class="form-group" v-if="m.printType == 2">
								<div class="col-sm-3 control-label">范围：</div>
								<div class="col-sm-9 radio-list">
									<label class="radio-inline">
										<input type="radio"  name="range" required v-model="m.range" value="1">
										区域打印机</label>
									<label class="radio-inline">
										<input type="radio"  name="range" v-model="m.range" value="0">
										前台打印机</label>
								</div>
							</div>

							<div class="form-group">
								<div class="col-sm-3 control-label">出单模式：</div>
								<div class="col-sm-9 radio-list">
									<label class="radio-inline">
										<input type="checkbox" name="supportTangshi" v-model="m.supportTangshi" value="1">
										堂食</label>
									<label class="radio-inline">
										<input type="checkbox" name="supportWaidai" v-model="m.supportWaidai" value="1">
										外带</label>
									<label class="radio-inline">
										<input type="checkbox" name="supportWaimai" v-model="m.supportWaimai" value="1">
										外卖</label>
								</div>
							</div>

							<div class="form-group">
								<div class="col-sm-3 control-label">出单类型：</div>
								<div class="col-sm-9 radio-list">
									<label class="radio-inline">
										<input type="checkbox" name="billOfAccount" v-model="m.billOfAccount" value="1">
										出结账单</label>
									<label class="radio-inline">
										<input type="checkbox" name="billOfConsumption" v-model="m.billOfConsumption" value="1">
										出消费单</label>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-3 control-label">关联钱箱：</div>
								<div class="col-sm-9 radio-list">
									<label class="radio-inline">
										<input type="radio"  name="receiveMoney" required v-model="m.receiveMoney" value="1">
										是</label>
									<label class="radio-inline">
										<input type="radio"  name="receiveMoney" v-model="m.receiveMoney" value="0">
										否</label>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-3 control-label">是否开启：</div>
								<div class="col-sm-9 radio-list">
									<label class="radio-inline">
										<input type="radio"  name="state" required v-model="m.state" value="1">
										是</label>
									<label class="radio-inline">
										<input type="radio"  name="state" v-model="m.state" value="0">
										否</label>
								</div>
							</div>
						</div>
						<div class="text-center">
							<input type="hidden" name="id" v-model="m.id" />
							<input class="btn green" type="submit" value="保存" />
							<a class="btn default" @click="cancel">取消</a>
						</div>
						<div class="text-center" style="display: none;" id="validateMsg">
							<br/><br/>
							<p class="text-danger"><strong>IP地址格式不正确</strong></p>
						</div>
					</form>
	            </div>
	        </div>
		</div>
	</div>
	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="printer/add">
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


<script>
	(function(){
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "printer/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
					title : "打印机名称",
					data : "name",
				},                 
				{                 
					title : "IP地址",
					data : "ip",
				},
                {
                    title : "备用IP地址",
                    data : "spareIp",
                },
                {
					title : "端口号",
					data : "port",
				},
				{                 
					title : "打印机类型",
					data : "printType",
					createdCell:function(td,tdData,rowData,row){
						switch(tdData){
						case 1:
							$(td).html('厨房');
						break;
						case 2:
							$(td).html('前台');
						break;
						case 3:
							$(td).html('打包');
						break;
						default:
							$(td).html('未知');
						}
					}
				},
				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="printer/delete">
							C.createDelBtn(tdData,"printer/delete"),
							</s:hasPermission>
							<s:hasPermission name="printer/edit">
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		
		var C = new Controller(cid,tb);
		var vueObj = new Vue({
			el:"#control",
			mixins:[C.formVueMix],
			methods:{
				save:function(e){
					var that = this;
					var formDom = e.target;
					if(isIP(formDom.ip.value)){
						C.ajaxFormEx(formDom,function(){
							that.cancel();
							tb.ajax.reload();
						});
					}else{
						$("#validateMsg").show();
					}
				},

			},

		});
		C.vue = vueObj;
	}());
	
	function isIP(str){
		console.log(str);
		var flag = true;
		var regexp = /^[A-Za-z]+$/	//验证是否为纯字母
		if(!regexp.test(str)){
			//验证 IP 地址是否合法
			regexp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
			flag = regexp.test(str)?true:false;
		}
		return flag;
	}

	
</script>
