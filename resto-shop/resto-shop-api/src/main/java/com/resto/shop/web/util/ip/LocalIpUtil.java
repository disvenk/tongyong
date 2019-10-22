package com.resto.shop.web.util.ip;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by zhaojingyang on 2015/12/25.
 */
public class LocalIpUtil {

    /**
     * 获取本地IP
     *
     * @return
     */
    public static String getLocalIp() {
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
            }
        }

        if (localip != null && !"".equals(localip)) {
            return localip;
        } else {
            return netip;
        }
    }

    public static void main(String args[]) {
        System.out.println(getLocalIp());
    }
}
