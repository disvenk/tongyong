<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki">表单</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form role="form" action="{{m.id?'brandtemplateedit/modify':'brandtemplateedit/create'}}" @submit.prevent="save">
						<div class="form-body">
<div class="form-group">
    <label>头内容</label>
    <input type="text" class="form-control" name="startSign" v-model="m.startSign">
</div>
<div class="form-group">
    <label>尾内容</label>
    <input type="text" class="form-control" name="endSign" v-model="m.endSign">
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
	
	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="brandtemplateedit/add">
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
				url : "brandtemplateedit/list_all",
				dataSrc : ""
			},
			columns : [
{                 
	title : "序号",
	data : "index",
},                 
{                 
	title : "适用模式",
	data : "pattern",
},                 
{                 
	title : "推送类型",
	data : "pushType",
},                 
{                 
	title : "推送标题",
	data : "pushTitle",
},                 
{                 
	title : "内容示例",
	data : "content",
},

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="brandtemplateedit/delete">
							C.createDelBtn(tdData,"brandtemplateedit/delete"),
							</s:hasPermission>
							<s:hasPermission name="brandtemplateedit/modify">
							C.createEditBtn(rowData),
							</s:hasPermission>
                            <s:hasPermission name="brandtemplateedit/reset">
                            reset(rowData),
                            </s:hasPermission>
                            <s:hasPermission name="brandtemplateedit/startOrOpen">
                            startOrOpen(rowData)
                            </s:hasPermission>
						];
						$(td).html(operator);
					}
				}],
		});
		
		var C = new Controller(null,tb);
		var vueObj = new Vue({
			el:"#control",
			mixins:[C.formVueMix]
		});
		C.vue=vueObj;

        function reset(model,url,urlData){

            var text = model.bigOpen? "关闭模板自定义":"开启模板自定义";
            var button = $("<button class='btn btn-xs btn-primary'>"+text+"</button>");
            button.click(function(){
                $.ajax({
                    type: 'POST',
                    url: "/shop/brandtemplateedit/reset",
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8',
                    data:JSON.stringify({id:model.id,bigOpen:!model.bigOpen}),
                    success: function (result) {
                        if(result.success){
                            C.simpleMsg("开启成功");
                           tb.ajax.reload();
                        }else {
                            C.errorMsg("开启失败");
                        }

                    },
                    error: function (err) {
                        C.errorMsg("开启失败");
                    }
                });


            });
            return button;
        }

        function startOrOpen(model,url,urlData){
            var textBtn = model.bigOpen? "开启系统动态模板":"关闭系统动态模板";
            var bigOpen = model.bigOpen? 0:1;
            var button = $("<button class='btn btn-xs btn-primary'>"+textBtn+"</button>");

            button.click(function(){
                $.ajax({
                    type: 'POST',
                    url: "/shop/brandtemplateedit/startOrOpen",
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8',
                    data:JSON.stringify({id:model.id,bigOpen:bigOpen}),
                    success: function (result) {
                        if(result.success){
                            if(model.bigOpen){
                                C.simpleMsg("开启成功");
							}
							else {
                                C.simpleMsg("关闭成功");
							}

                            tb.ajax.reload();
                        }else {
                            if(model.bigOpen){
                                C.simpleMsg("开启失败");
                            }
                            else {
                                C.simpleMsg("关闭失败");
                            }
                        }

                    },
                    error: function (err) {
                        if(model.bigOpen){
                            C.simpleMsg("开启失败");
                        }
                        else {
                            C.simpleMsg("关闭失败");
                        }
                    }
                });


            });
            return button;
        }
	}());




</script>
