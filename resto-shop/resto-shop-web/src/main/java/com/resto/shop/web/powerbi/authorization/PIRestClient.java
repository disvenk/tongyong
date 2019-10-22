package com.resto.shop.web.powerbi.authorization;

import com.alibaba.fastjson.JSON;
import com.resto.shop.web.powerbi.model.PIGroup;
import com.resto.shop.web.powerbi.model.PIReport;
import com.resto.shop.web.powerbi.model.PIReportView;
import com.resto.shop.web.util.DateUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PIRestClient {

    private PIAuthorizationContext authorizationContext = null;
    private HttpClient httpclient = HttpClients.createDefault();
    private HttpGet httpGet;
    private List<PIGroup> groups;
    private Date refreshDate;
    private Map<String, PIReportView> reportViewMap = new HashMap<>();

    public PIRestClient() {
        authorizationContext = new PIAuthorizationContext();
        initGroups("shop_reports");
        initReportsForAllGroups();
        initViewReportAllGroups();
    }

    private void initGroups(String groupName) {
        groups = getAllGroups(authorizationContext.getAccessToken());
        groups = groups.stream().filter(a -> a.getName().equals(groupName)).collect(Collectors.toList());
    }

    private void initReportsForAllGroups() {
        groups.forEach(g -> g.setReports(getAllReportsOfGroup(authorizationContext.getAccessToken(), g.getId())));
    }

    private List<PIGroup> getAllGroups(String token) {
        httpGet = new HttpGet(PIUrlTemplate.GROUPS);
        return processGetResponseHelper(token, PIGroup.class);
    }

    private void initViewReportAllGroups() {
        for (PIGroup group : groups) {
            String groupId = group.getId();
            List<PIReport> reports = group.getReports();
            for (PIReport report : reports) {
                String id = report.getId();
                String embedUrl = report.getEmbedUrl();
                String token = getReportViewToken(groupId, id);
                PIReportView reportView = new PIReportView(id, embedUrl, token, groupId);
                reportViewMap.put(report.getName(), reportView);
            }
            refreshDate = new Date();
        }
    }

    private void initViewReportTokenAllReports() {
            for (Map.Entry<String, PIReportView> entry : reportViewMap.entrySet()) {
                String reportName = entry.getKey();
                PIReportView reportView = entry.getValue();
                String token = getReportViewToken(reportView.getGroupId(), reportView.getReportId());
                reportView.setToken(token);
                reportViewMap.put(reportName, reportView);
            }
        refreshDate = new Date();
    }

    private String getReportViewToken(String group, String reportId) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = String.format(PIUrlTemplate.GROUP_REPORTS_VIEW_TOKEN, group, reportId);
        params.add(new BasicNameValuePair(PIUrlTemplate.VIEW_TOKEN, "view"));
        return getViewTokenHandler(url, this.authorizationContext.getAccessToken(), params);
    }

    private List<PIReport> getAllReportsOfGroup(String token, String group) {
        httpGet = new HttpGet(String.format(PIUrlTemplate.GROUP_REPORTS, group));
        return processGetResponseHelper(token, PIReport.class);
    }

    private <T> List<T> processGetResponseHelper(String token, Class<T> tClass) {
        try {
            httpGet.setHeader("Authorization", "Bearer " + token);

            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity);

            Map map = JSON.parseObject(res, Map.class);
            String value = map.get("value").toString();
            if (value != null)
                return JSON.parseArray(value, tClass);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    private String getViewTokenHandler(String url, String token, List<NameValuePair> params) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Authorization", "Bearer " + token);
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            StringBuilder sb = new StringBuilder();

            BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            Map map = JSON.parseObject(sb.toString(), Map.class);
            return map.get("token").toString();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PIAuthorizationContext.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PIAuthorizationContext.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PIAuthorizationContext.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    //getters and setters


    public List<PIGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<PIGroup> groups) {
        this.groups = groups;
    }

    public Map<String, PIReportView> getReportViewMap() {
        //50分钟刷新token
        if(DateUtil.getDiffMinute(refreshDate, new Date()) >=50){
            System.out.println("刷新下token");
            initViewReportTokenAllReports();
        }
        return reportViewMap;
    }

    public void setReportViewMap(Map<String, PIReportView> reportViewMap) {
        this.reportViewMap = reportViewMap;
    }
}
