package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BrandSetting implements Serializable{

    private static final long serialVersionUID = -2293357137492084058L;

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
    
    //是否需要选择配送模式默认选择否
    private Byte isChoiceMode;
    

    //第一次登入是否需要红包弹窗提醒
    private Boolean autoAlertAppraise;
    
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

    // yz 2017-07-17 计费系统 添加 品牌账户
    private Integer openBrandAccount;
    private String brandAccountId;

    private Integer openBonus;

    private Integer smsPushGiftCoupons;

    private Integer wechatPushGiftCoupons;

    private Integer openPosDiscount;

    private String shareText;

    private Integer consumptionRebate;

    private String tradeAppid;

    private String tradePrivateKey;

    private String tradePublicKey;

    private BigDecimal balanceReminder;

    private Integer intoWhere;

    private Integer openCompulsoryRegister;

    private Integer openCompulsoryRecharge;

    //是否开启转台功能
    private Integer turntable;

    //优惠券cd
    private Integer couponCD;

    private Integer commentTime;

    private Integer openCommentRecommend;

    private Integer isPushSms;

    //微信模板消息控制项
    private Integer templateEdition;

    private Integer delayAppraiseMoneyTime;

}