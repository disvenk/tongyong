<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="v-bind" uri="http://www.springframework.org/tags/form" %>
<style>
th {
	width: 30%;
}
dt,dd{
	height: 25px;
}
</style>

<h2 class="text-center">
	<strong>给员工分配角色</strong>
</h2>
<br />
<div class="row" id="empRole">
	<div class="col-md-12">
		<form class="form-inline" role="form"  action="/employee/assign_form"  @submit.prevent="save">
			<input type="hidden" name="employeeId" value="${employeeId}"/>
			<table class="table table-bordered">
					<c:forEach var="item" items="${elist}" >
						<tr>
							<td style="width: 25%"><input type="checkbox"  id="${item.shopId}" value=${item.shopId} class="item-edit" name="shops"  />${item.shopName}  </td>
							<c:forEach  var="i" items="${item.eRolelist}" >
								<td style="width: 25%">
									<div class="checkbox" >
										<label>
											<input type="checkbox" id="${item.shopId}_${i.id}" value=${item.shopId}_${i.id} name="spCodeId"  class="item-edit" /> ${i.roleName}
										</label>
									</div>
								</td>
							</c:forEach>
						</tr>
					</c:forEach>
			</table>
			<button type="submit" class="btn btn-primary">提交</button>
		</form>
	</div>
</div>


<script>

	$(function(){
		var employeeId =  $("[name='employeeId']").val();
		var vm;
		vm = new Vue({
			//parent: vueObj,

			el: "#empRole",
			data: {

				employeeRoles:[],
				formData: "",
			},
			methods: {
                save: function () {
                	var that = this;
                	//获取所有的选中的checkbox的id值
					$('input:checkbox[name=spCodeId]:checked').each(function(i){
						if(0==i){
							vm.formData = $(this).val();
						}else{
							vm.formData += (","+$(this).val());
						}
					});

                    $.ajax({
                        url: "employee/assign_form",
                        type: "post",
                        data: {
                            "employeeId": employeeId,
                            "id": vm.formData,
                        },
                        success: function (result) {
							toastr.success("保存成功");
							$("#employeeRoModal").modal('hide');
                        }
                    })
                },

			},
			ready : function () {
				//选中已经有的角色
				$.ajax({
							url :  "employee/listIds",
							data : {
								"employeeId":employeeId,
							},
							success: function (result) {
								var data = result.data;
								if(result.data!=null){
									for(var i=0 ; i<data.length ;i++){
										$("#"+data[i]).prop("checked", "checked");
									}
                                    vm.employeeRoles = data;
								}
						}
					}
				)
				//
			$('.item-edit').on('click', function() {
				var item = $(this).val();//获取这个id
				var temp = $(this).is(':checked');
				//选择店铺的时候做全选和全部选操作
				if(item.indexOf("_")==-1){
					$("input:checkbox[name=spCodeId]").each(function(i){
							if($(this).val().indexOf(item)>=0){
								$(this).prop("checked",temp)
							}
					});
				}else {
                    //获取店铺的
                    var item2 = item.substring(0,item.indexOf("_"));
                    //当前选中店铺必选中
						if(temp){
                            $("#"+item2).prop("checked",true);
						}else{
						    //如果当前是未选中，则判断这一行是否都是未选中，只有都是未选则更改店铺为未选中
                            //获取当时的shop状态
                            var temp3 = $("#"+item2).prop("checked");
                            //当前未选中，店铺是选中
                            if(temp3){
                                var temp4 = false;
                                var m;
                                $("input:checkbox[name=spCodeId]:checked").each(function(i){
                                     m = $(this).val().substring(0,32);
                                    if(m==item2){
                                       temp4 = true;
                                    }

                                });
                                //console.log(temp4);
                                $("#"+item2).prop("checked",temp4);
                            }


                        }



				}

			});

			},
		});



	});

</script>
