//package com.resto.pos.web.socket;
//
//import com.resto.shop.web.constant.ProductionStatus;
//import com.resto.shop.web.model.Order;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.*;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// *  用于 Tv 安卓客户端 链接          --   2017年1月20日 14:49:15    已废
// * Created by Lmx on 2017/1/19.
// */
//public class SocketThread extends Thread {
//
//    protected static Logger log = LoggerFactory.getLogger(SocketThread.class);
//
//    private  int PORT = 9999;
//
//    private ServerSocket serverSocket = null;
//
//    /**
//     * 保存 tv 端      socketClient
//     */
//    public static final Map<String, List<Socket>> TvClients = new ConcurrentHashMap<>();
//
//
//    public SocketThread(ServerSocket serverScoket){
//        try {
//            if(null == serverSocket){
//                this.serverSocket = new ServerSocket(PORT);
//                log.info("\n\nsocket start success    【IP】" + getLocalIP() +"  -- 【Port】" + PORT +"\n\n");
//            }
//        } catch (Exception e) {
//            log.error("SocketThread创建socket服务出错");
//            e.printStackTrace();
//        }
//
//    }
//
//    public void run(){
//        while(!this.isInterrupted()){
//            try {
//                Socket socket = serverSocket.accept();
//                if(null != socket && !socket.isClosed()){
//                    //处理接收的数据
//                    new SocketOperate(socket).start();
//                    log.info("\n新的socket链接成功 ....  \n【IP】："+socket.getRemoteSocketAddress()+"  【total】：" + TvClients.size());
//                }
//            }catch (Exception e) {
//                log.error("【58】SocketThread  error ...");
//                e.printStackTrace();
//            }
//        }
//        log.error("SocketThread  stop ...");
//    }
//
//    public static void saveClient(String key,Socket socket){
//        List<Socket> clientList =  TvClients.get(key);
//        if(clientList != null && clientList.size()>0){
//            clientList.add(socket);
//        }else{
//            clientList = new ArrayList<>();
//            clientList.add(socket);
//        }
//        TvClients.put(key,clientList);
//    }
//
//
//    public void closeSocketServer(){
//        try {
//            if(null!=serverSocket && !serverSocket.isClosed())
//            {
//                serverSocket.close();
//                log.error("SocketThread  close ......");
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            log.error("【86】SocketThread  error .....");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     *  接收订单
//     * @param order
//     */
//    public static void receiveOrder(Order order){
//        log.info("【Socket】 收到新订单：\n【brandId】"+ order.getBrandId()+"\n【shopId】"+order.getShopDetailId()+"  \n【orderId】" + order.getId());
//        int producStatus = order.getProductionStatus();
//        if (ProductionStatus.PRINTED == producStatus ) {//新订单
//            receive("new",order);
//        }else if(ProductionStatus.HAS_CALL == producStatus){//叫号
//            receive("remove",order);
//            receive("call",order);
//        }
//    }
//
//    public static void receive(String type,Order order){
//        JSONObject data = new JSONObject();
//        data.put("type",type);
//        data.put("code",order.getVerCode());
//        data.put("orderId",order.getId());
//        data.put("data","");
//        sendmsg(order.getShopDetailId(), data.toString());
//    }
//
//    public static void sendmsg(String key,String msg) {
//        log.info("\n【服务器】：\n【shopId】"+key + "\n【msg】" + msg);
//        List<Socket> clientList = TvClients.get(key);
//        if(clientList==null || clientList.size()<=0 ){
//            return;
//        }
//        for (Socket client : clientList ) {
//            PrintWriter pout = null;
//            try {
//                pout = new PrintWriter(new BufferedWriter(
//                        new OutputStreamWriter(client.getOutputStream())),true);
//                pout.println(msg);
//                log.info("send  success【orderId】"+key);
//            }catch (IOException e) {
//                log.error("发送失败！【shopId】"+key);
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 获取本机IP地址
//     * 在服务器中，获取的为阿里云内网IP
//     * 在开发环境中，获取的为127.0.0.1
//     * @return
//     */
//    public static String getLocalIP(){
//        String SERVER_IP = "";
//        try {
//            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
//            InetAddress ip = null;
//            while (netInterfaces.hasMoreElements()) {
//                NetworkInterface ni = (NetworkInterface) netInterfaces
//                        .nextElement();
//                ip = (InetAddress) ni.getInetAddresses().nextElement();
//                SERVER_IP = ip.getHostAddress();
//                if (SERVER_IP != null) {
//                    break;
//                } else {
//                    ip = null;
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        return SERVER_IP;
//    }
//}
