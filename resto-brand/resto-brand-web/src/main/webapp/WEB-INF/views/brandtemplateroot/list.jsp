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
	            	<form role="form" action="{{m.id?'brandtemplateroot/modify':'brandtemplateroot/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
    <label>模板编号</label>
    <input type="text" class="form-control" name="templateNumber" v-model="m.templateNumber">
</div>
<div class="form-group">
    <label>适用模式</label>
    <input type="text" class="form-control" name="pattern" v-model="m.pattern">
</div>
<div class="form-group">
    <label>推送类型</label>
    <input type="text" class="form-control" name="pushType" v-model="m.pushType">
</div>
<div class="form-group">
    <label>推送标题</label>
    <input type="text" class="form-control" name="pushTitle" v-model="m.pushTitle">
</div>
<div class="form-group">
    <label>内容示例</label>
    <input type="text" class="form-control" name="content" v-model="m.content">
</div>
<div class="form-group">
    <label>头内容</label>
    <input type="text" class="form-control" name="startSign" v-model="m.startSign">
</div>
<div class="form-group">
    <label>尾内容</label>
    <input type="text" class="form-control" name="endSign" v-model="m.endSign">
</div>
<div class="form-group">
	<label>业务编号</label>
	<input type="text" class="form-control" name="templateSign" v-model="m.templateSign">
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
			<s:hasPermission name="brandtemplateroot/add">
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
       var start ="";
       var end = "";
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "brandtemplateroot/list_all",
				dataSrc : ""
			},
			columns : [
                {
                    title : "业务编号",
                    data : "templateSign",
                },
				{

	title : "模板编号",
	data : "templateNumber",
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
	data : "contentStr",
    render: function (data) {
	    var strArr = data.split(",");
	    var str = "";
	    for( var item in strArr){
	        if(item==0){
	            str=str.concat("<span style='color: red'>"+strArr[item]+"</span></br>")
				continue;
			}
			if(item==strArr.length-1){
                str=str.concat("<span style='color: red'>"+strArr[item]+"</span>")
				break;
			}
            str=str.concat(strArr[item]+"</br>");
		}
		console.log(str)
        return str;
    }
},

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="brandtemplateroot/delete">
							C.createDelBtn(tdData,"brandtemplateroot/delete"),
							</s:hasPermission>
							<s:hasPermission name="brandtemplateroot/modify">
							C.createEditBtn(rowData),
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
	}());
	
	

	
</script>
