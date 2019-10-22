package com.resto.service.appraise;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.resto.core.common.ApolloCfg;
import com.resto.core.common.RestoConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApolloConfig({ApolloCfg.CONFIG_APPLICATION,
		ApolloCfg.CONFIG_PUBLIC_MYBATIS,
		ApolloCfg.CONFIG_PUBLIC_DRUID,
		ApolloCfg.CONFIG_PUBLIC_APPLICATION,
		ApolloCfg.CONFIG_PUBLIC_SERVER,
		ApolloCfg.CONFIG_PUBLIC_DUBBO,
})
@SpringBootApplication(scanBasePackages = {RestoConstant.BASE_PACKAGE})
public class ApplicatioService {
	public static void main(String[] args) {
		SpringApplication.run(ApplicatioService.class, args);
	}
}
