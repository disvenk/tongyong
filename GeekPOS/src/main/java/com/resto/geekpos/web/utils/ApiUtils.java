package com.resto.geekpos.web.utils;/**
 * Created by user on 2016/3/4.
 */

import com.resto.geekpos.web.model.MobilePackageBean;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * ApiUtils
 *
 * @author ken zhao
 * @date 2016/3/4
 */
public class ApiUtils {


    private static final Logger LOG = Logger.getLogger(ApiUtils.class);

    /**
     * 字符串数组转long数组
     *
     * @param ids
     * @return
     */
    public static Long[] stringToLongArray(String[] ids) {
        if (!(ids != null && ids.length > 0)) {
            return null;
        }

        Long[] longArray = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {
            longArray[i] = Long.valueOf(ids[i]);
        }
        return longArray;
    }

    /**
     * 将参数转化为数组
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String[] getStringArray(Map params, String key) {
        Object object = params.get(key);
        if (object == null) {
            return null;
        }

        String[] stringArray = null;
        if (object instanceof List) {
            List list = (List) object;
            stringArray = (String[]) list.toArray(new String[list.size()]);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = object.toString();
            try {
                stringArray = objectMapper.readValue(json, String[].class);
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }

        return stringArray;
    }

    /**
     * 校验报文包
     *
     * @param mobile
     * @param response
     * @return
     */
    public static boolean validatePackage(MobilePackageBean mobile, HttpServletResponse response) {
        if (mobile == null) {
            JSONObject returnObject = ApiUtils.returnObjectError("传入参数为空");
            ApiUtils.setBackInfo(response, returnObject); // 返回信息设置
            return false;
        }

        return true;
    }

    /**
     * 对请求解包
     *
     * @param request
     * @return
     */
    public static MobilePackageBean unpack(HttpServletRequest request) {
        MobilePackageBean mobile = null;
        try {
//			mobile = MobileCommunicatePackateUtil.getInstance().unpack(request); // 获取移动终端报文对象
            mobile = new MobileCommunicatePackateUtil().unpack(request);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return mobile;
    }

    /**
     * 设置返回信息
     *
     * @param response
     * @param returnObject
     */
    public static void setBackInfo(HttpServletResponse response, JSONObject returnObject) {
        MobilePackageBean mobileResponse = new MobilePackageBean();
        MobileCommunicatePackateUtil mobileCommunicatePackateUtil = MobileCommunicatePackateUtil.getInstance();
        mobileResponse.setContent(returnObject);
        mobileCommunicatePackateUtil.pack(mobileResponse, response);
    }

    public static void setBackInfo(HttpServletResponse response, net.sf.json.JSONObject returnObject) {
        MobilePackageBean mobileResponse = new MobilePackageBean();
        MobileCommunicatePackateUtil mobileCommunicatePackateUtil = MobileCommunicatePackateUtil.getInstance();
        mobileResponse.setContent(returnObject);
        mobileCommunicatePackateUtil.pack(mobileResponse, response);
    }

    /**
     * 返回错误信息
     *
     * @param msg
     * @return
     */
    public static JSONObject returnObjectError(String msg) {
        return returnObject("500", msg);
    }

    /**
     * 返回正确信息
     *
     * @param message
     * @return
     */
    public static JSONObject returnObjectSuccess(String message) {
        return returnObject("200", message);
    }


    /**
     * 返回正确信息
     *
     * @param message
     * @return
     */
    public static JSONObject returnObjectSuccess(String message, Object data) {
        return returnObject("200", message, data);
    }

    /**
     * 返回正确信息
     *
     * @return
     */
    public static JSONObject returnObjectSuccess() {
        return returnObjectSuccess(null);
    }

    /**
     * 返回信息
     *
     * @param code
     * @return
     */
    public static JSONObject returnObject(String code) {
        return returnObject(code, null);
    }

    /**
     * 返回信息
     *
     * @param code
     * @param msg
     * @return
     */
    public static JSONObject returnObject(String code, String msg) {
        JSONObject returnObject = new JSONObject();
        returnObject.append("returnCode", code);
        returnObject.append("returnMsg", msg);
        return returnObject;
    }

    /**
     * 返回信息
     *
     * @param code
     * @param msg
     * @return
     */
    public static JSONObject returnObject(String code, String msg, Object data) {
        JSONObject returnObject = new JSONObject();
        returnObject.append("returnCode", code);
        returnObject.append("returnMsg", msg);
        returnObject.append("data", data);
        return returnObject;
    }

    /**
     * 成功消息
     *
     * @param returnMessage
     */
    public static void success(String returnMessage) {
        success(returnMessage, null);
    }

    /**
     * 成功消息
     *
     * @param msg
     * @param data
     * @param data
     */
    public static void success(String msg, Object data) {
        JSONObject returnObject = new JSONObject();
        returnObject.append("returnCode", 200);
        returnObject.append("returnMsg", msg);
        returnObject.append("data", data);
        HttpServletResponse response = Servlets.getResponse();
        setBackInfo(response, returnObject);
    }

    /**
     * 错误消息
     *
     * @param message
     * @param message
     */
    public static void error(String message) {
        error("500", message);
    }

    /**
     * 错误消息
     *
     * @param returnCode
     * @param returnMessage
     */
    public static void error(String returnCode, String returnMessage) {
        JSONObject returnObject = new JSONObject();
        returnObject.append("returnCode", returnCode);
        returnObject.append("returnMsg", returnMessage);
        HttpServletResponse response = Servlets.getResponse();
        setBackInfo(response, returnObject);
    }
}
