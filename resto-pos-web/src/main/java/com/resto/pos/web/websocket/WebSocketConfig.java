package com.resto.pos.web.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	//注册  websocket 服务
        registry.addHandler(systemWebSocketHandler(),"/orderServer").addInterceptors(new WebSocketHandshakeInterceptor());
        //注册 sockjs 支持的 websocket
        registry.addHandler(systemWebSocketHandler(), "/sockjs/orderServer").addInterceptors(new WebSocketHandshakeInterceptor())
                .withSockJS();
    }
 
    @Bean
    public WebSocketHandler systemWebSocketHandler(){
        return SystemWebSocketHandler.getInstance();
    }
 
}
