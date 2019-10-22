package com.resto.service.brand;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.resto.core.common.ApolloCfg;
import com.resto.core.common.RestoConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableHystrix
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.resto.service.*.mapper")
@EnableApolloConfig({ApolloCfg.CONFIG_APPLICATION,
		ApolloCfg.CONFIG_PUBLIC_MYBATIS,
		ApolloCfg.CONFIG_PUBLIC_DRUID,
		ApolloCfg.CONFIG_PUBLIC_SWAGGER,
		ApolloCfg.CONFIG_PUBLIC_APPLICATION,
		ApolloCfg.CONFIG_PUBLIC_HYSTRIX,
		ApolloCfg.CONFIG_PUBLIC_FEIGN,
		ApolloCfg.CONFIG_PUBLIC_RIBBON,
		ApolloCfg.CONFIG_PUBLIC_SERVER,
		ApolloCfg.CONFIG_PUBLIC_EUREKA,
		ApolloCfg.CONFIG_PUBLIC_REDIS,

})
@SpringBootApplication(scanBasePackages = {RestoConstant.BASE_PACKAGE})
public class ApplicationBrandService {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ApplicationBrandService.class).web(true).run(args);
	}
}
