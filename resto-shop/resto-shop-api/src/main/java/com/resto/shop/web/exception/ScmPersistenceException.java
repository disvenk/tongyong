package com.resto.shop.web.exception;



/**
 * scm异常类
 *
 */
public class ScmPersistenceException extends BaseServiceException {
    public ScmPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScmPersistenceException() {
        super();
    }

    public ScmPersistenceException(String message) {
        super(message);
    }

    public ScmPersistenceException(Throwable cause) {
        super(cause);
    }


    public ScmPersistenceException(String errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }
}
