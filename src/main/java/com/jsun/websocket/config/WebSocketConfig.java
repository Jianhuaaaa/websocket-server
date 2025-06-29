package com.jsun.websocket.config;

import com.jsun.websocket.ws.EchoWebSocketHandler;
import com.jsun.websocket.ws.EchoWebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoHandler(), "/ws/echo")
                .setAllowedOrigins("*")
                .addInterceptors(echoInterceptor());
        // Using SockJS if WebSocket not work
        registry.addHandler(echoHandler(), "sockjs/ws/echo")
                .setAllowedOrigins("*")
                .addInterceptors(echoInterceptor())
                .withSockJS();
    }

    /**
     * Configure messaage header
     *
     * @return
     */
    @Bean
    public WebSocketHandler echoHandler() {
        return new EchoWebSocketHandler();
    }

    /**
     * WebSocket Interceptor
     *
     * @return
     */
    @Bean
    public EchoWebSocketHandshakeInterceptor echoInterceptor() {
        return new EchoWebSocketHandshakeInterceptor();
    }

}
