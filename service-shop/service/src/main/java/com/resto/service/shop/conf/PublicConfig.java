package com.resto.service.shop.conf;

import com.resto.api.shop.util.ShopConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by jimmy on 2017/2/24.
 */
@Configuration
public class PublicConfig extends WebMvcConfigurerAdapter {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DBJwtFilterApi()).addPathPatterns("/"+ ShopConstant.API_NAME+"/**");
        super.addInterceptors(registry);
    }
}
