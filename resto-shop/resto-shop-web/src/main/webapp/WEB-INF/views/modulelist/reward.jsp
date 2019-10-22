<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<form id="reward-form" role="form" action="modulelist/edit_reward">
	<div class="form-body">
		<div class="form-group">
		    <label>标题</label>
		    <input type="text" class="form-control" name="title" v-model="m.title">
		</div>
		<div class="form-group">
		    <label>金额列表</label>
		    <input type="text" class="form-control" name="moneyList" v-model="m.moneyList">
		</div>
		<div class="form-group">
		    <label>最低评分</label>
		    <input type="text" class="form-control" name="minLevel" v-model="m.minLevel">
		</div>
		<div class="form-group">
		    <label>最小评论长度</label>
		    <input type="text" class="form-control" name="minLength" v-model="m.minLength">
		</div>
		<div class="form-group">
		    <label>是否启用</label>
		    <div class="radio-list">
				<label class="radio-inline">
				  <input type="radio" name="isActivty" v-model="m.isActivty" value="1"> 启用
				</label>
				<label class="radio-inline">
				  <input type="radio" name="isActivty" v-model="m.isActivty" value="0"> 不启用
				</label>
		    </div>
		</div>
	</div>
</form>
<script>
	var obj = new Vue({
		el:"#reward-form",
		data:{
			m:{
				isActivty : 1,
			},
		},
		created:function(){
			var that = this;
			$.post("modulelist/data_reward",null,function(result){
				if(result.data){
					that.m = result.data;
				}
			});
		}
	});
</script>