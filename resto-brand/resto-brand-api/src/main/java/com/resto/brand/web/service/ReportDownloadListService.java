package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.ReportDownloadList;

import java.util.List;

public interface ReportDownloadListService extends GenericService<ReportDownloadList, Integer> {


    /**
     * 生成报表时需要生成一天清单记录
     * @param brandId
     * @param shopId
     * @param reportName 报表名称
     * @param path 文件地址
     * @return 返回的是当前记录的Id
     */
    Integer addReportDownloadList(String brandId, String shopId, String reportName, String path);

    /**
     * 根据品牌Id查询下载清单
     * @param brandId
     * @return
     */
    List<ReportDownloadList> selectListByBrandId(String brandId);

    /**
     * 根据品牌Id、店铺Id查询下载清单
     * @param brandId
     * @param shopId
     * @return
     */
    List<ReportDownloadList> selectListByBrandIdAndShopId(String brandId, String shopId);

    /**
     * 生产完成或者下载时调用该方法修改当前下载记录
     * @param id 当前下载记录的Id
     * @param isFinish 是否完成
     * @param isDownload 是否下载
     */
    void updateState(Integer id, boolean isFinish, boolean isDownload);
}
