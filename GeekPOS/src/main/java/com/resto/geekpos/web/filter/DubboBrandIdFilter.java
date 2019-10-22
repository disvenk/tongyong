package com.resto.geekpos.web.filter;

import com.alibaba.dubbo.rpc.*;
import com.resto.geekpos.web.rpcinterceptors.DataSourceTarget;
import org.springframework.stereotype.Component;

@Component
public class DubboBrandIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String interfaceName = invoker.getInterface().getName();
        if(interfaceName.matches("^com.resto.shop.web.service.*")||interfaceName.matches("^com.resto.api.*.service.*")){
            String brandId = DataSourceTarget.getDataSourceName();
            RpcContext.getContext().setAttachment("brandId", brandId);
        }
        return invoker.invoke(invocation);
    }

}
