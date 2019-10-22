package com.resto.wechat.web.util;
/**
 * Created by user on 2016/3/4.
 */

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ApiUtils
 *
 * @author ken zhao
 * @date 2016/3/4
 */
public class AppUtils {


    private static final Logger LOG = Logger.getLogger(AppUtils.class);

    private final static String EN_CODING = "UTF-8";

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

        String[] stringArray;
        if (object instanceof List) {
            List list = (List) object;
            stringArray = (String[]) list.toArray(new String[list.size()]);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = object.toString();
            try {
                stringArray = objectMapper.readValue(json, String[].class);
            } catch (Exception e) {
                stringArray = new String[0];
                LOG.error(e.getMessage());
            }
        }

        return stringArray;
    }

    /**
     * 对请求解包
     *
     * @param request
     * @return
     */
    public static MobilePackageBean unpack(HttpServletRequest request, HttpServletResponse response) {
        //设置跨域访问
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

        MobilePackageBean mobile = new MobilePackageBean();
        Map params = new HashMap();

        String key ;

        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            key = names.nextElement();
            params.put(key, request.getParameter(key));
        }
        mobile.setContent(params);
        return mobile;
    }

    /**
     * 设置返回信息
     *
     * @param response
     * @param object
     */
    public static void setBackInfo(HttpServletResponse response, Object object) throws IOException {
        MobilePackageBean mobileResponse = new MobilePackageBean();

        JSONObject result = new JSONObject();
        result.put("code", "200");
        result.put("msg", "请求成功");
        result.put("data", new JSONObject(object));

        mobileResponse.setContent(result);
        pack(mobileResponse, response);
    }


    /**
     * 设置返回信息
     *
     * @param response
     * @param object
     */
    public static void setBackInfo(HttpServletResponse response, Object object, String sign) throws IOException {
        MobilePackageBean mobileResponse = new MobilePackageBean();

        JSONObject result = new JSONObject();
        result.put("code", "200");
        result.put("msg", "请求成功");
        result.put("sign", sign);

        result.put("data", new JSONObject(object));

        mobileResponse.setContent(result);
        pack(mobileResponse, response);
    }


    /**
     * 设置返回信息
     *
     * @param response
     * @param object
     */
    public static void setBackInfo(HttpServletResponse response, Object object, int code, String msg) throws IOException {
        MobilePackageBean mobileResponse = new MobilePackageBean();

        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("msg", msg);
        result.put("data", new JSONObject(object));

        mobileResponse.setContent(result);
        pack(mobileResponse, response);
    }



    /**
     * 打包并发送
     *
     * @param packate  包内容
     * @param response 响应用户请求
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public static void pack(MobilePackageBean packate, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setCharacterEncoding(EN_CODING);
        response.setHeader("Cache-Control", "max-age=3600");
        response.setHeader("Expires", String.valueOf(System.currentTimeMillis() + 3600));
        StringBuilder str = new StringBuilder();

        str.append(packate.getContent().toString());

        String src = str.toString().replaceAll("\r\n", "\\r\\n");
        src = src.replaceAll("\n", "\\n");
        response.setContentLength(src.getBytes(EN_CODING).length);
        response.getWriter().write(src);
        response.getWriter().flush();

    }




}
