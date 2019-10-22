
####service方法名
#####brand端
###### BrandService(brand)
```
selectById/
selectBySign/
selectBrandBySetting/
selectByPrimaryKey/
```
###### ShopDetailService(brand)
```
selectByPrimaryKey/
selectByRestaurantId/
selectByBrandId/
selectByShopCity/
selectByCityOrName/
selectByCity/
selectOrderByIndex/
```
###### WechatConfigService(brand)
```
selectByBrandId/
selectById/
```

###### BrandSettingService(brand)
```
selectByBrandId/
```
###### ShareSettingService(brand)
```
selectByBrandId/
```
###### RewardSettingService(brand)
```
selectByBrandId/
```
###### WechatChargeConfigService(brand)
```
selectById/
```
###### WxServerConfigService(brand)
```
selectById/
```
###### ModuleListService(brand)
```
selectBrandHasModule/
```

#####shop端
###### ReceiptService(shop)
```
selectReceiptOrderList/
insertSelective/
```
###### ReceiptTitleService(shop)
```
selectOneList/
selectTypeList/
selectDefaultList/
insertSelective/
updateByPrimaryKeySelective/
```
###### AccountLogService(shop)
```
selectByShareMoneyCount/
selectByShareMoney/
```
###### AccountService(shop)
```
selectById/
selectAccountAndLogByCustomerId/
```
###### AdvertService(shop)
```
selectListByShopId/
```
###### AppraiseService(shop)
```
updateAndListAppraise/
selectDetailedById/
appraiseCount/
appraiseMonthCount/
saveAppraise/
addArticleLikes/
updatePhotoSquare/
selectDeatilByOrderId/
selectCustomerAllAppraise/
selectByCustomerCount/
selectByGoodAppraise/
selectById/
```
###### ArticleFamilyService(shop)
```
selectListByDistributionModeId/
```
###### ArticleService(shop)
```
selectById/
update/
selectListFull/
selectArticleList/
updateInitialsById/
selectListByShopIdRecommendCategory/
```
###### ChargeOrderService(shop)
```
chargeorderWxPaySuccess/
selectByCustomerIdAndBrandId/
createChargeOrder/
insert/
selectByCustomerIdNotChangeId/
withdrawals/
```
###### ChargeSettingService(shop)
```
selectListByShopId/
```
###### CouponService(shop)
```
selectById/
usedCouponBeforeByOrderId/
addRealTimeCoupon/
listCoupon/
listCouponUsed/
listCouponByStatus/
useCouponById/
```
###### CustomerService(shop)
```
selectById/
updateCustomer/
selectTelephoneList/
bindPhone/
unbindphone/
updateNewNoticeTime/
checkRegistered/
selectByShareCustomer/
update/
selectShareCustomerList/
login/
register/
selectByOpenIdInfo/
updateCustomer/
selectBySerialNumber/
```
###### NewCustomCouponService(shop)
```
selectRealTimeCoupon/
selectListByBrandId/
selectListByCouponType/
selectBirthCoupon/
giftCoupon/
```
###### NoticeService(shop)
```
selectListByShopId/
addNoticeHistory/
```
###### OrderItemService(shop)
```
listByOrderId/
selectById/
```
###### OrderPaymentItemService(shop)
```
selectByOrderId/
selectById/
insertByBeforePay/
```
###### OrderService(shop)
```
listOrder/
selectById/
selectReadyOrder/
pushOrder/
selectOrderStatesById/
getOrderInfo/
selectByParentId/
update/
selectPayBefore/
createOrder/
posDiscount/
repayOrder/
cancelOrder/
findCustomerNewOrder/
findCustomerNewPackage/
checkShop/
checkArticleCount/
setTableNumber/
selectByPrimaryKey/
payPrice/
useRedPrice/
refundPaymentByUnfinishedOrder/
afterPay/
customerByOrderForMyPage/
orderWxPaySuccess/
getLastOrderByTableNumber/
getChildItem/
fixedRefund/
alipayRefund/
```
###### PictureSliderService(shop)
```
selectListByShopId/
```
###### RedConfigService(shop)
```
selectListByShopId/
```
###### ShopCartService(shop)
```
listUserAndShop/
updateShopCart/
delMealItem/
selectByUuId/
delMealArticle/
clearShopCart/
```
###### ArticleAttrService(shop)
```
selectListByShopId/
```
###### ShowPhotoService(shop)
```
selectList/
selectById/
update/
```
###### ArticleRecommendService(shop)
```
getRecommentByArticleId/
```
###### GetNumberService(shop)
```
getWaitInfoByCustomerId/
selectById/
update/
```
###### ThirdService(shop)
```
orderAccept/
```
###### UnitService(shop)
```
getUnitByArticleid/
getUnitByArticleidWechat/
selectUnitDetail/
```
###### AppraiseFileService(shop)
```
appraiseFileList/
insert/
```
###### AppraiseCommentService(shop)
```
appraiseCommentList/
insertComment/
selectByCustomerCount/
```
###### AppraisePraiseService(shop)
```
appraisePraiseList/
selectByAppraiseIdCustomerId/
updateCancelPraise/
appraisePraiseList/
selectByCustomerCount/
```
###### TableQrcodeService(shop)
```
selectById/
```
###### CustomerDetailService(shop)
```
selectById/
insert/
update/
```
###### ArticleTopService(shop)
```
insert/
```
###### OrderRemarkService(shop)
```
getShopOrderRemark/
```
###### ExperienceService(shop)
```
selectListByShopId/
```
###### CustomerAddressService(shop)
```
selectOneList/
selectByPrimaryKey/
insertSelective/
updateByPrimaryKeySelective/
```
###### RecommendCategoryService(shop)
```
selectListSortShopId/
```
###### RedPacketService(shop)done
```
selectShareMoneyList/
```
