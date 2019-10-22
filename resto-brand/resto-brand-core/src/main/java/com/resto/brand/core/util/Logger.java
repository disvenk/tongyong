package com.resto.brand.core.util;

import org.apache.log4j.Level;
import org.apache.log4j.net.SyslogAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/11/14/0014 14:39
 * @Description:
 */
public class Logger {

    private final static int priority = 50050;
    private final org.apache.log4j.Logger logger;
    private static final String FQCN;

    //以下为自定义的日志级别
    public static final Level PAY_LEVEL = new SpecialLevel(priority, "SPECIAL", SyslogAppender.LOG_LOCAL0);

    static {
        FQCN = Logger.class.getName();
    }

    private Logger(Class<?> clazz) {
        logger = org.apache.log4j.Logger.getLogger(clazz);
    }

    private Logger() {
        logger = org.apache.log4j.Logger.getRootLogger();
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }

    public static Logger getRootLogger() {
        return new Logger();
    }

    public void special(Object message) {
        forcedLog(logger, PAY_LEVEL, message);
    }

    public void special(Object message, Throwable t) {
        forcedLog(logger, PAY_LEVEL, message, t);
    }

    private static void forcedLog(org.apache.log4j.Logger logger, Level level, Object message) {
        logger.callAppenders(new LoggingEvent(FQCN, logger, level, message, null));
    }

    private static void forcedLog(org.apache.log4j.Logger logger, Level level, Object message, Throwable t) {
        logger.callAppenders(new LoggingEvent(FQCN, logger, level, message, t));
    }
}
