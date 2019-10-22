<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="full-height">
    <div>
        <div class="swiper-container banner">
            <div class="swiper-wrapper">
                <div class="swiper-slide" v-for="p in pictureList">
                    <img :src="p.pictureUrl" class="banner"/>
                </div>
            </div>
        </div>
        <div class="weui_cells margin-top-0">
            <div class=" weui_cell">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>{{shop.name}}</p>
                </div>
                <div class="weui_cell_ft">
                    <start-span :level="monthStar"></start-span>
                    <span class="c-red">{{monthScore}}分</span>
                </div>
            </div>

            <div class="weui_cell weui_cells_access" @click="openshoplocation">
                <div class="weui_cell_hd">
                    <i class="icon-map-marker"></i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>{{shop.address}}</p>
                </div>
                <div class="weui_cell_ft"></div>
            </div>
            <a class="weui_cell weui_cells_access" href="tel:{{shop.phone}}">
                <div class="weui_cell_hd">
                    <i class="icon-phone"></i>
                </div>
                <div class="weui_c	ell_bd weui_cell_primary">
                    <p>营业时间 : {{new Date(shop.openTime).format("hh:mm")}} - {{new
                        Date(shop.closeTime).format("hh:mm")}}</p>
                </div>
                <div class="weui_cell_ft"></div>
            </a>
        </div>

        <div class="weui_cells weui_cells_access">
            <div class="weui_cell redbox" v-if="noticelist.length>0"
                 @click="this.$dispatch('show-notice-list',noticelist)">
                <div class="weui_cell_hd"></div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>点击查看最新公告</p>
                </div>
                <div class="weui_cell_ft"></div>
            </div>
        </div>
        <div class="weui_cells description" v-if="advert.description">{{{advert.description}}}</div>
        <div class="weui_cells weui_cells_access">
            <div class="weui_cell" @click="showFilter">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>
                        全部评价<span id="appraise-count-span">( {{appraiseCount.COUNT}}条  {{appraiseCount.AVG_SCORE.toFixed(2)}}分 )</span>
                    </p>
                </div>
            </div>
            <ul class="link comment">
                <li class="appraise-item" v-for="a in appraiseList">
	                    <div class="reviewListPhotoBox" :class="{badappraise:a.level<=4}">
	                        <img :src="a.pictureUrl" v-if="a.level==5"/>
	                    </div>
	                    <div class="reviewListInfo">
	                        <div class="reviewListInfoTitle">
	                            <div class="avatar">
	                                <img :src="a.headPhoto">
	                            </div>
	                            <p >
	                                <span v-if="a.level==5">{{a.nickName.substring(0,3)}}爱上了{{a.feedback}}</span>
	                                <span v-if="a.level<=4">{{a.nickName.substring(0,3)}}不满意{{a.feedback}}</span>
	                            </p>
	                            <div class="comment-rst">
	                                <span>{{a.createTime}}</span>
	                                <start-span :level="a.level"
	                                         v-bind:class="{bad:a.level<=4,good:a.level==5}"></start-span>
	                            </div>
	                        </div>
	                        <div class="reviewListInfoContent">
	                            <p>{{a.content}}</p>
	                        </div>
	                    </div>
                </li>
            </ul>
            <div class="weui_cell font-mini" v-if="isLoad">
                <p><i class="icon-spinner icon-spin"></i>正在加载中...</p>
            </div>
            <div class="weui_cell font-mini" v-if="isOver">
                <p><i class="icon-ok"></i>已经全部加载完成</p>
            </div>
        </div>
    </div>
    <appraise-filter-dialog :show.sync="appraiseFilterDialog.show" :countlist="countList"
                            @filter="reloadAppraise"></appraise-filter-dialog>
</div>