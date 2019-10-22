package com.resto.shop.web.constant;

/**
 * Created by KONATA on 2016/10/29.
 */
public class WeekName {

    public final  static int  MONDAY  = 1; //星期一

    public final  static int  TUESDAY  = 2;  //星期二

    public final static int WEDNESDAY  = 3; //星期三

    public final static int  THURSDAY   = 4; //星期四

    public final static int  FRIDAY   = 5;  //星期五

    public final static int SATURDAY   = 6; //星期六

    public final static int SUNDAY   = 7; //星期日


    public static String getName(int mid){
        switch (mid) {
            case MONDAY:
                return "一";
            case TUESDAY:
                return "二";
            case WEDNESDAY:
                return "三";
            case THURSDAY:
                return "四";
            case FRIDAY:
                return "五";
            case SATURDAY:
                return "六";
            case SUNDAY:
                return "日";
            default:
                return "";
        }
    }

}
