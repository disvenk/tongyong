package com.server;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.resto.core.ApolloCfg;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
@EnableApolloConfig({ApolloCfg.CONFIG_APPLICATION,
		ApolloCfg.CONFIG_PUBLIC_APPLICATION,})
public class EurekaApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(EurekaApplication.class).web(true).run(args);
	}

}
