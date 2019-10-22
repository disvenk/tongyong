package com.resto.service.customer.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.resto.conf.mybatis.datasource.DataSourceContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER}, before = "DBFilter")
public class DBFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String brandId = RpcContext.getContext().getAttachment("brandId");
        if(StringUtils.isNotBlank(brandId)){
            DataSourceContextHolder.setDataSourceName(brandId);
        }
        return invoker.invoke(invocation);
    }

}
