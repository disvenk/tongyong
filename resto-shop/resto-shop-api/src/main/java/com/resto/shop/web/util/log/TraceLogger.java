package com.resto.shop.web.util.log;

import java.util.List;

/**
 * Created by zhaojingyang on 2015/8/16.
 */
public interface TraceLogger {

    /**
     * 记录跟踪日志
     *
     * @param traceLog
     */
    public void log(TraceLog traceLog);

    /**
     * 查询日志
     *
     * @param traceId
     * @return
     */
    public List<TraceLog> query(String traceId);
}
