<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="tag-head.jsp" %>
<div class="page-header navbar navbar-fixed-top">
	<!-- BEGIN HEADER INNER -->
	<div class="page-header-inner ">
		<!-- BEGIN LOGO -->
		<div class="page-logo">
			<a href="javascript:(0)"> <img	src="<%=resourcePath%>/assets/pages/img/Resto+.png" width="110px" height="20px"	alt="logo" class="logo-default" style="margin-top: 15px;" />

            </a>
			<div class="menu-toggler sidebar-toggler">
                <%--<img src="http://app1.showapi.com/weather/icon/day/08.png">--%>
            </div>

		</div>
		<%--<div>--%>
			<%--<span style="color:#F00" >${wether_info.provinceName}/${wether_info.cityName}日期：${wether_info.dateTime} 星期：${wether_info.weekady} 天气:${wether_info.dayWeather}/${wether_info.dayTemperature}℃</span>--%>
			<%--<img src=${wether_info.dayWeatherPic}>--%>
		<%--</div>--%>

		<!-- END LOGO -->
		<!-- BEGIN RESPONSIVE MENU TOGGLER -->
		<a href="javascript:;" class="menu-toggler responsive-toggler"
			data-toggle="collapse" data-target=".navbar-collapse">
        </a>
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
						class="username username-hide-on-mobile"><span>${current_shop_name == null? current_brand_name : current_shop_name}：</span>
							<span class="namespan">${USER_INFO.username == null ? '超级管理' : USER_INFO.username} </span></span> <i class="fa fa-angle-down"></i>
				</a>
					<ul class="dropdown-menu dropdown-menu-default">
						<li id="updateUserInfo"><a href="javascript:;"> <i
								class="icon-user"></i>个人信息
						</a></li>
						<li id="updateManagerPwd"><a href="javascript:;"> <i
								class="icon-user"></i>操作口令
						</a></li>
						<li class="divider"></li>
						<li><a href="branduser/logout"> <i class="icon-key"></i>
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