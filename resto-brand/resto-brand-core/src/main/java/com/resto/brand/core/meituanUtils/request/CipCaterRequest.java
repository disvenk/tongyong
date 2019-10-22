package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestMethod;
import com.resto.brand.core.meituanUtils.domain.RequestSysParams;
import com.resto.brand.core.meituanUtils.utils.ReportUtils;
import com.resto.brand.core.meituanUtils.utils.SignUtils;
import com.resto.brand.core.meituanUtils.utils.ValidUtils;
import com.resto.brand.core.meituanUtils.utils.WebUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/18.
 */
public abstract class CipCaterRequest {
    protected RequestMethod requestMethod;
    protected String url;
    protected RequestSysParams requestSysParams;
    protected String timestamp = new Timestamp(new Date().getTime()).toString();

    /**
     * 获取公共参数
     */
    public Map<String, String> getSharedParams() {
        Map<String, String> sysParams = new HashMap<String, String>();
        sysParams.put("appAuthToken", requestSysParams.getAppAuthToken());
        sysParams.put("timestamp", timestamp);
        sysParams.put("charset", requestSysParams.getCharset());
        return sysParams;
    }

    /**
     * 检查是否缺少公共参数
     */
    public boolean sharedParamsAbsent() {
        if (requestSysParams == null) {
            return true;
        }
        boolean appAuthTokenAbsent = requestSysParams.getAppAuthToken() == null
                || requestSysParams.getAppAuthToken().trim().isEmpty();
        boolean charsetAbsent = requestSysParams.getCharset() == null
                || requestSysParams.getCharset().trim().isEmpty();
        boolean secretAbsent = requestSysParams.getSecret() == null
                || requestSysParams.getSecret().trim().isEmpty();
        return appAuthTokenAbsent || charsetAbsent || secretAbsent;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public RequestSysParams getRequestSysParams() {
        return requestSysParams;
    }

    public void setRequestSysParams(RequestSysParams requestSysParams) {
        this.requestSysParams = requestSysParams;
    }

    public String getTimestamp() {
        return timestamp;
    }

    /**
     * 根据公共参数和每个request独有的参数计算sign值
     */
    public String getSign() {
        Map<String, String> params = this.getSharedParams();
        params.putAll(this.getParams());
        return SignUtils.createSign(requestSysParams.getSecret(), params);
    }

    /**
     * 根据request中指定的请求方式发送http请求
     *
     * @return String json字符串, 错误信息或者请求结果, 示例: "{"data":{}}", "{"data":"errorMsg"}"
     * @throws IOException
     * @throws URISyntaxException
     * @throws RuntimeException   如果缺少必要参数,会抛出该异常
     */
    public String doRequest() throws IOException, URISyntaxException {
        String errorMsg = this.valid();
        if (errorMsg != null && !"".equals(errorMsg)) {
            throw new RuntimeException("参数校验错误: " + errorMsg);
        }
        // 每天一次统计上报
        ReportUtils.report(this);
        // 发送请求
        switch (this.getRequestMethod()) {
            case GET:
                return WebUtils.doGet(this);
            case POST:
                return WebUtils.doPost(this);
            case PUT:
                return "";
            case DELETE:
                return "";
            default:
                return "";
        }
    }

    /**
     * 参数格式验证
     */
    public String valid() {
        return ValidUtils.valid(this);
    }

    /**
     * 获取request独有参数
     */
    public abstract Map<String, String> getParams();

    /**
     * 检查是否缺少request独有参数
     *
     * @deprecated
     */
    public boolean paramsAbsent() {
        return false;
    }
}
