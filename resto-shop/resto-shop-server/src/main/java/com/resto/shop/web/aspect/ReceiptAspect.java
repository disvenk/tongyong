package com.resto.shop.web.aspect;

import com.resto.shop.web.model.Receipt;
import com.resto.shop.web.producer.MQMessageProducer;
import com.resto.shop.web.producer.MQTTMQMessageProducer;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* 发票管理功能切面
* @author xielc
* @date 2017年9月11日
*/
@Component
@Aspect
public class ReceiptAspect {

    @Autowired
    MQMessageProducer mqMessageProducer;

    @Autowired
    MQTTMQMessageProducer mqttMQMessageProducer;


    Logger log = LoggerFactory.getLogger(getClass());


    @Pointcut("execution(* com.resto.shop.web.service.ReceiptService.insertSelective(..))")
    public void createReceipt() {
    }

    @AfterReturning(value = "createReceipt()", returning = "receipt")
    public void createReceipt(Receipt receipt) throws Throwable {
        log.info("进入发票自动出单切面---"+"店铺id:"+receipt.getShopId());
        if (receipt!=null) {
            mqMessageProducer.sendReceiptPrintSuccess(receipt.getShopId(),receipt.getOrderNumber());
            pushReceiptToPos(receipt.getShopId(),receipt.getOrderNumber());
        }
    }

    private void pushReceiptToPos(String shopId, String orderNumber){
        JSONObject json = new JSONObject();
        json.put("orderNumber",orderNumber);
        json.put("dataType","receipt");
        json.put("shopId", shopId);
        mqttMQMessageProducer.sendReceiptPrintToPosMessage(json.toString());
    }
}
