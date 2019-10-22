package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.List;

public class VersionPackage implements Serializable{
    private Integer id;

    private String packageName;

    private String downloadAddress;

    private List<VersionBrandPackage> versionBrandPackage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress;
    }

    public List<VersionBrandPackage> getVersionBrandPackage() {
        return versionBrandPackage;
    }

    public void setVersionBrandPackage(List<VersionBrandPackage> versionBrandPackage) {
        this.versionBrandPackage = versionBrandPackage;
    }
}