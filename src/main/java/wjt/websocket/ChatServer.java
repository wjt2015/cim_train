package wjt.websocket;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参考:
 * 使用Java和WebSocket实现网页聊天室实例代码,
 * (https://www.jb51.net/article/87659.htm);
 * <p>
 * java 实现websocket的两种方式实例详解,
 * (https://www.jb51.net/article/144601.htm);
 */
@Slf4j
@ServerEndpoint(value = "/chat")
public class ChatServer {
    public ChatServer() {
        log.info("ChatServer!this={};", this);
    }

    @OnOpen
    public void onOpen(final Session session) {
        Set<Session> openSessions = session.getOpenSessions();
        Set<String> sessionIds = openSessions.stream().map(session1 -> session1.getId()).collect(Collectors.toSet());
        log.info("openSessions.size={};sessionIds={};", openSessions.size(), sessionIds);
        String sessionId = session.getId();
        long maxIdleTimeout = session.getMaxIdleTimeout();
        int maxTextMessageBufferSize = session.getMaxTextMessageBufferSize();
        log.info("webSocket on open!session={};sessionId={};maxIdleTimeout={};maxTextMessageBufferSize={};", session, sessionId, maxIdleTimeout, maxTextMessageBufferSize);
    }

    @OnClose
    public void onClose(final Session session) {
        log.info("webSocket close!sessionId={};", session.getId());
    }

    @OnError
    public void error(final Session session, final Throwable throwable) {
        log.info("webSocket error!sessionId={};throwable={};", session.getId(), throwable);
    }

    @OnMessage
    public void onMessage(final Session session, final String msg) {
        log.info("sessionId={};msg={};", session.getId(), msg);
    }

}

