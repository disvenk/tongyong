package com.resto.service.appraise.constant;

/**
 * Created by KONATA on 2016/10/29.
 */
public class MessageType {

    public final static  int  DAY_MESSAGE = 1; //日结短信

    public final static int  XUN_MESSAGE = 2;  //旬结短信

    public  final static int MONTH_MESSAGE = 3; //月结短信

    public final static int NORMAL = 1;//正常

    public  final static  int DELETE=0;//删除

    public static String getName(int tmp){
        switch (tmp){
            case DAY_MESSAGE:
                return "日结短信";
            case XUN_MESSAGE:
                return "旬结短信";
            case MONTH_MESSAGE:
                return "月结短信";
            default:
                return "";
        }
    }
}
