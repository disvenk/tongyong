package com.resto.shop.web.exception;

/**
 * 异常基类 其他模块异常都继承此异常
 */
public class BaseServiceException extends RuntimeException {
    private static final long serialVersionUID = 8393345103500713362L;
    protected String errorCode;
    protected String message;


    public BaseServiceException() {
        super();
    }

    public BaseServiceException(String message) {
        super(message);
        this.message = message;
    }

    public BaseServiceException(Throwable cause) {
        super(cause);
    }

    public BaseServiceException(String errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }

    public BaseServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
