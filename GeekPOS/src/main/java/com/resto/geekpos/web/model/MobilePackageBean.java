package com.resto.geekpos.web.model;/**
 * Created by user on 2016/3/4.
 */

import java.io.Serializable;

/**
 * MobilePackageBean
 *
 * @author ken zhao
 * @date 2016/3/4
 */
public class MobilePackageBean implements Serializable{

    private String transCode; //交易码
    private String packageFormat;//打包格式
    private int length;//报文长度
    private String p;//客户端校验串
    private String terminalType;//客户端类型
    private Object content;//报文内容
    private String returnCode = "200";
    private String apiMethod; //方法


    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getPackageFormat() {
        return packageFormat;
    }

    public void setPackageFormat(String packageFormat) {
        this.packageFormat = packageFormat;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }
}
