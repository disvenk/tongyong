package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.dao.WeBrandMapper;
import com.resto.brand.web.model.WeBrand;
import com.resto.brand.web.service.WeBrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class WeBrandServiceImpl extends GenericServiceImpl<WeBrand, Integer> implements WeBrandService {

    @Resource
    private WeBrandMapper webrandMapper;

    @Override
    public GenericDao<WeBrand, Integer> getDao() {
        return webrandMapper;
    }

    @Override
    public List<WeBrand> selectWeBrandList(String createTime) {
        Date date;
        if(StringUtils.isEmpty(createTime)|| DateUtil.formatDate(new Date(),"yyyy-MM-dd").equals(createTime)){
            //默认获取昨天数据
            date = DateUtil.fomatDate(DateUtil.getAfterDayDateStr("-1")) ;
        }else {
            date = DateUtil.fomatDate(createTime);
        }
        return webrandMapper.selectWeBrandList(date);
    }

}
