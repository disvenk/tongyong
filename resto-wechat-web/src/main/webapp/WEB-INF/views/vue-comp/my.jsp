<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="full-height">
	<div class="userCenter" >
   		<div class="weui_media_box weui_media_appmsg my-info">
            <div class="weui_media_hd">
                <img class="weui_media_appmsg_thumb" :src="customer.headPhoto" >
            </div>
            <div class="weui_media_bd">
                <h4 class="weui_media_title">{{customer.nickname}}</h4>
                <p class="weui_media_desc">手机号:{{customer.telephone||"未绑定"}}</p>
            </div>
        </div>
	    <div class="weui_cells weui_cells_access">
	         <a class="weui_cell"  @click="this.$dispatch('open-iframe','customer/informationAccount')">
	             <div class="weui_cell_hd"></div>
	             <div class="weui_cell_bd weui_cell_primary">
	                 <p>账户余额</p>
	             </div>
	             <div class="weui_cell_ft">
	             	<span class="price">{{customer.account.toFixed(2)}}</span>
	             </div>
	         </a>
	         <charge-rules :chargelist="chargeList"></charge-rules>
	         <a class="weui_cell" @click="this.$dispatch('open-iframe','customer/informationCoupon')" >
	             <div class="weui_cell_hd"></div>
	             <div class="weui_cell_bd weui_cell_primary">
	                 <p>我的优惠劵</p>
	             </div>
	             <div class="weui_cell_ft"></div>
	         </a>
	     </div>
	     
	     
	     <div class="order-list-mini">
	     	<div class="weui_cells">
	            <div class="weui_cell">
	                <div class="weui_cell_bd weui_cell_primary">
	                    <p>我的订单</p>
	                </div>
	            </div>


	            <order-mini-detailed v-for="order in orderlist" :order="order"></order-mini-detailed>
			    <div class="weui_cell font-mini" v-if="load">
					<p><i class="icon-spinner icon-spin"></i>正在加载中...</p>
				</div>
				<div class="weui_cell font-mini" v-if="over">
					<p><i class="icon-ok"></i>已经全部加载完成</p>
				</div>
	        </div>
	     </div>
	</div>
</div>