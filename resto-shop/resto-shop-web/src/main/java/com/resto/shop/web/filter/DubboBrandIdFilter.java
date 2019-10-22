package com.resto.shop.web.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.resto.shop.web.config.SessionKey;
import com.resto.shop.web.rpcinterceptors.DataSourceTarget;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER}, before = "DBFilter")
public class DubboBrandIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String brandId = (String) httpRequest.getSession().getAttribute(SessionKey.CURRENT_BRAND_ID);
        if(brandId == null){
            brandId = DataSourceTarget.getDataSourceName();
        }
        RpcContext.getContext().setAttachment("brandId", brandId);
        Result result = invoker.invoke(invocation);
        return result;
    }

}
