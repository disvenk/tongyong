<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>

<div id="control" class="row">
	<div class="col-md-offset-3 col-md-6" >
		<div class="portlet light bordered">
			<div class="portlet-title">
				<div class="caption">
					<span class="caption-subject bold font-blue-hoki"> 表单</span>
				</div>
			</div>
			<div class="portlet-body">
				<form role="form" class="form-horizontal"
					  action="{{'shopDetailManage/modify'}}" @submit.prevent="save">
					<div class="form-body">
						<div class="form-group">
							<label class="col-sm-3 control-label">店铺名称：</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="name"
									   :value="m.name" placeholder="必填" required="required">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">打印总单：</label>
							<div class="col-sm-9">
								<div>
									<label >
										<input type="radio" name="autoPrintTotal" v-model="m.autoPrintTotal" value="0">
										是
									</label>
									<label>
										<input type="radio" name="autoPrintTotal" v-model="m.autoPrintTotal" value="1">
										否
									</label>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">套餐出单方式：</label>
							<div class="col-sm-9">
								<div>
									<label >
										<input type="radio" name="printType" v-model="m.printType" value="0">
										整单出单
									</label>
									<label>
										<input type="radio" name="printType" v-model="m.printType" value="1">
										分单出单
									</label>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">买单后出总单（后付款模式）：</label>
							<div class="col-sm-9">
								<div>
									<label>
										<input type="radio" name="isPrintPayAfter" v-model="m.isPrintPayAfter" value="1">
										开
									</label>
									<label>
										<input type="radio" name="isPrintPayAfter"
											   v-model="m.isPrintPayAfter" value="0">
										关
									</label>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">启用餐盒费：</label>
							<div class="col-sm-9">
								<div>
									<label> <input type="radio" name="isMealFee"
												   v-model="m.isMealFee" value="1"> 是
									</label> <label> <input type="radio" name="isMealFee"
															v-model="m.isMealFee" value="0"> 否
								</label>
								</div>
							</div>
						</div>

						<div class="form-group" v-if="m.isMealFee==1">
							<label class="col-sm-3 control-label">名称：</label>
							<div class="col-sm-9">
								<input type="text" class="form-control"
									   name="mealFeeName" v-if="!m.mealFeeName" value="餐盒费"
									   required="required"> <input type="text"
																   class="form-control" name="mealFeeName" v-if="m.mealFeeName"
																   v-model="m.mealFeeName" required="required">
							</div>
						</div>

						<div class="form-group" v-if="m.isMealFee==1">
							<label class="col-sm-3 control-label">餐盒费/盒：</label>
							<div class="col-sm-9">
								<input type="number" class="form-control"
									   name="mealFeePrice" placeholder="(建议输入整数)"
									   v-model="m.mealFeePrice" required="required" min="0">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">开启就餐提醒：</label>
							<div class="col-sm-9">
								<div>
									<label> <input type="radio" name="isPush"
												   v-model="m.isPush" value="1"> 是
									</label> <label> <input type="radio" name="isPush"
															v-model="m.isPush" value="0"> 否
								</label>
								</div>
							</div>
						</div>

						<div class="form-group" v-if="m.isPush==1">
							<label class="col-sm-3 control-label">消息内容：</label>
							<div class="col-sm-9">
								<input type="text" class="form-control"
									   name="pushContext" v-if="!m.pushContext" placeholder="消息文案"
									   required="required"> <input type="text"
																   class="form-control" name="pushContext" v-if="m.pushContext"
																   v-model="m.pushContext" required="required">
							</div>
						</div>

						<div class="form-group" v-if="m.isPush==1">
							<label class="col-sm-3 control-label">推送时间：</label>
							<div class="col-sm-9">
								<input type="number" class="form-control"
									   name="pushTime" placeholder="(建议输入整数,以秒为单位)"
									   v-model="m.pushTime" required="required" min="0">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">店铺标语：</label>
							<div class="col-sm-9">
								<input type="text" class="form-control"
									   name="slogan" placeholder="请输入店铺标语,不填则取品牌设置的内容"
									   v-model="m.slogan">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">等位提示：</label>
							<div class="col-sm-9">
								<textarea rows="3" class="form-control"
										  name="queueNotice" placeholder="请输入等位提示,不填则取品牌设置的内容"
										  v-model="m.queueNotice"></textarea>
							</div>
						</div>


						<div class="form-group">
							<label class="col-sm-3 control-label">开启显示用户标识功能：</label>
							<div class="col-sm-9">
								<div>
									<label> <input type="radio" name="isUserIdentity"
												   v-model="m.isUserIdentity" value="1"> 是
									</label> <label> <input type="radio" name="isUserIdentity"
															v-model="m.isUserIdentity" value="0"> 否
								</label>
								</div>
							</div>
						</div>
					</div>
					<div class="text-center">
						<input class="btn green" type="submit" value="保存" />&nbsp;&nbsp;&nbsp;
						<a class="btn default" @click="cancel">取消</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script>
	$.ajax({
		url:"shopInfo/list_one",
		success:function(result){
			$(document).ready(function() {
				toastr.options = {
					"closeButton" : true,
					"debug" : false,
					"positionClass" : "toast-top-right",
					"onclick" : null,
					"showDuration" : "500",
					"hideDuration" : "500",
					"timeOut" : "3000",
					"extendedTimeOut" : "500",
					"showEasing" : "swing",
					"hideEasing" : "linear",
					"showMethod" : "fadeIn",
					"hideMethod" : "fadeOut"
				}
				var temp;
				var vueObj = new Vue({
					el : "#control",
					data : {
						m : result.data,
						showa:true,
						showlate:true
					},
					watch: {
						'm.consumeConfineUnit': 'hideShowa'
					},
					methods : {
						hideShowa : function(){
							if(this.m.consumeConfineUnit == 3){
								this.showa = false;
								this.showlate=false;
							}else{
								this.showa = true;
								this.showlate=true;
							}
						},
						initTime : function() {
							$(".timepicker-no-seconds").timepicker({
								autoclose : true,
								showMeridian : false,
								minuteStep : 5
							});
						},
						save : function(e) {
							var formDom = e.target;
							$.ajax({
								url : "shopInfo/modify",
								data : $(formDom).serialize(),
								success : function(result) {
									if (result.success) {
										toastr.clear();
										toastr.success("保存成功！");
									} else {
										toastr.clear();
										toastr.error("保存失败");
									}
								},
								error : function() {
									toastr.clear();
									toastr.error("保存失败");
								}
							})

						},
						cancel : function() {
							initContent();

						},
						uploadSuccess:function(url){
							$("[name='photo']").val(url).trigger("change");
							toastr.success("上传成功！");
						},
						uploadError:function(msg){
							toastr.error("上传失败");
						}
					}
				});

			}());
		}

	})

</script>
