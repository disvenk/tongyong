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
				<form role="form" action="{{'shopTvConfig/modify'}}" @submit.prevent="save">
					<div class="form-body">
						<input type="hidden" class="form-control" name="shopDetailId" v-model="m.shopDetailId">
						<input type="hidden" class="form-control" name="id" v-model="m.id">

						<div class="form-group">
							<label>准备中底色：</label>
							<div>
								<input type="text" class="form-control color-mini-a" name="readyBackColor"
									   data-position="bottom left" v-model="m.readyBackColor">
							</div>
						</div>

						<div class="form-group">
							<label>请取餐底色：</label>
							<div>
								<input type="text" class="form-control color-mini-b" name="takeMealBackColor"
									   data-position="bottom left" v-model="m.takeMealBackColor">
							</div>
						</div>

						<div class="form-group">
							<label>当前叫号底色：</label>
							<div>
								<input type="text" class="form-control color-mini-c" name="callBackColor"
									   data-position="bottom left" v-model="m.callBackColor">
							</div>
						</div>

						<div class="form-group">
							<label>背景图片</label>
							<div>
								<input type="hidden" name="tvBackground" v-model="m.tvBackground">
								<img-file-upload class="form-control" @success="uploadSuccess"
												 @error="uploadError" cut="false"></img-file-upload>
								<img v-if="m.tvBackground" :src="m.tvBackground" :alt="m.name" onerror="this.src='assets/pages/img/defaultImg.png'" width="80px" height="40px" class="img-rounded">
							</div>
						</div>

						<div class="form-group">
							<label>数字的颜色：</label>
							<div>
								<input type="text" class="form-control color-mini-d" name="numberColor"
									   data-position="bottom left" v-model="m.numberColor">
							</div>
						</div>

						<div class="form-group">
							<label>当前叫号的颜色：</label>
							<div>
								<input type="text" class="form-control color-mini-e" name="callNumberColor"
									   data-position="bottom left" v-model="m.callNumberColor">
							</div>
						</div>

						<div class="form-group">
							<label>提示文本信息：</label>
							<div>
								<input type="text" class="form-control" name="text" v-model="m.text">
							</div>
						</div>

						<div class="form-group">
							<label>标签上的字体颜色：</label>
							<div>
								<input type="text" class="form-control color-mini-f" name="labelColor"
									   data-position="bottom left" v-model="m.labelColor">
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
				var a = $('.color-mini-a').minicolors({
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
						$('.color-mini-a').minicolors("value", this.m.readyBackColor);
					}
				});

				var b = $('.color-mini-b').minicolors({
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
						$('.color-mini-b').minicolors("value", this.m.takeMealBackColor);
					}
				});

				var c = $('.color-mini-c').minicolors({
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
						$('.color-mini-c').minicolors("value", this.m.callBackColor);
					}
				});

				var d = $('.color-mini-d').minicolors({
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
						$('.color-mini-d').minicolors("value", this.m.numberColor);
					}
				});

				var e = $('.color-mini-e').minicolors({
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
						$('.color-mini-e').minicolors("value", this.m.callNumberColor);
					}
				});

				var f = $('.color-mini-f').minicolors({
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
						$('.color-mini-f').minicolors("value", this.m.labelColor);
					}
				});
			},
			methods:{
				save:function(e){
					var formDom = e.target;
					$.ajax({
						url:"shopTvConfig/modify",
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
			}

		});
		
		function initContent(){
			$.ajax({
				url:"shopTvConfig/list_one",
				success:function(result){
	 				vueObj.m=result.data;
				}
			})
		}
		
		
	}());
	
</script>
