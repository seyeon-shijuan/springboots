package kr.goodee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import kr.goodee.websocket.EchoHandler;

@Configuration
@EnableWebSocket   //websocket 관련 설정
public class WebSocketConfig implements WebSocketConfigurer {
	@Override
	public void registerWebSocketHandlers
	                          (WebSocketHandlerRegistry registry) {
		registry.addHandler(new EchoHandler(), "chatting.shop")
		.setAllowedOrigins("*");
	}	
}
