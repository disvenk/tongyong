//package com.resto.pos.web.socket;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.util.Enumeration;
//
///**
// * 用于 Tv 安卓客户端 链接          --   2017年1月20日 14:49:15    已废
// * Created by Lmx on 2017/1/19.
// */
//public class ServerSocketListener implements ServletContextListener {
//    protected Logger log = LoggerFactory.getLogger(getClass());
//
//    private SocketThread socketThread;
//
//    @Override
//    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        log.info("socket  contextInitialized ...  ");
//        if (socketThread == null)
//        {
//            socketThread = new SocketThread(null);
//            socketThread.start();
//            log.info("SocketThread  start ......");
//        }
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent servletContextEvent) {
//        if (socketThread != null && socketThread.isInterrupted())
//        {
//            socketThread.closeSocketServer();
//            socketThread.interrupt();
//            log.info("SocketThread  close ......");
//        }
//    }
//}
