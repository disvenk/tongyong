package com.resto.wechat.web.util;
/**
 * Created by user on 2016/3/4.
 */

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Servlets
 *
 * @author ken zhao
 * @date 2016/3/4
 */
public class Servlets {

    private static final Logger logger = Logger.getLogger(Servlets.class);
    /**
     * 获取访问者IP
     * <p/>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p/>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    @SuppressWarnings("unchecked")
    public static String getPostData(HttpServletRequest request) {
        String content;
        try {
            InputStream ins = request.getInputStream();
            List<String> lines = IOUtils.readLines(ins);
            if (CollectionUtils.isEmpty(lines)) {
                return null;
            }
            content = StringUtils.join(lines);
        } catch (IOException e) {
            content = "";
            logger.error(e.getMessage());
        }
        return content;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map getParamMap(HttpServletRequest request) {
        Map query = new HashMap();
        Enumeration<String> names = request.getParameterNames();
        String key;
        while (names.hasMoreElements()) {
            key = names.nextElement();
            query.put(key, request.getParameter(key));
        }
        return query;
    }


    /**
     * 获取request对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 获取Response对象
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = null;
        if (requestAttributes instanceof ServletWebRequest) {
            response = ((ServletWebRequest) requestAttributes).getResponse();
        }

        return response;
    }
}
