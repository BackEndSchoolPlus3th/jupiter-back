package com.jupiter.wyl.global.websocket;

import com.jupiter.wyl.global.app.AppConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 메세지를 받을 엔드포인트
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트 요청 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")  // 클라이언트가 연결할 엔드포인트
                .setAllowedOrigins("http://localhost:5173", AppConfig.getSiteFrontUrl())  // 클라이언트 URL을 정확히 허용
                .withSockJS();
    }
}