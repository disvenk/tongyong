package com.resto.brand.core.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KONATA on 2016/11/20.
 * 支付宝工具类
 */
public class AliPayUtils {


//    private static final String baseUrl = "https://openapi.alipaydev.com/gateway.do"; //沙箱环境
    private static final String baseUrl = "https://openapi.alipay.com/gateway.do"; //正式环境

    private static AlipayClient alipayClient;


    public static void connection(String appId, String privateKey, String publicKey, Integer aliEncrypt) {
        if (aliEncrypt == 0) {
            alipayClient = new DefaultAlipayClient(baseUrl, appId, privateKey, "json", "UTF-8", publicKey);
        } else if (aliEncrypt == 1) {
            alipayClient = new DefaultAlipayClient(baseUrl, appId, privateKey, "json", "UTF-8", publicKey, "RSA2");
        }
    }

    public static void phonePay(HttpServletResponse httpResponse, Map map, String returnUrl, String nofityUrl) throws Exception {
        JSONObject jsonObject = new JSONObject(map);
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        if(!StringUtils.isEmpty(returnUrl)){
            alipayRequest.setReturnUrl(returnUrl); //支付完成后回调地址
        }

        alipayRequest.setNotifyUrl(nofityUrl);//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent(jsonObject.toString());
        String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        httpResponse.setContentType("text/html;charset=UTF-8");
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
    }

