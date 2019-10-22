package com.resto.geekpos.web.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.resto.shop.web.model.GetNumber;
import com.resto.shop.web.model.Order;

public class SystemWebSocketHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(SystemWebSocketHandler.class);

    /**
     * 保存 Tv 排号的 端  WebSocketSession
     */
    private static final Map<String, Map<String, WebSocketSession>> TvClients = new ConcurrentHashMap<>();



    /**
     * 用于推送消息
     *
     * @param order
     */
    public static boolean receiveMsg(GetNumber getNumber,String type) {
    	log.info("收到指令：【shopId】" + getNumber.getShopDetailId()+"\n【type】:"+type);
        try {
            return sendInfoToTv(getNumber.getShopDetailId(), getNumber,type);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
        log.debug("websocket 连接断开  sessionID : " + session.getId());
        if(session.isOpen()){
            session.close();
        }
        
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("链接成功！  sessionID : " + session.getId());
    }

    /**
     * 客户端发送消息时 触发
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        JSONObject data = new JSONObject(message.getPayload().toString());
        log.info("客户端发送的消息为：  " + data);
        String type = data.getString("type");
        //如果为 参数为 check 则将当前的 session 保存在 对应的Map中
        if (("check").equals(type)) {
            String key = data.getString("shopId");
            saveSession(TvClients, key, session);
        } else {
            // 客户端发送信息了  ---->    处理信息
            log.info("处理信息 ----- sessionID：" + session.getId() + "  Msg：" + data);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable arg1) throws Exception {
        log.info("断开连接：     sessionID : " + session.getId());
        if (session.isOpen()) {
            session.close();
        }
        //从 对应的map中移除
    }

    @Override
    public boolean supportsPartialMessages() {
    	
        return false;
    }


    /**
     * 给指定的  电视端  发送 信息
     *
     * @param shopId 指定店铺
     * @param order  订单信息
     * @throws IOException
     */
    public static boolean sendInfoToTv(String shopId, GetNumber getNumber,String type) throws IOException {
        JSONObject data = new JSONObject(getNumber);
        data.put("dataType", type);
        TextMessage returnMessage = new TextMessage(data.toString());
        Map<String, WebSocketSession> tvMap = TvClients.get(shopId);
        if (tvMap != null && !tvMap.isEmpty()) {
            List<String> keys = new ArrayList<>();
            boolean isOpen = false;
            for (Map.Entry<String, WebSocketSession> entry : tvMap.entrySet()) {
                if (entry.getValue().isOpen()) {
                    entry.getValue().sendMessage(returnMessage);
                    isOpen = true;
                } else {
                    entry.getValue().close();
                    keys.add(entry.getKey());
                }
            }
            //更新保存的客户端
            tvMap = removeMapVal(tvMap, keys);
            TvClients.put(shopId, tvMap);
            //如果有客户端在线，则返回发送成功！
            return isOpen;
        }else{
        	return false;
        }
    }


    /**
     * 将 session 保存在  clients Map中 
     *
     * @param clientsMap
     * @param shopId
     * @param session
     */
    public void saveSession(Map<String, Map<String, WebSocketSession>> clientsMap, String shopId, WebSocketSession session) {
        Map<String, WebSocketSession> sessionMap = clientsMap.get(shopId);
        if (sessionMap != null && !sessionMap.isEmpty()) {
            //清空 当前 所有的 session 链接
            if (!sessionMap.containsKey(session.getId())) {
                sessionMap.put(session.getId(), session);
            }
        } else {
            sessionMap = new ConcurrentHashMap<String, WebSocketSession>();
            sessionMap.put(session.getId(), session);
        }
        clientsMap.put(shopId, sessionMap);
    }

    /**
     * 移除 当前 Map 中 指定的 keys集合
     * @param currentMap
     * @param keys
     * @return
     */
    private static Map<String, WebSocketSession> removeMapVal(Map<String, WebSocketSession> currentMap,List<String> keys){
        for(String key : keys){
            if(currentMap.containsKey(key)){
                try {
                    if(currentMap.get(key) != null && currentMap.get(key).isOpen()){
                        currentMap.get(key).close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                currentMap.remove(key);
            }
        }
        return currentMap;
    }

    /**
     * 关闭所有 session 链接
     * 并且清楚 Map 所有的值
     * @param currentMap
     */
    private static void removeMapAllVal(Map<String, WebSocketSession> currentMap){
        Set<String> keys = currentMap.keySet();
        for(String key :keys){
            try {
                if(currentMap.get(key) != null && currentMap.get(key).isOpen()){
                    currentMap.get(key).close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        currentMap.clear();
    }
}

