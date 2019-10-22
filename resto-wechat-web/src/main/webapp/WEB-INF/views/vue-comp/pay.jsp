<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="full-height">
	<div class="weui_cell weui_cells_access menuLogo" @click="switchShop">  
		<div class="weui_cell_bd weui_cell_primary">
			<p>[支付] <span>{{shop.name}}</span></p>
		</div>
		<div class="weui_cell_ft"></div>
    </div>
    <button class="WeChat_sweep"  @click="openScan">扫一扫</button>
    <p class="WeChat_registration" v-if="show">微信粉丝注册送xx元红包</p>
    <input type="button" value="立即注册" @click="register_now"/>
</div>