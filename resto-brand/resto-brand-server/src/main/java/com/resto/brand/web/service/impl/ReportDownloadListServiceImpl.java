package com.resto.brand.web.service.impl;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ReportDownloadListMapper;
import com.resto.brand.web.model.ReportDownloadList;
import com.resto.brand.web.service.ReportDownloadListService;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class ReportDownloadListServiceImpl extends GenericServiceImpl<ReportDownloadList, Integer> implements ReportDownloadListService {

    @Autowired
    private ReportDownloadListMapper reportdownloadlistMapper;

    @Override
    public GenericDao<ReportDownloadList, Integer> getDao() {
        return reportdownloadlistMapper;
    }


    @Override
    public Integer addReportDownloadList(String brandId, String shopId, String reportName, String path) {
        try {
            ReportDownloadList reportDownloadList = new ReportDownloadList();
            reportDownloadList.setBrandId(brandId);
            reportDownloadList.setShopId(shopId);
            reportDownloadList.setReportName(reportName);
            reportDownloadList.setPath(path);
            reportdownloadlistMapper.insertSelective(reportDownloadList);
            return reportDownloadList.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ReportDownloadList> selectListByBrandIdAndShopId(String brandId, String shopId) {
        List<ReportDownloadList> downloadListList = reportdownloadlistMapper.selectList();
        List<ReportDownloadList> result =  downloadListList.stream().filter(list -> list.getBrandId().equalsIgnoreCase(brandId)
            && list.getShopId().equalsIgnoreCase(shopId)).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<ReportDownloadList> selectListByBrandId(String brandId) {
        List<ReportDownloadList> downloadListList = reportdownloadlistMapper.selectList();
        List<ReportDownloadList> result =  downloadListList.stream().filter(list -> list.getBrandId().equalsIgnoreCase(brandId))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public void updateState(Integer id, boolean isFinish, boolean isDownload) {
        ReportDownloadList downloadList = reportdownloadlistMapper.selectByPrimaryKey(id);
        if (isFinish) {
            downloadList.setState(Byte.valueOf("2"));
            downloadList.setFinishTime(new Date());
        }
        if (isDownload) {
            downloadList.setState(Byte.valueOf("3"));
            downloadList.setDownloadTime(new Date());
        }
        reportdownloadlistMapper.updateByPrimaryKeySelective(downloadList);
    }
}
