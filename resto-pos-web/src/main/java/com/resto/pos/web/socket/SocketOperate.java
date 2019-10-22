//package com.resto.pos.web.socket;
//
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.Socket;
//
///**
// *  用于 Tv 安卓客户端 链接          --   2017年1月20日 14:49:15    已废
// * Created by Lmx on 2017/1/19.
// */
//public class SocketOperate extends Thread{
//
//    protected Logger log = LoggerFactory.getLogger(getClass());
//
//    private String shopId;
//    private Socket client;
//    private BufferedReader in = null;
//    private String msg = "";
//
//    public SocketOperate(Socket client) {
//        this.client=client;
//    }
//
//    public void run() {
//        try {
//            while(true) {
//                in =  new BufferedReader(new InputStreamReader(client.getInputStream()));
//                if((msg = in.readLine())!= null) {
//                    log.info("【client】："+msg);
//                    JSONObject data = new JSONObject(msg);
//                    if(data.get("shopId")!=null){
//                        shopId = data.getString("shopId");
//                        SocketThread.saveClient(shopId,client);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error("【Client】"+shopId+"    error ...  ");
//            e.printStackTrace();
//        }finally{
//            log.info("【client】："+shopId + " 已断开.....");
////            try{
////                if (in!=null){
////                    in.close();
////                }
////            }catch (Exception e){
////                e.printStackTrace();
////            }
//        }
//    }
//
//}
