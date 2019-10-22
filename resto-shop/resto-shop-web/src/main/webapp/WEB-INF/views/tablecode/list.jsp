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
	            	<form role="form" action="{{m.id?'tablecode/modify':'tablecode/create'}}" @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
                            <label>桌位名称</label>
                            <input type="text" class="form-control" name="name" v-model="m.name" @blur="checkName(m.name)">
                        </div>
                        <div class="form-group">
                            <label>桌位编号</label>
                            <input type="text" class="form-control" name="codeNumber" v-model="m.codeNumber" @blur="checkCodeNumber(m.codeNumber)">
                        </div>

                            <div class="form-group">
                                <label>排序</label>
                                <input type="text" class="form-control" name="sort" v-model="m.sort" >
                            </div>

                            <div class="form-group">
                                <label>最小人数</label>
                                <input type="number" class="form-control" name="minNumber" v-model="m.minNumber" @blur="checkNumber(m)">
                            </div>
                        <div class="form-group">
                            <label>最大人数</label>
                            <input type="number" class="form-control" name="maxNumber" v-model="m.maxNumber" @blur="checkNumber(m)">
                        </div>

                        <%--<div class="form-group">--%>
                            <%--<label>是否开启</label>--%>
                            <%--<input type="text" class="form-control" name="isUsed" v-model="m.isUsed">--%>
                        <%--</div>--%>

                            <div class="form-group">
                                <label class="col-sm-2 control-label">是否启用：</label>
                                <div class="col-sm-8 radio-list">
                                    <label class="radio-inline">
                                        <input type="radio" name="isUsed" v-model="m.isUsed" value="1"> 启用
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="isUsed" v-model="m.isUsed" value="0"> 不启用
                                    </label>
                                </div>
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
			<s:hasPermission name="tablecode/add">
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
            "order": [[ 2, "asc" ]],
			ajax : {
				url : "tablecode/list_all",
				dataSrc : ""
			},
			columns : [
				{                 
                    title : "桌位名称",
                    data : "name",
                },
                {
                    title : "桌位编号",
                    data : "codeNumber",
                },
                {
                    title : "排序",
                    data : "sort",
                },
                {
                    title : "最小人数",
                    data : "minNumber",
                },
                {
                    title : "最大人数",
                    data : "maxNumber",
                },

                {
                    title : "是否开启",
                    data : "isUsed",
                    createdCell: function (td,tdData) {
                        if(tdData==1){
                            $(td).html("开启");
                        }else if(tdData==0){
                            $(td).html("未开启")
                        }
                    }
                },

				{
					title : "操作",
					data : "id",
					createdCell:function(td,tdData,rowData,row){
						var operator=[
							<s:hasPermission name="tabcode/delete">
							C.createDelBtn(tdData,"tablecode/delete"),
							</s:hasPermission>
							<s:hasPermission name="tablecode/modify">
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
			mixins:[C.formVueMix],
            methods :{
                checkName : function (name) {
                    $.ajax({
                        url:'tablecode/checkName',
                        data:{
                            'name':name,
                        },
                        success:function (result) {
                           if(result.success==false){
                               toastr.error(result.message);
                           }
                        },
                    })
                },
                checkCodeNumber : function (codeNumber) {
                    $.ajax({
                        url:'tablecode/checkCodeNumber',
                        data:{
                            'codeNumber':codeNumber,
                        },
                        success:function (result) {
                            if(result.success==false){
                                toastr.error(result.message);
                            }
                        },
                    })
                },
                checkNumber: function (m) {
                    if(m.minNumber!=undefined&&m.maxNumber!=undefined){
                        if(parseInt(m.minNumber)>parseInt(m.maxNumber)){
                            toastr.error("最小人数不能大于最大人数");
                        }
                    }
                },
            }

		});
		C.vue=vueObj;
	}());
	
	

	
</script>
