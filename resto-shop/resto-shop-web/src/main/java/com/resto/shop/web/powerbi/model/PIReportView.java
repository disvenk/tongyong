package com.resto.shop.web.powerbi.model;

/**
 * title
 * company resto+
 * author jimmy 2018/8/23 下午5:03
 * version 1.0
 */
public class PIReportView {
    private String reportId;
    private String embedUrl;
    private String token;
    private String groupId;

    public PIReportView(String reportId, String embedUrl, String token, String groupId) {
        this.reportId = reportId;
        this.embedUrl = embedUrl;
        this.token = token;
        this.groupId = groupId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
