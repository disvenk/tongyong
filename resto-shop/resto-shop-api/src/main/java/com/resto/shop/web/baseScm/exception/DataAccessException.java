package com.resto.shop.web.baseScm.exception;

/**
 * Created by zhaojingyang on 2015/8/15.
 */
public class DataAccessException extends RuntimeException {

    private static final long serialVersionUID = -5696614826832449416L;

    /**
     * Constructor for DataAccessException.
     *
     * @param msg the detail message
     */
    public DataAccessException(String msg) {
        super(msg);
    }

    /**
     * Constructor for DataAccessException.
     *
     * @param msg the detail message
     * @param cause the root cause (usually from using a underlying data access
     *            API such as JDBC)
     */
    public DataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructor for DataAccessException.
     *
     * @param cause the root cause (usually from using a underlying data access
     *            API such as JDBC)
     */
    public DataAccessException(Throwable cause) {
        super(cause);
    }
}
