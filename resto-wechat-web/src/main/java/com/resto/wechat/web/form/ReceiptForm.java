package com.resto.wechat.web.form;

import com.resto.shop.web.model.Receipt;
import com.resto.shop.web.model.ReceiptTitle;

/**
 * Created by Administrator on 2018/6/14.
 */
public class ReceiptForm {
    public Receipt receipt  = new Receipt();
    public ReceiptTitle title = new ReceiptTitle();
    public String orderId;
    public String serialNum;
    public Integer type;

}
