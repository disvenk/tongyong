package com.resto.brand.core.meituanUtils.utils;

import com.resto.brand.core.meituanUtils.domain.Constants;
import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.request.CipCaterRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/18.
 */
public final class ReportUtils {
    private static final String reportUrl = RequestDomain.preUrl.getValue() + "/report";
    private static final String key = "postToday";
    private static final Map<String, Long> map = new HashMap<String, Long>();

    /**
     * 每天上报一次sdk版本号
     */
    public static void report(CipCaterRequest cipCaterRequest) throws IOException, URISyntaxException {
        boolean sentToday = map.containsKey(key) && (map.get(key) - System.currentTimeMillis()) / 1000 / 60 / 60 < 24;
        if (sentToday) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("sdkVersion", Constants.SDK_VERSION);
        params.put("appAuthToken", cipCaterRequest.getRequestSysParams().getAppAuthToken());
        params.put("timestamp", new Timestamp(new Date().getTime()).toString());
        WebUtils.post(reportUrl, params);
        map.put(key, System.currentTimeMillis());
    }
}
