package wjt.websocket;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
@EnableWebSocket
public class MyWebSocketConfigurer implements WebSocketConfigurer {

    private static final Map<String, WebSocketSession> ID_MAP_SESSION = new ConcurrentHashMap<>();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebSocketHandlerRegistration webSocketHandlerRegistration = registry.addHandler(new ChatMsgHandler(ID_MAP_SESSION), "/chat.ws")
                .setHandshakeHandler(handshakeHandler());
        log.info("webSocketHandlerRegistration={};", webSocketHandlerRegistration);
    }


    @Bean
    public DefaultHandshakeHandler handshakeHandler(){
        WebSocketPolicy webSocketPolicy=new WebSocketPolicy(WebSocketBehavior.SERVER);
        webSocketPolicy.setInputBufferSize(8192);
        webSocketPolicy.setIdleTimeout(100000);
        DefaultHandshakeHandler handshakeHandler = new DefaultHandshakeHandler(new JettyRequestUpgradeStrategy(new WebSocketServerFactory(webSocketPolicy)));
        log.info("handshakeHandler={};",handshakeHandler);
        return handshakeHandler;
    }


}
