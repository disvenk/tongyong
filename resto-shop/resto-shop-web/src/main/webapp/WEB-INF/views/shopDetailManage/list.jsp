<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<style>
	.formBox{
		color: #5bc0de;
	}
</style>
<div id="control" class="row">
	<div class="col-md-offset-3 col-md-6">
		<div class="portlet light bordered">
			<div class="portlet-title">
				<div class="caption">
					<span class="caption-subject bold font-blue-hoki"> 表单</span>
				</div>
			</div>
			<div class="portlet-body">
				<form role="form" action="{{'shopDetailManage/modify'}}" @submit.prevent="save">
					<div class="form-body">
						<div class="form-group">
							<div class="control-label">等位红包</div>
							<label >
								<input type="radio" name="waitRedEnvelope" v-model="m.waitRedEnvelope" value="1">
								开启
							</label>
							<label>
								<input type="radio" name="waitRedEnvelope" v-model="m.waitRedEnvelope" value="0">
								关闭
							</label>
						</div>
						<div class="form-group" v-if="m.waitRedEnvelope == 1">
							<label>下载店铺等位二维码：</label>
								<button class="btn green " @click="downloadQRcode">下载</button>
						</div>
						<div class="form-group">
							<label>等位红包失效时间：</label>
							<div>
								<input type="text" class="form-control" name="timeOut" v-model="m.timeOut" placeholder="单位为小时">
							</div>
						</div>
						<%--<div  class="form-group">--%>
							<%--<label>等位红包每秒增加价</label>--%>
							<%--<input type="text" class="form-control" name="baseMoney" :value="m.baseMoney">--%>
						<%--</div>--%>
						<%--<div  class="form-group">--%>
							<%--<label>等位红包上限价格</label>--%>
							<%--<input type="text" class="form-control" name="highMoney" :value="m.highMoney">--%>
						<%--</div>--%>

						<%--<div  class="form-group">--%>
							<%--<label style="float:left" >等位红包失效时间</label>--%>
							<%--<div style="clear:both"></div>--%>
							<%--<input v-if="showWaitTime" type="number" name="waitTime"  id="waitTime" min="1" class="form-control" style="width:20%;float:left;margin-right: 5px"  :value="m.waitTime">--%>
							<%--<select class="form-control" style="width:30%;float:left" id="waitUnit"  name="waitUnit" @click="selectWaitUnit" v-model="m.waitUnit">--%>
								<%--<option  value="1">小时</option>--%>
								<%--<option selected="selected" value="2">天</option>--%>
								<%--<option value="3">无限制</option>--%>
							<%--</select>--%>
						<%--</div>--%>

						<!--    Geek叫号功能    begin-->
						<div class="form-group">
							<label>店铺标语：</label>
							<div>
								<input type="text" class="form-control" name="slogan" placeholder="请输入店铺标语,不填则取品牌设置的内容"
									   v-model="m.slogan">
							</div>
						</div>

						<div class="form-group">
							<label>字体颜色</label>
							<div>
								<input type="text" class="form-control color-mini" name="tvTextColor"
									   data-position="bottom left" v-model="m.tvTextColor">
							</div>
						</div>

						<div class="form-group">
							<label>背景图片</label>
							<div>
								<input type="hidden" name="tvBackground" v-model="m.tvBackground">
								<img-file-upload class="form-control" @success="uploadSuccess"
												 @error="uploadError"></img-file-upload>
								<img v-if="m.tvBackground" :src="m.tvBackground" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
							</div>
						</div>

						<div class="form-group">
							<label>文本框底色</label>
							<div>
								<input type="text" class="form-control color-mini-textbox" name="tvTextBoxBackground"
									   data-position="bottom left" v-model="m.tvTextBoxBackground">
							</div>
						</div>

						<div class="form-group">
							<label>标头颜色</label>
							<div>
								<input type="text" class="form-control color-mini-head" name="tvHeadTextColor"
									   data-position="bottom left" v-model="m.tvHeadTextColor">
							</div>
						</div>

						<div class="form-group">
							<label>数字颜色</label>
							<div>
								<input type="text" class="form-control color-mini-number" name="tvNumberColor"
									   data-position="bottom left" v-model="m.tvNumberColor">
							</div>
						</div>
						<%--<div class="form-group">--%>
							<%--<label class="col-md-4 control-label">等位提示：</label>--%>
							<%--<div  class="col-md-6 ">--%>
								<%--<textarea rows="3" class="form-control" name="queueNotice" placeholder="请输入等位提示,不填则取品牌设置的内容"--%>
										  <%--v-model="m.queueNotice"></textarea>--%>
							<%--</div>--%>
						<%--</div>--%>
						<!--    Geek叫号功能    end-->
						<%--<div class="form-group">--%>
							<%--<label :class="{ formBox : m.isUserIdentity == 1}">开启显示用户标识功能：</label>--%>
							<%--<div>--%>
								<%--<label class="radio-inline">--%>
									<%--<input type="radio" name="isUserIdentity" v-model="m.isUserIdentity" value="1"> 是--%>
								<%--</label>--%>
								<%--<label class="radio-inline">--%>
									<%--<input type="radio" name="isUserIdentity" v-model="m.isUserIdentity" value="0"> 否--%>
								<%--</label>--%>
							<%--</div>--%>
						<%--</div>--%>

						<div class="form-group">
							<label>叫号推送模板：</label>
							<div>
								<input type="text" class="form-control" name="waitJiaohao" v-model="m.waitJiaohao">
							</div>
						</div>

						<div class="form-group">
							<label>就餐推送模板：</label>
							<div>
								<input type="text" class="form-control" name="waitJiucan" v-model="m.waitJiucan">
							</div>
						</div>

						<div class="form-group">
							<label>过号推送模板：</label>
							<div>
								<input type="text" class="form-control" name="waitGuohao" v-model="m.waitGuohao">
							</div>
						</div>

						<div class="form-group">
							<label :class="{ formBox : m.waitRemindSwitch == 1}">是否开启等位提醒：</label>
							<div>
								<label class="radio-inline">
									<input type="radio" name="waitRemindSwitch" v-model="m.waitRemindSwitch" value="1"> 是
								</label>
								<label class="radio-inline">
									<input type="radio" name="waitRemindSwitch" v-model="m.waitRemindSwitch" value="0"> 否
								</label>
							</div>
						</div>
						<div class="form-group" v-if="m.waitRemindSwitch==1">
							<label :class="{ formBox : m.waitRemindSwitch == 1}">提前提醒桌数：</label>
							<div>
								<input type="number" class="form-control" name="waitRemindNumber" v-model="m.waitRemindNumber">
							</div>
						</div>

						<div class="form-group" v-if="m.waitRemindSwitch==1">
							<label :class="{ formBox : m.waitRemindSwitch == 1}">提醒推送文案：</label>
							<div>
								<input type="text" class="form-control" name="waitRemindText" v-model="m.waitRemindText">
							</div>
						</div>

						<div style="clear:both"></div>
						<br/>
					</div>
					<input class="btn green" type="submit" value="保存" /> 
					<a class="btn default" @click="cancel">取消</a>
				</form>
			</div>
		</div>
	</div>
