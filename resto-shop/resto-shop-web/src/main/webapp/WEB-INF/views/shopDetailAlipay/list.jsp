<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>

<div id="control">
	<div class="col-md-offset-3 col-md-6">
		<div class="portlet light bordered">
			<div class="portlet-title">
				<div class="caption">
					<span class="caption-subject bold font-blue-hoki"> 表单</span>
				</div>
			</div>
			<div class="portlet-body">
				<form role="form" action="{{'shopDetailAlipay/modify'}}" @submit.prevent="save">
					<div class="form-body">
	                    <div class="form-group">
	                        <label>支付宝appId</label>
	                        <input type="text" class="form-control" name="aliAppId" :value="m.aliAppId">
	                    </div>
	                    <div class="form-group">
	                        <label>支付宝私钥</label>
	                         <textarea class="form-control" rows="5" name="aliPrivateKey"
	                                   v-model="m.aliPrivateKey"></textarea>
	                    </div>
	                    <div class="form-group">
	                        <label>支付宝公钥</label>
	                        <textarea class="form-control" rows="5" name="aliPublicKey"
	                                  v-model="m.aliPublicKey"></textarea>
	                    </div>
	                    <div class="form-group">
	                        <label>合作伙伴身份（PID）</label>
	                        <textarea class="form-control" rows="5" name="aliSellerId"
	                                  v-model="m.aliSellerId"></textarea>
	                    </div>
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
			},
			methods:{
				initTime :function(){
					$(".timepicker-no-seconds").timepicker({
						 autoclose: true,
						 showMeridian:false,
			             minuteStep: 5
					  });
				},
				save:function(e){
					var formDom = e.target;
					$.ajax({
						url:"shopDetailAlipay/modify",
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
					
				}
			}
		});
		
		function initContent(){
			$.ajax({
				url:"shopDetailAlipay/list_one",
				success:function(result){
					console.log(result.data);
					vueObj.m=result.data;
				}
			})
		}
		
		
	}());
	
</script>
