package com.resto.brand.web.conf;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * title
 * company resto+
 * author jimmy 2018/3/14 下午3:00
 * version 1.0
 */
@Configuration
@DubboComponentScan("com.resto.brand.web.service.impl") // 扫描 Dubbo 组件
public class DubboProviderConfiguration {

    @Value("zookeeper://${rpc.registry_address}")
    private String zkAddress;

    @Value("${dubbo.brand.port}")
    private int protocolPort;

    @Value("${dubbo.provider.timeOut}")
    private int providerTimeOut;

    @Value("${dubbo.provider.retries}")
    private int providerRetries;
    /**
     * 当前应用配置
     */
    @Bean(name = "provider_brand")
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("provider_brand");
        return applicationConfig;
    }

    /**
     * 当前连接注册中心配置
     */
    @Bean(name = "my-registry")
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(zkAddress);
        return registryConfig;
    }

    /**
     * 当前连接注册中心配置
     */
    @Bean(name = "dubboProtolConf")
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(protocolPort);
        return protocolConfig;
    }

    /**
     * 当前连接注册中心配置
     */
    @Bean(name = "dubboProvider")
    public ProviderConfig providerConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(providerTimeOut);
        providerConfig.setRetries(providerRetries);
        return providerConfig;
    }
}
