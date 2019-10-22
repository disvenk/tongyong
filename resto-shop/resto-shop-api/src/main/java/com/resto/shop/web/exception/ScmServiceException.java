package com.resto.shop.web.exception;


/**
 * scm异常类
 *
 */
public class ScmServiceException extends BaseServiceException {
    public ScmServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScmServiceException() {
        super();
    }

    public ScmServiceException(String message) {
        super(message);
    }

    public ScmServiceException(Throwable cause) {
        super(cause);
    }

    public ScmServiceException(String errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }
}