    /**
     * 面对面支付->条码支付
     * @param json
     * @return
     * @throws AlipayApiException
     */
    public static Map<String, Object> tradePay(JSONObject json) throws AlipayApiException {
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
        request.setBizModel(model);

        model.setOutTradeNo(json.getString("out_trade_no"));
        model.setSubject(json.get("subject").toString());
        model.setTotalAmount(json.getString("total_amount"));
        model.setAuthCode(json.getString("auth_code"));//沙箱钱包中的付款码
        model.setScene(json.getString("scene"));

        AlipayTradePayResponse response = null;
        Map result = new HashMap();
        try {
            response = alipayClient.execute(request);
            if(response.isSuccess()){
                result.put("success", true);
                result.put("msg", response.getOutTradeNo());
            }else{
                result.put("success", false);
                result.put("msg", response.getSubMsg());
                result.put("sub_code", response.getSubCode());
            }
            return result;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "付款出错，请线下处理");
            return result;
        }
    }


    /**
     * 面对面支付->查询订单状态
     * @param jsonObject
     * @return
     */
    public static Map<String, Object> tradeQuery(JSONObject jsonObject)  {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        Map result = new HashMap();
        request.setBizContent(jsonObject.toString());
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            if(response.isSuccess()){
                result.put("success", true);
                result.put("msg", response.getBody());
                result.put("trade_status", response.getTradeStatus());
                result.put("trade_no", response.getTradeNo());
                result.put("total_amount", response.getTotalAmount());
                return result;
            }else{
                result.put("success", false);
                result.put("msg", response.getSubMsg());
                result.put("sub_code", response.getSubCode());
                return result;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "查询出错，请线下处理");
            return result;
        }

    }

    /**
     * 面对面支付撤销订单接口
     * @param jsonObject
     * @return
     */
    public static Map<String, Object> tradeCancel(JSONObject jsonObject){
        //返回的map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", false);
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        //封装订单信息
        request.setBizContent(jsonObject.toJSONString());
        AlipayTradeCancelResponse response = null;
        try{
            response = alipayClient.execute(request);
            if (response.isSuccess()){
                resultMap.put("success", true);
            }else{
                if (StringUtils.isNotBlank(response.getSubMsg())){
                    resultMap.put("msg", response.getSubMsg());
                } else{
                    resultMap.put("msg", "撤销失败，请线下处理");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("msg", "撤销出错，请线下处理");
        }
        return resultMap;
    }


    public static Map<String, String> refundPay(Map map){
        //返回退款信息
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("success", "false");
        JSONObject jsonObject = new JSONObject(map);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(jsonObject.toString());
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.getCode().equalsIgnoreCase("10000")) {
                resultMap.put("success", "true");
                resultMap.put("returnMsg", response.getBody());
            } else {
                resultMap.put("errorMsg", StringUtils.isNotBlank(response.getSubMsg()) ? response.getSubMsg() : getRefundErrorMsg(response.getSubCode()));
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            resultMap.put("errorMsg", "退款出错，请检查配置重新发起");
            return resultMap;
        }
        return resultMap;
    }

    private static String getRefundErrorMsg(String subCode){
        String subMsg = "未知错误";
        if (StringUtils.isBlank(subCode)){
            return "未返回错误码，未知错误";
        }
        switch (subCode){
            case "ACQ.SYSTEM_ERROR":
                subMsg = "系统错误，请使用相同的参数再次调用";
                break;
            case "ACQ.INVALID_PARAMETER":
                subMsg = "请求参数有错，重新检查请求后，再调用退款";
                break;
            case "ACQ.SELLER_BALANCE_NOT_ENOUGH":
                subMsg = "卖家余额不足";
                break;
            case "ACQ.REFUND_AMT_NOT_EQUAL_TOTAL":
                subMsg = "退款金额超限";
                break;
            case "ACQ.REASON_TRADE_BEEN_FREEZEN":
                subMsg = "请求退款的交易被冻结";
                break;
            case "ACQ.TRADE_NOT_EXIST":
                subMsg = "交易不存在";
                break;
            case "ACQ.TRADE_HAS_FINISHED":
                subMsg = "交易已完结";
                break;
            case "ACQ.TRADE_STATUS_ERROR":
                subMsg = "交易状态非法";
                break;
            case "ACQ.DISCORDANT_REPEAT_REQUEST":
                subMsg = "不一致的请求";
                break;
            case "ACQ.REASON_TRADE_REFUND_FEE_ERR":
                subMsg = "退款金额无效";
                break;
            case "ACQ.TRADE_NOT_ALLOW_REFUND":
                subMsg = "当前交易不允许退款";
                break;
            case "ACQ.REFUND_FEE_ERROR":
                subMsg = "交易退款金额有误";
                break;
        }
        return subMsg;
    }


    public static void main(String[] args) throws AlipayApiException {
        connection("2016102800773386",
                        "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANP6llbFMPEF+Kzn" +
                        "ZbYUF4Tw30zVIR15geFQWCCYMveflqCyCIYzDctEBhoeXj674Nofv3xyIxVZSTPe" +
                        "SpGOhjjZRDke1+M++8AUZlIIdDZfkOuoFSxeGYja5m2hWCvUvVg8YbnspRuwjda+" +
                        "fYeWsk6/aDukpULZqTgkii3f2reXAgMBAAECgYBUXlQfzPQhueKzzpVo1q5Vtxjp" +
                        "F5rKhGXxK20n6+u9KsNkyfcikodW84gKNTQFe/mOVzx7Z2IXSSYdgsfjDvrUQ72G" +
                        "UWBESSd+g91SmP0sLwTBk7kQDTzPyNXTKDWdK9ouC3Oho5i+2FLGrdBLizozQpEC" +
                        "ApNbPeSmiy7dGil+gQJBAPeb3dIH4kSWZWkjdBM5RPBARpZAx1PSyJtHlVR+z2TW" +
                        "1mPmT9lbecZlPt2YHTvxGClWZ9nwpxcYi3QasCVf93ECQQDbKZzA6WMl2z+gbXoQ" +
                        "PepJvHDaIt+KhegeRlujAyg1ugx89KvC3UHNF4ajd8FQsAh9c88fzY79LZVpNUTf" +
                        "g2uHAkEA7Pc+UsM4yGsmonhLnhow37yj0Sgtmwse8XyQbUzvLpJsmy7PPDVPVY+P" +
                        "moL5d2REu0r2GJ03S+Mxkuv3p80wAQJBAI8G/yfeqDgCd+moyKpk3cu1USjq7Vwn" +
                        "u65WWGNwIgO+IXxC6P1JDDJekh2If/66gy/sLlYg/po373QzsXj0+W0CQQCu11Ly" +
                        "56Wz2g/8oHFxHgXy0B1QpUyw8nLq5zoUnPXGx+w1pSA+dtXHo9E+cyFq4l1SWQ6B" +
                        "KsZHaJMNcA9svoJi",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB"
        , 0);
        Map map = new HashMap();
        map.put("out_trade_no","ac10efee57e04a30b3f2cbd0652ee5d3");
        map.put("trade_no","2016112121001004850200065496");
        map.put("refund_amount","0.01");
        refundPay(map);
    }
}
