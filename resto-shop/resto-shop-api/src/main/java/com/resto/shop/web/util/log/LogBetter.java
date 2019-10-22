package com.resto.shop.web.util.log;


import com.resto.shop.web.util.env.EnvGroup;
import com.resto.shop.web.util.env.EnvUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhaojingyang on 2015/8/16.
 */
public class LogBetter {

    private TraceLogEntity traceLogEntity;

    private Logger logger;
    /**
     * log级别
     */
    private LogLevel level;
    /**
     * 信息
     */
    private String msg;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 异常
     */
    private Throwable e;
    /**
     * 参数列表
     */
    private Object[] parms;

    /**
     * 为无聊用户设置。想要把参数名和值都一直打印出来的
     */
    private Map<String, Object> parmMap = new LinkedHashMap<String, Object>();

    private EnvGroup[] envs;


    private boolean needLineNumber = true;
    /**
     * 设置了这个参数，会打印到class的上一级堆栈对应的类
     */
    private Class class2Trace;

    public static LogBetter instance(Logger logger) {
        LogBetter logBetter = new LogBetter();
        logBetter.logger = logger;
        return logBetter;
    }

    private final static String map2String(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer("");
        int i = 0;
        sb.append("[");
        for (Map.Entry<String, Object> e : map.entrySet()) {
            sb.append(e.getKey()).append(": ");
            sb.append(String.valueOf(e.getValue()));
            i++;
            if (i < map.size()) {
                sb.append(LogUtil.INTERVAL_CHAR);
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public LogBetter setTraceLogger(TraceLogEntity traceLogEntity) {
        this.traceLogEntity = traceLogEntity;
        return this;
    }

    public LogBetter setLevel(LogLevel level) {
        this.level = level;
        return this;
    }

    public LogBetter setMsg(String msg) {
        this.msg = msg;
        return this;

    }

    public LogBetter notNeedLineNumber() {
        this.needLineNumber = false;
        return this;
    }

    public LogBetter setException(Throwable e) {
        this.e = e;
        return this;
    }

    public LogBetter setParms(Object... parms) {
        this.parms = parms;
        return this;
    }

    public LogBetter setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public LogBetter setTraceClass(Class clasz) {
        this.class2Trace = clasz;
        return this;
    }

    /**
     * 设置了这个，只有当前环境与设置的环境一直才会打印log
     * 比如EnvGroup.DAILY, 只有项目环境是daily的情况下才会打印log
     *
     * @param env
     * @return
     */
    public LogBetter setEnv(EnvGroup env) {
        initEnv();
        for (int i = 0; i < 6; i++) {
            if (envs[i] == env) {
                break;
            }
            if (envs[i] == null) {
                envs[i] = env;
                break;
            }
        }
        return this;
    }

    public void initEnv() {
        if (this.envs == null) {
            this.envs = new EnvGroup[6];
        }
    }

    public LogBetter setParmsMap(Map<String, Object> map) {
        this.parmMap = map;
        return this;
    }

    public LogBetter addParm(String key, Object value) {
        this.parmMap.put(key, value);
        return this;
    }

    public void log() {
        if (this.level == null && StringUtils.isNotBlank(this.errorMsg)) {
            this.level = LogLevel.ERROR;
        } else if (this.level == null) {
            this.level = LogLevel.WARN;
        }

        if (checkEnv()) {
            if (null != this.parmMap && this.parmMap.size() > 0) {
                LogUtil.log(this.logger, this.traceLogEntity, this.msg, this.level, this.errorMsg, this.e, this.class2Trace == null ? LogBetter.class.getName() : class2Trace.getName(), needLineNumber, null != traceLogEntity && null != traceLogEntity.getTraceId(), map2String(this.parmMap));
            } else {
                LogUtil.log(this.logger, this.traceLogEntity, this.msg, this.level, this.errorMsg, this.e, this.class2Trace == null ? LogBetter.class.getName() : class2Trace.getName(), needLineNumber, null != traceLogEntity && null != traceLogEntity.getTraceId(), this.parms);
            }
        }

    }

    public boolean checkEnv() {
        if (this.envs == null) {
            return true;
        }
        for (EnvGroup env : envs) {
            if (env == EnvUtil.getEnvGroup()) {
                return true;
            }
        }
        return false;
    }
}
