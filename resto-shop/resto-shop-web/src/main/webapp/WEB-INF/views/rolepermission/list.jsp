<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>


<div id="control">
    <table id="dynamic-table" class="table table-bordered">
        <thead>
        <tr>
            <th class="center">序号</th>
            <th class='center'>角色</th>
                <!-- 按钮循环 -->
                <c:forEach items="${permissionList}" var="permission" varStatus="pb">
                    <th>${permission.permissionName }</th>
                </c:forEach>
        </tr>
        </thead>
                    <!-- 角色循环 -->
                    <c:forEach items="${roleList}" var="role" varStatus="rs">
                        <tr>
                            <td>${rs.index+1}</td>
                            <td>${role.roleName }</td>
                                <!-- 按钮循环 -->
                                <c:forEach items="${permissionList}" var="permission" varStatus="pb">
                                    <!-- 关联表循环 -->
                                    <c:forEach items="${rolePermissionList}" var="rp" varStatus="vsRb">
                                        <c:if test="${role.id == rp.roleId && rp.permissionId ==permission.id }">
                                            <c:set value="1" var="rbvalue"></c:set>
                                        </c:if>
                                    </c:forEach>
                                    <td>
                                        <input type="checkbox" class="make-switch"  name="${permission.id}" value="${role.id}"  data-on-color="primary" data-off-color="info" data-size="small"  <c:if test="${rbvalue == 1 }">checked="checked"</c:if> />
                                        <%--<input type="checkbox" class="make-switch" name="checkebox"  data-on-color="primary" data-off-color="info" data-size="small"  <c:if test="${rbvalue == 1 }">checked="checked"</c:if> />--%>
                                    </td>
                                    <c:set value="0" var="rbvalue"></c:set>
                                </c:forEach>
                        </tr>
                    </c:forEach>
    </table>
	</div>

    <script type="text/javascript">
        $(document).ready(function(){

        });
        //处理按钮点击
//        function upRb(roleId,permissionId){
//            console.log("1111");
//            $.ajax({
//                type: "POST",
//                url: "rolepermission/upRolePermision",
//                data:{
//                    "roleId":roleId,
//                    "permissionId":permissionId,
//                },
//                dataType:'json',
//                //beforeSend: validateData,
//                cache: false,
//                success: function(data){
//                },
//            });
//        }


        $("input[type=checkbox]").on('switchChange.bootstrapSwitch', function(event, state) {
//            console.log($(this).val()); // DOM element
//            console.log(event); // jQuery event
//            console.log(state); // true | false//var str = $(this).val();
            console.log($(this).val());
            console.log($(this).prop("name"))
            var roleId = $(this).val();
            var permissionId = $(this).prop("name");
            $.ajax({
                type: "POST",
                url: "rolepermission/upRolePermision",
                data:{
                    "roleId":roleId,
                    "permissionId":permissionId,
                    "state":state
                },
                dataType:'json',
                //beforeSend: validateData,
                cache: false,
                success: function(data){
                },
            });
        });


    </script>
