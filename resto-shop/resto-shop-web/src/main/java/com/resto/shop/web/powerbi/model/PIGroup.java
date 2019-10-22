package com.resto.shop.web.powerbi.model;

import java.util.List;

public class PIGroup {

    private String id;
    private String name;
    private boolean isReadOnly;
    private boolean isOnDedicatedCapacity;

    //additional fields
    private List<PIReport> reports;

    public PIGroup() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public boolean isOnDedicatedCapacity() {
        return isOnDedicatedCapacity;
    }

    public void setOnDedicatedCapacity(boolean onDedicatedCapacity) {
        isOnDedicatedCapacity = onDedicatedCapacity;
    }

    public List<PIReport> getReports() {
        return reports;
    }

    public void setReports(List<PIReport> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return "PIGroup{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isReadOnly=" + isReadOnly +
                ", isOnDedicatedCapacity=" + isOnDedicatedCapacity +
                '}';
    }
}
