package com.resto.shop.web.util.log;//package cn.restoplus.common.tracelog;
//
//import com.aliyun.openservices.ots.OTSClient;
//import com.aliyun.openservices.ots.model.*;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * traceLog的工具类，支持异步批量上传traceLog，有两种flush策略：
// * <p/>
// * 1. 定时Flush
// * 2. 按日志数量Flush
// * <p/>
// *
// * Created by zhaojingyang on 2015/8/16.
// */
//public class OtsTraceLogger implements TraceLogger {
//    public final static Logger logger = LoggerFactory.getLogger(OtsTraceLogger.class);
//    private static final String LOG_SPLIT = "&&&&&&";
//    private ConcurrentLinkedQueue<TraceLog> cachedQueue = new ConcurrentLinkedQueue<TraceLog>();
//    private AtomicLong logIndex = new AtomicLong(0);
//    private String traceType;
//    private String traceSubfix;
//    private OTSClient otsClient;
//
//    public OtsTraceLogger(String traceType, Properties properties) {
//
//        this.traceType = traceType;
//
//        /**
//         * 初始化otsclient
//         */
//        String endPoint = properties.getProperty("endPoint");
//        String accessId = properties.getProperty("accessId");
//        String accessKey = properties.getProperty("accessKey");
//        String instanceName = properties.getProperty("instanceName");
//        if (StringUtils.isEmpty(endPoint) || StringUtils.isEmpty(accessId)
//                || StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(instanceName)) {
//            throw new RuntimeException("invalid ots connection parameters: " + properties);
//        }
//        this.otsClient = new OTSClient(endPoint, accessId, accessKey, instanceName);
//        this.traceSubfix = properties.getProperty("traceSubfix", "");
//
//        /**
//         * 启动一个独立线程去刷日志
//         */
//        HbaseLogFlushThread hbaseLogFlushThread = new HbaseLogFlushThread("OtsTraceLogger-" + traceType);
//        hbaseLogFlushThread.start();
//    }
//
//    protected void triggerFlush() {
//        try {
//            List<TraceLog> traceLogList = new ArrayList<TraceLog>();
//            while (true) {
//                TraceLog traceLog = cachedQueue.poll();
//                if (traceLog == null) {
//                    break;
//                }
//                traceLogList.add(traceLog);
//            }
//            //flush
//            if (traceLogList.size() > 0) {
//                flushToOts(traceLogList);
//            }
//        } catch (Exception e) {
//            logger.error("HbaseLogFlushThread's exception (" + traceType + "-" + traceSubfix + "): " + e.getMessage(), e);
//        }
//    }
//
//    protected void flushToOts(List<TraceLog> traceLogList) {
//        BatchWriteRowRequest batchWriteRowRequest = new BatchWriteRowRequest();
//        for (TraceLog traceLog : traceLogList) {
//            RowPutChange rowPutChange = convertToPutChange(traceLog);
//            if (null != rowPutChange) {
//                batchWriteRowRequest.addRowPutChange(rowPutChange);
//            }
//        }
//        BatchWriteRowResult batchWriteRowResult = otsClient.batchWriteRow(batchWriteRowRequest);
//        Map<String, List<BatchWriteRowResult.RowStatus>> putRowStatus = batchWriteRowResult.getPutRowStatus();
//        if (null != putRowStatus) {
//            for (Map.Entry<String, List<BatchWriteRowResult.RowStatus>> entry : putRowStatus.entrySet()) {
//                List<BatchWriteRowResult.RowStatus> rowStatusList = entry.getValue();
//                if (null != rowStatusList) {
//                    for (BatchWriteRowResult.RowStatus rowStatus : rowStatusList) {
//                        if (!rowStatus.isSucceed()) {
//                            logger.error("failed to flush traceLog(" + traceType + "-" + traceSubfix + ")! error: " + rowStatus.getError().getCode() + " - " + rowStatus.getError().getMessage());
//                        }
//                    }
//                }
//            }
//        }
//
//
//    }
//
//    protected RowPutChange convertToPutChange(TraceLog traceLog) {
//        RowPutChange rowPutChange = new RowPutChange(TraceLogConfig.getInstance().getTraceLogConfig(traceType).getProperty("tableName"));
//        RowPrimaryKey primaryKey = new RowPrimaryKey();
//        primaryKey.addPrimaryKeyColumn("trace_id", PrimaryKeyValue.fromString(encodeTraceId(traceLog.getTraceId())));
//        primaryKey.addPrimaryKeyColumn("trace_time", PrimaryKeyValue.fromString(encodeTraceTime(traceLog.getTime())));
//        primaryKey.addPrimaryKeyColumn("trace_app", PrimaryKeyValue.fromString(encodeTraceApp(traceLog.getAppName())));
//        rowPutChange.setPrimaryKey(primaryKey);
//        rowPutChange.addAttributeColumn("trace_content", ColumnValue.fromString(encodeTraceConent(traceLog.getTraceLevel(), traceLog.getContent())));
//        return rowPutChange;
//    }
//
//    protected TraceLog convertToTraceLog(Row row) {
//        String traceId = row.getColumns().get("trace_id").asString();
//        String traceTime = row.getColumns().get("trace_time").asString();
//        String traceApp = row.getColumns().get("trace_app").asString();
//        String traceContent = row.getColumns().get("trace_content").asString();
//
//        String[] traceContentInfos = traceContent.split(LOG_SPLIT);
//        TraceLevel level = TraceLevel.INFO;
//        String logContent = traceContent;
//        if (traceContentInfos.length == 2) {
//            level = TraceLevel.valueOf(traceContentInfos[0]);
//            logContent = traceContentInfos[1];
//        }
//        return new TraceLog(decodeTraceId(traceId),
//                decodeTraceApp(traceApp),
//                decodeTraceTime(traceTime),
//                level,
//                logContent);
//    }
//
//    @Override
//    public void log(TraceLog traceLog) {
//        if (StringUtils.equalsIgnoreCase("true", TraceLogConfig.getInstance().getTraceLogConfig(traceType).getProperty("enable"))) {
//            if (!cachedQueue.add(traceLog)) {
//                logger.error("failed to add tracelog into queue!");
//            }
//            /**
//             * 如果日志数量超过了限制，也会触发flush
//             */
//            if (cachedQueue.size() >= Integer.parseInt(TraceLogConfig.getInstance().getTraceLogConfig(traceType).getProperty("flushSize"))) {
//                triggerFlush();
//            }
//        }
//    }
//
//    @Override
//    public List<TraceLog> query(String traceId) {
//        List<TraceLog> traceLogList = new ArrayList<TraceLog>();
//
//        try {
//            RangeRowQueryCriteria criteria = new RangeRowQueryCriteria(TraceLogConfig.getInstance().getTraceLogConfig(traceType).getProperty("tableName"));
//            RowPrimaryKey inclusiveStartKey = new RowPrimaryKey();
//            inclusiveStartKey.addPrimaryKeyColumn("trace_id", PrimaryKeyValue.fromString(encodeTraceId(traceId)));
//            inclusiveStartKey.addPrimaryKeyColumn("trace_time", PrimaryKeyValue.INF_MIN); // 范围的边界需要提供完整的PK，若查询的范围不涉及到某一列值的范围，则需要将该列设置为无穷大或者无穷小
//            inclusiveStartKey.addPrimaryKeyColumn("trace_app", PrimaryKeyValue.INF_MIN);
//
//            RowPrimaryKey exclusiveEndKey = new RowPrimaryKey();
//            exclusiveEndKey.addPrimaryKeyColumn("trace_id", PrimaryKeyValue.fromString(encodeTraceId((traceId))));
//            exclusiveEndKey.addPrimaryKeyColumn("trace_time", PrimaryKeyValue.INF_MAX); // 范围的边界需要提供完整的PK，若查询的范围不涉及到某一列值的范围，则需要将该列设置为无穷大或者无穷小
//            exclusiveEndKey.addPrimaryKeyColumn("trace_app", PrimaryKeyValue.INF_MAX);
//
//            criteria.setInclusiveStartPrimaryKey(inclusiveStartKey);
//            criteria.setExclusiveEndPrimaryKey(exclusiveEndKey);
//            GetRangeRequest request = new GetRangeRequest();
//            request.setRangeRowQueryCriteria(criteria);
//            GetRangeResult result = otsClient.getRange(request);
//            List<Row> rows = result.getRows();
//            for (Row row : rows) {
//                traceLogList.add(convertToTraceLog(row));
//
//            }
//            //按时间排序
//            Collections.reverse(traceLogList);
//        } catch (Exception e) {
//            logger.error("HbaseLog query exception (" + traceType + "-" + traceSubfix + "): " + e.getMessage(), e);
//        }
//        return traceLogList;
//    }
//
//    private String encodeTraceId(String traceId) {
//        return String.format("%03d", (traceId.hashCode() & 0x7FFFFFFF) % 1000 - 1) + ":" + traceId + traceSubfix;
//    }
//
//    private String decodeTraceId(String traceId) {
//        return traceId.split(":")[1];
//    }
//
//    private String encodeTraceTime(Date time) {
//        return (Long.MAX_VALUE - time.getTime()) + ":" + (Long.MAX_VALUE - logIndex.incrementAndGet()); //加上递加数，保证有序
//    }
//
//    private Date decodeTraceTime(String time) {
//        return new Date(Long.MAX_VALUE - Long.parseLong(time.split(":")[0]));
//    }
//
//    private String encodeTraceApp(String appName) {
//        return appName + "@" + TraceLoggerFactory.getIp();
//    }
//
//    private String decodeTraceApp(String app) {
//        return app;
//    }
//
//    private String encodeTraceConent(TraceLevel traceLevel, String content) {
//        return traceLevel.name() + LOG_SPLIT + content;
//    }
//
//    class HbaseLogFlushThread extends Thread {
//
//        public HbaseLogFlushThread() {
//            super();
//        }
//
//        public HbaseLogFlushThread(String name) {
//            super(name);
//        }
//
//        @Override
//        public void run() {
//            while (true) {
//                if (StringUtils.equalsIgnoreCase("true", TraceLogConfig.getInstance().getTraceLogConfig(traceType).getProperty("enable"))) {
//                    triggerFlush();
//                }
//                try {
//                    Thread.sleep(Integer.parseInt(TraceLogConfig.getInstance().getTraceLogConfig(traceType).getProperty("flushInterval")));
//                } catch (InterruptedException e) {
//                    logger.error(e.getMessage(), e);
//                }
//            }
//        }
//    }
//}
