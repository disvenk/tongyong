package com.resto.brand.core.entity;

import java.io.Serializable;

/**
 * TxSmsResult : 响应的结果对象
 *
 * @author StarZou
 * @since 2014-09-27 16:28
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 6288374846131788743L;

    /**
     * 信息
     */
    private String message;

    /**
     * 状态码
     */
    private int statusCode;

    /**
     * 是否成功
     */
    private boolean success;

    private  String openId;


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public static Result getSuccess(){
		return new Result(true);
    	
    }
    public Result() {
    }

    public Result(String message, int statusCode, boolean success) {
        this.message = message;
        this.statusCode = statusCode;
        this.success = success;
    }


    public Result(boolean success,String openId) {
        this.success = success;
        this.openId=openId;
    }

    public Result(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public Result(boolean isSuccess) {
    	this.success = isSuccess;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    
}
