<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="tag-head.jsp" %>


<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
    <!--<![endif]-->
    <!-- BEGIN HEAD -->
    <head>
    	<base href="<%=basePath%>" />
        <meta charset="utf-8" />
        <title>${current_shop_name == null? current_brand_name : current_shop_name} - 店铺管理</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta content="width=device-width, initial-scale=1" name="viewport" />
        <meta content="" name="description" />
        <meta content="" name="author" />
        <!-- BEGIN GLOBAL MANDATORY STYLES -->
        <link href="<%=resourcePath%>/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=resourcePath%>/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=resourcePath%>/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=resourcePath%>/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css" />
        <link href="<%=resourcePath%>/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.css" rel="stylesheet" type="text/css" />
        <!-- END GLOBAL MANDATORY STYLES -->
        <!-- BEGIN PAGE LEVEL PLUGINS -->
        <link href="<%=resourcePath%>/assets/global/plugins/datatables/datatables.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=resourcePath%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=resourcePath%>/assets/global/plugins/jstree/dist/themes/default/style.min.css"/>
		<link rel="stylesheet" type="text/css" href="<%=resourcePath%>/assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
		<link rel="stylesheet" type="text/css" href="<%=resourcePath%>/assets/global/plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css"/>
		
		<link rel="stylesheet" type="text/css" href="<%=resourcePath%>/assets/global/plugins/bootstrap-toastr/toastr.min.css">
		<link href="<%=resourcePath%>/assets/global/plugins/bootstrap-colorpicker/css/colorpicker.css" rel="stylesheet" type="text/css" />
        <link href="<%=resourcePath%>/assets/global/plugins/jquery-minicolors/jquery.minicolors.css" rel="stylesheet" type="text/css" />
		
		<link href="<%=resourcePath%>/assets/global/plugins/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=resourcePath%>/assets/global/plugins/select2/css/select2-bootstrap.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=resourcePath%>/assets/global/plugins/typeahead/typeahead.css" rel="stylesheet" type="text/css" />        
        <link href="<%=resourcePath%>/assets/global/plugins/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=resourcePath%>/assets/treeview/css/zTreeStyle.css" rel="stylesheet" type="text/css" />
        <!-- END PAGE LEVEL PLUGINS -->
        <!-- BEGIN THEME GLOBAL STYLES -->
        <link href="<%=resourcePath%>/assets/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
        <link href="<%=resourcePath%>/assets/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
        <!-- END THEME GLOBAL STYLES -->
        <!-- BEGIN THEME LAYOUT STYLES -->
        <link href="<%=resourcePath%>/assets/layouts/layout/css/layout.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=resourcePath%>/assets/layouts/layout/css/themes/darkblue.min.css" rel="stylesheet" type="text/css" id="style_color" />
        <link href="<%=resourcePath%>/assets/layouts/layout/css/custom.min.css" rel="stylesheet" type="text/css" />
        
        <link rel="stylesheet" href="<%=resourcePath %>/assets/customer/css/custom.css"/>
        <!-- END THEME LAYOUT STYLES -->
		<link rel="shortcut icon" href="assets/pages/img/favicon.ico" />
        <link href="<%=resourcePath%>/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />

        <%--    wangEditor  富文本编辑器  --%>
        <link href="<%=resourcePath%>/assets/global/plugins/wangEditor-2.1.22/css/wangEditor.css" rel="stylesheet">

    <!-- END HEAD -->

    <body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white">
        <!-- BEGIN HEADER -->
   		<jsp:include page="head.jsp"></jsp:include>
        <!-- END HEADER -->
        <!-- BEGIN HEADER & CONTENT DIVIDER -->
        <div class="clearfix"> </div>
        <!-- END HEADER & CONTENT DIVIDER -->
        <!-- BEGIN CONTAINER -->
        <div class="page-container">
            <!-- BEGIN SIDEBAR -->
            <div class="page-sidebar-wrapper">
                <!-- BEGIN SIDEBAR -->
                <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
                <!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
                <div class="page-sidebar navbar-collapse collapse">
                    <!-- BEGIN SIDEBAR MENU -->
                    <ul class="page-sidebar-menu  page-header-fixed " data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200" style="padding-top: 20px">
                    	<s:hasRole name="superAdmin">
							<c:forEach items="${allMenu}" var="m">
								<%@include file="menu-item.temp" %>
							</c:forEach>
						</s:hasRole>
						
						<s:lacksRole name="superAdmin">
							<c:forEach items="${allMenu}" var="m">
								<s:hasPermission name="${m.permissionSign }">								
									<%@include file="menu-item.temp" %>
								</s:hasPermission>
							</c:forEach>
						</s:lacksRole>
                    </ul>
                    <!-- END SIDEBAR MENU -->
                </div>
                <!-- END SIDEBAR -->
            </div>
            <!-- END SIDEBAR -->
            <!-- BEGIN CONTENT -->
            <div class="page-content-wrapper">
                <!-- BEGIN CONTENT BODY -->
                <div class="page-content">
                	<div class="page-content-body">
                	
                	</div>
                </div>
                <!-- END CONTENT BODY -->
            </div>
            <!-- END CONTENT -->
        </div>
        <!-- END CONTAINER -->
        <!-- BEGIN FOOTER -->
        <jsp:include page="footer.jsp"></jsp:include>
        <!-- END FOOTER -->
        <!--[if lt IE 9]>
		<script src="<%=resourcePath%>/assets/global/plugins/respond.min.js"></script>
		<script src="<%=resourcePath%>/assets/global/plugins/excanvas.min.js"></script> 
		<![endif]-->
        <!-- BEGIN CORE PLUGINS -->
        <script src="<%=resourcePath%>/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/js.cookie.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
        <script src="<%=resourcePath%>/assets/pages/scripts/components-date-time-pickers.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/treeview/js/jquery.ztree.core.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/treeview/js/jquery.ztree.excheck.js" type="text/javascript"></script>
        <!-- END CORE PLUGINS -->
        <!-- BEGIN PAGE LEVEL PLUGINS -->
        <script src="<%=resourcePath%>/assets/global/scripts/datatable.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/scripts/moment.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/scripts/moment-with-locales.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/datatables/datatables.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/jstree/dist/jstree.js"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/select2/js/select2.full.min.js" type="text/javascript"></script>
		<script src="<%=resourcePath%>/assets/global/plugins/typeahead/handlebars.min.js" type="text/javascript"></script>
		<script src="<%=resourcePath%>/assets/global/plugins/typeahead/typeahead.bundle.min.js" type="text/javascript"></script>
		<script src="<%=resourcePath%>/assets/customer/vuejs/vue.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/bootstrap-toastr/toastr.min.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
		<script src="<%=resourcePath%>/assets/global/plugins/jquery-validation/js/localization/messages_zh.js" type="text/javascript" charset="utf-8"></script>
		  <script src="<%=resourcePath%>/assets/global/plugins/bootstrap-colorpicker/js/bootstrap-colorpicker.js" type="text/javascript"></script>
        <script src="<%=resourcePath%>/assets/global/plugins/jquery-minicolors/jquery.minicolors.min.js" type="text/javascript"></script>
        <!-- END PAGE LEVEL PLUGINS -->
        
        <!-- BEGIN THEME GLOBAL SCRIPTS -->
        <script src="<%=resourcePath%>/assets/global/scripts/app.min.js" type="text/javascript"></script>
        <!-- END THEME GLOBAL SCRIPTS -->
        
        <!-- BEGIN THEME LAYOUT SCRIPTS -->
        <script src="<%=resourcePath%>/assets/layouts/layout/scripts/layout.js" type="text/javascript"></script>
		<!--	CUSTOMER SCRIPTS -->
		
        <script src="<%=resourcePath%>/assets/customer/datatable_lang_ch.js" type="text/javascript" charset="utf-8"></script>
		<script src="<%=resourcePath%>/assets/customer/dialog/customer_dialog.js" type="text/javascript" charset="utf-8"></script>
		<script src="<%=resourcePath%>/assets/customer/utils.js" type="text/javascript" charset="utf-8"></script>
        <script src="<%=resourcePath%>/assets/customer/vue-components.js?v=1" type="text/javascript" charset="utf-8"></script>
        <script src="<%=resourcePath%>/assets/customer/drag.js" type="text/javascript" charset="utf-8"></script>
		<script src="<%=resourcePath%>/assets/customer/controller.js" type="text/javascript" charset="utf-8"></script>

        <script src="<%=resourcePath%>/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.js" type="text/javascript"></script>

        <!--   wangEditor  富文本编辑器  -->
        <script type="text/javascript" src="<%=resourcePath%>/assets/global/plugins/wangEditor-2.1.22/js/wangEditor.js"></script>

        <!-- END THEME LAYOUT SCRIPTS -->
    </body>
	
	<script type="text/javascript">
		$("#updateUserInfo").click(function(){
			$.post("branduser/updatepage",function(result){
				var contentBody = $(".page-content .page-content-body");
				contentBody.html(result);
				App.initAjax();
			})
		})

        $("#updateManagerPwd").click(function(){
            $.post("branduser/updatemanagerpwd",function(result){
                var contentBody = $(".page-content .page-content-body");
                contentBody.html(result);
                App.initAjax();
            })
        })

        function format(str) {
            Date.prototype.format(str)
        }
	</script>
</html>