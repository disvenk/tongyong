package com.resto.geekpos.web.utils;/**
 * Created by user on 2016/3/4.
 */

import com.resto.geekpos.web.constant.DictConstants;
import com.resto.geekpos.web.model.MobilePackageBean;
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
        try {

            Map params = new HashMap();

            String key = null;

            Object object =  request.getParameter("order");

            Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()) {
                key = names.nextElement();
                params.put(key, request.getParameter(key));
            }
            mobile.setContent(params);

        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return mobile;
    }

    /**
     * 设置返回信息
     *
     * @param response
     * @param object
     */
    public static void setBackInfo(HttpServletResponse response, Object object) {
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
    public static void setBackInfo(HttpServletResponse response, Object object, String sign) {
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
    public static void setBackInfo(HttpServletResponse response, Object object, int code, String msg) {
        MobilePackageBean mobileResponse = new MobilePackageBean();

        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("msg", msg);
        result.put("data", new JSONObject(object));

        mobileResponse.setContent(result);
        pack(mobileResponse, response);
    }

    /**
     * 设置返回错误信息
     *
     * @param response
     */
    public static void setBackInfo(HttpServletResponse response) {
        MobilePackageBean mobileResponse = new MobilePackageBean();

        JSONObject result = new JSONObject();
        result.put("code", "-1");
        result.put("msg", DictConstants.LOGIN_FAIL);

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
    public static void pack(MobilePackageBean packate, HttpServletResponse response) {
        try {
            response.setContentType("text/json;charset=utf-8");
            response.setCharacterEncoding(EN_CODING);
            response.setHeader("Cache-Control", "max-age=3600");
            response.setHeader("Expires", String.valueOf(System.currentTimeMillis() + 3600));
            //response.setBufferSize(BUFF_SIZE);
            StringBuilder str = new StringBuilder();
            int len = 0;

            len = packate.getContent().toString().length();
//            str.append(getLengthToStr(len));
            str.append(packate.getContent().toString());

            LOG.debug("mobilePack [" + packate.getApiMethod() + "]交易请求响应打包完成:" + str.toString());
            String src = str.toString().replaceAll("\r\n", "\\r\\n");
            src = src.replaceAll("\n", "\\n");
            response.setContentLength(src.getBytes(EN_CODING).length);
            response.getWriter().write(src);
            response.getWriter().flush();

        } catch (Exception e) {
            LOG.error("mobilePack 发送报文异常：", e);
        }
    }


    /**
     * 校验报文包
     *
     * @param mobile
     * @param response
     * @return
     */
    public static boolean validatePackage(MobilePackageBean mobile, HttpServletResponse response, Map local) {
        JSONObject result = new JSONObject();
        if (mobile == null) {
            result.put("code", "-1");
            result.put("msg", "参数为空");
            ApiUtils.setBackInfo(response, result); // 返回信息设置
            return false;
        }

        Map map = (Map) mobile.getContent();
        //获取前台传入的Sign
        String inSign = map.get("header[sign]").toString();
        String api = local.get("api").toString();
        String timestamp = map.get("header[timestamp]").toString();


        String token = "";
        String key = "";

        String userid = map.get("header[userId]").toString();

        key = local.get("keyValue").toString();


        token = local.get("token").toString();


        StringBuffer temp = new StringBuffer();
        temp.append("api=").append(api).append(DictConstants.AND);
        temp.append("timestamp=").append(timestamp).append(DictConstants.AND);
        temp.append("token=").append(token).append(DictConstants.AND);
        temp.append("key=").append(key).append(DictConstants.AND);
        temp.append("userId=").append(userid);


        String mySign = StringUtils.md5(temp.toString());
        if (!mySign.equals(inSign)) {
            result.put("code", "-2");
            result.put("msg", "签名不正确");
            ApiUtils.setBackInfo(response, result); // 返回信息设置
            return false;
        }
        return true;
    }


}
