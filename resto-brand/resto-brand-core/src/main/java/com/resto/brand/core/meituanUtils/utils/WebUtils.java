package com.resto.brand.core.meituanUtils.utils;

import com.resto.brand.core.meituanUtils.request.CipCaterRequest;
import com.resto.brand.core.meituanUtils.request.CipCaterStreamRequest;
import com.resto.brand.core.meituanUtils.request.CipCaterStringPairRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP工具类
 * <p>
 * Created by cuibaosen on 16/8/3.
 */
public final class WebUtils {
    private static final Log log = LogFactory.getLog(WebUtils.class);

    /**
     * 发送GET请求
     *
     * @param cipCaterRequest 请求要素,url、secret、appAuthToken、可选的charset
     * @return String json字符串, 错误信息或者请求结果, 示例: "{"data":{}}", "{"error":{}}"
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String doGet(CipCaterRequest cipCaterRequest) throws IOException, URISyntaxException {
        return doGet((CipCaterStringPairRequest) cipCaterRequest);
    }

    /**
     * 发送普通POST请求
     *
     * @param cipCaterRequest 请求要素,url、secret、appAuthToken、可选的charset
     * @return String json字符串, 错误信息或者请求结果, 示例: "{"data":{}}", "{"error":{}}"
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String doPost(CipCaterRequest cipCaterRequest) throws IOException, URISyntaxException {
        if (cipCaterRequest instanceof CipCaterStringPairRequest) {
            return doPost((CipCaterStringPairRequest) cipCaterRequest);
        }
        else {
            return doPost((CipCaterStreamRequest) cipCaterRequest);
        }
    }

    /**
     * 发送普通GET请求
     *
     * @param request 请求要素,url、secret、appAuthToken、可选的charset
     * @return String json字符串, 错误信息或者请求结果, 示例: "{"data":{}}", "{"error":{}}"
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String doGet(CipCaterStringPairRequest request) throws IOException, URISyntaxException {
        // 拼接url
        List<NameValuePair> paramsInUrl = new ArrayList<NameValuePair>();
        paramsInUrl.add(new BasicNameValuePair("appAuthToken", request.getRequestSysParams().getAppAuthToken()));
        paramsInUrl.add(new BasicNameValuePair("charset", request.getRequestSysParams().getCharset()));
        paramsInUrl.add(new BasicNameValuePair("timestamp", request.getTimestamp()));
        paramsInUrl.add(new BasicNameValuePair("sign", request.getSign()));
        for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
            paramsInUrl.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        String url = new URIBuilder().setParameters(paramsInUrl).setPath(request.getUrl()).build().toString();

        return WebUtils.get(url);
    }

    /**
     * 发起GET请求
     *
     * @param url 请求url
     * @return json字符串, 错误信息或者请求结果, 示例: "{"data":{}}", "{"error":{}}"
     * @throws IOException
     * @throws URISyntaxException
     */
    private static String get(String url) throws URISyntaxException, IOException {
        // 校验url是否合法
        URI target = new URI(url);
        if (!target.isAbsolute() || URIUtils.extractHost(target) == null) {
//            return "{\"error\":{\"code\":404,\"message\":\"指定url不合法\"}}";
            return errorModel();
        }

        // 创建post请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);

        return WebUtils.getResult(httpClient, url, get);
    }

