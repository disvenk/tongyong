<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="tag-head.jsp" %>
<div class="page-header navbar navbar-fixed-top">
	<!-- BEGIN HEADER INNER -->
	<div class="page-header-inner ">
		<!-- BEGIN LOGO -->
		<div class="page-logo">
			<a href="javascript:(0)"> <img	src="<%=resourcePath%>/assets/pages/img/Resto+.png" width="110px" height="20px"	alt="logo" class="logo-default" style="margin-top: 15px;" />
			</a>
			<div class="menu-toggler sidebar-toggler"></div>
		</div>
		<!-- END LOGO -->
		<!-- BEGIN RESPONSIVE MENU TOGGLER -->
		<a href="javascript:;" class="menu-toggler responsive-toggler"
			data-toggle="collapse" data-target=".navbar-collapse"> </a>
		<!-- END RESPONSIVE MENU TOGGLER -->
		<!-- BEGIN TOP NAVIGATION MENU -->
		<div class="top-menu">
			<ul class="nav navbar-nav pull-right">
				<!-- BEGIN USER LOGIN DROPDOWN -->
				<!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
				<s:hasRole name="superAdmin">
					<li class="dropdown dropdown-extended " id="menu-manager"><a
						href="javascript:;" class="dropdown-toggle"> <i
							class="fa fa-list"></i> <span> 菜单管理 </span>
					</a></li>
				</s:hasRole>
				<li class="dropdown dropdown-user"><a href="javascript:;"
					class="dropdown-toggle" data-toggle="dropdown"
					data-hover="dropdown" data-close-others="true"> <span
						class="username username-hide-on-mobile"><span>登录用户</span>
							<span class="namespan">${userinfo.username } </span></span> <i class="fa fa-angle-down"></i>
				</a>
					<ul class="dropdown-menu dropdown-menu-default">
						<li><a href="javascript:;"> <i
								class="icon-user"></i> My Profile
						</a></li>
						<li class="divider"></li>
						<li><a href="user/logout"> <i class="icon-key"></i>
								退出
						</a></li>
					</ul></li>
				<!-- END USER LOGIN DROPDOWN -->
			</ul>
		</div>
		<!-- END TOP NAVIGATION MENU -->
	</div>
	<!-- END HEADER INNER -->
</div>