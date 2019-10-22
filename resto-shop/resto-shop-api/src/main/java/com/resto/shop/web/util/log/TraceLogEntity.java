package com.resto.shop.web.util.log;


/**
 * Created by zhaojingyang on 2015/8/16.
 */
public class TraceLogEntity {

    private TraceLogger traceLogger;

    private Object traceId;

    private String traceApp;

    public static TraceLogEntity instance(TraceLogger traceLogger, Object traceId, String traceApp) {
        return new TraceLogEntity(traceLogger, traceId, traceApp);
    }

    public TraceLogEntity() {
    }

    public TraceLogEntity(TraceLogger traceLogger, Object traceId, String traceApp) {
        this.traceLogger = traceLogger;
        this.traceId = traceId;
        this.traceApp = traceApp;
    }

    public TraceLogger getTraceLogger() {
        return traceLogger;
    }

    public void setTraceLogger(TraceLogger traceLogger) {
        this.traceLogger = traceLogger;
    }

    public Object getTraceId() {
        return traceId;
    }

    public void setTraceId(Object traceId) {
        this.traceId = traceId;
    }

    public String getTraceApp() {
        return traceApp;
    }

    public void setTraceApp(String traceApp) {
        this.traceApp = traceApp;
    }
}
