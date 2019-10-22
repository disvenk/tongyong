package com.resto.geekpos.web.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ImportResource;

@Configurable
@ImportResource("classpath:applicationContext*.xml")
public class SpringContextConfig {

}
