package com.resto.brand.web.model;

import lombok.Data;

import java.util.Date;

@Data
public class ReportDownloadList {
    private Integer id;

    private String brandId;

    private String shopId;

    private String reportName;

    private Byte state;

    private String path;

    private Date createTime;

    private Date finishTime;

    private Date downloadTime;
}