package com.resto.brand.web.dao;

import com.resto.brand.web.model.ReportDownloadList;
import com.resto.brand.core.generic.GenericDao;

public interface ReportDownloadListMapper  extends GenericDao<ReportDownloadList,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(ReportDownloadList record);

    int insertSelective(ReportDownloadList record);

    ReportDownloadList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ReportDownloadList record);

    int updateByPrimaryKey(ReportDownloadList record);
}
