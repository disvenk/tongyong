package com.resto.brand.web.model;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/5/27.
 */
public class PosConfig implements Serializable {
    private Long id;

    private String clientIp;

    private String serverIp;

    private String posWebUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getPosWebUrl() {
        return posWebUrl;
    }

    public void setPosWebUrl(String posWebUrl) {
        this.posWebUrl = posWebUrl;
    }
}
