package com.resto.wechat.web.exception;

/**
 * Created by KONATA on 2017/2/3.
 */
public class GenericException extends Exception {
    public GenericException(String message) {
        super(message);
    }

    public GenericException() {
        super();
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericException(Throwable cause) {
        super(cause);
    }

    protected GenericException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
