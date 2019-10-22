package com.resto.pos.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.pos.web.rpcinterceptors.DataSourceTarget;
import com.resto.pos.web.websocket.SystemWebSocketHandler;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.OrderItem;
import com.resto.shop.web.service.OrderItemService;
import com.resto.shop.web.service.OrderService;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  用于对 Tv端 进行的一些操作
 * Created by Lmx on 2017/4/15.
 */
@Controller
@RequestMapping("/socket")
public class SocketController extends GenericController{

    @Resource
    private ShopDetailService shopDetailService;
    @Resource
    private OrderService orderService;
    @Resource
    private OrderItemService orderItemService;

    /**
     * http://139.196.222.42:8580/pos/socket/test?type=call&shopId=&orderId=
     * @param type
     * @param shopId
     * @param orderId
     * @return
     * @throws IOException
     */
    @RequestMapping("/test")
    @ResponseBody
    public Result test(String type,String shopId,String orderId) throws IOException {
        if(StringUtils.isEmpty(shopId)){
            shopId = "31164cebcc4b422685e8d9a32db12ab8";
        }
        if(StringUtils.isEmpty(orderId)){
            orderId = "8cd68b43925d4cb2b6138d48c7973252";
        }
        Order order = orderService.selectById(orderId);
        JSONObject data = new JSONObject();
        data.put("id",order.getId());
        data.put("verCode",order.getVerCode());
        data.put("shopId",order.getShopDetailId());
        data.put("operationType",type);
        if("call".equals(type)){
            JSONArray jsonArray = new JSONArray();
            Map<String, String> param = new HashMap<>();
            param.put("orderId", orderId);
            List<OrderItem> orderItems = orderItemService.listByOrderId(param);
            for(OrderItem item : orderItems){
                JSONObject object = new JSONObject();
                object.put("count",item.getCount());
                object.put("articleName",item.getArticleName());
                jsonArray.add(object);
            }
            data.put("callData",jsonArray);
        }
        System.out.println("\n\n发送的消息为：");
        System.out.println(data.toString());
        System.out.println("\n\n");
        SystemWebSocketHandler.getInstance().sendOrderToTvTest(shopId,data);
        return getSuccessResult();
    }

    /**
     * http://139.196.222.42:8580/pos/socket/conn
     * @param shopId
     * @return
     */
    @RequestMapping("conn")
    @ResponseBody
    public Result conn(String shopId){
        Map data = new HashMap();
        System.out.println(SystemWebSocketHandler.getInstance().hashCode());
        if(StringUtils.isNotEmpty(shopId)){
            data.put("tv",SystemWebSocketHandler.getInstance().getConnClients("tv",shopId));
        }
        data.put("tvAll",SystemWebSocketHandler.getInstance().getConnClients("tv",null));
        if(StringUtils.isNotEmpty(shopId)){
            data.put("pos",SystemWebSocketHandler.getInstance().getConnClients("pos",shopId));
        }
        data.put("posAll",SystemWebSocketHandler.getInstance().getConnClients("pos",null));
        return getSuccessResult(data);
    }

    @RequestMapping("command")
    @ResponseBody
    public Result command(String shopId,String operation){
        SystemWebSocketHandler.getInstance().commandToTv(shopId,operation,null);
        return getSuccessResult();
    }

    /**
     * http://139.196.222.42:8580/pos/socket/update?shopId=31164cebcc4b422685e8d9a32db12ab8&url=http://192.168.1.56/test.apk
     * http://139.196.222.42:8580/pos/socket/update?shopId=31164cebcc4b422685e8d9a32db12ab8&url=http://139.196.222.42:8580/WebSocketTv_0.12.apk
     * @param shopId
     * @param url
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public Result update(String shopId,String url){
        if(StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(shopId)){
            Map parameter = new HashMap();
            parameter.put("url",url);
            SystemWebSocketHandler.getInstance().commandToTv(shopId,"update",parameter);
            return getSuccessResult();
        }else{
            return new Result(false,"参数错误~~~");
        }
    }


    @RequestMapping("tvSyncData")
    @ResponseBody
    public Result tvSyncData(String brandId,String shopId){
        if(StringUtils.isNotEmpty(brandId) && StringUtils.isNotEmpty(shopId)){
            DataSourceTarget.setDataSourceName(brandId);
            String[] orderStates =  new String[]{"2"};//【2：已付款】
            String[] productionStates = new String[]{"2"};//【2：已打印】
            List<Order> orders = orderService.selectByOrderSatesAndProductionStates(shopId, orderStates, productionStates);
            return getSuccessResult(orders);
        }else{
            return new Result(false);
        }
    }



    @RequestMapping("checkUpdate")
    @ResponseBody
    public Result checkUpdate(String shopId){
        if(StringUtils.isNotEmpty(shopId)){
            ShopDetail shopDetail = shopDetailService.selectById(shopId);
            return getSuccessResult(shopDetail.getTvApkVersion()!=null?shopDetail.getTvApkVersion():null);
        }else{
            return new Result(false);
        }
    }
}
