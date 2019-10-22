<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<form class="form-horizontal" id="sqlForm">
  <div class="form-group">
    <label for="inputEmail3" class="col-sm-2 control-label">数 据 库：</label>
    <div class="col-sm-10" id="databaseNames">	 
	</div>
  </div>
  <div class="form-group">
    <div class="col-sm-10 col-md-offset-2">
      	<label class="checkbox-inline" style="margin-left:-25px;">
		  <input type="checkbox" id="checkAll">全选
		</label>
    </div>
  </div>
  <div class="form-group">
    <label for="inputPassword3" class="col-sm-2 control-label">SQL 命令：</label>
    <div class="col-sm-10">
      <textarea class="form-control" name="sql" id="sql" rows="3" placeholder="【暂不支持查询语句】请输入要执行的SQL语句,若要执行多条SQL语句,请用分号隔开!"></textarea>
    </div>
  </div>
  <div class="form-group">
  	<label for="inputPassword3" class="col-sm-2 control-label">迷之口令：</label>
    <div class="col-sm-2">
    	<input type="password" name="pwd" id="pwd" class="form-control">
    </div>
    <div class="col-sm-3">
    	<button type="button" id="run" class="btn btn-default">执行</button>
    </div>
  </div>
  <div class="col-sm-offset-2">
    <p class="text-danger" id="errorMsg"></p>
  </div>
</form>
<div class="panel panel-success " id="result">
  <div class="panel-heading"><strong>执行结果</strong></div>
  <div class="panel-body" style="font-size:18px;font-weight: bold;">
  </div>
</div>

<script>
	$(function() {
		//查询 所有数据库
		$.ajax({
			url : "sqlutil/query",
			success : function(data) {
				$(data).each(function(i, item) {
					var str = "<label class='checkbox-inline'>"+
					"<input type='checkbox' name='databaseIds' value='"+item.id+"'/>"+item.name+
					"</label>";
					$("#databaseNames").append(str);
				})
				$("#databaseNames").trigger("create");
			}
		});
	
		//执行
		$("#run").click(function() {
			if($("input:checked").length<=0){
				$("#errorMsg").html("<strong>请选择要操作的数据库！</strong>");
				return false;
			}
			if(checkNull("sql")){
				$("#errorMsg").html("<strong>请输入SQL语句！</strong>");
				return false;
			}
			if(checkNull("pwd")){
				$("#errorMsg").html("<strong>请输入迷之口令！</strong>");
				return false;
			}
			$.ajax({
				type : "POST",
				url : "sqlutil/run",
				dataType : "json",
				data : $("#sqlForm").serialize(),
				success : function(data) {
					if(data.success){
						$("#result").removeClass("panel-danger").addClass("panel-success");
					}else{
						$("#result").removeClass("panel-success").addClass("panel-danger");
					}
					$("#result > .panel-body").html(data.message);
				}
			});
		});
	
		//全选
		$("#checkAll").click(function(){
			if($(this).prop("checked")){
				$(":checkbox[name='databaseIds']").prop("checked",true);
			}else{
				$(":checkbox[name='databaseIds']").prop("checked",false);
			}
		})
		
		function checkNull(id){
			var str = $("#"+id).val();
			if(str == null){
				return true;
			}
			str = str.replace(/(^s*)|(s*$)/g, "");
			if(str == ""){
				return true;
			}
			return false;
		}
	})
</script>
