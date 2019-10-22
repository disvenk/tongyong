package com.resto.brand.web.dto;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/2/9.
 */
public class LuroufanExcelModel implements Serializable {

    //日期
    public static final int POSDATE = 0;
    //开桌时间（年/月/日/小时/分钟）
    public static final int ADDTIME = 1;
    //服务员名称
    public static final int ADDNAME = 2;
    //店号
    public static final int POSID = 3;
    //桌号
    public static final int TABLENO = 4;
    //帐单号
    public static final int PFNAME = 5;
    //部门
    public static final int DEPARTMENT = 6;
    //副部门
    public static final int DEPUTY = 7;
    //类别
    public static final int MENU_TYPE = 8;
    //餐品编码
    public static final int MENU_CODE = 9;
    //餐品名称
    public static final int MENU_NAME = 10;
    //点餐数量
    public static final int QUANTITY = 11;
    //餐品金额（去折扣）
    public static final int AMOUNT_1 = 12;
    //餐品金额（含折扣）
    public static final int AMOUNT_2 = 13;
    //结帐科目
    public static final int ACCOUNT_NAME = 14;
    //支付方式
    public static final int PAY_METHOD = 15;
    //备注
    public static final int REMARK = 16;


}
