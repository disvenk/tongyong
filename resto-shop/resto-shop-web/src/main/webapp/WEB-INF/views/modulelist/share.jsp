<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="../tag-head.jsp" %>
<style>
	.formBox{
		color: #5bc0de;
	}
</style>
<form id="share-form" role="form" class="form-horizontal" action="modulelist/edit_share">
	<div class="form-body">
		<div class="form-group">
			<label  class="col-sm-3 control-label">分享标题</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" name="shareTitle" v-model="m.shareTitle">
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-3 control-label">是否启用</label>
			<div class="col-sm-8">
				<div class="radio-list">
					<label class="radio-inline">
						<input type="radio" name="isActivity" v-model="m.isActivity" value="1"> 启用
					</label>
					<label class="radio-inline">
						<input type="radio" name="isActivity" v-model="m.isActivity" value="0"> 不启用
					</label>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-3 control-label">分享图标</label>
			<div class="col-sm-8">
				<input type="hidden" class="form-control" name="shareIcon" v-model="m.shareIcon">
				<img-file-upload class="form-control" @success="uploadSuccess" @error="uploadError"></img-file-upload>
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-3 control-label">最低分享分数</label>
			<div class="col-sm-8">
				<input type="number" min="1" max="5" class="form-control" name="minLevel" v-model="m.minLevel">
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-3 control-label">最少分享字数</label>
			<div class="col-sm-8">
				<input type="number" class="form-control" name="minLength" v-model="m.minLength">
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-3 control-label">首单返利(%)</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" name="rebate" v-model="m.rebate">
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-3 control-label">首单最小金额</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" name="minMoney" v-model="m.minMoney">
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-3 control-label">首单最大金额</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" name="maxMoney" v-model="m.maxMoney">
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-3 control-label">注册按钮文字</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" name=registerButton v-model="m.registerButton">
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-3 control-label">延迟提醒时间(秒)</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" name="delayTime" v-model="m.delayTime">
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-3 control-label">分享弹窗文本</label>
			<div class="col-sm-8">
				<textarea id="dialogText" name="dialogText" style="height:300px;" v-model="m.dialogText">
				</textarea>
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-3 control-label" :class="{ formBox : m.openMultipleRebates == 1}">是否开启多次获得分享返利</label>
			<div class="col-sm-8">
				<div class="radio-list">
					<label class="radio-inline">
						<input type="radio" name="openMultipleRebates" v-model="m.openMultipleRebates" value="1"> 开启多次返利
					</label>
					<label class="radio-inline">
						<input type="radio" name="openMultipleRebates" v-model="m.openMultipleRebates" value="0"> 仅首单返利
					</label>
				</div>
			</div>
		</div>
		<div class="form-group" v-if="m.openMultipleRebates==1">
			<label  class="col-sm-3 control-label" :class="{ formBox : m.openMultipleRebates == 1}">以后订单返利(%)</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" name="afterRebate" v-model="m.afterRebate">
			</div>
		</div>
		<div class="form-group" v-if="m.openMultipleRebates==1">
			<label  class="col-sm-3 control-label" :class="{ formBox : m.openMultipleRebates == 1}">以后订单最小金额</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" name="afterMinMoney" v-model="m.afterMinMoney">
			</div>
		</div>
		<div class="form-group" v-if="m.openMultipleRebates==1">
			<label  class="col-sm-3 control-label" :class="{ formBox : m.openMultipleRebates == 1}">以后订单最大金额</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" name="afterMaxMoney" v-model="m.afterMaxMoney">
			</div>
		</div>
	</div>
</form>
<script>
	var obj = new Vue({
		el:"#share-form",
		data:{
			m:{
				isActivity : 1,
				minMoney:2,
				maxMoney:100,
			},
		},
		created:function(){
			var that = this;
			$.post("modulelist/data_share",null,function(result){
				if(result.data){
					that.m = result.data;
					that.initEditor();
				}
			});
		},
		methods:{
			uploadSuccess:function(url){
				this.m.shareIcon = url;
				toastr.success("上传图标成功");
			},
			uploadError:function(){
				toastr.error("上传失败");
			},
			initEditor : function () {
				Vue.nextTick(function(){
					var editor = new wangEditor('dialogText');
					editor.config.menus = [
						'bold',
						'underline',
						'italic',
						'strikethrough',
						'eraser',
						'forecolor',
						'bgcolor',
						'|',
						'fontsize',
						'alignleft',
						'aligncenter',
						'alignright',
						'|',
						'undo',
						'redo'
					];
					editor.create();
				});
			}
		}
	});
</script>