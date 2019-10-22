package com.resto.brand.web.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ShopDetail implements Serializable {


    private static final long serialVersionUID = 3344561212215601151L;
    private String id;

    @NotBlank(message="店铺名字不能为空")
    private String name;

    private String address;

    private String longitude;

    private String latitude;

    private String phone;

    @DateTimeFormat(pattern="HH:mm")
    private Date openTime;

    @DateTimeFormat(pattern="HH:mm")
    private Date closeTime;

    private Integer status;

    private String remark;

    @NotNull(message="店铺模式不能为空")
    private Integer shopMode;

    private String addUser;

    private Date addTime;

    private Date updateTime;

    private String brandId;

    private String updateUser;

    private Integer state;

    //关联字段
    private String brandName;

    private String shopmodeName;

    //新添加的
    private Boolean isOpen;

    //附加字段
    private int articleSellNum;//菜品销售总量

    private String photo;

    private String city;

    private double countAppraise;

    private double numAppraise;

    private Integer waitRedEnvelope;

    private Integer posWaitredEnvelope;

    private BigDecimal baseMoney;

    private BigDecimal highMoney;

    private Integer shopDetailIndex;

    //饿了吗店铺id

    private String restaurantId;

    private Integer geekposQueueIslogin;

    private Date geekposQueueLastLoginTime;

    //是否开启支付宝支付 0:未开通 1:已开通
    private Integer aliPay;

    //支付宝合作伙伴id
    private String aliSellerId;

    //支付宝应用id
    private String aliAppId;

    //支付宝私钥
    private String aliPrivateKey;

    //支付宝公钥
    private String aliPublicKey;

    //支付折扣
    private Integer aliPayDiscount;

    private String appid;

    private String appsecret;

    private String mchid;

    private String mchkey;

    private String payCertPath;

    private Long wxServerId;

    private Integer isMealFee;

    private String mealFeeName;

    private BigDecimal mealFeePrice;

    private Double appraiseMinMoney;

    private Integer autoConfirmTime;

    //设定多少秒之后不能加菜
    private Integer closeContinueTime;

    //是否需要选择配送模式默认选择否
    private Integer isChoiceMode;

    //第一次登入是否需要红包弹窗提醒
    private Integer autoAlertAppraise;

    //好评的最少字数
    private Integer goodAppraiseLength;

    //差评最少字数
    private Integer badAppraiseLength;

    //是否自动打印总单
    private Integer autoPrintTotal;

    //是否启用配餐包
    private Integer isUseRecommend;

    //套餐出单方式
    private Integer printType;

    //是否开启支付后打印总单
    private Integer isPrintPayAfter;

    //是否启用服务费
    private Integer isUseServicePrice;

    private String serviceName;

    //服务费
    private BigDecimal servicePrice;

    private Integer isPush;

    private String pushContext;

    private Long pushTime;

    //品牌标语
    private String slogan;

    //等位提示
    private String queueNotice;

    //是否开启新的二维码
    private Integer isNewQrcode;

    //是否开启显示用户标识功能
    private Integer isUserIdentity;

    private Integer consumeNumber;

    //店铺收款账户的公众号是否与服务商的公众号一致  （0 不一致 1 一致）
    private Integer isSub;

    //等位有效时间
    private Integer waitTime;

    //等位有效单位
    private Integer waitUnit;

    //等位红包失效时间
    private Integer timeOut;

    //用户消费时间限制天数和月分成为vip
    private Integer consumeConfineTime;
    //单位
    private Integer consumeConfineUnit;

    private Integer printReceipt;

    private Integer printKitchen;

    //yz--------新增(2017-01-12)
    private Integer isOpenSms;

    private  String noticeTelephone;

    private Integer recommendTime;

    private Integer allowAfterPay;

    private Integer allowFirstPay;

    private Integer rollingSwitch;

    private Integer sweepMode;

    private Integer openUnionPay;

    private Integer openMoneyPay;

    //美团 闪惠 支付      --lmx
    private Integer openShanhuiPay;

    //大众点评的店铺ID（和闪惠关联）     --lmx
    private String dazhongShopId;

    //大众点评 appAuthToken         --laoyin
    private String dazhongAppAuthToken;

    //pos加菜选择菜品价格
    private Integer posPlusType;

    //第三方appid
    private  String thirdAppid;

    private Integer integralPay;

    private Integer printMeituan;

    private Integer posOpenTable;

    private Integer openPosAliPay;

    private Integer openPosUnionPay;

    private Integer openPosMoneyPay;

    private Integer openPosShanhuiPay;

    private Integer openPosIntegralPay;

    private Integer openPosCharge;

    private Integer openPosWeChatPay;

    //电视叫号选择模式
    private Integer tvMode;
    //电视叫号 APK 版本   _lmx 2017年5月13日 15:55:34
    private String tvApkVersion;

    //0-旧版pos  1-新版
    private Integer isPosNew;

    //0-48 1-42 纸张尺寸
    private Integer pageSize;

    private  Integer daySmsType; //日结短信推送类型 1.短信 2.微信 3 微信+短信

    private Integer modifyOrderPrintReceipt;

    private Integer modifyOrderPrintKitchen;

    private Integer openPosPayOrder;

    private Integer openOrderRemark;

    private Integer limit;

    //加菜是否需要扫码 0- 不需要 1-需要
    private Integer continueOrderScan;

    private String deviceToken;

    private String appkey;

    private String appMasterSecret;

    private String tvIp;

    private Integer templateType;

    private Integer splitKitchen;

    private String waitIp;

    private String tvTextColor;

    private String tvBackground;

    private String tvTextBoxBackground;

    private String tvHeadTextColor;

    private String tvNumberColor;

    private String waitJiaohao;

    private String waitJiucan;

    private String waitGuohao;

    private Integer waitRemindSwitch;

    private Integer waitRemindNumber;

    private String waitRemindText;


    private Integer isRecommendCategory;

    private Integer openMeituan;

    private Long mqId;

    private ShopTvConfig shopTvConfig;
    //是否开启换桌
    private Integer isTurntable;

    private Integer turntablePrintType;

    private Boolean turntablePrintReceipt;

    private Boolean turntablePrintKitchen;

    private Boolean openBadAppraisePrintOrder;

    private Boolean badAppraisePrintReceipt;

    private Boolean badAppraisePrintKitchen;

    private Integer posVersion;

    public Integer getPosVersion() {
        return posVersion;
    }

    public void setPosVersion(Integer posVersion) {
        this.posVersion = posVersion;
    }

    private String informationPicture;

    private String articlePhoto;

    //是否开启R+外卖
    private Integer isTakeout;

    private String posKey;

    public String getPosKey() {
        return posKey;
    }

    public void setPosKey(String posKey) {
        this.posKey = posKey;
    }

    private Integer openPosDiscount;

    private Double apart;

    private Integer consumptionRebate;

    private String brandLogo;

    private String isOpenScm;


    @DateTimeFormat(pattern=("yyyy-MM-dd"))
    private Date rebateTime;


    private Integer orderBefore;

    private Integer areaId;

    private Integer provinceId;

    private Integer cityId;

    private Integer isOpenAddPrintTotal;

    private Integer isOpenUserSign;

    //    private Integer openConsumerRebate;
    private Integer openBadWarning;

    private String warningKey;

    private Integer warningWechat;

    private Integer warningSms;

    private Integer serviceType;

    private String tablewareFeeName;

    private BigDecimal tablewareFeePrice;

    private Integer isOpenTablewareFee;

    private String towelFeeName;

    private BigDecimal towelFeePrice;

    private Integer isOpenTowelFee;

    private String sauceFeeName;

    private BigDecimal sauceFeePrice;

    private Integer isOpenSauceFee;

    private String fansName;

    private Integer aliEncrypt;

    private String orderWelcomeMessage;

    private Integer shopType;

    private Integer autoPrintConsumeOrder;

    private Integer autoPrintCheckOutOrder;

    private Integer enableDuoDongXian;

    private Integer openGroupBuy;

    private Integer openCashCouponBuy;

    private Integer openPosGroupBuy;

    private Integer openPosCashCouponBuy;

    private Integer openRpay;

    private Integer printOutDetails;

    private Integer openManyCustomerOrder;

    //店铺标签
    private String shopTag;

    private List<String> shopTags;

    //是否开启EMQ推送
    private Integer isOpenEmqPush;
}