    /**
     * 发送普通POST请求
     *
     * @param request 请求要素,url、secret、appAuthToken、可选的charset
     * @return String json字符串, 错误信息或者请求结果, 示例: "{"data":{}}", "{"error":{}}"
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String doPost(CipCaterStringPairRequest request) throws IOException, URISyntaxException {
        // 拼接url
        List<NameValuePair> paramsInUrl = new ArrayList<NameValuePair>();
        paramsInUrl.add(new BasicNameValuePair("appAuthToken", request.getRequestSysParams().getAppAuthToken()));
        paramsInUrl.add(new BasicNameValuePair("charset", request.getRequestSysParams().getCharset()));
        paramsInUrl.add(new BasicNameValuePair("timestamp", request.getTimestamp()));
        paramsInUrl.add(new BasicNameValuePair("sign", request.getSign()));
        String url = new URIBuilder().setParameters(paramsInUrl).setPath(request.getUrl()).build().toString();

        return WebUtils.post(url, request.getParams());
    }

    public static String errorModel(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","404");
        jsonObject.put("message","指定url不合法");
        Map map = new HashMap(1);
        map.put("error",jsonObject);
        return new JSONObject(map).toString();
    }



    /**
     * 发起普通POST请求
     *
     * @param url    请求url
     * @param params 请求参数
     * @return json字符串, 错误信息或者请求结果, 示例: "{"data":{}}", "{"error":{}}"
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String post(String url, Map<String, String> params) throws IOException, URISyntaxException {
        // 校验url是否合法
        URI target = new URI(url);
        if (!target.isAbsolute() || URIUtils.extractHost(target) == null) {
//            return "{\"error\":{\"code\":404,\"message\":\"指定url不合法\"}}";
            return errorModel();
        }

        // 创建post请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        // 添加请求体参数
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf8"));
        }

        return WebUtils.getResult(httpClient, url, post);
    }

    /**
     * 发送流形式的POST请求
     *
     * @param request 请求要素,url、secret、appAuthToken、可选的charset
     * @return String json字符串, 错误信息或者请求结果, 示例: "{"data":{}}", "{"error":{}}"
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String doPost(CipCaterStreamRequest request) throws IOException, URISyntaxException {
        // 拼接url
        List<NameValuePair> paramsInUrl = new ArrayList<NameValuePair>();
        paramsInUrl.add(new BasicNameValuePair("appAuthToken", request.getRequestSysParams().getAppAuthToken()));
        paramsInUrl.add(new BasicNameValuePair("charset", request.getRequestSysParams().getCharset()));
        paramsInUrl.add(new BasicNameValuePair("timestamp", request.getTimestamp()));
        paramsInUrl.add(new BasicNameValuePair("sign", request.getSign()));
        String url = new URIBuilder().setParameters(paramsInUrl).setPath(request.getUrl()).build().toString();

        return WebUtils.post(url, request.getFiles(), request.getParams());
    }

    /**
     * 发起流形式POST请求
     *
     * @param url    请求url
     * @param params 请求参数
     * @return json字符串, 错误信息或者请求结果, 示例: "{"data":{}}", "{"error":{}}"
     * @throws IOException
     * @throws URISyntaxException
     */
    private static String post(String url, Map<String, File> files,
                               Map<String, String> params) throws IOException, URISyntaxException {
        // 校验url是否合法
        URI target = new URI(url);
        if (!target.isAbsolute() || URIUtils.extractHost(target) == null) {
//            return "{\"error\":{\"code\":404,\"message\":\"指定url不合法\"}}";
            return errorModel();
        }

        // 创建post请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        MultipartEntity multipartEntity = new MultipartEntity();
        // 添加File请求体参数
        if (files != null) {
            for (Map.Entry<String, File> file : files.entrySet()) {
                multipartEntity.addPart(file.getKey(), new FileBody(file.getValue()));
            }
        }
        // 添加String请求体参数
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                multipartEntity.addPart(entry.getKey(), new StringBody(entry.getValue()));
            }
        }
        post.setEntity(multipartEntity);

        return WebUtils.getResult(httpClient, url, post);
    }

    /**
     * 发起请求,返回响应的json串
     *
     * @param httpClient HttpClient
     * @param url        请求的url
     * @param method     请求方式,包括GET和POST
     * @return json字符串, 错误信息或者请求结果, 示例: "{"data":{}}", "{"error":{}}"
     * @throws IOException
     */
    private static String getResult(CloseableHttpClient httpClient, String url,
                                    HttpRequestBase method) throws IOException {
        CloseableHttpResponse response = null;
        String result = null;

        try {
            // 接收response
            response = httpClient.execute(method);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                return "{\"error\":{\"code\":" + statusCode + ",\"message\":\"请求失败\"}}";
            }

            // 获取返回体
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(response.getEntity());

            // 释放资源
            EntityUtils.consume(entity);
        } catch (IOException ioe) {
            throw new IOException("IOException in request. Request Url:" + url, ioe);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("Response close failed. Request Url:" + url, e);
                }
            }
        }

        return result;
    }
}
