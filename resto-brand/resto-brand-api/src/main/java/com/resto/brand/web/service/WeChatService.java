package com.resto.brand.web.service;

import com.resto.brand.core.entity.MdlUpload;
import com.resto.brand.core.entity.ResultDto;

import java.util.Map;

/**
 * title
 * company resto+
 * author jimmy 2018/7/19 下午1:58
 * version 1.0
 */
public interface WeChatService {
    static final String GET_ACTIVATEUSER_URL = "https://api.weixin.qq.com/card/membercard/activateuserform/set?access_token=TOKEN";
    static final String GET_ACTIVATE_URL = "https://api.weixin.qq.com/card/membercard/activate/geturl?access_token=ACCESS_TOKEN";
    final String GET_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
    final String GET_USER_INFO_URL_SUBSCRIBE = "https://api.weixin.qq.com/cgi-bin/user/info";//新的获取用户信息链接
    final String USER_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    final String GET_WEB_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    //    final String SEND_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token";
//    final String WEIXIN_PAY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单接口
//    final String WEIXIN_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    final String WEIXIN_REFRESH_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    final String WEIXIN_CHECK_TOKEN_URL = "https://api.weixin.qq.com/sns/auth";
    final String SEND_CUSTOM_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token";
    final String GET_PARAM_QRCODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    final String GET_FILE = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=";
    final String GET_API_SET_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=";
    //    final String GET_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=";
    final String GET_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=";
    final String SEND_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    final String DEL_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=";
    final String SHOW_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
    final String MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESSTOKEN&type=TYPEA";
    final String TOKEN = "resto_plus_wechat_token";

    public final static String onMenuShareTimeline = "onMenuShareTimeline";
    public final static String onMenuShareAppMessage = "onMenuShareAppMessage";
    public final static String onMenuShareQQ = "onMenuShareQQ";
    public final static String onMenuShareWeibo = "onMenuShareWeibo";
    public final static String onMenuShareQZone = "onMenuShareQZone";
    public final static String startRecord = "startRecord";
    public final static String stopRecord = "stopRecord";
    public final static String onVoiceRecordEnd = "onVoiceRecordEnd";
    public final static String playVoice = "playVoice";
    public final static String pauseVoice = "pauseVoice";
    public final static String stopVoice = "stopVoice";
    public final static String onVoicePlayEnd = "onVoicePlayEnd";
    public final static String uploadVoice = "uploadVoice";
    public final static String downloadVoice = "downloadVoice";
    public final static String chooseImage = "chooseImage";
    public final static String previewImage = "previewImage";
    public final static String uploadImage = "uploadImage";
    public final static String downloadImage = "downloadImage";
    public final static String translateVoice = "translateVoice";
    public final static String getNetworkType = "getNetworkType";
    public final static String openLocation = "openLocation";
    public final static String getLocation = "getLocation";
    public final static String hideOptionMenu = "hideOptionMenu";
    public final static String showOptionMenu = "showOptionMenu";
    public final static String hideMenuItems = "hideMenuItems";
    public final static String showMenuItems = "showMenuItems";
    public final static String hideAllNonBaseMenuItem = "hideAllNonBaseMenuItem";
    public final static String showAllNonBaseMenuItem = "showAllNonBaseMenuItem";
    public final static String closeWindow = "closeWindow";
    public final static String scanQRCode = "scanQRCode";
    public final static String chooseWXPay = "chooseWXPay";
    public final static String openProductSpecificView = "openProductSpecificView";
    public final static String addCard = "addCard";
    public final static String chooseCard = "chooseCard";
    public final static String openCard = "openCard";

    String getUserAuthorizeUrl(String state, String redirectUrl, String appid) ;

    String getUrlAccessToken(String code, String appid, String secret);

    String getUrlRefreshToken(String appid, String refresh_token);

    String getURLCheckToken(String access_token,String openid);

    String getUserInfo(String token, String openid);

    String getUserInfoSubscribe(String token, String openid);

    String getJsAccessToken(String appid, String secret);

    String getAccessToken(String appid, String secret);
    void flushAccessToken(String appid, String secret);
    String sendCustomerMsg(String message, String openid, String appid, String secret);
    String sendCustomerImageMsg(String mediaId, String openid, String appid, String secret);
    String getApiSetIndustry(String appid, String secret);
    String getTemplate(String templateIdShort, String appid, String secret);
    String sendTemplate(String opendId, String templateId, String jumpUrl, Map<String, Map<String, Object>> content, String appid, String secret);
    String sendTemplate(String opendId, String templateId, String jumpUrl, Map<String, Object> miniprogram, Map<String, Map<String, Object>> content, String appid, String secret);
    String delTemplate(String templateId, String appid, String secret);
    boolean checkSignature(String signature, String timestamp,
                                  String nonce);
    /**
     * 将文本消息封装成微信规定的xml格式
     *
     * @param toUserName
     * @param fromUserName
     * @param content
     * @return
     */
    String getXmlText(String toUserName, String fromUserName,
                             String content);

    /**
     * 生成单图文消息
     *
     * @param toUsername   收件人
     * @param fromUsername 发件人
     * @param title        标题
     * @param description  描述
     * @param picUrl       图片链接
     * @param url          消息链接（消息点开后转跳到对应链接）
     * @return
     */
    String getXmlImageUrl(String toUsername, String fromUsername,
                                 String title, String description, String picUrl, String url);

    String getXmlImg(String toUsername, String fromUsername, String picUrl);

    void sendCustomerMsgASync(final String message, final String wechatId, final String appid, final String appsecret);
    void sendDayCustomerMsgASync(final String message, final String wechatId, final String appid, final String appsecret, final String telephone, final String brandName,final String shopName);
    /**
     * 获取带参数的二维码
     * 参考：https://mp.weixin.qq.com/wiki/18/167e7d94df85d8389df6c94a7a8f78ba.html
     * @param token
     * @param qrParam（二维码的附带参数字符串类型，长度不能超过64）
     * @author lmx
     * @return
     */
    String getParamQrCode(String token,String qrParam);
    /**
     * 生成带参数的二维码
     */
    String showQrcode(String ticket);
    String downLoadWXFile(String appid, String secret, String mediaId, String savePath, String systemPath);
    ResultDto<MdlUpload> uploadPhoto(String accessToken, String type, String filePath);
    String getOpenMemberCardUrl(String appid,String secret,String cardId);

    }
