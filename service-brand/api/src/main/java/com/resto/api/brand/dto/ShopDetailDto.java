package com.resto.api.brand.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
@Data
public class ShopDetailDto implements Serializable {

    private static final long serialVersionUID = 3344561212215601151L;

    private String id;

    @ApiModelProperty(value = "店铺名字")
    private String name;

    @ApiModelProperty(value = "店铺地址")
    private String address;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "店铺电话")
    private String phone;

    @ApiModelProperty(value = "开店时间")
    @DateTimeFormat(pattern="HH:mm")
    private Date openTime;

    @ApiModelProperty(value = "关店时间")
    @DateTimeFormat(pattern="HH:mm")
    private Date closeTime;

    @ApiModelProperty(value = "店铺状态")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "店铺模式")
    private Integer shopMode;

    @ApiModelProperty(value = "添加人")
    private String addUser;

    @ApiModelProperty(value = "添加时间")
    private Date addTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "品牌Id")
    private String brandId;

    @ApiModelProperty(value = "数据状态")
    private String updateUser;

    @ApiModelProperty(value = "修改的用户")
    private Integer state;

    //关联字段
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "店铺模式名称")
    private String shopmodeName;

    //新添加的
    @ApiModelProperty(value = "1开启0关闭")
    private Boolean isOpen;

    //附加字段
    @ApiModelProperty(value = "菜品销售总量")
    private int articleSellNum;

    @ApiModelProperty(value = "店铺图片")
    private String photo;

    @ApiModelProperty(value = "所在城市")
    private String city;

    private double countAppraise;

    private double numAppraise;

    @ApiModelProperty(value = "等位红包开启")
    private Integer waitRedEnvelope;

    @ApiModelProperty(value = "pos端是否开启等位红包  默认不开启")
    private Integer posWaitredEnvelope;

    @ApiModelProperty(value = "等位红包增加价")
    private BigDecimal baseMoney;

    @ApiModelProperty(value = "等位红包最高价")
    private BigDecimal highMoney;

    @ApiModelProperty(value = "店铺序号")
    private Integer shopDetailIndex;

    @ApiModelProperty(value = "饿了吗店铺id")
    private String restaurantId;

    @ApiModelProperty(value = "是否登入geekpos  0退出 1登录")
    private Integer geekposQueueIslogin;

    @ApiModelProperty(value = "最后一个登录geekpos的时间")
    private Date geekposQueueLastLoginTime;

    @ApiModelProperty(value = "是否开启支付宝支付 0:未开通 1:已开通")
    private Integer aliPay;

    @ApiModelProperty(value = "支付宝合作伙伴id")
    private String aliSellerId;

    @ApiModelProperty(value = "支付宝应用id")
    private String aliAppId;

    @ApiModelProperty(value = "支付宝私钥")
    private String aliPrivateKey;

    @ApiModelProperty(value = "支付宝公钥")
    private String aliPublicKey;

    @ApiModelProperty(value = "支付折扣")
    private Integer aliPayDiscount;

    @ApiModelProperty(value = "微信公众号appid")
    private String appid;

    @ApiModelProperty(value = "微信公众号appsecret")
    private String appsecret;

    private String mchid;

    private String mchkey;

    private String payCertPath;

    @ApiModelProperty(value = "微信服务商id")
    private Long wxServerId;

    @ApiModelProperty(value = "是否开启餐包费")
    private Integer isMealFee;

    @ApiModelProperty(value = "餐盒费名称")
    private String mealFeeName;

    @ApiModelProperty(value = "餐盒费钱")
    private BigDecimal mealFeePrice;

    @ApiModelProperty(value = "评论最小金额")
    private Double appraiseMinMoney;

    @ApiModelProperty(value = "红包提醒倒计时")
    private Integer autoConfirmTime;

    @ApiModelProperty(value = "设定多少秒之后不能加菜")
    private Integer closeContinueTime;

    @ApiModelProperty(value = "是否需要选择配送模式默认选择否")
    private Integer isChoiceMode;

    @ApiModelProperty(value = "第一次登入是否需要红包弹窗提醒")
    private Integer autoAlertAppraise;

    @ApiModelProperty(value = "好评的最少字数")
    private Integer goodAppraiseLength;

    @ApiModelProperty(value = "差评最少字数")
    private Integer badAppraiseLength;

    @ApiModelProperty(value = "是否自动打印总单")
    private Integer autoPrintTotal;

    @ApiModelProperty(value = "是否启用配餐包")
    private Integer isUseRecommend;

    @ApiModelProperty(value = "套餐出单方式")
    private Integer printType;

    @ApiModelProperty(value = "是否开启支付后打印总单")
    private Integer isPrintPayAfter;

    @ApiModelProperty(value = "是否启用服务费")
    private Integer isUseServicePrice;

    @ApiModelProperty(value = "服务费名称")
    private String serviceName;

    @ApiModelProperty(value = "服务费")
    private BigDecimal servicePrice;

    @ApiModelProperty(value = "电视叫号模式下，控制消息推送")
    private Integer isPush;

    @ApiModelProperty(value = "消息推送文案")
    private String pushContext;

    @ApiModelProperty(value = "消息推送时间")
    private Long pushTime;

    @ApiModelProperty(value = "品牌标语")
    private String slogan;

    @ApiModelProperty(value = "等位提示")
    private String queueNotice;

    @ApiModelProperty(value = "是否开启新的二维码")
    private Integer isNewQrcode;

    @ApiModelProperty(value = "是否开启显示用户标识功能")
    private Integer isUserIdentity;

    @ApiModelProperty(value = "消费次数")
    private Integer consumeNumber;

    @ApiModelProperty(value = "店铺收款账户的公众号是否与服务商的公众号一致  （0 不一致 1 一致）")
    private Integer isSub;

    @ApiModelProperty(value = "等位有效时间")
    private Integer waitTime;

    @ApiModelProperty(value = "等位有效单位")
    private Integer waitUnit;

    @ApiModelProperty(value = "等位红包失效时间")
    private Integer timeOut;

    @ApiModelProperty(value = "用户消费时间限制天数和月分成为vip")
    private Integer consumeConfineTime;

    @ApiModelProperty(value = "次数单位")
    private Integer consumeConfineUnit;

    @ApiModelProperty(value = "退菜是否打印总单 0-不打印 1-打印")
    private Integer printReceipt;

    @ApiModelProperty(value = "退菜是否打印厨打 0-不打印 1-打印")
    private Integer printKitchen;

    //yz--------新增(2017-01-12)
    private Integer isOpenSms;

    private  String noticeTelephone;

    @ApiModelProperty(value = "红包到期提醒时间")
    private Integer recommendTime;

    @ApiModelProperty(value = "是否允许后付(0 允许 1不允许)")
    private Integer allowAfterPay;

    @ApiModelProperty(value = "是否允许后付(0 允许 1不允许)")
    private Integer allowFirstPay;

    @ApiModelProperty(value = "点击菜品分类，页面滑动效果   0 关闭不滑动 1打开滑动")
    private Integer rollingSwitch;

    @ApiModelProperty(value = "扫码进入是否默认堂食   0默认堂食  1需要选择就餐模式")
    private Integer sweepMode;

    @ApiModelProperty(value = "是否开启银联支付  不开启0  开启1")
    private Integer openUnionPay;

    @ApiModelProperty(value = "是否开启现金支付  不开启0  开启1")
    private Integer openMoneyPay;

    //美团 闪惠 支付      --lmx
    @ApiModelProperty(value = "美团 闪惠 支付")
    private Integer openShanhuiPay;

    //大众点评的店铺ID（和闪惠关联）     --lmx
    @ApiModelProperty(value = "大众点评的店铺ID")
    private String dazhongShopId;

    //大众点评 appAuthToken         --laoyin
    @ApiModelProperty(value = "大众点评 appAuthToken")
    private String dazhongAppAuthToken;

    @ApiModelProperty(value = "pos加菜选择菜品价格")
    private Integer posPlusType;

    @ApiModelProperty(value = "第三方接口appid 如果为null则代表需要品牌所有的数据，如果不为null则获取当前店铺的数据")
    private  String thirdAppid;

    @ApiModelProperty(value = "0-未开通   1-已开通")
    private Integer integralPay;

    @ApiModelProperty(value = "是否打印美团订单 0-不启用 1启用")
    private Integer printMeituan;

    @ApiModelProperty(value = "是否开启pos开台功能 0 不开启  1开启")
    private Integer posOpenTable;

    @ApiModelProperty(value = "pos端支付宝支付开关(0:关,1:开)")
    private Integer openPosAliPay;

    @ApiModelProperty(value = "pos端银联支付开关(0:关,1:开)")
    private Integer openPosUnionPay;

    @ApiModelProperty(value = "pos端现金支付开关(0:关,1:开)")
    private Integer openPosMoneyPay;

    @ApiModelProperty(value = "pos端闪惠支付开关(0:关,1:开)")
    private Integer openPosShanhuiPay;

    @ApiModelProperty(value = "pos端会员积分支付开关(0:关,1:开)")
    private Integer openPosIntegralPay;

    @ApiModelProperty(value = "开启pos端账户充值功能(0：未开通 1：已开通)")
    private Integer openPosCharge;

    @ApiModelProperty(value = "pos端微信支付开关(0：关闭 1：开启)")
    private Integer openPosWeChatPay;

    @ApiModelProperty(value = "电视叫号选择模式")
    private Integer tvMode;

    @ApiModelProperty(value = "电视叫号 APK 版本   _lmx 2017年5月13日 15:55:34")
    private String tvApkVersion;

    @ApiModelProperty(value = "0-旧版pos  1-新版")
    private Integer isPosNew;

    @ApiModelProperty(value = "0-48 1-42 纸张尺寸")
    private Integer pageSize;

    @ApiModelProperty(value = "日结短信推送类型 1.短信 2.微信 3 微信+短信")
    private  Integer daySmsType;

    private Integer modifyOrderPrintReceipt;

    private Integer modifyOrderPrintKitchen;

    @ApiModelProperty(value = "是否开启pos端订单结算功能(0:未开启,1:开启)")
    private Integer openPosPayOrder;

    @ApiModelProperty(value = "是否开启订单备注功能(0:不开启   1:开启)")
    private Integer openOrderRemark;

    private Integer limit;

    @ApiModelProperty(value = "加菜是否需要扫码 0- 不需要 1-需要")
    private Integer continueOrderScan;

    private String deviceToken;

    private String appkey;

    private String appMasterSecret;

    @ApiModelProperty(value = "本地电视ip")
    private String tvIp;

    @ApiModelProperty(value = "日结小票模板类型(0：经典版    1：升级版   2：简约版)")
    private Integer templateType;

    @ApiModelProperty(value = "厨打是否拆分打印 0-不 1-是")
    private Integer splitKitchen;

    @ApiModelProperty(value = "等位叫号电视端ip")
    private String waitIp;

    @ApiModelProperty(value = "控制等位叫号TV端字体颜色")
    private String tvTextColor;

    @ApiModelProperty(value = "控制等号叫号TV端背景图片")
    private String tvBackground;

    @ApiModelProperty(value = "控制等号叫号TV端文本框底色")
    private String tvTextBoxBackground;

    @ApiModelProperty(value = "控制等位叫号TV端标头字体颜色")
    private String tvHeadTextColor;

    @ApiModelProperty(value = "控制等位叫号TV端数字字体颜色")
    private String tvNumberColor;

    @ApiModelProperty(value = "叫号模板")
    private String waitJiaohao;

    @ApiModelProperty(value = "就餐模板")
    private String waitJiucan;

    @ApiModelProperty(value = "过号模板")
    private String waitGuohao;

    @ApiModelProperty(value = "是否开启等位叫号提示  还有几桌提示客户")
    private Integer waitRemindSwitch;

    @ApiModelProperty(value = "等位提示的桌数")
    private Integer waitRemindNumber;

    @ApiModelProperty(value = "等位提示的桌数 推送文案")
    private String waitRemindText;

    @ApiModelProperty(value = "推荐菜品类型控制字段默认1开启,0关闭")
    private Integer isRecommendCategory;

    @ApiModelProperty(value = "是否开通美团外卖 0-未开通 1-已开通")
    private Integer openMeituan;

    private Long mqId;

    private ShopTvConfigDto shopTvConfig;

    @ApiModelProperty(value = "是否开启换桌,1开启0关闭")
    private Integer isTurntable;

    @ApiModelProperty(value = "0关闭，1厨房，2前台,3全部")
    private Integer turntablePrintType;

    private Boolean turntablePrintReceipt;

    private Boolean turntablePrintKitchen;

    @ApiModelProperty(value = "是否开启差评打单")
    private Boolean openBadAppraisePrintOrder;

    @ApiModelProperty(value = "差评前台打印 0:不开启  1 ：开启")
    private Boolean badAppraisePrintReceipt;

    @ApiModelProperty(value = "差评厨房打印 0:不开启  1 ：开启")
    private Boolean badAppraisePrintKitchen;

    @ApiModelProperty(value = "个人二维码信息  底图")
    private String informationPicture;

    @ApiModelProperty(value = "店铺开启菜品图片大图还是小图展示    0 大图  1小图")
    private String articlePhoto;

    @ApiModelProperty(value = "是否开启R+外卖")
    private Integer isTakeout;

    @ApiModelProperty(value = "是否开启pos打折  （折上折） 0 不开启  1 开启")
    private Integer openPosDiscount;

    @ApiModelProperty(value = "设置配送距离(单位:km)")
    private Double apart;

    @ApiModelProperty(value = "消费返利   1:1返利活动")
    private Integer consumptionRebate;

    @ApiModelProperty(value = "消费返利解冻时间")
    @DateTimeFormat(pattern=("yyyy-MM-dd"))
    private Date rebateTime;

    @ApiModelProperty(value = "是否开启店铺差评预警(0:未开启  1:开启)")
    private Integer openBadWarning;

    @ApiModelProperty(value = "差评预警的关键字")
    private String warningKey;

    @ApiModelProperty(value = "预警微信推送 0:关 1:开")
    private Integer warningWechat;

    @ApiModelProperty(value = "预警短信推送 0:关 1:开")
    private Integer warningSms;

    @ApiModelProperty(value = "店铺标签")
    private String shopTag;

    private List<String> shopTags;

    private Integer aliEncrypt;

    private Integer isOpenSauceFee;

    private String sauceFeeName;

    private Integer isOpenTowelFee;

    private String towelFeeName;

    private Integer isOpenTablewareFee;

    private String tablewareFeeName;
}