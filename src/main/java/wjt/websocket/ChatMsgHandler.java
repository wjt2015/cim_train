package wjt.websocket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class ChatMsgHandler implements WebSocketHandler {

    private Map<String, WebSocketSession> idMapSession;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        idMapSession.put(session.getId(), session);
        log.info("idMapSession.size={};sessionId={};", idMapSession.size(), session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("recv msg!msg={};", message);
        idMapSession.values().forEach(webSocketSession -> {
            if (!webSocketSession.equals(session) && webSocketSession.isOpen()) {
                /**
                 * 为其他用户转发;
                 */
                try {
                    webSocketSession.sendMessage(message);
                } catch (IOException e) {
                    log.error("sendMsg error!sessionId={};", webSocketSession.getId());
                }
            }
        });
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        idMapSession.remove(session.getId());
        log.info("sessionId={} error!", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        idMapSession.remove(session.getId());
        log.info("sessionId={} close!", session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
