package com.resto.brand.core.TxSmsMessage;

/**
 * Created by yz on 2017-05-19.
 */
public class TxSmsTel {

    private String nationcode ;//国家码
    private String mobile;//手机号码

    public String getNationcode() {
        return nationcode;
    }

    public void setNationcode(String nationcode) {
        this.nationcode = nationcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public TxSmsTel() {
    }

    public TxSmsTel(String nationcode, String mobile) {
        this.nationcode = nationcode;
        this.mobile = mobile;
    }
}
