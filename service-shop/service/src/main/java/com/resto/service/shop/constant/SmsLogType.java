package com.resto.service.shop.constant;

/**
 * 短信通知的类型
 * @author Administrator
 *
 */
public class SmsLogType {
	
	public static final int AUTO_CODE = 1;  //品牌注册(阿里)

    public static final int NOT_ENOUGH = 2;  //短信余额不足通知(阿里)

    public  static final int  ARREARAGE =3 ;//欠费通知(阿里)

    public static final int CHARGE =4;//短信充值通知(阿里)

    public static final int DAYMESSGAGE =5;//日结短信通知(阿里)

    public static final  int XUNMESSAGE =6;//旬结短信通知(阿里)

    public static final int MONTHMESSAGE=7;//月结短信通知(阿里)

    public static final int WAKELOSS=8;//流失唤醒短信(阿里)

    public static final int WARNING=9;//差评预警短信(阿里)



	public static String getSmsLogTypeName(int state){
	    String typeName = "";
	    switch (state) {
            case AUTO_CODE:
                //return "验证码";
                typeName = "验证码";
                break;
            case  DAYMESSGAGE:
                typeName = "日结短信";
                break;
            case WAKELOSS:
                typeName = "流失唤醒短信";
                break;
            case WARNING:
                typeName = "差评预警短信";
                break;
            default:
                return "未知";
            }
	    
	    return typeName;
	}
}
