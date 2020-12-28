package wjt.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
@EnableWebSocket
public class MyWebSocketConfigurer implements WebSocketConfigurer {

    private static final Map<String, WebSocketSession> ID_MAP_SESSION = new ConcurrentHashMap<>();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebSocketHandlerRegistration webSocketHandlerRegistration = registry.addHandler(new ChatMsgHandler(ID_MAP_SESSION), "/chat");
        log.info("webSocketHandlerRegistration={};", webSocketHandlerRegistration);
    }


}
