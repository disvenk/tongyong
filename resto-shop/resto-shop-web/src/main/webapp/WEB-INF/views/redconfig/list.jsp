<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki">新建红包配置</span>
					</div>
				</div>
				<div class="portlet-body">
					<form role="form" class="form-horizontal" action="{{m.id?'redconfig/modify':'redconfig/create'}}" @submit.prevent="save">
						<div class="form-group">
							<label class="col-sm-3 control-label"><strong>红包提醒标题：</strong></label>
							<div class="col-sm-8">
								<input type="text" class="form-control" required name="title" v-model="m.title">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><strong>红包提醒备注：</strong></label>
							<div class="col-sm-8">
								<textarea class="form-control" required name="remark" v-model="m.remark"></textarea>
								<input type="hidden" name="delay" value="900" />
							</div>
						</div>
						<!-- 						<div class="form-group"> -->
						<%-- 		           			<label class="col-sm-3 control-label"><strong>延迟发送时间：</strong></label> --%>
						<!-- 						    <div class="col-sm-8"> -->
						<!-- 						    	<div class="input-group"> -->

						<!-- 							    	<input type="number" class="form-control" required min="0" placeholder="" name="delay" v-model="m.delay"/> -->
						<!-- 									<div class="input-group-addon">秒</div> -->
						<!-- 						    	</div> -->
						<!-- 						    </div> -->
						<!-- 						</div> -->
						<div class="form-group">
							<label class="col-sm-3 control-label"><strong>最小比例：</strong></label>
							<div class="col-sm-8">
								<input type="text" class="form-control" required min="0" placeholder="" name="minRatio" v-model="m.minRatio"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><strong>最大比例：</strong></label>
							<div class="col-sm-8">
								<input type="text" class="form-control" required min="0" placeholder="" name="maxRatio" v-model="m.maxRatio">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><strong>最大红包：</strong></label>
							<div class="col-sm-8">
								<div class="input-group">
									<input type="text" class="form-control" required min="0" placeholder="" name="maxSingleRed" v-model="m.maxSingleRed">
									<div class="input-group-addon"><span class="glyphicon glyphicon-yen" aria-hidden="true"></span></div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><strong>最小红包：</strong></label>
							<div class="col-sm-8">
								<div class="input-group">
									<input type="text" class="form-control" required min="0" placeholder="" name="minSignleRed" v-model="m.minSignleRed">
									<div class="input-group-addon"><span class="glyphicon glyphicon-yen" aria-hidden="true"></span></div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><strong>是否可以叠加：</strong></label>
							<div class="col-sm-8">
								<div class="md-radio-inline">
									<div class="md-radio">
										<!-- 判断是否 绑定的对象是否有值，如果没有则不绑定 -->
										<input type="radio" class="md-radiobtn" id="isAddRatio_yes" name="isAddRatio" value="1" v-model="m.isAddRatio" v-if="m.id">
										<input type="radio" class="md-radiobtn" id="isAddRatio_yes" name="isAddRatio" value="1"  v-if="!m.id" checked="checked">
										<label for="isAddRatio_yes">
											<span></span>
											<span class="check"></span>
											<span class="box"></span>是
										</label>
									</div>
									<div class="md-radio">
										<input type="radio" class="md-radiobtn" id="isAddRatio_no" name="isAddRatio" value="0" v-model="m.isAddRatio" v-if="m.id">
										<input type="radio" class="md-radiobtn" id="isAddRatio_no" name="isAddRatio" value="0" v-if="!m.id">
										<label for="isAddRatio_no">
											<span></span>
											<span class="check"></span>
											<span class="box"></span>否
										</label>
									</div>
								</div>
							</div>
						</div>
						<!-- 						<div class="form-group"> -->
						<%-- 		           			<label class="col-sm-3 control-label"><strong>最小消费额度：</strong></label> --%>
						<!-- 						    <div class="col-sm-8"> -->
						<!-- 								<div class="input-group"> -->
						<!-- 							    	<input type="number" class="form-control" required min="0" placeholder="" name="minTranslateMoney" v-model="m.minTranslateMoney"> -->
						<%-- 									<div class="input-group-addon"><span class="glyphicon glyphicon-yen" aria-hidden="true"></span></div> --%>
						<!-- 						    	</div> -->
						<!-- 						    </div> -->
						<!-- 						</div> -->
						<div class="form-group">
							<label class="col-sm-3 control-label"><strong>红包是否启用：</strong></label>
							<div class="col-sm-8">
								<div class="md-radio-inline">
									<div class="md-radio">
										<input type="radio" class="md-radiobtn" id="isActivity_start" name="isActivity" value="1" v-model="m.isActivity" v-if="m.id">
										<input type="radio" class="md-radiobtn" id="isActivity_start" name="isActivity" value="1"  v-if="!m.id" checked="checked">
										<label for="isActivity_start">
											<span></span>
											<span class="check"></span>
											<span class="box"></span>启用
										</label>
									</div>
									<div class="md-radio">
										<input type="radio" class="md-radiobtn" id="isActivity_stop" name="isActivity" value="0" v-model="m.isActivity" v-if="m.id">
										<input type="radio" class="md-radiobtn" id="isActivity_stop" name="isActivity" value="0" v-if="!m.id">
										<label for="isActivity_stop">
											<span></span>
											<span class="check"></span>
											<span class="box"></span>停用
										</label>
									</div>
								</div>
							</div>
						</div>
						<div class="text-center">
							<input type="hidden" name="id" v-model="m.id" />
							<input class="btn green"  type="submit"  value="保存"/>
							<a class="btn default" @click="cancel" >取消</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="redconfig/add">
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
		var C;
		var vueObj;
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "redconfig/list_all",
				dataSrc : ""
			},
			columns : [
				{
					title : "红包标题",
					data : "title",
				},
				{
					title : "红包备注",
					data : "remark",
				},
// 				{
// 					title : "发送延迟时间",
// 					data : "delay",
// 				},
				{
					title : "最小比例",
					data : "minRatio",
				},
				{
					title : "最大比例",
					data : "maxRatio",
				},
				{
					title : "最大红包",
					data : "maxSingleRed",
				},
				{
					title : "最小红包",
					data : "minSignleRed",
				},
				{
					title : "是否可以叠加",
					data : "isAddRatio",
					createdCell:function(td,tdData){
						var str = "未知"
						if(tdData == "0"){
							str = "否"
						}else if(tdData == "1"){
							str = "是"
						}
						$(td).html(str);
					}
				},
// 				{
// 					title : "最低消费额度",
// 					data : "minTranslateMoney",
// 				},
				{
					title : "是否启用",
					data : "isActivity",
					createdCell:function(td,tdData){
						var str = "未知"
						if(tdData == "0"){
							str = "停用"
						}else if(tdData == "1"){
							str = "启用"
						}
						$(td).html(str);
					}
				},
				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="redconfig/delete">
							C.createDelBtn(tdData,"redconfig/delete"),
							</s:hasPermission>
							<s:hasPermission name="redconfig/edit">
							C.createEditBtn(rowData),
							</s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		C = new Controller(cid,tb);

		var option = {
			el:cid,
			mixins:[C.formVueMix],
			methods:{
				save:function(e){
					var that = this;
					var url = that.m.id?'redconfig/modify':'redconfig/create';
					var formDom = e.target;
//                    C.ajaxFormEx(formDom,function(){
//                        that.cancel();
//                        tb.ajax.reload();
//                    });
					if(parseInt(that.m.maxRatio) < parseInt(that.m.minRatio)){
						toastr.clear();
						toastr.error("最大比例不得小于最小比例！");
						return;
					}
					$.ajax({
						url : url,
						data : $(formDom).serialize(),
						success : function(result) {
							if (result.success) {
								that.cancel();
								tb.ajax.reload();
								toastr.clear();
								toastr.success("保存成功！");
							} else {
								that.cancel();
								tb.ajax.reload();
								toastr.clear();
								toastr.error("保存失败");
							}
						},
						error : function() {
							that.cancel();
							tb.ajax.reload();
							toastr.clear();
							toastr.error("保存失败");
						}
					})
				}
			},
		};

		vueObj = C.vueObj(option);

	}());

</script>
