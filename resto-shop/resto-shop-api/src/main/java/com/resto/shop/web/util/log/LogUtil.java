package com.resto.shop.web.util.log;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.spi.LocationInfo;
import org.slf4j.Logger;

import java.util.Date;

/**
 * Created by zhaojingyang on 2015/8/16.
 */
public class LogUtil {

    public static final String INTERVAL_CHAR = ",  ";

    public static String[] getMsg(String detailMsg, String error, Object[] params) {
        return getMsg(detailMsg, error, params, null, false, false);
    }

    public static String[] getMsg(String detailMsg, String error, Object[] params, String class2Trace, boolean needLineNum, boolean isTrace) {
        String[] msgs = new String[2];
        StringBuffer traceSb = new StringBuffer();
        StringBuffer logSb = new StringBuffer();
        if (needLineNum) {
            LocationInfo locationInfo = new LocationInfo(new Throwable(), class2Trace);
            logSb.append("行数: ").append(locationInfo.fullInfo).append(" | ");
        }
        logSb.append("日志: ");
        if (StringUtils.isNotBlank(detailMsg)) {
            logSb.append(detailMsg);
            if(isTrace)
                traceSb.append(detailMsg);
        } else if (StringUtils.isNotBlank(error)) {
            logSb.append(error);
            if(isTrace)
                traceSb.append(error);
        }
        if (null != params && params.length > 0) {
            logSb.append(" | ").append("参数: ");
            if(isTrace)
                traceSb.append(": ");
            int i = 0;
            for (Object o : params) {
                logSb.append(String.valueOf(o));
                if(isTrace)
                    traceSb.append(String.valueOf(o));
                i++;
                if (i < params.length) {
                    logSb.append(INTERVAL_CHAR);
                    if(isTrace)
                        traceSb.append(INTERVAL_CHAR);
                }
            }

        }
        //最终输出的日志信息去掉空行，帮助排查问题
        msgs[0] = StringUtils.replace(logSb.toString(), "\n", "");
        msgs[1] = StringUtils.replace(traceSb.toString(), "\n", "");
        return msgs;
    }

    public static void log(Logger logger, TraceLogEntity traceLogEntity, String detailMessage, LogLevel level, String error, Throwable e, String class2Trace, boolean needLineNumber, boolean isTrace, Object... params) {
        String[] msg = new String[2];
        TraceLevel traceLevel = TraceLevel.INFO;
        if (level.equals(LogLevel.DEBUG)) {
            if (logger.isDebugEnabled()) {
                traceLevel = TraceLevel.DEBUG;
                msg = getMsg(detailMessage, error, params, class2Trace, needLineNumber, isTrace);
                debug(logger, msg[0], e);
            }
        }
        if (level.equals(LogLevel.WARN)) {
            if (logger.isWarnEnabled()) {
                traceLevel = TraceLevel.WRAN;
                msg = getMsg(detailMessage, error, params, class2Trace, needLineNumber, isTrace);
                warn(logger, msg[0], e);
            }
        }
        if (level.equals(LogLevel.INFO)) {
            if (logger.isInfoEnabled()) {
                traceLevel = TraceLevel.INFO;
                msg = getMsg(detailMessage, error, params, class2Trace, needLineNumber, isTrace);
                info(logger, msg[0], e);
            }
        }
        if (level.equals(LogLevel.ERROR)) {
            if (logger.isErrorEnabled()) {
                traceLevel = TraceLevel.ERROR;
                msg = getMsg(detailMessage, error, params, class2Trace, needLineNumber, isTrace);
                error(logger, msg[0], e);
            }
        }
        if (isTrace) {
            traceLogEntity.getTraceLogger().log(new TraceLog(traceLogEntity.getTraceId().toString(), traceLogEntity.getTraceApp(), new Date(), traceLevel, msg[1]));
        }
    }

    public static void warn(Logger logger, String msg, Throwable e) {
        if (e != null) {
            logger.warn(msg, e);
        } else {
            logger.warn(msg);
        }
    }

    public static void info(Logger logger, String msg, Throwable e) {
        if (e != null) {
            logger.info(msg, e);
        } else {
            logger.info(msg);
        }
    }

    public static void error(Logger logger, String msg, Throwable e) {
        if (e != null) {
            if (logger.isErrorEnabled()) {

                logger.error(msg, e);
            }
        } else {
            logger.error(msg);
        }
    }

    public static void debug(Logger logger, String msg, Throwable e) {
        if (e != null) {
            logger.debug(msg, e);
        } else {
            logger.debug(msg);
        }
    }
}
