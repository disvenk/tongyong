package com.resto.shop.web.util.log;/*
package cn.restoplus.common.tracelog;

import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

*/
/**
 * Created by zhaojingyang on 2015/8/16.
 *//*

public class TraceLogConfig {
    private Map<String, Properties> traceLogProperties = new HashMap<String, Properties>();
    private Map<String, DiamondManager> traceLogDimaondManagers = new HashMap<String, DiamondManager>();

    private final static Logger logger = LoggerFactory.getLogger(TraceLogConfig.class);

    private final static TraceLogConfig dynamicConfig = new TraceLogConfig();

    public static TraceLogConfig getInstance() {
        return dynamicConfig;
    }

    private TraceLogConfig() {

    }

    synchronized public void addDiamondManager(final String traceType) {
        String groupId = "UF";
        final String dataId = "com.urfresh.common.tracelog.config." + traceType;
        DefaultDiamondManager defaultDiamondManager = new DefaultDiamondManager(groupId, dataId, new ManagerListener() {
            public void receiveConfigInfo(String configInfo) {
                configInfo = StringUtils.strip(configInfo);
                if (StringUtils.isNotBlank(configInfo)) {
                    logger.warn("Change tracelog config, dataId: " + dataId + ", content: " + configInfo);
                    Properties prop = new Properties();
                    try {
                        prop.load(new StringReader(configInfo));
                        traceLogProperties.put(traceType, prop);
                    } catch (Exception e) {
                        logger.error("Parse tracelog config failed! dataId: " + dataId + ", content: " + configInfo, e);
                    }
                } else {
                    throw new RuntimeException("empty config! dataId: " + dataId + ", content: " + configInfo);
                }
            }

            public Executor getExecutor() {
                return null;
            }
        });
        //第一次获取数据
        defaultDiamondManager.getManagerListeners().get(0).receiveConfigInfo(defaultDiamondManager.getAvailableConfigureInfomation(5000));

        traceLogDimaondManagers.put(traceType, defaultDiamondManager);

    }

    public Properties getTraceLogConfig(String traceType) {
        if (!traceLogProperties.containsKey(traceType)) {
            addDiamondManager(traceType);
        }
        return traceLogProperties.get(traceType);
    }

    public static void main(String[] args) {
        TraceLogConfig.getInstance().getTraceLogConfig("tt");
    }
}
*/
