package com.resto.service.shop;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.resto.api.brand.define.api.BrandApi;
import com.resto.core.ApolloCfg;
import com.resto.core.RestoConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ImportResource;

@EnableHystrix
@EnableDiscoveryClient
@EnableFeignClients
		(basePackageClasses = BrandApi.class)
@ImportResource("classpath:config/spring-mybatis.xml")
@EnableApolloConfig({ApolloCfg.CONFIG_APPLICATION,
		ApolloCfg.CONFIG_PUBLIC_SWAGGER,
		ApolloCfg.CONFIG_PUBLIC_APPLICATION,
		ApolloCfg.CONFIG_PUBLIC_HYSTRIX,
		ApolloCfg.CONFIG_PUBLIC_FEIGN,
		ApolloCfg.CONFIG_PUBLIC_RIBBON,
		ApolloCfg.CONFIG_PUBLIC_SERVER,
		ApolloCfg.CONFIG_PUBLIC_EUREKA,
		ApolloCfg.CONFIG_PUBLIC_REDIS
})
@SpringBootApplication(scanBasePackages = {RestoConstant.BASE_PACKAGE})
public class ApplicationShopService {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ApplicationShopService.class).web(true).run(args);
	}
}