</div>

<script>
	
	$(document).ready(function(){
		initContent();
		toastr.options = {
				  "closeButton": true,
				  "debug": false,
				  "positionClass": "toast-top-right",
				  "onclick": null,
				  "showDuration": "500",
				  "hideDuration": "500",
				  "timeOut": "3000",
				  "extendedTimeOut": "500",
				  "showEasing": "swing",
				  "hideEasing": "linear",
				  "showMethod": "fadeIn",
				  "hideMethod": "fadeOut"
				}
		var temp;
		var vueObj = new Vue({
			el:"#control",
			data:{
				m:{},
				showWaitTime:true
			},
			created: function () {
//				tb.search("").draw();
				var n = $('.color-mini').minicolors({
					change: function (hex, opacity) {
						if (!hex) return;
						if (typeof console === 'object') {
							$(this).attr("value", hex);
						}
					},
					theme: 'bootstrap'
				});
				this.$watch("m", function () {
					if (this.m.id) {
						$('.color-mini').minicolors("value", this.m.tvTextColor);
					}
				});

				var m = $('.color-mini-textbox').minicolors({
					change: function (hex, opacity) {
						if (!hex) return;
						if (typeof console === 'object') {
							$(this).attr("value", hex);
						}
					},
					theme: 'bootstrap'
				});
				this.$watch("m", function () {
					if (this.m.id) {
						$('.color-mini-textbox').minicolors("value", this.m.tvTextBoxBackground);
					}
				});

				var k = $('.color-mini-head').minicolors({
					change: function (hex, opacity) {
						if (!hex) return;
						if (typeof console === 'object') {
							$(this).attr("value", hex);
						}
					},
					theme: 'bootstrap'
				});
				this.$watch("m", function () {
					if (this.m.id) {
						$('.color-mini-head').minicolors("value", this.m.tvHeadTextColor);
					}
				});

				var l = $('.color-mini-number').minicolors({
					change: function (hex, opacity) {
						if (!hex) return;
						if (typeof console === 'object') {
							$(this).attr("value", hex);
						}
					},
					theme: 'bootstrap'
				});
				this.$watch("m", function () {
					if (this.m.id) {
						$('.color-mini-number').minicolors("value", this.m.tvNumberColor);
					}
				});
			},
			methods:{
				initTime :function(){
					$(".timepicker-no-seconds").timepicker({
						 autoclose: true,
						 showMeridian:false,
			             minuteStep: 5
					  });
				},
				selectWaitUnit:function(){
					if($('#waitUnit').val() == 3){
						this.showWaitTime = false;
					}else{
						this.showWaitTime = true;
					}
				},
				save:function(e){
					var formDom = e.target;
					$.ajax({
						url:"shopDetailManage/modify",
						data:$(formDom).serialize(),
						success:function(result){
							if(result.success){
								toastr.clear();
								toastr.success("保存成功！");
							}else{
								toastr.clear();
								toastr.error("保存失败");
							}
						},
						error:function(){
							toastr.clear();
							toastr.error("保存失败");
						}
					})
				},
				cancel:function(){
					initContent();
				},
				uploadSuccess: function (url) {
					console.log(url);
					$("[name='tvBackground']").val(url).trigger("change");
					C.simpleMsg("上传成功");
					$("#tvBackground").attr("src", "/" + url);
				},
				uploadError: function (msg) {
					C.errorMsg(msg);
				},
				downloadQRcode: function () {
					var that = this;
					$.ajax({
						url: "shopDetailManage/openQRCode",
						type: "post",
						data: {"shopId": this.m.id},
						success: function (data) {
							window.open(data);
						}
					})
				},
			}
		});
		
		function initContent(){
			$.ajax({
				url:"shopDetailManage/list_one",
				success:function(result){
					var tem1 = result.data.openTime;
					var tem2 = result.data.closeTime;
					var open;
	 				var close;
					open = new Date(tem1).format("hh:mm"); 
	 				close = new Date(tem2).format("hh:mm");
	 				if(open=='aN:aN'){
						open = tem1;
					}
					if(close=='aN:aN'){
						close=tem2;
	 				}
	 				result.data.openTime=open;
	 				result.data.closeTime=close;
	 				objectName = result.data;
	 				vueObj.m=result.data;
					if(vueObj.m.waitUnit == 3){
						vueObj.showWaitTime = false;
					}
				}
			})
		}
	}());
	
</script>
