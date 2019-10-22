package com.resto.shop.web.util.log;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhaojingyang on 2015/8/16.
 */
public class TraceLoggerFactory {

    private volatile static String ip;

    private volatile static ConcurrentHashMap<String, TraceLogger> traceLoggerMap = new ConcurrentHashMap<String, TraceLogger>();

    public final static Logger logger = LoggerFactory.getLogger(TraceLoggerFactory.class);

    public TraceLoggerFactory() {
        this.ip = getLocalIP();
    }

//    public static TraceLogger getTraceLogger(String traceType) {
//        TraceLogger TraceLogger = traceLoggerMap.get(traceType);
//        if (null == TraceLogger) {
//            final TraceLogger tmpTraceLogger = new OtsTraceLogger(traceType, TraceLogConfig.getInstance().getTraceLogConfig(traceType));
//            TraceLogger = traceLoggerMap.putIfAbsent(traceType, tmpTraceLogger);
//            if (null == TraceLogger) {
//                TraceLogger = tmpTraceLogger;
//            }
//        }
//        return TraceLogger;
//    }

    public static String getIp() {
        if (StringUtils.isEmpty(ip)) {
            ip = getLocalIP();
        }
        return ip;
    }

    private static String getLocalIP() {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP

        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException("Can't find the localIP!");
        }
        InetAddress ip;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            try {
                if (!ni.isLoopback()) {
                    Enumeration<InetAddress> address = ni.getInetAddresses();
                    while (address.hasMoreElements()) {
                        ip = address.nextElement();
                        if (!ip.isSiteLocalAddress()
                                && !ip.isLoopbackAddress()
                                && !ip.getHostAddress().contains(":")) {// 外网IP
                            netip = ip.getHostAddress();
                            finded = true;
                            break;
                        } else if (ip.isSiteLocalAddress()
                                && !ip.isLoopbackAddress()
                                && !ip.getHostAddress().contains(":")) {// 内网IP
                            localip = ip.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (localip != null && !"".equals(localip)) {
            return localip;
        } else {
            return netip;
        }
    }
}
