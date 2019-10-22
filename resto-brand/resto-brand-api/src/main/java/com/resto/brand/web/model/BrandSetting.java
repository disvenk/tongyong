package com.resto.brand.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BrandSetting implements Serializable{
    private String id;

    private String smsSign;

    private BigDecimal appraiseMinMoney;

    private String customerRegisterTitle;

    private String wechatWelcomeImg;

    private String wechatWelcomeTitle;

    private String wechatWelcomeUrl;

    private String memberCardUrl;

    private String wechatWelcomeContent;

    private String wechatBrandName;

    private String wechatHomeName;

    private String wechatTangshiName;

    private String wechatMyName;

    private String wechatWaimaiName;

    private String wechatCustomoStyle;

    private Integer autoConfirmTime;
    
    //红包logo
    private String redPackageLogo;
    
    //设定多少秒之后不能加菜
    private Integer closeContinueTime;

    //设定延迟获得评论红包的时间
    private Integer delayAppraiseMoneyTime;
    
    //是否需要选择配送模式默认选择否
    private Byte isChoiceMode;

    //第一次登入是否需要红包弹窗提醒
    private Boolean autoAlertAppraise;

    private Integer openArticleLibrary;

    //好评的最少字数
    private Integer goodAppraiseLength;
    //差评最少字数
    private Integer badAppraiseLength;

    //打印连接次数
    private Integer reconnectTimes;

    //打印连接间隔时间
    private Integer reconnectSecond;

    //工作日库存
    private Integer stockWorkingDay;

    //周末库存
    private Integer stockWeekend;

    //是否自动打印总单
    private Byte autoPrintTotal;

    private Byte geekPosPrice;

    //支付宝支付
    private Integer aliPay;

    //支付宝应用id
    private String aliAppId;

    //支付宝私钥
    private String aliPrivateKey;

    //支付宝公钥
    private String aliPublicKey;

    //支付宝合作伙伴id
    private String aliSellerId;

    //销售产品码
    private String aliProductCode;

    //是否开启支付后打印总单
    private Integer isPrintPayAfter;

    private Integer isOptionalPhoto;

    private Integer isMealFee;
    
    //品牌标语
    private String slogan;
    
    //等位提示
    private String queueNotice;

    //微信充值账户配置
    private WechatChargeConfig wechatChargeConfig;

    private String exportPassword;

    //是否发送优惠券到期提醒的短信 0不发 1发
    private Integer isSendCouponMsg;

    //是否开启pos加菜功能
    private Integer isPosPlus;

    //是否开启银联支付
    private Integer openUnionPay;

    //是否开启现金支付
    private Integer openMoneyPay;

    //是否开启第三方对接接口
    private  Integer openThirdInterface;
    //第三方接口对接的appId
    private String appid;

    private Integer recommendTime;

    //第三方接口 用来定位数据库
    private  String brandId;

    //是否开启美团闪惠支付
    private Integer openShanhuiPay;

    private Integer openOrderRemark;

    //是否开启推荐餐包功能
    private Integer recommendArticle;

    //是否开启菜品推荐类型功能
    private Integer recommendCategory;

    //是否启用配餐包
    private Integer isUseRecommend;

    //套餐出单方式
    private Integer printType;

    //等位红包是否开启
    private Integer waitRedEnvelope;

    private BigDecimal baseMoney;

    //是否启用服务费
    private Integer isUseServicePrice;

    private BigDecimal highMoney;

    private String bizUrl;

    private String switchMode;

    //是否开启额外费用
    private Integer isAllowExtraPrice;

    //是否开启第三方外卖平台
    private Integer isOpenOutFood;

    private List<String> platformName;

    private String wechatChargeConfigId;

    private Integer isSub;

    private Integer integralPay;

    private Integer posOpenTable;

    private Integer openPosCharge;

    //进入页面是否需要开启未关注进入关注提示页面
    private Integer intoShopSubscribe;

    //领取红包是否开启必须关注才可以领取
    private Integer allowAppraiseSubscribe;

    //品牌关注二维码
    private String qrCodeBrand;

    //是否开启andriod 电视叫号apk
    private Integer openAndriodApk;

    //电视叫号   1 友盟    2 lmx那套
    private Integer callTvType;

    //饿了吗用户key
    private String consumerKey;
    //饿了吗用户secret
    private String consumerSecret;

    //显示品牌名称
    private String brandName;

    private Integer openShoplist;

	private Integer openPosReminder;

    private Integer openHttps;

    private Integer isOpenScm;

    //是否开启发票管理
    private Integer receiptSwitch;

    // yz 2017-07-17 计费系统 添加 品牌账户
    private Integer openBrandAccount;

    private String brandAccountId;

    private Integer openBonus;

    private Integer smsPushGiftCoupons;

    private Integer wechatPushGiftCoupons;

    private Integer openPosDiscount;

    private Integer openManyCustomerOrder;

    private String loadingTextColor;

    private String loadingLogo;

    private String loadingBackground;

    private Integer orderBefore;

    private Integer openTicket;

    private Integer openCompulsoryRegister;

    private Integer openCompulsoryRecharge;

    private Integer openRpay;

    private Integer openProductCoupon;

    private Integer openSmallProgramPush;

    private String productCouponPushUrl;

    private Integer openAudit;

    private String shareText;

    private Integer consumptionRebate;

    //微信模板消息控制项
    private Integer templateEdition;

    //开通微信消息模板升级版,是否发送短信
    private Integer messageSwitch;

    //评价体系控制项
    private Integer appraiseEdition;

    private Integer openGroupBuy;

    private Integer openCashCouponBuy;

    //是否开启转台功能
    private Integer turntable;

    //优惠券cd
    private Integer couponCD;

    private Integer commentTime;

    private Integer openCommentRecommend;

    private Integer isPushSms;

    private Integer openAutoPermission;

    public Integer getOpenAutoPermission() {
        return openAutoPermission;
    }

    public void setOpenAutoPermission(Integer openAutoPermission) {
        this.openAutoPermission = openAutoPermission;
    }

    public Integer getOpenArticleLibrary() {
        return openArticleLibrary;
    }

    public void setOpenArticleLibrary(Integer openArticleLibrary) {
        this.openArticleLibrary = openArticleLibrary;
    }

    public Integer getOpenSmallProgramPush() {
        return openSmallProgramPush;
    }

    public void setOpenSmallProgramPush(Integer openSmallProgramPush) {
        this.openSmallProgramPush = openSmallProgramPush;
    }

    public Integer getOpenAudit() {
        return openAudit;
    }

    public void setOpenAudit(Integer openAudit) {
        this.openAudit = openAudit;
    }

    public Integer getOpenProductCoupon() {
        return openProductCoupon;
    }

    public void setOpenProductCoupon(Integer openProductCoupon) {
        this.openProductCoupon = openProductCoupon;
    }

    public String getProductCouponPushUrl() {
        return productCouponPushUrl;
    }

    public void setProductCouponPushUrl(String productCouponPushUrl) {
        this.productCouponPushUrl = productCouponPushUrl;
    }

    public Integer getOpenRpay() {
        return openRpay;
    }

    public void setOpenRpay(Integer openRpay) {
        this.openRpay = openRpay;
    }

    private BigDecimal balanceReminder;
    public BigDecimal getBalanceReminder() {
        return balanceReminder;
    }

    public void setBalanceReminder(BigDecimal balanceReminder) {
        this.balanceReminder = balanceReminder;
    }

    public Integer getOpenCompulsoryRegister() {
        return openCompulsoryRegister;
    }

    public void setOpenCompulsoryRegister(Integer openCompulsoryRegister) {
        this.openCompulsoryRegister = openCompulsoryRegister;
    }

    public Integer getOpenCompulsoryRecharge() {
        return openCompulsoryRecharge;
    }

    public void setOpenCompulsoryRecharge(Integer openCompulsoryRecharge) {
        this.openCompulsoryRecharge = openCompulsoryRecharge;
    }

    public Integer getOpenTicket() {
        return openTicket;
    }

    public void setOpenTicket(Integer openTicket) {
        this.openTicket = openTicket;
    }

    public Integer getOrderBefore() {
        return orderBefore;
    }

    public void setOrderBefore(Integer orderBefore) {
        this.orderBefore = orderBefore;
    }

    public String getLoadingTextColor() {
        return loadingTextColor;
    }

    public void setLoadingTextColor(String loadingTextColor) {
        this.loadingTextColor = loadingTextColor;
    }

    public String getLoadingLogo() {
        return loadingLogo;
    }

    public void setLoadingLogo(String loadingLogo) {
        this.loadingLogo = loadingLogo;
    }

    public String getLoadingBackground() {
        return loadingBackground;
    }

    public void setLoadingBackground(String loadingBackground) {
        this.loadingBackground = loadingBackground;
    }

    public Integer getOpenManyCustomerOrder() {
        return openManyCustomerOrder;
    }

    public void setOpenManyCustomerOrder(Integer openManyCustomerOrder) {
        this.openManyCustomerOrder = openManyCustomerOrder;
    }

    public Integer getOpenGroupBuy() {
        return openGroupBuy;
    }

    public void setOpenGroupBuy(Integer openGroupBuy) {
        this.openGroupBuy = openGroupBuy;
    }

    public Integer getOpenCashCouponBuy() {
        return openCashCouponBuy;
    }

    public void setOpenCashCouponBuy(Integer openCashCouponBuy) {
        this.openCashCouponBuy = openCashCouponBuy;
    }

    public Integer getConsumptionRebate() {
        return consumptionRebate;
    }

    public void setConsumptionRebate(Integer consumptionRebate) {
        this.consumptionRebate = consumptionRebate;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public Integer getOpenPosDiscount() {
        return openPosDiscount;
    }

    public void setOpenPosDiscount(Integer openPosDiscount) {
        this.openPosDiscount = openPosDiscount;
    }

    public Integer getSmsPushGiftCoupons() {
        return smsPushGiftCoupons;
    }

    public void setSmsPushGiftCoupons(Integer smsPushGiftCoupons) {
        this.smsPushGiftCoupons = smsPushGiftCoupons;
    }

    public Integer getWechatPushGiftCoupons() {
        return wechatPushGiftCoupons;
    }

    public void setWechatPushGiftCoupons(Integer wechatPushGiftCoupons) {
        this.wechatPushGiftCoupons = wechatPushGiftCoupons;
    }

    public Integer getOpenBonus() {
        return openBonus;
    }

    public void setOpenBonus(Integer openBonus) {
        this.openBonus = openBonus;
    }

    public Integer getOpenBrandAccount() {
        return openBrandAccount;
    }

    public void setOpenBrandAccount(Integer openBrandAccount) {
        this.openBrandAccount = openBrandAccount;
    }

    public String getBrandAccountId() {
        return brandAccountId;
    }

    public void setBrandAccountId(String brandAccountId) {
        this.brandAccountId = brandAccountId;
    }

    public Integer getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(Integer recommendTime) {
        this.recommendTime = recommendTime;
    }

    public Integer getOpenThirdInterface() {
        return openThirdInterface;
    }

    public Integer getIsPushSms() {
        return isPushSms;
    }

    public void setIsPushSms(Integer isPushSms) {
        this.isPushSms = isPushSms;
    }

    public Integer getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Integer commentTime) {
        this.commentTime = commentTime;
    }

    public Integer getOpenCommentRecommend() {
        return openCommentRecommend;
    }

    public void setOpenCommentRecommend(Integer openCommentRecommend) {
        this.openCommentRecommend = openCommentRecommend;
    }

    public Integer getOpenPosReminder() {
        return openPosReminder;
    }

    public void setOpenPosReminder(Integer openPosReminder) {
        this.openPosReminder = openPosReminder;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public void setOpenThirdInterface(Integer openThirdInterface) {
        this.openThirdInterface = openThirdInterface;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Integer getOpenOrderRemark() {
        return openOrderRemark;
    }

    public void setOpenOrderRemark(Integer openOrderRemark) {
        this.openOrderRemark = openOrderRemark;
    }

    public Integer getIsPosPlus() {
        return isPosPlus;
    }

    public void setIsPosPlus(Integer isPosPlus) {
        this.isPosPlus = isPosPlus;
    }

    public Integer getIsSendCouponMsg() {
        return isSendCouponMsg;
    }

    public void setIsSendCouponMsg(Integer isSendCouponMsg) {
        this.isSendCouponMsg = isSendCouponMsg;
    }

    public String getExportPassword() {
        return exportPassword;
    }

    public void setExportPassword(String exportPassword) {
        this.exportPassword = exportPassword;
    }

    public WechatChargeConfig getWechatChargeConfig() {
        return wechatChargeConfig;
    }

    public void setWechatChargeConfig(WechatChargeConfig wechatChargeConfig) {
        this.wechatChargeConfig = wechatChargeConfig;
    }

    public Integer getIsMealFee() {
        return isMealFee;
    }

    public void setIsMealFee(Integer isMealFee) {
        this.isMealFee = isMealFee;
    }

    public Integer getIsOptionalPhoto() {
        return isOptionalPhoto;
    }

    public void setIsOptionalPhoto(Integer isOptionalPhoto) {
        this.isOptionalPhoto = isOptionalPhoto;
    }

    public Integer getIsPrintPayAfter() {
        return isPrintPayAfter;
    }

    public void setIsPrintPayAfter(Integer isPrintPayAfter) {
        this.isPrintPayAfter = isPrintPayAfter;
    }

    public String getAliProductCode() {
        return aliProductCode;
    }

    public void setAliProductCode(String aliProductCode) {
        this.aliProductCode = aliProductCode;
    }

    public String getAliSellerId() {
        return aliSellerId;
    }

    public void setAliSellerId(String aliSellerId) {
        this.aliSellerId = aliSellerId;
    }

    public String getAliAppId() {
        return aliAppId;
    }

    public void setAliAppId(String aliAppId) {
        this.aliAppId = aliAppId;
    }

    public String getAliPrivateKey() {
        return aliPrivateKey;
    }

    public void setAliPrivateKey(String aliPrivateKey) {
        this.aliPrivateKey = aliPrivateKey;
    }

    public String getAliPublicKey() {
        return aliPublicKey;
    }

    public void setAliPublicKey(String aliPublicKey) {
        this.aliPublicKey = aliPublicKey;
    }

    public Integer getAliPay() {
        return aliPay;
    }

    public void setAliPay(Integer aliPay) {
        this.aliPay = aliPay;
    }

    public Byte getGeekPosPrice() { return geekPosPrice; }

    public void setGeekPosPrice(Byte geekPosPrice) { this.geekPosPrice = geekPosPrice; }

    public Integer getCallTvType() {
        return callTvType;
    }

    public void setCallTvType(Integer callTvType) {
        this.callTvType = callTvType;
    }

    public Integer getOpenAndriodApk() {
        return openAndriodApk;
    }

    public void setOpenAndriodApk(Integer openAndriodApk) {
        this.openAndriodApk = openAndriodApk;
    }

    public String getQrCodeBrand() {
        return qrCodeBrand;
    }

    public void setQrCodeBrand(String qrCodeBrand) {
        this.qrCodeBrand = qrCodeBrand;
    }

    public Integer getIntoShopSubscribe() {
        return intoShopSubscribe;
    }

    public void setIntoShopSubscribe(Integer intoShopSubscribe) {
        this.intoShopSubscribe = intoShopSubscribe;
    }

    public Integer getAllowAppraiseSubscribe() {
        return allowAppraiseSubscribe;
    }

    public void setAllowAppraiseSubscribe(Integer allowAppraiseSubscribe) {
        this.allowAppraiseSubscribe = allowAppraiseSubscribe;
    }

    public Integer getOpenPosCharge() {
        return openPosCharge;
    }

    public void setOpenPosCharge(Integer openPosCharge) {
        this.openPosCharge = openPosCharge;
    }

    public Integer getPosOpenTable() {
        return posOpenTable;
    }

    public void setPosOpenTable(Integer posOpenTable) {
        this.posOpenTable = posOpenTable;
    }

    public Integer getIntegralPay() {
        return integralPay;
    }

    public void setIntegralPay(Integer integralPay) {
        this.integralPay = integralPay;
    }

    public Integer getIsSub() {
        return isSub;
    }

    public void setIsSub(Integer isSub) {
        this.isSub = isSub;
    }

    public String getWechatChargeConfigId() {
        return wechatChargeConfigId;
    }

    public void setWechatChargeConfigId(String wechatChargeConfigId) {
        this.wechatChargeConfigId = wechatChargeConfigId;
    }

    final public List<String> getPlatformName() {
        return platformName;
    }

    final public void setPlatformName(List<String> platformName) {
        this.platformName = platformName;
    }

    final public Integer getIsOpenOutFood() {
        return isOpenOutFood;
    }

    final public void setIsOpenOutFood(Integer isOpenOutFood) {
        this.isOpenOutFood = isOpenOutFood;
    }

    final public Integer getIsAllowExtraPrice() {
        return isAllowExtraPrice;
    }

    final public void setIsAllowExtraPrice(Integer isAllowExtraPrice) {
        this.isAllowExtraPrice = isAllowExtraPrice;
    }

    final public String getConsumerKey() {
        return consumerKey;
    }

    final public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    final public String getConsumerSecret() {
        return consumerSecret;
    }

    final public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getSwitchMode() {
        return switchMode;
    }

    public void setSwitchMode(String switchMode) {
        this.switchMode = switchMode;
    }

    public String getBizUrl() {
        return bizUrl;
    }

    public void setBizUrl(String bizUrl) {
        this.bizUrl = bizUrl;
    }

    public BigDecimal getHighMoney() {
        return highMoney;
    }

    public void setHighMoney(BigDecimal highMoney) {
        this.highMoney = highMoney;
    }

    final public Integer getIsUseServicePrice() {
        return isUseServicePrice;
    }

    final public void setIsUseServicePrice(Integer isUseServicePrice) {
        this.isUseServicePrice = isUseServicePrice;
    }

    final public Integer getPrintType() {
        return printType;
    }

    final public void setPrintType(Integer printType) {
        this.printType = printType;
    }

    final public Integer getIsUseRecommend() {
        return isUseRecommend;
    }

    final public void setIsUseRecommend(Integer isUseRecommend) {
        this.isUseRecommend = isUseRecommend;
    }

    final public Integer getRecommendArticle() {
        return recommendArticle;
    }

    final public void setRecommendArticle(Integer recommendArticle) {
        this.recommendArticle = recommendArticle;
    }

    public Integer getRecommendCategory() {
        return recommendCategory;
    }

    public void setRecommendCategory(Integer recommendCategory) {
        this.recommendCategory = recommendCategory;
    }

    final public Byte getAutoPrintTotal() {
        return autoPrintTotal;
    }

    final public void setAutoPrintTotal(Byte autoPrintTotal) {
        this.autoPrintTotal = autoPrintTotal;
    }

    final public Integer getStockWeekend() {
        return stockWeekend;
    }

    final public void setStockWeekend(Integer stockWeekend) {
        this.stockWeekend = stockWeekend;
    }

    final public Integer getStockWorkingDay() {
        return stockWorkingDay;
    }

    final public void setStockWorkingDay(Integer stockWorkingDay) {
        this.stockWorkingDay = stockWorkingDay;
    }

    final public Integer getReconnectSecond() {
        return reconnectSecond;
    }

    final public void setReconnectSecond(Integer reconnectSecond) {
        this.reconnectSecond = reconnectSecond;
    }

    final public Integer getReconnectTimes() {
        return reconnectTimes;
    }

    final public void setReconnectTimes(Integer reconnectTimes) {
        this.reconnectTimes = reconnectTimes;
    }

    public Boolean getAutoAlertAppraise() {
		return autoAlertAppraise;
	}

	public void setAutoAlertAppraise(Boolean autoAlertAppraise) {
		this.autoAlertAppraise = autoAlertAppraise;
	}

	public Integer getGoodAppraiseLength() {
		return goodAppraiseLength;
	}

	public void setGoodAppraiseLength(Integer goodAppraiseLength) {
		this.goodAppraiseLength = goodAppraiseLength;
	}

	public Integer getBadAppraiseLength() {
		return badAppraiseLength;
	}

	public void setBadAppraiseLength(Integer badAppraiseLength) {
		this.badAppraiseLength = badAppraiseLength;
	}

	public Byte getIsChoiceMode() {
		return isChoiceMode;
	}

	public void setIsChoiceMode(Byte isChoiceMode) {
		this.isChoiceMode = isChoiceMode;
	}

	public Integer getCloseContinueTime() {
		return closeContinueTime;
	}

	public void setCloseContinueTime(Integer closeContinueTime) {
		this.closeContinueTime = closeContinueTime;
	}

	public String getRedPackageLogo() {
		return redPackageLogo;
	}

	public void setRedPackageLogo(String redPackageLogo) {
		this.redPackageLogo = redPackageLogo;
	}

    public String getWechatBrandName() {
        return wechatBrandName;
    }

    public void setWechatBrandName(String wechatBrandName) {
        this.wechatBrandName = wechatBrandName;
    }

    public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSmsSign() {
        return smsSign;
    }

    public void setSmsSign(String smsSign) {
        this.smsSign = smsSign == null ? null : smsSign.trim();
    }

    public BigDecimal getAppraiseMinMoney() {
        return appraiseMinMoney;
    }

    public void setAppraiseMinMoney(BigDecimal appraiseMinMoney) {
        this.appraiseMinMoney = appraiseMinMoney;
    }

    public String getCustomerRegisterTitle() {
        return customerRegisterTitle;
    }

    public void setCustomerRegisterTitle(String customerRegisterTitle) {
        this.customerRegisterTitle = customerRegisterTitle == null ? null : customerRegisterTitle.trim();
    }

    public String getWechatWelcomeImg() {
        return wechatWelcomeImg;
    }

    public void setWechatWelcomeImg(String wechatWelcomeImg) {
        this.wechatWelcomeImg = wechatWelcomeImg == null ? null : wechatWelcomeImg.trim();
    }

    public String getWechatWelcomeTitle() {
        return wechatWelcomeTitle;
    }

    public void setWechatWelcomeTitle(String wechatWelcomeTitle) {
        this.wechatWelcomeTitle = wechatWelcomeTitle == null ? null : wechatWelcomeTitle.trim();
    }

    public String getWechatWelcomeUrl() {
        return wechatWelcomeUrl;
    }

    public void setWechatWelcomeUrl(String wechatWelcomeUrl) {
        this.wechatWelcomeUrl = wechatWelcomeUrl == null ? null : wechatWelcomeUrl.trim();
    }

    public String getWechatWelcomeContent() {
        return wechatWelcomeContent;
    }

    public void setWechatWelcomeContent(String wechatWelcomeContent) {
        this.wechatWelcomeContent = wechatWelcomeContent == null ? null : wechatWelcomeContent.trim();
    }

    public String getWechatHomeName() {
        return wechatHomeName;
    }

    public void setWechatHomeName(String wechatHomeName) {
        this.wechatHomeName = wechatHomeName == null ? null : wechatHomeName.trim();
    }

    public String getWechatTangshiName() {
        return wechatTangshiName;
    }

    public void setWechatTangshiName(String wechatTangshiName) {
        this.wechatTangshiName = wechatTangshiName == null ? null : wechatTangshiName.trim();
    }

    public String getWechatMyName() {
        return wechatMyName;
    }

    public void setWechatMyName(String wechatMyName) {
        this.wechatMyName = wechatMyName == null ? null : wechatMyName.trim();
    }

    public String getWechatWaimaiName() {
        return wechatWaimaiName;
    }

    public void setWechatWaimaiName(String wechatWaimaiName) {
        this.wechatWaimaiName = wechatWaimaiName == null ? null : wechatWaimaiName.trim();
    }

    public String getWechatCustomoStyle() {
        return wechatCustomoStyle;
    }

    public void setWechatCustomoStyle(String wechatCustomoStyle) {
        this.wechatCustomoStyle = wechatCustomoStyle == null ? null : wechatCustomoStyle.trim();
    }

	public Integer getAutoConfirmTime() {
		return autoConfirmTime;
	}

	public void setAutoConfirmTime(Integer autoConfirmTime) {
		this.autoConfirmTime = autoConfirmTime;
	}

    public Integer getWaitRedEnvelope() { return waitRedEnvelope; }

    public void setWaitRedEnvelope(Integer waitRedEnvelope) { this.waitRedEnvelope = waitRedEnvelope; }

    public BigDecimal getBaseMoney() { return baseMoney; }

    public void setBaseMoney(BigDecimal baseMoney) { this.baseMoney = baseMoney; }

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getQueueNotice() {
		return queueNotice;
	}

	public void setQueueNotice(String queueNotice) {
		this.queueNotice = queueNotice;
	}

    public Integer getOpenUnionPay() {
        return openUnionPay;
    }

    public void setOpenUnionPay(Integer openUnionPay) {
        this.openUnionPay = openUnionPay;
    }

    public Integer getOpenMoneyPay() {
        return openMoneyPay;
    }

    public void setOpenMoneyPay(Integer openMoneyPay) {
        this.openMoneyPay = openMoneyPay;
    }

    public Integer getOpenShanhuiPay() {
        return openShanhuiPay;
    }

    public void setOpenShanhuiPay(Integer openShanhuiPay) {
        this.openShanhuiPay = openShanhuiPay;
    }

    public Integer getOpenShoplist() {
        return openShoplist;
    }

    public void setOpenShoplist(Integer openShoplist) {
        this.openShoplist = openShoplist;
    }

    public Integer getOpenHttps() {
        return openHttps;
    }

    public void setOpenHttps(Integer openHttps) {
        this.openHttps = openHttps;
    }

    public Integer getTurntable() {
        return turntable;
    }

    public void setTurntable(Integer turntable) {
        this.turntable = turntable;
    }

    public Integer getCouponCD() {
        return couponCD;
    }

    public void setCouponCD(Integer couponCD) {
        this.couponCD = couponCD;
    }

    public String getMemberCardUrl() {
        return memberCardUrl;
    }

    public void setMemberCardUrl(String memberCardUrl) {
        this.memberCardUrl = memberCardUrl;
    }

    public Integer getIsOpenScm() {
        return isOpenScm;
    }

    public void setIsOpenScm(Integer isOpenScm) {
        this.isOpenScm = isOpenScm;
    }

    public Integer getReceiptSwitch() {
        return receiptSwitch;
    }

    public void setReceiptSwitch(Integer receiptSwitch) {
        this.receiptSwitch = receiptSwitch;
    }

    public Integer getTemplateEdition() {
        return templateEdition;
    }

    public void setTemplateEdition(Integer templateEdition) {
        this.templateEdition = templateEdition;
    }

    public Integer getMessageSwitch() {
        return messageSwitch;
    }

    public void setMessageSwitch(Integer messageSwitch) {
        this.messageSwitch = messageSwitch;
    }

    public Integer getDelayAppraiseMoneyTime() {
        return delayAppraiseMoneyTime;
    }

    public void setDelayAppraiseMoneyTime(Integer delayAppraiseMoneyTime) {
        this.delayAppraiseMoneyTime = delayAppraiseMoneyTime;
    }

    public Integer getAppraiseEdition() {
        return appraiseEdition;
    }

    public void setAppraiseEdition(Integer appraiseEdition) {
        this.appraiseEdition = appraiseEdition;
    }
}