package com.resto.brand.web.model;

import java.io.Serializable;

public class VersionPos implements Serializable {
    private Integer id;

    private String versionNo;

    private String downloadAddress;

    private Integer versionId;

    private Integer substituteMode;

    private Integer voluntarily;

    private String message;

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo == null ? null : versionNo.trim();
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress == null ? null : downloadAddress.trim();
    }

    public Integer getSubstituteMode() {
        return substituteMode;
    }

    public void setSubstituteMode(Integer substituteMode) {
        this.substituteMode = substituteMode;
    }

    public Integer getVoluntarily() {
        return voluntarily;
    }

    public void setVoluntarily(Integer voluntarily) {
        this.voluntarily = voluntarily;
    }
}