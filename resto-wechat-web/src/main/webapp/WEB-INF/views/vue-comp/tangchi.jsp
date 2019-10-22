<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="full-height">
    <div class="weui_cell weui_cells_access menuLogo" @click="switchShop">
        <div class="weui_cell_bd weui_cell_primary">
            <p>[切换门店] <span>{{shop.name}}</span></p>
        </div>
        <div class="weui_cell_ft">
        </div>
    </div>
    <div class="article-container">
        <div class="full-height article-family-list">
            <ul class="article-family-ul">
                <li v-for="f in familyList" v-if="f.articles.length" :class="{active:f.id==currentFamily.id}"
                    @click="changeCurrentFamily(f)">
                    {{f.name}}
                </li>
            </ul>
        </div>

        <div class="full-height article-list-wapper">
            <div class="article-list">
                <div class="article-family-group" v-for="f in familyList" v-if="f.articles.length"
                     :data-family-id="f.id">
                    <!-- 					<div v-for="a in f.articles" class="art-item"  :class="{'has-fans':hasFansPrice(a)||a.fansPrice&&a.articlePrices.length==0}" style="background:url({{a.photoSmall}}) no-repeat;background-size: 100% 100%;" @click.stop="showAction(a)"> -->
                    <div v-for="a in f.articles" class="art-item"
                         :class="{'has-fans':a.fansPrice||a.fansPrice&&a.articlePrices.length==0}"
                         style="background:url({{a.photoSmall}}) no-repeat;background-size: 100% 100%;"
                         @click.stop="showAction(a)">

                        <p>
                            <span class="art-name">{{a.name}}</span>
                            <span class="art-price price"> {{getPrice(a)}} / {{a.unit}}</span>
                        </p>
                        <div v-if="a.fansPrice">
                            <span class="fans-price-item">粉丝价:{{a.fansPrice}}元</span>
                        </div>
                        <%--<div>--%>
                        <%--<span class="fans-price-item">粉丝价:{{a.fansPrice}}元</span>--%>
                        <%--</div>--%>
                        <%--<div v-if="hasFansPrice(a)">--%>
                        <%--<span class="fans-price-item">粉丝价:{{hasFansPrice(a)}}元</span>--%>
                        <%--</div>--%>
                        <div class="numberControl" :style="{color:a.controlColor}"
                             v-if="a.articlePrices.length==0&&a.articleType==1&&a.recommendId&&a.recommendId != ''">
                            <span class="plus-span" @click.stop="showAction(a)" v-else><i class="icon iconfont plus">
                                &#xe6cd;</i></span>
                        </div>
                        <article-operator :style="{color:a.controlColor}"
                                          v-if="a.articlePrices.length==0&&a.articleType==1&&!a.recommendId"
                                          :article.sync="a"
                                          @add="addToShopCart" @cut="cutToShopCart"></article-operator>
                        <div class="numberControl" :style="{color:a.controlColor}" v-else>
                            <span v-if="a.isEmpty">已售罄</span>
                            <span class="plus-span" @click.stop="showAction(a)" v-else><i class="icon iconfont plus">
                                &#xe6cd;</i></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="menu-cart" @click="showShopCart">
            <div class="show-shop-cart">
                <a class="getRedPacket"></a>
                <div class="weui_cell_primary">
                    <span><i class="art-count">{{shopCart.totalNumber}}</i> | <span class="price art-total-money">{{shopCart.totalPrice.toFixed(2)}}</span></span>
                </div>
                <div class="text-center">
                    <span>点击查看购物车</span>
                </div>
                <div class="weui_cell_primary">
                    <div class="pull-right">
                        <button type="button" class="weui_btn weui_btn_mini weui_btn_main order-ok"
                                @click.stop="showCreateOrder(true)">点好了
                        </button>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <div class="weui_dialog_alert" v-if="isCreateOrder">
        <div class="weui_dialog ex_dialog">
            <div class="order-cart-list full-height">
                <div>
                    <div class="shop-cart-items">
                        <div class="weui_cells margin-top-0">
                            <div class="weui_cell order-item" v-for="item  in shopCart.items">
                                <div class="weui_cell_bd weui_cell_primary">
                                    <p class="cart-item-name"
                                       :class="{'colorchange':item.current_working_stock<item.data.number}">
                                        {{item.name}}</p>
                                    <p v-if="item.type==3" class="mini">
				                    	<span v-for="attr in item.data.mealAttrs">
				                    		{{attr.currentItem.name}}&nbsp;
				                    	</span>
                                    </p>
                                    <p class="price mini" v-else>{{item.data.realPrice}}</p>
                                </div>
                                <div class="weui_cell_ft">
                                    <article-operator :article.sync="item.data" @cut="cutToShopCart"
                                                      @add="addToShopCart"></article-operator>
                                </div>
                            </div>
                            <div class="weui_cell">
                                <div class="weui_cell_bd weui_cell_primary">
                                    <p class="font-16">消费总额</p>
                                </div>
                                <div class="weui_cell_ft">
                                    <span class="price font-blod"><span class="totalPrice">{{shopCart.totalPrice.toFixed(2)}}</span></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="weui_cells total-price" style="margin-top:-1px;">

                    </div>
                    <div class="order-operator-div">
                        <div class="weui_cells black-right-icon">
                            <div class="weui_cell weui_cell_select weui_select_after" v-if='canUseCoupon.length'>
                                <div class="weui_cell_hd">
                                    可用优惠
                                </div>
                                <div class="weui_cell_bd weui_cell_primary">
                                    <select class="weui_select" name="couponId" v-model="couponId">
                                        <option v-for="coupon in canUseCoupon" :value="coupon.id"
                                                :data-value="coupon.value" v-bind="{selected:$index==0}">
                                            {{coupon.name}} ￥{{coupon.value}}元
                                        </option>
                                    </select>
                                </div>
                            </div>

                            <div class="weui_cell weui_cell_switch" v-if="useCouponAmount>0">
                                <div class="weui_cell_hd weui_cell_primary">可用余额 <span class="price">{{customer.account}}</span>
                                </div>
                                <div class="weui_cell_ft">
				                    <span class="weui_switch useaccount" :class="{'checked':checkAccount}"
                                          @click="useChecked">
				                    </span>
                                </div>
                            </div>

                            <charge-rules :chargelist="chargeList"></charge-rules>

                            <div class="weui_cell">
                                <div class="weui_cell_bd weui_cell_primary">
                                    <p>实付金额</p>
                                </div>
                                <div class="weui_cell_ft">
                                    <span class="price sale">{{finalAmount.toFixed(2)}}</span>
                                </div>
                            </div>
                            <div class="weui_cell weui_cells_access " @click="closeCreateOrder">
                                <div class="weui_cell_bd weui_cell_primary">
                                    <p>继续点餐 </p>
                                </div>
                                <div class="weui_cell_ft"></div>
                            </div>
                        </div>
                        <div class="weui_btn_area padding-bottom">
                            <button type="button" class="weui_btn weui_btn_main" @click.stop="createOrder">买单</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div class="weui_dialog_alert" v-if="isShowShopcart">
        <div class="weui_mask pop_up" @click="closeShopCart"></div>
        <div class="weui_dialog big-dialog">
            <div class="full-height">
                <div class="shopcart-list" v-if="shopCart.items.length">
                    <div class="weui_cells" style="margin-top:0">
                        <div class="weui_cell order-descriptionitem" v-for="item  in shopCart.items">
                            <div class="weui_cell_bd weui_cell_primary">
                                <p class="cart-item-name">{{item.name}}</p>
                                <p v-if="item.type==3" class="mini">
			                    	<span v-for="attr in item.data.mealAttrs">

			                    		<label v-for="name in attr.currentItem">
                                            {{name.name}}&nbsp;
                                        </label>
			                    	</span>
                                </p>
                                <p class="price mini" v-else>{{item.data.realPrice}}</p>
                            </div>
                            <div class="weui_cell_ft">
                                <div v-if="item.type==3" class="numberControl">
                                    <span @click="cutToShopCart(item.data)" class="cut-span"><i
                                            class="icon iconfont minus">&#xe751;</i></span>
                                    <span class="art-number">{{item.data.number}}</span>
                                    <span @click="showAction(item.data)" class="meals-edit plus-span"><i
                                            class="icon icon-pencil"></i></span>
                                </div>
                                <%--<div v-if="item.type==5" class="numberControl">--%>
                                    <%--<span @click="cutToShopCartNew(item.data)" class="cut-span"><i--%>
                                            <%--class="icon iconfont minus">&#xe751;</i></span>--%>
                                    <%--<span class="art-number">{{item.data.number}}</span>--%>
                                    <%--<span @click="showActionNew(item.data)" class="meals-edit plus-span"><i--%>
                                            <%--class="icon icon-pencil"></i></span>--%>
                                <%--</div>--%>
                                <article-operator :article.sync="item.data" @cut="cutToShopCart" @add="addToShopCart"
                                                  v-else></article-operator>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="shopcart-list empty" v-if="!shopCart.items.length">
                    <p><i class="icon-shopping-cart"></i></p>
                    <p>购物车暂时为空</p>
                </div>
                <div class="bottom-button">
                    <div class="weui_dialog_ft margin-top-0 no-border-top">
                        <a class="weui_btn_dialog primary left" @click="clearShopCart">清空</a>
                        <a class="weui_btn_dialog primary right" @click="closeShopCartAndShowCreate">点好了</a>
                    </div>
                </div>
            </div>
            <div class="dialog-close" @click="closeShopCart"><i class="icon-remove-sign"></i></div>
        </div>
    </div>

    <div class="weui_dialog_alert" v-if="remindDialog.show">
        <div class="weui_mask pop_up" @click="closeRemindDialog"></div>
        <div class="weui_dialog middle-dialog">
            <div class="full-height">
                <div class="remind-title">
                    <p class="text-left padding-10">请问您是否需要:</p>
                </div>
                <div class="remind-list">
                    <div class="weui_cells" style="margin-top:0">
                        <div class="weui_cell order-item" v-for="item  in remindDialog.articles">
                            <div class="weui_cell_bd weui_cell_primary">
                                <p class="cart-item-name" v-if="item.type==1">{{item.name}}</p>
                                <p class="cart-item-name" v-else>{{item.article.name+item.name}}</p>
                                <p class="price mini">{{item.realPrice}}</p>
                            </div>
                            <div class="weui_cell_ft">
                                <article-operator :article.sync="item" @cut="cutToShopCart"
                                                  @add="addToShopCart"></article-operator>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="bottom-button">
                    <div class="weui_dialog_ft margin-top-0 no-border-top">
                        <a class="weui_btn_dialog primary" @click="remindDialogOk">点好了</a>
                    </div>
                </div>
            </div>
            <div class="dialog-close" @click="closeRemindDialog"><i class="icon-remove-sign"></i></div>
        </div>
    </div>


    <div class="weui_dialog_alert" v-if="currentArticle&&currentArticle.articleType==1||currentArticle.isEmpty">
        <div class="weui_mask pop_up" @click="closeAction"></div>
        <div class="weui_dialog dish_details"  :class="{'mult-price':unitArr.length>0}">
            <div class="full-height">
                <div class="weui_dialog_bd " v-if="currentArticle.showBig">
                    <img class="art-image" :src="currentArticle.photoSmall||'assets/img/resto.png'"/>
	            	<span class="article-likes">
	            		<i class="icon icon-star"></i>
	            		{{currentArticle.likes}}
	            	</span>
                </div>
                <div class="weui_article article-desc" v-if="currentArticle.showDesc">
                    <div>
                        <h3 class="article-name">{{currentArticle.name}}</h3>
                        <p class="article-desc">{{{currentArticle.description}}}</p>

                    </div>
                    <div class="dish_size_bg text-left padding-0 "
                         v-if="unitArr.length==0&&currentArticle.recommendId&&currentArticle.recommendId != ''">
                        <div class="weui_cells article-unit-list no-border-top no-border-bottom" style="margin-top:0">
                            <div class="dish_size_bg meals-wrap " style="margin-top: 5px;line-height: 30px">
                                <div class="meal-attr-items">
                                    <div class="meal-items">
                                        <div class="form-group">
                                            <p><span>推荐配餐</span></p>
                                        </div>

                                        <div class="clearfix"></div>
                                    </div>
                                </div>

                            </div>

                        </div>
                        <div class="dish_size_bg meals-wr" style="margin-top: -5px;">
                            <div class="items-name">
                                <div class="meal-item-div" style="margin-top: 5px"
                                     v-for="recommend in recommendList.items" v-if="recommend.currentWorkingStock>0">
	            					<span class="item-name" style="margin: auto" :class="{select:recommend.count!=0}"
                                          @click="selectRecommend(recommend)">
	            						{{recommend.articleName}} <span>/￥{{recommend.price}}</span>
	            					</span>
                                    <span v-if="recommend.count >0">X{{recommend.count}}</span>
                                </div>

                                <div class="clearfix"></div>
                            </div>
                        </div>
                        <div class="clearfix"></div>


                        <div class="bottom-button">
                            <div class="weui_dialog_ft margin-top-0 no-border-top"
                                 style="font-size:15px;">

                                <p style="padding-left: 10px;margin-top: 5px"><span style="">￥{{totalMoney}}</span>
                                    <br/><span
                                            style="font-size: 13px;">（配餐最多可选{{recommendList.count}}份）</span></p>
                                <a class="weui_btn_dialog primary" @click.stop="addToNewShopCart"
                                   style="margin-right:-5px;line-height: 40px;font-size: 18px">确定</a>

                            </div>
                        </div>


                    </div>
                </div>

                <%--<div class="dish_size_bg text-left padding-0 " v-if="unitArr.length>0">--%>
                    <%--<div class="weui_cells article-unit-list no-border-top no-border-bottom" style="margin-top:0">--%>
                        <%--<div class="weui_cell" v-for="item  in currentArticle.articlePrices"--%>
                             <%--v-if="item.currentWorkingStock && !unitList.item  ">--%>
                            <%--<div class="weui_cell_bd weui_cell_primary">--%>
                                <%--<p class="cart-item-name">{{currentArticle.name}}{{item.name}}</p>--%>
                                <%--<p class="font-mini">--%>
                                    <%--<template v-if="item.fansPrice">--%>
                                        <%--<span class="price delete">{{item.price}}元</span>&nbsp;&nbsp;--%>
                                        <%--<span class="price sale">{{item.fansPrice}}元</span>--%>
                                    <%--</template>--%>
                                    <%--<span class="price" v-else>{{item.price}}元</span>--%>
                                <%--</p>--%>
                            <%--</div>--%>
                            <%--<div class="weui_cell_ft">--%>
                                <%--<article-operator :article.sync="item" @cut="cutToShopCart"--%>
                                                  <%--@add="addToShopCart"></article-operator>--%>
                            <%--</div>--%>


                            <%--&lt;%&ndash;<div class="dish_size_bg meals-wrap"&ndash;%&gt;--%>
                                 <%--&lt;%&ndash;v-if="currentArticle.recommendId && currentArticle.recommendId != ''">&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<div class="meal-attr-items">&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;<div class="meal-items">&ndash;%&gt;--%>
                                        <%--&lt;%&ndash;<div class="form-group">&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;<p><span>推荐配餐</span></p>&ndash;%&gt;--%>
                                        <%--&lt;%&ndash;</div>&ndash;%&gt;--%>

                                        <%--&lt;%&ndash;<div class="clearfix"></div>&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>

                            <%--&lt;%&ndash;<div class="dish_size_bg meals-wrap"&ndash;%&gt;--%>
                                 <%--&lt;%&ndash;v-if="currentArticle.recommendId && currentArticle.recommendId != ''">&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<div class="items-name">&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;<div class="meal-item-div" style="margin-left: 10px"&ndash;%&gt;--%>
                                         <%--&lt;%&ndash;v-for="recommend in recommendList.items"  v-if="recommend.currentWorkingStock>0">&ndash;%&gt;--%>
	            					<%--&lt;%&ndash;<span class="item-name" :class="{select:recommend.count!=0}"&ndash;%&gt;--%>
                                          <%--&lt;%&ndash;@click="selectRecommend(recommend)" >&ndash;%&gt;--%>
	            						<%--&lt;%&ndash;{{recommend.articleName}} <span>/￥{{recommend.price}}</span>&ndash;%&gt;--%>
	            					<%--&lt;%&ndash;</span>&ndash;%&gt;--%>
                                        <%--&lt;%&ndash;<span v-if="recommend.count >0">X{{recommend.count}}</span>&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;</div>&ndash;%&gt;--%>

                                    <%--&lt;%&ndash;<div class="clearfix"></div>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                            <%--<div class="clearfix"></div>--%>


                            <%--<div class="bottom-button">--%>
                                <%--<div class="weui_dialog_ft margin-top-0 no-border-top"--%>
                                     <%--style="font-size:15px;line-height: 30px">--%>

                                    <%--<p style="padding-left: 10px"><span style="">￥111</span> <br/><span--%>
                                            <%--style="font-size: 13px"  v-if="currentArticle.recommendId && currentArticle.recommendId != ''">（配餐最多可选{{recommendList.count}}份）</span></p>--%>
                                    <%--<a class="weui_btn_dialog primary" @click.stop="addToNewShopCart"--%>
                                       <%--style="line-height: 40px;font-size: 18px">确定</a>--%>

                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>

                <div class="dish_size_bg text-left padding-0 " v-if="unitArr.length>0">
                    <div class="weui_cells article-unit-list no-border-top no-border-bottom" style="margin-top:0">
                        <div class="weui_cell" v-for="arr  in unitArr">
                            <div class="form-group">
                                <p><span>{{arr.name}}</span></p>
                            </div>
                            <div class="items-name">
                                <div class="meal-item-div" v-for="item in arr.details">
	            					<span class="item-name" :class="{select:item.click==true}"
                                          @click="selectUnitItem(item,arr)">
	            						{{item.name}} <span >+{{item.price}}元</span>
	            					</span>
                                </div>
                                <div class="clearfix"></div>
                            </div>





                            <div class="clearfix"></div>



                        </div>
                        <div >
                            <div class="weui_dialog_ft margin-top-0 no-border-top"
                                 style="font-size:15px;line-height: 30px">

                                <p style="padding-left: 10px"><span style="">￥{{totalMoney}}</span> <br/><span
                                        style="font-size: 13px" v-if="currentArticle.recommendId && currentArticle.recommendId != ''" >（配餐最多可选{{recommendList.count}}份）</span></p>
                                <a class="weui_btn_dialog primary" @click.stop="addToNewShopCart"
                                   style="line-height: 40px;font-size: 18px">确定</a>

                            </div>
                        </div>

                    </div>
                </div>


                <%--<div class="clearifx"  ></div>--%>
                <div v-else class="bottom-button" v-if="!currentArticle.recommendId ">
                    <div class="pull-right" style="margin-right:10px;">
                        <article-operator :article.sync="currentArticle" @cut="cutToShopCart"
                                          @add="addToShopCart"></article-operator>
                    </div>
                </div>
            </div>

            <div class="dialog-close" @click="closeAction"><i class="icon-remove-sign"></i></div>
        </div>
    </div>


    <div class="weui_dialog_alert sencond_mask"
         v-if="currentArticle&&currentArticle.articleType==2&&!currentArticle.isEmpty">
        <div class="weui_mask pop_up" @click="closeAction"></div>
        <div class="weui_dialog dish_details meals-dialog">
            <div class="full-height">
                <div class="form-group">
                    <p><span class="meals-name">{{currentArticle.name}}</span></p>
                </div>
                <div class="weui_dialog_bd " v-if="currentArticle.showBig">
                    <!-- 	            	<img class="art-image" :src="currentArticle.photoSmall"/> -->
                    <!-- 	            	<span class="article-likes"> -->
                    <!-- 	            		<i class="icon icon-star"></i> -->
                    <!-- 	            		{{currentArticle.likes}} -->
                    <!-- 	            	</span> -->
                    <div class="meal-div">
                        <div class="meal-attr-div" v-for="attr in currentArticle.mealAttrs" @click="moveToAttr(attr)"
                             :class="{'new-attr':$index>0&&$index%3==0}">
                            <div class="meal-item-img">
                                <img :src="attr.currentItem.photoSmall" alt=""/>
                            </div>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>
                <div class="dish_size_bg meals-wrap">
                    <div class="meal-attr-items">
                        <div class="meal-items" v-for="attr in currentArticle.mealAttrs |orderBy 'sort' "
                             :data-attr-id="attr.id">
                            <div class="form-group">
                                <p><span>{{attr.name}}</span></p>
                            </div>
                            <div class="items-name" v-if="attr.choiceType == 0">
                                <div class="meal-item-div" v-for="item in attr.mealItems">
	            					<span class="item-name" :class="{select:item.click}"
                                          @click="selectMealItem(item,attr)">
	            						{{item.name}} <span v-if="item.priceDif>0">+{{item.priceDif}}元</span>
	            					</span>
                                </div>
                                <div class="clearfix"></div>
                            </div>

                            <div class="items-name" v-if="attr.choiceType == 1">
                                <div class="meal-item-div" v-for="item in attr.mealItems">
	            					<span class="item-name" :class="{select:item.click}"
                                          @click="selectMealItem(item,attr)">
	            						{{item.name}} <span v-if="item.priceDif>0">+{{item.priceDif}}元</span>
	            					</span>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="bottom-button">
                    <div class="weui_dialog_ft margin-top-0 no-border-top">
                        <a class="meals-price price">{{getMealsReadPrice(currentArticle)}}/{{currentArticle.unit}}</a>
                        <a class="weui_btn_dialog primary" v-else @click="saveMeals(currentArticle)">确定</a>
                    </div>
                </div>
            </div>
            <div class="dialog-close" @click="closeAction"><i class="icon-remove-sign"></i></div>
        </div>
    </div>

    <choice-mode-dialog :show.sync="choiceModeDialog.show"></choice-mode-dialog>

</div>