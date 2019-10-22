package com.resto.shop.web.util.log;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhaojingyang on 2015/8/16.
 */
public class TraceLog implements Serializable {

    private static final long serialVersionUID = 7878042666602190036L;
    /**
     * 主键
     */
    private String traceId;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 时间
     */
    private Date time;
    /**
     * 日志内容
     */
    private String content;
    /**
     * 日志级别
     */
    private TraceLevel traceLevel;

    public TraceLog() {

    }

    public TraceLog(String traceId, String appName, Date time, TraceLevel traceLevel, String content) {
        this.traceId = traceId;
        this.appName = appName;
        this.time = time;
        this.content = content;
        this.traceLevel = traceLevel;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public TraceLevel getTraceLevel() {
        return traceLevel;
    }

    public void setTraceLevel(TraceLevel traceLevel) {
        this.traceLevel = traceLevel;
    }

    @Override
    public String toString() {
        return "TraceLog{" +
                "traceId='" + traceId + '\'' +
                ", appName='" + appName + '\'' +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", traceLevel=" + traceLevel +
                '}';
    }
}
