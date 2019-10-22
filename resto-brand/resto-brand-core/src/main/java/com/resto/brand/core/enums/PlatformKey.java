package com.resto.brand.core.enums;

/**
 *  外卖平台
 * Created by Lmx on 2017/3/22.
 */
public class PlatformKey {

    /**
     * 饿了么
     */
    public static final int ELEME   =   1  ;


    /**
     * 美团外卖
     */
    public static final int MEITUAN   =   2  ;


    /**
     * 百度外卖
     */
    public static final int BAIDU   =   3  ;

    /**
     * R+外卖
     */
    public static final int CANJIA   =   4  ;

    public static String getPlatformName(int key){
        String name = "---";
        switch (key){
            case ELEME:
                name =  "饿了么";
            break;
            case MEITUAN:
                name =  "美团外卖";
            break;
            case BAIDU:
                name =  "百度外卖";
            break;
            case CANJIA:
                name =  "R+外卖";
            break;
        }
        return name;
    }

}
